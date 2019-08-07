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

    public static AnimatorSet rippleAnimation(View v1) {

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorV1 = ObjectAnimator.ofPropertyValuesHolder(v1, PropertyValuesHolder.ofFloat("scaleX", 4.6f), PropertyValuesHolder.ofFloat("scaleY", 4.6f));
        animatorV1.setDuration(3000);
        animatorV1.setRepeatCount(ObjectAnimator.INFINITE);
        animatorV1.setRepeatMode(ObjectAnimator.RESTART);

        ObjectAnimator fadeoutV1=ObjectAnimator.ofFloat(v1,"alpha",1,0);
        fadeoutV1.setDuration(3000);
        fadeoutV1.setRepeatCount(ObjectAnimator.INFINITE);
        fadeoutV1.setRepeatMode(ObjectAnimator.RESTART);


        animatorSet.playTogether(animatorV1,fadeoutV1);
        return animatorSet;
    }


    public static AnimatorSet enableFlashHeadAnimation(View view)
    {
        AnimatorSet animatorSet=new AnimatorSet();


        ObjectAnimator enableAnimation=ObjectAnimator.ofPropertyValuesHolder(view,PropertyValuesHolder.ofFloat("scaleX", 7.0f), PropertyValuesHolder.ofFloat("scaleY", 7.0f));
        enableAnimation.setDuration(750);

        animatorSet.play(enableAnimation);

        return animatorSet;
    }


}
