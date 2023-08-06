package com.example.deltaproject.gameobject;

import android.graphics.Canvas;

public abstract class GameObject {
    protected double velocityX;
    protected double velocityY;

    protected double positionX;
    protected double positionY;
    protected static double directionX;
    protected static double directionY;

    public GameObject(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public static double getDistanceBetweenObjects(GameObject object1, GameObject object2) {

        return Math.sqrt(
          Math.pow(object2.getPositionX()- object1.getPositionX(), 2) +
          Math.pow(object2.getPositionY() - object1.getPositionY(), 2)
        );

    }

    public abstract void draw(Canvas canvas);
    public abstract void update();

    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }

    protected static double getDirectionX() {

        return directionX;
    }
    protected static double getDirectionY() {
        return directionY;
    }
}
