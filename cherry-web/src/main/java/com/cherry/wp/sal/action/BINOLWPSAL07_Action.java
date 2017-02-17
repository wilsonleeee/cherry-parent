package com.cherry.wp.sal.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.pay.interfaces.AlipayIf;
import com.cherry.cm.pay.interfaces.WeChatPayIf;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.sal.form.BINOLWPSAL07_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL03_IF;
import com.cherry.wp.sal.interfaces.BINOLWPSAL07_IF;
import com.cherry.wp.sal.service.BINOLWPSAL07_Service;
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

public class BINOLWPSAL07_Action extends BaseAction implements ModelDriven<BINOLWPSAL07_Form> {
	/**
	 * 
	 */
	static{
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO("pekonws");
		SavingscardWebServiceUrl = wsconfigDTO.getWebserviceURL();//PropertiesUtil.pps.getProperty("SavingscardWebServiceUrl");
		SavingscardAppID = wsconfigDTO.getAppID();//PropertiesUtil.pps.getProperty("SavingscardAppID");
	}
	@Resource
	private CodeTable code;
	
	private static String SavingscardWebServiceUrl;
	private static String SavingscardAppID;
	private static final long serialVersionUID = 1L;
	private List<Map<String, Object>> serverList;
	// 储值卡退货信息List
	private List<Map<String, Object>> savingsList;
	private BINOLWPSAL07_Form form = new BINOLWPSAL07_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL06_Action.class);
	
	@Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLWPCM01_BL")
	private BINOLWPCM01_IF binOLWPCM01_IF;
	
	@Resource(name="binOLWPSAL03_BL")
	private BINOLWPSAL03_IF binOLWPSAL03_IF;
	
	@Resource(name="binOLWPSAL07_BL")
	private BINOLWPSAL07_IF binOLWPSAL07_IF;
	
	/**查询调用Penkonws接口的密钥信息*/
	@Resource(name="thirdPartyConfig")
	private ThirdPartyConfig thirdPartyConfig;
	
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	@Resource(name="binOLWPSAL07_Service")
	private BINOLWPSAL07_Service binOLWPSAL07_Service;
	
	@Resource(name = "aliPayBL")
	private AlipayIf aliPayIF;
	
	@Resource(name = "weChatPayBL")
	private WeChatPayIf weChatPayIF;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private String nowDate;
	/** 云POS是否允许对未上传数据退货 **/
	private String POSTuihuo;
	/** 云POS是否使用白金价 **/
	private String isPlatinumPrice;
	/**支付方式List*/
	private List<Map<String, Object>> paymentTypeList;
	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getPOSTuihuo() {
		return POSTuihuo;
	}

	public void setPOSTuihuo(String pOSTuihuo) {
		POSTuihuo = pOSTuihuo;
	}

	public List<Map<String, Object>> getServerList() {
		return serverList;
	}

	public void setServerList(List<Map<String, Object>> serverList) {
		this.serverList = serverList;
	}

	public List<Map<String, Object>> getSavingsList() {
		return savingsList;
	}

	public void setSavingsList(List<Map<String, Object>> savingsList) {
		this.savingsList = savingsList;
	}

	public List<Map<String, Object>> getPaymentTypeList() {
		return paymentTypeList;
	}

	public void setPaymentTypeList(List<Map<String, Object>> paymentTypeList) {
		this.paymentTypeList = paymentTypeList;
	}

	public String init(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			// 获取当前日期
			nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
			
			// 获取BA列表
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 柜台部门ID
			paramMap.put("organizationId", counterInfo.getOrganizationId());
			// 获取BA列表
			List<Map<String, Object>> baList = binOLWPCM01_IF.getBAInfoList(paramMap);
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			// 云POS是否允许对未上传数据退货
			POSTuihuo = binOLCM14_BL.getWebposConfigValue("9019", organizationInfoId, brandInfoId);
			// 云POS是否使用白金价
			isPlatinumPrice = binOLCM14_BL.getWebposConfigValue("9026", organizationInfoId, brandInfoId);
			if(null == isPlatinumPrice || "".equals(isPlatinumPrice)){
				isPlatinumPrice = "N";
			}
			form.setBaList(baList);
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
	
	public String search(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 登陆用户
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 销售日期（起始）
			if(!CherryChecker.isNullOrEmpty(form.getDgSaleDateStart(), true)){
				map.put("saleDateStart", form.getDgSaleDateStart());
			}
			// 销售日期（截止）
			if(!CherryChecker.isNullOrEmpty(form.getDgSaleDateEnd(), true)){
				map.put("saleDateEnd", form.getDgSaleDateEnd());
			}
			// 单据号
			map.put("billCode", form.getDgBillCode());
			// 销售BA
			map.put("baCode", form.getDgBaCode());
			// 会员
			map.put("memberSearchStr", form.getMemberSearchStr());
			// 云POS是否使用白金价
			isPlatinumPrice = binOLCM14_BL.getWebposConfigValue("9026", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			if(null == isPlatinumPrice || "".equals(isPlatinumPrice)){
				isPlatinumPrice = "N";
			}
			//取得数量
			int count = binOLWPSAL07_IF.getFinishedBillsCount(map);
			if(count > 0){
				List<Map<String, Object>> billList = binOLWPSAL07_IF.getFinishedBillList(map);
				for(Map<String, Object> m : billList){
					if(null!=m.get("billCode")){
						map.put("billCode", m.get("billCode"));
						List<Map<String, Object>> serviceResultList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
						if(null!=serviceResultList && !serviceResultList.isEmpty()){
							Integer ServiceQuantity = 0;
							for(Map<String, Object> sm : serviceResultList){
								ServiceQuantity+=Integer.valueOf(sm.get("ServiceQuantity").toString());
							}
							Integer quantity = Integer.valueOf(m.get("quantity").toString());
							m.put("quantity", ServiceQuantity+quantity);
						}
					}
				}
				// 取得List
				form.setBillList(billList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
	
	public String returnHistory(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 登陆用户
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 销售日期（起始）
			if(!CherryChecker.isNullOrEmpty(form.getDgSaleDateStart(), true)){
				map.put("saleDateStart", form.getDgSaleDateStart());
			}
			// 销售日期（截止）
			if(!CherryChecker.isNullOrEmpty(form.getDgSaleDateEnd(), true)){
				map.put("saleDateEnd", DateUtil.addDateByDays("yyyy-MM-dd", form.getDgSaleDateEnd(), 1));
			}
			// 单据号
			map.put("billCode", form.getDgBillCode());
			// 销售BA
			map.put("baCode", form.getDgBaCode());
			// 会员
			map.put("memberSearchStr", form.getMemberSearchStr());
			
			//取得数量
			int count = binOLWPSAL07_IF.getReturnHistoryBillCount(map);
			if(count > 0){
				List<Map<String, Object>> billList = binOLWPSAL07_IF.getReturnHistoryBillList(map);
				// 取得List
				form.setBillList(billList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
	
	public String getAllBills() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			// 所属组织
//			paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//			// 所属品牌
//			paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//			// 登陆用户
//			paramMap.put(CherryConstants.USERID, userInfo.getBIN_UserID());
//			// 需要下载数据的柜台
//			paramMap.put("counterCode", counterInfo.getCounterCode());
//			// 下载其它终端设备数据
//			boolean getFlag = binOLWPSAL07_IF.tran_getAllBillsToWebPos(paramMap);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 登陆用户
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 销售日期（起始）
			if(!CherryChecker.isNullOrEmpty(form.getDgSaleDateStart(), true)){
				map.put("saleDateStart", form.getDgSaleDateStart());
			}
			// 销售日期（截止）
			if(!CherryChecker.isNullOrEmpty(form.getDgSaleDateEnd(), true)){
				map.put("saleDateEnd", form.getDgSaleDateEnd());
			}
			// 单据号
			map.put("billCode", form.getDgBillCode());
			// 销售BA
			map.put("baCode", form.getDgBaCode());
			// 会员
			map.put("memberSearchStr", form.getMemberSearchStr());
			
			//取得数量
			int count = binOLWPSAL07_IF.getFinishedBillsCount(map);
			if(count > 0){
				List<Map<String, Object>> billList = binOLWPSAL07_IF.getFinishedBillList(map);
				// 取得List
				form.setBillList(billList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
	//退货
	public String getBillDetail() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 单据号
			map.put("billDetailShowType", form.getBillDetailShowType());
			// 单据号
			map.put("billCode", form.getBillCode());
			//柜台号
			map.put("counterCode", userInfo.getDepartCode());
			// 获取单据明细
			// 获取单据明细第一步：获取云POS销售是否合并销售产品
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 云POS是否使用白金价
			isPlatinumPrice = binOLCM14_BL.getWebposConfigValue("9026", organizationInfoId, brandInfoId);
			if(null == isPlatinumPrice || "".equals(isPlatinumPrice)){
				isPlatinumPrice = "N";
			}
			String merge = binOLCM14_BL.getWebposConfigValue("9014", organizationInfoId, brandInfoId);
			if(null == merge || "".equals(merge)){
				merge = "Y";
			}
			map.put("merge",merge);
			//第二步：获取单据明细
			List<Map<String, Object>> resultList = binOLWPSAL07_IF.tran_getBillDetailByCode(map);
/*			List<Map<String, Object>> serviceResultList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
			List<Map<String, Object>> serverList_CodeKey = new ArrayList<Map<String,Object>>();
			if(serviceResultList.size()>0){
				// 服务类型
				serverList_CodeKey = code.getCodesByGrade("1338");
			}
			if(null!=serviceResultList && !serviceResultList.isEmpty()){
				for(Map<String, Object> m : serviceResultList){
					Map<String, Object> smap = new HashMap<String, Object>();
					smap.put("relevantCode", m.get("relevantCode"));
					smap.put("saleType", m.get("saleType"));
					smap.put("serviceType", m.get("ServiceType"));
					smap.put("quantity", m.get("ServiceQuantity"));
					if(null!=serverList_CodeKey && !serverList_CodeKey.isEmpty()){
						for(Map<String, Object> cm : serverList_CodeKey){
							if(null!=m.get("ServiceType") && null!=cm.get("CodeKey") && m.get("ServiceType").toString().equals(cm.get("CodeKey"))){
								smap.put("productName", "(服务类型)  "+cm.get("Value").toString());
							}
						}
					}
					resultList.add(smap);
				}
			}*/
			// 显示单据明细
			form.setBillDetailList(resultList);

			String isPermitMemPointNegative = binOLCM14_BL.getWebposConfigValue("9052", organizationInfoId, brandInfoId);
			form.setIsPermitMemPointNegative(isPermitMemPointNegative);
			if("N".equals(isPermitMemPointNegative)){

				//获取会员当前总积分和对应销售所得积分，用于计算退货时积分是否足够
				Map<String,Object> saleMemPointInfoMap = binOLWPSAL07_IF.getSaleMemPointInfo(map);
				if(saleMemPointInfoMap != null && saleMemPointInfoMap.size() > 0){
					//会员当前总积分
					String totalPoint = ConvertUtil.getString(saleMemPointInfoMap.get("TotalPoint"));
					form.setTotalPoint(totalPoint);
					//对应销售所得积分
					String pointGet = ConvertUtil.getString(saleMemPointInfoMap.get("PointGet"));
					form.setPointGet(pointGet);
				}
			}

			//获取该单据的主信息   允许补登对货时间为（销售时间~当前系统时间）
			Map<String, Object> billMap = binOLWPSAL07_IF.getFinishedBillMap(map);
			if(billMap != null){
				String returnbussinessDateStart=ConvertUtil.getString(billMap.get("businessDate"));
				form.setReturnbussinessDateStart(returnbussinessDateStart);
				String sysTime=CherryUtil.getSysDateTime("yyyy-MM-dd");
				form.setReturnbussinessDateEnd(sysTime);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
		return SUCCESS;
	}
	
	public String getSrBillDetail() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 单据号
			map.put("billCode", form.getBillCode());
			// 获取单据明细
			List<Map<String, Object>> resultList = binOLWPSAL07_IF.tran_getSrBillDetailByCode(map);
			// 显示单据明细
			form.setBillDetailList(resultList);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 云POS是否使用白金价
			isPlatinumPrice = binOLCM14_BL.getWebposConfigValue("9026", organizationInfoId, brandInfoId);
			if(null == isPlatinumPrice || "".equals(isPlatinumPrice)){
				isPlatinumPrice = "N";
			}
						
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
		return SUCCESS;
	}
	
	public void returnBill() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			// 生成退货单据号
			String srBillCode = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), 
					ConvertUtil.getString(userInfo.getBIN_BrandInfoID()), ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.WP_BILLPREFIX_WR);
			if(!srBillCode.contains(CherryConstants.WP_BILLPREFIX_WR)){
				ConvertUtil.setResponseByAjax(response, "ERROR");
				return;
			}

			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 云POS是否支持新储值卡支付
			String NEW_CZK_PAY = binOLCM14_BL.getWebposConfigValue("9022", organizationInfoId, brandInfoId);
			String merge = binOLCM14_BL.getWebposConfigValue("9014", organizationInfoId, brandInfoId);
			String brandCode = ConvertUtil.getString(userInfo.getBrandCode());
			Map<String, Object> map = new HashMap<String, Object>();
			// 是否合并相同产品标志
			if("N".equals(merge)){
				map.put("merge", merge);
			}
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌代号
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 登陆用户
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 语言
			map.put("language", userInfo.getLanguage());
			// 员工ID
			map.put("employeeId", userInfo.getBIN_EmployeeID());
			// 获取登录IP作为机器号
			map.put("machineCode", userInfo.getLoginIP());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 退货单据号
			map.put("srBillCode", srBillCode);
			// 单据号
			map.put("billCode", form.getBillCode());
			// BA编号
			map.put("baCode", form.getDgCheckedBaCode());
			// 会员卡号
			map.put("memberCode", form.getDgCheckedMemberCode());
			// 会员等级
			map.put("memberLevel", form.getDgCheckedMemberLevel());
			// 客户类型
			map.put("customerType", form.getDgCheckedCustomerType());
			// 销售类型
			map.put("saleType", form.getDgCheckedSaleType());
			// 原单业务日期
			map.put("orgBusinessDate", form.getDgCheckedBusinessDate());
			// 原单业务时间
			map.put("orgBusinessTime", form.getDgCheckedBusinessTime());
			// 整单数量
			map.put("totalQuantity", form.getDgCheckedTotalQuantity());
			// 整单金额
			map.put("totalAmount", form.getDgCheckedTotalAmount());
			// 整单原金额
			map.put("totalOriginalAmount", form.getDgCheckedTotalOriginalAmount());
			// 整单去零金额
			map.put("roundingAmount", form.getDgCheckedRoundingAmount());
			//****************************************************************/
			// 花费积分
			map.put("costPoint", form.getDgCheckedCostPoint());
			// 花费积分抵扣
			map.put("costPointAmount", form.getDgCheckedCostPointAmount());
			// 找零金额
			map.put("giveChange", form.getGiveChange());
			// 现金收款金额
			map.put("cash", form.getCash());
			// 银行卡收款金额
			map.put("bankCard", form.getBankCard());
			// 信用卡收款金额
			map.put("creditCard", form.getCreditCard());
			// 存值卡收款金额
			map.put("cashCard", form.getCashCard());
			// 支付宝退款金额
			map.put("aliPay", form.getAliPay());
			// 微信支付退款金额
			map.put("wechatPay", form.getWechatPay());
			// 支付宝收款金额
			map.put("orgAliPay", form.getOrgAliPay());
			// 微信支付收款金额
			map.put("orgWechatPay", form.getOrgWechatPay());
			// 积分花费值
			map.put("pointValue", form.getPointValue());
			// 积分抵扣金额
			map.put("exchangeCash", form.getExchangeCash());
			//补登退货时间
			map.put("returnbussinessDate", form.getReturnbussinessDate());
			//****************************************************************/
			map.put("paymentJsonList", form.getPaymentJsonList());
			if(!"".equals(ConvertUtil.getString(form.getPaymentJsonList()))){
				List<Map<String, Object>> jsonList = ConvertUtil.json2List(form.getPaymentJsonList());
				if(null!=jsonList && !jsonList.isEmpty()){
					for(Map<String, Object> m : jsonList){
						String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
						String storePayAmount = ConvertUtil.getString(m.get("storePayAmount"));
						if("WEPAY".equals(storePayCode)){
							// 微信支付 退款
							map.put("wechatPay", storePayAmount);
							form.setWechatPay(storePayAmount);
						}else if ("PT".equals(storePayCode)) {
							// 支付宝支付 退款
							map.put("aliPay", storePayAmount);
							form.setAliPay(storePayAmount);
						}else if("CZK".equals(storePayCode)){
							form.setCashCard(storePayAmount);
						}
					}
				}
			}
			// 备注
			map.put("comments", form.getComments());
			// 获取登陆用户部门号
			map.put("departCode", userInfo.getDepartCode());
			// 获取登录员工号
			map.put("employeeCode", userInfo.getEmployeeCode());
			// 支付来源
			map.put("serialNumber", form.getDgCardCode());
			
			// 判断支付方式是否为支付宝支付或微信支付
			String json = ConvertUtil.getString(form.getServiceJsonList());
			List<Map<String, Object>> jsonList = new ArrayList<Map<String,Object>>();
			if(!"".equals(json)){
				jsonList = ConvertUtil.json2List(json);
			}
			String payType = "";
			if(NEW_CZK_PAY.equals("Y")){
				/**查询储值卡退货信息*/
				savingsList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
			}
			if(!"".equals(ConvertUtil.getString(form.getAliPay()))){
				payType = "AL";
			}else if(!"".equals(ConvertUtil.getString(form.getWechatPay()))){
				payType = "WT";
			}else if("Y".equals(NEW_CZK_PAY) &&(!"".equals(ConvertUtil.getString(form.getCashCard())) || (jsonList!=null && jsonList.size()>0))){
				payType = "CZK";
				if(savingsList.size()>0){
					// 服务类型
					serverList = code.getCodesByGrade("1338");
					for(Map<String, Object> m : savingsList){
						if(m.get("CardCode")!=null){
							form.setDgCardCode(m.get("CardCode").toString());
							break;
						}
					}
				}
			}else{
				payType = "LC";
			}
			if(!"LC".equals(payType)){
				if(!"CZK".equals(payType)){
					map.put("payType", payType);
					// 调用支付宝或微信支付接口退款
					String returnState = webPayReturn(map);
					if("SUCCESS".equals(returnState)){
						// 退款返回成功的情况下进行执行退货操作
						String result = binOLWPSAL07_IF.tran_returnsBill(map);
						if(!result.equals(CherryConstants.WP_ERROR_STATUS)){
							ConvertUtil.setResponseByAjax(response, result);
						}else{
							ConvertUtil.setResponseByAjax(response, "ERROR");
						}
					}else{
						// 退款返回失败的情况下抛出返回代号
						ConvertUtil.setResponseByAjax(response, returnState);
					}
				}
				if("Y".equals(NEW_CZK_PAY)){
					if("CZK".equals(payType)){
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("TradeType", "SavingsCardTrade");
						data.put("CardCode", form.getDgCardCode());
						data.put("VerificationType", "2");
						data.put("TransactionType", "RE");
						data.put("BillCode", srBillCode);
						data.put("RelevantCode", form.getBillCode());
						data.put("CounterCode", ConvertUtil.getString(counterInfo.getCounterCode()));
						data.put("EmployeeCode", userInfo.getEmployeeCode());
						data.put("TradeAmount", form.getCashCard());
						data.put("Memo", form.getComments());
						if(null!=jsonList && jsonList.size()>0){
							data.put("ServiceDetail", jsonList);
						}
						MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
						queryParams.add("brandCode", brandCode);
						queryParams.add("appID", SavingscardAppID + "_" + brandCode);
						queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
						WebResource wr= binOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
						String result_card=wr.queryParams(queryParams).get(String.class);
						Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
						String ERRORCODE = result_card1.get("ERRORCODE").toString();
						if(ERRORCODE.equals("0")){
							String result = "";
							/*String cashCardPay = ConvertUtil.getString(form.getCashCard());
							String bankCardPay = ConvertUtil.getString(form.getBankCard());
							String creditCardPay = ConvertUtil.getString(form.getCreditCard());
							String cashPay = ConvertUtil.getString(form.getCash());
							// 退款返回成功的情况下进行执行退货操作
							if((!"".equals(cashCardPay) && Double.valueOf(cashCardPay)>0) || 
									!"".equals(bankCardPay) && Double.valueOf(bankCardPay)>0 ||
										!"".equals(creditCardPay) && Double.valueOf(creditCardPay)>0 ||
											!"".equals(cashPay) && Double.valueOf(cashPay)>0){
								result = binOLWPSAL07_IF.tran_returnsBill(map);
							}else {
								result = "1";
							}*/
							String paymentJsonList = ConvertUtil.getString(form.getPaymentJsonList());
							if(!"".equals(paymentJsonList)){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.put(CherryConstants.BRANDINFOID,brandInfoId);
								parMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
								parMap.put("billCode", ConvertUtil.getString(form.getBillCode()));
								//获取明细数据
								List<Map<String, Object>> billSrDetailList = binOLWPSAL07_Service.getBillDetailListByCode(parMap);
								if (null != billSrDetailList && !billSrDetailList.isEmpty()){
									result = binOLWPSAL07_IF.tran_returnsBill(map);
								}else {
									result="1";
								}
							}else {
								result = "1";
							}
							ConvertUtil.setResponseByAjax(response, result);
						}else if("STE0001".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0001");
						}else if("STE0012".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0012");
						}else if("STE0013".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0013");
						}else if("STE0015".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0015");
						}else if("STE0018".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0018");
						}else {
							ConvertUtil.setResponseByAjax(response, "ERROR");
							// 记录日志
							logger.info("优惠信息查询WebService调用结果："+ ConvertUtil.getString(result_card1.get("ERRORMSG")));
						}
					}
				}
			}else{
				// 非支付宝或微信支付方式时直接退货
				String result = binOLWPSAL07_IF.tran_returnsBill(map);
				if(!result.equals(CherryConstants.WP_ERROR_STATUS)){
					ConvertUtil.setResponseByAjax(response, result);
				}else{
					ConvertUtil.setResponseByAjax(response, "ERROR");
				}
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
	
	public void returnsGoods() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			// 生成退货单据号
			String srBillCode = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), 
					ConvertUtil.getString(userInfo.getBIN_BrandInfoID()), ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.WP_BILLPREFIX_WR);
			if(!srBillCode.contains(CherryConstants.WP_BILLPREFIX_WR)){
				ConvertUtil.setResponseByAjax(response, "ERROR");
				return;
			}
			// 云POS是否支持新储值卡支付
			String NEW_CZK_PAY = binOLCM14_BL.getWebposConfigValue("9022", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			String brandCode = ConvertUtil.getString(userInfo.getBrandCode());
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌代号
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 登陆用户
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 语言
			map.put("language", userInfo.getLanguage());
			// 员工ID
			map.put("employeeId", userInfo.getBIN_EmployeeID());
			// 获取登录IP作为机器号
			map.put("machineCode", userInfo.getLoginIP());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 退货单据号
			map.put("srBillCode", srBillCode);
			// 单据号
			map.put("billCode", form.getBillCode());
			// BA编号
			map.put("baCode", form.getDgCheckedBaCode());
			// 会员卡号
			map.put("memberCode", form.getDgCheckedMemberCode());
			// 会员等级
			map.put("memberLevel", form.getDgCheckedMemberLevel());
			// 客户类型
			map.put("customerType", form.getDgCheckedCustomerType());
			// 销售类型
			map.put("saleType", form.getDgCheckedSaleType());
			// 原单业务日期
			map.put("orgBusinessDate", form.getDgCheckedBusinessDate());
			// 原单业务时间
			map.put("orgBusinessTime", form.getDgCheckedBusinessTime());
			// 整单去零金额
			map.put("roundingAmount", form.getDgCheckedRoundingAmount());
			// 花费积分
			map.put("costPoint", form.getDgCheckedCostPoint());
			// 花费积分抵扣
			map.put("costPointAmount", form.getDgCheckedCostPointAmount());
			// 找零金额
			map.put("giveChange", form.getGiveChange());
			//****************************************************************/
			// 现金收款金额
			map.put("cash", form.getCash());
			// 银行卡收款金额
			map.put("bankCard", form.getBankCard());
			// 支付宝退款金额
			map.put("aliPay", form.getAliPay());
			// 微信支付退款金额
			map.put("wechatPay", form.getWechatPay());
			// 支付宝收款金额
			map.put("orgAliPay", form.getOrgAliPay());
			// 微信支付收款金额
			map.put("orgWechatPay", form.getOrgWechatPay());
			// 信用卡收款金额
			map.put("creditCard", form.getCreditCard());
			// 存值卡
			map.put("cashCard", form.getCashCard());
			// 积分花费值
			map.put("pointValue", form.getPointValue());
			// 积分抵扣金额
			map.put("exchangeCash", form.getExchangeCash());
			//补登退货时间
			map.put("returnbussinessDate", form.getReturnbussinessDate());
			//****************************************************************/
			map.put("paymentJsonList", form.getPaymentJsonList());
			if(!"".equals(ConvertUtil.getString(form.getPaymentJsonList()))){
				List<Map<String, Object>> jsonList = ConvertUtil.json2List(form.getPaymentJsonList());
				if(null!=jsonList && !jsonList.isEmpty()){
					for(Map<String, Object> m : jsonList){
						String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
						String storePayAmount = ConvertUtil.getString(m.get("storePayAmount"));
						if("WEPAY".equals(storePayCode)){
							// 微信支付 退款
							map.put("wechatPay", storePayAmount);
							form.setWechatPay(storePayAmount);
						}else if ("PT".equals(storePayCode)) {
							// 支付宝支付 退款
							map.put("aliPay", storePayAmount);
							form.setAliPay(storePayAmount);
						}else if("CZK".equals(storePayCode)){
							form.setCashCard(storePayAmount);
						}
					}
				}
			}
			// 明细数据
			map.put("srDetailStr", form.getBillSrDetailStr());
			// 获取登陆用户部门号
			map.put("departCode", userInfo.getDepartCode());
			// 获取登录员工号
			map.put("employeeCode", userInfo.getEmployeeCode());
			// 备注
			map.put("comments", form.getComments());
			// 支付来源
			map.put("serialNumber", form.getDgCardCode());
			// 判断支付方式是否为支付宝支付或微信支付
			String json = ConvertUtil.getString(form.getServiceJsonList());
			List<Map<String, Object>> jsonList = new ArrayList<Map<String,Object>>();
			if(!"".equals(json)){
				jsonList = ConvertUtil.json2List(json);
			}
			String payType = "";
			if(NEW_CZK_PAY.equals("Y")){
				/**查询储值卡退货信息*/
				savingsList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
			}
			if(!"".equals(ConvertUtil.getString(form.getAliPay()))){
				payType = "AL";
			}else if(!"".equals(ConvertUtil.getString(form.getWechatPay()))){
				payType = "WT";
			}else if(NEW_CZK_PAY.equals("Y") && !"".equals(ConvertUtil.getString(form.getCashCard())) || (jsonList!=null && jsonList.size()>0)){
				payType = "CZK";
				if(savingsList.size()>0){
					// 服务类型
					serverList = code.getCodesByGrade("1338");
					for(Map<String, Object> m : savingsList){
						if(m.get("CardCode")!=null){
							form.setDgCardCode(m.get("CardCode").toString());
							break;
						}
					}
				}
			}else{
				payType = "LC";
			}
			if(!"LC".equals(payType)){
				if(!"CZK".equals(payType)){
					map.put("payType", payType);
					// 调用支付宝或微信支付接口退款
					String returnState = webPayReturn(map);
					if("SUCCESS".equals(returnState)){
						// 退款返回成功的情况下进行执行退货操作
						String result = binOLWPSAL07_IF.tran_returnsGoods(map);
						if(!result.equals(CherryConstants.WP_ERROR_STATUS)){
							ConvertUtil.setResponseByAjax(response, result);
						}else{
							ConvertUtil.setResponseByAjax(response, "ERROR");
						}
					}else{
						// 退款返回失败的情况下抛出返回代号
						ConvertUtil.setResponseByAjax(response, returnState);
					}
				}
				if("Y".equals(NEW_CZK_PAY)){
					if("CZK".equals(payType)){
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("TradeType", "SavingsCardTrade");
						data.put("CardCode", form.getDgCardCode());
						data.put("VerificationType", "2");
						data.put("TransactionType", "RE");
						data.put("BillCode", srBillCode);
						data.put("RelevantCode", form.getBillCode());
						data.put("CounterCode", ConvertUtil.getString(counterInfo.getCounterCode()));
						data.put("EmployeeCode", userInfo.getEmployeeCode());
						data.put("TradeAmount", form.getCashCard());
						data.put("Memo", form.getComments());
						json = ConvertUtil.getString(form.getServiceJsonList());
						if(jsonList!=null && jsonList.size()>0){
							data.put("ServiceDetail", jsonList);
						}
						MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
						queryParams.add("brandCode", brandCode);
						queryParams.add("appID", SavingscardAppID + "_" + brandCode);
						queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
						WebResource wr= binOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
						String result_card=wr.queryParams(queryParams).get(String.class);
						Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
						String ERRORCODE = result_card1.get("ERRORCODE").toString();
						if(ERRORCODE.equals("0")){
							String result = "";
							/*String cashCardPay = ConvertUtil.getString(form.getCashCard());
							String bankCardPay = ConvertUtil.getString(form.getBankCard());
							String creditCardPay = ConvertUtil.getString(form.getCreditCard());
							String cashPay = ConvertUtil.getString(form.getCash());
							// 退款返回成功的情况下进行执行退货操作
							if((!"".equals(cashCardPay) && Double.valueOf(cashCardPay)>0) || 
									!"".equals(bankCardPay) && Double.valueOf(bankCardPay)>0 ||
										!"".equals(creditCardPay) && Double.valueOf(creditCardPay)>0 ||
											!"".equals(cashPay) && Double.valueOf(cashPay)>0){
								result = binOLWPSAL07_IF.tran_returnsGoods(map);
							}else {
								result = "1";
							}*/
							String paymentJsonList = ConvertUtil.getString(form.getPaymentJsonList());
							if(!"".equals(paymentJsonList)){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.put(CherryConstants.BRANDINFOID,ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
								parMap.put(CherryConstants.ORGANIZATIONINFOID, ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
								parMap.put("billCode", ConvertUtil.getString(form.getBillCode()));
								//获取明细数据
								List<Map<String, Object>> billSrDetailList = binOLWPSAL07_Service.getBillDetailListByCode(parMap);
								if (null != billSrDetailList && !billSrDetailList.isEmpty()){
									result = binOLWPSAL07_IF.tran_returnsBill(map);
								}else {
									result="1";
								}
							}else {
								result = "1";
							}
							ConvertUtil.setResponseByAjax(response, result);
						}else if("STE0001".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0001");
						}else if("STE0012".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0012");
						}else if("STE0013".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0013");
						}else if("STE0015".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0015");
						}else if("STE0018".equals(ERRORCODE)){
							ConvertUtil.setResponseByAjax(response, "STE0018");
						}else {
							// 记录日志
							logger.info("优惠信息查询WebService调用结果："+ ConvertUtil.getString(result_card1.get("ERRORMSG")));
						}
					}
				}else {
					// 非支付宝或微信支付方式时直接退货
					String result = binOLWPSAL07_IF.tran_returnsGoods(map);
					if(!result.equals(CherryConstants.WP_ERROR_STATUS)){
						ConvertUtil.setResponseByAjax(response, result);
					}else{
						ConvertUtil.setResponseByAjax(response, "ERROR");
					}
				}
			}else{
				// 非支付宝或微信支付方式时直接退货
				String result = binOLWPSAL07_IF.tran_returnsGoods(map);
				if(!result.equals(CherryConstants.WP_ERROR_STATUS)){
					ConvertUtil.setResponseByAjax(response, result);
				}else{
					ConvertUtil.setResponseByAjax(response, "ERROR");
				}
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
	
	public String webPayReturn(Map<String, Object> paramMap) throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(paramMap.get("brandInfoId"));
			String organizationInfoId = ConvertUtil.getString(paramMap.get("organizationInfoId"));
			String srBillCode = ConvertUtil.getString(paramMap.get("srBillCode"));
			String billCode = ConvertUtil.getString(paramMap.get("billCode"));
			String alipay = ConvertUtil.getString(paramMap.get("aliPay"));
			String wechatPay = ConvertUtil.getString(paramMap.get("wechatPay"));
			String orgWechatPay = ConvertUtil.getString(paramMap.get("orgWechatPay"));
			String counterCode = ConvertUtil.getString(paramMap.get("counterCode"));
			
			if("".equals(billCode)){
				// 单据号为空的情况
				return "BN";
			}else if("".equals(alipay) && "".equals(wechatPay)){
				// 支付金额为空的情况
				return "TN";
			}else{
				String payType = ConvertUtil.getString(paramMap.get("payType"));
				if("AL".equals(payType)){
					// 支付类型为支付宝支付时
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					boolean flag = aliPayIF.getAliPayConfig(map);
					if(flag){
						map.put("counterCode", counterCode);
						String version = binOLCM14_BL.getWebposConfigValue("9029", organizationInfoId, brandInfoId);
						if("1.0".equals(version)){
							map.put("payType", "ALIPAY");
						}else {
							map.put("payType", "ALIPAY2");
						}
						List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
						if (null != configList && !configList.isEmpty()){
							// 定义全部柜台收款账户存放Map
							Map<String, Object> allConfigMap = new HashMap<String, Object>();
							// 定义单个柜台收款账户存放Map
							Map<String, Object> counterConfigMap = new HashMap<String, Object>();
							
							// 从配置列表中获取配置项
							for(Map<String,Object> configMap : configList){
								String payCounter = ConvertUtil.getString(configMap.get("counterCode"));
								if("ALL".equals(payCounter)){
									// 当配置项为全部柜台情况下获取配置信息放入 allConfigMap 中
									allConfigMap.put("alipayPartnerId", ConvertUtil.getString(configMap.get("partnerId")));
									allConfigMap.put("alipaySignKey", ConvertUtil.getString(configMap.get("paternerKey")));
									allConfigMap.put("alipaySignType", ConvertUtil.getString(configMap.get("keyType")));
									allConfigMap.put("aliPayInputCharSet", ConvertUtil.getString(configMap.get("inputCharSet")));
								}else if(counterCode.equals(payCounter)){
									// 当配置项为指定柜台情况下获取配置信息放入 counterConfigMap 中
									counterConfigMap.put("alipayPartnerId", ConvertUtil.getString(configMap.get("partnerId")));
									counterConfigMap.put("alipaySignKey", ConvertUtil.getString(configMap.get("paternerKey")));
									counterConfigMap.put("alipaySignType", ConvertUtil.getString(configMap.get("keyType")));
									counterConfigMap.put("aliPayInputCharSet", ConvertUtil.getString(configMap.get("inputCharSet")));
								}
							}
							// 判断指定柜台的支付配置信息是否存在
							if (null != counterConfigMap && !counterConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(counterConfigMap);
								parMap.put("strOutTradeNo", billCode);
								parMap.put("strOutRequestNo", srBillCode);
								parMap.put("strRefundAmount", alipay);
								parMap.put("strBaCode", ConvertUtil.getString(map.get("baCode")));
								String result = "";
								if("1.0".equals(version)){
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndRefund(parMap);
									result = getAliPayReturnResult(returnList);
								}else if("2.0".equals(version)){
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndRefundTwo(parMap);
									result = getAliPayReturnResult(returnList);
								}
								return result;
							}else{
								if (null != allConfigMap && !allConfigMap.isEmpty()){
									Map<String, Object> parMap = new HashMap<String, Object>();
									parMap.putAll(allConfigMap);
									parMap.put("strOutTradeNo", billCode);
									parMap.put("strOutRequestNo", srBillCode);
									parMap.put("strRefundAmount", alipay);
									parMap.put("strBaCode", ConvertUtil.getString(map.get("baCode")));
									String result = "";
									if("1.0".equals(version)){
										List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndRefund(parMap);
										result = getAliPayReturnResult(returnList);
									}else if("2.0".equals(version)){
										List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndRefundTwo(parMap);
										result = getAliPayReturnResult(returnList);
									}
									return result;
								}else{
									// 没有给指定柜台配置收款账户信息的情况
									return "NAP";
								}
							}
						}else{
							// 没有给指定柜台配置收款账户信息的情况
							return "NAP";
						}
					}else{
						// 没有获取到支付宝配置的情况
						return "NC";
					}
				}else if("WT".equals(payType)){
					// 支付类型为微信支付时
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("counterCode", counterCode);
					map.put("payType", "WECHATPAY");
					List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
					if (null != configList && !configList.isEmpty()){
						int total_fee=Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(orgWechatPay)*100)));
						int refund_fee=Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(wechatPay)*100)));
						// 定义全部柜台收款账户存放Map
						Map<String, Object> allConfigMap = new HashMap<String, Object>();
						// 定义单个柜台收款账户存放Map
						Map<String, Object> counterConfigMap = new HashMap<String, Object>();
						
						// 从配置列表中获取配置项
						for(Map<String,Object> configMap : configList){
							String payCounter = ConvertUtil.getString(configMap.get("counterCode"));
							if("ALL".equals(payCounter)){
								// 当配置项为全部柜台情况下获取配置信息放入 allConfigMap 中
								allConfigMap.put("appid", ConvertUtil.getString(configMap.get("appId")));
								allConfigMap.put("mch_id", ConvertUtil.getString(configMap.get("partnerId")));
								allConfigMap.put("sub_mch_id", ConvertUtil.getString(configMap.get("subMchId")));
								allConfigMap.put("paternerKey", ConvertUtil.getString(configMap.get("paternerKey")));
							}else if(counterCode.equals(payCounter)){
								// 当配置项为指定柜台情况下获取配置信息放入 counterConfigMap 中
								counterConfigMap.put("appid", ConvertUtil.getString(configMap.get("appId")));
								counterConfigMap.put("mch_id", ConvertUtil.getString(configMap.get("partnerId")));
								counterConfigMap.put("sub_mch_id", ConvertUtil.getString(configMap.get("subMchId")));
								counterConfigMap.put("paternerKey", ConvertUtil.getString(configMap.get("paternerKey")));
							}
						}
						// 判断指定柜台的支付配置信息是否存在
						if (null != counterConfigMap && !counterConfigMap.isEmpty()){
							Map<String, Object> parMap = new HashMap<String, Object>();
							parMap.putAll(counterConfigMap);
							parMap.put("out_trade_no", billCode);
							parMap.put("out_refund_no", srBillCode);
							parMap.put("total_fee", ConvertUtil.getString(total_fee));
							parMap.put("refund_fee", ConvertUtil.getString(refund_fee));
							parMap.put("device_info", userInfo.getLoginIP());
							parMap.put("brandCode", userInfo.getBrandCode());
							List<Map<String, Object>> returnList = weChatPayIF.getWEpayRefund(parMap);
							String result = getWechatPayReturnResult(returnList);
							return result;
						}else{
							if (null != allConfigMap && !allConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(allConfigMap);
								parMap.put("out_trade_no", billCode);
								parMap.put("out_refund_no", srBillCode);
								parMap.put("total_fee", ConvertUtil.getString(total_fee));
								parMap.put("refund_fee", ConvertUtil.getString(refund_fee));
								parMap.put("device_info", userInfo.getLoginIP());
								parMap.put("brandCode", userInfo.getBrandCode());
								List<Map<String, Object>> returnList = weChatPayIF.getWEpayRefund(parMap);
								String result = getWechatPayReturnResult(returnList);
								return result;
							}else{
								// 没有给指定柜台配置收款账户信息的情况
								return "NWP";
							}
						}
					}else{
						// 没有给指定柜台配置收款账户信息的情况
						return "NWP";
					}
				}else{
					// 支付类型为非支付宝和微信支付时
					return "LC";
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return "IFERROR";
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return "IFERROR";
			 }
		}
	}
	
	// 获取支付宝支付退款结果
	private String getAliPayReturnResult(List<Map<String, Object>> returnList) throws Exception{
		String result = "";
		if(returnList != null && !returnList.isEmpty()) {
			for(Map<String,Object> returnMap : returnList){
				String returnCode = ConvertUtil.getString(returnMap.get("is_success"));
				if("T".equals(returnCode)){
					String resultCode = ConvertUtil.getString(returnMap.get("result_code"));
					if("SUCCESS".equals(resultCode)){
						// 退款成功
						result = "SUCCESS";
					}else if("REFUND_FAIL_WAIT_BUYER_PAY".equals(resultCode)){
						// 等待买家付款不允许退款
						result = "FWP";
					}else if("REFUND_FAIL_TRADE_CLOSED".equals(resultCode)){
						// 交易关闭不允许退款
						result = "FTC";
					}else if("REFUND_FAIL_TRADE_PENDING".equals(resultCode)){
						// 等待卖家收款不允许退款
						result = "FTP";
					}else if("REFUND_FAIL_TRADE_FINISHED".equals(resultCode)){
						// 交易结束不允许退款
						result = "FTF";
					}else{
						// 退款失败失败的情况
						result = "FAIL";
						logger.error(ConvertUtil.getString(returnMap));
					}
				}else{
					// 支付宝支付接口通讯失败的情况
					result = "FA";
					logger.error(ConvertUtil.getString(returnMap));
				}
			}
		}else{
			// 没有获取到返回结果的情况
			result = "NR";
		}
		return result;
	}
	
	// 获取微信支付退款结果
	private String getWechatPayReturnResult(List<Map<String, Object>> returnList) throws Exception{
		String result = "";
		if(returnList != null && !returnList.isEmpty()) {
			for(Map<String,Object> returnMap : returnList){
				String returnCode = ConvertUtil.getString(returnMap.get("return_code"));
				if("SUCCESS".equals(returnCode)){
					String resultCode = ConvertUtil.getString(returnMap.get("result_code"));
					if("SUCCESS".equals(resultCode)){
						// 微信支付成功
						result = "SUCCESS";
					}else{
						String errorCode = ConvertUtil.getString(returnMap.get("err_code"));
						if("REFUND_FEE_INVALID".equals(errorCode)){
							// 退款金额大于支付金额的情况
							result = "RFI";
						}else{
							// 退款失败的情况
							result = "FAIL";
						}
						logger.error(ConvertUtil.getString(returnMap));
					}
				}else{
					// 微信支付接口通讯失败的情况
					result = "FC";
					logger.error(ConvertUtil.getString(returnMap));
				}
			}
		}else{
			// 没有获取到返回结果的情况
			result = "NR";
		}
		return result;
	}
	
	public String getServiceBillDetail(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 单据号
			map.put("billDetailShowType", form.getBillDetailShowType());
			// 单据号
			map.put("billCode", form.getBillCode());
			//第二步：获取单据明细
			savingsList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
			// 储值卡明细
			List<Map<String, Object>> serverList_CodeKey = new ArrayList<Map<String,Object>>();
			if(null!=savingsList && !savingsList.isEmpty()){
				serverList_CodeKey = code.getCodesByGrade("1338");
				for(Map<String, Object> m : savingsList){
					if(null!=serverList_CodeKey && !serverList_CodeKey.isEmpty()){
						for(Map<String, Object> cm : serverList_CodeKey){
							if(null!=m.get("ServiceType") && null!=cm.get("CodeKey") && m.get("ServiceType").toString().equals(cm.get("CodeKey"))){
								m.put("serviceName", cm.get("Value"));
							}
						}
					}
				}
			}
			// 显示单据明细
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
		return SUCCESS;
	}
	public String getPayTypeBillDetail(){
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 单据号
			map.put("billCode", form.getBillCode());
			// 交易记录里的支付方式
			paymentTypeList = binOLWPSAL07_IF.getPaymentTypeList(map);
			//CODE值里的支付方式
			List<Map<String, Object>> paymentList = code.getCodesByGrade("1175");
			if(null!=paymentTypeList && !paymentTypeList.isEmpty()){
				for(Map<String, Object> CodeKey : paymentList){
					for(Map<String, Object> payType : paymentTypeList){
						String key = CodeKey.get("CodeKey").toString();
						String type = payType.get("payType").toString();
						if(key.equals(type)){
							payType.put("value", CodeKey.get("Value"));
						}
					}
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
		return SUCCESS;
	}
	@Override
	public BINOLWPSAL07_Form getModel() {
		return form;
	}

	public String getIsPlatinumPrice() {
		return isPlatinumPrice;
	}

	public void setIsPlatinumPrice(String isPlatinumPrice) {
		this.isPlatinumPrice = isPlatinumPrice;
	}
	
}
