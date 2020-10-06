package com.hyperdrive.woodstock.ui.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.ClientAdapter;
import com.hyperdrive.woodstock.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        setupRecyclerView();
        setupFloatingActionButton();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.list_client);
        ClientAdapter mAdapter;
        List<String> myDataset = new ArrayList<>();
        myDataset.add("We didn't start the fire");
        myDataset.add("It was always burning");
        myDataset.add("Since the world's been turning");
        myDataset.add("We didn't start the fire");
        myDataset.add("No we didn't light it");
        myDataset.add("But we tried to fight it");

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ClientAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_client);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, NewClientActivity.class);
                startActivity(intent);
            }
        });
    }
}