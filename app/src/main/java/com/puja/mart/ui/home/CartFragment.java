package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Adapter.CartAdapter;
import com.puja.mart.Modal.CartModal;
import com.puja.mart.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    Button btnCheckOut;
    NavController navController;
    ProgressDialog progress;
    RecyclerView rvcategory;
    SharedPreferences sharedPreferences;
    TextView tvAmount;
    String value = "";
    LinearLayout empty;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.sharedPreferences = getContext().getSharedPreferences("MySharedPref", 0);
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        this.rvcategory = (RecyclerView) v.findViewById(R.id.rvcategory);
        this.tvAmount = (TextView) v.findViewById(R.id.tvAmount);
        this.btnCheckOut = (Button) v.findViewById(R.id.btnCheckOut);
        this.empty=(LinearLayout) v.findViewById(R.id.emptyCart);
        getdata();
        this.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("value", CartFragment.this.value);
                bundle.putString("uid", CartFragment.this.sharedPreferences.getString("mobile", ""));
                CartFragment.this.navController.navigate(R.id.action_nav_cart_to_nav_address, bundle);
            }
        });
        return v;
    }

    public void getdata() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getCartList("https://api.pujanmart.com/getcart/" + this.sharedPreferences.getString("mobile", "")).enqueue(new Callback<List<CartModal>>() {
            public void onResponse(Call<List<CartModal>> call, Response<List<CartModal>> response) {
                CartFragment.this.progress.dismiss();
                CartFragment.this.setData(response.body());
                CartFragment.this.updateUI(response.body());
                CartFragment.this.generateString(response.body());
            }

            public void onFailure(Call<List<CartModal>> call, Throwable t) {
                Toast.makeText(CartFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                CartFragment.this.progress.dismiss();
            }
        });
    }

    public void updateUI(List<CartModal> list) {
        int price = 0;
        for (CartModal cartModal : list) {
            price += cartModal.getPrice();
        }
        this.tvAmount.setText("₹ " + price);
    }

    public void setData(List<CartModal> list) {
        if(list.size()==0){
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.INVISIBLE);
            this.rvcategory.setLayoutManager(new LinearLayoutManager(getContext()));
            this.rvcategory.setAdapter(new CartAdapter(list, getContext()));
        }
    }

    public void generateString(List<CartModal> list) {
        for (CartModal c : list) {
            this.value += c.product_id + "-" + c.getQuantity() + ",";
        }
    }
}
