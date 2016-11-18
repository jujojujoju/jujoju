package com.example.joju.myapplication7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by joju on 2016. 9. 27..
 */
public class Data
{

    ImageView imageView;
    Bitmap bitmap;
    float _x;
    float _y;
    float radius = 100;

    Paint paint;

//    RectF rect = new RectF();

    Data(Paint paint, Context context)
    {
//        this.context = context;
        imageView = new ImageView(context);
        this.paint = paint;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//
    }

    public synchronized ImageView clicked_scale(int scale)
    {
        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()*scale,bitmap.getHeight()*scale,true);

        float x = _x;
        float y = _y;

        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setImageBitmap(bitmap);

//        _canvas.drawBitmap(bitmap, x, y, null);

//        drawOnCanvas(_canvas);
//        canvas.drawBitmap(bitmap, _x, _y, null);
//        animation_bitmap = AnimationUtils.loadAnimation(context,R.anim.animation_bitmap);

        return imageView;
    }

    public void set_y(float y)
    {
        _y = y;
    }

    public void set_x(float x)
    {
        _x = x;
    }

    public float get_x()
    {
        return _x;
    }

    public float get_y()
    {
        return _y;
    }
    public synchronized  void drawOnCanvas(Canvas canvas)
    {
//        canvas.drawRoundRect(rect, _x, _y, paint);
//        canvas.drawCircle(_x,_y,radius,paint);

        canvas.drawBitmap(bitmap, _x, _y, null);
//        canvas.drawText("노트", _x, _y, paint);
    }

}
