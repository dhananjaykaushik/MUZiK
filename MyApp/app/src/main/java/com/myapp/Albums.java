package com.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class Albums extends Fragment{

    public static GridView gv;

    public static ArrayList<String> albumSongs;
    public static ArrayList<String> albumNames = new ArrayList();
    public static ArrayList<String> albumSongsData = new ArrayList();
    public static String albumName;
    public static boolean flag = false;
    public static boolean fragmentFlag = false;
    public static MyCustomAlbumAdapter ad1;
    public static ArrayList<String> selectedItems = new ArrayList<String>();
    List<String> tempList;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.albums,container,false);
        gv = (GridView) rootView.findViewById(R.id.album_names);
        if(MainActivity.songData != null) {

            albumNames = new ArrayList<>(new HashSet<>(MainActivity.songAlbums));
            tempList = albumNames;
            Collections.sort(tempList,String.CASE_INSENSITIVE_ORDER);

            ad1 = new MyCustomAlbumAdapter(getContext(),R.layout.custom_album_item,tempList);

            gv.setAdapter(ad1);
            gv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        }

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                flag = true;
                albumName = albumNames.get(position);
                albumSongs = new ArrayList();
                albumSongsData = new ArrayList<String>();

                for(int i = 0; i < MainActivity.songData.size() ; ++i) {
                    if(MainActivity.songAlbums.get(i).equals(albumName)) {
                        albumSongs.add(MainActivity.songNames.get(i));
                        albumSongsData.add(MainActivity.songData.get(i));
                    }
                }

                if(! albumSongs.isEmpty()) {
                    gv.setVisibility(View.INVISIBLE);
                    if(AlbumSongs.lv != null)
                        AlbumSongs.rl.setVisibility(View.VISIBLE);
                    Fragment fragment = new AlbumSongs();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.albumsRelativeLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "NO SONGS IN THIS ALBUM", Toast.LENGTH_SHORT).show();
                }

            }
        });

        gv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if(checked) {
                    ++count;
                    selectedItems.add(tempList.get(position));
                } else {
                    --count;
                    selectedItems.remove(tempList.get(position));
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
                MainActivity.currentFragment="Albums";
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

        gv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public static ArrayList<String> getAllAlbumSongs(String aName) {
        ArrayList<String> als = new ArrayList<>();
        for(int i = 0; i < MainActivity.songData.size() ; ++i) {
            if(MainActivity.songAlbums.get(i).equals(aName)) {
                als.add(MainActivity.songData.get(i));
            }
        }
        return als;
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
