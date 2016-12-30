/*
 * @(#)BINOLPTJCS01_BL.java     1.0 2011/04/11
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
package com.cherry.pt.jcs.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS01_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 产品分类维护 BL
 * 
 * @author lipc
 * @version 1.0 2011.04.11
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS01_BL implements BINOLPTJCS01_IF {

	@Resource
	private BINOLPTJCS01_Service binOLPTJCS01_Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * 移动分类树形显示顺序
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public void tran_move(Map<String, Object> map) throws Exception {
		// Map添加更新共通信息
		addUpdMap(map);
		String[] seqs = (String[]) map.get(ProductConstants.SEQ);
		// 需要移动的行[元素0:id,元素1:viewSeq]
		String[] seqInfo0 = seqs[0].split(CherryConstants.UNLINE);
		// 替换位置行
		String[] seqInfo1 = seqs[1].split(CherryConstants.UNLINE);
		int seq0 = CherryUtil.obj2int(seqInfo0[1]);
		int seq1 = CherryUtil.obj2int(seqInfo1[1]);
		// 上移
		if (seq0 > seq1) {
			map.put("direction", 1);
			map.put("viewSeq1", seq1);
			map.put("viewSeq2", seq0);
		} else {
			map.put("viewSeq1", seq0);
			map.put("viewSeq2", seq1);
		}
		// 更新移动行和替换位置行之间的所有行
		binOLPTJCS01_Service.updViewSeq(map);
		map.put("viewSeq", seq1);
		map.put(ProductConstants.PROPID, seqInfo0[0]);
		// 更新需要移动的行
		binOLPTJCS01_Service.updCatProperty(map);
	}

	/**
	 * 保存分类Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public void tran_save(Map<String, Object> map) throws Exception {
		// Map添加更新共通信息
		addUpdMap(map);
		// 取得产品分类信息参数
		String json = ConvertUtil.getString(map.get(ProductConstants.JSON));
		// 产品分类Map
		Map<String, Object> cateMap = (Map<String, Object>) JSONUtil
				.deserialize(json);
		// 分类ID
		int propId = CherryUtil.obj2int(cateMap.get(ProductConstants.PROPID));
		cateMap.putAll(map);
		if (propId == 0) { // 分类添加
			binOLPTJCS01_Service.addCatProperty(cateMap);
		} else { // 分类编辑
			binOLPTJCS01_Service.updCatProperty(cateMap);
		}
	}

	/**
	 * 保存分类选项值
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public void tran_saveVal(Map<String, Object> map) throws Exception {
		
		// 产品分类(终端用)编码生成方式 1:随机生成：随机生成4位编码、2:同步生成：与新后台定义的产品分类编码同值。新后台新增时，长度限制为4位。
		String confVal = binOLCM14_BL.getConfigValue("1300", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);

		// Map添加更新共通信息
		addUpdMap(map);
		// 取得产品分类选项值info
		String json = ConvertUtil.getString(map.get(ProductConstants.JSON));
		// 产品分类选项值List
		Map<String, Object> propValMap = (Map<String, Object>) JSONUtil
				.deserialize(json);
		int propValId = CherryUtil.obj2int(propValMap.get(ProductConstants.PROPVALID));
		propValMap.putAll(map);
		// 分类码后台用
		String propValueCherry = ConvertUtil.getString(propValMap.get(ProductConstants.PROPVALUECHERRY));
		if (propValId == 0) { // 添加
			if (propValueCherry.length() == 0) {
				// 取得不重复的分类码
				propValueCherry = getPropValue(map,ProductConstants.PROPVALUECHERRY);
			} 
			// 取得不重复的分类码
			String propValue = getPropValue(map,ProductConstants.PROPVALUE);
			if("2".equals(confVal)){
				propValue = propValueCherry;
			}
			
			// 分类码终端用
			propValMap.put(ProductConstants.PROPVALUE, propValue);
			// 分类码后台用
			propValMap.put(ProductConstants.PROPVALUECHERRY, propValueCherry);
			// 插入数据库
			binOLPTJCS01_Service.addPropVal(propValMap);
		} else { // 编辑
			
			// 分类码终端用 ,当终端分类系统配置项是同步生成时，更新时，与新后台产品分类编码保持一致
			if("2".equals(confVal)){
				propValMap.put(ProductConstants.PROPVALUE, propValueCherry);
			}
			binOLPTJCS01_Service.updPropVal(propValMap);
			
			// 更新分类关联的产品version
			map.put("propValId", propValId);
			binOLPTJCS01_Service.updProduct(map);
		}
	}

	/**
	 * 停用或者启用分类选项值
	 *
	 * @param map
	 *
	 * @return
	 */
	@Override
	public void tran_changeFlagVal(Map<String, Object> map) throws Exception {
		//设置共通参数
		addUpdMap(map);
		// 取得产品分类选项值info
		String json = ConvertUtil.getString(map.get(ProductConstants.JSON));
		// 产品分类选项值List
		Map<String, Object> propValMap = (Map<String, Object>) JSONUtil.deserialize(json);
		int propValId = CherryUtil.obj2int(propValMap.get(ProductConstants.PROPVALID));
		map.put("propValId",propValId);
		binOLPTJCS01_Service.changeFlagVal(map);
	}

	/**
	 * 取得品牌下不重复的分类属性值
	 * 
	 * @param map
	 * @return
	 */
	private String getPropValue(Map<String, Object> map,String key) {
		// 添加产品分类选项值
		Map<String, Object> temp = new HashMap<String, Object>();
		// 分类类别ID
		temp.put(ProductConstants.PROPID, map.get(ProductConstants.PROPID));
		while (true) {
			// 随机产生4位的字符串
			String randomStr = CherryUtil.getRandomStr(ProductConstants.CATE_LENGTH);
			temp.put(key, randomStr);
			// 取得分类属性值ID
			int propValId = binOLPTJCS01_Service.getCateValId1(temp);
			// 随机产生的4位字符串不重复
			if (propValId == 0) {
				return randomStr;
			}
		}
	}

	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private void addUpdMap(Map<String, Object> map) {
		// 用户信息
		UserInfo userInfo = (UserInfo) map
				.get(CherryConstants.SESSION_USERINFO);
		// 数据库系统时间
		String sysDate = binOLPTJCS01_Service.getSYSDate();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS01");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS01");
	}

	/**
	 * 根据分类名取得分类ID
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getPropId1(Map<String, Object> map) {
		return binOLPTJCS01_Service.getPropId1(map);
	}

	/**
	 * 根据分类终端下发取得分类ID
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getPropId2(Map<String, Object> map) {
		return binOLPTJCS01_Service.getPropId2(map);
	}

	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getCateValId1(Map<String, Object> map) {
		return binOLPTJCS01_Service.getCateValId1(map);
	}

	/**
	 * 根据属性值名查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getCateValId2(Map<String, Object> map) {
		return binOLPTJCS01_Service.getCateValId2(map);
	}

	/**
	 * 取得分类Info
	 * 
	 * @param map
	 */
	@Override
	public Map getCategoryInfo(Map<String, Object> map) {
		return binOLPTJCS01_Service.getCategoryInfo(map);
	}

	/**
	 * 取得分类选项值Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public Map getCateVal(Map<String, Object> map) {
		return binOLPTJCS01_Service.getCateVal(map);
	}

	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map) {
		return binOLPTJCS01_Service.getCategoryList(map);
	}

	/**
	 * 取得分类选项值List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCateValList(Map<String, Object> map) {
		return binOLPTJCS01_Service.getCateValList(map);
	}


	/**
	 * 查询某一分类选项值下的有效的产品数量
	 *
	 * @param map
	 */
	@Override
	public  int getProductEnableNum(Map<String, Object> map){
		return binOLPTJCS01_Service.getProductEnableNum(map);
	}
}
