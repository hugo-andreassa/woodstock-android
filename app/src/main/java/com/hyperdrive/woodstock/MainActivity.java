package com.hyperdrive.woodstock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.ui.login.LoginActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_user_logout).setOnMenuItemClickListener(menuItem -> {
            logout();
            return true;
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        loadNavHeaderInfo();
    }

    private void logout() {
        SharedPreferencesUtil sharedPreferences =
                new SharedPreferencesUtil(getApplicationContext());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        sharedPreferences.clearSharedPreferences();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loadNavHeaderInfo() {
        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(getApplicationContext());
        UserModel user = sharedPreferences.getUser();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header  = navigationView.getHeaderView(0);

        // Carrega os campos do navHeader
        TextView username = (TextView) header.findViewById(R.id.username_nav_header);
        TextView company = (TextView) header.findViewById(R.id.company_nav_header);

        // Altera os campos username e company
        username.setText(user.getName());
        company.setText(user.getCompany().getTradingName());
    }
}