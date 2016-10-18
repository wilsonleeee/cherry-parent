package com.cherry.jn.man.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.jn.common.bl.BINOLJNCOM01_BL;
import com.cherry.jn.common.service.BINOLJNCOM01_Service;
import com.cherry.jn.man.form.BINOLJNMAN04_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 积分查看Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN04_Action extends BaseAction implements
ModelDriven<BINOLJNMAN04_Form>{
	
	/** 参数FORM */
	private BINOLJNMAN04_Form form = new BINOLJNMAN04_Form();
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNCOM01_BL binoljncom01_BL;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	@Resource
	private BINOLJNCOM01_Service binoljncom01_Service;
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	/**
	 * <p>
	 * 积分一览画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		form.setCampaignType("3");
		form.setCampaignTypeName("会员积分");
		getBrandList();
		String brandInfoId = form.getBrandInfoId();
		if (null == brandInfoId && null != form.getBrandInfoList() && !form.getBrandInfoList().isEmpty()) {
			brandInfoId = String.valueOf(form.getBrandInfoList().get(0).get("brandInfoId"));
		}
		if (null != brandInfoId) {
			// 取得会员俱乐部List
			clubList = queryClubList(brandInfoId);
			if (CherryChecker.isNullOrEmpty(form.getMemberClubId()) &&  null != clubList &&
					!clubList.isEmpty()) {
				form.setMemberClubId(String.valueOf(clubList.get(0).get("memberClubId")));
			}
		}
		return "success";
	}

	/**
	 * 取得品牌信息
	 */
	public void getBrandList() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		//int brandInfoId = CherryConstants.BRAND_INFO_ID_VALUE;
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandList = binOLCM05_BL.getBrandInfoList(map);
			form.setBrandInfoList(brandList);
			if (null != brandList && !brandList.isEmpty()) {
				map.put("brandInfoId", ((Map<String, Object>) 
						brandList.get(0)).get("brandInfoId"));
			}
		} else {
			map.put("brandInfoId", brandInfoId);
			// 取得品牌名称
			form.setBrandName(binOLCM05_BL.getBrandName(map));
			// 品牌ID
			form.setBrandInfoId(ConvertUtil.getString(brandInfoId));
		}
	}
	
	/**
	 * 取得会员俱乐部List
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> queryClubList(String brandInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put("brandInfoId", brandInfoId);
		return binOLCM05_BL.getClubList(map);
	}
	
	public String search() throws Exception{
		Map<String, Object> baseMap = (Map<String, Object>) Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, baseMap);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		baseMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		baseMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 活动类型
		baseMap.put("campaignType", form.getCampaignType());
		// 业务日期
		String busDate = binoljncom01_Service.getBussinessDate(baseMap);
		baseMap.put("busDate", busDate);
		int count = binoljncom01_Service.getCampaignRuleCount(baseMap);
		if(count > 0){
			List<Map<String, Object>> camtempList = binoljncom01_Service.getCampaignList(baseMap);
			// 取得积分活动信息List
			form.setCamtempList(camtempList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "success";
	}
	
	/**
	 * 验证默认规则
	 */
	public String checkDefRule() {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		searchMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		searchMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 会员俱乐部ID
		searchMap.put("memberClubId", form.getMemberClubId());
		// 活动类型
		searchMap.put("campaignType", form.getCampaignType());
		// 取得默认规则数
		int count = binoljncom01_Service.getDefaultCount(searchMap);
		if (0 == count) {
			this.addActionError("请先创建默认积分规则！");
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	@Override
	public BINOLJNMAN04_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
}