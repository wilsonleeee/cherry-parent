package com.cherry.wp.sal.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.interfaces.BINOLMBSVC02_IF;
import com.cherry.wp.sal.bl.BINOLWPSAL13_BL;
import com.cherry.wp.sal.form.BINOLWPSAL13_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL02_IF;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL13_Action extends BaseAction implements ModelDriven<BINOLWPSAL13_Form> {

	private static final long serialVersionUID = 1L;
	@Resource
	private BINOLWPSAL13_BL binOLWPSAL13_BL;
	
	/** WebService共通BL */
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 查询会员 */
	@Resource(name="binOLWPSAL02_BL")
	private BINOLWPSAL02_IF binOLWPSAL02_IF;
	
	@Resource(name ="binOLMBSVC02_IF")  
	private BINOLMBSVC02_IF binOLMBSVC02_IF;
	
	/**查询调用Penkonws接口的密钥信息*/
	@Resource(name="thirdPartyConfig")
	private ThirdPartyConfig thirdPartyConfig;
	
	@Resource
	private CodeTable code;
	private String nowDate;
	private Logger logger = LoggerFactory.getLogger(BINOLWPSAL13_Action.class);
	private Map<String, Object> consumptionCodeMap;
	private List<Map<String, Object>> transactionDetailList;
	private Map<String, Object> transactionDetailMap;
	private List<Map<String, Object>> verificationTypeList = new ArrayList<Map<String,Object>>();
	
	
	private BINOLWPSAL13_Form form = new BINOLWPSAL13_Form();
	
	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 检查储值卡是否存在
	 * */
	public void checkCardCode() throws Exception{
		try {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			// 所属组织
			Map<String, Object> map = new HashMap<String, Object>();
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
			map.put("brandCode", userInfo.getBrandCode());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("cardCode", form.getCardCode());
			map.put("memberCode", form.getMemCode());
			map.put("counterCode", counterInfo.getCounterCode());
			//后期调用储值卡信息查询接口
			List<Map<String,Object>> cardInfoList=binOLWPSAL13_BL.getCard(map);
			if((form.getCardCode()==null || "".equals(form.getCardCode().toString())) 
					&& (form.getMemCode()==null || "".equals(form.getMemCode().toString()))){
				ConvertUtil.setResponseByAjax(response, "NOEXIST");
			}else if(null == cardInfoList || cardInfoList.isEmpty()){
				Map<String, Object> pmap = new HashMap<String, Object>();
				pmap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				pmap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				pmap.put("cardCode", ConvertUtil.getString(form.getMemCode()));
				Integer cardId = binOLWPSAL13_BL.getCardId(pmap);
				if(null!=cardId){
					ConvertUtil.setResponseByAjax(response, "SUCCESS");
				}else {
					ConvertUtil.setResponseByAjax(response, "NOEXIST");
				}
			}else{
				if(cardInfoList.size()==1){
					for(Map<String, Object> m : cardInfoList){
						if(null!=m && null!=m.get("ERROR")){
							String error = m.get("ERROR").toString();
							ConvertUtil.setResponseByAjax(response, error);
						}else {
							ConvertUtil.setResponseByAjax(response, m);
						}
					}
				}else{
					ConvertUtil.setResponseByAjax(response, "MORE");
				}
			}
		} catch (Exception e) {
			ConvertUtil.setResponseByAjax(response, "Failure");
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
	}
	/**
	 * 充值画面初始化
	 * */
	public String getConsumption() throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("brandCode", userInfo.getBrandCode());
			map.put("memberCode", form.getMemCode());
			map.put("counterCode", counterInfo.getCounterCode());
			//后期调用储值卡信息查询接口
			if(form.getCardCode()!=null && !"".equals(form.getCardCode().toString())){
				map.put("cardCode", form.getCardCode());
				List<Map<String,Object>> cardInfoList=binOLWPSAL13_BL.getCard(map);
				if(cardInfoList!=null && cardInfoList.size()==1){
					consumptionCodeMap = cardInfoList.get(0);
				}
			}
			// 储值卡/服务卡 活动查询
			form.setRechargeDiscountList(binOLWPSAL13_BL.getRechargeDiscountList(map));
			// 服务卡类型
			form.setServerList(code.getCodesByGrade("1338"));
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
		}
	}
	/**
	 * 打开开卡界面
	 * */
