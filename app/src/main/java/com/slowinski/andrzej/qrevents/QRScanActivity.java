package com.slowinski.andrzej.qrevents;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanactivity);
        qrScanner();


    }
    public void qrScanner(){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        String resault = rawResult.getText();
        alert1.show();
        Bundle extra = getIntent().getExtras();
        String responseDB = extra.getString("adddatabase");
        String valid = extra.getString("validate");
        boolean isInBase = false;
        QRCode ticket = null;
        String message = resault;
        DBHandler db = new DBHandler(this);
        List<QRCode> listQR = db.getAllQRCodes();
        for (QRCode item : listQR) {
            if (item.getCode().equals(resault)){
                isInBase = true;
                ticket = item;
            }
        }
        if (responseDB != null){
            if(!isInBase){
              db.addQRCode(new QRCode(resault,0));
              message = "dodane do bazy";
            }
            else
              message = "było już w bazie";

        }
        if (valid != null){
            if(ticket != null){
              if(ticket.getStatusCheck() < 1){
                ticket.setStatusCheck(1);
                db.updateQRCode(ticket);
                message = "bilet ok";
              }
                else{
                  message = "bilet się już raz pojawił";

                }
            }
            else
              message = "nie mogłem odczytać biletu";
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("code", message);
        startActivity(intent);
    }
}
