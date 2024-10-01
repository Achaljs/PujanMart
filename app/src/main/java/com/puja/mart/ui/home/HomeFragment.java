package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.*;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.API.itemOnClickListener;
import com.puja.mart.Adapter.ProductListAdapter;
import com.puja.mart.CatAdapter;
import com.puja.mart.CatItemclickListener;
import com.puja.mart.CatModal;
import com.puja.mart.Modal.CartModal;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.ProductItemclickListener;
import com.puja.mart.R;
import com.puja.mart.ServiceAdapter;
import com.puja.mart.ServiceModle;
import com.puja.mart.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ProductItemclickListener, itemOnClickListener {

    public FragmentHomeBinding binding;
    CatItemclickListener catItemclickListener;
    GridView gvProductList;
    itemOnClickListener itemOnClickListener;
    NavController navController;
    ProductItemclickListener productItemclickListener;
    ProgressDialog progress;
    TextView tvCount;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding inflate = FragmentHomeBinding.inflate(inflater, container, false);
        this.binding = inflate;
        View root = inflate.getRoot();
        this.gvProductList = this.binding.gvProductList;
        this.productItemclickListener = this;
        //this.catItemclickListener = this;
        this.itemOnClickListener = this;
        this.binding.cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeFragment.this.navController.navigate(R.id.action_nav_home_to_nav_cart);
            }
        });
        this.binding.ivWishList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeFragment.this.navController.navigate(R.id.action_nav_home_to_nav_wishlist);
            }
        });

        this.binding.categary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.this.navController.navigate(R.id.action_nav_home_to_nav_category);
            }
        });

        getService();


        ArrayList imageList = new ArrayList< SlideModel >(); // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(new SlideModel("https://pujanmart.com/images/other/E7XWCPW284_thumb.jpg","", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://pujanmart.com/images/other/PKD75D8HH4_thumb.jpg","", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://pujanmart.com/images/other/MK7EDYWJEG_thumb.jpg","", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://pujanmart.com/images/other/S6GV4ES73S_thumb.jpg","", ScaleTypes.FIT));



              this.binding.imageSlider.setImageList(imageList);



        return root;
    }

    public void sendOtpToServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Making the API call to getProductList
        ApiInterface apiService = Api.getRetrofitInstance().create(ApiInterface.class);
        apiService.getProuctList("productlist").enqueue(new Callback<List<ProductModal>>() {
            @Override
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                // Check if the response is successful and not null
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModal> productList = response.body();

                    // Handle empty product list case
                    if (productList.isEmpty()) {
                        Toast.makeText(getContext(), "No products available", Toast.LENGTH_SHORT).show();
                    } else {
                        HomeFragment.this.setProductList(productList);
                    }
                } else {
                    // Log or handle the case when the response body is null
                    Toast.makeText(getContext(), "Failed to retrieve product list", Toast.LENGTH_SHORT).show();
                }
                // Dismissing the progress dialog after response
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                // Handle failure scenarios
                Toast.makeText(getContext(), "An error occurred while fetching the product list", Toast.LENGTH_SHORT).show();

                // Dismiss progress dialog in case of failure
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                // Optional: Log the error for debugging
                Log.e("sendOtpToServer", "API Call Failure: " + t.getMessage());
            }
        });
    }


//    public void getcateogory() {
//        ProgressDialog progressDialog = new ProgressDialog(getContext());
//        this.progress = progressDialog;
//        progressDialog.setTitle("Loading");
//        this.progress.setMessage("Wait while loading...");
//        this.progress.setCancelable(false);
//        this.progress.show();
//        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getCatList("https://api.pujanmart.com/productctgylist").enqueue(new Callback<List<CatModal>>() {
//            public void onResponse(Call<List<CatModal>> call, Response<List<CatModal>> response) {
//                HomeFragment.this.progress.dismiss();
//                HomeFragment.this.setCategory(response.body());
//            }
//
//            public void onFailure(Call<List<CatModal>> call, Throwable t) {
//                Toast.makeText(HomeFragment.this.getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                HomeFragment.this.progress.dismiss();
//            }
//        });
//    }

    public void setProductList(List<ProductModal> productModalList) {
        if(productModalList!=null) {
            this.gvProductList.setAdapter(new ProductListAdapter(getContext(), R.layout.product_list_layout, productModalList, this.productItemclickListener));
        }
        this.progress.dismiss();



    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }

    public void onClick(String pid) {
        Bundle bundle = new Bundle();
        bundle.putString("pid", pid);
        this.navController.navigate(R.id.action_nav_home_to_nav_productdesc, bundle);
    }

