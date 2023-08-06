package com.example.deltaproject.gameobject;

import android.content.Context;

import com.example.deltaproject.GameLoop;
import com.example.deltaproject.R;
import com.example.deltaproject.gamepanel.Joystick;

public class Bullet extends Images {
    public static final double SPEED_PIXELS_PER_SECOND = 800.0 ;
    private static final double MAX_SPEED =  SPEED_PIXELS_PER_SECOND/ GameLoop.MAX_UPS;


    public Bullet(Context context, Joystick joystick, Player player) {
        super(
                context,
                R.drawable.bullet,
                joystick.getActuatorX(),
                joystick.getActuatorY(),
                50);
        this.positionX = player.getPositionX();
        this.positionY = player.getPositionY();
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;
    }

}
