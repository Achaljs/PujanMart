package com.puja.mart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String msg_from;
        if (bundle != null) {
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msg_from = msgs[i].getOriginatingAddress();
                    String msgBody = msgs[i].getMessageBody();

                    // Extract the OTP from the SMS body
                    String otp = extractOTP(msgBody);

                    // Broadcast the OTP or update UI
                    Intent otpIntent = new Intent("otp");
                    otpIntent.putExtra("otp", otp);
                    context.sendBroadcast(otpIntent);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception caught: " + e.getMessage());
            }
        }
    }

    private String extractOTP(String message) {
        // Example: Customize the logic to extract OTP based on your SMS format
        String otp = "";
        Pattern p = Pattern.compile("(\\d{6})");
        Matcher m = p.matcher(message);
        if (m.find()) {
            otp = m.group(0);
        }
        return otp;
    }
}
