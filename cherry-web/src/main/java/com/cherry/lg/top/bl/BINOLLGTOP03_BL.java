/*
 * @(#)BINOLLGTOP03_BL.java     1.0 2011/02/22
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
package com.cherry.lg.top.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.emp.bl.BINOLBSEMP04_BL;
import com.cherry.bs.emp.service.BINOLBSEMP03_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.lg.top.service.BINOLLGTOP03_Service;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;




public class BINOLLGTOP03_BL {
	@Resource
	private BINOLLGTOP03_Service binollgtop03_Service;
	
	@Resource(name="binOLBSEMP04_BL")
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	@Resource
	private CodeTable code;
	
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	@Resource(name="binOLPLUPM04_BL")
	private BINOLPLUPM04_BL binOLPLUPM04_BL;

	@Resource(name="binOLBSEMP03_Service")
	private BINOLBSEMP03_Service binolbsemp03Service;
	

	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	/**
	 * 更新用户信息
	 * 
	 * @param map
	 * @return int
	 */
	public void trans_UpdateUser(Map<String, Object> map) throws Exception{
	    String organizationInfoId = map.get(CherryConstants.ORGANIZATIONINFOID).toString();
	    String brandInfoId = map.get(CherryConstants.BRANDINFOID).toString();
	    String brandCode = map.get(CherryConstants.BRAND_CODE).toString();
	    
	    //设置【密码更改通知日期】【密码失效日】
	    binOLPLUPM04_BL.setPwdExpireDate(map);
	    
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, binollgtop03_Service.getSYSDate());
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLLGTOP03");
		String newPassWord = (String)map.get("newPassWord");
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		map.put("newPassWord", des.encrypt(newPassWord));
		// 更新用户信息
		int result = binollgtop03_Service.updateUser(map);
		// 更新失败
		if (0 == result) {
			throw new CherryException("ECM00038");
		}
		//↓↓↓↓↓↓↓↓↓↓WITPOSQA-19293 密码修改时是否清除微信绑定登录信息  BY Liyuan 2016/08/04 START↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		// 销售帮用户绑定信息是否可以清除
		String isClearFlag = binOLCM14_BL.getConfigValue("1369", String.valueOf(organizationInfoId), brandInfoId);
		if("1".equals(isClearFlag) && !ConvertUtil.isBlank(ConvertUtil.getString(map.get("newPassWord")))) {
			map.put("validFlag", CherryConstants.VALIDFLAG_DISABLE);
			binolbsemp03Service.updateBangUserValidFlag(map);
		}
		//↑↑↑↑↑↑↑↑↑↑WITPOSQA-19293 密码修改时是否清除微信绑定登录信息  BY Liyuan 2016/08/04 END↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

		// 对BI用户进行操作的场合
		if(binolbsemp04BL.isCreateBIUser(organizationInfoId, brandInfoId)) {
			// BI用户名
			String biLoginName = (String)map.get("longinName");
			// BI用户组名
			String biGroupName = null;
			// 从CodeTable中取得用户名的前缀和用户组名
			Map<String, Object> codeMap = code.getCode("1179", brandCode);
			if(codeMap != null && !codeMap.isEmpty()) {
				String pre = (String)codeMap.get("value1");
				if(pre != null && !"".equals(pre)) {
					biLoginName = pre + biLoginName;
				}
				biGroupName = (String)codeMap.get("value2");
			} else {
				throw new CherryException("EBS00062");
			}
			
			// 更新BI用户
			binolbsemp04Service.updateBIUser(biLoginName, null, newPassWord, biGroupName, brandCode);
		}
	}
	
	/**
	 * 取得用户密码
	 * 
	 * @param map
	 * @return 用户密码
	 */
	public String getUserPassWord(Map<String, Object> map) throws Exception {
		
		// 取得用户密码
		String password = binollgtop03_Service.getUserPassWord(map);
		// 解密处理
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		password =  des.decrypt(password);
		return password;
	}

	
}
