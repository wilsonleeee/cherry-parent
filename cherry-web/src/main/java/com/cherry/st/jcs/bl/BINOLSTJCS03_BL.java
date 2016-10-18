package com.cherry.st.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.interfaces.BINOLSTJCS03_IF;
import com.cherry.st.jcs.interfaces.BINOLSTJCS04_IF;
import com.cherry.st.jcs.service.BINOLSTJCS03_Service;

/*  
 * @(#)BINOLSTJCS03_BL.java    1.0 2012-6-18     
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
public class BINOLSTJCS03_BL implements BINOLSTJCS03_IF {

	@Resource(name="binOLSTJCS03_service")
	private BINOLSTJCS03_Service binOLSTJCS03_Service;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	/**部门仓库关系设定接口*/
	@Resource(name="binOLSTJCS04_BL")
	private BINOLSTJCS04_IF binOLSTJCS04_BL;

	/**
	 * 根据实体仓库ID取得仓库信息
	 * 
	 * */
	@Override
	public Map getDepotInfoById(Map map) {
		return binOLSTJCS03_Service.getDepotInfoById(map);
	}

	/**
	 * 根据实体仓库ID取得部门仓库关系
	 * 
	 * 
	 * */
	@Override
	public List<Map> getRelInfoByDepotId(Map map) {
		return binOLSTJCS03_Service.getRelInfoByDepotId(map);
	}

	/**
	 * 更新仓库信息
	 * 
	 * 
	 * */
	@Override
	public int tran_updateDepotInfo(Map map) throws Exception {
		//县级市ID
//		String countyID = ConvertUtil.getString(map.get("countyID"));
//		//市ID
//		String cityID = ConvertUtil.getString(map.get("cityID"));
//		//省ID
//		String provinceID = ConvertUtil.getString(map.get("provinceID"));
//		
//		//将级别最低的区域ID设定为仓库所在的区域ID
//		if(!"".equals(countyID)){
//			map.put("regionID", countyID);
//		}else if(!"".equals(cityID)){
//			map.put("regionID", cityID);
//		}else if(!"".equals(provinceID)){
//			map.put("regionID", provinceID);
//		}
		
		int count = 0;
		//调用service进行仓库信息的保存,并返回自增ID
		count = binOLSTJCS03_Service.updateDepotInfo(map);
		
		//如果保存失败抛出自定义异常:仓库信息保存失败!
		if(count == 0){
			throw new CherryException("EST00028");
		}
		
		//删除部门仓库关系
		binOLSTJCS03_Service.deleteRelInfo(map);
		
		//设定部门仓库关系
		Map<String,Object> relMap = new HashMap<String,Object>();
		//组织ID
		relMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		//品牌ID
		relMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		//实体仓库ID
		relMap.put("inventoryInfoId", map.get("depotId"));
		// 作成者为当前用户
		relMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 作成程序名为当前程序
		relMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		// 更新者为当前用户
		relMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名为当前程序
		relMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
		
		//关联部门
		String[] departArr = (String[]) map.get("relOrganizationIDArr");
		//默认区分
		String[] defaultFlagArr = (String[]) map.get("defaultFlagArr");
		//备注
		String[] commentArr = (String[]) map.get("commentArr");
		
		//先验证仓库的测试区分与绑定的部门的测试区分是否一致,如果不一致抛出异常
		//添加的仓库的测试区分
		String depotTestType = (String) map.get("testType");
		List<Map<String,Object>> relList = new ArrayList<Map<String,Object>>();
		if(null != depotTestType && !"".equals(depotTestType)){
			if(null != departArr && departArr.length > 0){
				for(int i = 0 ; i < departArr.length ; i++){
					String departId = departArr[i];
					//调用共通取得测试区分
					String testType = binOLCM00_BL.getDepartTestType(departId);
					//判断绑定的部门的测试区分和仓库的测试区分是否相同,如果不相同抛出自定义异常
					if(!depotTestType.equals(testType)){
						throw new CherryException("EST00029");
					}
					Map<String,Object> temMap = new HashMap<String,Object>();
					temMap.put("organizationId", departId);
					temMap.put("defaultFlag", defaultFlagArr[i]);
					temMap.put("comments", commentArr[i]);
					relList.add(temMap);
				}
				//调用部门仓库关系设定接口进行设定
				binOLSTJCS04_BL.tran_setInvDepRelation(relMap, relList);
			}
			
		}
		return 0;
	}

	/**
	 * 验证编辑后的实体仓库是否已经存在
	 * 
	 * */
	@Override
	public List<Map> isExist(Map map) {
		return binOLSTJCS03_Service.isExist(map);
	}

}
