# RoboMQ
rabbitMQ client library for android

initiation '\n'
- create robomq factory '\n'
        this.mqFactory = new MQFactory(RabbitCOnfig.hostName,
                RabbitCOnfig.virtualHostname,
                RabbitCOnfig.username,
                RabbitCOnfig.password,
                RabbitCOnfig.exchange,
                RabbitCOnfig.rotuingkey,
                RabbitCOnfig.port);
                
- create consumer '\n'
this.mqConsumer = mqFactory.createConsumer(this);

- subscribe message from server '\n'
 this.mqConsumer.subsribe();
