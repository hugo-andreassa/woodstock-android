package com.hyperdrive.woodstock.utils;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.hyperdrive.woodstock.R;

public class SnackbarUtil {

    public static void showError(Activity activity, String msg) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                msg.toUpperCase(),
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(activity.getResources().getColor(R.color.errorMessageColor));
        snackbar.show();
    }

    public static void showSuccess(Activity activity, String msg) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                msg.toUpperCase(),
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(activity.getResources().getColor(R.color.successMessageColor));
        snackbar.show();
    }

}
