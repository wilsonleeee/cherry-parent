/*  
 * @(#)OnlineUserList.java     1.0 2012/04/16      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.cmbeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;



/**
 * 
 * OnlineUserList 在线用户List类
 * 
 * @author niushunjie
 * @version 1.0 2012.04.16
 */
public class OnlineUserList {
    
    /**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(OnlineUserList.class);
    
    private static OnlineUserList instance = null;
    
    private Vector<UserInfo> v;
    
    //sessionMap的结构 key为登录名，value为（key为SessionID，value为Session的Map）
    private Hashtable<String,Object> sessionMap;
    
    //员工ID与员工登录名关系
    private Hashtable<String,Object> empIDLoginNameMap;
    
    // 以private的方式来声明构造方法,使得其他的类对象无法调用此类的构造函数
    private OnlineUserList(){
        v = new Vector<UserInfo>();
        sessionMap= new Hashtable<String,Object>();
        empIDLoginNameMap = new Hashtable<String,Object>();
    }
    
    public static synchronized OnlineUserList getInstance(){
        if (instance == null){
            instance = new OnlineUserList();
        }
        return instance;
    }
    
    /**
     * 消息List放在在线用户List里
     * @param msgParam
     */
    public void setMsgList(Map<String,Object> msgParam){
        Map<String,HttpSession> sessions = (Map<String, HttpSession>) sessionMap.get(msgParam.get("LoginName"));
        HttpSession httpSession = sessions.get(msgParam.get("SessionID"));
        if(httpSession == null){
            return;
        }
        List<Map<String,Object>> msgList = new ArrayList<Map<String,Object>>();
        try{
            msgList = (List<Map<String, Object>>) httpSession.getAttribute("msgList");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return;
        }

        String tradeType = ConvertUtil.getString(msgParam.get("TradeType"));
        int unReadCount = 0;//未读数
        Map<String, Object> newMessage = new HashMap<String, Object>();
        newMessage.put("MessageID", msgParam.get("MessageID"));
        newMessage.put("readType", "0");//0：未读，1：已读
        newMessage.put("time", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        if (tradeType.equals("exportMsg")) {
            String tempFilePath = ConvertUtil.getString(msgParam.get("tempFilePath"));
            if (!tempFilePath.equals("")) {
                String content = tempFilePath;

                // 判断重复生成的文件，只保留每个文件最新的一条消息
                for (int i = 0; i < msgList.size(); i++) {
                    if (msgList.get(i).get("content").equals(content)) {
                        msgList.remove(i);
                        i--;
                    }
                }

                newMessage.put("type", "export");//消息类别：1：导出完成 2：业务提示。（工作流）
                newMessage.put("content", content);
            }
        }else if(tradeType.equals("osworkflow")){
            newMessage.put("type", "osworkflow");//消息类别：1：导出完成 2：业务提示。（工作流）
            newMessage.put("BillID", msgParam.get("BillID"));
            newMessage.put("BillNo", msgParam.get("BillNo"));
            newMessage.put("OpCode", msgParam.get("OpCode"));
            newMessage.put("OpResult", msgParam.get("OpResult"));
            newMessage.put("OpenBillURL", msgParam.get("OpenBillURL"));
        }else if(tradeType.equals("PRT") || tradeType.equals("PRM") || tradeType.equals("DPRT") || tradeType.equals("ACT")){
            //实时业务结果反馈
            newMessage.put("type", tradeType);//消息类别：1：导出完成 2：业务提示。（工作流）
            String content = ConvertUtil.getString(msgParam.get("Content"));
            newMessage.put("content", content);
            String time = ConvertUtil.getString(msgParam.get("Time"));
            newMessage.put("time", time);
        }
        
        boolean duplicateFlag = false;
        String messageID = ConvertUtil.getString(msgParam.get("MessageID"));
        for(int i=0;i<msgList.size();i++){
            Map<String,Object> curMsg = msgList.get(i);
            //防止添加重复消息
            if(curMsg.get("MessageID").equals(messageID)){
                duplicateFlag = true;
            }
            if(ConvertUtil.getString(curMsg.get("readType")).equals("0")){
                unReadCount ++;
            }
        }
        if(!duplicateFlag){
            msgList.add(newMessage);
            unReadCount ++;
        }

        httpSession.setAttribute("msgList", msgList);
        httpSession.setAttribute("UnReadCount", unReadCount);
        msgParam.put("UnReadCount", unReadCount);
    }
    
    /**
     * 设置已读（1条/多条），返回未读数
     * @param param
     * @return
     */
    public int setMsgRead(Map<String,Object> param){
        String messageID = ConvertUtil.getString(param.get("MessageID"));
        String sessionID = ConvertUtil.getString(param.get("SessionID"));
        String loginName = ConvertUtil.getString(param.get("LoginName"));
        int unReadCount = 0;
        Map<String,HttpSession> sessions = (Map<String, HttpSession>) sessionMap.get(loginName);
        HttpSession httpSession = sessions.get(sessionID);
        List<Map<String,Object>> msgList = (List<Map<String, Object>>) httpSession.getAttribute("msgList");
        if(messageID.equals("ALL")){
            for(int i=0;i<msgList.size();i++){
                Map<String,Object> curMsg = msgList.get(i);
                curMsg.put("readType", "1");//已读
            }
        }else{
            for(int i=0;i<msgList.size();i++){
                Map<String,Object> curMsg = msgList.get(i);
                if(ConvertUtil.getString(curMsg.get("MessageID")).equals(messageID)){
                    curMsg.put("readType", "1");//已读
                }
                if(ConvertUtil.getString(curMsg.get("readType")).equals("0")){
                    unReadCount ++;
                }
            }
        }
        httpSession.setAttribute("UnReadCount", unReadCount);
        return unReadCount;
    }
    
    public List<Map<String,Object>> getMsgList(Map<String,Object> msgParam){
        Map<String,HttpSession> sessions = (Map<String, HttpSession>) sessionMap.get(msgParam.get("LoginName"));
        HttpSession httpSession = sessions.get(msgParam.get("SessionID"));
        List<Map<String,Object>> msgList = (List<Map<String, Object>>) httpSession.getAttribute("msgList");
        // 最新的未读消息排在最前，List 倒序
        Collections.sort(msgList,new MsgComparator());
        for(int i=0;i<msgList.size();i++){
            Map<String,Object> curMsg = msgList.get(i);
            curMsg.put("No", i+1);
        }
        return msgList;
    }
    
    /**
     * 加入在线用户列表
     * @param userInfo
     */
    public void addUser(UserInfo userInfo,HttpSession httpSession,Map<String,Object> paramMap){
        boolean sameTimeLoginFlag = (Boolean) paramMap.get("sameTimeLoginFlag");
        Map<String,Object> sessionsMap = (Map<String, Object>) sessionMap.get(userInfo.getLoginName());
        if(null == sessionsMap){
            sessionsMap = new HashMap<String,Object>();
        }
    	//判断用户是否已经存在登录信息
    	if(!sameTimeLoginFlag && sessionMap.containsKey(userInfo.getLoginName())){
    	    for(Map.Entry<String,Object> en:sessionsMap.entrySet()){
    	            Map<String,Object> map = new HashMap<String,Object>();
    	            map.put("TradeType", "kickUser");
    	            map.put("RemoteIP", userInfo.getLoginIP());
    	            map.put("SessionID", en.getKey());//sessionMap.get(userInfo.getLoginName()).getId());
    	            map.put("OrgCode", userInfo.getOrgCode());
    	            map.put("BrandCode", userInfo.getBrandCode());
    	            try {
    	                JQueryPubSubPush.push(map, "pushMsg", 0);
    	            } catch (InterruptedException e) {
    	                e.printStackTrace();
    	            }
    	            
//    	          这里销毁session会影响IE弹出，暂时注释。
//    	          HttpSession delSession = en.getValue();//sessionMap.get(userInfo.getLoginName());
//    	          delSession.invalidate();
    	    }
    	}
    	httpSession.setAttribute("msgList", new ArrayList<Map<String,Object>>());
    	httpSession.setAttribute("UnReadCount", 0);
    	if(sessionMap.contains(userInfo.getLoginName())){
    	    sessionsMap.put(httpSession.getId(), httpSession);
    	}else{
    	    sessionsMap.put(httpSession.getId(), httpSession);
    	    sessionMap.put(userInfo.getLoginName(), sessionsMap);
    	}
    	empIDLoginNameMap.put(ConvertUtil.getString(userInfo.getBIN_EmployeeID()), userInfo.getLoginName());
    	//sessionMap.put(userInfo.getLoginName(), httpSession);
        UserInfo ui = new UserInfo();
        ui.setLoginName(userInfo.getLoginName());
        ui.setLoginIP(userInfo.getLoginIP());
        ui.setLoginTime(userInfo.getLoginTime());
        ui.setUserAgent(userInfo.getUserAgent());
        v.addElement(ui);
        
        logger.error("当前在线用户总数："+instance.getUserCount());
    }
    
    /**
     * 在线用户列表中移除
     * @param userInfo
     */
    public void removeUser(UserInfo userInfo){
        Enumeration<UserInfo> en = v.elements();
        int i=0;
        while(en.hasMoreElements()){
            UserInfo curUserInfo = en.nextElement();
            //调用重写UserInfo的equals()方法判断是否一致。
            if (userInfo.equals(curUserInfo)) {
                v.remove(i);
                Map<String,Object> userSessions = (Map<String, Object>) sessionMap.get(userInfo.getLoginName());
                userSessions.remove(userInfo.getSessionID());
                break;
            }
            i++;
        }
    }
    
    /**
     * 返回在线用户列表
     * @return
     */
    public Enumeration<UserInfo> getOnlineUserList(){
        return v.elements();
    }
    
    /**
     * 返回在线用户列表分页
     * @param fromIndex
     * @param toIndex
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSubOnlineUserInfoList(int fromIndex,int toIndex,Map<String,Object> map){
        if(fromIndex < -1){
            fromIndex = 0;
        }
        if(toIndex > v.size()){
            toIndex = v.size();
        }
        //浏览器UA简写标志
        boolean userAgentLiteFlag = false;
        if(null != map.get("userAgentLiteFlag") && map.get("userAgentLiteFlag") instanceof Boolean){
            userAgentLiteFlag = (Boolean) map.get("userAgentLiteFlag");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        List<UserInfo> subList = v.subList(fromIndex, toIndex);
        List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<subList.size();i++){
            UserInfo userInfo = subList.get(i);
            Map<String,Object> userInfoMap = new HashMap<String,Object>();
            userInfoMap.put("No", fromIndex+i+1);
            userInfoMap.put("LoginName", userInfo.getLoginName());
            userInfoMap.put("LoginIP", userInfo.getLoginIP());
            userInfoMap.put("LoginTime", sdf.format(userInfo.getLoginTime()));
            userInfoMap.put("UserAgent", userInfo.getUserAgent());
            if(userAgentLiteFlag){
                userInfoMap.put("UserAgentLite",judgeBrowser(userInfo.getUserAgent()));
            }
            userList.add(userInfoMap);
        }
        return userList;
    }
    
    /**
     * 判断浏览器版本
     * @param userAgent
     * @return
     */
    private String judgeBrowser(String userAgent){
        userAgent = userAgent.toLowerCase();
        String browser = "";
        if(userAgent.indexOf("firefox")>-1){
            browser = "Firefox";
        }else if(userAgent.indexOf("opera")>-1){
            browser = "Opera";
        }else if(userAgent.indexOf("taobrowser")>-1){
            browser = "淘宝浏览器";
        }else if(userAgent.indexOf("maxthon")>-1){
            browser = "傲游浏览器";
        }else if(userAgent.indexOf("360se")>-1 || userAgent.indexOf("360ee")>-1){
            //360在极速模式、兼容模式下缺少自己的标识符，无法识别。
            browser = "360浏览器";
        }else if(userAgent.indexOf("qqbrowser")>-1){
            browser = "QQ浏览器";
        }else if(userAgent.indexOf("baidubrowser")>-1 || userAgent.indexOf("bidubrowser")>-1){
            browser = "百度浏览器";
        }else if(userAgent.indexOf("metasr")>-1){
            browser = "搜狗浏览器";
        }else if(userAgent.indexOf("lbbrowser")>-1){
            browser = "猎豹浏览器";
        }else if(userAgent.indexOf("theworld")>-1){
            browser = "世界之窗浏览器";
        }else if(userAgent.indexOf("msie")>-1){ 
            if(userAgent.indexOf("msie 6.0")>-1){
                //IE 6.0
                browser = "IE6";
            }else if(userAgent.indexOf("msie 7.0")>-1){
                //IE 7.0
                browser = "IE7";
            }else if(userAgent.indexOf("msie 8.0")>-1){
                //IE 8.0
                browser = "IE8";
            }else if(userAgent.indexOf("msie 9.0")>-1){
                //IE 9.0
                browser = "IE9";
            }else if(userAgent.indexOf("msie 10.0")>-1){
                //IE 10.0   
                browser = "IE10";
            }
        }else if(userAgent.indexOf("rv:11.0")>-1 && userAgent.indexOf("trident")>-1){
            //IE 11.0   
            browser = "IE11";
        }else if(userAgent.indexOf("chrome")>-1){
            browser = "Chrome";
        }else if(userAgent.indexOf("safari")>-1){
            browser = "Safari";
        }else{
            browser = "Unknown";
        }
        return browser;
    }
    
    /**
     * 返回在线用户数量
     * @return
     */
    public int getUserCount(){
        return v.size();
    }
    
    /**
     * 取用户在用户列表里的session
     * @return
     */
    public Map<String,HttpSession> getUser(String loginName){
        Map<String,HttpSession> sessions = (Map<String, HttpSession>) sessionMap.get(loginName);
        return sessions;
    }
    
    /**
     * 根据员工ID取员工登录名
     * @return
     */
    public String getEmpLoginName(String employeeID){
        String loginName= "";
        if(null != empIDLoginNameMap){
            loginName= ConvertUtil.getString(empIDLoginNameMap.get(employeeID));
        }
        return loginName;
    }
    
    /**
     * List排序（按readType，time排序）
     */
    public static class MsgComparator implements Comparator<Map<String, Object>> {

        public MsgComparator() {
            super();
        }

        @Override
        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            //排序方式（先按是否已读倒序，相同时按时间倒序）
            int readType1 = CherryUtil.obj2int(map1.get("readType"));
            int readType2 = CherryUtil.obj2int(map2.get("readType"));
            Date d1 = DateUtil.coverString2Date(ConvertUtil.getString(map1.get("time")));
            Date d2 = DateUtil.coverString2Date(ConvertUtil.getString(map2.get("time")));
            if (readType2 < readType1) {
                return 1;
            } else if (readType2 == readType1) {
                return d2.compareTo(d1);
            }else{
                return 0;
            }
        }
    }
}
