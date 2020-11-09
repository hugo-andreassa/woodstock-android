package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.ui.client.ClientActivity;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientViewModel extends ViewModel {
    private String TAG = "CLIENT_VIEW_MODEL";

    private MutableLiveData<List<ClientModel>> clients;
    private ProgressBar progressBar;

    public LiveData<List<ClientModel>> getClients(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;

        if (clients == null) {
            clients = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadClientsFromApi(id);

        return clients;
    }

    private void loadClientsFromApi(Long id) {
        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<List<ClientModel>> call = clientService.findAll(id);
        call.enqueue(new Callback<List<ClientModel>>() {
            @Override
            public void onResponse(Call<List<ClientModel>> call, Response<List<ClientModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    clients.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ClientModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Clients");
            }
        });

    }
}
