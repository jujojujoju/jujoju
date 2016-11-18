package com.example.joju.myapplication7;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by joju on 2016. 8. 25..
 */
public class MusicChartActivity extends AppCompatActivity
{

    Animation animation_button2;

        boolean add_action_clicked = false;
        boolean remove_action_clicked = false;
        boolean changing_password = false;
        boolean removing = false;

    LinearLayout list_layout;
    LinearLayout list_change_password;
    LinearLayout add_music_layout;

    ArrayList<DataClass> datas= new ArrayList<>();

    ListView listview;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicchart);



        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null)                                              //////공유 기능으로 추가
        {
            Uri musicUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (musicUri != null)
            {

                String[] proj = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM_ID };
                Cursor cursor = getContentResolver().query(musicUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int column_index_artistname = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int column_index_musicname = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int column_index_albumID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);


                cursor.moveToFirst();

                String musicID = cursor.getString(column_index);
                String artistname = cursor.getString(column_index_artistname);
                String musicname = cursor.getString(column_index_musicname);
                String musicalbumID = cursor.getString(column_index_albumID);

                //  datas.add(new DataClass(musicID,musicname,artistname,R.mipmap.ic_launcher));
                datas.add(new DataClass(musicID,musicname,artistname,musicalbumID));

            }
        }


        getMusicInfo();


//        datas.add(new DataClass("Stay Young","Oasis",R.drawable.music_image1));
//        datas.add(new DataClass("Love For A Child","Jason Mraz",R.drawable.music_image2));
//        datas.add(new DataClass("One Love","MC The Max",R.drawable.music_image3));
//        datas.add(new DataClass("She Will Be Loved","Maroon5",R.drawable.music_image4));


        list_layout = (LinearLayout)findViewById(R.id.list_layout);
        list_change_password = (LinearLayout)findViewById(R.id.list_change_password);
        add_music_layout = (LinearLayout)findViewById(R.id.add_music_layout);


        Button button_mylist1 = (Button)findViewById(R.id.button_mylist1);
        Button button_mylist2 = (Button)findViewById(R.id.button_mylist2);
        Button button_recentlist = (Button)findViewById(R.id.button_recentlist);
        Button button_addmusic = (Button)findViewById(R.id.button_addmusic);



        animation_button2 = AnimationUtils.loadAnimation(MusicChartActivity.this,R.anim.animation_button2);

        View.OnClickListener buttonlistener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(R.id.button_mylist1==view.getId())
                {
                    view.startAnimation(animation_button2);
                    DataClass data = new DataClass(null,"null","null",null);
                    Intent intent = new Intent(MusicChartActivity.this, MyPlayList1.class);
                    intent.putExtra("added_music",data);
                    startActivity(intent);
                }
                else if(R.id.button_mylist2==view.getId())
                {
                    view.startAnimation(animation_button2);
                    DataClass data = new DataClass(null,"null","null",null);
                    Intent intent = new Intent(MusicChartActivity.this, MyPlayList2.class);
                    intent.putExtra("added_music",data);
                    startActivity(intent);

                }
                else if(R.id.button_recentlist==view.getId())
                {
                    view.startAnimation(animation_button2);
                    DataClass data = new DataClass(null,"null","null",null);
                    Intent intent = new Intent(MusicChartActivity.this, RecentPlayedList.class);
                    intent.putExtra("added_music",data);
                    startActivity(intent);
                }
                else if(R.id.button_addmusic==view.getId())
                {

                    view.startAnimation(animation_button2);

                    EditText editText_addingname = (EditText)findViewById(R.id.editText_adding_name);
                    String str_name = editText_addingname.getText().toString();

                    EditText editText_adding_artist = (EditText)findViewById(R.id.editText_adding_artist);
                    String str_artist = editText_adding_artist.getText().toString();

                    String[] proj = {MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.ALBUM_ID,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.ARTIST
                    };


                    Cursor musicCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

                    if (musicCursor != null && musicCursor.moveToFirst()){
                        String musicID;
                        String albumID;
                        String musicTitle;
                        String singer;

                        int musicIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                        int albumIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                        int musicTitleCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                        int singerCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

                        do {
                            musicID = musicCursor.getString(musicIDCol);
                            albumID = musicCursor.getString(albumIDCol);
                            musicTitle = musicCursor.getString(musicTitleCol);
                            singer = musicCursor.getString(singerCol);

                            if(str_artist.equals(singer) && str_name.equals(musicTitle))
                            {
                                Toast.makeText(MusicChartActivity.this,"추가 되었습니다.", Toast.LENGTH_SHORT).show();
                                datas.add(0,new DataClass(musicID, musicTitle, singer, albumID));
                                ((DataAdapter) listview.getAdapter()).notifyDataSetChanged();
                                break;
                            }
                            else if(musicCursor.isLast())
                            {
                                Toast.makeText(MusicChartActivity.this,"찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }while (musicCursor.moveToNext());
                    }

                }

            }       //onclick
        };          //onclicklistener


        button_mylist1.setOnClickListener(buttonlistener);
        button_mylist2.setOnClickListener(buttonlistener);
        button_recentlist.setOnClickListener(buttonlistener);
        button_addmusic.setOnClickListener(buttonlistener);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.add_music_action)
        {

            if(add_action_clicked == false)
            {
                item.setTitle("취소");

                list_layout.setVisibility(View.GONE);
                add_music_layout.setVisibility(View.VISIBLE);

                add_action_clicked = true;

                return true;

            }
            else if(add_action_clicked == true)
            {
                item.setTitle("추가");

                list_layout.setVisibility(View.VISIBLE);
                add_music_layout.setVisibility(View.GONE);

                add_action_clicked = false;

                return true;
            }

        }
        else if (id == R.id.remove_music_action)
        {
            if(remove_action_clicked == false) {
                removing = true;
                item.setTitle("취소");
                Toast.makeText(this, "삭제하세요", Toast.LENGTH_SHORT).show();

                list_layout.setVisibility(View.GONE);
                list_change_password.setVisibility(View.GONE);
                add_music_layout.setVisibility(View.GONE);

                remove_action_clicked = true;
            }else if(remove_action_clicked == true)
            {
                removing = false;

                item.setTitle("삭제");

                list_layout.setVisibility(View.VISIBLE);
                list_change_password.setVisibility(View.GONE);
                add_music_layout.setVisibility(View.GONE);

                remove_action_clicked = false;
            }


        }
        else if (id == R.id.change_password)
        {
            if(changing_password == false) {
                list_layout.setVisibility(View.GONE);
                list_change_password.setVisibility(View.VISIBLE);

                changing_password = true;
            }
            else if(changing_password == true) {
                list_layout.setVisibility(View.VISIBLE);
                list_change_password.setVisibility(View.GONE);

                changing_password = false;
            }

        }


        return super.onOptionsItemSelected(item);
    }


    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
