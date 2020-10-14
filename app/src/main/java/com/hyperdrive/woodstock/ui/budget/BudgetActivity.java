package com.hyperdrive.woodstock.ui.budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.BudgetAdapter;
import com.hyperdrive.woodstock.adapters.ClientAdapter;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.ui.client.ClientActionFragment;
import com.hyperdrive.woodstock.viewmodel.BudgetViewModel;
import com.hyperdrive.woodstock.viewmodel.ClientViewModel;

import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private Preferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        sharedPreferences = new Preferences(this);

        setupFloatingActionButton();

        Bundle bundle = getIntent().getExtras();
        Long clientId = bundle.getLong("clientId");

        BudgetViewModel mBudgetViewModel = new BudgetViewModel();
        mBudgetViewModel.getClients(clientId).observe(this, clients -> {
            setupRecyclerView(clients);
        });
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_budget);
        fab.setOnClickListener(v -> {
            BudgetActionFragment budgetActionFragment = new BudgetActionFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.budget_fragment_layout, budgetActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

    private void setupRecyclerView(List<BudgetModel> budgets) {
        RecyclerView mRecyclerView = findViewById(R.id.list_budget);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // specify an adapter
        BudgetAdapter mAdapter = new BudgetAdapter(budgets, sharedPreferences.getAuthentication());
        mRecyclerView.setAdapter(mAdapter);
    }
}