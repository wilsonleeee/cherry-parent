/*  
 * @(#)BINOLMOCIO07_Action.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.mo.cio.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.cio.form.BINOLMOCIO07_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO07_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO07_Action  extends BaseAction implements
ModelDriven<BINOLMOCIO07_Form>{

	private static final long serialVersionUID = -2643933529055474343L;
	
	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO07_Action.class);
	/**销售月目标*/
	@Resource(name="binOLMOCIO07_BL")
	private BINOLMOCIO07_IF binOLMOCIO07_BL;
	/**销售日目标*/
	@Resource(name="binOLMOCIO07_01_BL")
	private BINOLMOCIO07_IF binOLMOCIO07_01_BL;
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	/** 参数FORM */
	private BINOLMOCIO07_Form form = new BINOLMOCIO07_Form();
	
	private List<Map<String, Object>> brandInfoList;
	
	private List<Map<String, Object>> errorTargetList;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	
	public List<Map<String, Object>> getErrorTargetList() {
		return errorTargetList;
	}

	public void setErrorTargetList(List<Map<String, Object>> errorTargetList) {
		this.errorTargetList = errorTargetList;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getUpExcelFileName() {
		return upExcelFileName;
	}

	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	@SuppressWarnings("unchecked")
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
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
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 目标月
		form.setTargetMonth(CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		// 目标日
		form.setTargetDay(CherryUtil.getSysDateTime(CherryConstants.DATE_YYMMDD));
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 取得单位类型code值，默认取第一个类型
		List<Map<String, Object>> codeList = codeTable.getCodes("1124");
		if(null != codeList && codeList.size() > 0) {
			form.setType(ConvertUtil.getString(codeList.get(0).get("CodeKey")));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 单位类型
		map.put("type", form.getType());
		// 目标类型
		map.put("targetType", form.getTargetType());
		// 活动CODE
		map.put("activityCode", form.getCampaignCode());
		// 活动名称
		map.put("activityName", form.getCampaignName());
		// 选择不同的目标日期类型，有效的参数也不同
		String targetMonth = ConvertUtil.getString(form.getTargetMonth());
		String targetDay = ConvertUtil.getString(form.getTargetDay());
		if(!"".equals(targetMonth)) {
			map.put("targetDateType", "1");
		} else if(!"".equals(targetDay)) {
			map.put("targetDateType", "2");
		}
		//目标日期
		map.put("targetDate", "".equals(targetMonth) ? targetDay : targetMonth);
		//名称
		map.put("parameter", form.getParameter());
		//制单人
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
        //设置权限控制参数
//        setPrivilegeParam(map);
        String paramsStr = form.getParams();
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(paramsStr);
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}
	
	/**
	 * 主查询销售目标
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap= getSearchMap();
		String type = ConvertUtil.getString(searchMap.get("type"));
		// 可不要，type参数的赋值已经在js上设置完成
		if (searchMap.get("FILTER_VALUE") != null) {
			// 生效状态
			type = searchMap.get("FILTER_VALUE").toString();
			searchMap.put("type", type);
		}
		// 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
		String targetDateType = ConvertUtil.getString(searchMap.get("targetDateType"));
		// 取得总数
		int count = 0;
		if("1".equals(targetDateType) || "".equals(targetDateType)) {
			// 销售月目标
			count = binOLMOCIO07_BL.getSaleTargetCount(searchMap);
		} else if("2".equals(targetDateType)) {
			// 销售日目标
			count = binOLMOCIO07_01_BL.getSaleTargetCount(searchMap);
		}
		if (count > 0) {
			// 取得渠道List
			if("1".equals(targetDateType) || "".equals(targetDateType)) {
				form.setSaleTargetList(binOLMOCIO07_BL.searchSaleTargetList(searchMap));
			} else if("2".equals(targetDateType)) {
				form.setSaleTargetList(binOLMOCIO07_01_BL.searchSaleTargetList(searchMap));
			}
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLMOCIO07_1";
}
	
	/**
	 * 设置销售目标（包括新增与编辑）
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String setTarget() throws Exception {
		try{
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 创建者
	        map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMOCIO07");	
			// 作成程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO07");	
			// 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
			// 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
			String targetDateType = ConvertUtil.getString(form.getTargetDateType());
			
			if(form.getAddBrandInfoId() != null){
				/********************新增销售目标*************************/
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, form.getAddBrandInfoId());
				// 单位类型
				map.put("type", form.getAddtype());
				//JS设定目标完成日期
				if("".equals(targetDateType) || "1".equals(targetDateType)) {
					map.put("targetDate", form.getAddTargetMonth());
				} else if("2".equals(targetDateType)) {
					map.put("targetDate", form.getAddTargetDay());
				}
				
				//JS设定目标完成金额
				map.put("targetMoney", form.getAddTargetMoney());
				//JS设定目标完成数量
				map.put("targetQuantity", form.getAddTargetQuantity());
				// 目标类型
				map.put("targetType", form.getTargetType());
				// 活动CODE
				map.put("activityCode", form.getCampaignCode());
				// 活动名称
				map.put("activityName", form.getCampaignName());
				
				String addpop=form.getAddpop();
				
				List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(addpop);
				String[] parameterArr =new String[list.size()];
				//String[] nameArr = new String[list.size()];
				//String[] differentArr=new String[list.size()];
				for(int i=0; i<list.size(); i++){
					if(("01").equals(String.valueOf(list.get(i).get("categoryCodeArr")))){
						parameterArr[i]=String.valueOf(list.get(i).get("parameterArr"));
						//nameArr[i]=String.valueOf(list.get(i).get("nameArr"));
						//differentArr[i]=String.valueOf(list.get(i).get("differentArr"));
					}			
				}
				//名称
				map.put("parameterArr", parameterArr);
				//名称
				//map.put("nameArr",nameArr);
				//区别码
				//map.put("differentArr",differentArr);
			} else {
				/************************编辑销售目标************************/
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, form.getEditBrandInfoId());
				// 类型
				map.put("type", form.getType());
				//名称
				map.put("parameterArr", form.getParameterArr());
				//名称
				//map.put("nameArr", form.getNameArr());
				//区别码
				//map.put("differentArr", form.getDifferentArr());
				//JS设定目标完成日期----编辑时不允许编辑日期
				map.put("targetDate", form.getEditTargetDate());
				//JS设定目标完成金额
				map.put("targetMoney", form.getEditTargetMoney());
				//JS设定目标完成数量
				map.put("targetQuantity", form.getEditTargetQuantity());
				// 目标类型
				map.put("targetType", form.getTargetType());
				// 活动CODE
				map.put("activityCode", form.getCampaignCode());
				// 活动名称
				map.put("activityName", form.getCampaignName());
			}
			if("".equals(targetDateType) || "1".equals(targetDateType)) {
				binOLMOCIO07_BL.tran_setSaleTarget(map);
			} else if("2".equals(targetDateType)) {
				binOLMOCIO07_01_BL.tran_setSaleTarget(map);
			}
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	this.addActionError(ex.getMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
		}
	}
	
	/**
	 * 设置销售目标的后台校验
	 * @throws Exception
	 */
