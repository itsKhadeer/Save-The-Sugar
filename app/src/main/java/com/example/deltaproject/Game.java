package com.example.deltaproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
/**
* Game class manages all the objects in teh game and is responsible for updating all states and
*  render all objects to the screen
*/
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private GameLoop gameLoop;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);


        gameLoop = new GameLoop(this, surfaceHolder);
        player = new Player(getContext(), 450, 550);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawFPS(canvas);
        drawUPS(canvas);

        player.draw(canvas);
    }
    public void drawUPS(Canvas canvas) {

        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);

    }
    public void drawFPS(Canvas canvas) {

        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void update() {

        player.update();
        //Update game state

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //handle touch events
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.setPosition((double)event.getX(),(double) event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                player.setPosition((double)event.getX(),(double) event.getY());
                return true;
        }
        return super.onTouchEvent(event);
    }
}
