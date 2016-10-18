/*  
 * @(#)CherryTaskInstance.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.cmbeans;

public class CherryTaskInstance {
	/**工作流     实例ID*/
	private String entryID;
	
	/**工作流   任务名称*/
	private String taskName;
	
	/**工作流   当前操作编码*/
	private String currentOperate;
	
	/**工作流   单据ID*/
	private String billID;
	
	/**工作流   单据号*/
	private String billCode;
	
	/**工作流   业务类型*/
	private String billType;

	/**工作流   产品类型*/
	private String proType;
	
	/**工作流 单据创建者*/
	private String billCreator;
	
	/**工作流 创建者用户ID*/
	private String billCreator_User;
	
	/**工作流 创建者岗位ID*/
	private String billCreator_Position;
	
	/**工作流 创建者部门ID*/
	private String billCreator_Depart;
	
	/** 发起方部门代码名称 */
	private String departCodeNameFrom;
	
	/** 接受方部门代码名称 */
	private String departCodeNameTo;
	
	/** 总数量 */
	private String totalQuantity;
	
	/** 总金额 */
	private String totalAmount;
	
	/**
	 * 员工代码名称
	 */
	private String employeeCodeName;
	
	/**
	 * 语言
	 */
	private String language;
	/**
	 * @return the entryID
	 */
	public String getEntryID() {
		return entryID;
	}

	/**
	 * @param entryID the entryID to set
	 */
	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the currentOperate
	 */
	public String getCurrentOperate() {
		return currentOperate;
	}

	/**
	 * @param currentOperate the currentOperate to set
	 */
	public void setCurrentOperate(String currentOperate) {
		this.currentOperate = currentOperate;
	}

	/**
	 * @return the billID
	 */
	public String getBillID() {
		return billID;
	}

	/**
	 * @param billID the billID to set
	 */
	public void setBillID(String billID) {
		this.billID = billID;
	}

	/**
	 * @return the billCode
	 */
	public String getBillCode() {
		return billCode;
	}

	/**
	 * @param billCode the billCode to set
	 */
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	/**
	 * @return the billType
	 */
	public String getBillType() {
		return billType;
	}

	/**
	 * @param billType the billType to set
	 */
	public void setBillType(String billType) {
		this.billType = billType;
	}

	/**
	 * @return the proType
	 */
	public String getProType() {
		return proType;
	}

	/**
	 * @param proType the proType to set
	 */
	public void setProType(String proType) {
		this.proType = proType;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the departCodeName
	 */
	public String getDepartCodeNameFrom() {
		return departCodeNameFrom;
	}

	/**
	 * @param departCodeName the departCodeName to set
	 */
	public void setDepartCodeNameFrom(String departCodeNameFrom) {
		this.departCodeNameFrom = departCodeNameFrom;
	}

	/**
	 * @return the employeeCodeName
	 */
	public String getEmployeeCodeName() {
		return employeeCodeName;
	}

	/**
	 * @param employeeCodeName the employeeCodeName to set
	 */
	public void setEmployeeCodeName(String employeeCodeName) {
		this.employeeCodeName = employeeCodeName;
	}

    public String getBillCreator() {
        return billCreator;
    }

    public void setBillCreator(String billCreator) {
        this.billCreator = billCreator;
    }

    public String getBillCreator_User() {
        return billCreator_User;
    }

    public void setBillCreator_User(String billCreatorUser) {
        billCreator_User = billCreatorUser;
    }

    public String getBillCreator_Position() {
        return billCreator_Position;
    }

    public void setBillCreator_Position(String billCreatorPosition) {
        billCreator_Position = billCreatorPosition;
    }

    public String getBillCreator_Depart() {
        return billCreator_Depart;
    }

    public void setBillCreator_Depart(String billCreatorDepart) {
        billCreator_Depart = billCreatorDepart;
    }

    public String getDepartCodeNameTo() {
        return departCodeNameTo;
    }

    public void setDepartCodeNameTo(String departCodeNameTo) {
        this.departCodeNameTo = departCodeNameTo;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
