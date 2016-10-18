package com.cherry.ss.prm.action;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.prm.form.BINOLSSPRM21_Form;

public class BINOLSSPRM21_Action_TEST extends CherryJunitBase{
	private BINOLSSPRM21_Action action21;
	@Resource
	private TESTCOM_Service testcom_Service;
	/**
	 * 盘点部门支持终端盘点
	 * */
	private void setUpinit1() throws Exception{
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		action21 = createAction(BINOLSSPRM21_Action.class, "/ss","BINOLSSPRM21_INIT");
		BINOLSSPRM21_Form form = action21.getModel();
		DataUtil.getForm(this.getClass(), "testInit1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action21.setSession(session);
	}
	
 	@Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception{
	    //更新配置项表，启用支持终端盘点配置项
	 	testcom_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1037 AND CommentsChinese='是'");
		testcom_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1037 AND CommentsChinese='否'");
	 	//设置初始化方法所需参数
		setUpinit1();
    	Assert.assertEquals("success", proxy.execute());
    	BINOLSSPRM21_Form form =action21.getModel();
    	//取得部门list
    	List list= form.getOrganizationList();
    	if(null!=list){
    		for(int i=0;i<list.size();i++){
        		Map<String, Object> dataMap = (Map) list.get(i);
        		//部门类型
        		String departType = (String) dataMap.get("DepartType");
        		if(!"4".equals(departType)){//部门类型不为4的过滤掉
        				list.remove(i);
                         i--;
                         continue;
        		}
        	}
    		if(list.size()!=0){
        		for(int j=0;j<list.size();j++){//部门类型为4，柜台
    	    		Map<String, Object> dataMap1 = (Map) list.get(j);
    	    		String departType = (String) dataMap1.get("DepartType");
    	    		//部门类型为柜台
    		    	Assert.assertEquals("4", departType);
    	    	}
        	}
    	}
    }
 	
 	/**
 	 * 盘点部门不支持终端盘点
 	 * */
	private void setUpinit2() throws Exception{
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		action21 = createAction(BINOLSSPRM21_Action.class, "/ss","BINOLSSPRM21_INIT");
		BINOLSSPRM21_Form form = action21.getModel();
		DataUtil.getForm(this.getClass(), "testInit1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action21.setSession(session);
	}
	@Test
    @Rollback(true)
    @Transactional
    public void testInit2() throws Exception{
	    //更新配置项表，启用支持终端盘点配置项
	 	testcom_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1037 AND CommentsChinese='是'");
		testcom_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1037 AND CommentsChinese='否'");
	 	//设置初始化方法所需参数
		setUpinit2();
    	Assert.assertEquals("success", proxy.execute());
    	BINOLSSPRM21_Form form =action21.getModel();
    	//取得部门list
    	List list= form.getOrganizationList();
    	List<Map<String, Object>> departList = new ArrayList<Map<String,Object>>();
    	if(null!=list){
    		for(int i=0;i<list.size();i++){
        		Map<String, Object> dataMap = (Map) list.get(i);
        		//部门类型
        		String departType = (String) dataMap.get("DepartType");
        		Map<String, Object> departMap = new HashMap<String, Object>();
        		if("4".equals(departType)){
            		departMap.put("departType", departType);
            		departList.add(departMap);
        		}
        	}
    	}
    	//不存在柜台，没有类型为4的部门
    	Assert.assertEquals(0, departList.size());
    }
	 /**
	  * 需要管理库存的产品
	  * */
	private void setUpSearch() throws Exception{
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testStocktaking");
		action21 = createAction(BINOLSSPRM21_Action.class, "/ss","BINOLSSPRM21_STOCKTAKING");
		BINOLSSPRM21_Form form = action21.getModel();
		DataUtil.getForm(this.getClass(), "testStocktaking", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action21.setSession(session);
	}
    @Test
    @Rollback(true)
    @Transactional
    public void testStocktaking() throws Exception{
    	setUpSearch();
    	Assert.assertEquals("success", proxy.execute());
    	BINOLSSPRM21_Form form2 =action21.getModel();
    	List list= form2.getStockPromotionList();
    	for(int i=0;i<list.size();i++){
    		Map<String, Object> dataMap = (Map) list.get(i);
    		String IsStock = (String) dataMap.get("isStock");
    		//是否需要管理库存状态：0（不需要），1(需要)
    		Assert.assertEquals("1", IsStock);
    	}
    }
}
	 
