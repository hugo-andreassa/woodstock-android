package com.hyperdrive.woodstock.utils;

import android.app.Activity;

import com.google.android.material.snackbar.Snackbar;
import com.hyperdrive.woodstock.R;

public class SnackbarUtil {

    public Activity activity;

    public SnackbarUtil(Activity activity) {
        this.activity = activity;
    }

    public void showError(String msg) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                msg.toUpperCase(),
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(activity.getResources().getColor(R.color.errorMessageColor));
        snackbar.show();
    }

    public void showSuccess(String msg) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                msg.toUpperCase(),
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(activity.getResources().getColor(R.color.successMessageColor));
        snackbar.show();
    }

}
