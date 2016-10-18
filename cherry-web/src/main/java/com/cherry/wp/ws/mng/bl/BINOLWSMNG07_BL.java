/*
 * @(#)BINOLWSMNG07_BL.java     1.0 2015-10-29 
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
package com.cherry.wp.ws.mng.bl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM41_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.ProductionConstants;
import com.cherry.st.common.bl.BINOLSTCM14_BL;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG07_IF;
import com.cherry.wp.ws.mng.service.BINOLWSMNG07_Service;

/**
 * 
 * @ClassName: BINOLWSMNG07_BL 
 * @Description: TODO(盘点申请BL) 
 * @author menghao
 * @version v1.0.0 2015-10-29 
 *
 */
public class BINOLWSMNG07_BL implements BINOLWSMNG07_IF {
	
	@Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_BL binOLSTCM14_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_BL binOLCM19_BL;
    
    @Resource(name="binOLCM41_IF")
    private BINOLCM41_IF binOLCM41_BL;
    
	@Resource(name="binOLWSMNG07_Service")
	private BINOLWSMNG07_Service binOLWSMNG07_Service;
	
    @Override
    public List<Map<String, Object>> getAuditBill(Map<String, Object> map) {
        return binOLWSMNG07_Service.getAuditBill(map);
    }

    @Override
    public List<Map<String, Object>> getAllCntPrtStockList(Map<String, Object> map) throws Exception {
        String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
        String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
        String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
        // 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
        String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", organizationInfoId, brandInfoId);
        // 产品方案添加产品模式 1:标准模式 2:颖通模式
        String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", organizationInfoId, brandInfoId);
        //商品盘点的商品排序字段（0：厂商编码，1：商品条码）
        String prtOrderBy = binOLCM14_BL.getConfigValue("1092", organizationInfoId, brandInfoId);
        String organizationID = ConvertUtil.getString(map.get("BIN_OrganizationID"));
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizationId", organizationID);
        Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(param);
        String counterCode = "";
        if(null != counterInfo){
            counterCode = ConvertUtil.getString(counterInfo.get("counterCode"));
        }

        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("organizationInfoId", organizationInfoId);
        praMap.put("brandInfoId", brandInfoId);
        praMap.put("counterCode", counterCode);
        String businessDate = binOLWSMNG07_Service.getBussinessDate(praMap);
        praMap.put("businessDate", businessDate);
        String isCntPrt = binOLCM41_BL.getIsCntPrt(praMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("isCntPrt", isCntPrt);
        paramMap.put("soluAddModeConf", soluAddModeConf);
        paramMap.put("cntPrtModeConf", cntPrtModeConf);
        paramMap.put("language", language);
        paramMap.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
        paramMap.put("organizationInfoId", organizationInfoId);
        paramMap.put("brandInfoId", brandInfoId);
        paramMap.put("businessDate", businessDate);
        paramMap.put("counterCode", counterCode);
        paramMap.put("BIN_InventoryInfoID", map.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", map.get("BIN_LogicInventoryInfoID"));
        paramMap.put("prtOrderBy", prtOrderBy);
        
        return binOLWSMNG07_Service.getAllCntPrtStockList(paramMap);
    }
    
    /**
     * 提交盘点申请单
     * @param map
     * @param list
     * @param userinfo
     * @return
     * @throws Exception
     */
    @Override
    public int tran_submit(Map<String, Object> map, List<String[]> list, UserInfo userinfo) throws Exception {
    	// 标记是否为提交--提交时需要开启盘点申请工作流
    	map.put("isSubmit", "1");
        // 盘点申请--数据的插入是在工作流中进行
    	int billID = tran_save(map, list, userinfo);
        if(billID == 0){
            //抛出自定义异常：操作失败！
            throw new CherryException("ISS00005");
        }
        
        return billID;
    }
    
    /**
     * 保存盘点申请单
     * @param map
     * @param list
     * @param userinfo
     * @return
     * @throws Exception
     */
    @Override
    public int tran_save(Map<String, Object> map, List<String[]> list,
            UserInfo userinfo) throws Exception {
    	int result = 1;
    	String tradeDateTime = binOLWSMNG07_Service.getSYSDateTime();
        //从list中获取各种参数数组
        String[] productVendorIdArr = list.get(0);
        String[] batchNoArr = list.get(1);
        String[] checkQuantityArr = list.get(2);
        String[] reasonArr = list.get(3);
        String[] priceArr = list.get(4);
        String[] gainCountArr = list.get(5);
        String[] bookCountArr = list.get(6);
        String[] htArr = list.get(7);
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
        	//验证产品厂商ID
        	if(CherryChecker.isNullOrEmpty(productVendorIdArr[i], true)){
            	throw new CherryException("EST00026",new String[]{ProductionConstants.Product_ProductVendorID});
            }
        	
        	//验证批次号
        	if("true".equals(map.get("isBatchStockTaking")) && CherryChecker.isNullOrEmpty(batchNoArr[i], true)){
        		throw new CherryException("EST00026",new String[]{ProductionConstants.ProductTaking_BatchNo});
        	}
        	
        	//验证数量
        	if(CherryChecker.isNullOrEmpty(gainCountArr[i], true)){
            	throw new CherryException("EST00026",new String[]{ProductionConstants.ProductTaking_Quantity});
            }
        }
        
        //总数量
        int totalQuantity = 0;
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
            totalQuantity += Integer.parseInt(gainCountArr[i]);
        }
        //总金额
        double totalAmount = 0.0;
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
            totalAmount += Double.parseDouble(priceArr[i])*Integer.parseInt(gainCountArr[i]);
        }
        
