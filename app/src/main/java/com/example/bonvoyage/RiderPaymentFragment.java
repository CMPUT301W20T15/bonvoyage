/**
 * Information on how to generate QR code from the following website:
 * https://www.c-sharpcorner.com/article/how-to-generate-qr-code-in-android/
 * The above article leads to the following github page:
 * https://github.com/androidmads/QRGenerator
 */
package com.example.bonvoyage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RiderPaymentFragment extends DialogFragment{

    String riderName = "Jane Doe";  // should be set to blank at the end
    String amount = "27.00";   // should be cleared for the end

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.payment_fragment_layout, null);

        QRGenerate gen = new QRGenerate();    // calls the generator to generate the QR code
        gen.generateQR(amount);

        // for the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Your ride is complete!")
                .setMessage(riderName + "will scan your QR code below to process your payment of $" + amount).create();

    }

    // Sets the amount that should be converted to QR code format
    public void setAmount(String val) {
        amount = val;
    }

    // to set the name
    public void setName(String name) {
        riderName = name;
    }
}
