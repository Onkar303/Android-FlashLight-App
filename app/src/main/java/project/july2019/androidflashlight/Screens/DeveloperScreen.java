package project.july2019.androidflashlight.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.androidflashlight.R;

import project.july2019.androidflashlight.Utils.CommonUtils;

public class DeveloperScreen extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_screen);

        CommonUtils.setFullScreen(getWindow());
        CommonUtils.setHidingNavigationBar(getWindow());
        init();
    }

    public void init(){
        button=findViewById(R.id.send_email);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send_email:


                Intent i =new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL,new String[]{"onkarkalpavriksha@gmail.com"});
                startActivity(Intent.createChooser(i,"Send email"));
                break;
        }
    }
}
