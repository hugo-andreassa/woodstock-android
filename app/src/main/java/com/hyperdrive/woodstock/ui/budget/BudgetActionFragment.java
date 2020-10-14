package com.hyperdrive.woodstock.ui.budget;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.utils.Mask;

import java.util.Calendar;

public class BudgetActionFragment extends Fragment {

    private static String ARG1 = "clientId";
    private static String ARG2 = "budget";

    private BudgetModel budget;
    private Long clientId;

    private EditText deadline;
    private EditText deliveryDay;
    private EditText paymentMethod;
    private Spinner spinnerStatus;
    private EditText cep;
    private EditText street;
    private EditText city;
    private Spinner spinnerEstados;
    private EditText number;
    private EditText comp;

    private DatePickerDialog datepicker;

    public BudgetActionFragment() {
    }

    public static BudgetActionFragment newInstance(BudgetModel budget, Long clientId) {
        BudgetActionFragment fragment = new BudgetActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG1, clientId);
        args.putSerializable(ARG2, budget);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientId = getArguments().getLong(ARG1);
            budget = (BudgetModel) getArguments().getSerializable(ARG2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_budget_action, container, false);

        setupSpinnerDropdownStatus(v);
        setupSpinnerDropdownEstados(v);

        setupEditTexts(v);
        if(budget != null) {
            loadFieldsInformation();
        }

        return v;
    }

    private void setupSpinnerDropdownStatus(View v) {
        spinnerStatus = v.findViewById(R.id.budget_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapter);
    }

    private void setupSpinnerDropdownEstados(View v) {
        spinnerEstados = v.findViewById(R.id.budget_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.state_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEstados.setAdapter(adapter);
    }

    private void loadFieldsInformation() {
    }

    private void setupEditTexts(View v) {
        EditText deadline = v.findViewById(R.id.budget_deadline);
        EditText deliveryDay = v.findViewById(R.id.budget_delivery_day);
        deliveryDay.setInputType(InputType.TYPE_NULL);
        deliveryDay.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            datepicker = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            deliveryDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, year, month, day);
            datepicker.show();
        });

        EditText paymentMethod = v.findViewById(R.id.budget_payment_method);
        paymentMethod.setText("Entrada de 30% e restante em até 10x no cartão.");

        EditText cep = v.findViewById(R.id.budget_cep);
        EditText street = v.findViewById(R.id.budget_street);
        EditText city = v.findViewById(R.id.budget_city);
        EditText number = v.findViewById(R.id.budget_number);
        EditText comp = v.findViewById(R.id.budget_comp);

        // Mask.mask(phone, Mask.PHONE)
    }

}