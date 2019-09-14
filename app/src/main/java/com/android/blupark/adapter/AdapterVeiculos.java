package com.android.blupark.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;

public class AdapterVeiculos extends RecyclerView.Adapter<AdapterVeiculos.MyViewHolder>{

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View veiculosLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_veiculos, parent, false);
        return new MyViewHolder(veiculosLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.placa.setText(UsuarioHelper.veiculo.getPlaca().toString());
        holder.modelo.setText(UsuarioHelper.veiculo.getModelo().toString());
        holder.tipo.setText(UsuarioHelper.veiculo.getPlaca().toString());
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView placa;
        TextView modelo;
        TextView tipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            placa = itemView.findViewById(R.id.textPlaca);
            modelo = itemView.findViewById(R.id.textModelo);
            tipo = itemView.findViewById(R.id.textTipo);
        }
    }

}