        try{
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            //i=0为隐藏
            for(int i=0;i<productVendorIdArr.length;i++){
                
                Map<String,Object> detailMap = new HashMap<String,Object>();
                detailMap.put("BIN_ProductVendorID", productVendorIdArr[i]);
                detailMap.put("BIN_ProductBatchID", null);
                detailMap.put("BookQuantity", bookCountArr[i]);
                if(null != checkQuantityArr && checkQuantityArr.length >= i && !"".equals(checkQuantityArr[i])){
                	detailMap.put("CheckQuantity", checkQuantityArr[i]);
                } else {
                	detailMap.put("CheckQuantity", ConvertUtil.getInt(bookCountArr[i])+ConvertUtil.getInt(gainCountArr[i]));
                }
                detailMap.put("GainQuantity", gainCountArr[i]);
                detailMap.put("Price", priceArr[i]);
                detailMap.put("BIN_ProductVendorPackageID", "0");
                detailMap.put("BIN_InventoryInfoID", map.get("depotInfoId"));
                detailMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(map.get("logicInventoryInfoId")));
                detailMap.put("BIN_StorageLocationInfoID", "0");
                detailMap.put("Comments", reasonArr[i]);
                String handleType = "2";
                if(null != htArr && htArr.length > i && !ConvertUtil.getString(htArr[i]).equals("")){
                    handleType = htArr[i];
                }
                detailMap.put("HandleType", handleType);
                detailMap.put("CreatedBy", map.get(CherryConstants.CREATEDBY));
                detailMap.put("CreatePGM", map.get(CherryConstants.CREATEPGM));
                detailMap.put("UpdatedBy", map.get(CherryConstants.UPDATEDBY));
                detailMap.put("UpdatePGM", map.get(CherryConstants.UPDATEPGM));
                
                detailList.add(detailMap);
            }
            //申明map存放盘点单主表数据
            Map<String,Object> mainData = new HashMap<String,Object>();
            mainData.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
            mainData.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
            mainData.put("StockTakingNoIF", map.get("StockTakingNoIF"));
            mainData.put("RelevanceNo", "");
            mainData.put("BIN_OrganizationID", map.get("organizationId"));
            mainData.put("BIN_InventoryInfoID", map.get("depotInfoId"));
            mainData.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(map.get("logicInventoryInfoId")));
            mainData.put("BIN_StorageLocationInfoID", 0);
         	//操作员
            String tradeEmployeeID = ConvertUtil.getString(map.get("TradeEmployeeID"));
            if(tradeEmployeeID.equals("")){
                mainData.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
            }else{
                mainData.put("BIN_EmployeeID", map.get("TradeEmployeeID"));
            }
            mainData.put("TotalQuantity", totalQuantity);
            mainData.put("TotalAmount", totalAmount);
            mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
            //用于区分门店做的自由盘点或商品盘点
            String addType = ConvertUtil.getString(map.get("addType"));
            //盘点类型
            if("0".equals(ConvertUtil.getString(map.get("blindFlag")))){
                mainData.put("StocktakeType", "F1");//自由盘点
                if(addType.equals("all")){
                    mainData.put("StocktakeType", "P1");//商品盘点
                }else if(addType.equals("given")){
                	 mainData.put("StocktakeType", "G1");//指定盘点
                }
            }else{
                mainData.put("StocktakeType", "F2");//自由盘点盲盘
                if(addType.equals("all")){
                    mainData.put("StocktakeType", "P2");//商品盘点盲盘
                }else if(addType.equals("given")){
               	 mainData.put("StocktakeType", "G2");//指定盘点盲盘
               }
            }
            mainData.put("TradeType", CherryConstants.OS_BILLTYPE_CR);
            mainData.put("IsBatch", "0");
            mainData.put("Comments", map.get("Comments"));
            mainData.put("StockReason", "");
            // 日期与时间没有传入则取数据库时间
