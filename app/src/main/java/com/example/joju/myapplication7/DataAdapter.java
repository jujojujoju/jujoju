package com.example.joju.myapplication7;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joju on 2016. 8. 28..
 */
public class DataAdapter extends BaseAdapter
{

    //Context context;

    Activity activity;

    ArrayList<DataClass> datas;

    LayoutInflater inflater;

    public DataAdapter(Activity activity, LayoutInflater inflater,ArrayList<DataClass> datas)
    {
        this.inflater = inflater;

        this.activity = activity;
        this.datas = datas;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {  return i;    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup_parent)
    {

        if(convertview == null)
            convertview = inflater.inflate(R.layout.list_row,null);


        TextView textView_music_1 = (TextView)convertview.findViewById(R.id.textView_music_1);

        TextView textView_music_artist= (TextView)convertview.findViewById(R.id.textView_music_artist);

        ImageView imageView= (ImageView)convertview.findViewById(R.id.imageView);



        textView_music_1.setText(datas.get(i).getname());

        textView_music_artist.setText(datas.get(i).getArtist());


        Bitmap albumImage = getAlbumImage(activity, Integer.parseInt((datas.get(i)).getImgId()), 170);
        imageView.setImageBitmap(albumImage);

        // imageView.setImageResource(datas.get(i).getImgId());


        return convertview;
    }

    private static final BitmapFactory.Options options = new BitmapFactory.Options();


    private static Bitmap getAlbumImage(Context context, int album_id, int MAX_IMAGE_SIZE) {

        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse("content://media/external/audio/albumart/" + album_id);
        if (uri != null) {
            ParcelFileDescriptor fd = null;
            try {
                fd = res.openFileDescriptor(uri, "r");

                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);
                int scale = 0;
                if (options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE) {
                    scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_IMAGE_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;

                Bitmap b = BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);

                if (b != null) {
                    // finally rescale to exactly the size we need
                    if (options.outWidth != MAX_IMAGE_SIZE || options.outHeight != MAX_IMAGE_SIZE) {
                        Bitmap tmp = Bitmap.createScaledBitmap(b, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, true);
                        b.recycle();
                        b = tmp;
                    }
                }

                return b;
            } catch (FileNotFoundException e) {
            } finally {
                try {
                    if (fd != null)
                        fd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
