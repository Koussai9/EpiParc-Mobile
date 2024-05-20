package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnWhereIsMyCar;
    private Button btnWhoTouchedCar;
    private Button btnWhosTheDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWhereIsMyCar = findViewById(R.id.btnWhereIsMyCar);
        btnWhereIsMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                startActivity(intent);
            }
        });

        btnWhoTouchedCar = findViewById(R.id.btnWhoTouchedCar);
        btnWhoTouchedCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        // Initialize and set the OnClickListener for the "Who's the Driver?" button
        btnWhosTheDriver = findViewById(R.id.btnWhosTheDriver);
        btnWhosTheDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LiveCamActivity.class);
                startActivity(intent);
            }
        });
    }
}
