package com.android.blupark.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.blupark.R;

public class LoadingTicketToDashboard extends AppCompatActivity {

    static AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_ticket_to_dashboard);

        //Loading Screen
        /*Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                    sleep(5000);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };*/
        Thread loadingThread = new Thread() {
            @Override
            public void run() {
                ImageView loading = (ImageView)findViewById(R.id.blupark_loading_png);
                animation = (AnimationDrawable)loading.getDrawable();
                animation.start();
            }
        };

        //myThread.start();
        loadingThread.start();

    }
}