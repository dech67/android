package com.naruto.mekvahandelivery.customer_pickup;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.naruto.mekvahandelivery.R;
import com.naruto.mekvahandelivery.ScanQrcode;
import com.naruto.mekvahandelivery.custom_list_data.CustomListAdapter;
import com.naruto.mekvahandelivery.customer_report.ViewCustomerReport;
import com.naruto.mekvahandelivery.upcoming_orders.PickupImageAdapter;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.callIntent;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.getDate;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.getTime;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.sendNavigateIntent;

public class OnGoingBookingVendorDrop extends AppCompatActivity {

    public ArrayList<String> arrayList;
    public ArrayList<String> arrayListsend;
    private RecyclerView recyclerView,recyclerViewPickupImage;
    private RecyclerView.Adapter adapter, pickupImageAdapter;
    private LinearLayout navigation;
    private TextView tvDetails, date, time, name, address, vehicleBrand, vehicleName, numberPlate, serviceName;
    private ImageView call, iv_qrcode, vehicle_image;
    private Button report_show, drop;
    private ArrayList<Uri> pickup_image;
    private Uri image_1, image_2, image_3, image_4, image_5, image_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoingbooking_vendordrop);

        tvDetails = findViewById(R.id.tvDetails_1);

        report_show = findViewById(R.id.report_view);

        call = findViewById(R.id.call);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        drop = findViewById(R.id.bt_drop);
        navigation = findViewById(R.id.ll_navigation);
        vehicle_image = findViewById(R.id.iv_carimage);

        date = findViewById(R.id.tv_date);
        time = findViewById(R.id.tv_time);
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        vehicleBrand = findViewById(R.id.tv_vehiclebrand);
        vehicleName = findViewById(R.id.tv_vehiclename);
        numberPlate = findViewById(R.id.tv_numberPlate);
        serviceName = findViewById(R.id.tv_servicename);

        arrayList = new ArrayList<>();
        arrayListsend = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String name_1 = bundle.getString("name");
        String vehicle_type = bundle.getString("vehicletype");
        String bookingid = bundle.getString("bookingid");
        String address_1 = bundle.getString("address");
        double latitude = Double.parseDouble(bundle.getString("latitude"));
        double longitude = Double.parseDouble(bundle.getString("longitude"));
        String dropdate = bundle.getString("dropDate");
        String dropTime = bundle.getString("dropTime");
        String amount = bundle.getString("amount");
        String otp_1 = bundle.getString("otp");
        String mobileNo = bundle.getString("mobile");
        String vehiclename = bundle.getString("vehiclename");
        String vehiclebrand = bundle.getString("vehiclebrand");
        String numberplate = bundle.getString("numberplate");
        String vehicleImageUrl = bundle.getString("imageurl");
        String servicename = bundle.getString("servicename");
        String action1 = bundle.getString("action1");
        String action2 = bundle.getString("action2");
        String action3 = bundle.getString("action3");
        String action4 = bundle.getString("action4");
        String action5 = bundle.getString("action5");
        String action6 = bundle.getString("action6");
        String action7 = bundle.getString("action7");
        String action8 = bundle.getString("action8");
        String action9 = bundle.getString("action9");
        String action10 = bundle.getString("action10");
        String action11 = bundle.getString("action11");
        String action12 = bundle.getString("action12");
        String action13 = bundle.getString("action13");
        String action14 = bundle.getString("action14");
        String action15 = bundle.getString("action15");
        String image1 = bundle.getString("image1");
        String image2 = bundle.getString("image2");
        String image3 = bundle.getString("image3");
        String image4 = bundle.getString("image4");
        String image5 = bundle.getString("image5");
        String image6 = bundle.getString("image6");

        image_1 =Uri.parse(image1);
        image_2 =Uri.parse(image2);
        image_3 =Uri.parse(image3);
        image_4 =Uri.parse(image4);
        image_5 =Uri.parse(image5);
        image_6 =Uri.parse(image6);

        pickup_image = new ArrayList<>();
        pickup_image.add(image_1);
        pickup_image.add(image_2);
        pickup_image.add(image_3);
        pickup_image.add(image_4);
        pickup_image.add(image_5);
        pickup_image.add(image_6);

        recyclerViewPickupImage = findViewById(R.id.rv_pickup_image);
        recyclerViewPickupImage.hasFixedSize();
        recyclerViewPickupImage.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
        pickupImageAdapter = new PickupImageAdapter(pickup_image, OnGoingBookingVendorDrop.this,"ongoing");
        recyclerViewPickupImage.setAdapter(pickupImageAdapter);

        arrayList.add(action1);
        arrayList.add(action2);
        arrayList.add(action3);
        arrayList.add(action4);
        arrayList.add(action5);
        arrayList.add(action6);
        arrayList.add(action7);
        arrayList.add(action8);
        arrayList.add(action9);
        arrayList.add(action10);
        arrayList.add(action11);
        arrayList.add(action12);
        arrayList.add(action13);
        arrayList.add(action14);
        arrayList.add(action15);

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).isEmpty()) {
                break;
            } else {
                arrayListsend.add(arrayList.get(i));
            }
        }

        recyclerView = findViewById(R.id.recyclerView_listView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        adapter = new CustomListAdapter(arrayListsend, "ongoing","partner_drop");
        recyclerView.setAdapter(adapter);

        name.setText(name_1);
        address.setText(address_1);

        date.setText(getDate(dropdate));
        time.setText(getTime(dropTime));
        vehicleName.setText(vehiclename);
        vehicleBrand.setText(vehiclebrand);
        numberPlate.setText(numberplate);
        serviceName.setText(servicename);

        try {
            Glide.with(OnGoingBookingVendorDrop.this).load(vehicleImageUrl)
                    .into(vehicle_image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        generateQrcode(otp_1);

        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setBackgroundColor(getColor(R.color.offwhite_01));
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional

        imagePopup.initiatePopup(iv_qrcode.getDrawable());

        iv_qrcode.setOnClickListener(view -> imagePopup.viewPopup());

        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OnGoingBookingVendorDrop.this, ScanQrcode.class);
                i.putExtra("otp", otp_1);
                startActivity(i);
            }
        });

        navigation.setOnClickListener(view -> sendNavigateIntent(OnGoingBookingVendorDrop.this, latitude, longitude));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setTitle(Html.fromHtml("Order id: " + bookingid));
        final Drawable upArrow = getDrawable(R.drawable.ic_keyboard_backspace_black_24dp);
        assert upArrow != null;
        upArrow.setColorFilter(getColor(R.color.chart_deep_red), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        tvDetails.setOnClickListener(new View.OnClickListener() {
            int check = 1;

            @Override
            public void onClick(View view) {

                if (check == 1) {
                    recyclerView.setVisibility(View.VISIBLE);
                    check = 0;
                } else {
                    recyclerView.setVisibility(View.GONE);
                    check = 1;
                }


            }
        });

        call.setOnClickListener(view -> callIntent(OnGoingBookingVendorDrop.this, mobileNo));
        report_show.setOnClickListener(view -> {
            Intent intent = new Intent(OnGoingBookingVendorDrop.this, ViewCustomerReport.class);
            intent.putExtra("bookingId", bookingid);
            intent.putExtra("vehicletype", vehicle_type);
            startActivity(intent);
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void generateQrcode(String otp) {
        try {
            //setting size of qr code
            int width = 300;
            int height = 300;
            int smallestDimension = width < height ? width : height;


            String qrCodeData = otp;
            //setting parameters for qr code
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            CreateQRCode(qrCodeData, charset, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }

    public void CreateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {


        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    pixels[offset + x] = matrix.get(x, y) ?
                            ResourcesCompat.getColor(getResources(), R.color.chart_deep_red, null) : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view

            Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.mek_logo);

            // iv_qrcode.setImageBitmap(mergeBitmaps(overlay,bitmap));
            iv_qrcode.setImageBitmap(bitmap);

        } catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
        }
    }


}