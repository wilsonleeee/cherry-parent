package com.cherry.cp.act.action;

import java.text.ParseException;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.bl.BINOLCPACT02_BL;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampaignDTO;

/**
 * 会员活动详细Action
 * 
 * @author LuoHong
 * @version 1.0 2012/07/10
 */

public class BINOLCPACT02_Action extends BaseAction{

	private static final long serialVersionUID = -2968319702234955138L;

	@Resource
    private BINOLCPACT02_BL binOLCPACT02_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private String campaignId;
	
	private String subCampId;
	
	private Map menuMap;
	
	private CampaignDTO campaignInfo;
	
	private Map subCampInfo;
	
	private Map campExtInfoMap;
	
	private List<Map<String,Object>> subMenuList;
	
	public String getCampaignId() {
		return campaignId;
	}
	
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public Map getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map menuMap) {
		this.menuMap = menuMap;
	}

	public String getSubCampId() {
		return subCampId;
	}

	public void setSubCampId(String subCampId) {
		this.subCampId = subCampId;
	}

	public CampaignDTO getCampaignInfo() {
		return campaignInfo;
	}

	public void setCampaignInfo(CampaignDTO campaignInfo) {
		this.campaignInfo = campaignInfo;
	}

	public Map getSubCampInfo() {
		return subCampInfo;
	}

	public void setSubCampInfo(Map subCampInfo) {
		this.subCampInfo = subCampInfo;
	}
	
	public List<Map<String, Object>> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<Map<String, Object>> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public Map getCampExtInfoMap() {
		return campExtInfoMap;
	}

	public void setCampExtInfoMap(Map campExtInfoMap) {
		this.campExtInfoMap = campExtInfoMap;
	}

	/**
	 * <p>
	 * 画面初期显示(会员活动详细画面)
	 * </p>
	 * 
	 *
	 * @return String 跳转页面
	 * 
	 */
	
	public String actDetailInit() throws Exception {
		Map<String,Object> map = getCommMap();
		// 会员活动ID
		map.put("campaignId", campaignId);
		// 查询会员活动首页内容
		campaignInfo = binOLCPACT02_BL.getCampaignInfo(map);
		validCampOrder(campaignInfo);
		// 取得子活动菜单
		subMenuList = binOLCPACT02_BL.getSubMenuList(map);
		return SUCCESS;
	}
	
	 /* <p>
	 * 画面初期显示(会员活动首页详细画面)
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String actTopInit() throws Exception {
		Map<String,Object> map = getCommMap();
		// 会员活动ID
		map.put("campaignId", campaignId);
		// 查询会员活动首页内容
		campaignInfo = binOLCPACT02_BL.getCampaignInfo(map);
		if (null != campaignInfo.getMemberClubId()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 语言类型
			paramMap.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			paramMap.put("memberClubId", campaignInfo.getMemberClubId());
			campaignInfo.setClubName(binOLCM05_BL.getClubName(paramMap));
		}
		// 品牌名称
		String brandName = ConvertUtil.getString(map.get(CherryConstants.BRAND_NAME));
		campaignInfo.setBrandName(brandName);
		if (null != campaignInfo) {
			// 预约时间不为空
			if (!CherryChecker.isNullOrEmpty(campaignInfo.getCampaignOrderFromDate(), true)) {
				// 需要预约
				campaignInfo.setReseFlag(CampConstants.RESE_FLAG_1);
			} else {
				// 不需要预约
				campaignInfo.setReseFlag(CampConstants.RESE_FLAG_0);
			}
		}
		//会员活动扩展属性信息
		campExtInfoMap = binOLCPACT02_BL.getCampaignExtInfo(map);
		return SUCCESS;
	}
	 
	/**
	 * <p>
	 * 画面初期显示(子活动详细画面)
	 * </p>
	 * 
	 *
	 * @return String 跳转页面
	 * 
	 */
	
	public String subInit() throws Exception {
		Map<String,Object> map = getCommMap();
		map.put("subCampId", subCampId);
		//子活动信息
		subCampInfo = binOLCPACT02_BL.getSubInfo(map);
		String userAuthorityFlag = ConvertUtil.getString(binOLCM14_BL.getConfigValue("1352",
				ConvertUtil.getString(map.get("organizationInfoId")),ConvertUtil.getString(map.get("brandInfoId"))));
		if ("1".equals(userAuthorityFlag)){
			binOLCPACT02_BL.getUserAuthorityPlace(subCampInfo,map);
		}
		// 会员活动ID
		map.put("campaignId", campaignId);
		// 查询会员活动首页内容
		campaignInfo = binOLCPACT02_BL.getCampaignInfo(map);
		return SUCCESS;
	}


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
		// 品牌
		map.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
		return map;
	}
	
	/**
	 * 活动初始化信息处理
	 *
	 * @param dto
	 * @throws ParseException
	 */
	
	@SuppressWarnings("unchecked")
	private void validCampOrder(CampaignDTO dto)
			throws Exception {
		// 共通信息Map
		Map<String, Object> map = getCommMap();
		// 业务日期
		String busTime = binOLCPACT02_BL.getBusDate(map);
		menuMap = new HashMap();
		menuMap.put("order", 1);
		menuMap.put("stock", 1);
		// 活动状态
		String state = dto.getState();
		if ("1".equals(state) || "2".equals(state)) {// 进行中的活动
			// 活动预约开始时间
			if (CherryChecker.isNullOrEmpty(dto.getCampaignOrderFromDate())) {
				menuMap.put("order", 0);
			} else {
				if (CherryChecker.compareDate(busTime, dto.getCampaignOrderFromDate()) < 0) {
					menuMap.put("order", 0);
				}
				// 活动预约结束时间
//				if (CherryChecker.compareDate(busTime, dto.getCampaignOrderToDate()) > 0) {
//					menuMap.put("order", 0);
//				}
			}
			// 活动备货开始时间
			if (CherryChecker.isNullOrEmpty(dto.getCampaignStockFromDate())) {
				menuMap.put("stock", 0);
			} else {
				if (CherryChecker.compareDate(busTime, dto.getCampaignStockFromDate()) < 0) {
					menuMap.put("stock", 0);
				}
				// 结束时间
//				if (CherryChecker.compareDate(busTime, dto.getCampaignStockToDate()) > 0) {
//					menuMap.put("stock", 0);
//				}
			}
		} else {// 未开始，经过期，草稿中的活动
			menuMap.put("order", 0);
			menuMap.put("stock", 0);
		}
	}
}
