package com.cherry.mo.pmc.form;

public class BINOLMOPMC03_Form {

	/** 菜单组ID*/
	private String menuGrpID;
	
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;
	
	/** 选择的柜台节点 */
	private String checkNodesArray;
	
	/**柜台树显示模式*/
	private String selMode;
	
	/** 大区ID */
	private String channelRegionId;
	
	/** 生效开始日期*/
	private String startDate;
	
	/** 生效结束日期*/
	private String endDate;
	
	/** 机器类型*/
	private String machineType;

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

	public String getCheckNodesArray() {
		return checkNodesArray;
	}

	public void setCheckNodesArray(String checkNodesArray) {
		this.checkNodesArray = checkNodesArray;
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

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
}
