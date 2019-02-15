package com.wakawala.api;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.wakawala.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingHelper {

    private Dialog dialog;
    private Context mContext;
    private static LoadingHelper instance;

    private LoadingHelper() {
    }

    public static LoadingHelper getInstance() {
        if (instance == null) {
            instance = new LoadingHelper();
        }
        return instance;
    }

    public void show(Context mContext) {
        if (checkProgressOpen())
            return;
        this.mContext = mContext;
        dialog = new Dialog(mContext, R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_progress);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        try {
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            if (!((AppCompatActivity) mContext).isFinishing())
                dialog.dismiss();
        }
    }

    private boolean checkProgressOpen() {
        return dialog != null && dialog.isShowing();
    }

    public boolean isDialogShowing() {
        if (dialog != null && dialog.isShowing()) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }
}
