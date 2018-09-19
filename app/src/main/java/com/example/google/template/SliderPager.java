package com.example.google.template;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 28-11-2017.
 */

public class SliderPager extends PagerAdapter {
    int value;
    private Context context;
    private LayoutInflater layoutInflater;
    File[] files;
    ArrayList<Bitmap> bitmapImages = new ArrayList();
    DatabaseHelper myDb;
    SQLiteDatabase db;
    String UserId = new applicationClass().setUserId();

    //List<Integer> images = new ArrayList<Integer>();

    private Integer[] images;
    //private Integer[] images = {0,1,2,3,4,5,6,7,8,9,10};

    /*public SliderPager(Context context,String User_Id) {
        this.context = context;
        //UserId = User_Id;
        //myDb = new DatabaseHelper(context);


    }
*/

    public  SliderPager(){

    }

    public SliderPager(Context context,ArrayList<Bitmap> bitmapImages) {
        this.context = context;
        this.bitmapImages = bitmapImages;
        //UserId = User_Id;
        //myDb = new DatabaseHelper(context);


    }


    public int imagesize(){
        value = 1;
        try {
            myDb = new DatabaseHelper(new applicationClass().getContext());

            db = myDb.getReadableDatabase();
            if(!db.isDbLockedByCurrentThread()){


            String UserGroupQuery = "Select COUNT(Site_Location_Id) from site_imagelist WHERE Site_Location_Id ='"+myDb.Site_IdUserSiteLinking(UserId)+"' AND Record_Status !='D'";
            Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
            if (cursor1.moveToFirst()) {
                do {
                    value=cursor1.getInt(0);
                } while (cursor1.moveToNext());
            }

           /* cursor1.moveToFirst();
            value = cursor1.getInt(0);*/
            cursor1.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(value== 0){
            images = new Integer[1];
        }else {
            images = new Integer[value];
        }
        return value;
    }

    @Override
    public int getCount() {

        imagesize();
        return images.length;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, null);

try {


    if(bitmapImages.size() == 0){
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.applogo);


    }else {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageBitmap(bitmapImages.get(position));
    }


        //imageView.setImageResource(images[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
}catch (Exception e){
    e.printStackTrace();
}
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }




}
