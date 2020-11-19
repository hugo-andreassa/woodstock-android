package com.hyperdrive.woodstock.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.UserService;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdminFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String TAG = "USER_ADMIN_FRAGMENT";
    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_INSERT = "Erro ao atualizar o Usuário.";
    private final String OK_REQUEST_INSERT = "Usuário atualizado com sucesso";

    private static final String ARG_PARAM1 = "user";

    private UserModel mUser;

    private TextInputEditText name;
    private TextInputEditText phone;
    private TextInputEditText email;
    private String status;
    private String type;

    private ProgressDialog progressDialog;

    public UserAdminFragment() {
    }

    public static UserAdminFragment newInstance(UserModel mUser) {
        UserAdminFragment fragment = new UserAdminFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, mUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (UserModel) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_admin, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando");

        setupSpinnerDropdownStatus(view);
        setupSpinnerDropdownType(view);
        setupEditTexts(view);
        if(mUser != null) {
            loadFieldsInformation();
            setupAtualizarButton(view);
        }

        return view;
    }

    private void loadFieldsInformation() {
        name.setText(mUser.getName());
        phone.setText(mUser.getPhone());
        email.setText(mUser.getEmail());
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        if(parent.getId() == R.id.user_admin_status) {
            this.status = parent.getItemAtPosition(pos).toString();
        } else if(parent.getId() == R.id.user_admin_type) {
            this.type = parent.getItemAtPosition(pos).toString();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    private void setupSpinnerDropdownStatus(View v) {
        Spinner spinnerStatus = v.findViewById(R.id.user_admin_status);
        spinnerStatus.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.user_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        if(mUser != null) {
            List<String> list = Arrays.asList(
                    v.getContext().getResources().getStringArray(R.array.user_status_array));
            for (int i=0; i<=list.size(); i++) {
                String str = list.get(i);
                if(str.equals(mUser.getStatus())) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupSpinnerDropdownType(View v) {
        Spinner spinnerStatus = v.findViewById(R.id.user_admin_type);
        spinnerStatus.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.user_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        if(mUser != null) {
            List<String> list = Arrays.asList(
                    v.getContext().getResources().getStringArray(R.array.user_type_array));
            for (int i=0; i<=list.size(); i++) {
                String str = list.get(i);
                if(str.equals(mUser.getType())) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupEditTexts(View view) {
        name = view.findViewById(R.id.user_admin_name);
        phone = view.findViewById(R.id.user_admin_phone);
        phone.addTextChangedListener(Mask.mask(phone, Mask.PHONE));
        email = view.findViewById(R.id.user_admin_email);
    }

    private UserModel getValuesFromFields() {
        UserModel user = new UserModel();

        user.setId(mUser.getId());
        user.setName(name.getText().toString());
        user.setPhone(Mask.unmask(phone.getText().toString()));
        user.setEmail(email.getText().toString());
        user.setStatus(status);
        user.setType(type);
        user.setPassword("");
        user.setCompanyId(1L);

        return user;
    }

    private void setupAtualizarButton(View view) {
        Button button = view.findViewById(R.id.user_atualizar_button);

        button.setOnClickListener((v) -> {
            UserModel user = getValuesFromFields();

            if(mUser != null) {
                updateUserInApi(user);
            }
        });
    }

    private void updateUserInApi(UserModel user) {
        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(getContext());
        String auth = sharedPreferences.getAuthentication();

        progressDialog.show();

        UserService userService = RetrofitConfig.getRetrofitInstance().create(UserService.class);
        Call<Void> call = userService.update(user.getId(), user, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    UserActivity.updateRecyclerView();

                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();

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