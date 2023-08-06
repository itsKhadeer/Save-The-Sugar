package com.example.deltaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/*" comments "*/
public class MainActivity extends AppCompatActivity {

    static boolean hurtSoundWasPlaying;
    static boolean bombBlastSoundWasPlaying;
    static boolean bombThrowSoundWasPlaying;
    static boolean enemySpawnSoundWasPlaying;
    static boolean gameOverSoundWasPlaying;

    private Game game;
    public static int HighScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        game = new Game(this);
        setContentView(game);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(gameOverSoundWasPlaying) {
            Game.game_over_sound.start();
            gameOverSoundWasPlaying = false;
        }
        if(bombThrowSoundWasPlaying) {
            Game.bomb_throw_sound.start();
            bombThrowSoundWasPlaying = false;
        }
        if(bombBlastSoundWasPlaying) {
            Game.bomb_blast_sound.start();
            bombBlastSoundWasPlaying = false;
        }
        if(hurtSoundWasPlaying) {
            Game.hurt_sound.pause();
            hurtSoundWasPlaying = false;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
        if(Game.game_over_sound.isPlaying()) {
            Game.game_over_sound.pause();
            gameOverSoundWasPlaying = true;
        }
        if(Game.bomb_throw_sound.isPlaying()) {
            Game.bomb_throw_sound.pause();
            bombThrowSoundWasPlaying = true;
        }
        if(Game.bomb_blast_sound.isPlaying()) {
            Game.bomb_blast_sound.pause();
            bombBlastSoundWasPlaying = true;
        }
        if(Game.hurt_sound.isPlaying()) {
            Game.hurt_sound.pause();
            hurtSoundWasPlaying = true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}