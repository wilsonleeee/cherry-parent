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

public class BINBEMQMES01_BL_TEST extends CherryJunitBase{
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
    public void testAnalyzeShiftData1() throws Exception {
        String caseName = "testAnalyzeShiftData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
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
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(6);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
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
        int inventoryInfoID = testCOM_Service.insertTableData(inventoryInfoInsertMap);
        
        //执行MQ接收
        tran_analyzeMessage(msg.toString());

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_PromotionShift");

        Map<String,Object> tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Inventory.BIN_PromotionShift");
        tableDataMap.put("BillNoIF", otherList.get(0).get("TradeNoIF"));
        List<Map<String,Object>> shiftList = testCOM_Service.getTableData(tableDataMap);
        
        assertEquals(1,shiftList.size());
        assertEquals(organizationID,shiftList.get(0).get("BIN_OrganizationID"));
        assertEquals("22",ConvertUtil.getString(shiftList.get(0).get("TotalQuantity")));
        assertEquals("2212.00",ConvertUtil.getString(shiftList.get(0).get("TotalAmount")));
        assertEquals("2012-09-28",ConvertUtil.getString(shiftList.get(0).get("OperateDate")));
        
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Inventory.BIN_PromotionShiftDetail");
        tableDataMap.put("BIN_PromotionShiftID", shiftList.get(0).get("BIN_PromotionShiftID"));
        List<Map<String,Object>> shiftDetailList = testCOM_Service.getTableData(tableDataMap);
        
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        assertEquals(2,shiftDetailList.size());
        assertEquals("1",ConvertUtil.getString(shiftDetailList.get(0).get("DetailNo")));
        assertEquals("2",ConvertUtil.getString(shiftDetailList.get(1).get("DetailNo")));
        
        assertEquals(ConvertUtil.getString(expectList.get(0).get("Quantity")),ConvertUtil.getString(shiftDetailList.get(0).get("Quantity")));
        assertEquals(ConvertUtil.getString(expectList.get(1).get("Quantity")),ConvertUtil.getString(shiftDetailList.get(1).get("Quantity")));
        
        assertEquals(ConvertUtil.getString(promotionProductVendorID1),ConvertUtil.getString(shiftDetailList.get(0).get("BIN_PromotionProductVendorID")));
        assertEquals(ConvertUtil.getString(promotionProductVendorID2),ConvertUtil.getString(shiftDetailList.get(1).get("BIN_PromotionProductVendorID")));
        
        assertEquals(ConvertUtil.getString(depotInfoID),ConvertUtil.getString(shiftDetailList.get(0).get("FromDepotInfoID")));
        assertEquals(ConvertUtil.getString(depotInfoID),ConvertUtil.getString(shiftDetailList.get(1).get("FromDepotInfoID")));
        
        assertEquals(ConvertUtil.getString(depotInfoID),ConvertUtil.getString(shiftDetailList.get(0).get("ToDepotInfoID")));
        assertEquals(ConvertUtil.getString(depotInfoID),ConvertUtil.getString(shiftDetailList.get(1).get("ToDepotInfoID")));
        
        assertEquals(ConvertUtil.getString(logicInventoryID1),ConvertUtil.getString(shiftDetailList.get(0).get("FromLogicInventoryInfoID")));
        assertEquals(ConvertUtil.getString(logicInventoryID1),ConvertUtil.getString(shiftDetailList.get(1).get("FromLogicInventoryInfoID")));
        
        assertEquals(ConvertUtil.getString(logicInventoryID2),ConvertUtil.getString(shiftDetailList.get(0).get("ToLogicInventoryInfoID")));
        assertEquals(ConvertUtil.getString(logicInventoryID2),ConvertUtil.getString(shiftDetailList.get(1).get("ToLogicInventoryInfoID")));
    
    
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Inventory.BIN_PromotionStock");
        tableDataMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        tableDataMap.put("BIN_InventoryInfoID", depotInfoID);
        tableDataMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        List<Map<String,Object>> stockList = testCOM_Service.getTableData(tableDataMap);
        assertEquals(ConvertUtil.getString(expectList.get(2).get("Quantity")),ConvertUtil.getString(stockList.get(0).get("Quantity")));
        
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Inventory.BIN_PromotionStock");
        tableDataMap.put("BIN_PromotionProductVendorID", promotionProductVendorID1);
        tableDataMap.put("BIN_InventoryInfoID", depotInfoID);
        tableDataMap.put("BIN_LogicInventoryInfoID", logicInventoryID2);
        stockList = testCOM_Service.getTableData(tableDataMap);
        assertEquals(ConvertUtil.getString(expectList.get(3).get("Quantity")),ConvertUtil.getString(stockList.get(0).get("Quantity")));
        
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Inventory.BIN_PromotionStock");
        tableDataMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        tableDataMap.put("BIN_InventoryInfoID", depotInfoID);
        tableDataMap.put("BIN_LogicInventoryInfoID", logicInventoryID1);
        stockList = testCOM_Service.getTableData(tableDataMap);
        assertEquals(ConvertUtil.getString(expectList.get(4).get("Quantity")),ConvertUtil.getString(stockList.get(0).get("Quantity")));
        
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Inventory.BIN_PromotionStock");
        tableDataMap.put("BIN_PromotionProductVendorID", promotionProductVendorID2);
        tableDataMap.put("BIN_InventoryInfoID", depotInfoID);
        tableDataMap.put("BIN_LogicInventoryInfoID", logicInventoryID2);
        stockList = testCOM_Service.getTableData(tableDataMap);
        assertEquals(ConvertUtil.getString(expectList.get(5).get("Quantity")),ConvertUtil.getString(stockList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAddPromotionStockInfo1() throws Exception{
        String caseName = "testAddPromotionStockInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //测试私有方法
        BINBEMQMES01_BL bl = applicationContext.getBean(BINBEMQMES01_BL.class);
        Class<?>[] args = new Class[]{List.class,Map.class,String.class,String.class};// 建立参数
        Method method = bl.getClass().getDeclaredMethod("addPromotionStockInfo",args);
        method.setAccessible(true);
        Map<String,Object> map = (Map<String, Object>) dataMap.get("MQMap");
        List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>) dataMap.get("MQList");
        method.invoke(bl,detailDataDTOList,map,"1","1");
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        paramMap.put("RelevantNo", map.get("tradeNoIF"));
        List<Map<String,Object>> promotionStockInOut = testCOM_Service.getTableData(paramMap);
        int stockInOutID = CherryUtil.obj2int(promotionStockInOut.get(0).get("BIN_PromotionStockInOutID"));
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        paramMap.put("BIN_PromotionStockInOutID", stockInOutID);
        List<Map<String,Object>> promotionStockDetail = testCOM_Service.getTableData(paramMap);
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expectedList");
        assertEquals(expectedList.size(),promotionStockDetail.size());
        for(int i=0;i<expectedList.size();i++){
            String expectedValue = ConvertUtil.getString(expectedList.get(i).get("BIN_PromotionProductVendorID"));
            String actualValue = ConvertUtil.getString(promotionStockDetail.get(i).get("BIN_PromotionProductVendorID"));
            assertEquals(expectedValue,actualValue);
        }
        
        method.setAccessible(false);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAddPromotionStockInfoForSale1() throws Exception{
        String caseName = "testAddPromotionStockInfoForSale1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //测试私有方法
        BINBEMQMES01_BL bl = applicationContext.getBean(BINBEMQMES01_BL.class);
        Class<?>[] args = new Class[]{List.class,Map.class,String.class,String.class};// 建立参数
        Method method = bl.getClass().getDeclaredMethod("addPromotionStockInfoForSale",args);
        method.setAccessible(true);
        Map<String,Object> map = (Map<String, Object>) dataMap.get("MQMap");
        List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>) dataMap.get("MQList");
        method.invoke(bl,detailDataDTOList,map,"1","1");
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        paramMap.put("RelevantNo", map.get("tradeNoIF"));
        List<Map<String,Object>> promotionStockInOut = testCOM_Service.getTableData(paramMap);
        int stockInOutID = CherryUtil.obj2int(promotionStockInOut.get(0).get("BIN_PromotionStockInOutID"));
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        paramMap.put("BIN_PromotionStockInOutID", stockInOutID);
        List<Map<String,Object>> promotionStockDetail = testCOM_Service.getTableData(paramMap);
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expectedList");
        assertEquals(expectedList.size(),promotionStockDetail.size());
        for(int i=0;i<expectedList.size();i++){
            String expectedValue = ConvertUtil.getString(expectedList.get(i).get("BIN_PromotionProductVendorID"));
            String actualValue = ConvertUtil.getString(promotionStockDetail.get(i).get("BIN_PromotionProductVendorID"));
            assertEquals(expectedValue,actualValue);
        }
        
        method.setAccessible(false);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeStockBirPresentData1() throws Exception{
        //促销品礼品领用（有coupon）
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
        insertCampaignOrderDetailMap1.put("BIN_ProductVendorID", promotionProductVendorID1);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap1);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap2 = dataList.get(16);
        insertCampaignOrderDetailMap2.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap2.put("BIN_ProductVendorID", promotionProductVendorID2);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap2);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualParamMap.put("RelevantNo", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
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
        assertEquals(promotionProductVendorID1,CherryUtil.obj2int(actualList.get(0).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType1"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(1).get("MainCode")));
        assertEquals(promotionProductVendorID2,CherryUtil.obj2int(actualList.get(1).get("BIN_ProductVendorID")));
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
        //促销品礼品领用（无coupon）
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
        insertCampaignOrderDetailMap1.put("BIN_ProductVendorID", promotionProductVendorID1);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap1);
        
        //Campaign.BIN_CampaignOrderDetail
        Map<String,Object> insertCampaignOrderDetailMap2 = dataList.get(16);
        insertCampaignOrderDetailMap2.put("BIN_CampaignOrderID", campaignOrderID);
        insertCampaignOrderDetailMap2.put("BIN_ProductVendorID", promotionProductVendorID2);
        testCOM_Service.insertTableDataNoReturnID(insertCampaignOrderDetailMap2);
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> otherList = (List<Map<String,Object>>)dataMap.get("otherList");
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        String tradeNoIF = ConvertUtil.getString(otherList.get(0).get("TradeNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualParamMap.put("RelevantNo", tradeNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
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
        assertEquals(promotionProductVendorID1,CherryUtil.obj2int(actualList.get(0).get("BIN_ProductVendorID")));
        assertEquals(expectList.get(0).get("InventoryTypeCode"),ConvertUtil.getString(actualList.get(0).get("InventoryTypeCode")));
        assertEquals(logicInventoryID1,CherryUtil.obj2int(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(expectList.get(0).get("GiftDrawDetailQuantity1"),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        assertEquals(expectList.get(0).get("GiftDrawDetailGiftType1"),ConvertUtil.getString(actualList.get(0).get("GiftType")));
        assertEquals(expectList.get(0).get("MainCode2"),ConvertUtil.getString(actualList.get(1).get("MainCode")));
        assertEquals(promotionProductVendorID2,CherryUtil.obj2int(actualList.get(1).get("BIN_ProductVendorID")));
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
        
        //Basis.BIN_PromotionProduct
        Map<String,Object> insertPromotionProductMap = dataList.get(2);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID1 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> insertPromotionProductVendorMap = dataList.get(3);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID1);
        int promotionProductVendorID1 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
        //Basis.BIN_PromotionProduct
        insertPromotionProductMap = dataList.get(4);
        insertPromotionProductMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPromotionProductMap.put("BIN_BrandInfoID", brandInfoID);
        int promotionProductID2 = testCOM_Service.insertTableData(insertPromotionProductMap);
        
        //Basis.BIN_PromotionProductVendor
        insertPromotionProductVendorMap = dataList.get(5);
        insertPromotionProductVendorMap.put("BIN_PromotionProductID", promotionProductID2);
        int promotionProductVendorID2 = testCOM_Service.insertTableData(insertPromotionProductVendorMap);
        
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
        paramMap.put("tableName", "Inventory.BIN_PromotionAllocation");
        paramMap.put("AllocationNoIF", tradeNoIF1);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals("",actualList.get(0).get("RelevanceNo"));
        assertEquals(expectList.get(0).get("VerifiedFlag"),actualList.get(0).get("VerifiedFlag"));
        assertEquals(expectList.get(0).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
        int promotionAllocationID = CherryUtil.obj2int(actualList.get(0).get("BIN_PromotionAllocationID"));
        
        //执行调出
        msgBody = getMessageBody(mqList.get(1));
        tran_analyzeMessage(msgBody);
        
        String tradeNoIF2 = ConvertUtil.getString(otherList.get(0).get("TradeNoIF2"));

        //验证调出单
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionAllocation");
        paramMap.put("AllocationNoIF", tradeNoIF2);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(tradeNoIF1,actualList.get(0).get("RelevanceNo"));
        assertEquals(expectList.get(2).get("VerifiedFlag"),actualList.get(0).get("VerifiedFlag"));
        assertEquals(expectList.get(2).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
        int productAllocationOutID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductAllocationOutID"));
        
        //验证调入单
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionAllocation");
        paramMap.put("AllocationNoIF", tradeNoIF1);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(expectList.get(3).get("VerifiedFlag"),actualList.get(0).get("VerifiedFlag"));
        assertEquals(expectList.get(3).get("TradeStatus"),actualList.get(0).get("TradeStatus"));
        int productAllocationInID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductAllocationInID"));
    }
}
