package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD

<<<<<<< HEAD
<<<<<<< HEAD
import android.os.Bundle;
=======
import com.example.bonvoyage.fragments.RiderStatusDialog;
>>>>>>> added fields to RiderStatusDialog
=======
import com.example.bonvoyage.fragments.RiderStatusDialog;
>>>>>>> added fields to RiderStatusDialog
=======
import android.os.Bundle;
import com.example.bonvoyage.fragments.RiderStatusDialog;
>>>>>>> 75b042654b04c976050ab085e5f0bd4fcc6dc4be

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> 75b042654b04c976050ab085e5f0bd4fcc6dc4be

        RiderStatusDialog dialog = new RiderStatusDialog();
        dialog.show(getSupportFragmentManager(), "dialog");

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> added fields to RiderStatusDialog
=======
        RiderStatusDialog dialog = new RiderStatusDialog();
        dialog.show(getSupportFragmentManager(), "dialog");

>>>>>>> added fields to RiderStatusDialog
=======
>>>>>>> 75b042654b04c976050ab085e5f0bd4fcc6dc4be
    }
}
