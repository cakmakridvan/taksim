package com.redblack.taksim.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redblack.taksim.R;
import com.redblack.taksim.model.BildirimData;

public class BildirimDataAdapter extends RecyclerView.Adapter<BildirimDataAdapter.ViewHolder> {

    private BildirimData[] bildirimData;

    public BildirimDataAdapter(BildirimData[] bildirimData){
        this.bildirimData = bildirimData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bildirim_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BildirimData myListData = bildirimData[position];
        holder.textView.setText(bildirimData[position].getMesaj());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"click on item: "+myListData.getMesaj(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return bildirimData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.txt_mesaj);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout_bildirim_data);
        }
    }
}
