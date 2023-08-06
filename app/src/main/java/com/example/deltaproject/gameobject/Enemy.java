package com.example.deltaproject.gameobject;

import android.content.Context;

import com.example.deltaproject.GameLoop;
import com.example.deltaproject.R;

public class Enemy extends Images {

    private static Player player;
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6 ;
    private static final double MAX_SPEED =  SPEED_PIXELS_PER_SECOND/ GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;

    public Enemy(Context context, Player player, double positionX, double positionY, int enemy_size) {
        super(context, R.drawable.enemy, positionX, positionY, enemy_size);
        this.player = player;
    }

    public Enemy(Context context, Player player) {
        super(
                context,
                R.drawable.enemy,
                Math.random()*1000,
                Math.random()*1000,
                100);
        this.player = player;
    }


    public static boolean readyToSpawn() {
        if(updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
    }

    @Override
    public void update() {

        //update the velocity of the enemy so that the velocity is in the direction of the enemy


        //calculate vector from enemy to player (int x and y)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;


        //calculate (absolute) distance between enemy(this) and player

        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

        //calculate the direction from enemy to player

        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

        //set velocity in the direction to the player

        if(distanceToPlayer > 0) {
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;

        } else {
            velocityX = 0;
            velocityY = 0;
        }


        //update the position of teh enemy

        positionX += velocityX;
        positionY += velocityY;

    }
}
