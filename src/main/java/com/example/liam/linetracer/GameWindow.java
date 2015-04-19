package com.example.liam.linetracer;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Liam on 18/04/2015.
 */
public class GameWindow extends ActionBarActivity {

    private ImageView drawingImageView;
    private Visualizer visualizer;
    private int previousValue = 0;
    private MediaPlayer mp;
    private Canvas canvas;
    private ArrayList<Line> lineList = new ArrayList<Line>();
    private Bitmap bitmap;
    private int distance = 0;
    public static int score = 0;
    private boolean isFinished = false;
    private boolean isPaused;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_window);
        drawingImageView = (ImageView) this.findViewById(R.id.DrawingImageView);
        bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);
        isPaused = false;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PauseMenu.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.DrawingImageView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isFinished) {
                    int x = (int) event.getX();
                    int lineX = lineList.get(0).getXEnd();
                    TextView textView = (TextView) findViewById(R.id.textView);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = (int) event.getX();
                            distance = x - lineX;
                            score += (int) (50 * Math.pow(0.8, Math.abs(distance) / 10));
                            textView.setText("" + score);
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            x = (int) event.getX();
                            distance = x - lineX;
                            score += (int) (50 * Math.pow(0.8, Math.abs(distance) / 10));
                            textView.setText("" + score);
                            return true;
                        case MotionEvent.ACTION_UP:
                            x = (int) event.getX();
                            distance = x - lineX;
                            score += (int) (50 * Math.pow(0.8, Math.abs(distance) / 10));
                            textView.setText("" + score);
                            return true;
                    }
                    distance = x - lineX;

                }
                return false;
            }

        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //countdown();
        new CountDownTimer(3000, 10) {
            TextView textView = (TextView) findViewById(R.id.textView);

            public void onTick(long millisUntilFinished) {
                textView.setText("" + ((millisUntilFinished / 1000) + 1));
            }

            public void onFinish() {
                textView.setText("0");
                mp = new MediaPlayer();
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MainActivity.songID);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mp.setDataSource(getApplicationContext(), contentUri);
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setVisualizer();
                visualizer.setEnabled(true);
                mp.start();
                isFinished = true;
            }
        }.start();
    }

    private void setVisualizer() {
        visualizer = new Visualizer(mp.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        int num = 0;
                        for (byte b : bytes) {
                            num = +b;
                        }
                        //num /= bytes.length;
                        showLines(((num + 128) * 720) / 256);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {

                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

    }


    public void showLines(int value) {
        if (mp.isPlaying()) {
            canvas.drawColor(Color.BLACK);
            Line line = new Line(previousValue, value);
            previousValue = value;
            canvas = new Canvas(bitmap);
            if (lineList.size() > 11) {
                lineList.remove(0);
            }
            lineList.add(line);
            for (Line l : lineList) {
                l.drawLine(canvas);
            }
            drawingImageView.setImageBitmap(bitmap);
        } else {
            try {
               ScoreHelper scoreHelper = new ScoreHelper();
               MainActivity.highScore = scoreHelper.checkHighScore(score,getApplicationContext());
               score = 0;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null && !mp.isPlaying()) {
            mp.start();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mp != null &&!mp.isPlaying()) {
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}


