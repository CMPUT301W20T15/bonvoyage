/**
 * Information on how to generate QR code from the following website:
 * https://www.c-sharpcorner.com/article/how-to-generate-qr-code-in-android/
 * The above article leads to the following github page:
 * https://github.com/androidmads/QRGenerator
 */
package com.example.bonvoyage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.ContentValues.TAG;
import static android.content.Context.WINDOW_SERVICE;

public class RiderPayment extends DialogFragment {

    ImageView qrCode;
    String amount;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.payment_fragment_layout, null);

        qrCode = view.findViewById(R.id.QR_Code);

        generateQR();    // calls the generator to generate the QR code

        // for the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Your ride is complete!")
                .setMessage("Jane Doe" + "will scan your QR code below to process your payment of $" + "27.00").create();

    }

    // this generates the QR code
    public void generateQR() {

        WindowManager manager = (WindowManager) getChildFragmentManager();   // error due to not using AppCompatAbility
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimention = width < height ? width : height;
        smallerDimention = smallerDimention * 3 / 4;

        // Generates the QR code
        qrgEncoder = new QRGEncoder(amount, null, QRGContents.Type.TEXT, smallerDimention);

        // Created and encodes to bitmap format
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

    }

    // Sets the amount that should be converted to QR code format
    public void setAmount(String val) {
        amount = val;
    }
}
