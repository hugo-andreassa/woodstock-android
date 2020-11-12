package com.hyperdrive.woodstock.ui.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.BudgetAdapter;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.viewmodel.BudgetViewModel;

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private static Long clientId;
    private BudgetAdapter mAdapter;
    private static BudgetViewModel mBudgetViewModel;
    private static ProgressBar progressBar;
    private UserModel mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preferences sharedPreferences = new Preferences(getApplicationContext());
        mUser = sharedPreferences.getUser();

        Bundle bundle = getIntent().getExtras();
        clientId = bundle.getLong("clientId");
        String clientName = bundle.getString("clientName");

        // Troca o titulo da Activity
        setTitle("Orçamentos " + clientName.split(" ")[0]);

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 2);

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_budget);
        mBudgetViewModel = new BudgetViewModel();
        mBudgetViewModel.getBudgets(clientId, progressBar).observe(this, budgets -> {
            mAdapter.updateData(budgets);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
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
        FloatingActionButton fab = findViewById(R.id.fab_budget);

        if(!mUser.getType().equals("ADMIN")) {
            fab.setVisibility(View.GONE);
        }

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

        // specify an adapter
        mAdapter = new BudgetAdapter(budgets, clientId, mUser.getType());
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mBudgetViewModel.getBudgets(clientId, progressBar);
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