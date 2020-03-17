package com.example.orderko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "order.db";
    public static final String TABLE_NAME = "order_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DRINK";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "BULK";
    public static final String COL_5 = "QUANTITY";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, DRINK TEXT,CATEGORY TEXT," +
                "BULK INTEGER,QUANTITY INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String drink, String category, String bulk, String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, drink);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, bulk);
        contentValues.put(COL_5, quantity);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
             return false;
        }
        else {
             return true;
         }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}
