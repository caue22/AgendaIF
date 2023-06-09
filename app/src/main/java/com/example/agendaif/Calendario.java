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


        calendarView = findViewById(R.id.calendarView);
        editText = findViewById(R.id.editText);
        spinnerHorarioInicio = findViewById(R.id.spinnerHorarioInicio);
        spinnerHorarioTermino = findViewById(R.id.spinnerHorarioTermino);
        spinnerLocais = findViewById(R.id.spinner_locais);

        ArrayAdapter<String> adapterHorarioInicio = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_layout, // ID do layout do item do spinner
                getResources().getStringArray(R.array.spinner_horarios)
        );

        adapterHorarioInicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioInicio.setAdapter(adapterHorarioInicio);


        ArrayAdapter<String> adapterHorarioTermino = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_layout,
                getResources().getStringArray(R.array.spinner_horarios)
        );
        adapterHorarioTermino.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioTermino.setAdapter(adapterHorarioTermino);

        ArrayAdapter<CharSequence> adapterLocais = new ArrayAdapter<CharSequence>(
                this,
                R.layout.spinner_item_layout, // Layout personalizado para os itens do spinner
                getResources().getStringArray(R.array.spinner_locais)
        );
        adapterLocais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocais.setAdapter(adapterLocais);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected =  Integer.toString(dayOfMonth)+"_"+ Integer.toString(month + 1) +"_"+ Integer.toString(year);
//                calendarClicked();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleBottomNavigationItemSelected(item);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_agenda);

    }



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
            Intent intent = new Intent(Calendario.this, MaCaue Garcia
                    Caue Garcia#1155

                    auroux — 07/12/2022 19:34
            asdasd
            Caue Garcia — 07/12/2022 19:34
            https://www.youtube.com/watch?v=3g-1l5BjfyA&authuser=0
            YouTube
            Curso Online DlteC do Brasil
            Como Ativar DHCP e DHCPv6 em VLANs (aula prática utilizando Cisco P...
            Imagem
            auroux — 02/03/2023 20:03
            Imagem
            Caue Garcia — 02/03/2023 20:12
            voltei
            auroux — 04/04/2023 16:12
            https://www.onlinedoctranslator.com/pt/translationform
            Online Doc Translator
            Tradutor Online de Documentos Grátis – Preserve o layout de seus do...
            Serviço grátis que traduz documentos office (Word, Excel, PowerPoint, PDF, OpenOffice, textos) em vários idiomas, mantendo o layout original. Arquivos suportados: Word: doc, docx; PDF: pdf; Excel: xls, xlsx; PowerPoint: ppt, pptx; Text xml, txt…
            Imagem
            Caue Garcia
            iniciou uma chamada que durou 27 minutos.
 — 28/05/2023 16:02
            auroux — 28/05/2023 16:14
            getSupportActionBar().hide();
            auroux — Hoje às 18:48
            to aaqui ja
            Caue Garcia — Hoje às 18:51
            opaa
            Caue Garcia
            iniciou uma chamada que durou 29 minutos.
 — Hoje às 18:51
            auroux — Hoje às 18:52
            Tipo de arquivo em anexo: document
            ProjetoFinal_PPDM_CAUE_FELIPE.docx
            364.22 KB
            Caue Garcia — Hoje às 19:08
            Imagem
                    Imagem
            Imagem
                    Imagem
            Imagem
            https://www.figma.com/proto/ogI0h9CH20N1buU06ceR63/Untitled?type=design&node-id=102-209&scaling=min-zoom&page-id=3%3A1559&starting-point-node-id=3%3A1560
            Caue Garcia
            iniciou uma chamada.
 — Hoje às 19:20
            Caue Garcia — Hoje às 19:30
            Telas do Aplicativo
                    Imagem
            Imagem
            Caue Garcia — Hoje às 19:41
            Imagem
            auroux — Hoje às 19:42
            Imagem
            Caue Garcia — Hoje às 20:15
            Imagem
                    Imagem
            Tipo de arquivo em anexo: archive
            AgendaIF_versao_final.zip
            164.11 KB
            auroux — Hoje às 20:31
package com.example.agendaif;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
            Expandir
            message.txt
            7 KB
﻿
package com.example.agendaif;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agendaif.Modelos.usuarios;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

            public class MainActivity extends AppCompatActivity {


                private static final String TAG = "CustomAuthActivity";
                private String mCustomToken;

                // [START declare_auth]
                private FirebaseAuth auth;
                // [END declare_auth]
                FirebaseDatabase database;

                Button login;


                private GoogleSignInClient client;


                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
                    getSupportActionBar().hide();

                    Log.d(TAG, " -----------------------------------> onCreate: A atividade foi criada com sucesso.");

                    auth = FirebaseAuth.getInstance();
                    database = FirebaseDatabase.getInstance("https://agendamobile-9bfa7-default-rtdb.firebaseio.com/");
//Teste novo de login

                    login = findViewById(R.id.login);
                    GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    client = GoogleSignIn.getClient(this, options);
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = client.getSignInIntent();
                            startActivityForResult(i, 123);
                        }
                    });


                }

                @Override
                protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
                    super.onActivityResult(requestCode, resultCode, data);
                    if (requestCode == 123){
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                            auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        usuarios usuario1= new usuarios();
                                        assert  user != null;
                                        usuario1.setUserId(user.getUid());
                                        usuario1.setUserName(user.getDisplayName());
                                        usuario1.setProfilePic(user.getPhotoUrl().toString());
                                        database.getReference().child("usuarios").child(user.getUid()).setValue(usuario1);
                                        Intent intent = new Intent (MainActivity.this, TelaOpcao.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } catch (ApiException e) {
                            e.printStackTrace();
                        }

                    }
                }

/*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void startSignIn() {

        // [START sign_in_custom]
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_custom]
    }

    private void updateUI(FirebaseUser user) {

    }*/
            }
            message.txt
            7 KBinActivity.class);
            startActivity(intent);
            finish(); // Finalizar a atividade atual para que o usuário não possa voltar pressionando o botão "Voltar"
            return null; // Retornar null quando o usuário não está autenticado
        }
    }
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







/*FirebaseAuth.getInstance().signOut();
                Intent mainIntent = new Intent(Calendario.this, MainActivity.class);
                startActivity(mainIntent);
                return true; */

