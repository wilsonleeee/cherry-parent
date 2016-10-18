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
import com.cherry.ss.prm.form.BINOLSSPRM22_Form;


public class BINOLSSPRM22_Action_TEST extends CherryJunitBase{
	private BINOLSSPRM22_Action action22;
    @Resource 
    private TESTCOM_Service testService;
	/**
	 * 盘点部门支持终端盘点
	 * */
	private void setUpinit1() throws Exception{
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		action22 = createAction(BINOLSSPRM22_Action.class, "/ss","BINOLSSPRM22_INIT");
		BINOLSSPRM22_Form form = action22.getModel();
		DataUtil.getForm(this.getClass(), "testInit1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action22.setSession(session);
	}
	
 	@Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception{
	    //更新配置项表，启用支持终端盘点配置项
 		testService.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1037 AND CommentsChinese='是'");
	 	testService.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1037 AND CommentsChinese='否'");
	 	//设置初始化方法所需参数
		setUpinit1();
    	Assert.assertEquals("success", proxy.execute());
    	BINOLSSPRM22_Form form =action22.getModel();
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
		action22 = createAction(BINOLSSPRM22_Action.class, "/ss","BINOLSSPRM22_INIT");
		BINOLSSPRM22_Form form = action22.getModel();
		DataUtil.getForm(this.getClass(), "testInit1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action22.setSession(session);
	}
	@Test
    @Rollback(true)
    @Transactional
    public void testInit2() throws Exception{
	    //更新配置项表，启用支持终端盘点配置项
		testService.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1037 AND CommentsChinese='是'");
		testService.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1037 AND CommentsChinese='否'");
	 	//设置初始化方法所需参数
		setUpinit2();
    	Assert.assertEquals("success", proxy.execute());
    	BINOLSSPRM22_Form form =action22.getModel();
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
    @Test
    @Rollback(true)
    @Transactional
    public void testgetIsStock() throws Exception{
    	action22 = createAction(BINOLSSPRM22_Action.class, "/ss", "BINOLSSPRM22_GETSTOCKCOUNT");
    	List Stocklist = DataUtil.getDataList(this.getClass(), "testgetIsStock"); 
    	int staticId =0;
		for(int i =0;i<Stocklist.size();i++){
			Map map = (Map)Stocklist.get(i);
			if(i!=0){
				map.put("BIN_PromotionProductID", staticId);
			}
			int PromotionProductID = testService.insertTableData(map);
			if(i==0){
				staticId = PromotionProductID;
			}
		}
		String promotionID = Integer.toString(staticId);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_PromotionProductVendor");
		paramMap.put("BIN_PromotionProductID", promotionID);
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> MainMap =(Map)resultList.get(0);
		//促销品厂商Id
		String []promVendorIdArr= { MainMap.get("BIN_PromotionProductVendorID").toString()};
		BINOLSSPRM22_Form form = action22.getModel();
		DataUtil.getForm(this.getClass(), "testgetIsStock", form);
		form.setPromotionProductVendorIDArr(promVendorIdArr);
		action22.setSession(session);
		//实体仓库ID
		request.setParameter("depot", "16");
		//逻辑仓库ID
		request.setParameter("logicDepot", "11");
		response.setCharacterEncoding("utf-8");
		action22.setServletResponse(response);
		action22.setServletRequest(request);
		action22.getStockCount();
    }
    @Test
    @Rollback(true)
    @Transactional
    public void testgetIsStock1() throws Exception{
    	action22 = createAction(BINOLSSPRM22_Action.class, "/ss", "BINOLSSPRM22_GETSTOCKCOUNT");
    	List Stocklist = DataUtil.getDataList(this.getClass(), "testgetIsStock1"); 
    	int staticId =0;
		for(int i =0;i<Stocklist.size();i++){
			Map map = (Map)Stocklist.get(i);
			if(i!=0){
				map.put("BIN_PromotionProductID", staticId);
			}
			int PromotionProductID = testService.insertTableData(map);
			if(i==0){
				staticId = PromotionProductID;
			}
		}
		String promotionID = Integer.toString(staticId);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_PromotionProductVendor");
		paramMap.put("BIN_PromotionProductID", promotionID);
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> MainMap =(Map)resultList.get(0);
		//促销品厂商Id
		String []promVendorIdArr= { MainMap.get("BIN_PromotionProductVendorID").toString()};
		BINOLSSPRM22_Form form = action22.getModel();
		DataUtil.getForm(this.getClass(), "testgetIsStock1", form);
		form.setPromotionProductVendorIDArr(promVendorIdArr);
		action22.setSession(session);
		//实体仓库ID
		request.setParameter("depot", "16");
		//逻辑仓库ID
		request.setParameter("logicDepot", "11");
		response.setCharacterEncoding("utf-8");
		action22.setServletResponse(response);
		action22.setServletRequest(request);
		action22.getStockCount();
    }
}
	 
