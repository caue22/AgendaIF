package com.example.agendaif;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TelaExibicaoEventos extends AppCompatActivity {


    //@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tela_exibicao_eventos);
//
//        recyclerViewEventos = findViewById(R.id.recyclerViewEventos);
//        listaEventos = new ArrayList<>();
//        eventoAdapter = new EventoAdapter(listaEventos);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerViewEventos.setLayoutManager(layoutManager);
//        recyclerViewEventos.setAdapter(eventoAdapter);
//
//        // Recuperar os dados passados como extras do Intent
//        Intent intent = getIntent();
//        String dataSelecionada = intent.getStringExtra("dataSelecionada");
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
//        databaseReference.child(dataSelecionada).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listaEventos.clear();
//
//                for (DataSnapshot eventoSnapshot : snapshot.getChildren()) {
//                    String descricao = eventoSnapshot.getValue().toString();
//                    Evento evento = new Evento(dataSelecionada, descricao);
//                    listaEventos.add(evento);
//                }
//
//                eventoAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Tratar erro, se necessário
//            }
//        });
//    }
//}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_exibicao_eventos);

        TextView textViewData = findViewById(R.id.textViewData);
        TextView textViewEvento = findViewById(R.id.textViewEvento);

        // Recuperar a referência do nó "eventos" do Firebase Realtime Database
        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference("Calendar");

        // Recuperar os dados do Firebase
        eventosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Iterar sobre os eventos
                    for (DataSnapshot eventoSnapshot : snapshot.getChildren()) {
                        // Obter os valores dos campos data e descricao do evento
                        String data = eventoSnapshot.child("data").getValue(String.class);
                        String descricao = eventoSnapshot.child("descricao").getValue(String.class);

                        // Exibir os dados na interface do usuário
                        textViewData.setText("Data selecionada: " + data);
                        textViewEvento.setText("Evento: " + descricao);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tratar o erro, se necessário
            }
        });
    }
}