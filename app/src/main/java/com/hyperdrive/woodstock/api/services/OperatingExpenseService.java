package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.CuttingPlanModel;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;

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

public interface OperatingExpenseService {
    @GET("expenses/")
    Call<List<OperatingExpenseModel>> findAll(
            @Query("company") Long companyId,
            @Header("Authorization") String auth);

    @POST("expenses/")
    Call<Void> insert(
            @Body OperatingExpenseModel operatingExpense,
            @Header("Authorization") String auth);

    @PUT("expenses/{id}")
    Call<Void> update(
            @Path("id") Long operatingExpenseId,
            @Body OperatingExpenseModel operatingExpense,
            @Header("Authorization") String auth);

    @DELETE("expenses/{id}")
    Call<Void> delete(
            @Path("id") Long operatingExpenseId,
            @Header("Authorization") String auth);
}
