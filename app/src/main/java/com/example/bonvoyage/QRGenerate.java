package com.example.bonvoyage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.ContentValues.TAG;

public class QRGenerate extends AppCompatActivity {

    private ImageView qrCode;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    /**
     * this generates the QR code
     * @param amount   takes in the value to generate
     */
    public void generateQR(String amount, int dimension) {

        View view = new RiderPaymentFragment().getView();

        qrCode = (ImageView) view.findViewById(R.id.QR_Code);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimention = width < height ? width : height;
        smallerDimention = smallerDimention * 3 / 4;

        // Generates the QR code
        qrgEncoder = new QRGEncoder(amount, null, QRGContents.Type.TEXT, dimension);

        // Creates and encodes to bitmap format
        try {
            this.bitmap = qrgEncoder.encodeAsBitmap();
            //qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }


    public Bitmap getBitmap(){
        return bitmap;
    }

}
