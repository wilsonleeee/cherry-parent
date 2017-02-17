package com.cherry.wp.sal.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.sal.form.BINOLWPSAL02_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL02_IF;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL02_Action extends BaseAction implements ModelDriven<BINOLWPSAL02_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLWPSAL02_Form form = new BINOLWPSAL02_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL02_Action.class);
	
	@Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLWPCM01_BL")
	private BINOLWPCM01_IF binOLWPCM01_IF;
	
	@Resource(name="binOLWPSAL02_BL")
	private BINOLWPSAL02_IF binOLWPSAL02_IF;
	
	private String businessBeginDate;
	
	private String nowDate;

	private String birthFormat;

	public String getBirthFormat() {
		return birthFormat;
	}

	public void setBirthFormat(String birthFormat) {
		this.birthFormat = birthFormat;
	}

	public String getBusinessBeginDate() {
		return businessBeginDate;
	}

	public void setBusinessBeginDate(String businessBeginDate) {
		this.businessBeginDate = businessBeginDate;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String init(){
		try{
			// 页面初始状态设置
			initPageSet();
		} catch(Exception e){
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
	
	public String initial(){
		try{
			// 页面初始状态设置
			initPageSet();
		} catch(Exception e){
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
	
	@SuppressWarnings("unchecked")
	public void search() throws Exception{
		try{
			String memberInfoId = ConvertUtil.getString(form.getMemberInfoId());
			String searchStr = ConvertUtil.getString(form.getSearchStr());
			if(!"".equals(memberInfoId) || !"".equals(searchStr)){
				// 用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());

				map.put("memberInfoId", memberInfoId);
				map.put("searchStr", searchStr);
				Map<String, Object> resultMap = binOLWPSAL02_IF.getMemberInfo(map);
				// 预约活动领取柜台
				List<Map<String, Object>> orderCounterCode = binOLWPSAL02_IF.getOrderCounterCode(map);
				if(resultMap != null && !resultMap.isEmpty()){
					String resultStatus = ConvertUtil.getString(resultMap.get("returnStatus"));
					String errorCode = ConvertUtil.getString(resultMap.get("errorCode"));
					if(resultStatus.equals(CherryConstants.WP_SUCCESS_STATUS)){
						Map<String, Object> memberInfo = (Map<String, Object>)resultMap.get("memberInfo");
						if(orderCounterCode != null && !orderCounterCode.isEmpty()){
							memberInfo.put("orderCounterCode", orderCounterCode);
						}else {
							memberInfo.put("orderCounterCode", "");
						}
						if(memberInfo != null && !memberInfo.isEmpty()){

							ConvertUtil.setResponseByAjax(response, memberInfo);
						}else{
							// 返回会员信息出现为空的情况
							ConvertUtil.setResponseByAjax(response, "");
						}
					}else{
						// 获取会员信息失败时判断是否为获取到多个会员的情况
						if(errorCode.equals(CherryConstants.WP_ERROR_GETMULTIPLEMEMBER)){
							// 若获取到的会员信息为多条时
							ConvertUtil.setResponseByAjax(response, "MULTIPLE");
						}else{
							ConvertUtil.setResponseByAjax(response, "");
						}
					}
				}else{
					// 返回结果出现为空的异常情况
					ConvertUtil.setResponseByAjax(response, "");
				}
			}else{
				// 返回空的情况
				ConvertUtil.setResponseByAjax(response, "");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "");
			 }
		}
	}
	public void searchCounterLimit() throws Exception{
		try{

			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());

			Map<String,Object> retMap = new HashMap<String,Object>();
			retMap.put("CurrentPoint","234");
			retMap.put("PlanStatus","1");
			retMap.put("MinWarningPoint","3000");

			// 返回空的情况
			ConvertUtil.setResponseByAjax(response, CherryUtil.map2Json(retMap));

		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "");
			 }
		}
	}

	public void returnsGoods() throws Exception{
		try{
			String billCode = "";
			String saleType = CherryConstants.WP_SALETYPE_SR;
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
					
			String nowSaleType = ConvertUtil.getString(form.getSaleType());
			if("SR".equals(nowSaleType)){
				// 生成销售单据号
				billCode = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID(), 
						ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.WP_BILLPREFIX_WN);
				saleType = CherryConstants.WP_SALETYPE_NS;
			}else{
				// 生成退货单据号
				billCode = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID(), 
						ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.WP_BILLPREFIX_WR);
				saleType = CherryConstants.WP_SALETYPE_SR;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("billCode", billCode);
			map.put("saleType", saleType);
			ConvertUtil.setResponseByAjax(response, map);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "");
			 }
		}
	}
	
	// 页面初始状态设置
	private boolean initPageSet() throws Exception{
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String organizationID = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
		// 获取当前日期
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		parMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		parMap.put(CherryConstants.ORGANIZATIONID, organizationID);
		String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
		if("2".equals(saleDateType)){
			nowDate = binOLWPCM01_IF.getBussinessDate(parMap);
		}else{
			nowDate = DateUtil.coverTime2YMD(binOLWPCM01_IF.getSYSDate(), "yyyy-MM-dd");
		}
		businessBeginDate = calcuBusinessBeginDate(parMap);
		form.setCounterCode(counterInfo.getCounterCode());

		//得到系统配置项  积分兑换活动是否限定产品
		String isLimitProduct = binOLCM14_BL.getWebposConfigValue("9055",organizationInfoId,brandInfoId);
		form.setIsLimitProduct(isLimitProduct);

		//获取当前柜台的联系电话
		String counterPhone=binOLWPSAL02_IF.getCounterPhone(parMap);
		if(null == counterPhone || "".equals(counterPhone)){
			counterPhone = "";
		}
		form.setCounterPhone(counterPhone);
		
		//获取当前柜台的地址
		String counterAddress=binOLWPSAL02_IF.getCounterAddress(parMap);
		if(null == counterAddress || "".equals(counterAddress)){
			counterAddress = "";
		}
		form.setCounterAddress(counterAddress);
		
		// 获取销售时最大输入产品行数配置，若取到空值则默认为50
		String showSaleRows = binOLCM14_BL.getWebposConfigValue("9005", organizationInfoId, brandInfoId);
		if(null == showSaleRows || "".equals(showSaleRows)){
			showSaleRows = "50";
		}
		form.setShowSaleRows(showSaleRows);
		
		// 获取自动弹出会员入会页面所需购买金额配置
		String conditionAmount = binOLCM14_BL.getWebposConfigValue("9007", organizationInfoId, brandInfoId);
		if(null == conditionAmount || "".equals(conditionAmount)){
			conditionAmount = "";
		}
		form.setConditionAmount(conditionAmount);
		
		// 获取是否对会员自动执行会员价的配置
		String useMemberPrice = binOLCM14_BL.getWebposConfigValue("9008", organizationInfoId, brandInfoId);
		if(null == useMemberPrice || "".equals(useMemberPrice)){
			useMemberPrice = "N";
		}
		form.setUseMemberPrice(useMemberPrice);
		
		// 获取首单是否享受会员价的配置
		String firstBillPrice = binOLCM14_BL.getWebposConfigValue("9009", organizationInfoId, brandInfoId);
		if(null == firstBillPrice || "".equals(firstBillPrice)){
			firstBillPrice = "N";
		}
		form.setFirstBillPrice(firstBillPrice);
		
		// 获取满指定金额入会后是否继续收款的配置
		String showCollectAfterJoin = binOLCM14_BL.getWebposConfigValue("9010", organizationInfoId, brandInfoId);
		if(null == showCollectAfterJoin || "".equals(showCollectAfterJoin)){
			showCollectAfterJoin = "N";
		}
		form.setShowCollectAfterJoin(showCollectAfterJoin);
		
		// 获取收款后是否自动打印小票的配置
		String autoPrintBill = binOLCM14_BL.getWebposConfigValue("9011", organizationInfoId, brandInfoId);
		if(null == autoPrintBill || "".equals(autoPrintBill)){
			autoPrintBill = "N";
		}
		form.setAutoPrintBill(autoPrintBill);
		
		// 获取云POS销售允许操作的最低折扣百分率配置
		String minDiscount = binOLCM14_BL.getWebposConfigValue("9013", organizationInfoId, brandInfoId);
		if(null == minDiscount || "".equals(minDiscount)){
			minDiscount = "0";
		}
		form.setMinDiscount(minDiscount);
		
		// 获取云POS销售是否合并销售产品
		String merge = binOLCM14_BL.getWebposConfigValue("9014", organizationInfoId, brandInfoId);
		if(null == merge || "".equals(merge)){
			merge = "Y";
		}
		form.setMerge(merge);
		//获取云POS是否允许负仓库操作的配置 Y是使用负仓库 N是不使用负仓库,默认情况下是允许的
		String stockType = binOLCM14_BL.getWebposConfigValue("9015", organizationInfoId, brandInfoId);
		if(null == stockType || "".equals(stockType)){
			stockType = "Y";
		}
		form.setStockType(stockType);
		//获取云POS是否允许商品打折的配置Y是允许打折N是不允许商品打折，默认情况下是允许的
		String discountType = binOLCM14_BL.getWebposConfigValue("9016", organizationInfoId, brandInfoId);
		if(null == discountType || "".equals(discountType)){
			discountType = "Y";
		}
		form.setDiscountType(discountType);
		//获取云POS是否允许高于原价销售 Y是 N否,默认情况下是不允许的
		String highPriceSal = binOLCM14_BL.getWebposConfigValue("9017", organizationInfoId, brandInfoId);
		if(null == highPriceSal || "".equals(highPriceSal)){
			highPriceSal = "N";
		}
		form.setHighPriceSal(highPriceSal);
		//获取云POS是否允许智能促销 Y是 N否,默认情况下是不允许
		String smartPromotion = binOLCM14_BL.getWebposConfigValue("9018", organizationInfoId, brandInfoId);
		if(null == smartPromotion || "".equals(smartPromotion)){
			smartPromotion = "N";
		}
		form.setSmartPromotion(smartPromotion);
		//前端是否允许【F9:充值/开卡】
		String rechargeAndOpendCardButton = binOLCM14_BL.getWebposConfigValue("9024", organizationInfoId, brandInfoId);
		if(null == rechargeAndOpendCardButton || "".equals(rechargeAndOpendCardButton)){
			rechargeAndOpendCardButton = "Y";
		}
		form.setRechargeAndOpendCardButton(rechargeAndOpendCardButton);
		
		// 生成销售单据号
		String billCode = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID(), 
				ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.WP_BILLPREFIX_WN);
		// 设置单据号
		form.setBillCode(billCode);
		
		//获取云POS是否允许储值卡支付
		String newCzkPay = binOLCM14_BL.getWebposConfigValue("9022", organizationInfoId, brandInfoId);
		if(null == newCzkPay || "".equals(newCzkPay)){
			newCzkPay = "N";
		}
		form.setNewCzkPay(newCzkPay);
		//获取云POS储值卡类型
		/*String czkType = binOLCM14_BL.getWebposConfigValue("9021", organizationInfoId, brandInfoId);
		if(null == newCzkPay || "".equals(newCzkPay)){
			czkType = "N";
		}*/
		//获取云POS销售是否允许折扣率
		String isDiscountFlag = binOLCM14_BL.getWebposConfigValue("9025", organizationInfoId, brandInfoId);
		if(null == isDiscountFlag || "".equals(isDiscountFlag)){
			isDiscountFlag = "Y";
		}
		form.setIsDiscountFlag(isDiscountFlag);
		
		String isPlatinumPrice = binOLCM14_BL.getWebposConfigValue("9026", organizationInfoId, brandInfoId);
		if(null == isPlatinumPrice || "".equals(isPlatinumPrice)){
			isPlatinumPrice = "N";
		}
		form.setIsPlatinumPrice(isPlatinumPrice);
		
		String baChooseModel = binOLCM14_BL.getWebposConfigValue("9028", organizationInfoId, brandInfoId);
		if(null == baChooseModel || "".equals(baChooseModel)){
			baChooseModel = "1";
		}
		form.setBaChooseModel(baChooseModel);
		//云POS是否允许去掉现金支付
		String isCA = binOLCM14_BL.getWebposConfigValue("9030", organizationInfoId, brandInfoId);
		if(null == isCA || "".equals(isCA)){
			isCA = "N";
		}
		form.setIsCA(isCA);
		//云POS是否允许不入会销售
		String isMemberSaleFlag=binOLCM14_BL.getWebposConfigValue("9031", organizationInfoId, brandInfoId);
		if(null == isMemberSaleFlag || "".equals(isMemberSaleFlag)){
			isMemberSaleFlag = "Y";
		}
		form.setIsMemberSaleFlag(isMemberSaleFlag);

		//是否启用柜台积分计划配置项
		String isExecuteLimitPlan = binOLCM14_BL.getConfigValue("1396", organizationInfoId, brandInfoId);
		if (ConvertUtil.isBlank(isExecuteLimitPlan)){
			isExecuteLimitPlan = "0";
		}
		form.setIsExecuteLimitPlan(isExecuteLimitPlan);

		//云POS直接打印模版
		String printBrandType=binOLCM14_BL.getWebposConfigValue("9035", organizationInfoId, brandInfoId);
		if(null == printBrandType || "".equals(printBrandType)){
			printBrandType = "NZDM";
		}
		form.setPrintBrandType(printBrandType);
		
		//云POS直接打印模版
		String stockSaleType=binOLCM14_BL.getWebposConfigValue("9036", organizationInfoId, brandInfoId);
		if(null == stockSaleType || "".equals(stockSaleType)){
			stockSaleType = "N";
		}
		form.setStockSaleType(stockSaleType);	
			
		//云POS会员入会生日必填/选填
		String birthFlag=binOLCM14_BL.getWebposConfigValue("9041", organizationInfoId, brandInfoId);
		if(null == birthFlag || "".equals(birthFlag)){
			birthFlag = "Y";
		}
		form.setBirthFlag(birthFlag);
		
		//云POS是否忽略领用中的购买门槛
		String isBuyFlag=binOLCM14_BL.getWebposConfigValue("9043", organizationInfoId, brandInfoId);
		if(null == isBuyFlag || "".equals(isBuyFlag)){
			isBuyFlag = "N";
		}
		form.setIsBuyFlag(isBuyFlag);
		
		//会员卡号校验规则
		String memCodeRule=binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandInfoId);
		form.setMemCodeRule(memCodeRule);

		// 获取柜台号
		String counterCode = ConvertUtil.getString(counterInfo.getCounterCode());

		if("1".equals(isExecuteLimitPlan)){
			//如果是否启用柜台积分计划配置项为"1"（是）,则获取柜台的积分额度信息
			Map<String,Object> pekonParamMap = new HashMap<String,Object>();
			WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO("pekonws");
			String url = wsconfigDTO.getWebserviceURL();
			String appID = wsconfigDTO.getAppID();
			String tradeType = "GetCounterCurrentPoint";
			String brandCode = userInfo.getBrandCode();//"AVENE";//

			pekonParamMap.put("appID",appID);
			pekonParamMap.put("TradeType",tradeType);
			pekonParamMap.put("brandCode",brandCode);
			pekonParamMap.put("CounterCode",counterCode);

			Map<String,Object> retMap = WebserviceClient.accessPekonWebService(pekonParamMap,url);
			// 获取会员信息成功的情况
			if( retMap != null && retMap.get("ResultContent") != null){
				//CurrentPoint,PlanStatus,MinWarningPoint
				form.setPointLimitInfo((Map<String,Object>)retMap.get("ResultContent"));

			}
		}

		// 获取柜台名称
		String counterName = ConvertUtil.getString(counterInfo.getCounterName());
		if(null != counterCode && !"".equals(counterCode)){
			form.setCounterCode(counterCode);
			form.setCounterName(counterName);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 柜台部门ID
			paramMap.put("organizationId", counterInfo.getOrganizationId());
			// 获取BA列表,根据配置项来取用是考勤的员工还是忽略考勤的员工
			String attendanceFlag=binOLCM14_BL.getWebposConfigValue("9044", organizationInfoId, brandInfoId);
			if(null == attendanceFlag || "".equals(attendanceFlag)){
				attendanceFlag = "N";
			}
			List<Map<String, Object>> baList= null;
			if("N".equals(attendanceFlag)){
				baList = binOLWPCM01_IF.getBAInfoList(paramMap);
			}else{
				baList = binOLWPCM01_IF.getActiveBAList(paramMap);
			}
			form.setBaList(baList);
			return true;
		}else{
			return false;
		}
	}
	
	private String calcuBusinessBeginDate(Map<String, Object> map){
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String resultValue = "";
		// 获取补登日期范围配置
		String businessDateType = binOLCM14_BL.getWebposConfigValue("9001", organizationInfoId, brandInfoId);
		String monthValue = binOLCM14_BL.getWebposConfigValue("9002", organizationInfoId, brandInfoId);
		String dayValue = binOLCM14_BL.getWebposConfigValue("9003", organizationInfoId, brandInfoId);
		// 计算补登允许的开始日期
		if("".equals(businessDateType)){
			resultValue = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -1);
		}else{
			if("MONTH".equals(businessDateType)){
				resultValue = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -CherryUtil.obj2int(monthValue));
			}else if("DAY".equals(businessDateType)){
				resultValue = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, nowDate, -CherryUtil.obj2int(dayValue));
			}else{
				resultValue = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -1);
			}
		}
		return resultValue;
	}
	
	@Override
	public BINOLWPSAL02_Form getModel() {
		return form;
	}
}
