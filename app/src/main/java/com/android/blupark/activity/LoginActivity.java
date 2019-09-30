package com.android.blupark.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.config.ConfiguracaoFireBase;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializar Componentes
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);


    }

    public void validarLoginUsuario(View view){
        //Recuperar os textos dos campos
       String textoEmail =  campoEmail.getText().toString();
       String textoSenha = campoSenha.getText().toString();
       if (!textoEmail.isEmpty()){
           if (!textoSenha.isEmpty()){
               Usuario usuario = new Usuario();
               usuario.setEmail(textoEmail);
               usuario.setSenha(textoSenha);
               logarUsuario(usuario);
           }else{
               Toast.makeText(LoginActivity.this,"Preencha a Senha !",
                       Toast.LENGTH_SHORT).show();
           }

       }else{
           Toast.makeText(LoginActivity.this,"Preencha o E-mail!",
                   Toast.LENGTH_SHORT).show();
       }

    }

    public void logarUsuario(Usuario usuario){
        autenticacao = ConfiguracaoFireBase.getFireBaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    UsuarioHelper.toLoadingLoginToDashboard(LoginActivity.this);
                     //UsuarioHelper.toDashBoardActivity(LoginActivity.this);
                     //UsuarioHelper.toCadastroVeiculoActivity(LoginActivity.this);


                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha não correspondem a um usuario válido";
                    }catch (Exception e){
                        excecao = "Erro ao efetuar o login. "+ e.getMessage();
                    }
                    Toast.makeText(LoginActivity.this,excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
