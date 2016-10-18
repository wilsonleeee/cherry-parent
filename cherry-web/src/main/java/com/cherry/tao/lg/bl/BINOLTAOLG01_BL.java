package com.cherry.tao.lg.bl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.ConvertUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.internal.util.WebUtils;
//import com.taobao.api.request.UserSellerGetRequest;
//import com.taobao.api.response.UserSellerGetResponse;

public class BINOLTAOLG01_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLTAOLG01_BL.class.getName());
	
	public String getAccessToken(String code) {
		if(code == null || "".equals(code)) {
			return "";
		}
		// 沙箱环境
//		String appKey="1023032180";
//	    String appSecret="sandbox5aec96c5562b6319425d286ea";
//	    String serverUrl="https://oauth.tbsandbox.com/token";
		
	    // 正式环境
	    String appKey="23034260";
	    String appSecret="740b45fc42265159d148f3196289f0ad";
		String serverUrl="https://oauth.taobao.com/token";
	    
	    Map<String,String> props=new HashMap<String,String>();
	    props.put("grant_type","authorization_code");
	    props.put("code",code);
	    props.put("client_id",appKey);
	    props.put("client_secret",appSecret);
	    props.put("redirect_uri","http://121.41.160.246");
	    props.put("view","web");
	    String s="";
	    try {
	    	WebUtils.setIgnoreSSLCheck(true);
	    	s=WebUtils.doPost(serverUrl, props, 30000, 30000);
	    } catch (Exception e) {
	    	logger.error(e.getMessage(), e);
		}
	    return s;
	}
	
	public String login(String code) {
		String accessToken = getAccessToken(code);
		if(accessToken != null && !"".equals(accessToken)) {
			Map<String, Object> atMap = ConvertUtil.json2Map(accessToken);
			String loginName = (String)atMap.get("taobao_user_nick");
			try {
				loginName = URLDecoder.decode(loginName, "UTF-8");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return loginName;
		} else {
			return null;
		}
	}
	
	/**
     * @param args
     * @throws ApiException 
     */
    public static void main(String[] args) throws ApiException {
        String appKey="1023032180";
        String appSecret="sandbox5aec96c5562b6319425d286ea";
        String serverUrl = "http://gw.api.tbsandbox.com/router/rest";
        /**创建client**/
        DefaultTaobaoClient client = new DefaultTaobaoClient(serverUrl , appKey , appSecret, "json");
//        UserSellerGetRequest req = new UserSellerGetRequest();//实例化具体API对应的Request类
//        /**设置API业务入参**/
//        req.setFields("nick,email");
//        UserSellerGetResponse resp = client.execute(req );
//        /**正常请求，获取用户信息，由于email是需要用户授权才能获取，因此返回的信息中不包含emaill信息**/
//        System.out.println(resp.getBody());
        
        /**传入用户授权的sessionkey， 可获取用户 的email**/
//        UserSellerGetResponse resp = client.execute(req, "6100a19859052e9b11dff124a53145e1bfe5c9cc4e0a034182558410");
//        System.out.println(resp.getBody());
        
        /**传入不存在的nick ，对错误进行处理****/
        //req.setNick("sandbox_nouser");
        //resp = client.execute(req);
        
//        if(resp.isSuccess()) {
//            System.out.println(resp.getBody());
//        } else {
//            /**如果subCode 以isp开头，可重试，否则是由于业务错误，请不要重试。***/
//            if(resp.getSubCode() != null && resp.getSubCode().startsWith("isp"))
//                resp = client.execute(req);
//            else 
//                System.out.println(resp.getBody());
//        }
    }

}
