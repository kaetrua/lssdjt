package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.example.zl447.tohapp.R;


public class ProgressDialogUtil {
    private static ProgressDialog mDialog;

    public static void showDialog(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setView(LayoutInflater.from(context).inflate(R.layout.layout_loading,null));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public static void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
