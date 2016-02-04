# RoboMQ
RoboMQ is RabbitMQ client wrapper-library for android base on [java library](http://www.rabbitmq.com/java-client.html)

## Usage
- initiation  <br/>
```java
        //activity or service implement MQCallback  
        MQFactory mqFactory = new MQFactory(MQConfig.hostName,
                MQConfig.virtualHostname,
                MQConfig.username,
                MQConfig.password,
                MQConfig.exchange,
                MQConfig.rotuingkey,
                MQConfig.port);
        
        // callback injected by factory instances for create consumer and produser instance
        MQConsumer mqConsumer = this.mqFactory.createConsumer(this);
        MQProducer mqProducer = this.mqFactory.createProducer(this);
                
```
- set property <br/>
```java
                    mqConsumer.setQueueName("newQueueName");
                    mqConsumer.setRoutingkey("newRoutingKey");
                    mqConsumer.setExchange("newExchange");
```
- start and stop consumer <br/> 
```java
        mqConsumer.setMessageListner(new MQConsumer.MQConsumerListener() {
            @Override
            public void onMessageReceived(QueueingConsumer.Delivery delivery) {
             //use delivery message
            }
        });
        
        mqConsumer.subsribe();
        mqConsumer.stop();

```

- start and stop produser <br/> 
```java
        mqProducer.publish(message, null);

```
## suggestion
for long running operation, preferably using a service
