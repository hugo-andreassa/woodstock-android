package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.MaterialModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MaterialService {
    @GET("materials/")
    Call<List<MaterialModel>> findAll(
            @Query("company") Long companyId);

    @POST("materials/")
    Call<Void> insert(
            @Body MaterialModel material,
            @Header("Authorization") String auth);

    @PUT("materials/{id}")
    Call<Void> update(
            @Path("id") Long materialId,
            @Body MaterialModel material,
            @Header("Authorization") String auth);

    @DELETE("materials/{id}")
    Call<Void> delete(
            @Path("id") Long materialId,
            @Header("Authorization") String auth);
}
