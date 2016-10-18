package com.cherry.pl.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

 
import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLPLCOM03_Action_TEST extends CherryJunitBase{
    
    private BINOLPLCOM03_Action action;
    
    private CodeTable code;
    
    @Before
    public void setUp() throws Exception {

    }
    
    @After
    public void tearDown() throws Exception {

    }

    @SuppressWarnings("unchecked")
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception{
        String caseName = "testInit1";
        action = createAction(BINOLPLCOM03_Action.class, "/pl","BINOLPLCOM03_init");
        code = (CodeTable) applicationContext.getBean("CodeTable");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_CHERRY_LANGUAGE,language);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.setSession(session);
        assertEquals("success",proxy.execute());
        
        List<Map<String,Object>> codeList = code.getCodes("1154");
        if(null != codeList && codeList.size()>0){
            Map<String,Object> keyValue = new HashMap<String,Object>();
            for(int i = 0 ;i<codeList.size();i++){
                String key = ConvertUtil.getString(codeList.get(i).get("CodeKey"));
                String value = ConvertUtil.getString(codeList.get(i).get("Value"));
                keyValue.put(key,value);
            }
            assertEquals("产品发货流程",keyValue.get("1"));
            assertEquals("产品盘点流程（纯后台业务）",keyValue.get("2"));
            assertEquals("产品入库流程",keyValue.get("3"));
            assertEquals("产品报损流程（纯后台业务）",keyValue.get("4"));
            assertEquals("产品移库流程",keyValue.get("6"));
            assertEquals("产品退库流程",keyValue.get("7"));
            assertEquals("促销品发货流程",keyValue.get("8"));
            assertEquals("促销品调拨流程（纯后台业务）",keyValue.get("9"));
            assertEquals("产品订货流程",keyValue.get("10"));
            assertEquals("产品退库申请流程",keyValue.get("11"));
            assertEquals("产品盘点申请流程",keyValue.get("12"));
            assertEquals("促销品移库流程（纯后台业务）",keyValue.get("13"));
            assertEquals("产品调拨流程（纯后台业务）",keyValue.get("14"));
        }
        
        List<Map<String,Object>> ruleList = action.getModel().getRuleList();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        assertEquals("生成发货单",ruleList.get(0).get("stepName"));
        assertEquals("4",ruleList.get(0).get("RuleType"));
        list = (List) ruleList.get(0).get("RuleContext");
        assertEquals(0,list.size());
        assertEquals("审核",ruleList.get(1).get("stepName"));
        assertEquals("3",ruleList.get(1).get("RuleType"));
        list = (List) ruleList.get(1).get("RuleContext");
        assertEquals(0,list.size());
        assertEquals("发货",ruleList.get(2).get("stepName"));
        assertEquals("2",ruleList.get(2).get("RuleType"));
        list = (List) ruleList.get(2).get("RuleContext");
        assertEquals(0,list.size());
        assertEquals("收货",ruleList.get(3).get("stepName"));
        assertEquals("2",ruleList.get(3).get("RuleType"));
        list = (List) ruleList.get(3).get("RuleContext");
        assertEquals(0,list.size());
    }
}
