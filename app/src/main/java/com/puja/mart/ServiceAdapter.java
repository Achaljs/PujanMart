package com.puja.mart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.puja.mart.Adapter.ProductListAdapter;
import com.puja.mart.Modal.CartModal;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyView> {
    CatItemclickListener catItemclickListener;
    /* access modifiers changed from: private */
    public ArrayList<ServiceModle> catModalList;
    Context context;

    public ServiceAdapter(ArrayList<ServiceModle> catModalList2, Context context2, CatItemclickListener catItemclickListener2) {
        this.catModalList = catModalList2;
        this.context = context2;
        this.catItemclickListener = catItemclickListener2;
    }

    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        ServiceModle serviceModle = catModalList.get(holder.getAdapterPosition());

        holder.textView.setText(serviceModle.getProductName().toString());
        holder.tvPrize.setText("₹ "+serviceModle.getProductMrp());
        holder.tvPrize.setPaintFlags(holder.tvPrize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvFinalprize.setText("₹ "+serviceModle.getProductSell()+"/-");
        holder.disc.setText(" "+serviceModle.getProductDisc()+"% OFF ");

        Glide.with(context)
                .load("https://pujanmart.com/images/other/" + serviceModle.getProductThumb())
                .error(R.mipmap.head)  // Placeholder in case of error
                .into(holder.productImage);


        holder.liMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i=new Intent(context,Bookservice.class);
              i.putExtra("id",serviceModle.getProductId());
              i.putExtra("name",serviceModle.getProductName());

                context.startActivity(i);
            }
        });
    }



    public int getItemCount() {
        return this.catModalList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        LinearLayout liMain;
        ShapeableImageView productImage;
        TextView textView;
        TextView tvPrize;
        TextView tvFinalprize;
        TextView disc;

        public MyView(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.tvProductName);
            this.productImage = (ShapeableImageView) view.findViewById(R.id.productImage);
            this.liMain = (LinearLayout) view.findViewById(R.id.liMain);
            this.tvPrize=(TextView) view.findViewById(R.id.tvPrize);
            this.tvFinalprize=(TextView) view.findViewById(R.id.tvfinalPrize);
            this.disc=(TextView) view.findViewById(R.id.discount);

        }
    }
}
