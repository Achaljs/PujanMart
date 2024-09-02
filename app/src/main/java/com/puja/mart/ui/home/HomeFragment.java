package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.puja.mart.databinding.FragmentHomeBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ProductItemclickListener, CatItemclickListener, itemOnClickListener {

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
        this.catItemclickListener = this;
        this.itemOnClickListener = this;
        this.binding.ivCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeFragment.this.navController.navigate(R.id.action_nav_home_to_nav_cart);
            }
        });
        this.binding.ivWishList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeFragment.this.navController.navigate(R.id.action_nav_home_to_nav_wishlist);
            }
        });
        getcateogory();
        return root;
    }

    public void sendOtpToServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getProuctList("https://api.pujanmart.com/productlist").enqueue(new Callback<List<ProductModal>>() {
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                HomeFragment.this.setProductList(response.body());
            }

            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                Toast.makeText(HomeFragment.this.getContext(), "An error has occured in Product list", Toast.LENGTH_SHORT).show();
                HomeFragment.this.progress.dismiss();
            }
        });
    }

    public void getcateogory() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getCatList("https://api.pujanmart.com/productctgylist").enqueue(new Callback<List<CatModal>>() {
            public void onResponse(Call<List<CatModal>> call, Response<List<CatModal>> response) {
                HomeFragment.this.progress.dismiss();
                HomeFragment.this.setCategory(response.body());
            }

            public void onFailure(Call<List<CatModal>> call, Throwable t) {
                Toast.makeText(HomeFragment.this.getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                HomeFragment.this.progress.dismiss();
            }
        });
    }

    public void setProductList(List<ProductModal> productModalList) {
        this.gvProductList.setAdapter(new ProductListAdapter(getContext(), R.layout.product_list_layout, productModalList, this.productItemclickListener));
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

    public void setCategory(List<CatModal> list) {
        this.binding.rvcategory.setLayoutManager(new LinearLayoutManager(getContext()));
        CatAdapter adapter = new CatAdapter(list, getContext(), this.catItemclickListener);
        this.binding.rvcategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.binding.rvcategory.setAdapter(adapter);
        sendOtpToServer();
    }

    public void onResume() {
        super.onResume();
        getcartdata();
    }

    public void onClickCat(String cid) {
        Bundle bundle = new Bundle();
        bundle.putString("cid", cid);
        this.navController.navigate(R.id.action_nav_home_to_nav_subcat, bundle);
    }

    public int onClick() {
        return 0;
    }

    public void getcartdata() {
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getCartList("https://api.pujanmart.com/getcart/" + getContext().getSharedPreferences("MySharedPref", 0).getString("mobile", "")).enqueue(new Callback<List<CartModal>>() {
            public void onResponse(Call<List<CartModal>> call, Response<List<CartModal>> response) {
                HomeFragment.this.binding.tvCount.setText(String.valueOf(response.body().size()));
            }

            public void onFailure(Call<List<CartModal>> call, Throwable t) {
                Toast.makeText(HomeFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
