package com.example.hidroflow;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hidroflow.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Inicio inicioFragment = new Inicio();
    private Historial historialFragment = new Historial();
    private Temperatura temperaturaFragment = new Temperatura();
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set initial fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, inicioFragment).commit();
        bottomNavigationView = binding.menu;

        bottomNavigationView.setSelectedItemId(R.id.btninicio);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.btninicio) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, inicioFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.btnhistorial) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, historialFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.btntemperatura) {
                // Pasar la instancia del cliente MQTT desde Inicio a Temperatura
                if (inicioFragment.getCliente() != null) {
                    temperaturaFragment.setCliente(inicioFragment.getCliente());
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, temperaturaFragment).commit();
                return true;
            } else {
                return false;
            }
        });
    }
}
