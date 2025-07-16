  package com.provizit.qrscanner;

  import android.annotation.SuppressLint;
  import android.content.Intent;
  import android.content.IntentFilter;
  import android.content.SharedPreferences;
  import android.content.pm.PackageManager;
  import android.os.Bundle;
  import android.view.animation.Animation;
  import android.view.animation.AnimationUtils;
  import android.widget.ImageView;
  import android.widget.LinearLayout;
  import android.widget.Toast;

  import androidx.appcompat.app.AppCompatActivity;

  import com.provizit.qrscanner.Services.EwsClient;

  import java.util.List;
  import java.util.Map;

  public class SplashActivity extends AppCompatActivity {
      LinearLayout upImage;
      Animation animationUp;
      IntentFilter intentfilter;
      private static final int REQUEST_WRITE_PERMISSION = 786;

      SharedPreferences.Editor editor1;
      SharedPreferences sharedPreferences1;

      @SuppressLint("MissingInflatedId")
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_splash);

          upImage = findViewById(R.id.l1);
          sharedPreferences1 = SplashActivity.this.getSharedPreferences("Provizit_Scanner", MODE_PRIVATE);
          editor1 = sharedPreferences1.edit();

//          new Thread(() -> {
              EwsClient ewsClient = new EwsClient();
              ewsClient.fetchInboxEmails();
//          }).start();



//          splashAnimation();

      }


      @Override
      public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              splashAnimation();
          }
      }

      public void splashAnimation() {
          animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
          upImage.setAnimation(animationUp);
          Thread t = new Thread() {
              @Override
              public void run() {
                  try {
                      sleep(2400);
                  } catch (Exception ignored) {

                  } finally {
                      if (sharedPreferences1.getInt("isloginProvizit", 0) == 1) {
                          Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                      } else {
                          Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                      }
                  }
              }
          };
          t.start();
      }
  }

