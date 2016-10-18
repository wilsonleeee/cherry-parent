package com.cherry.lg.top.action;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jxl.Sheet;
import jxl.Workbook;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.OnlineUserList;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.lg.top.form.Top_Form;

public class TopAction_TEST extends CherryJunitBase{
    private TopAction action;
    
    private static String[][] userDataArrs = {
        {"TestUser1","127.0.0.1","2013-04-22 09:01:01","IE8"},
        {"TestUser1","127.0.0.2","2013-04-22 09:01:02","Firefox"},
        {"TestUser2","127.0.0.3","2013-04-22 09:01:03","Firefox"},
        {"TestUser3","127.0.0.4","2013-04-22 09:01:04","Chrome"},
        {"TestUser1","127.0.0.1","2013-04-22 09:01:05","Chrome"}
    };
    
    @AfterClass
    public static void afterClass() throws Exception {
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        Enumeration<UserInfo> en = onlineUserList.getOnlineUserList();
        Vector<UserInfo> v  = new Vector<UserInfo>(); 
        while(en.hasMoreElements()){
            v.add(en.nextElement());
        }
        Enumeration<UserInfo> cloneEn = v.elements();
        while(cloneEn.hasMoreElements()){
            onlineUserList.removeUser(cloneEn.nextElement());
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetOnlineUser1() throws Exception{
        action = createAction(TopAction.class, "/lg","TopAction_getOnlineUser");
        action.setSession(session);
        
        Top_Form form = action.getModel();
        int datatableLength = 3;
        int datatableStart = 1;
        form.setIDisplayLength(datatableLength);
        form.setIDisplayStart(datatableStart);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        MockHttpServletRequest request = new MockHttpServletRequest();
        for(int i=0;i<userDataArrs.length;i++){
            UserInfo tmpUserInfo = new UserInfo();
            tmpUserInfo.setLoginName(userDataArrs[i][0]);
            tmpUserInfo.setLoginIP(userDataArrs[i][1]);
            Date date = sdf.parse(userDataArrs[i][2]);
            tmpUserInfo.setLoginTime(date);
            tmpUserInfo.setUserAgent(userDataArrs[i][3]);
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("sameTimeLoginFlag",true);
            onlineUserList.addUser(tmpUserInfo,request.getSession(),paramMap);
        }
        
        assertEquals("popOnlineUser_1",action.getOnlineUser());
        List<Map<String,Object>> expectedList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<userDataArrs.length;i++){
            if(i>=datatableStart && i<datatableStart+datatableLength){
                Map<String,Object> userInfoMap = new HashMap<String,Object>();
                userInfoMap.put("LoginName",userDataArrs[i][0]);
                userInfoMap.put("LoginIP",userDataArrs[i][1]);
                userInfoMap.put("LoginTime",userDataArrs[i][2]);
                userInfoMap.put("UserAgent",userDataArrs[i][3]);
                expectedList.add(userInfoMap);
            }
        }
        assertEquals(expectedList.size(),form.getOnlineUserInfoList().size());
        for(int i=0;i<expectedList.size();i++){
            Map<String,Object> expectMap = expectedList.get(i);
            Map<String,Object> actualMap = form.getOnlineUserInfoList().get(i);
            assertEquals(expectMap.get("LoginName"),actualMap.get("LoginName"));
            assertEquals(expectMap.get("LoginIP"),actualMap.get("LoginIP"));
            assertEquals(expectMap.get("LoginTime"),actualMap.get("LoginTime"));
            assertEquals(expectMap.get("UserAgent"),actualMap.get("UserAgent"));
        }
    }
    
//    @Test //Maven Test时，本测试用例会被BINOLBSEMP01_Action_TEST的testExport1()影响，先注释
//    @Rollback(true)
//    @Transactional
//    public void testExport1() throws Exception{
//        action = createAction(TopAction.class, "/lg","TopAction_export");
//        Top_Form form = action.getModel();
//        UserInfo userInfo = new UserInfo();
//        userInfo.setBIN_OrganizationInfoID(-9999);
//        userInfo.setBIN_BrandInfoID(-9999);
//        userInfo.setLanguage("zh_CN");
//        setSession(CherryConstants.SESSION_USERINFO, userInfo);
//        action.setSession(session);
//        assertEquals("TopAction_excel",action.export());
//        try {
//            InputStream is = form.getExcelStream();
//            Workbook wb = null;
//            wb = Workbook.getWorkbook(is);
//            if (null != wb) {
//                Sheet[] sheets = wb.getSheets();
//                Sheet dataSheet = null;
//                dataSheet = sheets[0];
//                
//                assertEquals("在线用户列表",dataSheet.getName());
//                assertEquals(4,dataSheet.getColumns());
//                
//                assertEquals("登录帐号",dataSheet.getCell(0, 0).getContents());
//                assertEquals("登录IP",dataSheet.getCell(1, 0).getContents());
//                assertEquals("登录时间",dataSheet.getCell(2, 0).getContents());
//                assertEquals("浏览器版本",dataSheet.getCell(3, 0).getContents());
//                
//                for(int i=0;i<userDataArrs.length;i++){
//                    int index = i+1;
//                    assertEquals(userDataArrs[i][0],dataSheet.getCell(0, index).getContents());
//                    assertEquals(userDataArrs[i][1],dataSheet.getCell(1, index).getContents());
//                    assertEquals(userDataArrs[i][2],dataSheet.getCell(2, index).getContents());
//                    assertEquals(userDataArrs[i][3],dataSheet.getCell(3, index).getContents());
//                }
//            }
//        }catch(Exception e){
//            fail("导出Excel错误！"+e.getMessage());
//        }
//    }
}