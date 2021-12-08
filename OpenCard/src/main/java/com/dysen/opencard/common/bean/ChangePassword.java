package com.dysen.opencard.common.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by hutian on 2018/6/22.
 * 卡密码修改服务回执
 */
@Table("ChangePassword")
public class ChangePassword implements Serializable{

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
	@Column("cardNo")
	private String cardNo;
	@Column("cardProductName")
	private String cardProductName;
	@Column("branchNo")
	private String branchNo;//交易网点
	@Column("terminalId")
	private String terminalId;//终端编号
	@Column("userId")
	private String userId;//柜员号
	@Column("printType")
	private int printType;//补打类型
	@Column("printTime")
	private int printTime;//补打次数

	public int getId() {
		return id;
	}

	public ChangePassword setId(int id) {
		this.id = id;
		return this;
	}

	public String getTransDate() {
		return transDate;
	}

	public ChangePassword setTransDate(String transDate) {
		this.transDate = transDate;
		return this;
	}

	public String getCustId() {
		return custId;
	}

	public ChangePassword setCustId(String custId) {
		this.custId = custId;
		return this;
	}

	public String getCustName() {
		return custName;
	}

	public ChangePassword setCustName(String custName) {
		this.custName = custName;
		return this;
	}

	public String getCardNo() {
		return cardNo;
	}

	public ChangePassword setCardNo(String cardNo) {
		this.cardNo = cardNo;
		return this;
	}

	public String getCardProductName() {
		return cardProductName;
	}

	public ChangePassword setCardProductName(String cardProductName) {
		this.cardProductName = cardProductName;
		return this;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public ChangePassword setBranchNo(String branchNo) {
		this.branchNo = branchNo;
		return this;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public ChangePassword setTerminalId(String terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public ChangePassword setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public int getPrintType() {
		return printType;
	}

	public ChangePassword setPrintType(int printType) {
		this.printType = printType;
		return this;
	}

	public int getPrintTime() {
		return printTime;
	}

	public ChangePassword setPrintTime(int printTime) {
		this.printTime = printTime;
		return this;
	}
}
