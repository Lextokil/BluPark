package com.android.blupark.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.helper.VeiculoHelper;
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
    private Button btnFinalizar, btnAtivarTicket, btnMaps;
    private ImageView iconVeiculo;
    private int indexVeiculo;
    private AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        qtdTickets = findViewById(R.id.textTicketsDisponiveis);
        textPlaca = findViewById(R.id.textPlaca);
        textModelo = findViewById(R.id.textModelo);
        textTimer = findViewById(R.id.textTimer);
        ticketsLayout = findViewById(R.id.ticketLayout);
        iconVeiculo = findViewById(R.id.iconveiculo);

        btnAtivarTicket = findViewById(R.id.btnAtivarTicket);
        btnAtivarTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioHelper.toAtivarTicketsActivity(DashBoardActivity.this);
            }
        });

        btnMaps = findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioHelper.toMapsActivity(DashBoardActivity.this);
            }
        });


        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view1) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(view1.getRootView().getContext());
                alertbox.setTitle("Finalizar Ticket");
                alertbox.setMessage("Deseja finalizar o ticket?");
                alertbox.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view1.getRootView().getContext(), "Ticket Finalizado!", Toast.LENGTH_SHORT).show();

                        ticketsLayout.setVisibility(View.INVISIBLE);
                        isTicketActive();



                    }
                });
                alertbox.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view1.getRootView().getContext(), "Finalização cancelada!", Toast.LENGTH_SHORT).show();
                    }
                });
                resetTimer();

                alerta = alertbox.create();
                alerta = alertbox.show();


                //METODO PARA EXCLUIR O TICKET DA DATABASE
               /* String ticketDeletado = UsuarioHelper.veiculo.getModelo() + " - " + UsuarioHelper.veiculo.getPlaca();
                UsuarioHelper.deletTicket(ticketDeletado);*/


            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetVeiculos();

        GetTickets();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        indexVeiculo = prefs.getInt("index", 0);
        UsuarioHelper.isTicketAtivo = mTimerRunning;
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftMillis < 0) {
                mTimeLeftMillis = 0;
                mTimerRunning = false;
                UsuarioHelper.isTicketAtivo = mTimerRunning;
                updateTempoTicket();

            } else {
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
        editor.putLong("millisLeft", mTimeLeftMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putInt("index", indexVeiculo);

        editor.apply();


    }

    //Pega os veiculos que o usuario tem cadastrado
    public void GetVeiculos() {
        String usuarioId;
        usuarioId = UsuarioHelper.getIDUsuarioAtual();


        veiculosRef.child(usuarioId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsuarioHelper.veiculos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Log.i("dados", "Retorno " + dados.toString());
                    Veiculo veiculo = dados.getValue(Veiculo.class);
                    UsuarioHelper.veiculos.add(veiculo);
                }
                updateTicketComponents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Atualiza a quantidade de tickets disponíveis do usuario
    public void GetTickets() {

        usuarioRef.child(UsuarioHelper.getIDUsuarioAtual()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);


                qtdTickets.setText("Tickets disponíveis: " + usuario.getQtdTickets());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    //Leva para tela de Compra de tickets
    public void ToCompraTickesActivity(View view) {
        UsuarioHelper.toCompraTicketsActivity(this);
    }

    public void ToCadastroVeiculos(View view) {
        UsuarioHelper.toCadastroVeiculoActivity(this);
    }

    private void startTimer() {
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


    private void resetTimer() {
        mTimerRunning = false;
        mTimeLeftMillis = START_TIME_IN_MILLIS;
        UsuarioHelper.isTicketAtivo = mTimerRunning;
        updateTempoTicket();


    }

    private void updateTempoTicket() {
        int minutos = (int) (mTimeLeftMillis / 1000) / 60;
        int segundos = (int) (mTimeLeftMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
        textTimer.setText(timeLeftFormatted);
    }

    private void isTicketActive() {
        if (UsuarioHelper.isTicketAtivo) {
            ticketsLayout.setVisibility(View.VISIBLE);
            updateTicketComponents();
            startTimer();
        } else {
            ticketsLayout.setVisibility(View.INVISIBLE);

            //METODO PARA EXCLUIR O TICKET DA DATABASE QUANDO FINALIZAR O SEU TEMPO
            /*try {
                String ticketDeletado = UsuarioHelper.veiculo.getModelo() + " - " + UsuarioHelper.veiculo.getPlaca();
                UsuarioHelper.deletTicket(ticketDeletado);
            } catch (Exception e) {
                e.printStackTrace();
            }*/


        }
    }

    private void updateTicketComponents() {
        try {
            UsuarioHelper.veiculo = UsuarioHelper.veiculos.get(indexVeiculo);
            if (UsuarioHelper.veiculo.getModelo().length() > 10) {
                textModelo.setText(UsuarioHelper.veiculo.getModelo().substring(0, 10));
            } else {
                textModelo.setText(UsuarioHelper.veiculo.getModelo());
            }

            textPlaca.setText(UsuarioHelper.veiculo.getPlaca());
            iconVeiculo.setImageResource(VeiculoHelper.GetIconTipe(UsuarioHelper.veiculo.getTipo()));

        } catch (Exception e) {

        }

    }

    public void veiculosCadastrados(View view) {
        UsuarioHelper.toVeiculosCadastrados(this);
    }
}
