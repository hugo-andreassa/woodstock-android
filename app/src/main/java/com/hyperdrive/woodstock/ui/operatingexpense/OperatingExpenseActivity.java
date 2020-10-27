package com.hyperdrive.woodstock.ui.operatingexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.OperatingExpenseAdapter;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;

import java.util.ArrayList;
import java.util.List;

public class OperatingExpenseActivity extends AppCompatActivity {

    private static Long mCompanyId;
    private OperatingExpenseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operating_expense);

        mCompanyId = 1L;

        List<OperatingExpenseModel> operatingExpenses = new ArrayList<>();
        operatingExpenses.add(new OperatingExpenseModel(null, "Despesa com Gasolina",
                "", 221.42, "2020-10-27T17:21:52Z", "", 1L));
        operatingExpenses.add(new OperatingExpenseModel(null, "Conserto da Coladeira",
                "", 566.70, "2020-10-27T17:21:52Z", "", 1L));
        operatingExpenses.add(new OperatingExpenseModel(null, "Conta de Luz",
                "", 340.83, "2020-10-27T17:21:52Z", "", 1L));
        operatingExpenses.add(new OperatingExpenseModel(null, "Refil de água",
                "", 15.0, "2020-10-27T17:21:52Z", "", 1L));

        setupFloatingActionButton();
        setupRecyclerView(operatingExpenses);
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
}