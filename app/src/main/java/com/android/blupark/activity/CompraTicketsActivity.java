package com.android.blupark.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;

public class CompraTicketsActivity extends AppCompatActivity {
    Button btn10, btn50, btn100, btnConfirm;
    TextView tickets;
    private int ticketsTemp;
    private AlertDialog alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_tickets);
        btn10 = findViewById(R.id.btn10);
        btn50 = findViewById(R.id.btn50);
        btn100 = findViewById(R.id.btn100);
        btnConfirm = findViewById(R.id.btnConfirm);
        tickets = findViewById(R.id.textTickets);

    }

    public void addTicket(View view) {
        if (view.equals(btn10)) {
            ticketsTemp += 10;
        } else if (view.equals(btn50)) {
            ticketsTemp += 50;
        } else {
            ticketsTemp += 100;
        }
        tickets.setText("Quantidade de tickets: " + ticketsTemp);

    }

    public void confirmarCompra(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja confirmar o pedido?");
        builder.setMessage(ticketsTemp + " Tickets");
        builder.setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(CompraTicketsActivity.this,
                        "Compra realizada com sucesso!",
                        Toast.LENGTH_LONG).show();
                UsuarioHelper.addTicketUsuarioAtual(ticketsTemp);
                UsuarioHelper.toDashBoardActivity(CompraTicketsActivity.this);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(CompraTicketsActivity.this, "Compra cancelada!", Toast.LENGTH_SHORT).show();
                ticketsTemp = 0;
                tickets.setText("Tickets no carrinho: " + ticketsTemp);
            }
        });
        alerta = builder.create();
        alerta.show();


    }

}


