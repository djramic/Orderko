package com.example.orderko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USER_ID";
    public static final String COL_3 = "USER_BILL";
    public static final String COL_4 = "TABLE_NUMB";
    public static final String COL_5 = "USER_LAST_BILL";
    public static final String COL_6 = "CLUB";
    public static final String COL_7 = "TABLE_BILL";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,USER_ID TEXT,USER_BILL TEXT," +
                "TABLE_NUMB TEXT,USER_LAST_BILL TEXT,CLUB TEXT, TABLE_BILL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String user_id, String user_bill, String table, String last_bill, String club, String table_bill){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, user_id);
        contentValues.put(COL_3, user_bill);
        contentValues.put(COL_4, table);
        contentValues.put(COL_5, last_bill);
        contentValues.put(COL_6, club);
        contentValues.put(COL_7, table_bill);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public boolean updateBill(String bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_3,bill);
        long res = db.update(TABLE_NAME, cv, "USER_ID='0'", null);
        if(res == -1) {
            return false;
        }else {
            return true;
        }
    }

    public boolean updateLastBill(String bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_5,bill);
        long res = db.update(TABLE_NAME, cv, "USER_ID='0'", null);
        if(res == -1) {
            return false;
        }else {
            return true;
        }
    }
}
