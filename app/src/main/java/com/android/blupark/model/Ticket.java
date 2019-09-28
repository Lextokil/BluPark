package com.android.blupark.model;

import com.android.blupark.config.ConfiguracaoFireBase;
import com.android.blupark.helper.UsuarioHelper;
import com.google.firebase.database.DatabaseReference;

public class Ticket {
    private String veiculo;
    private Double latitude;
    private Double longitute;
    private Long endTicketTime;

    public Ticket() {
    }

    public void salvarTicket(){
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBaseDataBase();
        DatabaseReference tickets = firebaseRef.child("tickets").child(UsuarioHelper.getIDUsuarioAtual()).push();
        tickets.setValue(this);
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitute() {
        return longitute;
    }

    public void setLongitute(Double longitute) {
        this.longitute = longitute;
    }

    public Long getEndTicketTime() {
        return endTicketTime;
    }

    public void setEndTicketTime(Long endTicketTime) {
        this.endTicketTime = endTicketTime;
    }
}
