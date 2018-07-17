package com.iiitd.swindlesheet.swindlesheetmobile;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Fragment is attached to Main Activity on the second tab layout by viewpager.
 * It have a bottomnavigationview for shoing three different type of list.
 * -Unpaid Bills for which recurring reminders are no sets
 * -Paid Bills
 * -Unpaid bills for which reminders are set.
 */
public class NonRecBillFragment extends Fragment {

    private List<Bill> billList = new ArrayList<>();
    private List<Bill> mPendingDatalist = new ArrayList<>();

    SqlDatabaseHelper2 myDb;
    NonRecBillAdapter mAdapter;
    public static NonRecBillFragment nonRecBillFragment=null;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    TextView helptxt;
    Notification n;

    public static NonRecBillFragment getInstance(){
        return nonRecBillFragment;
    }



    public NonRecBillFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nonRecBillFragment = this;
        View rootView =  inflater.inflate(R.layout.fragment_non_rec_bill, container, false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view_bilfrag);
        helptxt = (TextView)rootView.findViewById(R.id.helptxt);
        myDb=new SqlDatabaseHelper2(getActivity());
        mAdapter = new NonRecBillAdapter(billList,mPendingDatalist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());//new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecorationBill(getActivity(), LinearLayoutManager.VERTICAL,20));

        setUpItemTouchHelper();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {

            public void onClick(View view, int position) {

            }

            public void onLongClick(View view, int position) {


            }


        }));

        recyclerView.setAdapter(mAdapter);
        //showBill();


        bottomNavigationView = (BottomNavigationView)rootView.findViewById(R.id.bottom_navig);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_unpaidnonrec:
                                helptxt.setText("Swipe to Set Reminder");
                                showBill();
                                break;
                            case R.id.action_paid:
                                helptxt.setText("Swipe to Mark As Done");
                                showPaid();
                                break;
                            case R.id.action_unpaid:
                                helptxt.setText("Swipe to Unset Reminder");
                                showUnpaidRec();
                                break;
                        }
                        return false;
                    }
                });
        showUnpaidRec();

        return rootView;
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                // Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                myDb=new SqlDatabaseHelper2(getActivity());
                NonRecBillAdapter adapter = (NonRecBillAdapter)recyclerView.getAdapter();

                int a=viewHolder.getAdapterPosition();
                String landno =adapter.billList.get(a).getLandno();
                String bill_date = adapter.billList.get(a).getBill_date();
                String  rec = adapter.billList.get(a).getRec();
                String recv_data = billList.get(a).getRecv_date();
                Log.d("datata",adapter.billList.get(a).getRec() + adapter.billList.get(a).getRecv_date());


                n= new Notification();
                Cursor res = myDb.unpaidRec();

                if(rec.equalsIgnoreCase("false")&&recv_data.equalsIgnoreCase("null")){

                    boolean set=myDb.setUnset(landno,bill_date,true);

                    n.scheduleNotification(n.getNotification("You have "+ res.getCount()+" pending bills", getActivity()), 5000,getActivity());
                    Log.d("status","UnpaidNonRec");
                    adapter.remove(a);

                }
                else if(rec.equalsIgnoreCase("true")&&recv_data.equalsIgnoreCase("null")){

                    boolean set=myDb.setUnset(landno,bill_date,false);

                    if(res.getCount()==0){
                        Log.d("nodata","nodata");
                        n.stopnoti(getActivity());
                    }
                    else{
                        Log.d("nodata","data "+res.getCount());
                        n.scheduleNotification(n.getNotification("You have "+ res.getCount()+"pending bills", getActivity()), 5000,getActivity());

                    }
                    adapter.remove(a);

                }
                else if(!recv_data.equalsIgnoreCase("null")){
                    int deletedRows=myDb.deleteData(landno,bill_date);
                    adapter.remove(a);
                }

            }

            //took the referrence from stackoverflow for onChildDraw
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder

                    View itemView = viewHolder.itemView;
                    Bitmap icon;
                    Paint p = new Paint();

                    if (dX > 0) {

            /* Set your color for positive displacement */
                        p.setARGB(0,60,125,139);//607D8B  #FFFFFF

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);
                    } else {

                        //icon = BitmapFactory.decodeResource(getResources(), R.drawable.de);

            /* Set your color for negative displacement */
                        p.setARGB(255,60,125,139);
                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);

//                        c.drawBitmap(icon,
//                                (float) itemView.getRight() +1100 - icon.getWidth(),
//                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
//                                p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
/*
function to show
-Unpaid Bills for which recurring reminders are no set
 */
    public final void showBill()
    {
        helptxt.setText("Swipe to Set Reminder");
        if(getActivity()!=null){
            myDb=new SqlDatabaseHelper2(getActivity());
            //Cursor res=myDb.getAllData();
            Cursor res = myDb.unpaidNonRec();
            if(res.getCount()==0)
            {
                billList.clear();
                mAdapter.notifyDataSetChanged();
                return;
            }
            billList.clear();
            while(res.moveToNext())
            {
                billList.add(new Bill(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)));
                //spendList.add(new Spend(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)));
            }
            mAdapter.notifyDataSetChanged();
        }
    }
    /*
function to show
-Paid Bills
 */
    public final void showPaid(){
        helptxt.setText("Swipe to Mark As Done");
        if(getActivity()!=null){
            myDb=new SqlDatabaseHelper2(getActivity());
            Cursor res = myDb.paid(true);
            if(res.getCount()==0)
            {
                billList.clear();
                mAdapter.notifyDataSetChanged();
                return;
            }
            billList.clear();
            while(res.moveToNext())
            {
                billList.add(new Bill(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /*
function to show
-Unpaid Bills for which recurring reminders are set
 */

    public final void showUnpaidRec(){
        helptxt.setText("Swipe to Unset Reminder");
        if(getActivity()!=null){
            myDb=new SqlDatabaseHelper2(getActivity());
            //Cursor res=myDb.getAllData();
            Cursor res = myDb.unpaidRec();
            if(res.getCount()==0)
            {
                billList.clear();
                mAdapter.notifyDataSetChanged();
                return;
            }
            billList.clear();
            while(res.moveToNext())
            {
                billList.add(new Bill(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
