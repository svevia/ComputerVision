package com.example.aurelien.computervision;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by A643012 on 28/11/2017.
 */
public class CallAPI extends AsyncTask<String, String, String> {

    public CallAPI() {
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        String urlString = params[0]; // URL to call

        String data = params[1]; //data to post

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("img", data));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            response.getHeaders("Content-type");
            BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String t=null;
            while ((t = content.readLine()) != null) {
                System.out.println(t);
            }

            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(execute.getContent()));

            System.out.println("============");

            return "OK";
        } catch (Exception ex) {
            System.out.println("___________");
            Log.e(getClass().toString(),ex.getMessage());
            System.out.println(ex.getMessage());
            return "ERROR";
        }
    }

}
