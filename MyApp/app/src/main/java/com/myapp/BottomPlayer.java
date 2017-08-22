package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KRONOS on 7/2/17.
 */

public class BottomPlayer extends Fragment {
    public static ImageButton ppb;
    public static ImageButton nb;
    public static ImageButton pb;
    public static TextView sname;
    public static TextView artistName;
    public static ImageView albumArt;
    public View tempV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.bottom_player,container,false);
        tempV = rootView;

        ppb = (ImageButton) rootView.findViewById(R.id.playPauseButton);
        nb = (ImageButton) rootView.findViewById(R.id.nextButton);
        pb = (ImageButton) rootView.findViewById(R.id.previousButton);
        sname = (TextView) rootView.findViewById(R.id.sname);
        artistName = (TextView) rootView.findViewById(R.id.artistname);
        albumArt = (ImageView) rootView.findViewById(R.id.bottomPlayerAlbumArt);

        rootView.findViewById(R.id.bottomPlayerRelativeLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mp != null) {
                    Intent i = new Intent(MainActivity.tempC,ActiveSong.class);
                    startActivity(i);
                }
            }
        });

        return rootView;
    }

}

