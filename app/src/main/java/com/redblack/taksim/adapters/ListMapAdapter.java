package com.redblack.taksim.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redblack.taksim.R;
import com.redblack.taksim.model.ListMapData;
import com.redblack.taksim.ui.activity.FavoriteAddress;
import com.redblack.taksim.ui.activity.HomeAddress;
import com.redblack.taksim.ui.activity.JobAddress;

public class ListMapAdapter extends RecyclerView.Adapter<ListMapAdapter.ViewHolder> {

    private ListMapData[] listdata;
    private Context context;

    // RecyclerView recyclerView;
    public ListMapAdapter(ListMapData[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_map_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListMapData myListData = listdata[position];
        holder.textView.setText(listdata[position].getDescription());
        holder.imageView.setImageResource(listdata[position].getImgId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                if(position == 0){
                    //Home Address
                    Intent go_homeAddress = new Intent(context,HomeAddress.class);
                    context.startActivity(go_homeAddress);
                }else if(position == 1){
                    //Work Address
                    Intent go_jobAddress = new Intent(context,JobAddress.class);
                    context.startActivity(go_jobAddress);
                }else if(position == 2){
                    //Favori Address
                    Intent go_favoriteAddress = new Intent(context,FavoriteAddress.class);
                    context.startActivity(go_favoriteAddress);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
