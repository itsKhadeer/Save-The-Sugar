package com.example.deltaproject.gameobject;





import android.content.Context;
import android.graphics.Canvas;

import com.example.deltaproject.GameLoop;
import com.example.deltaproject.Utils;
import com.example.deltaproject.gamepanel.HealthBar;
import com.example.deltaproject.gamepanel.Joystick;
import com.example.deltaproject.R;

/*
* player class extends Images which extends game objects*/
public class Player extends Images {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0 ;
    public  final int MAX_HEALTH_POINTS = 10;
    private static final double MAX_SPEED =  SPEED_PIXELS_PER_SECOND/ GameLoop.MAX_UPS;


    private final Joystick joystick;
    private HealthBar healthBar;
    private static int healthPoints;



    public Player(Context context, Joystick joystick, double positionX, double positionY, int player_size){
        super( context, R.drawable.tank, positionX, positionY, player_size);


        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        healthPoints = MAX_HEALTH_POINTS;

/*      Paint paint = new Paint();
*       paint.setColor(Color.parseColor("#FFFFFF"));
*/


    }

    public void update() {
        //velocity is updated based on the actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        //update the position
        positionX += velocityX;
        positionY += velocityY;

        //update direction
        if(velocityX != 0 || velocityY != 0) {
            //normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;

        }
    }
    public void draw(Canvas canvas) {
        super.draw(canvas);
        healthBar.draw(canvas);
    }

    public static int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if(healthPoints >= 0) {
            this.healthPoints = healthPoints;
        }

    }
}
