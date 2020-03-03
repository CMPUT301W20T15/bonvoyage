package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
<<<<<<< HEAD
import android.os.Bundle;
=======
import com.example.bonvoyage.fragments.RiderStatusDialog;
>>>>>>> added fields to RiderStatusDialog
=======
import com.example.bonvoyage.fragments.RiderStatusDialog;
>>>>>>> added fields to RiderStatusDialog

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
=======

        RiderStatusDialog dialog = new RiderStatusDialog();
        dialog.show(getSupportFragmentManager(), "dialog");

<<<<<<< HEAD
>>>>>>> added fields to RiderStatusDialog
=======
        RiderStatusDialog dialog = new RiderStatusDialog();
        dialog.show(getSupportFragmentManager(), "dialog");

>>>>>>> added fields to RiderStatusDialog
    }
}
