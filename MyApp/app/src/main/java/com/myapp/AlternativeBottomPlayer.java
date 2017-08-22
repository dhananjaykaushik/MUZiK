package com.myapp;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KRONOS on 7/11/17.
 */

public class AlternativeBottomPlayer extends Fragment {

    public static ImageButton altPlay,altDel,altAdd,altBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alternative_bottom_player,container,false);
        altPlay = (ImageButton) rootView.findViewById(R.id.altPlay);
        altAdd = (ImageButton) rootView.findViewById(R.id.altAdd);
        altDel = (ImageButton) rootView.findViewById(R.id.altDel);
        altBack = (ImageButton) rootView.findViewById(R.id.altBack);


        altPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (MainActivity.currentFragment) {
                    case "Tracks" : MainActivity.currentlyPlaying = Tracks.selectedItems;
                        break;
                    case "Artists" :
                        ArrayList<String> curSongs2 = new ArrayList<String>();

                        for(String aln : Artists.selectedItems) {
                            ArrayList<String> tempal = Artists.getAllArtistSongs(aln);
                            curSongs2.addAll(tempal);
                        }

                        MainActivity.currentlyPlaying = curSongs2;
                        break;
                    case "ArtistSongs" : MainActivity.currentlyPlaying = ArtistSongs.selectedItems;
                        break;

                    case "Albums" :
                        ArrayList<String> curSongs1 = new ArrayList<String>();

                        for(String aln : Albums.selectedItems) {
                            ArrayList<String> tempal = Albums.getAllAlbumSongs(aln);
                            curSongs1.addAll(tempal);
                        }

                        MainActivity.currentlyPlaying = curSongs1;
                        break;

                    case "AlbumSongs" : MainActivity.currentlyPlaying = AlbumSongs.selectedItems;
                        break;
                    case "Playlists" :
                        ArrayList<String> curSongs = new ArrayList<String>();

                        for(String pln : Playlists.selectedItems) {
                            ArrayList<String> tempal = Playlists.getAllPlaylistSongs(pln);
                            curSongs.addAll(tempal);
                        }
                        MainActivity.currentlyPlaying = curSongs;

                        break;
                    case "PlaylistSongs" : MainActivity.currentlyPlaying = PlaylistSongs.selectedItems;
                        break;
                }

                MediaPlayer prevTemp = MainActivity.mp;
                MainActivity.mp = MediaPlayer.create(getContext(), Uri.parse(MainActivity.currentlyPlaying.get(0)));
                MainActivity.currentSong = MainActivity.currentlyPlaying.get(0);
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
                BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(getContext(),android.R.drawable.ic_media_pause));


                View frag = getActivity().findViewById(R.id.fragment);
                frag.setVisibility(View.VISIBLE);
                View frag1 = getActivity().findViewById(R.id.fragment1);
                frag1.setVisibility(View.INVISIBLE);

                MainActivity.fakeBack();

            }
        });


        altDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (MainActivity.currentFragment) {
                    case "Tracks" :

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);

                        alertDialogBuilder.setTitle("Deleting Songs");
                        alertDialogBuilder.setMessage("Are you sure to delete song(s) permanently from your device");

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(String s : Tracks.selectedItems) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            MediaStore.Audio.Media._ID + " = '"+
                                            MainActivity.songID.get(MainActivity.songData.indexOf(s))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Song(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();

                            }
                        });

                        AlertDialog adb = alertDialogBuilder.create();
                        adb.show();

                        break;


                    case "Artists" :

                        alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);
                        alertDialogBuilder.setTitle("Deleting Songs");
                        alertDialogBuilder.setMessage("Are you sure to delete all artist songs permanently from your device");


                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<String> curSongs2 = new ArrayList<String>();

                                for(String aln : Artists.selectedItems) {
                                    ArrayList<String> tempal = Artists.getAllArtistSongs(aln);
                                    curSongs2.addAll(tempal);
                                }

                                for(String s : curSongs2) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID + " = '"+
                                            MainActivity.songID.get(MainActivity.songData.indexOf(s))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Song(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();
                            }
                        });

                        adb = alertDialogBuilder.create();
                        adb.show();
                        break;

                    case "ArtistSongs" :

                        alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);

                        alertDialogBuilder.setTitle("Deleting Songs");
                        alertDialogBuilder.setMessage("Are you sure to delete song(s) permanently from your device");

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(String s : ArtistSongs.selectedItems) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID + " = '"+
                                            MainActivity.songID.get(MainActivity.songData.indexOf(s))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Song(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();
                            }
                        });

                        adb = alertDialogBuilder.create();
                        adb.show();

                        break;

                    case "Albums" :
                        alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);
                        alertDialogBuilder.setTitle("Deleting Songs");
                        alertDialogBuilder.setMessage("Are you sure to delete all album songs permanently from your device");


                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<String> curSongs2 = new ArrayList<String>();

                                for(String aln : Albums.selectedItems) {
                                    ArrayList<String> tempal = Albums.getAllAlbumSongs(aln);
                                    curSongs2.addAll(tempal);
                                }

                                for(String s : curSongs2) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID + " = '"+
                                            MainActivity.songID.get(MainActivity.songData.indexOf(s))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Song(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();
                            }
                        });

                        adb = alertDialogBuilder.create();
                        adb.show();
                        break;

                    case "AlbumSongs" :
                        alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);

                        alertDialogBuilder.setTitle("Deleting Songs");
                        alertDialogBuilder.setMessage("Are you sure to delete song(s) permanently from your device");

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(String s : AlbumSongs.selectedItems) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID + " = '"+
                                            MainActivity.songID.get(MainActivity.songData.indexOf(s))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Song(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();
                            }
                        });

                        adb = alertDialogBuilder.create();
                        adb.show();

                        break;
                    case "Playlists" :

                        alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);
                        alertDialogBuilder.setTitle("Deleting Playlists");
                        alertDialogBuilder.setMessage("Are you sure to delete playlist(s)");

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(String pln : Playlists.selectedItems) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                                            MediaStore.Audio.Playlists._ID + " = '"+Playlists.playlistNamesId.get(Playlists.playlistNames.indexOf(pln))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Playlist(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();
                            }
                        });

                        adb = alertDialogBuilder.create();
                        adb.show();


                        break;
                    case "PlaylistSongs" :

                        alertDialogBuilder = new AlertDialog.Builder(MainActivity.mat);

                        alertDialogBuilder.setTitle("Removing Songs");
                        alertDialogBuilder.setMessage("Are you sure to remove song(s) from Playlist : " + Playlists.currentPlaylistName);

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Long curId = Long.parseLong(Playlists.playlistNamesId.get(Playlists.playlistNames.indexOf(Playlists.currentPlaylistName)));

                                for(String s : PlaylistSongs.selectedItems) {
                                    MainActivity.tempC.getContentResolver().delete(MediaStore.Audio.Playlists.Members.getContentUri("external",curId),MediaStore.Audio.Playlists.Members._ID + " = '"+
                                            Playlists.playlistSongsId.get(Playlists.playlistSongsData.indexOf(s))+"'",null);
                                }

                                MainActivity.appReset();
                                Toast.makeText(getContext(), "Song(s) deleted !!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fakeBack();
                            }
                        });

                        adb = alertDialogBuilder.create();
                        adb.show();

                        break;
                }

            }
        });


        altAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.mat,AddTrack.class);
                startActivity(i);
            }
        });



        altBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });



        return rootView;
    }
}
