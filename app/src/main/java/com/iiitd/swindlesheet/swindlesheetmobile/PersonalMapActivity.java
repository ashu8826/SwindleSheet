package com.iiitd.swindlesheet.swindlesheetmobile;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/*
Scrolling acitivty which is modified by attaching
a mapview in the Appbar.
it uses nested scrolling for
-for the scrolling the appbar
-and for the content of the activity
Display the Data for the expenditure with the location of the expenditure
 */
public class PersonalMapActivity extends AppCompatActivity {


    Double latd, longid;
    private GoogleMap mMap;
    MapView mMapView;
    Spend cur;
    TextView ttypetxt,amttxt,bodytxt,sendertxt,sms_datetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ttypetxt = (TextView)findViewById(R.id.ttypetxt);
        amttxt = (TextView)findViewById(R.id.amttxt);
        bodytxt = (TextView)findViewById(R.id.bodytxt);
        sendertxt = (TextView)findViewById(R.id.sendertxt);
        sms_datetxt = (TextView)findViewById(R.id.sms_datetxt);


        mMapView = (MapView) findViewById(R.id.mapViewfrag);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.getIntent() != null) {
            Bundle spendBundle = this.getIntent().getBundleExtra("Spend");
            cur = new Spend(spendBundle.getString("sender"),spendBundle.getString("sms_date"),spendBundle.getString("body"),spendBundle.getString("ttype"),
                    spendBundle.getString("cord"),spendBundle.getString("rec"),spendBundle.getString("amt"),spendBundle.getString("latt"),spendBundle.getString("longi"));
            ttypetxt.setText(cur.getTtype());
            amttxt.setText(cur.getAmount());
            bodytxt.setText(cur.getBody());
            sendertxt.setText(cur.getSender());
            sms_datetxt.setText(cur.getSms_date());

            if(cur.getLatt()!=null&&cur.getLongi()!=null){
                latd = Double.parseDouble(cur.getLatt());
                longid = Double.parseDouble(cur.getLongi());
            }
            else{
                latd = 0.0;
                longid = 0.0;
            }
        }
        else{
            finish();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                if(latd==0.0||longid==0.0){

                }
                else{
                    //iiitd location 28.547554 77.2737238
                    //Log.d("currentlocation",Double.parseDouble(lat)+"   "+Double.parseDouble(longi));
                    LatLng current = new LatLng(latd,longid);
                    mMap.addMarker(new MarkerOptions().position(current).title(cur.getCord()+"    "+cur.getAmount()).snippet(cur.getSms_date()));
                    //mMap.addMarker(new MarkerOptions().position(current).title("Marker in Sydney"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}