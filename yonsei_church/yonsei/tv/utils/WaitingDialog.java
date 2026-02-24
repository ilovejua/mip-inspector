package yonsei_church.yonsei.tv.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import yonsei_church.yonsei.tv.R;

public class WaitingDialog {
    private static ProgressDialog mDialog;

    public static void showWaitingDialog(final Context context) {
        if (context != null && !((Activity) context).isFinishing()) {
            if (mDialog == null) {
                mDialog = new ProgressDialog(R.style.WaitingDialog, context) {
                    public void onBackPressed() {
                        WaitingDialog.cancelWaitingDialog();
                        ((Activity) context).onBackPressed();
                    }
                };
                mDialog.setProgressStyle(16973854);
            }
            if (!mDialog.isShowing()) {
                mDialog.setCancelable(true);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
            }
        }
    }

    public static void cancelWaitingDialog() {
        try {
            if (mDialog != null) {
                mDialog.setOnCancelListener((DialogInterface.OnCancelListener) null);
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
            mDialog = null;
        } catch (Exception unused) {
        }
    }

    public static boolean isShowingWaitingDialog() {
        return mDialog != null && mDialog.isShowing();
    }
}
