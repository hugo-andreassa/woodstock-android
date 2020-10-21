package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.CuttingPlanModel;

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

public interface CuttingPlanService {

    @GET("cuttingPlans/")
    Call<List<CuttingPlanModel>> findAll(
            @Query("budgetItem") Long budgetItemId);

    @POST("cuttingPlans/")
    Call<Void> insert(
            @Body CuttingPlanModel cuttingPlan,
            @Header("Authorization") String auth);

    @PUT("cuttingPlans/{id}")
    Call<Void> update(
            @Path("id") Long cuttingPlanId,
            @Body CuttingPlanModel cuttingPlan,
            @Header("Authorization") String auth);

    @DELETE("cuttingPlans/{id}")
    Call<Void> delete(
            @Path("id") Long cuttingPlanId,
            @Header("Authorization") String auth);
}
