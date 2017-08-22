package com.myapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Playlists extends Fragment {


    public static ListView lv;
    public static ArrayList<String> playlistNames = new ArrayList<>();
    public static ArrayList<String> playlistNamesId = new ArrayList<>();
    public static ArrayList<String> playlistSongs;
    public static ArrayList<String> playlistSongsData;
    public static ArrayList<String> playlistSongsId;
    public static String currentPlaylistName;
    boolean flag = false;
    public static boolean fragmentFlag = false;
    public static MyCustomSongNameOnlyAdapter ad1;
    static Context tc;
    public static FragmentActivity tcp;

    public static Cursor playListCursor;
    public static ArrayList<String> selectedItems = new ArrayList<String>();
    int count = 0;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlists,container,false);
        tc = getContext();
        tcp = getActivity();
        lv = (ListView) rootView.findViewById(R.id.playlist_names);
        playlistNames = new ArrayList();
        playlistNamesId = new ArrayList<>();

        String[] proj = {"*"};
        Uri tempPlaylistURI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        playListCursor = getActivity().managedQuery(tempPlaylistURI, proj, null,null,null);

        if(playListCursor == null){
            Toast.makeText(getContext(), "NO PLAYLISTS", Toast.LENGTH_SHORT).show();
        }

        String playListName;
        String playListNameId;


        for(int i = 0; i < playListCursor.getCount() ; i++)
        {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("NAME"));
            playListNameId = playListCursor.getString(playListCursor.getColumnIndex("_ID"));
            playlistNames.add(playListName);
            playlistNamesId.add(playListNameId);
        }

        ad1 = new MyCustomSongNameOnlyAdapter(getContext(),R.layout.custom_album_songs_list_item,playlistNames);

        lv.setAdapter(ad1);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                flag = true;

                playlistSongs = new ArrayList<>();
                playlistSongsData = new ArrayList<>();
                currentPlaylistName = playlistNames.get(position);
                playlistSongsId = new ArrayList<String>();

                String[] proj = {   MediaStore.Audio.Playlists.Members.AUDIO_ID,
                        MediaStore.Audio.Playlists.Members.ARTIST,
                        MediaStore.Audio.Playlists.Members.TITLE,
                        MediaStore.Audio.Playlists.Members._ID,
                        MediaStore.Audio.Playlists.Members.DATA
                };



                Cursor c1 = getContext().getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(playlistNamesId.get(position))),
                        proj,
                        null,
                        null,
                        null);

                int j = c1.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA);
                int k = c1.getColumnIndex(MediaStore.Audio.Playlists.Members._ID);

                while(c1.moveToNext()) {
                    playlistSongs.add(c1.getString(c1.getColumnIndex("TITLE")));
                    playlistSongsData.add(c1.getString(j));
                    playlistSongsId.add(c1.getString(k));
                }

                if(! playlistSongsData.isEmpty()) {
                    lv.setVisibility(View.INVISIBLE);
                    if(PlaylistSongs.lv != null)
                        PlaylistSongs.lv.setVisibility(View.VISIBLE);
                    Fragment fragment = new PlaylistSongs();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.playlistsRelativeLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "NO SONGS IN THIS PLAYLIST", Toast.LENGTH_SHORT).show();
                }

            }
        });


        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if(checked) {
                    ++count;
                    selectedItems.add(playlistNames.get(position));
                } else {
                    --count;
                    selectedItems.remove(playlistNames.get(position));
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
                MainActivity.currentFragment="Playlists";
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



        return rootView;
    }

    public static ArrayList<String> getAllPlaylistSongs(String plName) {
        ArrayList<String> plSongsData = new ArrayList<String>();
        String plId = Playlists.playlistNamesId.get(Playlists.playlistNames.indexOf(plName));

        String[] proj = {   MediaStore.Audio.Playlists.Members.AUDIO_ID,
                MediaStore.Audio.Playlists.Members.ARTIST,
                MediaStore.Audio.Playlists.Members.TITLE,
                MediaStore.Audio.Playlists.Members._ID,
                MediaStore.Audio.Playlists.Members.DATA
        };



        Cursor c1 = tc.getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(plId)),
                proj,
                null,
                null,
                null);


        int j = c1.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA);

        while(c1.moveToNext()) {
            plSongsData.add(c1.getString(j));
        }

        return plSongsData;
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

    public static void updatePlaylist() {
        playlistNames = new ArrayList();
        playlistNamesId = new ArrayList<>();

        String[] proj = {"*"};
        Uri tempPlaylistURI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        playListCursor = tcp.managedQuery(tempPlaylistURI, proj, null,null,null);

        if(playListCursor == null){
            Toast.makeText(tc, "NO PLAYLISTS", Toast.LENGTH_SHORT).show();
        }

        String playListName;
        String playListNameId;


        for(int i = 0; i < playListCursor.getCount() ; i++)
        {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("NAME"));
            playListNameId = playListCursor.getString(playListCursor.getColumnIndex("_ID"));
            playlistNames.add(playListName);
            playlistNamesId.add(playListNameId);
        }
    }

}
