package com.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageButton ib;
    TextView tv9,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("muziksetting",MODE_PRIVATE);
        String splVal = sharedPreferences.getString("splashScreen", "off");

        if(splVal.equals("on")) {
            setContentView(R.layout.activity_splash_screen);
            getSupportActionBar().hide();
            ib = (ImageButton) findViewById(R.id.imageButton);
            tv9 = (TextView) findViewById(R.id.textView9);
            tv9.animate().setDuration(3000).alpha(0f);
            tv2 = (TextView) findViewById(R.id.textView2);
            tv2.animate().setDuration(3000).alpha(0f);

            getWindow().getAttributes().windowAnimations = R.style.Fade;

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv9.animate().setDuration(600).alpha(0f);
                    tv2.animate().setDuration(600).alpha(0f);
                    ib.animate().alpha(0.8f).scaleX(1.32f).scaleY(1.32f).setDuration(900).rotation(360f).translationY(-100f).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreen.this,MainActivity.class);
                            startActivity(i);
                            SplashScreen.this.finish();
                        }
                    });
                }
            });
        } else {
            Intent i = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(i);
            SplashScreen.this.finish();
        }


    }
}
