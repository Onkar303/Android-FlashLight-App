package project.july2019.androidflashlight.Animations;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class ButtonAnimations {

    public void buttonScaleAnimation(Context context,View v){


        Animation anim = new ScaleAnimation(
                1f, 0.8f, // Start and end values for the X axis scaling
                1f, 0.8f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(false); // Needed to keep the result of the animation
        anim.setDuration(100);
        v.startAnimation(anim);


    }


}
