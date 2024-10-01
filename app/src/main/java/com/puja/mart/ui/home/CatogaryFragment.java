package com.puja.mart.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.puja.mart.API.Api;
import com.puja.mart.API.ApiInterface;
import com.puja.mart.Adapter.ProductListAdapter;
import com.puja.mart.CatAdapter;
import com.puja.mart.CatItemclickListener;
import com.puja.mart.CatModal;
import com.puja.mart.ProductItemclickListener;
import com.puja.mart.R;
import com.puja.mart.databinding.FragmentCatogaryBinding;
import com.puja.mart.databinding.FragmentHomeBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CatogaryFragment extends Fragment implements CatItemclickListener {

    NavController navController;
    ProgressDialog progress;

    private FragmentCatogaryBinding binding;
    CatItemclickListener catItemclickListener;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCatogaryBinding inflate = FragmentCatogaryBinding.inflate(inflater, container, false);
        binding = inflate;
        View root = inflate.getRoot();
        this.catItemclickListener=this;
         getcateogory();
        return root;
    }


    public void getcateogory() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progress = progressDialog;
        progressDialog.setTitle("Loading");
        this.progress.setMessage("Wait while loading...");
        this.progress.setCancelable(false);
        this.progress.show();
        ((ApiInterface) Api.getRetrofitInstanceForSMS().create(ApiInterface.class)).getCatList("https://api.pujanmart.com/productctgylist").enqueue(new Callback<List<CatModal>>() {
            public void onResponse(Call<List<CatModal>> call, Response<List<CatModal>> response) {
                CatogaryFragment.this.progress.dismiss();
                CatogaryFragment.this.setCategory(response.body());
            }

            public void onFailure(Call<List<CatModal>> call, Throwable t) {
                Toast.makeText(CatogaryFragment.this.getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                CatogaryFragment.this.progress.dismiss();
            }
        });
    }


    public void setCategory(List<CatModal> list) {

        binding.gvProductList.setAdapter(new CatAdapter(getContext(), R.layout.categorylayout, list, catItemclickListener));
        this.progress.dismiss();

    }



    public void onClickCat(String str) {


        Bundle bundle = new Bundle();
        bundle.putString("cid", str);
       this.navController.navigate(R.id.action_nav_category_to_nav_subcat, bundle);
    }
}