package com.android.blupark.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blupark.R;
import com.android.blupark.activity.DashBoardActivity;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Veiculo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterVeiculos extends RecyclerView.Adapter<AdapterVeiculos.MyViewHolder>{
    private DatabaseReference veiculosRef = FirebaseDatabase.getInstance().getReference("veiculos").
            child(UsuarioHelper.getIDUsuarioAtual());



    public AdapterVeiculos(List<Veiculo> lista) {
        UsuarioHelper.veiculos = (ArrayList<Veiculo>) lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View veiculosLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_veiculos, parent, false);
        return new MyViewHolder(veiculosLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Veiculo veiculos = UsuarioHelper.veiculos.get( position );
        holder.placa.setText(veiculos.getPlaca());
        holder.modelo.setText(veiculos.getModelo());
        holder.tipo.setText(veiculos.getTipo());

    }

    @Override
    public int getItemCount() {
        return UsuarioHelper.veiculos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button btnExcluirVEiculo;
        TextView placa;
        TextView modelo;
        TextView tipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            placa = itemView.findViewById(R.id.textPlaca);
            modelo = itemView.findViewById(R.id.textModelo);
            tipo = itemView.findViewById(R.id.textTipo);
            btnExcluirVEiculo = itemView.findViewById(R.id.btnExcluirVeiculo);
            btnExcluirVEiculo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    excluirVeiculo();
                }

            });

        }
        public void excluirVeiculo (){
            veiculosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataVeiculo : dataSnapshot.getChildren()) {

                        Veiculo veiculoDelete = dataVeiculo.getValue(Veiculo.class);

                        UsuarioHelper.veiculos.clear();
                        if (veiculoDelete.getPlaca().equalsIgnoreCase(placa.getText().toString())) {
                            dataVeiculo.getRef().removeValue();
                        } else {
                            UsuarioHelper.veiculos.add(veiculoDelete);
                        }

                    }

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }



}
