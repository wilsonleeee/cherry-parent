/*	
 * @(#)BINOLSTCM18_Form     1.0 2013/09/02		
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
package com.cherry.st.common.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 产品入库单弹出table共通Form
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
public class BINOLSTCM18_Form extends DataTable_BaseForm{
	/**入库单ID*/
    private String inDepotID;
    
    /**入库单号*/
    private String billNoIF;
    
    /**入库部门ID*/
    private String organizationID;
    
    /**入库实体仓库ID*/
    private String depotInfoId;
    
    /**入库逻辑仓库ID*/
    private String logicDepotInfoId;
    
    /**入库单List*/
    private List<Map<String,Object>> inDepotList;

    public String getInDepotID() {
        return inDepotID;
    }

    public void setInDepotID(String inDepotID) {
        this.inDepotID = inDepotID;
    }

    public String getBillNoIF() {
        return billNoIF;
    }

    public void setBillNoIF(String billNoIF) {
        this.billNoIF = billNoIF;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getDepotInfoId() {
        return depotInfoId;
    }

    public void setDepotInfoId(String depotInfoId) {
        this.depotInfoId = depotInfoId;
    }

    public String getLogicDepotInfoId() {
        return logicDepotInfoId;
    }

    public void setLogicDepotInfoId(String logicDepotInfoId) {
        this.logicDepotInfoId = logicDepotInfoId;
    }

    public List<Map<String, Object>> getInDepotList() {
        return inDepotList;
    }

    public void setInDepotList(List<Map<String, Object>> inDepotList) {
        this.inDepotList = inDepotList;
    }
}