package com.hyperdrive.woodstock.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.ui.client.ClientActivity;
import com.hyperdrive.woodstock.ui.material.MaterialActivity;
import com.hyperdrive.woodstock.ui.operatingexpense.OperatingExpenseActivity;
import com.hyperdrive.woodstock.ui.user.UserActivity;

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
        setupDespesaMenuButton(view);
        setupUserMenuButton(view);

        return view;
    }

    private void setupUserMenuButton(View view) {
        Button button = view.findViewById(R.id.menu_users);

        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(getContext());
        UserModel user = sharedPreferences.getUser();

        if(user.getType().equals("ADMIN")) {
            button.setVisibility(View.VISIBLE);
        }

        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.requireActivity().getApplication(),
                    UserActivity.class);
            intent.putExtra("companyId", 1L);
            startActivity(intent);
        });
    }

    private void setupDespesaMenuButton(View view) {
        Button button = view.findViewById(R.id.menu_despesas);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.requireActivity().getApplication(),
                    OperatingExpenseActivity.class);
            intent.putExtra("companyId", 1L);
            startActivity(intent);
        });
    }

    private void setupEstoqueMenuButton(View view) {
        Button button = view.findViewById(R.id.menu_estoque);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.requireActivity().getApplication(),
                    MaterialActivity.class);
            intent.putExtra("companyId", 1L);
            startActivity(intent);
        });
    }

    private void setupClienteMenuButton(View view) {
        Button button = view.findViewById(R.id.menu_cliente);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.requireActivity().getApplication(),
                    ClientActivity.class);
            intent.putExtra("companyId", 1L);
            startActivity(intent);
        });
    }
}