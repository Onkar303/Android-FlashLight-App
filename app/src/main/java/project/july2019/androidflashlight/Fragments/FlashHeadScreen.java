package project.july2019.androidflashlight.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Animations.ButtonAnimations;

public class FlashHeadScreen extends Fragment implements View.OnClickListener {

    private FloatingActionButton flashHeadFab;
    private AnimatorSet enableFlashHeadAnimation;
    private View flashHeadEnableView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flashheadscreen,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }


    public void init(View v)
    {

        flashHeadFab=v.findViewById(R.id.flash_head_fab);
        flashHeadFab.setOnClickListener(this);
        flashHeadEnableView=v.findViewById(R.id.flash_head_enable_view);

        enableFlashHeadAnimation=ButtonAnimations.enableFlashHeadAnimation(flashHeadFab);

        enableFlashHeadAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                System.exit(0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.flash_head_fab:
                enableFlashHeadAnimation.start();
                break;
        }
    }
}
