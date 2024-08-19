package com.example.hidroflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Historial extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        // Obtener la fecha actual
        String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date());

        // Capitalizar el primer carácter de cada palabra relevante
        currentDate = capitalizeFirstLetters(currentDate);
        // Obtener el TextView y establecer el texto
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        textViewDate.setText(currentDate);

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
}
