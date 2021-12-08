/*    */ package com.dysen.socket_library.msg;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/*    */ 
/*    */ /*    */ 
/*    */ 
/*    */ 
/*    */ public class RemoteAuthorization
{
	  ActiveXComponent com;
	  Dispatch disp;
	  private static RemoteAuthorization instance = new RemoteAuthorization();

	  public static RemoteAuthorization getInstance() {
	    return instance;
	  }

	  private RemoteAuthorization() {
	    this.com = new ActiveXComponent("RACLIENTOCX.RAClientOcxCtrl.1");
	    this.disp = this.com.getObject();
	  }

	  public String RaActiveSQ(String BranchNo, String TellerNum, String TellerName, String TransCode, String TxLs, String SqLevel, String SqAmount, String SqPoint, String TxInfo)
	  {
	    String result = Dispatch.call(this.disp, "RaActiveSQ", new Object[] { new Variant(BranchNo), new Variant(TellerNum), new Variant(TellerName), new Variant(TransCode), new Variant(TxLs), new Variant(SqLevel), new Variant(SqAmount), new Variant(SqPoint), new Variant(TxInfo) }).getString();
	    return result;
	  }

	  public String RaCancelSQ(String TxLs)
	  {
	    String result = Dispatch.call(this.disp, "RaCancelSQ", new Object[] { new Variant(TxLs) }).getString();
	    return result;
	  }

	  public String RaGetResultSQ(String TxLs)
	  {
	    String result = Dispatch.call(this.disp, "RaGetResultSQ", new Object[] { new Variant(TxLs) }).getString();
	    return result;
	  }
	}

/* Location:           F:\鐢靛瓙閾惰寮�鍙戝洟闃焅鍓崱e鍔╂墜\HBNX_BusinessSystem\HBNX_BusinessSystem.jar
 * Qualified Name:     com.centerm.msg.RemoteAuthorization
 * JD-Core Version:    0.5.4
 */