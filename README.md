# RoboMQ
RoboMQ is RabbitMQ client wrapper-library for android base on [java library](http://www.rabbitmq.com/java-client.html)

## Usage
- initiation  <br/>
```java
        this.mqFactory = new MQFactory(MQConfig.hostName,
                MQConfig.virtualHostname,
                MQConfig.username,
                MQConfig.password,
                MQConfig.exchange,
                MQConfig.rotuingkey,
                MQConfig.port);

        this.mqConsumer = this.mqFactory.createConsumer(this);
        this.mqProducer = this.mqFactory.createProducer(this);
                
```
- set property <br/>
```java
                    mqConsumer.setQueueName("newQueueName");
                    mqConsumer.setRoutingkey("newRoutingKey");
                    mqConsumer.setExchange("newExchange");
```
- start and stop consumer <br/> 
```java
        mqConsumer.subsribe();
        mqConsumer.stop();

```

## suggestion
for long running operation, preferably using a service
