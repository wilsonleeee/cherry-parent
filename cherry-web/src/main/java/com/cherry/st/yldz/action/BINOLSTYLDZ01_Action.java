package com.cherry.st.yldz.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.yldz.form.BINOLSTYLDZ01_Form;
import com.cherry.st.yldz.interfaces.BINOLSTYLDZ01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTYLDZ01_Action extends BaseAction implements ModelDriven<BINOLSTYLDZ01_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2130685979140760286L;

	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTYLDZ01_Action.class);
	
	private BINOLSTYLDZ01_Form form = new BINOLSTYLDZ01_Form();
	
	@Resource(name="binOLSTYLDZ01_BL")
	private BINOLSTYLDZ01_IF binOLSTYLDZ01_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	/** 导入结果 */
	private Map resultMap;
	/** 上传的文件 */
	private File upExcel;
	public String init() throws Exception{
		return SUCCESS;
	}
	public String search(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 获取销售总数
		ConvertUtil.setForm(form, map);
		map.put("sysBillCode", ConvertUtil.getString(form.getSysBillCode()));
		map.put("hedgingBillCode", ConvertUtil.getString(form.getHedgingBillCode()));
		map.put("posBillCode", ConvertUtil.getString(form.getPosBillCode()));
		map.put("referenceCode", ConvertUtil.getString(form.getReferenceCode()));
		map.put("posCode", ConvertUtil.getString(form.getPosCode()));
		map.put("companyCode", ConvertUtil.getString(form.getCompanyCode()));
		map.put("cardCode", ConvertUtil.getString(form.getCardCode()));
		map.put("companyName", ConvertUtil.getString(form.getCompanyName()));
		map.put("tradeResult", ConvertUtil.getString(form.getTradeResult()));
		map.put("tradeAnswer", ConvertUtil.getString(form.getTradeAnswer()));
		map.put("batchId", ConvertUtil.getString(form.getBatchId()));
		map.put("tradeType", ConvertUtil.getString(form.getTradeType()));
		map.put("tradeDateStart", ConvertUtil.getString(form.getTradeDateStart()));
		map.put("tradeDateEnd", ConvertUtil.getString(form.getTradeDateEnd()));
		try {
			int count = binOLSTYLDZ01_BL.getSaleListCount(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count>0){
				// 获取销售数据
				form.setSaleList(binOLSTYLDZ01_BL.getSaleList(map));
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	public String editInit(){
		String billId = ConvertUtil.getString(form.getBillId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billId", billId);
		try {
			form.setEditSaleMap(binOLSTYLDZ01_BL.editInit(map));
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	public void delete() throws Exception{
		String billId = ConvertUtil.getString(form.getBillId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billId", billId);
		map.put("updatedBy", "BINOLSTYLDZ01");
		map.put("updatePGM", "CherryMaven");
		try {
			binOLSTYLDZ01_BL.delete(map);
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		} catch (Exception e) {
			ConvertUtil.setResponseByAjax(response, "ERROR");
			logger.info(e.getMessage(),e);
		}
	}
	
	public void addBankBill() throws Exception{
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		String tradeDate = ConvertUtil.getString(form.getTradeDate());
		String tradeTime = ConvertUtil.getString(form.getTradeTime());
		String sysBillCode = ConvertUtil.getString(form.getSysBillCode());
		String cardCode = ConvertUtil.getString(form.getCardCode());
		String companyCode = ConvertUtil.getString(form.getCompanyCode());
		String companyName = ConvertUtil.getString(form.getCompanyName());
		String posCode = ConvertUtil.getString(form.getPosCode());
		String posBillCode = ConvertUtil.getString(form.getPosBillCode());
		String hedgingBillCode = ConvertUtil.getString(form.getHedgingBillCode());
		String referenceCode = ConvertUtil.getString(form.getReferenceCode());
		String amount = ConvertUtil.getString(form.getAmount());
		String tradeType = ConvertUtil.getString(form.getTradeType());
		String tradeResult = ConvertUtil.getString(form.getTradeResult());
		String tradeAnswer = ConvertUtil.getString(form.getTradeAnswer());
		if("".equals(amount)){
			amount="0";
		}
		if(CherryChecker.isNullOrEmpty(tradeDate, true) || !CherryChecker.checkDate(tradeDate)){
			ConvertUtil.setResponseByAjax(response, "tradeDate");
		}else if(!CherryChecker.isNullOrEmpty(tradeTime, true) && !CherryChecker.checkTime(tradeTime)){
			ConvertUtil.setResponseByAjax(response, "tradeTime");
		}else if(cardCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "cardCode");
		}else if(companyCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "companyCode");
		}else if(companyName.length() > 50){
			ConvertUtil.setResponseByAjax(response, "companyName");
		}else if(CherryChecker.isNullOrEmpty(posCode, true) || posCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "posCode");
		}else if(CherryChecker.isNullOrEmpty(posBillCode, true) || posBillCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "posBillCode");
		}else if(sysBillCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "sysBillCode");
		}else if(hedgingBillCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "hedgingBillCode");
		}else if(referenceCode.length() > 50){
			ConvertUtil.setResponseByAjax(response, "referenceCode");
		}else if(!CherryChecker.isDecimal(amount, 6, 2)){
			ConvertUtil.setResponseByAjax(response, "amount");
		}else if(tradeType.length() > 20){
			ConvertUtil.setResponseByAjax(response, "tradeType");
		}else if(tradeResult.length() > 50){
			ConvertUtil.setResponseByAjax(response, "tradeResult");
		}else if(tradeAnswer.length() > 50){
			ConvertUtil.setResponseByAjax(response, "tradeAnswer");
		}else{
			map.put(CherryConstants.ORGANIZATIONINFOID, ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
			map.put(CherryConstants.BRANDINFOID, ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			map.put("tradeDate", tradeDate);
			map.put("tradeTime", tradeTime);
			map.put("sysBillCode", sysBillCode);
			map.put("cardCode", cardCode);
			map.put("companyCode", companyCode);
			map.put("companyName", companyName);
			map.put("posCode", posCode);
			map.put("posBillCode", posBillCode);
			map.put("hedgingBillCode", hedgingBillCode);
			map.put("referenceCode", referenceCode);
			map.put("amount", amount);
			map.put("tradeType", tradeType);
			map.put("tradeResult", tradeResult);
			map.put("tradeAnswer", tradeAnswer);
			map.put("createdBy", "BINOLSTYLDZ01");
			map.put("createPGM", "BINOLSTYLDZ01");
			try {
				int count = binOLSTYLDZ01_BL.checkBillCode(map);
				if(count<1){
					binOLSTYLDZ01_BL.addBankBill(map);
					ConvertUtil.setResponseByAjax(response, "SUCCESS");
				}else {
					ConvertUtil.setResponseByAjax(response, "BANKBILLCODE");
				}
			} catch (Exception e) {
				ConvertUtil.setResponseByAjax(response, "ERROR");
				logger.info(e.getMessage(),e);
			}
		}
	}
	public void updateBankBill() throws Exception{
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
		String billId = ConvertUtil.getString(form.getBillId());
		String tradeDate = ConvertUtil.getString(form.getTradeDate());
		String tradeTime = ConvertUtil.getString(form.getTradeTime());
		String sysBillCode = ConvertUtil.getString(form.getSysBillCode());
		String cardCode = ConvertUtil.getString(form.getCardCode());
		String companyCode = ConvertUtil.getString(form.getCompanyCode());
		String companyName = ConvertUtil.getString(form.getCompanyName());
		String posCode = ConvertUtil.getString(form.getPosCode());
		String posBillCode = ConvertUtil.getString(form.getPosBillCode());
		String hedgingBillCode = ConvertUtil.getString(form.getHedgingBillCode());
		String referenceCode = ConvertUtil.getString(form.getReferenceCode());
		String amount = ConvertUtil.getString(form.getAmount());
		String tradeType = ConvertUtil.getString(form.getTradeType());
		String tradeResult = ConvertUtil.getString(form.getTradeResult());
		String tradeAnswer = ConvertUtil.getString(form.getTradeAnswer());
		if("".equals(amount)){
			amount="0";
		}
		if(CherryChecker.isNullOrEmpty(tradeDate, true) || !CherryChecker.checkDate(tradeDate)){
			ConvertUtil.setResponseByAjax(response, "tradeDate");
		}else if(!CherryChecker.isNullOrEmpty(tradeTime, true) && !CherryChecker.checkTime(tradeTime)){
			ConvertUtil.setResponseByAjax(response, "tradeTime");
		}else if(cardCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "cardCode");
		}else if(companyCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "companyCode");
		}else if(companyName.length() > 50){
			ConvertUtil.setResponseByAjax(response, "companyName");
		}else if(CherryChecker.isNullOrEmpty(posCode, true) || posCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "posCode");
		}else if(CherryChecker.isNullOrEmpty(posBillCode, true) || posBillCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "posBillCode");
		}else if(sysBillCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "sysBillCode");
		}else if(hedgingBillCode.length() > 30){
			ConvertUtil.setResponseByAjax(response, "hedgingBillCode");
		}else if(referenceCode.length() > 50){
			ConvertUtil.setResponseByAjax(response, "referenceCode");
		}else if(!CherryChecker.isDecimal(amount, 6, 2)){
			ConvertUtil.setResponseByAjax(response, "amount");
		}else if(tradeType.length() > 20){
			ConvertUtil.setResponseByAjax(response, "tradeType");
		}else if(tradeResult.length() > 50){
			ConvertUtil.setResponseByAjax(response, "tradeResult");
		}else if(tradeAnswer.length() > 50){
			ConvertUtil.setResponseByAjax(response, "tradeAnswer");
		}else{
			map.put(CherryConstants.ORGANIZATIONINFOID, ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
			map.put(CherryConstants.BRANDINFOID, ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			map.put("billId",billId);
			map.put("tradeDate", tradeDate);
			map.put("tradeTime", tradeTime);
			map.put("sysBillCode", sysBillCode);
			map.put("cardCode", cardCode);
			map.put("companyCode", companyCode);
			map.put("companyName", companyName);
			map.put("posCode", posCode);
			map.put("posBillCode", posBillCode);
			map.put("hedgingBillCode", hedgingBillCode);
			map.put("referenceCode", referenceCode);
			map.put("amount", amount);
			map.put("tradeType", tradeType);
			map.put("tradeResult", tradeResult);
			map.put("tradeAnswer", tradeAnswer);
			map.put("updatedBy", "BINOLSTYLDZ01");
			map.put("updatePGM", "BINOLSTYLDZ01");
			try {
				int count = binOLSTYLDZ01_BL.checkBillCode(map);
				if(count<1){
					binOLSTYLDZ01_BL.updateBankBill(map);
					ConvertUtil.setResponseByAjax(response, "SUCCESS");
				}else {
					ConvertUtil.setResponseByAjax(response, "BANKBILLCODE");
				}
			} catch (Exception e) {
				e.printStackTrace();
				ConvertUtil.setResponseByAjax(response, "ERROR");
				logger.error(e.getMessage(),e);
			}
		}
	}
	/**
	 * 银联对账导入初始化
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String importInit() throws Exception {
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE,
					session.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
				// 总部场合
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
				// 品牌名称
				brandMap.put("brandName", getText("PPL00006"));
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			} else {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				// 品牌名称
				brandMap.put("brandName", userInfo.getBrandName());
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				// 操作失败！
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 银联对账导入
	 * @return
	 * @throws Exception
	 */
	public String importBankBills() throws Exception {
		try {
			Map<String, Object> sessionMap = getSessionMap();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			//导入的数据
			List<Map<String, Object>> importDataList = binOLSTYLDZ01_BL.ResolveExcel(sessionMap);
			Map<String, Object> resultMap = binOLSTYLDZ01_BL.tran_excelHandle(importDataList, sessionMap);
			resultMap.put("currentImportBatchCode", sessionMap.get("currentImportBatchCode"));
			setResultMap(resultMap);
			return "BINOLSTYLDZ01_04";
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				// 未知错误，请重新导入该数据
				this.addActionError(getText("EMO00079"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
		}
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		map.put("Comments", form.getComments());
		map.put("ImportBatchCode", form.getImportBatchCode());
		map.put("importBatchCode", form.getImportBatchCode());
		map.put("ImportBatchCodeIF", form.getImportBatchCode());
		map.put("CreatedBy", "BINOLSTYLDZ01");
		map.put("CreatePGM", "BINOLSTYLDZ01");
		map.put("UpdatedBy", "BINOLSTYLDZ01");
		map.put("UpdatePGM", "BINOLSTYLDZ01");
		map.put("Type", "OD");
		map.put("isChecked", form.getIsChecked());
		map.put("importRepeat", form.getImportRepeat());
		map.put("type", "OD");
		
		return map;
	}	
	
	
	public String addBankBillInit(){
		return SUCCESS;
	}
	
	@Override
	public BINOLSTYLDZ01_Form getModel() {
		return form;
	}
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}
	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	public File getUpExcel() {
		return upExcel;
	}
	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
	public Map getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}
	
}
