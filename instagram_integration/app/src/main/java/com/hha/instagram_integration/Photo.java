package com.hha.instagram_integration;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.hha.instagram_integration.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Photo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getPhotosList();
    }

    private void getPhotosList(){
        InstagramManager manager = InstagramManager.getInstance();
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


    public class Download_Image extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();

            try {
                String url = urlStringForRecentMediaRequest().toString();
                HttpGet httpget = new HttpGet(url);
                HttpResponse response;
                JSONArray imageArray = new JSONArray();

                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);// method to convert stream to String
                    JSONObject object = new JSONObject(new JSONTokener(result));

                    JSONArray mediaArray = object.getJSONArray("data");
                    for (int i=0;i<mediaArray.length();i++) {
                        JSONObject jObj = mediaArray.getJSONObject(i);
                        if (jObj.get("type").equals("image")){
                            imageArray.put(jObj);
                        }
                    }
                    System.out.println(imageArray);
                    instream.close();
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return "";
//            return imageArray;
        }
        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
