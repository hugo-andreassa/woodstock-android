package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;

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

public interface BudgetService {

    @GET("budgets/")
    Call<List<BudgetModel>> findAll(
            @Query("client") Long clientId);

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
