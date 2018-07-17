package com.iiitd.swindlesheet.swindlesheetmobile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
the main class of the application.it is the class which have the navigation drawer, viewpager with fragments
it also reads for the sms of the phone but in efficiently using shared preferences and Asyntask.
- asks for required premission like sms and location.
also do have the login functionality
and downoads photo for display on the navigation dwar.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    GoogleMapsFragments googleMapsFragments;
    SmsRecyclerFragment smsRecycler;
    NonRecBillFragment nonRecBillFragment;

    String pcomb[]={"android.permission.READ_SMS","android.permission.ACCESS_COARSE_LOCATION"};

    SqlDatabaseHelper myDb;
    SqlDatabaseHelper2 myDb2;
    BufferedReader br;
    ArrayList<Reg> regex = new ArrayList<>();
    SharedPreferences sharedPref;
    String lastdate;
    public final int MY_PERMISSIONS_REQUEST_CODE_READ_SMS = 1;

    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth;
    FirebaseUser user;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());

    int flag = 0;
    Cursor cursor;
    ProgressDialog progress;
    int viewpagerindex=0;
    SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        if(in!=null){
            viewpagerindex =in.getIntExtra("viewpager",0);
        }

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            //user is logged in


        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.ss4)
                            .setPrivacyPolicyUrl(getString(R.string.privacy_policy))
                            .build(),
                    RC_SIGN_IN);
        }

        myDb = new SqlDatabaseHelper(this);
        myDb2 = new SqlDatabaseHelper2(this);




        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container11);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(viewpagerindex);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 0) {

                } else if (slideOffset > 0.1) {

                    if (flag == 0) {
                        flag = 1;
                        ImageView photoIV = (ImageView) findViewById(R.id.imageView);
                        TextView emailtxt = (TextView) findViewById(R.id.email_id);
                        TextView nametxt = (TextView) findViewById(R.id.name_id);

                        String photourlstr;

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            for (UserInfo profile : user.getProviderData()) {

                                if (profile.getDisplayName() != null) {
                                    nametxt.setText(profile.getDisplayName());
                                }
                                if (profile.getEmail() != null) {
                                    emailtxt.setText(profile.getEmail());
                                }
                                if (profile.getPhotoUrl() != null && photoIV != null) {
                                    photourlstr = profile.getPhotoUrl().toString();
                                    wrap w = new wrap();
                                    w.setIv(photoIV);
                                    w.setUrl(photourlstr);
                                    retrievePhotoTask r = new retrievePhotoTask();
                                    r.execute(w);
                                }
                                break;
                            }
                        }
                    }
                    // started opening
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        progress=new ProgressDialog(this);//,R.style.custom);
        progress.setMessage("Parsing Sms\n");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
