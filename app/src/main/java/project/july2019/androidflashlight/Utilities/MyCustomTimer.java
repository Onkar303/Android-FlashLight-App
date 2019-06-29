package project.july2019.androidflashlight.Utilities;

import android.os.CountDownTimer;

public class MyCustomTimer extends CountDownTimer {


    MyCustomTimer(int timer,int interval)
    {
        super(timer,interval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
    }

    @Override
    public void onFinish() {

    }
}
