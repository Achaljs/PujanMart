package com.puja.mart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.CatAdapter;
import com.puja.mart.CatItemclickListener;
import com.puja.mart.Modal.CartModal;
import com.puja.mart.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyView> {
    /* access modifiers changed from: private */
    public List<CartModal> CartModalList;
    CatItemclickListener catItemclickListener;
    Context context;

    public CartAdapter(List<CartModal> CartModalList2, Context context2) {
        this.CartModalList = CartModalList2;
        this.context = context2;
    }

    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart, parent, false));
    }

    public void onBindViewHolder(final MyView holder,  int position) {

        holder.tvPname.setText(this.CartModalList.get(holder.getAdapterPosition()).getProduct_name());
        holder.tvPrice.setText("₹ " + this.CartModalList.get(holder.getAdapterPosition()).getPrice());
        holder.productQuantityTextView.setText("" + this.CartModalList.get(holder.getAdapterPosition()).getQuantity());
        Glide.with(this.context).load("https://pujanmart.com/images/product/" + this.CartModalList.get(holder.getAdapterPosition()).getProduct_thumb()).into(holder.ivImage);
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CartAdapter cartAdapter = CartAdapter.this;
                double amount = cartAdapter.getAmount(String.valueOf(((CartModal) cartAdapter.CartModalList.get(holder.getAdapterPosition())).getPrice()), String.valueOf(((CartModal) CartAdapter.this.CartModalList.get(holder.getAdapterPosition())).getQuantity()));
                CartAdapter.this.addToCart(((CartModal) CartAdapter.this.CartModalList.get(holder.getAdapterPosition())).getUser_id(), ((CartModal) CartAdapter.this.CartModalList.get(holder.getAdapterPosition())).getProduct_id(), String.valueOf(amount), holder.productQuantityTextView);
                holder.productQuantityTextView.setText("" + (Integer.valueOf(holder.productQuantityTextView.getText().toString()).intValue() + 1));
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String mob = ((CartModal) CartAdapter.this.CartModalList.get(holder.getAdapterPosition())).getUser_id();
                String pid = ((CartModal) CartAdapter.this.CartModalList.get(holder.getAdapterPosition())).getProduct_id();
                int val = Integer.valueOf(holder.productQuantityTextView.getText().toString()).intValue();
                if (val != 0) {
                    CartAdapter.this.removeToCart(mob, pid);
                    holder.productQuantityTextView.setText("" + (val - 1));
                    return;
                }
                holder.productQuantityTextView.setText("0");
            }
        });
    }

    public int getItemCount() {
        return this.CartModalList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        Button btnMinus;
        Button btnPlus;
        ImageView ivImage;
        TextView productQuantityTextView;
        TextView tvPname;
        TextView tvPrice;

        public MyView(View view) {
            super(view);
            this.btnPlus = (Button) view.findViewById(R.id.btnPlus);
            this.btnMinus = (Button) view.findViewById(R.id.btnMinus);
            this.tvPname = (TextView) view.findViewById(R.id.tvPname);
            this.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            this.productQuantityTextView = (TextView) view.findViewById(R.id.productQuantityTextView);
            this.ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

    public void addToCart(String mobile, String pid, String amount, TextView tvQuan) {
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/cartinsert/" + mobile + "/" + pid + "/" + amount + "/1").enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().contains("1")) {
                    Toast.makeText(CartAdapter.this.context, "Added in cart", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(CartAdapter.this.context, "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeToCart(String mobile, String pid) {
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/cartremove/" + mobile + "/" + pid).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.body().contains("1")) {
                        Toast.makeText(CartAdapter.this.context, "Removed from cart", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CartAdapter.this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public double getAmount(String amount, String count) {
        return Double.valueOf(amount.replace("₹", "")).doubleValue() * Double.valueOf(count).doubleValue();
    }
}
