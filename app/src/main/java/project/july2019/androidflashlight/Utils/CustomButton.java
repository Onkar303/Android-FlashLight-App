package project.july2019.androidflashlight.Utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomButton extends AppCompatButton {

    AssetManager am;
    Typeface typeface;

    public CustomButton(Context context) {
        super(context);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context)
    {
       am=context.getApplicationContext().getAssets();
       typeface=Typeface.createFromAsset(am, "fonts/bellasya.ttf");
       setTypeface(typeface);
    }
}
