/*	
 * @(#)BINOLCM39_BL.java     1.0 2013/08/15		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM33_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;

/**
 * 会员检索条件转换共通BL
 * 
 * @author WangCT
 * @version 1.0 2013/08/15	
 */
public class BINOLCM39_BL {
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/** 会员检索画面共通Service **/
	@Resource
	private BINOLCM33_Service binOLCM33_Service;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 把会员搜索条件转换为文字说明
	 * 
	 * @param conditionMap 检索条件
	 * @return 转换成文字说明的检索条件
	 */
	public String conditionDisplay(Map<String, Object> conditionMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(conditionMap);
		
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String andJoin = CherryConstants.SPACE + CherryUtil.getResourceValue(null, language, "global.page.andJoin") + CherryConstants.SPACE;
		String orJoin = CherryConstants.SPACE + CherryUtil.getResourceValue(null, language, "global.page.orJoin") + CherryConstants.SPACE;
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		List<String> conditionList = new ArrayList<String>();
		
		// 地址非空
		String addrNotEmpty = (String)map.get("addrNotEmpty");
		if(addrNotEmpty != null && !"".equals(addrNotEmpty)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.addrNotEmpty"));
		}
		// 电话非空
		String telNotEmpty = (String)map.get("telNotEmpty");
		if(telNotEmpty != null && !"".equals(telNotEmpty)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.telNotEmpty"));
		}
		// 手机验证
		String telCheck = (String)map.get("telCheck");
		if(telCheck != null && !"".equals(telCheck)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.telCheck"));
		}
		// 邮箱非空
		String emailNotEmpty = (String)map.get("emailNotEmpty");
		if(emailNotEmpty != null && !"".equals(emailNotEmpty)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.emailNotEmpty"));
		}
		// 发卡柜台非空
		String couNotEmpty = (String)map.get("couNotEmpty");
		if(couNotEmpty != null && !"".equals(couNotEmpty)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.couNotEmpty"));
		}
		// 仅停用会员
		String validFlag = (String)map.get("validFlag");
		if(validFlag != null && !"".equals(validFlag)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memValidFlag"));
		}
		// 仅资料不全的会员
		String memInfoRegFlg = (String)map.get("memInfoRegFlg");
		if(memInfoRegFlg != null && !"".equals(memInfoRegFlg)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memInfoRegFlg"));
		}
		// 只显示测试
		String testType = (String)map.get("testType");
		if(testType != null && !"".equals(testType)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memTestType"));
		}
		// 会员类型
		Object memType = (Object)map.get("memType");
		if(memType != null) {
			List<String> _memType = new ArrayList<String>();
			if(memType instanceof String) {
				if(!"".equals(memType.toString())) {
					_memType.add(memType.toString());
				}
			} else {
				_memType = (List)memType;
			}
			if(!_memType.isEmpty()) {
				StringBuffer memTypeCondition = new StringBuffer();
				if(_memType.size() > 1) {
					memTypeCondition.append("(");
				}
				for(int i = 0; i < _memType.size(); i++) {
					if(i > 0) {
						memTypeCondition.append(orJoin);
					}
					memTypeCondition.append(code.getVal("1204", _memType.get(i)));
				}
				if(_memType.size() > 1) {
					memTypeCondition.append(")");
				}
				conditionList.add(memTypeCondition.toString());
			}
		}
		// 会员等级
		Object memLevel = (Object)map.get("memLevel");
		if(memLevel != null) {
			List<String> _memLevel = new ArrayList<String>();
			if(memLevel instanceof String) {
				if(!"".equals(memLevel.toString())) {
					_memLevel.add(memLevel.toString());
				}
			} else {
				_memLevel = (List)memLevel;
			}
			if(!_memLevel.isEmpty()) {
				StringBuffer memLevelCondition = new StringBuffer();
				// 查询会员等级信息List
				List<Map<String, Object>> memLevelInfoList = binOLCM33_Service.getMemberLevelInfoList(map);
				if(_memLevel.size() > 1) {
					memLevelCondition.append("(");
				}
				for(int i = 0; i < _memLevel.size(); i++) {
					if(i > 0) {
						memLevelCondition.append(orJoin);
					}
					memLevelCondition.append(binOLCM33_BL.getMemLevelName(_memLevel.get(i), memLevelInfoList));
				}
				if(_memLevel.size() > 1) {
					memLevelCondition.append(")");
				}
				conditionList.add(memLevelCondition.toString());
			}
		}
		
		// 入会时间条件
		StringBuffer joinDateCondition = new StringBuffer();
		// 入会日期
		String joinDateMode = (String)map.get("joinDateMode");
		if(joinDateMode != null && !"".equals(joinDateMode)) {
			if("0".equals(joinDateMode)) {
				String joinDateRange = (String)map.get("joinDateRange");
				if(joinDateRange != null && !"".equals(joinDateRange)) {
					String joinDateUnit = (String)map.get("joinDateUnit");
					String joinDateUnitFlag = (String)map.get("joinDateUnitFlag");
					joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateDes")
							+joinDateRange+code.getVal("1239", joinDateUnit));
					if("1".equals(joinDateUnitFlag)) {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateUnitFlag1"));
					} else if("2".equals(joinDateUnitFlag)) {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateUnitFlag2"));
					}
				}
			} else if("1".equals(joinDateMode)) {
				joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curJoinDate"));
			} else if("9".equals(joinDateMode)) {
				String joinDateStart = (String)map.get("joinDateStart");
				String joinDateEnd = (String)map.get("joinDateEnd");
				if(joinDateStart != null && !"".equals(joinDateStart)) {
					if(joinDateEnd != null && !"".equals(joinDateEnd)) {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDate")+":"+joinDateStart+"-"+joinDateEnd);
					} else {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDate")+":"+joinDateStart+"-"+notLimit);
					}
				} else {
					if(joinDateEnd != null && !"".equals(joinDateEnd)) {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDate")+":"+notLimit+"-"+joinDateEnd);
					}
				}
			}
		}
		// 会员标签
		if(binOLCM14_BL.isConfigOpen("1339", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)))) {
			// 是否新会员
			String isNewMember = (String) map.get("isNewMember");
			String text = null;
			if (!CherryChecker.isNullOrEmpty(isNewMember)) {
				if ("1".equals(isNewMember)) {
					text = CherryUtil.getResourceValue(null, language, "global.page.yes");
				} else {
					text = CherryUtil.getResourceValue(null, language, "global.page.no");
				}
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.isNewMember")+":"+ text);
			}
			
			// 本年购买次数
			String flagBuyCount = (String) map.get("flagBuyCount");
			if (!CherryChecker.isNullOrEmpty(flagBuyCount)) {
				if ("N1".equals(flagBuyCount)) {
					text = CherryUtil.getResourceValue(null, language, "global.page.flagBuyCountVal1");
				} else if ("N2".equals(flagBuyCount)){
					text = CherryUtil.getResourceValue(null, language, "global.page.flagBuyCountVal2");
				} else {
					text = CherryUtil.getResourceValue(null, language, "global.page.flagBuyCountVal3");
				}
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.flagBuyCount")+":"+text);
			}
			// 是否敏感会员
			String isActivityMember = (String) map.get("isActivityMember");
			if (!CherryChecker.isNullOrEmpty(isActivityMember)) {
				if ("1".equals(isActivityMember)) {
					text = CherryUtil.getResourceValue(null, language, "global.page.yes");
				} else {
					text = CherryUtil.getResourceValue(null, language, "global.page.no");
				}
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.isActivityMember")+":"+ text);
			}
			// 活动次数(敏感度)
			String actiCountStart = (String) map.get("actiCountStart");
			String actiCountEnd = (String) map.get("actiCountEnd");
			boolean isStart = !CherryChecker.isNullOrEmpty(actiCountStart);
			boolean isEnd = !CherryChecker.isNullOrEmpty(actiCountEnd);
			if (isStart || isEnd) {
				text = CherryUtil.getResourceValue(null, language, "global.page.actiCount") + ":";
				if (isStart) {
					text += actiCountStart;
				} else {
					text += notLimit;
				}
				if (isEnd) {
					text += "-" + actiCountEnd;
				} else {
					text += "-" + notLimit;
				}
				conditionList.add(text);
			}
			// 最喜欢的活动类型
			String favActiType = (String) map.get("favActiType");
			if (!CherryChecker.isNullOrEmpty(favActiType)) {
				if ("1".equals(isActivityMember)) {
					text = CherryUtil.getResourceValue(null, language, "global.page.favActiType1");
				} else {
					text = CherryUtil.getResourceValue(null, language, "global.page.favActiType2");
				}
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.favActiType")+":"+ text);
			}
			// 购买间隔周期
			String unBuyInterval = (String) map.get("unBuyInterval");
			if (!CherryChecker.isNullOrEmpty(unBuyInterval)) {
				if ("3".equals(unBuyInterval)) {
					text = CherryUtil.getResourceValue(null, language, "global.page.unBuyInterval1");
				} else if ("6".equals(unBuyInterval)){
					text = CherryUtil.getResourceValue(null, language, "global.page.unBuyInterval2");
				} else if ("9".equals(unBuyInterval)){
					text = CherryUtil.getResourceValue(null, language, "global.page.unBuyInterval3");
				} else {
					text = CherryUtil.getResourceValue(null, language, "global.page.unBuyInterval4");
				}
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.unBuyInterval")+":"+text);
			}
			// 最多购买的产品大类
			Object mostCateId = (Object)map.get("mostCateBClassId");
			if(mostCateId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(mostCateId instanceof String) {
					if(!"".equals(mostCateId.toString())) {
						_cateValId.add(mostCateId.toString());
					}
				} else {
					_cateValId = (List)mostCateId;
				}
				if(!_cateValId.isEmpty()) {
					StringBuffer buyCateCondition = new StringBuffer();
					map.put("cateValId", _cateValId);
					List<Map<String, Object>> proCatInfoList = binOLCM33_Service.getProTypeInfoList(map);
					if(_cateValId.size() > 1) {
						buyCateCondition.append("(");
					}
					buyCateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.mostCateBClass")+":");
					for(int i = 0; i < _cateValId.size(); i++) {
						if(i > 0) {
							buyCateCondition.append(orJoin);
						}
						if(proCatInfoList != null && !proCatInfoList.isEmpty()) {
							for(Map<String, Object> proCatInfoMap : proCatInfoList) {
								String cateValIdTemp = proCatInfoMap.get("cateValId").toString();
								if(cateValIdTemp.equals(_cateValId.get(i))) {
									buyCateCondition.append(proCatInfoMap.get("cateValName"));
									break;
								}
							}
						}
					}
					if(_cateValId.size() > 1) {
						buyCateCondition.append(")");
					}
					conditionList.add(buyCateCondition.toString());
				}
			}
			// 最多购买的产品大类
			Object mostCateMClassId = (Object)map.get("mostCateMClassId");
			if(mostCateMClassId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(mostCateMClassId instanceof String) {
					if(!"".equals(mostCateMClassId.toString())) {
						_cateValId.add(mostCateMClassId.toString());
					}
				} else {
					_cateValId = (List)mostCateMClassId;
				}
				if(!_cateValId.isEmpty()) {
					StringBuffer buyCateCondition = new StringBuffer();
					map.put("cateValId", _cateValId);
					List<Map<String, Object>> proCatInfoList = binOLCM33_Service.getProTypeInfoList(map);
					if(_cateValId.size() > 1) {
						buyCateCondition.append("(");
					}
					buyCateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.mostCateMClass")+":");
					for(int i = 0; i < _cateValId.size(); i++) {
						if(i > 0) {
							buyCateCondition.append(orJoin);
						}
						if(proCatInfoList != null && !proCatInfoList.isEmpty()) {
							for(Map<String, Object> proCatInfoMap : proCatInfoList) {
								String cateValIdTemp = proCatInfoMap.get("cateValId").toString();
								if(cateValIdTemp.equals(_cateValId.get(i))) {
									buyCateCondition.append(proCatInfoMap.get("cateValName"));
									break;
								}
							}
						}
					}
					if(_cateValId.size() > 1) {
						buyCateCondition.append(")");
					}
					conditionList.add(buyCateCondition.toString());
				}
			}
			// 最多购买的产品
			Object mostPrtId = (Object)map.get("mostPrtId");
			if(mostPrtId != null) {
				List<String> _prtVendorId = new ArrayList<String>();
				if(mostPrtId instanceof String) {
					if(!"".equals(mostPrtId.toString())) {
						_prtVendorId.add(mostPrtId.toString());
					}
				} else {
					_prtVendorId = (List)mostPrtId;
				}
				if(!_prtVendorId.isEmpty()) {
					StringBuffer buyPrtCondition = new StringBuffer();
					map.put("prtVendorId", _prtVendorId);
					List<Map<String, Object>> proInfoList = binOLCM33_Service.getProInfoList(map);
					if(_prtVendorId.size() > 1) {
						buyPrtCondition.append("(");
					}
					buyPrtCondition.append(CherryUtil.getResourceValue(null, language, "global.page.mostPrt")+":");
					for(int i = 0; i < _prtVendorId.size(); i++) {
						if(i > 0) {
							buyPrtCondition.append(orJoin);
						}
						if(proInfoList != null && !proInfoList.isEmpty()) {
							for(Map<String, Object> proInfoMap : proInfoList) {
								String prtVendorIdTemp = proInfoMap.get("prtVendorId").toString();
								if(prtVendorIdTemp.equals(_prtVendorId.get(i))) {
									buyPrtCondition.append(proInfoMap.get("nameTotal"));
									break;
								}
							}
						}
					}
					if(_prtVendorId.size() > 1) {
						buyPrtCondition.append(")");
					}
					conditionList.add(buyPrtCondition.toString());
				}
			}
//			// 连带购买产品系列
//			Object jointCateId = (Object)map.get("jointCateId");
//			if(jointCateId != null) {
//				List<String> _cateValId = new ArrayList<String>();
//				if(jointCateId instanceof String) {
//					if(!"".equals(jointCateId.toString())) {
//						_cateValId.add(jointCateId.toString());
//					}
//				} else {
//					_cateValId = (List)jointCateId;
//				}
//				if(!_cateValId.isEmpty()) {
//					StringBuffer buyCateCondition = new StringBuffer();
//					map.put("cateValId", _cateValId);
//					List<Map<String, Object>> proCatInfoList = binOLCM33_Service.getProTypeInfoList(map);
//					if(_cateValId.size() > 1) {
//						buyCateCondition.append("(");
//					}
//					buyCateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.jointCate")+":");
//					for(int i = 0; i < _cateValId.size(); i++) {
//						if(i > 0) {
//							buyCateCondition.append(orJoin);
//						}
//						if(proCatInfoList != null && !proCatInfoList.isEmpty()) {
//							for(Map<String, Object> proCatInfoMap : proCatInfoList) {
//								String cateValIdTemp = proCatInfoMap.get("cateValId").toString();
//								if(cateValIdTemp.equals(_cateValId.get(i))) {
//									buyCateCondition.append(proCatInfoMap.get("cateValName"));
//									break;
//								}
//							}
//						}
//					}
//					if(_cateValId.size() > 1) {
//						buyCateCondition.append(")");
//					}
//					conditionList.add(buyCateCondition.toString());
//				}
//			}
//			// 连带购买的产品
//			Object jointPrtId = (Object)map.get("jointPrtId");
//			if(jointPrtId != null) {
//				List<String> _prtVendorId = new ArrayList<String>();
//				if(jointPrtId instanceof String) {
//					if(!"".equals(jointPrtId.toString())) {
//						_prtVendorId.add(jointPrtId.toString());
//					}
//				} else {
//					_prtVendorId = (List)jointPrtId;
//				}
//				if(!_prtVendorId.isEmpty()) {
//					StringBuffer buyPrtCondition = new StringBuffer();
//					map.put("prtVendorId", _prtVendorId);
//					List<Map<String, Object>> proInfoList = binOLCM33_Service.getProInfoList(map);
//					if(_prtVendorId.size() > 1) {
//						buyPrtCondition.append("(");
//					}
//					buyPrtCondition.append(CherryUtil.getResourceValue(null, language, "global.page.jointPrt")+":");
//					for(int i = 0; i < _prtVendorId.size(); i++) {
//						if(i > 0) {
//							buyPrtCondition.append(orJoin);
//						}
//						if(proInfoList != null && !proInfoList.isEmpty()) {
//							for(Map<String, Object> proInfoMap : proInfoList) {
//								String prtVendorIdTemp = proInfoMap.get("prtVendorId").toString();
//								if(prtVendorIdTemp.equals(_prtVendorId.get(i))) {
//									buyPrtCondition.append(proInfoMap.get("nameTotal"));
//									break;
//								}
//							}
//						}
//					}
//					if(_prtVendorId.size() > 1) {
//						buyPrtCondition.append(")");
//					}
//					conditionList.add(buyPrtCondition.toString());
//				}
//			}
			// 客单价
			String pctStart = (String) map.get("pctStart");
			String pctEnd = (String) map.get("pctEnd");
			isStart = !CherryChecker.isNullOrEmpty(pctStart);
			isEnd = !CherryChecker.isNullOrEmpty(pctEnd);
			if (isStart || isEnd) {
				text = CherryUtil.getResourceValue(null, language, "global.page.pct") + ":";
				if (isStart) {
					text += pctStart;
				} else {
					text += notLimit;
				}
				if (isEnd) {
					text += "-" + pctEnd;
				} else {
					text += "-" + notLimit;
				}
				conditionList.add(text);
			}
		} 
		// 购买条件查询方式
		String isSaleFlag = (String)map.get("isSaleFlag");
		// 无购买查询条件的场合
		if(isSaleFlag != null && "0".equals(isSaleFlag)) {
			if(joinDateCondition.length() > 0) {
				conditionList.add(joinDateCondition.toString());
			}
			// 无购买时间模式
			String notSaleTimeMode = (String)map.get("notSaleTimeMode");
			if(notSaleTimeMode != null && !"".equals(notSaleTimeMode)) {
				if("0".equals(notSaleTimeMode)) {
					String notSaleTimeRange = (String)map.get("notSaleTimeRange");
					if(notSaleTimeRange != null && !"".equals(notSaleTimeRange)) {
						String notSaleTimeUnit = (String)map.get("notSaleTimeUnit");
						conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.notSaleDateDes")
								+notSaleTimeRange+code.getVal("1239", notSaleTimeUnit)
								+CherryUtil.getResourceValue(null, language, "global.page.notSaleTimeUnitFlag"));
					}
				} else if("9".equals(notSaleTimeMode)) {
					String notSaleTimeStart = (String)map.get("notSaleTimeStart");
					String notSaleTimeEnd = (String)map.get("notSaleTimeEnd");
					if(notSaleTimeStart != null && !"".equals(notSaleTimeStart)) {
						if(notSaleTimeEnd != null && !"".equals(notSaleTimeEnd)) {
							conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.notSaleTime")+":"+notSaleTimeStart+"-"+notSaleTimeEnd);
						} else {
							conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.notSaleTime")+":"+notSaleTimeStart+"-"+notLimit);
						}
					} else {
						if(notSaleTimeEnd != null && !"".equals(notSaleTimeEnd)) {
							conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.notSaleTime")+":"+notLimit+"-"+notSaleTimeEnd);
						}
					}
				} else if ("8".equals(notSaleTimeMode)) {
					String notSaleTimeRangeLast = (String)map.get("notSaleTimeRangeLast");
					if(notSaleTimeRangeLast != null && !"".equals(notSaleTimeRangeLast)) {
						conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.lastSaleDay") + " " 
								+ CherryUtil.getResourceValue(null, language, "global.page.notSaleDateDes")
								+ notSaleTimeRangeLast + CherryUtil.getResourceValue(null, language, "global.page.Day")
								+ CherryUtil.getResourceValue(null, language, "global.page.notSaleTimeUnitFlag"));
					}
				}
			}
		} else { // 有购买查询条件的场合
			// 购买记录条件(仅包含购买时间和购买柜台条件)
			List<String> saleCondition1List = new ArrayList<String>();
			// 购买记录条件(仅包含购买次数的条件)
			List<String> saleCondition2List = new ArrayList<String>();
			// 购买记录条件(仅包含购买金额、购买产品、未购买产品的条件)
			List<String> saleCondition3List = new ArrayList<String>();
			// 购买时间
			String saleTimeMode = (String)map.get("saleTimeMode");
			if(saleTimeMode != null && !"".equals(saleTimeMode)) {
				if("0".equals(saleTimeMode)) {
					String saleTimeRange = (String)map.get("saleTimeRange");
					if(saleTimeRange != null && !"".equals(saleTimeRange)) {
						String saleTimeUnit = (String)map.get("saleTimeUnit");
						saleCondition1List.add(CherryUtil.getResourceValue(null, language, "global.page.saleDateDes")
								+saleTimeRange+code.getVal("1239", saleTimeUnit)
								+CherryUtil.getResourceValue(null, language, "global.page.saleTimeUnitFlag"));
					}
				} else if("9".equals(saleTimeMode)) {
					String saleTimeStart = (String)map.get("saleTimeStart");
					String saleTimeEnd = (String)map.get("saleTimeEnd");
					if(saleTimeStart != null && !"".equals(saleTimeStart)) {
						if(saleTimeEnd != null && !"".equals(saleTimeEnd)) {
							saleCondition1List.add(CherryUtil.getResourceValue(null, language, "global.page.saleTime")+":"+saleTimeStart+"-"+saleTimeEnd);
						} else {
							saleCondition1List.add(CherryUtil.getResourceValue(null, language, "global.page.saleTime")+":"+saleTimeStart+"-"+notLimit);
						}
					} else {
						if(saleTimeEnd != null && !"".equals(saleTimeEnd)) {
							saleCondition1List.add(CherryUtil.getResourceValue(null, language, "global.page.saleTime")+":"+notLimit+"-"+saleTimeEnd);
						}
					}
				}
			}
			// 购买柜台
			String saleCounterId = (String)map.get("saleCounterId");
			if(saleCounterId != null && !"".equals(saleCounterId)) {
				List<String> organizationIdList = new ArrayList<String>();
				organizationIdList.add(saleCounterId);
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("organizationIdList", organizationIdList);
				organizationIdList = binOLCM33_Service.getOrganizationList(param);
				if(organizationIdList != null && !organizationIdList.isEmpty()) {
					saleCondition1List.add(CherryUtil.getResourceValue(null, language, "global.page.saleCounter")+":"+organizationIdList.get(0));
				}
			}
			// 购买柜台
			String modeFlag = (String)map.get("saleModeFlag");
			if(modeFlag != null && !"".equals(modeFlag)) {
				StringBuffer counterCondition = new StringBuffer();
				List<String> counterNameList = new ArrayList<String>();
				if("1".equals(modeFlag)) {
					List<String> regionIdList = new ArrayList<String>();
					String regionId = (String)map.get("saleRegionId");
					if(regionId != null && !"".equals(regionId)) {
						regionIdList.addAll(Arrays.asList(regionId.split(",")));
					}
					String provinceId = (String)map.get("saleProvinceId");
					if(provinceId != null && !"".equals(provinceId)) {
						regionIdList.addAll(Arrays.asList(provinceId.split(",")));
					}
					String cityId = (String)map.get("saleCityId");
					if(cityId != null && !"".equals(cityId)) {
						regionIdList.addAll(Arrays.asList(cityId.split(",")));
					}
					if(regionIdList != null && !regionIdList.isEmpty()) {
						map.put("regionIdList", regionIdList);
						regionIdList = binOLCM33_Service.getRegionList(map);
						if(regionIdList != null && !regionIdList.isEmpty()) {
							counterNameList.addAll(regionIdList);
						}
					}
				} else if("2".equals(modeFlag)) {
					List<String> regionIdList = new ArrayList<String>();
					String channelRegionId = (String)map.get("saleChannelRegionId");
					if(channelRegionId != null && !"".equals(channelRegionId)) {
						regionIdList.addAll(Arrays.asList(channelRegionId.split(",")));
					}
					if(regionIdList != null && !regionIdList.isEmpty()) {
						map.put("regionIdList", regionIdList);
						regionIdList = binOLCM33_Service.getRegionList(map);
						if(regionIdList != null && !regionIdList.isEmpty()) {
							counterNameList.addAll(regionIdList);
						}
					}
					List<String> channelIdList = new ArrayList<String>();
					String channelId = (String)map.get("saleChannelId");
					if(channelId != null && !"".equals(channelId)) {
						channelIdList.addAll(Arrays.asList(channelId.split(",")));
					}
					if(channelIdList != null && !channelIdList.isEmpty()) {
						map.put("channelIdList", channelIdList);
						channelIdList = binOLCM33_Service.getChannelList(map);
						if(channelIdList != null && !channelIdList.isEmpty()) {
							counterNameList.addAll(channelIdList);
						}
					}
				} else if("4".equals(modeFlag) || "5".equals(modeFlag)) {
					String regionId = (String)map.get("saleBelongId");
					if(regionId != null && !"".equals(regionId)) {
						List<String> belongIdList = new ArrayList<String>();
						belongIdList.addAll(Arrays.asList(regionId.split(",")));
						List<Map<String, Object>> list = code.getCodes("1309");
						if(null != list && !list.isEmpty()){
							for (int i = 0; i < belongIdList.size(); i++) {
								String bid = belongIdList.get(i);
								for (Map<String, Object> codeInfo : list) {
									if (bid.equals(codeInfo.get("CodeKey"))) {
										counterNameList.add((String) codeInfo.get("Value"));
										break;
									}
								}
							}
						}
					}
				}
				List<String> organizationIdList = new ArrayList<String>();
				String memCounterId = (String)map.get("saleMemCounterId");
				if(memCounterId != null && !"".equals(memCounterId)) {
					organizationIdList.addAll(Arrays.asList(memCounterId.split(",")));
				}
				if(organizationIdList != null && !organizationIdList.isEmpty()) {
					map.put("organizationIdList", organizationIdList);
					organizationIdList = binOLCM33_Service.getOrganizationList(map);
					if(organizationIdList != null && !organizationIdList.isEmpty()) {
						counterNameList.addAll(organizationIdList);
					}
				}
				if(counterNameList != null && !counterNameList.isEmpty()) {
					String couValidFlag = (String)map.get("saleCouValidFlag");
					if("0".equals(couValidFlag)) {
						counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.saleCounter")
								+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag0")+")"+":");
					} else if("1".equals(couValidFlag)) {
						counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.saleCounter")
								+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag1")+")"+":");
					}
					for(int j = 0; j < counterNameList.size(); j++) {
						if(j > 0) {
							counterCondition.append(",");
						}
						counterCondition.append(counterNameList.get(j));
					}
					saleCondition1List.add(counterCondition.toString());
				}
			}
			
			//购买子品牌
			String saleSubBrand = (String) map.get("saleSubBrand");
			if(saleSubBrand != null && !"".equals(saleSubBrand)){
				saleCondition2List.add(CherryUtil.getResourceValue(null, language, "global.page.saleSubBrand")+":"+code.getVal("1299", saleSubBrand));
			}
		
			// 购买次数
			String saleCountStart = (String)map.get("saleCountStart");
			String saleCountEnd = (String)map.get("saleCountEnd");
			if(saleCountStart != null && !"".equals(saleCountStart)) {
				if(saleCountEnd != null && !"".equals(saleCountEnd)) {
					saleCondition2List.add(CherryUtil.getResourceValue(null, language, "global.page.saleCount")+":"+saleCountStart+"-"+saleCountEnd);
				} else {
					saleCondition2List.add(CherryUtil.getResourceValue(null, language, "global.page.saleCount")+":"+saleCountStart+"-"+notLimit);
				}
			} else {
				if(saleCountEnd != null && !"".equals(saleCountEnd)) {
					saleCondition2List.add(CherryUtil.getResourceValue(null, language, "global.page.saleCount")+":"+notLimit+"-"+saleCountEnd);
				}
			}
			// 购买支数
			String payQuantityStart = (String)map.get("payQuantityStart");
			String payQuantityEnd = (String)map.get("payQuantityEnd");
			if(payQuantityStart != null && !"".equals(payQuantityStart)) {
				if(payQuantityEnd != null && !"".equals(payQuantityEnd)) {
					saleCondition3List.add(CherryUtil.getResourceValue(null, language, "global.page.payQuantity")+":"+payQuantityStart+"-"+payQuantityEnd);
				} else {
					saleCondition3List.add(CherryUtil.getResourceValue(null, language, "global.page.payQuantity")+":"+payQuantityStart+"-"+notLimit);
				}
			} else {
				if(payQuantityEnd != null && !"".equals(payQuantityEnd)) {
					saleCondition3List.add(CherryUtil.getResourceValue(null, language, "global.page.payQuantity")+":"+notLimit+"-"+payQuantityEnd);
				}
			}
			// 购买金额
			String payAmountStart = (String)map.get("payAmountStart");
			String payAmountEnd = (String)map.get("payAmountEnd");
			if(payAmountStart != null && !"".equals(payAmountStart)) {
				if(payAmountEnd != null && !"".equals(payAmountEnd)) {
					saleCondition3List.add(CherryUtil.getResourceValue(null, language, "global.page.payAmount")+":"+payAmountStart+"-"+payAmountEnd);
				} else {
					saleCondition3List.add(CherryUtil.getResourceValue(null, language, "global.page.payAmount")+":"+payAmountStart+"-"+notLimit);
				}
			} else {
				if(payAmountEnd != null && !"".equals(payAmountEnd)) {
					saleCondition3List.add(CherryUtil.getResourceValue(null, language, "global.page.payAmount")+":"+notLimit+"-"+payAmountEnd);
				}
			}
			// 购买产品
			Object prtVendorId = (Object)map.get("buyPrtVendorId");
			// 产品之间关系
			Object relation = (Object)map.get("relation");
			if(prtVendorId != null) {
				List<String> _prtVendorId = new ArrayList<String>();
				if(prtVendorId instanceof String) {
					if(!"".equals(prtVendorId.toString())) {
						_prtVendorId.add(prtVendorId.toString());
					}
				} else {
					_prtVendorId = (List)prtVendorId;
				}
				if(!_prtVendorId.isEmpty()) {
					StringBuffer buyPrtCondition = new StringBuffer();
					map.put("prtVendorId", _prtVendorId);
					List<Map<String, Object>> proInfoList = binOLCM33_Service.getProInfoList(map);
					if(_prtVendorId.size() > 1) {
						buyPrtCondition.append("(");
					}
					buyPrtCondition.append(CherryUtil.getResourceValue(null, language, "global.page.saleProduct")+":");
					for(int i = 0; i < _prtVendorId.size(); i++) {
						if(i > 0) {
							if(relation != null && "1".equals(relation.toString())) {
								buyPrtCondition.append(andJoin);
							} else {
								buyPrtCondition.append(orJoin);
							}
						}
						if(proInfoList != null && !proInfoList.isEmpty()) {
							for(Map<String, Object> proInfoMap : proInfoList) {
								String prtVendorIdTemp = proInfoMap.get("prtVendorId").toString();
								if(prtVendorIdTemp.equals(_prtVendorId.get(i))) {
									buyPrtCondition.append(proInfoMap.get("nameTotal"));
									break;
								}
							}
						}
					}
					if(_prtVendorId.size() > 1) {
						buyPrtCondition.append(")");
					}
					saleCondition3List.add(buyPrtCondition.toString());
				}
			}
			// 购买产品分类
			Object cateValId = (Object)map.get("buyCateValId");
			if(cateValId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(cateValId instanceof String) {
					if(!"".equals(cateValId.toString())) {
						_cateValId.add(cateValId.toString());
					}
				} else {
					_cateValId = (List)cateValId;
				}
				if(!_cateValId.isEmpty()) {
					StringBuffer buyCateCondition = new StringBuffer();
					map.put("cateValId", _cateValId);
					List<Map<String, Object>> proCatInfoList = binOLCM33_Service.getProTypeInfoList(map);
					if(_cateValId.size() > 1) {
						buyCateCondition.append("(");
					}
					buyCateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.saleProduct")+":");
					for(int i = 0; i < _cateValId.size(); i++) {
						if(i > 0) {
							if(relation != null && "1".equals(relation.toString())) {
								buyCateCondition.append(andJoin);
							} else {
								buyCateCondition.append(orJoin);
							}
						}
						if(proCatInfoList != null && !proCatInfoList.isEmpty()) {
							for(Map<String, Object> proCatInfoMap : proCatInfoList) {
								String cateValIdTemp = proCatInfoMap.get("cateValId").toString();
								if(cateValIdTemp.equals(_cateValId.get(i))) {
									buyCateCondition.append(proCatInfoMap.get("cateValName"));
									break;
								}
							}
						}
					}
					if(_cateValId.size() > 1) {
						buyCateCondition.append(")");
					}
					saleCondition3List.add(buyCateCondition.toString());
				}
			}
			// 未购买产品
			Object notPrtVendorId = (Object)map.get("notPrtVendorId");
			// 产品之间关系
			Object notRelation = (Object)map.get("notRelation");
			if(notPrtVendorId != null) {
				List<String> _prtVendorId = new ArrayList<String>();
				if(notPrtVendorId instanceof String) {
					if(!"".equals(notPrtVendorId.toString())) {
						_prtVendorId.add(notPrtVendorId.toString());
					}
				} else {
					_prtVendorId = (List)notPrtVendorId;
				}
				if(!_prtVendorId.isEmpty()) {
					StringBuffer notPrtCondition = new StringBuffer();
					map.put("prtVendorId", _prtVendorId);
					List<Map<String, Object>> notProInfoList = binOLCM33_Service.getProInfoList(map);
					if(_prtVendorId.size() > 1) {
						notPrtCondition.append("(");
					}
					notPrtCondition.append(CherryUtil.getResourceValue(null, language, "global.page.notSaleProduct")+":");
					for(int i = 0; i < _prtVendorId.size(); i++) {
						if(i > 0) {
							if(notRelation != null && "1".equals(notRelation.toString())) {
								notPrtCondition.append(andJoin);
							} else {
								notPrtCondition.append(orJoin);
							}
						}
						if(notProInfoList != null && !notProInfoList.isEmpty()) {
							for(Map<String, Object> notProInfoMap : notProInfoList) {
								String prtVendorIdTemp = notProInfoMap.get("prtVendorId").toString();
								if(prtVendorIdTemp.equals(_prtVendorId.get(i))) {
									notPrtCondition.append(notProInfoMap.get("nameTotal"));
									break;
								}
							}
						}
					}
					if(_prtVendorId.size() > 1) {
						notPrtCondition.append(")");
					}
					saleCondition3List.add(notPrtCondition.toString());
				}
			}
			// 未购买产品分类
			Object notCateValId = (Object)map.get("notCateValId");
			if(notCateValId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(notCateValId instanceof String) {
					if(!"".equals(notCateValId.toString())) {
						_cateValId.add(notCateValId.toString());
					}
				} else {
					_cateValId = (List)notCateValId;
				}
				if(!_cateValId.isEmpty()) {
					StringBuffer notCateCondition = new StringBuffer();
					map.put("cateValId", _cateValId);
					List<Map<String, Object>> notProCatInfoList = binOLCM33_Service.getProTypeInfoList(map);
					if(_cateValId.size() > 1) {
						notCateCondition.append("(");
					}
					notCateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.notSaleProduct")+":");
					for(int i = 0; i < _cateValId.size(); i++) {
						if(i > 0) {
							if(notRelation != null && "1".equals(notRelation.toString())) {
								notCateCondition.append(andJoin);
							} else {
								notCateCondition.append(orJoin);
							}
						}
						if(notProCatInfoList != null && !notProCatInfoList.isEmpty()) {
							for(Map<String, Object> notProCatInfoMap : notProCatInfoList) {
								String cateValIdTemp = notProCatInfoMap.get("cateValId").toString();
								if(cateValIdTemp.equals(_cateValId.get(i))) {
									notCateCondition.append(notProCatInfoMap.get("cateValName"));
									break;
								}
							}
						}
					}
					if(_cateValId.size() > 1) {
						notCateCondition.append(")");
					}
					saleCondition3List.add(notCateCondition.toString());
				}
			}
			
			// 入会时间与购买时间关系
			String joinDateSaleDateRel = (String)map.get("joinDateSaleDateRel");
			// 入会时间和购买时间是OR关系而且入会时间和购买时间都存在判断
			boolean joinDateOrSaleDate = false;
			if(joinDateSaleDateRel != null && "2".equals(joinDateSaleDateRel)) {
				if(joinDateMode != null && !"".equals(joinDateMode) && !"-1".equals(joinDateMode)) {
					if((saleTimeMode != null && !"".equals(saleTimeMode) && !"-1".equals(saleTimeMode))
							|| (saleCounterId != null && !"".equals(saleCounterId))
							|| (modeFlag != null && !"".equals(modeFlag))
							|| (saleCountStart != null && !"".equals(saleCountStart))
							|| (saleCountEnd != null && !"".equals(saleCountEnd))) {
						joinDateOrSaleDate = true;
					}
				}
			}
			// 入会时间和购买时间是OR关系而且入会时间和购买时间都存在的场合
			if(joinDateOrSaleDate) {
				StringBuffer joinDateSaleDateCondition = new StringBuffer();
				joinDateSaleDateCondition.append("(");
				joinDateSaleDateCondition.append(joinDateCondition.toString());
				joinDateSaleDateCondition.append(orJoin);
				
				// 购买记录条件(包含购买时间、购买柜台、购买次数条件)
				StringBuffer saleCondition = new StringBuffer();
				List<String> saleConditionList = new ArrayList<String>();
				saleConditionList.addAll(saleCondition1List);
				saleConditionList.addAll(saleCondition2List);
				if(saleConditionList.size() > 1) {
					saleCondition.append("(");
				}
				for(int i = 0; i < saleConditionList.size(); i++) {
					if(i > 0) {
						saleCondition.append(andJoin);
					}
					saleCondition.append(saleConditionList.get(i));
				}
				if(saleConditionList.size() > 1) {
					saleCondition.append(")");
				}
				joinDateSaleDateCondition.append(saleCondition.toString());
				joinDateSaleDateCondition.append(")");
				conditionList.add(joinDateSaleDateCondition.toString());
				
				if(saleCondition3List != null && !saleCondition3List.isEmpty()) {
					// 购买记录条件(包含购买时间、购买柜台、购买金额、购买产品、未购买产品条件)
					saleCondition = new StringBuffer();
					saleConditionList = new ArrayList<String>();
					saleConditionList.addAll(saleCondition1List);
					saleConditionList.addAll(saleCondition3List);
					if(saleConditionList.size() > 1) {
						saleCondition.append("(");
					}
					for(int i = 0; i < saleConditionList.size(); i++) {
						if(i > 0) {
							saleCondition.append(andJoin);
						}
						saleCondition.append(saleConditionList.get(i));
					}
					if(saleConditionList.size() > 1) {
						saleCondition.append(")");
					}
					conditionList.add(saleCondition.toString());
				}
			} else {
				if(joinDateCondition.length() > 0) {
					conditionList.add(joinDateCondition.toString());
				}
				List<String> saleConditionList = new ArrayList<String>();
				saleConditionList.addAll(saleCondition1List);
				saleConditionList.addAll(saleCondition2List);
				saleConditionList.addAll(saleCondition3List);
				if(saleConditionList != null && !saleConditionList.isEmpty()) {
					StringBuffer saleCondition = new StringBuffer();
					if(saleConditionList.size() > 1) {
						saleCondition.append("(");
					}
					for(int i = 0; i < saleConditionList.size(); i++) {
						if(i > 0) {
							saleCondition.append(andJoin);
						}
						saleCondition.append(saleConditionList.get(i));
					}
					if(saleConditionList.size() > 1) {
						saleCondition.append(")");
					}
					conditionList.add(saleCondition.toString());
				}
			}
		}
		
		// 生日条件
		StringBuffer birthDayCondition = new StringBuffer();
		// 生日
		String birthDayMode = (String)map.get("birthDayMode");
		if(birthDayMode != null && !"".equals(birthDayMode)) {
			if("0".equals(birthDayMode)) {
				birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curBirthDayMonth"));
				String birthDayDateStart = (String)map.get("birthDayDateStart");
				String birthDayDateEnd = (String)map.get("birthDayDateEnd");
				String dateText = CherryUtil.getResourceValue(null, language, "global.page.date");
				if(birthDayDateStart != null && !"".equals(birthDayDateStart)) {
					if(birthDayDateEnd != null && !"".equals(birthDayDateEnd)) {
						birthDayCondition.append(":"+birthDayDateStart+dateText+"-"+birthDayDateEnd+dateText);
					} else {
						birthDayCondition.append(":"+birthDayDateStart+dateText+"-"+notLimit);
					}
				} else {
					if(birthDayDateEnd != null && !"".equals(birthDayDateEnd)) {
						birthDayCondition.append(":"+notLimit+"-"+birthDayDateEnd+dateText);
					}
				}
				String joinDateFlag = (String)map.get("joinDateFlag");
				if(joinDateFlag != null && !"".equals(joinDateFlag)) {
					birthDayCondition.append(andJoin);
					birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateFlag"));
				}
			} else if("1".equals(birthDayMode)) {
				birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curBirthDayDate"));
				String curDayJoinDateFlag = (String)map.get("curDayJoinDateFlag");
				if(curDayJoinDateFlag != null && !"".equals(curDayJoinDateFlag)) {
					birthDayCondition.append(andJoin);
					birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curDayJoinDateFlag"));
				}
			} else if("2".equals(birthDayMode)) {
				String birthDayRange = (String)map.get("birthDayRange");
				if(birthDayRange != null && !"".equals(birthDayRange)) {
					String birthDayPath = (String)map.get("birthDayPath");
					if("1".equals(birthDayPath)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayPath1"));
					} else if("2".equals(birthDayPath)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayPath2"));
					}
					birthDayCondition.append(birthDayRange);
					String birthDayUnit = (String)map.get("birthDayUnit");
					if("1".equals(birthDayUnit)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayUnit1"));
					} else if("2".equals(birthDayUnit)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayUnit2"));
					}
					birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayText"));
				}
			} else if("9".equals(birthDayMode)) {
				String birthDayMonth = (String)map.get("birthDayMonth");
				String birthDayDate = (String)map.get("birthDayDate");
				if(birthDayMonth != null && !"".equals(birthDayMonth)) {
					if(birthDayDate != null && !"".equals(birthDayDate)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDay")+":"
								+birthDayMonth+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDate+CherryUtil.getResourceValue(null, language, "global.page.date"));
					} else {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDay")+":"
								+birthDayMonth+CherryUtil.getResourceValue(null, language, "global.page.month"));
					}
				}
			} else if("3".equals(birthDayMode)) {
				String birthDayMonthRangeStart = (String)map.get("birthDayMonthRangeStart");
				String birthDayMonthRangeEnd = (String)map.get("birthDayMonthRangeEnd");
				String birthDayDateRangeStart = (String)map.get("birthDayDateRangeStart");
				String birthDayDateRangeEnd = (String)map.get("birthDayDateRangeEnd");
				if(birthDayDateRangeStart == null || "".equals(birthDayDateRangeStart)) {
					birthDayDateRangeStart = "1";
				}
				if(birthDayDateRangeEnd == null || "".equals(birthDayDateRangeEnd)) {
					birthDayDateRangeEnd = "1";
				}
				if(birthDayMonthRangeStart != null && !"".equals(birthDayMonthRangeStart)) {
					if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayRange")+":"
								+birthDayMonthRangeStart+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeStart+CherryUtil.getResourceValue(null, language, "global.page.date")
								+"-"+birthDayMonthRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.date"));
					} else {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayRange")+":"
								+birthDayMonthRangeStart+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeStart+CherryUtil.getResourceValue(null, language, "global.page.date")
								+"-"+notLimit);
					}
				} else {
					if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
						birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.birthDayRange")+":"
								+notLimit
								+"-"+birthDayMonthRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.date"));
						
					}
				}
			}
		}
		if(birthDayCondition.length() > 0) {
			conditionList.add(birthDayCondition.toString());
		}
		// 第二次选择的生日条件
		StringBuffer birthDayCondition2 = new StringBuffer();
		String birthDayDateMode = (String)map.get("birthDayDateMode");
		if(birthDayDateMode != null && !"".equals(birthDayDateMode)) {
			if("0".equals(birthDayDateMode)) {
				birthDayCondition2.append(CherryUtil.getResourceValue(null, language, "global.page.curBirthDayMonth"));
				String birthDayDateMoreStart = (String)map.get("birthDayDateMoreStart");
				String birthDayDateMoreEnd = (String)map.get("birthDayDateMoreEnd");
				String dateText = CherryUtil.getResourceValue(null, language, "global.page.date");
				if(birthDayDateMoreStart != null && !"".equals(birthDayDateMoreStart)) {
					if(birthDayDateMoreEnd != null && !"".equals(birthDayDateMoreEnd)) {
						birthDayCondition2.append(":"+birthDayDateMoreStart+dateText+"-"+birthDayDateMoreEnd+dateText);
					} else {
						birthDayCondition2.append(":"+birthDayDateMoreStart+dateText+"-"+notLimit);
					}
				} else {
					if(birthDayDateMoreEnd != null && !"".equals(birthDayDateMoreEnd)) {
						birthDayCondition2.append(":"+notLimit+"-"+birthDayDateMoreEnd+dateText);
					}
				}
			} else if("1".equals(birthDayDateMode)) {
				birthDayCondition2.append(CherryUtil.getResourceValue(null, language, "global.page.curBirthDayDate"));
			}
		}
		if(birthDayCondition2.length() > 0) {
			conditionList.add(birthDayCondition2.toString());
		}
		// 会员卡号
		String memCode = (String)map.get("memCode");
		if(memCode != null && !"".equals(memCode)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memCode")+":"+memCode);
		}
		// 会员姓名
		String memName = (String)map.get("name");
		if(memName != null && !"".equals(memName)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memName")+":"+memName);
		}
		// 会员手机
		String mobilePhone = (String)map.get("mobilePhone");
		if(mobilePhone != null && !"".equals(mobilePhone)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memMobilePhone")+":"+mobilePhone);
		}
		// 会员积分
		String memberPointStart = (String)map.get("memberPointStart");
		String memberPointEnd = (String)map.get("memberPointEnd");
		if(memberPointStart != null && !"".equals(memberPointStart)) {
			if(memberPointEnd != null && !"".equals(memberPointEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memberPoint")+":"+memberPointStart+"-"+memberPointEnd);
			} else {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memberPoint")+":"+memberPointStart+"-"+notLimit);
			}
		} else {
			if(memberPointEnd != null && !"".equals(memberPointEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memberPoint")+":"+notLimit+"-"+memberPointEnd);
			}
		}
		// 可兑换积分
		String changablePointStart = (String)map.get("changablePointStart");
		String changablePointEnd = (String)map.get("changablePointEnd");
		if(changablePointStart != null && !"".equals(changablePointStart)) {
			if(changablePointEnd != null && !"".equals(changablePointEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.changablePoint")+":"+changablePointStart+"-"+changablePointEnd);
			} else {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.changablePoint")+":"+changablePointStart+"-"+notLimit);
			}
		} else {
			if(changablePointEnd != null && !"".equals(changablePointEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.changablePoint")+":"+notLimit+"-"+changablePointEnd);
			}
		}
		// 积分到期日
		String curDealDateStart = (String)map.get("curDealDateStart");
		String curDealDateEnd = (String)map.get("curDealDateEnd");
		if(curDealDateStart != null && !"".equals(curDealDateStart)) {
			if(curDealDateEnd != null && !"".equals(curDealDateEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.curDealDate")+":"+curDealDateStart+"-"+curDealDateEnd);
			} else {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.curDealDate")+":"+curDealDateStart+"-"+notLimit);
			}
		} else {
			if(curDealDateEnd != null && !"".equals(curDealDateEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.curDealDate")+":"+notLimit+"-"+curDealDateEnd);
			}
		}
		// 性别
		Object mebSex = (Object)map.get("mebSex");
		if(mebSex != null) {
			List<String> _mebSex = new ArrayList<String>();
			if(mebSex instanceof String) {
				if(!"".equals(mebSex.toString())) {
					_mebSex.add(mebSex.toString());
				}
			} else {
				_mebSex = (List)mebSex;
			}
			if(!_mebSex.isEmpty()) {
				StringBuffer mebSexCondition = new StringBuffer();
				if(_mebSex.size() > 1) {
					mebSexCondition.append("(");
				}
				for(int i = 0; i < _mebSex.size(); i++) {
					if(i > 0) {
						mebSexCondition.append(orJoin);
					}
					mebSexCondition.append(code.getVal("1006", _mebSex.get(i)));
				}
				if(_mebSex.size() > 1) {
					mebSexCondition.append(")");
				}
				conditionList.add(mebSexCondition.toString());
			}
		}
		// 年龄
		String ageStart = (String)map.get("ageStart");
		String ageEnd = (String)map.get("ageEnd");
		if(ageStart != null && !"".equals(ageStart)) {
			if(ageEnd != null && !"".equals(ageEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memAge")+":"+ageStart+"-"+ageEnd);
			} else {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memAge")+":"+ageStart+"-"+notLimit);
			}
		} else {
			if(ageEnd != null && !"".equals(ageEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memAge")+":"+notLimit+"-"+ageEnd);
			}
		}
		
		// 激活途径
		String activeChannel = (String)map.get("activeChannel");
		if(activeChannel != null && !"".equals(activeChannel)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.activeChannel")+":"+code.getVal("1298", activeChannel));
		}
		
		// 发卡柜台
		String modeFlag = (String)map.get("modeFlag");
		if(modeFlag != null && !"".equals(modeFlag)) {
			StringBuffer counterCondition = new StringBuffer();
			List<String> counterNameList = new ArrayList<String>();
			if("1".equals(modeFlag)) {
				List<String> regionIdList = new ArrayList<String>();
				String regionId = (String)map.get("regionId");
				if(regionId != null && !"".equals(regionId)) {
					regionIdList.addAll(Arrays.asList(regionId.split(",")));
				}
				String provinceId = (String)map.get("provinceId");
				if(provinceId != null && !"".equals(provinceId)) {
					regionIdList.addAll(Arrays.asList(provinceId.split(",")));
				}
				String cityId = (String)map.get("cityId");
				if(cityId != null && !"".equals(cityId)) {
					regionIdList.addAll(Arrays.asList(cityId.split(",")));
				}
				if(regionIdList != null && !regionIdList.isEmpty()) {
					map.put("regionIdList", regionIdList);
					regionIdList = binOLCM33_Service.getRegionList(map);
					if(regionIdList != null && !regionIdList.isEmpty()) {
						counterNameList.addAll(regionIdList);
					}
				}
			} else if("2".equals(modeFlag)) {
				List<String> regionIdList = new ArrayList<String>();
				String channelRegionId = (String)map.get("channelRegionId");
				if(channelRegionId != null && !"".equals(channelRegionId)) {
					regionIdList.addAll(Arrays.asList(channelRegionId.split(",")));
				}
				if(regionIdList != null && !regionIdList.isEmpty()) {
					map.put("regionIdList", regionIdList);
					regionIdList = binOLCM33_Service.getRegionList(map);
					if(regionIdList != null && !regionIdList.isEmpty()) {
						counterNameList.addAll(regionIdList);
					}
				}
				List<String> channelIdList = new ArrayList<String>();
				String channelId = (String)map.get("channelId");
				if(channelId != null && !"".equals(channelId)) {
					channelIdList.addAll(Arrays.asList(channelId.split(",")));
				}
				if(channelIdList != null && !channelIdList.isEmpty()) {
					map.put("channelIdList", channelIdList);
					channelIdList = binOLCM33_Service.getChannelList(map);
					if(channelIdList != null && !channelIdList.isEmpty()) {
						counterNameList.addAll(channelIdList);
					}
				}
			} else if("4".equals(modeFlag) || "5".equals(modeFlag)) {
				String regionId = (String)map.get("belongId");
				if(regionId != null && !"".equals(regionId)) {
					List<String> belongIdList = new ArrayList<String>();
					belongIdList.addAll(Arrays.asList(regionId.split(",")));
					List<Map<String, Object>> list = code.getCodes("1309");
					if(null != list && !list.isEmpty()){
						for (int i = 0; i < belongIdList.size(); i++) {
							String bid = belongIdList.get(i);
							for (Map<String, Object> codeInfo : list) {
								if (bid.equals(codeInfo.get("CodeKey"))) {
									counterNameList.add((String) codeInfo.get("Value"));
									break;
								}
							}
						}
					}
				}
			}
			List<String> organizationIdList = new ArrayList<String>();
			String memCounterId = (String)map.get("memCounterId");
			if(memCounterId != null && !"".equals(memCounterId)) {
				organizationIdList.addAll(Arrays.asList(memCounterId.split(",")));
			}
			if(organizationIdList != null && !organizationIdList.isEmpty()) {
				map.put("organizationIdList", organizationIdList);
				organizationIdList = binOLCM33_Service.getOrganizationList(map);
				if(organizationIdList != null && !organizationIdList.isEmpty()) {
					counterNameList.addAll(organizationIdList);
				}
			}
			if(counterNameList != null && !counterNameList.isEmpty()) {
				String exclusiveFlag = (String)map.get("exclusiveFlag");
				String couValidFlag = (String)map.get("couValidFlag");
				if("1".equals(exclusiveFlag)) {
					if("0".equals(couValidFlag)) {
						counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.memCounter")
								+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag0")+")"
								+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag1")+":");
					} else if("1".equals(couValidFlag)) {
						counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.memCounter")
								+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag1")+")"
								+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag1")+":");
					}
				} else if("2".equals(exclusiveFlag)) {
					if("0".equals(couValidFlag)) {
						counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.memCounter")
								+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag0")+")"
								+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag2")+":");
					} else if("1".equals(couValidFlag)) {
						counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.memCounter")
								+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag1")+")"
								+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag2")+":");
					}
				}
				for(int j = 0; j < counterNameList.size(); j++) {
					if(j > 0) {
						counterCondition.append(",");
					}
					counterCondition.append(counterNameList.get(j));
				}
				conditionList.add(counterCondition.toString());
			}
		}
		
		// 发卡BA
		String employeeId = (String)map.get("employeeId");
		if(employeeId != null && !"".equals(employeeId)) {
			String employName = binOLCM33_Service.getEmployeeName(map);
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.employeeId")+":"+employName);
		}
		
		// 是否绑定微信
		String bindWeChat = (String)map.get("bindWeChat");
		if(bindWeChat != null && !"".equals(bindWeChat)) {
			if("1".equals(bindWeChat)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.bindWeChat")+":"+CherryUtil.getResourceValue(null, language, "global.page.bindWeChat1"));
			} else if ("2".equals(bindWeChat)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.bindWeChat")+":"+CherryUtil.getResourceValue(null, language, "global.page.bindWeChat2"));
			}
		}
		
		// 推荐会员
		String referFlag = (String)map.get("referFlag");
		if(referFlag != null && !"".equals(referFlag)) {
			if("1".equals(referFlag)) {
				// 被推荐会员卡号
				String referredMemCode = (String)map.get("referredMemCode");
				if(referredMemCode != null && !"".equals(referredMemCode)) {
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.referredMemCode")+":"+referredMemCode);
				} else {
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.referFlag1"));
				}
			} else if("2".equals(referFlag)) {
				// 推荐者卡号
				String referrerMemCode = (String)map.get("referrerMemCode");
				if(referrerMemCode != null && !"".equals(referrerMemCode)) {
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.referrerMemCode")+":"+referrerMemCode);
				} else {
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.referFlag2"));
				}
			}
		}
		
		// 入会途径
		String channelCode = (String)map.get("channelCode");
		if(channelCode != null && !"".equals(channelCode)) {
			conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.channelCode")+":"+code.getVal("1301", channelCode));
		}
		
		// 等级调整日期
		String levelAdjustDayFlag = (String)map.get("levelAdjustDayFlag");
		if(levelAdjustDayFlag != null && !"".equals(levelAdjustDayFlag)) {
			// 等级调整日期条件
			StringBuffer levelAdjustDayCondition = new StringBuffer();
			if("1".equals(levelAdjustDayFlag)) {
				levelAdjustDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDay")+":"+CherryUtil.getResourceValue(null, language, "global.page.curLevelAdjustDay"));
			} else if("2".equals(levelAdjustDayFlag)) {
				String levelAdjustDayRange = (String)map.get("levelAdjustDayRange");
				if(levelAdjustDayRange != null && !"".equals(levelAdjustDayRange)) {
					String levelAdjustDayUnit = (String)map.get("levelAdjustDayUnit");
					String levelAdjustDayUnitText = "";
					if(levelAdjustDayUnit != null) {
						if("1".equals(levelAdjustDayUnit)) {
							levelAdjustDayUnitText = CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDayUnit1");
						} else if("2".equals(levelAdjustDayUnit)) {
							levelAdjustDayUnitText = CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDayUnit2");
						}
					}
					levelAdjustDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDay")+":"
							+CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDayDes")
							+levelAdjustDayRange+levelAdjustDayUnitText);
				}
			} else if("3".equals(levelAdjustDayFlag)) {
				String levelAdjustDayStart = (String)map.get("levelAdjustDayStart");
				String levelAdjustDayEnd = (String)map.get("levelAdjustDayEnd");
				if(levelAdjustDayStart != null && !"".equals(levelAdjustDayStart)) {
					if(levelAdjustDayEnd != null && !"".equals(levelAdjustDayEnd)) {
						levelAdjustDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDay")+":"+levelAdjustDayStart+"-"+levelAdjustDayEnd);
					} else {
						levelAdjustDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDay")+":"+levelAdjustDayStart+"-"+notLimit);
					}
				} else {
					if(levelAdjustDayEnd != null && !"".equals(levelAdjustDayEnd)) {
						levelAdjustDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.levelAdjustDay")+":"+notLimit+"-"+levelAdjustDayEnd);
					}
				}
			}
			if(!"".equals(levelAdjustDayCondition.toString())) {
				String levelChangeType = (String)map.get("levelChangeType");
				if(levelChangeType != null && !"".equals(levelChangeType)) {
					conditionList.add("("+levelAdjustDayCondition.toString()+andJoin+code.getVal("1249", levelChangeType)+")");
				} else {
					conditionList.add("("+levelAdjustDayCondition.toString()+")");
				}
			}
		}
		
		// 最近一次购买时间
		String lastSaleDateStart = (String)map.get("lastSaleDateStart");
		String lastSaleDateEnd = (String)map.get("lastSaleDateEnd");
		if(lastSaleDateStart != null && !"".equals(lastSaleDateStart)) {
			if(lastSaleDateEnd != null && !"".equals(lastSaleDateEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.lastSaleDate")+":"+lastSaleDateStart+"-"+lastSaleDateEnd);
			} else {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.lastSaleDate")+":"+lastSaleDateStart+"-"+notLimit);
			}
		} else {
			if(lastSaleDateEnd != null && !"".equals(lastSaleDateEnd)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.lastSaleDate")+":"+notLimit+"-"+lastSaleDateEnd);
			}
		}
		
		String firstStartDay = (String)map.get("firstStartDay");
		String firstEndDay = (String)map.get("firstEndDay");
		if(firstStartDay != null && !"".equals(firstStartDay)) {
			if(firstEndDay != null && !"".equals(firstEndDay)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.firstBuyDayRange")+":"+firstStartDay+"-"+firstEndDay);
			} else {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.firstBuyDayRange")+":"+firstStartDay+"-"+notLimit);
			}
		} else {
			if(firstEndDay != null && !"".equals(firstEndDay)) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.firstBuyDayRange")+":"+notLimit+"-"+firstEndDay);
			}
		}
		
		// 首次购买多少天内未购买
		String notSaleDays = null;
		if ("2".equals(map.get("noSaleDaysMode"))) {
			notSaleDays = (String)map.get("notSaleDaysRange");
		} else {
			notSaleDays = (String)map.get("notSaleDays");
		}
		if(notSaleDays != null && !"".equals(notSaleDays)) {
			String noSaleDayTxt = CherryUtil.getResourceValue(null, language, "global.page.afterFirstBuy")+notSaleDays
					+CherryUtil.getResourceValue(null, language, "global.page.notSaleDays1");
			if (!"2".equals(map.get("noSaleDaysMode"))) {
				noSaleDayTxt += "(" + CherryUtil.getResourceValue(null, language, "global.page.nsdMode1Txt") + ")";
			}
			conditionList.add(noSaleDayTxt);
		}
		/* *************************************************俱乐部属性   START****************************************************** */
		// 会员俱乐部
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			// 查询会员俱乐部信息
			Map<String, Object> clubInfo = binOLCM33_Service.getMemberClubInfo(map);
			if (null != clubInfo && !clubInfo.isEmpty()) {
				conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.memClub")
						+ ":(" + clubInfo.get("clubCode") + ")" + clubInfo.get("clubName"));
				// 俱乐部发卡柜台
				String clubModeFlag = (String)map.get("clubModeFlag");
				if(clubModeFlag != null && !"".equals(clubModeFlag)) {
					StringBuffer counterCondition = new StringBuffer();
					List<String> counterNameList = new ArrayList<String>();
					if("1".equals(clubModeFlag)) {
						List<String> regionIdList = new ArrayList<String>();
						String regionId = (String)map.get("clubRegionId");
						if(regionId != null && !"".equals(regionId)) {
							regionIdList.addAll(Arrays.asList(regionId.split(",")));
						}
						String provinceId = (String)map.get("clubProvinceId");
						if(provinceId != null && !"".equals(provinceId)) {
							regionIdList.addAll(Arrays.asList(provinceId.split(",")));
						}
						String cityId = (String)map.get("clubCityId");
						if(cityId != null && !"".equals(cityId)) {
							regionIdList.addAll(Arrays.asList(cityId.split(",")));
						}
						if(regionIdList != null && !regionIdList.isEmpty()) {
							map.put("regionIdList", regionIdList);
							regionIdList = binOLCM33_Service.getRegionList(map);
							if(regionIdList != null && !regionIdList.isEmpty()) {
								counterNameList.addAll(regionIdList);
							}
						}
					} else if("2".equals(modeFlag)) {
						List<String> regionIdList = new ArrayList<String>();
						String channelRegionId = (String)map.get("clubChannelRegionId");
						if(channelRegionId != null && !"".equals(channelRegionId)) {
							regionIdList.addAll(Arrays.asList(channelRegionId.split(",")));
						}
						if(regionIdList != null && !regionIdList.isEmpty()) {
							map.put("regionIdList", regionIdList);
							regionIdList = binOLCM33_Service.getRegionList(map);
							if(regionIdList != null && !regionIdList.isEmpty()) {
								counterNameList.addAll(regionIdList);
							}
						}
						List<String> channelIdList = new ArrayList<String>();
						String channelId = (String)map.get("clubChannelId");
						if(channelId != null && !"".equals(channelId)) {
							channelIdList.addAll(Arrays.asList(channelId.split(",")));
						}
						if(channelIdList != null && !channelIdList.isEmpty()) {
							map.put("channelIdList", channelIdList);
							channelIdList = binOLCM33_Service.getChannelList(map);
							if(channelIdList != null && !channelIdList.isEmpty()) {
								counterNameList.addAll(channelIdList);
							}
						}
					} else if("4".equals(modeFlag) || "5".equals(modeFlag)) {
						String regionId = (String)map.get("clubBelongId");
						if(regionId != null && !"".equals(regionId)) {
							List<String> belongIdList = new ArrayList<String>();
							belongIdList.addAll(Arrays.asList(regionId.split(",")));
							List<Map<String, Object>> list = code.getCodes("1309");
							if(null != list && !list.isEmpty()){
								for (int i = 0; i < belongIdList.size(); i++) {
									String bid = belongIdList.get(i);
									for (Map<String, Object> codeInfo : list) {
										if (bid.equals(codeInfo.get("CodeKey"))) {
											counterNameList.add((String) codeInfo.get("Value"));
											break;
										}
									}
								}
							}
						}
					}
					List<String> organizationIdList = new ArrayList<String>();
					String memCounterId = (String)map.get("clubMemCounterId");
					if(memCounterId != null && !"".equals(memCounterId)) {
						organizationIdList.addAll(Arrays.asList(memCounterId.split(",")));
					}
					if(organizationIdList != null && !organizationIdList.isEmpty()) {
						map.put("organizationIdList", organizationIdList);
						organizationIdList = binOLCM33_Service.getOrganizationList(map);
						if(organizationIdList != null && !organizationIdList.isEmpty()) {
							counterNameList.addAll(organizationIdList);
						}
					}
					if(counterNameList != null && !counterNameList.isEmpty()) {
						String exclusiveFlag = (String)map.get("clubExclusiveFlag");
						String couValidFlag = (String)map.get("clubCouValidFlag");
						if("1".equals(exclusiveFlag)) {
							if("0".equals(couValidFlag)) {
								counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.clubMemCounter")
										+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag0")+")"
										+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag1")+":");
							} else if("1".equals(couValidFlag)) {
								counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.clubMemCounter")
										+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag1")+")"
										+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag1")+":");
							}
						} else if("2".equals(exclusiveFlag)) {
							if("0".equals(couValidFlag)) {
								counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.clubMemCounter")
										+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag0")+")"
										+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag2")+":");
							} else if("1".equals(couValidFlag)) {
								counterCondition.append(CherryUtil.getResourceValue(null, language, "global.page.clubMemCounter")
										+"("+CherryUtil.getResourceValue(null, language, "global.page.popCouValidFlag1")+")"
										+CherryUtil.getResourceValue(null, language, "global.page.selExclusiveFlag2")+":");
							}
						}
						for(int j = 0; j < counterNameList.size(); j++) {
							if(j > 0) {
								counterCondition.append(",");
							}
							counterCondition.append(counterNameList.get(j));
						}
						conditionList.add(counterCondition.toString());
					}
				}
				
				// 俱乐部发卡BA
				String clubEmployeeId = (String)map.get("clubEmployeeId");
				if(clubEmployeeId != null && !"".equals(clubEmployeeId)) {
					Map<String, Object> selMap = new HashMap<String, Object>();
					selMap.put("employeeId", clubEmployeeId);
					String employName = binOLCM33_Service.getEmployeeName(selMap);
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.clubEmployeeId")+":"+employName);
				}
				// 俱乐部发卡日期
				String clubJoinTimeStart = (String)map.get("clubJoinTimeStart");
				String clubJoinTimeEnd = (String)map.get("clubJoinTimeEnd");
				if(clubJoinTimeStart != null && !"".equals(clubJoinTimeStart)) {
					if(clubJoinTimeEnd != null && !"".equals(clubJoinTimeEnd)) {
						conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.clubJoinTimeStart")+":"+clubJoinTimeStart+"-"+clubJoinTimeEnd);
					} else {
						conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.clubJoinTimeStart")+":"+clubJoinTimeStart+"-"+notLimit);
					}
				} else {
					if(clubJoinTimeEnd != null && !"".equals(clubJoinTimeEnd)) {
						conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.clubJoinTimeStart")+":"+notLimit+"-"+clubJoinTimeEnd);
					}
				}
				// 是否接收通知
				String isReceiveMsg = (String) map.get("isReceiveMsg");
				if ("1".equals(isReceiveMsg)) {
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.isReceiveMsg")+":"
							+CherryUtil.getResourceValue(null, language, "global.page.isReceiveMsg1"));
				} else if ("0".equals(isReceiveMsg)) {
					conditionList.add(CherryUtil.getResourceValue(null, language, "global.page.isReceiveMsg")+":"
							+CherryUtil.getResourceValue(null, language, "global.page.isReceiveMsg0"));
				}
			}
		}
		/* *************************************************俱乐部属性   END****************************************************** */
		// 活动条件
		List<String> actConditionList = new ArrayList<String>();
		// 活动时间
		String participateTimeStart = (String)map.get("participateTimeStart");
		String participateTimeEnd = (String)map.get("participateTimeEnd");
		if(participateTimeStart != null && !"".equals(participateTimeStart)) {
			if(participateTimeEnd != null && !"".equals(participateTimeEnd)) {
				actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignTime")+":"+participateTimeStart+"-"+participateTimeEnd);
			} else {
				actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignTime")+":"+participateTimeStart+"-"+notLimit);
			}
		} else {
			if(participateTimeEnd != null && !"".equals(participateTimeEnd)) {
				actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignTime")+":"+notLimit+"-"+participateTimeEnd);
			}
		}
		// 活动柜台
		String campaignCounterId = (String)map.get("campaignCounterId");
		if(campaignCounterId != null && !"".equals(campaignCounterId)) {
			List<String> organizationIdList = new ArrayList<String>();
			organizationIdList.add(campaignCounterId);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("organizationIdList", organizationIdList);
			organizationIdList = binOLCM33_Service.getOrganizationList(param);
			if(organizationIdList != null && !organizationIdList.isEmpty()) {
				actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignCounter")+":"+organizationIdList.get(0));
			}
		}
		// 活动代码
		String campaignCode = (String)map.get("campaignCode");
		// 活动类型
		String campaignMode = (String)map.get("campaignMode");
		if(campaignCode != null && !"".equals(campaignCode)) {
			Map<String, Object> param = new HashMap<String, Object>();
			// 所属组织
			param.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
			// 所属品牌
			param.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
			param.put("campaignCode", campaignCode);
			if(campaignMode != null && "1".equals(campaignMode)) {
				// 查询促销活动名称
				String campaignName = binOLCM33_Service.getPrmCampaignName(param);
				if(campaignName != null && !"".equals(campaignName)) {
					actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignName")+":"+campaignName);
				}
			} else {
				// 查询会员活动名称
				String memCampaignName = binOLCM33_Service.getMemCampaignName(param);
				if(memCampaignName != null && !"".equals(memCampaignName)) {
					actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignName")+":"+memCampaignName);
				}
			}
		}
		// 会员类型
		Object campaignState = (Object)map.get("campaignState");
		if(campaignState != null) {
			List<String> _campaignState = new ArrayList<String>();
			if(campaignState instanceof String) {
				if(!"".equals(campaignState.toString())) {
					_campaignState.add(campaignState.toString());
				}
			} else {
				_campaignState = (List)campaignState;
			}
			if(!_campaignState.isEmpty()) {
				StringBuffer campaignStateCondition = new StringBuffer();
				if(_campaignState.size() > 1) {
					campaignStateCondition.append("(");
				}
				for(int i = 0; i < _campaignState.size(); i++) {
					if(i > 0) {
						campaignStateCondition.append(orJoin);
					}
					campaignStateCondition.append(code.getVal("1116", _campaignState.get(i)));
				}
				if(_campaignState.size() > 1) {
					campaignStateCondition.append(")");
				}
				actConditionList.add(CherryUtil.getResourceValue(null, language, "global.page.campaignState")+":"+campaignStateCondition.toString());
			}
		}
		if(actConditionList != null && !actConditionList.isEmpty()) {
			StringBuffer actCondition = new StringBuffer();
			if(actConditionList.size() > 1) {
				actCondition.append("(");
			}
			for(int i = 0; i < actConditionList.size(); i++) {
				if(i > 0) {
					actCondition.append(andJoin);
				}
				actCondition.append(actConditionList.get(i));
			}
			if(actConditionList.size() > 1) {
				actCondition.append(")");
			}
			conditionList.add(actCondition.toString());
		}
		
		// 会员扩展条件
		List<Map<String, Object>> propertyInfoList = (List)map.get("propertyInfoList");
		if(propertyInfoList != null && !propertyInfoList.isEmpty()) {
			List<Map<String, Object>> _propertyInfoList = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < propertyInfoList.size(); i++) {
				Map<String, Object> propertyInfoMap = (Map)propertyInfoList.get(i);
				List propertyValue = (List)propertyInfoMap.get("propertyValues");
				if(propertyValue != null && !propertyValue.isEmpty() && !"".equals(propertyValue.get(0))) {
					_propertyInfoList.add(propertyInfoMap);
				}
			}
			if(_propertyInfoList != null && !_propertyInfoList.isEmpty()) {
				String orgId = String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID));
	    		String brandId = String.valueOf(map.get(CherryConstants.BRANDINFOID));
	    		String searchMode = "";
	    		if(binOLCM14_BL.isConfigOpen("1071", orgId, brandId)) {
	    			searchMode = "1";
	    		} else {
	    			searchMode = "2";
	    		}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("propertyInfoList", _propertyInfoList);
				List<Map<String, Object>> paperQuestionList = binOLCM33_Service.getPaperQuestionList(param);
				for(int i = 0; i < _propertyInfoList.size(); i++) {
					Map<String, Object> propertyInfoMap = _propertyInfoList.get(i);
					String extendPropertyId = (String)propertyInfoMap.get("extendPropertyId");
					List<String> propertyValues = (List)propertyInfoMap.get("propertyValues");
					String propertyType = (String)propertyInfoMap.get("propertyType");
					StringBuffer extendCondition = new StringBuffer();
					String name = binOLCM33_BL.getNameByKey(extendPropertyId, paperQuestionList);
					if(propertyValues.size() > 1) {
						extendCondition.append("(");
					}
					extendCondition.append(name+":");
					// 多选题的场合
					if("2".equals(propertyType)) {
						// 数据库查询的场合
						if("2".equals(searchMode)) {
							for(int x = 0; x < paperQuestionList.size(); x++) {
								String id = paperQuestionList.get(x).get("id").toString();
								if(id.equals(extendPropertyId)) {
									Map<String, Object> paperQuestionMap = paperQuestionList.get(x);
									for(int j = 0; j < propertyValues.size(); j++) {
										if(j > 0) {
											extendCondition.append(orJoin);
										}
										char ca = (char)(Integer.parseInt(propertyValues.get(j))+64);
										String value = (String)paperQuestionMap.get("option"+ca);
										extendCondition.append(value);
									}
									break;
								}
							}
						} else {
							for(int j = 0; j < propertyValues.size(); j++) {
								if(j > 0) {
									extendCondition.append(orJoin);
								}
								extendCondition.append(propertyValues.get(j));
							}
						}
						
					} else {
						for(int j = 0; j < propertyValues.size(); j++) {
							if(j > 0) {
								extendCondition.append(orJoin);
							}
							extendCondition.append(propertyValues.get(j));
						}
					}
					if(propertyValues.size() > 1) {
						extendCondition.append(")");
					}
					conditionList.add(extendCondition.toString());
				}
			}
		}
		
		StringBuffer conditionContent = new StringBuffer();
		if(conditionList != null && !conditionList.isEmpty()) {
			for(int i = 0; i < conditionList.size(); i++) {
				if(i > 0) {
					conditionContent.append(andJoin);
				}
				conditionContent.append(conditionList.get(i));
			}
		}
		return conditionContent.toString();
	}

}
