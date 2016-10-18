package com.cherry.cp.act.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.bl.BINOLCPACT01_BL;
import com.cherry.cp.act.form.BINOLCPACT01_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCPACT01_Action extends BaseAction implements
ModelDriven<BINOLCPACT01_Form>{

	/**
	 * 会员活动Action
	 * @author LuoHong
	 * @version 1.0 2012/07/10
	 */
	private static final long serialVersionUID = -2968319702234955138L;
	
	/** 会员活动Form */
	private BINOLCPACT01_Form form = new BINOLCPACT01_Form();

	/** 共通 BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14_bl;
	
	/** 会员活动BL */
	@Resource
	private BINOLCPACT01_BL binOLCPACT01_BL;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 会员主题活动List */
	private List<Map<String, Object>> mainList;
	
	/** 会员活动List */
	private List<Map<String, Object>> subList;
		
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}
	
	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	public List<Map<String, Object>> getMainList() {
		return mainList;
	}

	public void setMainList(List<Map<String, Object>> mainList) {
		this.mainList = mainList;
	}
	public List<Map<String, Object>> getSubList() {
		return subList;
	}

	public void setSubList(List<Map<String, Object>> subList) {
		this.subList = subList;
	}
	/**
	 * 会员活动初始化
	 * 
	 * @return
	 */
	public String init()throws Exception{
		// 品牌List
		brandInfoList = binolcm05_BL.getBrandList(session);
		if(CherryChecker.isNullOrEmpty(form.getSearchMode()) || "1".equals(form.getSearchMode())){
			return "BINOLCPACT01";
		}else{
			return "BINOLCPACT02";
		}
	}
	/**
	 * 会员活动查询
	 * 
	 * @return
	 */
	public String search() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户Id
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 操作类型
		map.put("operationType","1");
		// 业务类型
		map.put("businessType","1");
		// 主活动类型
		map.put("campType",form.getCampType());
		// 主活动类型
		map.put("subcampType",form.getSubcampType());
		// 活动状态
		map.put("campState",form.getCampState());
		// 保存状态
		map.put("saveStatus",form.getSaveStatus());
		// 有效状态
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		// 活动代码
		map.put("campCode",form.getCampCode());
		// 活动代码
		map.put("subcampCode",form.getSubcampCode());
		ConvertUtil.setForm(form, map);
		CherryUtil.trimMap(map);
		if("1".equals(form.getSearchMode())){
			//主题活动数
			int maincount = binOLCPACT01_BL.getMainCount(map);
			// 主题活动list
			if (maincount > 0) { 
			mainList = binOLCPACT01_BL.getMainList(map);
			// 用户登陆ID
			form.setLoginUserId(userInfo.getBIN_UserID());
			form.setITotalDisplayRecords(maincount);
			form.setITotalRecords(maincount);
			}
			return "BINOLCPACT01_1";
		}else{
			//活动数
			int subcount = binOLCPACT01_BL.getSubCount(map);
			// 活动数list
			if (subcount > 0) {
				subList = binOLCPACT01_BL.getSubList(map);
			}
			form.setITotalDisplayRecords(subcount);
			form.setITotalRecords(subcount);
			return "BINOLCPACT01_2";
		}
	}
	
	/**
	 * 根据输入的字符串模糊查询会员活动名称
	 * 
	 * 
	 * */
	public void getCampName() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户Id
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		if(userInfo.getBIN_BrandInfoID() == -9999){
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		//主题活动名称模糊查询
		map.put("campInfoStr", form.getCampInfoStr().trim());
		String resultStr = binOLCPACT01_BL.getCampName(map);
		ConvertUtil.setResponseByAjax(response, resultStr);
		
	}
	/**
	 * 根据输入的字符串模糊查询会员活动名称
	 * 
	 * 
	 * */
	public void getSubCampName() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		//活动名称模糊查询
		map.put("subCampInfoStr", form.getSubCampInfoStr().trim());
		String resultStr = binOLCPACT01_BL.getSubCampName(map);
		ConvertUtil.setResponseByAjax(response, resultStr);
		
	}
	
	/**
	 * 停用会员主题活动
	 * @throws Exception
	 */
	public void stopCampaign () throws Exception {
		// 取得共通参数
		Map<String,Object> map =  getCommMap();
		//主题活动Id
		map.put("campaignId", form.getCampaignId());
		map.put("validFlag", form.getValidFlag());
		binOLCPACT01_BL.tran_stopCampaign(map);
		ConvertUtil.setResponseByAjax(response, "");
	}
	/**
	 * 停用会员活动
	 * @throws Exception
	 */
	public  void stopSubCampaign() throws Exception{
		//共通参数Map
		Map<String,Object> map =  getCommMap();
		//活动Id
		map.put("subCampaignId", form.getSubCampaignId());
		map.put("validFlag", form.getValidFlag());
		binOLCPACT01_BL.tran_stopSubCampaign(map);
		ConvertUtil.setResponseByAjax(response, "");
	}
	/**
	 * 共通参数Map
	 * @return
	 */
	private Map<String,Object> getCommMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户Id
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		 // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
        map.put(CherryConstants.UPDATEPGM, "BINOLCPACT01");	
		return map;
	}
	@Override
	public BINOLCPACT01_Form getModel() {
		return form;
	}
	
}
