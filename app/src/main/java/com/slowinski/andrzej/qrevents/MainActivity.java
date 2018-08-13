package com.slowinski.andrzej.qrevents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_QR_ACTIVITY = 234;

    private String code;
    private ListView list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler db = new DBHandler(this);
        db.deleteAllQRCode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_QR_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String resultFormat = data.getStringExtra(QrCodeActivity.BUNDLE_QR_RESULT_FORMAT);
                String resultText = data.getStringExtra(QrCodeActivity.BUNDLE_QR_RESULT_TEXT);

                onResultsHandled(resultFormat, resultText);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void qrScanner(View view) {
        Intent intent = new Intent(this, QrCodeActivity.class);
        startActivityForResult(intent, REQ_CODE_QR_ACTIVITY);
    }

    public void addToDatabase(View view) {
        DBHandler db = new DBHandler(this);
        qrScanner(view);
        db.addQRCode(new QRCode(code, 0));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Action result");
        builder.setMessage("Try add to the database");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public void showDatabase(View view) {
        DBHandler db = new DBHandler(this);
        ArrayList<String> ticketsList = new ArrayList<>();
        List<QRCode> listQR = db.getAllQRCodes();
        for (QRCode item : listQR) {
            ticketsList.add(item.getCode());
        }

        setContentView(R.layout.qrcode_listview);
        list = findViewById(R.id.listView1);
        adapter = new ArrayAdapter<>(this, R.layout.single_row_item, ticketsList);
        list.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResultsHandled(String resultFormat, String resultText) {
        code = resultFormat;
        Log.e("handler", resultText);
        Log.e("handler", resultFormat);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(resultText);
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}