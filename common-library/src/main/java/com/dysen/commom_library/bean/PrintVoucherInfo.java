package com.dysen.commom_library.bean;

import java.io.Serializable;

/**
 * 凭单打印实体类（汇总）
 * Created by Administrator on 2018/5/9.
 */

public class PrintVoucherInfo implements Serializable{
    //String custId, String custName, String certType, String certNo, String phoneNo, String branchNo, String terminalId, String userId
    private int id;
    private String custId;
    private String custName;
    private String certType;
    private String certNo;
    private String phoneNo;
    private String branchNo;
    private String terminalId;
    private String tellerId;
    private String userId;
    private int printType;
    private String ICCardNo;
    private String accountNo;
    private String cardName;
    private String signCardNo;
    private String chargeStandard;
    private String chargeCardNo;
    private String productType;
    private String whetherShowLeft;
    private String limitChangeLowest;
    private String limitWarnLowest;
    private String cardNo;
    private String signaccount;
    private String serviceProject;
    private int printVoucherType;
    private String saveDate;
    private int printTime;
    private String signCardNum;

    public String getSignCardNum() {
        return signCardNum;
    }

    public void setSignCardNum(String signCardNum) {
        this.signCardNum = signCardNum;
    }

    public int getPrintTime() {
        return printTime;
    }

    public void setPrintTime(int printTime) {
        this.printTime = printTime;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTellerId() {
        return tellerId;
    }

    public void setTellerId(String tellerId) {
        this.tellerId = tellerId;
    }

    public String getSignaccount() {
        return signaccount;
    }

    public void setSignaccount(String signaccount) {
        this.signaccount = signaccount;
    }

    public String getServiceProject() {
        return serviceProject;
    }

    public void setServiceProject(String serviceProject) {
        this.serviceProject = serviceProject;
    }

    public int getPrintVoucherType() {
        return printVoucherType;
    }

    public void setPrintVoucherType(int printVoucherType) {
        this.printVoucherType = printVoucherType;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPrintType() {
        return printType;
    }

    public void setPrintType(int printType) {
        this.printType = printType;
    }

    public String getICCardNo() {
        return ICCardNo;
    }

    public void setICCardNo(String ICCardNo) {
        this.ICCardNo = ICCardNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getSignCardNo() {
        return signCardNo;
    }

    public void setSignCardNo(String signCardNo) {
        this.signCardNo = signCardNo;
    }

    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public String getChargeCardNo() {
        return chargeCardNo;
    }

    public void setChargeCardNo(String chargeCardNo) {
        this.chargeCardNo = chargeCardNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getWhetherShowLeft() {
        return whetherShowLeft;
    }

    public void setWhetherShowLeft(String whetherShowLeft) {
        this.whetherShowLeft = whetherShowLeft;
    }

    public String getLimitChangeLowest() {
        return limitChangeLowest;
    }

    public void setLimitChangeLowest(String limitChangeLowest) {
        this.limitChangeLowest = limitChangeLowest;
    }

    public String getLimitWarnLowest() {
        return limitWarnLowest;
    }

    public void setLimitWarnLowest(String limitWarnLowest) {
        this.limitWarnLowest = limitWarnLowest;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    //int printType, String custId, String custName, String ICCardNo, String accountNo, String cardName, String branchNo, String terminalId, String userId
    //int printType, String signCardNo, String certType, String certNo, String chargeCardNo, String productType, String custName, String phoneNo,
    //String chargeStandard, Double limitChangeLowest, Double limitWarnLowest, String whetherShowLeft, String branchNo, String terminalId, String userId
    //String custId, String custName, String cardNo, String cardName, String branchNo, String terminalId, String userId
}
