/*
 * @(#)BINOLPLSCF15_BL.java     1.0 2011/12/19
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

package com.cherry.pl.scf.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.common.interfaces.BINOLPLCOM02_IF;
import com.cherry.pl.scf.interfaces.BINOLPLSCF15_IF;
import com.cherry.pl.scf.service.BINOLPLSCF15_Service;

/**
 * 业务流程配置BL
 * 
 * @author niushunjie
 * @version 1.0 2011.12.19
 */
public class BINOLPLSCF15_BL implements BINOLPLSCF15_IF{
    @Resource
    private CodeTable code;
    
    @Resource
    private BINOLPLCOM02_IF binOLPLCOM02_BL;
    
    @Resource
    private BINOLPLSCF15_Service binOLPLSCF15_Service;
    
    /**
     * 取得业务流程配置一览
     * @param map
     * @return
     */
    @Override
    public List<Map<String,Object>> getFlowList(Map<String, Object> map) {
        List<Map<String,Object>> fileStorelist = binOLPLSCF15_Service.getFileStoreList(map);
        List<Map<String, Object>> workflowInfoList = code.getCodes("1154");
        //status=0 系统默认，status=1 自定义
        String status = "";
        for(int i=0;i<workflowInfoList.size();i++){
            String flowType = ConvertUtil.getString(workflowInfoList.get(i).get("CodeKey"));
            String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
            status = "0";
            for(int j=0;j<fileStorelist.size();j++){
                String fileCode = ConvertUtil.getString(fileStorelist.get(j).get("FileCode"));
                if (flowFileName.equals(fileCode)){
                    status = "1";
                    break;
                }
            }
            workflowInfoList.get(i).put("configStatus", status);
        }
        
        
        return workflowInfoList;
    }
	

}
