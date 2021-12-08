package com.dysen.opencard.common.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by hutian on 2018/6/22.
 * 开户 实体表 个人客户基本信息
 */
@Table("CreateAccount")
public class CreateAccount implements Serializable{

	// 指定自增，每个对象需要有一个主键
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	@Column("id") // 指定列名
	private int id;
	@Column("transDate")
	private String transDate;
	@Column("custId")
	private String custId;  //唯一
	@Column("custName")
	private String custName;
	@Column("certType")
	private String certType;
	@Column("certNo")
	private String certNo;
	@Column("phoneNo")
	private String phoneNo;
	@Column("branchNo")
	private String branchNo;//交易网点
	@Column("terminalId")
	private String terminalId;//终端号
	@Column("userId")
	private String userId;//柜员号
	@Column("printType")
	private int printType;//补打类型
	@Column("printTime")
	private int printTime;//补打次数

	public int getId() {
		return id;
	}

	public CreateAccount setId(int id) {
		this.id = id;
		return this;
	}

	public String getTransDate() {
		return transDate;
	}

	public CreateAccount setTransDate(String transDate) {
		this.transDate = transDate;
		return this;
	}

	public String getCustId() {
		return custId;
	}

	public CreateAccount setCustId(String custId) {
		this.custId = custId;
		return this;
	}

	public String getCustName() {
		return custName;
	}

	public CreateAccount setCustName(String custName) {
		this.custName = custName;
		return this;
	}

	public String getCertType() {
		return certType;
	}

	public CreateAccount setCertType(String certType) {
		this.certType = certType;
		return this;
	}

	public String getCertNo() {
		return certNo;
	}

	public CreateAccount setCertNo(String certNo) {
		this.certNo = certNo;
		return this;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public CreateAccount setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
		return this;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public CreateAccount setBranchNo(String branchNo) {
		this.branchNo = branchNo;
		return this;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public CreateAccount setTerminalId(String terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public CreateAccount setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public int getPrintType() {
		return printType;
	}

	public CreateAccount setPrintType(int printType) {
		this.printType = printType;
		return this;
	}

	public int getPrintTime() {
		return printTime;
	}

	public CreateAccount setPrintTime(int printTime) {
		this.printTime = printTime;
		return this;
	}
}
