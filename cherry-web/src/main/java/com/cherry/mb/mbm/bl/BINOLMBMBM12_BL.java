/*
 * @(#)BINOLMBMBM12_BL.java     1.0 2013/04/11
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

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM12_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM13_Service;

/**
 * 会员资料修改履历查询画面BL
 * 
 * @author WangCT
 * @version 1.0 2013/04/11
 */
public class BINOLMBMBM12_BL implements BINOLCM37_IF {
	
	/** 会员资料修改履历查询画面Service */
	@Resource
	private BINOLMBMBM12_Service binOLMBMBM12_Service;
	
	@Resource
	private CodeTable codeTable;
	
	/** 会员资料修改履历明细画面Service **/
	@Resource
	private BINOLMBMBM13_Service binOLMBMBM13_Service;
	
	/**
	 * 查询会员资料修改履历总件数
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历件数
	 */
	public int getMemInfoRecordCount(Map<String, Object> map) {
		
		// 查询会员资料修改履历总件数
		return binOLMBMBM12_Service.getMemInfoRecordCount(map);
	}
	
	/**
	 * 查询会员资料修改履历
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getMemInfoRecordList(Map<String, Object> map) throws Exception {
		//会员资料修改履历List
		List<Map<String, Object>>  memInfoRecordList = binOLMBMBM12_Service.getMemInfoRecordList(map);
		if (memInfoRecordList != null && !memInfoRecordList.isEmpty()) {
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			for (Map<String, Object> memInfoRecordMap : memInfoRecordList) {
				// 会员【备注1】字段解密
				if (!CherryChecker.isNullOrEmpty(memInfoRecordMap.get("remark"),true)) {
					String memo1 = ConvertUtil.getString(memInfoRecordMap.get("remark"));
					memInfoRecordMap.put("remark",CherrySecret.decryptData(brandCode, memo1));
				}
				// 会员【电话】字段解密
				if (!CherryChecker.isNullOrEmpty(memInfoRecordMap.get("telephone"), true)) {
					String telephone = ConvertUtil.getString(memInfoRecordMap.get("telephone"));
					memInfoRecordMap.put("telephone", CherrySecret.decryptData(brandCode,telephone));
				}
				// 会员【手机号】字段解密
				if (!CherryChecker.isNullOrEmpty(memInfoRecordMap.get("mobilePhone"), true)) {
					String mobilePhone = ConvertUtil.getString(memInfoRecordMap.get("mobilePhone"));
					memInfoRecordMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
				// 会员【电子邮箱】字段解密
				if (!CherryChecker.isNullOrEmpty(memInfoRecordMap.get("email"), true)) {
					String email = ConvertUtil.getString(memInfoRecordMap.get("email"));
					memInfoRecordMap.put("email", CherrySecret.decryptData(brandCode,email));
				}
			}
		}
		// 查询会员资料修改履历
		return memInfoRecordList;
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// 查询会员资料修改履历(导出用)
		List<Map<String, Object>> memInfoRecordExportList = binOLMBMBM12_Service.getMemInfoRecordExportList(map);
		if(memInfoRecordExportList != null && !memInfoRecordExportList.isEmpty()) {
			String language = (String)map.get("language");
			// 品牌信息Code
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			// 柜台List
			List<Map<String, Object>> counterInfoList = new ArrayList<Map<String,Object>>();
			// 员工List
			List<Map<String, Object>> employeeList = new ArrayList<Map<String,Object>>();
			// 区域List
			List<Map<String, Object>> regionList = new ArrayList<Map<String,Object>>();
			// 柜台信息是否已取
			boolean getCounterFlag = false;
			// 员工信息是否已取
			boolean getEmployeeFlag = false;
			// 区域信息是否已取
			boolean getRegionFlag = false;
			
			for(Map<String, Object> memInfoRecordDetailMap : memInfoRecordExportList) {
				if(memInfoRecordDetailMap.get("modifyField") == null) {
					continue;
				}
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
					if(!getRegionFlag) {
						// 取得所有区域信息List
						regionList = binOLMBMBM12_Service.getRegionList(map);
						getRegionFlag = true;
					}
					if(oldValue != null && !"".equals(oldValue)) {
						memInfoRecordDetailMap.put("oldValue", this.getNameByKey(oldValue, regionList));
					}
					if(newValue != null && !"".equals(newValue)) {
						memInfoRecordDetailMap.put("newValue", this.getNameByKey(newValue, regionList));
					}
				} else if("12".equals(modifyField)) { // 开卡BA
					if(!getEmployeeFlag) {
						// 取得所有员工信息List
						employeeList = binOLMBMBM12_Service.getEmployeeList(map);
						getEmployeeFlag = true;
					}
					if(oldValue != null && !"".equals(oldValue)) {
						memInfoRecordDetailMap.put("oldValue", this.getNameByKey(oldValue, employeeList));
					}
					if(newValue != null && !"".equals(newValue)) {
						memInfoRecordDetailMap.put("newValue", this.getNameByKey(newValue, employeeList));
					}
				} else if("13".equals(modifyField)) { // 开卡柜台
					if(!getCounterFlag) {
						// 取得所有柜台信息List
						counterInfoList = binOLMBMBM12_Service.getCounterInfoList(map);
						getCounterFlag = true;
					}
					if(oldValue != null && !"".equals(oldValue)) {
						memInfoRecordDetailMap.put("oldValue", this.getNameByKey(oldValue, counterInfoList));
					}
					if(newValue != null && !"".equals(newValue)) {
						memInfoRecordDetailMap.put("newValue", this.getNameByKey(newValue, counterInfoList));
					}
				} else if("14".equals(modifyField)) { // 推荐会员
					if(oldValue != null && !"".equals(oldValue)) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("memberInfoId", oldValue);
						memInfoRecordDetailMap.put("oldValue", binOLMBMBM13_Service.getMemName(paramMap));
					}
					if(newValue != null && !"".equals(newValue)) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("memberInfoId", newValue);
						memInfoRecordDetailMap.put("newValue", binOLMBMBM13_Service.getMemName(paramMap));
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
				}else if("31".equals(modifyField)) { // 肤质
					if(oldValue != null && !"".equals(oldValue)) {
						memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1424", oldValue));
					}
					if(newValue != null && !"".equals(newValue)) {
						memInfoRecordDetailMap.put("newValue", codeTable.getVal("1424", newValue));
					}
				}else if("32".equals(modifyField)) { // 回访方式
					if(oldValue != null && !"".equals(oldValue)) {
						memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1423", oldValue));
					}
					if(newValue != null && !"".equals(newValue)) {
						memInfoRecordDetailMap.put("newValue", codeTable.getVal("1423", newValue));
					}
				}else if("33".equals(modifyField)) { // 收入
					if(oldValue != null && !"".equals(oldValue)) {
						memInfoRecordDetailMap.put("oldValue", codeTable.getVal("1425", oldValue));
					}
					if(newValue != null && !"".equals(newValue)) {
						memInfoRecordDetailMap.put("newValue", codeTable.getVal("1425", newValue));
					}
				}
			}
		}
		return memInfoRecordExportList;
	}
	
	/**
	 * 根据key取得相应的名称
	 * 
	 * @param key 指定key
	 * @param list 指定一组数据List
	 * @return 名称
	 */
	public String getNameByKey(String key, List<Map<String, Object>> list) {
		
		if(list != null && !list.isEmpty()) {
			for(Map<String, Object> map : list) {
				String id = map.get("id").toString();
				if(key.equals(id)) {
					return (String)map.get("name");
				}
			}
		}
		return null;
	}

}
