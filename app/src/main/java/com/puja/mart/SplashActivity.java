package com.puja.mart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                if (SplashActivity.this.isLogin()) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, GlobalActivity.class));
                    SplashActivity.this.finish();
                    return;
                }
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                SplashActivity.this.finish();
            }
        }, 3000);
    }

    public boolean isLogin() {
        return getSharedPreferences("MySharedPref", 0).getBoolean("isLogin", false);
    }
}
