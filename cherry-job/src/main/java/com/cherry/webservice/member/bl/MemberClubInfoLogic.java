/*  
 * @(#)MemberPointInfoLogic.java     1.0 2014/08/01      
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
package com.cherry.webservice.member.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.clb.bl.BINBEMBCLB01_BL;
import com.cherry.webservice.member.interfaces.MemberClubInfo_IF;

/**
 * 
 * 下发俱乐部BL
 * 
 * @author HUB
 * @version 1.0 2014.12.08
 */
public class MemberClubInfoLogic implements MemberClubInfo_IF{
	
	private static final Logger logger = LoggerFactory.getLogger(MemberClubInfoLogic.class);
	
	@Resource
	private BINBEMBCLB01_BL binbembclb01_BL;
	
	/**
     * 下发会员俱乐部信息
     * @param paramMap
     * @return
     */
	@Override
	public Map<String, Object> sendClubInfo(Map<String, Object> params) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put("organizationInfoId", params.get("BIN_OrganizationInfoID"));
		map.put("bin_OrganizationInfoID", params.get("BIN_OrganizationInfoID"));
		// 品牌信息ID
		map.put("brandInfoId", params.get("BIN_BrandInfoID"));
		map.put("brandInfoID", params.get("BIN_BrandInfoID"));
		// 品牌Code
		map.put("brandCode", params.get("brandCode"));
		try {
			logger.info("*********俱乐部实时Batch下发处理开始*********");
			binbembclb01_BL.tran_batchClub(map);
			logger.info("*********俱乐部实时Batch下发处理结束*********");
			resMap.put("ERRORCODE", 0);
		} catch (CherryBatchException cbx) {
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", cbx.getMessage());
		} catch (Exception e) {
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", e.getMessage());
		}
		return resMap;
	}
	
}
