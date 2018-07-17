package com.iiitd.swindlesheet.swindlesheetmobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.NotificationCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

//import android.icu.util.Calendar;
/*
class to generate notification and set it time to notify
using alarm manager.
Also have function to disable notifications
 */
public class Notification {
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    //@TargetApi(Build.VERSION_CODES.N)
    public void scheduleNotification(android.app.Notification notification, int delay , Context context) {


        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 25);


        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Log.d("time",Long.toString(calendar.getTimeInMillis())+"    "+System.currentTimeMillis()+"    "+System.currentTimeMillis()+10000);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    public void stopnoti(Context context)
    {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pintent =PendingIntent.getBroadcast(context,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager!=null){
            Log.d("cancelnoti","cancelnoti");
            //alarmManager.cancel(pendingIntent);
            alarmManager.cancel(pintent);
        }else{
            Log.d("cancelnoti","not working");
        }
    }

    public android.app.Notification getNotification(String content, Context context) {

        Intent repeating_intend=new Intent(context,MainActivity.class);
        repeating_intend.putExtra("viewpager",1);
        repeating_intend.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent p=PendingIntent.getActivity(context,100,repeating_intend,PendingIntent.FLAG_UPDATE_CURRENT);


        android.app.Notification.Builder builder = new android.app.Notification.Builder(context);
        builder.setContentTitle("Swindle Sheet");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.swindle_noti);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(p);
        builder.setAutoCancel(true);

        Log.d("notiset","notiiiiiiiii");


        return builder.build();


    }
}
