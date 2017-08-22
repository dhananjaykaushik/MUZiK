package com.myapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener, MediaPlayer.OnCompletionListener{

    public static MediaPlayer mp;
    public static Cursor c;


    public static ArrayList<String> songNames = new ArrayList();
    public static ArrayList<String> songData =  new ArrayList();
    public static ArrayList<String> songArtists =  new ArrayList();
    public static ArrayList<String> songAlbums =  new ArrayList();
    public static ArrayList<String> songAlbumId =  new ArrayList();
    public static ArrayList<String> currentlyPlaying = new ArrayList();
    public static ArrayList<String> songAlbumArt = new ArrayList<>();
    public static ArrayList<String> songID = new ArrayList<>();
    public static boolean shuffle = false;
    public static ArrayList<String> shuffledCurrentlyPlaying = new ArrayList<>();


    public static boolean songCompletionFlag = false;
    public static int NO_LOOP = 0;
    public static int REPEAT_ALL_LOOP = 1;
    public static int REPEAT_ONE_LOOP = 2;

    public static int repeat = NO_LOOP;


    public static String currentFragment;

    public static RelativeLayout mc;


    public static String currentSong = null;

    public static Context tempC;

    public static MainActivity mat;

    public static Intent startIntent;


    private SensorManager mSensorManager;
    private Sensor mProximity;
    private Sensor mAccelerometer;
    private static final int SENSOR_SENSITIVITY = 4;
    private static final float SHAKE_THRESHOLD = 30f;
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mat = this;
        tempC = this;
        startIntent = getIntent();
        View frag1 = findViewById(R.id.fragment1);
        frag1.setVisibility(View.INVISIBLE);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_GAME);

        soulOfProject();
        mc = (RelativeLayout) findViewById(R.id.main_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        BottomPlayer.ppb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer temp = MainActivity.mp;
                if(temp != null) {
                    if(! temp.isPlaying()) {
                        temp.start();
                        BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
                    } else {
                        temp.pause();
                        BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_play));
                    }
                }
            }
        });

        BottomPlayer.nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songCompletionFlag = false;
                if(MainActivity.mp != null) {
                    MainActivity.currentSong = MainActivity.getNextSong();

                    if(MainActivity.currentSong != null) {
                        MediaPlayer prevTemp = MainActivity.mp;
                        MainActivity.mp = MediaPlayer.create(MainActivity.this, Uri.parse(MainActivity.currentSong));
                        MediaPlayer temp = MainActivity.mp;
                        BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                        BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                        Glide.with(MainActivity.this).load(uri).crossFade().placeholder(R.drawable.noalbumart).into(BottomPlayer.albumArt);
                        if(prevTemp != null && prevTemp.isPlaying()) {
                            prevTemp.stop();
                        }
                        temp.start();
                        BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
                    }


                }
            }
        });

        BottomPlayer.pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songCompletionFlag = false;
                if(MainActivity.mp != null) {

                    if(MainActivity.mp.isPlaying() && MainActivity.currentSong != null) {
                        MainActivity.currentSong = MainActivity.getPreviousSong();
                        if(MainActivity.currentSong != null) {
                            MediaPlayer prevTemp = MainActivity.mp;
                            MainActivity.mp = MediaPlayer.create(MainActivity.this, Uri.parse(MainActivity.currentSong));
                            MediaPlayer temp = MainActivity.mp;
                            BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                            BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));

                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                            Glide.with(MainActivity.this).load(uri).crossFade().placeholder(R.drawable.noalbumart).into(BottomPlayer.albumArt);

                            if(prevTemp != null && prevTemp.isPlaying()) {
                                prevTemp.stop();
                            }
                            temp.start();
                            BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
                        }


                    }
                }
            }
        });


    }

    public static String getNextSong() {
        int index = currentlyPlaying.indexOf(currentSong);
        int indexr = shuffledCurrentlyPlaying.indexOf(currentSong);

        if(MainActivity.songCompletionFlag) {
            if(repeat == REPEAT_ONE_LOOP) {
                return currentSong;
            }
            if(shuffle) {
                if(indexr == MainActivity.shuffledCurrentlyPlaying.size() - 1) {
                    return MainActivity.shuffledCurrentlyPlaying.get(0);
                } else {
                    return MainActivity.shuffledCurrentlyPlaying.get(indexr + 1);
                }
            } else {
                if (repeat == NO_LOOP) {
                    if (isLastSong()) {
                        return currentSong;
                    } else {
                        return MainActivity.currentlyPlaying.get(index + 1);
                    }
                }

                if (repeat == REPEAT_ALL_LOOP && isLastSong()) {
                    return MainActivity.getFirstSong();
                } else {
                    return MainActivity.currentlyPlaying.get(index + 1);
                }

            }
        } else {
            if(shuffle) {
                if(indexr == MainActivity.shuffledCurrentlyPlaying.size() - 1) {
                    return MainActivity.shuffledCurrentlyPlaying.get(0);
                } else {
                    return MainActivity.shuffledCurrentlyPlaying.get(indexr + 1);
                }
            } else {
                if(isLastSong()) {
                    return MainActivity.currentlyPlaying.get(0);
                } else {
                    return MainActivity.currentlyPlaying.get(index + 1);
                }
            }
        }
    }

    public static String getFirstSong() {
        return currentlyPlaying.get(0);
    }

    public static String getLastSong() {
        return currentlyPlaying.get(currentlyPlaying.size() - 1);
    }

    public static boolean isLastSong() {
        if(MainActivity.currentlyPlaying.indexOf(MainActivity.currentSong) == (MainActivity.currentlyPlaying.size()) - 1)
            return true;
        return false;
    }

    public static boolean isFirstSong() {
        if(MainActivity.currentlyPlaying.indexOf(MainActivity.currentSong) == 0)
            return true;
        return false;
    }

    public static String getRandomSong() {
        int size = MainActivity.songData.size();
        Random r = new Random();
        int Low = 0;
        int High = size - 1;
        int Result = r.nextInt(High-Low) + Low;

        return MainActivity.songData.get(Result);
    }

    public static String getPreviousSong() {
        int index = currentlyPlaying.indexOf(currentSong);
        int indexr = shuffledCurrentlyPlaying.indexOf(currentSong);

        if(MainActivity.songCompletionFlag) {

            if(repeat == REPEAT_ONE_LOOP) {
                return currentSong;
            }
            if(shuffle) {
                return MainActivity.shuffledCurrentlyPlaying.get(indexr - 1);
            } else {
                if (repeat == NO_LOOP) {
                    if (isFirstSong()) {
                        return currentSong;
                    } else {
                        return MainActivity.currentlyPlaying.get(index - 1);
                    }
                }

                if (repeat == REPEAT_ALL_LOOP && isFirstSong()) {
                    return MainActivity.getLastSong();
                } else {
                    return MainActivity.currentlyPlaying.get(index - 1);
                }

            }

        } else {
            if(shuffle) {
                if(indexr == 0) {
                    return MainActivity.shuffledCurrentlyPlaying.get(MainActivity.shuffledCurrentlyPlaying.size() - 1);
                } else {
                    return MainActivity.shuffledCurrentlyPlaying.get(indexr - 1);
                }
            } else {
                if(isFirstSong()) {
                    return getLastSong();
                } else {
                    return MainActivity.currentlyPlaying.get(index - 1);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.action_about) {
            Intent i = new Intent(this,About.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences sharedPreferences = getSharedPreferences("muziksetting",MODE_PRIVATE);
        String proxVal = sharedPreferences.getString("proxSensor", "off");
        String shakeVal = sharedPreferences.getString("shakeSensor", "off");

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                if(proxVal.equals("on") && MainActivity.mp != null && MainActivity.mp.isPlaying()) {
                    MainActivity.mp.pause();
                    BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_play));
                    if(ActiveSong.pp != null) {
                        ActiveSong.pp.setBackground(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.ic_media_play));
                    }
                }
            } else {
                //far
                if(proxVal.equals("on") && MainActivity.mp != null && !MainActivity.mp.isPlaying()) {
                    MainActivity.mp.start();
                    MainActivity.mp.setOnCompletionListener(MainActivity.mat);
                    BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
                    if(ActiveSong.pp != null)
                        ActiveSong.pp.setBackground(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.ic_media_pause));
                }
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    // SHAKE DETECTED

                    if(shakeVal.equals("on") && MainActivity.mp != null && MainActivity.mp.isPlaying()) {
                        //PLAYING RANDOM SONG

                        MainActivity.currentSong = MainActivity.getRandomSong();

                        if(MainActivity.currentSong != null) {
                            MediaPlayer prevTemp = MainActivity.mp;
                            MainActivity.mp = MediaPlayer.create(MainActivity.this, Uri.parse(MainActivity.currentSong));
                            MediaPlayer temp = MainActivity.mp;
                            BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                            BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                            Glide.with(MainActivity.this).load(uri).crossFade().error(R.drawable.noalbumart).into(BottomPlayer.albumArt);
                            if(prevTemp != null && prevTemp.isPlaying()) {
                                prevTemp.stop();
                            }
                            temp.start();
                            MainActivity.mp.setOnCompletionListener(MainActivity.mat);
                            BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
                        }

                        if(ActiveSong.pp != null) {
                            ActiveSong.setStuffUp();
                        }


                    }


                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCompletion(MediaPlayer mp) {

        songCompletionFlag = true;

        if(MainActivity.mp != null) {
            MainActivity.currentSong = MainActivity.getNextSong();

            if(MainActivity.currentSong != null) {
                MediaPlayer prevTemp = MainActivity.mp;
                MainActivity.mp = MediaPlayer.create(MainActivity.this, Uri.parse(MainActivity.currentSong));
                MediaPlayer temp = MainActivity.mp;
                BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
                Glide.with(MainActivity.this).load(uri).crossFade().error(R.drawable.noalbumart).into(BottomPlayer.albumArt);
                if(prevTemp != null && prevTemp.isPlaying()) {
                    prevTemp.stop();
                }
                temp.start();
                MainActivity.mp.setOnCompletionListener(MainActivity.mat);
                BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
            }

            if(ActiveSong.pp != null) {
                ActiveSong.setStuffUp();
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 : Playlists pl = new Playlists();
                    return pl;
                case 1 : Albums al = new Albums();
                    return al;
                case 2 : Artists ar = new Artists();
                    return ar;
                case 3 : Tracks tr = new Tracks();
                    return tr;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PLAYLIST";
                case 1:
                    return "ALBUMS";
                case 2:
                    return "ARTISTS";
                case 3:
                    return "TRACKS";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }



    @Override
    protected void onStart() {
        soulOfProject();
        super.onStart();
    }

    @Override
    protected void onResume() {
        soulOfProject();
        super.onResume();
    }

    public static void soulOfProject() {

        if(mp == null || ! mp.isPlaying()) {
            BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(tempC,android.R.drawable.ic_media_play));
        } else {
            BottomPlayer.ppb.setBackground(ContextCompat.getDrawable(tempC,android.R.drawable.ic_media_pause));
        }

        if(mp != null) {
            BottomPlayer.sname.setText(MainActivity.songNames.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
            BottomPlayer.artistName.setText(MainActivity.songArtists.get(MainActivity.songData.indexOf(MainActivity.currentSong)));
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(MainActivity.songAlbumId.get(MainActivity.songData.indexOf(MainActivity.currentSong))));
            Glide.with(MainActivity.tempC).load(uri).crossFade().error(R.drawable.noalbumart).into(BottomPlayer.albumArt);

        }

        songNames = new ArrayList<>();
        songData = new ArrayList<>();
        songArtists = new ArrayList<>();
        songAlbums = new ArrayList<>();
        songAlbumArt = new ArrayList<>();
        songID = new ArrayList<>();
        songAlbumId = new ArrayList<>();

        c = tempC.getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.TITLE);


        while (MainActivity.c.moveToNext()) {

            int i = MainActivity.c.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int j = MainActivity.c.getColumnIndex(MediaStore.Audio.Media.DATA);
            int k = MainActivity.c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int l = MainActivity.c.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int m = MainActivity.c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int n = MainActivity.c.getColumnIndex(MediaStore.Audio.Media._ID);


            if(MainActivity.c.getString(i) != null) {
                songNames.add(MainActivity.c.getString(i));
                songData.add(MainActivity.c.getString(j));
                songArtists.add(MainActivity.c.getString(k));
                songAlbums.add(MainActivity.c.getString(l));
                songAlbumId.add(MainActivity.c.getString(m));
                songID.add(MainActivity.c.getString(n));
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    public static String convertToDuration(long dur) {
        String duration;
        dur = dur / 1000;
        duration = (dur / 60) + ":" + (dur % 60);
        return duration;
    }

    public static void fakeBack() {
        mat.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        mat.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }


    public static void appReset() {

        MainActivity.mat.finish();
        MainActivity.mat.startActivity(startIntent);

        Tracks.selectedItems = new ArrayList<String>();
        Artists.selectedItems = new ArrayList<String>();
        ArtistSongs.selectedItems = new ArrayList<String>();
        Albums.selectedItems = new ArrayList<String>();
        AlbumSongs.selectedItems = new ArrayList<String>();
        Playlists.selectedItems = new ArrayList<String>();
        PlaylistSongs.selectedItems = new ArrayList<String>();

        soulOfProject();
    }



}