package com.iiitd.swindlesheet.swindlesheetmobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by mayur on 14/11/16.
 * broadcast reciever for Getting , parsing ,
 * getting location and saving in the database
 */

public class Smsb extends BroadcastReceiver {
    SqlDatabaseHelper myDb;
    SqlDatabaseHelper2 myDb2;

    SharedPreferences sharedPref;
    String lastdate;
    BufferedReader br;
    ArrayList<Reg> regex=new ArrayList<Reg>();
    // Context context;

    public static final String SMS_BUNDLE = "pdus";
    private LocationManager locationManager;
    private LocationListener listener;
    public String latt;
    public  String longi;

    public void regex_func(Context context){
        br=null;


        regex= new ArrayList<>();
        try {
            int count=0;
            br=new BufferedReader(new InputStreamReader(context.getAssets().open("RegAssets.txt")));

            String line;
            while((line=br.readLine())!=null)
            {
                String[] words = line.split("  ");
                if(words.length==5){
                    Reg r=new Reg(words[0],words[1],words[2],words[3],words[4]);
                    regex.add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public void onReceive(final Context context, Intent intent) {


        myDb = new SqlDatabaseHelper(context);
        myDb2 = new SqlDatabaseHelper2(context);
        Log.d("ashusms", "in onCreate");

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);//LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Toast.makeText(context, "longitudesms" + location.getLongitude() + "Latitude" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                Log.d("broadcast ",location.getLatitude()+"   "+location.getLongitude());
                latt=Double.toString(location.getLatitude());
                longi=Double.toString(location.getLongitude());
                //Log.d("mesrec","executed");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {


            }

            @Override
            public void onProviderDisabled(String s) {
//                try{
//
//                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    context.startActivity(i);
//                }
//                catch (Exception e){
//                    Log.d("kool","everything is kool");
//                }
            }
        };


        sharedPref = context.getSharedPreferences("ashu",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        lastdate  =sharedPref.getString("lastdate","1970-1-1 00:00:00");

        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            regex_func(context);
            String rdate =null;
            for (int j = 0 ;j < sms.length; ++j) {
                Pattern pattern,pattern1;
                String mtnlrev="\\bDear Subscriber, [A-Z| a-z]+ No.(\\d{8})+ , [A-Z| a-z]+(\\d{2}\\/\\d{2}\\/\\d{4}) [a-z|A-Z| ]+(\\d*|.) [A-Z| a-z]+(\\d{2}-[A-Z]+-\\d{4})\\b";
                String mtnlpaid="\\bDear Subscriber, [A-Z| a-z]+ Rs. (\\d*|.) against Your MTNL [A-Z|a-z]+ No:(\\d{8}) [A-Z|a-z| |,]+(\\d{2}\\/\\d{2}\\/\\d{4}) [A-Z|a-z| ]+\\b(\\d{2}-\\d{2}-\\d{4})\\b";
                Pattern billpat = Pattern.compile(mtnlrev, Pattern.MULTILINE);
                Pattern billpaidpat  = Pattern.compile(mtnlpaid, Pattern.MULTILINE);
                Matcher matcher;
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[j]);
                for(int i=0;i<regex.size();i++) {
                    String pat= regex.get(i).regEx ;

                    pattern = Pattern.compile(pat, Pattern.MULTILINE);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    rdate = formatter.format(new Date(smsMessage.getTimestampMillis()));

                    if(rdate.compareTo(lastdate)<0){
                        Log.d("mainsms","i didnt checked it");
                        //showMessage();
                        return ;
                    }
                    else{
                        Log.d("mainsms","conterpositive");
                    }

                    matcher = pattern.matcher(smsMessage.getMessageBody());
                    if (matcher.find()) {

                        pattern1=Pattern.compile("\\b(Rs|rs|RS|INR)+( |. |.)?(?:(\\d|,\\d)*\\.)?\\d+",Pattern.MULTILINE);
                        Matcher m = pattern1.matcher(smsMessage.getMessageBody());
                        Boolean isInserted = true;
                        if(m.find()&&configure_button(context)){
                            Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(l!=null && (latt==null) && (longi==null)){
                                latt = Double.toString(l.getLatitude());
                                longi = Double.toString(l.getLongitude());
                                Log.d("ashutlastsms",latt + "  "+ longi);

                            }
                            isInserted = myDb.insertData(regex.get(i).bank, rdate, smsMessage.getMessageBody(), regex.get(i).
                                    ttype, regex.get(i).cod, regex.get(i).rec,m.group(),latt,longi);// Double.toString(28.5444356), Double.toString(77.2723927));
                            Log.d("sms inserted sms",isInserted.toString()+"  "+latt+ "  "+longi);
                            if(SmsRecyclerFragment.getInstance()!=null){
                                SmsRecyclerFragment.getInstance().showMessage();
                                Log.d("smsrecycler","modified");
                            }
                        }
                        break;
                    }
                }
                matcher =billpat.matcher(smsMessage.getMessageBody());
                if(matcher.find()){

                    myDb2.insertData("MTNL", matcher.group(1),rdate,matcher.group(2),matcher.group(4),"null",smsMessage.getMessageBody(),"false", matcher.group(3));

                }
                matcher = billpaidpat.matcher(smsMessage.getMessageBody());
                if(matcher.find()){
                    myDb2.updatePaid(matcher.group(2),matcher.group(3), matcher.group(4));
                }
                if(NonRecBillFragment.getInstance()!=null){
                    NonRecBillFragment.getInstance().showBill();
                    Log.d("smsrecycler","modified");
                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sdate = formatter.format(new Date());
            editor.putString("lastdate",sdate);
            editor.apply();

        }
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        configure_button(context);
    }

    private Boolean configure_button(Context context) {

        if (ActivityCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        return true;
    }
}