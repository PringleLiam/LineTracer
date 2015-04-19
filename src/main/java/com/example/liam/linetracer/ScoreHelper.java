package com.example.liam.linetracer;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Liam on 18/04/2015.
 */
public class ScoreHelper extends ActionBarActivity {


    public int checkHighScore(int currentScore, Context context) throws IOException {

        int fileContent = 0;
        FileInputStream fis = context.openFileInput("highScore");
        BufferedInputStream br = new BufferedInputStream(fis);


        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ( (receiveString = bufferedReader.readLine()) != null ) {
            stringBuilder.append(receiveString);
        }

        br.close();
        String ret = stringBuilder.toString();

        fileContent = Integer.parseInt(ret);




        if (currentScore > fileContent) {
            saveHighScore(currentScore, context);
            return currentScore;
        } else {
            saveHighScore(fileContent, context);
            return fileContent;
        }
    }

    private void saveHighScore(int currentScore, Context context) throws IOException {

        String s = "" + currentScore;
        FileOutputStream outputStream = context.openFileOutput("highScore", Context.MODE_PRIVATE);
        outputStream.write(s.getBytes());
        outputStream.close();


    }

}

