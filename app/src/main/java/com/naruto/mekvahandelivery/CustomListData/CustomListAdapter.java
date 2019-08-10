package com.naruto.mekvahandelivery.CustomListData;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.naruto.mekvahandelivery.OngoingOrders.MyListDataOngoingBooking;
import com.naruto.mekvahandelivery.R;
import com.naruto.mekvahandelivery.UpcomingOrders.MyListDataUpcomingBooking;
import com.naruto.mekvahandelivery.history.Car_Data;
import com.naruto.mekvahandelivery.history.Service_Completed;

import java.util.ArrayList;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private ArrayList<String> data;

    public CustomListAdapter(ArrayList<String> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.data.setText(data.get(i));
        if(i%2!=0){
            holder.fm.setBackgroundResource(R.drawable.frame_rect);
        }





    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView data;
        private FrameLayout fm;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            data = view.findViewById(R.id.tv_data);
            fm=view.findViewById(R.id.fl_framelayout);

        }
    }
}
