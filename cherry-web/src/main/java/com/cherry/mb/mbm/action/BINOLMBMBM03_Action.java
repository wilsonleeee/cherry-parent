package com.cherry.mb.mbm.action;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM31_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM03_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM03_Form;
import com.cherry.mq.mes.common.MessageConstants;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 会员属性调整Action
 * 
 * @author LuoHong
 * @version 1.0 2012.06.06
 */
@SuppressWarnings("unchecked")
public class BINOLMBMBM03_Action extends BaseAction implements ModelDriven<BINOLMBMBM03_Form>{
	
	private static final long serialVersionUID = -5372261684018148097L;
	/** 会员属性调整BL */
	@Resource
	private BINOLMBMBM03_BL binOLMBMBM03_BL;
	/** 共通 */
	@Resource
	private BINOLCM31_BL binOLCM31_BL;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 会员信息 */
	private Map memberInfoMap;

	/** 会员修改状态List */
	private List<Map<String, Object>> memUsedInfoList;
	
	/** 会员等级List */
	private List<Map<String, Object>> memberLevellist;
	
	/** 会员等级List */
	private List<Map<String, Object>> memberConfiglist;
	
	/** 备注信息 */
	private Map reasonInfoMap;
	
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
	public List<Map<String, Object>> getMemberLevellist() {
		return memberLevellist;
	}
	public void setMemberLevellist(List<Map<String, Object>> memberLevellist) {
		this.memberLevellist = memberLevellist;
	}
	public Map getMemberInfoMap() {
		return memberInfoMap;
	}
	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}
	public List<Map<String, Object>> getMemUsedInfoList() {
		return memUsedInfoList;
	}
	public void setMemUsedInfoList(List<Map<String, Object>> memUsedInfoList) {
		this.memUsedInfoList = memUsedInfoList;
	}

	/** 会员属性调整Form */
	private BINOLMBMBM03_Form form = new BINOLMBMBM03_Form();

	@Override
	public BINOLMBMBM03_Form getModel() {
		return form;
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
		map.put("tradeType", MessageConstants.MSG_MEMBER_MS);
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
				//	map.put("memberClubId", clubList.get(0).get("memberClubId"));
				}
			}
		}
		// 会员信息Map
		//memberInfoMap = binOLMBMBM03_BL.getMemberInfo(map);
		// 会员修改状态List
		//memUsedInfoList=binOLMBMBM03_BL.getMemUsedInfoList(map);
		//取得会员配置项list
		memberConfiglist= binOLCM14_BL.getConfigValByGroup(13,userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 查询会员等级变化情况
	 * </p>
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String searchLevel() throws Exception {
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
		map.put("tradeType", MessageConstants.MSG_MEMBER_MS);
		map.put("memberClubId", form.getMemberClubId());
		// 会员信息Map
		memberInfoMap = binOLMBMBM03_BL.getMemberInfo(map);
		// 会员修改状态List
		memUsedInfoList=binOLMBMBM03_BL.getMemUsedInfoList(map);
		//取得会员配置项list
		memberConfiglist= binOLCM14_BL.getConfigValByGroup(13,userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
		return SUCCESS;
	}
	/**
	 *<p> 
	 * 编辑
	 *</p>
	 * @param 无
	 * @return String 跳转页面
	 */
	public String  editinit()throws Exception{
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
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 会员Id
		map.put("memberInfoId", form.getMemberInfoId());
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
		// 会员信息
		memberInfoMap = binOLMBMBM03_BL.getMemberInfo(map);
		// 会员【手机】字段解密
  		if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("MobilePhone"), true)) {
  			String brandCode = ConvertUtil.getString(userInfo.getBrandCode());
  			String mobilePhone = ConvertUtil.getString(memberInfoMap.get("MobilePhone"));
  			memberInfoMap.put("MobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
  		}
		// 会员等级List
		memberLevellist = binOLCM31_BL.getMemberLevelList(map);
		//取得会员配置项list
		memberConfiglist= binOLCM14_BL.getConfigValByGroup(13,userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
		doList(memberConfiglist);
		return SUCCESS;
	}
	
	/**
	 * AJAX 取得等级列表
	 * 
	 * @throws Exception
	 */
	public void changeLevel() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("memberInfoId", form.getMemberInfoId());
		map.put("memberClubId", form.getMemberClubId());
		// 会员信息
		Map<String, Object> memberInfo = binOLMBMBM03_BL.getMemberInfo(map);
		String oldLevel = "";
		if (null != memberInfo && !CherryChecker.isNullOrEmpty(memberInfo.get("memberLevelId"))) {
			oldLevel = String.valueOf(memberInfo.get("memberLevelId"));
		}
		// 会员等级List
		List<Map<String, Object>> levellist = binOLCM31_BL.getMemberLevelList(map);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("MSGJSON", levellist);
		jsonMap.put("oldLevelId", oldLevel);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, jsonMap);
	}
	
	/**
	 *<P>
	 * 保存
	 *</P>
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	public String save() throws Exception{
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
		//员工id
		map.put("employeeId",userInfo.getBIN_EmployeeID());
		//员工code
		map.put("employeeCode",userInfo.getEmployeeCode());
		//会员id
		map.put("memberInfoId", form.getMemberInfoId());
		//会员id
		map.put("memberCode", form.getMemCode());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		//更新前等级Id
		if(null==form.getOldmemberLevelId()||"".equals(form.getOldmemberLevelId())){
			map.put("oldMemberLevelId", "");
		}else{
			map.put("oldMemberLevelId", form.getOldmemberLevelId());
		}
		if(null==form.getMemberLevelId()||"".equals(form.getMemberLevelId())){
		//等级Id
			map.put("memberLevelId","");
		}else{
			map.put("memberLevelId",form.getMemberLevelId());
		}
		if(null==form.getOldjoinDate()||"".equals(form.getOldjoinDate())){
//		//更新前入会时间
			map.put("oldJoinDate","");
		}else{
			String oldJoinDate=(String)form.getOldjoinDate();
			Date memDate = DateUtil.coverString2Date(oldJoinDate);
			oldJoinDate = DateUtil.date2String(memDate, "yyyy-MM-dd");
			map.put("oldJoinDate",oldJoinDate);
		}
		if(null==form.getJoinDate()||"".equals(form.getJoinDate())){
		//入会时间
			map.put("joinDate","");
		}else {
			String joinDate=(String)form.getJoinDate();
			Date memDate = DateUtil.coverString2Date(joinDate);
			joinDate = DateUtil.date2String(memDate, "yyyy-MM-dd");
			map.put("joinDate",joinDate);
		}
		if(null==form.getOldbtimes()||"".equals(form.getOldbtimes())){
		//更新前化妆次数
			map.put("oldBtimes","");
		}else{
			map.put("oldBtimes",form.getOldbtimes().trim());
		}
		if(null==form.getBtimes()||"".equals(form.getBtimes())){
		//化妆次数
			map.put("btimes","");
		}else{
			map.put("btimes",form.getBtimes().trim());
		}
		//更新前累计金额
		if(null==form.getOldtotalAmount()||"".equals(form.getOldtotalAmount())){
			map.put("oldTotalAmount","");
		}else{
			DecimalFormat df = new DecimalFormat("#0.00");
			String DoubletotalAmount=df.format(Double.valueOf(form.getOldtotalAmount()));
			//累计金额
			map.put("oldTotalAmount",DoubletotalAmount);
		}
		if(null==form.getTotalAmount()||"".equals(form.getTotalAmount())){
			//累计金额
			map.put("totalAmount","");
		}else{
			DecimalFormat df = new DecimalFormat("#0.00");
			String DoubletotalAmount=df.format(Double.valueOf(form.getTotalAmount()));
			//累计金额
			map.put("totalAmount",DoubletotalAmount);
		}
		//备注
		map.put("reason",form.getComments().trim());
		//会员信息更新时间
		map.put("memInfoUdTime",form.getMemInfoUdTime());
		//会员信息更新次数
		map.put("memInfoMdCount",form.getMemInfoMdCount());
		//扩展信息更新时间
		map.put("extInfoUdTime",form.getExtInfoUdTime());
		//扩展信息更新次数
		map.put("extInfoMdCount",form.getExtInfoMdCount());
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			// 会员信息
			Map<String, Object> memberInfo = binOLMBMBM03_BL.getMemberInfo(map);
			if (null != memberInfo && !memberInfo.isEmpty()) {
				//会员俱乐部等级更新时间
				map.put("clubInfoUdTime", memberInfo.get("clubInfoUdTime"));
				//会员俱乐部等级更新次数
				map.put("clubInfoMdCount", memberInfo.get("clubInfoMdCount") );
			}
		}
		binOLMBMBM03_BL.tran_save(map);
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
	public void validateSave() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userInfo.getBIN_UserID());
		@SuppressWarnings("unused")
		boolean isCorrect = true;
		// 累计金额验证
		if (null != form.getTotalAmount()
				&& !"".equals(form.getTotalAmount())) {
				if (!CherryChecker.isFloatValid(form.getTotalAmount(), 14, 2)) {
					this.addFieldError("totalAmount", getText("ECM00049",
							new String[] { getText("EMB00004"), "14", "2" }));
					isCorrect = false;
				}
			}
		// 化妆次数验证
		if (null != form.getBtimes() && !"".equals(form.getBtimes())) {
			if (form.getBtimes().length() > 9) {
				this.addFieldError("btimes", getText("ECM00020",
						new String[] { getText("EMB00003"), "9" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getBtimes())) {
				this.addFieldError("btimes", getText("ECM00021",
						new String[] { getText("EMB00003") }));
				isCorrect = false;
			}
		}
		
	}
	/**
	 * 取得备注信息
	 * @return
	 * @throws Exception
	 */
	public String getReason () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//履历明细ID
		map.put("memUsedDetailId", form.getMemUsedDetailId());
		
		// 会员信息Map
		reasonInfoMap = binOLMBMBM03_BL.getReason(map);

		return SUCCESS;
	}
	/**
	 * 取得配置项
	 * @param list
	 */
	private void doList(List<Map<String, Object>> list){
		if(null != list){
			for(int i = 0 ; i< list.size()-1; i += 2){
				Map<String, Object> map = list.get(i);
				Map<String, Object> map2 = list.get(i+1);
				String configvalue2= (String)map2.get("ConfigValue");
				map.put("configvalue2", configvalue2);
			}
		}
	}
}

