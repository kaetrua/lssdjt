package utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackBarUtil {
    public static void showShort(View view,String msg){
        Snackbar.make(view,msg, Snackbar.LENGTH_SHORT).show();
    }
    public static void showLong(View view,String msg){
        Snackbar.make(view,msg, Snackbar.LENGTH_LONG).show();
    }
}
