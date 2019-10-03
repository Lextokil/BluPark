package com.android.blupark.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.blupark.R;
import com.android.blupark.model.Veiculo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class VeiculoHelper {
    public static DatabaseReference veiculosRef = FirebaseDatabase.getInstance().getReference("veiculos");


    public static int GetIconTipe(String tipoVeiculo) {
        int iconReturn = 0;
        if (tipoVeiculo.equalsIgnoreCase("MOTO")) {
            iconReturn = R.drawable.iconmoto;

        } else if (tipoVeiculo.equalsIgnoreCase("CARRO")) {
            iconReturn = R.drawable.iconcarro;
        } else if (tipoVeiculo.equalsIgnoreCase("ONIBUS")) {
            iconReturn = R.drawable.iconbus;
        } else {
            iconReturn = R.drawable.iconcaminhao;
        }
        return iconReturn;
    }

    //Pega os veiculos que o usuario tem cadastrado
    public static void GetVeiculos() {
        String usuarioId;
        usuarioId = UsuarioHelper.getIDUsuarioAtual();


        veiculosRef.child(usuarioId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsuarioHelper.veiculos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Log.i("dados", "Retorno " + dados.toString());
                    Veiculo veiculo = dados.getValue(Veiculo.class);
                    UsuarioHelper.veiculos.add(veiculo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
