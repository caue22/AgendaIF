package com.example.agendaif;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.agendaif.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TelaOpcao extends AppCompatActivity {

    ActivityMainBinding binding;
    private CardView cardView;
    private Fragment currentFragment;
    private boolean isCardVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_opcao);

        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: -----------------------------------> A atividade foi criada com sucesso.");

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);

        Button calendario = findViewById(R.id.btAgenda);
        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCalendario = new Intent(TelaOpcao.this, Calendario.class);
                startActivity(telaCalendario);
            }
        });

        Button reservas = findViewById(R.id.btReserva);
        reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telareservas = new Intent(TelaOpcao.this, Reservas.class);
                startActivity(telareservas);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleBottomNavigationItemSelected(item);
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_inicio);
    }

    private boolean handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_inicio:
                return true;

            case R.id.action_agenda:
                Intent intent2 = new Intent(TelaOpcao.this, Calendario.class);
                startActivity(intent2);
                return true;

            case R.id.action_reservas:
                Intent intent3 = new Intent(TelaOpcao.this, Reservas.class);
                startActivity(intent3);
                return true;

            case R.id.action_perfil:
                toggleCardVisibility(new fragmentoPerfil());
                return true;
        }
        return false;
    }

    private void toggleCardVisibility(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isCardVisible) {
            // Se a CardView estiver visível, remove o fragmento e oculta a CardView
            if (currentFragment != null && currentFragment.getClass() == fragment.getClass()) {
                fragmentTransaction.remove(currentFragment);
                currentFragment = null;
            }
            cardView.setVisibility(View.GONE);
            isCardVisible = false;
        } else {
            // Se a CardView não estiver visível, adiciona o fragmento e exibe a CardView
            fragmentTransaction.add(R.id.cardView, fragment);
            currentFragment = fragment;
            cardView.setVisibility(View.VISIBLE);
            cardView.bringToFront();
            cardView.setVisibility(View.VISIBLE);
            isCardVisible = true;
        }

        fragmentTransaction.commit();
    }
}
