package com.puja.mart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends Activity {
    Button btnSubmit;
    EditText mEditText;
    int otp;
    ProgressDialog progress;
    String session;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.progress = progressDialog;
        progressDialog.setTitle("Verifying");
        this.progress.setMessage("Wait while verifying...");
        this.progress.setCancelable(false);
        this.btnSubmit = (Button) findViewById(R.id.btnSubmit);
        this.mEditText = (EditText) findViewById(R.id.etOtp);
        this.btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (OtpActivity.this.mEditText.getText().toString().isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please Insert Correct OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                OtpActivity.this.progress.show();
                OtpActivity.this.verifyOtp();
            }
        });
    }

    public void verifyOtp() {
        this.session = getIntent().getStringExtra("session");
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/otpverify/" + this.session + "/" + this.mEditText.getText().toString()).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                OtpActivity.this.setPref();
                OtpActivity.this.startActivity(new Intent(OtpActivity.this, GlobalActivity.class));
                OtpActivity.this.finish();
                OtpActivity.this.progress.dismiss();
            }

            public void onFailure(Call<String> call, Throwable t) {
                OtpActivity.this.progress.dismiss();
                Toast.makeText(OtpActivity.this.getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPref() {
        SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", 0).edit();
        myEdit.putBoolean("isLogin", true);
        myEdit.putString("mobile", getIntent().getStringExtra("mobile"));
        myEdit.commit();
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver for the OTP
        IntentFilter filter = new IntentFilter("otp");
        registerReceiver(otpReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(otpReceiver);
    }

    private BroadcastReceiver otpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String otp = intent.getStringExtra("otp");
            if (otp != null) {
                // Update your UI with the OTP
                OtpActivity.this.mEditText.setText(otp);
            }
        }
    };

}
