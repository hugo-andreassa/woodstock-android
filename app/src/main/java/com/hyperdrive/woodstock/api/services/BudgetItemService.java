package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.models.BudgetModel;

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

public interface BudgetItemService {

    @GET("budgetItems/")
    Call<List<BudgetItemModel>> findAll(
            @Query("budget") Long budgetId);

    @POST("budgetItems/")
    Call<Void> insert(
            @Body BudgetItemModel budget,
            @Header("Authorization") String auth);

    @PUT("budgetItems/{id}")
    Call<Void> update(
            @Path("id") Long budgetItemId,
            @Body BudgetItemModel budget,
            @Header("Authorization") String auth);

    @DELETE("budgetItems/{id}")
    Call<Void> delete(
            @Path("id") Long budgetItemId,
            @Header("Authorization") String auth);
}
