package com.example.agendaif.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfuguracaoFirebase {
    private static DatabaseReference database;


    public static DatabaseReference getFirebaseDatabase() {
        if(database==null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
}
