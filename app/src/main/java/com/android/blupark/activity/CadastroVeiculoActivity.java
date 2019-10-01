package com.android.blupark.activity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Veiculo;
import com.google.android.material.textfield.TextInputEditText;
import com.android.blupark.activity.VeiculosCadastrados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CadastroVeiculoActivity extends AppCompatActivity {
    private TextInputEditText campoPlaca, campoModelo;

    private Button btnCarro, btnMoto, btnOnibus, btnCaminhao;
    private Veiculo veiculo;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);
        Log.i("regex", "regex:  " + placaVeiculadoValida("miz2670"));


        tipo = "";
        campoModelo = findViewById(R.id.editModelo);
        campoPlaca = findViewById(R.id.editPlaca);
        btnCarro = findViewById(R.id.btnCarro);
        btnMoto = findViewById(R.id.btnMoto);
        btnOnibus = findViewById(R.id.btnOnibus);
        btnCaminhao = findViewById(R.id.btnCaminhao);


    }



    public void salvarVeiculo(View view){
        String textPlaca = campoPlaca.getText().toString().toUpperCase();
        String placaFormatada = textPlaca;
        String placaLetras = placaFormatada.substring(0,3);
        String placaNumeros = placaFormatada.substring(3,7);
        placaFormatada = (placaLetras+"-"+placaNumeros);

        String textModelo = campoModelo.getText().toString().toUpperCase();

        if (!tipo.isEmpty()) {
            if (!textModelo.isEmpty()) {
                if (!textPlaca.isEmpty()) {
                    if (placaVeiculadoValida(textPlaca)) {
                        try {
                            veiculo = new Veiculo();
                            veiculo.setPlaca(placaFormatada.toUpperCase());
                            veiculo.setModelo(textModelo.toUpperCase());
                            veiculo.setTipo(tipo);
                            veiculo.salvarVeiculo();
                            UsuarioHelper.toDashBoardActivity(CadastroVeiculoActivity.this);
                            Toast.makeText(CadastroVeiculoActivity.this,
                                    "Veículo cadastrado com sucesso!",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(CadastroVeiculoActivity.this,
                                "Digite uma placa valida: mmm9999", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(CadastroVeiculoActivity.this,
                            "Preencha a placa do veículo!", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(CadastroVeiculoActivity.this,
                        "Preencha o modelo do veículo!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(CadastroVeiculoActivity.this,
                    "Selecione o tipo de veículo!", Toast.LENGTH_LONG).show();
        }
    }

    public String tipoVeiculo(View view) {

        btnMoto.setBackgroundTintList(getResources().getColorStateList(R.color.btnCadastro));
        btnCaminhao.setBackgroundTintList(getResources().getColorStateList(R.color.btnCadastro));
        btnCarro.setBackgroundTintList(getResources().getColorStateList(R.color.btnCadastro));
        btnOnibus.setBackgroundTintList(getResources().getColorStateList(R.color.btnCadastro));
        //Muda a cor do botão selecionado
        view.setBackgroundTintList(getResources().getColorStateList(R.color.btnLogin));
        if (view.getId() == btnCarro.getId()) {
            tipo = "CARRO";
        } else if (view.getId() == btnMoto.getId()) {
            tipo = "MOTO";
        } else if (view.getId() == btnCaminhao.getId()) {
            tipo = "CAMINHAO";
        } else {
            tipo = "ONIBUS";
        }
        return tipo;
    }


    public boolean placaVeiculadoValida(String placa) {
        return Pattern.compile("[a-zA-Z]{3}[0-9]{4}").matcher(placa).matches();
    }
}
