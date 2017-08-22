package com.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class About extends AppCompatActivity {

    ImageView iv;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        iv = (ImageView) findViewById(R.id.imageView);
        rl = (RelativeLayout) findViewById(R.id.rlabout);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.animate().rotation(iv.getRotation()+45).setDuration(700);
            }
        });
    }
}
