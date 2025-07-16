package com.provizit.qrscanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.provizit.qrscanner.Services.DataManger;
import com.provizit.qrscanner.Services.Invite;
import com.provizit.qrscanner.Services.Model;
import com.provizit.qrscanner.Services.Model1;
import com.provizit.qrscanner.Services.SupplierDetail;
import com.provizit.qrscanner.Services.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    LinearLayout valid,invalid,user_data;
    ImageView dummy;
    Button cancel;
    String key1 = "";
    String key2 = "";
    String key3 = "";
    String key4 = "";
    String name = "";
    String id = "";
    TextView  name_user,id_value;
    LinearLayout  name_layout,id_layout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
          sharedPreferences = getSharedPreferences("Provizit_Scanner", MODE_PRIVATE);
         Button scanqr = findViewById(R.id.scan_qr);
         cancel = findViewById(R.id.closeButton);
         name_user = findViewById(R.id.name);
         name_layout = findViewById(R.id.name_layout);
         id_layout = findViewById(R.id.id_layout);
        user_data = findViewById(R.id.user_data);
         id_value = findViewById(R.id.id_value);
        ImageView logo = findViewById(R.id.logoc);
        ImageView logout = findViewById(R.id.logout);
        scanqr.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setPrompt("Scan a QR Code");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
              valid = findViewById(R.id.valid);
            dummy = findViewById(R.id.qr_dummy);
              invalid = findViewById(R.id.invalid);

            dummy.setVisibility(View.VISIBLE);
            valid.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            invalid.setVisibility(View.GONE);

            // 👇 Set your custom scanner activity
            integrator.setCaptureActivity(CustomScannerActivity.class);

             integrator.initiateScan(); // Launch the scanner
         });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dummy.setVisibility(View.VISIBLE);
                    valid.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    invalid.setVisibility(View.GONE);
                }
            });
         logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 logout();
             }
         });
        Glide.with(MainActivity.this)
                .load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences.getString("comp_id", "") + "/" + sharedPreferences.getString("pic", ""))
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk cache
                .error(R.drawable.img)
                .skipMemoryCache(true)  // Disable memory cache
                .into(logo);


    }
    public void logout(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply(); // or editor.commit(); if you prefer synchronous

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // ✅ QR code scanned successfully

                seperatorKeys(result.getContents());
             } else {
                // ❌ Scan was cancelled
//                Toast.makeText(this, "Invalid 1", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void seperatorKeys (String input) {

        // Step 1: Split by "###"
        String[] mainParts = input.split("###");

        System.out.println("Subahsdh " + input);
        if (mainParts.length == 3) {
              key1 = mainParts[0]; // "meeting"
            String[] subParts = mainParts[1].split("\\*\\*\\*"); // Split by "***"
            if (subParts.length == 2) {
                  key2 = subParts[0]; // "ftprovizitstc"
                  key3 = subParts[1]; // "67f75d88840fb60f417772d9"
                  key4 = mainParts[2]; // "bandanagaraju7979@gmail.com"
                scan(key1,key2,key3,key4);


            } else {
                cancel.setVisibility(View.VISIBLE);
                invalid.setVisibility(View.VISIBLE);
                valid.setVisibility(View.GONE);
                dummy.setVisibility(View.GONE);
            }
        } else {
            cancel.setVisibility(View.VISIBLE);
            invalid.setVisibility(View.VISIBLE);
            valid.setVisibility(View.GONE);
            dummy.setVisibility(View.GONE);
        }
    }
    private void scan(String key1,String key2, String key3, String key4) {
        LoaderUtil.showLoader(this);   // To show

        DataManger dataManager = DataManger.getDataManager();
        dataManager.getqrcodeStatus(new Callback<Model1>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();
                LoaderUtil.hideLoader(MainActivity.this);

//                                            progressDialog.dismiss();

//                progress.dismiss();
                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        cancel.setVisibility(View.VISIBLE);
                        invalid.setVisibility(View.VISIBLE);
                        valid.setVisibility(View.GONE);
                        dummy.setVisibility(View.GONE);
//                        Toast.makeText(MainActivity.this, "Invalid 2", Toast.LENGTH_SHORT).show();

                    } else if (statuscode.equals(not_verified)) {
                        cancel.setVisibility(View.VISIBLE);
                        invalid.setVisibility(View.VISIBLE);
                        valid.setVisibility(View.GONE);
                        dummy.setVisibility(View.GONE);
//                        Toast.makeText(MainActivity.this, "Invalid 3", Toast.LENGTH_SHORT).show();

                    } else if (statuscode.equals(successcode)) {
                        boolean isToday;


                        if (model.getItems().getStatus() != null && model.getItems().getStatus() == 1){
                            cancel.setVisibility(View.VISIBLE);
                            invalid.setVisibility(View.VISIBLE);
                            valid.setVisibility(View.GONE);
                            dummy.setVisibility(View.GONE);
//                            Toast.makeText(MainActivity.this, "Invalid 4", Toast.LENGTH_SHORT).show();
                        }
                        else if ((key1.equals("workpermit") || key1.equals("material"))  &&  model.getItems().getStatus() != null && model.getItems().getStatus() == 2){
                            cancel.setVisibility(View.VISIBLE);
                            invalid.setVisibility(View.VISIBLE);

                            valid.setVisibility(View.GONE);
                            dummy.setVisibility(View.GONE);
//                            Toast.makeText(MainActivity.this, "Invalid 5", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            user_data.setVisibility(View.VISIBLE);



                            if (key1.equals("workpermit")) {


                                ArrayList<SupplierDetail> contractorArray = new ArrayList<>();
                                ArrayList<SupplierDetail> subContractorArray = new ArrayList<>();
                                contractorArray = model.getItems().getContractorsData();
                                subContractorArray = model.getItems().getSubcontractorsData();


                                for (int i = 0; i < contractorArray.size(); i++) {

                                    if (contractorArray.get(i).getEmail().equals(key4) || contractorArray.get(i).getMobile().equals(key4)) {
                                        name = contractorArray.get(i).getName();
                                        id = contractorArray.get(i).getId_number();
                                        break;
                                    }
                                }
                                for (int i = 0; i < subContractorArray.size(); i++) {
                                    if (subContractorArray.get(i).getEmail().equals(key4) || subContractorArray.get(i).getMobile().equals(key4)) {
                                        name = subContractorArray.get(i).getName();
                                        id = subContractorArray.get(i).getId_number();
                                        break;
                                    }
                                }

                            } else if (key1.equals("material") ) {


                                    ArrayList<SupplierDetail> supplierArray = new ArrayList<>();
                                    supplierArray = model.getItems().getSupplier_details();


                                    for (int i = 0; i < supplierArray.size(); i++) {
                                        if (supplierArray.get(i).getSupplier_email().equals(key4)) {
                                            name = supplierArray.get(i).getContact_person();
                                            id = supplierArray.get(i).getId_number();
                                            break;
                                        }
                                    }

                            } else if (key1.equals("meeting")) {
                                name = model.getItems().getName();
                                id = model.getItems().getIdnumber();

                            }
                            else if (key1.equals("checkin")){
                                name = model.getItems().getName();
                                id = model.getItems().getIdnumber();

                            }
                            else{
                                user_data.setVisibility(View.GONE);
                            }

                            isToday = TimeUtils.isTodayValid(model.getItems().getStart(), model.getItems().getEnd(), "1");

                            if (isToday){
//                                Toast.makeText(MainActivity.this, "valid ", Toast.LENGTH_SHORT).show();
                                valid.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.VISIBLE);
                                name_user.setText(name);
                                id_value.setText(id);

                                if (name.isEmpty()){
                                    name_layout.setVisibility(View.GONE);
                                }
                                else{
                                    name_layout.setVisibility(View.VISIBLE);
                                }
                                if (id.isEmpty()){
                                    id_layout.setVisibility(View.GONE);
                                }
                                else{
                                    id_layout.setVisibility(View.VISIBLE);
                                }

                                invalid.setVisibility(View.GONE);
                                dummy.setVisibility(View.GONE);
                            }
                            else{
//                                Toast.makeText(MainActivity.this, "Invalid 6", Toast.LENGTH_SHORT).show();
                                cancel.setVisibility(View.VISIBLE);
                                invalid.setVisibility(View.VISIBLE);
                                valid.setVisibility(View.GONE);
                                dummy.setVisibility(View.GONE);
                            }
                        }





                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
//                                            progressDialog.dismiss();
                System.out.println(t + "subhash");


            }
        },MainActivity.this,"",key1,key3,key4);

//
    }
}
