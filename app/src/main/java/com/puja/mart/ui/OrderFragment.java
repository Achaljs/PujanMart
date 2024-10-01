package com.puja.mart.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Adapter.CartAdapter;
import com.puja.mart.Adapter.OrderAdapter;
import com.puja.mart.Modal.CartModal;
import com.puja.mart.Modal.Order;
import com.puja.mart.R;
import com.puja.mart.ui.home.CartFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderFragment extends Fragment {


    NavController navController;
    ProgressDialog progress;
    RecyclerView rvcategory;
    SharedPreferences sharedPreferences;

    LinearLayout empty;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.sharedPreferences = getContext().getSharedPreferences("MySharedPref", 0);
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        this.rvcategory = (RecyclerView) v.findViewById(R.id.rvOrder);

        getdata();

        return v;
    }

    public void getdata() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();




        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getOrderList("https://api.pujanmart.com/orders/" + this.sharedPreferences.getString("mobile", "")).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                OrderFragment.this.progress.dismiss();
                OrderFragment.this.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable throwable) {
                Toast.makeText(OrderFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                OrderFragment.this.progress.dismiss();
            }
        });
    }



    public void setData(List<Order> list) {
        if(list.size()==0){

        }
        else {
            
            this.rvcategory.setLayoutManager(new LinearLayoutManager(getContext()));
            this.rvcategory.setAdapter(new OrderAdapter(getContext(),list));
        }
    }


}