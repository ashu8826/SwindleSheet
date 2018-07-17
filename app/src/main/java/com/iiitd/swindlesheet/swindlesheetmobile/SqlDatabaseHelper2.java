package com.iiitd.swindlesheet.swindlesheetmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Created by mayur on 1/10/16.
 * Database helper class for Spends database
 */
public class SqlDatabaseHelper2 extends SQLiteOpenHelper{

    public static final String databaseName="SmsBill.db";
    public static final String tableName="SmsBilltab";
    public static final String col1="ID";
    public static final String col2="SENDER";
    public static final String col3="LAND_NO";
    public static final String col4="SMS_DATE";
    public static final String col5="BILL_DATE";
    public static final String col6="DUE_DATE";
    public static final String col7="RECV_DATE";
    public static final String col8="BODY";
    public static final String col9="REC";
    public static final String col10="AMOUNT";





    public SqlDatabaseHelper2(Context context) {
        super(context, databaseName,null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ tableName + " (ID INTEGER ,SENDER TEXT ,LAND_NO TEXT ,SMS_DATE TEXT,BILL_DATE TEXT ," +
                "DUE_DATE TEXT,RECV_DATE TEXT,BODY TEXT,REC TEXT,AMOUNT TEXT, PRIMARY KEY (LAND_NO,BILL_DATE))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS"+tableName);
        onCreate(db);
    }

    public boolean insertData(String Sender,String LAND_NO,String SMS_DATE,String BILL_DATE,String DUE_DATE,String RECV_DATE,String BODY,String REC,String AMOUNT)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col2,Sender);
        contentValues.put(col3,LAND_NO);
        contentValues.put(col4,SMS_DATE);
        contentValues.put(col5,BILL_DATE);
        contentValues.put(col6,DUE_DATE);
        contentValues.put(col7,RECV_DATE);
        contentValues.put(col8,BODY);
        contentValues.put(col9,REC);
        contentValues.put(col10,AMOUNT);

        long result=db.insert(tableName,null,contentValues);
        if(result==-1){
            Log.d("datains","inserted");
            return false;
        }
        else{
            Log.d("datains","not inserted");
            return true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName,null);
        return res;

    }

    public Cursor getRecData(boolean su)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        if(su)
        {
            Cursor res=db.rawQuery("select * from "+tableName+" WHERE REC = \"true\"",null);
            return res;
        }else
        {
            Cursor res=db.rawQuery("select * from "+tableName+" WHERE REC = \"false\"",null);
            return res;
        }
    }

    public Cursor paid(boolean su)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        if(su)
        {
            Cursor res=db.rawQuery("select * from "+tableName+" WHERE RECV_DATE != \"null\"",null);
            return res;
        }else
        {
            Cursor res=db.rawQuery("select * from "+tableName+" WHERE RECV_DATE = \"null\"",null);
            return res;
        }


    }


    public Cursor unpaidNonRec()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName+" WHERE RECV_DATE = \"null\" AND REC = \"false\"",null);
        return res;
    }



    public Cursor unpaidRec()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName+" WHERE RECV_DATE = \"null\" AND REC = \"true\"",null);
        return res;
    }




//    public boolean updateData(String id,String name,String roll,String mob)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(col2,name);
//        contentValues.put(col3,roll);
//        contentValues.put(col4,mob);
//        db.update(tableName,contentValues," ID = ?",new String[] { id });
//        return true;
//    }

    public boolean setUnset(String landcol3,String billcol5,boolean onOff)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        if(onOff){
            contentValues.put(col9,"true");
        }
        else{
            contentValues.put(col9,"false");
            Log.d("setunset","false");
        }
        db.update(tableName,contentValues,col3+" = ? and "+col5+" = ?",new String[] { landcol3,billcol5 });
        return true;
    }


    public boolean updatePaid(String landcol3,String billcol5,String rdate)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col7,rdate);
        int re = db.update(tableName,contentValues,col3+" = ? and "+col5+" = ?",new String[] { landcol3,billcol5 });
        if(re>0){
            return true;
        }
        else{
            return false;
        }
        //return true;
    }

//    public boolean isUnpaidRec(String landcol3,String billcol5){
//        SQLiteDatabase db=this.getWritableDatabase();
//        Log.d("unpaidrec","in unpaid rec");
//        //Cursor res=db.rawQuery("select * from "+tableName+" WHERE LAND_NO = " + landcol3 + " and BILL_DATE = "+billcol5+" and RECV_DATE = \"null\" AND REC = \"true\"",null);
//        Cursor res=db.rawQuery("select * from "+tableName+" WHERE LAND_NO = " + landcol3 + " and BILL_DATE = "+billcol5,null);
//        if(res.moveToNext()){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//    public boolean isUnpaidNonRec(String landcol3,String billcol5){
//        SQLiteDatabase db=this.getWritableDatabase();
//        Log.d("unpaidnonrec","in unpaidnonrec");
//        Cursor res=db.rawQuery("select * from "+tableName+" WHERE LAND_NO = " + landcol3 + " and BILL_DATE = "+billcol5+" and RECV_DATE = \"null\" AND REC = \"false\"",null);
//        if(res.moveToNext()){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//    public boolean isPaid(String landcol3,String billcol5){
//        SQLiteDatabase db=this.getWritableDatabase();
//        Log.d("paid","in paid");
//        Cursor res=db.rawQuery("select * from "+tableName+" WHERE LAND_NO = " + landcol3 + " and BILL_DATE = "+billcol5+" and RECV_DATE != \"null\"",null);
//        if(res.getCount()>=1){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }

    public int deleteData(String landcol3,String billcol5)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return  db.delete(tableName,col3+" = ? and "+col5+" = ?",new String[] { landcol3,billcol5 });
    }

}
