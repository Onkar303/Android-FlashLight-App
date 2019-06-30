package project.july2019.androidflashlight.Utils;

import android.content.Context;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.CountDownTimer;

public class MyCustomTimer extends CountDownTimer {

    Context context;
    CameraManager cameraManager;
    Thread thread;
    public MyCustomTimer(int timer, int interval, Context context, CameraManager cameraManager)
    {
        super(timer,interval);
        this.context=context;
        this.cameraManager=cameraManager;
        thread=new Thread();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        doFlash(millisUntilFinished);
    }

    @Override
    public void onFinish() {

    }


    public void doFlash(final long milliseconds)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if(milliseconds%2000==0)
                        {
                            cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
                        }
                        else
                        {
                            cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).run();




        }

    }


}
