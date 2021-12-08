package com.dysen.socket_library.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.dysen.socket_library.R;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class SocketUtil {
	private String HOST = "";// ��������IP��ַ
	private int PORT = 0;// ��������˿ں�
	private Socket socket = null;// Socket
	private Context context;//
	private Dialog dialog;// ���ؿ�
	private boolean showDialog = false;// Ĭ�ϲ���ʾDialog
	private String data = null;// ���͵�XML����
	private InputStream is;// �������������������
	private String responseString;// �ӷ��������ع���������
	private OutputStream os;// �ͻ��˷��͵�������
	private GetSocketData getData;// �ɹ����շ������ϵ�

	// �����������ݴ�������߳�
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (msg.what == 2) {
				responseString = (String) msg.obj;
				closeSocket();
				getData.getSocketData(responseString + 1);
			} else if (msg.what == 404) {
				//DialogManager.commonMessageDialog(context, "��������ʧ��",0,0);
			} else if (msg.what == 303) {
				//DialogManager.commonMessageDialog(context, "��·�ж�",0,0);
			}
		};
	};

	/**
	 * ���캯��
	 * 
	 * @param context
	 */
	public SocketUtil(Context context) {
		this.context = context;
	}

	public SocketUtil setDialog(boolean showDialog) {
		this.showDialog = showDialog;
		return this;
	}

	/**
	 * ����Ip��ַ
	 * 
	 * @param Host
	 * @return
	 */
	public SocketUtil setHost(String Host) {
		this.HOST = Host;
		return this;
	}

	/**
	 * �����̣߳����ӷ����
	 */
	public void execute() {
		if (showDialog == true) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.socket_dialog_progress, null);
			dialog = new AlertDialog.Builder(context).create();
			dialog.show();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.x = 250;
			lp.y = 0;
			dialog.getWindow().setAttributes(lp);
			dialog.getWindow().setContentView(view);
			dialog.setCancelable(false);
		}
		new Thread(sendRunnable).start();
	}

	/**
	 * ���ö˿ں�
	 * 
	 * @param port
	 * @return
	 */
	public SocketUtil setPort(int port) {
		this.PORT = port;
		return this;
	}

	/**
	 * ����ƴװ��XML
	 * 
	 * @param data
	 * @return
	 */
	public SocketUtil setData(String data) {
		this.data = data;
		return this;
	}

	/**
	 * �ر�Socket��
	 */
	private void closeSocket() {
		try {
			os.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����socket����
	 */
	private void connect() {
		try {
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(HOST, PORT);
			socket.connect(address, 4000);
			socket.setSoTimeout(3000);
			os = socket.getOutputStream();
			is = socket.getInputStream();
		} catch (Exception e) {
			handler.sendEmptyMessage(404);
		}
	}

	/**
	 * ��������
	 */
	private synchronized void rdata() {
		try {
			if (socket != null) {
				if (socket.isConnected()) {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					byte[] data = new byte[4096];
					int count = -1;
					while ((count = is.read(data, 0, 4096)) != -1) {
						outputStream.write(data, 0, count);
					}
					data = null;
					String s = new String(outputStream.toByteArray(), "gb2312");
					Message msg = new Message();
					msg.obj = s;
					msg.what = 2;
					handler.sendMessage(msg);
				} else {
				}
			} else {
				connect();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(303);
		}
	}

	/**
	 * Socket ���ӳɹ���Ļص��ӿ�
	 * 
	 * @author ��־��
	 * 
	 */
	public interface GetSocketData {
		public void getSocketData(String responseString);
	}

	/**
	 * ���ýӿ�
	 * 
	 * @param getSocketData
	 */
	public void setGetSocketData(GetSocketData getSocketData) {
		this.getData = getSocketData;
	}

	/**
	 * �������� ������ӳɹ���������
	 */
	Runnable sendRunnable = new Runnable() {
		@Override
		public void run() {
			connect();
			if (socket != null) {
				if (socket.isConnected()) {
					try {
						PrintWriter out = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(os, "gb2312")), true);
						// �������
						out.write(data);
						out.flush();
						rdata();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
				}
			} else {
			}
		}
	};
}
