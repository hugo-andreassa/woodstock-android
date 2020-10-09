package com.hyperdrive.woodstock.ui.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.ClientAdapter;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.listeners.RecyclerItemClickListener;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.utils.SnackbarUtil;
import com.hyperdrive.woodstock.viewmodel.ClientViewModel;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {

    private final String SERVER_ERROR_DELETE = "Erro ao deletar Cliente...";
    private static final String TAG = "CLIENT_ACTIVITY";

    private RecyclerView mRecyclerView;
    private ClientAdapter mAdapter;
    private ClientViewModel mClientViewModel;
    private Preferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        sharedPreferences = new Preferences(this);

        setupFloatingActionButton();

        mClientViewModel = new ClientViewModel();
        mClientViewModel.getClients().observe(this, clients -> {
            setupRecyclerView(clients);
        });
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_client);
        fab.setOnClickListener(v -> {
            ClientActionFragment clientActionFragment = new ClientActionFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.client_fragment_layout, clientActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupRecyclerView(List<ClientModel> clients) {
        mRecyclerView = findViewById(R.id.list_client);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // specify an adapter
        mAdapter = new ClientAdapter(clients);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                getApplicationContext(),
                mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Ignore
                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();

                        ClientModel clientModel = clients.get(position);
                        ClientActionFragment clientActionFragment = ClientActionFragment.newInstance(clientModel);

                        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.client_fragment_layout, clientActionFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        ClientModel clientModel = clients.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
                        builder.setMessage("Deseja mesmo excluir o cadastro do(a) " + clientModel.getName())
                                .setPositiveButton("Sim", (dialog, id) -> {
                                    removeClientFromApi(clientModel.getId(), clients, position);
                                })
                                .setNegativeButton("Cancelar", (dialog, id) -> {
                                    dialog.dismiss();
                                });

                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                }));
    }

    private void updateRecyclerView(List<ClientModel> clients, int position) {
        clients.remove(position);
        mRecyclerView.removeViewAt(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, clients.size());
    }

    private void removeClientFromApi(Long id, List<ClientModel> clients, int position) {
        String auth = sharedPreferences.getAuthentication();

        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<Void> call = clientService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    updateRecyclerView(clients, position);
                    SnackbarUtil.showSuccess(ClientActivity.this, "Cliente deletado com sucesso");
                } else {
                    SnackbarUtil.showError(ClientActivity.this, SERVER_ERROR_DELETE);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SnackbarUtil.showError(ClientActivity.this, SERVER_ERROR_DELETE);
                Log.e(TAG, t.getMessage());
            }
        });
    }
}