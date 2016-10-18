/*	
 * @(#)BINOLSTCM15_Action     1.0 2012/09/10		
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
package com.cherry.st.common.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.rps.bl.BINOLPTRPS11_BL;
import com.cherry.st.common.form.BINOLSTCM15_Form;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM15_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品发货接收方库存弹出table共通Action
 * 
 * @author niushunjie
 * @version 1.0 2012.09.10
 */
public class BINOLSTCM15_Action extends BaseAction implements ModelDriven<BINOLSTCM15_Form>{	

    private static final long serialVersionUID = -3884219141968582238L;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLSTCM15_BL")
	private BINOLSTCM15_IF binOLSTCM15_BL;
    
    @Resource(name="BINOLPTRPS11_IF")
    private BINOLPTRPS11_BL binOLPTRPS11_BL;
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;

	/** 参数FORM */
    private BINOLSTCM15_Form form = new BINOLSTCM15_Form();
	
    @Override
    public BINOLSTCM15_Form getModel() {
        return form;
    }
    
    /**
     * 查询默认实体仓库、逻辑仓库及实体仓库List、逻辑仓库List（画面需要select框时）放入map里。
     * @return
     * @throws Exception
     */
    private Map<String,Object> getInventoryInfo() throws Exception{
        Map<String,Object> inventoryMap = new HashMap<String,Object>();
        // 登陆用户信息
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        
        String initType = ConvertUtil.getString(form.getInitType());
        if(initType.equals("select")){
            //实体仓库/逻辑仓库显示下拉框
            //实体仓库List
            String departID = ConvertUtil.getString(form.getDepartID());
            List<Map<String,Object>> depotList = binOLCM18_BL.getDepotsByDepartID(departID, language);
            
            //取得逻辑仓库List
            String logicType = "0";//后端
            Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(departID,language);
            if(null != departInfoMap){
                String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
                //终端
                if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                    logicType = "1";
                }
            }
            Map<String,Object> parmMap = new HashMap<String,Object>();
            parmMap.put("BIN_BrandInfoID", form.getBrandInfoID());
            parmMap.put("Type", logicType);
            parmMap.put("language", language);
            List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(parmMap);
            
            inventoryMap.put("initType", "select");
            inventoryMap.put("DepotCodeName", ConvertUtil.getString(depotList.get(0).get("DepotCodeName")));
            inventoryMap.put("LogicInventoryCodeName", ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryName")));
            inventoryMap.put("BIN_InventoryInfoID", depotList.get(0).get("BIN_DepotInfoID"));
            inventoryMap.put("BIN_LogicInventoryInfoID", logicDepotList.get(0).get("BIN_LogicInventoryInfoID"));
            inventoryMap.put("depotList", depotList);
            inventoryMap.put("logicDepotList", logicDepotList);
        }else if(initType.equals("showCodeNameByID")){
            //传入实体仓库ID、逻辑仓库ID显示相应的编号名称
            Map<String,String> paramMap =new HashMap<String,String>();
            paramMap.put("BIN_DepotInfoID", form.getInventoryInfoID());
            paramMap.put("language", language);
            List<Map<String,Object>> depotList = binOLCM18_BL.getDepotsList(paramMap);
            if(null != depotList && depotList.size()>0){
                inventoryMap.put("DepotCodeName", ConvertUtil.getString(depotList.get(0).get("DepotCodeName")));
                inventoryMap.put("BIN_InventoryInfoID", depotList.get(0).get("BIN_DepotInfoID"));
            }
            Map<String,Object> logicParamMap = new HashMap<String,Object>();
            logicParamMap.put("BIN_LogicInventoryInfoID", form.getLogicInventoryInfoID());
            logicParamMap.put("language", language);
            List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicParamMap);
            if(null != logicDepotList && logicDepotList.size()>0){
                inventoryMap.put("LogicInventoryCodeName", ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryName")));
                inventoryMap.put("BIN_LogicInventoryInfoID", logicDepotList.get(0).get("BIN_LogicInventoryInfoID"));
            }
        }else{
            int productOrderID = 0;
            if(null == form.getEntryID() || "".equals(form.getEntryID())){
                productOrderID = CherryUtil.obj2int(form.getProductOrderID());
            }else{
                long entryID = Long.parseLong(form.getEntryID());
                PropertySet ps = workflow.getPropertySet(entryID);
                Collection collection = ps.getKeys("BIN_ProductOrderID");
                if(collection.size()>0){
                    productOrderID = ps.getInt("BIN_ProductOrderID");
                }
            }
            
            List<Map<String,Object>> productOrderDetailData = binOLSTCM02_BL.getProductOrderDetailData(productOrderID, language, null);
            if(null != productOrderDetailData && productOrderDetailData.size()>0){
                inventoryMap.put("DepotCodeName", ConvertUtil.getString(productOrderDetailData.get(0).get("DepotCodeName")));
                inventoryMap.put("LogicInventoryCodeName", ConvertUtil.getString(productOrderDetailData.get(0).get("LogicInventoryName")));
                inventoryMap.put("BIN_InventoryInfoID", productOrderDetailData.get(0).get("BIN_InventoryInfoID"));
                inventoryMap.put("BIN_LogicInventoryInfoID", productOrderDetailData.get(0).get("BIN_LogicInventoryInfoID"));
            }
        }
        
        return inventoryMap;
    }
    
