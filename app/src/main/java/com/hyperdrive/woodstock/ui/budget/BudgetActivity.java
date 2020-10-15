package com.hyperdrive.woodstock.ui.budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private static Long clientId;
    private BudgetAdapter mAdapter;
    private static BudgetViewModel mBudgetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        // LET CODE IN PEACE
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setupFloatingActionButton();

        Bundle bundle = getIntent().getExtras();
        clientId = bundle.getLong("clientId");

        setupRecyclerView(new ArrayList<>());

        mBudgetViewModel = new BudgetViewModel();
        mBudgetViewModel.getBudgets(clientId).observe(this, budgets -> {
            mAdapter.updateData(budgets);
        });

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_budget);
        fab.setOnClickListener(v -> {
            BudgetActionFragment budgetActionFragment = BudgetActionFragment.newInstance(null, clientId);

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
        mAdapter = new BudgetAdapter(budgets, clientId);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mBudgetViewModel.getBudgets(clientId);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
        }
    }
}