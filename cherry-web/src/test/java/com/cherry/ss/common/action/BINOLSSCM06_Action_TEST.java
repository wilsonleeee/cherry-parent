package com.cherry.ss.common.action;

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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.common.form.BINOLSSCM06_Form;

public class BINOLSSCM06_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testCOM_Service;
	
	private BINOLSSCM06_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
	public void testGetDeliver1() throws Exception {
        String caseName = "testGetDeliver1";
        action = createAction(BINOLSSCM06_Action.class, "/ss","BINOLSSCM06_getDeliver");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("SQLList");
        String sql1 = ConvertUtil.getString(sqlList.get(0).get("sql1"));
        String sql2 = ConvertUtil.getString(sqlList.get(0).get("sql2"));
        String sql3 = ConvertUtil.getString(sqlList.get(0).get("sql3"));
        sql1 = sql1.replaceAll("#TestType#", "0");
        List<Map<String,Object>> tempList = testCOM_Service.select(sql1);
        if(null != tempList && tempList.size()>0){
            int organizationID= CherryUtil.obj2int(tempList.get(0).get("BIN_OrganizationID"));
            sql2 = sql2.replaceAll("#BIN_OrganizationID#", ConvertUtil.getString(organizationID));
            tempList = testCOM_Service.select(sql2);
            if(null != tempList && tempList.size()>0){
                int userID = CherryUtil.obj2int(tempList.get(0).get("BIN_UserID"));
                sql3 = sql3.replaceAll("#BIN_OrganizationID#", ConvertUtil.getString(organizationID));
                tempList = testCOM_Service.select(sql3);
                if(null != tempList && tempList.size()>0){
                    String billNoIF = ConvertUtil.getString(tempList.get(0).get("DeliverReceiveNoIF"));
                    
                    userInfo.setBIN_UserID(userID);
                    setSession(CherryConstants.SESSION_USERINFO, userInfo);
                    //设置语言
                    String language = userInfo.getLanguage();
                    setSession(CherryConstants.SESSION_LANGUAGE,language);
                    
                    BINOLSSCM06_Form form = action.getModel();
                    DataUtil.getForm(this.getClass(), caseName, form);
                    form.setSSearch(billNoIF);
                    
                    request.setParameter("organizationId", ConvertUtil.getString(organizationID));
                    action.setServletRequest(request);
                    
                    //proxy代理
                    assertEquals("BINOLSSCM06_1", proxy.execute());
                }
            }
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetDeliver2() throws Exception {
        String caseName = "testGetDeliver2";
        action = createAction(BINOLSSCM06_Action.class, "/ss","BINOLSSCM06_getDeliver");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("SQLList");
        String sql1 = ConvertUtil.getString(sqlList.get(0).get("sql1"));
        String sql2 = ConvertUtil.getString(sqlList.get(0).get("sql2"));
        String sql3 = ConvertUtil.getString(sqlList.get(0).get("sql3"));
        sql1 = sql1.replaceAll("#TestType#", "1");
        List<Map<String,Object>> tempList = testCOM_Service.select(sql1);
        if(null != tempList && tempList.size()>0){
            int organizationID= CherryUtil.obj2int(tempList.get(0).get("BIN_OrganizationID"));
            sql2 = sql2.replaceAll("#BIN_OrganizationID#", ConvertUtil.getString(organizationID));
            tempList = testCOM_Service.select(sql2);
            if(null != tempList && tempList.size()>0){
                int userID = CherryUtil.obj2int(tempList.get(0).get("BIN_UserID"));
                sql3 = sql3.replaceAll("#BIN_OrganizationID#", ConvertUtil.getString(organizationID));
                tempList = testCOM_Service.select(sql3);
                if(null != tempList && tempList.size()>0){
                    String billNoIF = ConvertUtil.getString(tempList.get(0).get("DeliverReceiveNoIF"));
                    
                    userInfo.setBIN_UserID(userID);
                    setSession(CherryConstants.SESSION_USERINFO, userInfo);
                    //设置语言
                    String language = userInfo.getLanguage();
                    setSession(CherryConstants.SESSION_LANGUAGE,language);
                    
                    BINOLSSCM06_Form form = action.getModel();
                    DataUtil.getForm(this.getClass(), caseName, form);
                    form.setSSearch(billNoIF);
                    
                    request.setParameter("organizationId", ConvertUtil.getString(organizationID));
                    action.setServletRequest(request);
                    
                    //proxy代理
                    assertEquals("BINOLSSCM06_1", proxy.execute());
                }
            }
        }
    }
}