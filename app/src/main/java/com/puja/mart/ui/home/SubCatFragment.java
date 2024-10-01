package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Adapter.CatListAdapter;
import com.puja.mart.CatItemclickListener;
import com.puja.mart.R;
import com.puja.mart.SubCatModal;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatFragment extends Fragment implements CatItemclickListener {
    CatItemclickListener catItemclickListener;
    String cid;
    GridView gvSubcatList;
    NavController navController;
    ProgressDialog progress;
    TextView showAll;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sub_cat, container, false);
        this.cid = (String) getArguments().get("cid");
        this.gvSubcatList = (GridView) v.findViewById(R.id.gvSubcatList);
        this.showAll = (TextView) v.findViewById(R.id.showAll);
        this.catItemclickListener = this;
        ((TextView) v.findViewById(R.id.tvCategory)).setText(this.cid + ":");
        getSubCat(this.cid);
        this.showAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("cid", SubCatFragment.this.cid);
                bundle.putString("scid", "-1");
                SubCatFragment.this.navController.navigate(R.id.action_nav_subcat_to_nav_productfilter, bundle);
            }
        });
        return v;
    }

    public void getSubCat(String cid2) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getSubCatList("https://api.pujanmart.com/productsubctgylist/" + cid2).enqueue(new Callback<List<SubCatModal>>() {
            public void onResponse(Call<List<SubCatModal>> call, Response<List<SubCatModal>> response) {
                SubCatFragment.this.setProductList(response.body());
            }

            public void onFailure(Call<List<SubCatModal>> call, Throwable t) {
                Toast.makeText(SubCatFragment.this.getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                SubCatFragment.this.progress.dismiss();
            }
        });
    }

    public void setProductList(List<SubCatModal> productModalList) {
        this.gvSubcatList.setAdapter(new CatListAdapter(getContext(), R.layout.categorylayout, productModalList, this.catItemclickListener));
        this.progress.dismiss();
    }

    public void onClickCat(String subcatid) {
        Bundle bundle = new Bundle();
        bundle.putString("cid", this.cid);
        bundle.putString("scid", subcatid);
        this.navController.navigate(R.id.action_nav_subcat_to_nav_productfilter, bundle);
    }
}
