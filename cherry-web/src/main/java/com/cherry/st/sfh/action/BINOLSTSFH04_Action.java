/*  
 * @(#)BINOLSTSFH04_Action.java     1.0 2011/09/14      
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
package com.cherry.st.sfh.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.sfh.form.BINOLSTSFH04_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH04_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品发货单一览Action
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public class BINOLSTSFH04_Action extends BaseAction implements
ModelDriven<BINOLSTSFH04_Form>{
	
    private static final long serialVersionUID = 8775571001093572590L;
    
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH04_Action.class);

    @Resource
	private BINOLSTSFH04_IF binOLSTSFH04_BL;
	
	@Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
    @Resource
    private BINOLCM37_BL binOLCM37_BL;
	
	/** 参数FORM */
	private BINOLSTSFH04_Form form = new BINOLSTSFH04_Form();
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws JSONException {
		/**
		 * 修改，注释掉开始日期和结束日期。对应的JIRA票：NEWWITPOS-1137
		 * 毛戈平：发货单处理:查询条件,默认发货日期不要
		 * 
		 * @author zhanggl
		 * @version Cherry-1.0.0.v20111229 2011-12-29
		 * 
		 * */
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 开始日期
//		form.setStartDate(binOLCM00BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
//		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 产品发货单一览
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap= getSearchMap();
		Map<String, Object> sumInfo = binOLSTSFH04_BL.getSumInfo(searchMap);
		// 取得总数
		int count = CherryUtil.obj2int(sumInfo.get("count"));
		if (count > 0) {
			// 取得发货单List
			form.setProductDeliverList(binOLSTSFH04_BL.searchProductDeliverList(searchMap));
        }
		 form.setSumInfo(sumInfo);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTSFH04_1";
	}	
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
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
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
	    // 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		//发货单号
		map.put("deliverNo", form.getDeliverNo());
		//关联单号
		map.put("relevanceNo", form.getRelevanceNo());
		// 导入批次
		map.put("importBatch", form.getImportBatch());
		//发货部门
		map.put("outOrgan", form.getOutOrgan());
        //收货部门
        map.put("inOrgan", form.getInOrgan());
		//审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
		//订单状态
		map.put("tradeStatus",form.getTradeStatus());
	    //产品厂商ID
        map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
        //产品名称
        map.put("productName", form.getProductName());
        //部门
        map.put("inOrganizationId", form.getInOrganizationId());
        //部门联动条 查询 发货部门/收货部门 标志
        map.put("departInOutFlag", form.getDepartInOutFlag());
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        //部门联动条标记
        map.put("linkageDepartFlag", linkageDepartFlag(map));
        
		return map;
	}
	
	/**
	 * 判断部门联动条查询参数是否为空
	 * 
	 * @return "0":查询参数为空  "1":查询参数不为空
	 */
	private String linkageDepartFlag(Map<String, Object> map) {
		String flag = "0";
		if (map.containsKey("departId") || map.containsKey("channelId")
				|| map.containsKey("departType") || map.containsKey("regionId")
				|| map.containsKey("provinceId") || map.containsKey("cityId")
				|| map.containsKey("countyId")) {
			// 查询参数不为空
			flag = "1";
		}
		return flag;
	}
	
	/**
	 * 查询结果Excel导出
	 * @return
	 */
	public String export() {
		List<String> billIdList = new ArrayList<String>();
        // 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getSearchMap();
        	/**
        	 * 导出允许选择指定的单据进行导出，不勾选默认查询结果全部导出
        	 */
        	String[] billIdStr = form.getBillId();
        	if(billIdStr != null && billIdStr.length > 0){
        		for(String billId : billIdStr) {
            		billIdList.add(billId);
            	}
            	searchMap.put("billIdList", billIdList);
        	}
        	
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH04", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTSFH04_BL.exportExcel(searchMap), fileName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00022"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
//		if(CherryChecker.isNullOrEmpty(startDate)){
//			// 开始日期不能为空
//			this.addActionError(getText("ECM00009",new String[]{getText("PCM00001")}));
//			isCorrect = false;
//		}
//		if(CherryChecker.isNullOrEmpty(endDate)){
//			// 结束日期不能为空
//			this.addActionError(getText("ECM00009",new String[]{getText("PCM00002")}));
//			isCorrect = false;
//		}
		
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
	
	
	@Override
	public BINOLSTSFH04_Form getModel() {
		return form;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

}
