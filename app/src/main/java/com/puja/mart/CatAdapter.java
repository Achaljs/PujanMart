package com.puja.mart;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.ProductItemclickListener;
import com.puja.mart.R;
import com.puja.mart.ServiceModle;

import java.util.List;

public class CatAdapter extends ArrayAdapter<CatModal> {
    int custom_layout_id;
    List<CatModal> items_list;
    CatItemclickListener catItemclickListener;


    public CatAdapter(Context context, int resource, List<CatModal> objects, CatItemclickListener catItemclickListener) {
        super(context, resource, objects);
        this.items_list = objects;
        this.custom_layout_id = resource;
        this.catItemclickListener = catItemclickListener;
    }

    public int getCount() {
        return this.items_list.size();
    }

    public View getView( int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.custom_layout_id, (ViewGroup) null);
        }

        CatModal product = this.items_list.get(position);
        Glide.with(getContext()).load("https://pujanmart.com/images/ctgy/" + this.items_list.get(position).ctgy_img).into((ShapeableImageView) v.findViewById(R.id.productImage));
        ((TextView) v.findViewById(R.id.tvProductName)).setText(product.ctgy_name.toString());

        ((LinearLayout) v.findViewById(R.id.liProduct)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            CatAdapter.this.catItemclickListener.onClickCat(product.getCtgy_slug());
            }
        });
        return v;
    }
}
