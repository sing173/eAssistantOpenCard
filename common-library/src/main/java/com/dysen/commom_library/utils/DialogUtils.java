package com.dysen.commom_library.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.dysen.commom_library.R;
import com.dysen.commom_library.views.LoadingDailog;

import java.util.Arrays;
import java.util.List;


/**
 * dialog工具类
 *
 * @author wy
 */
public class DialogUtils {
    private static ProgressDialog dialog;
    private static AlertDialog alertDialog;
    private static Dialog d;

    private static LoadingDailog loadingDailog;//自定义Dailog

    public static void showDialog(Context context, String message) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showDialog(Context context, String message, Boolean isCancel) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(isCancel);
        dialog.show();
    }

    /**
     * 提示框
     *
     * @param context
     * @param str
     * @return
     */
    public static DialogAlert ShowDialog(Context context, String str) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.show();
        dialog.setMsg(str);
//		dialog.setMsg("抱歉，查询条件不能为空！");

        return dialog;
    }

    public static DialogAlert ShowDialog(Context context, View view) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.setContentView(view);
        dialog.show();
//		dialog.setMsg("抱歉，查询条件不能为空！");

        return dialog;
    }
    public static void showDialog(Context context) {
        d = new Dialog(context, R.style.new_circle_progress);
        d.setContentView(R.layout.flush_view);
        d.show();
    }
	/**
	 * 关闭清除提示 create by hutian 2018-05-18
	 */
	public static void ShowPromptDailog(Context context, final OnButtonClick onButtonClick ) {
		//View view = LayoutInflater.from(aty).inflate(R.layout.dialog_alert, null);
		String str="是否确认关闭该页面并清空数据？";
		final DialogAlert dialog = new DialogAlert(context);
		dialog.show();
		dialog.setMsg(str);
//		final Dialog dialog = DialogUtils.ShowDialog(aty, view);
//		dialog.show();
		Button btn_ok=dialog.getWindow().findViewById(com.dysen.commom_library.R.id.btn_ok);
		Button btn_cancle=dialog.getWindow().findViewById(com.dysen.commom_library.R.id.btn_cancle);
		btn_cancle.setVisibility(View.VISIBLE);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onButtonClick.buttonClick(view.getId());
				dialog.dismiss();
			}
		});
		btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});


	}
    public static Dialog showCloseDialog(Context context, View view) {
        d = new Dialog(context, R.style.new_circle_progress);
        d.setContentView(view);
//		在dialog.show()之前调dialog.setCanceledOnTouchOutside(true);
//		d.setCanceledOnTouchOutside(true);
        d.show();

        return d;
    }

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (d != null && d.isShowing()) {
            d.dismiss();
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static LoadingDailog showLoadingDailog(final Context context, String msg) {
        final LoadingDailog.Builder builder = new LoadingDailog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(true);
        loadingDailog = builder.create(msg);
        loadingDailog.show();

        return loadingDailog;
    }

    public static void closeLoadingDailog(Context context, Dialog dialog) {
        if (loadingDailog != null || loadingDailog.isShowing()) {
            loadingDailog.dismiss();
        }
    }

    /**
     * 滚动列表
     *
     * @param typeList
     */
    private void ShowWheel(String[] typeList, WheelView wheelView) {
        LogUtils.d("打开");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
//		wheel_line = (WheelView) view.findViewById(R.id.wheel_line);
        wheelView.setOffset(1);
        wheelView.setItems(Arrays.asList(typeList));
        wheelView.setSeletion(1);
    }

    private void ShowWheel(List<String> typeList, WheelView wheelView) {
        LogUtils.d("打开");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
//		wheel_line = (WheelView) view.findViewById(R.id.wheel_line);
        wheelView.setOffset(1);
        wheelView.setItems(typeList);
        wheelView.setSeletion(1);
    }

	/**
	 * 通用自定义布局 对话框
	 * @param context
	 * @param view
	 * @param width 显示宽度
	 * @param height 显示高度
	 * @return
	 */
	public static Dialog showCommonDialog(Context context, View view,int width,int height) {
		d = new Dialog(context, R.style.MyDialogStyle);
		d.setContentView(view);
		d.setContentView(view);
		Window window = d.getWindow();
		// window.setGravity(Gravity.LEFT | Gravity.TOP);
		Display display = window.getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = display.getWidth() * width / 10;
		 lp.height = display.getHeight() * height / 10;
		// lp.alpha = 1.0f;
		window.setAttributes(lp);
		d.setCancelable(false);
		d.show();

		return d;
	}
	/**
	 * 自定义布局Dialog提示框
	 *
	 * @param context
	 * @param view
	 * @return
	 */
	public static Dialog showCommonDialog(final Context context, View view) {
		Dialog promptdialog = new Dialog(context, R.style.MyDialogStyle);
		promptdialog.setContentView(view);
		promptdialog.setCancelable(false);
//		Window dialogWindow = promptdialog.getWindow();
//		dialogWindow.setGravity(Gravity.TOP);
		Window window = promptdialog.getWindow();
		Display display =window.getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams params = window.getAttributes() ;
		params.height = (int) (display.getHeight()*0.5);              //使用这种方式更改了dialog的框

		promptdialog.setCanceledOnTouchOutside(false);
		params.width =(int) (display.getWidth()*0.5);                     //使用这种方式更改了dialog的框宽
		window.setAttributes(params);
		return promptdialog;
	}
	public static Dialog CreatePromptDailog(final Context context, View view) {
		Dialog promptdialog = new Dialog(context, R.style.MyDialogStyle);
		promptdialog.setContentView(view);
		promptdialog.setCancelable(false);
		promptdialog.setCanceledOnTouchOutside(false);
		Window dialogWindow = promptdialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		return promptdialog;
	}
	public interface OnButtonClick {
		void buttonClick(int id);
	}
}
