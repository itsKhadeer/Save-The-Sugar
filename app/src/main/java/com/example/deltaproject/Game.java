package com.example.deltaproject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.deltaproject.gameobject.Circle;
import com.example.deltaproject.gameobject.Ant;
import com.example.deltaproject.gameobject.Sugar;
import com.example.deltaproject.gamepanel.GameOver;
import com.example.deltaproject.gamepanel.Performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game manages all objects in the game and is responsible for updating all states and render all
 * objects to the screen
 */
class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Sugar sugar;
    private GameLoop gameLoop;
    private List<Ant> antList = new ArrayList<Ant>();
    private GameOver gameOver;
    private Performance performance;

    public Game(Context context) {
        super(context);
        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        // Initialize game panels
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);

        // Initialize game objects
        sugar = new Sugar(context,2*500, 500, 2*500, 500, 32);

        // Initialize display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Handle user input touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (sugar.check_is_touched(event.getX(), event.getY())) {
                     sugar.set_is_touched(true);
                }
                //if the ant is smashed, ant dies
                antList.removeIf(ant -> ant.is_touched(event.getX(), event.getY()));
                return true;
            case MotionEvent.ACTION_MOVE:
                if (sugar.get_is_touched()) {
                    sugar.setFinger_position_x(event.getX());
                    sugar.setFinger_position_y(event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                sugar.set_is_touched(false);
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw Tilemap

        // Draw game objects
        sugar.draw(canvas);

        for (Ant ant : antList) {
            ant.draw(canvas);
        }

        // Draw game panels
        performance.draw(canvas);

        // Draw Game over if the ant eat the whole sugar
        if (sugar.getHealthPoint() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {
        // Stop updating the game if the ants ate the sugar
        if (sugar.getHealthPoint() <= 0) {
            return;
        }

        // Update game state
        sugar.update();

        // Spawn ant
        if(Ant.readyToSpawn()) {
            antList.add(new Ant(getContext(), sugar));
        }

        // Update states of all ants
        for (Ant ant : antList) {
            ant.update();
        }

        // Iterate through antList and Check for collision between each ant and the ant and
        for (Circle ant : antList) {
            if (Circle.isColliding(ant, sugar)) {
                sugar.setHealthPoint(sugar.getHealthPoint() - 1);
            }
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}