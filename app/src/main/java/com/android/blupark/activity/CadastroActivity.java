package com.android.blupark.activity;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText campoNome, campoEmail, campoSenha, confirmarSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campoNome = findViewById(R.id.editCadastroNome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        confirmarSenha = findViewById(R.id.editConfirmarSenha);

    }

    public void validarCadastroUsuario(View view){
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textSenha = campoSenha.getText().toString();

        if (!textoNome.isEmpty()){
            if (!textoEmail.isEmpty()){
                if (validarSenha()){
                    if (!textSenha.isEmpty()){
                        Usuario usuario = new Usuario();
                        usuario.setNome(textoNome);
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textSenha);

                        cadastrarUsuarioFB(usuario);

                    }else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Senhas Diferentes!", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(CadastroActivity.this,
                        "Preencha o E-mail!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this,
                    "Preencha o Nome!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validarSenha(){
        String textSenha = campoSenha.getText().toString();
        String confirmSenha = confirmarSenha.getText().toString();
        if (textSenha.equals(confirmSenha)){
            return true;
        }else{
            return false;
        }

    }

    public void cadastrarUsuarioFB(final Usuario usuario){

        autenticacao = ConfiguracaoFireBase.getFireBaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   try {
                       String idUsuario = task.getResult().getUser().getUid();
                       usuario.setId(idUsuario);
                       usuario.salvar();

                       //Atualizar nome  no UserProfile
                       UsuarioHelper.atualizarNomeUsuario(usuario.getNome());

                       startActivity(new Intent(CadastroActivity.this,LoginActivity.class));
                       finish();
                       Toast.makeText(CadastroActivity.this,
                               "Cadastro realizado com sucesso!",
                               Toast.LENGTH_LONG).show();

                   }catch (Exception e){
                        e.printStackTrace();
                   }
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido!";
                    }catch(FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já está cadastrada!";
                    }catch(Exception e){
                        excecao = "Erro ao cadastrar usuário!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

   /* PEGAR DATA DE NASCIMENTO COM CALENDARIO E CALCULAR IDADE
   public void pegarData(){
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        calcularIdade(dayOfMonth, month, year);

                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }
    private int calcularIdade(int dayN, int monthN, int yearN){
        Calendar c = Calendar.getInstance();
        int dayA = c.get(Calendar.DAY_OF_MONTH);
        int monthA = c.get(Calendar.MONTH);
        int yearA = c.get(Calendar.YEAR);

        int idade = yearA- yearN;
        if(monthN > monthA){
            idade --;
        }else if(monthA == monthN){
            if (dayN > dayA){
                idade --;
            }
        }
        //textCalendar.setText("Idade: "+idade);
        return idade;

    }*/
}
