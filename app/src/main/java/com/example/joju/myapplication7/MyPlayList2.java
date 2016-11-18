package com.example.joju.myapplication7;

import android.content.SharedPreferences;
import android.view.WindowManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import  android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by joju on 2016. 8. 28..
 */
public class MyPlayList2 extends AppCompatActivity
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
//                Toast.makeText(MyPlayList2.this, "재생목록1에 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            else {
//                for (int i = 0; i < datas.size(); i++) {
//                    if (datas.get(i).getname().equals(data.getname())) {
//                        Toast.makeText(MyPlayList2.this, "이미 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        datas.add(data);
//                        Toast.makeText(MyPlayList2.this, "재생목록1에 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }
//
//            }
//        }

        setContentView(R.layout.my_playlist2);


        SharedPreferences pref = getSharedPreferences("music_app_data",MODE_PRIVATE);
        String str = pref.getString("playlist_2","nonono");

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
            Toast.makeText(MyPlayList2.this,"데이터 없음", Toast.LENGTH_SHORT).show();
        }






        listview = (ListView) findViewById(R.id.MylistView2);
        DataAdapter adapter = new DataAdapter(MyPlayList2.this, getLayoutInflater(), datas);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(listener);


    }


    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            Intent intent1 = new Intent(MyPlayList2.this, MusicPlayingActivity.class);
            intent1.putExtra("current_music", datas.get(position));
            intent1.putExtra("current_music_num", position);
            intent1.putExtra("current_datas", datas);
            startActivity(intent1);
//
        }
    };


}
