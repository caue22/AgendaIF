package com.example.agendaif.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfuguracaoFirebase {
    private static DatabaseReference database;

    // Retorna a instância do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase() {
        // Verifica se a referência do database é nula
        if (database == null) {
            // Obtém a instância do FirebaseDatabase e a referência para o nó raiz do banco de dados
            database = FirebaseDatabase.getInstance().getReference();
        }
        // Retorna a referência do database
        return database;
    }
}