    /**
     * ajax取得当前部门的实体仓库List、逻辑仓库List
     * @throws Exception
     */
    public void getDepotLogicInfo() throws Exception{
        Map<String,Object> infoMap = getInventoryInfo();
        ConvertUtil.setResponseByAjax(response, infoMap);
    }
    
    /**
     * 初始化弹出框
     * @return
     * @throws Exception
     */
    public String init() throws Exception{
        Map<String,Object> inventoryInfo = getInventoryInfo();
        form.setInventoryMap(inventoryInfo);
        
        // 开始日期
        String today = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
        form.setStartDate(DateUtil.addDateByDays(CherryConstants.DATE_PATTERN,today, -7));
        // 截止日期
        form.setEndDate(today);
        return SUCCESS;
    }
    
    /**
     * 取得发货接收方库存List
     * @return
     * @throws Exception 
     */
    public String getStock() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得session信息
        Map<String, Object> map  = this.getSessionInfo();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        
        map.put("ProductVendorIDArr", form.getProductVendorIDArr());
        
        List<String> filterList = new ArrayList<String>();
        // 需要过滤的字段名
        filterList.add("E.NameForeign");
        filterList.add("E.NameTotal");
        filterList.add("D.BarCode");
        filterList.add("E.UnitCode");
        map.put(CherryConstants.FILTER_LIST_NAME, filterList);
        
        int count = 0;
        if(null != form.getProductVendorIDArr() && form.getProductVendorIDArr().length>0){
            List<Map<String,Object>> productList = binOLSTCM15_BL.getStockList(map);
            
            if(null != productList && productList.size()>0){
                count = productList.size();
                //调用取库存共通
                map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
                map.put("startDate", form.getStartDate());
                map.put("endDate", form.getEndDate());
                map.put("prtList", productList);
                map.put("depotId", form.getInventoryInfoID());
                map.put("lgcInventoryId", form.getLogicInventoryInfoID());
                List<Map<String,Object>> depotStockList = binOLPTRPS11_BL.getDepotStockList(map);
                form.setStockList(depotStockList);
            }
        }

        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLSTCM15_1";
    }
    
    /**
     * 取得session的信息
     * @param map
     * @throws Exception 
     */
    private Map<String,Object> getSessionInfo() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
        // 取得所属组织
        map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = (String)(userInfo.getCurrentBrandInfoID());
        if (brandInfoID!=null && !brandInfoID.equals("-9999")){
            // 取得所属品牌
            map.put("brandInfoID", brandInfoID);
        }
        // 取得语言区分
        map.put("language", userInfo.getLanguage());
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 操作类型
        map.put("operationType", "1");
        // 业务类型
        map.put("businessType", "1");
        return map;
    }
}