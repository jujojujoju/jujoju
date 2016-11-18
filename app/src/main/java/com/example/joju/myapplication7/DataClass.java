package com.example.joju.myapplication7;

import java.io.Serializable;

/**
 * Created by joju on 2016. 8. 28..
 */
public class DataClass implements Serializable
{

    String musicID;
    String name;    //이름 저장
    String artist;      ////가수 이름
    String imgId;          ///이미지 변수


public DataClass(String musicID, String name, String artist, String imgId)
    {
        this.musicID = musicID;
        this.name = name;
        this.artist = artist;
        this.imgId = imgId;
    }

    public String getMusicID()
    {
        return this.musicID;
    }



    public String getname()
    {
        return this.name;
    }

    public String getArtist()
    {
        return this.artist;
    }

    public String getImgId()
    {
        return this.imgId;
    }

}
