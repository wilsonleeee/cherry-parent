package com.cherry.webservice.auth.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class UserAuthService extends BaseService {
	
	/**
	 * 取得数据源
	 * 
	 * @param loginName 用户名
	 * @return 数据源
	 */
	public String getDBByName (String loginName) {
		Map<String, Object> parameterMap  = new HashMap<String, Object>();
		parameterMap.put("loginName",loginName);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "UserAuth.getDBByName");
		return (String)baseConfServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得用户信息
	 * 
	 * @param loginName 用户名
	 * @return 用户信息
	 */
	public Map<String, Object> getUserSecurityInfo (String loginName) {
		Map<String, Object> parameterMap  = new HashMap<String, Object>();
		parameterMap.put("loginName",loginName);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "UserAuth.getUserSecurityInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}

    /**
     * 取得用户信息
     * 
     * @param map
     * @return 用户信息
     */
    public Map<String, Object> getUserInfo (Map<String,Object> map) {
        Map<String, Object> parameterMap  = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "UserAuth.getUserInfo");
        return (Map)baseServiceImpl.get(parameterMap);
    }
}
