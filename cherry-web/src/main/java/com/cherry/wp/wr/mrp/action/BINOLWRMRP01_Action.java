package com.cherry.wp.wr.mrp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.wr.mrp.form.BINOLWRMRP01_Form;
import com.cherry.wp.wr.mrp.interfaces.BINOLWRMRP01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员信息查询Action
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP01_Action extends BaseAction implements ModelDriven<BINOLWRMRP01_Form> {
	
	private static final long serialVersionUID = -6904302522068129143L;
	
	@Resource
	private BINOLWRMRP01_IF binOLWRMRP01_BL;

	/**
	 * 会员信息查询画面初始化
	 * 
	 * @return 会员信息查询画面 
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 会员信息查询
	 * 
	 * @return 会员信息查询画面 
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		String memberInfoId = form.getMemberInfoId();
		if(memberInfoId == null || "".equals(memberInfoId)) {
			// 会员卡号
			if(form.getMemCode() != null && !"".equals(form.getMemCode())) {
				map.put("memberCode", form.getMemCode());
			}
			// 会员姓名
			if(form.getMemName() != null && !"".equals(form.getMemName())) {
				map.put("memberName", form.getMemName());
			}
			// 会员手机
			String mobilePhone = form.getMobilePhone();
			// 会员手机加密
			if (mobilePhone != null && !"".equals(mobilePhone)) {
				map.put("mobilePhone", CherrySecret.encryptData(userInfo.getBrandCode(),mobilePhone));
			}
			// 会员生日
			String brithMonth = form.getBirthDayMonth();
			String brithDate = form.getBirthDayDate();
			if(brithMonth != null && !"".equals(brithMonth) 
					&& brithDate != null && !"".equals(brithDate)) {
				if(brithMonth.length() == 1) {
					brithMonth = "0" + brithMonth;
				}
				if(brithDate.length() == 1) {
					brithDate = "0" + brithDate;
				}
				map.put("birthDay", brithMonth+brithDate);
			}
			int count = binOLWRMRP01_BL.getMemCount(map);
			if(count == 0) {
				ConvertUtil.setResponseByAjax(response, "0");
				return null;
			} else {
				if(count == 1) {
					List<Map<String, Object>> allMemList = binOLWRMRP01_BL.searchAllMemList(map);
					if(allMemList != null && allMemList.size() == 1) {
						memberInfoId = String.valueOf(allMemList.get(0).get("memberInfoId"));
					}
				}
			}
		}
		
		if(memberInfoId != null && !"".equals(memberInfoId)) {
			map.put("memberInfoId", memberInfoId);
			memberInfoMap = binOLWRMRP01_BL.searchMemInfo(map);
			if(memberInfoMap == null) {
				ConvertUtil.setResponseByAjax(response, "0");
				return null;
			}
		} else {
			ConvertUtil.setResponseByAjax(response, "1");
			return null;
		}
		return SUCCESS;
	}
	
	/**
	 * 会员信息查询
	 * 
	 * @return 会员信息查询画面 
	 */
	public String list() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 会员卡号
		if(form.getMemCode() != null && !"".equals(form.getMemCode())) {
			map.put("memberCode", form.getMemCode());
		}
		// 会员姓名
		if(form.getMemName() != null && !"".equals(form.getMemName())) {
			map.put("memberName", form.getMemName());
		}
		// 会员手机
		String mobilePhone = form.getMobilePhone();
		// 会员手机加密
		if (mobilePhone != null && !"".equals(mobilePhone)) {
			map.put("mobilePhone", CherrySecret.encryptData(userInfo.getBrandCode(),mobilePhone));
		}
		// 会员生日
		String brithMonth = form.getBirthDayMonth();
		String brithDate = form.getBirthDayDate();
		if(brithMonth != null && !"".equals(brithMonth) 
				&& brithDate != null && !"".equals(brithDate)) {
			if(brithMonth.length() == 1) {
				brithMonth = "0" + brithMonth;
			}
			if(brithDate.length() == 1) {
				brithDate = "0" + brithDate;
			}
			map.put("birthDay", brithMonth+brithDate);
		}
		int count = binOLWRMRP01_BL.getMemCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			memberInfoList = binOLWRMRP01_BL.searchMemList(map);
		}
		return SUCCESS;
	}
	
	/** 会员详细信息 */
	private Map memberInfoMap;
	
	/** 会员信息List */
	private List<Map<String, Object>> memberInfoList;
	
	public Map getMemberInfoMap() {
		return memberInfoMap;
	}

	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}

	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	/** 会员信息查询Form **/
	private BINOLWRMRP01_Form form = new BINOLWRMRP01_Form();

	@Override
	public BINOLWRMRP01_Form getModel() {
		return form;
	}

}
