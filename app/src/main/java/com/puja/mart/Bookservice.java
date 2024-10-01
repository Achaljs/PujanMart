package com.puja.mart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.ServiceModle;
import com.puja.mart.Utils.Utility;
import com.puja.mart.databinding.ActivityBookserviceBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.text.Charsets;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bookservice extends AppCompatActivity {

    String id = "";
    String name = "";
    ActivityBookserviceBinding binding;

    private final String apiEndPoint = "/pg/v1/pay";
    private final String salt = "54801292-c6ed-4b84-911b-6d4fbd1ad7df"; // Securely store on server
    private final String MERCHANT_ID = "M1J1FYDUIBBB"; // Securely store on server
    private final String MERCHANT_TID = Utility.getOrderId(); // Replace with a unique transaction ID
    private final String BASE_URL = "https://api-preprod.phonepe.com/";
    private int amount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookserviceBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            name = extras.getString("name");
        }
        getProductDetail();

        // Initialize PhonePe SDK

        PhonePe.init(this, PhonePeEnvironment.RELEASE, MERCHANT_ID, "");
        binding.btnCheckOut.setOnClickListener(view -> startPayment());
    }

    private void startPayment() {




        JSONObject data = new JSONObject();
        try {
            data.put("merchantId", MERCHANT_ID);
            data.put("merchantTransactionId", MERCHANT_TID); // Unique transaction ID
            data.put("merchantUserId", System.currentTimeMillis());// Amount in paise (₹2.00)
            data.put("amount", amount);


            data.put("callbackUrl", "https://webhook.site/callback-url");
            data.put("mobileNumber", "8318844254"); // Optional field
            JSONObject paymentInstrument = new JSONObject();
            paymentInstrument.put("type", "UPI_INTENT"); // UPI payment type
            // Targeting PhonePe
            data.put("paymentInstrument", paymentInstrument);



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create payment request!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Base64 encode the data
        String payloadBase64 = Base64.encodeToString(data.toString().getBytes(Charset.defaultCharset()), Base64.NO_WRAP);
        String checksum = sha256(payloadBase64 + apiEndPoint + salt) + "###1";

        // Build the B2BPGRequest
        B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                .setData(payloadBase64)
                .setChecksum(checksum)
                .setUrl(apiEndPoint)
                .build();

        // Start the payment activity
        try {
            startActivityForResult(PhonePe.getImplicitIntent(
                    /* Context */ this, b2BPGRequest,"com.pujan.mart"),777);
        } catch(PhonePeInitException e){
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {

            Log.d("PhonePeResponse", "Payment Data: " + data.getData());
            Toast.makeText(Bookservice.this, "Payment Status Updated: Success", Toast.LENGTH_SHORT).show();

            checkPaymentStatus();
        }
    }

    private void checkPaymentStatus() {
        String xVerify = sha256("/pg/v1/status/" + MERCHANT_ID + "/" + MERCHANT_TID + salt) + "###1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Create request body for checking payment status
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("orderid", MERCHANT_TID);  // Use actual order ID
            requestBody.put("transactionid", MERCHANT_TID); // Use actual transaction ID
            requestBody.put("payment_status", "Paid");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating request body!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Retrofit API interface and make the POST request
        ApiInterface apiInterface = Api.getRetrofitInstance().create(ApiInterface.class);
        Call<JSONObject> call = apiInterface.updateOrderStatus(headers, requestBody.toString());

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseBody = response.body();
                        if (responseBody != null && responseBody.getBoolean("success")) {
                            Toast.makeText(Bookservice.this, "Payment Status Updated: Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Bookservice.this, "Payment Status Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Bookservice.this, "Error updating payment status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(Bookservice.this, "Network Error: Unable to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProductDetail() {
        ApiInterface apiInterface = Api.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getServList("otherdetails/" + id).enqueue(new Callback<List<ServiceModle>>() {
            @Override
            public void onResponse(Call<List<ServiceModle>> call, Response<List<ServiceModle>> response) {
                if (response.isSuccessful()) {
                    List<ServiceModle> services = response.body();
                    if (services != null) {
                        updateUI(services);
                    } else {
                        Log.d("API_RESPONSE", "Empty or null response body.");
                    }
                } else {
                    Log.d("API_ERROR", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ServiceModle>> call, Throwable t) {
                Log.d("API_FAILURE", "Error: " + t.getMessage());
            }
        });
    }

    void updateUI(List<ServiceModle> service) {
        Glide.with(this).load("https://pujanmart.com/images/other/" + service.get(0).getProductThumb()).into(binding.ivProduct);
        binding.tvPrice.setText("₹" + service.get(0).getProductSell());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // For Android N and above
            binding.tvDesc.setText(Html.fromHtml(service.get(0).getProduct_detail(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            // For below Android N
            binding.tvDesc.setText(Html.fromHtml(service.get(0).getProduct_detail()));
        }

        binding.tvPname.setText(service.get(0).getProductName());
        amount= (int) (amount*service.get(0).getProductSell());
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(Charsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
