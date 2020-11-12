package com.hyperdrive.woodstock.ui.client;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.ClientAdapter;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.viewmodel.ClientViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "CLIENT_ACTIVITY";

    private static Long mCompanyId;
    private ClientAdapter mAdapter;
    private static ClientViewModel mClientViewModel;
    private static ProgressBar progressBar;
    private UserModel mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preferences sharedPreferences = new Preferences(getApplicationContext());
        mUser = sharedPreferences.getUser();

        Bundle bundle = getIntent().getExtras();
        mCompanyId = bundle.getLong("companyId");

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_client);
        mClientViewModel = new ClientViewModel();
        mClientViewModel.getClients(mCompanyId, progressBar).observe(this, clients -> {
            mAdapter.updateData(clients);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_client);

        if(!mUser.getType().equals("ADMIN")) {
            fab.setVisibility(View.GONE);
        }

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
        mAdapter = new ClientAdapter(clients, mUser.getType());
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mClientViewModel.getClients(mCompanyId, progressBar);
    }
}