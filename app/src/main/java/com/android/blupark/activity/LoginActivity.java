package com.android.blupark.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.config.ConfiguracaoFireBase;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.helper.VeiculoHelper;
import com.android.blupark.model.Usuario;
import com.android.blupark.model.Veiculo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;
    private Button btnEntrar;
    private DatabaseReference veiculosRef = FirebaseDatabase.getInstance().getReference("veiculos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializar Componentes
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);


        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // IMPEDE O USUARIO DE CLICAR VARIAS VEZES NO BOTÃO
                btnEntrar.setEnabled(false);
                validarLoginUsuario();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void validarLoginUsuario() {
        //Recuperar os textos dos campos
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                logarUsuario(usuario);
            } else {
                Toast.makeText(LoginActivity.this, "Preencha a Senha!",
                        Toast.LENGTH_SHORT).show();
                btnEntrar.setEnabled(true);

            }

        } else {
            Toast.makeText(LoginActivity.this, "Preencha o E-mail!",
                    Toast.LENGTH_SHORT).show();
            btnEntrar.setEnabled(true);

        }

    }

    public void logarUsuario(Usuario usuario) {
        autenticacao = ConfiguracaoFireBase.getFireBaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Thread pegarVeiculos = new Thread() {
                                @Override
                                public void run() {
                                    VeiculoHelper.GetVeiculos();
                                    UsuarioHelper.toLoadingLoginToDashboard(LoginActivity.this);
                                    do {
                                        try {
                                            sleep(1000);
                                        } catch (Exception e) {
                                            Log.e("", e.getMessage());
                                        }

                                    } while (UsuarioHelper.veiculos == null);

                                    UsuarioHelper.toDashBoardActivity(LoginActivity.this);

                                }

                            };
                            pegarVeiculos.start();


                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuário não está cadastrado!";
                                btnEntrar.setEnabled(true);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "E-mail e senha não correspondem a um usuario válido!";
                                btnEntrar.setEnabled(true);
                            } catch (Exception e) {
                                excecao = "Erro ao efetuar o login!" + e.getMessage();
                                btnEntrar.setEnabled(true);
                            }
                            Toast.makeText(LoginActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
