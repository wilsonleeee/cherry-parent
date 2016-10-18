package com.cherry.cm.cmbussiness.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.TestAction;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

/**
 * 逻辑仓库共通测试代码
 * @author jijw
 *
 */
public class BINOLCM18_BL_TEST extends CherryJunitBase{
	
	@Resource
	private BINOLCM18_BL binOLCM18_BL;
	
	@Resource
	private TESTCOM_Service service;
	
	private TestAction textAction;
	
    @Before
    public void setUp() throws Exception {
    	textAction = createAction(TestAction.class, "/common",
				"textActionName");
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    /**
     * 测试返回异常
     * 查出结果为0件：未查询到指定的逻辑仓库,仓库代码={0}。
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
    public void testGetLogicDepotByCode1() throws Exception{
    	String caseName = "testGetLogicDepotByCode1";
    	Map<String,Object> caseNameMap = (Map<String,Object>)DataUtil.getDataMap(this.getClass(),caseName);
    	Map<String,Object> praMap = (Map<String,Object>)caseNameMap.get("praMap");
    	String insertSql1 = (String)caseNameMap.get("insertSql1");
    	String insertSql2 = (String)caseNameMap.get("insertSql2");
    	String insertSql3 = (String)caseNameMap.get("insertSql3");
    	String insertSql4 = (String)caseNameMap.get("insertSql4");
    	service.insert(insertSql1);
    	service.insert(insertSql2);
    	service.insert(insertSql3);
    	service.insert(insertSql4);
    	 
    	try {
    		Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByCode(praMap);
    	 } catch( CherryException e ){
    		Assert.assertEquals(e.getText("ECM00062", new String[]{praMap.get("LogicInventoryCode").toString()}), e.getErrMessage());
    		//Assert.assertEquals(action.getText("ECM00062", new String[]{praMap.get("LogicInventoryCode").toString()}), e.getErrMessage());
    	 } 
    } 
    
    /**
     * 测试返回异常
     * 查询出多条结果：查询出多条结果，仓库代码={0}。
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	    @Test
    @Transactional
    @Rollback(true)
    public void testGetLogicDepotByCode2() throws Exception{
    	String caseName = "testGetLogicDepotByCode2";
    	Map<String,Object> testgetLogicDepotByID4Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID4Map.get("praMap");
    	String insertSql1 = (String)testgetLogicDepotByID4Map.get("insertSql1");
    	String insertSql2 = (String)testgetLogicDepotByID4Map.get("insertSql2");
    	String insertSql3 = (String)testgetLogicDepotByID4Map.get("insertSql3");
    	String insertSql4 = (String)testgetLogicDepotByID4Map.get("insertSql4");
    	service.insert(insertSql1);
    	service.insert(insertSql2);
    	service.insert(insertSql3);
    	service.insert(insertSql4);
    	
    	try {
    		Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByCode(praMap);
    	} catch( CherryException e ){
    		Assert.assertEquals(e.getText("ECM00063", new String[]{praMap.get("LogicInventoryCode").toString()}), e.getErrMessage());
    	}
    } 
    
    /**
     * 测试返回正常数据
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Transactional
    @Rollback(true)
	@Test
    public void testGetLogicDepotByCode3() throws Exception{
    	String caseName = "testGetLogicDepotByCode3";
    	Map<String,Object> testgetLogicDepotByID4Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID4Map.get("praMap");
    	String insertSql1 = (String)testgetLogicDepotByID4Map.get("insertSql1");
    	String insertSql2 = (String)testgetLogicDepotByID4Map.get("insertSql2");
    	String insertSql3 = (String)testgetLogicDepotByID4Map.get("insertSql3");
    	String insertSql4 = (String)testgetLogicDepotByID4Map.get("insertSql4");
    	service.insert(insertSql1);
    	service.insert(insertSql2);
    	service.insert(insertSql3);
    	service.insert(insertSql4);
    	
		Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByCode(praMap);
		
		Assert.assertEquals(9, resultMap.keySet().size());
		Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
		Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
		Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
		Assert.assertEquals(true, resultMap.containsKey("Type"));
		Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
		Assert.assertEquals(true, resultMap.containsKey("Comments"));
		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    } 
    
    /**
     * 测试没有LogicInventoryCode，返回的结果
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    @Transactional
    @Rollback(true)
    public void testGetLogicDepotByCode4() throws Exception{
    	String caseName = "testGetLogicDepotByCode4";
    	Map<String,Object> testgetLogicDepotByID4Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID4Map.get("praMap");
    	
    	String insertSql1 = (String)testgetLogicDepotByID4Map.get("insertSql1");
    	String insertSql2 = (String)testgetLogicDepotByID4Map.get("insertSql2");
    	String insertSql3 = (String)testgetLogicDepotByID4Map.get("insertSql3");
    	String insertSql4 = (String)testgetLogicDepotByID4Map.get("insertSql4");
    	service.insert(insertSql1);
    	service.insert(insertSql2);
    	service.insert(insertSql3);
    	service.insert(insertSql4);
    	
    	Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByCode(praMap);
    	
    	Assert.assertEquals(9, resultMap.keySet().size());
    	Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
    	Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
    	Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
    	Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
    	Assert.assertEquals(true, resultMap.containsKey("Type"));
    	Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
    	Assert.assertEquals(true, resultMap.containsKey("Comments"));
    	Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
    	Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    } 
    
    /**
     * 测试没有LogicInventoryCode，返回0条异常结果
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    @Transactional
    @Rollback(true)
    public void testGetLogicDepotByCode5() throws Exception{
    	String caseName = "testGetLogicDepotByCode5";
    	Map<String,Object> testgetLogicDepotByID4Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID4Map.get("praMap");
    	
    	String insertSql1 = (String)testgetLogicDepotByID4Map.get("insertSql1");
    	String insertSql2 = (String)testgetLogicDepotByID4Map.get("insertSql2");
    	String insertSql3 = (String)testgetLogicDepotByID4Map.get("insertSql3");
    	String insertSql4 = (String)testgetLogicDepotByID4Map.get("insertSql4");
    	service.insert(insertSql1);
    	service.insert(insertSql2);
    	service.insert(insertSql3);
    	service.insert(insertSql4);
    	try {
    		Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByCode(praMap);
    	} catch (CherryException e){
    		Assert.assertEquals(e.getText("ECM00062", new String[]{null}), e.getErrMessage());
    	}
    } 
    
    /**
     * 测试没有LogicInventoryCode，返回不至1条异常结果
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    @Transactional
    @Rollback(true)
    public void testGetLogicDepotByCode6() throws Exception{
    	String caseName = "testGetLogicDepotByCode6";
    	Map<String,Object> testgetLogicDepotByID4Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID4Map.get("praMap");
    	
    	String insertSql1 = (String)testgetLogicDepotByID4Map.get("insertSql1");
    	String insertSql2 = (String)testgetLogicDepotByID4Map.get("insertSql2");
    	String insertSql3 = (String)testgetLogicDepotByID4Map.get("insertSql3");
    	String insertSql4 = (String)testgetLogicDepotByID4Map.get("insertSql4");
    	service.insert(insertSql1);
    	service.insert(insertSql2);
    	service.insert(insertSql3);
    	service.insert(insertSql4);
    	try {
    		Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByCode(praMap);
    	} catch (CherryException e){
    		Assert.assertEquals(e.getText("ECM00063", new String[]{null}), e.getErrMessage());
    	}
    } 
    
    /**
     * 测试返回正常
     *  
     * @throws Exception
     */
    // @Test
    @SuppressWarnings("unchecked")
	public void testGetLogicDepotByID1() throws Exception{
    	String caseName = "testGetLogicDepotByID1";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByID(praMap);
    	
		Assert.assertEquals(9, resultMap.keySet().size());
		Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
		Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
		Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
		Assert.assertEquals(true, resultMap.containsKey("Type"));
		Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
		Assert.assertEquals(true, resultMap.containsKey("Comments"));
		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    	
    }
    
    /**
     * 测试返回异常
     *  查出结果为0件：未查询到指定的逻辑仓库,仓库ID={0}。
     * @throws Exception
     */
    // @Test
    @SuppressWarnings("unchecked")
    public void testGetLogicDepotByID2() throws Exception{
    	String caseName = "testGetLogicDepotByID2";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	try {
    		Map<String,Object> resultMap = binOLCM18_BL.getLogicDepotByID(praMap);
    		
    	} catch(CherryException e){
    		Assert.assertEquals(e.getText("ECM00064", new String[]{praMap.get("BIN_LogicInventoryInfoID").toString()}), e.getErrMessage());
    	}
    	
    }
    
    /**
     * 测试返回正常结果
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
	public void testGetLogicDepotList1() throws Exception{
    	String caseName = "testGetLogicDepotList1";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	List<Map<String,Object>> resultList = binOLCM18_BL.getLogicDepotList(praMap);
    	if(null != resultList && resultList.size() != 0){
    		Map<String,Object> resultMap = resultList.get(0);
    		Assert.assertEquals(9, resultMap.keySet().size());
    		Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
    		Assert.assertEquals(true, resultMap.containsKey("Type"));
    		Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
    		Assert.assertEquals(true, resultMap.containsKey("Comments"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    	} 
    }
    
    /**
     * 测试返回正常结果
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLogicDepotList2() throws Exception{
    	String caseName = "testGetLogicDepotList2";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	List<Map<String,Object>> resultList = binOLCM18_BL.getLogicDepotList(praMap);
    	if(null != resultList && resultList.size() != 0){
    		Map<String,Object> resultMap = resultList.get(0);
    		Assert.assertEquals(9, resultMap.keySet().size());
    		Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
    		Assert.assertEquals(true, resultMap.containsKey("Type"));
    		Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
    		Assert.assertEquals(true, resultMap.containsKey("Comments"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    	} 
    }
    
    /**
     * 测试返回正常结果
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLogicDepotList3() throws Exception{
    	String caseName = "testGetLogicDepotList3";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	List<Map<String,Object>> resultList = binOLCM18_BL.getLogicDepotList(praMap);
    	if(null != resultList && resultList.size() != 0){
    		Map<String,Object> resultMap = resultList.get(0);
    		Assert.assertEquals(9, resultMap.keySet().size());
    		Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
    		Assert.assertEquals(true, resultMap.containsKey("Type"));
    		Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
    		Assert.assertEquals(true, resultMap.containsKey("Comments"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    	} 
    }
    
    /**
     * 测试返回正常结果
     * 可以查到业务关联的逻辑仓库
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLogicDepotByBusiness0() throws Exception{
    	String caseName = "testGetLogicDepotByBusiness0";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	
    	try {
    		List<Map<String,Object>> resultList = binOLCM18_BL.getLogicDepotByBusiness(praMap);
    		if(null != resultList && resultList.size() != 0){
    			Map<String,Object> resultMap = resultList.get(0);
    			Assert.assertEquals(9, resultMap.keySet().size());
    			Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
    			Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
    			Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
    			Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
    			Assert.assertEquals(true, resultMap.containsKey("Type"));
    			Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
    			Assert.assertEquals(true, resultMap.containsKey("Comments"));
    			Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
    			Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    		} 
    	}catch(CherryException e) {
    		Object subType = praMap.get("SubType");
    		Assert.assertEquals(
    				e.getText("ECM00065",
    						new String[] { praMap.get("Type").toString(),
    						praMap.get("ProductType").toString(),
    						praMap.get("BusinessType").toString(),
    						(null != subType) ? subType.toString() : null }),
    						e.getErrMessage());
    	}
    }
    
    /**
     * 测试返回正常结果
     * 可以查到业务关联的逻辑仓库可以查找getLogicDepotList共通方法获取数据
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLogicDepotByBusiness1() throws Exception{
    	String caseName = "testGetLogicDepotByBusiness1";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	
    	try {
	    	List<Map<String,Object>> resultList = binOLCM18_BL.getLogicDepotByBusiness(praMap);
	    	if(null != resultList && resultList.size() != 0){
	    		Map<String,Object> resultMap = resultList.get(0);
	    		Assert.assertEquals(9, resultMap.keySet().size());
	    		Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
	    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
	    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
	    		Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
	    		Assert.assertEquals(true, resultMap.containsKey("Type"));
	    		Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
	    		Assert.assertEquals(true, resultMap.containsKey("Comments"));
	    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
	    		Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
	    	} 
    	}catch(CherryException e) {
    		Object subType = praMap.get("SubType");
			Assert.assertEquals(
					e.getText("ECM00065",
							new String[] { praMap.get("Type").toString(),
									praMap.get("ProductType").toString(),
									praMap.get("BusinessType").toString(),
									(null != subType) ? subType.toString() : null }),
					e.getErrMessage());
    	}
    }
    
    /**
     * 测试返回异常
     * 查询不到任何数据
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLogicDepotByBusiness2() throws Exception{
    	String caseName = "testGetLogicDepotByBusiness2";
    	Map<String,Object> testgetLogicDepotByID1Map = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	Map<String,Object> praMap = (Map<String,Object>)testgetLogicDepotByID1Map.get("praMap");
    	
    	try {
    		List<Map<String,Object>> resultList = binOLCM18_BL.getLogicDepotByBusiness(praMap);
    		if(null != resultList && resultList.size() != 0){
    			Map<String,Object> resultMap = resultList.get(0);
    			Assert.assertEquals(9, resultMap.keySet().size());
    			Assert.assertEquals(true, resultMap.containsKey("BIN_LogicInventoryInfoID"));
    			Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCode"));
    			Assert.assertEquals(true, resultMap.containsKey("InventoryNameCN"));
    			Assert.assertEquals(true, resultMap.containsKey("InventoryNameEN"));
    			Assert.assertEquals(true, resultMap.containsKey("Type"));
    			Assert.assertEquals(true, resultMap.containsKey("OrderNO"));
    			Assert.assertEquals(true, resultMap.containsKey("Comments"));
    			Assert.assertEquals(true, resultMap.containsKey("LogicInventoryName"));
    			Assert.assertEquals(true, resultMap.containsKey("LogicInventoryCodeName"));
    		} 
    	}catch(CherryException e) {
    		Object subType = praMap.get("SubType");
    		Assert.assertEquals(
    				e.getText("ECM00066",
    						new String[] { praMap.get("Type").toString(),
    						praMap.get("ProductType").toString(),
    						praMap.get("BusinessType").toString(),
    						(null != subType) ? subType.toString() : null }),
    						e.getErrMessage());
    	}
    }

}
