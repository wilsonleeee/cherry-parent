/*
 * @(#)BINBESSPRO04_Action.java     1.0 2012/04/20
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
package com.cherry.ss.pro.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.pro.interfaces.BINBESSPRO04_IF;

/**
 * 
 * 平产品库存ACTION
 * 
 * 
 * @author niushunjie
 * @version 1.0 2012.04.20
 */
public class BINBESSPRO04_Action extends BaseAction {

    private static final long serialVersionUID = -9157554349861065254L;
    
    @Resource
    private BINBESSPRO04_IF binBESSPRO04_BL;

    private static Logger logger = LoggerFactory.getLogger(BINBESSPRO04_Action.class.getName());
    
    /** 品牌Id */
    private String brandInfoId;
    
    private String organizationID;
    
    private String inventoryInfoID;
    
    private String logicInventoryInfoID;
    
    private String departCodeName;
    
    private String depotCodeName;
    
    private String logicCodeName;
    
    private String stockInOutDate;
    
    /**是否测试仓库*/
    private String testType;
    
    
    /**
     * 初始化
     * @return
     * @throws Exception
     */
    public String init() throws Exception {
        return SUCCESS;
    }
    
    /**
     * 初始化返回部门仓库
     * @throws Exception 
     */
    public void getDepart() throws Exception{
        //登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", brandInfoId);
        param.put("TestType", testType);
        List<Map<String,Object>> list = binBESSPRO04_BL.getDepotInfoList(param);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 初始化显示异常数据
     * @return
     * @throws Exception 
     */
    public String getDiff() throws Exception{
        //登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_OrganizationID", organizationID);
        map.put("BIN_InventoryInfoID",inventoryInfoID);
        map.put("BIN_LogicInventoryInfoID",logicInventoryInfoID);
        map.put("departCodeName", departCodeName);
        map.put("depotCodeName", depotCodeName);
        map.put("logicCodeName", logicCodeName);
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoId", brandInfoId);
        
        try{
            List<Map<String,Object>> list = binBESSPRO04_BL.getDiffStockList(map);
            ConvertUtil.setResponseByAjax(response, list);
        }catch(Exception e){
            this.addActionError("获取库存差异失败");
            return "error";
        }
        
        return null;
    }
    
    /**
     * 执行插入出库表
     * @return
     * @throws Exception
     */
    public String binBESSPRO04Exec() throws Exception {
        logger.info("******************************BINBESSPRO04处理开始***************************");
        // 设置batch处理标志
        int flg = CherryBatchConstants.BATCH_SUCCESS;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 登陆用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryBatchConstants.SESSION_USERINFO);
            // 所属组织
            map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
            // 品牌Id
            map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
            //是否测试仓库
            map.put("TestType", testType);
            //入出库日期
            map.put("stockInOutDate", stockInOutDate);
            flg = binBESSPRO04_BL.tran_insert(map);
        } catch (CherryBatchException cbx) {
            flg = CherryBatchConstants.BATCH_WARNING;
        } catch (Exception e) {
            e.printStackTrace();
            flg = CherryBatchConstants.BATCH_ERROR;
        } finally {
            if (flg == CherryBatchConstants.BATCH_SUCCESS) {
                this.addActionMessage("平产品库存处理正常终了");
                logger.info("******************************BINBESSPRO04处理正常终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_WARNING) {
                this.addActionError("平产品库存处理警告终了");
                logger.info("******************************BINBESSPRO04处理警告终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_ERROR) {
                this.addActionError("平产品库存处理异常终了");
                logger.info("******************************BINBESSPRO04处理异常终了***************************");
            }
        }
        
        return SUCCESS;
    }
    
    public void export(){
        // 取得参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryBatchConstants.SESSION_USERINFO);
        // 所属组织
        map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 品牌Id
        map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
        //是否测试仓库
        map.put("TestType", testType);
        String language = "zh_CN";
        map.put(CherryBatchConstants.SESSION_LANGUAGE, language);
        try {
            response.setContentType("application/vnd.excel");
            response.setCharacterEncoding(CherryConstants.CHAR_ENCODING);
            String exportName = binBESSPRO04_BL.getResourceValue("BINBESSPRO04", language, "downloadFileName");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(exportName.getBytes("GBK"),"ISO8859-1"));
            byte[] excel = binBESSPRO04_BL.exportExcel(map);
            response.getOutputStream().write(excel);
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            logger.error(e.getMessage(),e);
        }
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getInventoryInfoID() {
        return inventoryInfoID;
    }

    public void setInventoryInfoID(String inventoryInfoID) {
        this.inventoryInfoID = inventoryInfoID;
    }

    public String getLogicInventoryInfoID() {
        return logicInventoryInfoID;
    }

    public void setLogicInventoryInfoID(String logicInventoryInfoID) {
        this.logicInventoryInfoID = logicInventoryInfoID;
    }

    public String getDepartCodeName() {
        return departCodeName;
    }

    public void setDepartCodeName(String departCodeName) {
        this.departCodeName = departCodeName;
    }

    public String getDepotCodeName() {
        return depotCodeName;
    }

    public void setDepotCodeName(String depotCodeName) {
        this.depotCodeName = depotCodeName;
    }

    public String getLogicCodeName() {
        return logicCodeName;
    }

    public void setLogicCodeName(String logicCodeName) {
        this.logicCodeName = logicCodeName;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getStockInOutDate() {
        return stockInOutDate;
    }

    public void setStockInOutDate(String stockInOutDate) {
        this.stockInOutDate = stockInOutDate;
    }
}
