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
 * Created by KRONOS on 7/9/17.
 */

public class MyCustomAlbumAdapter extends ArrayAdapter {
    ImageView albumArt;
    ArrayList<String> al;
    TextView albumName,songArtistName;
    CheckBox cb;
    RelativeLayout rl;


    public MyCustomAlbumAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        al = (ArrayList)items;
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
            v = vi.inflate(R.layout.custom_album_item, null);
        }

        albumArt = (ImageView) v.findViewById(R.id.customAlbumItemArt);
        albumName = (TextView) v.findViewById(R.id.customAlbumItemName);
        songArtistName = (TextView) v.findViewById(R.id.customAlbumItemArtistName);
        cb = (CheckBox) v.findViewById(R.id.customAlbumItemCheckBox);
        rl = (RelativeLayout) v.findViewById(R.id.rlx);

        if(MainActivity.currentFragment != null) {
            switch(MainActivity.currentFragment) {
                case "Albums" :
                    if(Albums.selectedItems.contains(al.get(position))) {
                        rl.setBackgroundColor(Color.GRAY);
                        albumName.setTextColor(Color.WHITE);
                        songArtistName.setTextColor(Color.WHITE);
                    } else {
                        rl.setBackgroundColor(Color.WHITE);
                        albumName.setTextColor(Color.BLACK);
                        songArtistName.setTextColor(Color.BLACK);
                    }
                    break;
            }
        }

        if(albumName != null) {
            albumName.setText(al.get(position));
        }
        if(songArtistName != null) {
            songArtistName.setText(MainActivity.songArtists.get(MainActivity.songAlbums.indexOf(al.get(position))));
        }
        if(albumArt != null) {
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songAlbums.indexOf(al.get(position)))));
            Glide.with(getContext()).load(uri).crossFade().placeholder(R.drawable.noalbumart).into(albumArt);
        }



        return v;
    }
}
