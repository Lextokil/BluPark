package com.android.blupark.helper;

import com.android.blupark.R;

public class VeiculoHelper {

    public static  int GetIconTipe (String tipoVeiculo){
        int iconReturn = 0;
        if (tipoVeiculo.equalsIgnoreCase("MOTO")){
            iconReturn = R.drawable.iconmoto;

        }else if(tipoVeiculo.equalsIgnoreCase("CARRO")){
            iconReturn = R.drawable.iconcarro;
        }else if(tipoVeiculo.equalsIgnoreCase("ONIBUS")) {
            iconReturn = R.drawable.iconbus;
        }else{
            iconReturn = R.drawable.iconcaminhao;
        }
        return iconReturn;
    }


}
