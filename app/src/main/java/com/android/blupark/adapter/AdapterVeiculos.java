package com.android.blupark.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class AdapterVeiculos extends RecyclerView.Adapter<AdapterVeiculos.MyViewHolder>{

    public AdapterVeiculos(List<Veiculo> lista) {
        UsuarioHelper.veiculos = (ArrayList<Veiculo>) lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View veiculosLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_veiculos, parent, false);
        return new MyViewHolder(veiculosLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.placa.setText(UsuarioHelper.veiculos.get(position).getPlaca());
        holder.modelo.setText(UsuarioHelper.veiculos.get(position).getModelo());
        holder.tipo.setText(UsuarioHelper.veiculos.get(position).getTipo());
    }

    @Override
    public int getItemCount() {
        return UsuarioHelper.veiculos.size();
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
