/*  
 * @(#)OnlineUserBindingListener.java     1.0 2012/04/16      
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
package com.cherry.cm.core;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.cherry.cm.cmbeans.OnlineUserList;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.util.CherryUtil;

/**
 * 
 * OnlineUserBindingListener
 * 
 * @author niushunjie
 * @version 1.0 2012.04.16
 */
public class OnlineUserBindingListener implements HttpSessionBindingListener{
    private OnlineUserList onlineUserList = OnlineUserList.getInstance();
    
    UserInfo userInfo;
    HttpSession httpSession;
    Map<String,Object> paramMap;
    
    public OnlineUserBindingListener(UserInfo userInfo,HttpSession httpSession,Map<String,Object> paramMap){
        this.userInfo = userInfo;
        this.httpSession = httpSession;
        this.paramMap = paramMap;
    }
    
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        // 从在线列表中增加用户
        onlineUserList.addUser(this.userInfo,this.httpSession,this.paramMap);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        // 从在线列表中删除用户
        onlineUserList.removeUser(this.userInfo);
        CherryUtil.deleteTempFile(this.userInfo.getSessionID());
    }
}