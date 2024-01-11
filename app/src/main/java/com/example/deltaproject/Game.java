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
import com.example.deltaproject.gameobject.Player;
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

    private final Player player;
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
        player = new Player(context,2*500, 500, 2*500, 500, 32);

        // Initialize display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Handle user input touch event actions
        player.setFinger_position_x(event.getX());
        player.setFinger_position_y(event.getY());

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
        player.draw(canvas);

        for (Ant ant : antList) {
            ant.draw(canvas);
        }

        // Draw game panels
        performance.draw(canvas);

        // Draw Game over if the player is dead
        if (player.getHealthPoint() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {
        // Stop updating the game if the player is dead
        if (player.getHealthPoint() <= 0) {
            return;
        }

        // Update game state
        player.update();

        // Spawn enemy
        if(Ant.readyToSpawn()) {
            antList.add(new Ant(getContext(), player));
        }

        // Update states of all enemies
        for (Ant ant : antList) {
            ant.update();
        }

        // Iterate through enemyList and Check for collision between each enemy and the player and
        // spells in spellList.
        Iterator<Ant> iteratorAnt = antList.iterator();
        while (iteratorAnt.hasNext()) {
            Circle ant = iteratorAnt.next();
            if (Circle.isColliding(ant, player)) {
                // Remove enemy if it collides with the player
                iteratorAnt.remove();
                player.setHealthPoint(player.getHealthPoint() - 1);
            }
        }

        // Update gameDisplay so that it's center is set to the new center of the player's
        // game coordinates
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}