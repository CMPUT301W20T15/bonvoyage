/**
 * instructions for the code from
 * https://www.journaldev.com/18198/qr-code-barcode-scanner-android
 */
package com.example.bonvoyage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class DriverPayment extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView barcodeVal;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private RiderPaymentListener paymentListener;
    Button doneButton;
    Button skipButton;
    String intentData = "";

    /**
     * creates the qr code scanner and does the scanning
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        barcodeVal = findViewById(R.id.barcode_value);
        surfaceView = findViewById(R.id.surfaceView);
        doneButton = findViewById(R.id.done_button);
        skipButton = findViewById(R.id.skip_button);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentData.length() > 0) {
                    startActivity(new Intent(DriverPayment.this, DriverPostPayment.class));  // calls post payment to handle the paymet updates for the driver
                    startActivity(new Intent(DriverPayment.this, RiderPostPayment.class));  // Sets Rider side rating and update payment
                } else {
                    Toast.makeText(getApplicationContext(), "No payment found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Going back to home. Payment not processed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverPayment.this, DriverMapActivity.class));  // goes back to driver home without payment being processed
            }
        });
        
    }

    /**
     * Creates the qr code detector and does the scanning
     */
    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) // enables auto focus for the camera
                .build();

        // gets the permissions for using the camera functions
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(DriverPayment.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(DriverPayment.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        cameraSource.start(surfaceView.getHolder());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    // sets the button text val after finding a barcode
                    barcodeVal.post(new Runnable() {
                        @Override
                        public void run() {
                            barcodeVal.removeCallbacks(null);
                            intentData = barcodes.valueAt(0).displayValue;
                            barcodeVal.setText(intentData);
                            doneButton.setText("ADD TO WALLET");
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
