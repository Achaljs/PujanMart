package com.puja.mart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.ProductItemclickListener;
import com.puja.mart.R;

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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.custom_layout_id, (ViewGroup) null);
        }
        Glide.with(getContext()).load("https://pujanmart.com/images/product/" + this.items_list.get(position).getProduct_thumb()).into((ImageView) v.findViewById(R.id.productImage));
        ((TextView) v.findViewById(R.id.tvProductName)).setText(this.items_list.get(position).getProduct_name());
        ((TextView) v.findViewById(R.id.tvPrice)).setText("₹ " + this.items_list.get(position).getProduct_price() + " /-");
        ((LinearLayout) v.findViewById(R.id.liProduct)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ProductListAdapter.this.productItemclickListener.onClick(ProductListAdapter.this.items_list.get(position).getProduct_id());
            }
        });
        return v;
    }
}
