/**
 * Information on how to generate QR code from the following website:
 * https://www.c-sharpcorner.com/article/how-to-generate-qr-code-in-android/
 * The above article leads to the following github page:
 * https://github.com/androidmads/QRGenerator
 */
package com.example.bonvoyage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class RiderPaymentFragment extends DialogFragment{

    private FirebaseHandler firebaseHandler;
    private FirebaseFirestore db;
    private RiderPaymentListener riderPaymentListener;

    Rider rider;
    float cost = 10;

    public RiderPaymentFragment(RiderPaymentListener riderPaymentListener){
        this.riderPaymentListener = riderPaymentListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.payment_fragment_layout, null);
        ImageView QRCode = view.findViewById(R.id.QR_Code);
        firebaseHandler = new FirebaseHandler();
        FirebaseUser fb_rider = firebaseHandler.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        
        DocumentReference docRef = db.collection("riders").document(fb_rider.getEmail());


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGenerate gen = new QRGenerate();
        gen.generateQR(String.valueOf(cost), smallerDimension);
        Bitmap bitmap = gen.getBitmap();
        QRCode.setImageBitmap(bitmap);

        // for the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alert = builder.create();
        Button doneBtn = alert.getButton(AlertDialog.BUTTON_POSITIVE);

        return builder
                .setView(view)
                .setTitle("Your ride is complete!")
                .setMessage(rider + "will scan your QR code below to process your payment of $" + cost)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        riderPaymentListener.onPaymentComplete();
                    }
                }).create();

    }

}
