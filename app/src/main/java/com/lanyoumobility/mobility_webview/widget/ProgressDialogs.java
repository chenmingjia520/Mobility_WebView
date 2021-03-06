package com.lanyoumobility.mobility_webview.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lanyoumobility.mobility_webview.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;



/**
 * 自定义进度条 ProgressDialog
 *
 * @author haiwei 2016/3/9.
 */
public class ProgressDialogs {
    private Dialog dialog;
    private int dialogLayoutResId = R.layout.layout_progress_dialog;
    private Context context;
    private static final int CLOSE_SAMPLE_VIEW = 0;  //定义关闭SampleView的动作信号标志
    private DelayCloseController delayCloseController;

    private Handler mHandler = new MyHandler(this);

    private class DelayCloseController extends TimerTask {
        private Timer timer = new Timer();
        private int actionFlags = 0;//标志位参数

        public DelayCloseController() {
            super();
            timer.schedule(this, 30000);   //启动定时器
        }

        public void setCloseFlags(int flag) {
            actionFlags = flag;
        }

        @Override
        public void run() {
            Message messageFinish = new Message();
            messageFinish.what = actionFlags;
            mHandler.sendMessage(messageFinish);
        }
    }

    public ProgressDialogs(Context context) {
        this.context = context;
    }

    public boolean isShow() {
        return dialog != null && dialog.isShowing();
    }



    TextView textView;

    public void setDialogLayout(int layoutResId){
        this.dialogLayoutResId = layoutResId;
    }

    public void showDialog(String msg) {
        if (dialog != null) {
            if (TextUtils.isEmpty(msg)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(msg);
            }
        } else {
            dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            window.setContentView( dialogLayoutResId );
//            window.setBackgroundDrawableResource(R.color.transparent);
            window.setBackgroundDrawable(new BitmapDrawable());//去掉黑边
            window.setDimAmount(0);

            textView = (TextView) window.findViewById(R.id.progress_message);
            if (TextUtils.isEmpty(msg)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(msg);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                        dialog.dismiss();
//                    }
//                    return false;
//                }
//            });
        }
    }


    public void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static class MyHandler extends Handler {

        private WeakReference<ProgressDialogs> weakReference = null;

        public MyHandler(ProgressDialogs progressDialogs) {
            weakReference = new WeakReference<ProgressDialogs>(progressDialogs);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLOSE_SAMPLE_VIEW:
                    if (weakReference.get().dialog != null && weakReference.get().dialog.isShowing()) {
//                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                        weakReference.get().closeDialog();
                    }
                    break;
            }
        }
    }


}
