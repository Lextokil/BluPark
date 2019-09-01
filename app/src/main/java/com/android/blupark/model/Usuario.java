package com.android.blupark.model;

import com.android.blupark.config.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private int qtdTickets = 0;


    public Usuario() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBaseDataBase();
        DatabaseReference usuarios = firebaseRef.child("usuarios").child(getId());
        usuarios.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getQtdTickets() {
        return qtdTickets;
    }

    public void setQtdTickets(int qtdTickets) {
        this.qtdTickets = qtdTickets;
    }

}
