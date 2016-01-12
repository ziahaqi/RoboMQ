package net.ziahaqi.robomq;

import android.os.AsyncTask;

import com.rabbitmq.client.ShutdownListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by ziahaqi on 1/11/2016.
 */
public class MQProducer extends MQConnector{


    public MQProducer(String host, String virtualHost, String username, String password, int port) {
        super(host, virtualHost, username, password, port);
    }

    @Override
    protected void createChannel() {

    }

    @Override
    protected ShutdownListener createShutDownListener() {
        return null;
    }
}
