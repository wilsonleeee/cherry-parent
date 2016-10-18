/*	
 * @(#)BINOLJNMAN05_BL.java     1.0 2012/10/30		
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
package com.cherry.jn.man.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.jn.man.interfaces.BINOLJNMAN05_IF;
import com.cherry.jn.man.service.BINOLJNMAN05_Service;

/**
 * 积分规则配置一览BL
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN05_BL implements BINOLJNMAN05_IF{
	
	@Resource
	private BINOLJNMAN05_Service binoljnman05_Service;
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	/**
     * 取得规则配置List
     * 
     * @param map
     * @return
     * 		规则配置List
     */
	@Override
    public List<Map<String, Object>> getCampGrpList(Map<String, Object> map) {
    	// 取得规则配置List
    	return binoljnman05_Service.getCampGrpList(map);
    }
    
    /**
     * 取得规则配置件数 
     * 
     * @param map
     * @return
     * 		规则配置件数
     */
	@Override
    public int getCampGrpCount(Map<String, Object> map) {
    	// 取得规则配置件数 
    	return binoljnman05_Service.getCampGrpCount(map);
    }
	
	/**
	 * 停用或者启用配置
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_editValid(Map<String, Object> map) throws Exception{
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLJNMAN05");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLJNMAN05");
		// 作成者
		map.put(CherryConstants.CREATEDBY, "BINOLJNMAN05");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "BINOLJNMAN05");
		// 停用或者启用配置
		int result = binoljnman05_Service.updateValidConfig(map);
		// 更新报错处理
		if(0 == result){
			throw new CherryException("ECM00005");
		}
		// 启用配置
		if ("1".equals(map.get("validFlag"))) {
			// 更新规则的配置ID
			result = binoljnman05_Service.updateRuleGrpId(map);
			if(0 == result){
				throw new CherryException("ECM00005");
			}
		}
		// 刷新该组规则
		knowledgeEngine.refreshGrpRule(Integer.parseInt(map.get("campaignGrpId").toString()));
	}
}
