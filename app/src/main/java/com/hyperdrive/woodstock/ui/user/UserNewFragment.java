package com.hyperdrive.woodstock.ui.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.models.UserModel;

public class UserNewFragment extends Fragment {

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

        setupEditTexts(view);
        setupRegistrarButton(view);

        return view;
    }

    private void setupEditTexts(View view) {
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.user_phone);
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

            insertUserInApi();
        });
    }

    private void insertUserInApi() {

    }
}