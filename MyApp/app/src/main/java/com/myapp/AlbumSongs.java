package com.myapp;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KRONOS on 7/4/17.
 */

public class AlbumSongs extends Fragment{

    public static ListView lv;
    public static RelativeLayout rl;
    TextView tt;
    ImageView aa;
    public static boolean fragmentFlag = false;
    public static MyCustomSongNameOnlyAdapter ad1;
    public static ArrayList<String> selectedItems = new ArrayList<String>();
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custom_album_songs_layout,container,false);

        lv = (ListView) rootView.findViewById(R.id.customAlbumSongsListView);
        rl = (RelativeLayout) rootView.findViewById(R.id.customAlbumSongsRelativeLayout);
        aa = (ImageView) rootView.findViewById(R.id.customAlbumSongsAlbumArt);
        tt = (TextView) rootView.findViewById(R.id.customAlbumSongsAlbumName);

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songAlbums.indexOf(Albums.albumName))));
        Glide.with(getContext()).load(uri).crossFade().error(R.drawable.noalbumart).into(aa);

        tt.setText(Albums.albumName);

        ad1 = new MyCustomSongNameOnlyAdapter(getContext(),R.layout.custom_album_songs_list_item,Albums.albumSongs);

        lv.setAdapter(ad1);

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                MainActivity.currentlyPlaying = Albums.albumSongsData;
                MediaPlayer prevTemp = MainActivity.mp;
                MainActivity.mp = MediaPlayer.create(getContext(), Uri.parse(MainActivity.currentlyPlaying.get(position)));
                MainActivity.currentSong = MainActivity.currentlyPlaying.get(position);
                MediaPlayer temp = MainActivity.mp;
                BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                Glide.with(getContext()).load(uri).crossFade().error(R.drawable.noalbumart).into(BottomPlayer.albumArt);


                if(prevTemp != null && prevTemp.isPlaying()) {
                    prevTemp.stop();
                }
                temp.start();
                MainActivity.mp.setOnCompletionListener(MainActivity.mat);
                BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(getContext(),android.R.drawable.ic_media_pause));

            }
        });


        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if(checked) {
                    ++count;
                    selectedItems.add(Albums.albumSongsData.get(position));
                } else {
                    --count;
                    selectedItems.remove(Albums.albumSongsData.get(position));
                }

                mode.setTitle(""+count);
                ad1.notifyDataSetChanged();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                View frag = getActivity().findViewById(R.id.fragment);
                frag.setVisibility(View.INVISIBLE);
                View frag1 = getActivity().findViewById(R.id.fragment1);
                frag1.setVisibility(View.VISIBLE);

                selectedItems = new ArrayList<String>();
                count = 0;
                MainActivity.currentFragment="AlbumSongs";
                MenuInflater inf = mode.getMenuInflater();
                inf.inflate(R.menu.my_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectedItems = new ArrayList<String>();
                count = 0;
                View frag = getActivity().findViewById(R.id.fragment);
                frag.setVisibility(View.VISIBLE);
                View frag1 = getActivity().findViewById(R.id.fragment1);
                frag1.setVisibility(View.INVISIBLE);

            }
        });

        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    rl.setVisibility(View.INVISIBLE);
                    if(Albums.gv != null)
                        Albums.gv.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        } );

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.soulOfProject();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.soulOfProject();
    }

}
