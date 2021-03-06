package com.naruto.mekvahandelivery.customer_report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.naruto.mekvahandelivery.R;
import com.naruto.mekvahandelivery.common_files.LoginSessionManager;
import com.naruto.mekvahandelivery.signature.SignatureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.BASE;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.NO_OF_RETRY;
import static com.naruto.mekvahandelivery.common_files.CommonVaribalesFunctions.RETRY_SECONDS;

@SuppressLint("SetTextI18n")
public class AddCustomerReport extends AppCompatActivity implements Car_Add_fragment.OnFragmentInteractionListener,
        Bike_Add_fragment.OnFragmentInteractionListener, AddCustomerReportAdapter.OnAdapterClickListener {

    private FrameLayout car, bike;
    private Button btn,addDetails;
    private ImageView car_image, bike_image, img_sign;
    private TextView tvbike, tvcar, document;
    RecyclerView.Adapter imageDocumentAdapter;
    RecyclerView imageDocumentView;
    File mPhotoFile;
    private LoginSessionManager sessionManager;

    Bitmap bmp = null;

    private String bookingId, newBId, buttonId, vehicle_type;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int rv_index = 6;
    private Uri photoURI = null;
    private Map<String, String> carButton, bikeButton, reportButton;
    private Map<String, Integer> dataIndex;
    private List<AddCustomerReportData> reportDocument;
    private SeekBar sk_fuelmeter;
    private String meter_percentage="40";
    private EditText headRest,floorMats,mudFlaps,seatCover,otherReport,odoMeter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_report);

        carButton = new HashMap<>();
        carButton = initCarData();
        bikeButton = new HashMap<>();
        bikeButton = initBikeData();
        reportButton = new HashMap<>();
        reportButton = initReportData();
        dataIndex = new HashMap<>();
        dataIndex = initIndex();
        reportDocument = new ArrayList<>();
        reportDocument = initDocumentData();

        bookingId = getIntent().getStringExtra("bookingid");

        assert bookingId != null;
        newBId = bookingId.substring(1);



        try{
            final Drawable upArrow = getDrawable(R.drawable.ic_keyboard_backspace_black_24dp);
            if (getSupportActionBar() != null && upArrow != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Customer report</font>"));
                upArrow.setColorFilter(getColor(R.color.chart_deep_red), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        headRest= findViewById(R.id.et_headrest);
        floorMats = findViewById(R.id.et_floormats);
        mudFlaps = findViewById(R.id.et_mudflap);
        seatCover = findViewById(R.id.et_seatcover);
        otherReport = findViewById(R.id.et_otherreport);
        odoMeter = findViewById(R.id.et_odometer);
        addDetails = findViewById(R.id.bt_done);
        car = findViewById(R.id.frame_2);
        bike = findViewById(R.id.frame_1);
        car_image = findViewById(R.id.car_image);
        bike_image = findViewById(R.id.bike_image);
        tvbike = findViewById(R.id.tvbike);
        tvcar = findViewById(R.id.tvcar);
        document = findViewById(R.id.tvDocument);
        img_sign=findViewById(R.id.image_sign);
        sessionManager = new LoginSessionManager(this);
        Button take_sign = findViewById(R.id.bt_sign);

        sk_fuelmeter=findViewById(R.id.sb_fuelmeter);

        ImageView img_cancel = findViewById(R.id.image_cross);
        imageDocumentView = findViewById(R.id.rv_imagedocument);
        imageDocumentView.setHasFixedSize(false);
        imageDocumentView.setLayoutManager(new LinearLayoutManager(imageDocumentView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        imageDocumentAdapter = new AddCustomerReportAdapter(reportDocument, imageDocumentView.getContext());
        imageDocumentView.setAdapter(imageDocumentAdapter);

        vehicle_type =getIntent().getStringExtra("vehicletype");

        if (vehicle_type != null) {
            if(vehicle_type.equalsIgnoreCase("car")){
                loadCarFragment();
                load_car();
            }

            else if(vehicle_type.equalsIgnoreCase("bike")){
                loadBikeFragment();
                load_bike();
            }
        }

//        fuel meter
        sk_fuelmeter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                meter_percentage=(Integer.toString(progressChangedValue));
                Log.e("TAG",meter_percentage);
            }
        });

        take_sign.setOnClickListener(view -> {
           if(!isWriteStoragePermissionGranted()){
              return;
           }
            Intent i=new Intent(AddCustomerReport.this, SignatureActivity.class);
            startActivityForResult(i,2);

        });
        img_cancel.setOnClickListener(view -> img_sign.setImageResource(R.drawable.image_svg));

        addDetails.setOnClickListener(view -> {
            if (vehicle_type.equalsIgnoreCase("car")) {



               sendCarReport();
            } else {
                sendBikeReport();
            }
        });





    }


    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted2");
                return true;
            } else {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted2");
            return true;
        }
    }

    private void sendCarReport() {

        String rc = reportDocument.get(0).getBtnstate();
        String puc = reportDocument.get(1).getBtnstate();
        String insurance = reportDocument.get(2).getBtnstate();

        if(rc.contains("0")){
            Snackbar.make(addDetails, "Please upload RC",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }
        if(puc.contains("0")){
            Snackbar.make(addDetails, "Please upload PUC",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }
        if(insurance.contains("0")){
            Snackbar.make(addDetails, "Please upload Insurance",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }

        if (reportDocument.size()<8) {
            Snackbar.make(addDetails, "Please upload all images!",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading report....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE+"CarRegularServiceReport",
                response -> {

            String resultResponse = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(resultResponse);
                        Toast.makeText(getApplicationContext(), "Customer report Upload..!", Toast.LENGTH_LONG).show();
                        Log.e("data check", jsonObject.toString());
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        JSONArray documentDataArray = dataObject.getJSONArray("rc");
                        Log.e("response report", documentDataArray.getString(1)+" new bookingId:" +
                                dataObject.getString("booking_id"));

                        progressDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("upload_status", "true");
                        setResult(101, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Failed to Upload..!", Toast.LENGTH_LONG).show();
            NetworkResponse networkResponse = error.networkResponse;
            String errorMessage = "Unknown error";
            if (networkResponse == null) {
                if (error.getClass().equals(TimeoutError.class)) {
                    errorMessage = "Request timeout";
                } else if (error.getClass().equals(NoConnectionError.class)) {
                    errorMessage = "Failed to connect server";
                }
            } else {
                String result = new String(networkResponse.data);
                try {
                    Log.e("result string", newBId+" "+result);
                    JSONObject response = new JSONObject(result);
                    String status = response.getString("status");
                    String message = response.getString("message");

                    Log.e("Error Status", status);
                    Log.e("Error Message", message);

                    switch (networkResponse.statusCode) {
                        case 404:
                            errorMessage = "Resource not found";
                            break;
                        case 401:
                            errorMessage = message + " Please login again";
                            break;
                        case 400:
                            errorMessage = message + " Check your inputs";
                            break;
                        case 500:
                            errorMessage = message + " Something is getting wrong";
                            break;
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            Log.i("Error", errorMessage);
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> bodyparams = new HashMap<>();
                bodyparams.put("booking_id", newBId);
                bodyparams.put("tool_kit", carButton.get("toolkit"));
                bodyparams.put("stepney", carButton.get("stepney"));
                bodyparams.put("mudguard", carButton.get("mudguard"));
                bodyparams.put("mats", carButton.get("mats"));
                bodyparams.put("keychain", carButton.get("keychain"));
                bodyparams.put("service_book", carButton.get("servicebook"));
                bodyparams.put("wheel_cover", carButton.get("wheelcover"));
                bodyparams.put("lock", carButton.get("lock"));
                bodyparams.put("jack_handle", carButton.get("jackhandle"));
                bodyparams.put("carpet", carButton.get("carpet"));
                bodyparams.put("stereo_panel", carButton.get("stereopanel"));
                bodyparams.put("speakers", carButton.get("speakers"));
                bodyparams.put("car_cover", "0");    //?????
                bodyparams.put("seat_cover",seatCover.getText().toString() );   
                bodyparams.put("meter_percentage", meter_percentage); //fuelmeter seekbar
                bodyparams.put("odometer", odoMeter.getText().toString());
                bodyparams.put("description", otherReport.getText().toString());
                bodyparams.put("battery_info", reportButton.get("sbrand"));
                bodyparams.put("miscellaneous", "0");    //??????
                bodyparams.put("head_rest", headRest.getText().toString());
                bodyparams.put("floor_mats", floorMats.getText().toString());
                bodyparams.put("wheel_cap", reportButton.get("wheelcap"));
                bodyparams.put("mud_flap", mudFlaps.getText().toString());

                return bodyparams;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("rc", new DataPart(getFilename(0), getBytes(0), "image/jpeg"));
                params.put("puc", new DataPart(getFilename(1), getBytes(1), "image/jpeg"));
                params.put("insurance", new DataPart(getFilename(2), getBytes(2), "image/jpeg"));
                params.put("road_tax", new DataPart(getFilename(3), getBytes(3), "image/jpeg"));
                params.put("pollution_paper", new DataPart(getFilename(4), getBytes(4), "image/jpeg"));
                params.put("passenger_tax", new DataPart(getFilename(5), getBytes(5), "image/jpeg"));
                params.put("image", new DataPart(getFilename(6), getBytes(6), "image/jpeg"));
                params.put("signature", new DataPart(getFilename(7), getBytes(7), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerParams = new HashMap<>();
                headerParams.put("Accept", "application/json");
                headerParams.put("Authorization", sessionManager.getUserDetailsFromSP()
                        .get(LoginSessionManager.TOKEN_TYPE)+" "+sessionManager.getUserDetailsFromSP()
                        .get(LoginSessionManager.ACCESS_TOKEN));
                return headerParams;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS*1000),
                NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private void sendBikeReport() {

        String rc = reportDocument.get(0).getBtnstate();
        String puc = reportDocument.get(1).getBtnstate();
        String insurance = reportDocument.get(2).getBtnstate();

        if(rc.contains("0")){
            Snackbar.make(addDetails, "Please upload RC",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }
        if(puc.contains("0")){
            Snackbar.make(addDetails, "Please upload PUC",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }
        if(insurance.contains("0")){
            Snackbar.make(addDetails, "Please upload Insurance",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }
        if (reportDocument.size()<8) {
            Snackbar.make(addDetails, "Please upload all images!",
                    BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", null).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading report....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE+"BikeRegularServiceReport",
                response -> {

                    String resultResponse = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(resultResponse);
                        Log.e("data check", jsonObject.toString());
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        JSONArray documentDataArray = dataObject.getJSONArray("rc");
                        Log.e("response report", documentDataArray.getString(1)+" new bookingId:" +
                                dataObject.getString("booking_id"));

                        progressDialog.dismiss();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Failed to Upload..!", Toast.LENGTH_LONG).show();
            NetworkResponse networkResponse = error.networkResponse;
            String errorMessage = "Unknown error";
            if (networkResponse == null) {
                if (error.getClass().equals(TimeoutError.class)) {
                    errorMessage = "Request timeout";
                } else if (error.getClass().equals(NoConnectionError.class)) {
                    errorMessage = "Failed to connect server";
                }
            } else {
                String result = new String(networkResponse.data);
                try {
                    Log.e("result string", newBId+" "+result);
                    JSONObject response = new JSONObject(result);
                    String status = response.getString("status");
                    String message = response.getString("message");

                    Log.e("Error Status", status);
                    Log.e("Error Message", message);

                    switch (networkResponse.statusCode) {
                        case 404:
                            errorMessage = "Resource not found";
                            break;
                        case 401:
                            errorMessage = message + " Please login again";
                            break;
                        case 400:
                            errorMessage = message + " Check your inputs";
                            break;
                        case 500:
                            errorMessage = message + " Something is getting wrong";
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("Error", errorMessage);
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> bodyparams = new HashMap<>();
                bodyparams.put("booking_id", newBId);
                bodyparams.put("tool_kit", bikeButton.get("toolkit"));
                bodyparams.put("first_aid_kit", bikeButton.get("firstadkit"));
                bodyparams.put("key_chain", bikeButton.get("keychain"));
                bodyparams.put("bike_cover", bikeButton.get("bikecover"));
                bodyparams.put("keychain", bikeButton.get("keychain"));
                bodyparams.put("service_book", bikeButton.get("servicebook"));
                bodyparams.put("miscellaneous", bikeButton.get("miscellenoustool"));
                bodyparams.put("helmet_lock", "0");    //?????
                bodyparams.put("seat_cover", seatCover.getText().toString());   //????
                bodyparams.put("mobile_holder", "0");   //????
                bodyparams.put("puncture_kit", "0");   //????
                bodyparams.put("wheel_lock", "0");   //????
                bodyparams.put("meter_percentage", meter_percentage); //fuelmeter seekbar
                bodyparams.put("odometer", odoMeter.getText().toString());
                bodyparams.put("description", otherReport.getText().toString());
                bodyparams.put("battery_info", reportButton.get("sbrand"));
                bodyparams.put("head_rest",headRest.getText().toString());
                bodyparams.put("floor_mats", floorMats.getText().toString());
                bodyparams.put("wheel_cap", reportButton.get("wheelcap"));
                bodyparams.put("mud_flap", mudFlaps.getText().toString());

                return bodyparams;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("rc", new DataPart(getFilename(0), getBytes(0), "image/jpeg"));
                params.put("puc", new DataPart(getFilename(1), getBytes(1), "image/jpeg"));
                params.put("insurance", new DataPart(getFilename(2), getBytes(2), "image/jpeg"));
                params.put("road_tax", new DataPart(getFilename(3), getBytes(3), "image/jpeg"));
                params.put("pollution_paper", new DataPart(getFilename(4), getBytes(4), "image/jpeg"));
                params.put("passenger_tax", new DataPart(getFilename(5), getBytes(5), "image/jpeg"));
                params.put("image", new DataPart(getFilename(6), getBytes(6), "image/jpeg"));
                params.put("signature", new DataPart(getFilename(7), getBytes(7), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerParams = new HashMap<>();
                headerParams.put("Accept", "application/json");
                headerParams.put("Authorization", sessionManager.getUserDetailsFromSP()
                        .get(LoginSessionManager.TOKEN_TYPE)+" "+sessionManager.getUserDetailsFromSP()
                        .get(LoginSessionManager.ACCESS_TOKEN));
                return headerParams;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS*1000),
                NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private String getFilename(int indx) {
        String fileName = reportDocument.get(indx).getPhotoUri().toString();
        int lindex = fileName.lastIndexOf('/');
        return  fileName.substring(lindex+1);
    }

    public byte[] getBytes(int i) throws IOException {
        Uri uri=null;
        if (i==7) {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "sign", null);
            uri = Uri.parse(path);

        } else {
            uri =reportDocument.get(i).getPhotoUri();
        }

        byte[] data = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        return data = baos.toByteArray();
    }

    // storing the index of all buttons passed in the list sequentially
    private Map<String, Integer> initIndex() {
        Map<String, Integer> dataIndex = new HashMap<>();
        dataIndex.put("bt_rc", 0);
        dataIndex.put("bt_puc", 1);
        dataIndex.put("bt_insurance", 2);
        dataIndex.put("bt_roadtax", 3);
        dataIndex.put("bt_passengertax", 4);
        dataIndex.put("bt_pollutionpaper", 5);
        return dataIndex;
    }

    private List<AddCustomerReportData> initDocumentData() {
        List<AddCustomerReportData> reportDocument = new ArrayList<>();
        reportDocument.add(new AddCustomerReportData("bt_rc", null, "0"));
        reportDocument.add(new AddCustomerReportData("bt_puc", null,"0"));
        reportDocument.add(new AddCustomerReportData("bt_insurance", null, "0"));
        reportDocument.add(new AddCustomerReportData("bt_roadtax", null, "0"));
        reportDocument.add(new AddCustomerReportData("bt_passengertax", null, "0"));
        reportDocument.add(new AddCustomerReportData("bt_pollutionpaper", null, "0"));
        return reportDocument;
    }



    private Map<String, String> initReportData() {
        Map<String, String> reportData = new HashMap<>();
        reportData.put("headrest", "0");
        reportData.put("floormats", "0");
        reportData.put("wheelcap", "0");
        reportData.put("mudflap", "0");
        reportData.put("odometer", "0");
        reportData.put("otherreport", "0");
        reportData.put("sbrand", "Exide");
        return reportData;
    }

    private Map<String, String> initBikeData() {
        Map<String, String> bikeButton = new HashMap<>();
        bikeButton.put("toolkit", "0");
        bikeButton.put("firstadkit", "0");
        bikeButton.put("keychain", "0");
        bikeButton.put("bikecover", "0");
        bikeButton.put("servicebook", "0");
        bikeButton.put("miscellenoustool", "0");
        return bikeButton;
    }

    private Map<String, String> initCarData() {
        Map<String, String> carButton = new HashMap<>();
        carButton.put("stepney", "0");
        carButton.put("toolkit", "0");
        carButton.put("mudguard", "0");
        carButton.put("keychain", "0");
        carButton.put("servicebook", "0");
        carButton.put("mats", "0");
        carButton.put("wheelcover", "0");
        carButton.put("lock", "0");
        carButton.put("jackhandle", "0");
        carButton.put("carpet", "0");
        carButton.put("stereopanel", "0");
        carButton.put("speakers", "0");
        return carButton;
    }

    private void load_bike() {
        bike.setBackgroundResource(R.color.chart_deep_red);
        tvbike.setTextColor(Color.WHITE);
        bike_image.setImageResource(R.drawable.bike_white);
        car.setBackgroundResource(R.color.white);
        tvcar.setTextColor(Color.BLACK);
        car_image.setImageResource(R.drawable.carblack);
        document.setText("Bike document");
    }

    private void load_car() {
        bike.setBackgroundResource(R.color.white);
        tvbike.setTextColor(Color.BLACK);
        bike_image.setImageResource(R.drawable.bike_black);
        car.setBackgroundResource(R.color.chart_deep_red);
        tvcar.setTextColor(Color.WHITE);
        car_image.setImageResource(R.drawable.car_white);
        document.setText("Car document");
    }

    private void loadCarFragment() {
        Car_Add_fragment c_Fragment = new Car_Add_fragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout,
                c_Fragment,
                c_Fragment.getTag()).commit();
    }

    private void loadBikeFragment() {
        Bike_Add_fragment b_Fragment = new Bike_Add_fragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout,
                b_Fragment,
                b_Fragment.getTag()).commit();
    }

    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.naruto.mekvahandelivery",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "MEK_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Save a file: path for use with ACTION_VIEW intents
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        String currentPhotoPath = image.getAbsolutePath();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

//    adding all camera images to the adapter and changing button colour
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && data!=null){
            byte[] bytes = data.getByteArrayExtra("image");
            if (bytes != null) {
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            img_sign.setImageBitmap(bmp);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            int dIndex = dataIndex.get(buttonId);
            reportDocument.set(dIndex, new AddCustomerReportData(buttonId, photoURI, "1"));
            imageDocumentAdapter.notifyItemChanged(dIndex);
            imageDocumentView.scrollToPosition(dIndex);
            btn.setBackgroundResource(R.drawable.customer_reprt_bt02);
            btn.setTextColor(getColor(android.R.color.white));
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            Log.e("no image", "no image was captured!");
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            reportDocument.add(rv_index, new AddCustomerReportData("bt_rvcamera", photoURI, "3"));
            imageDocumentAdapter.notifyItemInserted(rv_index++);
            imageDocumentView.scrollToPosition(rv_index-1);
        } else if (requestCode == 3) {
            Log.e("no image", "no image was captured!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    interface imlemented by this class for click listeners from car and bike fragments
    @Override
    public void onFragmentInteraction(View view) {
        if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof Car_Add_fragment) {
            onCarFragmentButtonClick(view);
        } else if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof Bike_Add_fragment) {
            onBikeFragmentButtonClick(view);
        }
    }

//    click listeners for buttons present in bike fragment
    private void onBikeFragmentButtonClick(View view) {
        String bikeButtonId = getResources().getResourceEntryName(view.getId());
        Button btn = findViewById(view.getId());
        bikeButtonId = bikeButtonId.substring(3);
        if (bikeButton.get(bikeButtonId).equals("0")) {
            btn.setBackgroundResource(R.drawable.customer_reprt_bt02);
            btn.setTextColor(getColor(android.R.color.white));
            bikeButton.put(bikeButtonId, "1");
        } else if (carButton.get(bikeButtonId).equals("1")) {
            btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
            btn.setTextColor(getColor(android.R.color.black));
            bikeButton.put(bikeButtonId, "0");
        }
    }

//    click listeners for buttons present in car fragment
    private void onCarFragmentButtonClick(View view) {
        String carButtonId = getResources().getResourceEntryName(view.getId());
        Button btn = findViewById(view.getId());
        carButtonId = carButtonId.substring(3);
        if (carButton.get(carButtonId).equals("0")) {
            btn.setBackgroundResource(R.drawable.customer_reprt_bt02);
            btn.setTextColor(getColor(android.R.color.white));
            carButton.put(carButtonId, "1");
        } else if (carButton.get(carButtonId).equals("1")) {
            btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
            btn.setTextColor(getColor(android.R.color.black));
            carButton.put(carButtonId, "0");
        }
    }

//    click lsitener for buttons present in Add Report Activity
    public void onAddReportButton(View view) {
        buttonId = getResources().getResourceEntryName(view.getId());

        if (!buttonId.equals("bt_rvcamera")) {
            btn = findViewById(view.getId());
            if (reportDocument.get(dataIndex.get(buttonId)).getBtnstate().equals("0")) {
                if (ContextCompat.checkSelfPermission(AddCustomerReport.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddCustomerReport.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
                    return;
                }
                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);
            } else if (reportDocument.get(dataIndex.get(buttonId)).getBtnstate().equals("1")) {
                reportDocument.set(dataIndex.get(buttonId), new AddCustomerReportData(buttonId, null, "0"));
                imageDocumentAdapter.notifyItemChanged(dataIndex.get(buttonId));
                btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
                btn.setTextColor(getColor(android.R.color.black));
            }
        } else {
            if (ContextCompat.checkSelfPermission(AddCustomerReport.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddCustomerReport.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
                return;
            }
            dispatchTakePictureIntent(3);
        }
    }

//    click listener interface from recycler adapter to implement cross imagebutton click
    public void onAddReportButton(String buttonId) {
        this.buttonId = buttonId;
        if (!buttonId.endsWith("z")) {
            btn = findViewById(getResources().getIdentifier(buttonId, "id", getApplicationContext().getPackageName()));

            reportDocument.set(dataIndex.get(buttonId), new AddCustomerReportData(buttonId, null, "0"));
            imageDocumentAdapter.notifyItemChanged(dataIndex.get(buttonId));
            btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
            btn.setTextColor(getColor(android.R.color.black));
        } else {
            buttonId = buttonId.substring(0, buttonId.length()-1);
            int final_index = Integer.parseInt(buttonId);
            reportDocument.remove(final_index);
            imageDocumentView.getRecycledViewPool().clear();
            imageDocumentAdapter.notifyItemRemoved(final_index);

            // use below code only in case of crash due to position issue
//            imageDocumentAdapter.notifyDataSetChanged();

            rv_index--;
        }
    }



    @Override
    public void onAdapterInteraction(String buttonId) {
        onAddReportButton(buttonId);
    }
}