/*  
 * @(#)BINOLMOCIO07_BL.java     1.0 2011/05/31      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO07_IF;
import com.cherry.mo.cio.service.BINOLMOCIO07_01_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * @ClassName: BINOLMOCIO07_01_BL 
 * @Description: TODO(销售日目标导入处理类) 
 * @author menghao
 * @version v1.0.0 2016-4-19 
 *
 */
@SuppressWarnings("unchecked")
public class BINOLMOCIO07_01_BL extends SsBaseBussinessLogic implements BINOLMOCIO07_IF{
	
	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO07_01_BL.class);
    
	@Resource(name="binOLMOCIO07_01_Service")
	private BINOLMOCIO07_01_Service binOLMOCIO07_01_Service;

    @Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binolcm00Service;
    @Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
    
	/**
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getSaleTargetCount(Map<String, Object> map) {
		return binOLMOCIO07_01_Service.getSaleDayTargetCount(map);
	}
	
	/**
	 * 取得List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> searchSaleTargetList(Map<String, Object> map) {
		return binOLMOCIO07_01_Service.searchSaleDayTargetList(map);
	}
	
	/**
	 * 获取柜台树节点
	 * 
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getTreeNodes(Map<String, Object> map)	throws Exception {
		// 调用service获取按共通部门/人员权限取得树节点（区域、柜台、美容顾问），按照path排序
		List<Map<String, Object>> list = binOLMOCIO07_01_Service.getTreeNodes(map);
		// 将获取的list转换成树形结构需要的形式
		List<Map<String, Object>> resultList = ConvertUtil.getTreeList(list, "nodes");
		// 调用递归函数，处理resultList中的数据，将不需要节点清除
		resultList = dealJsDepartList(resultList);
		if(null != resultList && map.get("type").equals("3")){
			// 取得最大岗位级别
			String grade = binolcm00Service.getMaxPosCategoryGrade(map);
			if(grade != null && !"".equals(grade)) {
				List<Map<String, Object>> resultTempList = new ArrayList<Map<String, Object>>();
				for(int i=0; i<resultList.size(); i++){
					Object gradeTemp = resultList.get(i).get("grade");
					if(gradeTemp == null || !grade.equals(gradeTemp.toString())) {
						resultTempList.add(resultList.get(i));
						resultList.remove(i);
						i--;
					}
				}
                if (null != resultTempList && resultTempList.size() > 0) {
                    Map<String, Object> noHigherMap = new HashMap<String, Object>();
                    noHigherMap.put("name", CherryConstants.UNKNOWN_DEPARTNAME);
                    noHigherMap.put("nodes", resultTempList);
                    resultList.add(noHigherMap);
                }
			}
		}
		return resultList;
	}
	
	/**
	 * 递归处理list，将不需要的key清除
	 * 
	 * @param list
	 * @return list
	 * 
	 * */
	public List<Map<String, Object>> dealJsDepartList(
			List<Map<String, Object>> list) {
		if (null != list && list.size() > 0) {
			for (int index = 0; index < list.size(); index++) {
				list.get(index).remove("path");
				list.get(index).remove("level");
				if (null != list.get(index).get("nodes")) {
					List<Map<String, Object>> childList = (List<Map<String, Object>>) list
							.get(index).get("nodes");
					dealJsDepartList(childList);
				}
			}
		}
		return list;
	}
	
	/**
	 * 下发订货参数
	 * 
	 * 
	 * */
	@Override
	public void down(Map<String, Object> map) throws Exception {
		
		String targetType = ConvertUtil.getString(map.get("targetType"));
		if("".equals(targetType) || "PRM".equals(targetType)) {
			// 只能下发目标类型为产品的销售目标，请重新选择进行下发！
			throw new CherryException("EMO00086");
		}
		String brandInfoId = (String) map.get("brandInfoId");
		String brandCode=binOLMOCIO07_01_Service.getBrandCode(brandInfoId);
		
		List<Map<String ,Object>> downList = binOLMOCIO07_01_Service.searchDownList(map);
		Map<String,Object> mapIF = new HashMap<String,Object>();
		if(null != downList && downList.size() > 0){
			//遍历柜台和产品list，将这两个list中的map整合到paramList中用于插入操作
			for(int i = 0 ; i < downList.size() ; i ++){
				Map<String,Object> paramMap = downList.get(i);
				String unitType = ConvertUtil.getString(paramMap.get("Type"));
				Map<String,Object> param = binOLMOCIO07_01_Service.searchParameter(paramMap);
				if("1".equals(unitType)){
					// 单位类型为区域【终端regiontarget表的取的是单位名称】
					int year=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(2, 4));
                	int month=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(4, 6));
                	mapIF.put("Year", year);
                	mapIF.put("Month",month);
                	mapIF.put("BrandCode",brandCode);
                	// 单位类型
                	mapIF.put("Type", paramMap.get("Type"));
                	mapIF.put("TargetMoney", paramMap.get("TargetMoney"));
                	mapIF.put("TargetQuantity", paramMap.get("TargetQuantity"));
                	// 单位名称
                	mapIF.put("Param",param.get("param"));
                	// 目标类型【目前只下发产品】
                	mapIF.put("TargetType", paramMap.get("TargetType"));
                	// 活动CODE
                	mapIF.put("ActivityCode", paramMap.get("ActivityCode"));
                	// 活动名称
                	mapIF.put("ActivityName", paramMap.get("ActivityName"));
                	mapIF.put("BIN_SaleDayTargetID",paramMap.get("BIN_SaleDayTargetID"));
                	mapIF.put("updatedBy", map.get("updatedBy"));
                	mapIF.put("updatePGM", map.get("updatePGM"));
                	// 日销售目标不写数据到终端，数据只存在新后台，通过同步状态来判断数据是否生效
//                	saleTargetSynchro_IF.synchroSaleTarget(mapIF);
                	binOLMOCIO07_01_Service.downUpdate(mapIF);
				} else if("2".equals(unitType) || "3".equals(unitType)) {
					// 单位类型为柜台或者美容顾问
					int year=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(2, 4));
                	int month=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(4, 6));
                	mapIF.put("Year", year);
                	mapIF.put("Month",month);
                	mapIF.put("BrandCode",brandCode);
                	mapIF.put("Type", paramMap.get("Type"));
                	mapIF.put("TargetMoney", paramMap.get("TargetMoney"));
                	mapIF.put("TargetQuantity", paramMap.get("TargetQuantity"));
                	mapIF.put("Param",param.get("paramID"));
                	// 目标类型【目前只下发产品】
                	mapIF.put("TargetType", paramMap.get("TargetType"));
                	// 活动CODE
                	mapIF.put("ActivityCode", paramMap.get("ActivityCode"));
                	// 活动名称
                	mapIF.put("ActivityName", paramMap.get("ActivityName"));
                	mapIF.put("BIN_SaleDayTargetID",paramMap.get("BIN_SaleDayTargetID"));
                	mapIF.put("updatedBy", map.get("updatedBy"));
                	mapIF.put("updatePGM", map.get("updatePGM"));
                	// 日销售目标不写数据到终端，数据只存在新后台，通过同步状态来判断数据是否生效
