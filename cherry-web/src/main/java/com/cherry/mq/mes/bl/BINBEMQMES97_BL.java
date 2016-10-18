/*  
 * @(#)BINBEMQMES97_BL.java     1.0 2012/12/06      
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
package com.cherry.mq.mes.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.annota.TimeLog;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.service.BINBEMQMES97_Service;

/**
 * 查询共通 BL
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public class BINBEMQMES97_BL{
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
    
    @Resource(name="binBEMQMES97_Service")
    private BINBEMQMES97_Service binBEMQMES97_Service;
    
    /**
     * 查询部门信息
     * @param map (BIN_OrganizationInfoID,BIN_BrandInfoID,CounterCode)
     * @param errorFlag true:抛错
     * @throws CherryMQException
     */
    public Map<String,Object> getOrganizationInfo(Map<String,Object> map,boolean errorFlag) throws CherryMQException{
        // 取得部门信息
        Map<String,Object> resultMap = binBEMQMES97_Service.selCounterDepartmentInfo(map);
        if(null == resultMap || null == resultMap.get("BIN_OrganizationID")){
            // 没有查询到相关部门信息
            if(errorFlag){
                MessageUtil.addMessageWarning(map,"柜台号为\""+map.get("CounterCode")+"\""+MessageConstants.MSG_ERROR_06);
            }
        }
        return resultMap;
    }
    
    /**
     * 查询员工信息
     * @param map (BIN_OrganizationInfoID,BIN_BrandInfoID,EmployeeCode)
     * @param errorFlag true:抛错
     * @throws CherryMQException
     */
    public Map<String,Object> getEmployeeInfo(Map<String,Object> map,boolean errorFlag) throws CherryMQException{
        // 查询员工信息
        Map<String,Object> resultMap = binBEMQMES97_Service.selEmployeeInfo(map);
        if(null == resultMap || null == resultMap.get("BIN_EmployeeID")){
            // 没有查询到相关员工信息
            if(errorFlag){
                MessageUtil.addMessageWarning(map,"员工号为\""+map.get("EmployeeCode")+"\""+MessageConstants.MSG_ERROR_07);
            }
        }
        return resultMap;
    }
    
    /**
     * 查询仓库ID
     * @param map (BIN_OrganizationID,CounterCode)
     * @param errorFlag true:抛错
     * @throws CherryMQException
     */
    public int getInventoryInfoID(Map<String,Object> map,boolean errorFlag) throws CherryMQException{
        int inventoryInfoID = 0;
        // 查询仓库信息
        List<Map<String, Object>> inventoryInfoList = binOLCM18_BL.getDepotsByDepartID(map.get("BIN_OrganizationID").toString(), "");
        if(null != inventoryInfoList && inventoryInfoList.size()>0){
            Map<String,Object> resultMap = (Map<String,Object>) inventoryInfoList.get(0);
            inventoryInfoID = CherryUtil.obj2int(resultMap.get("BIN_DepotInfoID"));
        }else{
            if(errorFlag){
                MessageUtil.addMessageWarning(map,"柜台号为\""+map.get("CounterCode")+"\""+MessageConstants.MSG_ERROR_36);
            }
        }
        return inventoryInfoID;
    }

    /**
     * 查询逻辑仓库ID
     * @param map
     * @param param (BIN_BrandInfoID,LogicInventoryCode)
     * @param errorFlag true:抛错
     * @return 逻辑仓库ID
     * @throws Exception
     */
    public int getLogicInventoryInfoID(Map<String,Object> map,Map<String,Object> param,boolean errorFlag) throws Exception{
        int logicInventoryInfoID =0;
        //逻辑仓库
        Map<String,Object> logicInventoryInfoMap = new HashMap<String,Object>();
        logicInventoryInfoMap.put("BIN_BrandInfoID", param.get("BIN_BrandInfoID"));
        logicInventoryInfoMap.put("LogicInventoryCode", param.get("LogicInventoryCode"));
        logicInventoryInfoMap.put("Type", "1");//终端逻辑仓库 
        logicInventoryInfoMap.put("language", null);
        try{
            Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
            if(logicInventoryInfo != null && !logicInventoryInfo.isEmpty()){
                logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
            }
        }catch(Exception e){
            // 没有查询到相关逻辑仓库信息
            if(errorFlag){
                throw e;
            }
        }
        return logicInventoryInfoID;
    }
    
    /**
     * 取产品ID
     * @param map
     * @param param (BIN_OrganizationInfoID,BIN_BrandInfoID,BarCode,UnitCode,TradeDateTime)
     * @param errorFlag true:抛错
     * @return
     * @throws CherryMQException
     */
    public int getProductVendorID(Map<String,Object> map,Map<String,Object> param,boolean errorFlag) throws CherryMQException{
        int productVendorID = 0;
        // 查询产品信息
        Map<String,Object> resultMap = binBEMQMES97_Service.selProductInfo(param);
        if (null == resultMap || resultMap.get("BIN_ProductVendorID")==null){
            //查找对应的产品条码对应关系表
            resultMap = binBEMQMES97_Service.selPrtBarCode(param);
            Map<String,Object> temp = new HashMap<String,Object>();
            
            if(resultMap!=null){
                temp.put("BIN_ProductVendorID", resultMap.get("BIN_ProductVendorID"));
                //根据产品条码，继续查找产品表
                resultMap = binBEMQMES97_Service.selProductInfoByPrtVenID(resultMap);
                if(resultMap==null){
                     // 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
                     List<Map<String,Object>> list = binBEMQMES97_Service.selProAgainByPrtVenID(temp);
                     if(list!=null&&!list.isEmpty()){
                         resultMap = (Map<String,Object>)list.get(0);
                     }else{
                         resultMap = new HashMap<String,Object>();
                         resultMap.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
                     }
                }
            }
            if(resultMap==null){
                //在产品条码对应关系表里找不到，放开时间条件再找一次，找不到抛错
                List<Map<String,Object>> prtBarCodeList = binBEMQMES97_Service.selPrtBarCodeList(param);
                if(null != prtBarCodeList && prtBarCodeList.size()>0){
                    //取tradeDateTime与StartTime最接近的第一条
                    resultMap = new HashMap<String,Object>();
                    resultMap.put("BIN_ProductVendorID", prtBarCodeList.get(0).get("BIN_ProductVendorID"));
                }else{
                    // 没有查询到相关商品信息
                    if(errorFlag){
                        MessageUtil.addMessageWarning(map, "厂商编码为\""+param.get("UnitCode")+"\"产品条码为\""+param.get("BarCode")+"\""+MessageConstants.MSG_ERROR_09);
                    }
                }
            }
        }
        if(null != resultMap && resultMap.get("BIN_ProductVendorID")!=null){
            productVendorID = CherryUtil.obj2int(resultMap.get("BIN_ProductVendorID"));
        }
        return productVendorID;
    }
    
    /**
     * 取促销品厂商ID、ExPoint
     * @param map
     * @param param (BIN_BrandInfoID,BIN_OrganizationID,BarCode,UnitCode,TradeDateTime)
     * @param errorFlag true:抛错
     * @return Map
     * @throws CherryMQException
     */
    public Map<String,Object> getPrmInfo(Map<String,Object> map,Map<String,Object> param,boolean errorFlag) throws CherryMQException{
        Map resultMap = binBEMQMES97_Service.selPrmProductInfo(param);
        if(resultMap==null||resultMap.get("BIN_PromotionProductVendorID") == null){
             resultMap = binBEMQMES97_Service.selPrmProductPrtBarCodeInfo(param);
             Map<String,Object> temp = new HashMap<String,Object>();
            
             if(resultMap!=null){//促销品信息unitcode或barcode存在变更
                 temp.put("BIN_PromotionProductVendorID", resultMap.get("BIN_PromotionProductVendorID"));
                 resultMap = binBEMQMES97_Service.selPrmProductInfoByPrmVenID(resultMap);
                 if(resultMap==null){
                     // 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
                     List list = binBEMQMES97_Service.selPrmAgainByPrmVenID(temp);
                     if(list!=null&&!list.isEmpty()){
                         resultMap = (HashMap)list.get(0);
                     }else{
                         //查询促销产品信息  根据促销产品厂商ID，不区分有效状态
                         list = binBEMQMES97_Service.selPrmByPrmVenID(temp);
                         if(list!=null&&!list.isEmpty()){
                             resultMap = (HashMap)list.get(0);
                         }
                     }
                 }
             }else{
                 //在促销产品条码对应关系表里找不到，放开时间条件再找一次
                 List<Map<String,Object>> prmPrtBarCodeList = binBEMQMES97_Service.selPrmPrtBarCodeList(param);
                 if(null != prmPrtBarCodeList && prmPrtBarCodeList.size()>0){
                     //取tradeDateTime与StartTime最接近的第一条
                     temp.put("BIN_PromotionProductVendorID", prmPrtBarCodeList.get(0).get("BIN_PromotionProductVendorID"));
                     //查询促销产品信息  根据促销产品厂商ID，不区分有效状态
                     List list = binBEMQMES97_Service.selPrmByPrmVenID(temp);
                     if(list!=null&&!list.isEmpty()){
                         resultMap = (HashMap)list.get(0);
                     }
                 }
             }
             //明细里detailType为促销品时，resultMap为null抛错
             if((null == resultMap || resultMap.get("BIN_PromotionProductVendorID") == null)){
                 if(errorFlag){
                     // 没有查询到相关商品信息
                     MessageUtil.addMessageWarning(map, "厂商编码为\""+param.get("UnitCode")+"\"促销品条码为\""+param.get("BarCode")+"\""+MessageConstants.MSG_ERROR_09);
                 }else if(null == resultMap){
                     resultMap = new HashMap();
                 }
             }
        }
        return resultMap;
    }
    
    /**
     * 查询会员信息
     * @param map (BIN_OrganizationInfoID,BIN_BrandInfoID,MemCode)
     * @param errorFlag true:抛错
     * @throws CherryMQException
     */
    @TimeLog
    public Map<String,Object> getMemberInfo(Map<String,Object> map,boolean errorFlag) throws CherryMQException{
        // 查询会员信息
        Map<String,Object> resultMap = binBEMQMES97_Service.selMemberInfo(map);
        if(null == resultMap || null == resultMap.get("BIN_MemberInfoID")){
            // 没有查询到相关会员信息
            if(errorFlag){
                MessageUtil.addMessageWarning(map,"会员号为\""+map.get("MemCode")+"\""+MessageConstants.MSG_ERROR_34);
            }
        }
        return resultMap;
    }
    
    /**
     * 查询问题ID
     * @param map 
     * @param param (BIN_OrganizationInfoID,BIN_BrandInfoID,BIN_PaperID,DisplayOrder)
     * @param errorFlag true:抛错
     * @throws CherryMQException
     */
    public int getQuestionID(Map<String,Object> map,Map<String,Object> param,boolean errorFlag) throws CherryMQException{
        // 查询问题ID
        int paperQuestionID = 0;
        Map<String,Object> resultMap = binBEMQMES97_Service.selQuestionID(param);
        if(null == resultMap || null == resultMap.get("BIN_PaperQuestionID")){
            // 没有查询到相关问题信息
            if(errorFlag){
                MessageUtil.addMessageWarning(map, "问卷ID为\""+param.get("BIN_PaperID")+"\""+"题号为\""+param.get("DisplayOrder")+"\""+MessageConstants.MSG_ERROR_13);
            }
        }else{
            paperQuestionID = CherryUtil.obj2int(resultMap.get("BIN_PaperQuestionID"));
        }
        return paperQuestionID;
    }
}