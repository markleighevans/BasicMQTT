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
    //String BrokerID     = "tcp://iot.eclipse.org:1883";
    String BrokerID     =   "tcp://xi7mod.messaging.internetofthings.ibmcloud.com:1883";
    String M2MIO_DOMAIN = "iot-2/evt/" + "Temperature"  + "/fmt/json";
    String M2MIO_STUFF = "things";

    String clientId = "d:xi7mod:MQTTDevice:thisisthedeviceid"; // d:<org-id>:<type-id>:<device-id>
    String M2MIO_USERNAME = "use-token-auth";
    String M2MIO_PASSWORD_MD5 = "9KuuaLWin!fFiSe(kC";
    //String Topic        = "/SA107SY/Lounge/Temperature";
    //String Topic          = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + clientId;
    String Topic          = M2MIO_DOMAIN ;

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

        //Set connection Options//
        connOpts = new MqttConnectOptions();
        connOpts.setUserName(M2MIO_USERNAME);
        connOpts.setPassword(M2MIO_PASSWORD_MD5.toCharArray());
        connOpts.setCleanSession(true);
        ///////////////////////////////
        client.setCallback(this);

        client.connect(connOpts);

        //client.subscribe(Topic);
        MqttMessage message = new MqttMessage();
        int Counter = 0;
        do {
            //message.setPayload(new SimpleDateFormat("dd-MM-yyyy-hhmmss").format(new Date()).getBytes());
            String MessageText = "{\"pubmsg\":" + Counter + "}";
            message.setPayload(MessageText.getBytes());
            client.publish(Topic, message);
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