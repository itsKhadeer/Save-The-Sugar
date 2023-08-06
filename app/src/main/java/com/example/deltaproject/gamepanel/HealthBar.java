package com.example.deltaproject.gamepanel;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.deltaproject.R;
import com.example.deltaproject.gameobject.Player;

/*
* HealthBar
* */
public class HealthBar {

    private final Paint borderPaint;
    private Player player;
    private int width, height, margin;
    private final Paint healthPaint;
    public HealthBar(Context context, Player player) {
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;
        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.white);
        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.magenta);
        healthPaint.setColor(healthColor);
        borderPaint.setColor(borderColor);
    }
    public void draw(Canvas canvas) {


        float x = (float)player.getPositionX() + 50;
        float y = (float)player.getPositionY() + 50;
        float distanceToPlayer = 60;
        float healthPointPercentage = (float)player.getHealthPoints()/player.MAX_HEALTH_POINTS;
        // draw border
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x-width/2;
        borderRight = x + width/2;
        borderTop = y-height-distanceToPlayer;
        borderBottom = y-distanceToPlayer;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, borderPaint);

        //draw health
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointPercentage;
        healthBottom = borderBottom-margin;
        healthTop = healthBottom - healthHeight;

        canvas.drawRect(healthLeft, healthTop, healthRight, healthBottom, healthPaint);

    }

}
