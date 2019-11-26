package com.nicholas.timetable.networking;

import com.nicholas.timetable.JsonHandler.Handler;
import com.nicholas.timetable.viewmodels.FragmentDialog;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RequestSender implements Updateable{

    private static RequestSender instance;
    public static String strRes = "";

    Retrofit retrofit;
    IApiRequests api;


    private RequestSender(){
        initNetworkObject();
    }

    public static RequestSender getInstance(){
        if(instance == null)
            instance = new RequestSender();
        return instance;
    }


    public IApiRequests getApi(){return api;}

    private void initNetworkObject(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.4.109:8080/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        api = retrofit.create(IApiRequests.class);
    }


    @Override
    public void update(final FragmentDialog fragment) {
        Call<String> result = api.getGroups();
        result.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Handler jsonHandler = new Handler();
                    strRes = jsonHandler.setGroups(response.body());
                    fragment.showSuccessDialog();
                }
                else
                    fragment.showErrorDialog();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                fragment.showErrorDialog();
            }
        });
    }

}
