package com.redblack.taksim.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.redblack.taksim.R;
import com.redblack.taksim.model.Address;
import com.redblack.taksim.model.ListMapData;
import com.redblack.taksim.ui.activity.FavoriteAddress;
import com.redblack.taksim.ui.activity.HomeAddress;
import com.redblack.taksim.ui.activity.JobAddress;

import io.paperdb.Paper;

public class AdreslerimDataAdapter extends RecyclerView.Adapter<AdreslerimDataAdapter.ViewHolder> {

    private ListMapData[] listdata;
    private Context context;
    private Activity activity;
    private Address address_home;

    public AdreslerimDataAdapter(ListMapData[] listdata,Activity activity){
        this.listdata = listdata;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_map_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem,activity);
        context = parent.getContext();
      //Initialize Paper
        Paper.init(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ListMapData myListData = listdata[position];
        holder.textView.setText(listdata[position].getDescription());
        holder.imageView.setImageResource(listdata[position].getImgId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position == 0){
                    //Send Home Address

                    address_home = Paper.book().read("home_address");
                    if(address_home != null) {
                        String getName_home = address_home.getName();
                        double lat_home = address_home.getLat();
                        double lng_home = address_home.getLng();
                        Log.i("name", getName_home);

                    }

                }else if(position == 1){
                    //Send Work Address

                    address_home = Paper.book().read("job_address");
                    if(address_home != null) {
                        String getName_home = address_home.getName();
                        double lat_home = address_home.getLat();
                        double lng_home = address_home.getLng();
                        Log.i("name", getName_home);

                    }

                }else if(position == 2){
                    //Send Favori Address

                    address_home = Paper.book().read("favorite_address");
                    if(address_home != null) {
                        String getName_home = address_home.getName();
                        double lat_home = address_home.getLat();
                        double lng_home = address_home.getLng();
                        Log.i("name", getName_home);
                    }
                }
            }
        });

        holder.imgbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    //Home Address
                    /*Intent go_homeAddress = new Intent(context,HomeAddress.class);
                    context.startActivity(go_homeAddress);*/
                }else if(position == 1){
                    //Work Address
                    /*Intent go_jobAddress = new Intent(context,JobAddress.class);
                    context.startActivity(go_jobAddress);*/
                }else if(position == 2){
                    //Favori Address
                    /*Intent go_favoriteAddress = new Intent(context,FavoriteAddress.class);
                    context.startActivity(go_favoriteAddress);*/
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
        public ImageButton imgbtn_add;
        public RelativeLayout relativeLayout;
        private Activity activity;

        public ViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.imgbtn_add = (ImageButton) itemView.findViewById(R.id.imgbtn);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            this.activity = activity;
        }
    }

}
