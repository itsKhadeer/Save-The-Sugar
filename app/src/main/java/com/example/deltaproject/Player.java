package com.example.deltaproject;





import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Player {
    Bitmap player;
    RectF rectf = rectf = new RectF();;
    int PLAYER_SIZE = 100;

    private Context context;
    public Player(Context context, double positionLeft, double positionTop){
        this.rectf.left = (float)positionLeft;
        this.rectf.top = (float)positionTop;
        this.rectf.bottom = this.rectf.top+ PLAYER_SIZE;
        this.rectf.right = this.rectf.left+ PLAYER_SIZE;
        this.context = context;

    }
    public void draw(Canvas canvas) {


        player = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank);
        canvas.drawBitmap(player,null, rectf, null );
//        Paint paint = new Paint();
//        paint.setColor(Color.parseColor("#FFFFFF"));
//        canvas.drawCircle((float)positionLeft, (float)positionRight, PLAYER_SIZE, paint);
    }

    public void update() {
    }

    public void setPosition(double x, double y) {

        rectf.left = (float)x - PLAYER_SIZE;
        rectf.top =(float) y - PLAYER_SIZE;
        this.rectf.bottom = this.rectf.top+ PLAYER_SIZE;
        this.rectf.right = this.rectf.left+ PLAYER_SIZE;
    }
}
