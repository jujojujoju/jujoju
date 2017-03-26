package com.example.joju.myapplication7;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import static android.widget.Toast.LENGTH_SHORT;


import java.util.ArrayList;

/**
* Created by joju on 2016. 8. 29..
        */

public class MyPlayList1 extends AppCompatActivity
{

     ArrayList<DataClass> datas= new ArrayList<>();

    DataClass data;

    ListView listview;


    JSONArray recent_array;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        Intent intent = getIntent();
//        data = (DataClass) intent.getExtras().get("added_music");
//
//
//        if (!data.getname().equals("null"))
//        {
//
//            if(datas.size() == 0)
//            {
//                datas.add(data);
//                Toast.makeText(MyPlayList1.this, "재생목록1에 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            else {
//                for (int i = 0; i < datas.size(); i++) {
//                    if (datas.get(i).getname().equals(data.getname())) {
//                        Toast.makeText(MyPlayList1.this, "이미 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        datas.add(data);
//                        Toast.makeText(MyPlayList1.this, "재생목록1에 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }
//
//            }
//        }

        setContentView(R.layout.my_playlist1);

        SharedPreferences pref = getSharedPreferences("music_app_data",MODE_PRIVATE);
        String str = pref.getString("playlist_1","nonono");

        try
        {
            recent_array = new JSONArray(str);

            String musicID;
            String name;    //이름 저장
            String artist;      ////가수 이름
            String imgId;          ///이미지 변수

            if(!str.equals("nonono"))
            {

                for (int i = 0; i < recent_array.length(); i++)
                {

                    musicID = recent_array.getJSONObject(i).getString("musicID");
                    name = recent_array.getJSONObject(i).getString("name");
                    artist = recent_array.getJSONObject(i).getString("artist");
                    imgId = recent_array.getJSONObject(i).getString("imgId");

                    int j;
                    for (j = 0; j < datas.size(); j++)
                    {
                        if(datas.get(j).getMusicID() == musicID)
                            break;
                    }
                    if(j==datas.size())

                    datas.add(0,new DataClass(musicID,name,artist,imgId));

                }

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(MyPlayList1.this,"데이터 없음", Toast.LENGTH_SHORT).show();
        }



        listview = (ListView) findViewById(R.id.MylistView1);
            DataAdapter adapter = new DataAdapter(MyPlayList1.this, getLayoutInflater(), datas);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(listener);


//        listview.setOnLongClickListener(longlistener);

    }

    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            Intent intent1 = new Intent(MyPlayList1.this, MusicPlayingActivity.class);
            intent1.putExtra("current_music", datas.get(position));
            intent1.putExtra("current_music_num", position);
            intent1.putExtra("current_datas", datas);
            startActivity(intent1);
//
        }
    };



}
