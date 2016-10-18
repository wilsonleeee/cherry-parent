/*      
 * @(#)JQueryPubSubPush.java     1.0 2013/10/15      
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
package com.cherry.mq.mes.atmosphere;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.OnlineUserList;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 推送消息共通
 * 
 * @author niushunjie
 * @version 1.0 2013.10.15
 */
public class JQueryPubSubPush {
	
	private static final Logger logger = LoggerFactory.getLogger(JQueryPubSubPush.class);
	
    /**
     * 推送消息
     * @param msgParam
     * @param broadCasterName
     * @param sleepTime
     * @throws InterruptedException
     */
    public static void push(Map<String,Object> msgParam,String broadCasterName,long sleepTime) throws InterruptedException{
        PushThread pt = new PushThread(msgParam,broadCasterName,sleepTime);
        Thread t = new Thread(pt);
        t.start();
    }
    
    /**
     * 推送消息
     * @param msgParam
     * @param broadCasterName
     * @param sleepTime
     */
    public static void pushMsg(Map<String,Object> msgParam,String broadCasterName,long sleepTime) {
    	try {
			Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(Broadcaster.class, broadCasterName);
			if(broadcaster != null) {
                msgParam.put("MessageID", UUID.randomUUID().toString().replace("-", ""));
                
				//先写入session，后推送。
				String tradeType = ConvertUtil.getString(msgParam.get("TradeType"));
				if(tradeType.equals("exportMsg")){
                    //消息加入在线用户的session里。
                    OnlineUserList onlineUserList = OnlineUserList.getInstance();
                    msgParam.put("LoginName", msgParam.get("LoginName"));
                    onlineUserList.setMsgList(msgParam);
				}else if(tradeType.equals("osworkflow")){
                    Map<String,Object> employeeIDMap =  (Map<String, Object>) msgParam.get("EmployeeIDMap");
                    for(Map.Entry<String,Object> en:employeeIDMap.entrySet()){
                        //消息加入在线用户的session里。
                        OnlineUserList onlineUserList = OnlineUserList.getInstance();
                        String loginName = ConvertUtil.getString(en.getValue());
                        Map<String,HttpSession> sessions = onlineUserList.getUser(loginName);
                        if(null != sessions){
                            for(Map.Entry<String,HttpSession> curSession:sessions.entrySet()){
                                msgParam.put("LoginName", loginName);
                                msgParam.put("SessionID", curSession.getKey());
                                onlineUserList.setMsgList(msgParam);
                            }
                        }
                    }
				}else if(tradeType.equals("PRT") || tradeType.equals("PRM") || tradeType.equals("DPRT") || tradeType.equals("ACT")){
				    //实时业务结果反馈
				    //员工ID数组
                    Integer[] toUserArr = (Integer[]) msgParam.get("ToUser");
                    Map<String,Object> employeeIDMap = new HashMap<String,Object>();
                    for(int i=0;i<toUserArr.length;i++){
                        //消息加入在线用户的session里。
                        OnlineUserList onlineUserList = OnlineUserList.getInstance();
                        String employeeID = ConvertUtil.getString(toUserArr[i]);
                        String loginName = onlineUserList.getEmpLoginName(employeeID);
                        employeeIDMap.put(employeeID, loginName);
                        Map<String,HttpSession> sessions = onlineUserList.getUser(loginName);
                        if(null != sessions){
                            for(Map.Entry<String,HttpSession> curSession:sessions.entrySet()){
                                msgParam.put("LoginName", loginName);
                                msgParam.put("SessionID", curSession.getKey());
                                onlineUserList.setMsgList(msgParam);
                            }
                        }
                    }
                    msgParam.put("EmployeeIDMap", employeeIDMap);
				}
				
				broadcaster.broadcast(JSONUtil.serialize(msgParam));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
}

class PushThread implements Runnable {
    /** 消息参数 */
    private Map<String,Object> msgParam;
    
    /** 广播推送名 */
    private String broadCasterName;
    
    /** 延迟时间 */
    long sleepTime;
    
    public PushThread(Map<String,Object> msgParam,String broadCasterName,long sleepTime) { 
        this.msgParam = msgParam;
        this.broadCasterName = broadCasterName;
        this.sleepTime = sleepTime;
    }

    public void run() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(Broadcaster.class, broadCasterName);
        if (broadcaster != null) {
            try {
                broadcaster.broadcast(JSONUtil.serialize(msgParam));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}