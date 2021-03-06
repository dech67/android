package com.naruto.mekvahandelivery.ongoing_orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.naruto.mekvahandelivery.R;
import com.naruto.mekvahandelivery.customer_pickup.OnGoingBookingVendorDrop;
import com.naruto.mekvahandelivery.vendor_pickup.OngoingBookingCustomerDrop;
import java.util.ArrayList;

import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.callIntent;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.getDate;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.getTime;

public class OngoingAdapter_1 extends RecyclerView.Adapter<OngoingAdapter_1.ViewHolder> {

    private Context context;
    private ArrayList<MyListDataOngoingBooking> dataArrayList;

    OngoingAdapter_1(Context context, ArrayList<MyListDataOngoingBooking> dataArrayList) {
        this.context=context;
        this.dataArrayList = dataArrayList;

    }

    @NonNull
    @Override
    public OngoingAdapter_1.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_item, viewGroup, false);
        return new OngoingAdapter_1.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

       final MyListDataOngoingBooking data = dataArrayList.get(i);

       viewHolder.textViewstatus.setText(data.getstatus());

        viewHolder.tv_date.setText(getDate(data.getDate()));
        viewHolder.tv_time.setText(getTime(data.getTime()));


        viewHolder.tv_modelName.setText(data.getModelName());
        viewHolder.tv_numberPlate.setText(data.getNumberPlate());
        viewHolder.tv_bookingid.setText(data.getOrderId());
        viewHolder.tv_serviceType.setText(data.getService_name());

        viewHolder.tv_needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callIntent(context,"123456789");
            }
        });

        viewHolder.ll_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callIntent(context,data.getMobile());
            }
        });

        String payment_status=data.getPayment_status();
        if(payment_status.contains("Payment awaiting")){
            viewHolder.tv_paymentStatus.setText(payment_status);
            viewHolder.tv_paymentStatus.setTextColor(Color.RED);
        }

        Glide.with(context).load(data.getLogo())
                .into(viewHolder.iv_logo);

        //viewHolder.textViewRupee.setText("\u20B9" + " 99");

        int statusid=data.getStatusid();
        String service_name=data.getService_type();

        if (service_name.contains("sos_service")) {
            viewHolder.cv_details.setOnClickListener(view -> {
                Intent i1= new Intent(view.getContext(), OngoingBookingCustomerDrop.class);
                i1.putExtra("name",data.getName());
                i1.putExtra("vehicletype",data.getVehicle_type());
                i1.putExtra("bookingid",data.getOrderId());
                i1.putExtra("address",data.getAddress());
                i1.putExtra("latitude",data.getLatitude());
                i1.putExtra("longitude",data.getLongitude());
                i1.putExtra("dropDate",data.getDrop_date());
                i1.putExtra("dropTime",data.getDrop_time());
                i1.putExtra("amount",data.getAmount());
                i1.putExtra("otp",data.getOtp());
                i1.putExtra("mobile",data.getMobile());
                i1.putExtra("servicename",data.getService_name());
                i1.putExtra("action1",data.getAction1());
                i1.putExtra("action2",data.getAction2());
                i1.putExtra("action3",data.getAction3());
                i1.putExtra("action4",data.getAction4());
                i1.putExtra("action5",data.getAction5());
                i1.putExtra("action6",data.getAction6());
                i1.putExtra("action7",data.getAction7());
                i1.putExtra("action8",data.getAction8());
                i1.putExtra("action9",data.getAction9());
                i1.putExtra("action10",data.getAction10());
                i1.putExtra("action11",data.getAction11());
                i1.putExtra("action12",data.getAction12());
                i1.putExtra("action13",data.getAction13());
                i1.putExtra("action14",data.getAction14());
                i1.putExtra("action15",data.getAction15());
                view.getContext().startActivity(i1);

            });

        }

        else if (service_name.contains("regular_service")) {

            if(statusid==3){

                viewHolder.cv_details.setOnClickListener(view -> {
                    Intent i1 = new Intent(view.getContext(), OnGoingBookingVendorDrop.class);
                    i1.putExtra("name",data.getName());
                    i1.putExtra("vehicletype",data.getVehicle_type());
                    i1.putExtra("bookingid",data.getOrderId());
                    i1.putExtra("address",data.getAddress());
                    i1.putExtra("latitude",data.getLatitude());
                    i1.putExtra("longitude",data.getLongitude());
                    i1.putExtra("dropDate",data.getDrop_date());
                    i1.putExtra("dropTime",data.getDrop_time());
                    i1.putExtra("amount",data.getAmount());
                    i1.putExtra("otp",data.getOtp());
                    i1.putExtra("mobile",data.getMobile());
                    i1.putExtra("vehiclename",data.getModelName());
                    i1.putExtra("vehiclebrand",data.getVehicleBrand());
                    i1.putExtra("numberplate",data.getNumberPlate());
                    i1.putExtra("imageurl",data.getImage_url());
                    i1.putExtra("servicename",data.getService_name());
                    i1.putExtra("action1",data.getAction1());
                    i1.putExtra("action2",data.getAction2());
                    i1.putExtra("action3",data.getAction3());
                    i1.putExtra("action4",data.getAction4());
                    i1.putExtra("action5",data.getAction5());
                    i1.putExtra("action6",data.getAction6());
                    i1.putExtra("action7",data.getAction7());
                    i1.putExtra("action8",data.getAction8());
                    i1.putExtra("action9",data.getAction9());
                    i1.putExtra("action10",data.getAction10());
                    i1.putExtra("action11",data.getAction11());
                    i1.putExtra("action12",data.getAction12());
                    i1.putExtra("action13",data.getAction13());
                    i1.putExtra("action14",data.getAction14());
                    i1.putExtra("action15",data.getAction15());
                    i1.putExtra("image1",data.getImage1());
                    i1.putExtra("image2",data.getImage2());
                    i1.putExtra("image3",data.getImage3());
                    i1.putExtra("image4",data.getImage4());
                    i1.putExtra("image5",data.getImage5());
                    i1.putExtra("image6",data.getImage6());
                    view.getContext().startActivity(i1);

                });

            }

            if(statusid==5){
                viewHolder.cv_details.setOnClickListener(view -> {
                    Intent i1= new Intent(view.getContext(), OngoingBookingCustomerDrop.class);
                    i1.putExtra("name",data.getName());
                    i1.putExtra("vehicletype",data.getVehicle_type());
                    i1.putExtra("bookingid",data.getOrderId());
                    i1.putExtra("address",data.getAddress());
                    i1.putExtra("latitude",data.getLatitude());
                     i1.putExtra("longitude",data.getLongitude());
                    i1.putExtra("dropDate",data.getDrop_date());
                    i1.putExtra("dropTime",data.getDrop_time());
                    i1.putExtra("amount",data.getAmount());
                    i1.putExtra("otp",data.getOtp());
                    i1.putExtra("mobile",data.getMobile());
                    i1.putExtra("vehiclename",data.getModelName());
                    i1.putExtra("vehiclebrand",data.getVehicleBrand());
                    i1.putExtra("numberplate",data.getNumberPlate());
                    i1.putExtra("imageurl",data.getImage_url());
                    i1.putExtra("servicename",data.getService_name());
                    i1.putExtra("action1",data.getAction1());
                    i1.putExtra("action2",data.getAction2());
                    i1.putExtra("action3",data.getAction3());
                    i1.putExtra("action4",data.getAction4());
                    i1.putExtra("action5",data.getAction5());
                    i1.putExtra("action6",data.getAction6());
                    i1.putExtra("action7",data.getAction7());
                    i1.putExtra("action8",data.getAction8());
                    i1.putExtra("action9",data.getAction9());
                    i1.putExtra("action10",data.getAction10());
                    i1.putExtra("action11",data.getAction11());
                    i1.putExtra("action12",data.getAction12());
                    i1.putExtra("action13",data.getAction13());
                    i1.putExtra("action14",data.getAction14());
                    i1.putExtra("action15",data.getAction15());

                    view.getContext().startActivity(i1);

                });
            }

        }

    }

    @Override
    public int getItemCount() {
        if (dataArrayList != null) {
            return dataArrayList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_bookingid, textViewstatus, tv_numberPlate,tv_serviceType,tv_paymentStatus,tv_date,tv_time,tv_modelName,tv_needHelp;
        private ImageView iv_logo;
        private CardView cv_details;
        private LinearLayout ll_connect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cv_details= itemView.findViewById(R.id.cv_details);
            textViewstatus= itemView.findViewById(R.id.status);
            tv_bookingid= itemView.findViewById(R.id.booking_id);
            tv_numberPlate= itemView.findViewById(R.id.numberPlate);
            tv_serviceType= itemView.findViewById(R.id.service_type);
            tv_paymentStatus= itemView.findViewById(R.id.payment_status);
            tv_date= itemView.findViewById(R.id.date);
            tv_time= itemView.findViewById(R.id.time);
            tv_modelName= itemView.findViewById(R.id.mode_name);
            iv_logo= itemView.findViewById(R.id.logo);
            tv_needHelp=itemView.findViewById(R.id.tv_needhelp);
            ll_connect=itemView.findViewById(R.id.ll_connect);

        }
    }
}