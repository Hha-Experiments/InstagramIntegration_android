/**
 * Created by HhA on 1/9/14.
 */

package com.hha.instagram_integration;

import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.webkit.CookieManager;

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
import java.util.ArrayList;

class InstagramManager {


    private static InstagramManager manager;
    private String access_token;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    private InstagramManager() {

    }

    public static InstagramManager getInstance() {

        if (manager == null) {

            synchronized (InstagramManager.class) {

                if (manager == null) {

                    manager = new InstagramManager();
                }
            }
        }
        return manager;
    }

    public void setAccessToken(String token){

        this.access_token = token;
    }

    public String getAccessToken () { return this.access_token; }


    public String getInstagramCookies() {

        CookieManager cookieMgr =  CookieManager.getInstance();
        String cookies = null;
        if (cookieMgr.hasCookies())
        {
            cookies = cookieMgr.getCookie("https://instagram.com/");
        }

        return cookies;
    }

    public void deleteInstagramCookies() {

        CookieManager cookieMgr =  CookieManager.getInstance();
        if (cookieMgr.hasCookies()) {

            cookieMgr.removeAllCookie();
        }
    }

    public void getInstagramImages(){

        JSONArray object = null;
        try {
            Get_Images get_images = new Get_Images();
            get_images.execute("");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String urlStringForAuthenticationRequest(){

        String apiBaseUrl = Constants.BASE_URL;
        String authenticate = Constants.AUTHENTICATE;
        String clientId = Constants.CLIENT_ID;
        String redirectUri = Constants.REDIRECT_URI;

        String strUrl = apiBaseUrl + authenticate + "client_id=" + clientId + "&redirect_uri=" + redirectUri;
        return strUrl;
    }

    private String urlStringForUserInfoRequest() {

        String apiBaseUrl = Constants.API_BASE_URL;
        String selfInfo = Constants.SELF_INFO;
        String clientId = Constants.CLIENT_ID;
        String redirectUri = Constants.REDIRECT_URI;

        String strUrl = apiBaseUrl + selfInfo + "access_token=" + this.access_token;
        return strUrl;
    }

    private String urlStringForRecentMediaRequest() {

        String apiBaseUrl = Constants.API_BASE_URL;
        String selfMedia = Constants.SELF_MEDIA;

        String strUrl = apiBaseUrl + selfMedia + "access_token=" + this.access_token;
        return strUrl;
    }

    public class Get_Images extends AsyncTask<String, Void,ArrayList<String>> {

        protected ArrayList<String> doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            ArrayList<String> imageArray = new ArrayList<String>();

            try {
                String url = urlStringForRecentMediaRequest().toString();
                HttpGet httpget = new HttpGet(url);
                HttpResponse response;

                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);// method to convert stream to String
                    JSONObject object = new JSONObject(new JSONTokener(result));

                    JSONArray mediaArray = object.getJSONArray("data");
                    JSONArray imgArray = new JSONArray();
                    for (int i=0;i<mediaArray.length();i++) {
                        JSONObject jObj = mediaArray.getJSONObject(i);
                        if (jObj.get("type").equals("image")){
                            imgArray.put(jObj);
                        }
                    }

                    if (imgArray!=null){
//                      images>>standard_resolution>>url
                        for (int i=0;i<imgArray.length();i++) {
                            JSONObject jObj = imgArray.getJSONObject(i);
                            JSONObject objImage = (JSONObject) jObj.get("images");
                            JSONObject objStdImage = (JSONObject) objImage.get("standard_resolution");
                            String img_url = objStdImage.getString("url");
                            imageArray.add(img_url);

                        }

                    }
                    System.out.println(imageArray);
                    instream.close();
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return imageArray;
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
        protected void onPostExecute(ArrayList<String> result) {
            Photo.showImage(result);
        }
    }
}

