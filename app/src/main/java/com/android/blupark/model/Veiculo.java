package com.android.blupark.model;

import com.android.blupark.config.ConfiguracaoFireBase;
import com.android.blupark.helper.UsuarioFireBase;
import com.google.firebase.database.DatabaseReference;

public class Veiculo {
    private String placa;
    private String modelo;
    private String tipo;



    public Veiculo() {
    }

    public void salvarVeiculo(){

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBaseDataBase();
        DatabaseReference veiculos = firebaseRef.child("veiculos").child(UsuarioFireBase.getIDUsuarioAtual()).push();
        veiculos.setValue(this);
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
