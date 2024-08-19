package com.example.hidroflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Inicio extends Fragment {

    private MqttAndroidClient cliente; // Cliente MQTT
    private MqttConnectOptions opciones;

    // Configuración del servidor MQTT
    static String MQTTHOST = "tcp://sensores-isc93.cloud.shiftr.io:1883";
    static String MQTTUSER = "sensores-isc93";
    static String MQTTPASS = "sKofheYUVbYrsgza";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        connectBroker(); // Conectar al broker en Inicio

        return view;
    }

    private void connectBroker() {
        cliente = new MqttAndroidClient(requireActivity().getApplicationContext(), MQTTHOST, "ClientID");
        opciones = new MqttConnectOptions();
        opciones.setUserName(MQTTUSER);
        opciones.setPassword(MQTTPASS.toCharArray());

        try {
            IMqttToken token = cliente.connect(opciones);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Conexión exitosa
                    suscribirseTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Manejar el error
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void suscribirseTopic() {
        try {
            cliente.subscribe("sensors/distance", 0);

            cliente.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Manejar pérdida de conexión
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String distancia = new String(message.getPayload());
                    // Aquí manejar el mensaje recibido para 'sensors/distance'
                    TextView tv_agua = getView().findViewById(R.id.tv_agua);
                    tv_agua.setText(distancia);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Manejar cuando se complete la entrega de un mensaje
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el cliente MQTT desde MainActivity
    public MqttAndroidClient getCliente() {
        return cliente;
    }
}
