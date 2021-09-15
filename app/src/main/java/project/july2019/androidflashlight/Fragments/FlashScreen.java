package project.july2019.androidflashlight.Fragments;

import android.animation.AnimatorSet;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Animations.ButtonAnimations;
import project.july2019.androidflashlight.Utils.CommonUtils;

public class FlashScreen extends Fragment implements View.OnClickListener{

    private AnimatorSet rippleAnimation;
    private TextView flash_status;
    private CoordinatorLayout mainFrame;
    private boolean isOn = false;
    private ButtonAnimations buttonAnimations;
    private View outerView;
    private FloatingActionButton mainButton;
    private CameraManager camManager;
    private String cameraId = null;



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

        if(!CommonUtils.getSosCameraStatus(getContext()))
        {
            if (!CommonUtils.getFlashCameraStatus(getContext())) {
                try {
                    CommonUtils.enableCamera(getContext(),camManager);
                    rippleAnimation.start();
                    flash_status.setText("On");
                } catch (Exception e) {

                    e.printStackTrace();
                }
                CommonUtils.setFlashCameraStatus(true,getContext());
            } else {

                try {
                    CommonUtils.disableCamera(getContext(),camManager);
                    rippleAnimation.end();
                    flash_status.setText("Off");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CommonUtils.setFlashCameraStatus(false,getContext());
            }
        }
        else
        {
            CommonUtils.endSosAlertPopUp(getContext(),"Please disable SOS \n Disable Now ?","Attention!",camManager);
        }

    }




}