//	public String openCard(){
//		form.setServerList(code.getCodesByGrade("1338"));
//		form.getMemCode();
//		form.getMobilePhone();
//		return SUCCESS;
//	}
	
	/**
	 * 充值方法
	 * @throws Exception 
	 * */
	public void recharge() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String brandCode = userInfo.getBrandCode();
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, brandCode);
		}
		String businessType = form.getBusinessType();
		List<Map<String, Object>> jsonList = new ArrayList<Map<String,Object>>();
		if(null!=businessType && !"".equals(businessType)){
			double GiveAmount = form.getGiveAmount();
			map.put("GiveAmount", GiveAmount);
			if(!businessType.equals("1")){
				String json = form.getServiceJsonList();
				if(json!=null && !"".equals(json)){
				jsonList = ConvertUtil.json2List(json);
			}
			map.put("ServiceDetail", jsonList);
			}
		}
		String CardCode = form.getCardCode();
		int RechargeValue = form.getAmount();
		map.put("RechargeValue", RechargeValue);
		String EmployeeCode = userInfo.getEmployeeCode();
		String CounterCode = counterInfo.getCounterCode();
		String Memo = form.getMemo();
		map.put("CardCode", CardCode);
		map.put("BusinessType", businessType);
		map.put("Memo", Memo);
		map.put("EmployeeCode", EmployeeCode);
		map.put("CounterCode", CounterCode);
		if(null==CardCode && "".equals(CardCode)){
			ConvertUtil.setResponseByAjax(response, "NC");
		}else if(null==businessType && "".equals(businessType)){
			ConvertUtil.setResponseByAjax(response, "NT");
		}else if(RechargeValue <= 0 && "1".equals(businessType)){
			ConvertUtil.setResponseByAjax(response, "NA");
		}else if(!"1".equals(businessType) && RechargeValue <= 0 && jsonList.isEmpty()){
			ConvertUtil.setResponseByAjax(response, "NAL");
		}else {
			Map<String, Object> rmap = binOLWPSAL13_BL.savingsCardRecharge(map);
			String ERRORCODE = rmap.get("ERRORCODE").toString();
			if("0".equals(ERRORCODE)){
				logger.error("充值成功！ERRORCODE值为"+ERRORCODE);
				String j = rmap.get("ResultContent").toString();
				WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
				String ResultContent = CherryAESCoder.decrypt(j, wsconfigDTO.getSecretKey());
				Map<String, Object> m = ConvertUtil.json2Map(ResultContent);
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}else if("SRE0013".equals(ERRORCODE)){
				ConvertUtil.setResponseByAjax(response, rmap.get("ERRORMSG").toString());
			}else {
				logger.error("充值失败！ERRORCODE值为"+ERRORCODE);
				ConvertUtil.setResponseByAjax(response, ERRORCODE);
			}
		}
	}
	/**
	 * 开卡
	 * @throws Exception 
	 * */
	public void createCard() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		// 根据手机查询会员信息
		map.put("memCode", form.getMemCode());
		if(form.getMemCode() == null || "".equals(form.getMemCode().toString())){
			map.put("brandCode", userInfo.getBrandCode());
			map.put("searchStr", form.getMobilePhone());
			Map<String, Object> resultMap = binOLWPSAL02_IF.getMemberInfo(map);
			if(resultMap!=null && !resultMap.isEmpty()){
				if("0".equals(resultMap.get("errorCode"))){
					Map<String, Object> m = (Map<String, Object>) resultMap.get("memberInfo");
					map.put("memCode", m.get("memberCode"));
				}
			}
		}
		map.put("counterCode", counterInfo.getCounterCode());
		map.put("brandCode", userInfo.getBrandCode());
		map.put("cardCode", form.getCardCode());
		map.put("businessType", form.getBusinessType());
		map.put("mobilePhone", form.getMobilePhone());
		map.put("employeeCode", userInfo.getEmployeeCode());
		map.put("password", form.getPassword());
		map.put("RechargeValue", form.getAmount());
		if(form.getAmount()==0){
			map.put("RechargeValue", "");
		}
		map.put("GiveAmount", form.getGiveAmount());
		map.put("memo", form.getMemo());
		if(form.getBusinessType()!=null && !"".equals(form.getBusinessType())){
			String businessType = form.getBusinessType().toString();
			if(!"1".equals(businessType)){
				List<Map<String, Object>> jsonList = new ArrayList<Map<String,Object>>();
				String json = form.getServiceJsonList();
				if(json!=null && !"".equals(json)){
					jsonList = ConvertUtil.json2List(json);
				}
				map.put("ServiceDetail", jsonList);
			}
		}
		Map<String, Object> rmap = binOLWPSAL13_BL.createCard(map);
		String ERRORCODE = rmap.get("ERRORCODE").toString();
		if("0".equals(ERRORCODE)){
			logger.error("开卡成功！ERRORCODE值为"+ERRORCODE);
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		}else if("SAE0020".equals(ERRORCODE)){
			ConvertUtil.setResponseByAjax(response, "EXISTE");
		}else if("SAE0011".equals(ERRORCODE)){
			ConvertUtil.setResponseByAjax(response, "SAE0011");
		}else if("SAE0013".equals(ERRORCODE)){
			ConvertUtil.setResponseByAjax(response, "SAE0013");
		}else if("SAE0015".equals(ERRORCODE)){
			ConvertUtil.setResponseByAjax(response, "SAE0015");
		}else if("SAE0015".equals(ERRORCODE)){
			ConvertUtil.setResponseByAjax(response, "SAE0015");
		}else if("SAE0001".equals(ERRORCODE)){
			ConvertUtil.setResponseByAjax(response, "SAE0001");
		}else {
			logger.error("开卡失败！ERRORCODE值为"+ERRORCODE);
			ConvertUtil.setResponseByAjax(response, ERRORCODE);
		}
	}
	
	// 获取储值卡信息
	public String getConsumptionCode() throws Exception{
		Map<String,Object> data=new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		data.put("TradeType", "GetSavingsCardInfo");
		data.put("MemberCode", form.getMemCode());
		//云POS默认会员卡号为储值卡卡号
		data.put("CardCode", form.getMemCode());
		CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		data.put("CounterCode", counterInfo.getCounterCode());
		Map<String,Object> m = binOLWPSAL13_BL.getMemberIdByCode(data);
		if(m!=null && !m.isEmpty()){
			m.put("memberCode", form.getMemCode());
		}
		String brandCode = ConvertUtil.getString(userInfo.getBrandCode());
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", wsconfigDTO.getAppID());
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), wsconfigDTO.getSecretKey()));
		WebResource wr= binOLCM27_BL.getWebResource(wsconfigDTO.getWebserviceURL());
		String result_card= "";
		try {
			result_card=wr.queryParams(queryParams).get(String.class);
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
			ConvertUtil.setResponseByAjax(response, "ERROR");
			return null;
		}
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String ERRORCODE = result_card1.get("ERRORCODE").toString();
		if(ERRORCODE.equals("0")){
			String ResultContent = result_card1.get("ResultContent").toString();
			String s = CherryAESCoder.decrypt(ResultContent, wsconfigDTO.getSecretKey());
			if(null!=s && !"".equals(s) && !"null".equals(s)){
				form.setServerList(code.getCodesByGrade("1338"));
				List<Map<String, Object>> r_List = ConvertUtil.json2List(s);
				if(r_List.size()==0){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					map.put("cardCode", ConvertUtil.getString(form.getMemCode()));
					Integer cardId = binOLWPSAL13_BL.getCardId(map);
					if(null!=cardId){
						ConvertUtil.setResponseByAjax(response, "SUCCESS");
					}else {
						ConvertUtil.setResponseByAjax(response, "NOEXIST");
					}
					return null;
				}
				form.setCardList(r_List);
			}
		}else {
			// 记录日志
			logger.info("优惠信息查询WebService调用结果："+ ConvertUtil.getString(result_card1.get("ERRORMSG")));
			ConvertUtil.setResponseByAjax(response, "Failure");
			return null;
		}
		return SUCCESS;
	}
	/**
	 * 储值卡交易明细初始化
	 * @throws Exception 
	 * */
	public String transactionDetailInit() throws Exception{
		// 获取当前日期
		nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
		return SUCCESS;
	}
	/**
	 * 储值卡交易明细
	 * @throws Exception 
	 * */
	public String transactionDetail() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandCode", ConvertUtil.getString(userInfo.getBrandCode()));
		map.put("cardCode", ConvertUtil.getString(form.getCardCode()));
		map.put("billCode", ConvertUtil.getString(form.getBillCode()));
		map.put("fromDate", ConvertUtil.getString(form.getFromDate()));
		map.put("toDate", ConvertUtil.getString(form.getToDate()));
		map.put("brandInfoId", brandInfoId);
		map.put("organizationInfoId", organizationInfoId);
		// 查询数据的条数
		ConvertUtil.setForm(form, map);
		Map<String, Object> countInfo = binOLMBSVC02_IF.getSaleDetailCountInfo(map);
		int count = 0;
		if (countInfo != null) {
			count = (Integer) countInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			this.setTransactionDetailList(binOLMBSVC02_IF.getSaleByCardCode(map));
		}
		/*Map<String, Object> result_map = binOLWPSAL13_BL.transactionDetail(map);
		String ERRORCODE = result_map.get("ERRORCODE").toString();
		if("0".equals(ERRORCODE)){
			String ResultContent = result_map.get("ResultContent").toString();
			String s = CherryAESCoder.decrypt(ResultContent, SavingscardAESKEY);
			if(null!=s && !"".equals(s)){
				List<Map<String, Object>> r_List = ConvertUtil.json2List(s);
				if(r_List==null || r_List.size()==0){
					ConvertUtil.setResponseByAjax(response, "NOEXIST");
					return null;
				}else {
					ConvertUtil.setForm(form, map);
					this.setTransactionDetailList(r_List);
					form.setITotalDisplayRecords(transactionDetailList.size());
					form.setITotalRecords(transactionDetailList.size());
				}
			}
		}*/
		return SUCCESS;
	}
	/**
	 * 撤销初始化
	 * */
	public String revokeInit(){
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", brandInfoId);
		map.put("organizationInfoId", organizationInfoId);
		map.put("brandCode", ConvertUtil.getString(userInfo.getBrandCode()));
		map.put("cardCode", ConvertUtil.getString(form.getCardCode()));
		map.put("billCode", ConvertUtil.getString(form.getBillCode()));
		this.setTransactionDetailMap(binOLMBSVC02_IF.getSaleByBillCode(map));
		List<Map<String, Object>> vlist = code.getCodesByGrade("1349");
		if(vlist!=null && !vlist.isEmpty()){
			for(Map<String, Object> m : vlist){
				String key = m.get("CodeKey").toString();
				if("1".equals(key) || "2".equals(key)){
					this.getVerificationTypeList().add(m);
				}
			}
		}
		return SUCCESS;
	}
	/**
	 * 撤销
	 * */
	public void revoke() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String EmployeeCode = userInfo.getEmployeeCode();
		String CounterCode = counterInfo.getCounterCode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeCode", EmployeeCode);
		map.put("counterCode", CounterCode);
		map.put("brandInfoId", brandInfoId);
		map.put("organizationInfoId", organizationInfoId);
		map.put("brandCode", ConvertUtil.getString(userInfo.getBrandCode()));
		map.put("cardCode", ConvertUtil.getString(form.getCardCode()));
		map.put("billCode", ConvertUtil.getString(form.getBillCode()));
		map.put("verificationType", ConvertUtil.getString(form.getVerificationType()));
		if(form.getVerificationType()!=null){
			String v = form.getVerificationType().toString();
			if("1".equals(v)){
				map.put("password", form.getVerificationCode());
			}else if ("2".equals(v)) {
				map.put("verificationCode", form.getVerificationCode());
			}
		}
		Map<String, Object> rmap = binOLWPSAL13_BL.revoke(map);
		String ERRORCODE = rmap.get("ERRORCODE").toString();
		if("0".equals(ERRORCODE)){
			logger.error("撤销成功！ERRORCODE值为"+ERRORCODE);
			/*String j = rmap.get("ResultContent").toString();
			String ResultContent = CherryAESCoder.decrypt(j, SavingscardAESKEY);
			Map<String, Object> m = ConvertUtil.json2Map(ResultContent);*/
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		}else {
			logger.error("撤销失败！ERRORCODE值为"+ERRORCODE);
			ConvertUtil.setResponseByAjax(response, ERRORCODE);
		}
	}
	
	public void relation() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		map.put("cardCode", ConvertUtil.getString(form.getMemCode()));
		map.put("MemberCode", ConvertUtil.getString(form.getMemCode()));
		Integer cardId = binOLWPSAL13_BL.getCardId(map);
		if(null!=cardId){
			Map<String, Object> resultMap = binOLWPSAL13_BL.getMemberIdByCode(map);
			if(resultMap != null && !resultMap.isEmpty()){
				map.put("mobilePhone", ConvertUtil.getString(form.getMobilePhone()));
				map.put("memberInfoId",ConvertUtil.getString(resultMap.get("memberInfoId")));
				map.put("cardId", cardId);
				map.put("updatedBy", "BINOLWPSAL13");
				map.put("updatePGM", "BINOLWPSAL13");
				// 关联方法
				try {
					binOLWPSAL13_BL.relation(map);
					ConvertUtil.setResponseByAjax(response, "SUCCESS");
				} catch (Exception e) {
					logger.info("储值卡与会员卡关联失败，参数："+map);
					logger.info(e.getMessage(),e);
				}
			}else{
					ConvertUtil.setResponseByAjax(response, "NOMEMBER");
			}
		}else {
			ConvertUtil.setResponseByAjax(response, "NOCARDID");
		}
	}
