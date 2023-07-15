package com.example.flutter_projects.core;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressiveIndicator {
    static ProgressDialog progressDialog;
    public static void showDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Connecting ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public static void closeDialog(Context context) {
        progressDialog.hide();
    }
}
