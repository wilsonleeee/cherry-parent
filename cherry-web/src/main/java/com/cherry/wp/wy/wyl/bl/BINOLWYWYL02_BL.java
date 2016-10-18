package com.cherry.wp.wy.wyl.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.wp.wy.wyl.interfaces.BINOLWYWYL02_IF;
import com.cherry.wp.wy.wyl.service.BINOLWYWYL02_Service;

public class BINOLWYWYL02_BL implements BINOLWYWYL02_IF{
	
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;

	@Resource(name="binOLWYWYL02_Service")
	private BINOLWYWYL02_Service binOLWYWYL02_Service;
	
	@Override
	public List<Map<String, Object>> getActivityOrderInfo(Map<String, Object> map)
			throws Exception {
		return binOLWYWYL02_Service.getOrderList(map);
	}
	
	public Map<String, Object> updateMemberInfo(Map<String, Object> map) throws Exception {
		boolean updateFlag = false;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String birthDay = "";
		String memberInfoId = ConvertUtil.getString(map.get("memberInfoId"));
		String memberName = ConvertUtil.getString(map.get("memberName"));
		String month = ConvertUtil.getString(map.get("month"));
		String day = ConvertUtil.getString(map.get("day"));
		String gender = ConvertUtil.getString(map.get("gender"));
		String orgMemberName = ConvertUtil.getString(map.get("orgMemberName"));
		String orgBirthDay = ConvertUtil.getString(map.get("orgBirthDay"));
		String orgGender = ConvertUtil.getString(map.get("orgGender"));
		String sysDate = binOLWYWYL02_Service.getSYSDate();
		
		if(!"".equals(month)){
			if(month.length() == 1){
				month = "0" + month;
			}
		}else{
			if(orgBirthDay.length() >= 4){
				month = orgBirthDay.substring(0,2);
			}
		}
		if(!"".equals(day)){
			if(day.length() == 1){
				day = "0" + day;
			}
		}else{
			if(orgBirthDay.length() >= 4){
				day = orgBirthDay.substring(2,4);
			}
		}
		birthDay = month + day;
		// 更新通用参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 比较会员姓名是否更新
		if(!memberName.equals(orgMemberName)){
			// 会员姓名
			paramMap.put("memberName", memberName);
			updateFlag = true;
		}
		// 比较会员性别是否更新
		if(!gender.equals(orgGender)){
			// 会员性别
			paramMap.put("gender", gender);
			updateFlag = true;
		}
		// 比较会员生日是否更新
		if(!birthDay.equals(orgBirthDay) && birthDay.length() == 4){
			// 会员生日
			paramMap.put("birthDay", birthDay);
			updateFlag = true;
		}
		// 检查会员ID是否存在
		if(!"".equals(memberInfoId)){
			if(updateFlag){
				// 组织ID
				paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
				// 品牌ID
				paramMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
				// 会员ID
				paramMap.put("memberInfoId", memberInfoId);
				// 更新程序
				paramMap.put(CherryConstants.UPDATEPGM, "BINOLWYWYL02");
				// 更新用户
				paramMap.put("updatedBy", map.get("userId"));
				// 更新会员信息
				binOLWYWYL02_Service.updateMemberInfo(paramMap);
			}
			returnMap.put("updateFlag", "N");
		}else{
			// 查询会员参数
			Map<String, Object> praMap = new HashMap<String, Object>();
			// 组织ID
			praMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
			// 品牌ID
			praMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
			// 微信号
			praMap.put("messageId", map.get("messageId"));
			// 根据微信号查会员
			Map<String, Object> memberInfoMap = binOLWYWYL02_Service.getMemberInfoByMessageId(praMap);
			
			if(null != memberInfoMap && !memberInfoMap.isEmpty()){
				boolean changeFlag = false;
				// 更新会员参数
				Map<String, Object> updateMap = new HashMap<String, Object>();
				
				if(!memberName.equals(ConvertUtil.getString(memberInfoMap.get("memberName")))){
					// 会员姓名
					updateMap.put("memberName", memberName);
					changeFlag = true;
				}
				// 比较会员性别是否更新
				if(!gender.equals(ConvertUtil.getString(memberInfoMap.get("gender")))){
					// 会员性别
					updateMap.put("gender", gender);
					changeFlag = true;
				}
				// 比较会员生日是否更新
				if(!birthDay.equals(ConvertUtil.getString(memberInfoMap.get("birthDay"))) && birthDay.length() == 4){
					// 会员生日
					updateMap.put("birthDay", birthDay);
					changeFlag = true;
				}
				if(changeFlag){
					// 组织ID
					updateMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
					// 品牌ID
					updateMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
					// 会员ID
					updateMap.put("memberInfoId", memberInfoMap.get("memberInfoId"));
					// 更新程序
					updateMap.put(CherryConstants.UPDATEPGM, "BINOLWYWYL02");
					// 更新用户
					updateMap.put("updatedBy", map.get("userId"));
					// 更新会员信息
					binOLWYWYL02_Service.updateMemberInfo(updateMap);
				}
				returnMap.put("updateFlag", "Y");
				returnMap.put("memberInfoId", memberInfoMap.get("memberInfoId"));
				returnMap.put("memCode", memberInfoMap.get("memCode"));
			}else{
				// 新增会员参数
				Map<String, Object> prmMap = new HashMap<String, Object>();
				// 组织ID
				prmMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
				// 品牌ID
				prmMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
				// 组织代码
				prmMap.put(CherryConstants.ORG_CODE, map.get("orgCode"));
				// 品牌代码
				prmMap.put(CherryConstants.BRAND_CODE, map.get("brandCode"));
				// 会员卡号
				prmMap.put("memCode", map.get("mobilePhone"));
				// 会员姓名
				prmMap.put("memName", memberName);
				// 会员性别
				prmMap.put("gender", gender);
				// 会员生日
				if(!"".equals(birthDay) && birthDay.length() == 4){
					birthDay = "1990-" + month + "-" + day;
					prmMap.put("birth", birthDay);
				}
				// 微信号
				prmMap.put("messageId", map.get("messageId"));
				// 手机号
				prmMap.put("mobilePhone", map.get("mobilePhone"));
				// 发卡柜台ID
				prmMap.put("organizationId", map.get("organizationId"));
				//　柜台号
				prmMap.put("organizationCode", map.get("counterCode"));
				// 入会时间
				prmMap.put("joinDate", sysDate);
				// 作成者
				prmMap.put(CherryConstants.CREATEDBY, map.get("userId"));
				// 作成程序名
				prmMap.put(CherryConstants.CREATEPGM, "BINOLWYWYL02");
				// 更新者
				prmMap.put(CherryConstants.UPDATEDBY, map.get("userId"));
				// 更新程序名
				prmMap.put(CherryConstants.UPDATEPGM, "BINOLWYWYL02");
				// 操作员工
				prmMap.put("modifyEmployee", map.get("employeeCode"));
				// 调用增加会员的方法新增会员
				String memberId = binOLMBMBM11_BL.tran_addMemberInfo(prmMap);
				
				returnMap.put("updateFlag", "Y");
				returnMap.put("memberInfoId", memberId);
				returnMap.put("memCode", map.get("mobilePhone"));
			}
			
		}
		return returnMap;
	}

	@Override
	public void updateOrderMemberId(Map<String, Object> map)
			throws Exception {
		// 更新会员信息
		binOLWYWYL02_Service.updateOrderMemberId(map);
	}
}
