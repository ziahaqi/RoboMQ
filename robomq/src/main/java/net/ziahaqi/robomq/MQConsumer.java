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
    private MQConsumerCallback mCallback;
    private Thread subscribeThread;
    private QueueingConsumer mQueue;
    private String mQueueName;
    private String mExchange;
    private String mRoutingKey;

    private Handler mCallbackHandler = new Handler();
    private boolean queuing = true;
    private boolean subscribeRunning = true;
    public interface MQConsumerCallback {

        void onMQConnectionFailure(String message);

        void onMQDisconnected();

        void onMQConnectionClosed(String message);

        void onMQMessegeReceived(QueueingConsumer.Delivery delivery);

    }

    public MQConsumer(String host, String virtualHost, String username, String password, int port, String routingKey, String excahnge, MQConsumerCallback callback) {
        super(host, virtualHost, username, password, port);
        this.mRoutingKey = routingKey;
        this.mExchange = excahnge;
        this.mCallback = callback;
        this.mQueueName = createDefaultQueueName();
    }

    public String createDefaultQueueName(){
        return   "123@" + UUID.randomUUID();
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


    public void subsribe(){
        Log.d(TAG, "subscribe");
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "subscribe>run");

                while(subscribeRunning) {
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
                                    mCallback.onMQMessegeReceived(delivery);
                                }
                            });
                        }
                    } catch (InterruptedException | ConsumerCancelledException
                            | ShutdownSignalException | IOException | TimeoutException e) {
                        sendBackErrorMessage(e);
                        try {
                            Thread.sleep(4000);
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
    @Override
    protected void createChannel() {

        try {
            mChannel = this.mConnection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
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