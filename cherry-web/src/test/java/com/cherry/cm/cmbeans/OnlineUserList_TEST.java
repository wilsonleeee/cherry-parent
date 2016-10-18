package com.cherry.cm.cmbeans;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * 在线用户单例模式测试
 * @author niushunjie
 *
 */
public class OnlineUserList_TEST {

    private static String[][] dataArrs = {
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
    public void testAddUser() throws ParseException{
        assertEquals(0,OnlineUserList.getInstance().getUserCount());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        MockHttpServletRequest request = new MockHttpServletRequest();
        for(int i=0;i<dataArrs.length;i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setLoginName(dataArrs[i][0]);
            userInfo.setLoginIP(dataArrs[i][1]);
            Date date = sdf.parse(dataArrs[i][2]);
            userInfo.setLoginTime(date);
            userInfo.setUserAgent(dataArrs[i][3]);
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("sameTimeLoginFlag",true);
            onlineUserList.addUser(userInfo,request.getSession(),paramMap);
        }

        assertEquals(5,OnlineUserList.getInstance().getUserCount());
        
        Vector<UserInfo> expectVector = new Vector<UserInfo>();
        for(int i=0;i<dataArrs.length;i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setLoginName(dataArrs[i][0]);
            userInfo.setLoginIP(dataArrs[i][1]);
            Date date = sdf.parse(dataArrs[i][2]);
            userInfo.setLoginTime(date);
            userInfo.setUserAgent(dataArrs[i][3]);
            expectVector.addElement(userInfo);
        }
        
        int i=0;
        Enumeration<UserInfo> en = onlineUserList.getOnlineUserList();
        while(en.hasMoreElements()){
            assertTrue(expectVector.elementAt(i).equals(en.nextElement()));
            i++;
        }
    }
    
    @Test
    public void testRemoveUser() throws ParseException{
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        assertEquals(5,OnlineUserList.getInstance().getUserCount());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        
        int[] removeIndex = {0,2};
        
        for(int i=0;i<dataArrs.length;i++){
            if(i == removeIndex[0] || i == removeIndex[1]){
                UserInfo userInfo = new UserInfo();
                userInfo.setLoginName(dataArrs[i][0]);
                userInfo.setLoginIP(dataArrs[i][1]);
                Date date = sdf.parse(dataArrs[i][2]);
                userInfo.setLoginTime(date);
                userInfo.setUserAgent(dataArrs[i][3]);
                onlineUserList.removeUser(userInfo);
            }
        }
        
        Vector<UserInfo> expectVector = new Vector<UserInfo>();
        for(int i=0;i<dataArrs.length;i++){
            if(i != removeIndex[0] && i != removeIndex[1]){
                UserInfo userInfo = new UserInfo();
                userInfo.setLoginName(dataArrs[i][0]);
                userInfo.setLoginIP(dataArrs[i][1]);
                Date date = sdf.parse(dataArrs[i][2]);
                userInfo.setLoginTime(date);
                userInfo.setUserAgent(dataArrs[i][3]);
                expectVector.addElement(userInfo);
            }
        }
        
        int i=0;
        Enumeration<UserInfo> en = onlineUserList.getOnlineUserList();
        while(en.hasMoreElements()){
            assertTrue(expectVector.elementAt(i).equals(en.nextElement()));
            i++;
        }
    }
    
    @Test
    public void testGetSubOnlineUserInfoList1(){
        OnlineUserList onlineUserList = OnlineUserList.getInstance();

        int fromIndex = 0;
        int toIndex = onlineUserList.getUserCount();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("userAgentLiteFlag", true);
        List<Map<String,Object>> subList = onlineUserList.getSubOnlineUserInfoList(fromIndex, toIndex, param);
        
        List<Map<String,Object>> expectedList= new ArrayList<Map<String,Object>>();
        int[] removeIndex = {0,2};
        for(int i=0;i<dataArrs.length;i++){
            if(i != removeIndex[0] && i != removeIndex[1]){
                Map<String,Object> userInfoMap = new HashMap<String,Object>();
                userInfoMap.put("LoginName",dataArrs[i][0]);
                userInfoMap.put("LoginIP",dataArrs[i][1]);
                userInfoMap.put("LoginTime",dataArrs[i][2]);
                userInfoMap.put("UserAgent",dataArrs[i][3]);
                expectedList.add(userInfoMap);
            }
        }
        
        assertEquals(expectedList.size(),subList.size());
        for(int i=0;i<expectedList.size();i++){
            Map<String,Object> expectMap = expectedList.get(i);
            Map<String,Object> actualMap = subList.get(i);
            assertEquals(expectMap.get("LoginName"),actualMap.get("LoginName"));
            assertEquals(expectMap.get("LoginIP"),actualMap.get("LoginIP"));
            assertEquals(expectMap.get("LoginTime"),actualMap.get("LoginTime"));
            assertEquals(expectMap.get("UserAgent"),actualMap.get("UserAgent"));
        }
    }
    
    @Test
    public void testGetSubOnlineUserInfoList2(){
        OnlineUserList onlineUserList = OnlineUserList.getInstance();

        int fromIndex = 0;
        int toIndex = 1;
        Map<String,Object> param = new HashMap<String,Object>();
        //param.put("userAgentLiteFlag", false);
        List<Map<String,Object>> subList = onlineUserList.getSubOnlineUserInfoList(fromIndex, toIndex, param);
        
        List<Map<String,Object>> expectList= new ArrayList<Map<String,Object>>();
        for(int i=0;i<dataArrs.length;i++){
            if(i == 1){
                Map<String,Object> userInfoMap = new HashMap<String,Object>();
                userInfoMap.put("LoginName",dataArrs[i][0]);
                userInfoMap.put("LoginIP",dataArrs[i][1]);
                userInfoMap.put("LoginTime",dataArrs[i][2]);
                userInfoMap.put("UserAgent",dataArrs[i][3]);
                expectList.add(userInfoMap);
            }
        }
        
        assertEquals(expectList.size(),subList.size());
        for(int i=0;i<expectList.size();i++){
            Map<String,Object> expectMap = expectList.get(i);
            Map<String,Object> actualMap = subList.get(i);
            assertEquals(expectMap.get("LoginName"),actualMap.get("LoginName"));
            assertEquals(expectMap.get("LoginIP"),actualMap.get("LoginIP"));
            assertEquals(expectMap.get("LoginTime"),actualMap.get("LoginTime"));
            assertEquals(expectMap.get("UserAgent"),actualMap.get("UserAgent"));
        }
    }
    
    @Test
    public void testGetUserCount(){
        OnlineUserList onlineUserList1 = OnlineUserList.getInstance();
        assertEquals(3,onlineUserList1.getUserCount());
    }
}