package com.puja.mart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.puja.mart.databinding.ActivityGlobalBinding;

public class GlobalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityGlobalBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGlobalBinding inflate = ActivityGlobalBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        setSupportActionBar(this.binding.appBarGlobal.toolbar);
        DrawerLayout drawer = this.binding.drawerLayout;
        NavigationView navigationView = this.binding.navView;
        this.mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_global);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) this, navController, this.mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment_content_global), this.mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_global);
        int id = item.getItemId();
        if (id == R.id.logout) {
            SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", 0).edit();
            editor.clear();
            editor.apply();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_myprofile) {
            navController.navigate(R.id.action_nav_home_to_nav_myprofile);
        } else if (id == R.id.nav_home) {
            navController.navigate(R.id.action_nav_myprofile_to_nav_home);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }
}
