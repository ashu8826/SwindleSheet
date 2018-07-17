package com.iiitd.swindlesheet.swindlesheetmobile;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/*
 * A simple {@link Fragment} subclass.
 * Fragment which shows Recycler having data of the spending
 * from your various accounts
 */
public class SmsRecyclerFragment extends Fragment {
    private List<Spend> spendList = new ArrayList<>();
    SqlDatabaseHelper myDb;
    SpendAdapter mAdapter;
    public static SmsRecyclerFragment smsRecyclerFragment=null;


    public SmsRecyclerFragment() {
        // Required empty public constructor
    }

    public static SmsRecyclerFragment getInstance(){
        return smsRecyclerFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        smsRecyclerFragment=this;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sms_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_viewfrag);
        mAdapter = new SpendAdapter(spendList);
        if(getActivity()==null||getActivity().getApplicationContext()==null){
            Log.d("fragashu","asnjansnkn");
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());//new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecorationBill(getActivity(), LinearLayoutManager.VERTICAL,30));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {

            public void onClick(View view, int position) {

                Intent i = new Intent(getActivity(), PersonalMapActivity.class);
                Spend cur = spendList.get(position);

                Bundle spendBundle = new Bundle();
                spendBundle.putString("ttype",cur.getTtype());
                spendBundle.putString("amt",cur.getAmount());
                spendBundle.putString("body",cur.getBody());
                spendBundle.putString("rec",cur.getRec());
                spendBundle.putString("cord",cur.getCord());
                spendBundle.putString("sender",cur.getSender());
                spendBundle.putString("sms_date",cur.getSms_date());
                spendBundle.putString("latt",cur.getLatt());
                spendBundle.putString("longi",cur.getLongi());
                i.putExtra("Spend", spendBundle);
                startActivity(i);
            }


            public void onLongClick(View view, int position) {

            }
        }));

        recyclerView.setAdapter(mAdapter);
        showMessage();
        return rootView;
    }
    public final void showMessage()
    {
        if(getActivity()!=null){
            myDb=new SqlDatabaseHelper(getActivity());
            Cursor res=myDb.getAllData();
            if(res.getCount()==0)
            {
                return;
            }
            spendList.clear();
            while(res.moveToNext())
            {
                spendList.add(new Spend(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
