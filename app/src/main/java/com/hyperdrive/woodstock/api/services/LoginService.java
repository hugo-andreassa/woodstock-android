package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<Void> login(@Body LoginModel login);
}
