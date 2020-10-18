package com.hyperdrive.woodstock.ui.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.ClientAdapter;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.viewmodel.ClientViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "CLIENT_ACTIVITY";

    private Preferences sharedPreferences;
    private static ClientViewModel mClientViewModel;
    private ClientAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        sharedPreferences = new Preferences(this);

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        mClientViewModel = new ClientViewModel();
        mClientViewModel.getClients().observe(this, clients -> {
            mAdapter.updateData(clients);
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
        RecyclerView mRecyclerView = findViewById(R.id.list_client);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // specify an adapter
        mAdapter = new ClientAdapter(clients);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mClientViewModel.getClients();
    }
}