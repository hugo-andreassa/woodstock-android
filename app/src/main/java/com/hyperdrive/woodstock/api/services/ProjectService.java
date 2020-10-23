package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.CuttingPlanModel;
import com.hyperdrive.woodstock.models.ProjectModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectService {

    @GET("projects/")
    Call<List<ProjectModel>> findAll(
            @Query("budgetItem") Long budgetItemId);

    @POST("projects/")
    Call<Void> insert(
            @Body ProjectModel cuttingPlan,
            @Header("Authorization") String auth);

    @PUT("projects/{id}")
    Call<Void> update(
            @Path("id") Long projectId,
            @Body ProjectModel cuttingPlan,
            @Header("Authorization") String auth);

    @DELETE("projects/{id}")
    Call<Void> delete(
            @Path("id") Long projectId,
            @Header("Authorization") String auth);

    @Multipart
    @POST("projects/picture")
    Call<Void> uploadImage(
            @Query("project") Long projectId,
            @Part MultipartBody.Part file,
            @Header("Authorization") String auth);
}
