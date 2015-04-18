package com.example.liam.linetracer;

import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;

/**
 * Created by Liam on 18/04/2015.
 */
public class AdapterSpinnerArray implements AdapterView.OnItemSelectedListener {

    private final HashMap<Long, String> songList;

    public AdapterSpinnerArray(HashMap<Long, String> givenSongList){
        songList = givenSongList;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedSong = (String)parent.getItemAtPosition(position);
        for(Long song : songList.keySet()) {
            if (selectedSong.equals(songList.get(song).split(",")[0])) {
                MainActivity.songID = song;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
