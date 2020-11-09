package com.hyperdrive.woodstock.ui.operatingexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.OperatingExpenseAdapter;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.viewmodel.OperatingExpenseViewModel;

import java.util.ArrayList;
import java.util.List;

public class OperatingExpenseActivity extends AppCompatActivity {

    private static Long mCompanyId;
    private OperatingExpenseAdapter mAdapter;
    private static OperatingExpenseViewModel mOperatingExpenseViewModel;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operating_expense);

        Bundle bundle = getIntent().getExtras();
        mCompanyId = bundle.getLong("companyId");

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_operatingexpense);
        mOperatingExpenseViewModel = new OperatingExpenseViewModel();
        mOperatingExpenseViewModel.getOperatingExpenses(mCompanyId, progressBar).observe(
                this, operatingExpenses -> {
                    mAdapter.updateData(operatingExpenses);
        });
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_operatingexpense);
        fab.setOnClickListener(v -> {
            OperatingExpenseActionFragment operatingExpenseActionFragment =
                    OperatingExpenseActionFragment.newInstance(mCompanyId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.operatingexpense_fragment_layout, operatingExpenseActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupRecyclerView(List<OperatingExpenseModel> operatingExpenses) {
        RecyclerView mRecyclerView = findViewById(R.id.list_operatingexpense);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new OperatingExpenseAdapter(operatingExpenses, mCompanyId);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mOperatingExpenseViewModel.getOperatingExpenses(mCompanyId, progressBar);
    }
}