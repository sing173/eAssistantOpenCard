package com.dysen.print;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants.Connect;
import com.android.print.sdk.PrinterInstance;
import com.dysen.ble_lib.R;

public class TestActivity extends Activity{
	protected static IPrinterOpertion myOpertion;
	private PrinterInstance mPrinter;
	private static boolean isConnected;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_print);
		mContext=TestActivity.this;
		myOpertion = new BluetoothOperation(TestActivity.this, mHandler);
		BlueToothConnectTool.openConn(false, TestActivity.this, mHandler,myOpertion);
	}
	
	//用于接受连接状态消息的 Handler
		private Handler mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Connect.SUCCESS:
					isConnected = true;
	                mPrinter = myOpertion.getPrinter();
	                Toast.makeText(mContext, "蓝牙连接成功！",
							Toast.LENGTH_SHORT).show();
					break;
				case Connect.FAILED:
					isConnected = false;
					Toast.makeText(mContext, R.string.conn_failed,
							Toast.LENGTH_SHORT).show();
					break;
				case Connect.CLOSED:
					isConnected = false;
					Toast.makeText(mContext, R.string.conn_closed, Toast.LENGTH_SHORT)
							.show();
					break;
				case Connect.NODEVICE:
					isConnected = false;
					Toast.makeText(mContext, R.string.conn_no, Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
				}

				/*updateButtonState();

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}*/
			}

		};
	
}
