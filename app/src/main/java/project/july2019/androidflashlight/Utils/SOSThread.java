package project.july2019.androidflashlight.Utils;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.SystemClock;

public class SOSThread extends Thread{

    private int frequency;
    private Context context;
    private CameraManager cameraManager;
    private boolean isEnabled=false;

    public SOSThread(Context context, CameraManager cameraManager)
    {
        this.context=context;
        this.cameraManager=cameraManager;
    }

    @Override
    public void run() {

        for(int i=0;i<100000000;i++)
        {
           try{
               if(isEnabled)
               {
                   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                   {
                       cameraManager.setTorchMode(cameraManager.getCameraIdList()[0],false);
                       isEnabled=false;
                   }
               }
               else{
                   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                   {
                       cameraManager.setTorchMode(cameraManager.getCameraIdList()[0],true);
                       isEnabled=true;
                   }
               }

               sleep(1000/frequency);
           }catch (Exception e)
           {
               e.printStackTrace();
           }
        }
    }

    public void setFrequency(int frequency)
    {
        this.frequency=frequency;
    }





}
