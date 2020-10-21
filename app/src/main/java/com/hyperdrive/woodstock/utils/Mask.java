package com.hyperdrive.woodstock.utils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

public abstract class Mask {

    public static final String CPF = "###.###.###-##";
    public static final String CNPJ = "##.###.###/####-##";
    public static final String PHONE = "(##) ####-#####";
    public static final String CEP = "#####-###";
    public static final String DATE = "##/##/####";
    public static final String HOUR = "##:##";

    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = Mask.unmask(s.toString());
                String mascara = "";

                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }

                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
        };
    }

    public static TextWatcher moneyMask(final EditText ediTxt) {
        return new TextWatcher() {
            private boolean isUpdating = false;
            private NumberFormat nf = NumberFormat.getCurrencyInstance();

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(CharSequence s, final int start, final int before, final int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                isUpdating = true;
                String str = s.toString();
                str = str.replaceAll("[^\\d]", "");

                try {
                    str = nf.format(Double.parseDouble(str) / 100);
                    ediTxt.setText(str);
                    ediTxt.setSelection(ediTxt.getText().length());

                } catch (NumberFormatException e) {
                    s = "";
                }
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "")
                .replaceAll("[-]", "")
                .replaceAll("[/]", "")
                .replaceAll("[(]", "")
                .replaceAll("[:]", "")
                .replaceAll("[)]", "")
                .replaceAll("[,]", "")
                .replaceAll("[ ]","");
    }

    public static Double unmaskMoney(final String s) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        try {
            Double number = nf.parse(s).doubleValue();
            
            return number;
        } catch (ParseException e) {
            return null;
        }
    }
}