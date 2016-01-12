# RoboMQ
rabbitMQ client library for android

initiation 
- create robomq factory
        this.mqFactory = new MQFactory(RabbitCOnfig.hostName,
                RabbitCOnfig.virtualHostname,
                RabbitCOnfig.username,
                RabbitCOnfig.password,
                RabbitCOnfig.exchange,
                RabbitCOnfig.rotuingkey,
                RabbitCOnfig.port);
                
- create consumer 
this.mqConsumer = mqFactory.createConsumer(this);

- subscribe message from server
 this.mqConsumer.subsribe();
