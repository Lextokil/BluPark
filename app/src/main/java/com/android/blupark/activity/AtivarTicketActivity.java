package com.android.blupark.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Veiculo;

public class AtivarTicketActivity extends AppCompatActivity {



    private Button btnAtivarTicket;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativar_ticket);
        spinner = findViewById(R.id.spinnerVeiculos);


        ArrayAdapter<Veiculo> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,UsuarioHelper.veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.clearDisappearingChildren();
        spinner.setAdapter(adapter);



        btnAtivarTicket = findViewById(R.id.btnAtivarTicket);
        btnAtivarTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               activateTicket();
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










    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
