package com.cherry.mo.pmc.form;


public class BINOLMOPMC02_Form {
	/** 菜单组ID*/
	private String menuGrpID;
	
	/** 菜单组是否过期标记*/
	private String pastStatus;
	
	/** 菜单ID*/
	private String posMenuID;
	
	/** 菜单显示状态*/
	private String menuStatus;
	
	/**与品牌菜单配置有差异的节点*/
	private String diffentNodesArray;
	
	/** 菜单提示下拉框显示条数 */
	private String number;
	
	/** 菜单提示STR*/
	private String menuInfoStr;
	
	/** 机器类型*/
	private String machineType;
	
	public String getMenuGrpID() {
		return menuGrpID;
	}

	public void setMenuGrpID(String menuGrpID) {
		this.menuGrpID = menuGrpID;
	}

	public String getPosMenuID() {
		return posMenuID;
	}

	public void setPosMenuID(String posMenuID) {
		this.posMenuID = posMenuID;
	}

	public String getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(String menuStatus) {
		this.menuStatus = menuStatus;
	}

	public String getDiffentNodesArray() {
		return diffentNodesArray;
	}

	public void setDiffentNodesArray(String diffentNodesArray) {
		this.diffentNodesArray = diffentNodesArray;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMenuInfoStr() {
		return menuInfoStr;
	}

	public void setMenuInfoStr(String menuInfoStr) {
		this.menuInfoStr = menuInfoStr;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public String getPastStatus() {
		return pastStatus;
	}

	public void setPastStatus(String pastStatus) {
		this.pastStatus = pastStatus;
	}
}
