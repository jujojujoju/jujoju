package com.example.joju.myapplication7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.Math.abs;


/**
 * Created by joju on 2016. 9. 20..
 */
public class RythmGameActivity extends AppCompatActivity
{

    Clap clap;
    Check check;
    View.OnClickListener buttonlistener;
//    MediaPlayer mPlayer; // 음악 재생을 위한 객체
    MediaPlayer mPlayer2;


    JSONObject jObject = null;

    Bitmap bitmap;
    Bitmap getBitmap = null;

    GameView gameview;

    LinearLayout liner;

    int button_speed = 100;

    MenuItem item1;
    int max = 0;

    ArrayList<Integer> score_array = new ArrayList<>();

    String str;
    CustomDialog dlg;

    double lastLevel = 0;
    int bufferSize;
    AudioRecord audio;


    Animation animation_image;

    Animation animation_button;

    Animation animation_line1 ;
    Animation animation_line2 ;
    Animation animation_line3 ;
    Animation animation_line4 ;



    Button button1;
    Button button2;
    Button button3;
    Button button4;


    TextView linetext1;
    TextView linetext2;
    TextView linetext3;
    TextView linetext4;


    ImageView  imageView1;
    ImageView  imageView2;
    ImageView imageView3;
    ImageView  imageView4;


    FrameLayout frame;

    boolean clapmode = false;


    View.OnClickListener okListener = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "OK버튼 클릭", Toast.LENGTH_SHORT).show();

            dlg.dismiss();
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "cancel버튼 클릭", Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_score, menu);
        item1 = menu.findItem(R.id.current_score);
        item1.setTitle("최고점 : "+max);
