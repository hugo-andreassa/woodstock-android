package com.hyperdrive.woodstock.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.UserService;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNewFragment extends Fragment {

    private final String TAG = "USER_NEW_FRAGMENT";
    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_INSERT = "Erro ao cadastrar o Usuário. Este email já está em uso";
    private final String OK_REQUEST_INSERT = "Usuário cadastrado com sucesso";

    private ProgressDialog progressDialog;

    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText phone;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private Long companyId = 1L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_new, container, false);

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Carregando...");

        setupEditTexts(view);
        setupRegistrarButton(view);

        return view;
    }

    private void setupEditTexts(View view) {
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.user_phone);
        phone.addTextChangedListener(Mask.mask(phone, Mask.PHONE));
        password = view.findViewById(R.id.user_password);
        confirmPassword = view.findViewById(R.id.user_password_confirm);
    }

    private UserModel getValuesFromFields() {
        if(validateFields()) {
            UserModel user = new UserModel();
            user.setName(name.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setPassword(password.getText().toString());
            user.setType("MARCENEIRO");
            user.setStatus("DESATIVADO");
            user.setCompanyId(companyId);

            return user;
        }

        return null;
    }

    private boolean validateFields() {

        if(name.getText().toString().isEmpty() ||
            email.getText().toString().isEmpty() ||
            password.getText().toString().isEmpty() ||
            confirmPassword.getText().toString().isEmpty()) {

            String error = getActivity().getResources().getString(R.string.campo_obrigatorio);
            name.setError(error);
            email.setError(error);
            password.setError(error);
            confirmPassword.setError(error);

            return false;
        }

        String passwd1 = password.getText().toString();
        String passwd2 = confirmPassword.getText().toString();
        if(!passwd1.equals(passwd2)) {
            confirmPassword.setError("As senhas não combinam!");

            return false;
        }

        if(passwd1.length() < 6) {
            password.setError("A senha deve ter mais do que 6 caracteres");

            return false;
        }

        return true;
    }

    private void setupRegistrarButton(View view) {
        Button registrarButton = view.findViewById(R.id.user_registrar_button);

        registrarButton.setOnClickListener(v -> {
            UserModel user = getValuesFromFields();
            if(user == null) {
                return;
            }

            insertUserInApi(user);
        });
    }

    private void clearTextFields() {
        name.setText("");
        email.setText("");
        phone.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    private void insertUserInApi(UserModel user) {
        progressDialog.show();

        UserService userService = RetrofitConfig.getRetrofitInstance().create(UserService.class);
        Call<Void> call = userService.insert(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    clearTextFields();
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, user.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();

                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, t.getMessage());
            }
        });
    }
}