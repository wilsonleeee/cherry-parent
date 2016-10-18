package com.cherry.mo.pmc.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOPMC01_Form extends DataTable_BaseForm {
	
	/** 用于查询的菜单组名称*/
	private String menuGrpNameSearch;
	
	/** 菜单组名称 */
	private String menuGrpName;
	
	/** 菜单组ID*/
	private String menuGrpID;
	
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;
	
	/** 柜台选择模式(1：按区域，2：按渠道) */
	private String selMode;
	
	/** 大区ID */
	private String channelRegionId;
	
	/** 当前菜单组ID*/
	private String currentMenuGrpID;
	
	/** 生效开始日期*/
	private String startDate;
	
	/** 生效结束日期*/
	private String endDate;
	
	/** 发布开始日期*/
	private String startPublishDate;
	
	/** 发布结束日期*/
	private String endPublishDate;
	
	/**机器类型*/
	private String machineType;

	public String getMenuGrpName() {
		return menuGrpName;
	}

	public void setMenuGrpName(String menuGrpName) {
		this.menuGrpName = menuGrpName;
	}

	public String getMenuGrpID() {
		return menuGrpID;
	}

	public void setMenuGrpID(String menuGrpID) {
		this.menuGrpID = menuGrpID;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public String getSelMode() {
		return selMode;
	}

	public void setSelMode(String selMode) {
		this.selMode = selMode;
	}

	public String getChannelRegionId() {
		return channelRegionId;
	}

	public void setChannelRegionId(String channelRegionId) {
		this.channelRegionId = channelRegionId;
	}

	public String getCurrentMenuGrpID() {
		return currentMenuGrpID;
	}

	public void setCurrentMenuGrpID(String currentMenuGrpID) {
		this.currentMenuGrpID = currentMenuGrpID;
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

	public String getStartPublishDate() {
		return startPublishDate;
	}

	public void setStartPublishDate(String startPublishDate) {
		this.startPublishDate = startPublishDate;
	}

	public String getEndPublishDate() {
		return endPublishDate;
	}

	public void setEndPublishDate(String endPublishDate) {
		this.endPublishDate = endPublishDate;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public String getMenuGrpNameSearch() {
		return menuGrpNameSearch;
	}

	public void setMenuGrpNameSearch(String menuGrpNameSearch) {
		this.menuGrpNameSearch = menuGrpNameSearch;
	}

}
