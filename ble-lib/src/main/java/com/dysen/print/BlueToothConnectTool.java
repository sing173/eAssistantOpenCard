package com.dysen.print;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.dysen.ble_lib.R;

public class BlueToothConnectTool {
    private static Dialog searchDialog;
    public static void openConn(boolean isConnected, final Context context, final Handler mHandler, final IPrinterOpertion myOpertion) {
        if (!isConnected) {
            new AlertDialog.Builder(context).setTitle(R.string.str_message)
                    .setMessage(R.string.str_connlast)
                    .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //myOpertion = new BluetoothOperation(context, mHandler);
                            myOpertion.btAutoConn(context, mHandler);
                        }
                    })
                    .setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //myOpertion = new BluetoothOperation(context, mHandler);
                            myOpertion.chooseDevice();
                        }

                    })
                    .show();
            /*View searchView = LayoutInflater.from(context).inflate(
                    R.layout.dialog_search_print_device, null);
            Button searchSubmit = (Button) searchView
                    .findViewById(R.id.print_device_searchagain_bt);
            Button commitConnect=(Button)searchView.findViewById(R.id.print_device_commit_bt);
            Button saveVoucher=(Button)searchView.findViewById(R.id.print_device_savevoucher_bt);

            searchSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOpertion.chooseDevice();
                }
            });
            commitConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOpertion.btAutoConn(context, mHandler);
                }
            });
            saveVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrintVoucherInfo info=new PrintVoucherInfo();
                    if(ParamUtils.print_voucher_type==1){

                    }else if(ParamUtils.print_voucher_type==2){

                    }else if(ParamUtils.print_voucher_type==3){

                    }else if(ParamUtils.print_voucher_type==4){

                    }else if(ParamUtils.print_voucher_type==5){

                    }
                }
            });

            searchDialog = new AlertDialog.Builder(context).create();
            searchDialog.show();
            searchDialog.getWindow().setContentView(searchView);
            searchDialog.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            WindowManager.LayoutParams lp = searchDialog.getWindow()
                    .getAttributes();
            // searchRemindDialog.getWindow().setGravity(Gravity.CENTER);
            lp.x = 49;
            lp.y = 0;
            lp.width =400;
            lp.height = 300;
            searchDialog.getWindow().setAttributes(lp);
            searchDialog.setCanceledOnTouchOutside(false);*/


        } else {
            Log.i("---","BlutToothConnectTool---close");
            myOpertion.close();
            //myOpertion = null;
            //mPrinter = null;
        }
    }
}
