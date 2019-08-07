package project.july2019.androidflashlight;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Adapters.CustomViewPagerAdpater;
import project.july2019.androidflashlight.Fragments.FlashHeadScreen;
import project.july2019.androidflashlight.Fragments.FlashScreen;
import project.july2019.androidflashlight.Fragments.SOSScreen;
import project.july2019.androidflashlight.Utils.CommonUtils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {


    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    ViewPager viewPager;
    CustomViewPagerAdpater adapter;
    List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CommonUtils.setFullScreen(getWindow());
        CommonUtils.setTranslucentNavigation(getWindow());
        CommonUtils.setHidingNavigationBar(getWindow());


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET},
                1);

        initAdMob();
        init();

    }

    public void initFlashHead() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }

    }

    private void initializeView() {
        startService(new Intent(MainActivity.this, ChatHeadServcie.class));
        finish();
    }


    public boolean hasFlashLight() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    public void init() {

        viewPager=findViewById(R.id.viewpager);
        list=new ArrayList<>();
        //list.add(new FlashHeadScreen());
        list.add(new FlashScreen());
        list.add(new SOSScreen());
        adapter=new CustomViewPagerAdpater(getSupportFragmentManager(),list,this);
        viewPager.setAdapter(adapter);

        //setting the viepager at the middle
        //viewPager.setCurrentItem(1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setTitle("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        navigationView=findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);


        drawer=findViewById(R.id.my_drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }






    public void initAdMob() {
        MobileAds.initialize(this, getResources().getString(R.string.admob_ID));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    onStop();
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    initializeView();
                } else { //Permission is not available
                    Toast.makeText(this,
                            getResources().getString(R.string.overlay_grant_permission),
                            Toast.LENGTH_LONG).show();

                }
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setHidingNavigationBar(getWindow());
    }


    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.addflash_head:
                  initFlashHead();
                  return true;

            case R.id.rate_app:
                rateApp();
                return true;

            case R.id.share_app:
                ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText("http://play.google.com/store/apps/details?id=" + getPackageName())
                        .startChooser();
                return true;

            case R.id.remove_ads:
                return true;

            case R.id.reset:
                return true;

            case R.id.app_info:
                return true;

            case R.id.exit:
                CommonUtils.exitAlertPopUp(this,"Do you want to exit?","");
                return true;
        }

        return false;
    }

    public void rateApp()
    {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }


}

