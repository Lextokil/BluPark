package com.android.blupark.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFireBase {

    private static DatabaseReference database;
    private static FirebaseAuth auth;


    //Retorna instancia do FirebaseDataBase
    public static DatabaseReference getFireBaseDataBase(){
        if (database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    //Retorna a instancia do FireBaseAuth
    public static FirebaseAuth getFireBaseAutenticacao(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static void PersistirDados(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
