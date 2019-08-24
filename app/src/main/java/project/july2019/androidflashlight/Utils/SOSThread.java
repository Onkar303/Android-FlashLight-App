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

    public SOSThread(Context context, CameraManager cameraManager) {
        this.context = context;
        this.cameraManager = cameraManager;
    }

    @Override
    public void run() {
        for (long i = 0; i < 1000000000; i++) {

            try {
                if (isEnabled) {
                    disableCamera();
                } else {
                    enableCamera();
                }
                sleep(1000 / frequency);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void enableCamera() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
                isEnabled = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableCamera() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
                isEnabled = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {

        if (isEnabled) {
            disableCamera();


        }

    }


}
