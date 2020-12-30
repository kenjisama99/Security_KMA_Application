package com.example.kma_application.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kma_application.Models.Medicine;
import com.example.kma_application.Models.Medicine;
import com.example.kma_application.Models.ResponseModel;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LoadMedicineTask extends AsyncTask<Void,Void,String> {
    Context context;
    String phone;
    private EditText txtContent;
    private EditText txtStartDate;
    private EditText txtEndDate;

    public LoadMedicineTask(Context context, String phone, EditText txtContent, EditText txtStartDate, EditText txtEndDate) {
        this.context = context;
        this.phone = phone;
        this.txtContent = txtContent;
        this.txtStartDate = txtStartDate;
        this.txtEndDate = txtEndDate;
    }

    OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String postResponse = doPostRequest("https://nodejscloudkenji.herokuapp.com/getMedicine", userJson(phone));
            //String postResponse = doPostRequest("http://192.168.1.68:3000/login", jsons[0]);
            return postResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String postResponse) {
        Gson gson = new Gson();
        Medicine medicine = null;
        ResponseModel responseModel= null;
        try {
            responseModel = gson.fromJson(postResponse,ResponseModel.class);
            medicine = gson.fromJson(postResponse,Medicine.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (responseModel.getResponse() == null){
            if (true){
                if (medicine != null){
                    //Toast.makeText(this.context, "Class: "+medicine.get_class(), Toast.LENGTH_LONG).show();
                    //String birth = invertedDate(medicine.getBirth());
                    txtContent.setText(medicine.getContent());
                    txtStartDate.setText(medicine.getStartDate());
                    txtEndDate.setText(medicine.getEndDate());

                }else
                    Toast.makeText(this.context, "Medicine: "+medicine, Toast.LENGTH_LONG).show();
            }

        }else
            Toast.makeText(this.context, "Response: "+ responseModel.getResponse(), Toast.LENGTH_LONG).show();
    }

    private String invertedDate(String birth) {
        String[] strings = birth.split("-",3);
        String result = strings[2]+"-"+strings[1]+"-"+strings[0];
        return result;
    }

    // post request code here
    String userJson(String phone) {
        return "{\"phone\":\"" + phone + "\"}";
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}