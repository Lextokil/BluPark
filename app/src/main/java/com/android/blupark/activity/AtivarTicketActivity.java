package com.android.blupark.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.blupark.R;
import com.android.blupark.adapter.VeiculoRow;
import com.android.blupark.adapter.VeiculoRowAdapater;
import com.android.blupark.helper.Permissoes;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.helper.VeiculoHelper;
import com.android.blupark.model.Ticket;
import com.android.blupark.model.Veiculo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AtivarTicketActivity extends AppCompatActivity {


    private Button btnAtivarTicket;
    private Spinner spinner;
    private ArrayList<VeiculoRow> mVeiculosList;
    private VeiculoRowAdapater mAdapter;
    private AlertDialog alerta;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng meulocal;

    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativar_ticket);
        spinner = findViewById(R.id.spinnerVeiculos);
        initlist();

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);

        mAdapter = new VeiculoRowAdapater(this, mVeiculosList);
        spinner.setAdapter(mAdapter);

        //Pegar a localização do usuario e armazenar
         getLocalizacao();


        btnAtivarTicket = findViewById(R.id.btnAtivarTicket);
        btnAtivarTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AtivarTicketActivity.this);
                builder.setTitle("Cofirmação de ticket");
                builder.setMessage("Deseja Ativar o ticket?");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Thread myThread = new Thread() {
                            @Override
                            public void run() {
                               do {try {
                                   sleep(1000);
                               }catch (Exception e){
                                   Log.e("",e.getMessage());
                               }

                               }while (UsuarioHelper.latitude == 0 && UsuarioHelper.longitute == 0);

                                    decreaseTicketByOne();

                            }

                        };
                        myThread.start();
                        UsuarioHelper.toLoadingTicketToDashboard(AtivarTicketActivity.this);


                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AtivarTicketActivity.this, "Ativação Cancelada!", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta = builder.create();
                alerta = builder.show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    public void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para ativar o ticket é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();

    }

    private void initlist() {
        mVeiculosList = new ArrayList<>();
        for (Veiculo veiculo : UsuarioHelper.veiculos) {
            String rowText = veiculo.getPlaca().toUpperCase() + " - " + veiculo.getModelo().toUpperCase();
            int iconVeiculo = VeiculoHelper.GetIconTipe(veiculo.getTipo());

            VeiculoRow veiculoRow = new VeiculoRow(rowText, iconVeiculo);
            mVeiculosList.add(veiculoRow);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void decreaseTicketByOne() {
        final DatabaseReference ticketsRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(UsuarioHelper.getIDUsuarioAtual()).child("qtdTickets");


        ticketsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int total = dataSnapshot.getValue(int.class);
                total -= 1;
                if (total >= 0) {
                    ticketsRef.setValue(total);
                    Toast.makeText(AtivarTicketActivity.this,
                            "Ticket ativado com sucesso!",
                            Toast.LENGTH_LONG).show();
                    activateTicket();


                } else {
                    Toast.makeText(AtivarTicketActivity.this,
                            "Tickets Insuficientes!",
                            Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void activateTicket() {



        UsuarioHelper.isTicketAtivo = true;
        int index = spinner.getSelectedItemPosition();
        long endtime;

        UsuarioHelper.veiculo = UsuarioHelper.veiculos.get(index);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putLong("millisLeft", 300000);
        editor.putBoolean("timerRunning", UsuarioHelper.isTicketAtivo);
        endtime = System.currentTimeMillis() + 300000;
        Log.i("endtime", "Endtime : "+ endtime);
        editor.putLong("endTime", (endtime));
        editor.putInt("index", index);
        editor.apply();

        //Salvar os dados no banco
        postTicket((UsuarioHelper.veiculo.getModelo() + " - " + UsuarioHelper.veiculo.getPlaca()), endtime);
        UsuarioHelper.latitude = 0;
        UsuarioHelper.longitute = 0;
        UsuarioHelper.toDashBoardActivity(AtivarTicketActivity.this);



    }

    public void postTicket(String veiculo, Long endtime) {
        Ticket ticketAtivo = new Ticket();
        ticketAtivo.setVeiculo(veiculo);
        ticketAtivo.setLatitude(meulocal.latitude);
        ticketAtivo.setLongitute(meulocal.longitude);
        ticketAtivo.setEndTicketTime(endtime);
        ticketAtivo.salvarTicket();
    }

    public void getLocalizacao() {


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                UsuarioHelper.latitude = location.getLatitude();
                UsuarioHelper.longitute = location.getLongitude();
                meulocal = new LatLng(UsuarioHelper.latitude, UsuarioHelper.longitute);

            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    locationListener
            );

        }


    }


}




