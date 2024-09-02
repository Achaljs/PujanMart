package com.puja.mart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    public static final String OTP_RECEIVED_ACTION = "otpReceived";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Extract the SMS message
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    // Extract OTP using a regex
                    String otp = extractOtpFromMessage(message);

                    if (otp != null) {
                        // Broadcast OTP locally within the app
                        Intent otpIntent = new Intent(OTP_RECEIVED_ACTION);
                        otpIntent.putExtra("otp", otp);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(otpIntent);
                    }
                    break;

                case CommonStatusCodes.TIMEOUT:
                    Toast.makeText(context, "OTP retrieval timed out", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private String extractOtpFromMessage(String message) {
        // Regex pattern to match exactly five digits
        Pattern pattern = Pattern.compile("\\b\\d{5}\\b");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
