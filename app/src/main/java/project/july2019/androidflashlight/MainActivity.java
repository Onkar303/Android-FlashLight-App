package project.july2019.androidflashlight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Animations.ButtonAnimations;
import project.july2019.androidflashlight.Utilities.CustomButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class   MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton mainButton,leftOffButton,rightOnButton;
    CameraManager camManager;
    String cameraId = null;
    boolean isOpen=false;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    CustomButton button;
    CoordinatorLayout mainFrame;
    boolean isOn=false;
    ButtonAnimations buttonAnimations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFullScreen();
        setTranslucentNavigation();
        setHidingNavigationBar();



        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.INTERNET},
                1);

        initAdMob();

        init();

    }

    public void initChathead(){
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


    public boolean hasFlashLight()
    {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    public void init()
    {

         buttonAnimations=new ButtonAnimations();


        Toolbar toolbar=(Toolbar)findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setTitle("");
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        mainFrame=(CoordinatorLayout)findViewById(R.id.parent_layout);

        button=(CustomButton)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChathead();
            }
        });


        //experiment
//
//        GradientDrawable gradientDrawable = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM,
//                new int[]{ContextCompat.getColor(this, R.color.colorAccent),
//                        ContextCompat.getColor(this, R.color.colorPrimary),
//                        ContextCompat.getColor(this, R.color.colorPrimaryDark),
//                       });
//
//        findViewById(R.id.parent_layout).setBackground(gradientDrawable);


        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        mainButton=(FloatingActionButton)findViewById(R.id.mainButton);

        mainButton.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.mainButton:
                doFlash();
                buttonAnimations.buttonScaleAnimation(this,mainButton);
                break;
        }
    }


    public void MyToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void ErrorAlert(String errorMessage)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(errorMessage);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog=builder.create();
        dialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doFlash() {

        if(!isOn)
        {
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);
            } catch (Exception e) {

                e.printStackTrace();
            }
            isOn=true;
        }
        else
        {

            try{
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            isOn=false;
        }

    }


    public void initAdMob(){
        MobileAds.initialize(this,getResources().getString(R.string.admob_ID));
        AdView mAdView = (AdView)findViewById(R.id.adView);
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

    public void setFullScreen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setTranslucentNavigation()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                if (Settings.canDrawOverlays(this)) {
                    initializeView();
                } else { //Permission is not available
                    Toast.makeText(this,
                            getResources().getString(R.string.overlay_grant_permission),
                            Toast.LENGTH_LONG ).show();

                }
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setHidingNavigationBar(){

        getWindow().getDecorView().setSystemUiVisibility(  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }



    public void MyAlertWindow()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Attention");
        builder.setMessage("Keep flash active ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setHidingNavigationBar();
    }



    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
}
