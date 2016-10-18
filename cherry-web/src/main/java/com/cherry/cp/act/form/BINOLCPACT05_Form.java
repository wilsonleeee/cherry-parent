package com.cherry.cp.act.form;

import java.io.File;

public class BINOLCPACT05_Form{

	// 预约方式
	private String orderMode;
	
	// 预约柜台号
	private String orderCntCode;
	
	private String gotCounter;
	
	// 主题活动ID
	private int campaignId;
	
	private String campMebJson;
	
	//档次Code
	private String subCampCode;
	
	//预约次数
	private int times;
	
	//会员ID
	private int[] memId;
	
	//会员卡号
	private String[] memCode;
	
	/** 上传的文件 */
	private File upExcel;

	public String getOrderMode() {
		return orderMode;
	}

	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}
	
	public String getOrderCntCode() {
		return orderCntCode;
	}

	public void setOrderCntCode(String orderCntCode) {
		this.orderCntCode = orderCntCode;
	}

	public String getGotCounter() {
		return gotCounter;
	}

	public void setGotCounter(String gotCounter) {
		this.gotCounter = gotCounter;
	}

	public String getSubCampCode() {
		return subCampCode;
	}

	public void setSubCampCode(String subCampCode) {
		this.subCampCode = subCampCode;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int[] getMemId() {
		return memId;
	}

	public void setMemId(int[] memId) {
		this.memId = memId;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String[] getMemCode() {
		return memCode;
	}

	public void setMemCode(String[] memCode) {
		this.memCode = memCode;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampMebJson() {
		return campMebJson;
	}

	public void setCampMebJson(String campMebJson) {
		this.campMebJson = campMebJson;
	}
}
