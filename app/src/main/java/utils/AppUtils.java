package utils;


import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.homework.news_master.R;

/**
 * 工具类
 */
public class AppUtils {
    /**
     * 点击帮助的按钮的弹窗
     * @param ctx
     * @param content
     */
    public static void showHelp(Context ctx, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle(ctx.getResources().getString(R.string.help));
        builder.setMessage(content);
        builder.setPositiveButton(ctx.getResources().getString(R.string.know), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
