package com.android.blupark.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.android.blupark.R;
import com.android.blupark.helper.UsuarioHelper;
import com.android.blupark.model.Ticket;
import com.android.blupark.model.Veiculo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference ticketsRef = FirebaseDatabase.getInstance().getReference("tickets");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String usuarioId;
        usuarioId = UsuarioHelper.getIDUsuarioAtual();


        ticketsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Log.i("dados", "Retorno "+ dados.toString());
                    Ticket ticket = dados.getValue(Ticket.class);

                    LatLng point = new LatLng(ticket.getLatitude(),ticket.getLongitute());
                    mMap.addMarker(
                            new MarkerOptions().
                                    position(point).
                                    title(ticket.getVeiculo()).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.blupark_pin))
                    ); mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(point, 30)
                    );

                    //VERIFICA SE O TICKET DA DATABASE JA NÃO PASSOU DO TEMPO SE JA PASSOU DELETA
                    /*if(!(ticket.getEndTicketTime() - System.currentTimeMillis() < 0)){
                        LatLng point = new LatLng(ticket.getLatitude(),ticket.getLongitute());
                        mMap.addMarker(
                                new MarkerOptions().
                                        position(point).
                                        title(ticket.getVeiculo())//.icon(BitmapDescriptorFactory.fromResource(R.drawable.bluparklogo))
                        ); mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(point, 30)
                        );
                    }else{
                        dados.getRef().removeValue();
                    }*/


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


}
