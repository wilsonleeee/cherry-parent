package com.cherry.mq.mes.bl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CodeTable;
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
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINBEMQMES99_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binBEMQMES99_BL")
    private BINBEMQMES99_BL binBEMQMES99_BL;
    
    @Resource(name="CodeTable")
    private CodeTable code;
   
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
    public void testAnalyzeSaleReturnStockMessage1() throws Exception {
        //销售
        String caseName = "testAnalyzeSaleReturnStockMessage1";
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        
        //执行MQ接收
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
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(2).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(3).get("MainCode"));
        assertEquals(expectList.get(0).get("CountActCode1"),actualList.get(2).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(3).get("CountActCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        assertEquals(expectList.get(0).get("PROStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(memberInfoID,actualList.get(1).get("BIN_MemberInfoID"));
        assertEquals(null,actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(1).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(0).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(1).get("MainCode"));
        assertEquals("NS",actualList.get(0).get("TradeType"));
        assertEquals("NS",actualList.get(1).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals("OK",actualList.get(1).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(organizationID,actualList.get(1).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals("0",ConvertUtil.getString(actualList.get(1).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(1).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(1).get("BrandCode"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage2() throws Exception {
        //退货
        String caseName = "testAnalyzeSaleReturnStockMessage2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));
        Map<String,Object> messageBody = mqList.get(0);

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
                
        //执行MQ接收
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
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        assertEquals(expectList.get(0).get("PROStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage3() throws Exception {
        //关联退货
        String caseName = "testAnalyzeSaleReturnStockMessage3";
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        
        //执行MQ接收
        //销售
        tran_analyzeMessage(msgBody1);
        //关联退货
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
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        assertEquals(expectList.get(0).get("PROStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SaleRecord");
        actualParamMap.put("BillCode", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("NSSaleComment"),ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage4() throws Exception {
        //修改销售 未进行过月度库存计算
        String caseName = "testAnalyzeSaleReturnStockMessage4";
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        Map<String,Object> insertMemberInfoMap1 = dataList.get(16);
        insertMemberInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID1 = testCOM_Service.insertTableData(insertMemberInfoMap1);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap1 = dataList.get(17);
        insertMemCardInfoMap1.put("BIN_MemberInfoID", memberInfoID1);
        testCOM_Service.insertTableData(insertMemCardInfoMap1);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap2 = dataList.get(18);
        insertMemberInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID2 = testCOM_Service.insertTableData(insertMemberInfoMap2);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap2 = dataList.get(19);
        insertMemCardInfoMap2.put("BIN_MemberInfoID", memberInfoID2);
        testCOM_Service.insertTableData(insertMemCardInfoMap2);
        
        //执行MQ接收
        //销售
        tran_analyzeMessage(msgBody1);
        //修改销售
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
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(promotionProductVendorID1,actualList.get(0).get("BIN_PromotionProductVendorID"));
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        assertEquals(expectList.get(1).get("PRMHISQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("PRMHISQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(1).get("StockType"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        assertEquals(expectList.get(1).get("PROHISQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("PROHISQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(1).get("StockType"));
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(memberInfoID2,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(0).get("MainCode"));
        assertEquals("NS",actualList.get(0).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage5() throws Exception {
        //修改销售 进行过月度库存计算
        String caseName = "testAnalyzeSaleReturnStockMessage5";
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
                
        //执行MQ接收
        //销售
        tran_analyzeMessage(msgBody1);
        
        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        sql = "update Inventory.BIN_ProductInOut set CloseFlag = '1' where RelevanceNo = '"+tradeNoIF+"'";
        testCOM_Service.update(sql);
        sql = "update Inventory.BIN_PromotionStockInOut set CloseFlag = '1' where RelevantNo = '"+tradeNoIF+"'";
        testCOM_Service.update(sql);
        
        //修改销售
        tran_analyzeMessage(msgBody2);
        
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
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualParamMap.put("RelevantNo", tradeNoIF);
        actualParamMap.put("ChangeFlag", "1");
        actualList = testCOM_Service.getTableData(actualParamMap);
        String promotionStockInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_PromotionStockInOutID"));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualParamMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(promotionProductVendorID1,actualList.get(0).get("BIN_PromotionProductVendorID"));
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualParamMap.put("ChangeCount", "1");
        actualList = testCOM_Service.getTableData(actualParamMap);
        String productInOutID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROTradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        actualParamMap.put("BIN_ProductInOutID", productInOutID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage6() throws Exception {
        //销售
        String caseName = "testAnalyzeSaleReturnStockMessage6";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> codesList = new ArrayList<Map<String,Object>>();
        String[] payCodeArr = {"CC","BC","CR","PT","OT","MC","LOKC","UC","AC","CP","CA"};
        for(int i=0;i<payCodeArr.length;i++){
            Map<String,Object> code1175 = new HashMap<String,Object>();
            code1175.put("codeType", "1175");
            code1175.put("orgCode", "-9999");
            code1175.put("brandCode", "-9999");
            code1175.put("codeKey", payCodeArr[i]);
            code1175.put("grade", i+1);
            codesList.add(code1175);
        }
        code.setCodesMap(codesList);
        
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
        //执行MQ接收
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
        assertEquals(expectList.get(0).get("CostPoint"),ConvertUtil.getString(actualList.get(0).get("CostPoint")));
        
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
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(2).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(3).get("MainCode"));
        assertEquals(expectList.get(0).get("CountActCode1"),actualList.get(2).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(3).get("CountActCode"));
        assertEquals(expectList.get(0).get("Discount1"),ConvertUtil.getString(actualList.get(0).get("Discount")));
        assertEquals(expectList.get(0).get("Discount2"),ConvertUtil.getString(actualList.get(1).get("Discount")));
        assertEquals(expectList.get(0).get("Discount3"),ConvertUtil.getString(actualList.get(2).get("Discount")));
        assertEquals(expectList.get(0).get("Discount4"),ConvertUtil.getString(actualList.get(3).get("Discount")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(expectList.get(0).get("PRMIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PRMIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PromotionStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(expectList.get(0).get("PROIOQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("PROIOQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(0).get("StockType")));
        assertEquals(expectList.get(0).get("PROStockType"),ConvertUtil.getString(actualList.get(1).get("StockType")));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        assertEquals(expectList.get(0).get("PROStockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage7() throws Exception {
        //销售-Coupon
        String caseName = "testAnalyzeSaleReturnStockMessage7";
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap = dataList.get(18);
        insertCampaignOrderMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID = testCOM_Service.insertTableData(insertCampaignOrderMap);
        
        //执行MQ接收
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
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(0).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(1).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(2).get("MainCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(3).get("MainCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(0).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(1).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(2).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(3).get("CountActCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("SerialNumber1"),ConvertUtil.getString(actualList.get(0).get("SerialNumber")));
        assertEquals(expectList.get(0).get("SerialNumber2"),ConvertUtil.getString(actualList.get(1).get("SerialNumber")));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
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
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode"),actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(0).get("MainCode"));
        assertEquals("NS",actualList.get(0).get("TradeType"));
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
        
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(2).get("MainCode")));
        assertEquals(promotionProductVendorID1,CherryUtil.obj2int(actualList.get(2).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(2).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(2).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity3"),ConvertUtil.getString(actualList.get(2).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType3"),ConvertUtil.getString(actualList.get(2).get("GiftType")));
        
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(3).get("MainCode")));
        assertEquals(promotionProductVendorID2,CherryUtil.obj2int(actualList.get(3).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(3).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(3).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity4"),ConvertUtil.getString(actualList.get(3).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType4"),ConvertUtil.getString(actualList.get(3).get("GiftType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("OK",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage8() throws Exception {
        //关联退货-coupon
        String caseName = "testAnalyzeSaleReturnStockMessage8";
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap = dataList.get(20);
        insertCampaignOrderMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID = testCOM_Service.insertTableData(insertCampaignOrderMap);
        
        //执行MQ接收
        //销售
        tran_analyzeMessage(msgBody1);
        //关联退货
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
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
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
        actualParamMap.put("tableName", "Sale.BIN_SaleRecord");
        actualParamMap.put("BillCode", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("NSSaleComment"),ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        int giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("ValidFlag")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("AR",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage9() throws Exception {
        //销售-会员活动Coupon
        String caseName = "testAnalyzeSaleReturnStockMessage9";
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
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap2 = dataList.get(14);
        activityTransHisInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap2.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap2);
        
        //Campaign.BIN_Campaign
        Map<String,Object> campaignInsertMap1 = dataList.get(15);
        campaignInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        campaignInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        int campaignID1 = testCOM_Service.insertTableData(campaignInsertMap1);
        
        //Campaign.BIN_CampaignRule
        Map<String,Object> campaignRuleInsertMap1 = dataList.get(16);
        campaignRuleInsertMap1.put("BIN_CampaignID", campaignID1);
        int campaignRuleID1 = testCOM_Service.insertTableData(campaignRuleInsertMap1);
        
        //Campaign.BIN_Campaign
        Map<String,Object> campaignInsertMap2 = dataList.get(17);
        campaignInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        campaignInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        int campaignID2 = testCOM_Service.insertTableData(campaignInsertMap2);
        
        //Campaign.BIN_CampaignRule
        Map<String,Object> campaignRuleInsertMap2 = dataList.get(18);
        campaignRuleInsertMap2.put("BIN_CampaignID", campaignID2);
        int campaignRuleID = testCOM_Service.insertTableData(campaignRuleInsertMap2);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(19);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(20);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap1 = dataList.get(21);
        insertCampaignOrderMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap1.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID1 = testCOM_Service.insertTableData(insertCampaignOrderMap1);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap2 = dataList.get(22);
        insertCampaignOrderMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap2.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID2 = testCOM_Service.insertTableData(insertCampaignOrderMap2);
        
        //执行MQ接收
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
        assertEquals(expectList.get(0).get("CountActCode1"),actualList.get(0).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode1"),actualList.get(1).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(2).get("CountActCode"));
        assertEquals(expectList.get(0).get("CountActCode2"),actualList.get(3).get("CountActCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
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
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode1"),actualList.get(0).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode1"),actualList.get(0).get("MainCode"));
        assertEquals("NS",actualList.get(0).get("TradeType"));
        assertEquals("OK",actualList.get(0).get("State"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("1",ConvertUtil.getString(actualList.get(0).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(0).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(0).get("BrandCode"));
        assertEquals(memberInfoID,actualList.get(1).get("BIN_MemberInfoID"));
        assertEquals(expectList.get(0).get("CampaignCode2"),actualList.get(1).get("CampaignCode"));
        assertEquals(expectList.get(0).get("MainCode2"),actualList.get(1).get("MainCode"));
        assertEquals("NS",actualList.get(1).get("TradeType"));
        assertEquals("OK",actualList.get(1).get("State"));
        assertEquals(organizationID,actualList.get(1).get("BIN_OrganizationID"));
        assertEquals("1",ConvertUtil.getString(actualList.get(1).get("CampaignType")));
        assertEquals(userInfo.getOrganizationInfoCode(),actualList.get(1).get("OrgCode"));
        assertEquals(userInfo.getBrandCode(),actualList.get(1).get("BrandCode"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", tradeNoIF);
        actualParamMap.put("CampaignCode", insertCampaignOrderMap1.get("CampaignCode"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        int giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals(organizationInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(brandInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_BrandInfoID")));
        assertEquals(organizationID,CherryUtil.obj2int(actualList.get(0).get("BIN_OrganizationID")));
        assertEquals(insertCampaignOrderMap1.get("TradeNoIF"),ConvertUtil.getString(actualList.get(0).get("RelevanceNo")));
        assertEquals(insertCampaignOrderMap1.get("TradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));
        assertEquals(insertCampaignOrderMap1.get("SubType"),ConvertUtil.getString(actualList.get(0).get("SubType")));
        assertEquals(insertCampaignOrderMap1.get("CouponCode"),ConvertUtil.getString(actualList.get(0).get("CouponCode")));
        assertEquals(insertCampaignOrderMap1.get("CampaignCode"),ConvertUtil.getString(actualList.get(0).get("CampaignCode")));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("InformType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode1"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(productVendorID1,CherryUtil.obj2int(actualList.get(0).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType1"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals(expectList.get(0).get("MainCode1"),ConvertUtil.getString(actualList.get(1).get("MainCode")));
        assertEquals(productVendorID2,CherryUtil.obj2int(actualList.get(1).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(1).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(1).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType2"),ConvertUtil.getString(actualList.get(1).get("GiftType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", tradeNoIF);
        actualParamMap.put("CampaignCode", insertCampaignOrderMap2.get("CampaignCode"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals(organizationInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(brandInfoID,ConvertUtil.getString(actualList.get(0).get("BIN_BrandInfoID")));
        assertEquals(organizationID,CherryUtil.obj2int(actualList.get(0).get("BIN_OrganizationID")));
        assertEquals(insertCampaignOrderMap2.get("TradeNoIF"),ConvertUtil.getString(actualList.get(0).get("RelevanceNo")));
        assertEquals(insertCampaignOrderMap2.get("TradeType"),ConvertUtil.getString(actualList.get(0).get("TradeType")));
        assertEquals(insertCampaignOrderMap2.get("SubType"),ConvertUtil.getString(actualList.get(0).get("SubType")));
        assertEquals(insertCampaignOrderMap2.get("CouponCode"),ConvertUtil.getString(actualList.get(0).get("CouponCode")));
        assertEquals(insertCampaignOrderMap2.get("CampaignCode"),ConvertUtil.getString(actualList.get(0).get("CampaignCode")));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("InformType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDrawDetail");
        actualParamMap.put("BIN_GiftDrawID", giftDrawID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(0).get("MainCode")));
        assertEquals(promotionProductVendorID1,CherryUtil.obj2int(actualList.get(0).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity3"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType3"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(1).get("MainCode")));
        assertEquals(promotionProductVendorID2,CherryUtil.obj2int(actualList.get(1).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(1).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(1).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity4"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType4"),ConvertUtil.getString(actualList.get(1).get("GiftType")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("OK",actualList.get(0).get("State"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("OK",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage10() throws Exception {
        //关联退货-会员活动coupon
        String caseName = "testAnalyzeSaleReturnStockMessage10";
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
        
        //Promotion.BIN_ActivityTransHis
        Map<String,Object> activityTransHisInsertMap2 = dataList.get(14);
        activityTransHisInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap2.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap2);
        
        //Campaign.BIN_Campaign
        Map<String,Object> campaignInsertMap1 = dataList.get(15);
        campaignInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        campaignInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        int campaignID1 = testCOM_Service.insertTableData(campaignInsertMap1);
        
        //Campaign.BIN_CampaignRule
        Map<String,Object> campaignRuleInsertMap1 = dataList.get(16);
        campaignRuleInsertMap1.put("BIN_CampaignID", campaignID1);
        int campaignRuleID1 = testCOM_Service.insertTableData(campaignRuleInsertMap1);
        
        //Campaign.BIN_Campaign
        Map<String,Object> campaignInsertMap2 = dataList.get(17);
        campaignInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        campaignInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        int campaignID = testCOM_Service.insertTableData(campaignInsertMap2);
        
        //Campaign.BIN_CampaignRule
        Map<String,Object> campaignRuleInsertMap2 = dataList.get(18);
        campaignRuleInsertMap2.put("BIN_CampaignID", campaignID);
        int campaignRuleID = testCOM_Service.insertTableData(campaignRuleInsertMap2);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(19);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(20);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap1 = dataList.get(23);
        insertCampaignOrderMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap1.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID1 = testCOM_Service.insertTableData(insertCampaignOrderMap1);
        
        //Campaign.BIN_CampaignOrder
        Map<String,Object> insertCampaignOrderMap2 = dataList.get(24);
        insertCampaignOrderMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCampaignOrderMap2.put("BIN_BrandInfoID", brandInfoID);
        int campaignOrderID2 = testCOM_Service.insertTableData(insertCampaignOrderMap2);
        
        //执行MQ接收
        //销售
        tran_analyzeMessage(msgBody1);
        //关联退货
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
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Sale.BIN_SalePayList");
        actualParamMap.put("BIN_SaleRecordID", saleRecordID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(2,actualList.size());
        assertEquals(expectList.get(0).get("PayTypeCode1"),actualList.get(0).get("PayTypeCode"));
        assertEquals(expectList.get(0).get("PayTypeCode2"),actualList.get(1).get("PayTypeCode"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        
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
        actualParamMap.put("tableName", "Inventory.BIN_ProductInOut");
        actualParamMap.put("RelevanceNo", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
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
        actualParamMap.put("tableName", "Sale.BIN_SaleRecord");
        actualParamMap.put("BillCode", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("NSSaleComment"),ConvertUtil.getString(actualList.get(0).get("Comments")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_GiftDraw");
        actualParamMap.put("BillNoIF", otherList.get(0).get("NSTradeNoIF"));
        actualList = testCOM_Service.getTableData(actualParamMap);
        int giftDrawID = CherryUtil.obj2int(actualList.get(0).get("BIN_GiftDrawID"));
        assertEquals("0",ConvertUtil.getString(actualList.get(0).get("ValidFlag")));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("AR",actualList.get(0).get("State"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignOrder");
        actualParamMap.put("BIN_CampaignOrderID", campaignOrderID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("AR",actualList.get(0).get("State"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage11() throws Exception {
        //销售假登录
        String caseName = "testAnalyzeSaleReturnStockMessage11";
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
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("EmployeeCode", "TCBA001");
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String memCode = ConvertUtil.getString(otherList.get(0).get("MemCode"));
        String bacode = ConvertUtil.getString(otherList.get(0).get("BAcode"));
        String counterCode = ConvertUtil.getString(otherList.get(0).get("CounerCode"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Members.BIN_MemCardInfo");
        actualParamMap.put("MemCode", memCode);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        String memberInfoID = ConvertUtil.getString(actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(1,actualList.size());
        assertEquals(bacode,ConvertUtil.getString(actualList.get(0).get("BaCode")).trim());
        assertEquals(counterCode,ConvertUtil.getString(actualList.get(0).get("CounterCode")).trim());

        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Members.BIN_MemberInfo");
        actualParamMap.put("BIN_MemberInfoID", memberInfoID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(employeeID,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(bacode,actualList.get(0).get("BaCodeBelong"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(counterCode,actualList.get(0).get("CounterCodeBelong"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeSaleReturnStockMessage12() throws Exception {
        //修改销售 整单全退 未进行过月度库存计算
        String caseName = "testAnalyzeSaleReturnStockMessage12";
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
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("EmployeeCode", "TCBA001");
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
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
        Map<String,Object> activityTransHisInsertMap = dataList.get(13);
        activityTransHisInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        activityTransHisInsertMap.put("BIN_BrandInfoID", brandInfoID);
        activityTransHisInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(activityTransHisInsertMap);
        
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
        Map<String,Object> insertMemberInfoMap1 = dataList.get(16);
        insertMemberInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID1 = testCOM_Service.insertTableData(insertMemberInfoMap1);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap1 = dataList.get(17);
        insertMemCardInfoMap1.put("BIN_MemberInfoID", memberInfoID1);
        testCOM_Service.insertTableData(insertMemCardInfoMap1);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap2 = dataList.get(18);
        insertMemberInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID2 = testCOM_Service.insertTableData(insertMemberInfoMap2);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap2 = dataList.get(19);
        insertMemCardInfoMap2.put("BIN_MemberInfoID", memberInfoID2);
        testCOM_Service.insertTableData(insertMemCardInfoMap2);
        
        //执行MQ接收
        //销售
        tran_analyzeMessage(msgBody1);
        //修改销售
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
        assertEquals(employeeID,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals("TCBA001",actualList.get(0).get("EmployeeCode"));
        
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
        assertEquals(expectList.get(0).get("PRMStockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        assertEquals(expectList.get(0).get("PROStockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(promotionProductVendorID1,actualList.get(0).get("BIN_PromotionProductVendorID"));
        assertEquals(promotionProductVendorID2,actualList.get(1).get("BIN_PromotionProductVendorID"));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        assertEquals(expectList.get(1).get("PRMHISQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("PRMHISQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(1).get("StockType"));
        
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
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(depotInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryID1,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        assertEquals(expectList.get(1).get("PROHISQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(1).get("PROHISQuantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(1).get("StockType"),actualList.get(1).get("StockType"));
    
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Campaign.BIN_CampaignHistory");
        actualParamMap.put("TradeNoIF", tradeNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(0,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSplitMessageMap1() throws Exception{
        String caseName = "testSplitMessageMap1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //测试私有方法
        Class<?> clazz = Class.forName("com.cherry.mq.mes.bl.BINBEMQMES99_BL");
        BINBEMQMES99_BL bl =(BINBEMQMES99_BL)clazz.newInstance();
        Method method = clazz.getDeclaredMethod("splitMessageMap",Map.class);
        method.setAccessible(true);
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> detailDataDTOList = new ArrayList<Map<String,Object>>();
        detailDataDTOList.add(dataList.get(0));
        detailDataDTOList.add(dataList.get(1));
        detailDataDTOList.add(dataList.get(2));
        detailDataDTOList.add(dataList.get(3));
        detailDataDTOList.add(dataList.get(4));
        map.put("detailDataDTOList", detailDataDTOList);
        List<Map<String,Object>> resultList = (List<Map<String,Object>>) method.invoke(bl, map);
        assertEquals(1,resultList.size());
        List<Map<String,Object>> actualDetailDataDTOList = (List<Map<String, Object>>) resultList.get(0).get("detailDataDTOList");
        List<Map<String,Object>> exceptedList = (List<Map<String, Object>>) dataMap.get("exceptedList");
        assertEquals(exceptedList.size(),actualDetailDataDTOList.size());
        for(int i=0;i<exceptedList.size();i++){
            assertEquals(ConvertUtil.getString(exceptedList.get(i).get("unitcode")),ConvertUtil.getString(actualDetailDataDTOList.get(i).get("unitcode")));
        }
        method.setAccessible(false);
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeStockData1() throws Exception{
        String caseName = "testAnalyzeStockData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));
        
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
        
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = dataList.get(2);
        insertProductMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap1.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = dataList.get(3);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = dataList.get(4);
        insertProductMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap2.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = dataList.get(5);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(6);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertLogicInventoryMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryInfoID = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(7);
        insertCounterInfoMap.put("BIN_OrganizationInfoID",organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID",brandInfoID);
        insertCounterInfoMap.put("BIN_OrganizationID",organizationID);
        testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoInfoMap = dataList.get(8);
        insertDepotInfoInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoInfoMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = dataList.get(9);
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        //Inventory.BIN_ProductDeliver
        Map<String,Object> insertProductDeliverMap = dataList.get(10);
        insertProductDeliverMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductDeliverMap.put("BIN_BrandInfoID", brandInfoID);
        testCOM_Service.insertTableData(insertProductDeliverMap);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);
        
        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductReceive");
        actualParamMap.put("ReceiveNoIF", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        
        assertEquals(ConvertUtil.getString(insertProductDeliverMap.get("WorkFlowID")),ConvertUtil.getString(actualList.get(0).get("WorkFlowID")));
    }
}
