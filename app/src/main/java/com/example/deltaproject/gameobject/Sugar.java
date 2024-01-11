package com.example.deltaproject.gameobject;
import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import com.example.deltaproject.GameLoop;
import com.example.deltaproject.gamepanel.HealthBar;
import com.example.deltaproject.R;
import com.example.deltaproject.Utils;

/**
 * Sugar is the main character of the game, which the user can control with touch.
 * The Sugar class is an extension of a Circle, which is an extension of a GameObject
 */
public class Sugar extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 200.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static final int MAX_HEALTH_POINTS = 600;
    public static final int MAX_SPEED_DIST = 200;
    protected boolean is_touched;
    protected double finger_position_x;
    protected double finger_position_y;
    private HealthBar healthBar;
    private int healthPoints = MAX_HEALTH_POINTS;

    public Sugar(Context context,double finger_position_x, double finger_position_y, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.white), positionX, positionY, radius);
        this.finger_position_x = finger_position_x;
        this.finger_position_y = finger_position_y;
        this.healthBar = new HealthBar(context, this);
    }

    public boolean get_is_touched() {
        return is_touched;
    }
     public void set_is_touched(boolean is_touched) {
        this.is_touched = is_touched;
     }

     public boolean check_is_touched(double finger_position_x, double finger_position_y) {
        double distance = Utils.getDistanceBetweenPoints(finger_position_x, finger_position_y, this.positionX, this.positionY);

        return (distance < radius);
     }

    public void update() {

        // Update velocity based on finger position
        double deltaX = finger_position_x - positionX;
        double deltaY = finger_position_y - positionY;
        double actuatorX = deltaX/MAX_SPEED_DIST;
        double actuatorY = deltaY/MAX_SPEED_DIST;


        velocityX = actuatorX*MAX_SPEED;
        velocityY = actuatorY*MAX_SPEED;

        // Update position
        positionX += velocityX;
        positionY += velocityY;

        // Update direction
        if (velocityX != 0 || velocityY != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
    }

    public void draw(Canvas canvas) {
//        animator.draw(canvas, gameDisplay, this);
        double x = this.getPositionX();
        double y = this.getPositionY();
        canvas.drawCircle((float)x, (float)y, (float)radius, paint);
        healthBar.draw(canvas);
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setFinger_position_x(double finger_position_x) {
        this.finger_position_x = finger_position_x;
    }
    public void setFinger_position_y(double finger_position_y) {
        this.finger_position_y = finger_position_y;
    }

    public void setHealthPoint(int healthPoints) {
        // Only allow positive values
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }


}