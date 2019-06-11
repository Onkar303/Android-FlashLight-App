package project.july2019.androidflashlight.Animations;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

    public static AnimatorSet rippleAnimation(View v1,View v2) {

        AnimatorSet animatorSet = new AnimatorSet();


        ObjectAnimator animatorV2 = ObjectAnimator.ofPropertyValuesHolder(v2, PropertyValuesHolder.ofFloat("scaleX", 4.6f), PropertyValuesHolder.ofFloat("scaleY", 4.6f));
        animatorV2.setDuration(3000);
        animatorV2.setRepeatCount(ObjectAnimator.INFINITE);
        animatorV2.setRepeatMode(ObjectAnimator.RESTART);

        ObjectAnimator fadeoutV2=ObjectAnimator.ofFloat(v2,"alpha",1,0);
        fadeoutV2.setDuration(3000);
        fadeoutV2.setRepeatCount(ObjectAnimator.INFINITE);
        fadeoutV2.setRepeatMode(ObjectAnimator.RESTART);

        animatorSet.playTogether(animatorV2,fadeoutV2);
        return animatorSet;
    }

}
