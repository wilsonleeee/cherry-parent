/*
 * @(#)BINOLMBMBM13_BL.java     1.0 2013/04/11
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM13_Service;

/**
 * 会员资料修改履历明细画面BL
 * 
 * @author WangCT
 * @version 1.0 2013/04/11
 */
public class BINOLMBMBM13_BL {
	
	/** 会员资料修改履历明细画面Service **/
	@Resource
	private BINOLMBMBM13_Service binOLMBMBM13_Service;
	
	@Resource
	private CodeTable codeTable;
	
	/**
	 * 查询会员资料修改履历
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历
	 * @throws Exception 
	 */
	public Map<String, Object> getMemInfoRecordInfo(Map<String, Object> map) throws Exception {
		
		// 查询会员资料修改履历
		Map<String, Object> memInfoRecordInfo = binOLMBMBM13_Service.getMemInfoRecordInfo(map);
		if(memInfoRecordInfo != null) {
			// 查询会员资料修改履历明细
			List<Map<String, Object>> memInfoRecordDetail = binOLMBMBM13_Service.getMemInfoRecordDetail(map);
			//品牌信息Code
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			if(memInfoRecordDetail != null && !memInfoRecordDetail.isEmpty()) {
				String language = (String)map.get("language");
				for(Map<String, Object> memInfoRecordDetailMap : memInfoRecordDetail) {
					String modifyField = (String)memInfoRecordDetailMap.get("modifyField").toString();
					String oldValue = (String)memInfoRecordDetailMap.get("oldValue");
					String newValue = (String)memInfoRecordDetailMap.get("newValue");
					// 修改字段为性别的场合
					if("4".equals(modifyField)) {
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1006", oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", codeTable.getVal("1006", newValue));
						}
					} else if("5".equals(modifyField) || "6".equals(modifyField)) { // 修改字段是省份或者城市的场合
						if(oldValue != null && !"".equals(oldValue)) {
							map.put("regionId", oldValue);
							memInfoRecordDetailMap.put("oldValue", binOLMBMBM13_Service.getRegionName(map));
						}
						if(newValue != null && !"".equals(newValue)) {
							map.put("regionId", newValue);
							memInfoRecordDetailMap.put("newValue", binOLMBMBM13_Service.getRegionName(map));
						}
					} else if("12".equals(modifyField)) { // 开卡BA
						if(oldValue != null && !"".equals(oldValue)) {
							map.put("employeeId", oldValue);
							memInfoRecordDetailMap.put("oldValue", binOLMBMBM13_Service.getEmployeeName(map));
						}
						if(newValue != null && !"".equals(newValue)) {
							map.put("employeeId", newValue);
							memInfoRecordDetailMap.put("newValue", binOLMBMBM13_Service.getEmployeeName(map));
						}
					} else if("13".equals(modifyField)) { // 开卡柜台
						if(oldValue != null && !"".equals(oldValue)) {
							map.put("organizationId", oldValue);
							memInfoRecordDetailMap.put("oldValue", binOLMBMBM13_Service.getDepartName(map));
						}
						if(newValue != null && !"".equals(newValue)) {
							map.put("organizationId", newValue);
							memInfoRecordDetailMap.put("newValue", binOLMBMBM13_Service.getDepartName(map));
						}
					} else if("14".equals(modifyField)) { // 推荐会员
						if(oldValue != null && !"".equals(oldValue)) {
							map.put("memberInfoId", oldValue);
							memInfoRecordDetailMap.put("oldValue", binOLMBMBM13_Service.getMemName(map));
						}
						if(newValue != null && !"".equals(newValue)) {
							map.put("memberInfoId", newValue);
							memInfoRecordDetailMap.put("newValue", binOLMBMBM13_Service.getMemName(map));
						}
					} else if("19".equals(modifyField)) { // 婚姻状况
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1043", oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", codeTable.getVal("1043", newValue));
						}
					} else if("22".equals(modifyField)) { // 职业
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1236", oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", codeTable.getVal("1236", newValue));
						}
					} else if("23".equals(modifyField)) { // 最佳联络时间
						if(oldValue != null && !"".equals(oldValue)) {
							String[] oldValues = oldValue.split(",");
							String connectTimeValue = "";
							for(int i = 0; i < oldValues.length; i++) {
								if(i == 0) {
									connectTimeValue = codeTable.getVal("1237", oldValues[i]);
								} else {
									connectTimeValue += "、" + codeTable.getVal("1237", oldValues[i]);
								}
							}
							memInfoRecordDetailMap.put("oldValue", connectTimeValue);
						}
						if(newValue != null && !"".equals(newValue)) {
							String[] newValues = newValue.split(",");
							String connectTimeValue = "";
							for(int i = 0; i < newValues.length; i++) {
								if(i == 0) {
									connectTimeValue = codeTable.getVal("1237", newValues[i]);
								} else {
									connectTimeValue += "、" + codeTable.getVal("1237", newValues[i]);
								}
							}
							memInfoRecordDetailMap.put("newValue", connectTimeValue);
						}
					} else if("24".equals(modifyField)) { // 会员类型
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1204", oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", codeTable.getVal("1204", newValue));
						}
					} else if("15".equals(modifyField)) { // 会员年龄获知方式
						if(oldValue != null && !"".equals(oldValue)) {
							if("1".equals(oldValue)) {
								memInfoRecordDetailMap.put("oldValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_birthYearGetType1"));
							} else if("0".equals(oldValue)) {
								memInfoRecordDetailMap.put("oldValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_birthYearGetType0"));
							}
						}
						if(newValue != null && !"".equals(newValue)) {
							if("1".equals(newValue)) {
								memInfoRecordDetailMap.put("newValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_birthYearGetType1"));
							} else if("0".equals(newValue)) {
								memInfoRecordDetailMap.put("newValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_birthYearGetType0"));
							}
						}
					} else if("20".equals(modifyField)) { // 激活状态
						if(oldValue != null && !"".equals(oldValue)) {
							if("1".equals(oldValue)) {
								memInfoRecordDetailMap.put("oldValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_active1"));
							} else if("0".equals(oldValue)) {
								memInfoRecordDetailMap.put("oldValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_active0"));
							}
						}
						if(newValue != null && !"".equals(newValue)) {
							if("1".equals(newValue)) {
								memInfoRecordDetailMap.put("newValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_active1"));
							} else if("0".equals(newValue)) {
								memInfoRecordDetailMap.put("newValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_active0"));
							}
						}
					} else if("21".equals(modifyField)) { // 是否接收通知
						if(oldValue != null && !"".equals(oldValue)) {
							if("1".equals(oldValue)) {
								memInfoRecordDetailMap.put("oldValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_isReceiveMsg1"));
							} else if("0".equals(oldValue)) {
								memInfoRecordDetailMap.put("oldValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_isReceiveMsg0"));
							}
						}
						if(newValue != null && !"".equals(newValue)) {
							if("1".equals(newValue)) {
								memInfoRecordDetailMap.put("newValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_isReceiveMsg1"));
							} else if("0".equals(newValue)) {
								memInfoRecordDetailMap.put("newValue", CherryUtil.getResourceValue("BINOLMBMBM13", language, "binolmbmbm13_isReceiveMsg0"));
							}
						}
					}else if("2".equals(modifyField)) { // 电话解密
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", CherrySecret.decryptData(brandCode,oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", CherrySecret.decryptData(brandCode,newValue));
						}
					}else if("3".equals(modifyField)) { // 手机解密
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", CherrySecret.decryptData(brandCode,oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", CherrySecret.decryptData(brandCode,newValue));
						}
					}else if("11".equals(modifyField)) { // 邮箱解密
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", CherrySecret.decryptData(brandCode,oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", CherrySecret.decryptData(brandCode,newValue));
						}
					}else if("18".equals(modifyField)) { // 身份证解密
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", CherrySecret.decryptData(brandCode,oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", CherrySecret.decryptData(brandCode,newValue));
						}
					} else if("28".equals(modifyField)) {
						if(oldValue != null && !"".equals(oldValue)) {
							memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1301", oldValue));
						}
						if(newValue != null && !"".equals(newValue)) {
							memInfoRecordDetailMap.put("newValue", codeTable.getVal("1301", newValue));
						}
					}
				}
				memInfoRecordInfo.put("memInfoRecordDetail", memInfoRecordDetail);
			}
		}
		return memInfoRecordInfo;
	}
}
