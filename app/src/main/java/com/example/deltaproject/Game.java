package com.example.deltaproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.deltaproject.gameobject.Bullet;
import com.example.deltaproject.gameobject.Enemy;
import com.example.deltaproject.gameobject.GameObject;
import com.example.deltaproject.gameobject.Images;
import com.example.deltaproject.gameobject.Player;
import com.example.deltaproject.gamepanel.GameOver;
import com.example.deltaproject.gamepanel.Joystick;
import com.example.deltaproject.gamepanel.Performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
* Game class manages all the objects in teh game and is responsible for updating all states and
*  render all objects to the screen
*/
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    public static MediaPlayer bomb_blast_sound;
    public static MediaPlayer bomb_throw_sound;
    public static MediaPlayer enemy_spawn_sound;
    public static MediaPlayer game_over_sound;
    public static MediaPlayer hurt_sound;
    public static boolean gameOverSoundPlayed = false;
    private final Player player;
    private final Joystick joystick2;
    private static int score;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Bullet> bulletList = new ArrayList<Bullet>();
    private  final Joystick joystick;
    private GameLoop gameLoop;
    private GameOver gameOver;
    private int joystickPointerId =  -1;
    private int joystick2PointerId = -1;
    private Performance performance;

    private int MaxBullets = 1;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameOverSoundPlayed = false;
        bomb_blast_sound = MediaPlayer.create(context, R.raw.bomb_blast_sound);
        bomb_throw_sound = MediaPlayer.create(context, R.raw.bomb_throw_sound);
        enemy_spawn_sound = MediaPlayer.create(context, R.raw.enemy_spawn_sound);
        game_over_sound = MediaPlayer.create(context, R.raw.game_over_sound);
        hurt_sound = MediaPlayer.create(context, R.raw.hurt_sound);

        gameLoop = new GameLoop(this, surfaceHolder);

        //initialize game panels

        score = 0;
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);
        joystick = new Joystick(275, 900, 150, 100);
        joystick2 = new Joystick(1800, 900,100, 60);

        //initialize game objects

        player = new Player(context, joystick,2*500, 500, 100);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        if(gameLoop.getState().equals(Thread.State.TERMINATED)) {

            gameLoop = new GameLoop(this, holder);
        }


        gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: lmao ");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d(TAG, "surfaceChanged: lmao ");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //draw game objects
        player.draw(canvas);

        //draw game panels
        joystick.draw(canvas);
        joystick2.draw(canvas);
        performance.draw(canvas);
        drawScore(canvas);
        for(Enemy enemy: enemyList) {
            enemy.draw(canvas);
        }
        for(Bullet bullet: bulletList) {
            bullet.draw(canvas);
        }
        if(Player.getHealthPoints() <= 0) {
            gameOver.draw(canvas);
        }



    }


    public void update() {


        if(player.getHealthPoints() <= 0) {SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("HighScore",Math.max(score, MainActivity.HighScore));
            editor.apply();
            if(!gameOverSoundPlayed) {
                game_over_sound.start();
                gameOverSoundPlayed = true;
            }
            return;
        }


        joystick.update();
        joystick2.update();

        player.update();
        score++;

        if(Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
            enemy_spawn_sound.start();
        }

        //Update the state of each enemy
        for(Enemy enemy: enemyList) {
            enemy.update();
        }

        //Update the state of each bullet
        for(Bullet bullet: bulletList) {
            bullet.update();
            if(bullet.getPositionX()> 2000 || bullet.getPositionY() > 1000 || bullet.getPositionX() < 0 || bullet.getPositionY() < 0) {

                bulletList.remove(bullet);
                if(MaxBullets < 4) {
                    MaxBullets++;
                }


            }        }

        Iterator<Enemy> iteratorEnemy = enemyList.iterator();

        while(iteratorEnemy.hasNext()) {
            Images enemy = iteratorEnemy.next();
            if(Images.isColliding(enemy, player)){
                //remove enemy if collides with the player
                iteratorEnemy.remove();

                player.setHealthPoints(Player.getHealthPoints()-1);
                if(Player.getHealthPoints() > 1) {
                    hurt_sound.start();
                }
                continue;
            }
            Iterator<Bullet> iteratorBullet = bulletList.iterator();
            while(iteratorBullet.hasNext()) {

                if(Images.isColliding(iteratorBullet.next(), enemy)){
                    //remove enemy if collides with the player

                    iteratorBullet.remove();
                    iteratorEnemy.remove();
                    bomb_blast_sound.start();
                    score += 500;

                    if(MaxBullets < 1) {
                        MaxBullets++;
                    }
                    break;
                }
            }
        }
//        for(Enemy enemy: enemyList) {
//            if(Images.isColliding(enemy, player)) {
//                enemyList.remove(enemy);
//                player.setHealthPoints(Player.getHealthPoints()-1);
//                continue;
//            }
//            for(Bullet bullet: bulletList) {
//                if(Images.isColliding(bullet, enemy) ) {
//                    enemyList.remove(enemy);
//                    bulletList.remove(bullet);
//                    break;
//                }
//            }
//        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //handle touch events
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                if (joystick2.isPressed((double) event.getX(), (double) event.getY())) {

                    joystick2PointerId = event.getPointerId(event.getActionIndex());
                    joystick2.setIsPressed(true);
                }
                if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                    //joystick was previously, and is not pressed in this event -> shoot bullet
                }
                if(Player.getHealthPoints() <= 0) {

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                }
                return true;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if(joystick2.isPressed(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()))) {
                        joystick2PointerId = event.getPointerId(event.getActionIndex());
                        joystick2.setIsPressed(true);
                    }
                    if(joystick.isPressed(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()))) {
                        joystickPointerId = event.getPointerId(event.getActionIndex());
                        joystick.setIsPressed(true);
                    }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount() <= 1) {
                    // JoyStick was pressed previously and is now moved
                    if(joystick2PointerId == event.getPointerId(event.getActionIndex())) {
                        joystick2.setActuator((double) event.getX(event.getActionIndex()), (double) event.getY(event.getActionIndex()));
                        joystick2.setIsPressed(true);
                        if(joystick2.shoot && MaxBullets > 0) {
                            bulletList.add(new Bullet(getContext(), joystick2, player));
                            MaxBullets --;
                            bomb_throw_sound.start();
                        }
                    }
                    else if(joystickPointerId == event.getPointerId(event.getActionIndex()))  {
                        joystick.setActuator((double) event.getX(event.getActionIndex()), (double) event.getY(event.getActionIndex()));
                        joystick.setIsPressed(true);
                    }

                } else {

                    if(joystick2PointerId == event.getPointerId(event.getActionIndex())){
                        joystick2.setActuator((double) event.getX(0), (double) event.getY(0));
                        joystick2.setIsPressed(true);


                        joystick.setActuator((double) event.getX(1), (double) event.getY(1));
                        joystick.setIsPressed(true);
                    } else if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                        joystick2.setActuator((double) event.getX(1), (double) event.getY(1));
                        joystick2.setIsPressed(true);
                        joystick.setActuator((double) event.getX(0), (double) event.getY(0));
                        joystick.setIsPressed(true);
                    }
                    if(joystick2.shoot && MaxBullets > 0) {
                        bulletList.add(new Bullet(getContext(), joystick2, player));
                        MaxBullets --;
                    }

                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                if(joystick2PointerId == event.getPointerId(event.getActionIndex())) {
                    joystick2.setIsPressed(false);
                    joystick2.resetActuator();
                    joystick2PointerId = -1;
                }
                if(joystickPointerId == event.getPointerId(event.getActionIndex())){
                joystick.setIsPressed(false);
                joystick.resetActuator();
                joystickPointerId = -1;
            }
//                Log.d(TAG, "joystickPointerId: "+joystickPointerId+" event.getPointerId(event.getActionIndex()): "+event.getPointerId(event.getActionIndex()));
//
//                if(event.getX() < (float) getWidth()/2) {
//                    joystick.setIsPressed(false);
//                    joystick.resetActuator();
//                    joystickPointerId = -1;
//                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void pause() {
        gameLoop.stopLoop();
    }
    public void drawScore(Canvas canvas) {
        String score = Integer.toString((int) getScore());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score: "+score, 1800, 200, paint);
    }

    public static int getScore() {
        return score;
    }
}
