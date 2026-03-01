package com.raithumitra.ui.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.raithumitra.utils.DialogHelper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showLoading() {
        // TODO: Implement Loading Dialog
    }

    public void hideLoading() {
        // TODO: Hide Loading
    }

    public void showSuccess(String title, String message) {
        DialogHelper.showSuccessDialog(this, title, message, null);
    }

    public void showSuccess(String title, String message, Runnable onDismiss) {
        DialogHelper.showSuccessDialog(this, title, message, onDismiss);
    }

    public void showError(String title, String message) {
        DialogHelper.showErrorDialog(this, title, message);
    }
}
