package com.redblack.taksim.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.redblack.taksim.R;
import com.redblack.taksim.model.HistorySearch;

public class HistorySearchAdapter extends RecyclerView.Adapter<HistorySearchAdapter.ViewHolder> {

    private HistorySearch[] historydata;
    private Context context;

    public HistorySearchAdapter(HistorySearch[] historydata){

        this.historydata = historydata;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistorySearch myListData = historydata[position];
        holder.textView.setText(historydata[position].getAddress());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.history_search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return historydata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.txt_history_item);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout_history_search_item);
        }
    }
}
