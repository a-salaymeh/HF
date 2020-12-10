package com.example.hf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "hfDatabase.db";
    private static final int VERSION_NUM = 2;
    public static final String TABLE_NAME = "tableOfMsg";
    private static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";
    private static final String DATABASE_CREATE = "create table "
            +TABLE_NAME + "(" + KEY_ID
            + " integer primary key autoincrement, " + KEY_MESSAGE
            + " text not null);";
    public dataBaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db ) {
        db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper ", "Calling onCreate ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer , int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion = " + oldVer  + "newVersion= " + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}
