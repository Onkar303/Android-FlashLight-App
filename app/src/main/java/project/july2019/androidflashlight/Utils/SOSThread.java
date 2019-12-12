package project.july2019.androidflashlight.Utils;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.SystemClock;

public class SOSThread extends Thread {

    private int frequency;
    private Context context;
    private CameraManager cameraManager;
    private boolean isEnabled = false;
    private int count = 1000000000;

    public SOSThread(Context context, CameraManager cameraManager) {
        this.context = context;
        this.cameraManager = cameraManager;
    }

    @Override
    public void run() {
        for (long i = 0; i < count; i++) {

            try {
                    if(frequency == 0)
                    {
                        CommonUtils.enableCamera(context,cameraManager);
                    }
                    else
                    {
                        CommonUtils.disableCamera(context,cameraManager);
                        if (isEnabled) {
                            CommonUtils.disableCamera(context,cameraManager);
                        } else {
                            CommonUtils.enableCamera(context,cameraManager);
                        }

                        sleep(1000 / frequency);
                    }


            } catch (InterruptedException e) {
                CommonUtils.disableCamera(context,cameraManager);
                if(isInterrupted())
                {
                    interrupted();
                }
                return;

            }

        }
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency()
    {
        return frequency;
    }


    public void stopThread() {

        if (isEnabled) {
            CommonUtils.disableCamera(context,cameraManager);
        }

    }


}
