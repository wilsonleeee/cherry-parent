/*  
 * @(#)BINOLSTCM13_BL.java     1.0 2012/7/24      
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
package com.cherry.st.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.interfaces.BINOLSTCM21_IF;
import com.cherry.st.common.service.BINOLSTCM13_Service;
import com.cherry.st.common.service.BINOLSTCM21_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品退库申请单操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.07.24
 */
public class BINOLSTCM21_BL implements BINOLSTCM21_IF{

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource(name="binOLSTCM09_BL")
    private BINOLSTCM09_IF binOLSTCM09_BL;
    
    @Resource(name="binOLSTCM21_Service")
    private BINOLSTCM21_Service binOLSTCM21_Service;
    
    @Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
    
    /**
     * 给退货申请单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    @Override
    public Map<String, Object> getSaleReturnRequestMainData(int saleReturnRequestID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_SaleReturnRequestID", saleReturnRequestID);
        map.put("language", language);
        return binOLSTCM21_Service.getSaleReturnRequestMainData(map);
    }
    
    /**
     * 给退库申请主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getSaleReturnReqDetailData(int saleReturnRequestID, String language,Map<String,Object> otherParam) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_SaleReturnRequestID", saleReturnRequestID);
        map.put("language", language);
        if(null != otherParam){
            //排序方式
            String organizationInfoID = ConvertUtil.getString(otherParam.get("BIN_OrganizationInfoID"));
            String brandInfoID = ConvertUtil.getString(otherParam.get("BIN_BrandInfoID"));
            String detailOrderBy = binOLCM14_BL.getConfigValue("1120", organizationInfoID, brandInfoID);
            map.put("detailOrderBy", detailOrderBy);
        }
        return binOLSTCM21_Service.getSaleReturnReqDetailData(map);
    }
    
    /**
     * 给退库申请主ID，取得支付明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getSaleReturnReqPayDetailData(int saleReturnRequestID, String language,Map<String,Object> otherParam) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_SaleReturnRequestID", saleReturnRequestID);
        map.put("language", language);
        if(null != otherParam){
            //排序方式
            String organizationInfoID = ConvertUtil.getString(otherParam.get("BIN_OrganizationInfoID"));
            String brandInfoID = ConvertUtil.getString(otherParam.get("BIN_BrandInfoID"));
            String detailOrderBy = binOLCM14_BL.getConfigValue("1120", organizationInfoID, brandInfoID);
            map.put("detailOrderBy", detailOrderBy);
        }
        return binOLSTCM21_Service.getSaleReturnReqPayDetailData(map);
    }
    /**
     * 将退库申请信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    @Override
    public int insertSaleReturnRequestAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList,List<Map<String, Object>> detailPayList) {
        int saleReturnRequestID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String billNo = ConvertUtil.getString(mainData.get("BillCode"));
        String billNoIF = ConvertUtil.getString(mainData.get("BillNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = ConvertUtil.getString(mainData.get("TradeType"));
        if("".equals(bussType)){
        	bussType = CherryConstants.OS_BILLTYPE_SA;
        }
        //如果billNo不存在调用共通生成单据号
        if(null == billNo || "".equals(billNo)){
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("BillCode", billNo);
        }
        if(null == billNoIF || "".equals(billNoIF)){
            mainData.put("BillNoIF", billNo);
        }       
        
        //插入产品退库申请单主表
        saleReturnRequestID = binOLSTCM21_Service.insertSaleReturnRequest(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> proReturnReqDetail = detailList.get(i);
            proReturnReqDetail.put("BIN_SaleReturnRequestID", saleReturnRequestID);
            proReturnReqDetail.put("DetailNo", i+1);
        }
        binOLSTCM21_Service.insertSaleReturnReqDetail(detailList);
        
        for(int i=0;i<detailPayList.size();i++){
            Map<String,Object> proReturnReqDetail = detailPayList.get(i);
            proReturnReqDetail.put("BIN_SaleReturnRequestID", saleReturnRequestID);
            proReturnReqDetail.put("DetailNo", i+1);
        }
        binOLSTCM21_Service.insertSaleReturnReqPayDetail(detailPayList);
        
        return saleReturnRequestID;
    }
    
    /**
     * 根据退库申请单创建退库申请单
     * @param praMap
     * @return
     */
    @Override
    public int createSaleReturnRequest(Map<String, Object> praMap) {
        int saleReturnRequestID = CherryUtil.obj2int(praMap.get("BIN_SaleReturnRequestID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        
        //取得退库申请单主表
        Map<String,Object> saleReturnReqMap = getSaleReturnRequestMainData(saleReturnRequestID,null);
        
        List<Map<String,Object>> saleReturnReqDetail = getSaleReturnReqDetailData(saleReturnRequestID,null,null);
        for(int i=0;i<saleReturnReqDetail.size();i++){
        	Map<String,Object> temp = saleReturnReqDetail.get(i);
        	temp.put("CreatedBy", createdBy);
        	temp.put("CreatePGM", createPGM);
        	temp.put("UpdatedBy", createdBy);
        	temp.put("UpdatePGM", createPGM);
        }
        
        List<Map<String,Object>> saleReturnReqPayDetail = getSaleReturnReqPayDetailData(saleReturnRequestID,null,null);
        
        saleReturnReqMap.put("RelevanceNo", saleReturnReqMap.get("BillCode"));
        saleReturnReqMap.put("BillCode", null);
        saleReturnReqMap.put("BillNoIF", null);
        saleReturnReqMap.put("TradeType", CherryConstants.OS_BILLTYPE_SJ);
        saleReturnReqMap.put("CreatedBy", createdBy);
        saleReturnReqMap.put("CreatePGM", createPGM);
        saleReturnReqMap.put("UpdatedBy", createdBy);
        saleReturnReqMap.put("UpdatePGM", createPGM);
        
        int newSaleReturnRequestID = insertSaleReturnRequestAll(saleReturnReqMap, saleReturnReqDetail,saleReturnReqPayDetail);
        return newSaleReturnRequestID;
    }
       
    /**
     * 修改退库申请单主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateSaleReturnRequest(Map<String, Object> praMap) {
        return binOLSTCM21_Service.updateSaleReturnRequest(praMap);
    }
    
    /**
     * 发送退库审核MQ
     * @param pramMap
     * @throws Exception 
     */
    @Override
    public void sendMQ(int[] saleReturnRequestIDArr, Map<String, String> pramMap) throws Exception {
        for(int i=0;i<saleReturnRequestIDArr.length;i++){
        	 Map<String,Object> mainData = getSaleReturnRequestMainData(saleReturnRequestIDArr[i], null);
        	
        	 String tradeDateTime = binOLSTCM21_Service.getConvertSysDate();
        	 
        	 Map<String,Object> mainDataMap = new HashMap<String,Object>();
             mainDataMap.put("BrandCode", pramMap.get("BrandCode"));
             mainDataMap.put("TradeNoIF", ConvertUtil.getString(pramMap.get("BillNoIF")));
             mainDataMap.put("ModifyCounts", "");
             mainDataMap.put("TradeType", CherryConstants.OS_BILLTYPE_SJ);
             String auditResult = "OK";
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if (CherryConstants.SAAUDIT_FLAG_AGREE.equals(verifiedFlag)) {
                 auditResult = "OK";               
             } else if(CherryConstants.SAAUDIT_FLAG_DISAGREE.equals(verifiedFlag)){
                 auditResult = "NG";
             }
             
             mainDataMap.put("SubType", auditResult);
             mainDataMap.put("CounterCode", ConvertUtil.getString(pramMap.get("OrganizationCode")));             
             mainDataMap.put("RelevantNo", ConvertUtil.getString(mainData.get("RelevanceNo")));
             mainDataMap.put("Reason", ConvertUtil.getString(mainData.get("Reason")));
             mainDataMap.put("TradeDate", tradeDateTime.split(" ")[0]);
             mainDataMap.put("TradeTime", tradeDateTime.split(" ")[1]);
             
        	 Map<String,Object> dataLine = new HashMap<String,Object>();
             dataLine.put("MainData", mainDataMap);
             
             Map<String,Object> msgDataMap = new HashMap<String,Object>();
             msgDataMap.put(CherryConstants.MESSAGE_VERSION_TITLE, MessageConstants.MESSAGE_VERSION_SJ);
             msgDataMap.put(CherryConstants.MESSAGE_DATATYPE_TITLE, CherryConstants.DATATYPE_APPLICATION_JSON);
             msgDataMap.put(CherryConstants.DATALINE_JSON_XML,dataLine);
     
             // MQ消息 DTO
             MQInfoDTO mqInfoDTO = new MQInfoDTO();
             //消息发送队列名
//             mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
             mqInfoDTO.setMsgQueueName("cherryToPosMsgQueue");
             // 单据号
             mqInfoDTO.setBillCode(ConvertUtil.getString(mainData.get("BillNoIF")));
             // 单据类型
             mqInfoDTO.setBillType(CherryConstants.OS_BILLTYPE_SJ);
             // 所属品牌
             mqInfoDTO.setBrandInfoId(CherryUtil.obj2int(mainData.get("BIN_BrandInfoID")));
             // 所属组织
             mqInfoDTO.setOrganizationInfoId(CherryUtil.obj2int(mainData.get("BIN_OrganizationInfoID")));
             // 柜台号
             mqInfoDTO.setCounterCode(ConvertUtil.getString(pramMap.get("OrganizationCode")));
             // 消息体
             mqInfoDTO.setMsgDataMap(msgDataMap);
             // 创建者
             mqInfoDTO.setCreatedBy(pramMap.get("BIN_UserID"));
             // 更新者
             mqInfoDTO.setUpdatedBy(pramMap.get("BIN_UserID"));
             // 创建模块
             mqInfoDTO.setCreatePGM(pramMap.get("CurrentUnit"));
             // 更新模块
             mqInfoDTO.setUpdatePGM(pramMap.get("CurrentUnit"));
             
             // 业务流水
             DBObject dbObject = new BasicDBObject();
             // 组织代号
             dbObject.put("OrgCode", pramMap.get("OrganizationInfoCode"));
             // 品牌代码，即品牌简称
             dbObject.put("BrandCode", pramMap.get("BrandCode"));
             // 业务类型
             dbObject.put("TradeType", CherryConstants.OS_BILLTYPE_SJ);
             // 单据号
             dbObject.put("TradeNoIF", ConvertUtil.getString(mainData.get("BillNoIF")));
             // 修改次数
             dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
             // 发生时间
             dbObject.put("OccurTime", tradeDateTime);
             // 事件内容
             dbObject.put("Content", mqInfoDTO.getData());
             // 业务流水
             mqInfoDTO.setDbObject(dbObject);
             
             binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
        }
    }
    
    /**
     * pos机确认退库后，要来完成工作流（根据工作流ID来结束相应的工作流）
     * @param mainData
     * @throws WorkflowException 
     * @throws InvalidInputException 
     * @throws NumberFormatException 
     */
    @Override
    public void posConfirmReturnFinishFlow(Map<String, Object> mainData) throws Exception{
        String flowID = String.valueOf(mainData.get("WorkFlowID"));
        if(flowID!=null&&!"".equals(flowID)&&!"null".equals(flowID)){
            int[] actionArr = workflow.getAvailableActions(Long.parseLong(flowID), null);
            if(actionArr==null||actionArr.length==0){
                return;
            }
            
            Map<String,Object> updateParam = new HashMap<String,Object>();
            updateParam.put(CherryConstants.OS_ACTOR_TYPE_USER, "MQ");
            updateParam.put("CurrentUnit", "MQ");
            updateParam.put(CherryConstants.OS_MAINKEY_OPERATE_FLAG, CherryConstants.OS_MAINKEY_OPERATE_POS);
            updateParam.put("TradeDateTime", mainData.get("TradeDateTime"));
            Map<String,Object> input = new HashMap<String,Object>();
            input.put("mainData", updateParam);

            workflow.doAction_single(Long.parseLong(flowID), actionArr[0], input);
        }
    }


}
