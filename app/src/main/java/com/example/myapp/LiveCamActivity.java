package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LiveCamActivity extends AppCompatActivity {
    private EditText ipAddressInput;
    private Button startStreamButton;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_cam);

        ipAddressInput = findViewById(R.id.ipAddressInput);
        startStreamButton = findViewById(R.id.startStreamButton);
        webView = findViewById(R.id.webView);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // Set a custom WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        startStreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveStream();
            }
        });
    }

    private void startLiveStream() {
        String ip = ipAddressInput.getText().toString();
        if (!ip.isEmpty()) {
            String url = "http://" + ip + ":81/stream" ;
            webView.loadUrl(url);
        } else {
            Toast.makeText(LiveCamActivity.this, "Please enter an IP address", Toast.LENGTH_SHORT).show();
        }
    }
}
