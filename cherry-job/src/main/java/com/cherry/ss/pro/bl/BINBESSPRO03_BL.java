/*
 * @(#)BINBESSPRM03_BL.java     1.0 2011/09/01
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
package com.cherry.ss.pro.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.ProductConstants;
import com.cherry.ss.pro.interfaces.BINBESSPRO04_IF;
import com.cherry.ss.pro.interfaces.BINBESSPRO04_IF.ExcelParam;
import com.cherry.ss.pro.service.BINBESSPRO03_Service;
import com.cherry.st.common.bl.BINOLSTCM01_BL;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;


/**
 * 
 * 产品库存同步BL
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.09.01
 */
@SuppressWarnings("unchecked")
public class BINBESSPRO03_BL {
    /** BATCH处理标志 */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;

    /** 成功条数 */
    private int successCount = 0;
    /** 失败条数 */
    private int failCount = 0;
    
    @Resource
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource
    private BINBESSPRO03_Service binBESSPRO03_Service;
    
    @Resource
    private BINBESSPRO04_IF binBESSPRO04_BL;
    
	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;
    
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private transient BINOLCM14_BL binOLCM14_BL;
    
    /**
     * 取得柜台List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getCounterInfoList(Map<String, Object> map){
        return binBESSPRO03_Service.getCounterInfoList(map);
    }
    
    /**
     * 按柜台查询产品
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductList(Map<String, Object> map){
        return binBESSPRO03_Service.getProductList(map);
    }
    
    /**
     * 取得有差异的产品List
     * 
     * @param map
     * @return 有差异的产品List
     */
    public List<Map<String,Object>> getDiffProductList(Map<String, Object> map){
        List<Map<String,Object>> productList = new ArrayList<Map<String,Object>>();
        
        List<Map<String,Object>> oldBG = binBESSPRO03_Service.getOldStockList(map);
        List<Map<String,Object>> newBG = binBESSPRO03_Service.getProductList(map);
        
        Map<String,Object> otherInfo = new HashMap<String,Object>();
        otherInfo.put("counterCode", ConvertUtil.getString(map.get("counterCode")));
        otherInfo.put("counterName", ConvertUtil.getString(map.get("counterName")));
        otherInfo.put("inventoryCode", ConvertUtil.getString(map.get("inventoryCode")));
        otherInfo.put("inventoryName", ConvertUtil.getString(map.get("inventoryName")));
        otherInfo.put("logicInventoryCode", ConvertUtil.getString(map.get("logicInventoryCode")));
        otherInfo.put("logicInventoryName", ConvertUtil.getString(map.get("logicInventoryName")));
        otherInfo.put("inventoryInfoId", ConvertUtil.getString(map.get("inventoryInfoId")));
        otherInfo.put("logicInventoryInfoId", CherryUtil.obj2int(map.get("logicInventoryInfoId")));  
        otherInfo.put("isSync", ConvertUtil.getString(map.get("isSync")));
        otherInfo.put("filterPrtNotExist", ConvertUtil.getString(map.get("filterPrtNotExist")));
        otherInfo.put("filterStockNotExist", ConvertUtil.getString(map.get("filterStockNotExist")));
        productList.addAll(compareProduct(newBG, oldBG, otherInfo));
        
        return productList;
    }
    
    /**
     * 显示编号+名称
     */
    private String getCodePlueName(String code,String name){
        if(null == name){
            name = "";
        }
        if(null!=code && code.length()>0){
            name = "("+code+")"+name;
        } 
        return name;
    }
    