//	public void validateSetTarget() throws Exception {
//		// 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
//		String targetDateType = ConvertUtil.getString(form.getTargetDateType());
//		if(form.getAddBrandInfoId() != null){
//			// 新增模式
//			if(!CherryChecker.isFloatValid(form.getAddTargetMoney(), 9, 2)) {
//				this.addFieldError("addTargetMoney", getText("ECM00024",new String[]{"目标金额","9","2"}));
//			}
//			
//			if(!CherryChecker.isNumeric(form.getAddTargetQuantity())) {
//				this.addFieldError("addTargetQuantity", getText("ECM00021",new String[]{"目标数量"}));
//			}
//			
//			if("".equals(targetDateType) || "1".equals(targetDateType)) {
//				if(CherryChecker.isNullOrEmpty(form.getAddTargetMonth(), true)) {
//					// 月销售目标
//					this.addFieldError("addTargetMonth", getText("ECM00009",new String[]{"目标年月"}));
//				} else if(!CherryChecker.checkDate(form.getAddTargetMonth(), CherryConstants.DATEYYYYMM)) {
//					// {0}格式错误，正确的格式为：{1}。
//					this.addFieldError("addTargetMonth", getText("ESS00071",new String[]{"目标年月",CherryConstants.DATEYYYYMM}));
//				}
//			} else if("2".equals(targetDateType)) {
//				// 日销售目标
//				if(CherryChecker.isNullOrEmpty(form.getAddTargetDay(), true)) {
//					this.addFieldError("addTargetDay", getText("ECM00009",new String[]{"目标日期"}));
//				} else if(!CherryChecker.checkDate(form.getAddTargetDay(), CherryConstants.DATE_YYMMDD)) {
//					// {0}格式错误，正确的格式为：{1}。
//					this.addFieldError("addTargetDay", getText("ESS00071",new String[]{"目标日期",CherryConstants.DATE_YYMMDD}));
//				}
//			}
//		} else {
//			// 编辑模式
//			if(!CherryChecker.isFloatValid(form.getEditTargetMoney(), 9, 2)) {
//				this.addFieldError("editTargetMoney", getText("ECM00024",new String[]{"目标金额","9","2"}));
//			}
//			
//			if(!CherryChecker.isNumeric(form.getEditTargetQuantity())) {
//				this.addFieldError("editTargetQuantity", getText("ECM00021",new String[]{"目标数量"}));
//			}
//			
//			if("".equals(targetDateType) || "1".equals(targetDateType)) {
//				if(CherryChecker.isNullOrEmpty(form.getEditTargetDate(), true)) {
//					// 月销售目标
//					this.addFieldError("editTargetDate", getText("ECM00009",new String[]{"目标年月"}));
//				} else if(!CherryChecker.checkDate(form.getEditTargetDate(), CherryConstants.DATEYYYYMM)) {
//					// {0}格式错误，正确的格式为：{1}。
//					this.addFieldError("editTargetDate", getText("ESS00071",new String[]{"目标年月",CherryConstants.DATEYYYYMM}));
//				}
//			} else if("2".equals(targetDateType)) {
//				// 日销售目标
//				if(CherryChecker.isNullOrEmpty(form.getEditTargetDate(), true)) {
//					this.addFieldError("editTargetDate", getText("ECM00009",new String[]{"目标日期"}));
//				} else if(!CherryChecker.checkDate(form.getEditTargetDate(), CherryConstants.DATE_YYMMDD)) {
//					// {0}格式错误，正确的格式为：{1}。
//					this.addFieldError("editTargetDate", getText("ESS00071",new String[]{"目标日期",CherryConstants.DATE_YYMMDD}));
//				}
//			}
//		}
//	}
	
	/**
	 * 导入销售目标action方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importTarget() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 创建者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMOCIO07");
			// 作成程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO07");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			if (!CherryChecker.isNullOrEmpty(form.getImportBrandInfoId())) {
				map.put(CherryConstants.BRANDINFOID,
						form.getImportBrandInfoId());
			} else {
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE,
					session.get(CherryConstants.SESSION_LANGUAGE));
			// 上传的文件
			map.put("upExcel", upExcel);
			// 设置权限控制参数
			setPrivilegeParam(map);

			// 解析excel文件信息（并更新或插入相应销售目标），返回处理结果信息
			// 判断是月销售目标还是日销售目标是通过EXCEL内容来判定，固需要在BL中做处理
			Map<String, Object> infoMap = binOLMOCIO07_BL.resolveExcel(map);
			// 保存导入失败的销售目标的明细list
			List<Map<String, Object>> errorTargetLists = (List<Map<String, Object>>) infoMap
					.get("errorTargetList");
			setErrorInfoList(errorTargetLists);

			if (null != errorTargetLists) {
				// 存在导入失败的销售目标，在页面给出提示
				errorTargetList = (List<Map<String, Object>>) errorTargetLists;
				form.setMessage(getText(
						"EBS00055",
						new String[] {
								ConvertUtil.getString(infoMap.get("totalCount")),
								ConvertUtil.getString(infoMap
										.get("successCount")),
								ConvertUtil.getString(infoMap.get("failCount")) }));
			} else {
				// 导入成功(若返回globalAcctionResult页面会有JS错误)
				form.setMessage(getText(
						"EBS00055",
						new String[] {
								ConvertUtil.getString(infoMap.get("totalCount")),
								ConvertUtil.getString(infoMap
										.get("successCount")),
								ConvertUtil.getString(infoMap.get("failCount")) }));
				return "BINOLMOCIO07_3";
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage(),e);
			//销售目标数据sheet名不正确的情况
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLMOCIO07_2";
	}

	/**
	 * 设置错误结果的提示信息
	 * 
	 * @param errorInfo
	 * @param errorTargetLists
	 */
	private void setErrorInfoList(List<Map<String, Object>> errorTargetLists) {
		if (!CherryChecker.isNullOrEmpty(errorTargetLists)) {
			for (Map<String, Object> errorMap : errorTargetLists) {
				// 错误信息集合
				String errorInfoList = "";
				StringBuffer sb = new StringBuffer();
				if (errorMap.get("typeError") != null) {
					// 单位类型有误
					sb.append(getText("EMO00071",new String[]{getText("PMO00024")}) + ",");
				}
				if (errorMap.get("targetDateError") != null) {
					// 年月有误
					sb.append(getText("EMO00072") + ",");
				}
				if (errorMap.get("targetCodeError") != null) {
					// 代号有误
					sb.append(getText("EMO00073") + ",");
				}
				if (errorMap.get("targetNameError") != null) {
					// 名称有误
					sb.append(getText("EMO00074") + ",");
				}
				if (errorMap.get("targetCodeNameError") != null) {
					// 名称有误(名称不为code对应的名称)
					sb.append(getText("EMO00077") + ",");
				}
				if (errorMap.get("targetNameCodeError") != null) {
					// 代号有误（代号与名称不对应）
					sb.append(getText("EMO00078") + ",");
				}
				if (!CherryChecker.isNullOrEmpty(errorMap.get("targetTypeError"))) {
					// 目标类型有误
					sb.append(getText("EMO00071",new String[]{getText("PMO00025")}) + ",");
				}
				if (!CherryChecker.isNullOrEmpty(errorMap.get("activityNameNullError"))) {
					// {活动代号}非空时，{活动名称}必填
					sb.append(getText("EMO00089",new String[]{getText("PMO00027"),getText("PMO00026")}) + ",");
				}
				if (!CherryChecker.isNullOrEmpty(errorMap.get("activityCodeNameError"))) {
					// {活动代号}有误（不是{活动名称}对应的{活动代号}）
					sb.append(getText("EMO00087",new String[]{getText("PMO00027"),getText("PMO00026"),getText("PMO00027")}) + ",");
				}
				if (!CherryChecker.isNullOrEmpty(errorMap.get("activityNameExistError"))) {
					// 活动名称不存在
					sb.append(getText("EMO00088",new String[]{getText("PMO00026")}) + ",");
				}
				if (errorMap.get("targetMoneyError") != null) {
					// 金额有误
					sb.append(getText("EMO00075") + ",");
				}
				if (errorMap.get("targetQuantityError") != null) {
					// 数量有误
					sb.append(getText("EMO00076") + ",");
				}
				
				Iterator<Entry<String, Object>> it = errorMap.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, Object> en = it.next();
					String key = en.getKey();
					String value = ConvertUtil.getString(en.getValue());
					if(key.startsWith("targetQuantity") && key.endsWith("Error")) {
						// 第{0}列的数量有误
						sb.append(getText("EMO00106",new String[]{value})+",");
					} else if(key.startsWith("targetMoney") && key.endsWith("Error")) {
						// 第{0}列的金额有误
						sb.append(getText("EMO00105",new String[]{value})+",");
					}
				}
				if (errorMap.get("unknownError") != null) {
					// 未知错误(数据库更新或者插入异常)
					sb.append(getText("EMO00079") + ",");
				}
				errorInfoList = sb.toString();
				if (!"".equals(errorInfoList)) {
					// 将最后一个"，"去掉
					errorInfoList = sb.substring(0, sb.length() - 1);
					errorMap.put("errorInfoList", errorInfoList);
				}
			}
		}
	}

	/**
	 * 获取树节点
	 * @throws Exception
	 */
	public void getTreeNodes() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getAddBrandInfoId());
		// 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
		String targetDateType = ConvertUtil.getString(form.getTargetDateType());
		// 目标年月
		if("".equals(targetDateType) || "1".equals(targetDateType)) {
			map.put("targetDate", form.getAddTargetMonth());
		} else if("2".equals(targetDateType)) {
			map.put("targetDate", form.getAddTargetDay());
		}
		// 目标类型
		map.put("targetType", form.getTargetType());
		// 单位类型
		map.put("type", form.getAddtype());
