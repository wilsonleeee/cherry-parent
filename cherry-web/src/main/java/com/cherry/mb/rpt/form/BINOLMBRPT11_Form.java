package com.cherry.mb.rpt.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 扫粉关注报表
 * 
 * @author Hujh
 * @version 2015-11-11
 */
public class BINOLMBRPT11_Form extends DataTable_BaseForm {
	
	private List<Map<String, Object>> subscribeList;
	
	/** 柜台 */
	private String counter;
	
	/** BAS */
	private String counterBAS;
	
	/** 代理商 */
	private String agencyName;
	
	/** 是否排除不含参数 "1":排除*/
	private String keyFlag;
	
	/** 是否只显示首次关注"1":排除*/
	private String firstFlag;
	
	private String openID;
	
	private String subscribeEventKey;
	
	/** 关注开始时间 */
	private String startDate;
	
	/** 结束时间 */
	private String endDate;
	
	private String holidays;
	
	private String exportType;
	
	private String number;
	
	public List<Map<String, Object>> getSubscribeList() {
		return subscribeList;
	}

	public void setSubscribeList(List<Map<String, Object>> subscribeList) {
		this.subscribeList = subscribeList;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getCounterBAS() {
		return counterBAS;
	}

	public void setCounterBAS(String counterBAS) {
		this.counterBAS = counterBAS;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(String keyFlag) {
		this.keyFlag = keyFlag;
	}

	public String getFirstFlag() {
		return firstFlag;
	}

	public void setFirstFlag(String firstFlag) {
		this.firstFlag = firstFlag;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getSubscribeEventKey() {
		return subscribeEventKey;
	}

	public void setSubscribeEventKey(String subscribeEventKey) {
		this.subscribeEventKey = subscribeEventKey;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
