package com.cherry.st.common.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSTCM09_IF_TEST extends CherryJunitBase{

    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM09_BL")
    private BINOLSTCM09_IF bl;
    
    //1
    @Test
    @Rollback(true)
    @Transactional
    public void testChangeStock1() throws Exception{
        String caseName = "testChangeStock1";
        bl = applicationContext.getBean(BINOLSTCM09_IF.class);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass(),caseName);

        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap3 = insertBaseDataList.get(4);
        int productID3 = testCOM_Service.insertTableData(insertProductMap3);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap3 = insertBaseDataList.get(5);
        insertProductVendorMap3.put("BIN_ProductID", productID3);
        int productVendorID3 = testCOM_Service.insertTableData(insertProductVendorMap3);
        
        //插入产品退库主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> detailData3 = dataList.get(3);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductReturnID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableData(detailData1);
        detailData2.put("BIN_ProductReturnID", billID);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableData(detailData2);
        detailData3.put("BIN_ProductReturnID", billID);
        detailData3.put("BIN_ProductVendorID", productVendorID3);
        testCOM_Service.insertTableData(detailData3);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductReturnID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        //验证数据
        Map<String,Object> inOutData = new HashMap<String,Object>();
        inOutData.put("tableName", "Inventory.BIN_ProductInOut");
        inOutData.put("RelevanceNo", mainData.get("ReturnNoIF"));
        List<Map<String,Object>> actualData = testCOM_Service.getTableData(inOutData);
        
        assertEquals(1,actualData.size());
        
        //验证入出库日期
        assertEquals(mainData.get("ReturnDate"),actualData.get(0).get("StockInOutDate"));
        
        inOutData = new HashMap<String,Object>();
        inOutData.put("tableName", "Inventory.BIN_ProductInOutDetail");
        inOutData.put("BIN_ProductInOutID", actualData.get(0).get("BIN_ProductInOutID"));
        actualData = testCOM_Service.getTableData(inOutData);
        
        assertEquals(ConvertUtil.getString(detailData1.get("BIN_ProductVendorID")),ConvertUtil.getString(actualData.get(0).get("BIN_ProductVendorID")));
        assertEquals(ConvertUtil.getString(detailData1.get("Quantity")),ConvertUtil.getString(actualData.get(0).get("Quantity")));
        assertEquals(ConvertUtil.getString(detailData1.get("BIN_InventoryInfoID")),ConvertUtil.getString(actualData.get(0).get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getString(detailData1.get("BIN_LogicInventoryInfoID")),ConvertUtil.getString(actualData.get(0).get("BIN_LogicInventoryInfoID")));
    
        assertEquals(ConvertUtil.getString(detailData2.get("BIN_ProductVendorID")),ConvertUtil.getString(actualData.get(1).get("BIN_ProductVendorID")));
        assertEquals(ConvertUtil.getString(detailData2.get("Quantity")),ConvertUtil.getString(actualData.get(1).get("Quantity")));
        assertEquals(ConvertUtil.getString(detailData2.get("BIN_InventoryInfoID")),ConvertUtil.getString(actualData.get(1).get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getString(detailData2.get("BIN_LogicInventoryInfoID")),ConvertUtil.getString(actualData.get(1).get("BIN_LogicInventoryInfoID")));
        
        assertEquals(ConvertUtil.getString(detailData3.get("BIN_ProductVendorID")),ConvertUtil.getString(actualData.get(2).get("BIN_ProductVendorID")));
        assertEquals(ConvertUtil.getString(detailData3.get("Quantity")),ConvertUtil.getString(actualData.get(2).get("Quantity")));
        assertEquals(ConvertUtil.getString(detailData3.get("BIN_InventoryInfoID")),ConvertUtil.getString(actualData.get(2).get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getString(detailData3.get("BIN_LogicInventoryInfoID")),ConvertUtil.getString(actualData.get(2).get("BIN_LogicInventoryInfoID")));
    }
    
    //2
    @Test
    @Rollback(true)
    @Transactional
    public void testChangeStock2() throws Exception{
        String caseName = "testChangeStock2";
        bl = applicationContext.getBean(BINOLSTCM09_IF.class);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass(),caseName);

        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap3 = insertBaseDataList.get(4);
        int productID3 = testCOM_Service.insertTableData(insertProductMap3);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap3 = insertBaseDataList.get(5);
        insertProductVendorMap3.put("BIN_ProductID", productID3);
        int productVendorID3 = testCOM_Service.insertTableData(insertProductVendorMap3);
        
        //插入产品退库主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> detailData3 = dataList.get(3);
        Map<String,Object> detailData4 = dataList.get(4);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductReturnID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableData(detailData1);
        detailData2.put("BIN_ProductReturnID", billID);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableData(detailData2);
        detailData3.put("BIN_ProductReturnID", billID);
        detailData3.put("BIN_ProductVendorID", productVendorID3);
        testCOM_Service.insertTableData(detailData3);
        //插入逻辑仓库
        testCOM_Service.insertTableData(detailData4);
        
        //判断业务日期是否存在，不存在插入一个
        Map<String,Object> bussinessDateParam = new HashMap<String,Object>();
        bussinessDateParam.put("organizationInfoId", mainData.get("BIN_OrganizationInfoID"));
        bussinessDateParam.put("brandInfoId", mainData.get("BIN_BrandInfoID"));
        String bussinessDate = testCOM_Service.getBussinessDate(bussinessDateParam);
        Map<String,Object> bussinessDateInsertMap = dataList.get(5);
        if(null == bussinessDate || "".equals(bussinessDate)){
            testCOM_Service.insertTableData(bussinessDateInsertMap);
            bussinessDate = ConvertUtil.getString(bussinessDateInsertMap.get("ControlDate"));
        }
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductReturnID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        //验证数据
        Map<String,Object> inOutData = new HashMap<String,Object>();
        inOutData.put("tableName", "Inventory.BIN_ProductInOut");
        inOutData.put("RelevanceNo", mainData.get("ReturnNoIF"));
        List<Map<String,Object>> actualData = testCOM_Service.getTableData(inOutData);
        
        assertEquals(1,actualData.size());
        
        //验证入出库日期
        assertEquals(bussinessDate,ConvertUtil.getString(actualData.get(0).get("StockInOutDate")));
        
        inOutData = new HashMap<String,Object>();
        inOutData.put("tableName", "Inventory.BIN_ProductInOutDetail");
        inOutData.put("BIN_ProductInOutID", actualData.get(0).get("BIN_ProductInOutID"));
        actualData = testCOM_Service.getTableData(inOutData);
        
        assertEquals(ConvertUtil.getString(detailData1.get("BIN_ProductVendorID")),ConvertUtil.getString(actualData.get(0).get("BIN_ProductVendorID")));
        assertEquals(ConvertUtil.getString(detailData1.get("Quantity")),ConvertUtil.getString(actualData.get(0).get("Quantity")));
        assertEquals(ConvertUtil.getString(detailData1.get("BIN_InventoryInfoID")),ConvertUtil.getString(actualData.get(0).get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getString(detailData1.get("BIN_LogicInventoryInfoID")),ConvertUtil.getString(actualData.get(0).get("BIN_LogicInventoryInfoID")));
    
        assertEquals(ConvertUtil.getString(detailData2.get("BIN_ProductVendorID")),ConvertUtil.getString(actualData.get(1).get("BIN_ProductVendorID")));
        assertEquals(ConvertUtil.getString(detailData2.get("Quantity")),ConvertUtil.getString(actualData.get(1).get("Quantity")));
        assertEquals(ConvertUtil.getString(detailData2.get("BIN_InventoryInfoID")),ConvertUtil.getString(actualData.get(1).get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getString(detailData2.get("BIN_LogicInventoryInfoID")),ConvertUtil.getString(actualData.get(1).get("BIN_LogicInventoryInfoID")));
        
        assertEquals(ConvertUtil.getString(detailData3.get("BIN_ProductVendorID")),ConvertUtil.getString(actualData.get(2).get("BIN_ProductVendorID")));
        assertEquals(ConvertUtil.getString(detailData3.get("Quantity")),ConvertUtil.getString(actualData.get(2).get("Quantity")));
        assertEquals(ConvertUtil.getString(detailData3.get("BIN_InventoryInfoID")),ConvertUtil.getString(actualData.get(2).get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getString(detailData3.get("BIN_LogicInventoryInfoID")),ConvertUtil.getString(actualData.get(2).get("BIN_LogicInventoryInfoID")));
    }
}