//                	saleTargetSynchro_IF.synchroSaleTarget(mapIF);
                	binOLMOCIO07_01_Service.downUpdate(mapIF);
				} else {
					// 单位类型为办事处、经销商、柜台主管
					mapIF.put("BIN_SaleDayTargetID",paramMap.get("BIN_SaleDayTargetID"));
					mapIF.put("updatedBy", map.get("updatedBy"));
                	mapIF.put("updatePGM", map.get("updatePGM"));
                	// 只更新新后台相关数据的状态，不实际下发数据
                	binOLMOCIO07_01_Service.downUpdate(mapIF);
				}
			}
		}else{
			throw new CherryException("IMO00057");
		}
	}

	@Override
	public void tran_setSaleTarget(Map<String, Object> map) throws CherryException {
		Map<String, Object> targetInfoMap = new HashMap<String, Object>();
		
		String[] parameterArr = (String[]) map.get("parameterArr");
		if ("".equals(ConvertUtil.getString(map.get("targetDate")))) {
			map.put("targetDate",
					CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		}
		if ("".equals(ConvertUtil.getString(map.get("targetMoney")))) {
			map.put("targetMoney", "0");
		}
		if ("".equals(ConvertUtil.getString(map.get("targetQuantity")))) {
			map.put("targetQuantity", "0");
		}
        for(int i=0;i<parameterArr.length;i++){
        	if(("").equals(parameterArr[i]) || null == parameterArr[i]){
        		i++;
        	} else {
        		targetInfoMap.put("type", map.get("type"));
        		targetInfoMap.put("parameter", parameterArr[i]);
        		targetInfoMap.put("targetDate", map.get("targetDate"));
        		targetInfoMap.put("targetType", map.get("targetType"));
        		
        		targetInfoMap.put("targetMoney", map.get("targetMoney"));
        		targetInfoMap.put("targetQuantity", map.get("targetQuantity"));
        		targetInfoMap.put("activityCode", map.get("activityCode"));
        		targetInfoMap.put("activityName", map.get("activityName"));
        		targetInfoMap.put("Source", CherryConstants.SALETARGET_SOURCE_BACKEND);
        		
        		targetInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
    					map.get(CherryConstants.ORGANIZATIONINFOID));
    			targetInfoMap.put(CherryConstants.BRANDINFOID,
    					map.get(CherryConstants.BRANDINFOID));
    			
    			targetInfoMap.put(CherryConstants.CREATEPGM, map.get("createPGM"));
    			targetInfoMap.put(CherryConstants.CREATEDBY, map.get("createdBy"));
    			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("createPGM"));
    			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("createdBy"));
    			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("updatedBy"));
    			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("updatePGM"));
    			binOLMOCIO07_01_Service.mergeSaleDayTarget(targetInfoMap);
            }
        }
	}
	
	/**
	 * 导入销售目标处理(统一在BINOLMOCIO07_BL中处理，因为目标日期类型是在EXCEL文件中，无法预先知晓)
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> resolveExcel(Map<String, Object> map)
			throws Exception {
		return null;
	}

	/**
	 * 导入销售目标处理
	 * 
	 * @param map
	 * @return 正常更新或者插入则返回1
	 * @throws Exception
	 */
	public int handleSetSaleTarget(Map<String, Object> map) throws Exception {
		// 记录销售目标信息
		Map<String, Object> targetInfoMap = new HashMap<String, Object>();
		String parameter = map.get("parameter").toString();
		// 未设置销售目标年月则默认为当月
		if (CherryChecker.isNullOrEmpty(map.get("targetDate"))) {
			targetInfoMap.put("targetDate",
					CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		}
		targetInfoMap.put("type", map.get("type"));
		targetInfoMap.put("parameter", parameter);
		
		targetInfoMap.put("targetType", map.get("targetType"));
		// 目标活动
		targetInfoMap.put("activityCode", map.get("activityCode"));
		targetInfoMap.put("activityName", map.get("activityName"));
		targetInfoMap.put("Source", CherryConstants.SALETARGET_SOURCE_BACKEND);
		
		// 目标月份
		String targetMonth = ConvertUtil.getString(map.get("targetDate"));
//		targetInfoMap.put("targetDate", map.get("targetDate"));
		// 销售指标
		targetInfoMap.put("targetMoney", map.get("targetMoney"));
		targetInfoMap.put("targetQuantity", map.get("targetQuantity"));
				
		
		try {
			
			targetInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
					map.get(CherryConstants.ORGANIZATIONINFOID));
			targetInfoMap.put(CherryConstants.BRANDINFOID,
					map.get(CherryConstants.BRANDINFOID));
			
			targetInfoMap.put(CherryConstants.CREATEPGM, map.get("createPGM"));
			targetInfoMap.put(CherryConstants.CREATEDBY, map.get("createdBy"));
			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("createPGM"));
			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("createdBy"));
			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("updatedBy"));
			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("updatePGM"));
			
			// 格式化下标
			String pattern="00";
			java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
			// 获取当月天数
			int moneyLen = ConvertUtil.getInt(DateUtil.getLastDateByMonth(targetMonth.substring(4, 6)));
			
			Map<String, Object> targetMap = new HashMap<String, Object>();
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Object> en = it.next();
				String key = en.getKey();
				String value = ConvertUtil.getString(en.getValue());
				
				if(key.startsWith("targetMoney")) {
					int index = ConvertUtil.getInt(key.substring("targetMoney".length(),key.length()));
					// 对于大于当月最大天数的目标直接忽略
					if(index > moneyLen) {
						continue;
					}
					String targetDate = targetMonth + df.format(index);
					if(targetMap.containsKey(targetDate)) {
						Map<String, Object> targetMoneyQuantityMap = (Map<String, Object>)targetMap.get(targetDate);
						targetMoneyQuantityMap.put("targetMoney", value);
//						targetMoneyQuantityMap.put("targetDate", targetDate);
						// 合并相同日期的金额及数量指标
						targetMap.put(targetDate, targetMoneyQuantityMap);
					} else {
						Map<String, Object> targetMoneyQuantityMap = new HashMap<String, Object>();
						targetMoneyQuantityMap.put("targetMoney", value);
//						targetMoneyQuantityMap.put("targetDate", targetDate);
						// 合并相同日期的金额及数量指标
						targetMap.put(targetDate, targetMoneyQuantityMap);
					}
					
				} else if(key.startsWith("targetQuantity")) {
					int index = ConvertUtil.getInt(key.substring("targetQuantity".length(),key.length()));
					// 对于大于当月最大天数的目标直接忽略
					if(index > moneyLen) {
						continue;
					}
					String targetDate = targetMonth + df.format(index);
					if(targetMap.containsKey(targetDate)) {
						Map<String, Object> targetMoneyQuantityMap = (Map<String, Object>)targetMap.get(targetDate);
						targetMoneyQuantityMap.put("targetQuantity", value);
//						targetMoneyQuantityMap.put("targetDate", targetDate);
						// 合并相同日期的金额及数量指标
						targetMap.put(targetDate, targetMoneyQuantityMap);
					} else {
						Map<String, Object> targetMoneyQuantityMap = new HashMap<String, Object>();
						targetMoneyQuantityMap.put("targetQuantity", value);
//						targetMoneyQuantityMap.put("targetDate", targetDate);
						// 合并相同日期的金额及数量指标
						targetMap.put(targetDate, targetMoneyQuantityMap);
					}
				}
			}
			
			Iterator<Entry<String, Object>> itMap = targetMap.entrySet().iterator();
			while(itMap.hasNext()) {
				Entry<String, Object> en = itMap.next();
				String key = en.getKey();
				targetInfoMap.put("targetDate", key);
				Map<String, Object> targetMoneyQuantityMap = (Map<String, Object>)en.getValue();
				targetInfoMap.put("targetQuantity", targetMoneyQuantityMap.get("targetQuantity"));
				targetInfoMap.put("targetMoney", targetMoneyQuantityMap.get("targetMoney"));
				// 目前活动不起作用，故不实现不同活动不同目标的功能【此设定逻辑放在SQL文中】
				binOLMOCIO07_01_Service.mergeSaleDayTarget(targetInfoMap);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		// 正常更新或者插入则返回1
		return 1;
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "different", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_different"), "15", "", "" },
				{ "parameterName", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_parameter"), "20", "", "" },
				{ "type", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_type"), "15", "", "1124" },
				{ "targetDate", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetDate"), "20", "", "" },
				{ "targetType", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetType"), "15", "", "1300" },
				{ "activity", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_activity"), "20", "", "" },
				{ "targetQuantity", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetQuantity"), "15", "", "" },
				{ "targetMoney", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetMoney"), "15", "", "" },
				{ "synchroFlag", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_downFlag"), "15", "", "1177" },
				{ "source", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_source"), "15", "", "1304" },
				{ "targetSetTime", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetSetTime"), "30", "", "" }
		};
		int dataLen = this.getSaleTargetCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "targetSheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "targetDownloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "targetSetTime desc");
		return map;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return this.searchSaleTargetList(map);
	}
}