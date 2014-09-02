/**
 * Created by HhA on 2/9/14.
 */

package com.hha.instagram_integration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter
{
    private Context context;
    ArrayList<String> urls = null;

    public ImageAdapter(Context context,  ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    private void displayImage(String url, ImageView imgImageView) {
        if (url.length() > 0) {
            Drawable drawable = LoadImageFromWebOperations(url);
            imgImageView.setImageDrawable(drawable);
        }
    }

    private Drawable LoadImageFromWebOperations(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        displayImage(urls.get(position), imageView);
        return imageView;
    }
}

