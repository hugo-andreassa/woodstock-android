package com.hyperdrive.woodstock.ui.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.ClientAdapter;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.listeners.RecyclerItemClickListener;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.ui.home.HomeFragment;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        progressDialog = new ProgressDialog(ClientActivity.this);
        progressDialog.setMessage("Carregando....");
        progressDialog.show();

        getClientsFromApi();
        setupFloatingActionButton();
    }

    private void getClientsFromApi() {
        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<List<ClientModel>> call = clientService.findAll(1L);
        call.enqueue(new Callback<List<ClientModel>>() {
            @Override
            public void onResponse(Call<List<ClientModel>> call, Response<List<ClientModel>> response) {

                if(response.isSuccessful()) {
                    setupRecyclerView(response.body());
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    SnackbarUtil.showError(ClientActivity.this, "Erro ao carregar Clientes...");
                }
            }

            @Override
            public void onFailure(Call<List<ClientModel>> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(ClientActivity.this, "Erro ao carregar Clientes...");
            }
        });
    }

    private void setupRecyclerView(List<ClientModel> clients) {
        mRecyclerView = findViewById(R.id.list_client);
        ClientAdapter mAdapter;

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
                        ClientActionFragment clientActionFragment = ClientActionFragment.newInstance(clients.get(position));

                        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.client_fragment_layout, clientActionFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(
                                getApplicationContext(),
                                "ME MATA PFVR",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }));
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_client);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientActionFragment clientActionFragment = new ClientActionFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.client_fragment_layout, clientActionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}