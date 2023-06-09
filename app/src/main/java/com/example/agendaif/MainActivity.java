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
    Button teste;

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

}