package com.cherry.ss.prm.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

 
 

public class BINOLSSPRM32_Action_TEST extends CherryJunitBase{
	
	private BINOLSSPRM32_Action action;
	
	@Resource
	private TESTCOM_Service service;
	

	
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed1() throws Exception{
		 String caseName = "testDetailed1";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql1").toString();
		 service.insert(insert);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql1").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','3022','2','2','0.0000','0','0','3022','30220167','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        List<Map<String, Object>> detailList = action.getDetailList();
        int quantity = 0 ;
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        }
        assertEquals("OT010031208030220167",detailList.get(0).get("TradeNoIF"));
        assertEquals(2,quantity);
        assertEquals("0",detailList.get(0).get("StockType"));

	 }
	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed2() throws Exception{
		 String caseName = "testDetailed2";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql2").toString();
		 service.insert(insert);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql2").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','8888','2','-11','0.0000','0','0','8888','88888888','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        List<Map<String, Object>> detailList = action.getDetailList();
        
        int quantity = 0 ;
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        }
        assertEquals("OT010031208030220168",detailList.get(0).get("TradeNoIF"));
        assertEquals(11,quantity);
        assertEquals("1",detailList.get(0).get("StockType"));

	 }
	

	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed3() throws Exception{
		 String caseName = "testDetailed3";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql3").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql31").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql3").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','6666','2','12','0.0000','0','0','6666','66666666','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
				List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql31").toString());
				String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
				String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
										"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
										"VALUES('"+stockID2+"','6666','2','18','0.0000','0','0','6666','66666666','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
				service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220169",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220170",detailList.get(i).get("TradeNoIF"));
        	}
    	
        }
        assertEquals(30,quantity);
        assertEquals("0",detailList.get(0).get("StockType"));

	 }
	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed4() throws Exception{
		 String caseName = "testDetailed4";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql4").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql41").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql4").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','9999','2','27','0.0000','0','0','9999','44444969','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql41").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','9999','2','23','0.0000','0','1','9999','44444969','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	
        	if(i==0){
        		assertEquals("OT010031208030220171",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220172",detailList.get(i).get("TradeNoIF"));
        	}
    	
        }      
        assertEquals(4,quantity);
        assertEquals("0",detailList.get(0).get("StockType"));

	 }
	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed5() throws Exception{
		 String caseName = "testDetailed5";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql5").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql51").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql5").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','7777','2','127','0.0000','0','0','7777','77777777','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql51").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','7777','2','121','0.0000','0','1','7777','77777777','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220173",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220174",detailList.get(i).get("TradeNoIF"));
        	}
        }
        assertEquals(-6,quantity);
        assertEquals("1",detailList.get(0).get("StockType"));

	 }
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed6() throws Exception{
		 String caseName = "testDetailed6";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql6").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql61").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql6").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','11111','2','25','0.0000','0','0','11111','11111111','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql61").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','11111','2','25','0.0000','0','1','11111','11111111','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220175",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220176",detailList.get(i).get("TradeNoIF"));
        	}
        }
        assertEquals(0,quantity);
        assertEquals("0",detailList.get(0).get("StockType"));

	 }
	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed7() throws Exception{
		 String caseName = "testDetailed7";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql7").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql71").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql7").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','22222','2','27','0.0000','0','0','22222','22222222','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql71").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','22222','2','121','0.0000','0','1','22222','22222222','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220177",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220178",detailList.get(i).get("TradeNoIF"));
        	}
        }
        assertEquals(-94,quantity);
        assertEquals("0",detailList.get(0).get("StockType"));

	 }
	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed8() throws Exception{
		 String caseName = "testDetailed8";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql8").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql81").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql8").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','33333','2','127','0.0000','0','0','33333','33333333','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql81").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','33333','2','121','0.0000','0','1','33333','33333333','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220179",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220180",detailList.get(i).get("TradeNoIF"));
        	}
        }
        assertEquals(-6,quantity);
        assertEquals("1",detailList.get(0).get("StockType"));

	 }
	 	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed9() throws Exception{
		 String caseName = "testDetailed9";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql9").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql91").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql9").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','44444','2','227','0.0000','0','0','44444','43434343','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql91").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','44444','2','121','0.0000','0','1','44444','43434343','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220181",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220182",detailList.get(i).get("TradeNoIF"));
        	}
        }
        assertEquals(106,quantity);
        assertEquals("0",detailList.get(0).get("StockType"));

	 }
	 
	 @Test
	 @Transactional
	 @Rollback(true)
	 public void testDetailed10() throws Exception{
		 String caseName = "testDetailed10";
		 
		 Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		 //插入
		 String insert =praData.get("insertSql10").toString();
		 service.insert(insert);
		 //插入
		 String insert1 =praData.get("insertSql101").toString();
		 service.insert(insert1);
		 
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID = service.select(praData.get("selSql10").toString());
		String stockID = stockInOutID.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert2 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID+"','55555','2','12','0.0000','0','0','55555','545454','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert2);
		// 查询促销品入库主表中BIN_PromotionStockInOutID
		List<Map<String, Object>>  stockInOutID2 = service.select(praData.get("selSql101").toString());
		String stockID2 = stockInOutID2.get(0).get("BIN_PromotionStockInOutID").toString();
		//拼接促销品入出库明细表信息插入SQL
		String insert3 ="INSERT INTO Inventory.BIN_PromotionStockDetail" +
								"(BIN_PromotionStockInOutID,BIN_PromotionProductVendorID,DetailNo,Quantity,Price,BIN_ProductVendorPackageID,StockType,BIN_InventoryInfoID,BIN_LogicInventoryInfoID,BIN_StorageLocationInfoID,ValidFlag,CreateTime,CreatedBy,CreatePGM,UpdateTime,UpdatedBy,UpdatePGM,ModifyCount)" +
								"VALUES('"+stockID2+"','55555','2','2','0.0000','0','1','55555','545454','0','1','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','2012-08-28 09:07:26.493','mgpbrand','BINOLSSPRM24','0')";
		service.insert(insert3);

	    action = createAction(BINOLSSPRM32_Action.class,"/ss","BINOLSSPRM32_getdetailed");
	    UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
//	    BINOLSSPRM32_Form form = action.getModel();
	    setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        proxy.execute();
        //根据条件查询出明细信息
        int quantity = 0 ;
        List<Map<String, Object>> detailList = action.getDetailList();
        for(int i = 0;i<detailList.size();i++){
        	quantity +=(Integer) detailList.get(i).get("Quantity");
        	if(i==0){
        		assertEquals("OT010031208030220183",detailList.get(i).get("TradeNoIF"));
        	}else{
        		assertEquals("OT010031208030220184",detailList.get(i).get("TradeNoIF"));
        	}
        }
        assertEquals(-10,quantity);
        assertEquals("1",detailList.get(0).get("StockType"));

	 }
}
