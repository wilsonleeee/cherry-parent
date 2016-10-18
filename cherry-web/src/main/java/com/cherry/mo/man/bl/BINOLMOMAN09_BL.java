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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.interfaces.BINOLMOMAN09_IF;
import com.cherry.mo.man.service.BINOLMOMAN09_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
 
/**
 * 
 *  POS菜单BL
 * 
 * @author 
 * @version 1.0 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN09_BL  extends SsBaseBussinessLogic implements BINOLMOMAN09_IF{

	@Resource(name="binOLMOMAN09_Service")
    private BINOLMOMAN09_Service binOLMOMAN09_Service;
    
	/**WebService 共通BL*/
	@Resource(name="binOLCM27_BL")
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
    /**
     * 取得POS菜单总数
     * 
     * @param map
     * @return POS菜单总数
     */
	@Override
	public int searchMemuInfoCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOMAN09_Service.getPosMemuInfoCount(map);
	}
	
	 /**
     * 取得POS菜单List 
     * 
     * @param map
     * @return POS菜单List
     */
	@Override
	public List<Map<String, Object>> searchPosMemuInfoList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOMAN09_Service.getPosMemuInfoList(map);
	}
	  
	/**
	* 取得POS菜单INFO
	* 
	* @param map
	* 
	* @return
	*/	
	@Override
	public Map getPosMemu(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOMAN09_Service.getPosMemu(map);
	}
	
	/**
     *  修改菜单数据
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	@Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void tran_updatePosMemu(Map<String, Object> map) throws Exception{
		// TODO Auto-generated method stub
		  binOLMOMAN09_Service.updatePosMemu(map);
		  getLogInvWSMap(map);
	}
	
	/**
     * 新增菜单数据
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	@Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int tran_addPosMemu(Map<String, Object> map) throws Exception{	
		int  posMenuID =binOLMOMAN09_Service.addPosMemu(map);
		 map.put("posMenuID", posMenuID);
		getLogInvWSMap(map);
        return posMenuID;
	}
	
	/**
     * 删除菜单数据
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	@Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void tran_deletePosMemu(Map<String, Object> map) throws Exception{	
		binOLMOMAN09_Service.deletePosMemu(map);
		getLogInvWSMap(map);
	}
	
	@Override
	public void getLogInvWSMap(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		try{
		boolean issuedWS = binOLCM14_BL.isConfigOpen("1064",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		if(issuedWS){
		Map<String,Object> logInvData = new HashMap<String,Object>();
		
		Map<String,Object> maps = new HashMap<String,Object>();
		
		// 头文件
		Map<String,Object> dataHead = new HashMap<String,Object>();
		// 品牌代码
		dataHead.put("BrandCode", map.get("BrandCode"));
		// 业务类型
		dataHead.put("BussinessType", "POSMenu");
		// 消息体版本号
		dataHead.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
		// SubType
		dataHead.put("SubType", "");
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		detailList.addAll(0,binOLMOMAN09_Service.getPosMemuWS(map));
		maps.put("DetailList", detailList);
		// 明细数据行--POS配置项数据		
		List<Map<String,Object>> detailLists = (List<Map<String,Object>>)maps.get("DetailList");
			if(detailList.size()==0){ 
	
			}else{
				logInvData.putAll(dataHead);
				// 明细数据行--POS配置项数据
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
