package com.android.blupark.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Veiculo;

public class AtivarTicketActivity extends AppCompatActivity {



    private Button btnAtivarTicket;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativar_ticket);

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
        Veiculo veiculoDoTicket = new Veiculo();
        veiculoDoTicket.setPlaca("TicketPlaca");
        veiculoDoTicket.setTipo("TicketTipo");
        veiculoDoTicket.setModelo("ModeloTip");
        UsuarioHelper.veiculo = veiculoDoTicket;
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
