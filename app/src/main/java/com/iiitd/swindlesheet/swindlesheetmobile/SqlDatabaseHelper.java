package com.iiitd.swindlesheet.swindlesheetmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by mayur on 1/10/16.
 * Database helper class for expenditure database
 */
public class SqlDatabaseHelper extends SQLiteOpenHelper{

    public static final String databaseName="Smsashu1.db";
    public static final String tableName="Smstab1";
    public static final String col1="ID";
    public static final String col2="SENDER";
    public static final String col3="DATE";
    public static final String col4="BODY";
    public static final String col5="TTYPE";
    public static final String col6="COD";
    public static final String col7="REC";
    public static final String col8="AMOUNT";
    public static final String col9="LATT";
    public static final String col10="LONGI";





    public SqlDatabaseHelper(Context context) {
        super(context, databaseName,null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ tableName + " (ID INTEGER PRIMARY KEY ,SENDER TEXT ,DATE TEXT,BODY TEXT UNIQUE,TTYPE TEXT,COD TEXT,REC TEXT,AMOUNT TEXT,LATT TEXT,LONGI TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS"+tableName);
        onCreate(db);
    }

    public boolean insertData(String Sender,String Date,String Body,String ttype,String cod,String rec,String amt,String latt,String longi)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col2,Sender);
        contentValues.put(col3,Date);
        contentValues.put(col4,Body);
        contentValues.put(col5,ttype);
        contentValues.put(col6,cod);
        contentValues.put(col7,rec);
        contentValues.put(col8,amt);
        contentValues.put(col9,latt);
        contentValues.put(col10,longi);



        long result=db.insert(tableName,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName,null);
        return res;

    }

}
