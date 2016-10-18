package com.cherry.wp.sal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.sal.form.BINOLWPSAL11_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL11_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL11_Action extends BaseAction implements ModelDriven<BINOLWPSAL11_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 936484048469921233L;

	private BINOLWPSAL11_Form form = new BINOLWPSAL11_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL11_Action.class);
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLWPCM01_BL")
	private BINOLWPCM01_IF binOLWPCM01_IF;
	
	@Resource(name="binOLWPSAL11_BL")
	private BINOLWPSAL11_IF binOLWPSAL11_IF;
	
	@Resource(name="binOLMBMBM11_BL")
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	private String memberRule;
	
	private String mobileRule;
	
	public String getMemberRule() {
		return memberRule;
	}

	public void setMemberRule(String memberRule) {
		this.memberRule = memberRule;
	}

	public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
	}
	
	/** 是否需要开启会员手机验证 */
	private String isNeedCheck;

	public String getIsNeedCheck() {
		return isNeedCheck;
	}

	public void setIsNeedCheck(String isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}

	@SuppressWarnings("unchecked")
	public String init(){
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());		
			// 取得区域List
			List<Map<String, Object>> reginList = binOLCM08_BL.getReginList(map);
			form.setReginList(reginList);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 柜台部门ID
			paramMap.put("organizationId", counterInfo.getOrganizationId());
			// 查询柜台营业员信息
			// 获取BA列表,根据配置项来取用是考勤的员工还是忽略考勤的员工
			String attendanceFlag=binOLCM14_BL.getWebposConfigValue("9044", organizationInfoId, brandInfoId);
			if(null == attendanceFlag || "".equals(attendanceFlag)){
				attendanceFlag = "N";
			}
			List<Map<String, Object>> baList = null;
			if("N".equals(attendanceFlag)){
				baList = binOLWPCM01_IF.getBAInfoList(paramMap);
			}else{
				baList = binOLWPCM01_IF.getActiveBAList(paramMap);
			}
			form.setBaList(baList);
			// 手机号校验规则
			memberRule = binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandInfoId);
			// 手机号校验规则
			mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
			//是否需要手机验证
			isNeedCheck = binOLCM14_BL.getWebposConfigValue("9037", organizationInfoId, brandInfoId);
			//是否开启手机卡号同步
			form.setCardMobileSyn(binOLCM14_BL.getWebposConfigValue("9040", organizationInfoId, brandInfoId));
			// 生日格式
			String birthFormat = binOLCM14_BL.getWebposConfigValue("9012", organizationInfoId, brandInfoId);
			form.setBirthFormat(birthFormat);
			//生日是否必填项
			String birthFlag=binOLCM14_BL.getWebposConfigValue("9041", organizationInfoId, brandInfoId);
			form.setBirthFlag(birthFlag);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	public void addMember() throws Exception{
		try{
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			String counterCode = counterInfo.getCounterCode();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			
			boolean addFlag = true;
			String memberCode = ConvertUtil.getString(form.getDgMemberCode()).trim();
			Map<String, Object> validResultMap = validMemberCode(memberCode);
			if(validResultMap != null && !validResultMap.isEmpty()){
				String memberValidState = ConvertUtil.getString(validResultMap.get("memberValidState"));
				if("T".equals(memberValidState)){
					// 已存在会员的情况下
					addFlag = false;
				}else{
					// 会员假登录的情况下
					addFlag = true;
				}
			}else{
				// 会员不存在的情况下
				addFlag = true;
			}
			// 判断验证结果是否允许增加会员
			if(addFlag){
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 会员卡号
				map.put("memCode", memberCode);
				// 会员姓名
				map.put("memName", form.getDgMemberName());
				// 会员手机号
				map.put("mobilePhone", form.getDgMobilePhone());
				// 电话号码
				map.put("telephone", form.getDgTelephone());
				// 发卡BA员工ID
				map.put("employeeId", form.getDgBaInfoId());
				// 电子邮箱
				map.put("email", form.getDgEmail());
				// 会员生日
				if("2".equals(ConvertUtil.getString(form.getBirthFormat()))){
					String birthMonthValue = form.getBirthMonthValue();
					String birthDayValue = form.getBirthDayValue();
					if(birthMonthValue.length() == 1){
						birthMonthValue = "0" + birthMonthValue;
					}
					if(birthDayValue.length() == 1){
						birthDayValue = "0" + birthDayValue;
					}
					String birthDay = birthMonthValue + birthDayValue;
					map.put("birthDay", birthDay);
				}else{
					map.put("birth", form.getDgBirthday());
				}
				// 会员性别
				map.put("gender", form.getDgGender());
				// 身份证号
				map.put("identityCard", form.getDgIdentityCard());
				// 微博号
				map.put("blogId", form.getDgBlogId());
				// 微信号
				map.put("messageId", form.getDgMessageId());
				// 推荐会员
				map.put("referrer", form.getDgReferrer());
				// 是否接收短信
				map.put("isReceiveMsg", "1");
				// 省份ID
				map.put("provinceId", form.getProvinceId());
				// 城市ID
				map.put("cityId", form.getCityId());
				// 区县ID
				map.put("countyId", form.getCountyId());
				// 详细地址
				map.put("address", form.getDgAddress());
				// 邮编号码
				map.put("postcode", form.getDgPostCode());
				// 备注
				map.put("memo", form.getDgMemo());
				// 柜台号
				map.put("counterCode", counterCode);
				// 操作柜台
				map.put("modifyCounter", counterCode);
				// 组织代码
				map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
				// 品牌代码
				map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
				// 作成者
				map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
				// 作成程序名
				map.put(CherryConstants.CREATEPGM, "BINOLWPSAL11");
				// 更新者
				map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
				// 更新程序名
				map.put(CherryConstants.UPDATEPGM, "BINOLWPSAL11");
				// 操作员工
				map.put("modifyEmployee", userInfo.getEmployeeCode());
				// 假登录会员会员ID、等级相关信息
				if(validResultMap != null && !validResultMap.isEmpty()){
					map.putAll(validResultMap);
				}
				
				// 增加会员
				String addResult = binOLWPSAL11_IF.addMember(map);
				if("SUCCESS".equals(addResult)){
					// 增加会员成功后获取需要生成Coupon码的活动，判断活动是否已生成Coupon码，若未生成则给会员生成Coupon码
					
					
					
					// 获取新入会会员的会员资料
					Map<String, Object> parMap = new HashMap<String, Object>();
					parMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					parMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					parMap.put("searchStr", form.getDgMemberCode());
					// 获取会员资料
					Map<String, Object> memberInfo = binOLWPCM01_IF.getMemberInfo(map);
					if(memberInfo != null && !memberInfo.isEmpty()){
						ConvertUtil.setResponseByAjax(response, memberInfo);
					}else{
						// 返回结果出现为空的异常情况
						ConvertUtil.setResponseByAjax(response, "SEARCHERROR");
					}
				}else{
					ConvertUtil.setResponseByAjax(response, "ERROR");
				}
			}else{
				Map<String, Object> parMap = new HashMap<String, Object>();
				parMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				parMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				parMap.put("memberInfoId", ConvertUtil.getString(validResultMap.get("memberInfoId")));
				// 获取会员资料
				Map<String, Object> memberInfo = binOLWPCM01_IF.getMemberInfo(parMap);
				memberInfo.put("memberValidState", "T");
				ConvertUtil.setResponseByAjax(response, memberInfo);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
	}
	
	public Map<String, Object> validMemberCode(String memCode) throws Exception{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 会员卡号
		map.put("memCode", memCode);
		// 通过会员卡号查询会员信息
		Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
		if(memberInfoMap != null) {
			Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
			if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
				// 会员已存在，但是为假登陆状态
				returnMap.put("memberValidState", "J");
				String memberLevel = ConvertUtil.getString(memberInfoMap.get("memberLevel"));
				if(memberLevel != null && !"".equals(memberLevel)) {
					String levelName = ConvertUtil.getString(memberInfoMap.get("levelName"));
					returnMap.put("oldLevelName", levelName);
					returnMap.put("oldMemberLevel", memberLevel);					
				}
				String memberInfoId = ConvertUtil.getString(memberInfoMap.get("memberInfoId"));
				returnMap.put("memberInfoId", memberInfoId);
				return returnMap;
			} else {
				// 会员已存在
				returnMap.put("memberValidState", "T");
				returnMap.putAll(memberInfoMap);
				return returnMap;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 初始化入会短信验证界面
	 * @return
	 */
	public String mobileCheckInit(){
		return SUCCESS;
	}
	
	public void validateAddMember() throws Exception {
		// 手机验证
		if(form.getDgMobilePhone() != null && !"".equals(form.getDgMobilePhone())) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 会员手机号
				map.put("mobilePhone", form.getDgMobilePhone());
				List<String> memMobileList = binOLMBMBM11_BL.getMemMobile(map);
				if(memMobileList != null && !memMobileList.isEmpty()) {
					this.addFieldError("dgMobilePhone", getText("MBM00052"));
				}
			}
		}
	}
	
	public void validateMobileCheckInit() throws Exception {
		// 手机验证
		if(form.getDgMobilePhone() != null && !"".equals(form.getDgMobilePhone())) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 会员手机号
				map.put("mobilePhone", form.getDgMobilePhone());
				List<String> memMobileList = binOLMBMBM11_BL.getMemMobile(map);
				if(memMobileList != null && !memMobileList.isEmpty()) {
					this.addFieldError("dgMobilePhone", getText("MBM00052"));
				}
			}
		}
	}
	
	@Override
	public BINOLWPSAL11_Form getModel() {
		return form;
	}
}
