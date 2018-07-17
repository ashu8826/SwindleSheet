package com.iiitd.swindlesheet.swindlesheetmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Created by mayur on 22/10/16.
 * Asynctask for retrieving profile photo from facebook,google
 */
class retrievePhotoTask extends AsyncTask<wrap, Void,Bitmap> {

    private Exception exception;
    Bitmap b;
    wrap wr = null;


    protected Bitmap doInBackground(wrap... w) {
        try
        {
            // Toast.makeText(MainActivity.this, "wrrrrr", Toast.LENGTH_SHORT).show();

            //String urls = iarr[0].getTag().toString();

//            for(int i=0;i<urls.length;i++){
//                Log.d("urls",urls[i]);
//            }

            this.wr = w[0];
            return download_Image(wr.getUrl());



        }catch (Exception e){
            this.exception = e;
            e.printStackTrace();
            Log.d("error","eroro");
            return null;

        }
    }
    protected void onPostExecute(Bitmap bm) {
        // TODO: check this.exception
        // TODO: do something with the feed
        wr.getIv().setImageBitmap(bm);
    }
    private Bitmap download_Image(String urls) {
        try{
            URL url=new URL(urls);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();
            Bitmap myBitmap= BitmapFactory.decodeStream(input);
            //Toast.makeText(, "wrrrrr", Toast.LENGTH_SHORT).show();
            Log.d("ashuashu",myBitmap.toString());
            b=myBitmap;

            Log.d("ashuashu",b.toString());
            return myBitmap;

        }catch(Exception e){
            this.exception = e;
            e.printStackTrace();
            Log.d("erro","eroro");
            return null;
        }

    }
}