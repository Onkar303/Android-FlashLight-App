package project.july2019.androidflashlight.Fragments;

import android.animation.AnimatorSet;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Animations.ButtonAnimations;

public class FlashScreen extends Fragment implements View.OnClickListener{

    AnimatorSet rippleAnimation;
    TextView flash_status;
    CoordinatorLayout mainFrame;
    boolean isOn = false;
    ButtonAnimations buttonAnimations;
    View outerView;
    FloatingActionButton mainButton;
    CameraManager camManager;
    String cameraId = null;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flashscreen,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    public void init(View view)
    {
        flash_status=view.findViewById(R.id.flash_status);
        flash_status.setText("Off");
        buttonAnimations = new ButtonAnimations();
        outerView = view.findViewById(R.id.outer_view);
        mainFrame = (CoordinatorLayout)view.findViewById(R.id.parent_layout);

        camManager = (CameraManager)getContext().getSystemService(Context.CAMERA_SERVICE);
        mainButton = (FloatingActionButton)view.findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);

        rippleAnimation=ButtonAnimations.rippleAnimation(outerView);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainButton:
                doFlash();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doFlash() {

        if (!isOn) {
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);
                rippleAnimation.start();
                flash_status.setText("On");
            } catch (Exception e) {

                e.printStackTrace();
            }
            isOn = true;
        } else {

            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);
                rippleAnimation.end();
                flash_status.setText("Off");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isOn = false;
        }

    }
}