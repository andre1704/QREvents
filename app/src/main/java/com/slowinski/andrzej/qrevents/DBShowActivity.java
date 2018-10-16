package com.slowinski.andrzej.qrevents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DBShowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_listview);
        String pom;
        ListView list;
        ArrayAdapter<String> adapter;
        DBHandler db = new DBHandler(this);
        ArrayList<String> ticketsList = new ArrayList<String>();
        List<QRCode> listQR = db.getAllQRCodes();
        for (QRCode item : listQR) {
            pom = item.getCode().toString() + "  " + item.getStatusCheck();
            ticketsList.add(pom);
        }
        list = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this, R.layout.single_row_item, ticketsList);
        list.setAdapter(adapter);
    }

}
