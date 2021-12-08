package com.dysen.opencard.common.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by hutian on 2018/6/22.
 * 手机银行签约
 */
@Table("MobileBankSign")
public class MobileBankSign implements Serializable{

	// 指定自增，每个对象需要有一个主键
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	@Column("id") // 指定列名
	private int id;
	@Column("transDate")
	private String transDate;
	@Column("custName")
	private String custName;
	@Column("certType")
	private String certType;
	@Column("certNo")
	private String certNo;
	@Column("cardNo")
	private String cardNo;
	@Column("signaccount")
	private String signaccount;
	@Column("serviceProject")
	private String serviceProject;
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

	public MobileBankSign setId(int id) {
		this.id = id;
		return this;
	}

	public String getTransDate() {
		return transDate;
	}

	public MobileBankSign setTransDate(String transDate) {
		this.transDate = transDate;
		return this;
	}

	public String getCustName() {
		return custName;
	}

	public MobileBankSign setCustName(String custName) {
		this.custName = custName;
		return this;
	}

	public String getCertType() {
		return certType;
	}

	public MobileBankSign setCertType(String certType) {
		this.certType = certType;
		return this;
	}

	public String getCertNo() {
		return certNo;
	}

	public MobileBankSign setCertNo(String certNo) {
		this.certNo = certNo;
		return this;
	}

	public String getCardNo() {
		return cardNo;
	}

	public MobileBankSign setCardNo(String cardNo) {
		this.cardNo = cardNo;
		return this;
	}

	public String getSignaccount() {
		return signaccount;
	}

	public MobileBankSign setSignaccount(String signaccount) {
		this.signaccount = signaccount;
		return this;
	}

	public String getServiceProject() {
		return serviceProject;
	}

	public MobileBankSign setServiceProject(String serviceProject) {
		this.serviceProject = serviceProject;
		return this;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public MobileBankSign setBranchNo(String branchNo) {
		this.branchNo = branchNo;
		return this;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public MobileBankSign setTerminalId(String terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public MobileBankSign setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public int getPrintType() {
		return printType;
	}

	public MobileBankSign setPrintType(int printType) {
		this.printType = printType;
		return this;
	}

	public int getPrintTime() {
		return printTime;
	}

	public MobileBankSign setPrintTime(int printTime) {
		this.printTime = printTime;
		return this;
	}
}
