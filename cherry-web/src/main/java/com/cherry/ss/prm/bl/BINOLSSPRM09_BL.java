/*		
 * @(#)BINOLSSPRM09_BL.java     1.0 2010/11/23		
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
package com.cherry.ss.prm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.service.BINOLSSPRM09_Service;

/**
 * 促销产品类别查询 BL
 * 
 */

public class BINOLSSPRM09_BL {

	@Resource
	private BINOLSSPRM09_Service binolssprm09_Service;
	
	/**
	 * 取得某一促销品类别的直属下级类别
	 * 
	 * @param map 检索条件
	 * @return 树结构字符串
	 */
	@SuppressWarnings("unchecked")
	public String getNextCategoryList(Map<String, Object> map) {
		// 促销品类别节点位置
		String path = (String)map.get(CherryConstants.PATH);
		List<Map<String, Object>> list =  new ArrayList<Map<String, Object>>();
		if(path == null || "".equals(path)) {
			// 取得某一用户能访问的顶层促销品类别级别
			Integer level = binolssprm09_Service.getFirstCategoryLevel(map);
			if(level != null) {
				// 顶层促销品类别级别
				map.put(CherryConstants.LEVEL, level);
				// 取得某一用户能访问的顶层促销品类别List
				list = binolssprm09_Service.getFirstCategoryList(map);
			}
		} else {
			// 取得某一促销品类别的直属下级类别
			list = binolssprm09_Service.getNextCategoryInfoList(map);
		}
		StringBuffer jsonTree = new StringBuffer();
		// 把取得的促销品类别List转换成树结构的字符串
		if(list != null && !list.isEmpty()) {
			jsonTree.append("[");
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> categoryMap = list.get(i);
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(categoryMap.get("child") != null && !"".equals(categoryMap.get("child"))) {
					child = Integer.parseInt(categoryMap.get("child").toString()) > 1 ? "closed" : "";
				}
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":\""+categoryMap.get("itemClassName")+"\",\"attr\":{\"id\":\""+categoryMap.get("prmCategoryId")+"\"}},\"attr\":{\"id\":\""+categoryMap.get("path")+"\"},\"state\":\""+child+"\"}");
				if(i+1 != list.size()) {
					jsonTree.append(",");
				}
			}
			jsonTree.append("]");
		}
		
		return jsonTree.toString();
	}

	/**
	 * 取得上级促销品类别List
	 * 
	 * @param map 检索条件
	 * @return 上级促销品类别List
	 */
	@SuppressWarnings("unchecked")
	public List getHigherCategoryList(Map<String, Object> map) {
		
		// 取得上级岗位信息List
		return binolssprm09_Service.getHigherCategoryList(map);
	}
	/**
	 * 取得促销品类别总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrmCateCount(Map<String, Object> map) {

		return binolssprm09_Service.getPrmCateCount(map);
	}

	/**
	 * 取得促销品类别信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List searchPrmCategoryList(Map<String, Object> map) {

		// 取得促销品基本信息List
		return binolssprm09_Service.getPrmCategoryList(map);
	}

	/**
	 * 伦理删除促销产品类别信息
	 * 
	 * @param prmInfos
	 *            促销产品类别参数数组（有效区分+促销产品类别ID+更新日期+更新次数）
	 * @param updatedBy （更新者ID）
	 * 
	 * @param validFlag
	 *            有效区分1：启用, 0：停用           
	 * @return
	 */
	public void tran_operatePrmCategory(String[] prmCategoryInfos, String validFlag,
			int updatedBy) throws CherryException, Exception {
		// 没有选择任何促销品类别
		if (null == prmCategoryInfos || 0 == prmCategoryInfos.length) {
			throw new CherryException("ESS00018");
		}
		for (String prmCategoryInfo : prmCategoryInfos) {
			String[] info = prmCategoryInfo.split(CherryConstants.UNLINE);
			// 最后修改时间或者修改次数为空时
			if (info.length < 4) {
				String itemClassCode = ConvertUtil.getString(binolssprm09_Service
						.getPrmCategoryInfo(info[1]).get(CherryConstants.ITEMCLASSCODE));
				throw new CherryException("ESS00019", new String[] { itemClassCode });
			}
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 促销产品类别ID
			map.put("prmCategoryId", info[1]);
			// 修改时间
			map.put(CherryConstants.MODIFY_TIME, info[2]);
			// 修改次数
			map.put(CherryConstants.MODIFY_COUNT, info[3]);
			// 更新者
			map.put(CherryConstants.UPDATEDBY, updatedBy);
			//有效区分
			map.put(CherryConstants.VALID_FLAG, validFlag);
			// 更新时间
			map.put(CherryConstants.UPDATE_TIME, binolssprm09_Service.getSYSDate());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM09");
			
			// 伦理删除促销品类别信息
			binolssprm09_Service.operatePrmCategory(map);
		}
	}
	
}
