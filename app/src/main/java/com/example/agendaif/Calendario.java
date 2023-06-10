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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Calendario extends AppCompatActivity {
    CalendarView calendarView;
    EditText editText;
    Spinner spinnerHorarioInicio;
    Spinner spinnerHorarioTermino;
    Spinner spinnerLocais;
    String stringDateSelected;

    private CardView cardView;
    private Fragment currentFragment;
    private boolean isCardVisible = false;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: -----------------------------------> A atividade foi criada com sucesso.");

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);

        // Inicialização dos componentes de layout
        calendarView = findViewById(R.id.calendarView);
        editText = findViewById(R.id.editText);
        spinnerHorarioInicio = findViewById(R.id.spinnerHorarioInicio);
        spinnerHorarioTermino = findViewById(R.id.spinnerHorarioTermino);
        spinnerLocais = findViewById(R.id.spinner_locais);

        // Configuração do adaptador para o spinner de horário de início
        ArrayAdapter<String> adapterHorarioInicio = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_layout, // ID do layout do item do spinner
                getResources().getStringArray(R.array.spinner_horarios)
        );
        adapterHorarioInicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioInicio.setAdapter(adapterHorarioInicio);

        // Configuração do adaptador para o spinner de horário de término
        ArrayAdapter<String> adapterHorarioTermino = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_layout,
                getResources().getStringArray(R.array.spinner_horarios)
        );
        adapterHorarioTermino.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioTermino.setAdapter(adapterHorarioTermino);

        // Configuração do adaptador para o spinner de locais
        ArrayAdapter<CharSequence> adapterLocais = new ArrayAdapter<CharSequence>(
                this,
                R.layout.spinner_item_layout, // Layout personalizado para os itens do spinner
                getResources().getStringArray(R.array.spinner_locais)
        );
        adapterLocais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocais.setAdapter(adapterLocais);

        // Listener para capturar a data selecionada no calendário
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected =  Integer.toString(dayOfMonth)+"_"+ Integer.toString(month + 1) +"_"+ Integer.toString(year);
            }
        });

        // Inicialização da referência ao nó "Calendar" no banco de dados Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        // Configuração do BottomNavigationView para lidar com os itens de navegação inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleBottomNavigationItemSelected(item);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_agenda);
    }

    // Método para tratar o clique do botão "Salvar Evento"
    public void buttonSaveEvent(View view) {
        String nomeEvento = editText.getText().toString();
        String dataEvento = stringDateSelected;
        String horarioInicio = spinnerHorarioInicio.getSelectedItem().toString();
        String horarioTermino = spinnerHorarioTermino.getSelectedItem().toString();
        String localEvento = spinnerLocais.getSelectedItem().toString();

        // Verificar se já existe um evento na mesma data e horário
        String eventoKey = stringDateSelected + "_" + horarioInicio + "_" + horarioTermino + "_" + localEvento; // Chave única para o evento

        // Verificar se o evento já existe com as mesmas informações (local, data e horário)
        databaseReference.child(eventoKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Evento já existe
                    Toast.makeText(Calendario.this, "Já existe um evento marcado para o mesmo local, data e horário", Toast.LENGTH_SHORT).show();
                } else {
                    // Evento não existe, pode ser salvo
                    String userId = getUserId(); // Obtenha o ID do usuário atualmente logado
                    String userName = getUserName(); // Obtenha o nome do usuário atualmente logado

                    Evento evento = new Evento(nomeEvento, stringDateSelected, horarioInicio, horarioTermino, localEvento);
                    evento.setUserId(userId);
                    evento.setUserName(userName);
                    databaseReference.child(eventoKey).setValue(evento)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Calendario.this, "Evento salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Calendario.this, "Erro ao salvar o evento", Toast.LENGTH_SHORT).show();
                                    Log.e("Calendario", "Erro ao salvar o evento", e);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Calendario.this, "Erro ao verificar a existência de eventos", Toast.LENGTH_SHORT).show();
                Log.e("Calendario", "Erro ao verificar a existência de eventos", error.toException());
            }
        });
    }

    // Método para obter o ID do usuário atualmente logado
    private String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            return userId;
        } else {
            // Usuário não está autenticado
            // Exibir mensagem de erro
            Toast.makeText(Calendario.this, "Você não está logado. Por favor, faça login.", Toast.LENGTH_SHORT).show();

            // Redirecionar para a tela de login
            Intent intent = new Intent(Calendario.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finalizar a atividade atual para que o usuário não possa voltar pressionando o botão "Voltar"
            return null; // Retornar null quando o usuário não está autenticado
        }
    }

    // Método para obter o nome do usuário atualmente logado
    private String getUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userName = currentUser.getDisplayName();
            return userName;
        } else {
            // Usuário não está autenticado
            // Exibir mensagem de erro
            Toast.makeText(Calendario.this, "Você não está logado. Por favor, faça login.", Toast.LENGTH_SHORT).show();

            // Redirecionar para a tela de login
            Intent intent = new Intent(Calendario.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finalizar a atividade atual para que o usuário não possa voltar pressionando o botão "Voltar"
            return null; // Retornar null quando o usuário não está autenticado
        }
    }

    // Método para tratar a seleção de itens do BottomNavigationView
    private boolean handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_inicio:
                Intent intent4 = new Intent(Calendario.this, TelaOpcao.class);
                startActivity(intent4);
                return true;

            case R.id.action_agenda:
                return true;

            case R.id.action_reservas:
                Intent intent3 = new Intent(Calendario.this, Reservas.class);
                startActivity(intent3);
                return true;

            case R.id.action_perfil:
                toggleCardVisibility(new fragmentoPerfil());
                return true;
        }

        return false;
    }

    // Método para alternar a visibilidade da CardView com um fragmento
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
