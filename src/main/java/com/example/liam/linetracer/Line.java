package com.example.liam.linetracer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Liam on 18/04/2015.
 */
public class Line {
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;



    public Line(int xStart, int xEnd){
        this.xStart = xEnd;
        yStart = 0;
        yEnd = 100;
        this.xEnd = xStart;

    }

    private void updateValues() {
        yStart += 100;
        yEnd += 100;
    }

    public void drawLine(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        canvas.drawLine(xStart,yStart,xEnd,yEnd,paint);
        updateValues();
    }

    public int getXStart() {
        return xStart;
    }

    public int getYEnd() {
        return yEnd;
    }

    public int getYStart() {
        return yStart;
    }

    public int getXEnd() {
        return xEnd;
    }
}