//    public void setCategory(List<CatModal> list) {
//        this.binding.rvcategory.setLayoutManager(new LinearLayoutManager(getContext()));
//        CatAdapter adapter = new CatAdapter(list, getContext(), this.catItemclickListener);
//        this.binding.rvcategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        this.binding.rvcategory.setAdapter(adapter);
//
//        sendOtpToServer();
//    }

    public void onResume() {
        super.onResume();
        getcartdata();
    }

//    public void onClickCat(String cid) {
//        Bundle bundle = new Bundle();
//        bundle.putString("cid", cid);
//        this.navController.navigate(R.id.action_nav_home_to_nav_subcat, bundle);
//    }

    public int onClick() {
        return 0;
    }

    public void getcartdata() {
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getCartList("https://api.pujanmart.com/getcart/" + getContext().getSharedPreferences("MySharedPref", 0).getString("mobile", "")).enqueue(new Callback<List<CartModal>>() {
            public void onResponse(Call<List<CartModal>> call, Response<List<CartModal>> response) {

            }

            public void onFailure(Call<List<CartModal>> call, Throwable t) {
                Toast.makeText(HomeFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getService(){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...service");
        this.progress.setCancelable(false);
        this.progress.show();

        ((ApiInterface) Api.getService().create(ApiInterface.class)).getServList("otherproducts").enqueue(new Callback<List<ServiceModle>>() {

            @Override
            public void onResponse(Call<List<ServiceModle>> call, Response<List<ServiceModle>> response) {
                HomeFragment.this.progress.dismiss();

                if (response.isSuccessful()) {
                    List<ServiceModle> services = response.body();
                    if (services != null) {

                        Log.d("API_RESPONSE", services.toString());
                        HomeFragment.this.setService(services);
                    } else {
                        Log.d("API_RESPONSE", "Empty or null response body.");
                    }
                } else {
                    HomeFragment.this.progress.dismiss();
                    Log.d("API_ERROR", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ServiceModle>> call, Throwable throwable) {
                Toast.makeText(HomeFragment.this.getContext(), ""+throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                HomeFragment.this.progress.dismiss();
                Log.d("API_FAILURE", "Error: " + throwable.getMessage());
            }
        });
    }

    public void setService(List<ServiceModle> list){

        if (list == null || list.isEmpty()) {
            Toast.makeText(getContext(), "No services available", Toast.LENGTH_SHORT).show();

        } else {

            ArrayList<ServiceModle> pujan = new ArrayList<>();
            ArrayList<ServiceModle> servise=new ArrayList<>();
            ArrayList<ServiceModle> consultancy= new ArrayList<>();
            for(int i=0; i<list.size();i++) {
                if (list.get(i).getProductType().equals("Pujan")){
                    pujan.add(list.get(i));
                }
                else if (list.get(i).getProductType().equals("Service")){
                    servise.add(list.get(i));
                }
                else {
                    consultancy.add(list.get(i));
                }
            }
            this.binding.rvconsultancy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            this.binding.rvcategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            this.binding.rvService.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


            ServiceAdapter adapterpujan = new ServiceAdapter(pujan, getContext(), this.catItemclickListener);
            ServiceAdapter adapterserv = new ServiceAdapter(servise, getContext(), this.catItemclickListener);
            ServiceAdapter adaptercons = new ServiceAdapter(consultancy, getContext(), this.catItemclickListener);

            this.binding.rvService.setAdapter(adapterserv);
            this.binding.rvconsultancy.setAdapter(adaptercons);
            this.binding.rvcategory.setAdapter(adapterpujan);
        }
        this.progress.dismiss();
        sendOtpToServer();
    }

}
