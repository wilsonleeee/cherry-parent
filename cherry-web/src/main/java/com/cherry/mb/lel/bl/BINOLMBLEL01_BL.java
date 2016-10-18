/*
 * @(#)BINOLMBLEL01_BL.java     1.0 2011/07/20
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

package com.cherry.mb.lel.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.mb.clb.bl.BINOLMBCLB01_BL;
import com.cherry.mb.common.MembersConstants;
import com.cherry.mb.lel.interfaces.BINOLMBLEL01_IF;
import com.cherry.mb.lel.service.BINOLMBLEL01_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员等级维护BL
 * 
 * @author lipc
 * @version 1.0 2011/07/20
 */
public class BINOLMBLEL01_BL implements BINOLMBLEL01_IF {

	@Resource
	private BINOLMBLEL01_Service service;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBLEL01_BL.class);

	/**
	 * 取得会员等级详细List
	 * 
	 * @param brandInfoId
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getLelDetailList(Map<String, Object> map) throws Exception {
		// 取得会员等级List
		List<Map<String, Object>> list = service.getLelDetailList(map);
		// 取得会员有效期
		for(Map<String, Object> memberDateMap : list){
			if(null != memberDateMap.get("periodvalidity")){
				Map<String, Object> periodvalidityMap = (Map<String, Object>) JSONUtil.deserialize((String) memberDateMap.get("periodvalidity"));
				String index = (String) periodvalidityMap.get("normalYear");
				memberDateMap.put("memberDate", periodvalidityMap.get("memberDate" + index));
				memberDateMap.put("textName", "text" + index);
			}
		}
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
//		List<String[]> keyList = new ArrayList<String[]>();
//		String[] key = { MembersConstants.GROUPID, MembersConstants.FROM_DATE,
//				MembersConstants.TO_DATE };
//		keyList.add(key);
//		// 按照有效期分段处理
//		ConvertUtil.convertList2DeepList(list, resultList, keyList, 0);
		
		return list;
	}

	/**
	 * 取得会员等级List
	 * 
	 * @param brandInfoId
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getLevelList(Map<String, Object> map) {
		return service.getLevelList(map);
	}

	/**
	 * 保存会员等级
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public void tran_save(Map<String, Object> map) throws Exception {
		// 添加更新共通信息Map
		Map<String, Object> paramsMap = getUpdMap(map);
		// 更新会员等级表,返回添加操作的记录信息
		updLevel(paramsMap);
		// 更新会员等级明细表
		updLevelDetail(paramsMap);
	}
	
	/**
	 * 执行规则重算
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public void tran_reCalc(Map<String, Object> map) throws Exception {
		//是否是全部会员
		String selectMode=(String) map.get("selectMode");
		if("1".equals(selectMode)){
			//会员ID：-9999，全部会员
			map.put("memberInfoId",map.get("memberInfoId"));
			// 查询重算日期信息
			Map<String, Object> reCalcDateInfo = binbedrcom01BL.getReCalcDateInfo(map);
			if (null != reCalcDateInfo && !reCalcDateInfo.isEmpty()) {
				// 重算日期
				String reCalcDate = (String) reCalcDateInfo.get("reCalcDate");
				reCalcDate = DateUtil.coverTime2YMD(reCalcDate, DateUtil.DATETIME_PATTERN);
				throw new CherryException("EMB00009", new String[]{reCalcDate});
			}
			// 插入重算信息表
			binbedrcom01BL.insertReCalcInfo(map);
			// 发送MQ重算消息进行实时重算
			binbedrcom01BL.sendReCalcMsg(map);
		}else{
			String []memberIDArr =(String[]) map.get("memberIDArr");
			String []memberCodeArr =(String[]) map.get("memberCodeArr");
			if (memberIDArr==null || memberIDArr.length==0) {
				throw new CherryException("IMB00002");
			}else{
				int lengthMember = memberIDArr.length;
				int lengthCode = 0;
				if (memberCodeArr != null && memberCodeArr.length > 0) {
					lengthCode = memberCodeArr.length;
				}
				for(int i=0;i<lengthMember;i++){
					map.put("memberInfoId",memberIDArr[i]);
					if (i < lengthCode) {
						map.put("memberCode", memberCodeArr[i]);
					}
					// 查询重算日期信息
					Map<String, Object> reCalcDateInfo1 = binbedrcom01BL.getReCalcDateInfo(map);
					if (null != reCalcDateInfo1 && !reCalcDateInfo1.isEmpty()) {
						// 重算日期
						String reCalcDate = (String) reCalcDateInfo1.get("reCalcDate");
						reCalcDate = DateUtil.coverTime2YMD(reCalcDate, DateUtil.DATETIME_PATTERN);
						throw new CherryException("EMB00009", new String[]{reCalcDate});
					}
					// 插入重算信息表
					binbedrcom01BL.insertReCalcInfo(map);
				}
				//发MQ部分会员ID设置为空
				map.put("memberInfoId","");
				// 发送MQ重算消息进行实时重算
				binbedrcom01BL.sendReCalcMsg(map);
			}
			
		}
	}

	/**
	 * 会员等级按照有效期分组处理
	 * 
	 * @param list
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> doList(Map<String, Object> paramMap,
			List<Map<String, Object>> list) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 会员等级有效期日期数组
		String[] dateArr = getDateArr(list);
		// 业务日期
		String busDate = ConvertUtil.getString(paramMap
				.get(MembersConstants.BUS_DATE));
		// 按照日期将会员等级分组
		for (int i = 0; i < dateArr.length - 1; i++) {
			// 剔除截止日期小于系统日期的日期段
			if (CherryChecker.compareDate(busDate, dateArr[i + 1]) <= 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 日期段开始日期为上一日期结束日期 +1天
				map.put(MembersConstants.FROM_DATE, DateUtil.addDateByDays(
						CherryConstants.DATE_PATTERN, dateArr[i], 1));
				map.put(MembersConstants.TO_DATE, dateArr[i + 1]);
				resultList.add(map);
			}
		}
		// 设置日期段内的会员等级list
		for (Map<String, Object> map : resultList) {
			List<Map<String, Object>> lelList = new ArrayList<Map<String, Object>>();
			// 有效期段开始日期
			String fromDate = ConvertUtil.getString(map
					.get(MembersConstants.FROM_DATE));
			// 有效期段截止日期
			String toDate = ConvertUtil.getString(map
					.get(MembersConstants.TO_DATE));
			for (Map<String, Object> item : list) {
				if (!item.isEmpty()) {
					// 开始日期
					String from = ConvertUtil.getString(item
							.get(MembersConstants.FROM_DATE));
					// 截止日期
					String to = ConvertUtil.getString(item
							.get(MembersConstants.TO_DATE));
					// 比较有效期，添加日期段内的会员等级
					if (CherryChecker.compareDate(fromDate, from) >= 0
							&& CherryChecker.compareDate(toDate, to) <= 0) {
						lelList.add(item);
					}
				}
			}
			// ，设置日期段内的会员等级list
			map.put("list", lelList);
		}
		return resultList;
	}

	/**
	 * 更新会员等级表
	 * 
	 * @param map
	 * @param updMap
	 * @param dateArr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updLevel(Map<String, Object> map) throws Exception {
		// 顺序的有效日期数组
		String[] dateArr = null;
		// 保存添加操作的记录信息
		Map<String, Integer> insertMap = new HashMap<String, Integer>();
		// 要保存或者更新的会员等级
		String json = ConvertUtil.getString(map.get("json"));
		// 删除操作的会员等级
		String delJson = ConvertUtil.getString(map.get("delJson"));
		// 要保存或者更新的会员等级list
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) JSONUtil
				.deserialize(json);
		// 会员等级删除List
		List<Map<String, Object>> delList = (List<Map<String, Object>>) JSONUtil
				.deserialize(delJson);
		// 会员等级删除
		if (null != delList) {
			for (Map<String, Object> delMap : delList) {
				if (!delMap.isEmpty()) {
					// 删除会员等级表
					service.delLevel(delMap);
					// 删除会员等级详细表
					service.delLevelDetail(delMap);
				}
			}
		}
		// 保存或者更新的会员等级
		if (null != levelList) {
			for (Map<String, Object> level : levelList) {
				// 默认等级开始时间
				level.put("fromDate", MembersConstants.DEFAULT_FROM_DATE);
				// 默认等级结束时间
				level.put("toDate", MembersConstants.DEFAULT_TO_DATE);
				if (!level.isEmpty()) {
					// 会员等级Id
					int levelId = CherryUtil.obj2int(level
							.get(MembersConstants.LEVELID));
					level.putAll(map);
					if (levelId == 0) {
						// 添加会员等级
						levelId = service.addLevel(level);
						// 等级名作为Key，等级ID作为Value
						insertMap.put(ConvertUtil.getString(level
								.get(MembersConstants.LEVEL_NAME)), levelId);
					} else {
						// 更新会员等级
						service.updLevel(level);
					}
				}
			}
		}
		dateArr = getDateArr(levelList);
		map.put("dateArr", dateArr);
		map.put("insertMap", insertMap);
	}

	/**
	 * 更新会员等级明细表
	 * 
	 * @param map
	 * @param updMap
	 * @param dateArr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updLevelDetail(Map<String, Object> map) throws Exception {
		// 有序的有效日期
		String[] dateArr = (String[]) map.get("dateArr");
		// 要保存或者更新的会员等级
		String json = ConvertUtil.getString(map.get("json"));
		// 会员等级明细
		String detailJson = ConvertUtil.getString(map.get("detailJson"));
		// 保存添加操作的记录信息
		Map<String, Integer> insertMap = (Map<String, Integer>) map
				.get("insertMap");
		// 要保存或者更新的会员等级list
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) JSONUtil
				.deserialize(json);
		// 会员等级明细List
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) JSONUtil
				.deserialize(detailJson);
		// 更新会员等级明细
		if (null != detailList) {
			Map<String, Object> delMap = new HashMap<String, Object>();
			if (dateArr.length > 1) {
				// 业务日期
				delMap.put(MembersConstants.BUS_DATE, map
						.get(MembersConstants.BUS_DATE));
				// 截止日期
				delMap.put(MembersConstants.TO_DATE,
						dateArr[dateArr.length - 1]);
			}
			// 清空所有旧的会员等级明细
			for (Map<String, Object> level : levelList) {
				if (!level.isEmpty()) {
					// 会员等级Id
					int levelId = CherryUtil.obj2int(level
							.get(MembersConstants.LEVELID));
					if (levelId != 0) {
						// 删除会员等级明细表
						delMap.put(MembersConstants.LEVELID, levelId);
						service.delLevelDetail(delMap);
					}
				}
			}
			// 添加会员等级明细
			for (Map<String, Object> detail : detailList) {
				if(detail.isEmpty()){
					continue;
				}
				// 默认等级开始时间
				detail.put("fromDate", MembersConstants.DEFAULT_FROM_DATE);
				// 默认等级结束时间
				detail.put("toDate", MembersConstants.DEFAULT_TO_DATE);
				if (!detail.isEmpty()) {
					// 会员等级Id
					int levelId = CherryUtil.obj2int(detail
							.get(MembersConstants.LEVELID));
					detail.putAll(map);
					if (levelId == 0) {
						// 取得等级名称
						String levelName = ConvertUtil.getString(detail
								.get(MembersConstants.LEVEL_NAME));
						// 取得新添加的会员等级ID
						levelId = insertMap.get(levelName);
						detail.put(MembersConstants.LEVELID, levelId);
					}
					// 添加会员等级明细表
					service.addLevelDetail(detail);
				}
			}
		}
	}

	/**
	 * 取得顺序的有效日期数组
	 */
	private String[] getDateArr(List<Map<String, Object>> list) {
		// 不重复，顺序的日期set
		Set<String> dateSet = new TreeSet<String>();
		// 取得所有日期
		for (Map<String, Object> map : list) {
			if (!map.isEmpty()) {
				String fromDate = ConvertUtil.getString(map
						.get(MembersConstants.FROM_DATE));
				String toDate = ConvertUtil.getString(map
						.get(MembersConstants.TO_DATE));
				if (CherryConstants.BLANK.equals(toDate)) {
					// 默认有效截止日期
					toDate = CherryConstants.longLongAfter;
					map.put(MembersConstants.TO_DATE, toDate);
				}
				// 开始日期-1处理
				dateSet.add(DateUtil.addDateByDays(
						CherryConstants.DATE_PATTERN, fromDate, -1));
				dateSet.add(toDate);
			}
		}
		// 会员等级有效期日期数组
		return dateSet.toArray(new String[0]);
	}

	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getUpdMap(Map<String, Object> map) {
		// 用户信息
		UserInfo userInfo = (UserInfo) map
				.get(CherryConstants.SESSION_USERINFO);
		// 数据库系统时间
		String sysDate = service.getSYSDate();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLMBLEL01");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLMBLEL01");
		return map;
	}

	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return String
	 */
	@Override
	public String getBussinessDate(Map<String, Object> map) {
		return service.getBussinessDate(map);
	}

	/**
	 * 取得会员等级ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getLevelId(Map<String, Object> map) {
		return service.getLevelId(map);
	}
	
	/**
	 * 更新会员等级表中会员有效期信息
	 * 
	 * @param map
	 * @return String
	 */
	@Override
	public int updMemberDate(Map<String, Object> map) {
		return service.updMemberDate(map);
	}

	/**
	 * 取得会员默认等级标志
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getDefaultFlag(Map<String, Object> map) {
		return service.getDefaultFlag(map);
	}
	
	/**
	 * 取得会员等级个数
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getMemberCount(Map<String, Object> map) {
		return service.getMemberCount(map);
	}
	
	/**
	 * 取得升降级规则个数
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getUpLevelRuleCount(Map<String, Object> map) {
		return service.getUpLevelRuleCount(map);
	}
	
	/**
	 * 取得默认等级
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public int getDefaultLevel(Map<String, Object> map) {
		return service.getDefaultLevel(map);
	}
	
	/**
	 * 执行下发处理
	 * 
	 * @param map
	 * 			参数集合
	 */
	@Override
	public Map<String, Object> sendLevel(Map<String, Object> map) throws Exception {
		 Map<String, Object> result;
        String errCode = "";
        String errMsg = "OK";
        
        Map<String, Object> msgMap = new HashMap<String, Object>();
		try{
			map.put("TradeType","SendLevelInfo");
			result = WebserviceClient.accessBatchWebService(map);
            if(null != result){
                errCode = ConvertUtil.getString(result.get("ERRORCODE"));
                errMsg = ConvertUtil.getString(result.get("ERRORMSG"));
                if(!"0".equals(errCode)){
                	msgMap.put("result", "1");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                    logger.error("*********会员等级webService下发处理异常ERRORCODE【"+errCode+"】*********");
                    logger.error("*********会员等级webService下发处理异常ERRORMSG【"+errMsg+"】*********");
                } else {
                	msgMap.put("result", "0");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                }
            }else{
            	msgMap.put("result", "1");
                errCode = "-1";
                errMsg = "webService访问返回结果信息为空";
                msgMap.put("ERRORCODE", errCode);
                msgMap.put("ERRORMSG", errMsg);
                logger.error("********* 会员等级webService下发处理异常ERRORCODE【"+errCode+"】*********");
                logger.error("********* 会员等级webService下发处理异常ERRORMSG【"+errMsg+"】*********");
            }

            
            logger.info("*********会员等级webService下发处理结束【"+errCode+"】*********");
		}catch(Exception e){
			msgMap.put("result", "1");
			logger.error(e.getMessage(),e);
		}
		return msgMap;
	}
}
