/*
 * @(#)BINOLMOMAN01_BL.java     1.0 2011/3/15
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
package com.cherry.mo.man.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.interfaces.BINOLMOMAN10_IF;
import com.cherry.mo.man.service.BINOLMOMAN10_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

 
/**
 * 
 *  POS品牌菜单管理BL
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN10_BL  extends SsBaseBussinessLogic implements BINOLMOMAN10_IF{

	@Resource(name="binOLMOMAN10_Service")
    private BINOLMOMAN10_Service binOLMOMAN10_Service;
	
	/**WebService 共通BL*/
	@Resource(name="binOLCM27_BL")
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	 /**
     * 取得POS品牌菜单管理List 
     * 
     * @param map
     * @return POS品牌菜单管理List
     */
	@Override
	public List<Map<String, Object>> searchPosMenuBrandInfoList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOMAN10_Service.searchPosMenuBrandInfoList(map);
	}
	
	/**
     *  修改POS品牌菜单管理
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	@Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void tran_updatePosMenuBrand(Map<String, Object> map) throws Exception{
		// TODO Auto-generated method stub
		  binOLMOMAN10_Service.updatePosMenuBrand(map);
		  getLogInvWSMap(map);
	}
	
	/**
     *  修改POS品牌菜单管理MenuStatus
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	public void tran_updatePosMenuBrandMenuStatus(Map<String, Object> map) throws Exception{
		// TODO Auto-generated method stub
		  binOLMOMAN10_Service.updatePosMenuBrandMenuStatus(map);
		  getLogInvWSMap(map);
	}
	
	/**
     *  新增POS品牌菜单管理
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	public int tran_addPosMenuBrand(Map<String, Object> map) throws Exception{
		// TODO Auto-generated method stub
		int  posMenuBrandID =  binOLMOMAN10_Service.addPosMenuBrand(map);
        map.put("posMenuBrandID", posMenuBrandID);
        getLogInvWSMap(map);
        return posMenuBrandID;
		

	}
	
	 /**
     * 取得POS品牌菜单管理List (调用accessWebService所用数据格式)
     * 
     * @param map
     * @return POS品牌菜单管理List
     */
	@Override
	public void searchPosMenuBrandInfoWithWS (Map<String, Object> map) throws Exception{
			getLogInvWSMap(map);
	}
	
	@Override
	public void getLogInvWSMap(Map<String, Object> map) throws Exception {
		try{
		//是否调用Webservice进行逻辑仓库数据下发
		boolean issuedWS = binOLCM14_BL.isConfigOpen("1064",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		if(issuedWS){
			
		// TODO Auto-generated method stub
		Map<String,Object> logInvData = new HashMap<String,Object>();
		
		Map<String,Object> maps = new HashMap<String,Object>();
		
		// 头文件
		Map<String,Object> dataHead = new HashMap<String,Object>();
		// 品牌代码
		dataHead.put("BrandCode", map.get("brandCodename"));
		// 业务类型
		dataHead.put("BussinessType", "POSBrandMenu");
		// 消息体版本号
		dataHead.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
		// SubType
		dataHead.put("SubType", "");
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		detailList.addAll(0,binOLMOMAN10_Service.getPosMenuBrandWithWS(map));
		maps.put("DetailList", detailList);
		// 明细数据行--POS品牌菜单管理	
		List<Map<String,Object>> detailLists = (List<Map<String,Object>>)maps.get("DetailList");
			if(detailList.size()==0){ 
	
			}else{
				logInvData.putAll(dataHead);
				// 明细数据行--POS品牌菜单管理
				logInvData.put("DetailList", detailLists);
				
				if(null != logInvData){
	//				WebService方式台信息Map
					Map<String,Object> resultMap = binOLCM27_BL.accessWebService(logInvData);
					String state = ConvertUtil.getString(resultMap.get("State"));
					if(state.equals("ERROR")){
						throw new CherryException("ECM00035");
					}
				}
				
			}
	
		}
		}catch(Exception e){
			throw e;
		}
	}
		
}
