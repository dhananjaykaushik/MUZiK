package com.myapp;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class ActiveSong extends AppCompatActivity {

    public static ImageView albumArt;
    public static TextView songName,artistName,cd,td;
    public static SeekBar sb;
    public static ImageButton pp,ne,pre,shuffle,repeat;
    public static Context c;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_song);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        c = this;


        albumArt = (ImageView) findViewById(R.id.activeSongAlbumArt);
        songName = (TextView) findViewById(R.id.activeSongSongName);
        artistName = (TextView) findViewById(R.id.activeSongArtistName);
        cd = (TextView) findViewById(R.id.curSeek);
        td = (TextView) findViewById(R.id.totDur);
        sb = (SeekBar) findViewById(R.id.activeSongSeekBar);
        pp = (ImageButton) findViewById(R.id.aplpaButton);
        ne = (ImageButton) findViewById(R.id.anextButton);
        pre = (ImageButton) findViewById(R.id.aprevButton);
        shuffle = (ImageButton) findViewById(R.id.shuffbtn);
        repeat = (ImageButton) findViewById(R.id.repbtn);


        setStuffUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        setStuffUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        setStuffUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStuffUp() {

        if(MainActivity.shuffle) {
            shuffle.setBackground(ContextCompat.getDrawable(c,R.drawable.shuffle));
        } else {
            shuffle.setBackground(ContextCompat.getDrawable(c,R.drawable.noshuffle));
        }

        if(MainActivity.repeat == MainActivity.NO_LOOP) {
            repeat.setBackground(ContextCompat.getDrawable(c,R.drawable.repnone));
        } else if(MainActivity.repeat == MainActivity.REPEAT_ALL_LOOP) {
            repeat.setBackground(ContextCompat.getDrawable(c,R.drawable.repall));
        } else {
            repeat.setBackground(ContextCompat.getDrawable(c,R.drawable.repone));
        }

        if(MainActivity.mp.isPlaying()) {
            pp.setBackground(ContextCompat.getDrawable(c,android.R.drawable.ic_media_pause));
        } else {
            pp.setBackground(ContextCompat.getDrawable(c,android.R.drawable.ic_media_play));
        }

        songName.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
        artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
        Glide.with(c.getApplicationContext()).load(uri).crossFade().error(R.drawable.noalbumart).into(albumArt);
        sb.setMax(MainActivity.mp.getDuration());
        sb.setProgress(MainActivity.mp.getCurrentPosition());

        cd.setText(MainActivity.convertToDuration(MainActivity.mp.getCurrentPosition()));
        td.setText(MainActivity.convertToDuration(MainActivity.mp.getDuration()));

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    MainActivity.mp.seekTo(progress);
                    cd.setText(MainActivity.convertToDuration(MainActivity.mp.getCurrentPosition()));
                } else {
                    cd.setText(MainActivity.convertToDuration(MainActivity.mp.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread() {
            @Override
            public void run() {
                while(MainActivity.mp.getDuration() != MainActivity.mp.getCurrentPosition()) {
                    sb.setProgress(MainActivity.mp.getCurrentPosition());
                }
            }
        }.start();

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mp.isPlaying()) {
                    pp.setBackground(ContextCompat.getDrawable(c,android.R.drawable.ic_media_play));
                    MainActivity.mp.pause();
                } else {
                    pp.setBackground(ContextCompat.getDrawable(c,android.R.drawable.ic_media_pause));
                    MainActivity.mp.start();
                }
            }
        });

        ne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.songCompletionFlag = false;
                if(MainActivity.mp != null) {

                    MainActivity.currentSong = MainActivity.getNextSong();

                    if(MainActivity.currentSong != null) {
                        MediaPlayer prevTemp = MainActivity.mp;
                        MainActivity.mp = MediaPlayer.create(c, Uri.parse(MainActivity.currentSong));
                        MediaPlayer temp = MainActivity.mp;
                        songName.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                        artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                        Glide.with(c).load(uri).crossFade().error(R.drawable.noalbumart).into(albumArt);
                        if(prevTemp != null && prevTemp.isPlaying()) {
                            prevTemp.stop();
                        }
                        temp.start();
                        MainActivity.mp.setOnCompletionListener(MainActivity.mat);
                        pp.setBackground(ContextCompat.getDrawable(c,android.R.drawable.ic_media_pause));
                    }

                    if(ActiveSong.pp != null) {
                        ActiveSong.setStuffUp();
                    }

                }
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.songCompletionFlag = false;
                if(MainActivity.mp != null) {

                    MainActivity.currentSong = MainActivity.getPreviousSong();

                    if(MainActivity.currentSong != null) {
                        MediaPlayer prevTemp = MainActivity.mp;
                        MainActivity.mp = MediaPlayer.create(c, Uri.parse(MainActivity.currentSong));
                        MediaPlayer temp = MainActivity.mp;
                        songName.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                        artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                        Glide.with(c).load(uri).crossFade().error(R.drawable.noalbumart).into(albumArt);
                        if(prevTemp != null && prevTemp.isPlaying()) {
                            prevTemp.stop();
                        }
                        temp.start();
                        MainActivity.mp.setOnCompletionListener(MainActivity.mat);
                        pp.setBackground(ContextCompat.getDrawable(c,android.R.drawable.ic_media_pause));
                    }

                    if(ActiveSong.pp != null) {
                        ActiveSong.setStuffUp();
                    }
                }
            }
        });

        ne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.mp.seekTo(MainActivity.mp.getCurrentPosition() + 3000);
                return true;
            }
        });

        pre.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.mp.seekTo(MainActivity.mp.getCurrentPosition() - 3000);
                return true;
            }
        });


        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! MainActivity.shuffle) {
                    MainActivity.shuffle = true;
                    shuffle.setBackground(ContextCompat.getDrawable(c,R.drawable.shuffle));
                    MainActivity.shuffledCurrentlyPlaying = new ArrayList<String>(MainActivity.currentlyPlaying.size());
                    for(String s : MainActivity.currentlyPlaying) {
                        MainActivity.shuffledCurrentlyPlaying.add((String) String.copyValueOf(s.toCharArray()));
                    }
                    Collections.shuffle(MainActivity.shuffledCurrentlyPlaying);
                } else {
                    MainActivity.shuffle = false;
                    shuffle.setBackground(ContextCompat.getDrawable(c,R.drawable.noshuffle));
                }
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.repeat == MainActivity.NO_LOOP) {
                    MainActivity.repeat = MainActivity.REPEAT_ALL_LOOP;
                    repeat.setBackground(ContextCompat.getDrawable(c,R.drawable.repall));
                } else if(MainActivity.repeat == MainActivity.REPEAT_ALL_LOOP) {
                    MainActivity.repeat = MainActivity.REPEAT_ONE_LOOP;
                    repeat.setBackground(ContextCompat.getDrawable(c,R.drawable.repone));
                } else {
                    MainActivity.repeat = MainActivity.NO_LOOP;
                    repeat.setBackground(ContextCompat.getDrawable(c,R.drawable.repnone));
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
        BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
        Glide.with(MainActivity.tempC).load(uri).crossFade().error(R.drawable.noalbumart).into(BottomPlayer.albumArt);

        super.onBackPressed();

    }
}
