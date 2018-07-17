package com.iiitd.swindlesheet.swindlesheetmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SpendAdapter extends RecyclerView.Adapter<SpendAdapter.MyViewHolder> {

    private List<Spend> spendList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sender,sms_date,ttype,amount;

        public MyViewHolder(View view) {
            super(view);
            sender = (TextView) view.findViewById(R.id.sender);
            sms_date = (TextView) view.findViewById(R.id.sms_date);
            ttype = (TextView) view.findViewById(R.id.ttype);
            amount = (TextView) view.findViewById(R.id.amount);
        }
    }


    public SpendAdapter(List<Spend> spendList) {
        this.spendList = spendList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spend_list_row1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Spend spend = spendList.get(position);
        holder.amount.setText(spend.getAmount());
        holder.ttype.setText(spend.getTtype());
        holder.sms_date.setText(spend.getSms_date());
        holder.sender.setText(spend.getSender());
    }


    @Override
    public int getItemCount() {
        return spendList.size();
    }
}