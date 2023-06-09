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
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(this));

        // Lista de eventos e adaptador
        eventosList = new ArrayList<>();
        databaseReference = ConfuguracaoFirebase.getFirebaseDatabase();
        databaseReference.child("Calendar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dn : snapshot.getChildren()) {
                    Evento e = dn.getValue(Evento.class);
                    eventosList.add(e);
                }
                eventosAdapter = new EventosAdapter(eventosList);
                recyclerViewEventos.setAdapter(eventosAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                Intent intent4 = new Intent(Reservas.this, TelaOpcao.class);
                startActivity(intent4);
                return true;

            case R.id.action_agenda:
                Intent intent2 = new Intent(Reservas.this, Calendario.class);
                startActivity(intent2);
                return true;

            case R.id.action_reservas:
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


//        eventosAdapter = new EventosAdapter(eventosList);
//        recyclerViewEventos.setAdapter(eventosAdapter);
//
//        // Referência ao nó "Calendar" no banco de dados
//        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
//
//        // Listener para capturar os dados do banco de dados
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                eventosList.clear(); // Limpa a lista de eventos
//
//                // Percorre os snapshots dos dados recebidos
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
//                        // Verifica se o snapshot possui os campos necessários
//                        if (eventSnapshot.hasChild("nome") && eventSnapshot.hasChild("data") && eventSnapshot.hasChild("horario")) {
//                            // Obtém os valores dos campos
//                            String nome = eventSnapshot.child("nomeEvento").getValue(String.class);
//                            String data = eventSnapshot.child("dataEvento").getValue(String.class);
//                            String horarioInicio = eventSnapshot.child("horarioInicio").getValue(String.class);
//                            String horarioFim = eventSnapshot.child("horarioTermino").getValue(String.class);
//                            String local = eventSnapshot.child("localEvento").getValue(String.class);
//
//                            // Cria o objeto Evento e adiciona à lista
//                            Evento evento = new Evento(nome, data, horarioInicio, horarioFim, local);
//                            eventosList.add(evento);
//                        }
//                    }
//                }
//
//                eventosAdapter.notifyDataSetChanged(); // Notifica o adaptador que os dados foram alterados
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Tratar o erro, se necessário
//            }
//        });
//    }
//}