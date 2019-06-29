package project.july2019.androidflashlight.Utils;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.MainActivity;

public class CommonUtils {

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

    public static void exitAlertPopUp(final Context context, String title, String message)
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
        alertDialog.getWindow().setWindowAnimations(R.style.AppTheme);
        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.aler_dialog_background));
        alertDialog.show();

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).finish();
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


}