//        progress.show();



        sharedPref = this.getSharedPreferences("ashu", Context.MODE_PRIVATE);//Preferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        lastdate = sharedPref.getString("lastdate", "1970-1-1 00:00:00");
        Log.d("lastdate", lastdate);

        //demo();


        new MyRecyclerTask().execute();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rdate = formatter.format(new Date());
        editor.putString("lastdate", rdate);
        editor.apply();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            super.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d("Signedin","called one more time");

            } else {
                Log.d("AUTH", "NOT AUTHENTICATED");
                super.finish();
            }
        }

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

    }//onActivityResult ends

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new MyRecyclerTask().execute();
                    Log.d("ashuprem","acceptedprem");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    editor.putString("lastdate", "1970-1-1 00:00:00");
                    editor.apply();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.refresh) {
            if(smsRecycler!=null){
                smsRecycler.showMessage();
            }
            else{
                mViewPager.setCurrentItem(0);
            }
            //demo();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            mViewPager.setCurrentItem(0);

        } else if (id == R.id.nav_slideshow) {
            mViewPager.setCurrentItem(1);

        } else if (id == R.id.nav_manage) {
            mViewPager.setCurrentItem(2);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(MainActivity.this,SummaryStatistics.class);
            startActivity(i);

        } else if (id == R.id.logout) {
            AuthUI.getInstance()
                    .signOut(MainActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("AUTH", "USER LOGGED OUT");
                            finish();
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void regex_func(){
        br=null;

        try {
            br=new BufferedReader(new InputStreamReader(getAssets().open("RegAssets.txt")));
            String line;
            while((line=br.readLine())!=null)
            {
                String[] words = line.split("  ");
                if(words.length==5){
                    Reg r=new Reg(words[0],words[1],words[2],words[3],words[4]);
                    regex.add(r);
                }
            }
            //Toast.makeText(MainActivity.this, a.get(9).toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean prepareSplendData(){

        if ((ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_SMS)== PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            //progressBar.setMax(cursor.getCount());
            try{
                progress.setMax(cursor.getCount());
            }
            catch (NullPointerException e){
                progress.setMax(1);

            }
            return true;
        }
        else{
            ActivityCompat.requestPermissions(this,pcomb,MY_PERMISSIONS_REQUEST_CODE_READ_SMS);
            return false;
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0){
                smsRecycler =new SmsRecyclerFragment();
                return smsRecycler;
            }
            if(position==2){
                googleMapsFragments = new GoogleMapsFragments();
                return googleMapsFragments;
            }
            if(position==1){
                nonRecBillFragment = new NonRecBillFragment();
                return nonRecBillFragment;
            }
            smsRecycler =new SmsRecyclerFragment();
            return smsRecycler;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Expenditure";
                case 1:
                    return "Bills";
                case 2:
                    return "Maps";
            }
            return null;
        }
    }

    /*
    AsycnTask for sms reading and parsing for the correct sms and storing the database.
    It also displays ProgressDialog.
     */
    class MyRecyclerTask extends AsyncTask<Void, Integer, Void> {
        int flag=0;
        @Override
        protected Void doInBackground(Void... params) {
            regex_func();
            if(prepareSplendData()){
                try{
                    if(cursor.moveToFirst())
                    {
                        int smsno=1;
                        String rdate="null";
                        ArrayList<String> body =new ArrayList<>();

                        String mtnlrev="\\bDear Subscriber, [A-Z| a-z]+ No.(\\d{8})+ , [A-Z| a-z]+(\\d{2}\\/\\d{2}\\/\\d{4}) [a-z|A-Z| ]+(\\d*|.) [A-Z| a-z]+(\\d{2}-[A-Z]+-\\d{4})\\b";
                        String mtnlpaid="\\bDear Subscriber, [A-Z| a-z]+ Rs. (\\d*|.) against Your MTNL [A-Z|a-z]+ No:(\\d{8}) [A-Z|a-z| |,]+(\\d{2}\\/\\d{2}\\/\\d{4}) [A-Z|a-z| ]+\\b(\\d{2}-\\d{2}-\\d{4})\\b";
                        Pattern billpat = Pattern.compile(mtnlrev, Pattern.MULTILINE);
                        Pattern billpaidpat  = Pattern.compile(mtnlpaid, Pattern.MULTILINE);
                        Matcher matcher;
                        do{
                            flag=1;
                            Pattern pattern,pattern1;
                            for(int i=0;i<regex.size();i++) {
                                String pat= regex.get(i).regEx ;

                                pattern = Pattern.compile(pat, Pattern.MULTILINE);

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                rdate = formatter.format(new Date(cursor.getLong(cursor.getColumnIndex("date"))));

                                if(rdate.compareTo(lastdate)<0){
                                    Log.d("mainsms","i didnt checked it");
                                    return null;
                                }
                                matcher = pattern.matcher(cursor.getString(cursor.getColumnIndex("Body")));
                                if (matcher.find()) {

                                    pattern1=Pattern.compile("\\b(Rs|rs|RS|INR)+( |. |.)?(?:(\\d|,\\d)*\\.)?\\d+",Pattern.MULTILINE);
                                    Matcher m = pattern1.matcher(cursor.getString(cursor.getColumnIndex("Body")));
                                    Boolean isInserted = true;
                                    if(m.find()){
                                        isInserted = myDb.insertData(regex.get(i).bank, rdate, cursor.getString(cursor.getColumnIndex("Body")), regex.get(i).ttype, regex.get(i).cod, regex.get(i).rec,m.group(), null, null);
                                    }
                                    break;
                                }
                                else{

                                }
                            }
                            matcher =billpat.matcher(cursor.getString(cursor.getColumnIndex("Body")));
                            if(matcher.find()){

                                myDb2.insertData("MTNL", matcher.group(1),rdate,matcher.group(2),matcher.group(4),"null",cursor.getString(cursor.getColumnIndex("Body")),"false", matcher.group(3));

                            }
                            matcher = billpaidpat.matcher(cursor.getString(cursor.getColumnIndex("Body")));
                            if(matcher.find()){

                                Boolean res = myDb2.updatePaid(matcher.group(2),matcher.group(3), matcher.group(4));
                                if(!res){
                                    body.add(cursor.getString(cursor.getColumnIndex("Body")));
                                }

                            }
                            publishProgress(smsno);
                            smsno++;

                        }while(cursor.moveToNext());

                        for(String b:body){
                            matcher = billpaidpat.matcher(b);
                            if(matcher.find()){
                                myDb2.updatePaid(matcher.group(2),matcher.group(3), matcher.group(4));
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "NO SMS", Toast.LENGTH_SHORT).show();//no sms
                    }

                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }
            }
            else{
                Log.d("premashu","no premission");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progress.hide();
            if (progress.isShowing()&&flag==1){
                progress.dismiss();

            }
            if(smsRecycler!=null){
                smsRecycler.showMessage();
            }if(nonRecBillFragment!=null){
                nonRecBillFragment.showBill();
            }
        }

        @Override
        protected void onPreExecute() {

            progress.show();
            progress.setProgress(0);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

    }
}
