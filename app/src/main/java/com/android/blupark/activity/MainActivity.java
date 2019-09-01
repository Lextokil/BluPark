package com.android.blupark.activity;
import com.android.blupark.R;
import com.android.blupark.helper.UsuarioFireBase;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void abrirTelaLogin(View view){
        startActivity( new Intent(this, LoginActivity.class));
    }

    public void abrirTelaCadastro(View view){
        startActivity( new Intent( this, CadastroActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = UsuarioFireBase.getUsuarioAtual();
        if (user != null){
           // UsuarioFireBase.toDashBoardActivity(this);
        }

    }
}
