package com.myapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Switch s1,s2,s3;
    String s1val, s2val, s3val;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        s1 = (Switch) findViewById(R.id.switch1);
        s2 = (Switch) findViewById(R.id.switch2);
        s3 = (Switch) findViewById(R.id.switch3);

        SharedPreferences sharedPreferences = getSharedPreferences("muziksetting",MODE_PRIVATE);
        s1val = sharedPreferences.getString("proxSensor", "off");
        s2val = sharedPreferences.getString("shakeSensor", "off");
        s3val = sharedPreferences.getString("splashScreen", "off");

        if(s1val.equals("on")) {
            s1.setChecked(true);
        } else {
            s1.setChecked(false);
        }

        if(s2val.equals("on")) {
            s2.setChecked(true);
        } else {
            s2.setChecked(false);
        }

        if(s3val.equals("on")) {
            s3.setChecked(true);
        } else {
            s3.setChecked(false);
        }


        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                s1val = isChecked ? "on" : "off";
                if(s1val.equals("on")) {
                    Toast.makeText(SettingsActivity.this, "PROXIMITY SENSOR ENABLED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "PROXIMITY SENSOR DISABLED", Toast.LENGTH_SHORT).show();
                }
            }
        });

        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                s2val = isChecked ? "on" : "off";
                if(s2val.equals("on")) {
                    Toast.makeText(SettingsActivity.this, "SHAKE SENSOR ENABLED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "SHAKE SENSOR DISABLED", Toast.LENGTH_SHORT).show();
                }
            }
        });

        s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                s3val = isChecked ? "on" : "off";
                if(s3val.equals("on")) {
                    Toast.makeText(SettingsActivity.this, "SPLASH SCREEN ENABLED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "SPLASH SCREEN DISABLED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    public void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("muziksetting",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("proxSensor", s1val);
        editor.putString("shakeSensor", s2val);
        editor.putString("splashScreen", s3val);
        editor.commit();
    }
}
