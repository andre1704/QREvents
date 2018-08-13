package com.slowinski.andrzej.qrevents;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.zxing.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static String code;
    private ListView list ;
    private ArrayAdapter<String> adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler db = new DBHandler(this);
        db.deleteAllQRCode();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }



    public void qrScanner(View view){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />
    }
    public void addToDatabase(View view){
        DBHandler db = new DBHandler(this);
        qrScanner(view);
        db.addQRCode(new QRCode(code, 0));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Action result");
        builder.setMessage("Try add to the database");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
    public void showDatabase(View view){
        DBHandler db = new DBHandler(this);
        ArrayList<String> ticketsList = new ArrayList<String>();
        List<QRCode> listQR = db.getAllQRCodes();
        for (QRCode item:listQR) {
            ticketsList.add(item.getCode().toString());
        }

        setContentView(R.layout.qrcode_listview);
        list = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this, R.layout.single_row_item, ticketsList);
        list.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPause() {
            super.onPause();
            mScannerView.stopCamera();   // Stop camera on pause<br />
    }
    @Override
    public void handleResult(Result rawResult) {
        code = rawResult.getBarcodeFormat().toString();
        Log.e("handler", rawResult.getText());
        Log.e("handler", rawResult.getBarcodeFormat().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
        mScannerView.stopCameraPreview();
        mScannerView.stopCamera();
        setContentView(R.layout.activity_main);

    }
}