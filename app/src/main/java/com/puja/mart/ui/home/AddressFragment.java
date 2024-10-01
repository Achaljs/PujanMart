package com.puja.mart.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Bookservice;
import com.puja.mart.GlobalActivity;
import com.puja.mart.R;
import com.puja.mart.Utils.Utility;
import com.puja.mart.ui.OrderPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import kotlin.text.Charsets;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFragment extends Fragment {
    private MaterialButton btnSubmit, btnpay;
    private TextInputEditText etAddress, etCity, etLocality, etName, etPinCode, etState, etUserId, etMob;
    private String uid, value;
    private final String apiEndPoint = "/pg/v1/pay";
    private final String salt = "54801292-c6ed-4b84-911b-6d4fbd1ad7df"; // Securely store on server
    private final String MERCHANT_ID = "M1J1FYDUIBBB"; // Securely store on server
    private final String MERCHANT_TID = ""+System.currentTimeMillis(); // Replace with a unique transaction ID
    private final String BASE_URL = "https://api-preprod.phonepe.com/";
    private int amount = 1500000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address, container, false);

        etName = v.findViewById(R.id.etName);
        etMob = v.findViewById(R.id.etMob);
        etAddress = v.findViewById(R.id.etAddress);
        etCity = v.findViewById(R.id.etCity);
        etLocality = v.findViewById(R.id.etLocality);
        etState = v.findViewById(R.id.etState);
        etPinCode = v.findViewById(R.id.etPinCode);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        btnpay = v.findViewById(R.id.btnpay);
        value = getArguments().getString("value");
        uid = getArguments().getString("uid");
        if(uid=="update"){
            btnSubmit.setText("Update");
            btnpay.setVisibility(View.GONE);
        }


        PhonePe.init(getContext(), PhonePeEnvironment.RELEASE, MERCHANT_ID, "");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    postOrder();
                }
            }
        });
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    startPayment();
                }
            }
        });

        return v;
    }

    private boolean validateFields() {
        if (etAddress.getText().toString().isEmpty()) {
            showToast("Enter Address");
            return false;
        } else if (etCity.getText().toString().isEmpty()) {
            showToast("Enter City");
            return false;
        } else if (etLocality.getText().toString().isEmpty()) {
            showToast("Enter Locality");
            return false;
        } else if (etPinCode.getText().toString().isEmpty()) {
            showToast("Enter Pin Code");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void postOrder() {
        String oid = Utility.getOrderId();
        OrderPost orderPost = new OrderPost();
        orderPost.setOrderid(oid);
        orderPost.setUserid(this.uid);
        orderPost.setValue(this.value);
        orderPost.setCoupon_code("");
        orderPost.setUser_name(this.etName.getText().toString());
        orderPost.setUser_mbl(this.etMob.getText().toString());
        orderPost.setUser_address(this.etAddress.getText().toString());
        orderPost.setLocality(this.etLocality.getText().toString());
        orderPost.setCity(this.etCity.getText().toString());
        orderPost.setPincode(this.etPinCode.getText().toString());
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).createPost(orderPost).enqueue(new Callback<Integer>() {
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Toast.makeText(AddressFragment.this.getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                AddressFragment.this.startActivity(new Intent(AddressFragment.this.getContext(), GlobalActivity.class));
                AddressFragment.this.getActivity().finish();
            }

            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(AddressFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
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
            paymentInstrument.put("type", "PAY_PAGE"); // UPI payment type
            // Targeting PhonePe
            data.put("paymentInstrument", paymentInstrument);



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to create payment request!", Toast.LENGTH_SHORT).show();
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
                    /* Context */ getContext(), b2BPGRequest,"com.phonepe.app"),777);
        } catch(PhonePeInitException e){
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {

            Log.d("PhonePeResponse", "Payment Data: " + data.getData());

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
            Toast.makeText(getContext(), "Error creating request body!", Toast.LENGTH_SHORT).show();
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
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: Unable to update status", Toast.LENGTH_SHORT).show();
            }
        });
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
