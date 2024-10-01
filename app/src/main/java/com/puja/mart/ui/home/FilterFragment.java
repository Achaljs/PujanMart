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

public class FilterFragment extends Fragment implements ProductItemclickListener {
    String cid;
    GridView gvProductList;
    NavController navController;
    ProductItemclickListener productItemclickListener;
    ProgressDialog progress;
    String scid;
    TextView tvCategory;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, container, false);
        this.cid = (String) getArguments().get("cid");
        this.scid = (String) getArguments().get("scid");
        this.gvProductList = (GridView) v.findViewById(R.id.gvProductList);
        this.tvCategory = (TextView) v.findViewById(R.id.tvCategory);
        this.productItemclickListener = this;
        if (this.scid.equalsIgnoreCase("-1")) {
            this.tvCategory.setText(this.cid);
            getProductByCat();
        } else {
            this.tvCategory.setText(this.cid + "/" + this.scid + ":");
            getProduct();
        }
        return v;
    }

    public void getProductByCat() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getProuctList("https://api.pujanmart.com/productbyctgy/" + this.cid).enqueue(new Callback<List<ProductModal>>() {
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                if (response.body().size() > 0) {
                    FilterFragment.this.setProductList(response.body());
                    return;
                }
                FilterFragment.this.progress.dismiss();
                Toast.makeText(FilterFragment.this.getContext(), "No Product Available.", Toast.LENGTH_SHORT).show();
            }

            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                Toast.makeText(FilterFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                FilterFragment.this.progress.dismiss();
            }
        });
    }

    public void getProduct() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getProuctList("https://api.pujanmart.com/productbysubctgy/" + this.cid + "/" + this.scid).enqueue(new Callback<List<ProductModal>>() {
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                if (response.body().size() > 0) {
                    FilterFragment.this.setProductList(response.body());
                    return;
                }
                FilterFragment.this.progress.dismiss();
                Toast.makeText(FilterFragment.this.getContext(), "No Product Available.", Toast.LENGTH_SHORT).show();
            }

            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                Toast.makeText(FilterFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                FilterFragment.this.progress.dismiss();
            }
        });
    }

    public void setProductList(List<ProductModal> productModalList) {
        this.gvProductList.setAdapter(new ProductListAdapter(getContext(), R.layout.product_list_layout, productModalList, this.productItemclickListener));
        this.progress.dismiss();
    }

    public void onClick(String pid) {
        Bundle bundle = new Bundle();
        bundle.putString("pid", pid);
        this.navController.navigate(R.id.action_nav_productdesc, bundle);
    }
}
