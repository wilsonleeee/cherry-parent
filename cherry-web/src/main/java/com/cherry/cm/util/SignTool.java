package com.cherry.cm.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TmallMeiCrmCallbackPointChangeRequest;
import com.taobao.api.request.TmallMeiCrmMemberSyncRequest;
import com.taobao.api.response.TmallMeiCrmCallbackPointChangeResponse;
import com.taobao.api.response.TmallMeiCrmMemberSyncResponse;

/**
 * Created by 尘东 on 2015/3/13.
 */
public class SignTool {
	
	public static String ACTION_QUERY = "query";
	public static String ACTION_BIND = "bind";
	public static String ACTION_REGISTER = "register";
	public static String ACTION_MEMQUERY = "memQuery";
	
//	public static String appKey = "23192212";//你的沙箱环境APPKEY
//	public static String appSecret = "3123b58bf5ae9a2b4eb5d6f99b03b35b";//你的沙箱环境AppSecret
	public static String url = "ws://mc.api.taobao.com/";
//	public static String sessionKey = "61007047d37b1f6df7c4140968c71488174263cabb8cb14698461607";
	public static String serverUrl = "http://gw.api.taobao.com/router/rest";//无须改动
//	
//	public static String mixKey = "F4rgLk";
	
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(SignTool.class);

    public static String getBody(HttpServletRequest request){
        return getBody(request,"utf-8");
    }

    public static String getBody(HttpServletRequest request,String encode){
        try {
            InputStream inputStream = request.getInputStream();
            return IOUtils.toString(inputStream, encode);
        }catch (Exception e){
            throw new java.lang.RuntimeException(e);
        }
    }

    public static boolean checkSign(HttpServletRequest request,String body, String secret){
        return checkSign(request,body,secret,"utf-8");
    }

    public static boolean checkSign(HttpServletRequest request,String body, String secret,String encode) {

        if(request == null || secret == null){
            return false;
        }

        Map<String,String> headMap = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        do {
            String headName =(String) headerNames.nextElement();
            headMap.put(headName, decode(request.getHeader(headName),encode));
        }while (headerNames.hasMoreElements());

        Map<String,String> queryMap = new HashMap<String, String>();

        Enumeration parameterNames = request.getParameterNames();
        do{
            String parameterName =(String) parameterNames.nextElement();
            if(!"sign".equals(parameterName)){
                queryMap.put(parameterName,decode(request.getParameter(parameterName),encode));
            }
        }while (parameterNames.hasMoreElements());

        try {
            String sign = EncryptUtil.sign(headMap, queryMap, body, secret, encode);
            if(sign.equals(request.getParameter("sign"))){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("do sign exception:",e);
            return false;
        }
    }

    private static String decode(String orgi,String encode){
        if(orgi == null){
            return null;
        }
        try {
            return URLDecoder.decode(orgi,encode);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    
    public static TmallMeiCrmCallbackPointChangeResponse pointChangeResponse(
    		TmallMeiCrmCallbackPointChangeRequest req, String appKey, String appSecret, String sessionKey) throws ApiException {
    	TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		TmallMeiCrmCallbackPointChangeResponse response = client.execute(req ,
		sessionKey);
		return response;
    }
    
    public static TmallMeiCrmMemberSyncResponse syncResponse(TmallMeiCrmMemberSyncRequest req, String appKey, String appSecret, String sessionKey) throws ApiException {
    	TaobaoClient client=new DefaultTaobaoClient(serverUrl, appKey, appSecret);
    	TmallMeiCrmMemberSyncResponse response = client.execute(req , sessionKey);
    	return response;
    }
}
