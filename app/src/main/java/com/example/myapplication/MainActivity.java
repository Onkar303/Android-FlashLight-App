package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton mainButton,leftOffButton,rightOnButton;
    CameraManager camManager;
    String cameraId = null;
    boolean isOpen=false;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFullScreen();
        setTranslucentNavigation();


        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChathead();
            }
        });

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

        leftOffButton=(FloatingActionButton)findViewById(R.id.leftOfButton);


        rightOnButton=(FloatingActionButton)findViewById(R.id.rightOnButton);


        mainButton.setOnClickListener(this);
        leftOffButton.setOnClickListener(this);
        rightOnButton.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.mainButton:
                showButton();
                break;

            case R.id.rightOnButton:
                startFlash();
                break;

            case R.id.leftOfButton:
                endFlash();
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
    public void startFlash() {
        try {
            cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, true);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void endFlash(){

        try{
            cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, false);
        }catch (Exception e)
        {
          e.printStackTrace();
        }

    }

    @SuppressLint("RestrictedApi")
    public void showButton()
    {

        if(!isOpen)
        {
            Animation rightopenanimation = AnimationUtils.loadAnimation(this,R.anim.rightfadein);
            rightopenanimation.setDuration(100);

            Animation leftopenanimation = AnimationUtils.loadAnimation(this,R.anim.leftfadein);
            leftopenanimation.setDuration(100);

            rightOnButton.startAnimation(rightopenanimation);
            leftOffButton.startAnimation(leftopenanimation);


            rightOnButton.setVisibility(View.VISIBLE);
            leftOffButton.setVisibility(View.VISIBLE);

            isOpen=true;
        }
        else
        {
            Animation rightcloseanimation = AnimationUtils.loadAnimation(this,R.anim.rightfadeout);
            rightcloseanimation.setDuration(100);

            Animation leftcloseanimation = AnimationUtils.loadAnimation(this,R.anim.leftfadeout);
            leftcloseanimation.setDuration(100);

            rightOnButton.startAnimation(rightcloseanimation);
            leftOffButton.startAnimation(leftcloseanimation);

            rightOnButton.setVisibility(View.GONE);
            leftOffButton.setVisibility(View.GONE);
            isOpen=false;
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
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_LONG ).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void MyAlertDialog()
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
}

