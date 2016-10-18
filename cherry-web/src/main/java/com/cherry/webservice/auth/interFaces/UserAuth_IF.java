package com.cherry.webservice.auth.interFaces;

import java.util.Map;

public interface UserAuth_IF {
	
	/**
	 * 用户登录验证
	 * 
	 * @param map 查询条件
	 * @return 用户信息
	 */
	Map<String, Object> getAuthInfo(Map<String, Object> map);

    /**
     * 账号密码验证
     * 
     * @param map 查询条件
     * @return 用户信息
     */
    Map<String, Object> getUserInfo(Map<String, Object> map);
}
