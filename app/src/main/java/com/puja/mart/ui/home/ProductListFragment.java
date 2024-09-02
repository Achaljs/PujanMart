package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {
    Button btnCheckOut;
    Button btnMinus;
    Button btnPlus;
    TextView itemCount;
    ImageView ivProduct;
    ImageView ivWishList;
    LinearLayout liAddTocart;
    String mobile;
    NavController navController;
    TextView noOfRatingAndReview;
    String pid;
    ProductModal productModal;
    String productModalList;
    ProgressDialog progress;
    Button tvAddToCartDefault;
    TextView tvDesc;
    TextView tvPname;
    TextView tvPrice;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);
        this.pid = (String) getArguments().get("pid");
        this.mobile = getContext().getSharedPreferences("MySharedPref", 0).getString("mobile", (String) null);
        this.ivWishList = (ImageView) v.findViewById(R.id.ivWishList);
        this.tvPname = (TextView) v.findViewById(R.id.tvPname);
        this.noOfRatingAndReview = (TextView) v.findViewById(R.id.noOfRatingAndReview);
        this.tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        this.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
        this.ivProduct = (ImageView) v.findViewById(R.id.ivProduct);
        this.btnCheckOut = (Button) v.findViewById(R.id.btnCheckOut);
        this.liAddTocart = (LinearLayout) v.findViewById(R.id.liAddTocart);
        this.tvAddToCartDefault = (Button) v.findViewById(R.id.tvAddToCartDefault);
        this.btnMinus = (Button) v.findViewById(R.id.btnMinus);
        this.btnPlus = (Button) v.findViewById(R.id.btnPlus);
        this.itemCount = (TextView) v.findViewById(R.id.itemCount);
        setDef();
        this.tvAddToCartDefault.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.liAddTocart.setVisibility(View.VISIBLE);
                ProductListFragment.this.tvAddToCartDefault.setVisibility(View.GONE);
                ProductListFragment.this.itemCount.setText(String.valueOf(Integer.valueOf(ProductListFragment.this.itemCount.getText().toString()).intValue() + 1));
                ProductListFragment.this.addToCart();
            }
        });
        this.btnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.itemCount.setText(String.valueOf(Integer.valueOf(ProductListFragment.this.itemCount.getText().toString()).intValue() + 1));
                ProductListFragment.this.addToCart();
            }
        });
        this.btnMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int count = Integer.valueOf(ProductListFragment.this.itemCount.getText().toString()).intValue() - 1;
                ProductListFragment.this.itemCount.setText(String.valueOf(count));
                if (count == 0) {
                    ProductListFragment.this.liAddTocart.setVisibility(View.GONE);
                    ProductListFragment.this.tvAddToCartDefault.setVisibility(View.VISIBLE);
                }
                ProductListFragment.this.removeToCart();
            }
        });
        this.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("pid", ProductListFragment.this.pid);
                ProductListFragment.this.navController.navigate(R.id.action_nav_productdesc_to_nav_cart, bundle);
            }
        });
        this.ivWishList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ProductListFragment.this.addToWishList();
            }
        });
        getProductDetail();
        return v;
    }

    public void setDef() {
        this.tvAddToCartDefault.setVisibility(View.VISIBLE);
        this.liAddTocart.setVisibility(View.GONE);
    }

    public void getProductDetail() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getProuctList("https://api.pujanmart.com/productdetails/" + this.pid).enqueue(new Callback<List<ProductModal>>() {
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                System.out.println(response.body());
                ProductListFragment.this.progress.dismiss();
                ProductListFragment.this.productModal = (ProductModal) response.body().get(0);
                ProductListFragment.this.updateUI((ProductModal) response.body().get(0));
            }

            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                Toast.makeText(ProductListFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                ProductListFragment.this.progress.dismiss();
            }
        });
    }

    public void updateUI(ProductModal productModal2) {
        Glide.with(getContext()).load("https://pujanmart.com/images/product/" + productModal2.getProduct_thumb()).into(this.ivProduct);
        this.tvPname.setText(productModal2.getProduct_name());
        this.tvPrice.setText("₹" + String.valueOf(productModal2.getProduct_price()));
        this.tvDesc.setText(productModal2.getProduct_desc());
    }

    public void addToCart() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/cartinsert/" + this.mobile + "/" + this.pid + "/" + getAmount() + "/1").enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().contains("1")) {
                    Toast.makeText(ProductListFragment.this.getContext(), "Added in cart", Toast.LENGTH_SHORT).show();
                }
                ProductListFragment.this.progress.dismiss();
            }

            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ProductListFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                ProductListFragment.this.progress.dismiss();
            }
        });
    }

    public void removeToCart() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/cartremove/" + this.mobile + "/" + this.pid).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.body().contains("1")) {
                        Toast.makeText(ProductListFragment.this.getContext(), "Removed from cart", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ProductListFragment.this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                ProductListFragment.this.progress.dismiss();
            }

            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ProductListFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                ProductListFragment.this.progress.dismiss();
            }
        });
    }

    public double getAmount() {
        return Double.valueOf(this.tvPrice.getText().toString().replace("₹", "")).doubleValue() * Double.valueOf(this.itemCount.getText().toString()).doubleValue();
    }

    public int getQuantity() {
        return Integer.valueOf(this.itemCount.getText().toString()).intValue();
    }

    public void addToWishList() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).send("https://api.pujanmart.com/wishlistadd/" + this.mobile + "/" + this.productModal.getProduct_id()).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response) {
                response.body().contains("1");
                ProductListFragment.this.progress.dismiss();
                ProductListFragment.this.getProductDetail();
                ProductListFragment.this.getWishListMaster();
            }

            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ProductListFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                ProductListFragment.this.progress.dismiss();
            }
        });
    }

    public void onResume() {
        super.onResume();
        getWishListMaster();
    }

    public void getWishListMaster() {
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getProuctList("https://api.pujanmart.com/wishlistget/" + getContext().getSharedPreferences("MySharedPref", 0).getString("mobile", "")).enqueue(new Callback<List<ProductModal>>() {
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                ProductListFragment.this.productModalList = response.body().toString();
                if (ProductListFragment.this.productModalList.contains(ProductListFragment.this.productModal.getProduct_id())) {
                    ProductListFragment.this.ivWishList.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ProductListFragment.this.getContext(), R.color.purple_500)));
                } else {
                    ProductListFragment.this.ivWishList.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ProductListFragment.this.getContext(), R.color.black)));
                }
            }

            public void onFailure(Call<List<ProductModal>> call, Throwable t) {
                Toast.makeText(ProductListFragment.this.getContext(), "An error has occured in Product list", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
