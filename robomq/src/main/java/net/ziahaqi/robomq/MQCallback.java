package net.ziahaqi.robomq;

/**
 * Created by ziahaqi on 2/4/2016.
 */
public interface MQCallback {
    void onMQConnectionFailure(String message);

    void onMQDisconnected();

    void onMQConnectionClosed(String message);
}
