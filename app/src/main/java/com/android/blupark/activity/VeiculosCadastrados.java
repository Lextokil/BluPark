package com.android.blupark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.android.blupark.R;
import com.android.blupark.adapter.AdapterVeiculos;
import com.android.blupark.helper.UsuarioHelper;

public class VeiculosCadastrados extends AppCompatActivity {

    private RecyclerView recyclerVeiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculos_cadastrados);

        recyclerVeiculos.findViewById(R.id.recyclerteste);
        recyclerVeiculos.findViewById(R.id.recyclerVeiculos);

        //Config Adapter
        AdapterVeiculos adapter = new AdapterVeiculos( UsuarioHelper.veiculos );

        //Config RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerVeiculos.setLayoutManager(layoutManager);
        recyclerVeiculos.setAdapter( adapter );

        //Otimiza o Recycler fixando o tamanho
        recyclerVeiculos.setHasFixedSize(true);

    }
}



















