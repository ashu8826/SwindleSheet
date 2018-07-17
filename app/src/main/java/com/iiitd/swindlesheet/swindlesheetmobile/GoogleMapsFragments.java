package com.iiitd.swindlesheet.swindlesheetmobile;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapsFragments extends Fragment  {//implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap mMap;
    LatLngBounds.Builder builder;
    SqlDatabaseHelper myDb;
    Cursor res;
    ArrayList<LatLng> allocation =new ArrayList<>();
    ArrayList<Spend> spendList =new ArrayList<>();

    public GoogleMapsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_google_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        myDb=new SqlDatabaseHelper(getActivity());
        res=myDb.getAllData();
        if(res.getCount()==0)
        {
            //
            // Toast.makeText(MainActivity.this, "NOthing found", Toast.LENGTH_SHORT).show();
            Log.d("ashucount","Nothing found wala case");

        }

        while(res.moveToNext())
        {
            if(res.getString(8)!=null&&!res.getString(8).equalsIgnoreCase("null")) {
                allocation.add(new LatLng(Double.parseDouble(res.getString(8)),Double.parseDouble(res.getString(9))));
                spendList.add(new Spend(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),
                        res.getString(8),res.getString(9)));
            }
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("premissionsashu","true");
                    mMap.setMyLocationEnabled(true);
                }
                if(allocation.size()==0){

                }

                else if(allocation.size()==1){
                    mMap.addMarker(new MarkerOptions().position(allocation.get(0)).title(spendList.get(0).getCord()).snippet(spendList.get(0).getAmount()+"   "+spendList.get(0).getSms_date()));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(allocation.get(0)).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else{
                    int i=0;
                    builder = new LatLngBounds.Builder();
                    for(LatLng ll : allocation){
                        mMap.addMarker(new MarkerOptions().position(ll).title(spendList.get(i).getCord()+"    "+spendList.get(i).getAmount()).snippet(spendList.get(i).getSms_date()));
                        builder.include(ll);
                        i++;
                    }
                    LatLngBounds bounds = builder.build();

                    final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,50);
                    //CameraUpdate cu1 = CameraUpdateFactory.newLatLngZoom(sydney, 12F);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    //mMap.animateCamera(cu);
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                           mMap.animateCamera(cu);

                            //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
                        }
                    });

                }


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        return false;
                    }
                });

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent i = new Intent(getActivity(), PersonalMapActivity.class);
                        LatLng mark = marker.getPosition();
                        Spend cur=null;
                        for(Spend s:spendList){
                            if(Double.parseDouble(s.getLatt())==mark.latitude && Double.parseDouble(s.getLongi())==mark.longitude) {
                                cur = s;
                            }
                        }
                        if(cur!=null){
                            Bundle spendBundle = new Bundle();
                            spendBundle.putString("ttype",cur.getTtype());
                            spendBundle.putString("amt",cur.getAmount());
                            spendBundle.putString("body",cur.getBody());
                            spendBundle.putString("rec",cur.getCord());
                            spendBundle.putString("sender",cur.getSender());
                            spendBundle.putString("sms_date",cur.getSms_date());
                            spendBundle.putString("latt",cur.getLatt());
                            spendBundle.putString("longi",cur.getLongi());
                            i.putExtra("Spend", spendBundle);
                            startActivity(i);
                        }
                    }
                });

            }

        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMapView!=null){
            mMapView.onResume();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMapView!=null){
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMapView!=null){
            mMapView.onDestroy();
        }
        else{
            Log.d("Destory","error");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView!=null){
            mMapView.onLowMemory();
        }

    }
}
