package com.cherry.mb.mbm.action;

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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM03_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM03_Form;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 会员积分维护Action
 * 
 * @author LuoHong
 * @version 1.0 2012.06.06
 */
@SuppressWarnings("unchecked")
public class BINOLMBMBM08_Action extends BaseAction implements ModelDriven<BINOLMBMBM03_Form>{
	
	private static final long serialVersionUID = -5372261684018148097L;
	/** 会员属性调整BL */
	@Resource
	private BINOLMBMBM03_BL binOLMBMBM03_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	/** 会员信息 */
	private Map memberInfoMap;
	
	/** 会员配置项List */
	private List<Map<String, Object>> memberConfiglist;
	
	/** 会员积分记录List */
	private List<Map<String, Object>> memPointList;
	
		/** 备注信息 */
	private Map reasonInfoMap;
	
	public Map getReasonInfoMap() {
		return reasonInfoMap;
	}
	public void setReasonInfoMap(Map reasonInfoMap) {
		this.reasonInfoMap = reasonInfoMap;
	}

	
	public List<Map<String, Object>> getMemberConfiglist() {
		return memberConfiglist;
	}
	public void setMemberConfiglist(List<Map<String, Object>> memberConfiglist) {
		this.memberConfiglist = memberConfiglist;
	}

	public Map getMemberInfoMap() {
		return memberInfoMap;
	}
	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}

	public List<Map<String, Object>> getMemPointList() {
		return memPointList;
	}
	public void setMemPointList(List<Map<String, Object>> memPointList) {
		this.memPointList = memPointList;
	}

	/** 会员属性调整Form */
	private BINOLMBMBM03_Form form = new BINOLMBMBM03_Form();

	@Override
	public BINOLMBMBM03_Form getModel() {
		return form;
	}
	
	private String clubMod;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
		// 不是总部的场合
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 会员Id
		map.put("memberInfoId", form.getMemberInfoId());
		// 业务类型
		map.put("tradeType", "PT");
		// 俱乐部模式
		clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!"3".equals(clubMod)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得会员俱乐部列表
			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
			if (null != clubInfoList && !clubInfoList.isEmpty()) {
				// 取得会员已经拥有的俱乐部列表
				clubList = binOLCM05_BL.getMemClubList(map);
				if (null == clubList || clubList.isEmpty()) {
					clubList = clubInfoList;
				} else {
					for (Map<String, Object> clubMap : clubList) {
						// 会员俱乐部ID
						String memberClubId = String.valueOf(clubMap.get("memberClubId"));
						for (int i = 0; i < clubInfoList.size(); i++) {
							Map<String, Object> clubInfo = clubInfoList.get(i);
							if (memberClubId.equals(String.valueOf(clubInfo.get("memberClubId")))) {
								clubMap.putAll(clubInfo);
								clubInfoList.remove(i);
								break;
							}
						}
					}
					clubList.addAll(clubInfoList);
					map.put("memberClubId", clubList.get(0).get("memberClubId"));
				}
			}
		}
		// 会员信息Map
		memberInfoMap = binOLMBMBM03_BL.getMemberInfo(map);
		// 积分修改状态List
		memPointList = binOLMBMBM03_BL.getMemPointList(map);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 查询会员积分信息
	 * </p>
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String searchMemInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
		// 不是总部的场合
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 会员Id
		map.put("memberInfoId", form.getMemberInfoId());
		// 业务类型
		map.put("tradeType", "PT");
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			map.put("memberClubId", form.getMemberClubId());
		}
		// 会员信息Map
		memberInfoMap = binOLMBMBM03_BL.getMemberInfo(map);
		// 积分修改状态List
		memPointList = binOLMBMBM03_BL.getMemPointList(map);
		return SUCCESS;
	}
	
	
	/**
	 * 积分维护
	 * @param map
	 * return 更新成功画面
	 * */
	public String updateCurPoint() throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 组织code
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌code
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			//员工code
			map.put("employeeCode",userInfo.getEmployeeCode());
			//会员ID
			map.put("memberInfoId", form.getMemberInfoId());
			// 会员俱乐部ID
			map.put("memberClubId", form.getMemberClubId());
			//会员卡号
			map.put("memberCode", form.getMemCode());
			//会员总积分值
			map.put("curPoints", form.getTotalPoint());
			//总积分指定时日
			map.put("dateTime",form.getDateTime());
			//指定时间(时)
			map.put("startHH", form.getStartHH());
			//指定时间(分)
			map.put("startMM", form.getStartMM());
			//指定时间(秒)
			map.put("startSS", form.getStartSS());
			//会员差值积分
			map.put("difPoint", form.getDifPoint());
			//差值积分指定时间
			map.put("difdateTime",form.getDifdateTime());
			//差值积分指定时间(时)
			map.put("startHour", form.getStartHour());
			//差值积分指定时间(分)
			map.put("startMinute", form.getStartMinute());
			//差值积分指定时间(秒)
			map.put("startSecond", form.getStartSecond());
			//积分维护类型
			map.put("pointType", form.getPointType());
			//备注
			map.put("reason",form.getComments().trim());
			//更新时间
			map.put("pointUdTime",form.getPointUdTime());
			//更新次数
			map.put("pointMdCount",form.getPointMdCount());
			binOLMBMBM03_BL.tran_update(map);
			//处理成功
			this.addActionMessage(getText("ICM00002"));
		}catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	/**
	 *<p>
	 * 验证处理
	 *</p>
	 * @param 无
	 * @return 
	 * @throws Exception
	 * */
	public void validateUpdateCurPoint() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userInfo.getBIN_UserID());
		@SuppressWarnings("unused")
		boolean isCorrect = true;
		if("1".equals(form.getPointType())){
			//时分秒匹配表达式
			String timeRegex = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
			// 时
			String startHH = ConvertUtil.getString(form.getStartHH());
			// 分
			String startMM = ConvertUtil.getString(form.getStartMM());
			// 秒
			String startSS = ConvertUtil.getString(form.getStartSS());
			// 指定日期验证
			String dateTime = ConvertUtil.getString(form.getDateTime());
			if(CherryChecker.isNullOrEmpty(dateTime, true)){
				this.addFieldError("dateTime", getText("EMB00011"));
			}else if(!CherryChecker.checkDate(dateTime, CherryConstants.DATE_PATTERN)){
				//格式不正确
				this.addFieldError("difdateTime", getText("PCP00028"));
			}
			String startTime = null;
			if (CherryChecker.isNullOrEmpty(startHH, true) ||CherryChecker.isNullOrEmpty(startMM, true)||CherryChecker.isNullOrEmpty(startSS, true)) {
				this.addFieldError("startHH", getText("EMB00011"));
			}else{
				startTime = startHH + ":" + startMM + ":" + startSS;
				if (!startTime.matches(timeRegex)) {//验证时分秒
					this.addFieldError("startHH", getText("EMB00010"));
				}
			}
			//总积分值验证
			if (CherryChecker.isNullOrEmpty(form.getTotalPoint(), true)) {
				this.addFieldError("totalPoint", getText("EMB00011"));
			}else{
				if (!CherryChecker.isFloatValid(form.getTotalPoint(), 10, 2)) {
					this.addFieldError("totalPoint", getText("ECM00049",
							new String[] { getText("EMB00012"), "10", "2" }));
					isCorrect = false;
				}
			}
		}else{
			//时分秒匹配表达式
			String timeRegex = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
			// 时
			String startHour = ConvertUtil.getString(form.getStartHour());
			// 分
			String startMinute = ConvertUtil.getString(form.getStartMinute());
			// 秒
			String startSecond = ConvertUtil.getString(form.getStartSecond());
			// 指定日期验证
			String difdateTime = ConvertUtil.getString(form.getDifdateTime());
			if(CherryChecker.isNullOrEmpty(difdateTime, true)){
				this.addFieldError("difdateTime", getText("EMB00011"));
			}else if(!CherryChecker.checkDate(difdateTime, CherryConstants.DATE_PATTERN)){
				//格式不正确
				this.addFieldError("difdateTime", getText("PCP00028"));
			}
			String startTime = null;
			if (CherryChecker.isNullOrEmpty(startHour, true) ||CherryChecker.isNullOrEmpty(startMinute, true)||CherryChecker.isNullOrEmpty(startSecond, true)) {
				this.addFieldError("startHour", getText("EMB00011"));
			}else{
				startTime = startHour + ":" + startMinute + ":" + startSecond;
				if (!startTime.matches(timeRegex)) {//验证时分秒
					this.addFieldError("startHour", getText("EMB00010"));
				}
			}
			//积分差值验证
			if (CherryChecker.isNullOrEmpty(form.getDifPoint(), true)) {
				this.addFieldError("difPoint", getText("EMB00011"));
			}else{
				if (!CherryChecker.isPositiveAndNegative(form.getDifPoint())) {
					this.addFieldError("difPoint", getText("ECM00021",
							new String[] { getText("EMB00013")}));
					isCorrect = false;
				}
			}
		} 
		String comment = ConvertUtil.getString(form.getComments());
		//备注长度验证
		if(CherryChecker.isNullOrEmpty(comment, true)){
			this.addFieldError("comments", getText("EMB00011"));
		}else{
			if(comment.length()>100){
				this.addFieldError("comments", getText("ECM00020",
						new String[] { getText("PMB00070"),"100"}));
			}
		}
	}
	/**
	 *<p>
	 * 获取备注详细
	 *</p>
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * */
	public String getReason () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
 			//积分明细ID
 			map.put("memUsedDetailId", form.getMemPointId());
 			
 			// 会员信息Map
 			reasonInfoMap = binOLMBMBM03_BL.getReason(map);

		return SUCCESS;
	}
}

