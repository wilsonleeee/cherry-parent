package com.cherry.webservice.auth.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.auth.interFaces.UserAuth_IF;
import com.cherry.webservice.auth.service.UserAuthService;


public class UserAuthLogic implements UserAuth_IF {
	
	private static Logger logger = LoggerFactory.getLogger(UserAuthLogic.class.getName());
	
	@Resource
	private UserAuthService userAuthService;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * 用户登录验证
	 * 
	 * @param map 查询条件
	 * @return 用户信息
	 */
	@Override
	public Map<String, Object> getAuthInfo(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> userInfo = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		String loginName = (String)map.get("LoginName");
		String passWord = (String)map.get("PassWord");
		String organizationInfoID = String.valueOf(map.get("BIN_OrganizationInfoID"));
		String brandInfoID = String.valueOf(map.get("BIN_BrandInfoID"));
		String source = (String)map.get("Source");
		
		// 判断来源是否合法
		String validSource = binOLCM14_BL.getConfigValue("1385", organizationInfoID, brandInfoID);
		if(validSource != null && !"".equals(validSource)) {
			boolean validFlag = false;
			if(source != null && !"".equals(source)) {
				String[] validSources = validSource.split(",");
				for(int i = 0; i < validSources.length; i++) {
					if(source.equals(validSources[i])) {
						validFlag = true;
						break;
					}
				}
			}
			if(!validFlag) {
				retMap.put("ERRORCODE", "WSE0009");
				retMap.put("ERRORMSG", "不合法的登陆");
				return retMap;
			}
		}
		
		try {
			Map<String, Object> userMap = userAuthService.getUserSecurityInfo(loginName);
			if(userMap == null) {
				retMap.put("ERRORCODE", "WSE0007");
				retMap.put("ERRORMSG", "用户名不存在");
				return retMap;
			}
			
			String _passWord = (String)userMap.get("passWord");
			// 解密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			_passWord = des.decrypt(_passWord);
			if(passWord != null && passWord.equals(_passWord)) {
				userInfo.put("basCode", userMap.get("employeeCode"));
				userInfo.put("basName", userMap.get("employeeName"));
			} else {
				retMap.put("ERRORCODE", "WSE0008");
				retMap.put("ERRORMSG", "密码不正确");
				return retMap;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}
		
    	retMap.put("ResultMap", userInfo);
		return retMap;
	}

    /**
     * 账号密码验证
     * 
     * @param map 查询条件
     * @return 用户信息
     */
    @Override
    public Map<String, Object> getUserInfo(Map<String, Object> map) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Map<String, Object> userInfo = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
        paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
        String loginName = ConvertUtil.getString(map.get("UserName"));
        String passWord = ConvertUtil.getString(map.get("PassWord"));
        //SubType 子类型 不填或者 NNP：依靠账号和密码校验，NO:仅根据账号校验，此时会忽略密码。
        String subType = ConvertUtil.getString(map.get("SubType"));
        
        try {
            paramMap.put("loginName", loginName);
            Map<String, Object> userMap = userAuthService.getUserInfo(paramMap);
            if(userMap == null) {
                retMap.put("ERRORCODE", "WSE0007");
                retMap.put("ERRORMSG", "用户名不存在");
                return retMap;
            }
            
            boolean returnResultFlag = false;
            if(subType.equals("") || subType.equals("NNP")){
                String _passWord = ConvertUtil.getString(userMap.get("PassWord"));
                // 解密处理
                DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
                _passWord = des.decrypt(_passWord);
                if(passWord != null && passWord.equals(_passWord)) {
                    returnResultFlag = true;
                } else {
                    retMap.put("ERRORCODE", "WSE0008");
                    retMap.put("ERRORMSG", "密码不正确");
                    return retMap;
                }
            }else if(subType.equals("NO")){
                returnResultFlag = true;
            }
            
            if(returnResultFlag){
                userInfo.put("EmployeeCode", ConvertUtil.getString(userMap.get("EmployeeCode")));
                userInfo.put("EmployeeName", ConvertUtil.getString(userMap.get("EmployeeName")));
                userInfo.put("DepartType", ConvertUtil.getString(userMap.get("Type")));
                userInfo.put("DepartCode", ConvertUtil.getString(userMap.get("DepartCode")));
                userInfo.put("DepartName", ConvertUtil.getString(userMap.get("DepartName")));
                userInfo.put("CategoryCode", ConvertUtil.getString(userMap.get("CategoryCode")));
                userInfo.put("CategoryName", ConvertUtil.getString(userMap.get("CategoryName")));
                userInfo.put("CategoryGrade", ConvertUtil.getString(userMap.get("CategoryGrade")));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常");
            return retMap;
        }
        
        retMap.put("ResultMap", userInfo);
        return retMap;
    }
}
