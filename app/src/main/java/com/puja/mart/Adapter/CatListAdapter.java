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
import com.puja.mart.CatItemclickListener;
import com.puja.mart.R;
import com.puja.mart.SubCatModal;
import java.util.List;

public class CatListAdapter extends ArrayAdapter<SubCatModal> {
    CatItemclickListener catItemclickListener;
    String cid;
    int custom_layout_id;
    List<SubCatModal> items_list;

    public CatListAdapter(Context context, int resource, List<SubCatModal> objects, CatItemclickListener catItemclickListener2) {
        super(context, resource, objects);
        this.items_list = objects;
        this.custom_layout_id = resource;
        this.catItemclickListener = catItemclickListener2;
    }

    public int getCount() {
        return this.items_list.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.custom_layout_id, (ViewGroup) null);
        }
        ((TextView) v.findViewById(R.id.tvPrice)).setVisibility(View.GONE);
        Glide.with(getContext()).load("https://pujanmart.com/images/subctgy/" + this.items_list.get(position).getSubctgy_img()).into((ImageView) v.findViewById(R.id.productImage));
        ((TextView) v.findViewById(R.id.tvProductName)).setText(this.items_list.get(position).getSubctgy_name());
        ((LinearLayout) v.findViewById(R.id.liProduct)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CatListAdapter.this.catItemclickListener.onClickCat(CatListAdapter.this.items_list.get(position).getSubctgy_slug());
            }
        });
        return v;
    }
}
