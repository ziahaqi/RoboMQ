package com.ziahaqi.mqsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rabbitmq.client.QueueingConsumer;

import net.ziahaqi.robomq.MQConsumer;
import net.ziahaqi.robomq.MQFactory;
import net.ziahaqi.robomq.MQProducer;

public class MainActivity extends AppCompatActivity implements MQConsumer.MQConsumerCallback{


    private MQFactory mqFactory;
    private MQConsumer mqConsumer;

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
        this.mqConsumer = mqFactory.createConsumer(this);
        this.mqConsumer.subsribe();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    @Override
    public void onMQMessegeReceived(QueueingConsumer.Delivery delivery) {
        Toast.makeText(this, "delivery", Toast.LENGTH_SHORT).show();
    }
}
