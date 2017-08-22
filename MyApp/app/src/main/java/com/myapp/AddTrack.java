package com.myapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddTrack extends AppCompatActivity {

    public ArrayList<String> plNames;
    public Context c;
    public ListView lv;
    ArrayList<String> songsToAdd = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        lv = (ListView) findViewById(R.id.addTrackListView);
        c = this;
        addTrack();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addTrack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addTrack();
    }

    public void addTrack() {
        plNames = new ArrayList<String>();
        plNames.add("Create New Playlist");

        for(String s : Playlists.playlistNames)
            plNames.add(s);

        ArrayAdapter ad = new ArrayAdapter(c,android.R.layout.simple_list_item_1,plNames);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songsToAdd = new ArrayList<String>();
                String plName = plNames.get(position);

                    switch (MainActivity.currentFragment) {
                        case "Tracks" : songsToAdd = Tracks.selectedItems;
                            break;
                        case "Artists" :

                            songsToAdd = new ArrayList<String>();

                            for(String aln : Artists.selectedItems) {
                                ArrayList<String> tempal = Artists.getAllArtistSongs(aln);
                                songsToAdd.addAll(tempal);
                            }

                            break;
                        case "ArtistSongs" : songsToAdd = ArtistSongs.selectedItems;
                            break;
                        case "Albums" :
                            songsToAdd = new ArrayList<String>();

                            for(String aln : Albums.selectedItems) {
                                ArrayList<String> tempal = Albums.getAllAlbumSongs(aln);
                                songsToAdd.addAll(tempal);
                            }
                            break;
                        case "AlbumSongs" : songsToAdd = AlbumSongs.selectedItems;
                            break;
                        case "Playlists" :
                            songsToAdd = new ArrayList<String>();

                            for(String aln : Playlists.selectedItems) {
                                ArrayList<String> tempal = Playlists.getAllPlaylistSongs(aln);
                                songsToAdd.addAll(tempal);
                            }
                            break;
                        case "PlaylistSongs" : songsToAdd = PlaylistSongs.selectedItems;
                            break;
                    }

                    if(! plNames.get(position).equals("Create New Playlist")) {

                    String plId = Playlists.playlistNamesId.get(Playlists.playlistNames.indexOf(plName));

                    if(plId != null) {
                        ContentResolver resolver = c.getContentResolver();
                        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(plId));

                        String[] projection = new String[] { MediaStore.Audio.Playlists.Members.PLAY_ORDER };
                        Cursor cursor = resolver.query(uri, projection, null, null, null);
                        int base = 0;
                        if (cursor.moveToLast())
                            base = cursor.getInt(0) + 1;
                        cursor.close();


                        ContentValues values;
                        for (int i = 0; i < songsToAdd.size(); i++) {
                            values = new ContentValues();
                            values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,Long.parseLong(MainActivity.songID.get(MainActivity.songData.indexOf(songsToAdd.get(i)))));
                            values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,base + i);
                            resolver.insert(uri,values);
                        }



                        MainActivity.appReset();
                        Toast.makeText(c, "SONG ADDED TO PLAYLIST", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(c, "ERROR TRYING TO ADD SONGS TO PLAYLIST", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
                    View mView = getLayoutInflater().inflate(R.layout.create_playlist_dialog,null);
                    final EditText newPlName = (EditText) mView.findViewById(R.id.editText);
                    newPlName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newPlName.setText("");
                        }
                    });
                    Button createButton = (Button) mView.findViewById(R.id.button);
                    Button cancelButton = (Button) mView.findViewById(R.id.button2);

                    createButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //CREATE PLAYLIST
                            String n = newPlName.getText().toString();
                            if(n != null) {
                                if(Playlists.playlistNames.contains(n)) {
                                    Toast.makeText(c, "Playlist Already Exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    ContentResolver resolver = c.getContentResolver();
                                    Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
                                    ContentValues v1 = new ContentValues();
                                    v1.put(MediaStore.Audio.Playlists.NAME, n);
                                    v1.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
                                    resolver.insert(playlists, v1);
                                    MainActivity.soulOfProject();
                                    Playlists.updatePlaylist();

                                    String plId = Playlists.playlistNamesId.get(Playlists.playlistNames.indexOf(n));

                                    if (plId != null) {
                                        resolver = c.getContentResolver();

                                        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(plId));

                                        String[] projection = new String[]{MediaStore.Audio.Playlists.Members.PLAY_ORDER};
                                        Cursor cursor = resolver.query(uri, projection, null, null, null);
                                        int base = 0;
                                        if (cursor.moveToLast())
                                            base = cursor.getInt(0) + 1;
                                        cursor.close();


                                        ContentValues values;
                                        for (int i = 0; i < songsToAdd.size(); i++) {
                                            values = new ContentValues();
                                            values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, Long.parseLong(MainActivity.songID.get(MainActivity.songData.indexOf(songsToAdd.get(i)))));
                                            values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, base + i);
                                            resolver.insert(uri, values);
                                        }


                                        MainActivity.appReset();
                                        Toast.makeText(c, "SONG ADDED TO PLAYLIST", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            } else {
                                Toast.makeText(c, "Invalid Name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.appReset();
                            Toast.makeText(c, "Playlist creation cancelled!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog ad = mBuilder.create();
                    ad.show();

                }


            }
        });

    }

}
