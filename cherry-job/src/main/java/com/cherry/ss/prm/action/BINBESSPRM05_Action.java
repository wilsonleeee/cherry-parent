/*  
 * @(#)BINBESSPRM05_Action.java     1.0 2011/08/09  
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
package com.cherry.ss.prm.action;

import java.util.ArrayList;
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
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINBESSPRM05_BL;
import com.cherry.ss.pro.interfaces.BINBESSPRO04_IF;

/**
 * 促销产品同步
 * 
 * @author niushunjie
 * @version 1.0 2011.08.09
 */
public class BINBESSPRM05_Action extends BaseAction {

    private static final long serialVersionUID = -8354584144840873529L;

	@Resource
	private BINBESSPRM05_BL binBESSPRM05_BL;
	
	@Resource
	private BINBESSPRO04_IF binBESSPRO04_BL;

	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM05_Action.class.getName());
	
	/** 品牌Id */
    private String brandInfoId;
    
    /** 组织Id */
    private String organizationInfoId;

    /**柜台编号*/
    private String counterCode;
    
    /**逻辑仓库编号*/
    private String logicInventoryCode;
    
    /**日期*/
    private String stockInOutDate;
    
    /**过滤新后台促销品不存在*/
    private String filterPrtNotExist;
    
    /**过滤新后台库存不存在*/
    private String filterStockNotExist;
    
    /**
     * 初始化
     * @return
     * @throws Exception
     */
	public String init() throws Exception {
	    return SUCCESS;
	}
	
	/**
	 * 初始化返回柜台
	 * @throws Exception 
	 */
	public void getCounter() throws Exception{
	    Map<String,Object> param = new HashMap<String,Object>();
	    param.put("counterCode", counterCode);
	    List<Map<String,Object>> list = binBESSPRM05_BL.getOldCounterInfoList(param);
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
        //取得实体仓库ID、逻辑仓库ID
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("counterCode", counterCode);
        param.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        param.put("brandInfoId", brandInfoId);
        List<Map<String,Object>> newCounterDepotInfoList = binBESSPRM05_BL.getNewCounterDepotInfoList(param);
        String counterInfoId = "";
        int depotID = 0;
        int organID = 0;
        String counterName = "";
        String depotCode = "";
        String depotName = "";
        if(null != newCounterDepotInfoList && newCounterDepotInfoList.size()>0){
            counterInfoId = CherryBatchUtil.getString(newCounterDepotInfoList.get(0).get("BIN_CounterInfoID"));
            depotID = CherryBatchUtil.Object2int(newCounterDepotInfoList.get(0).get("BIN_DepotInfoID"));
            organID = CherryBatchUtil.Object2int(newCounterDepotInfoList.get(0).get("BIN_OrganizationID"));
            counterName = CherryBatchUtil.getString(newCounterDepotInfoList.get(0).get("CounterNameIF"));
            depotCode = CherryBatchUtil.getString(newCounterDepotInfoList.get(0).get("DepotCode"));
            depotName = CherryBatchUtil.getString(newCounterDepotInfoList.get(0).get("DepotNameCN"));
        }else{
            ConvertUtil.setResponseByAjax(response, new ArrayList<Map<String,Object>>());
            return null;
        }
	    
        Map<String, Object> map = new HashMap<String, Object>();
        // 所属组织
        map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 品牌Id
        map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
        //部门ID
        map.put("organizationId",organID);
        //柜台ID
        map.put("counterInfoId",counterInfoId);
        //柜台号
        map.put("counterCode",counterCode);
        //逻辑仓库号
        param.put("logicInventoryCode", logicInventoryCode);
        int logicInventoryInfoID = 0;
        String logicInventoryName = "";
        List<Map<String,Object>> newCounterLogicInfoList = binBESSPRM05_BL.getNewCounterLogicInfoList(param);
        if(null != newCounterLogicInfoList && newCounterLogicInfoList.size()>0){
            logicInventoryInfoID = CherryBatchUtil.Object2int(newCounterLogicInfoList.get(0).get("BIN_LogicInventoryInfoID"));
            logicInventoryName = CherryBatchUtil.getString(newCounterLogicInfoList.get(0).get("InventoryNameCN"));
        }else{
            ConvertUtil.setResponseByAjax(response, new ArrayList<Map<String,Object>>());
            return null;
        }
        
        //实体仓库ID
        map.put("inventoryInfoId",depotID);
        //逻辑仓库ID
        map.put("logicInventoryInfoId",logicInventoryInfoID);
        map.put("logicInventoryCode", logicInventoryCode);
        
        map.put("counterCode", counterCode);
        map.put("counterName", counterName);
        map.put("inventoryCode", depotCode);
        map.put("inventoryName", depotName);
        map.put("logicInventoryCode", logicInventoryCode);
        map.put("logicInventoryName", logicInventoryName);
        map.put("filterPrtNotExist", filterPrtNotExist);
        map.put("filterStockNotExist", filterStockNotExist);
        try{
            List<Map<String,Object>> list = binBESSPRM05_BL.getDiffPromotionProductList(map);
            ConvertUtil.setResponseByAjax(response, list);
        }catch(Exception e){
            this.addActionError("获取促销产品库存失败");
            return "error";
        }
        
        return null;
	}
	
	/**
	 * 同步数据
	 * @return
	 * @throws Exception
	 */
	public String binBESSPRM05Exec() throws Exception {
	    logger.info("******************************BINBESSPRM05处理开始***************************");
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
	        // 日期
	        map.put("stockInOutDate", stockInOutDate);
	        //柜台编号
	        map.put("counterCode", counterCode);
	        //新后台促销品不存在
	        map.put("filterPrtNotExist", filterPrtNotExist);
	        //新后台库存不存在
	        map.put("filterStockNotExist", filterStockNotExist);
	        flg = binBESSPRM05_BL.tran_sync(map);
	    } catch (CherryBatchException cbx) {
	        flg = CherryBatchConstants.BATCH_WARNING;
	    } catch (Exception e) {
	        e.printStackTrace();
	        flg = CherryBatchConstants.BATCH_ERROR;
	    } finally {
	        if (flg == CherryBatchConstants.BATCH_SUCCESS) {
	            this.addActionMessage("促销产品库存同步处理正常终了");
	            logger.info("******************************BINBESSPRM05处理正常终了***************************");
	        } else if (flg == CherryBatchConstants.BATCH_WARNING) {
	            this.addActionError("促销产品库存同步处理警告终了");
	            logger.info("******************************BINBESSPRM05处理警告终了***************************");
	        } else if (flg == CherryBatchConstants.BATCH_ERROR) {
	            this.addActionError("促销产品库存同步处理异常终了");
	            logger.info("******************************BINBESSPRM05处理异常终了***************************");
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
        //柜台编号
        map.put("counterCode", counterCode);
        //新后台促销品不存在
        if("on".equals(filterPrtNotExist) || "true".equals(filterPrtNotExist)){
            map.put("filterPrtNotExist", "true");
        }else{
            map.put("filterPrtNotExist", "false");
        }
        //新后台库存不存在
        if("on".equals(filterStockNotExist) || "true".equals(filterStockNotExist)){
            map.put("filterStockNotExist", "true");
        }else{
            map.put("filterStockNotExist", "false");
        }
        String language = "zh_CN";
        map.put(CherryBatchConstants.SESSION_LANGUAGE, language);
        try {
            response.setContentType("application/vnd.excel");
            response.setCharacterEncoding(CherryConstants.CHAR_ENCODING);
            String exportName = binBESSPRO04_BL.getResourceValue("BINBESSPRM05", language, "downloadFileName");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(exportName.getBytes("GBK"),"ISO8859-1"));
            byte[] excel = binBESSPRM05_BL.exportExcel(map);
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

    public void setOrganizationInfoId(String organizationInfoId) {
        this.organizationInfoId = organizationInfoId;
    }

    public String getOrganizationInfoId() {
        return organizationInfoId;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getLogicInventoryCode() {
        return logicInventoryCode;
    }

    public void setLogicInventoryCode(String logicInventoryCode) {
        this.logicInventoryCode = logicInventoryCode;
    }

    public String getStockInOutDate() {
        return stockInOutDate;
    }

    public void setStockInOutDate(String stockInOutDate) {
        this.stockInOutDate = stockInOutDate;
    }

    public String getFilterPrtNotExist() {
        return filterPrtNotExist;
    }

    public void setFilterPrtNotExist(String filterPrtNotExist) {
        this.filterPrtNotExist = filterPrtNotExist;
    }

    public String getFilterStockNotExist() {
        return filterStockNotExist;
    }

    public void setFilterStockNotExist(String filterStockNotExist) {
        this.filterStockNotExist = filterStockNotExist;
    }
}
