package com.hyperdrive.woodstock.api.services;

import com.hyperdrive.woodstock.models.BudgetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BudgetService {

    @GET("budgets/")
    Call<List<BudgetModel>> findAll(
            @Query("client") Long clientId);
}
