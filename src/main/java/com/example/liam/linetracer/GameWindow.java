package com.example.liam.linetracer;

import android.app.Activity;
import android.content.ContentUris;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
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






        findViewById(R.id.DrawingImageView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                         x = (int) event.getX();
                         y = (int) event.getY();

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        x = (int) event.getX();
                        y = (int) event.getY();

                        return true;

                    case MotionEvent.ACTION_UP:
                        x = (int) event.getX();
                        y = (int) event.getY();

                        return true;


                }
                System.out.println(x + " , " + y);
                return false;
            }

        });


     TextView textView = (TextView) findViewById(R.id.textView);
        int[] intList = new int[2];
        textView.getLocationOnScreen(intList);

        for(int i : intList) {
            System.out.println(i);
        }
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
                    mp.setDataSource(getApplicationContext(),contentUri);
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setVisualizer();
                visualizer.setEnabled(true);
                mp.start();


            }
        }.start();
    }

    public void countdown(){
        TextView textView = (TextView) findViewById(R.id.textView);
        for (int count = 3; count >= 0; count--){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String currentCount = "" + count;
            textView.setText(currentCount);
        }

    }

    private void setVisualizer(){
        visualizer = new Visualizer(mp.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        int num = 0;
                        for(byte b : bytes) {
                            num =+ b;
                        }
                        //num /= bytes.length;
                        showLines(((num+128)*720)/256);
                    }
                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {

                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

    }


    public void showLines(int value){
        canvas.drawColor(Color.BLACK);
        Line line = new Line(previousValue,value);
        previousValue = value;
        canvas = new Canvas(bitmap);
       if(lineList.size() > 11){
           lineList.remove(0);
      }
        lineList.add(line);
        for(Line l : lineList){
            l.drawLine(canvas);
        }
        drawingImageView.setImageBitmap(bitmap);





    }


    }

