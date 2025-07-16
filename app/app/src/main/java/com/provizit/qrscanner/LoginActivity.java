package com.provizit.qrscanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.provizit.qrscanner.Services.AESUtil;
import com.provizit.qrscanner.Services.DataManger;
import com.provizit.qrscanner.Services.Items;
import com.provizit.qrscanner.Services.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    EditText email,password;
    String pwd;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    AESUtil aesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences =  LoginActivity.this.getSharedPreferences("Provizit_Scanner", MODE_PRIVATE);
        editor = sharedPreferences.edit();
         aesUtil = new AESUtil(LoginActivity.this);

        Button login = findViewById(R.id.verifyButton);
          email = findViewById(R.id.emailEditText);
          password = findViewById(R.id.passwordEditText);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();

                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                } else {
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("val", email.getText().toString().trim());
                        jsonObj_.put("type", "email");
//                        jsonObj_.put("pwd", password.getText().toString().trim());
                        pwd = aesUtil.encrypt(password.getText().toString().trim(),email.getText().toString().trim());
                        jsonObj_.put("password",pwd );




                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("Password",aesUtil.decrypt(email.getText().toString().trim(),pwd));

                    Login(gsonObject);
//                    progress.show();
                }
            }
        });




    }
    private void Login(JsonObject jsonObject) {
        LoaderUtil.showLoader(this);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.userLogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                final Model model = response.body();
                LoaderUtil.hideLoader(LoginActivity.this);
//                                            progressDialog.dismiss();

//                progress.dismiss();
                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        AlertDialog alertDialog =  new AlertDialog.Builder(LoginActivity.this)
//                                    .setTitle("ACCESS DENIED")
                                .setMessage( "Presently, this app is accessible by only the enterprise users of PROVIZIT.\n" +
                                        "\n" +
                                        "We couldn’t find you as an enterprise user.\n" +
                                        "\n" +
                                        "You may contact your organization or write to info@provizit.com for more information." )
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);


                    } else if (statuscode.equals(successcode)) {

                        System.out.println("asfasf" + model.result);

//
                       Items items = model.getItems();
                        editor.putInt("isloginProvizit", 1);
                        editor.putString("comp_id", items.getComp_id());
                        editor.putString("email", email.getText().toString());
                        editor.putString("pic",model.getIncomplete_data().getPic().get(0) );
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                      overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
//                                            progressDialog.dismiss();
                System.out.println(t + "subhash");


            }
        },LoginActivity.this, jsonObject);

//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // ✅ QR code scanned successfully
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            } else {
                // ❌ Scan was cancelled
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
