package com.example.hidroflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Temperatura extends Fragment {

    Button btnliberar;

    private MqttAndroidClient cliente; // Cliente MQTT recibido

    static String TOPIC = "sensors/abrir";
    static String TOPIC_MSG_ON = "1";
    static String TOPIC_MSG_OFF = "0";

    Boolean isOn = false; // Variable para mantener el estado

    // Método setter para pasar la instancia del cliente MQTT desde MainActivity
    public void setCliente(MqttAndroidClient cliente) {
        this.cliente = cliente;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperatura, container, false);

        btnliberar = view.findViewById(R.id.btnliberar);

        if (cliente != null) {
            // Solo suscribirse al tema, ya no conectar de nuevo
            suscribirseTopic();
        }

        // Obtener la fecha actual
        String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date());

        // Capitalizar el primer carácter de cada palabra relevante
        String capitalizedDate = capitalizeFirstLetters(currentDate);

        // Obtener el TextView y establecer el texto
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        textViewDate.setText(capitalizedDate);

        btnliberar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alternar estado
                if (isOn) {
                    enviarMensaje(TOPIC, TOPIC_MSG_OFF);
                } else {
                    enviarMensaje(TOPIC, TOPIC_MSG_ON);
                }
                isOn = !isOn; // Cambiar el estado después de enviar el mensaje
            }
        });

        return view;
    }

    private String capitalizeFirstLetters(String text) {
        String[] words = text.split(" ");
        StringBuilder capitalizedText = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedText.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalizedText.toString().trim();
    }

    private void suscribirseTopic() {
        try {
            cliente.subscribe("sensors/temperature", 0);

            cliente.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Manejar pérdida de conexión
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String temperatura = new String(message.getPayload());
                    // Aquí manejar el mensaje recibido para 'sensors/temperature'
                    TextView tv_temp = getView().findViewById(R.id.tv_temp);
                    tv_temp.setText(temperatura + " °C");
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

    private void enviarMensaje(String topic, String msg) {
        try {
            int qos = 0;
            this.cliente.publish(topic, msg.getBytes(), qos, false);
            Toast.makeText(getView().getContext(), topic + " : " + msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

