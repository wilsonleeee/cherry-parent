/*  
 * @(#)BINOLMOCIO15_BL.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO15_IF;
import com.cherry.mo.cio.service.BINOLMOCIO15_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.bl.RivalSynchro;

@SuppressWarnings("unchecked")
public class BINOLMOCIO15_BL extends SsBaseBussinessLogic implements BINOLMOCIO15_IF{
	@Resource
	private BINOLMOCIO15_Service binolmocio15_Service;
	
	@Resource
	private RivalSynchro rivalSynchro;
	
	
	/**
	 * 取得竞争对手总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchRivalCount(Map<String, Object> map) {
		return binolmocio15_Service.getRivalCount(map);
	}
	/**
	 * 取得竞争对手List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchRivalList(Map<String, Object> map) {
		return binolmocio15_Service.getRivalList(map);
	}
	
	/**
	 * 添加竞争对手
	 * 
	 * @param map
	 * @return
	 * @throws CherryException 
	 */
	@Override
	public void tran_addRival(Map<String, Object> map) throws CherryException {
		int	rival_id =binolmocio15_Service.addRival(map);
		String brandCode=binolmocio15_Service.getBrandCode(String.valueOf(map.get("brandInfoId")));
		String rivalID=this.convertRivalId(rival_id, brandCode);
	
		Map<String, Object> mapIF = new HashMap<String, Object>();
		mapIF.put("BrandCode",brandCode);
		//竞争对手名称
		mapIF.put("RivalName",map.get("rivalNameCN"));
		mapIF.put("RivalID", rivalID);
		mapIF.put(CherryConstants.SYNCHRORIVAL_OPERATEFLAG, CherryConstants.SYNCHRORIVAL_OPERATEFLAG_ADD);
		rivalSynchro.synchroRival(mapIF);
	}
	
	/**
	 * 更新竞争对手信息
	 * 
	 */
	@Override
	public void tran_updateRival(Map<String, Object> map) throws Exception {
		// 取得更新前竞争对手所属品牌CODE
		String brandCode = binolmocio15_Service.getRivalBrandCode(map);
		// 更新竞争对手信息
		binolmocio15_Service.updateRival(map);
		// 更新后的所属品牌CODE
		String brandCodeNew = binolmocio15_Service.getBrandCode(String.valueOf(map
				.get("brandInfoId")));
		// 竞争对手ID
		int rival_id = ConvertUtil.getInt(map.get("rivalId"));
		// 终端数据库中的竞争对手ID格式
		String rivalID = this.convertRivalId(rival_id, brandCode);
		String rivalIDNew = this.convertRivalId(rival_id,brandCodeNew);
		Map<String, Object> mapIF = new HashMap<String, Object>();
		mapIF.put("BrandCode",brandCode);
		mapIF.put("BrandCodeNew", brandCodeNew);
		//竞争对手名称
		mapIF.put("RivalName",map.get("rivalNameCN"));
		mapIF.put("RivalID", rivalID);
		mapIF.put("RivalIDNew", rivalIDNew);
		mapIF.put(CherryConstants.SYNCHRORIVAL_OPERATEFLAG, CherryConstants.SYNCHRORIVAL_OPERATEFLAG_UPDATE);
		rivalSynchro.synchroRival(mapIF);
	}
	
	/**
	 * 将竞争对手ID转换为终端数据库的竞争对手ID格式
	 * @param rivalId
	 * @param brandCode
	 * @return
	 */
	private String convertRivalId(int rivalId, String brandCode) {
		
		String rivalIdStr = String.format("%04d", rivalId);
		if (brandCode.length() < 6) {
			String brandcodetemp = "000000";
			brandCode = brandCode
					+ brandcodetemp.substring(0, brandcodetemp.length()
							- brandCode.length());
		} else if (brandCode.length() > 6) {
			brandCode = brandCode.substring(0, 5)
					+ brandCode.substring(brandCode.length() - 1);
		}
		// 终端数据库中的竞争对手ID格式
		String rivalID = brandCode + rivalIdStr;
		return rivalID;
	}
	
	/**
	 * 删除竞争对手
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_deleteRival(Map<String, Object> map) throws Exception {
		// 取得待删除竞争对手所属品牌CODE
		String brandCode = binolmocio15_Service.getRivalBrandCode(map);
		binolmocio15_Service.deleteRival(map);
		// 竞争对手ID
		int rival_id = ConvertUtil.getInt(map.get("rivalId"));
		// 终端数据库中的竞争对手ID格式
		String rivalID = this.convertRivalId(rival_id,brandCode);
		Map<String, Object> mapIF = new HashMap<String, Object>();
		mapIF.put("BrandCode",brandCode);
		//竞争对手名称
		mapIF.put("RivalID", rivalID);
		mapIF.put(CherryConstants.SYNCHRORIVAL_OPERATEFLAG, CherryConstants.SYNCHRORIVAL_OPERATEFLAG_DELETE);
		rivalSynchro.synchroRival(mapIF);
	}
	
	@Override
	public String getCount(Map<String, Object> map) {
		return binolmocio15_Service.getCount(map);
	}
}
