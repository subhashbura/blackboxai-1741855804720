  package com.provizit.qrscanner;

  import android.annotation.SuppressLint;
  import android.content.Intent;
  import android.content.IntentFilter;
  import android.content.SharedPreferences;
  import android.content.pm.PackageManager;
  import android.os.Bundle;
  import android.util.Log;
  import android.view.View;
  import android.view.animation.Animation;
  import android.view.animation.AnimationUtils;
  import android.widget.Button;
  import android.widget.ImageView;
  import android.widget.LinearLayout;
  import android.widget.Toast;

  import androidx.annotation.Nullable;
  import androidx.appcompat.app.AlertDialog;
  import androidx.appcompat.app.AppCompatActivity;

  import com.bumptech.glide.Glide;
  import com.bumptech.glide.load.engine.DiskCacheStrategy;
  import com.google.zxing.integration.android.IntentIntegrator;
  import com.google.zxing.integration.android.IntentResult;
  import com.provizit.qrscanner.Services.DataManger;
  import com.provizit.qrscanner.Services.Model;
  import com.provizit.qrscanner.Services.Model1;
  import com.provizit.qrscanner.Services.TimeUtils;

  import retrofit2.Call;
  import retrofit2.Callback;
  import retrofit2.Response;

  public class AfterScanActivity extends AppCompatActivity {

      private static final int REQUEST_WRITE_PERMISSION = 786;

      SharedPreferences.Editor editor1;
      SharedPreferences sharedPreferences1;

      @SuppressLint("MissingInflatedId")
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_after_scan);
            Button closeButton = findViewById(R.id.closeButton);
            Button scanButton = findViewById(R.id.scanButton);
            closeButton.setOnClickListener(v -> {
                finish();
            });
          ImageView logo = findViewById(R.id.logoc);

          LinearLayout valid = findViewById(R.id.valid);
            LinearLayout invalid = findViewById(R.id.invalid);
          Integer isValid = getIntent().getIntExtra("isValid", 0);

          if (isValid == 1){
              valid.setVisibility(View.VISIBLE);
              invalid.setVisibility(View.GONE);
          }
          else{
              invalid.setVisibility(View.VISIBLE);
              valid.setVisibility(View.GONE);
          }
          sharedPreferences1 = AfterScanActivity.this.getSharedPreferences("Provizit_Scanner", MODE_PRIVATE);
          editor1 = sharedPreferences1.edit();
          Glide.with(AfterScanActivity.this)
                  .load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("comp_id", "") + "/" + sharedPreferences1.getString("pic", ""))
                  .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk cache
                  .error(R.drawable.img)
                  .skipMemoryCache(true)  // Disable memory cache
                  .into(logo);

            scanButton.setOnClickListener(v -> {
                IntentIntegrator integrator = new IntentIntegrator(AfterScanActivity.this);
                integrator.setPrompt("Scan a QR Code");
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);

                // 👇 Set your custom scanner activity
                integrator.setCaptureActivity(CustomScannerActivity.class);

                integrator.initiateScan();
                valid.setVisibility(View.GONE);
                invalid.setVisibility(View.GONE);

            });


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
                  Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
              }
          }
      }

      public void seperatorKeys (String input) {

          // Step 1: Split by "###"
          String[] mainParts = input.split("###");


          if (mainParts.length == 3) {
              String key1 = mainParts[0]; // "meeting"
              String[] subParts = mainParts[1].split("\\*\\*\\*"); // Split by "***"
              if (subParts.length == 2) {
                  String key2 = subParts[0]; // "ftprovizitstc"
                  String key3 = subParts[1]; // "67f75d88840fb60f417772d9"
                  String key4 = mainParts[2]; // "bandanagaraju7979@gmail.com"

                  scan(key1,key2,key3,key4);

                  Log.e("key1",key1);
                  Log.e("key2",key2);
                  Log.e("key3",key3);
                  Log.e("key4",key4);





              } else {
                  System.out.println("Second section format incorrect (expected '***').");
              }
          } else {
              System.out.println("Main string format incorrect (expected '###').");
          }
      }
      private void scan(String key1,String key2, String key3, String key4) {
          LoaderUtil.showLoader(this);
          DataManger dataManager = DataManger.getDataManager();
          dataManager.getqrcodeStatus(new Callback<Model1>() {
              @Override
              public void onResponse(Call<Model1> call, Response<Model1> response) {
                  final Model1 model = response.body();
                  LoaderUtil.hideLoader(AfterScanActivity.this);
//                                            progressDialog.dismiss();

//                progress.dismiss();
                  if (model != null) {
//                                                approve_btn.setEnabled(true);
                      Integer statuscode = model.getResult();
                      Integer successcode = 200, failurecode = 201, not_verified = 404;
                      if (statuscode.equals(failurecode)) {
                          AlertDialog alertDialog =  new AlertDialog.Builder(AfterScanActivity.this)
//                                    .setTitle("ACCESS DENIED")
                                  .setMessage( "Invalid QRCode" )
                                  .setPositiveButton(android.R.string.ok, null)
                                  .show();

                      } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);


                      } else if (statuscode.equals(successcode)) {






                      }
                  }
              }

              @Override
              public void onFailure(Call<Model1> call, Throwable t) {
//                                            progressDialog.dismiss();
                  System.out.println(t + "subhash");


              }
          },AfterScanActivity.this,"",key1,key3,key4);

//
      }

  }