//
            if(removing == false) {
                Intent intent1 = new Intent(MusicChartActivity.this, MusicPlayingActivity.class);
                intent1.putExtra("current_music", datas.get(position));
                intent1.putExtra("current_music_num", position);
                intent1.putExtra("current_datas", datas);
                startActivity(intent1);

            }

            else if(removing == true)
            {

            for(int i=datas.size()-1 ; i >= 0 ; i--)
            {
                if(i == position) {
                    datas.remove(i);
                    ((DataAdapter) parent.getAdapter()).notifyDataSetChanged();

                }
            }

            }

        }
    };


    public void getMusicInfo()
    {

        String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        int []random = new int[10];

        for(int k = 0; k<10;k++)
        random [k] = (int)(Math.random()*1000)+1;

        int i = 0;

        Cursor musicCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()){
            String musicID;
            String albumID;
            String musicTitle;
            String singer;

            int musicIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int albumIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int musicTitleCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int singerCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                musicID = musicCursor.getString(musicIDCol);
                albumID = musicCursor.getString(albumIDCol);
                musicTitle = musicCursor.getString(musicTitleCol);
                singer = musicCursor.getString(singerCol);

                for(int k = 0; k<10;k++)
                {
                    if(random[k]==Integer.parseInt(musicID))
                    {
                        datas.add(new DataClass(musicID,musicTitle,singer,albumID));
                        i++;
                        break;
                    }
                }
            }while (musicCursor.moveToNext()&& i<10);
        }
       // musicCursor.close();
        return;
    }


    public void onclick(View view) {

        EditText editText = (EditText)findViewById(R.id.editText);
        String resultpassword = editText.getText().toString();

        if(!resultpassword.equals(""))
        {
            Intent ResultData = getIntent();
            ResultData.putExtra("변경 비밀번호", resultpassword);
            setResult(1, ResultData);
            this.finish();
        }

    }

    protected void onStart()
    {
        super.onStart();
//        Toast.makeText(this, "main_start", LENGTH_SHORT).show();

    }


    protected void onRestart() {

        super.onRestart();
//        Toast.makeText(this, "main_restart", LENGTH_SHORT).show();


    }

    protected void onPause()
    {
        super.onPause();
//        Toast.makeText(this, "main_pause", LENGTH_SHORT).show();
    }

    protected void onStop()
    {
        super.onStop();
//        Toast.makeText(this, "main_stop", LENGTH_SHORT).show();


    }

    protected void onResume() {

        super.onResume();
//        Toast.makeText(this, "main_resume", LENGTH_SHORT).show();

        listview = (ListView)findViewById(R.id.chart_listView);
        final DataAdapter adapter = new DataAdapter(MusicChartActivity.this,getLayoutInflater(), datas);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(listener);


    }

    protected void onDestroy() {

        super.onDestroy();
//        Toast.makeText(this, "main_destroy", LENGTH_SHORT).show();

    }


}
