package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface BudgetService {

    @GET("budgets/")
    Call<List<BudgetModel>> findAll(
            @Query("client") Long clientId);

    @GET("pdf/")
    @Streaming
    Call<ResponseBody> downloadPdf(
            @Query("company") Long companyId,
            @Query("client") Long clientId,
            @Query("budget") Long budgetId,
            @Header("Authorization") String auth);

    @POST("budgets/")
    Call<Void> insert(
            @Body BudgetModel budget,
            @Header("Authorization") String auth);

    @PUT("budgets/{id}")
    Call<Void> update(
            @Path("id") Long budgetId,
            @Body BudgetModel budget,
            @Header("Authorization") String auth);

    @DELETE("budgets/{id}")
    Call<Void> delete(
            @Path("id") Long budgetId,
            @Header("Authorization") String auth);
}
