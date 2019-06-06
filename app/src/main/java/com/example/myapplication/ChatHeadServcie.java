package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class ChatHeadServcie extends Service {

    private WindowManager windowManager;
    private View mChatHeadView;
    boolean isFlashLightOn=false;

    public ChatHeadServcie()
    {

    }

    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    @Override
    public void onCreate() {
        super.onCreate();

        mChatHeadView=LayoutInflater.from(this).inflate(R.layout.chatheadlayout,null);




//        final WindowManager.LayoutParams params=new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//        );

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.gravity= Gravity.TOP;
        params.x=0;
        params.y=100;

        windowManager=(WindowManager)getSystemService(Context.WINDOW_SERVICE);

        windowManager.addView(mChatHeadView, params);



        Button fab=(Button)mChatHeadView.findViewById(R.id.fab);
        fab.setOnTouchListener(new View.OnTouchListener() {

            int lastAction;
            int intitalX;
            int intitalY;
            float initialTouchX;
            float initialTouchY;


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        intitalX=params.x;
                        intitalY=params.y;

                        initialTouchX=event.getRawX();
                        initialTouchY=event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:

                        /*if(lastAction==MotionEvent.ACTION_DOWN)
                        {
                            intitalX = params.x;
                            intitalY = params.y;

                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();

                            Toast.makeText(ChatHeadServcie.this, "clicked", Toast.LENGTH_SHORT).show();
                        }
                        lastAction=event.getAction();*/

                        if( (Math.abs(initialTouchX - event.getRawX())<5) && (Math.abs(initialTouchY - event.getRawY())<5) )
                        {

                                configureFlashLight();

                        }
                        else Log.e(TAG,"you moved the head");
                        return true;



                    case  MotionEvent.ACTION_MOVE:

                        params.x = intitalX + (int) (event.getRawX() - initialTouchX);
                        params.y = intitalY + (int) (event.getRawY() - initialTouchY);

                        windowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;

                }

                return false;



            }
        });


        ImageView cancelView=(ImageView)mChatHeadView.findViewById(R.id.removeView);
        cancelView.bringToFront();

        cancelView.setOnTouchListener(new View.OnTouchListener() {

            int lastAction;
            int intitalX;
            int intitalY;
            float initialTouchX;
            float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {


                    case MotionEvent.ACTION_DOWN:

                        intitalX=params.x;
                        intitalY=params.y;

                        initialTouchX=event.getRawX();
                        initialTouchY=event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        if( (Math.abs(initialTouchX - event.getRawX())<5) && (Math.abs(initialTouchY - event.getRawY())<5) )
                        {
                            windowManager.removeView(mChatHeadView);
                            stopSelf();

                        }

                        return true;
                }

                return false;
            }
        });


        //the below method is redunadant as we handle this event during the on touch listner

//        fab.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mChatHeadView == null)
        {
           // windowManager.removeView(mChatHeadView);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void configureFlashLight(){

        CameraManager cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);

        if(isFlashLightOn)
        {
            try{
                String camerId=cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(camerId,false);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            isFlashLightOn=false;
        }else
        {
            try{
                String camerId=cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(camerId,true);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            isFlashLightOn=true;
        }

    }


}
