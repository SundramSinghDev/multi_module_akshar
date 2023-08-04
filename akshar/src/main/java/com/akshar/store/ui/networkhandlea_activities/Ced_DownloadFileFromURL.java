/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */

package com.akshar.store.ui.networkhandlea_activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.akshar.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by developer on 2/19/2016.
 */
public class Ced_DownloadFileFromURL extends AsyncTask<String, String, String> {
    private Context activity;
    private ProgressDialog progressDialog;
    private String Name;
    private String tittle = "sample";
    private String message = "";

    /* NotificationManager  mNotifyManager;
     NotificationCompat.Builder  mBuilder;*/
    public Ced_DownloadFileFromURL(Context context, String name) {
        activity = context;
        Name = name;
    }

    public Ced_DownloadFileFromURL(Activity context, String name, String titt) {
        activity = context;
        Name = name;
        tittle = titt;
    }

    private HostnameVerifier allHostsValid = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setIcon(R.drawable.mydownloadpro);
        progressDialog.setMessage("Downloading file. Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        int count;
        String error = "";
        try {
            Log.i("downloadResponse", "" + f_url[0]);
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept-Encoding", "identity");
            Log.i("downloadResponse", "" + httpConnection.getResponseCode());
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String s = httpConnection.getHeaderField("Set-Success");
                String line;
                Log.i("downloadResponse", "" + s);
                JSONObject object = new JSONObject(s);
                if (object.getString("success").equals("false")) {
                    error = "error";
                    message = object.getString("message");
                } else {
                    message = object.getString("message");
                    error = "success";
                    int lenghtOfFile = httpConnection.getContentLength();
                    try {
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);
                        OutputStream output = new FileOutputStream("/sdcard/" + Name);
                        byte[] data = new byte[1024];
                        long total = 0;
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    } catch (Exception e) {

                    }
                }

                return error;
            }
        } catch (IOException e) {
            return "failure";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error;
    }

    /**
     * Updating progress bar
     */
    @Override
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage

        /* progressDialog.setProgress(Integer.parseInt(progress[0]));*/
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String error) {
        if (error.equals("success")) {
            Toast.makeText(activity, "Your product" + tittle + " has been downloaded to sd card", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

}