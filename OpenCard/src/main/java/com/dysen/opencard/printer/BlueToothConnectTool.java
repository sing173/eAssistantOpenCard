package com.dysen.opencard.printer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.dysen.ble_lib.R;
import com.dysen.commom_library.bean.PrintVoucherInfo;
import com.dysen.print.IPrinterOpertion;

import org.kymjs.kjframe.KJDB;

import java.util.List;

public class BlueToothConnectTool {
    private static KJDB kjdb;
    private static List<PrintVoucherInfo> list;
    private static List<PrintVoucherInfo> list1;
    private static List<PrintVoucherInfo> list2;
    private static List<PrintVoucherInfo> list3;
    private static List<PrintVoucherInfo> list4;
    private static List<PrintVoucherInfo> list5;
    public static void openConn(boolean isConnected, final Context context, final Handler mHandler, final IPrinterOpertion myOpertion) {

        if (!isConnected) {
            new AlertDialog.Builder(context).setTitle(R.string.str_message)
                    .setMessage(R.string.str_connlast)
//                    .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            //myOpertion = new BluetoothOperation(context, mHandler);
//                            PrintVoucherInfo info=new PrintVoucherInfo();
//                            kjdb=KJDB.create(context,"voucherDb");
//                            /*list=kjdb.findAll(PrintVoucherInfo.class);
//                            for(int i=0;i<list.size();i++){
//                                String s=list.get(i).getPrintVoucherType()+list.get(i).getCustId()+list.get(i).getUserId()+list.get(i).getSaveDate();
//                                for(int j=i+1;j<list.size();j++){
//                                    String n=list.get(j).getPrintVoucherType()+list.get(j).getCustId()+list.get(j).getUserId()+list.get(j).getSaveDate();
//                                    if(n.equals(s)){
//                                        //db.delete(allList.get(j));
//                                    }
//                                }
//                            }*/
//                            if(ParamUtils.print_voucher_type==1){
//                                info.setCustId(ParamUtils.cusNum);
//                                info.setCustName(ParamUtils.certName);
//                                info.setCertType(ParamUtils.certType);
//                                info.setCertNo(ParamUtils.certId);
//                                info.setPhoneNo(ParamUtils.telPhone);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(1);
//                                int temp=0;
//                                if(ParamUtils.print_voucher_from_type==2) {
//                                     temp = ParamUtils.print_voucher_time + 1;
//                                     ParamUtils.print_voucher_time=temp;
//                                }
//                                info.setPrintTime(temp);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                kjdb.save(info);
//                            }else if(ParamUtils.print_voucher_type==2){
//                                info.setCustId(ParamUtils.cusNum);
//                                info.setCustName(ParamUtils.certName);
//                                info.setICCardNo(ParamUtils.cardNum);
//                                info.setAccountNo(ParamUtils.accountNum);
//                                info.setCardName(ParamUtils.cardType);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(2);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                kjdb.save(info);
//                            }else if(ParamUtils.print_voucher_type==4){
//                                info.setCustId(ParamUtils.cusNum);
//                                info.setCustName(ParamUtils.cusName);
//                                info.setCardNo(ParamUtils.cardNum);
//                                info.setCardName(ParamUtils.cardProductName);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(4);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                kjdb.save(info);
//                            }else if(ParamUtils.print_voucher_type==3){
//                                info.setSignCardNo(ParamUtils.msg_cardNum);
//                                info.setCertType(ParamUtils.msg_certType);
//                                info.setCertNo(ParamUtils.msg_certId);
//                                info.setProductType(ParamUtils.msg_cardProductName);
//                                info.setCustName(ParamUtils.msg_certName);
//                                info.setSignaccount(ParamUtils.msg_signAccount);
//                                if(ParamUtils.msg_mobileNums.size()>0) {
//                                    info.setPhoneNo(ParamUtils.msg_mobileNums.get(0));
//                                }
//                                info.setChargeStandard(ParamUtils.msg_charges);
////                                info.setLimitChangeLowest(Double.valueOf(ParamUtils.msg_amountLowerLimit));
////                                info.setLimitWarnLowest(Double.valueOf(ParamUtils.msg_remindLowerlimit));
//                                info.setWhetherShowLeft(ParamUtils.msg_isShowFree);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(3);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                kjdb.save(info);
//                            }else if(ParamUtils.print_voucher_type==5){
//                                info.setCustName(ParamUtils.cusName);
//                                info.setCertType(ParamUtils.certType);
//                                info.setCertNo(ParamUtils.certId);
//                                info.setSignaccount(ParamUtils.cardNum);
//                                info.setServiceProject(ParamUtils.serviceProject);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setTellerId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(5);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                kjdb.save(info);
//
//                            }
//                            myOpertion.btAutoConn(context, mHandler);
//                        }
//                    })
                    .setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //myOpertion = new BluetoothOperation(context, mHandler);
//                            PrintVoucherInfo info=new PrintVoucherInfo();
//                            kjdb=KJDB.create(context,"voucherDb");
//                            list=kjdb.findAll(PrintVoucherInfo.class);
//                            list1=new ArrayList<PrintVoucherInfo>();
//                            list2=new ArrayList<PrintVoucherInfo>();
//                            list3=new ArrayList<PrintVoucherInfo>();
//                            list4=new ArrayList<PrintVoucherInfo>();
//                            list5=new ArrayList<PrintVoucherInfo>();
//                            for(PrintVoucherInfo pinfo:list){
//                                if(1==pinfo.getPrintVoucherType()){
//                                    list1.add(pinfo);
//                                }else if(2==pinfo.getPrintVoucherType()){
//                                    list2.add(pinfo);
//                                }else if(3==pinfo.getPrintVoucherType()){
//                                    list3.add(pinfo);
//                                }else if(4==pinfo.getPrintVoucherType()){
//                                    list4.add(pinfo);
//                                }else if(5==pinfo.getPrintVoucherType()){
//                                    list5.add(pinfo);
//                                }
//                            }
//                            if(ParamUtils.print_voucher_type==1){
//                                boolean add=true;
//                                int id=0;
//                                for(int i=0;i<list1.size();i++){
//                                    String s=list1.get(i).getPrintVoucherType()+list1.get(i).getCustId()+list1.get(i).getUserId()+list1.get(i).getSaveDate();
//                                    String newInfo=ParamUtils.print_voucher_type+ParamUtils.cusNum+ParamUtils.tellerId+ParamUtils.print_voucher_date;
//                                    if(s.equals(newInfo)){
//                                        id=list1.get(i).getId();
//                                        add=false;
//                                    }
//                                }
//                                info.setCustId(ParamUtils.cusNum);
//                                info.setCustName(ParamUtils.certName);
//                                info.setCertType(ParamUtils.certType);
//                                info.setCertNo(ParamUtils.certId);
//                                info.setPhoneNo(ParamUtils.telPhone);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(1);
//                                int temp=ParamUtils.print_voucher_time+1;
//                                ParamUtils.print_voucher_time=temp;
//                                info.setPrintTime(temp);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                if(add) {
//                                    kjdb.save(info);
//                                }else{
//                                    kjdb.deleteById(PrintVoucherInfo.class,id);
//                                    kjdb.save(info);
//                                }
//                            }else if(ParamUtils.print_voucher_type==2){
//                                boolean add=true;
//                                int id=0;
//                                for(int i=0;i<list2.size();i++){
//                                    String s=list2.get(i).getPrintVoucherType()+list2.get(i).getCustId()+list2.get(i).getUserId()+list2.get(i).getSaveDate();
//                                    String newInfo=ParamUtils.print_voucher_type+ParamUtils.cusNum+ParamUtils.tellerId+ParamUtils.print_voucher_date;
//                                    if(s.equals(newInfo)){
//                                        id=list2.get(i).getId();
//                                        add=false;
//                                    }
//                                }
//                                info.setCustId(ParamUtils.cusNum);
//                                info.setCustName(ParamUtils.certName);
//                                info.setICCardNo(ParamUtils.cardNum);
//                                info.setAccountNo(ParamUtils.accountNum);
//                                info.setCardName(ParamUtils.cardType);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(2);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                info.setPrintTime(ParamUtils.print_voucher_time+1);
//                                if(add) {
//                                    kjdb.save(info);
//                                }else{
//                                    kjdb.deleteById(PrintVoucherInfo.class,id);
//                                    kjdb.save(info);
//                                }
//                            }else if(ParamUtils.print_voucher_type==4){
//                                boolean add=true;
//                                int id=0;
//                                for(int i=0;i<list4.size();i++){
//                                    String s=list4.get(i).getPrintVoucherType()+list4.get(i).getCustId()+list4.get(i).getUserId()+list4.get(i).getSaveDate();
//                                    String newInfo=ParamUtils.print_voucher_type+ParamUtils.cusNum+ParamUtils.tellerId+ParamUtils.print_voucher_date;
//                                    if(s.equals(newInfo)){
//                                        id=list4.get(i).getId();
//                                        add=false;
//                                    }
//                                }
//                                info.setCustId(ParamUtils.cusNum);
//                                info.setCustName(ParamUtils.cusName);
//                                info.setCardNo(ParamUtils.cardNum);
//                                info.setCardName(ParamUtils.cardProductName);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(4);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                info.setPrintTime(ParamUtils.print_voucher_time+1);
//                                if(add) {
//                                    kjdb.save(info);
//                                }else{
//                                    kjdb.deleteById(PrintVoucherInfo.class,id);
//                                    kjdb.save(info);
//                                }
//                            }else if(ParamUtils.print_voucher_type==3){
//                                boolean add=true;
//                                int id=0;
//                                for(int i=0;i<list3.size();i++){
//                                    String s=list3.get(i).getPrintVoucherType()+list3.get(i).getCertNo()+list3.get(i).getSignaccount()+list3.get(i).getSaveDate();
//                                    String newInfo=ParamUtils.print_voucher_type+ParamUtils.msg_certId+ParamUtils.msg_signAccount+ParamUtils.print_voucher_date;
//                                    if(s.equals(newInfo)){
//                                        id=list3.get(i).getId();
//                                        add=false;
//                                    }
//                                }
//                                info.setSignCardNo(ParamUtils.msg_cardNum);
//                                info.setCertType(ParamUtils.msg_certType);
//                                info.setCertNo(ParamUtils.msg_certId);
//                                info.setProductType(ParamUtils.msg_cardProductName);
//                                info.setCustName(ParamUtils.msg_certName);
//                                info.setSignaccount(ParamUtils.msg_signAccount);
//                                if(ParamUtils.msg_mobileNums.size()>0) {
//                                    info.setPhoneNo(ParamUtils.msg_mobileNums.get(0));
//                                }
//                                info.setChargeStandard(ParamUtils.msg_charges);
//                                info.setLimitChangeLowest(Double.valueOf(ParamUtils.msg_amountLowerLimit));
//                                info.setLimitWarnLowest(Double.valueOf(ParamUtils.msg_remindLowerlimit));
//                                info.setWhetherShowLeft(ParamUtils.msg_isShowFree);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setUserId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(3);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                info.setPrintTime(ParamUtils.print_voucher_time+1);
//                                if(add) {
//                                    kjdb.save(info);
//                                }else{
//                                    kjdb.deleteById(PrintVoucherInfo.class,id);
//                                    kjdb.save(info);
//                                }
//                            }else if(ParamUtils.print_voucher_type==5){
//                                boolean add=true;
//                                int id=0;
//                                for(int i=0;i<list5.size();i++){
//                                    String s=list5.get(i).getPrintVoucherType()+list5.get(i).getCertNo()+list5.get(i).getSignaccount()+list5.get(i).getSaveDate();
//                                    String newInfo=ParamUtils.print_voucher_type+ParamUtils.certId+ParamUtils.cardNum+ParamUtils.print_voucher_date;
//                                    if(s.equals(newInfo)){
//                                        id=list5.get(i).getId();
//                                        add=false;
//                                    }
//                                }
//                                info.setCustName(ParamUtils.cusName);
//                                info.setCertType(ParamUtils.certType);
//                                info.setCertNo(ParamUtils.certId);
//                                info.setSignaccount(ParamUtils.cardNum);
//                                info.setServiceProject(ParamUtils.serviceProject);
//                                info.setBranchNo(ParamUtils.orgId);
//                                info.setTerminalId(ParamUtils.terminalId);
//                                info.setTellerId(ParamUtils.tellerId);
//                                info.setPrintVoucherType(5);
//                                info.setSaveDate(DatetimeUtil.getToday2());
//                                info.setPrintTime(ParamUtils.print_voucher_time+1);
//                                if(add) {
//                                    kjdb.save(info);
//                                }else{
//                                    kjdb.deleteById(PrintVoucherInfo.class,id);
//                                    kjdb.save(info);
//                                }
//                            }
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
                    PrintVoucherInfo info=new PrintVoucherInfo();
                    kjdb=KJDB.create(context,"voucherDb");
                    if(ParamUtils.print_voucher_type==1){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.certName);
                        info.setCertType(ParamUtils.certType);
                        info.setCertNo(ParamUtils.certId);
                        info.setPhoneNo(ParamUtils.telPhone);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(1);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==2){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.certName);
                        info.setICCardNo(ParamUtils.cardNum);
                        info.setAccountNo(ParamUtils.accountNum);
                        info.setCardName(ParamUtils.cardType);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(2);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==4){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.cusName);
                        info.setCardNo(ParamUtils.cardNum);
                        info.setCardName(ParamUtils.cardProductName);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(4);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==3){
                        info.setSignCardNo(ParamUtils.msg_cardNum);
                        info.setCertType(ParamUtils.msg_certType);
                        info.setCertNo(ParamUtils.msg_certId);
                        info.setProductType(ParamUtils.msg_cardProductName);
                        info.setCustName(ParamUtils.msg_certName);
                        info.setSignaccount(ParamUtils.msg_signAccount);
                        if(ParamUtils.msg_mobileNums.size()>0) {
                            info.setPhoneNo(ParamUtils.msg_mobileNums.get(0));
                        }
                        info.setChargeStandard(ParamUtils.msg_charges);
                        info.setLimitChangeLowest(Double.valueOf(ParamUtils.msg_amountLowerLimit));
                        info.setLimitWarnLowest(Double.valueOf(ParamUtils.msg_remindLowerlimit));
                        info.setWhetherShowLeft(ParamUtils.msg_isShowFree);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(3);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==5){
                        info.setCustName(ParamUtils.cusName);
                        info.setCertType(ParamUtils.certType);
                        info.setCertNo(ParamUtils.certId);
                        info.setSignaccount(ParamUtils.cardNum);
                        info.setServiceProject(ParamUtils.serviceProject);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setTellerId(ParamUtils.tellerId);
                        info.setPrintVoucherType(5);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }
                    searchDialog.dismiss();
                    myOpertion.chooseDevice();
                }
            });
            commitConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrintVoucherInfo info=new PrintVoucherInfo();
                    kjdb=KJDB.create(context,"voucherDb");
                    if(ParamUtils.print_voucher_type==1){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.certName);
                        info.setCertType(ParamUtils.certType);
                        info.setCertNo(ParamUtils.certId);
                        info.setPhoneNo(ParamUtils.telPhone);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(1);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==2){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.certName);
                        info.setICCardNo(ParamUtils.cardNum);
                        info.setAccountNo(ParamUtils.accountNum);
                        info.setCardName(ParamUtils.cardType);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(2);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==4){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.cusName);
                        info.setCardNo(ParamUtils.cardNum);
                        info.setCardName(ParamUtils.cardProductName);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(4);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==3){
                        info.setSignCardNo(ParamUtils.msg_cardNum);
                        info.setCertType(ParamUtils.msg_certType);
                        info.setCertNo(ParamUtils.msg_certId);
                        info.setProductType(ParamUtils.msg_cardProductName);
                        info.setCustName(ParamUtils.msg_certName);
                        info.setSignaccount(ParamUtils.msg_signAccount);
                        if(ParamUtils.msg_mobileNums.size()>0) {
                            info.setPhoneNo(ParamUtils.msg_mobileNums.get(0));
                        }
                        info.setChargeStandard(ParamUtils.msg_charges);
                        info.setLimitChangeLowest(Double.valueOf(ParamUtils.msg_amountLowerLimit));
                        info.setLimitWarnLowest(Double.valueOf(ParamUtils.msg_remindLowerlimit));
                        info.setWhetherShowLeft(ParamUtils.msg_isShowFree);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(3);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==5){
                        info.setCustName(ParamUtils.cusName);
                        info.setCertType(ParamUtils.certType);
                        info.setCertNo(ParamUtils.certId);
                        info.setSignaccount(ParamUtils.cardNum);
                        info.setServiceProject(ParamUtils.serviceProject);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setTellerId(ParamUtils.tellerId);
                        info.setPrintVoucherType(5);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }
                    searchDialog.dismiss();
                    myOpertion.btAutoConn(context, mHandler);
                }
            });
            saveVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrintVoucherInfo info=new PrintVoucherInfo();
                    kjdb=KJDB.create(context,"voucherDb");
                    if(ParamUtils.print_voucher_type==1){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.certName);
                        info.setCertType(ParamUtils.certType);
                        info.setCertNo(ParamUtils.certId);
                        info.setPhoneNo(ParamUtils.telPhone);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(1);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==2){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.certName);
                        info.setICCardNo(ParamUtils.cardNum);
                        info.setAccountNo(ParamUtils.accountNum);
                        info.setCardName(ParamUtils.cardType);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(2);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==4){
                        info.setCustId(ParamUtils.cusNum);
                        info.setCustName(ParamUtils.cusName);
                        info.setCardNo(ParamUtils.cardNum);
                        info.setCardName(ParamUtils.cardProductName);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(4);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==3){
                        info.setSignCardNo(ParamUtils.msg_cardNum);
                        info.setCertType(ParamUtils.msg_certType);
                        info.setCertNo(ParamUtils.msg_certId);
                        info.setProductType(ParamUtils.msg_cardProductName);
                        info.setCustName(ParamUtils.msg_certName);
                        info.setSignaccount(ParamUtils.msg_signAccount);
                        if(ParamUtils.msg_mobileNums.size()>0) {
                            info.setPhoneNo(ParamUtils.msg_mobileNums.get(0));
                        }
                        info.setChargeStandard(ParamUtils.msg_charges);
                        info.setLimitChangeLowest(Double.valueOf(ParamUtils.msg_amountLowerLimit));
                        info.setLimitWarnLowest(Double.valueOf(ParamUtils.msg_remindLowerlimit));
                        info.setWhetherShowLeft(ParamUtils.msg_isShowFree);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setUserId(ParamUtils.tellerId);
                        info.setPrintVoucherType(3);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }else if(ParamUtils.print_voucher_type==5){
                        info.setCustName(ParamUtils.cusName);
                        info.setCertType(ParamUtils.certType);
                        info.setCertNo(ParamUtils.certId);
                        info.setSignaccount(ParamUtils.cardNum);
                        info.setServiceProject(ParamUtils.serviceProject);
                        info.setBranchNo(ParamUtils.orgId);
                        info.setTerminalId(ParamUtils.terminalId);
                        info.setTellerId(ParamUtils.tellerId);
                        info.setPrintVoucherType(5);
                        info.setSaveDate(DatetimeUtil.getToday2());
                        kjdb.save(info);
                    }
                    searchDialog.dismiss();
                    ToastUtil.showLongToast(context,"凭单信息已保存，请退出当前页");
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
            lp.width =680;
            lp.height = 470;
            searchDialog.getWindow().setAttributes(lp);
            searchDialog.setCanceledOnTouchOutside(true);*/

        } else {
            Log.i("---","BlutToothConnectTool---close");
            myOpertion.close();
            //myOpertion = null;
            //mPrinter = null;
        }
    }
}
