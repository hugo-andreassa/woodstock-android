package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @POST("users")
    Call<Void> insert(
            @Body UserModel user);

    @GET("users/email")
    Call<UserModel> getUserByEmail(
            @Query("email") String email,
            @Header("Authorization") String auth);

    @PUT("users/{id}")
    Call<Void> update(
            @Path("id") Long userId,
            @Body UserModel user,
            @Header("Authorization") String auth);
}
