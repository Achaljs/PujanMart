package com.puja.mart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.MyView> {
    CatItemclickListener catItemclickListener;
    /* access modifiers changed from: private */
    public List<CatModal> catModalList;
    Context context;

    public CatAdapter(List<CatModal> catModalList2, Context context2, CatItemclickListener catItemclickListener2) {
        this.catModalList = catModalList2;
        this.context = context2;
        this.catItemclickListener = catItemclickListener2;
    }

    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        holder.textView.setText(this.catModalList.get(holder.getAdapterPosition()).getCtgy_name());
        Glide.with(this.context).load("https://pujanmart.com/images/ctgy/" + this.catModalList.get(holder.getAdapterPosition()).getCtgy_img()).into(holder.productImage);
        holder.liMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CatAdapter.this.catItemclickListener.onClickCat(((CatModal) CatAdapter.this.catModalList.get(holder.getAdapterPosition())).getCtgy_slug());
            }
        });
    }


    public int getItemCount() {
        return this.catModalList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        LinearLayout liMain;
        ImageView productImage;
        TextView textView;

        public MyView(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.tvProductName);
            this.productImage = (ImageView) view.findViewById(R.id.productImage);
            this.liMain = (LinearLayout) view.findViewById(R.id.liMain);
        }
    }
}