//            mainData.put("Date", map.get("tradeDate"));
//            mainData.put("TradeTime", map.get("tradeTime"));
            
            mainData.put("CreatedBy", map.get(CherryConstants.CREATEDBY));
            mainData.put("CreatePGM", map.get(CherryConstants.CREATEPGM));
            mainData.put("UpdatedBy", map.get(CherryConstants.UPDATEDBY));
            mainData.put("UpdatePGM", map.get(CherryConstants.UPDATEPGM));
            
            String isSubmit = ConvertUtil.getString(map.get("isSubmit"));
            if("".equals(isSubmit)) {
            	// 暂存
	            int productStockTakingID = 0;
	            productStockTakingID = binOLSTCM14_BL.insertProStocktakeRequestAll(mainData, detailList);
	            
	            if(productStockTakingID == 0){
	            	//抛出自定义异常：操作失败！
	            	throw new CherryException("ISS00005");
	            }
	            
	            return productStockTakingID;
            } else {
            	// 提交--即开启工作流
            	Map<String,Object> mainWorkFlowData = new HashMap<String,Object>();
            	mainWorkFlowData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CR);//业务类型
            	mainWorkFlowData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));// 制单人员ID
            	mainWorkFlowData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, mainData.get("BIN_EmployeeID"));// 制单人员ID
            	mainWorkFlowData.put(CherryConstants.OS_ACTOR_TYPE_USER,userinfo.getBIN_UserID());//用户ID--同一柜台下的BA使用同一个云POS账号，用户ID都一样
            	mainWorkFlowData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
            	mainWorkFlowData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get("BIN_OrganizationID"));//部门ID
            	mainWorkFlowData.put("CurrentUnit", "BINOLWSMNG07");//当前机能ID
            	mainWorkFlowData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));//品牌ID
            	mainWorkFlowData.put("UserInfo", userinfo);
            	mainWorkFlowData.put("OrganizationInfoCode", userinfo.getOrgCode());
            	mainWorkFlowData.put("BrandCode", userinfo.getBrandCode());
            	mainWorkFlowData.put("OrganizationCode", map.get("counterCode"));
            	// 盘点申请单已经存在的需要做更新处理
            	mainData.put("BIN_ProStocktakeRequestID", map.get("proStocktakeRequestID"));
            	mainWorkFlowData.put("stocktakeReqMainData", mainData);
            	mainWorkFlowData.put("stocktakeReqDetailList", detailList);
            	mainWorkFlowData.put("TradeDateTime", tradeDateTime);//MQ单据的时间
                //工作流开始
                binOLSTCM00_BL.StartOSWorkFlow(mainWorkFlowData);
            }
        }catch(Exception e){
        	if(e instanceof CherryException){
        		throw (CherryException)e;
        	}else{
        		throw new CherryException(e.getMessage());
        	}
        }
        return result;
    }

    /**
     * 盘点指定商品
     */
	@Override
	public List<Map<String, Object>> getGivenCntPrtStockList(Map<String, Object> map) throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
        String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
        String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
       
        Map<String,Object> parMap = new HashMap<String,Object>();
        //获取数据库时间
        String bussDate = binOLWSMNG07_Service.getSYSDate();
        parMap.put("bussDate", bussDate);
        parMap.put("brandInfoId",brandInfoId);
        parMap.put("organizationInfoId",organizationInfoId);
        parMap.put("prtFunType", 1);
        
        //获取有效的产品盘点功能List
        List<Map<String, Object>> prtFunList = binOLWSMNG07_Service.getPrtFunList(parMap);
        if(prtFunList.isEmpty() || prtFunList.size()==0){
        	return null;
        }
        
        //商品盘点ID
        Integer productFunctionID = (Integer) prtFunList.get(0).get("productFunctionID");
        
        //获取当前指定的盘点商品list并返回
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("language", language);
        paramMap.put("productFunctionID", productFunctionID);
        paramMap.put("BIN_InventoryInfoID", map.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", map.get("BIN_LogicInventoryInfoID"));
        
        return binOLWSMNG07_Service.getGivenCntPrtStockList(paramMap);
	}
    
}