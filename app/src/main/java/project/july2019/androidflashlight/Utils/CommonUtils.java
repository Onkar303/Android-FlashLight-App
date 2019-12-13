package project.july2019.androidflashlight.Utils;


import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Animations.ButtonAnimations;
import project.july2019.androidflashlight.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class CommonUtils {


    static AnimatorSet rippleAnimation;

    public static void alertPopUp(Context context,String title,String message)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog=builder.create();
        dialog.getWindow().setWindowAnimations(R.style.AppTheme);
        dialog.show();
    }


    public static void errorToast(Context context,String title)
    {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    public static void setHidingNavigationBar(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public static void setFullScreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setTranslucentNavigation(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    public static void endFlashAlertPopUp(final Context context, String message, String title, final CameraManager cameraManager)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View v= LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout,null);
        TextView message_text=v.findViewById(R.id.message);
        TextView title_text=v.findViewById(R.id.title);
        Button button_yes=v.findViewById(R.id.button_yes);
        Button button_no=v.findViewById(R.id.button_no);
        message_text.setText(message);
        title_text.setText(title);
        builder.setView(v);
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        alertDialog.getWindow().setWindowAnimations(R.style.AppTheme);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setLayout(1000,700);
        //alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.aler_dialog_background));

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    setFlashCameraStatus(false,context);
                    disableCamera(context,cameraManager);
                    alertDialog.dismiss();

            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    public static void endSosAlertPopUp(final Context context, String message, String title, final CameraManager cameraManager)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View v= LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout,null);
        TextView message_text=v.findViewById(R.id.message);
        TextView title_text=v.findViewById(R.id.title);
        Button button_yes=v.findViewById(R.id.button_yes);
        Button button_no=v.findViewById(R.id.button_no);
        message_text.setText(message);
        title_text.setText(title);
        builder.setView(v);
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        alertDialog.getWindow().setWindowAnimations(R.style.AppTheme);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setLayout(1000,700);
        //alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.aler_dialog_background));

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SOSThread.interrupted();
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    public static void setExitBottomDialog(final Context context){

        final BottomSheetDialog dialog=new BottomSheetDialog(context,R.style.AlertDiaogTheme);

        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheetdialogexit,null);
        dialog.setContentView(view);

        CustomTextView yes = view.findViewById(R.id.yes);
        CustomTextView no = view.findViewById(R.id.no);


        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public static void initSharePreferences(Context context)
    {
        SharedPreferences.Editor preferences=context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE).edit();
        preferences.putBoolean(StringConstants.preferenceAttrFlash,false);
        preferences.putBoolean(StringConstants.preferenceAttrSOS,false);
        preferences.commit();
        preferences.apply();

    }

    public static boolean getFlashCameraStatus(Context context)
    {

        SharedPreferences preferences = context.getSharedPreferences(StringConstants.preferenceName, MODE_PRIVATE);
        return preferences.getBoolean(StringConstants.preferenceAttrFlash,false);

    }

    public static boolean getSosCameraStatus(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE);
        return preferences.getBoolean(StringConstants.preferenceAttrSOS,false);
    }

    public static void setFlashCameraStatus(boolean isFlashEnabled,Context context)
    {
        SharedPreferences.Editor preferences=context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE).edit();
        preferences.putBoolean(StringConstants.preferenceAttrFlash,isFlashEnabled);
        preferences.commit();
        preferences.apply();
    }


    public static void setSosCameraStatus(boolean isSosEnabled,Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE).edit();
        editor.putBoolean(StringConstants.preferenceAttrSOS,isSosEnabled);
        editor.apply();
        editor.commit();
    }



    public static void enableCamera(Context context, CameraManager cameraManager){

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void disableCamera(Context context,CameraManager cameraManager){

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void setCameraFrequency(Context context,int frequency)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE).edit();
        editor.putInt(StringConstants.preferenceferquency,frequency);
        editor.apply();
        editor.commit();

    }


    public static int getCameraFrequency(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE);
        return preferences.getInt(StringConstants.preferenceferquency,0);
    }



    public static void removeSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(StringConstants.preferenceName,MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }





}
