package com.dysen.opencard.common.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by hutian on 2018/6/22.
 * 开卡 实体表
 */
@Table("OpenCard")
public class OpenCard implements Serializable{

	// 指定自增，每个对象需要有一个主键
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	@Column("id") // 指定列名
	private int id;
	@Column("transDate")
	private String transDate;
	@Column("custId")
	private String custId;
	@Column("custName")
	private String custName;
	@Column("ICCardNo")
	private String ICCardNo;//卡号
	@Column("accountNo")
	private String accountNo;//账号
	@Column("cardName")
	private String cardName;//卡产品名称
	@Column("branchNo")
	private String branchNo;//交易网点
	@Column("terminalId")
	private String terminalId;
	@Column("userId")
	private String userId;//柜员号
	@Column("printType")
	private int printType;//补打类型
	@Column("printTime")
	private int printTime;//补打次数

	public int getId() {
		return id;
	}

	public OpenCard setId(int id) {
		this.id = id;
		return this;
	}

	public String getTransDate() {
		return transDate;
	}

	public OpenCard setTransDate(String transDate) {
		this.transDate = transDate;
		return this;
	}

	public String getCustId() {
		return custId;
	}

	public OpenCard setCustId(String custId) {
		this.custId = custId;
		return this;
	}

	public String getCustName() {
		return custName;
	}

	public OpenCard setCustName(String custName) {
		this.custName = custName;
		return this;
	}

	public String getICCardNo() {
		return ICCardNo;
	}

	public OpenCard setICCardNo(String ICCardNo) {
		this.ICCardNo = ICCardNo;
		return this;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public OpenCard setAccountNo(String accountNo) {
		this.accountNo = accountNo;
		return this;
	}

	public String getCardName() {
		return cardName;
	}

	public OpenCard setCardName(String cardName) {
		this.cardName = cardName;
		return this;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public OpenCard setBranchNo(String branchNo) {
		this.branchNo = branchNo;
		return this;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public OpenCard setTerminalId(String terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public OpenCard setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public int getPrintType() {
		return printType;
	}

	public OpenCard setPrintType(int printType) {
		this.printType = printType;
		return this;
	}

	public int getPrintTime() {
		return printTime;
	}

	public OpenCard setPrintTime(int printTime) {
		this.printTime = printTime;
		return this;
	}
}
