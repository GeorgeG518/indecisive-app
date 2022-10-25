package com.example.indecisive;
import android.os.AsyncTask;

import java.io.IOException;

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
    String get(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();
        try(Response response = client.newCall(request).execute()){
            response_ret = response.body().string();
            return response_ret;
        }

    }


}


