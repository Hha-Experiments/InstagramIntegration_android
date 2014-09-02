package com.hha.instagram_integration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import java.util.ArrayList;

public class Photo extends Activity {

    InstagramManager manager = InstagramManager.getInstance();

    private static GridView gridView;
    private static Context context;

    private Button logoutButton;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        settings = getSharedPreferences(Constants.PREF_NAME, 0);
        editor = settings.edit();

        logoutButton = (Button) findViewById(R.id.btnLogout);

        gridView = (GridView)findViewById(R.id.gridview);
        context = getBaseContext();

        getPhotosList();
    }

    private void getPhotosList(){

        manager.getInstagramImages();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_photo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void showImage(ArrayList<String> arrImage){
        System.out.println(arrImage);
        ImageAdapter adapter = new ImageAdapter(context,arrImage);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed () {

    }

    public void logoutButtonAction(View v) {

        manager.deleteInstagramCookies();
        editor.remove("access_token");
        editor.commit();

        show_main_view();
    }

    private void show_main_view(){
        Intent intent = new Intent(Photo.this, MyActivity.class);
        startActivity(intent);
        finish();
    }
}
