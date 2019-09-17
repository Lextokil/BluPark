package com.android.blupark.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.adapter.VeiculoRow;
import com.android.blupark.adapter.VeiculoRowAdapater;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.helper.VeiculoHelper;
import com.android.blupark.model.Veiculo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AtivarTicketActivity extends AppCompatActivity {



    private Button btnAtivarTicket;
    private Spinner spinner;
    private ArrayList<VeiculoRow> mVeiculosList;
    private VeiculoRowAdapater mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativar_ticket);
        spinner = findViewById(R.id.spinnerVeiculos);
        initlist();

         mAdapter = new VeiculoRowAdapater(this,mVeiculosList);


        spinner.setAdapter(mAdapter);



        btnAtivarTicket = findViewById(R.id.btnAtivarTicket);
        btnAtivarTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               decreaseTicketByOne();
            }
        });
    }


    private  void initlist(){
        mVeiculosList  = new ArrayList<>();
        for (Veiculo veiculo: UsuarioHelper.veiculos){
            String rowText = veiculo.getPlaca().toUpperCase() + " - " + veiculo.getModelo().toUpperCase();
            int iconVeiculo = VeiculoHelper.GetIconTipe(veiculo.getTipo());

            VeiculoRow veiculoRow = new VeiculoRow(rowText, iconVeiculo);
            mVeiculosList.add(veiculoRow);
        }
    }





    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    public void decreaseTicketByOne(){
        final DatabaseReference ticketsRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(UsuarioHelper.getIDUsuarioAtual()).child("qtdTickets");
              boolean condition = false;

        ticketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total =  dataSnapshot.getValue(int.class);
                total -= 1;
                if (total > 0){
                    ticketsRef.setValue(total);
                    Toast.makeText(AtivarTicketActivity.this,
                            "Ticket ativado com sucesso!",
                            Toast.LENGTH_LONG).show();
                    activateTicket();

                }else{
                    Toast.makeText(AtivarTicketActivity.this,
                            "Tickets Insuficientes!",
                            Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void activateTicket(){


        UsuarioHelper.isTicketAtivo = true;
        int index = spinner.getSelectedItemPosition();

        UsuarioHelper.veiculo = UsuarioHelper.veiculos.get(index);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putLong("millisLeft",60000);
        editor.putBoolean("timerRunning", UsuarioHelper.isTicketAtivo);
        editor.putLong("endTime",(System.currentTimeMillis() + 60000));
        editor.putInt("index", index);
        editor.apply();
        UsuarioHelper.toDashBoardActivity(AtivarTicketActivity.this);

    }


}