//        Toast.makeText(this, "createoption", LENGTH_SHORT).show();

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.current_score)
        {
            dlg = new CustomDialog(RythmGameActivity.this,okListener,cancelListener);
            dlg.show();
        }
        else if (id == R.id.clap)
        {
            if(!clapmode)
            {
                item.setTitle("취소");
                clapmode = true;
//                Toast.makeText(this, "박수 모드 ON", LENGTH_SHORT).show();

                return true;
            }
            else if(clapmode)
            {
                item.setTitle("박수");
                clapmode = false;
//                Toast.makeText(this, "박수 모드 OFF", LENGTH_SHORT).show();
                return true;
            }

//            item.setTitle("ssss");
        }


        return super.onOptionsItemSelected(item);
    }

    public class CustomDialog extends Dialog
    {

        View.OnClickListener oklistenr;
        View.OnClickListener cancelListener;


        protected CustomDialog(Context context, View.OnClickListener oklistenr, View.OnClickListener cancelListener)
        {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.oklistenr = oklistenr;
            this.cancelListener = cancelListener;
        }

        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            getWindow().setAttributes(lpWindow);
            setContentView(R.layout.score_chart_dialog);


            Button button_ok = (Button)findViewById(R.id.button_ok);
//            Button button_cancel = (Button)findViewById(R.id.button_cancel);


            button_ok.setOnClickListener(oklistenr);
//            button_cancel.setOnClickListener(cancelListener);


            TextView maintext = (TextView)findViewById(R.id.maintext);
            TextView score1 = (TextView)findViewById(R.id.score1);
            TextView score2 = (TextView)findViewById(R.id.score2);
            TextView score3 = (TextView)findViewById(R.id.score3);
            TextView score4 = (TextView)findViewById(R.id.score4);
            TextView score5 = (TextView)findViewById(R.id.score5);


            maintext.setText("역대 스코어");
            score1.setText("-");
            score2.setText("-");
            score3.setText("-");
            score4.setText("-");
            score5.setText("-");


            for (int i = 0; i < 5; i++)
                score_array.add(0);

            if(!str.equals("nonono"))
            {
                for (int i = 0; i < jObject.length(); i++)
                {
                    try {
                        score_array.add(jObject.getInt("점수" + (i + 1)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(score_array,comparator);


                String[] strarray = new String[5];


                for (int i = 0; i < 5; i++)
                {
                    strarray[i] = "-";
                    if(0!=score_array.get(i))
                    strarray[i] = ""+score_array.get(i);
                }


                score1.setText("1 : "+strarray[0]);
                score2.setText("2 : "+strarray[1]);
                score3.setText("3 : "+strarray[2]);
                score4.setText("4 : "+strarray[3]);
                score5.setText("5 : "+strarray[4]);

            }

        }


    }


    final Comparator<Integer> comparator = new Comparator<Integer>()
    {
        @Override
        public int compare(Integer integer, Integer t1) {
            return (integer>t1)?-1:(integer > t1) ? 1:0 ;
        }
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rythm_game);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rythm_game);


        Intent intent = getIntent();
//        Bitmap getBitmap = (Bitmap)intent.getExtras().get("game_image");
        int musicid = Integer.parseInt(((DataClass)intent.getExtras().get("game_music")).getMusicID());
        final Uri musicURI = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + musicid);




//        mPlayer = MediaPlayer.create(RythmGameActivity.this,musicURI);
        gameview = new GameView(RythmGameActivity.this,this,musicURI);
//        bitmap = getBitmap;
//        gameview = (GameView)findViewById(R.id.gameview);

        frame = (FrameLayout)findViewById(R.id.gameview_frame);
        frame.addView(gameview,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gameview.setZOrderOnTop(false);


        SharedPreferences pref = getSharedPreferences("music_app_data", MODE_PRIVATE);
        str =  pref.getString("score_data","nonono");

        try
        {
            if(!str.equals("nonono"))
            {
                jObject = new JSONObject(str);
                for (int i = 0; i < jObject.length(); i++)
                {
                    if (max < jObject.getInt("점수" + (i + 1)))
                        max = jObject.getInt("점수" + (i + 1));
                }
            }
            else
            {
                Toast.makeText(RythmGameActivity.this,"최초데이터 없음",Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }



        linetext1 = (TextView)findViewById(R.id.linetext1);
        linetext2= (TextView)findViewById(R.id.linetext2);
        linetext3= (TextView)findViewById(R.id.linetext3);
        linetext4= (TextView)findViewById(R.id.linetext4);


        linetext1.setBackgroundColor(Color.argb(255, 130, 255, 255));
        linetext2.setBackgroundColor(Color.argb(255, 130, 255, 255));
        linetext3.setBackgroundColor(Color.argb(255, 130, 255, 255));
        linetext4.setBackgroundColor(Color.argb(255, 130, 255, 255));


        linetext1.setAlpha(0);
        linetext2.setAlpha(0);
        linetext3.setAlpha(0);
        linetext4.setAlpha(0);



        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button1.setAlpha(0);
        button2.setAlpha(0);
        button3.setAlpha(0);
        button4.setAlpha(0);


        bitmap  = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
//        bitmap = getBitmap;
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);

        imageView1.setImageBitmap(bitmap);
        imageView2.setImageBitmap(bitmap);
        imageView3.setImageBitmap(bitmap);
        imageView4.setImageBitmap(bitmap);



        liner = (LinearLayout)findViewById(R.id.liner);
//
        buttonThread bth = new buttonThread();
        bth.start();


        buttonlistener = new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view)
            {
//                gameview.clicked = true;

                    if (R.id.button1 == view.getId()) {
                        gameview.clicked_buttonNum = 1;
                        line_color_on(gameview.clicked_buttonNum);
                    } else if (R.id.button2 == view.getId()) {
                        gameview.clicked_buttonNum = 2;
                        line_color_on(gameview.clicked_buttonNum);
                    } else if (R.id.button3 == view.getId()) {
                        gameview.clicked_buttonNum = 3;
                        line_color_on(gameview.clicked_buttonNum);
                    } else if (R.id.button4 == view.getId()) {
                        gameview.clicked_buttonNum = 4;
                        line_color_on(gameview.clicked_buttonNum);
                    }


                gameview.line_top = liner.getTop();
                gameview.line_bottom = liner.getBottom();
            }
        };


        button1.setOnClickListener(buttonlistener);
        button2.setOnClickListener(buttonlistener);
        button3.setOnClickListener(buttonlistener);
        button4.setOnClickListener(buttonlistener);





        animation_image = AnimationUtils.loadAnimation(RythmGameActivity.this,R.anim.animation_bitmap);

//        animation_button = AnimationUtils.loadAnimation(RythmGameActivity.this,R.anim.animation);
        animation_line1 = AnimationUtils.loadAnimation(RythmGameActivity.this,R.anim.animation_linetext);
        animation_line2 = AnimationUtils.loadAnimation(RythmGameActivity.this,R.anim.animation_linetext);
        animation_line3 = AnimationUtils.loadAnimation(RythmGameActivity.this,R.anim.animation_linetext);
        animation_line4 = AnimationUtils.loadAnimation(RythmGameActivity.this,R.anim.animation_linetext);


        int sampleRate = 8000;
        try {
            bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        } catch (Exception e) {
            android.util.Log.e("TrackingFlow", "Exception", e);
        }
        audio.startRecording();

        check = new Check();
        check.isplaying = true;
        check.start();

        clap = new Clap();
        clap.isplaying = true;
        clap.start();
//        Toast.makeText(getApplicationContext(), "oncreate", Toast.LENGTH_SHORT).show();
    }


    class Clap extends Thread {

        boolean isplaying = false;
        Handler han = new Handler(Looper.getMainLooper())
        {
            public synchronized void handleMessage(Message msg)
            {
                if (clapmode == true && lastLevel > 70)
                {
//                    Toast.makeText(getApplicationContext(), "assss", Toast.LENGTH_SHORT).show();
                    buttonlistener.onClick(button1);
                    buttonlistener.onClick(button2);
                    buttonlistener.onClick(button3);
                    buttonlistener.onClick(button4);

                }

            }
        };
        public void run()
        {
            while (isplaying)
            {
//                Toast.makeText(getApplicationContext(), "aaaaa", Toast.LENGTH_SHORT).show();
                readAudioBuffer();
                han.sendEmptyMessage(0);
            }
        }

    }

    class Check extends Thread
    {

        boolean isplaying = false;
        Handler han = new Handler(Looper.getMainLooper())
        {
            public synchronized void handleMessage(Message msg)
            {

//
                if (animation_line1.hasEnded())
                    linetext1.setAlpha(0);

                if (animation_line2.hasEnded())
                    linetext2.setAlpha(0);

                if (animation_line3.hasEnded())
                    linetext3.setAlpha(0);

                if (animation_line4.hasEnded())
                    linetext4.setAlpha(0);


            }
        };
        public void run()
        {
            while (isplaying)
            {
//                readAudioBuffer();
                han.sendEmptyMessage(0);
            }
        }
    }



    private void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];

            int bufferReadResult = 1;

            if (audio != null) {

// Sense the voice…
                bufferReadResult = audio.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++)
                {
                    sumLevel += buffer[i];
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }
        }
        catch (Exception e)
        { e.printStackTrace(); }
    }


    public synchronized void line_color_on(int _clicked_buttonNum)
    {

        if (_clicked_buttonNum == 1)
        {
            linetext1.setAlpha((float)0.6);
            linetext1.startAnimation(animation_line1);
        }

        if (_clicked_buttonNum == 2) {
            linetext2.setAlpha((float)0.6);
            linetext2.startAnimation(animation_line2);
        }

        if (_clicked_buttonNum == 3) {
            linetext3.setAlpha((float)0.6);
            linetext3.startAnimation(animation_line3);
        }

        if (_clicked_buttonNum == 4)
        {
            linetext4.setAlpha((float)0.6);
            linetext4.startAnimation(animation_line4);
        }
    }

    public void button_rotate1()
    {
//        Toast.makeText(getApplicationContext(), 1+"", Toast.LENGTH_SHORT).show();
        imageView1.startAnimation(animation_image);
    }
    public void button_rotate2()
    {
//        Toast.makeText(getApplicationContext(), 2+"", Toast.LENGTH_SHORT).show();
        imageView2.startAnimation(animation_image);
    }
    public void button_rotate3()
    {
//        Toast.makeText(getApplicationContext(), 3+"", Toast.LENGTH_SHORT).show();
        imageView3.startAnimation(animation_image);
    }
    public void button_rotate4()
    {
//        Toast.makeText(getApplicationContext(), 4+"", Toast.LENGTH_SHORT).show();
        imageView4.startAnimation(animation_image);
    }

    public void button_rotate(int _clicked_buttonNum)
    {
        if(_clicked_buttonNum == 1) {
//            button1.startAnimation(animation_button);
            Toast.makeText(getApplicationContext(), _clicked_buttonNum+"", Toast.LENGTH_SHORT).show();
            imageView1.startAnimation(animation_image);
        }
         if(_clicked_buttonNum == 2) {
//            button2.startAnimation(animation_button);
             Toast.makeText(getApplicationContext(), _clicked_buttonNum+"", Toast.LENGTH_SHORT).show();
            imageView2.startAnimation(animation_image);
        }
         if(_clicked_buttonNum == 3) {
//            button3.startAnimation(animation_button);
             Toast.makeText(getApplicationContext(), _clicked_buttonNum+"", Toast.LENGTH_SHORT).show();
            imageView3.startAnimation(animation_image);
        }
         if(_clicked_buttonNum == 4)
        {
//            button4.startAnimation(animation_button);
            Toast.makeText(getApplicationContext(), _clicked_buttonNum+"", Toast.LENGTH_SHORT).show();
            imageView4.startAnimation(animation_image);
        }

    }


    public class buttonThread extends Thread
    {

        public synchronized void run()
        {

            while (true)
            {
                if(gameview.clicked_buttonNum!=-1)
                {
                    try {
                        Thread.sleep(button_speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    gameview.clicked_buttonNum = -1;
                }

            }//
        }
    }


    public class ThreadTask extends AsyncTask<String,String,String >
    {

        @Override
        protected String doInBackground(String... strings)
        {

            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                readAudioBuffer();
                if (lastLevel > 200) {
                    buttonlistener.onClick(button1);

                }

//                return null;
            }
        }
    }

    public class delayed_music extends Thread
    {
        public void run()
        {
            try {
                Thread.sleep(2800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mPlayer2.start();
        }
    }

    protected void onPause()
    {
        super.onPause();


        try {
            if(jObject == null)
            {
                jObject = new JSONObject();
                jObject.put("점수" + 1, gameview.getscore());
            }

            if(gameview.getscore() != 0)
                jObject.put("점수"+(jObject.length()+1),gameview.getscore());

        } catch (JSONException e) {
            e.printStackTrace();

        }


        SharedPreferences pref = getSharedPreferences("music_app_data",MODE_PRIVATE);
        SharedPreferences.Editor editer = pref.edit();
        editer.putString("score_data",jObject.toString());
        editer.commit();

//        Toast.makeText(this, "main_pause", LENGTH_SHORT).show();

    }


    protected void onStop()
    {
        super.onStop();

        check.isplaying = false;
        clap.isplaying = false;
//        mPlayer.stop();
//        mPlayer2.stop();
//        Toast.makeText(this, "main_stop", LENGTH_SHORT).show();
    }

    protected void onDestroy() {

        super.onDestroy();



        // mPlayer.pause();
        //  mPlayer.release();


//        Toast.makeText(this, "rythmgame_destroy", LENGTH_SHORT).show();

    }

}
