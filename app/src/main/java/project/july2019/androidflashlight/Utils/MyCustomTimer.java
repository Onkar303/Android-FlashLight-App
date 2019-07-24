package project.july2019.androidflashlight.Utils;

import android.content.Context;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

import project.july2019.androidflashlight.Fragments.SOSScreen;

public class MyCustomTimer extends CountDownTimer {

    Context context;
    CameraManager cameraManager;
    int currentFrequency=0;
    SOSScreen sosScreen;


    public MyCustomTimer(int timer, int interval, Context context, CameraManager cameraManager) {
        super(timer, interval);
        this.context = context;
        this.cameraManager = cameraManager;


    }

    @Override
    public void onTick(long millisUntilFinished) {
        doFlash(millisUntilFinished);
    }

    @Override
    public void onFinish() {

    }


    public void doFlash(final long milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Log.d("seconds",String.valueOf(Math.round(milliseconds/1000)));
            try {
                if ((Math.round(milliseconds/1000)%(currentFrequency+1))==0) {
                    cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
                } else {
                    cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
