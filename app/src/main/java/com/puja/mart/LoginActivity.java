package com.puja.mart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Modal.SMSModal;
import com.puja.mart.Utils.Utility;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    MaterialButton btnSubmit;
    EditText etError;
    TextInputEditText mEditText;
    int otp;
    ProgressDialog progress;
    int session;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.progress = progressDialog;
        progressDialog.setTitle("Verifying");
        this.progress.setMessage("Wait while verifying...");
        this.progress.setCancelable(false);
        this.btnSubmit = (MaterialButton) findViewById(R.id.btnSubmit);
        this.mEditText = (TextInputEditText) findViewById(R.id.etMob);
        this.etError = (EditText) findViewById(R.id.etError);
        this.btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LoginActivity.this.mEditText.getText().toString().equals("") || LoginActivity.this.mEditText.getText().toString().length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please Insert Correct Mobile No", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginActivity.this.progress.show();
                LoginActivity.this.sendOtp();
            }
        });
    }

    public void sendOtp() {
        this.otp = generateOtp();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).sendSMS("https://sms.webtextsolution.com/sms-panel/api/http/index.php?username=NAKSHTRA&apikey=0E8D4-439EA&apirequest=Text&sender=PUJANM&mobile=" + this.mEditText.getText().toString() + "&message=" + this.otp + "%20is%20your%20verification%20OTP.%20Do%20not%20share%20this%20with%20anyone.%20Nakshatra%20Tak%20Private%20Limited.&route=TRANS&TemplateID=1207169892171775125&format=JSON").enqueue(new Callback<SMSModal>() {
            public void onResponse(Call<SMSModal> call, Response<SMSModal> response) {
                LoginActivity.this.sendOtpToServer();
            }

            public void onFailure(Call<SMSModal> call, Throwable t) {
                LoginActivity.this.progress.dismiss();
                LoginActivity.this.etError.setText("Cause: => " + t.getCause() + "  Message: => " + t.getMessage() + "  :" + t.getLocalizedMessage());
                Log.e("see2",t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this.getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendOtpToServer() {
        this.session = generateSession();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/otpsave/" + this.mEditText.getText().toString() + "/" + this.session + "/" + this.otp).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                Intent i = new Intent(LoginActivity.this, OtpActivity.class);
                i.putExtra("session", LoginActivity.this.session);
                i.putExtra("mobile", LoginActivity.this.mEditText.getText().toString());
                LoginActivity.this.startActivity(i);
                LoginActivity.this.finish();
                LoginActivity.this.progress.dismiss();
            }

            public void onFailure(Call<String> call, Throwable t) {
                LoginActivity.this.progress.dismiss();
                LoginActivity.this.etError.setText("Cause: => " + t.getCause() + "  Message: => " + t.getMessage() + "  :" + t.getLocalizedMessage());
                Log.e("see",t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this.getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int generateOtp() {
        return Utility.getFiveDigitRandomNumberString();
    }

    public int generateSession() {
        return new Random().nextInt(9999);
    }
}
