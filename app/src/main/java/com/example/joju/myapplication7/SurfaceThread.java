package com.example.joju.myapplication7;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by joju on 2016. 9. 27..
 */
public class SurfaceThread extends Thread
{
    boolean allmatched = false;
    int score = 0;
    public void toss_score(int score)
    {
        this.score = score;
    }

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Bitmap bitmap;
    private SurfaceHolder msurfaceHolder;
    private GameView mgameView;
    private boolean isrunning = false;
    RythmGameActivity rythmGameActivity;

    boolean []matched = {false,false,false,false};

    public SurfaceThread(SurfaceHolder surfaceHolder, GameView gameView,RythmGameActivity rythmGameActivity)
    {
        this.rythmGameActivity = rythmGameActivity;
        msurfaceHolder = surfaceHolder;
        mgameView = gameView;

//        bitmap = Bitmap.createScaledBitmap(bitmap, gameView.getWidth(), gameView.getHeight(), true);
        paint.setColor(Color.BLUE);
        paint.setTextSize(50);
    }
    public void setrunning(boolean isrunning)
    {
        this.isrunning = isrunning;
    }

    public void run()
    {
        Canvas _canvas;

        while (isrunning)
        {
            _canvas = null;

            try {
                _canvas = msurfaceHolder.lockCanvas(null);
                synchronized (msurfaceHolder)
                {
//                    mgameView.onDraw(_canvas);

                    if(_canvas!=null)
                    {
//                        _canvas.drawBitmap(bitmap,0,0,null);
                        _canvas.drawColor(Color.WHITE);
                        _canvas.drawText(""+score,200,200,paint);


//                        for(int i = 0;i<4;i++)
//                        {
//                            if(matched[i])
//                            {
////                                rythmGameActivity.button_rotate(i+1);
//                                if(i==0)
//                                    rythmGameActivity.button_rotate1();
//                                else if(i==1)
//                                    rythmGameActivity.button_rotate2();
//                                else if(i==2)
//                                    rythmGameActivity.button_rotate3();
//                                else if(i==3)
//                                    rythmGameActivity.button_rotate4();
//
//                                matched[i] = false;
//                            }
//                        }

//                        if(allmatched)
//                        {
//                            rythmGameActivity.button_rotate(1);
//                            rythmGameActivity.button_rotate(2);
//                            rythmGameActivity.button_rotate(3);
//                            rythmGameActivity.button_rotate(4);
//                            allmatched = false;
//                        }

                        if(mgameView.dataQueue != null)
                        {
                            for (int i = 0; i < mgameView.dataQueue.size(); i++)
                                mgameView.dataQueue.get(i).drawOnCanvas(_canvas);
                        }

                    }

                }

//                Thread.sleep(50);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(_canvas!=null)
                {
                    msurfaceHolder.unlockCanvasAndPost(_canvas);
                }
            }

        }



    }

}
