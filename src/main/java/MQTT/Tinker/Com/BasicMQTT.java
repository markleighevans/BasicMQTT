package MQTT.Tinker.Com;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark on 3/25/16.
 */
public class BasicMQTT implements MqttCallback {
    MqttClient client;
    MqttConnectOptions connOpts;
    MemoryPersistence persistence = new MemoryPersistence();
    String BrokerID     = "tcp://iot.eclipse.org:1883";
    String clientId     = "JavaSample";

    public BasicMQTT()
    {
        //Constructor
    }

    public static void main(String[] args) {
        new BasicMQTT().RunClient();
    }

    public void RunClient()
    {
    try {
        client = new MqttClient(BrokerID, clientId, persistence);
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
        client.setCallback(this);
        client.subscribe("/SA107SY/Lounge/Temperature");
        MqttMessage message = new MqttMessage();
        int Counter = 0;
        do {
            message.setPayload(new SimpleDateFormat("dd-MM-yyyy-hhmmss").format(new Date())
                    .getBytes());
            client.publish("/SA107SY/Lounge/Temperature", message);
            Counter++;
        }
        while ( Counter < 10);
    } catch (MqttException e) {
        e.printStackTrace();
    }
}

    @Override
    public void connectionLost(Throwable cause)
    {
        System.out.println("Connection to  broker lost!" + cause.getMessage());

    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println("\nMessage received!" +
                "\n\tTopic:   " + topic +
                "\n\tMessage: " + new String(message.getPayload()) +
                "\n\tQoS:     " + message.getQos() + "\n");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}