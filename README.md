# RoboMQ
rabbitMQ client library for android

+ how to use : <br/>
- create robomq factory <br/>
        this.mqFactory = new MQFactory(RabbitCOnfig.hostName,
                RabbitCOnfig.virtualHostname,
                RabbitCOnfig.username,
                RabbitCOnfig.password,
                RabbitCOnfig.exchange,
                RabbitCOnfig.rotuingkey,
                RabbitCOnfig.port);
                
- create consumer __
this.mqConsumer = mqFactory.createConsumer(this);

- subscribe message from server __
 this.mqConsumer.subsribe();
