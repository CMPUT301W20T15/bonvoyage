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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.HashMap;

import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class RiderPaymentFragment extends DialogFragment{

    private FirebaseHandler firebaseHandler;
    private FirebaseFirestore db;
    private RiderPaymentListener riderPaymentListener;
    private HashMap requstInfo;



    /**
     * sets the rider payment fragment
     * @param riderPaymentListener
     */
    public RiderPaymentFragment(RiderPaymentListener riderPaymentListener){
        this.riderPaymentListener = riderPaymentListener;

    }

    /**
     * Generates the fragment for the qrcode and call the qr code generator
     * @param savedInstanceState
     * @return                      returns the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.payment_fragment_layout, null);
        ImageView QRCode = view.findViewById(R.id.QR_Code);
        firebaseHandler = new FirebaseHandler();
        FirebaseUser user = firebaseHandler.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        this.requstInfo = (HashMap) bundle.getSerializable("HashMap");

        String driverName = requstInfo.get("driver_firstName").toString();
        String cost = requstInfo.get("cost").toString();


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
                .setMessage(driverName + "will scan your QR code below to process your payment of $" + cost)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("HashMap", requstInfo);
                        riderPaymentListener.onPaymentComplete(bundle);
                        dismiss();
                    }
                }).create();

    }

}