    /**
     * 比较新老后台产品库存
     * 
     * @param map
     * @return 库存不一致的产品
     */
    public List<Map<String,Object>> compareProduct(List<Map<String,Object>> newBG,List<Map<String,Object>> oldBG,Map otherInfo){
        List difference = new ArrayList();
        sortList(newBG);
        sortList(oldBG);
        
        String counterCode = ConvertUtil.getString(otherInfo.get("counterCode"));
        String counterName = ConvertUtil.getString(otherInfo.get("counterName"));
        String inventoryCode = ConvertUtil.getString(otherInfo.get("inventoryCode"));
        String inventoryName = ConvertUtil.getString(otherInfo.get("inventoryName"));
        String logicInventoryCode = ConvertUtil.getString(otherInfo.get("logicInventoryCode"));
        String logicInventoryName = ConvertUtil.getString(otherInfo.get("logicInventoryName"));
        String inventoryInfoId = ConvertUtil.getString(otherInfo.get("inventoryInfoId"));
        String logicInventoryInfoId = ConvertUtil.getString(otherInfo.get("logicInventoryInfoId"));
        
        String showCounter = getCodePlueName(counterCode,counterName);
        String showCangKu=getCodePlueName(inventoryCode,inventoryName);
        String showLogicCangku =getCodePlueName(logicInventoryCode,logicInventoryName);
        
        Map diffInfo = null;
        for(Map<String, Object> oldProduct: oldBG){
        	
            String oldUnitCode = ConvertUtil.getString(oldProduct.get("unitCode")).toLowerCase();
            String oldBarCode = ConvertUtil.getString(oldProduct.get("barCode")).toLowerCase();
            //老后台库存有小数点
            int oldQuantity = CherryBatchUtil.decimal2int(oldProduct.get("quantity"));
            String oldProductName = ConvertUtil.getString(oldProduct.get("productName"));
            
            boolean flag = false;
            for(Map<String, Object> newProduct: newBG){
                String newUnitCode = ConvertUtil.getString(newProduct.get("unitCode")).toLowerCase();
                String newBarCode = ConvertUtil.getString(newProduct.get("barCode")).toLowerCase();
                int newQuantity = CherryBatchUtil.Object2int(newProduct.get("quantity"));                
                if(newUnitCode.equals(oldUnitCode) && newBarCode.equals(oldBarCode)){
                	newProduct.put("matchflag", "true");
                    if(newQuantity != oldQuantity){
                        int productVendorId = CherryUtil.obj2int(newProduct.get("productVendorId"));
                        String newProductName = ConvertUtil.getString(newProduct.get("productName"));
                        int productStockId = CherryUtil.obj2int(newProduct.get("productStockId"));
                        Object salePrice = newProduct.get("salePrice");                        
                        diffInfo = new HashMap();
                        diffInfo.put("productVendorId", productVendorId);
                        diffInfo.put("counterName", showCounter);
                        diffInfo.put("inventoryName", showCangKu);
                        diffInfo.put("logicInventoryName", showLogicCangku);
                        diffInfo.put("productName", newProductName);
                        diffInfo.put("unitCode", newUnitCode);
                        diffInfo.put("barCode", newBarCode);
                        diffInfo.put("newQuantity", newQuantity);
                        diffInfo.put("oldQuantity", oldQuantity);
                        diffInfo.put("productStockId", productStockId);
                        diffInfo.put("inventoryInfoId", inventoryInfoId);
                        diffInfo.put("logicInventoryInfoId", logicInventoryInfoId);
                        diffInfo.put("salePrice", salePrice);
                        difference.add(diffInfo);
                    }
                    flag = true;
                    break;
                }
            }
            //当一方库存为0，另一方库存不存在时，不要再显示出来，就当成库存一致处理；
            if(!flag && oldQuantity != 0){
                boolean addFlag = true;
                String isSync = CherryBatchUtil.getString(otherInfo.get("isSync"));
                String filterPrtNotExist = CherryBatchUtil.getString(otherInfo.get("filterPrtNotExist"));
                String filterStockNotExist = CherryBatchUtil.getString(otherInfo.get("filterStockNotExist"));
                diffInfo = new HashMap<String,Object>();

                //界面显示
                if(!"true".equals(isSync)){
                    diffInfo.put("newQuantity", "库存不存在");
                }else{
                    diffInfo.put("newQuantity", null);
                }
                if("true".equals(filterStockNotExist)){
                    continue;
                }
                Map<String,Object> paramProduct = new HashMap<String,Object>();
                paramProduct.put("barCode", oldBarCode);
                paramProduct.put("unitCode", oldUnitCode);
                List<Map<String,Object>> product =getNewProductList(paramProduct);
                if(null != product && product.size()>0){
                    //查询厂商ID
                    diffInfo.put("productVendorId", product.get(0).get("productVendorId"));
                    //查询价格
                    diffInfo.put("salePrice", product.get(0).get("salePrice")); 
                }else{
                    addFlag = false;
                    diffInfo.put("newQuantity", "产品不存在");
                    if("true".equals(filterPrtNotExist)){
                        continue;
                    }
                }
                diffInfo.put("counterName", showCounter);
                diffInfo.put("inventoryName", showCangKu);
                diffInfo.put("logicInventoryName", showLogicCangku);
                diffInfo.put("productName", oldProductName);
                diffInfo.put("unitCode", oldUnitCode);
                diffInfo.put("barCode", oldBarCode);
                diffInfo.put("oldQuantity", oldQuantity);
                diffInfo.put("inventoryInfoId", inventoryInfoId);
                diffInfo.put("logicInventoryInfoId", logicInventoryInfoId);
                //新后台产品不存在不同步该产品库存
                if(addFlag || !"true".equals(isSync)){
                    difference.add(diffInfo);
                }
            }
        }
        
        //找出新后台有库存而老后台没有的，加入到同步的
        for(Map<String, Object> newProduct: newBG){
        	 String newUnitCode = ConvertUtil.getString(newProduct.get("unitCode")).toLowerCase();
             String newBarCode = ConvertUtil.getString(newProduct.get("barCode")).toLowerCase();
             int newQuantity = CherryBatchUtil.Object2int(newProduct.get("quantity"));  
             if(newQuantity==0){
            	 //如果新后台库存为0，无需同步（老后台没有库存，相当于0），继续下一次循环
            	 continue;
             }
             String matchflag = ConvertUtil.getString(newProduct.get("matchflag")).toLowerCase();
             if("true".equals(matchflag)){
            	 //曾经匹配到，跳过
            	 continue;
             }else {
            	 //没有匹配到过，说明新后台新后台有库存，老后台没有，需要清除掉
            	 diffInfo = new HashMap();
                 diffInfo.put("productVendorId", CherryUtil.obj2int(newProduct.get("productVendorId")));
                 diffInfo.put("counterName", showCounter);
                 diffInfo.put("inventoryName", showCangKu);
                 diffInfo.put("logicInventoryName", showLogicCangku);
                 diffInfo.put("productName", ConvertUtil.getString(newProduct.get("productName")));
                 diffInfo.put("unitCode", newUnitCode);
                 diffInfo.put("barCode", newBarCode);
                 diffInfo.put("newQuantity", newQuantity);
                 diffInfo.put("oldQuantity", 0);
                 diffInfo.put("productStockId", CherryUtil.obj2int(newProduct.get("productStockId")));
                 diffInfo.put("inventoryInfoId", inventoryInfoId);
                 diffInfo.put("logicInventoryInfoId", logicInventoryInfoId);
                 diffInfo.put("salePrice", newProduct.get("salePrice"));
                 difference.add(diffInfo);
             }
        }
        return difference;
    }
    
