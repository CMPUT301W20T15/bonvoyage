package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bonvoyage.fragments.RiderStatusDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RiderStatusDialog dialog = new RiderStatusDialog();
        dialog.show(getSupportFragmentManager(), "dialog");

    }
}
