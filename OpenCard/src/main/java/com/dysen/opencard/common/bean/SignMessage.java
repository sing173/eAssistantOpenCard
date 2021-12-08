package com.dysen.opencard.common.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by hutian on 2018/6/22.
 * 卡短信签约实体表
 */
@Table("SignMessage")
public class SignMessage implements Serializable{

	// 指定自增，每个对象需要有一个主键
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	@Column("id") // 指定列名
	private int id;
	@Column("transDate")
	private String transDate;
	@Column("signCardNum")
	private String signCardNum;
	@Column("certType")
	private String certType;
	@Column("certNo")
	private String certNo;
	@Column("productType")
	private String productType;
	@Column("custName")
	private String custName;
	@Column("chargeCardNo")
	private String chargeCardNo;//收费卡号
	@Column("mobileNums")
	private String mobileNums;
	@Column("chargetandards")
	private String chargetandards;//收费标准
	@Column("balanceChargeLowerlimit")
	private String balanceChargeLowerlimit;//
	@Column("balanceRemindLowerlimit")
	private String balanceRemindLowerlimit;
	@Column("isShowFree")
	private String isShowFree;
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

	public SignMessage setId(int id) {
		this.id = id;
		return this;
	}

	public String getTransDate() {
		return transDate;
	}

	public SignMessage setTransDate(String transDate) {
		this.transDate = transDate;
		return this;
	}

	public String getSignCardNum() {
		return signCardNum;
	}

	public SignMessage setSignCardNum(String signCardNum) {
		this.signCardNum = signCardNum;
		return this;
	}

	public String getCertType() {
		return certType;
	}

	public SignMessage setCertType(String certType) {
		this.certType = certType;
		return this;
	}

	public String getCertNo() {
		return certNo;
	}

	public SignMessage setCertNo(String certNo) {
		this.certNo = certNo;
		return this;
	}

	public String getProductType() {
		return productType;
	}

	public SignMessage setProductType(String productType) {
		this.productType = productType;
		return this;
	}

	public String getCustName() {
		return custName;
	}

	public SignMessage setCustName(String custName) {
		this.custName = custName;
		return this;
	}

	public String getChargeCardNo() {
		return chargeCardNo;
	}

	public SignMessage setChargeCardNo(String chargeCardNo) {
		this.chargeCardNo = chargeCardNo;
		return this;
	}

	public String getMobileNums() {
		return mobileNums;
	}

	public SignMessage setMobileNums(String mobileNums) {
		this.mobileNums = mobileNums;
		return this;
	}

	public String getChargetandards() {
		return chargetandards;
	}

	public SignMessage setChargetandards(String chargetandards) {
		this.chargetandards = chargetandards;
		return this;
	}

	public String getBalanceChargeLowerlimit() {
		return balanceChargeLowerlimit;
	}

	public SignMessage setBalanceChargeLowerlimit(String balanceChargeLowerlimit) {
		this.balanceChargeLowerlimit = balanceChargeLowerlimit;
		return this;
	}

	public String getBalanceRemindLowerlimit() {
		return balanceRemindLowerlimit;
	}

	public SignMessage setBalanceRemindLowerlimit(String balanceRemindLowerlimit) {
		this.balanceRemindLowerlimit = balanceRemindLowerlimit;
		return this;
	}

	public String getIsShowFree() {
		return isShowFree;
	}

	public SignMessage setIsShowFree(String isShowFree) {
		this.isShowFree = isShowFree;
		return this;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public SignMessage setBranchNo(String branchNo) {
		this.branchNo = branchNo;
		return this;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public SignMessage setTerminalId(String terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public SignMessage setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public int getPrintType() {
		return printType;
	}

	public SignMessage setPrintType(int printType) {
		this.printType = printType;
		return this;
	}

	public int getPrintTime() {
		return printTime;
	}

	public SignMessage setPrintTime(int printTime) {
		this.printTime = printTime;
		return this;
	}
}
