package com.hha.instagram_integration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.hha.instagram_integration.R;

public class Login extends Activity {

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InstagramManager manager = InstagramManager.getInstance();
        String authRequest = manager.urlStringForAuthenticationRequest();

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.setWebViewClient (new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlString) {

                System.out.println(urlString);
                if (urlString.contains("#access_token=")) {
                    String[] arrURL = urlString.split("#");

                    settings = getSharedPreferences(getResources().getString(R.string.pref_name), 0);
                    editor = settings.edit();

                    //to set the token value
                    String[] tokens = arrURL[1].split("=");

                    editor.putString("access_token", tokens[1]);
                    editor.commit();
                    InstagramManager manager = InstagramManager.getInstance();
                    manager.setAccessToken(tokens[1]);

                    show_photo_view();
                    return true;
                }
                return false;
            }
        });
        myWebView.loadUrl(authRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_instragram_login, menu);
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

    private void show_photo_view(){
        Intent intent = new Intent(Login.this, Photo.class);
        startActivity(intent);
        finish();
    }
}
