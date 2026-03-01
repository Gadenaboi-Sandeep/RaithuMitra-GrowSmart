package com.raithumitra.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.raithumitra.R;

public class DialogHelper {

    public static void showSuccessDialog(Context context, String title, String message, Runnable onDismiss) {
        showDialog(context, title, message, R.drawable.ic_home, R.color.primary_green, onDismiss); // TODO: Use
                                                                                                   // Checkmark Icon
    }

    public static void showErrorDialog(Context context, String title, String message) {
        showDialog(context, title, message, android.R.drawable.ic_delete, R.color.secondary_blue, null); // Using Blue
                                                                                                         // for
                                                                                                         // "Info/alert"
                                                                                                         // per theme or
                                                                                                         // red? User
                                                                                                         // said "Light
                                                                                                         // Green & Blue
                                                                                                         // theme
                                                                                                         // throughout".
                                                                                                         // Blue is
                                                                                                         // secondary.
    }

    // Generic method
    private static void showDialog(Context context, String title, String message, int iconResId, int colorResId,
            Runnable onDismiss) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_generic);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Views
        ImageView icon = dialog.findViewById(R.id.img_dialog_icon);
        TextView tvTitle = dialog.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = dialog.findViewById(R.id.tv_dialog_message);
        Button btnAction = dialog.findViewById(R.id.btn_dialog_action);
        View cardContent = dialog.findViewById(R.id.img_dialog_icon).getParent() instanceof View
                ? (View) dialog.findViewById(R.id.img_dialog_icon).getParent()
                : null;

        // Bind Data
        icon.setImageResource(iconResId);
        // icon.setColorFilter(context.getResources().getColor(colorResId, null)); //
        // Tint handled in XML or here
        tvTitle.setText(title);
        tvMessage.setText(message);

        // Animation
        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        if (cardContent != null) {
            // We animate the root linear layout inside the card
            ((View) icon.getParent()).startAnimation(scaleUp);
        }

        btnAction.setOnClickListener(v -> {
            dialog.dismiss();
            if (onDismiss != null)
                onDismiss.run();
        });

        dialog.show();
    }
}
