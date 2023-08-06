package com.example.deltaproject.gamepanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;

import com.example.deltaproject.Game;
import com.example.deltaproject.MainActivity;
import com.example.deltaproject.R;

public class GameOver  {
    Context context;
    float x =(float) getScreenWidth()/3;
    float y = (float)getScreenHeight()/2;


    public GameOver(Context context) {

        this.context = context;
    }
    public void draw(Canvas canvas ) {
        String text = "Game Over";


        String tap = "Tap anywhere to restart";
        Paint paint = new Paint();
        float left = x-50;
        float top = y-100;
        float right = x+750;
        float bottom = y+ 200;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int HighScore = preferences.getInt("HighScore", 0);
        MainActivity.HighScore = HighScore;
        String showScore = "Score: "+ Game.getScore()+"  HighScore: "+HighScore;

        paint.setColor(ContextCompat.getColor(context, R.color.white));

        canvas.drawRect(left-10, top-10, right+10, bottom+10, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.Yellow));
        canvas.drawRect(left, top, right, bottom, paint);
        int color = ContextCompat.getColor(context, R.color.Red);
        paint.setColor(color);
        float textSize = 75;
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);
        paint.setColor(ContextCompat.getColor(context, R.color.blue));

        paint.setTextSize(50);
        canvas.drawText(showScore,x, y+100, paint );
        canvas.drawText(tap, x, y+150, paint);

    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
