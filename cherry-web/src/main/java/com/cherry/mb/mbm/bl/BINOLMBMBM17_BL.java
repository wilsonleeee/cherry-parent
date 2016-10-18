/*
 * @(#)BINOLMBMBM16_BL.java     1.0 2013/05/15
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
package com.cherry.mb.mbm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM36_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM17_Service;

/**
 * 会员启用停用处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/05/15
 */
public class BINOLMBMBM17_BL {
	
	/** 会员启用停用处理Service **/
	@Resource
	private BINOLMBMBM17_Service binOLMBMBM17_Service;
	
	/** WebService共通BL */
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 会员信息修改履历管理BL */
	@Resource
	private BINOLCM36_BL binOLCM36_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 会员添加画面Service **/
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	
	/**
	 * 会员启用停用处理
	 * 
	 * @param map 查询参数
	 */
	public void tran_editValidFlag(Map<String, Object> map) throws Exception {
		
		// 会员IDList
		List<String> memberInfoIdList = (List)map.get("memberInfoId");
		// 会员卡号List
		List<String> memCodeList = (List)map.get("memCode");
		// 会员当前有效区分List
		List<String> memCurValidFlagList = (List)map.get("memCurValidFlag");
		// 会员信息版本号List
		List<String> versionDbList = (List)map.get("versionDb");
		// 停用启用标志
		String validFlag = (String)map.get("validFlag");
		// 取得系统时间
		String sysDate = binOLMBMBM17_Service.getSYSDate();
		// 获取系统配置项参数
		int organizationInfoId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandInfoId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		// 停用会员的场合
		if("0".equals(validFlag)) {
			if(memberInfoIdList != null && !memberInfoIdList.isEmpty()) {
				List<Map<String, Object>> memberInfoList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < memberInfoIdList.size(); i++) {
					String memCurValidFlag = memCurValidFlagList.get(i);
					// 有效会员停用处理，无效会员不处理
					if("1".equals(memCurValidFlag)) {
						Map<String, Object> memberInfoMap = new HashMap<String, Object>();
						memberInfoMap.put("memberInfoId", memberInfoIdList.get(i));
						memberInfoMap.put("memCode", memCodeList.get(i));
						// 版本号
						String version = versionDbList.get(i);
						if(version != null && !"".equals(version)) {
							memberInfoMap.put("version", Integer.parseInt(version)+1);
						} else {
							memberInfoMap.put("version", 1);
						}
						memberInfoMap.put("validFlag", validFlag);
						// 作成者
						memberInfoMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
						// 作成程序名
						memberInfoMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
						// 更新者
						memberInfoMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
						// 更新程序名
						memberInfoMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
						// 更新时间
						memberInfoMap.put("updateTime", sysDate);
						memberInfoList.add(memberInfoMap);
					}
				}
				if(memberInfoList != null && !memberInfoList.isEmpty()) {
					// 停用启用会员信息
					binOLMBMBM17_Service.updMemValidFlag(memberInfoList);
					// 停用启用会员卡信息
					binOLMBMBM17_Service.updMemCardValidFlag(memberInfoList);
					// 添加会员修改履历
					this.addMemberInfoRecord(map, memberInfoList);
					// 调用WebService和老后台同步会员信息
					// 获取系统配置项【1297】维护会员信息是否同步到老后台的值
					if(binOLCM14_BL.isConfigOpen("1297", organizationInfoId, brandInfoId)) {
						this.synMemberInfo(map, memberInfoList);
					}
				}
			}
		} else {// 启用会员的场合
			if(memberInfoIdList != null && !memberInfoIdList.isEmpty()) {
				List<Map<String, Object>> memberInfoList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < memberInfoIdList.size(); i++) {
					String memCurValidFlag = memCurValidFlagList.get(i);
					// 无效会员启用处理，有效会员不处理
					if("0".equals(memCurValidFlag)) {
						Map<String, Object> memberInfoMap = new HashMap<String, Object>();
						memberInfoMap.put("memberInfoId", memberInfoIdList.get(i));
						memberInfoMap.put("validFlag", validFlag);
						// 版本号
						String version = versionDbList.get(i);
						if(version != null && !"".equals(version)) {
							memberInfoMap.put("version", Integer.parseInt(version)+1);
						} else {
							memberInfoMap.put("version", 1);
						}
						memberInfoMap.put("validFlag", validFlag);
						// 作成者
						memberInfoMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
						// 作成程序名
						memberInfoMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
						// 更新者
						memberInfoMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
						// 更新程序名
						memberInfoMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
						// 更新时间
						memberInfoMap.put("updateTime", sysDate);
						memberInfoList.add(memberInfoMap);
					} else {
						memberInfoIdList.remove(i);
						i--;
					}
				}
				if(memberInfoList != null && !memberInfoList.isEmpty()) {
					// 查询会员卡信息List
					List<Map<String, Object>> memCardInfoList = binOLMBMBM17_Service.getMemCardInfoList(map);
					if(memCardInfoList != null && !memCardInfoList.isEmpty()) {
						for(int i = 0 ; i < memberInfoList.size(); i++) {
							Map<String, Object> memberInfoMap = memberInfoList.get(i);
							String memberInfoId = (String)memberInfoMap.get("memberInfoId");
							for(int j = 0; j < memCardInfoList.size(); j++) {
								Map<String, Object> memCardInfoMap = memCardInfoList.get(j);
								String _memberInfoId = memCardInfoMap.get("memberInfoId").toString();
								if(memberInfoId.equals(_memberInfoId)) {
									// 把无效会员的最新卡号设置到启用会员信息中
									memberInfoMap.put("memCode", memCardInfoMap.get("memCode"));
									memCardInfoList.remove(j);
									break;
								}
							}
						}
						// 停用启用会员信息
						binOLMBMBM17_Service.updMemValidFlag(memberInfoList);
						// 停用启用会员卡信息
						binOLMBMBM17_Service.updMemCardValidFlag(memberInfoList);
						// 添加会员修改履历
						this.addMemberInfoRecord(map, memberInfoList);
						// 调用WebService和老后台同步会员信息
						// 获取系统配置项【1297】维护会员信息是否同步到老后台的值
						if(binOLCM14_BL.isConfigOpen("1297", organizationInfoId, brandInfoId)) {
							this.synMemberInfo(map, memberInfoList);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 添加会员修改履历
	 * 
	 * @param map 会员信息
	 */
	public void addMemberInfoRecord(Map<String, Object> map, List<Map<String, Object>> memberInfoList) {
		
		// 停用启用标志
		String validFlag = (String)map.get("validFlag");
		for(int i = 0; i < memberInfoList.size(); i++) {
			Map<String, Object> memberInfoMap = memberInfoList.get(i);
			Map<String, Object> memInfoRecordMap = new HashMap<String, Object>();
			memInfoRecordMap.put("memberInfoId", memberInfoMap.get("memberInfoId"));
			memInfoRecordMap.put("memCode", memberInfoMap.get("memCode"));
			memInfoRecordMap.put("organizationInfoId", map.get("organizationInfoId"));
			memInfoRecordMap.put("brandInfoId", map.get("brandInfoId"));
			memInfoRecordMap.put("modifyTime", memberInfoMap.get("updateTime"));
			if("0".equals(validFlag)) {
				memInfoRecordMap.put("modifyType", "3");
			} else {
				memInfoRecordMap.put("modifyType", "5");
			}
			memInfoRecordMap.put("sourse", "Cherry");
			memInfoRecordMap.put("version", memberInfoMap.get("version"));
			memInfoRecordMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
			memInfoRecordMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
			memInfoRecordMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
			memInfoRecordMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
			memInfoRecordMap.put("modifyEmployee", map.get("modifyEmployee"));
			// 添加会员信息修改履历
			binOLCM36_BL.addMemberInfoRecord(memInfoRecordMap);
		}
	}
	
	/**
	 * 调用WebService和老后台同步会员信息
	 * 
	 * @param map 会员信息
	 */
	public void synMemberInfo(Map<String, Object> map, List<Map<String, Object>> memberInfoList) throws Exception {
		
		// 停用启用标志
		String validFlag = (String)map.get("validFlag");
		List<Map<String, Object>> memDetailList = new ArrayList<Map<String,Object>>();
		if("1".equals(validFlag)) {
			map.put("memberInfoList", memberInfoList);
			memDetailList = binOLMBMBM17_Service.getMemInfoList(map);
		}
		for(int i = 0; i < memberInfoList.size(); i++) {
			Map<String, Object> memberInfo = memberInfoList.get(i);
			Map<String, Object> memberInfoMap = new HashMap<String, Object>();
			// 品牌代码
			memberInfoMap.put("BrandCode", map.get(CherryConstants.BRAND_CODE));
			// 业务类型
			memberInfoMap.put("BussinessType", "memberinfo");
			if("0".equals(validFlag)) {
				// 子类型
				memberInfoMap.put("SubType", "3");
			} else {
				// 子类型
				memberInfoMap.put("SubType", "4");
			}
			// 版本号
			memberInfoMap.put("Version", "1.0");
			List<Map<String, Object>> detailList = new ArrayList<Map<String,Object>>();
			Map<String, Object> detailMap = new HashMap<String, Object>();
			// 会员卡号
			detailMap.put("MemberCode", memberInfo.get("memCode"));
			// 版本号
			detailMap.put("Version", memberInfo.get("version"));
			
			if("1".equals(validFlag)) {
				Object memberInfoId = memberInfo.get("memberInfoId");
				if(memDetailList != null && !memDetailList.isEmpty()) {
					for(int j = 0; j < memDetailList.size(); j++) {
						Map<String, Object> memDetailMap = memDetailList.get(j);
						Object _memberInfoId = memDetailMap.get("memberInfoId");
						if(memberInfoId != null && _memberInfoId != null 
								&& memberInfoId.toString().equals(_memberInfoId.toString())) {
							// 姓名
							detailMap.put("MemName", memDetailMap.get("memName"));
							// 电话
							detailMap.put("MemPhone", memDetailMap.get("telephone"));
							// 手机
							detailMap.put("MemMobile", memDetailMap.get("mobilePhone"));
							// 性别
							detailMap.put("MemSex", memDetailMap.get("gender"));
							// 省份
							Object provinceId = memDetailMap.get("provinceId");
							if(provinceId != null && !"".equals(provinceId.toString())) {
								memDetailMap.put("regionId", provinceId);
								String regionName = binOLMBMBM11_Service.getRegionName(memDetailMap);
								detailMap.put("MemProvince", regionName);
							}
							// 城市
							Object cityId = memDetailMap.get("cityId");
							if(cityId != null && !"".equals(cityId.toString())) {
								memDetailMap.put("regionId", cityId);
								String regionName = binOLMBMBM11_Service.getRegionName(memDetailMap);
								detailMap.put("MemCity", regionName);
							}
							// 地址
							detailMap.put("MemAddress", memDetailMap.get("address"));
							// 邮编
							detailMap.put("MemPostcode", memDetailMap.get("postcode"));
							// 生日
							String birthYear = (String)memDetailMap.get("birthYear");
							String birthDay = (String)memDetailMap.get("birthDay");
							if(birthYear != null && !"".equals(birthYear) 
									&& birthDay != null && !"".equals(birthDay)) {
								detailMap.put("MemBirthday", birthYear+birthDay);
							}
							// 邮箱
							detailMap.put("MemMail", memDetailMap.get("email"));
							// 开卡时间
							String joinDate = (String)memDetailMap.get("joinDate");
							if(joinDate != null && !"".equals(joinDate)) {
								detailMap.put("MemGranddate", joinDate.substring(0,4)+joinDate.substring(5,7)+joinDate.substring(8, 10));
							}
							// 发卡BA
							detailMap.put("BAcode", memDetailMap.get("employeeCode"));
							// 发卡柜台
							detailMap.put("CardCounter", memDetailMap.get("organizationCode"));
							// 推荐会员卡号
							detailMap.put("Referrer", memDetailMap.get("referrer"));
							// 是否愿意接收短信
							detailMap.put("IsReceiveMsg", memDetailMap.get("isReceiveMsg"));
							// 是否测试会员
							detailMap.put("TestMemFlag", memDetailMap.get("testType"));
							memDetailList.remove(j);
							break;
						}
					}
				}
			}
			detailList.add(detailMap);
			// 会员信息
			memberInfoMap.put("DetailList", detailList);
			
			Map<String, Object> resultMap = binOLCM27_BL.accessWebService(memberInfoMap);
			String State = (String)resultMap.get("State");
			String Data = (String)resultMap.get("Data");
			if(State.equals("ERROR")) {
				CherryException CherryException = new CherryException("ECM00005");
				CherryException.setErrMessage(Data);
				throw CherryException;
			}
		}
	}

}
