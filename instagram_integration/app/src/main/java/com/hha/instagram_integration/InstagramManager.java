package com.hha.instagram_integration;

import android.content.Context;
import android.content.res.Resources;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by HhA on 1/9/14.
 */

class InstagramManager {


    private static InstagramManager manager;
    private String access_token;
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

    public String getInstagramCookies() {

        CookieManager cookieMgr =  CookieManager.getInstance();
        String cookies = null;
        if (cookieMgr.hasCookies())
        {
            cookies = cookieMgr.getCookie("https://instagram.com/");
        }

        return cookies;
    }

    public JSONArray getInstagramImages(){

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
}

