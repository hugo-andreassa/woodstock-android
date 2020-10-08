package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.ClientModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClientService {

    @GET("clients/")
    Call<List<ClientModel>> findAll(
            @Query("company") Long companyId);

    @POST("clients/")
    Call<Void> insert(
            @Body ClientModel client,
            @Header("Authorization") String auth);

    @PUT("clients/{id}")
    Call<Void> update(
            @Path("id") long clientId,
            @Body ClientModel client,
            @Header("Authorization") String auth);
}
