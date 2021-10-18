package com.hyun.loldb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LolDB extends SQLiteOpenHelper {
    public LolDB(Context context ) {
        super(context, "lolDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS groupTbl " +
                "(lNo INTEGER PRIMARY KEY, lName TEXT, lPrice INTEGER, lGrade TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groupTbl");
        onCreate(db);

    }
}
