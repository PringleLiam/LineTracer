package com.example.liam.linetracer;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Liam on 18/04/2015.
 */
public class ScoreHelper extends Activity {


    public int checkHighScore(int currentScore) throws IOException {
        File file = new File("highScore");
        if(file.exists()) {
            BufferedInputStream br = null;

            try {
                br = new BufferedInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int fileContent = 0;
            while (br != null) {
                fileContent = br.read();

            }
            if (currentScore > fileContent) {
                saveHighScore(currentScore);
                return currentScore;
            } else {
                return fileContent;
            }
        }
        saveHighScore(currentScore);
        return currentScore;
    }
    private void saveHighScore(int currentScore) throws FileNotFoundException {
        FileOutputStream outputStream = null;

        try {
            outputStream = openFileOutput("highScore", Context.MODE_PRIVATE);
            outputStream.write(currentScore);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
