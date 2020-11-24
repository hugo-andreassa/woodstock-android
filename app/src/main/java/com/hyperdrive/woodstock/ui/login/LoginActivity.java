package com.hyperdrive.woodstock.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyperdrive.woodstock.MainActivity;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.LoginService;
import com.hyperdrive.woodstock.api.services.UserService;
import com.hyperdrive.woodstock.models.LoginModel;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.ui.user.UserNewFragment;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_ERROR = "Erro ao realizar login. " +
            "Certifique-se de ter digitado os dados corretamente";

    ProgressDialog progressDialog;
    SharedPreferencesUtil sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove tittle
        getSupportActionBar().hide(); // Hide tittle bar
        setContentView(R.layout.activity_login);

        setupLoginButton();
        setupRegistrar();
    }

    private void setupRegistrar() {
        TextView textView = findViewById(R.id.login_registrar);
        textView.setOnClickListener(v -> {
            callNewUserFragment();
        });
    }

    private void setupLoginButton() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Carregando....");

        sharedPreferences = new SharedPreferencesUtil(LoginActivity.this);

        Button button = findViewById(R.id.login_button);
        button.setOnClickListener(v -> {
            // LoginModel loginModel = getLoginModel();
            LoginModel loginModel = new LoginModel("hugo.andreassa@gmail.com", "hugo@hugo");
            // LoginModel loginModel = new LoginModel("edsonsantos@gmail.com", "123456");

            if(loginModel != null) {
                progressDialog.show();
                getLoginFromApi(loginModel);
            } else {
                SnackbarUtil.showError(LoginActivity.this, "Preencha todos os campos!");
            }
        });
    }

    public LoginModel getLoginModel() {
        EditText usernameEdit = findViewById(R.id.login_username);
        EditText passwordEdit = findViewById(R.id.login_password);

        if (usernameEdit.getText().toString().trim().length() > 0
                && passwordEdit.getText().toString().trim().length() > 0) {

            String email = usernameEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            LoginModel login = new LoginModel(email, password);

            return login;
        } else {
            return null;
        }
    }

    private void getLoginFromApi(LoginModel login) {
        LoginService loginService = RetrofitConfig.getRetrofitInstance().create(LoginService.class);
        Call<Void> call = loginService.login(login);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    String auth = response.headers().get("Authorization");

                    sharedPreferences.setAuthentication(auth);
                    getUserDataFromApi(login, auth);
                } else {
                    progressDialog.dismiss();
                    SnackbarUtil.showError(LoginActivity.this, BAD_REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(LoginActivity.this, SERVER_ERROR);
            }
        });
    }

    private void getUserDataFromApi(LoginModel login, String auth) {
        UserService userService = RetrofitConfig.getRetrofitInstance().create(UserService.class);
        Call<UserModel> call = userService.getUserByEmail(login.getEmail(), auth);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    UserModel user = response.body();

                    if(user.getStatus().equals("DESATIVADO")) {
                        SnackbarUtil.showError(LoginActivity.this, "Este usuário " +
                                "está desativado. Entre em contato com um administrador");
                    } else {
                        sharedPreferences.setUser(user);
                        callHomeActivity();
                    }
                } else {
                    SnackbarUtil.showError(LoginActivity.this, SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(LoginActivity.this, SERVER_ERROR);
            }
        });

    }

    public void callHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void callNewUserFragment() {
        UserNewFragment userNewFragment = new UserNewFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.new_user_fragment_layout, userNewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}