package com.cherry.wp.wy.wyl.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWYWYL01_Form extends DataTable_BaseForm{
	
	// 品牌ID
	private String brandInfoId;
	
	// 活动查询字符串
	private String subCampInfoStr;
	
	// 显示数量
	private String number;
	
	// 活动状态
	private String state;
	
	// 申领单据列表
	private List<Map<String, Object>> billList;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getSubCampInfoStr() {
		return subCampInfoStr;
	}

	public void setSubCampInfoStr(String subCampInfoStr) {
		this.subCampInfoStr = subCampInfoStr;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Map<String, Object>> getBillList() {
		return billList;
	}

	public void setBillList(List<Map<String, Object>> billList) {
		this.billList = billList;
	}
	
}
