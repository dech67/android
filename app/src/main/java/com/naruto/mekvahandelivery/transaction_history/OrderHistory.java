package com.naruto.mekvahandelivery.transaction_history;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.naruto.mekvahandelivery.NavActivity;
import com.naruto.mekvahandelivery.R;
import com.naruto.mekvahandelivery.common_files.LoginSessionManager;
import com.naruto.mekvahandelivery.common_files.MySingleton;
import com.naruto.mekvahandelivery.upcoming_orders.MyListDataUpcomingBooking;
import com.naruto.mekvahandelivery.upcoming_orders.UpcomingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.NO_OF_RETRY;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.RETRY_SECONDS;
import static com.naruto.mekvahandelivery.common_files.LoginSessionManager.ACCESS_TOKEN;
import static com.naruto.mekvahandelivery.common_files.LoginSessionManager.TOKEN_TYPE;

public class OrderHistory extends AppCompatActivity {

    private LoginSessionManager sessionManager;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static final String myUrl = "https://mekvahan.com/api/delivery_boy/my_transaction";

    private List<MyListDataUpcomingBooking> mBookingList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Delivery report</font>"));
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.chart_deep_red), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
        catch (Exception e){

        }

        sessionManager = new LoginSessionManager(this);

        recyclerView = findViewById(R.id.recyclerView_1);

        mBookingList = new ArrayList<>();


        mSwipeRefreshLayout  = findViewById(R.id.swipe_to_refresh);

        upcoming();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Need to fetch all orders
                upcoming();
            }
        });

    }
    private void upcoming() {

        mSwipeRefreshLayout.setRefreshing(true);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try {

                        mSwipeRefreshLayout.setRefreshing(false);
                        if (mBookingList.size() > 0)
                            mBookingList.clear();

                        JSONObject Object = new JSONObject(response);

                        JSONArray jsonArray = Object.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String bookingId = "#"+jsonObject.getString("booking_id");
                            String my_amount = jsonObject.getString("my_amount");

                            String service_type=jsonObject.getString("service_type");
                            String otp=jsonObject.getString("otp");


                            JSONObject vehicle=jsonObject.getJSONObject("vehicle");

                            String vehicle_name= vehicle.getString("name");
                            String vehicle_type=vehicle.getString("vehicle_type");
                            //vehicle_logo
                            JSONArray companyId =vehicle.getJSONArray("company_id");
                            JSONObject object2 = companyId.getJSONObject(0);
                            String vehicleBrand =object2.getString("name");
                            JSONArray logo =object2.getJSONArray("logo");
                            String logo_url = logo.getString(1);
                            //vehicle_image
                            JSONArray images =vehicle.getJSONArray("images");
                            String image_url = images.getString(1);


                            String name="",mobile="",address="",latitude="",longitude="",
                                    action1="",action2="",action3="",action4="",action5="",action6="",
                                    action7="",action8="",action9="",action10="",action11="",action12="",
                                    action13="",action14="",action15="";

                            String serviceName="";
                            String categoryName="";

                            int status_id = jsonObject.getJSONObject("category").getInt("status_id");


                            if(service_type.contains("regular_service")){
                                JSONObject regular_service=jsonObject.getJSONObject("regular_service");
                                categoryName=regular_service.getString("category");
                                serviceName = regular_service.getString("service_name");


                                String[] action_1 = (regular_service.getString("action1").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action1 = action_1[0];
                                String[] action_2 = (regular_service.getString("action2").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action2=action_2[0];
                                String[] action_3 = (regular_service.getString("action3").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action3=action_3[0];
                                String[] action_4 = (regular_service.getString("action4").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action4=action_4[0];
                                String[] action_5 = (regular_service.getString("action5").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action5=action_5[0];
                                String[] action_6 = (regular_service.getString("action6").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action6=action_6[0];
                                String[] action_7 = (regular_service.getString("action7").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action7=action_7[0];
                                String[] action_8 = (regular_service.getString("action8").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action8=action_8[0];
                                String[] action_9 = (regular_service.getString("action9").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action9=action_9[0];
                                String[] action_10 = (regular_service.getString("action10").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action10=action_10[0];
                                String[] action_11 = (regular_service.getString("action11").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action11=action_11[0];
                                String[] action_12 = (regular_service.getString("action12").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action12=action_12[0];
                                String[] action_13 = (regular_service.getString("action13").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action13=action_13[0];
                                String[] action_14 = (regular_service.getString("action14").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action14=action_14[0];
                                String[] action_15 = (regular_service.getString("action15").replaceAll("[^a-zA-Z0-9(), ]+", "")).split(",");
                                action15=action_15[0];



                                if(status_id==3 ){
                                    JSONArray partner=jsonObject.getJSONArray("partner");
                                    JSONObject part=partner.getJSONObject(0);

                                    name=part.getString("center_name");
                                    mobile=part.getString("mobile");
                                    address=part.getString("address");
                                    latitude=part.getString("latitude");
                                    longitude=part.getString("longitude");

                                }

                                if(status_id==5 ){
                                    JSONArray customer=jsonObject.getJSONArray("customer");
                                    JSONObject cust=customer.getJSONObject(0);


                                    name=cust.getString("name");
                                    mobile=cust.getString("mobile");
                                    JSONObject drop_add=jsonObject.getJSONObject("pickup_address");
                                    address=drop_add.getString("address");
                                    try {
                                        latitude=drop_add.getString("latitude");
                                        longitude=drop_add.getString("longitide");
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }

                            }

                            if(service_type.contains("sos_service")){
                                serviceName=jsonObject.getJSONObject("sos_service").getString("service_name");

                                if(status_id==5){
                                    JSONArray customer=jsonObject.getJSONArray("customer");
                                    JSONObject cust=customer.getJSONObject(0);

                                    name=cust.getString("name");
                                    mobile=cust.getString("mobile");
                                    JSONObject pick_add=jsonObject.getJSONObject("pickup_address");
                                    address=pick_add.getString("address");
                                    try {
                                        latitude=pick_add.getString("latitude");
                                        longitude=pick_add.getString("longitude");
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                                if(status_id==8){
                                    JSONArray customer=jsonObject.getJSONArray("customer");
                                    JSONObject cust=customer.getJSONObject(0);

                                    name=cust.getString("name");
                                    mobile=cust.getString("mobile");
                                    JSONObject drop_add=jsonObject.getJSONObject("drop_address");
                                    address=drop_add.getString("address");
                                    try {
                                        latitude=drop_add.getString("latitude");
                                        longitude=drop_add.getString("longitude");
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }

                            }



                            String drop_date=jsonObject.getString("pickup_date");
                            String drop_time=jsonObject.getString("pickup_time");

                            String createdDate = jsonObject.getJSONObject("created_at").getString("date");

                            String[] str = createdDate.split(" ");
                            String serviceDate=str[0];
                            String serviceTime=str[1];


                            String amount=jsonObject.getString("cod");

                            String status = jsonObject.getJSONObject("category").getString("status_title");

                            String licencePlate=jsonObject.getString("license_plate");
                            String paymentStatus="Payment awaiting";

                            mBookingList.add(new MyListDataUpcomingBooking(status,serviceDate,serviceTime, logo_url,mobile,vehicle_name,
                                    licencePlate, bookingId, paymentStatus,serviceName,image_url,otp,name,address,latitude,longitude,
                                    drop_date,drop_time,amount,vehicleBrand,service_type,action1,action2,action3,action4,action5,action6,
                                    action7,action8,action9,action10,action11,action12,action13,action14,action15,status_id,vehicle_type,my_amount
                            ));
                            Log.e("TRANS:",bookingId);


                        }


                        adapter = new OrderHistoryAdapter(this, (ArrayList<MyListDataUpcomingBooking>) mBookingList);

                        recyclerView.hasFixedSize();
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setAdapter(adapter);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            Log.e("TAG", error.toString());
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Authorization", sessionManager.getUserDetailsFromSP()
                        .get(LoginSessionManager.TOKEN_TYPE)+" "+sessionManager.getUserDetailsFromSP()
                        .get(LoginSessionManager.ACCESS_TOKEN));
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS*1000),
                NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}



