package project.july2019.androidflashlight.Utils;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;

public class SOSThread implements Runnable {

    Context context;

    public SOSThread(Context context)
    {
        this.context=context;
    }

    @Override
    public void run() {
       long x = SystemClock.uptimeMillis();
    }


}
