package com.mastergenova.controldiabetes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DataService {

    @POST("link")
    Call<Event> sendData(@Body Event event);
}
