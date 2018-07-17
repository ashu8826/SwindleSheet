package com.iiitd.swindlesheet.swindlesheetmobile;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/*
Acivity which shows statistics of the app.
 */

public class SummaryStatistics extends AppCompatActivity  implements AdapterView.OnItemSelectedListener  {
    TextView billspendtxt,otherspendtxt,totalspendtxt;
    SqlDatabaseHelper spendDb;
    SqlDatabaseHelper2 billDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spendDb = new SqlDatabaseHelper(this);
        billDb = new SqlDatabaseHelper2(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Spinner mySpinner=(Spinner)findViewById(R.id.spinner1);
        billspendtxt = (TextView)findViewById(R.id.billspendtxt);
        otherspendtxt = (TextView)findViewById(R.id.otherspendtxt);
        totalspendtxt = (TextView)findViewById(R.id.totalspendtxt);

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(this);


    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        Double total=0.0,other=0.0,bill=0.0;
        Cursor res1,res2;
        String t,o,b;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int lastmonth = cal.get(Calendar.MONTH);
        int curmonth = lastmonth+1;
        if(curmonth==1){
            lastmonth=12;
        }
        String show;

        switch (position) {
            case 0:
                //All time
                res1 = spendDb.getAllData();
                res2 = billDb.getAllData();
                while(res1.moveToNext()){
                    //String totaldate = res1.getString(2);
                    String totalamt = res1.getString(7);
                    total = total + Double.parseDouble(totalamt.substring(3));
                }
                while(res2.moveToNext()){
                    //String billdate = res2.getString(3);
                    String billamt = res2.getString(9);
                    bill = bill + Double.parseDouble(billamt);
                }
                other = total - bill;
                if(other<0){
                    other=0.0;
                }
                show = "Rs. "+Double.toString(bill);
                billspendtxt.setText(show);
                show = "Rs. "+Double.toString(other);
                otherspendtxt.setText(show);
                show = "Rs. "+Double.toString(total);
                totalspendtxt.setText(show);
                break;
            case 1:
                // Last Month
                res1 = spendDb.getAllData();
                res2 = billDb.getAllData();
                while(res1.moveToNext()){
                    String totaldate = res1.getString(2);
                    //Log.d("getmonth",totaldate.substring(5,7));
                    if(Integer.parseInt(totaldate.substring(5,7))==lastmonth){
                        String totalamt = res1.getString(7);
                        total = total + Double.parseDouble(totalamt.substring(3));
                    }
                }
                while(res2.moveToNext()){
                    String billdate = res2.getString(3);
                    if(Integer.parseInt(billdate.substring(5,7))==lastmonth){
                        String billamt = res2.getString(9);
                        bill = bill + Double.parseDouble(billamt);
                    }
                }
                other = total - bill;
                if(other<0){
                    other=0.0;
                }
                show = "Rs. "+Double.toString(bill);
                billspendtxt.setText(show);
                show = "Rs. "+Double.toString(other);
                otherspendtxt.setText(show);
                show = "Rs. "+Double.toString(total);
                totalspendtxt.setText(show);
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                //Current Month
                res1 = spendDb.getAllData();
                res2 = billDb.getAllData();
                while(res1.moveToNext()){
                    String totaldate = res1.getString(2);
                    if(Integer.parseInt(totaldate.substring(5,7))==curmonth){
                        String totalamt = res1.getString(7);
                        total = total + Double.parseDouble(totalamt.substring(3));
                    }
                }
                while(res2.moveToNext()){
                    String billdate = res2.getString(3);
                    if(Integer.parseInt(billdate.substring(5,7))==curmonth){
                        String billamt = res2.getString(9);
                        bill = bill + Double.parseDouble(billamt);
                    }
                }
                other = total - bill;
                if(other<0){
                    other=0.0;
                }
                show = "Rs. "+Double.toString(bill);
                billspendtxt.setText(show);
                show = "Rs. "+Double.toString(other);
                otherspendtxt.setText(show);
                show = "Rs. "+Double.toString(total);
                totalspendtxt.setText(show);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
