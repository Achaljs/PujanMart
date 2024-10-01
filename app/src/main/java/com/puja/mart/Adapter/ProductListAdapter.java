package com.puja.mart.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.ProductItemclickListener;
import com.puja.mart.R;
import com.puja.mart.ServiceModle;

import java.util.List;

public class ProductListAdapter extends ArrayAdapter<ProductModal> {
    int custom_layout_id;
    List<ProductModal> items_list;
    ProductItemclickListener productItemclickListener;

    public ProductListAdapter(Context context, int resource, List<ProductModal> objects, ProductItemclickListener productItemclickListener2) {
        super(context, resource, objects);
        this.items_list = objects;
        this.custom_layout_id = resource;
        this.productItemclickListener = productItemclickListener2;
    }

    public int getCount() {
        return this.items_list.size();
    }

    public View getView( int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.custom_layout_id, (ViewGroup) null);
        }

        ProductModal product = this.items_list.get(position);
        Glide.with(getContext()).load("https://pujanmart.com/images/product/" + this.items_list.get(position).getProduct_thumb()).into((ShapeableImageView) v.findViewById(R.id.productImage));
        ((TextView) v.findViewById(R.id.tvProductName)).setText(product.getProduct_name().toString());
       ((TextView) v.findViewById(R.id.tvPrize)).setText("₹" +product.getProduct_price()+ "/-");
       ((TextView) v.findViewById(R.id.tvPrize)).setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG);
//
        ((TextView) v.findViewById(R.id.discount)).setText(" "+product.getProduct_disc()+ " % OFF ");
       ((TextView) v.findViewById(R.id.tvfinalPrize)).setText("₹" +product.getProduct_new_price()+ "/-");

        ((LinearLayout) v.findViewById(R.id.liProduct)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ProductListAdapter.this.productItemclickListener.onClick(ProductListAdapter.this.items_list.get(position).getProduct_id());
            }
        });
        return v;
    }
}
