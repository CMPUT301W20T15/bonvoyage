package com.example.bonvoyage;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RiderPaymentTest {

    @Test
    public void testQRGenerator() {

        RiderPaymentFragment payment = new RiderPaymentFragment();
        QRGenerate generate = new QRGenerate();
        generate.setAmount("27.0");

        generate.generateQR();

        assertTrue(generate.qrCode != null);
    }
}
