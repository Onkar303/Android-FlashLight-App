package project.july2019.androidflashlight;

import android.Manifest;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Animations.ButtonAnimations;
import project.july2019.androidflashlight.Utilities.CustomButton;
import project.july2019.androidflashlight.Utils.CommonUtils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton mainButton, leftOffButton, rightOnButton;
    CameraManager camManager;
    String cameraId = null;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    CustomButton button;
    CoordinatorLayout mainFrame;
    boolean isOn = false;
    ButtonAnimations buttonAnimations;
    View outerView;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    AnimatorSet animatorSet;
    TextView flash_status;


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

        flash_status=findViewById(R.id.flash_status);
        flash_status.setText("Off");
        buttonAnimations = new ButtonAnimations();
        outerView = findViewById(R.id.outer_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setTitle("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mainFrame = (CoordinatorLayout) findViewById(R.id.parent_layout);

        navigationView=findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);



        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        mainButton = (FloatingActionButton) findViewById(R.id.mainButton);

        mainButton.setOnClickListener(this);

        drawer=findViewById(R.id.my_drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        animatorSet=ButtonAnimations.rippleAnimation(outerView);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainButton:
                doFlash();


                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doFlash() {

        if (!isOn) {
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);
                animatorSet.start();
                flash_status.setText("On");
            } catch (Exception e) {

                e.printStackTrace();
            }
            isOn = true;
        } else {

            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);
                animatorSet.end();
                flash_status.setText("Off");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isOn = false;
        }

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
}

