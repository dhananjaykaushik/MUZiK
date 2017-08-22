package com.myapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRONOS on 7/9/17.
 */

public class MyCustomSongNameOnlyAdapter extends ArrayAdapter {

    ArrayList<String> al;
    TextView songName,ex;
    CheckBox cb;
    RelativeLayout rl;

    public MyCustomSongNameOnlyAdapter(Context context, int resource, List<String> items) {
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
            v = vi.inflate(R.layout.custom_album_songs_list_item, null);
        }


        songName = (TextView) v.findViewById(R.id.textView6);
        cb = (CheckBox) v.findViewById(R.id.checkBoxNameOnly);
        rl = (RelativeLayout) v.findViewById(R.id.rls);
        ex = (TextView) v.findViewById(R.id.textView5); // -

        if(MainActivity.currentFragment != null) {
            switch(MainActivity.currentFragment) {
                case "AlbumSongs" :
                    if(AlbumSongs.selectedItems.contains(al.get(position))) {
                        rl.setBackgroundColor(Color.GRAY);
                        songName.setTextColor(Color.WHITE);
                        ex.setTextColor(Color.WHITE);
                    } else {
                        rl.setBackgroundColor(Color.WHITE);
                        songName.setTextColor(Color.BLACK);
                        ex.setTextColor(Color.BLACK);
                    }
                    break;
                case "Playlists" :
                    if(Playlists.selectedItems.contains(al.get(position))) {
                        rl.setBackgroundColor(Color.GRAY);
                        songName.setTextColor(Color.WHITE);
                        ex.setTextColor(Color.WHITE);
                    } else {
                        rl.setBackgroundColor(Color.WHITE);
                        songName.setTextColor(Color.BLACK);
                        ex.setTextColor(Color.BLACK);
                    }
                    break;
            }
        }

        if(songName != null) {
            songName.setText(al.get(position));
        }

        return v;
    }
}
