package net.ziahaqi.robomq;

import android.os.Handler;
import android.util.Log;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by ziahaqi on 1/11/2016.
 */
public class MQConsumer extends MQConnector{
    private static final String TAG = "MQConsumer";
    private MQCallback mCallback;
    private Thread subscribeThread;
    private QueueingConsumer mQueue;
    private String mQueueName;
    private String mExchange;
    private String mRoutingKey;

    private Handler mCallbackHandler = new Handler();
    private MQConsumerListener mqConsumerListener;
    private boolean queuing = true;
    public interface MQConsumerListener{
        public void onMessageReceived(QueueingConsumer.Delivery delivery);
    }

    public static MQConsumer createInstance(MQFactory factory, MQCallback callback){
        return new MQConsumer(factory.getHostName(),
                factory.getVirtualHostName(),
                factory.getUsername(),
                factory.getPassword(),
                factory.getPort(),
                factory.getRoutingKey(),
                factory.getExcahnge(),
                callback
                );
    }

    private MQConsumer(String host, String virtualHost, String username, String password, int port, String routingKey, String excahnge, MQCallback callback) {
        super(host, virtualHost, username, password, port);
        this.mRoutingKey = routingKey;
        this.mExchange = excahnge;
        this.mCallback = callback;
        this.mQueueName = createDefaultQueueName();
    }

    public void setMessageListner(MQConsumerListener listner){
        mqConsumerListener = listner;
    };

    public String createDefaultQueueName(){
        return    mRoutingKey +"@" + UUID.randomUUID();
    }


    @Override
    protected ShutdownListener createShutDownListener() {
        ShutdownListener listener = new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                String errorMessage = cause.getMessage() == null ? "cunsumer connection was shutdown" : "consumer " + cause.getMessage();
                mCallback.onMQConnectionClosed(errorMessage);
            }
        };
        return listener;
    }

    public void stop(){
        try {
            closeMQConnection();
        } catch (IOException | TimeoutException e) {
            sendBackErrorMessage(e);
            e.printStackTrace();
        }
    }

    public void subsribe(){
        Log.d(TAG, "subscribe");
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "subscribe>run");

                while(isRunning) {
                    try {
                        Log.d(TAG, "subscribe>run>subscribeRunning");

                        initConnection();
                        initchanenel();
                        declareQueue();
                        mChannel.queueBind(mQueueName, mExchange, mRoutingKey);
                        mQueue = new QueueingConsumer(mChannel);
                        mChannel.basicConsume(mQueueName, mQueue);
                        while(queuing){
                            Log.d(TAG, "subscribe>run>subscribeRunning>queuing");

                            final QueueingConsumer.Delivery delivery;
                                delivery = mQueue.nextDelivery();
                                mChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                                mCallbackHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mqConsumerListener.onMessageReceived(delivery);
                                }
                            });
                        }
                    } catch (InterruptedException | ConsumerCancelledException
                            | ShutdownSignalException | IOException | TimeoutException e) {
                        sendBackErrorMessage(e);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }

    private void sendBackErrorMessage(Exception e) {
        final String errorMessage = e.getMessage() == null ? e.toString() : e.getMessage();
        mCallbackHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMQConnectionFailure(errorMessage);
            }
        });
    }



    private void initConnection() throws IOException, TimeoutException {
        if(!isConnected()){
            createConnection();
        }
    }


    private void initchanenel(){
        if(!isChannelAvailable()){
            createChannel();
        }
    }


    private void declareQueue() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x-expires", 2 * 60 * 60 * 1000);
        mChannel.queueDeclare(mQueueName, true, false, false, params);
        Log.d(TAG, "Queue :" + "queue:name:" + mQueueName + " declared");
    }

    public String getExchange() {
        return mExchange;
    }

    public void setExchange(String mExchange) {
        this.mExchange = mExchange;
    }

    public String getRoutingkey() {
        return mRoutingKey;
    }

    public void setRoutingkey(String mRoutingKey) {
        this.mRoutingKey = mRoutingKey;
    }

    public String getQueueName() {
        return mQueueName;
    }

    public void setQueueName(String mQueueName) {
        this.mQueueName = mQueueName;
    }
}
