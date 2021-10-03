package com.example.kma_application.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.kma_application.Activity.PostStatusActivity;
import com.example.kma_application.Models.ResponseModel;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Calendar;

public class SubmitPostTask extends AsyncTask<Void,Void,String> {
    private Context context;
    String _class, description, originalBase64, resizeBase64;
    PostStatusActivity postStatusActivity;

    public SubmitPostTask(Context context, String _class, String description, String originalBase64, String resizeBase64, PostStatusActivity postStatusActivity) {
        this.context = context;
        this._class = _class;
        this.description = description;
        this.originalBase64 = originalBase64;
        this.resizeBase64 = resizeBase64;
        this.postStatusActivity = postStatusActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String postResponse = doPostRequest(
                    "https://nodejscloudkenji.herokuapp.com/submitPost", userJson());
            //String postResponse = doPostRequest("http://192.168.1.68:3000/login", jsons[0]);
            return postResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String userJson() {
        //get current day for submit today meal
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        //to String
        String date =
                calendar.get(Calendar.DAY_OF_MONTH)+"/"+
                        calendar.get(Calendar.MONTH)+"/"+
                        calendar.get(Calendar.YEAR);

        return "{\"_class\":\"" + _class + "\","
                +"\"date\":\"" + date + "\","
                +"\"description\":\"" + description +"\","
                + "\"originalBase64\":\"" + originalBase64 + "\","
                + "\"resizeBase64\":\"" + resizeBase64 + "\"}";
    }

    @Override
    protected void onPostExecute(String postResponse) {
        Gson gson = new Gson();
        ResponseModel responseModel= null;
        try {
            responseModel = gson.fromJson(postResponse,ResponseModel.class);
        }catch (Exception e){
            Toast.makeText(this.context, "Lỗi kết nối", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            this.cancel(true);
        }

        if (responseModel != null){
            Toast.makeText(this.context, responseModel.getResponse(), Toast.LENGTH_LONG).show();
            if (responseModel.getRes())
                postStatusActivity.finish();
        }
    }

    // post request code here

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String doPostRequest(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