    /**
     * 按unitCode、barCode对List排序
     * 
     * @param list
     */
    public static void sortList(List list){
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            public int compare(Map map1, Map map2) {
                String unitCode1 = ConvertUtil.getString(map1.get("unitCode"));
                String unitCode2 = ConvertUtil.getString(map2.get("unitCode"));
                int sortflag = unitCode1.compareTo(unitCode2);
                if (sortflag == 0) {
                    String barCode1 = ConvertUtil.getString(map1.get("barCode"));
                    String barCode2 = ConvertUtil.getString(map2.get("barCode"));
                    return barCode1.compareTo(barCode2);
                } else {
                    return sortflag;
                }
            }
        });
    }
    
    /**
     * 插入【产品盘点业务单据表】
     * @param map
     * @return
     */
    public int insertProductStockTaking(Map<String,Object> map){
        return binBESSPRO03_Service.insertProductStockTaking(map);
    }
    
    /**
     * 插入【产品盘点业务单据明细表】
     * @param map
     * @return
     */
    public void insertProductTakingDetail(List<Map<String, Object>> list){
        binBESSPRO03_Service.insertProductTakingDetail(list);
    }
    
    /**
     * 将一组盘点单据ID对应的数据写入到【产品入出库表】和【产品入出库明细表】
     * @param productStockTakingIDArr 盘点单据ID数组
     * @throws CherryBatchException 
     */
    public void insertStockInOutAndDetail(int[] productStockTakingIDArr,Map<String,Object> map,List<Map<String,Object>> diffProduct) throws CherryBatchException{
        //整理传入的参数
        String currentOrganizationInfoID = CherryBatchUtil.getString(map.get("organizationInfoId"));
        String currentBrandInfoID = CherryBatchUtil.getString(map.get("brandInfoId"));
        String stockInOutDate = CherryBatchUtil.getString(map.get("stockInOutDate"));
        Map<String,Object> deliverDetailMap;
        for(int i=0;i<productStockTakingIDArr.length;i++){
        	
        	 Map<String,Object> mainData = new HashMap<String, Object>();//产品入出库批次主表数据
        	 List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();//产品入出库批次从表数据
        	 
            //取得指定ID的盘点单据信息（包括详细）
            List<Map<String,Object>> list = binBESSPRO03_Service.getStockTakingInfoByID(productStockTakingIDArr[i]);
            String deliverNo ="";
            int productStockInOutID = 0;
            for(int n=0;n<list.size();n++){
            	 Map<String,Object>  detailData = new HashMap<String, Object>();//产品入出库批次从表数据
            	 
                deliverDetailMap = list.get(n);
                if(n==0){
                    int countTotal = CherryBatchUtil.Object2int(deliverDetailMap.get("totalQuantity"));
                    String typeflag ="";
                    if(countTotal>=0){
                        typeflag= ProductConstants.BUSINESS_TYPE_STORAGE_IN;
                    }else{
                        typeflag= ProductConstants.BUSINESS_TYPE_STORAGE_OUT;
                    }
                    //【产品入出库表】                
                    //插入入出库表前，取得单据号
                    deliverNo = binOLCM03_BL.getTicketNumber(currentOrganizationInfoID,currentBrandInfoID,ProductConstants.USERNAME, typeflag);
                    Map<String,Object> stockInOutMap = new HashMap<String,Object>();
                    
                    //主表       组织ID
                    stockInOutMap.put("organizationInfoId", currentOrganizationInfoID);  
                    //主表       品牌ID
                    stockInOutMap.put("brandInfoId", currentBrandInfoID);
                    //主表      单据号           
                    stockInOutMap.put("tradeNo", deliverNo);
                    //主表      接口单据号
                    stockInOutMap.put("tradeNoIF", deliverNo);
                    //主表      入出库部门
                    stockInOutMap.put("organizationId", deliverDetailMap.get("organizationId"));
                    //主表      制单员工
                    stockInOutMap.put("employeeId", deliverDetailMap.get("employeeId"));
                    //主表      总数量
                    stockInOutMap.put("totalQuantity", Math.abs(countTotal));
                    //主表      总金额
                    stockInOutMap.put("totalAmount", deliverDetailMap.get("totalAmount"));
                    //主表      入出库区分 0：入库    1：出库     TODO: 盘点统一设置为0                 
                    stockInOutMap.put("stockType", countTotal>=0?"0":"1");
                    //主表      业务类型  P:盘点
                    stockInOutMap.put("tradeType", ProductConstants.BUSINESS_TYPE_STOCKTAKE);
                    //主表      关联单号  即为盘点单号
                    stockInOutMap.put("relevanceNo", deliverDetailMap.get("stockTakingNo"));
                    //主表     TODO:物流ID
                    stockInOutMap.put("logisticInfoId", null);
                    //主表     入出库理由
                    stockInOutMap.put("comments", deliverDetailMap.get("comments"));
                    //主表     入出库日期
                    stockInOutMap.put("stockInOutDate", stockInOutDate);
                    //主表     入出库时间
                    stockInOutMap.put("stockInOutTime", CherryBatchUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss"));
                    //主表     审核区分
                    stockInOutMap.put("verifiedFlag", ProductConstants.AUDIT_FLAG_AGREE);
                    //入出库前产品总金额
                    stockInOutMap.put("totalAmountBefore", null);
                    //入出库后产品总金额
                    stockInOutMap.put("totalAmountAfter", null);  
                    //主表    共通字段
                    stockInOutMap.put(CherryBatchConstants.CREATEDBY, ProductConstants.USERNAME);  
                    stockInOutMap.put(CherryBatchConstants.CREATEPGM, ProductConstants.BINBESSPRO03);    
                    stockInOutMap.put(CherryBatchConstants.UPDATEDBY, ProductConstants.USERNAME);  
                    stockInOutMap.put(CherryBatchConstants.UPDATEPGM, ProductConstants.BINBESSPRO03);
                    
                    //产品入库出库批次主表       组织ID
                    mainData.put("BIN_OrganizationInfoID", currentOrganizationInfoID);  
                    //产品入库出库批次主表       品牌ID
                    mainData.put("BIN_BrandInfoID", currentBrandInfoID);
                    //产品入库出库批次主表      单据号           
                    mainData.put("TradeNo", deliverNo);
                    //产品入库出库批次主表      接口单据号
                    mainData.put("TradeNoIF", deliverNo);
                    //产品入库出库批次主表      入出库部门
                    mainData.put("BIN_OrganizationID", deliverDetailMap.get("organizationId"));
                    //产品入库出库批次主表      制单员工
                    mainData.put("BIN_EmployeeID", deliverDetailMap.get("employeeId"));
                    //产品入库出库批次主表      总数量
                    mainData.put("TotalQuantity", Math.abs(countTotal));
                    //产品入库出库批次主表      总金额
                    mainData.put("TotalAmount", deliverDetailMap.get("totalAmount"));
                    //产品入库出库批次主表      入出库区分 0：入库    1：出库     TODO: 盘点统一设置为0                 
                    mainData.put("StockType", countTotal>=0?"0":"1");
                    //产品入库出库批次主表      业务类型  P:盘点
                    mainData.put("TradeType", ProductConstants.BUSINESS_TYPE_STOCKTAKE);
                    //产品入库出库批次主表      关联单号  即为盘点单号
                    mainData.put("RelevanceNo", deliverDetailMap.get("stockTakingNo"));
                    mainData.put("StockTakingNoIF", deliverDetailMap.get("stockTakingNo"));
                    //产品入库出库批次主表     TODO:物流ID
                    mainData.put("BIN_LogisticInfoID", null);
                    //产品入库出库批次主表     入出库理由
                    mainData.put("Comments", deliverDetailMap.get("comments"));
                    //产品入库出库批次主表     入出库日期
                    mainData.put("StockInOutDate", CherryBatchUtil.getSysDateTime("yyyy-MM-dd"));
                    //产品入库出库批次主表     入出库时间
                    mainData.put("StockInOutTime", CherryBatchUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss"));
                    //产品入库出库批次主表     审核区分
                    mainData.put("VerifiedFlag", ProductConstants.AUDIT_FLAG_AGREE);
                    //入出库前产品总金额
                    mainData.put("TotalAmountBefore", null);
                    //入出库后产品总金额
                    mainData.put("TotalAmountAfter", null);  
                    //产品入库出库批次主表    共通字段
                    mainData.put("CreatedBy", ProductConstants.USERNAME);  
                    mainData.put("CreatePGM", ProductConstants.BINBESSPRO03);    
                    mainData.put("UpdatedBy", ProductConstants.USERNAME);  
                    mainData.put("UpdatePGM", ProductConstants.BINBESSPRO03);
                    
                    //insert 【产品入出库表】并返回自增长ID
                    try{
                        productStockInOutID = binBESSPRO03_Service.insertProductStockInOut(stockInOutMap);
                    }catch(Exception e){
                        BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                        batchExceptionDTO.setBatchName(this.getClass());
                        batchExceptionDTO.setErrorCode("ESS00026");
                        batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                        batchExceptionDTO.setException(e);
                        //组织ID：
                        batchExceptionDTO.addErrorParam(currentOrganizationInfoID);
                        //品牌ID：
                        batchExceptionDTO.addErrorParam(currentBrandInfoID);
                        //部门ID：
                        batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(deliverDetailMap.get("organizationId")));
                        //接口单据号
                        batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(deliverDetailMap.get("stockTakingNo")));
                        //入出库部门
                        batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(deliverDetailMap.get("organizationId")));
                        throw new CherryBatchException(batchExceptionDTO);
                    }
                    
                    
                }
                //【产品入出库明细表】和【产品入出库明细表】结构大致相同，仅需要整理下不同的几个字段即可
                //明细表    产品入出库记录ID
                deliverDetailMap.put("productStockInOutId", productStockInOutID);
                //明细表    明细连番
                deliverDetailMap.put("detailNo", n+1);
                //明细表    入出库区分 0：入库 1：出库
                int gainQuantity = CherryBatchUtil.string2int(String.valueOf(deliverDetailMap.get("gainQuantity")));
                if(gainQuantity>=0){
                    deliverDetailMap.put("stockType", "0");
                    //明细表    入出库数量
                    deliverDetailMap.put("quantity", gainQuantity);
                }else{
                    deliverDetailMap.put("stockType", "1");
                    //明细表    入出库数量
                    deliverDetailMap.put("quantity", 0-gainQuantity);
                }
                //明细表    共通字段
                deliverDetailMap.put(CherryBatchConstants.CREATEDBY, ProductConstants.USERNAME);   
                deliverDetailMap.put(CherryBatchConstants.CREATEPGM, ProductConstants.BINBESSPRO03); 
                deliverDetailMap.put(CherryBatchConstants.UPDATEDBY, ProductConstants.USERNAME);   
                deliverDetailMap.put(CherryBatchConstants.UPDATEPGM, ProductConstants.BINBESSPRO03);
                
                //产品厂商ID/数量 /价格/包装类型ID/实体仓库ID/逻辑仓库ID/仓库库位ID/入出库理由
                //以上字段已经存在于deliverDetailMap中，原样使用即可
                //TODO:insert 【产品入出库明细表】  性能优化时可尝试不在循环中插入，而是在外层插入list
                try{
                    binBESSPRO03_Service.insertProductStockDetail(deliverDetailMap);
                }catch(Exception e){
                    BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                    batchExceptionDTO.setBatchName(this.getClass());
                    batchExceptionDTO.setErrorCode("ESS00027");
                    batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchExceptionDTO.setException(e);
                    //产品入出库记录ID
                    batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productStockInOutID));
                    throw new CherryBatchException(batchExceptionDTO); 
                }
                //修改库存
                try{
                    int cnt = binBESSPRO03_Service.updateProduct(deliverDetailMap);
                    if(cnt<1){
                        binBESSPRO03_Service.insertProductStock(deliverDetailMap);
                    }
                    successCount ++;
                }catch(Exception e){
                    BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                    batchExceptionDTO.setBatchName(this.getClass());
                    batchExceptionDTO.setErrorCode("ESS00028");
                    batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchExceptionDTO.setException(e);
                    //产品厂商ID
                    batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(deliverDetailMap.get("productVendorId")));
                    //实体仓库ID
                    batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(deliverDetailMap.get("inventoryInfoId")));
                    //逻辑仓库ID
                    batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(deliverDetailMap.get("logicInventoryInfoId")));
                    throw new CherryBatchException(batchExceptionDTO); 
                }
                
                //产品入库出库批次从表       组织ID
                detailData.put("BIN_OrganizationInfoID", currentOrganizationInfoID);  
                //产品入库出库批次从表       品牌ID
                detailData.put("BIN_BrandInfoID", currentBrandInfoID);
                //产品入库出库批次从表      入出库部门
                detailData.put("BIN_OrganizationID", deliverDetailMap.get("organizationId"));
                //产品入库出库批次从表      制单员工
                detailData.put("BIN_EmployeeID", deliverDetailMap.get("employeeId"));
                //产品入库出库批次从表      业务类型  P:盘点
                detailData.put("TradeType", ProductConstants.BUSINESS_TYPE_STOCKTAKE);
                //产品入库出库批次从表      关联单号  即为盘点单号
                detailData.put("RelevanceNo", deliverDetailMap.get("stockTakingNo"));
                //产品入库出库批次从表     TODO:物流ID
                detailData.put("BIN_LogisticInfoID", null);
                //产品入库出库批次从表     入出库理由
                detailData.put("Comments", deliverDetailMap.get("comments"));
                //产品入库出库批次从表     入出库日期
                detailData.put("StockInOutDate", CherryBatchUtil.getSysDateTime("yyyy-MM-dd"));
                //产品入库出库批次从表     入出库时间
                detailData.put("StockInOutTime", CherryBatchUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss"));
                //产品入库出库批次从表     审核区分
                detailData.put("VerifiedFlag", ProductConstants.AUDIT_FLAG_AGREE);
                //产品入库出库批次从表    入出库前产品总金额
                detailData.put("TotalAmountBefore", null);
                //产品入库出库批次从表    入出库后产品总金额
                detailData.put("TotalAmountAfter", null);
                
                //产品入库出库批次从表    价格
                detailData.put("Price", deliverDetailMap.get("price"));
                //产品入库出库批次从表    产品厂商ID
                detailData.put("BIN_ProductVendorID", deliverDetailMap.get("productVendorId"));
                //产品入库出库批次从表    实体仓库ID
                detailData.put("BIN_InventoryInfoID", deliverDetailMap.get("inventoryInfoId"));
                //产品入库出库批次从表    逻辑仓库ID
                detailData.put("BIN_LogicInventoryInfoID", deliverDetailMap.get("logicInventoryInfoId"));
                //产品入库出库批次从表    仓库库位ID
                detailData.put("BIN_StorageLocationInfoID", deliverDetailMap.get("storageLocationInfoId"));
                //产品入库出库批次从表    包装ID
                detailData.put("BIN_ProductVendorPackageID", deliverDetailMap.get("productVendorPackageId"));
                
                if(null == detailData.get("BIN_ProductVendorPackageID")){
                	detailData.put("BIN_ProductVendorPackageID", 0);
                }
                if(null == detailData.get("BIN_LogicInventoryInfoID")){
                	detailData.put("BIN_LogicInventoryInfoID", 0);
                }
                if(null == detailData.get("BIN_StorageLocationInfoID")){
                	detailData.put("BIN_StorageLocationInfoID", 0);
                }
                
                //产品入库出库批次从表        入出库区分 0：入库 1：出库
                int quantity = CherryBatchUtil.string2int(String.valueOf(deliverDetailMap.get("gainQuantity")));
                if(quantity>=0){
                	detailData.put("StockType", "0");
                    //明细表    入出库数量
                    detailData.put("Quantity", quantity);
                }else{
                	detailData.put("StockType", "1");
                    //明细表    入出库数量
                    detailData.put("Quantity", 0-quantity);
                }
                
                
                //产品入库出库批次从表    共通字段
                detailData.put("CreatedBy", ProductConstants.USERNAME);  
                detailData.put("CreatePGM", ProductConstants.BINBESSPRO03);    
                detailData.put("UpdatedBy", ProductConstants.USERNAME);  
                detailData.put("UpdatePGM", ProductConstants.BINBESSPRO03);
                
                detailList.add(detailData);
            }
            for(int index=0;index<diffProduct.size();index++){
                //库存变化写入日志
                BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
                batchLoggerDTO.setCode("ISS00008");
                batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
                //柜台
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("counterName")));
                //产品名称
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("productName")));
                //厂商编码
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("unitCode")));
                //产品条码
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("barCode")));
                //实体仓库
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("inventoryName")));
                //逻辑仓库
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("logicInventoryName")));
                //同步前数量
                String newQuantity = CherryBatchUtil.getString(diffProduct.get(index).get("newQuantity"));
                if("".equals(newQuantity)){
                    batchLoggerDTO.addParam("null");
                }else{
                    batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("newQuantity")));
                }
                //同步后数量
                batchLoggerDTO.addParam(CherryBatchUtil.getString(diffProduct.get(index).get("oldQuantity")));
                CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
                cherryBatchLogger.BatchLogger(batchLoggerDTO);
            }
            
         // 是否记录产品入出库成本
            boolean isConfigOpen = binOLCM14_BL.isConfigOpen("1365", currentOrganizationInfoID, currentBrandInfoID);
            if(isConfigOpen){
            	// 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
            	binOLSTCM01_BL.handleProductInOutBatch(mainData, detailList);
            }
        }
    }
    
    /**
     * 同步数据
     */
    public int tran_sync(Map<String,Object> map) throws CherryBatchException{
        //总计数
        int totalCount = 0;
        
        //组织ID
        String organizationInfoId = CherryBatchUtil.getString(map.get("organizationInfoId"));
        //品牌ID
        String brandInfoId = CherryBatchUtil.getString(map.get("brandInfoId"));
        //盘点日期
        String deliverDate = CherryBatchUtil.getString(map.get("stockInOutDate"));
        //盘点原因
        String reasonAll = "BATCH同步库存";
        //柜台编号(text框)
        String textCounterCode = CherryBatchUtil.getString(map.get("counterCode"));
        
        //取得品牌CODE
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_BrandInfoID", brandInfoId);
        String brandCode = getBrandCode(param);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("counterCode", textCounterCode);
        List<Map<String,Object>> oldCounterInfoList = getOldCounterInfoList(paramMap);
        
        Map<String,Object> paramCounter = new HashMap<String,Object>();
        Map<String,Object> paramLogic = new HashMap<String,Object>();
        Map<String,Object> diffParam = new HashMap<String,Object>();
        Map<String,Object> productStockTaking = new HashMap<String,Object>();
        
        paramLogic.put(CherryBatchConstants.ORGANIZATIONINFOID, organizationInfoId);
        paramLogic.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
        List<Map<String,Object>> newCounterLogicInfoList = binBESSPRO03_Service.getNewCounterLogicInfoList(paramLogic);
        
        for(Map<String,Object> counterInfo :oldCounterInfoList){
            try{
                String counterCode = ConvertUtil.getString(counterInfo.get("counterCode"));
                String logicInventoryCode = ConvertUtil.getString(counterInfo.get("logicInventoryCode"));
                paramCounter.put("counterCode", counterCode);
                paramCounter.put(CherryBatchConstants.ORGANIZATIONINFOID, organizationInfoId);
                paramCounter.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
                List<Map<String,Object>> newCounterDepotInfoList = binBESSPRO03_Service.getNewCounterDepotInfoList(paramCounter);
                //柜台ID
                String counterInfoId = "";
                String counterName = "";
                //盘点部门ID
                String organizationId = "";
                //盘点实体仓库ID
                String depotId = "";
                String depotCode = "";
                String depotName = "";
                if(null != newCounterDepotInfoList && newCounterDepotInfoList.size()>0){
                    counterInfoId = ConvertUtil.getString(newCounterDepotInfoList.get(0).get("BIN_CounterInfoID"));
                    counterName = ConvertUtil.getString(newCounterDepotInfoList.get(0).get("CounterNameIF"));
                    organizationId = ConvertUtil.getString(newCounterDepotInfoList.get(0).get("BIN_OrganizationID"));
                    depotId = ConvertUtil.getString(newCounterDepotInfoList.get(0).get("BIN_DepotInfoID"));
                    depotCode = ConvertUtil.getString(newCounterDepotInfoList.get(0).get("DepotCode"));
                    depotName = ConvertUtil.getString(newCounterDepotInfoList.get(0).get("DepotNameCN"));
                }else{
                    continue;
                }
                
                //盘点逻辑仓库ID
                int logicInventoryInfoId = 0;
                String logicInventoryName = "";
                boolean hasLogicId = false;
                if(null != newCounterLogicInfoList && newCounterLogicInfoList.size()>0){
                    for(Map<String,Object> newCounterLogicInfo :newCounterLogicInfoList){
                        if(logicInventoryCode.equals(newCounterLogicInfo.get("LogicInventoryCode"))){
                            logicInventoryInfoId = CherryBatchUtil.Object2int(newCounterLogicInfo.get("BIN_LogicInventoryInfoID"));
                            logicInventoryName = CherryBatchUtil.getString(newCounterLogicInfo.get("InventoryNameCN"));
                            hasLogicId = true;
                            break; 
                        }
                    }
                }
                if(!hasLogicId){
                    continue;
                }
                
                diffParam.put(CherryBatchConstants.ORGANIZATIONINFOID, organizationInfoId);
                diffParam.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
                diffParam.put("counterInfoId", counterInfoId);
                diffParam.put("organizationId", organizationId);
                diffParam.put("inventoryInfoId", depotId);
                diffParam.put("logicInventoryInfoId", logicInventoryInfoId);
                diffParam.put("brandCode", brandCode);
                //取得柜台号、逻辑仓库Code
                diffParam.put("counterCode", counterCode);
                diffParam.put("counterName", counterName);
                diffParam.put("inventoryCode", depotCode);
                diffParam.put("inventoryName", depotName);
                diffParam.put("logicInventoryCode", logicInventoryCode);
                diffParam.put("logicInventoryName", logicInventoryName);
                diffParam.put("isSync", "true");
                diffParam.put("filterPrtNotExist", CherryBatchUtil.getString(map.get("filterPrtNotExist")));
                diffParam.put("filterStockNotExist", CherryBatchUtil.getString(map.get("filterStockNotExist")));
                List<Map<String,Object>> diffProduct = getDiffProductList(diffParam);//同一柜台同一仓库（实体、逻辑）下的
                totalCount += diffProduct.size();
                //厂商ID
                int[] productVendorIDArr = new int[diffProduct.size()];
                //账面数量
                int[] bookCountArr = new int[diffProduct.size()];
                //盘点数量
                int[] quantityuArr = new int[diffProduct.size()];
                //盘差数量 
                int[] gainCount = new int[diffProduct.size()];
                //盘差金额
                double[] gainMoney = new double[diffProduct.size()];
                //产品单价
                double[] priceUnitArr = new double[diffProduct.size()];
                //一次盘点的总盈亏数量
                int totalQuantity =0;
                //一次盘点的总盈亏金额
                double totalAmount =0;
                if(diffProduct.size()>0){
                    for(int i=0;i<diffProduct.size();i++){
                        productVendorIDArr[i] = CherryBatchUtil.Object2int(diffProduct.get(i).get("productVendorId"));
                        bookCountArr[i] = CherryBatchUtil.Object2int(diffProduct.get(i).get("newQuantity"));
                        quantityuArr[i] = CherryBatchUtil.Object2int(diffProduct.get(i).get("oldQuantity"));
                        priceUnitArr[i] = CherryBatchUtil.bigDecimalTofloat(diffProduct.get(i).get("salePrice"));
                        gainCount[i] = quantityuArr[i]-bookCountArr[i];
                        gainMoney[i] = gainCount[i]*priceUnitArr[i];
                        
                        //统计一单的总数量和总金额
                        int tempCount = quantityuArr[i]-bookCountArr[i];
                        double money = (priceUnitArr[i])*tempCount;
                        totalAmount += money;
                        totalQuantity += tempCount; 
                    }

                    //盘点处理
                    //取得单据号
                    String stockTakingNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,ProductConstants.USERNAME, ProductConstants.BUSINESS_TYPE_STOCKTAKE);
                    productStockTaking.put(CherryBatchConstants.ORGANIZATIONINFOID, organizationInfoId);
                    productStockTaking.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
                    productStockTaking.put("stockTakingNo", stockTakingNo);
                    productStockTaking.put("stockTakingNoIF", stockTakingNo);
                    productStockTaking.put("relevanceNo", null);
                    productStockTaking.put("organizationId", organizationId);
                    productStockTaking.put("employeeId", ProductConstants.EMPLOYEEID);
                    productStockTaking.put("totalQuantity", totalQuantity);
                    productStockTaking.put("totalAmount", totalAmount);
                    productStockTaking.put("verifiedFlag", ProductConstants.AUDIT_FLAG_AGREE);
                    productStockTaking.put("type", ProductConstants.STOCKTAKING_TYPE_FREEDOM);
                    productStockTaking.put("isBatch", ProductConstants.Batch_No);
                    productStockTaking.put("comments", reasonAll);
                    productStockTaking.put("date", deliverDate);
                    productStockTaking.put(CherryBatchConstants.CREATEDBY, ProductConstants.USERNAME);
                    productStockTaking.put(CherryBatchConstants.CREATEPGM, ProductConstants.BINBESSPRO03);
                    productStockTaking.put(CherryBatchConstants.UPDATEDBY, ProductConstants.USERNAME);
                    productStockTaking.put(CherryBatchConstants.UPDATEPGM, ProductConstants.BINBESSPRO03);
                        
                    //插入 【产品盘点单据概要表】
                    int productStockTakingId = 0;
                    try{
                        productStockTakingId = insertProductStockTaking(productStockTaking);
                    }catch(Exception e){
                        BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                        batchExceptionDTO.setBatchName(this.getClass());
                        batchExceptionDTO.setErrorCode("ESS00024");
                        batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                        batchExceptionDTO.setException(e);
                        //组织ID
                        batchExceptionDTO.addErrorParam(organizationInfoId);
                        //品牌ID
                        batchExceptionDTO.addErrorParam(brandInfoId);
                        //部门ID
                        batchExceptionDTO.addErrorParam(organizationId);
                        throw new CherryBatchException(batchExceptionDTO);
                    }
                    
                    List<Map<String,Object>> productStockTakingDetailList = new ArrayList<Map<String,Object>>();
                    for(int i=0;i<diffProduct.size();i++){
                        Map<String,Object> productStockTakingDetail = new HashMap();
                        productStockTakingDetail.put("productStockTakingId", productStockTakingId);
                        productStockTakingDetail.put("productVendorId", productVendorIDArr[i]);
                        productStockTakingDetail.put("productBatchId", null);
                        productStockTakingDetail.put("detailNo", i+1);
                        productStockTakingDetail.put("quantity", bookCountArr[i]);
                        productStockTakingDetail.put("price", priceUnitArr[i]);
                        productStockTakingDetail.put("productVendorPackageId", 0);
                        productStockTakingDetail.put("gainQuantity", gainCount[i]);
                        productStockTakingDetail.put("inventoryInfoId", depotId);
                        productStockTakingDetail.put("logicInventoryInfoId", logicInventoryInfoId);
                        productStockTakingDetail.put("storageLocationInfoId", 0);
                        productStockTakingDetail.put("comments", reasonAll);
                        productStockTakingDetail.put("handleType", ProductConstants.HandleType_MANUAL);
                        productStockTakingDetail.put(CherryBatchConstants.CREATEDBY, ProductConstants.USERNAME);
                        productStockTakingDetail.put(CherryBatchConstants.CREATEPGM, ProductConstants.BINBESSPRO03);
                        productStockTakingDetail.put(CherryBatchConstants.UPDATEDBY, ProductConstants.USERNAME);
                        productStockTakingDetail.put(CherryBatchConstants.UPDATEPGM, ProductConstants.BINBESSPRO03);
                        productStockTakingDetailList.add(productStockTakingDetail);
                    }
                    //插入【产品盘点单据明细表】
                    try{
                        insertProductTakingDetail(productStockTakingDetailList);
                    }catch(Exception e){
                        BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                        batchExceptionDTO.setBatchName(this.getClass());
                        batchExceptionDTO.setErrorCode("ESS00025");
                        batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                        batchExceptionDTO.setException(e);
                        //产品盘点ID
                        batchExceptionDTO.addErrorParam(String.valueOf(productStockTakingId));
                        throw new CherryBatchException(batchExceptionDTO);
                    }

                    //入出库处理
                    insertStockInOutAndDetail(new int[]{productStockTakingId},map,diffProduct);
                    
                    // 事务提交
                    binBESSPRO03_Service.manualCommit();
                }
            }catch(Throwable e){
                e.printStackTrace();
                // 事务回滚
                binBESSPRO03_Service.manualRollback();
                failCount ++;
                flag = CherryBatchConstants.BATCH_WARNING;
            }
        }

        // 处理总件数
        BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
        batchLoggerDTO2.setCode("IIF00001");
        batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO2.addParam(String.valueOf(totalCount));
        // 成功件数
        BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
        batchLoggerDTO3.setCode("IIF00002");
        batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO3.addParam(String.valueOf(successCount));
        // 失败件数
        BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
        batchLoggerDTO5.setCode("IIF00005");
        batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO5.addParam(String.valueOf(failCount));
        
        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
        // 处理总件数
        cherryBatchLogger.BatchLogger(batchLoggerDTO2);
        // 成功件数
        cherryBatchLogger.BatchLogger(batchLoggerDTO3);
        // 失败件数
        cherryBatchLogger.BatchLogger(batchLoggerDTO5);
        
        return flag;
    }
    
    /**
     * 取得老后台柜台List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getOldCounterInfoList(Map<String, Object> map){
        return binBESSPRO03_Service.getOldCounterInfoList(map);
    }
    
    /**
     * 取得实体仓库ID
     * 
     * @param map
     * @return
     */
    public int getDepotID(Map<String, Object> map){
        int depotID = 0;
        List<Map<String,Object>> list = binBESSPRO03_Service.getNewCounterDepotInfoList(map);
        if(null != list && list.size()>0){
            depotID = CherryUtil.obj2int(list.get(0).get("BIN_DepotInfoID"));
        }
        return depotID;
    }
    
    /**
     * 取得柜台ID
     * 
     * @param map
     * @return
     */
    public int getCounterID(Map<String, Object> map){
        int counterID = 0;
        List<Map<String,Object>> list = binBESSPRO03_Service.getNewCounterDepotInfoList(map);
        if(null != list && list.size()>0){
            counterID = CherryUtil.obj2int(list.get(0).get("BIN_CounterInfoID"));
        }
        return counterID;
    }
    
    /**
     * 取得逻辑仓库ID
     * 
     * @param map
     * @return
     */
    public int getLogicInventoryInfoID(Map<String, Object> map){
        int logicInventoryInfoID = 0;
        List<Map<String,Object>> list = binBESSPRO03_Service.getNewCounterLogicInfoList(map);
        if(null != list && list.size()>0){
            logicInventoryInfoID = CherryUtil.obj2int(list.get(0).get("BIN_LogicInventoryInfoID"));
        }
        return logicInventoryInfoID;
    }
    
    /**
     * 取得品牌CODE
     * 
     * @param map
     * @return
     */
    public String getBrandCode(Map<String, Object> map){
        String brandCode = "";
        List<Map<String,Object>> list = binBESSPRO03_Service.getBrandInfoList(map);
        if(null != list && list.size()>0){
            brandCode = CherryBatchUtil.getString(list.get(0).get("BrandCode"));
        }
        return brandCode;
    }
    
    /**
     * 取得新后台柜台List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getNewCounterDepotInfoList(Map<String, Object> map){
        return binBESSPRO03_Service.getNewCounterDepotInfoList(map);
    }
 
    /**
     * 取得逻辑仓库List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getNewCounterLogicInfoList(Map<String, Object> map){
        return binBESSPRO03_Service.getNewCounterLogicInfoList(map);
    }
    
    /**
     * 取得产品信息List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getNewProductList(Map<String, Object> map){
        return binBESSPRO03_Service.getNewProductList(map);
    }
    
    public byte[] exportExcel(Map<String, Object> map) throws Exception{
        String organizationInfoID = ConvertUtil.getString(map.get(CherryBatchConstants.ORGANIZATIONINFOID));
        String brandInfoID = ConvertUtil.getString(map.get(CherryBatchConstants.BRANDINFOID));
        String counterCode = ConvertUtil.getString(map.get("counterCode"));
        Map<String,Object> departParam = new HashMap<String,Object>();
        departParam.put("BIN_OrganizationInfoID", organizationInfoID);
        departParam.put("BIN_BrandInfoID", brandInfoID);
        departParam.put("counterCode", counterCode);
        List<Map<String,Object>> list = getOldCounterInfoList(departParam);
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for(int i=0;i<list.size();i++){
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("counterCode", list.get(i).get("counterCode"));
            param.put("organizationInfoId", map.get("organizationInfoId"));
            param.put("brandInfoId", map.get("brandInfoId"));
            List<Map<String,Object>> newCounterDepotInfoList = getNewCounterDepotInfoList(param);
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
                continue;
            }
            //部门ID
            param.put("organizationId",organID);
            //柜台ID
            param.put("counterInfoId",counterInfoId);
            param.put("counterName", counterName);
            //实体仓库ID
            param.put("inventoryInfoId",depotID);
            param.put("inventoryCode", depotCode);
            param.put("inventoryName", depotName);
            //逻辑仓库号
            param.put("logicInventoryCode", list.get(i).get("logicInventoryCode"));
            int logicInventoryInfoID = 0;
            String logicInventoryName = "";
            List<Map<String,Object>> newCounterLogicInfoList = getNewCounterLogicInfoList(param);
            if(null != newCounterLogicInfoList && newCounterLogicInfoList.size()>0){
                logicInventoryInfoID = CherryBatchUtil.Object2int(newCounterLogicInfoList.get(0).get("BIN_LogicInventoryInfoID"));
                logicInventoryName = CherryBatchUtil.getString(newCounterLogicInfoList.get(0).get("InventoryNameCN"));
            }else{
                continue;
            }
            //逻辑仓库ID
            param.put("logicInventoryInfoId",logicInventoryInfoID);
            param.put("logicInventoryName", logicInventoryName);
            
            param.put("filterPrtNotExist", CherryBatchUtil.getString(map.get("filterPrtNotExist")));
            param.put("filterStockNotExist", CherryBatchUtil.getString(map.get("filterStockNotExist")));

            List differStockList = getDiffProductList(param);
            if(null != differStockList && differStockList.size()>0){
                dataList.addAll(differStockList);
            }
        }
        
        String[][] array = {
                { "counterName", "DepartCodeName", "25", "", "" },
                { "inventoryName", "DepotCodeName", "20", "", "" },
                { "logicInventoryName", "LogicCodeName", "20", "", "" },
                { "productName", "productName", "20", "", "" },
                { "unitCode", "unitCode", "15", "", "" },
                { "barCode", "barCode", "15", "", "" },
                { "newQuantity", "newQuantity", "15", "right", "" },
                { "oldQuantity", "oldQuantity", "15", "right", "" }
        };
        
        ExcelParam ep = new ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINBESSPRO03");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binBESSPRO04_BL.getExportExcel(ep);
    }
}
