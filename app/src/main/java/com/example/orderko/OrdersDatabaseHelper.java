package com.example.orderko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OrdersDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "orders.db";
    public static final String TABLE_NAME = "orders_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DRINK";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "BULK";
    public static final String COL_5 = "QUANTITY";
    public static final String COL_6 = "PRICE";
    public static final String COL_7 = "TABLE_NUMBER";
    public static final String COL_8 = "CLUB";
    public static final String COL_9 = "TIME_AND_DATE";


    public OrdersDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DRINK TEXT,CATEGORY TEXT," +
                "BULK INTEGER,QUANTITY TEXT,PRICE TEXT,TABLE_NUMBER TEXT,CLUB TEXT,TIME_AND_DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String drink, String category, String bulk, String quantity, String price, String table_number, String club,String time_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, drink);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, bulk);
        contentValues.put(COL_5, quantity);
        contentValues.put(COL_6, price);
        contentValues.put(COL_7, table_number);
        contentValues.put(COL_8, club);
        contentValues.put(COL_9, time_date);

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

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public Cursor getDataOfDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where TIME_AND_DATE == '" + date + "'" , null);
        return res;
    }

}
