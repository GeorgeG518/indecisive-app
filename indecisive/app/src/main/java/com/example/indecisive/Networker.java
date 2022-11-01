/*
    George Gannon
    Networker.java

    Networker that performs network calls, but assumes you are using them in an asynchronous method e.g AsyncTask. Two methods:
        get(string:url) returns a string from an address.
        getBitmap(string:url): gets a bitmap from an address.

 */
package com.example.indecisive;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Networker  {
    final OkHttpClient client = new OkHttpClient();
    String response_ret = "";
    Bitmap response_bit;
    /*
        Returns a string of the data retrieved from a given url.
     */
    String get(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();
        try(Response response = client.newCall(request).execute()){
            response_ret = response.body().string();
            return response_ret;
        }

    }

    Bitmap getBitmap(String url)throws IOException {

        Request request = new Request.Builder().url(url).build();
        try(Response response = client.newCall(request).execute()){
            ResponseBody in = response.body();
            BufferedInputStream buff = new BufferedInputStream(in.byteStream());
            response_bit = BitmapFactory.decodeStream(buff);

            return response_bit;
        }

    }


}


