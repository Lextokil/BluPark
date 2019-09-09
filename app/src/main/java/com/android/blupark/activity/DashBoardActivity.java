package com.android.blupark.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.config.ConfiguracaoFireBase;
import com.android.blupark.helper.UsuarioFireBase;
import com.android.blupark.model.Veiculo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {
    private LinearLayout layoutCarros;
    private ArrayList<Veiculo> veiculos = new ArrayList<>();
    private DatabaseReference veiculosRef = ConfiguracaoFireBase.getFireBaseDataBase();
    private ValueEventListener valueEventListenerVeiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);



    }

    @Override
    protected void onStart() {
        super.onStart();
        GetVeiculos();




    }

    @Override
    protected void onStop() {
        super.onStop();
        veiculosRef.removeEventListener(valueEventListenerVeiculos);
    }

    public void GetVeiculos(){
        String usuarioId;
        usuarioId = UsuarioFireBase.getIDUsuarioAtual();

        FirebaseDatabase.getInstance().getReference("veiculos").child(usuarioId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Log.i("dados", "Retorno "+ dados.toString());
                    Veiculo veiculo = dados.getValue(Veiculo.class);
                    veiculos.add(veiculo);

                }
                Log.i("Veiculos", "1 Index Array : "+ veiculos.get(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        /*valueEventListenerVeiculos = veiculosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                veiculos.clear();
                for (DataSnapshot dados: dataSnapshot.child(UsuarioFireBase.getIDUsuarioAtual()).getChildren()){
                    Log.i("dados","retorno teste"+ dados.toString());
                    Toast.makeText(DashBoardActivity.this,dados.toString()+"Chegou no loop",
                            Toast.LENGTH_SHORT).show();

                    Veiculo veiculo = dados.getValue(Veiculo.class);

                    veiculos.add(veiculo);
                    Log.i("Retorno","Dados"+veiculo.getPlaca());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
