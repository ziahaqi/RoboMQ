package net.ziahaqi.robomq;

/**
 * Created by ziahaqi on 1/11/2016.
 */
public class MQFactory {
    private static final String TAG = "MQFactory";

    private String excahnge;
    private String routingKey;
    private String hostName;
    private String virtualHostName;
    private String username;
    private String password;
    private int port;

    public MQFactory(String hostName, String virtualHostName, String username, String password, String excahnge, String routingKey, int port) {
        this.excahnge = excahnge;
        this.routingKey = routingKey;
        this.hostName = hostName;
        this.virtualHostName = virtualHostName;
        this.username = username;
        this.password = password;
        this.port = port;
    }



    public MQConsumer createConsumer(MQCallback callback){
        MQConsumer consumer = MQConsumer.createInstance(this, callback);
        return consumer;
    }

    public MQProducer createProducer(MQCallback callback){
        MQProducer producer = MQProducer.createInstance(this, callback);
        return producer;
    }

    public String getExcahnge() {
        return excahnge;
    }

    public void setExcahnge(String excahnge) {
        this.excahnge = excahnge;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public void setVirtualHostName(String virtualHostName) {
        this.virtualHostName = virtualHostName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
