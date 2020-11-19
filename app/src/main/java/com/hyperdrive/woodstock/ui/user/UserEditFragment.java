package com.hyperdrive.woodstock.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.UserService;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEditFragment extends Fragment {

    private String TAG = "USER_ACTION_FRAGMENT";
    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_INSERT = "Erro ao atualizar o Usuário.";
    private final String OK_REQUEST_INSERT = "Usuário atualizado com sucesso";

    private UserModel mUser;

    private TextInputEditText name;
    private TextInputEditText phone;
    private TextInputEditText password;

    private ProgressDialog progressDialog;
    private SharedPreferencesUtil sharedPreferences;

    public UserEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_edit, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando");
        sharedPreferences = new SharedPreferencesUtil(getContext());

        mUser = sharedPreferences.getUser();
        setupEditTexts(view);
        setupSaveButton(view);
        loadFieldsInformation(view);

        return view;
    }

    private void setupEditTexts(View view) {
        name = view.findViewById(R.id.user_name);
        phone = view.findViewById(R.id.user_phone);
        phone.addTextChangedListener(Mask.mask(phone, Mask.PHONE));
        password = view.findViewById(R.id.user_password);
    }

    private void loadFieldsInformation(View view) {
        name.setText(mUser.getName());
        phone.setText(mUser.getPhone());
    }

    private UserModel getValuesFromFields() {
        if(validateFields()) {
            UserModel user = new UserModel();
            user.setCompanyId(1L);

            if(mUser != null) {
                user.setId(mUser.getId());
                user.setEmail(mUser.getEmail());
                user.setStatus(mUser.getStatus());
                user.setType(mUser.getType());
            }

            user.setName(name.getText().toString());
            user.setPhone(Mask.unmask(phone.getText().toString()));

            if(password.getText().toString().isEmpty()) {
                user.setPassword("");
            } else {
                user.setPassword(password.getText().toString());
            }

            return user;
        }
        return null;
    }

    private boolean validateFields() {
        if(name.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
            String erro = getContext().getResources().getString(R.string.campo_obrigatorio);
            name.setError(erro);
            phone.setError(erro);

            return false;
        }

        String passwd1 = password.getText().toString();
        if(passwd1.length() < 6) {
            password.setError("A senha deve ter mais do que 6 caracteres");

            return false;
        }

        return true;
    }

    private void setupSaveButton(View view) {
        Button button = view.findViewById(R.id.user_salvar_button);

        button.setOnClickListener((v) -> {
            UserModel user = getValuesFromFields();
            if(user == null) {
                return;
            }

            if(mUser != null) {
                updateUserInApi(user);
            }
        });
    }

    private void updateUserInApi(UserModel user) {
        String auth = sharedPreferences.getAuthentication();

        progressDialog.show();

        UserService userService = RetrofitConfig.getRetrofitInstance().create(UserService.class);
        Call<Void> call = userService.update(user.getId(), user, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, response.toString());
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