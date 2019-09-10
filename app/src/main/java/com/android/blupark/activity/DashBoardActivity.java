package com.android.blupark.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioFireBase;
import com.android.blupark.model.Usuario;
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
    private DatabaseReference veiculosRef = FirebaseDatabase.getInstance().getReference("veiculos");
    private DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private ValueEventListener valueEventListenerVeiculos;

    private TextView qtdTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        qtdTickets = findViewById(R.id.textTickets);




    }

    @Override
    protected void onStart() {
        super.onStart();
        GetVeiculos();
        GetTickets();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Verifica de o banco ou o listener não estão nulos se não estiverem remove o listener
        if (veiculosRef != null && valueEventListenerVeiculos != null) {
            veiculosRef.removeEventListener(valueEventListenerVeiculos);
        }
        if (usuarioRef != null && valueEventListenerVeiculos != null) {
            usuarioRef.removeEventListener(valueEventListenerVeiculos);
        }
       // veiculosRef.removeEventListener(valueEventListenerVeiculos);
       // usuarioRef.removeEventListener(valueEventListenerVeiculos);

    }

    //Pega os veiculos que o usuario tem cadastrado
    public void GetVeiculos(){
        String usuarioId;
        usuarioId = UsuarioFireBase.getIDUsuarioAtual();


        veiculosRef.child(usuarioId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Log.i("dados", "Retorno "+ dados.toString());
                    Veiculo veiculo = dados.getValue(Veiculo.class);
                    veiculos.add(veiculo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Atualiza a quantidade de tickets disponíveis do usuario
    public void GetTickets(){

        usuarioRef.child(UsuarioFireBase.getIDUsuarioAtual()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                qtdTickets.setText("Tickets disponíveis: "+ usuario.getQtdTickets());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    //Leva para tela de Compra de tickets
    public void ToCompraTickesActivity(View view){
        UsuarioFireBase.toCompraTicketsActivity(this);
    }

    public void ToCadastroVeiculos(View view){
        UsuarioFireBase.toCadastroVeiculoActivity(this);
    }
}
