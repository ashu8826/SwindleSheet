package com.iiitd.swindlesheet.swindlesheetmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class NonRecBillAdapter extends RecyclerView.Adapter<NonRecBillAdapter.MyViewHolder> {

    public List<Bill> billList;
    public List<Bill> itemsPendingRemoval;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView landno,mtnlsender,amt,due_date;

        public MyViewHolder(View view) {
            super(view);
            landno = (TextView) view.findViewById(R.id.landno);
            mtnlsender = (TextView) view.findViewById(R.id.mtnlsender);
            amt = (TextView) view.findViewById(R.id.amt);
            due_date = (TextView) view.findViewById(R.id.due_date);
        }
    }


    public NonRecBillAdapter(List<Bill> dataList, List<Bill> itemsPendingRemoval) {
        this.billList = dataList;
        this.itemsPendingRemoval = itemsPendingRemoval;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unpaidnonrecbillsrows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bill data = billList.get(position);
        holder.landno.setText(data.getLandno());
        String amttxt = "Rs "+data.getAmount();
        holder.amt.setText(amttxt);
        holder.due_date.setText(data.getDue_date());
        holder.mtnlsender.setText(data.getSender());
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }

    public void remove(int position) {
        Bill item = billList.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (billList.contains(item)) {
            billList.remove(position);
            notifyItemRemoved(position);
        }
    }



}
