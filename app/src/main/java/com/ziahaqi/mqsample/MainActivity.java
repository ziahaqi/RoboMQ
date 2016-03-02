package com.ziahaqi.mqsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rabbitmq.client.QueueingConsumer;

import net.ziahaqi.robomq.MQCallback;
import net.ziahaqi.robomq.MQConsumer;
import net.ziahaqi.robomq.MQFactory;
import net.ziahaqi.robomq.MQProducer;

public class MainActivity extends AppCompatActivity implements MQCallback{

    private MQFactory mqFactory;
    private MQConsumer mqConsumer;

    private boolean consumerModeStart = true;
    private MQProducer mqProducer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mqFactory = new MQFactory(MQConfig.hostName,
                MQConfig.virtualHostname,
                MQConfig.username,
                MQConfig.password,
                MQConfig.exchange,
                MQConfig.rotuingkey,
                MQConfig.port);

        this.mqConsumer = this.mqFactory.createConsumer(this);
        this.mqProducer = this.mqFactory.createProducer(this);

        this.mqConsumer.setMessageListner(new MQConsumer.MQConsumerListener() {
            @Override
            public void onMessageReceived(QueueingConsumer.Delivery delivery) {
                Toast.makeText(MainActivity.this, "receive message", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_consumer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(consumerModeStart){
                    mqConsumer.subscribe();


                    consumerModeStart = false;
                    ((Button)findViewById(R.id.btn_consumer)).setText("stop consumer");
                    Toast.makeText(MainActivity.this, "start consumer", Toast.LENGTH_SHORT).show();

                }
                else{
                    consumerModeStart = true;
                    mqConsumer.stop();
                    ((Button)findViewById(R.id.btn_consumer)).setText("start consumer");
                    Toast.makeText(MainActivity.this, "stop consumer", Toast.LENGTH_SHORT).show();

                }
            }
        });

        findViewById(R.id.btn_producer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ((EditText) findViewById(R.id.edit_producer)).getText().toString();
                mqProducer.publish(message, null);
                Toast.makeText(MainActivity.this, "publish", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMQConnectionFailure(String message) {
        Toast.makeText(this, "failure : "+ message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMQDisconnected() {
        Toast.makeText(this, "disconnected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMQConnectionClosed(String message) {
        Toast.makeText(this, "closed : "+ message, Toast.LENGTH_SHORT).show();

    }

}
