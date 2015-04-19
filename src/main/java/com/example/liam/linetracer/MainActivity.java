package com.example.liam.linetracer;



import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    private final Activity parent = this;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;


    private HashMap<Long, String> songList = new HashMap<Long,String>();

    public static long songID;
    public static int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        TextView textView =  (TextView)findViewById(R.id.textView3);
        textView.setText("High Score: "+MainActivity.highScore);
        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button testButton = (Button) v;
                Intent intent = new Intent(parent , GameWindow.class);
                intent.putExtra("Song_ID", songID);
                startActivity(intent);
            }
        });


        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);
                String titleArtist = thisTitle + "," + thisArtist;
                songList.put( thisId,titleArtist);
            } while (cursor.moveToNext());
        }


         Spinner spinner = (Spinner)findViewById(R.id.spinner);
         String[] stringList = new String[songList.keySet().size()];
        int i = 0;
        for(Long l : songList.keySet()){
            stringList[i] = songList.get(l).split(",")[0];
            i++;
         }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringList);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener adapterSpinner =  new AdapterSpinnerArray(songList);
        spinner.setOnItemSelectedListener(adapterSpinner);

}




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            ScoreHelper scoreHelper = new ScoreHelper();
            highScore = scoreHelper.checkHighScore(0,getApplicationContext()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView textView =  (TextView)findViewById(R.id.textView3);
        textView.setText("High Score: "+highScore);

    }


    @Override
    protected void onResume(){
        super.onResume();

        try {
                ScoreHelper scoreHelper = new ScoreHelper();
                highScore = scoreHelper.checkHighScore(GameWindow.score,getApplicationContext());
                GameWindow.score = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView textView =  (TextView)findViewById(R.id.textView3);
        textView.setText("High Score: "+highScore);

    }

}


