package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GpsActivity extends AppCompatActivity {

    private TextView latitudeTextView, longitudeTextView;
    private Button showMapButton;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        showMapButton = findViewById(R.id.showMapButton);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch latitude
        databaseReference.child("LAT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    latitude = dataSnapshot.getValue(Double.class);
                    latitudeTextView.setText(latitude != null ? latitude.toString() : "N/A");
                    Log.d("GpsActivity", "Latitude fetched: " + latitude);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("GpsActivity", "Error fetching latitude", databaseError.toException());
            }
        });

        // Fetch longitude
        databaseReference.child("LNG").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    longitude = dataSnapshot.getValue(Double.class);
                    longitudeTextView.setText(longitude != null ? longitude.toString() : "N/A");
                    Log.d("GpsActivity", "Longitude fetched: " + longitude);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("GpsActivity", "Error fetching longitude", databaseError.toException());
            }
        });

        // Set up the button click listener
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != null && longitude != null) {
                    // Use a geo URI with a query parameter (q) to drop a pin at the exact coordinates
                    String geoUriString = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(My+Location)"; // 'My Location' is the label for the pin
                    Uri gmmIntentUri = Uri.parse(geoUriString);

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps"); // Ensure Google Maps app handles the intent

                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(GpsActivity.this, "No application available to open map", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(GpsActivity.this, "Latitude or Longitude is null", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
