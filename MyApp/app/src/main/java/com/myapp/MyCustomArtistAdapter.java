package com.myapp;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRONOS on 7/8/17.
 */

public class MyCustomArtistAdapter extends ArrayAdapter {

    ImageView albumArt;
    ArrayList<String> al;
    TextView songArtistName;
    CheckBox cb;
    RelativeLayout rl;

    public MyCustomArtistAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        al = (ArrayList) items;
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return al.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_artist_item, null);
        }

        albumArt = (ImageView) v.findViewById(R.id.customArtistAlbumArt);
        songArtistName = (TextView) v.findViewById(R.id.customArtistName);
        cb = (CheckBox) v.findViewById(R.id.customArtistItemCheckBox);
        rl = (RelativeLayout) v.findViewById(R.id.cutomArtistItemRelativeLayout);

        if(MainActivity.currentFragment != null) {
            switch(MainActivity.currentFragment) {
                case "Artists" :
                    if(Artists.selectedItems.contains(al.get(position))) {
                        rl.setBackgroundColor(Color.GRAY);
                        songArtistName.setTextColor(Color.WHITE);
                    } else {
                        rl.setBackgroundColor(Color.WHITE);
                        songArtistName.setTextColor(Color.BLACK);
                    }
                    break;
            }
        }

        if(songArtistName != null) {
            songArtistName.setText(al.get(position));
        }
        if(albumArt != null) {
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songArtists.indexOf(al.get(position)))));
            Glide.with(getContext()).load(uri).crossFade().placeholder(R.drawable.noalbumart).into(albumArt);
        }



        return v;
    }
}
