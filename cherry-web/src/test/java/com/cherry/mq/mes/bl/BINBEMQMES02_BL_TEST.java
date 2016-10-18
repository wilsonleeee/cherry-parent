package com.cherry.mq.mes.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.common.CherryMessageReceiverImpl;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.service.BINBEMQMES02_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.workflow.ProFlowRA_FN;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.opensymphony.workflow.Workflow;

public class BINBEMQMES02_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binBEMQMES02_BL")
    private BINBEMQMES02_BL binBEMQMES02_BL;
    
    @Resource(name="binBEMQMES02_Service")
    private BINBEMQMES02_Service binBEMQMES02_Service;
    
    @Resource(name="binBEMQMES99_BL")
    private BINBEMQMES99_BL binBEMQMES99_BL;
    
    @Resource(name="workflow")
    private Workflow workflow;
    
    private static List<Map<String,Object>> tradeNoIFList = new ArrayList<Map<String,Object>>();
        
    @AfterClass
    public static void afterClass() throws Exception {
        CherryMessageReceiverImpl.brandMap.clear();
        
        //MongoDB删除日志
        for(int i=0;i<tradeNoIFList.size();i++){
            Map<String,Object> temp = tradeNoIFList.get(i);
            String tradeNoIF = ConvertUtil.getString(temp.get("tradeNoIF"));
            if(!"".equals(tradeNoIF)){
                DBObject removeCondition = new BasicDBObject();
                removeCondition.put("TradeNoIF", tradeNoIF);
                MongoDB.removeAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, removeCondition);
                MongoDB.findAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, removeCondition);
            }
        }
    }
    
    /**
     * 执行MQ接收
     * @param msg
     * @throws Exception
     */
    public void tran_analyzeMessage(String msg) throws Exception{
        // 调用共通将消息体解析成Map
        String msgFlag = "Old";
        Map<String,Object> map = MessageUtil.message2Map(msg);
        Object mainDataDTO = null;
        if(null == map){
            mainDataDTO = Message2Bean.parseMessage((String) msg);//消息转化成DTO
            map = (Map<String, Object>) Bean2Map.toHashMap(mainDataDTO);//DTO转化成map
        }else{
            msgFlag = "New";
        }
        if("Old".equals(msgFlag)){
            //老消息体
            binBEMQMES99_BL.tran_analyzeMessage(mainDataDTO, map);
        }else{
            //新消息体
            binBEMQMES99_BL.tran_analyzeMessage(map);
        }
        tradeNoIFList.add(map);
    }
    
    /**
     * 组成消息体
     * @param messageBody
     * @return
     */
    public String getMessageBody(Map<String,Object> messageBody){
        StringBuffer msg = new StringBuffer();
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        return msg.toString();
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSetDetailDataInfo1() throws Exception {
        String caseName = "testSetDetailDataInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_PromotionProduct
        Map<String,Object> promotionProductInsertMap = dataList.get(0);
        int promotionProductID = testCOM_Service.insertTableData(promotionProductInsertMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object > promotionProductVendorInsertMap = dataList.get(1);
        promotionProductVendorInsertMap.put("BIN_PromotionProductID", promotionProductID);
        int promotionProductVendorID = testCOM_Service.insertTableData(promotionProductVendorInsertMap);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object > promotionPrtBarCodeInsertMap = dataList.get(2);
        promotionPrtBarCodeInsertMap.put("BIN_PromotionProductVendorID", promotionProductVendorID);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object > logicInventoryInsertMap = dataList.get(3);
        testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_PromotionProduct Set ValidFlag = '0' WHERE BIN_PromotionProductID="+promotionProductID);
        testCOM_Service.update("Update Basis.BIN_PromotionProductVendor Set ValidFlag = '0' WHERE BIN_PromotionProductVendorID="+promotionProductVendorID);
                
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", promotionPrtBarCodeInsertMap.get("OldUnitCode"));
        detailDto.put("barcode", promotionPrtBarCodeInsertMap.get("OldBarCode"));
        detailDto.put("inventoryTypeCode", "DF01");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-8-28");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-8-28 13:01:30");
        map.put("tradeType", "GR");
        
        binBEMQMES02_BL.setDetailDataInfo(detailDataList, map);
        
        assertEquals(ConvertUtil.getString(promotionProductVendorID),ConvertUtil.getString(detailDataList.get(0).get("promotionProductVendorID")));
        assertEquals("CXLP",ConvertUtil.getString(detailDataList.get(0).get("PromotionCateCD")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSetDetailDataInfo2() throws Exception {
        String caseName = "testSetDetailDataInfo2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap = dataList.get(2);
        prtBarCodeInsertMap.put("BIN_ProductVendorID", productVendorID);
        testCOM_Service.insertTableData(prtBarCodeInsertMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(3);
        testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
                
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", prtBarCodeInsertMap.get("OldUnitCode"));
        detailDto.put("barcode", prtBarCodeInsertMap.get("OldBarCode"));
        detailDto.put("inventoryTypeCode", "DF01");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-8-28");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-8-28 13:01:30");
        map.put("tradeType", "GR");
        
        binBEMQMES02_BL.setDetailDataInfo(detailDataList, map);
        
        assertEquals(ConvertUtil.getString(productVendorID),ConvertUtil.getString(detailDataList.get(0).get("productVendorID")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSetDetailDataInfo3() throws Exception {
        String caseName = "testSetDetailDataInfo3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_PromotionProduct
        Map<String,Object> promotionProductInsertMap = dataList.get(0);
        int promotionProductID = testCOM_Service.insertTableData(promotionProductInsertMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object > promotionProductVendorInsertMap = dataList.get(1);
        promotionProductVendorInsertMap.put("BIN_PromotionProductID", promotionProductID);
        int promotionProductVendorID = testCOM_Service.insertTableData(promotionProductVendorInsertMap);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object > promotionPrtBarCodeInsertMap1 = dataList.get(2);
        promotionPrtBarCodeInsertMap1.put("BIN_PromotionProductVendorID", promotionProductVendorID);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap1);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object > promotionPrtBarCodeInsertMap2 = dataList.get(3);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap2);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object > promotionPrtBarCodeInsertMap3 = dataList.get(4);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap3);
        
        //Basis.BIN_LogicInventory
        Map<String,Object > logicInventoryInsertMap = dataList.get(5);
        testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_PromotionProduct Set ValidFlag = '0' WHERE BIN_PromotionProductID="+promotionProductID);
        testCOM_Service.update("Update Basis.BIN_PromotionProductVendor Set ValidFlag = '0' WHERE BIN_PromotionProductVendorID="+promotionProductVendorID);
                
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", promotionPrtBarCodeInsertMap1.get("OldUnitCode"));
        detailDto.put("barcode", promotionPrtBarCodeInsertMap1.get("OldBarCode"));
        detailDto.put("inventoryTypeCode", "DF01");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");
        
        binBEMQMES02_BL.setDetailDataInfo(detailDataList, map);
        
        assertEquals(ConvertUtil.getString(promotionProductVendorID),ConvertUtil.getString(detailDataList.get(0).get("promotionProductVendorID")));
        assertEquals("TZZK",ConvertUtil.getString(detailDataList.get(0).get("PromotionCateCD")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSetDetailDataInfo4() throws Exception {
        String caseName = "testSetDetailDataInfo4";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap1 = dataList.get(2);
        prtBarCodeInsertMap1.put("BIN_ProductVendorID", productVendorID);
        testCOM_Service.insertTableData(prtBarCodeInsertMap1);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap2 = dataList.get(3);
        testCOM_Service.insertTableData(prtBarCodeInsertMap2);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap3 = dataList.get(4);
        testCOM_Service.insertTableData(prtBarCodeInsertMap3);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(5);
        testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
                
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", prtBarCodeInsertMap1.get("OldUnitCode"));
        detailDto.put("barcode", prtBarCodeInsertMap1.get("OldBarCode"));
        detailDto.put("inventoryTypeCode", "DF01");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");
        
        binBEMQMES02_BL.setDetailDataInfo(detailDataList, map);
        
        assertEquals(ConvertUtil.getString(productVendorID),ConvertUtil.getString(detailDataList.get(0).get("productVendorID")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSetDetailDataInfo5() throws Exception {
        String caseName = "testSetDetailDataInfo5";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_PromotionProduct
        Map<String,Object> promotionProductInsertMap = dataList.get(0);
        int promotionProductID = testCOM_Service.insertTableData(promotionProductInsertMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object > promotionProductVendorInsertMap = dataList.get(1);
        promotionProductVendorInsertMap.put("BIN_PromotionProductID", promotionProductID);
        int promotionProductVendorID = testCOM_Service.insertTableData(promotionProductVendorInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_PromotionProduct Set ValidFlag = '0' WHERE BIN_PromotionProductID="+promotionProductID);
        testCOM_Service.update("Update Basis.BIN_PromotionProductVendor Set ValidFlag = '0' WHERE BIN_PromotionProductVendorID="+promotionProductVendorID);
                
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", promotionProductInsertMap.get("UnitCode"));
        detailDto.put("barcode", promotionProductVendorInsertMap.get("BarCode"));
        detailDto.put("inventoryTypeCode", "DF01");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");
        
        try{
            binBEMQMES02_BL.setDetailDataInfo(detailDataList, map);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
            String expectException = ConvertUtil.getString(expectList.get(0).get("expectException"));
            expectException = expectException.replaceAll("UnitCode", ConvertUtil.getString(promotionProductInsertMap.get("UnitCode")));
            expectException = expectException.replaceAll("BarCode", ConvertUtil.getString(promotionProductVendorInsertMap.get("BarCode")));
            expectException = expectException.replaceAll("TradeType", ConvertUtil.getString(map.get("tradeType")));
            assertEquals(expectException,e.getMessage());
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSetDetailDataInfo6() throws Exception {
        String caseName = "testSetDetailDataInfo6";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
       
        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
                
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", productInsertMap.get("UnitCode"));
        detailDto.put("barcode", productVendorInsertMap.get("BarCode"));
        detailDto.put("inventoryTypeCode", "DF01");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");
        
        try{
            binBEMQMES02_BL.setDetailDataInfo(detailDataList, map);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
            String expectException = ConvertUtil.getString(expectList.get(0).get("expectException"));
            expectException = expectException.replaceAll("UnitCode", ConvertUtil.getString(productInsertMap.get("UnitCode")));
            expectException = expectException.replaceAll("BarCode", ConvertUtil.getString(productVendorInsertMap.get("BarCode")));
            expectException = expectException.replaceAll("TradeType", ConvertUtil.getString(map.get("tradeType")));
            assertEquals(expectException,e.getMessage());
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSelPrtGetPriceByPrtVenID1() throws Exception {
        String caseName = "testSelPrtGetPriceByPrtVenID1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);

        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductVendorID", productVendorID);
        Map<String,Object> productMap = binBEMQMES02_Service.selPrtGetPriceByPrtVenID(map);
        assertFalse(productMap.isEmpty());
        
        map = new HashMap<String,Object>();
        map.put("BIN_ProductVendorID", 0);
        productMap = binBEMQMES02_Service.selPrtGetPriceByPrtVenID(map);
        assertTrue(productMap == null);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeDeliverData1() throws Exception {
        String caseName = "testAnalyzeDeliverData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testCOM_Service.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(1);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testCOM_Service.insertTableData(productOrderDetailInsertMap);
        
        //加载工作流文件
        String orgCode = "LQX";
        String brandCode = "LQX";
        String flowName = "proFlowOD";
        String flowFile = "proFlowOD_LQX.xml";
        String sysName = "st";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode,sysName);
        
        //设置订单一审自动审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(52).getMetaAttributes();
        metaAttributes.put("OS_Rule",metaAttributes.get("OS_Rule").toString().replace("\"AutoAuditFlag\":\"false\"", "\"AutoAuditFlag\":\"true\""));
        workflow.getWorkflowDescriptor(wfName).getStep(52).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(521).setMetaAttributes(metaAttributes);
        
        //设置订单二审自动审核
        workflow.getWorkflowDescriptor(wfName).getStep(58).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(581).setMetaAttributes(metaAttributes);
        
        //设置订单三审自动审核
        workflow.getWorkflowDescriptor(wfName).getStep(61).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(611).setMetaAttributes(metaAttributes);
        
        //启动工作流
        Map<String,Object> newMap1 = new HashMap<String,Object>();
        newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        newMap1.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        newMap1.put("BIN_EmployeeID", "123");
        newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "123");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,"-9998");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "10");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        newMap1.put("BIN_OrganizationID", "10");
        newMap1.put("CurrentUnit", "MQ");
        newMap1.put("BIN_BrandInfoID", "99");
        newMap1.put("BIN_OrganizationInfoID", "99");
        newMap1.put("BrandCode", "LQX");
        newMap1.put("OrganizationCode", "TestCase001");
        newMap1.put("UserInfo", userInfo);
        newMap1.put("tradeDateTime", "2012-9-3 11:00:30");
        
        long workflowID = binOLSTCM00_BL.StartOSWorkFlow(newMap1);
        
        testCOM_Service.update("update Inventory.BIN_ProductOrder set workflowid='"+workflowID+"' where BIN_ProductOrderID = '"+productOrderID+"'");
        
        //接收KS发货单
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("subType", "SD");
        map.put("organizationID", "10");
        map.put("tradeNoIF", productOrderInsertMap.get("OrderNoIF"));
        map.put("totalQuantity", "100");
        map.put("totalAmount", "10000.00");
        map.put("tradeDate", "2012-9-3");
        map.put("relevantNo", productOrderInsertMap.get("OrderNoIF"));
        map.put("organizationInfoID", "99");
        map.put("brandInfoID", "99");
        map.put("orgCode", "LQX");
        map.put("brandCode","LQX");
        map.put("positionCategoryID", "10");
        map.put("employeeID", "123");
        map.put("createdBy", "TestCase");
        map.put("createPGM", "TestCase");
        map.put("updatedBy", "TestCase");
        map.put("updatePGM", "TestCase");
        List<Map<String,Object>> detailDataDTOList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDataDTO = new HashMap<String,Object>();
        detailDataDTO.put("quantity", productOrderDetailInsertMap.get("Quantity"));
        detailDataDTO.put("price", productOrderDetailInsertMap.get("Price"));
        detailDataDTO.put("tradeNoIF", productOrderInsertMap.get("OrderNoIF"));
        detailDataDTOList.add(detailDataDTO);
        map.put("detailDataDTOList", detailDataDTOList);
        binBEMQMES02_BL.analyzeDeliverData(map);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductDeliver");
        paramMap.put("RelevanceNo", productOrderInsertMap.get("OrderNoIF"));
        List<Map<String,Object>> productDeliverList = testCOM_Service.getTableData(paramMap);
        assertEquals(1,productDeliverList.size());
        assertEquals("",ConvertUtil.getString(productDeliverList.get(0).get("DeliverType")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeDeliverData2() throws Exception {
        //订货拒绝
        String caseName = "testAnalyzeDeliverData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_BrandInfoID(brandInfoID);
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Organization
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(2);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(3);
        insertProductVendorMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Inventory.BIN_ProductOrder
        Map<String,Object> insertProductOrderMap = dataList.get(4);
        insertProductOrderMap.put("BIN_OrganizationInfoID",organizationInfoID);
        insertProductOrderMap.put("BIN_BrandInfoID",brandInfoID);
        int productOrderID = testCOM_Service.insertTableData(insertProductOrderMap);

        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> insertProductOrderDetailMap = dataList.get(5);
        insertProductOrderDetailMap.put("BIN_ProductOrderID",productOrderID);
        insertProductOrderDetailMap.put("BIN_ProductVendorID",productVendorID);
        testCOM_Service.insertTableData(insertProductOrderDetailMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(6);
        insertCounterInfoMap.put("BIN_OrganizationInfoID",organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID",brandInfoID);
        insertCounterInfoMap.put("BIN_OrganizationID",organizationID);
        testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoInfoMap = dataList.get(7);
        insertDepotInfoInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoInfoMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = dataList.get(8);
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "proFlowOD";
        String flowFile = "proFlowOD_LQX.xml";
        String sysName = "st";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode,sysName);

        //设置订单一审自动审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(52).getMetaAttributes();
        metaAttributes.put("OS_Rule",metaAttributes.get("OS_Rule").toString().replace("\"AutoAuditFlag\":\"false\"", "\"AutoAuditFlag\":\"true\""));
        workflow.getWorkflowDescriptor(wfName).getStep(52).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(521).setMetaAttributes(metaAttributes);
        
        //设置订单二审自动审核
        workflow.getWorkflowDescriptor(wfName).getStep(58).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(581).setMetaAttributes(metaAttributes);
        
        //设置订单三审自动审核
        workflow.getWorkflowDescriptor(wfName).getStep(61).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(611).setMetaAttributes(metaAttributes);
        
        //启动工作流
        Map<String,Object> newMap1 = new HashMap<String,Object>();
        newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        newMap1.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        newMap1.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "123");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,"-9998");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "10");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        newMap1.put("BIN_OrganizationID", organizationID);
        newMap1.put("CurrentUnit", "MQ");
        newMap1.put("BIN_BrandInfoID", brandInfoID);
        newMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        newMap1.put("BrandCode", userInfo.getBrandCode());
        newMap1.put("OrganizationCode", "TCCounter001");
        newMap1.put("UserInfo", userInfo);
        newMap1.put("tradeDateTime", "2012-9-3 11:00:30");
        
        long workflowID = binOLSTCM00_BL.StartOSWorkFlow(newMap1);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        //订货拒绝
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductOrder");
        paramMap.put("BIN_ProductOrderID", productOrderID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals("1",ConvertUtil.getString(actualList.get(0).get("ValidFlag")));
        assertEquals("999",workflow.getPropertySet(workflowID).getString("OS_Current_Operate"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeDeliverData3() throws Exception {
        //退库申请拒绝
        String caseName = "testAnalyzeDeliverData3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_BrandInfoID(brandInfoID);
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Organization
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(2);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(3);
        insertProductVendorMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Inventory.BIN_ProReturnRequest
        Map<String,Object> insertProReturnRequestMap = dataList.get(4);
        insertProReturnRequestMap.put("BIN_OrganizationInfoID",organizationInfoID);
        insertProReturnRequestMap.put("BIN_BrandInfoID",brandInfoID);
//
        //Inventory.BIN_ProReturnReqDetail
        Map<String,Object> insertProReturnReqDetailMap = dataList.get(5);
        insertProReturnReqDetailMap.put("BIN_ProductVendorID",productVendorID);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(6);
        insertCounterInfoMap.put("BIN_OrganizationInfoID",organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID",brandInfoID);
        insertCounterInfoMap.put("BIN_OrganizationID",organizationID);
        testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoInfoMap = dataList.get(7);
        insertDepotInfoInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoInfoMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = dataList.get(8);
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "proFlowRA";
        String flowFile = "proFlowRA_LQX.xml";
        String sysName = "st";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode,sysName);

        //启动工作流
        String counterCode = "TCCounter001";
        Map<String,Object> newMap1 = new HashMap<String,Object>();
        newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RA);
        newMap1.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "123");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,"-9998");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "10");
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        newMap1.put("BIN_OrganizationID", organizationID);
        newMap1.put("CurrentUnit", "MQ");
        newMap1.put("BIN_BrandInfoID", brandInfoID);
        newMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        newMap1.put("BrandCode", userInfo.getBrandCode());
        newMap1.put("OrganizationCode", counterCode);
        newMap1.put("UserInfo", userInfo);
        newMap1.put("tradeDateTime", "2012-9-3 11:00:30");
        newMap1.put("returnReqMainData", insertProReturnRequestMap);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(insertProReturnReqDetailMap);
        newMap1.put("returnReqDetailList", detailList);
        
        long workflowID = binOLSTCM00_BL.StartOSWorkFlow(newMap1);
                
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        //退库申请拒绝
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProReturnRequest");
        paramMap.put("BillNoIF", ConvertUtil.getString(insertProReturnRequestMap.get("BillNoIF")));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals("1",ConvertUtil.getString(actualList.get(0).get("ValidFlag")));
        assertEquals("999",workflow.getPropertySet(workflowID).getString("OS_Current_Operate"));
        
        //发送MQ消息
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Interfaces.BIN_MQLog");
        paramMap.put("SendOrRece", "S");
        paramMap.put("BillType", "RJ");
        paramMap.put("BillCode", ConvertUtil.getString(insertProReturnRequestMap.get("BillNoIF")));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(counterCode,ConvertUtil.getString(actualList.get(0).get("CounterCode")));
        String sendMQMsg = ConvertUtil.getString(actualList.get(0).get("Data"));
        assertTrue(sendMQMsg.indexOf("\"BrandCode\":\""+brandCode+"\"")>0);
        assertTrue(sendMQMsg.indexOf("\"TradeType\":\"RJ\"")>0);
        assertTrue(sendMQMsg.indexOf("\"CounterCode\":\""+counterCode+"\"")>0);
        assertTrue(sendMQMsg.indexOf("NG")>0);
    }

    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName,String sysName){
        String rootpath = ProFlowRA_FN.class.getResource("/").getPath();
        rootpath = rootpath.replace("test-classes", "classes");
        String path = "";
        if("".equals(sysName)){
            path = rootpath + "worflowfile/st/"+fileName;
        }else{
            path = rootpath + "worflowfile/"+sysName+"/"+fileName;
        }
         
        return path;
    }
    
    /**
     * 加载工作流文件到内存中
     * @param filePath
     * @param orgCode
     * @param brandCode
     * @param fileCode
     * @throws Exception
     */
    public void loadWorkFlowDescriptor(String filePath,String fileCode,String orgCode,String brandCode,String sysName) throws Exception{
        String path = getWorkFlowFilePath(filePath,sysName);
        String fileContentNew = FileUtils.readFileToString(new File(path),"UTF-8");
        FileStoreDTO fileStoreNew = null;
        FileStoreDTO fileStoreDTO = binolcm30IF.getFileStoreByCode(fileCode, orgCode, brandCode);
        fileStoreNew = fileStoreDTO;
        ConvertUtil.convertDTO(fileStoreNew, fileStoreDTO, true);
        fileStoreNew.setFileStoreId(0);
        fileStoreNew.setFileCode(fileCode);
        fileStoreNew.setOrgCode(orgCode);
        fileStoreNew.setBrandCode(brandCode);
        // 刷新内存的工作流文件内容
        fileStoreNew.setFileContent(fileContentNew);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSelMessageInfo1() throws Exception {
        String caseName = "testSelMessageInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TestCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertMap = dataList.get(0);
        insertMap.put("BIN_OrganizationID", organizationID);
        insertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertMap);
        
        //Basis.BIN_Employee;
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(1);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(2);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", depotInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        int inventoryInfoID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(3);
        logicInventoryInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        logicInventoryInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryInfoID = testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(4);
        productInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        productInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(5);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
        
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDto = new HashMap<String,Object>();
        detailDto.put("unitcode", productInsertMap.get("UnitCode"));
        detailDto.put("barcode", productVendorInsertMap.get("BarCode"));
        detailDto.put("inventoryTypeCode", logicInventoryInsertMap.get("LogicInventoryCode"));
        detailDto.put("BAcode", "TestBA001");
        detailDataList.add(detailDto);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("counterCode", "TestCounter001");
        map.put("organizationInfoID", organizationInfoID);
        map.put("brandInfoID", brandInfoID);
        map.put("memberCode", null);
        map.put("detailDataDTOList", detailDataList);
        map.put("tradeType", "GR");
        binBEMQMES02_BL.selMessageInfo(map);
        assertEquals("NP",ConvertUtil.getString(map.get("consumer_type")));
        
        
        map = new HashMap<String,Object>();
        map.put("counterCode", "TestCounter001");
        map.put("organizationInfoID", organizationInfoID);
        map.put("brandInfoID", brandInfoID);
        map.put("memberCode", "");
        map.put("detailDataDTOList", detailDataList);
        map.put("tradeType", "GR");
        binBEMQMES02_BL.selMessageInfo(map);
        assertEquals("NP",ConvertUtil.getString(map.get("consumer_type")));
        
        map = new HashMap<String,Object>();
        map.put("counterCode", "TestCounter001");
        map.put("organizationInfoID", organizationInfoID);
        map.put("brandInfoID", brandInfoID);
        map.put("memberCode", "000000000");
        map.put("detailDataDTOList", detailDataList);
        map.put("tradeType", "GR");
        binBEMQMES02_BL.selMessageInfo(map);
        assertEquals("NP",ConvertUtil.getString(map.get("consumer_type")));
        
        map = new HashMap<String,Object>();
        map.put("counterCode", "TestCounter001");
        map.put("organizationInfoID", organizationInfoID);
        map.put("brandInfoID", brandInfoID);
        map.put("memberCode", "TC0000000");
        map.put("detailDataDTOList", detailDataList);
        map.put("tradeType", "GR");
        binBEMQMES02_BL.selMessageInfo(map);
        assertEquals("MP",ConvertUtil.getString(map.get("consumer_type")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeStockBirPresentData1() throws Exception{
        //产品礼品领用（有coupon）
        String caseName = "testAnalyzeStockBirPresentData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(0);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_Product
        insertProductMap = dataList.get(2);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        insertProductVendorMap = dataList.get(3);
        insertProductVendorMap.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(4);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicInventory
        insertLogicInventoryMap = dataList.get(5);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(6);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(7);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(8);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", depotInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        int inventoryInfo_IDENTITYID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap = dataList.get(9);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
        //Promotion.BIN_PromotionActGrp
        Map<String,Object> promotionActGrpInsertMap = dataList.get(10);
        promotionActGrpInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActGrpInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionActGrpID = testCOM_Service.insertTableData(promotionActGrpInsertMap);
        
        //Promotion.BIN_PromotionActivity
        Map<String,Object> promotionActivityInsertMap = dataList.get(11);
        promotionActivityInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActivityInsertMap.put("BIN_BrandInfoID", brandInfoID);
        promotionActivityInsertMap.put("BIN_PromotionActGrpID", promotionActGrpID);
        int promotionActivityID = testCOM_Service.insertTableData(promotionActivityInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(12);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(13);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap = dataList.get(14);
        insertCampaignOrderMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID = testCOM_Service.insertTableData(insertCampaignOrderMap);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap1 = dataList.get(15);
        insertCampaignOrderDetailMap1.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap1);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap2 = dataList.get(16);
        insertCampaignOrderDetailMap2.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap2);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        String productInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        actualParamMap.put("BIN_ProductInOutID", productInOutID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
               
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PROStockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(1,actualList.size());
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(0).get("MainCode"));
        assertEquals("SP",actualList.get(0).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        int giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals(organizationInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(brandInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_BrandInfoID")));
        assertEquals(organizationID,CherryUtil.obj2int(actualList.get(0).get("BIN_OrganizationID")));
        assertEquals(insertCampaignOrderMap.get("TradeNoIF"),ConvertUtil.getString(actualList.get(0).get("RelevanceNo")));
        assertEquals(insertCampaignOrderMap.get("TradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));
        assertEquals(insertCampaignOrderMap.get("SubType"),ConvertUtil.getString(actualList.get(0).get("SubType")));
        assertEquals(insertCampaignOrderMap.get("CouponCode"),ConvertUtil.getString(actualList.get(0).get("CouponCode")));
        assertEquals(insertCampaignOrderMap.get("CampaignCode"),ConvertUtil.getString(actualList.get(0).get("CampaignCode")));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("InformType")));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(productVendorID1,CherryUtil.obj2int(actualList.get(0).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType1"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(1).get("MainCode")));
        assertEquals(productVendorID2,CherryUtil.obj2int(actualList.get(1).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(1).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(1).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType2"),ConvertUtil.getString(actualList.get(1).get("GiftType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("OK",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeStockBirPresentData2() throws Exception{
        //产品礼品领用（无coupon）
        String caseName = "testAnalyzeStockBirPresentData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(0);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_Product
        insertProductMap = dataList.get(2);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        insertProductVendorMap = dataList.get(3);
        insertProductVendorMap.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(4);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicInventory
        insertLogicInventoryMap = dataList.get(5);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(6);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(7);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(8);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", depotInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        int inventoryInfo_IDENTITYID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap = dataList.get(9);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
        //Promotion.BIN_PromotionActGrp
        Map<String,Object> promotionActGrpInsertMap = dataList.get(10);
        promotionActGrpInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActGrpInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionActGrpID = testCOM_Service.insertTableData(promotionActGrpInsertMap);
        
        //Promotion.BIN_PromotionActivity
        Map<String,Object> promotionActivityInsertMap = dataList.get(11);
        promotionActivityInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActivityInsertMap.put("BIN_BrandInfoID", brandInfoID);
        promotionActivityInsertMap.put("BIN_PromotionActGrpID", promotionActGrpID);
        int promotionActivityID = testCOM_Service.insertTableData(promotionActivityInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(12);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(13);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap = dataList.get(14);
        insertCampaignOrderMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID = testCOM_Service.insertTableData(insertCampaignOrderMap);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap1 = dataList.get(15);
        insertCampaignOrderDetailMap1.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap1);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap2 = dataList.get(16);
        insertCampaignOrderDetailMap2.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap2);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        String productInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        actualParamMap.put("BIN_ProductInOutID", productInOutID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
               
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PROStockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(1,actualList.size());
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(0).get("MainCode"));
        assertEquals("SP",actualList.get(0).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        int giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals(organizationInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(brandInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_BrandInfoID")));
        assertEquals(organizationID,CherryUtil.obj2int(actualList.get(0).get("BIN_OrganizationID")));
        assertEquals(insertCampaignOrderMap.get("TradeNoIF"),ConvertUtil.getString(actualList.get(0).get("RelevanceNo")));
        assertEquals(insertCampaignOrderMap.get("TradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));
        assertEquals(insertCampaignOrderMap.get("SubType"),ConvertUtil.getString(actualList.get(0).get("SubType")));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("CouponCode")));
        assertEquals(insertCampaignOrderMap.get("CampaignCode"),ConvertUtil.getString(actualList.get(0).get("CampaignCode")));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("InformType")));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(productVendorID1,CherryUtil.obj2int(actualList.get(0).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType1"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(1).get("MainCode")));
        assertEquals(productVendorID2,CherryUtil.obj2int(actualList.get(1).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(1).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(1).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType2"),ConvertUtil.getString(actualList.get(1).get("GiftType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("OK",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeStockBirPresentData3() throws Exception{
        //产品+促销品礼品领用（有coupon）
        String caseName = "testAnalyzeStockBirPresentData3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(0);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_Product
        insertProductMap = dataList.get(2);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        insertProductVendorMap = dataList.get(3);
        insertProductVendorMap.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(4);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicInventory
        insertLogicInventoryMap = dataList.get(5);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(6);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(7);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(8);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", depotInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        int inventoryInfo_IDENTITYID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap = dataList.get(9);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
        //Promotion.BIN_PromotionActGrp
        Map<String,Object> promotionActGrpInsertMap = dataList.get(10);
        promotionActGrpInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActGrpInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionActGrpID = testCOM_Service.insertTableData(promotionActGrpInsertMap);
        
        //Promotion.BIN_PromotionActivity
        Map<String,Object> promotionActivityInsertMap = dataList.get(11);
        promotionActivityInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActivityInsertMap.put("BIN_BrandInfoID", brandInfoID);
        promotionActivityInsertMap.put("BIN_PromotionActGrpID", promotionActGrpID);
        int promotionActivityID = testCOM_Service.insertTableData(promotionActivityInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(12);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(13);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap = dataList.get(14);
        insertCampaignOrderMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID = testCOM_Service.insertTableData(insertCampaignOrderMap);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap1 = dataList.get(15);
        insertCampaignOrderDetailMap1.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap1);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap2 = dataList.get(16);
        insertCampaignOrderDetailMap2.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap2);
        
        //Basis.BIN_PromotionProduct
        Map<String,Object> insertPromotionProductMap = dataList.get(17);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID1 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> insertPromotionProductVendorMap = dataList.get(18);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID1);
        int promotionProductVendorID1 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Basis.BIN_PromotionProduct
        insertPromotionProductMap = dataList.get(19);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID2 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        insertPromotionProductVendorMap = dataList.get(20);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID2);
        int promotionProductVendorID2 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap3 = dataList.get(21);
        insertCampaignOrderDetailMap3.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap3.put("BIN_ProductVendorID", promotionProductVendorID1);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap3);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap4 = dataList.get(22);
        insertCampaignOrderDetailMap4.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap4.put("BIN_ProductVendorID", promotionProductVendorID2);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap4);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        //产品
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        String productInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        actualParamMap.put("BIN_ProductInOutID", productInOutID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
               
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PROStockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        //促销品
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualParamMap.put("RelevantNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        String promotionStockInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_PromotionStockInOutID"));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualParamMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(promotionProductVendorID1,actualList.get(0).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
               
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStock");
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PRMStockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStock");
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualParamMap.put("MainCode", expectList.get(0).get("MainCode2"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(1,actualList.size());
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(0).get("CampaignCode"));
        assertEquals("SP",actualList.get(0).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualParamMap.put("MainCode", expectList.get(0).get("MainCode3"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(1,actualList.size());
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(0).get("CampaignCode"));
        assertEquals("SP",actualList.get(0).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        int giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals(organizationInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(brandInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_BrandInfoID")));
        assertEquals(organizationID,CherryUtil.obj2int(actualList.get(0).get("BIN_OrganizationID")));
        assertEquals(insertCampaignOrderMap.get("TradeNoIF"),ConvertUtil.getString(actualList.get(0).get("RelevanceNo")));
        assertEquals(insertCampaignOrderMap.get("TradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));
        assertEquals(insertCampaignOrderMap.get("SubType"),ConvertUtil.getString(actualList.get(0).get("SubType")));
        assertEquals(insertCampaignOrderMap.get("CouponCode"),ConvertUtil.getString(actualList.get(0).get("CouponCode")));
        assertEquals(insertCampaignOrderMap.get("CampaignCode"),ConvertUtil.getString(actualList.get(0).get("CampaignCode")));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("InformType")));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType1"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals("3",ConvertUtil.getString(actualList.get(0).get("DetailNo")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType2"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals("4",ConvertUtil.getString(actualList.get(0).get("DetailNo")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualParamMap.put("BIN_ProductVendorID", promotionProductVendorID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode3"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity3"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType3"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals("1",ConvertUtil.getString(actualList.get(0).get("DetailNo")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualParamMap.put("BIN_ProductVendorID", promotionProductVendorID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode3"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity4"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType4"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals("2",ConvertUtil.getString(actualList.get(0).get("DetailNo")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("OK",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAddProductAllocationOut1() throws Exception {
        String caseName = "testAddProductAllocationOut1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> dataDTO = dataList.get(0);
        map.put("organizationInfoID", dataDTO.get("BIN_OrganizationInfoID"));
        map.put("brandInfoID", dataDTO.get("BIN_BrandInfoID"));
        map.put("cherry_no", dataDTO.get("AllocationOutNo"));
        map.put("tradeNoIF", dataDTO.get("AllocationOutNoIF"));
        map.put("relevantNo", dataDTO.get("RelevanceNo"));
        map.put("organizationID", dataDTO.get("BIN_OrganizationIDIn"));
        map.put("employeeID", dataDTO.get("BIN_EmployeeID"));
        map.put("totalQuantity", dataDTO.get("TotalQuantity"));
        map.put("totalAmount", dataDTO.get("TotalAmount"));
        map.put("verifiedFlag", dataDTO.get("VerifiedFlag"));
        map.put("reason", dataDTO.get("Comments"));
        map.put("tradeDate", dataDTO.get("Date"));
        int billID = binBEMQMES02_Service.addProductAllocationOut(map);
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductAllocationOut");
        actualParamMap.put("BIN_ProductAllocationOutID", billID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(ConvertUtil.getString(dataDTO.get("BIN_OrganizationInfoID")),ConvertUtil.getString(actualList.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(ConvertUtil.getString(dataDTO.get("BIN_BrandInfoID")),ConvertUtil.getString(actualList.get(0).get("BIN_BrandInfoID")));
        assertEquals(ConvertUtil.getString(dataDTO.get("AllocationOutNo")),ConvertUtil.getString(actualList.get(0).get("AllocationOutNo")));
        assertEquals(ConvertUtil.getString(dataDTO.get("AllocationOutNoIF")),ConvertUtil.getString(actualList.get(0).get("AllocationOutNoIF")));
        assertEquals(ConvertUtil.getString(dataDTO.get("RelevanceNo")),ConvertUtil.getString(actualList.get(0).get("RelevanceNo")));
        assertEquals(ConvertUtil.getString(dataDTO.get("BIN_EmployeeID")),ConvertUtil.getString(actualList.get(0).get("BIN_EmployeeID")));
        assertEquals(ConvertUtil.getString(dataDTO.get("TotalQuantity")),ConvertUtil.getString(actualList.get(0).get("TotalQuantity")));
        assertEquals(ConvertUtil.getString(dataDTO.get("VerifiedFlag")),ConvertUtil.getString(actualList.get(0).get("VerifiedFlag")));
        assertEquals(ConvertUtil.getString(dataDTO.get("Comments")),ConvertUtil.getString(actualList.get(0).get("Comments")));
        assertEquals(ConvertUtil.getString(dataDTO.get("Date")),ConvertUtil.getString(actualList.get(0).get("Date")));
        assertEquals(ConvertUtil.getString(dataDTO.get("SynchFlag")),ConvertUtil.getString(actualList.get(0).get("SynchFlag")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzePXData1() throws Exception {
        //积分兑换
        String caseName = "testAnalyzePXData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_PromotionProduct
        Map<String,Object> insertPromotionProductMap = dataList.get(0);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID1 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> insertPromotionProductVendorMap = dataList.get(1);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID1);
        int promotionProductVendorID1 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Basis.BIN_PromotionProduct
        insertPromotionProductMap = dataList.get(2);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID2 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        insertPromotionProductVendorMap = dataList.get(3);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID2);
        int promotionProductVendorID2 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(4);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(5);
        insertProductVendorMap.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_Product
        insertProductMap = dataList.get(6);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        insertProductVendorMap = dataList.get(7);
        insertProductVendorMap.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(8);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicInventory
        insertLogicInventoryMap = dataList.get(9);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(10);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(11);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(12);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", depotInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        int inventoryInfo_IDENTITYID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap1 = dataList.get(13);
        activityTransHisInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap1.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap1);
        
        //Promotion.BIN_PromotionActGrp
        Map<String,Object> promotionActGrpInsertMap = dataList.get(14);
        promotionActGrpInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActGrpInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionActGrpID = testCOM_Service.insertTableData(promotionActGrpInsertMap);
        
        //Promotion.BIN_PromotionActivity
        Map<String,Object> promotionActivityInsertMap = dataList.get(15);
        promotionActivityInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActivityInsertMap.put("BIN_BrandInfoID", brandInfoID);
        promotionActivityInsertMap.put("BIN_PromotionActGrpID", promotionActGrpID);
        int promotionActivityID = testCOM_Service.insertTableData(promotionActivityInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(16);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(17);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap2 = dataList.get(18);
        activityTransHisInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap2.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap2);
        
        //Campaign.BIN_Campaign
        Map<String,Object> campaignInsertMap2 = dataList.get(19);
        campaignInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        campaignInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        int campaignID2 = testCOM_Service.insertTableData(campaignInsertMap2);
        
        //Campaign.BIN_CampaignRule
        Map<String,Object> campaignRuleInsertMap2 = dataList.get(20);
        campaignRuleInsertMap2.put("BIN_CampaignID", campaignID2);
        int campaignRuleID = testCOM_Service.insertTableData(campaignRuleInsertMap2);
        
        //执行MQ接收
        //积分兑换
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleRecord");
        actualParamMap.put("BillCode", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        String saleRecordCode = ConvertUtil.getString(actualList.get(0).get("SaleRecordCode"));
        String saleRecordID = ConvertUtil.getString(actualList.get(0).get("BIN_SaleRecordID"));
        assertEquals(1,actualList.size());
        assertEquals(userInfo.getBIN_OrganizationInfoID(),actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(userInfo.getBIN_BrandInfoID(),actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(expectList.get(0).get("SaleType"),actualList.get(0).get("SaleType"));
        assertEquals(expectList.get(0).get("TicketType"),actualList.get(0).get("TicketType"));
        assertEquals(expectList.get(0).get("Channel"),actualList.get(0).get("Channel"));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleRecordDetail");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(4,actualList.size());
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(promotionProductVendorID1,actualList.get(2).get("BIN_ProductVendorID"));
        assertEquals(promotionProductVendorID2,actualList.get(3).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("SaleQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("SaleQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("SaleQuantity3"),ConvertUtil.getString(actualList.get(2).get("Quantity")));
        assertEquals(expectList.get(0).get("SaleQuantity4"),ConvertUtil.getString(actualList.get(3).get("Quantity")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(0).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(1).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(2).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(3).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(0).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(1).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(2).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(3).get("MainCode"));
        assertEquals(null,actualList.get(0).get("CountActCode"));
        assertEquals(null,actualList.get(1).get("CountActCode"));
        assertEquals(null,actualList.get(2).get("CountActCode"));
        assertEquals(null,actualList.get(3).get("CountActCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualParamMap.put("RelevantNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        String promotionStockInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_PromotionStockInOutID"));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualParamMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualParamMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        String productInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        actualParamMap.put("BIN_ProductInOutID", productInOutID);
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        actualParamMap.put("BIN_ProductInOutID", productInOutID);
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStock");
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStock");
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PROStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(memberInfoID,actualList.get(1).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode1"),actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("CampaignCode2"),actualList.get(1).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(0).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(1).get("MainCode"));
        assertEquals("PX",actualList.get(0).get("TradeType"));
        assertEquals("PX",actualList.get(1).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals("OK",actualList.get(1).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(organizationID,actualList.get(1).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals("1",ConvertUtil.getString(actualList.get(1).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(1).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(1).get("BrandCode"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzePXData2() throws Exception {
        //积分兑换取消
        String caseName = "testAnalyzePXData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody1 = getMessageBody(mqList.get(0));
        String msgBody2 = getMessageBody(mqList.get(1));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_PromotionProduct
        Map<String,Object> insertPromotionProductMap = dataList.get(0);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID1 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> insertPromotionProductVendorMap = dataList.get(1);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID1);
        int promotionProductVendorID1 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Basis.BIN_PromotionProduct
        insertPromotionProductMap = dataList.get(2);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID2 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        insertPromotionProductVendorMap = dataList.get(3);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID2);
        int promotionProductVendorID2 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(4);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(5);
        insertProductVendorMap.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_Product
        insertProductMap = dataList.get(6);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        insertProductVendorMap = dataList.get(7);
        insertProductVendorMap.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(8);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicInventory
        insertLogicInventoryMap = dataList.get(9);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(10);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(11);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(12);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", depotInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        int inventoryInfo_IDENTITYID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap1 = dataList.get(13);
        activityTransHisInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap1.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap1);
        
        //Promotion.BIN_PromotionActGrp
        Map<String,Object> promotionActGrpInsertMap = dataList.get(14);
        promotionActGrpInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActGrpInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionActGrpID = testCOM_Service.insertTableData(promotionActGrpInsertMap);
        
        //Promotion.BIN_PromotionActivity
        Map<String,Object> promotionActivityInsertMap = dataList.get(15);
        promotionActivityInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        promotionActivityInsertMap.put("BIN_BrandInfoID", brandInfoID);
        promotionActivityInsertMap.put("BIN_PromotionActGrpID", promotionActGrpID);
        int promotionActivityID = testCOM_Service.insertTableData(promotionActivityInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(16);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(17);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap2 = dataList.get(18);
        activityTransHisInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap2.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap2);
        
        //Campaign.BIN_Campaign
        Map<String,Object> campaignInsertMap2 = dataList.get(19);
        campaignInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        campaignInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        int campaignID2 = testCOM_Service.insertTableData(campaignInsertMap2);
        
        //Campaign.BIN_CampaignRule
        Map<String,Object> campaignRuleInsertMap2 = dataList.get(20);
        campaignRuleInsertMap2.put("BIN_CampaignID", campaignID2);
        int campaignRuleID = testCOM_Service.insertTableData(campaignRuleInsertMap2);
        
        //执行MQ接收
        //积分兑换
        tran_analyzeMessage(msgBody1);      
        //积分兑换取消
        tran_analyzeMessage(msgBody2);
        
        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleRecord");
        actualParamMap.put("BillCode", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        String saleRecordCode = ConvertUtil.getString(actualList.get(0).get("SaleRecordCode"));
        String saleRecordID = ConvertUtil.getString(actualList.get(0).get("BIN_SaleRecordID"));
        assertEquals(1,actualList.size());
        assertEquals(userInfo.getBIN_OrganizationInfoID(),actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(userInfo.getBIN_BrandInfoID(),actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(expectList.get(0).get("SaleType"),actualList.get(0).get("SaleType"));
        assertEquals(expectList.get(0).get("TicketType"),actualList.get(0).get("TicketType"));
        assertEquals(expectList.get(0).get("Channel"),actualList.get(0).get("Channel"));
        String mainModifiedTimes = ConvertUtil.getString(actualList.get(0).get("ModifiedTimes"));
        assertEquals("1",mainModifiedTimes);
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleRecordDetail");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(4,actualList.size());
        assertEquals(mainModifiedTimes,ConvertUtil.getString(actualList.get(0).get("ModifiedTimes")));
        assertEquals(mainModifiedTimes,ConvertUtil.getString(actualList.get(1).get("ModifiedTimes")));
        assertEquals(mainModifiedTimes,ConvertUtil.getString(actualList.get(2).get("ModifiedTimes")));
        assertEquals(mainModifiedTimes,ConvertUtil.getString(actualList.get(3).get("ModifiedTimes")));
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(promotionProductVendorID1,actualList.get(2).get("BIN_ProductVendorID"));
        assertEquals(promotionProductVendorID2,actualList.get(3).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("SaleQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("SaleQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("SaleQuantity3"),ConvertUtil.getString(actualList.get(2).get("Quantity")));
        assertEquals(expectList.get(0).get("SaleQuantity4"),ConvertUtil.getString(actualList.get(3).get("Quantity")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(0).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(1).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(2).get("InventoryTypeCode"));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),actualList.get(3).get("InventoryTypeCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualParamMap.put("RelevantNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStock");
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStock");
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID1);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductStock");
        actualParamMap.put("BIN_ProductVendorID", productVendorID2);
        actualParamMap.put("BIN_InventoryInfoID", depotInfoID);
        actualParamMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("PROStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleRecordHistory");
        actualParamMap.put("BillCode", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        String saleRecordHistoryID = ConvertUtil.getString(actualList.get(0).get("BIN_SaleRecordHistoryID"));
        assertEquals(expectList.get(1).get("SaleType"),actualList.get(0).get("SaleType"));
        assertEquals(expectList.get(1).get("TicketType"),actualList.get(0).get("TicketType"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleDetailHistory");
        actualParamMap.put("BIN_SaleRecordHistoryID", saleRecordHistoryID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(1).get("UnitCode1"),actualList.get(0).get("UnitCode"));
        assertEquals(expectList.get(1).get("BarCode1"),actualList.get(0).get("BarCode"));
        assertEquals(expectList.get(1).get("InventoryTypeCode"),actualList.get(0).get("InventoryTypeCode"));
        assertEquals(expectList.get(1).get("SaleQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("SaleType1"),actualList.get(0).get("SaleType"));
        assertEquals(expectList.get(1).get("UnitCode2"),actualList.get(1).get("UnitCode"));
        assertEquals(expectList.get(1).get("BarCode2"),actualList.get(1).get("BarCode"));
        assertEquals(expectList.get(1).get("InventoryTypeCode"),actualList.get(1).get("InventoryTypeCode"));
        assertEquals(expectList.get(1).get("SaleQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(1).get("SaleType2"),actualList.get(1).get("SaleType"));
        assertEquals(expectList.get(1).get("UnitCode3"),actualList.get(2).get("UnitCode"));
        assertEquals(expectList.get(1).get("BarCode3"),actualList.get(2).get("BarCode"));
        assertEquals(expectList.get(1).get("InventoryTypeCode"),actualList.get(2).get("InventoryTypeCode"));
        assertEquals(expectList.get(1).get("SaleQuantity3"),ConvertUtil.getString(actualList.get(2).get("Quantity")));
        assertEquals(expectList.get(1).get("SaleType3"),actualList.get(2).get("SaleType"));
        assertEquals(expectList.get(1).get("UnitCode4"),actualList.get(3).get("UnitCode"));
        assertEquals(expectList.get(1).get("BarCode4"),actualList.get(3).get("BarCode"));
        assertEquals(expectList.get(1).get("InventoryTypeCode"),actualList.get(3).get("InventoryTypeCode"));
        assertEquals(expectList.get(1).get("SaleQuantity4"),ConvertUtil.getString(actualList.get(3).get("Quantity")));
        assertEquals(expectList.get(1).get("SaleType4"),actualList.get(3).get("SaleType"));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromoStockInOutHis");
        actualParamMap.put("RelevantNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        String promoStockInOutHisID = ConvertUtil.getString(actualList.get(0).get("BIN_PromoStockInOutHisID"));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(1).get("TradeType"),actualList.get(0).get("TradeType"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromoStockDetailHis");
        actualParamMap.put("BIN_PromoStockInOutHisID", promoStockInOutHisID);
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromoStockDetailHis");
        actualParamMap.put("BIN_PromoStockInOutHisID", promoStockInOutHisID);
        actualParamMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(expectList.get(1).get("PRMHISQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutHistory");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        String productInOutHistoryID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductInOutHistoryID"));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(1).get("PROTradeType"),actualList.get(0).get("TradeType"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetailHistory");
        actualParamMap.put("BIN_ProductInOutHistoryID", productInOutHistoryID);
        actualParamMap.put("BIN_ProductVendorID",productVendorID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetailHistory");
        actualParamMap.put("BIN_ProductInOutHistoryID", productInOutHistoryID);
        actualParamMap.put("BIN_ProductVendorID",productVendorID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(expectList.get(1).get("PROHISQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
            
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeAllocationOutData1() throws Exception {
        String caseName = "testAnalyzeAllocationOutData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        String counterCode1 = ConvertUtil.getString(otherList.get(0).get("CounterCode1"));
        String counterCode2 = ConvertUtil.getString(otherList.get(0).get("CounterCode2"));
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_BrandInfoID(brandInfoID);
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#CounterCode1#", counterCode1);
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", counterCode1);
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID1 = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#CounterCode2#", counterCode2);
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", counterCode2);
        organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID2 = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(3).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);

        //Tools.BIN_SystemConfig
        String sql5 = ConvertUtil.getString(sqlList.get(4).get("sql"));
        sql5 = sql5.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql5 = sql5.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql5);
        
        //Tools.BIN_SystemConfig
        String sql6 = ConvertUtil.getString(sqlList.get(5).get("sql"));
        sql6 = sql6.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql6 = sql6.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql6);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = dataList.get(2);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = dataList.get(3);
        insertProductVendorMap.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_Product
        insertProductMap = dataList.get(4);
        insertProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap);
        
        //Basis.BIN_ProductVendor
        insertProductVendorMap = dataList.get(5);
        insertProductVendorMap.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(6);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicInventory
        insertLogicInventoryMap = dataList.get(7);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap1 = dataList.get(8);
        insertCounterInfoMap1.put("BIN_OrganizationID", organizationID1);
        insertCounterInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID1 = testCOM_Service.insertTableData(insertCounterInfoMap1);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap1 = dataList.get(9);
        depotInfoInsertMap1.put("BIN_OrganizationID", organizationID1);
        depotInfoInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID1 = testCOM_Service.insertTableData(depotInfoInsertMap1);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap1 = dataList.get(10);
        inventoryInfoInsertMap1.put("BIN_InventoryInfoID", depotInfoID1);
        inventoryInfoInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap1.put("BIN_OrganizationID", organizationID1);
        int inventoryInfo_IDENTITYID1 = testCOM_Service.insertTableData(inventoryInfoInsertMap1);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap2 = dataList.get(11);
        insertCounterInfoMap2.put("BIN_OrganizationID", organizationID2);
        insertCounterInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID2 = testCOM_Service.insertTableData(insertCounterInfoMap2);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap2 = dataList.get(12);
        depotInfoInsertMap2.put("BIN_OrganizationID", organizationID2);
        depotInfoInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID2 = testCOM_Service.insertTableData(depotInfoInsertMap2);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap2 = dataList.get(13);
        inventoryInfoInsertMap2.put("BIN_InventoryInfoID", depotInfoID2);
        inventoryInfoInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap2.put("BIN_OrganizationID", organizationID2);
        int inventoryInfo_IDENTITYID2 = testCOM_Service.insertTableData(inventoryInfoInsertMap2);
        
        //调入申请
        //执行MQ接收
        tran_analyzeMessage(msgBody);
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF1 = ConvertUtil.getString(otherList.get(0).get("TradeNoIF1"));
        
        //验证调入申请单
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductAllocation");
        paramMap.put("AllocationNoIF", tradeNoIF1);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(null,actualList.get(0).get("RelevanceNo"));
        assertEquals(organizationID1,actualList.get(0).get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,actualList.get(0).get("BIN_OrganizationIDOut"));
        assertEquals(expectList.get(0).get("VerifiedFlag"),actualList.get(0).get("VerifiedFlag"));
        assertEquals(expectList.get(0).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
        assertEquals(expectList.get(0).get("Date"),actualList.get(0).get("Date"));
        int productAllocationID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductAllocationID"));
        
        //执行调出
        msgBody = getMessageBody(mqList.get(1));
        tran_analyzeMessage(msgBody);
        
        String tradeNoIF2 = ConvertUtil.getString(otherList.get(0).get("TradeNoIF2"));

        //验证调出单
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductAllocationOut");
        paramMap.put("AllocationOutNoIF", tradeNoIF2);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(tradeNoIF1,actualList.get(0).get("RelevanceNo"));
        assertEquals(organizationID1,actualList.get(0).get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,actualList.get(0).get("BIN_OrganizationIDOut"));
        assertEquals(expectList.get(2).get("VerifiedFlag"),actualList.get(0).get("VerifiedFlag"));
        assertEquals(expectList.get(2).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
        assertEquals(expectList.get(2).get("Date"),actualList.get(0).get("Date"));
        assertEquals("1",actualList.get(0).get("SynchFlag"));
        int productAllocationOutID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductAllocationOutID"));
        
        //验证调入单
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductAllocationIn");
        paramMap.put("RelevanceNo", tradeNoIF2);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(organizationID1,actualList.get(0).get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,actualList.get(0).get("BIN_OrganizationIDOut"));
        assertEquals(expectList.get(3).get("VerifiedFlag"),actualList.get(0).get("VerifiedFlag"));
        assertEquals(expectList.get(3).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
        assertEquals(expectList.get(3).get("Date"),actualList.get(0).get("Date"));
        int productAllocationInID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductAllocationInID"));
        
        //验证调入申请单
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductAllocation");
        paramMap.put("AllocationNoIF", tradeNoIF1);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(4).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
    }
}
