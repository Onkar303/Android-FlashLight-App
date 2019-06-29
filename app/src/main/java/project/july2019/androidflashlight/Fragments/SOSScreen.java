package project.july2019.androidflashlight.Fragments;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidflashlight.R;

import java.util.Calendar;
import java.util.Date;

import project.july2019.androidflashlight.Utils.CircularSeekBar;

public class SOSScreen extends Fragment implements View.OnClickListener,CircularSeekBar.OnCircularSeekBarChangeListener{

    FloatingActionButton sosButton;
    CircularSeekBar circularSeekBar;
    Vibrator vibrator;
    VibrationEffect vibrationEffect;
    CameraManager cameraManager;
    Date date;
    Calendar cal;
    CountDownTimer countDownTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sosscreen,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    public void init(View view)
    {
        sosButton=view.findViewById(R.id.sosbutton);
        sosButton.setOnClickListener(this);

        circularSeekBar=view.findViewById(R.id.circularseekbar);
        circularSeekBar.setOnSeekBarChangeListener(this);

        vibrator= (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        cameraManager=(CameraManager)getActivity().getSystemService(Context.CAMERA_SERVICE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sosbutton:
                countDownTimer.start();
                break;
        }
    }

    //circular bar methods
    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        int max = circularSeekBar.getMax();
        if((progress%25) == 0 && progress !=0  )
        {
            //Toast.makeText(getContext(), "tiggered", Toast.LENGTH_SHORT).show();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            {
                vibrator.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else
            {
                vibrator.vibrate(100);
            }

        }


    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {

    }

    public void doFlash(final long milliseconds)
    {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        cal.get(Calendar.SECOND);
                        try {
                            if(milliseconds%2==0)
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
