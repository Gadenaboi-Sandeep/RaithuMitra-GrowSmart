package com.raithumitra.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    public static void showSuccessDialog(Context context, String message, Runnable onPositiveAction) {
        new AlertDialog.Builder(context)
                .setTitle("విజయవంతం (Success)")
                .setMessage(message)
                .setPositiveButton("లాగిన్ (Login)", (dialog, which) -> {
                    dialog.dismiss();
                    if (onPositiveAction != null) {
                        onPositiveAction.run();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public static void showErrorDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("లోపం (Error)")
                .setMessage(message)
                .setPositiveButton("మూసివేయండి (Close)", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}
