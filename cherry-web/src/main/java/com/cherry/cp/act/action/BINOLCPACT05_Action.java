package com.cherry.cp.act.action;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.act.bl.BINOLCPACT05_BL;
import com.cherry.cp.act.form.BINOLCPACT05_Form;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动批量预约Action
 * 
 * @author LuoHong
 * @version 1.0 2013/04/24
 */

public class BINOLCPACT05_Action extends BaseAction implements
		ModelDriven<BINOLCPACT05_Form> {

	private static final long serialVersionUID = -2968319702234955138L;

	/** 会员活动预约Form */
	private BINOLCPACT05_Form form = new BINOLCPACT05_Form();

	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;

	/** 会员活动预约BL */
	@Resource(name="binOLCPACT05_BL")
	private BINOLCPACT05_BL bl;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03bl;

	/** 会员活动DTO */
	private Map<String, Object> campaignInfo;

	/** 会员活动档次信息 */
	private List<Map<String, Object>> subCampaignList;

	public Map<String, Object> getCampaignInfo() {
		return campaignInfo;
	}

	public void setCampaignInfo(Map<String, Object> campaignInfo) {
		this.campaignInfo = campaignInfo;
	}

	public List<Map<String, Object>> getSubCampaignList() {
		return subCampaignList;
	}

	public void setSubCampaignList(List<Map<String, Object>> subCampaignList) {
		this.subCampaignList = subCampaignList;
	}

	/**
	 * 活动批量处理初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String actBatchReservation() throws Exception {
		Map<String, Object> map = getCommMap();
		map.put("campaignId", form.getCampaignId());
		// 查询会员活动首页内容
		campaignInfo = bl.getCampaignInfo(map);
//		if (validCampOrder(campaignInfo)) {
//			// 档次信息List
//			subCampaignList = bl.getSubCampList(map);
//			return "BINOLCPACT05";
//		} else {
//			return "BINOLCPACT05_1";
//		}
		
		subCampaignList = bl.getSubCampList(map);
		return "BINOLCPACT05";
	}

	/**
	 * 提交预约信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String confirm() throws Exception {
		try {
			int result = CherryConstants.SUCCESS;
			// 参数MAP
			Map<String, Object> comMap = getCommMap();
			Map<String, Object> map = new HashMap<String, Object>(comMap);
			map.put(CampConstants.SUBCAMP_CODE, form.getSubCampCode());
			// 主题活动信息
			Map<String, Object> campInfo = com05IF.getCampInfo(form
					.getSubCampCode());
			// 预约次数
//			campInfo.put("times", form.getTimes());
			// 档次礼品信息List
			List<Map<String, Object>> prtList = bl.getPrtList(map);
			if (null != prtList) {
				for (Map<String, Object> prt : prtList) {
					int q = ConvertUtil.getInt(prt.get(CampConstants.QUANTITY));
					prt.put(CampConstants.QUANTITY, q * form.getTimes());
				}
			}
			String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
			String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
			comMap.put(CampConstants.BATCHNO, batchNo);
			// 添加到活动预约相关表
			Map<String,Object> param = (Map<String,Object>)Bean2Map.toHashMap(form);
			result = bl.addOrderInfo(comMap, param, campInfo, prtList);
			// 发送积分维护MQ
			int r = com05IF.sendPointMQ(comMap, campInfo);
			if(r > result){
				result = r;
			}
			// 发送沟通MQ
			int r2 = com05IF.sendGTMQ(comMap, batchNo, CampConstants.BILL_STATE_RV);
			if( r2 > result){
				result = r2;
			}
			if (CherryConstants.SUCCESS == result) {
				// 成功信息SUCCESS
				this.addActionMessage(getText("ICM00002"));
			} else if (CherryConstants.WARN == result) {
				// 警告：活动预约操作异常！
				this.addActionError(getText("ACT00038"));
			}
		} catch (CherryException e) {// 错误信息ERROR
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 批量导入Excel
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void importExcel() throws Exception {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			int result = CherryConstants.SUCCESS;
			int sumNum = 0;
			int successNum = 0;
			// 参数MAP
			Map<String, Object> comMap = getCommMap();
			// 解析成功后的活动对象Map
			List<Map<String, Object>> orderList = bl
					.parseMemExcel(form.getUpExcel(), comMap);
			if (null != orderList && orderList.size() > 0) {
				sumNum = orderList.size();
				Map<String, Object> mem = orderList.get(0);
				List<Map<String, Object>> prtList = (List<Map<String, Object>>) mem
						.get(CampConstants.KEY_LIST);
				if (null != prtList && prtList.size() > 0) {
					String subCampCode = ConvertUtil.getString(prtList.get(0)
							.get(CampConstants.SUBCAMP_CODE));
					// 主题活动信息
					Map<String, Object> campInfo = com05IF
							.getCampInfo(subCampCode);
					if(null == campInfo){
						throw new CherryException("ACT00044", new String[] {subCampCode});
					}
					String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
					String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
					String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
					comMap.put(CampConstants.BATCHNO, batchNo);
					int fromIndex = 0;
					while (true) {
						List<Map<String, Object>> pageList = CherryUtil
								.getSubList(orderList, fromIndex,
										CherryConstants.BATCH_PAGE_MAX_NUM);
						if (null != pageList && pageList.size() > 0) {
							// 活动预约表状态操作
							int r = com05IF.tran_campOrderBAT(comMap, campInfo,
									CampConstants.BILL_STATE_RV, pageList);
							successNum += pageList.size();
							if (r > result) {
								result = r;
							}
							if(pageList.size() < CherryConstants.BATCH_PAGE_MAX_NUM){
								break;
							}
							fromIndex += CherryConstants.BATCH_PAGE_MAX_NUM;
						} else {
							break;
						}
					}
					// 发送积分维护MQ
					int r = com05IF.sendPointMQ(comMap, campInfo);
					if(r > result){
						result = r;
					}
					// 发送沟通MQ
					int r2 = com05IF.sendGTMQ(comMap, batchNo, CampConstants.BILL_STATE_RV);
					if( r2 > result){
						result = r2;
					}
				}
			}
			if (CherryConstants.SUCCESS == result) {
				msgMap.put("msg", getText("ACT000107",new String[]{sumNum+"",successNum+""}));
				msgMap.put("level", 0);
			} else if (CherryConstants.WARN == result) {
				// 警告：活动预约操作异常！
				msgMap.put("msg", getText("ACT00038"));
				msgMap.put("level", 1);
			}
		} catch (CherryException e) {
			msgMap.put("msg", e.getErrMessage());
			msgMap.put("level", 2);
		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, msgMap);
	}

	/**
	 * 共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getCommMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 用户Id
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 员工code
		map.put("employeeCode", userInfo.getEmployeeCode());
		// 组织Id
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 组织Code
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 品牌Id
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 业务日期
		String busDate = bl.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, busDate);
		map.put("sysTime", bl.getSYSDate());
		Date date = new Date();
		busDate += CherryConstants.SPACE
				+ DateUtil.date2String(date, DateUtil.TIME_PATTERN);
		map.put(CampConstants.OPT_TIME, busDate);
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.CREATEPGM, "BINOLCPACT05");
		map.put(CherryConstants.UPDATEPGM, "BINOLCPACT05");
		return map;
	}

	/**
	 * 活动初始化信息处理
	 * 
	 * @param campDto
	 * @throws ParseException
	 */
	private boolean validCampOrder(Map<String, Object> campInfoMap)
			throws Exception {
		// 共通信息Map
		Map<String, Object> map = getCommMap();
		// 业务日期
		String busTime = bl.getBussinessDate(map);
		// 活动状态
		String state = ConvertUtil.getString(campInfoMap.get("state"));
		if ("1".equals(state)) {// 进行中的活动
			// 活动预约开始时间
			String startTime = ConvertUtil.getString(campInfoMap
					.get("CampaignOrderFromDate"));
			if ("".equals(startTime)) {
				this.addActionError(getText("ACT00032"));
				return false;
			} else {
				if (CherryChecker.compareDate(busTime, startTime) < 0) {
					this.addActionError(getText("ACT00033"));
					return false;
				}
				// 活动预约结束时间
				String endTime = ConvertUtil.getString(campInfoMap
						.get("CampaignOrderToDate"));
				if (CherryChecker.compareDate(busTime, endTime) > 0) {
					this.addActionError(getText("ACT00034"));
					return false;
				}
			}

		} else {// 未开始，经过期，草稿中的活动
			if ("0".equals(state)) {
				this.addActionError(getText("ACT00035"));
			} else if ("2".equals(state)) {
				this.addActionError(getText("ACT00036"));
			} else if ("3".equals(state)) {
				this.addActionError(getText("ACT00037"));
			}
			return false;
		}
		return true;
	}

	@Override
	public BINOLCPACT05_Form getModel() {
		return form;
	}
}
