package com.slowinski.andrzej.qrevents;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extra = getIntent().getExtras();
        code = null;
        if (extra != null){
            this.code = extra.getString("code");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Scan Result");
            builder.setMessage(this.code);
            AlertDialog alert1 = builder.create();
            alert1.show();

        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void scanQR(View view){
      Intent intent = new Intent(this, QRScanActivity.class);
      startActivity(intent);
    }

    public void databaseAdd(View view){
      Intent intent = new Intent(this, QRScanActivity.class);
      intent.putExtra("adddatabase","add" );
      startActivity(intent);
    }
    public void showDatabase(View view) {
        Intent intent = new Intent(this, DBShowActivity.class);
        startActivity(intent);
    }

    public void validate(View view){
        Intent intent = new Intent(this,QRScanActivity.class);
        intent.putExtra("validate","valid" );
        startActivity(intent);
    }
}