//	public String getServerList() throws Exception{
//		Map<String,Object> map=new HashMap<String, Object>();
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		map.put("brandCode", ConvertUtil.getString(userInfo.getBrandCode()));
//		map.put("cardCode", form.getCardCode());
//		List<Map<String,Object>> list = binOLWPSAL13_BL.getCard(map);
//		if(list!=null){
//			if(list.size()==1){
//				consumptionCodeMap = list.get(0);
//				String cardType = consumptionCodeMap.get("CardType").toString();
//				if("1".equals(cardType)){
//					ConvertUtil.setResponseByAjax(response, cardType);
//					return null;
//				}
//				// 服务卡类型
//				form.setServerList(code.getCodesByGrade("1338"));
//			}
//		}else {
//			ConvertUtil.setResponseByAjax(response, "NOEXIST");
//			return null;
//		}
//		return SUCCESS;
//	}
	// 输入CardCode界面
//	public String getCardCode(){
//		return SUCCESS;
//	}
	
	@Override
	public BINOLWPSAL13_Form getModel() {
		return form;
	}
	public Map<String, Object> getConsumptionCodeMap() {
		return consumptionCodeMap;
	}
	public void setConsumptionCodeMap(Map<String, Object> consumptionCodeMap) {
		this.consumptionCodeMap = consumptionCodeMap;
	}

	public List<Map<String, Object>> getTransactionDetailList() {
		return transactionDetailList;
	}

	public void setTransactionDetailList(
			List<Map<String, Object>> transactionDetailList) {
		this.transactionDetailList = transactionDetailList;
	}

	public Map<String, Object> getTransactionDetailMap() {
		return transactionDetailMap;
	}

	public void setTransactionDetailMap(Map<String, Object> transactionDetailMap) {
		this.transactionDetailMap = transactionDetailMap;
	}

	public List<Map<String, Object>> getVerificationTypeList() {
		return verificationTypeList;
	}

	public void setVerificationTypeList(
			List<Map<String, Object>> verificationTypeList) {
		this.verificationTypeList = verificationTypeList;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	
}
