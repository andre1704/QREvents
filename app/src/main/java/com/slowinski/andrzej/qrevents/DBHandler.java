package com.slowinski.andrzej.qrevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "QREventsDatabase";
    // Contacts table name
    private static final String TABLE_QRCODES = "QRCodes";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CODE = "qrcode";
    private static final String KEY_STATUS = "status";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_QRCODES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CODE + " TEXT,"
                + KEY_STATUS + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QRCODES);
        // Creating tables again
        onCreate(db);
    }
    public void addQRCode(QRCode qrCode){
        if (qrCode != null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CODE, qrCode.getCode());
            values.put(KEY_STATUS,qrCode.getStatusCheck());
            db.insert(TABLE_QRCODES, null, values);
            db.close();
        }
    }
    public QRCode getQRCode(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QRCODES, new String[]{
                KEY_ID, KEY_CODE, KEY_STATUS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null,null);
        if (cursor != null){
            cursor.moveToFirst();
            QRCode ticket = new QRCode(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)));
            return ticket;
        }
        return null;
    }

    public List<QRCode> getAllQRCodes() {
        List<QRCode> shopList = new ArrayList<QRCode>();
        String selectQuery = "SELECT * FROM " + TABLE_QRCODES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                QRCode ticket = new QRCode();
                ticket.setId(Integer.parseInt(cursor.getString(0)));
                ticket.setCode(cursor.getString(1));
                ticket.setStatusCheck(Integer.parseInt(cursor.getString(2)));
                shopList.add(ticket);
            } while (cursor.moveToNext());
        }
        return shopList;
    }
    public int getQRCodeCount() {
        String countQuery = "SELECT * FROM " + TABLE_QRCODES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    public int updateQRCode(QRCode ticket) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CODE, ticket.getCode());
        values.put(KEY_STATUS, ticket.getStatusCheck());
// updating row
        return db.update(TABLE_QRCODES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(ticket.getId())});
    }
    public void deleteQRCode(QRCode ticket) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QRCODES, KEY_ID + " = ?",
                new String[] { String.valueOf(ticket.getId()) });
        db.close();
    }
    public void deleteAllQRCode() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QRCODES,null,null);
        db.close();
    }
}
