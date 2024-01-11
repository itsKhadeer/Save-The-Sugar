package com.example.deltaproject.gameobject;

import android.content.Context;
import android.content.res.Resources;

import androidx.core.content.ContextCompat;

import com.example.deltaproject.GameLoop;
import com.example.deltaproject.R;
import com.example.deltaproject.Utils;

/**
 * Ant is a character which always moves in the direction of the Sugar.
 * The Ant class is an extension of a Circle, which is an extension of a GameObject
 */
public class Ant extends Circle {

    private static final double SPEED_PIXELS_PER_SECOND = Sugar.SPEED_PIXELS_PER_SECOND*1.5;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private Sugar sugar;

    public Ant(Context context, Sugar sugar, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.Red), positionX, positionY, radius);
        this.sugar = sugar;
    }

    /**
     * Ant is an overload constructor used for spawning ants in random locations at the border
     * @param context
     * @param sugar
     */
    public Ant(Context context, Sugar sugar) {
        super(
                context,
                ContextCompat.getColor(context, R.color.Red),
                Math.random()*getScreenWidth(),
                Math.random()*getScreenHeight(),
                30
        );
        this.sugar = sugar;
    }

    /**
     * readyToSpawn checks if a new ant should spawn, according to the decided number of spawns
     * per minute (see SPAWNS_PER_MINUTE at top)
     * @return
     */
    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
    }

    public void update() {
        // =========================================================================================
        //   Update velocity of the ant so that the velocity is in the direction of the sugar
        // =========================================================================================
        // Calculate vector from ant to sugar (in x and y)
        double distanceToSugarX = sugar.getPositionX() - positionX;
        double distanceToSugarY = sugar.getPositionY() - positionY;

        // Calculate (absolute) distance between ant (this) and sugar
        double distanceToSugar = GameObject.getDistanceBetweenObjects(this, sugar);

        // Calculate direction from ant to sugar
        double directionX = distanceToSugarX/distanceToSugar;
        double directionY = distanceToSugarY/distanceToSugar;

        // Set velocity in the direction to the sugar
        if(distanceToSugar > 0) { // Avoid division by zero
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }

        // =========================================================================================
        //   Update position of the ant
        // =========================================================================================
        positionX += velocityX;
        positionY += velocityY;
    }

    public boolean is_touched(double finger_position_x, double finger_position_y) {
        double distance = Utils.getDistanceBetweenPoints(finger_position_x, finger_position_y, getPositionX(), getPositionY());
        if (distance < radius) {
            return true;
        }
        return false;
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}