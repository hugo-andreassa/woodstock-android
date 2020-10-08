package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UserService {

    @GET("users/email")
    Call<UserModel> getUserByEmail(@Query("email") String email, @Header("Authorization") String auth);
}
