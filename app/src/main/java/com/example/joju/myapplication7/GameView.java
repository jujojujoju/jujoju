package com.example.joju.myapplication7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by joju on 2016. 9. 27..
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    int score = 0;
    int note_speed = 70;
    Bitmap  bitmap;
//    MainActivity mainActivity;/**/
    RythmGameActivity rythmGameActivity;

    boolean clicked = false;

    static  public ArrayList<Data> dataQueue = new ArrayList<>();

    private SurfaceThread surfacethread;

    MediaPlayer mPlayer;
    MediaPlayer mPlayer2;

    int subcount = 0;

    double wk=6.283185307 / 1024;

    Visualizer mVisualizer;

    int clicked_buttonNum = -1;

    float line_bottom;
    float line_top;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public GameView(final Context context, RythmGameActivity rythmGameActivity, Uri _musicuri)
    {
        super(context);


//        bitmap = _bitmap;
        this.rythmGameActivity = rythmGameActivity;
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
//        paint.setStrokeWidth(5);
//        paint.setStyle(Paint.Style.FILL);



        mPlayer = MediaPlayer.create(context,_musicuri);
        mPlayer2 = MediaPlayer.create(context,_musicuri);



        mPlayer.setVolume((float)0.01, (float)0.01);
        mPlayer.start();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(context,"asdfasdfasdfasdf",Toast.LENGTH_SHORT).show();
                mPlayer2.setVolume(10,10);
                mPlayer2.start();

            }
        });

        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
        {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate)
            {
                int postheight = -128;
                int count = 0;

//                for (int j=0; j<1024; j++)
//                    bytes[j] = (byte) (bytes[j] * (0.54 - 0.46 * Math.cos(j * wk)));

                for(int i = 0;i<1024;i++)
                {
                    if(abs(bytes[i] - postheight)>100)
                        count++;
                    postheight =  bytes[i];
                }

                if(abs(count - subcount)>20)
                {
                    if(count>80)
                    {
                        node thnode = new node(1);
                        thnode.start();
                    }
                    else if(count>50)
                    {
                        node thnode = new node(2);
                        thnode.start();
                    }
                    else if(count>20)
                    {
                        node thnode = new node(3);
                        thnode.start();
                    }
                    if(count>10)
                    {
                        node thnode = new node(4);
                        thnode.start();
                    }
                }
                subcount = count;

            }
            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate)
            {            }
        };


        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate() / 4,true,false);
        mVisualizer.setEnabled(true);


        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        //Your code
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(this);
        surfacethread = new SurfaceThread(getHolder(),this,rythmGameActivity);
        setFocusable(true); // make sure we get key events
        //do stuff that was in your original constructor...
    }


    class node extends Thread
    {


        int x = 0;
        float y = 10;
        boolean isrunning = false;

        Data data = new Data(paint,getContext());

        public node(int xx)
        {

            isrunning = true;
            x = xx;

            if(x == 1)
                data.set_x((float)30);
            else if(x ==2)
                data.set_x((float)140);
            else if(x == 3)
                data.set_x((float)270);
            else if(x ==4)
                data.set_x((float)390);


            data.set_y(y);

            dataQueue.add(data);
        }


        public synchronized  void run()
        {

            try
            {
                while(isrunning)
                {

                    y = y+40;
                    data.set_y(y);


                    if(y > 700)
                    {
                        isrunning = false;
                        dataQueue.remove(dataQueue.indexOf(data));//
                    }

                    if( x == clicked_buttonNum && y>line_top-50)
                    {

                        score = score+10;
                        surfacethread.toss_score(score);
                        isrunning = false;
//                        han.sendEmptyMessage(0);


                        surfacethread.matched[clicked_buttonNum-1] = true;

//                        if(rythmGameActivity.clapmode == true && surfacethread.matched)
//                            surfacethread.allmatched = true;

                        dataQueue.remove(dataQueue.indexOf(data));

                    }


                    try {
                        Thread.sleep(note_speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            catch (Exception e)
            {
//                Toast.makeText(MainActivity.this, "스레드 종료", LENGTH_SHORT).show();
            }

        }
    }

    public int getscore()
    {
        return score;
    }

    public void onDraw(Canvas canvas)
    {



    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
        surfacethread.setrunning(true);
        surfacethread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {

        boolean retry = true;
        surfacethread.setrunning(false);

        mPlayer.stop();
        mPlayer2.stop();

        while(retry)
        {
            try {
                surfacethread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
