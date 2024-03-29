package project.july2019.androidflashlight.Fragments;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Utils.CircularSeekBar;
import project.july2019.androidflashlight.Utils.CommonUtils;
import project.july2019.androidflashlight.Utils.SOSThread;

public class SOSScreen extends Fragment implements View.OnClickListener, CircularSeekBar.OnCircularSeekBarChangeListener {

    private FloatingActionButton sosButton;
    private CircularSeekBar circularSeekBar;
    private Vibrator vibrator;
    private VibrationEffect vibrationEffect;
    private CameraManager cameraManager;
    private TextView frequencyText;
    private boolean isEnabled = false;
    private SOSThread sosThread;
    private int frequency;
    private Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sosscreen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    public void init(View view) {

        sosButton = view.findViewById(R.id.sosbutton);
        sosButton.setOnClickListener(this);

        circularSeekBar = view.findViewById(R.id.circularseekbar);
        circularSeekBar.setOnSeekBarChangeListener(this);
        frequencyText = view.findViewById(R.id.frequencyText);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        //myCustomTimer=new MyCustomTimer(1000000000,1000,getContext(),cameraManager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sosbutton:

                if(!CommonUtils.getFlashCameraStatus(getContext()))
                {
                    if (CommonUtils.getSosCameraStatus(getContext())) {
                        try {
                            sosThread.interrupt();
                            CommonUtils.setSosCameraStatus(false,getContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sosThread = new SOSThread(getActivity(), cameraManager);
                        sosThread.start();
                        CommonUtils.setSosCameraStatus(true,getContext());
                    }
                }
                else
                {
                    CommonUtils.endFlashAlertPopUp(getContext(),"Please disable Flash \n Disable Now ?","Attention!",cameraManager);
                }




//                    if(isEnabled)
//                    {
//                        myCustomTimer.cancel();
//                        try
//                        {
//                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
//                            {
//                                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0],false);
//                            }
//                        }catch(Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        isEnabled=false;
//                    }
//                else
//                {
//                    myCustomTimer.start();
//                    isEnabled=true;
//                }
                break;
        }
    }

    //circular seek bar methods
    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

        try
        {

            if ((progress % 25) == 0) {

                frequencyText.setText(String.valueOf((progress / 25)));
                frequency = progress / 25;
                CommonUtils.setCameraFrequency(getContext(),frequency);
                sosThread.setFrequency(CommonUtils.getCameraFrequency(getContext()));
                //Toast.makeText(getContext(), "tiggered", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(70);
                }
            }
        }catch (Exception e)
        {
            System.out.println("divide by 0 exception");
        }
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) { }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) { }

}
