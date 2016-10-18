package com.cherry.pl.scf.action;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLPLSCF15_Action_TEST extends CherryJunitBase{
    private BINOLPLSCF15_Action action;
    
    private CodeTable code;
    
    @After
    public void tearDown() throws Exception {

    }
    @Before
    public void setUp() throws Exception {

    }
    
    @Test
    public void testInit1() throws Exception{
        String caseName = "testInit1";
        action = createAction(BINOLPLSCF15_Action.class, "/pl","BINOLPLSCF15_init");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_CHERRY_LANGUAGE,language);
        action.setSession(session);
               
        assertEquals("success", proxy.execute());
        List<Map<String,Object>> brandInfoList = action.getModel().getBrandInfoList();
        List<Map<String,Object>> workflowInfoList = action.getModel().getWorkflowInfoList();
        assertEquals("3",ConvertUtil.getString(brandInfoList.get(0).get("brandInfoId")));
        assertEquals("毛戈平",ConvertUtil.getString(brandInfoList.get(0).get("brandName")));
        
        if(workflowInfoList.size()!=0){
            for(int i = 0 ;i<workflowInfoList.size();i++){
            	int configStatus = Integer.parseInt((String) workflowInfoList.get(i).get("configStatus"));
            	int key = Integer.parseInt((String) workflowInfoList.get(i).get("CodeKey"));
            	String value = (String) workflowInfoList.get(i).get("Value");
    	        	if(key==1){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("1",ConvertUtil.getString(key));
    	        	     assertEquals("产品发货流程",ConvertUtil.getString(value));
    	        	}else if(key==2){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("2",ConvertUtil.getString(key));
    	        	     assertEquals("产品盘点流程",ConvertUtil.getString(value));
    	        	}else if(key==3){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("3",ConvertUtil.getString(key));
    	                assertEquals("产品入库流程",ConvertUtil.getString(value));
    	        	}else if(key==4){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("4",ConvertUtil.getString(key));
    	        	     assertEquals("产品报损流程",ConvertUtil.getString(value));
    	        	}else if(key==6){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("6",ConvertUtil.getString(key));
    	                assertEquals("产品移库流程",ConvertUtil.getString(value));
    	        	}else if(key==7){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("7",ConvertUtil.getString(key));
    	        		assertEquals("产品退库流程",ConvertUtil.getString(value));
    	        	}else if(key==8){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	        	    assertEquals("8",ConvertUtil.getString(key));
    	        		assertEquals("促销品发货流程",ConvertUtil.getString(value));
    	        	}else if(key==9){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	        	    assertEquals("9",ConvertUtil.getString(key));
    	        		assertEquals("促销品调拨流程",ConvertUtil.getString(value));
    	        	}
            	}
        }

    }
    
    @Test
    public void testInit2() throws Exception{
        String caseName = "testInit2";
        action = createAction(BINOLPLSCF15_Action.class, "/pl","BINOLPLSCF15_init");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_CHERRY_LANGUAGE,language);
        action.setSession(session);
               
        assertEquals("success", proxy.execute());
        List<Map<String,Object>> brandInfoList = action.getModel().getBrandInfoList();
        List<Map<String,Object>> workflowInfoList = action.getModel().getWorkflowInfoList();
        assertEquals("-9999",ConvertUtil.getString(brandInfoList.get(0).get("brandInfoId")));
        assertEquals("集团总部",ConvertUtil.getString(brandInfoList.get(0).get("brandName")));
        
        if(workflowInfoList.size()!=0){
            for(int i = 0 ;i<workflowInfoList.size();i++){
            	int configStatus = Integer.parseInt((String) workflowInfoList.get(i).get("configStatus"));
            	int key = Integer.parseInt((String) workflowInfoList.get(i).get("CodeKey"));
            	String value = (String) workflowInfoList.get(i).get("Value");
    	        	if(key==1){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("1",ConvertUtil.getString(key));
    	        	     assertEquals("产品发货流程",ConvertUtil.getString(value));
    	        	}else if(key==2){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("2",ConvertUtil.getString(key));
    	        	     assertEquals("产品盘点流程",ConvertUtil.getString(value));
    	        	}else if(key==3){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("3",ConvertUtil.getString(key));
    	                assertEquals("产品入库流程",ConvertUtil.getString(value));
    	        	}else if(key==4){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("4",ConvertUtil.getString(key));
    	        	     assertEquals("产品报损流程",ConvertUtil.getString(value));
    	        	}else if(key==6){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("6",ConvertUtil.getString(key));
    	                assertEquals("产品移库流程",ConvertUtil.getString(value));
    	        	}else if(key==7){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("7",ConvertUtil.getString(key));
    	        		assertEquals("产品退库流程",ConvertUtil.getString(value));
    	        	}else if(key==8){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	        	    assertEquals("8",ConvertUtil.getString(key));
    	        		assertEquals("促销品发货流程",ConvertUtil.getString(value));
    	        	}else if(key==9){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	        	    assertEquals("9",ConvertUtil.getString(key));
    	        		assertEquals("促销品调拨流程",ConvertUtil.getString(value));
    	        	}
            	}
        }
    }
    
    @Test
    public void testChangeBrand() throws Exception{
        String caseName = "testChangeBrand";
        action = createAction(BINOLPLSCF15_Action.class, "/pl","BINOLPLSCF15_changeBrand");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_CHERRY_LANGUAGE,language);
        action.setSession(session);
        proxy.execute();
        
        List<Map<String, Object>> workflowInfoList = (List<Map<String, Object>>)JSONUtil.deserialize(response.getContentAsString());
                
        if(workflowInfoList.size()!=0){
            for(int i = 0 ;i<workflowInfoList.size();i++){
            	int configStatus = Integer.parseInt((String) workflowInfoList.get(i).get("configStatus"));
            	int key = Integer.parseInt((String) workflowInfoList.get(i).get("CodeKey"));
            	String value = (String) workflowInfoList.get(i).get("Value");
    	        	if(key==1){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("1",ConvertUtil.getString(key));
    	        	     assertEquals("产品发货流程",ConvertUtil.getString(value));
    	        	}else if(key==2){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("2",ConvertUtil.getString(key));
    	        	     assertEquals("产品盘点流程",ConvertUtil.getString(value));
    	        	}else if(key==3){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("3",ConvertUtil.getString(key));
    	                assertEquals("产品入库流程",ConvertUtil.getString(value));
    	        	}else if(key==4){
    	        		 assertEquals("0",ConvertUtil.getString(configStatus));
    	        	     assertEquals("4",ConvertUtil.getString(key));
    	        	     assertEquals("产品报损流程",ConvertUtil.getString(value));
    	        	}else if(key==6){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("6",ConvertUtil.getString(key));
    	                assertEquals("产品移库流程",ConvertUtil.getString(value));
    	        	}else if(key==7){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	                assertEquals("7",ConvertUtil.getString(key));
    	        		assertEquals("产品退库流程",ConvertUtil.getString(value));
    	        	}else if(key==8){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	        	    assertEquals("8",ConvertUtil.getString(key));
    	        		assertEquals("促销品发货流程",ConvertUtil.getString(value));
    	        	}else if(key==9){
    	        		assertEquals("0",ConvertUtil.getString(configStatus));
    	        	    assertEquals("9",ConvertUtil.getString(key));
    	        		assertEquals("促销品调拨流程",ConvertUtil.getString(value));
    	        	}
            	}
        }
    }
}
