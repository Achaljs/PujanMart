package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Adapter.ProductListAdapter;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.ProductItemclickListener;
import com.puja.mart.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListFragment extends Fragment implements ProductItemclickListener {
    GridView gvProductList;
    NavController navController;
    ProductItemclickListener productItemclickListener;
    ProgressDialog progress;
    LinearLayout empty;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wish_list, container, false);
        this.gvProductList = (GridView) v.findViewById(R.id.gvProductList);
        this.empty=(LinearLayout)v.findViewById(R.id.empty) ;
        this.productItemclickListener = this;
        getData();
        return v;
    }

    public void getData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", 0);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getProuctList("https://api.pujanmart.com/wishlistget/" + sharedPreferences.getString("mobile", "")).enqueue(new Callback<List<ProductModal>>() {
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                WishListFragment.this.setProductList(response.body());
            }

            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                Toast.makeText(WishListFragment.this.getContext(), "An error has occured in Product list", Toast.LENGTH_SHORT).show();
                WishListFragment.this.progress.dismiss();
            }
        });
    }

    public void setProductList(List<ProductModal> productModalList) {
        if(productModalList.size()==0){
            WishListFragment.this.empty.setVisibility(View.VISIBLE);
            this.progress.dismiss();
        }
        else {
            WishListFragment.this.empty.setVisibility(View.INVISIBLE);
            this.gvProductList.setAdapter(new ProductListAdapter(getContext(), R.layout.product_list_layout, productModalList, this.productItemclickListener));
            this.progress.dismiss();

        }
    }


    public void onClick(String pid) {
        Bundle bundle = new Bundle();
        bundle.putString("pid", pid);
        this.navController.navigate(R.id.action_nav_wishlist_to_nav_productdesc, bundle);
    }
}
