package com.hyperdrive.woodstock.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.ui.client.ClientActivity;
import com.hyperdrive.woodstock.ui.operatingexpense.OperatingExpenseActivity;

import java.util.Objects;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupClienteMenuButton(view);
        setupEstoqueMenuButton(view);
        setupSolicitacoesMenuButton(view);
        setupDespesaMenuButton(view);

        return view;
    }

    private void setupDespesaMenuButton(View view) {
        Button button = view.findViewById(R.id.menu_despesas);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.requireActivity().getApplication(),
                    OperatingExpenseActivity.class);
            startActivity(intent);
        });
    }

    private void setupSolicitacoesMenuButton(View view) {
    }

    private void setupEstoqueMenuButton(View view) {
    }

    private void setupClienteMenuButton(View view) {
        Button button = view.findViewById(R.id.menu_cliente);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.requireActivity().getApplication(), ClientActivity.class);
            startActivity(intent);
        });
    }
}