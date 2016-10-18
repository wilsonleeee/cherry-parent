package com.cherry.lg.lgn.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;

public class LoginAction_TEST extends CherryJunitBase{
    private LoginAction action;
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    public void testDoLogout1() throws Exception{
        String caseName = "testDoLogout1";
        action = createAction(LoginAction.class, "/","logout");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        HttpServletRequest hsr = ServletActionContext.getRequest();
        action.setServletRequest(hsr);
        assertEquals("success",action.doLogout());
        assertEquals(false,request.getSession().getAttributeNames().hasMoreElements());
    }
}
