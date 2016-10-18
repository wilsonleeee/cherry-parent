/*  
 * @(#)BINOLSSCM06_Form.java     1.0 2011/11/21      
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
package com.cherry.ss.common.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 促销产品发货单弹出table共通Form
 * @author niushunjie
 *
 */
public class BINOLSSCM06_Form extends DataTable_BaseForm{
    /**发货单ID*/
    private String deliverId;
    
    /**发货单号*/
    private String deliverRecNo;
    
    /**发货部门ID*/
    private String organizationId;
    
    /**发货实体仓库ID*/
    private String depotInfoId;
    
    /**发货逻辑仓库ID*/
    private String logicDepotInfoId;
    
    /**发货单List*/
    private List<Map<String,Object>> deliverList;

    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }

    public String getDeliverRecNo() {
        return deliverRecNo;
    }

    public void setDeliverRecNo(String deliverRecNo) {
        this.deliverRecNo = deliverRecNo;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDepotInfoId() {
        return depotInfoId;
    }

    public void setDepotInfoId(String depotInfoId) {
        this.depotInfoId = depotInfoId;
    }

    public List<Map<String, Object>> getDeliverList() {
        return deliverList;
    }

    public void setDeliverList(List<Map<String, Object>> deliverList) {
        this.deliverList = deliverList;
    }

    public String getLogicDepotInfoId() {
        return logicDepotInfoId;
    }

    public void setLogicDepotInfoId(String logicDepotInfoId) {
        this.logicDepotInfoId = logicDepotInfoId;
    }

}
