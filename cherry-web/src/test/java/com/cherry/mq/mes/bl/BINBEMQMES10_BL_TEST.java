package com.cherry.mq.mes.bl;

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

public class BINBEMQMES10_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binBEMQMES99_BL")
    private BINBEMQMES99_BL binBEMQMES99_BL;
    
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
    
    public String getMessageBody(List<Map<String,Object>> mqList){
        StringBuffer msg = new StringBuffer();
        Map<String,Object> messageBody = mqList.get(0);
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
    public void testAnalyzeStockHB1() throws Exception {
        //合并入库
        String caseName = "testAnalyzeStockHB1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
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
        Map<String,Object> counterInfoInsertMap = dataList.get(2);
        counterInfoInsertMap.put("BIN_OrganizationID", organizationID);
        counterInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        counterInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(3);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int inventoryInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(4);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", inventoryInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(5);
        logicInventoryInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        logicInventoryInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryInfoID = testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //Basis.BIN_Product
        Map<String,Object> productInsertMap1 = dataList.get(6);
        productInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        productInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(productInsertMap1);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap1 = dataList.get(7);
        productVendorInsertMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(productVendorInsertMap1);
        
        //Basis.BIN_Product
        Map<String,Object> productInsertMap2 = dataList.get(8);
        productInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        productInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(productInsertMap2);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap2 = dataList.get(9);
        productVendorInsertMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(productVendorInsertMap2);
        
        //Inventory.BIN_ProductStock
        Map<String,Object> productStockInsertMap = dataList.get(10);
        productStockInsertMap.put("BIN_ProductVendorID", productVendorID1);
        productStockInsertMap.put("BIN_InventoryInfoID", inventoryInfoID);
        productStockInsertMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        testCOM_Service.insertTableData(productStockInsertMap);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInDepot");
        paramMap.put("BillNoIF", ConvertUtil.getString(otherList.get(0).get("TradeNoIF")));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        int productInDepotID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductInDepotID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(null,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(expectList.get(0).get("TotalQuantity"),ConvertUtil.getString(actualList.get(0).get("TotalQuantity")));
        assertEquals(expectList.get(0).get("TotalAmount"),ConvertUtil.getString(actualList.get(0).get("TotalAmount")));
        assertEquals(null,actualList.get(0).get("WorkFlowID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInDepotDetail");
        paramMap.put("BIN_ProductInDepotID", productInDepotID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        assertEquals(expectList.get(0).get("Quantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("Quantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(inventoryInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(inventoryInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOut");
        paramMap.put("RelevanceNo", ConvertUtil.getString(otherList.get(0).get("TradeNoIF")));
        actualList = testCOM_Service.getTableData(paramMap);
        int productInOutID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(null,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(expectList.get(0).get("TotalQuantity"),ConvertUtil.getString(actualList.get(0).get("TotalQuantity")));
        assertEquals(expectList.get(0).get("TotalAmount"),ConvertUtil.getString(actualList.get(0).get("TotalAmount")));
        assertEquals(expectList.get(0).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(0).get("TradeType"),actualList.get(0).get("TradeType"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        paramMap.put("BIN_ProductInOutID", productInOutID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        assertEquals(expectList.get(0).get("Quantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("Quantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(inventoryInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(inventoryInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", productVendorID1);
        paramMap.put("BIN_InventoryInfoID", inventoryInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("StockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", productVendorID2);
        paramMap.put("BIN_InventoryInfoID", inventoryInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("StockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeStockHB2() throws Exception {
        //合并出库
        String caseName = "testAnalyzeStockHB2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
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
        Map<String,Object> insertMap = dataList.get(2);
        insertMap.put("BIN_OrganizationID", organizationID);
        insertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(3);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int inventoryInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(4);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", inventoryInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(5);
        logicInventoryInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        logicInventoryInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryInfoID = testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        //Basis.BIN_Product
        Map<String,Object> productInsertMap1 = dataList.get(6);
        productInsertMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        productInsertMap1.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(productInsertMap1);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap1 = dataList.get(7);
        productVendorInsertMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(productVendorInsertMap1);
        
        //Basis.BIN_Product
        Map<String,Object> productInsertMap2 = dataList.get(8);
        productInsertMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        productInsertMap2.put("BIN_BrandInfoID", brandInfoID);
        int productID2 = testCOM_Service.insertTableData(productInsertMap2);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap2 = dataList.get(9);
        productVendorInsertMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(productVendorInsertMap2);
        
        //Inventory.BIN_ProductStock
        Map<String,Object> productStockInsertMap = dataList.get(10);
        productStockInsertMap.put("BIN_ProductVendorID", productVendorID1);
        productStockInsertMap.put("BIN_InventoryInfoID", inventoryInfoID);
        productStockInsertMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        testCOM_Service.insertTableData(productStockInsertMap);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);
        
        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductOutDepot");
        paramMap.put("BillNoIF", ConvertUtil.getString(otherList.get(0).get("TradeNoIF")));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        int productOutDepotID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductOutDepotID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(null,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(expectList.get(0).get("TotalQuantity"),ConvertUtil.getString(actualList.get(0).get("TotalQuantity")));
        assertEquals(expectList.get(0).get("TotalAmount"),ConvertUtil.getString(actualList.get(0).get("TotalAmount")));
        assertEquals(null,actualList.get(0).get("WorkFlowID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductOutDepotDetail");
        paramMap.put("BIN_ProductOutDepotID", productOutDepotID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        assertEquals(expectList.get(0).get("Quantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("Quantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(inventoryInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(inventoryInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOut");
        paramMap.put("RelevanceNo", ConvertUtil.getString(otherList.get(0).get("TradeNoIF")));
        actualList = testCOM_Service.getTableData(paramMap);
        int productInOutID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductInOutID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(null,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(expectList.get(0).get("TotalQuantity"),ConvertUtil.getString(actualList.get(0).get("TotalQuantity")));
        assertEquals(expectList.get(0).get("TotalAmount"),ConvertUtil.getString(actualList.get(0).get("TotalAmount")));
        assertEquals(expectList.get(0).get("StockType"),actualList.get(0).get("StockType"));
        assertEquals(expectList.get(0).get("TradeType"),actualList.get(0).get("TradeType"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
        paramMap.put("BIN_ProductInOutID", productInOutID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(productVendorID1,actualList.get(0).get("BIN_ProductVendorID"));
        assertEquals(productVendorID2,actualList.get(1).get("BIN_ProductVendorID"));
        assertEquals(1,actualList.get(0).get("DetailNo"));
        assertEquals(2,actualList.get(1).get("DetailNo"));
        assertEquals(expectList.get(0).get("Quantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("Quantity2"),ConvertUtil.getString(actualList.get(1).get("Quantity")));
        assertEquals(inventoryInfoID,actualList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(inventoryInfoID,actualList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(0).get("BIN_LogicInventoryInfoID"));
        assertEquals(logicInventoryInfoID,actualList.get(1).get("BIN_LogicInventoryInfoID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", productVendorID1);
        paramMap.put("BIN_InventoryInfoID", inventoryInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("StockQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", productVendorID2);
        paramMap.put("BIN_InventoryInfoID", inventoryInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("StockQuantity2"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
}