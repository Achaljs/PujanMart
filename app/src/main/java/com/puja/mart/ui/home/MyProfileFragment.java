package com.puja.mart.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileFragment extends Fragment {
    Button btnSubmit;
    EditText etAlterMob;
    EditText etEmail;
    EditText etName;
    EditText etUserId;
    LinearLayout liAddress;
    LinearLayout liUpdatePicture;
    NavController navController;
    ImageView ivProfileImage;  // Declare the ImageView for profile image

    private static final int CAMERA_REQUEST_CODE = 90;
    private static final int PERMISSION_REQUEST_CODE = 95;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Initialize Views
        this.etUserId = (EditText) v.findViewById(R.id.etUserId);
        this.etName = (EditText) v.findViewById(R.id.etName);
        this.etAlterMob = (EditText) v.findViewById(R.id.etAlterMob);
        this.etEmail = (EditText) v.findViewById(R.id.etEmail);
        this.btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        this.liUpdatePicture = (LinearLayout) v.findViewById(R.id.liUpdatePicture);
        this.liAddress = (LinearLayout) v.findViewById(R.id.liAddress);
        this.ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);  // Initialize the ImageView

        // Request Camera Permission
        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{"android.permission.CAMERA"}, PERMISSION_REQUEST_CODE);

        // Handle Submit Button Click
        this.btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (etUserId.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter User Id", Toast.LENGTH_SHORT).show();
                } else if (etName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (etAlterMob.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Alternate Mobile No", Toast.LENGTH_SHORT).show();
                } else if (etEmail.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                } else {
                    updateDetails();
                }
            }
        });

        // Handle Update Picture Click
        this.liUpdatePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);  // Launch camera to capture image
            }
        });

        // Handle Update Address Click
        this.liAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("value", "User");
                bundle.putString("uid", "update");
                navController.navigate(R.id.action_nav_myprofile_to_nav_address, bundle);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // Get the image from the camera data
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Set the image to the ImageView
                ivProfileImage.setImageBitmap(photo);  // Display the captured image in the ImageView
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE &&
                ContextCompat.checkSelfPermission(getContext(), "android.permission.CAMERA") == -1) {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle updating the user details
    public void updateDetails() {
        ApiInterface apiInterface = Api.getRetrofitInstanceForSMS().create(ApiInterface.class);
        String userId = etUserId.getText().toString();
        String name = etName.getText().toString();
        String alterMob = etAlterMob.getText().toString();
        String email = etEmail.getText().toString();

        apiInterface.send("https://api.pujanmart.com/updateuserdetails/" + userId + "/" + name + "/" + alterMob + "/" + email)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
