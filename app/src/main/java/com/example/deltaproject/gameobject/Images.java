package com.example.deltaproject.gameobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public abstract class Images extends GameObject {

    protected Bitmap bitmap;
    protected int bitmap_size;
    protected RectF rectf = new RectF();

    public Images(Context context,int resourceID, double positionX, double positionY, int bitmap_size) {
        super(positionX, positionY);

        this.bitmap_size = bitmap_size;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID);

    }

    public static boolean isColliding(Images object1 , Images object2) {
        if(object1.rectf.intersect(object2.rectf)) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {

        rectf.left = (float)positionX;
        rectf.top = (float)positionY;
        rectf.right = (float)positionX + bitmap_size;
        rectf.bottom = (float)positionY + bitmap_size;



        canvas.drawBitmap(bitmap,null,  rectf, null );
//        canvas.drawCircle((float)positionLeft, (float)positionRight, PLAYER_SIZE, paint);
    }


}
