package com.android.blupark.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Veiculo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class DashBoardActivity extends AppCompatActivity {
    private LinearLayout layoutCarros;
    private ArrayList<Veiculo> veiculos = new ArrayList<>();
    private DatabaseReference veiculosRef = FirebaseDatabase.getInstance().getReference("veiculos");
    private DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private ValueEventListener valueEventListenerVeiculos;

    private static final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer mCountDownTimer;
    private boolean timerIsRunning;
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


    }



    @Override
    protected void onPause() {
        super.onPause();
        /*SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft",mTimeLeftMillis);
        editor.putBoolean("timerRunning", timerIsRunning);
        editor.putLong("endTime",mEndTime);

        editor.apply();
        mCountDownTimer.cancel();*/
    }


    @Override
    protected void onStart() {
        super.onStart();
        GetVeiculos();
        GetTickets();
        ticketsLayout.setVisibility(View.INVISIBLE);
        isTicketActive();
       /* SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        timerIsRunning = prefs.getBoolean("timerRunning", false);
        isTicketActive();

        if(timerIsRunning){
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftMillis = mEndTime - System.currentTimeMillis();

            if(mTimeLeftMillis < 0){
                mTimeLeftMillis = 0;
                timerIsRunning = false;
                updateTempoTicket();

            }else{
                startTimer();
            }
        }*/
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
        /*SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft",mTimeLeftMillis);
        editor.putBoolean("timerRunning", timerIsRunning);
        editor.putLong("endTime",mEndTime);

        editor.apply();
        mCountDownTimer.cancel();*/

    }

    //Pega os veiculos que o usuario tem cadastrado
    public void GetVeiculos(){
        String usuarioId;
        usuarioId = UsuarioHelper.getIDUsuarioAtual();


       /* veiculosRef.child(usuarioId).addValueEventListener(new ValueEventListener() {
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
        });*/

    }

    //Atualiza a quantidade de tickets disponíveis do usuario
    public void GetTickets(){

       /* usuarioRef.child(UsuarioHelper.getIDUsuarioAtual()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                qtdTickets.setText("Tickets disponíveis: "+ usuario.getQtdTickets());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });*/

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
                timerIsRunning = false;
                UsuarioHelper.isTicketAtivo = false;
                isTicketActive();
            }
        }.start();
        timerIsRunning = true;
    }


    private void pauseTimer(){
        mCountDownTimer.cancel();
        timerIsRunning = false;

    }

    private void resetTimer(){
        timerIsRunning = false;
        mTimeLeftMillis = START_TIME_IN_MILLIS;
        updateTempoTicket();
        UsuarioHelper.isTicketAtivo = false;



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
          //  resetTimer();
        }
    }

    private void updateTicketComponents(){
        textModelo.setText(UsuarioHelper.veiculo.getModelo());
        textPlaca.setText(UsuarioHelper.veiculo.getPlaca());

    }
}
