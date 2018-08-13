package com.slowinski.andrzej.qrevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QRCodeDBAdapter {


    private static final String DEBUG_TAG = "SqLiteQRCodeManager";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_QRCODES_TABLE = "qrcodes";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_QRCODES = "description";
    public static final String QRCODES_OPTIONS = "TEXT NOT NULL";
    public static final int QRCODES_COLUMN = 1;
    public static final String KEY_STATUS = "completed";
    public static final String STATUS_OPTIONS = "INTEGER DEFAULT 0";
    public static final int STATUS_COLUMN = 2;

    private static final String DB_CREATE_TODO_TABLE =
            "CREATE TABLE " + DB_QRCODES_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_QRCODES + " " + QRCODES_OPTIONS + ", " +
                    KEY_STATUS + " " + STATUS_OPTIONS +
                    ");";
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + QRCODES_OPTIONS;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_QRCODES_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_QRCODES_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public QRCodeDBAdapter(Context context) {
        this.context = context;
    }

    public QRCodeDBAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public int inserQRCode(String ticket) {
        ContentValues newTicket = new ContentValues();
        newTicket.put(KEY_QRCODES, ticket);
        return (int) db.insert(DB_QRCODES_TABLE, null, newTicket);
    }

    public boolean updateTodo(QRCode ticket) {
        int id = ticket.getId();
        String code = ticket.getCode();
        int completed = ticket.getStatusCheck();
        return updateTodo(id, code, completed);
    }

    public boolean updateTodo(int id, String code, int completed) {
        String where = KEY_ID + "=" + id;
        ContentValues updateTodoValues = new ContentValues();
        updateTodoValues.put(KEY_QRCODES, code);
        updateTodoValues.put(KEY_STATUS, completed);
        return db.update(DB_QRCODES_TABLE, updateTodoValues, where, null) > 0;
    }

    public boolean deleteTodo(int id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_QRCODES_TABLE, where, null) > 0;
    }

    public Cursor getAllTodos() {
        String[] columns = {KEY_ID, KEY_QRCODES, KEY_STATUS};
        return db.query(DB_QRCODES_TABLE, columns, null, null, null, null, null);
    }

    public QRCode getTicket(int id) {
        String[] columns = {KEY_ID, KEY_QRCODES, KEY_STATUS};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_QRCODES_TABLE, columns, where, null, null, null, null);
        QRCode ticket = null;
        if(cursor != null && cursor.moveToFirst()) {
            String code = cursor.getString(QRCODES_COLUMN);
            int completed = cursor.getInt(STATUS_COLUMN);
            ticket = new QRCode(id, code, completed);
        }
        return ticket;
    }
}

