package com.example.agendaif;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class fragmentoPerfil extends Fragment {

    private TextView nomeUsuario;
    private TextView emailUsuario;
    private Button btnLogoff;

    private FirebaseAuth auth;

    public fragmentoPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.nomeUsuario);
        emailUsuario = view.findViewById(R.id.emailUsuario);
        btnLogoff = view.findViewById(R.id.btnLogoff);

        auth = FirebaseAuth.getInstance();

        // Obtém os detalhes do usuário logado
        if (auth.getCurrentUser() != null) {
            String nome = auth.getCurrentUser().getDisplayName();
            String email = auth.getCurrentUser().getEmail();

            nomeUsuario.setText(nome);
            emailUsuario.setText(email);
        }

        btnLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Realiza o logout do usuário
                auth.signOut();

                // Verifica se o fragmento do pop-up está aberto
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.cardView);
                if (fragment instanceof fragmentoPerfil) {
                    // Se estiver aberto, fecha o fragmento do pop-up
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }

                // Redireciona para a tela de login (MainActivity)
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
