/*
 * @(#)BINOLCTTPL04_BL.java     1.0 2013/11/19
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
package com.cherry.ct.tpl.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL04_IF;
import com.cherry.ct.tpl.service.BINOLCTTPL04_Service;

/**
 * 沟通模板内容非法字符设置 BL
 * 
 * @author ZhangLe
 * @version 1.0 2013.11.19
 */
public class BINOLCTTPL04_BL implements BINOLCTTPL04_IF {
	@Resource(name="binOLCTTPL04_Service")
	private BINOLCTTPL04_Service binOLCTTPL04_Service;
	

	@Override
	public int getIllegalCharCount(Map<String, Object> map) {
		return binOLCTTPL04_Service.getIllegalCharCount(map);
	}

	@Override
	public List<Map<String, Object>> getIllegalCharList(Map<String, Object> map) {
		List<Map<String, Object>> illegalCharList = binOLCTTPL04_Service.getIllegalCharList(map);
		for(Map<String, Object> fm : illegalCharList){
			String remark = ConvertUtil.getString(fm.get("remark"));
			if(!CherryChecker.isNullOrEmpty(remark, true) && remark.length() > 30){
				String newRemark = remark.substring(0, 28)+"...";
				fm.put("cutRemark", newRemark);
			}else{
				fm.put("cutRemark", remark);
			}
		}
		return illegalCharList;
	}

	@Override
	public int tran_addIllegalChar(Map<String, Object> map) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		params.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		//排序
		map.put("orderNo", this.getIllegalCharCount(params)+1);
		return binOLCTTPL04_Service.addIllegalChar(map);
	}

	@Override
	public int tran_updateIllegalChar(Map<String, Object> map) throws Exception {
		return binOLCTTPL04_Service.updateIllegalChar(map);
	}

	@Override
	public Map<String, Object> getIllegalCharMap(Map<String, Object> map) {
		return binOLCTTPL04_Service.getIllegalCharMap(map);
	}

	/**
	 * 判断是否重复，重复返回true，不重复返回false
	 * @param map
	 * @return
	 */
	@Override
	public boolean isRepeat(Map<String, Object> map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		params.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		params.put("sCharValue", map.get("charValue"));
		params.put("commType", map.get("commType"));
		Map<String, Object> illegalCharInfo = binOLCTTPL04_Service.getIllegalCharMap(params);
		String charId= ConvertUtil.getString(map.get("charId"));
		if(illegalCharInfo != null && !illegalCharInfo.isEmpty()){
			String resultCharId = ConvertUtil.getString(illegalCharInfo.get("charId"));
			if(CherryChecker.isNullOrEmpty(charId, true) || !charId.equals(resultCharId)){
				return true;
			}
		}
		return false;
	}

}
