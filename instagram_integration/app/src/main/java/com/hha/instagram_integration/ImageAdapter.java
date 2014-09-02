/**
 * Created by HhA on 2/9/14.
 */

package com.hha.instagram_integration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter
{
    private Context context;
    ArrayList<String> urls = null;
    int layoutResourceId;
    ImageHolder holder = null;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context,   int layoutResourceId, ArrayList<String> urls) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.urls = urls;

        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View cell_container = convertView;
        ImageHolder holder;

        if (cell_container == null){

            cell_container = layoutInflater.inflate(layoutResourceId,  parent, false);

            holder = new ImageHolder();

            holder.image = (ImageView) cell_container.findViewById(R.id.img_photo);

            cell_container.setTag(holder);
        }
        else{
            holder = (ImageHolder)cell_container.getTag();
        }

        Resources res = context.getResources();
        Drawable drawable = res.getDrawable( R.drawable.loading );
        holder.image .setImageDrawable( drawable );

        if (holder.image != null) {

            ImageDownloaderTask imgDownloader = new ImageDownloaderTask(holder.image);
            imgDownloader.execute(urls.get(position));
        }

        return cell_container;
    }

    static class ImageHolder {
        ImageView image;
    }

}