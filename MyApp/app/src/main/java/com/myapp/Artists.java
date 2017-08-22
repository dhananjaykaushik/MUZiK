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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Artists extends Fragment {

    public static ListView lv;

    boolean flag = false;

    public static ArrayList<String> artistSongs;
    public static ArrayList<String> artistSongsData;

    public static ArrayList<String> arArtistNames;
    public static String artistName;
    public static boolean fragmentFlag = false;
    public static MyCustomArtistAdapter ad1;
    public static ArrayList<String> selectedItems = new ArrayList<String>();
    int count = 0;
    List<String> tempList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.artists,container,false);

        lv = (ListView) rootView.findViewById(R.id.artist_names);


        if(MainActivity.songData != null) {

            arArtistNames = new ArrayList<>(new HashSet<>(MainActivity.songArtists));
            tempList = arArtistNames;
            Collections.sort(tempList, String.CASE_INSENSITIVE_ORDER);

            ad1 = new MyCustomArtistAdapter(getContext(),R.layout.custom_artist_item,tempList);
            lv.setAdapter(ad1);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                flag = true;
                artistName = arArtistNames.get(position);
                artistSongs = new ArrayList();
                artistSongsData = new ArrayList();



                for(int i = 0; i < MainActivity.songData.size() ; ++i) {
                    if(MainActivity.songArtists.get(i).equals(artistName)) {
                        artistSongs.add(MainActivity.songNames.get(i));
                        artistSongsData.add(MainActivity.songData.get(i));
                    }
                }

                if(! artistSongs.isEmpty()) {
                    lv.setVisibility(View.INVISIBLE);
                    if(ArtistSongs.lv != null)
                        ArtistSongs.lv.setVisibility(View.VISIBLE);
                    Fragment fragment = new ArtistSongs();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.artistsRelativeLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "NO SONGS BY THIS ARTIST", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
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
                MainActivity.currentFragment="Artists";
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

    public static ArrayList<String> getAllArtistSongs(String aName) {
        ArrayList<String> als = new ArrayList<>();
        for(int i = 0; i < MainActivity.songData.size() ; ++i) {
            if(MainActivity.songArtists.get(i).equals(aName)) {
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
