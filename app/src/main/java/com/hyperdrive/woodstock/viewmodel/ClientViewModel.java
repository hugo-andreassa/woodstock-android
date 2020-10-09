package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;

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

        public LiveData<List<ClientModel>> getClients() {
            if (clients == null) {
                clients = new MutableLiveData<>();
            }

            loadClientsFromApi();

            return clients;
        }

        private void loadClientsFromApi() {
            ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
            Call<List<ClientModel>> call = clientService.findAll(1L);
            call.enqueue(new Callback<List<ClientModel>>() {
                @Override
                public void onResponse(Call<List<ClientModel>> call, Response<List<ClientModel>> response) {

                    if(response.isSuccessful()) {
                        clients.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<ClientModel>> call, Throwable t) {
                    Log.e(TAG, "onFailure findAll Clients");
                }
            });

        }
}
