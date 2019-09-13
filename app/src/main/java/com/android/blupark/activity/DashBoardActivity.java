package com.android.blupark.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Usuario;
import com.android.blupark.model.Veiculo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class DashBoardActivity extends AppCompatActivity {
    private LinearLayout layoutCarros;

    private DatabaseReference veiculosRef = FirebaseDatabase.getInstance().getReference("veiculos");
    private DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private ValueEventListener valueEventListenerVeiculos;

    private static final long START_TIME_IN_MILLIS = 60000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    private TextView qtdTickets, textPlaca, textModelo, textTimer;
    private LinearLayout ticketsLayout;
    private Button btnFinalizar, btnAtivarTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        qtdTickets = findViewById(R.id.textTicketsDisponiveis);
        textPlaca = findViewById(R.id.textPlaca);
        textModelo = findViewById(R.id.textModelo);
        textTimer = findViewById(R.id.textTimer);
        ticketsLayout = findViewById(R.id.ticketLayout);

        btnAtivarTicket = findViewById(R.id.btnAtivarTicket);
        btnAtivarTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioHelper.toAtivarTicketsActivity(DashBoardActivity.this);
            }
        });



        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();

                isTicketActive();
            }
        });

        ticketsLayout.setVisibility(View.INVISIBLE);
        isTicketActive();

    }



    @Override
    protected void onStart() {
        super.onStart();
        GetVeiculos();

        GetTickets();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        UsuarioHelper.isTicketAtivo = mTimerRunning;
        if(mTimerRunning){
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftMillis = mEndTime - System.currentTimeMillis();
            if(mTimeLeftMillis < 0){
                mTimeLeftMillis = 0;
                mTimerRunning = false;
                UsuarioHelper.isTicketAtivo = mTimerRunning;
                updateTempoTicket();

            }else{
                startTimer();
            }

        }
        isTicketActive();
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
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putLong("millisLeft",mTimeLeftMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime",mEndTime);

        editor.apply();


    }

    //Pega os veiculos que o usuario tem cadastrado
    public void GetVeiculos(){
        String usuarioId;
      /*  usuarioId = UsuarioHelper.getIDUsuarioAtual();


        veiculosRef.child(usuarioId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Log.i("dados", "Retorno "+ dados.toString());
                    Veiculo veiculo = dados.getValue(Veiculo.class);
                    UsuarioHelper.veiculos.add(veiculo);
                }
                Log.i("Veiculos", "1 Index Array : "+ UsuarioHelper.veiculos.get(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    //Atualiza a quantidade de tickets disponíveis do usuario
    public void GetTickets(){

         usuarioRef.child(UsuarioHelper.getIDUsuarioAtual()).addValueEventListener(new ValueEventListener() {
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
        UsuarioHelper.toCompraTicketsActivity(this);
    }

    public void ToCadastroVeiculos(View view){
        UsuarioHelper.toCadastroVeiculoActivity(this);
    }

    private void startTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updateTempoTicket();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                UsuarioHelper.isTicketAtivo = mTimerRunning;
                isTicketActive();
            }
        }.start();
        mTimerRunning = true;

    }


    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }

    private void resetTimer(){
        mTimerRunning = false;
        mTimeLeftMillis = START_TIME_IN_MILLIS;
        UsuarioHelper.isTicketAtivo = mTimerRunning;
        updateTempoTicket();




    }

    private void updateTempoTicket(){
        int minutos = (int) (mTimeLeftMillis / 1000) /60;
        int segundos = (int) (mTimeLeftMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutos,segundos);
        textTimer.setText(timeLeftFormatted);
    }

    private void  isTicketActive(){
        if (UsuarioHelper.isTicketAtivo){
            ticketsLayout.setVisibility(View.VISIBLE);
            updateTicketComponents();
            startTimer();
        }else{
            ticketsLayout.setVisibility(View.INVISIBLE);

        }
    }

    private void updateTicketComponents(){
       // textModelo.setText(UsuarioHelper.veiculo.getModelo());
       // textPlaca.setText(UsuarioHelper.veiculo.getPlaca());

    }
}
