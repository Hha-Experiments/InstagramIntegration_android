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

public class MyActivity extends Activity {

    private Button loginButton;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        //pref name to set the catche value
        settings = getSharedPreferences( getResources().getString(R.string.pref_name), 0);
        editor = settings.edit();
        InstagramManager manager = InstagramManager.getInstance();
        //to set the token value for instragram login
        //editor.putString("login_token", "");
        //editor.commit();

        String token = settings.getString("access_token", "");
        if (token.trim().length()>0) //already login
        {
            //redirect to photo_list view
            manager.setAccessToken(token);
            show_photo_view();
        }

        //set up UI components
        setupUIComponents(this);
    }

    private void setupUIComponents(Context context){

        //find login button to set on click event
        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(login_button_click_listener);
    }


    //onclick listener for login button
    private View.OnClickListener login_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            //redirect to login view
            show_login_view();
        }
    };


    //call to login view
    private void show_login_view(){
        Intent intent = new Intent(MyActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void show_photo_view(){
        Intent intent = new Intent(MyActivity.this, Photo.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
}
