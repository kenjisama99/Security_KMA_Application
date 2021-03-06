package com.example.kma_application.AsyncTask;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import com.example.kma_application.Models.ResponseModel;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class ChangePassTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String phone;

    public ChangePassTask(Context context, String phone) {
        this.context = context;
        this.phone = phone;
    }



    @Override
    protected String doInBackground(String... jsons) {
        try {
            String postResponse = LoadInfosTask.doPostRequest("https://nodejscloudkenji.herokuapp.com/changePass", jsons[0],context);
            //String postResponse = doPostRequest("http://192.168.1.68:3000/changePass", jsons[0]);
            return postResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String postResponse) {
        Gson gson = new Gson();
        ResponseModel responseModel = gson.fromJson(postResponse,ResponseModel.class);

        if (responseModel.getRes()){
            Toast.makeText(this.context, responseModel.getResponse(), Toast.LENGTH_LONG).show();
            //startUserActivities();
        }else
            Toast.makeText(this.context, responseModel.getResponse(), Toast.LENGTH_LONG).show();

    }

}
