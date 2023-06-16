package com.example.agendaif;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.agendaif.config.ConfuguracaoFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reservas extends AppCompatActivity {

    RecyclerView recyclerViewEventos;
    EventosAdapter eventosAdapter;
    List<Evento> eventosList;

    DatabaseReference databaseReference;

    private CardView cardView;
    private Fragment currentFragment;
    private boolean isCardVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: -----------------------------------> A atividade foi criada com sucesso.");

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);

        // Referência ao RecyclerView no layout
        recyclerViewEventos = findViewById(R.id.recyclerViewEventos);
        //organiza os itens em uma lista vertical.
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(this));

        // Lista de eventos e adaptador
        eventosList = new ArrayList<>();

        // Obtendo referência do banco de dados
        databaseReference = ConfuguracaoFirebase.getFirebaseDatabase();

        // Recuperando os eventos do banco de dados Firebase
        databaseReference.child("Calendar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dn : snapshot.getChildren()) {
                    //os dados do snapshot são percorridos usando um loop e convertidos em objetos Evento e adicionado ao eventosList
                    Evento e = dn.getValue(Evento.class);
                    eventosList.add(e);
                }
                // Criando e configurando o adaptador de eventos
                eventosAdapter = new EventosAdapter(eventosList);
                recyclerViewEventos.setAdapter(eventosAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tratando erros de leitura do banco de dados Firebase
            }
        });

        // Configurando o BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleBottomNavigationItemSelected(item);
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_reservas);
    }

    private boolean handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_inicio:
                // Abrir a atividade TelaOpcao
                Intent intent4 = new Intent(Reservas.this, TelaOpcao.class);
                startActivity(intent4);
                return true;

            case R.id.action_agenda:
                // Abrir a atividade Calendario
                Intent intent2 = new Intent(Reservas.this, Calendario.class);
                startActivity(intent2);
                return true;

            case R.id.action_reservas:
                return true;

            case R.id.action_perfil:
                // Alternar a visibilidade da CardView com base no fragmento selecionado
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
