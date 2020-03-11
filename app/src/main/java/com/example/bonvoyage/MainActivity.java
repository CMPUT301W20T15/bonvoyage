package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnScanBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnScanBarcode = findViewById(R.id.scan_barcode);
        btnScanBarcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        new RiderPaymentFragment().show(getSupportFragmentManager(), "PAY");

    }
}