//		// 活动CODE
//		map.put("campaignCode", form.getCampaignCode());
//		// 活动名称
//		map.put("campaignName", form.getCampaignName());
	    // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
	    //设置权限控制参数
        setPrivilegeParam(map);
        List<Map<String,Object>> list = null;
        if("".equals(targetDateType) || "1".equals(targetDateType)) {
        	list = binOLMOCIO07_BL.getTreeNodes(map);
        } else if("2".equals(targetDateType)) {
        	list = binOLMOCIO07_01_BL.getTreeNodes(map);
        }
		ConvertUtil.setResponseByAjax(response, list);
	}
	
	/**
	 * 下发[目标只下发产品类型的销售目标]
	 * 
	 * @return
	 * @throws Exception
	 */
	public String down() throws Exception {
		try{
			// 参数MAP
			Map<String, Object> map = getSearchMap();
			// 用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put("brandInfoId", form.getBrandInfoId());
			// 作成程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO07");	
			// 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        // 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
			String targetDateType = ConvertUtil.getString(map.get("targetDateType"));
			// 目标年月
			if("".equals(targetDateType) || "1".equals(targetDateType)) {
				binOLMOCIO07_BL.down(map);
			} else if("2".equals(targetDateType)) {
				binOLMOCIO07_01_BL.down(map);
			}
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	this.addActionError(ex.getMessage());
            	return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
		}
	}
	
	/**
	 * 销售目标设定数据导出校验
	 * @throws Exception
	 */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = this.getSearchMap();
		// 此参数用于区分是导出的数据是有设定销售目标的数据
        map.put("exportModel", "1");
        // 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
		String targetDateType = ConvertUtil.getString(map.get("targetDateType"));
		int count = 0;
		if("".equals(targetDateType) || "1".equals(targetDateType)) {
			count = binOLMOCIO07_BL.getSaleTargetCount(map);
		} else if("2".equals(targetDateType)) {
			count = binOLMOCIO07_01_BL.getSaleTargetCount(map);
		}
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		} else if(count == 0){
			// 需要导出的明细数据为空，不能导出！
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00099"));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 查询结构导出
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		try {
        	Map<String, Object> searchMap = this.getSearchMap();
        	// 销售目标日期类型（1：月目标；2：日目标；默认为月目标）
    		String targetDateType = ConvertUtil.getString(searchMap.get("targetDateType"));
    		
            Map<String, Object> exportMap = null;
            if("".equals(targetDateType) || "1".equals(targetDateType)) {
            	exportMap =	binOLMOCIO07_BL.getExportMap(searchMap);
            } else if("2".equals(targetDateType)) {
            	exportMap =	binOLMOCIO07_01_BL.getExportMap(searchMap);
            }
    		// 此参数用于区分是导出的数据是有设定销售目标的数据
            exportMap.put("exportModel", "1");
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            exportName = zipName + ".zip";
        	// EXCEL导出
            byte[] byteArray = null;
            if("".equals(targetDateType) || "1".equals(targetDateType)) {
        		byteArray = binOLCM37_BL.exportExcel(exportMap,binOLMOCIO07_BL);
            } else if("2".equals(targetDateType)) {
            	byteArray = binOLCM37_BL.exportExcel(exportMap,binOLMOCIO07_01_BL);
            }
        	excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00094"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	@Override
	public BINOLMOCIO07_Form getModel() {
		return form;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
	
	/**
	 * 设置权限参数
	 * @param map
	 */
	private void setPrivilegeParam(Map<String,Object> map){
	    //操作类型 查询
        map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
        //业务类型 销售
        map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE3);
	}
}
