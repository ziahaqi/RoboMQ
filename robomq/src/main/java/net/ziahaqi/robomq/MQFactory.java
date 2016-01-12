package net.ziahaqi.robomq;

/**
 * Created by ziahaqi on 1/11/2016.
 */
public class MQFactory {
    private static final String TAG = "MQFactory";

    public String excahnge;
    public String routingKey;
    String hostName;
    String virtualHostName;
    String username;
    String password;
    int port;

    public MQFactory(String hostName, String virtualHostName, String username, String password, String excahnge, String routingKey, int port) {
        this.excahnge = excahnge;
        this.routingKey = routingKey;
        this.hostName = hostName;
        this.virtualHostName = virtualHostName;
        this.username = username;
        this.password = password;
        this.port = port;
    }



    public MQConsumer createConsumer(MQConsumer.MQConsumerCallback callback){
        MQConsumer consumer = new MQConsumer(this.hostName, this.virtualHostName, this.username, password, this.port,
                this.routingKey, this.excahnge,
                callback);
        return consumer;
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