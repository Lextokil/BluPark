package com.android.blupark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.blupark.R;
import com.android.blupark.model.Veiculo;

import java.util.ArrayList;

public class VeiculoRowAdapater extends ArrayAdapter<VeiculoRow> {
    public VeiculoRowAdapater(Context context, ArrayList<VeiculoRow> veiculoList){
        super(context, 0, veiculoList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.veiculos_spinner_row, parent , false);
        }
        ImageView imageIcon = convertView.findViewById(R.id.image_view_icon);
        TextView textRow = convertView.findViewById(R.id.text_row);

        VeiculoRow currentRow = getItem(position);
        if (currentRow != null) {
            imageIcon.setImageResource(currentRow.getIcon());
            textRow.setText(currentRow.getRowText());
        }

        return  convertView;
    }
}
