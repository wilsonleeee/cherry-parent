/*	
 * @(#)BINOLPTJCS04_Action.java     1.0 2011/03/28		
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
package com.cherry.pt.jcs.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS24_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS04_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS17_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS24_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品查询Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.03.28
 */
public class BINOLPTJCS24_Action extends BaseAction implements
		ModelDriven<BINOLPTJCS24_Form> {
			
	private static final long serialVersionUID = 7052727878284081396L;

	/** 参数FORM */
	private BINOLPTJCS24_Form form = new BINOLPTJCS24_Form();
	
	/** 导出excel共通BL **/
    @Resource
    private BINOLCM37_BL binOLCM37_BL;

    /** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLPTJCS24_IF")
	private BINOLPTJCS24_IF binOLPTJCS24_IF;
	
	@Resource(name="binOLPTJCS17_IF")
	private BINOLPTJCS17_IF binOLPTJCS17_IF;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Resource(name="binOLPTJCS04_IF")
	private BINOLPTJCS04_IF binolptjcs04_IF;

	/** 产品List */
	private List<Map<String, Object>> proList;

	/** 所管辖的品牌List */
	private List<Map<String, Object>> brandInfoList;

    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    /** 产品编码条码信息 */
    private Map ubMap;
    
    /** 产品编码条码启用 */
    private Map enableMap;
    
    /** 存在于促销品的重复条码 */
    private Map extistBarcodeMap = new HashMap();
    
	/** 是否一品多码 */
	private boolean isU2M = false;
    
	@Override
	public BINOLPTJCS24_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getProList() {
		return proList;
	}

	public void setProList(List<Map<String, Object>> proList) {
		this.proList = proList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public Map getUbMap() {
		return ubMap;
	}

	public void setUbMap(Map ubMap) {
		this.ubMap = ubMap;
	}
	
	public boolean getIsU2M() {
		return isU2M;
	}

	public void setU2M(boolean isU2M) {
		this.isU2M = isU2M;
	}
	
	public Map getEnableMap() {
		return enableMap;
	}

	public void setEnableMap(Map enableMap) {
		this.enableMap = enableMap;
	}
	
	public Map getExtistBarcodeMap() {
		return extistBarcodeMap;
	}

	public void setExtistBarcodeMap(Map extistBarcodeMap) {
		this.extistBarcodeMap = extistBarcodeMap;
	}

	/**
	 * 画面初期显示
	 * 
	 * @param
	 * @return
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	/**
	 * 产品一览（树模式）
	 * @param
	 * @return
	 */
	public String treeInit() {
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得某一类别的直属下级类别
	 * 
	 * @return
	 */
	public void next() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, 
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 只查询有效产品对应的产品分类数据
		map.put("validProduct", "validProduct");
		
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 类别节点位置
		map.put(CherryConstants.PATH, form.getPath());
		// 取得产品分类信息
		String categoryTree = binOLPTJCS24_IF.getCategoryList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, categoryTree);
	}
	
	/**
	 * 查询定位到的部门的所有上级部门位置
	 * 
	 */
	public String locateCat() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put("locationPosition", form.getLocationPosition());
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		
		// 取得产品分类的上级分类
		String locationHigher = binOLPTJCS24_IF.getLocateCatHigher(map);
		
		ConvertUtil.setResponseByAjax(response, locationHigher);
		return null;
	}
	
	/**
	 * 产品一览（列表模式）
	 * 
	 * @return
	 */
	public String listInit() throws Exception {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		form.setBrandInfoId(String.valueOf(userInfo.getBIN_BrandInfoID()));
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得品牌List
		brandInfoList = queryBrandList(map);
		//是否一品多码
		isU2M = binOLCM14_BL.isConfigOpen("1077",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));

		return SUCCESS;
	}

	/**
	 * <p>
	 * AJAX产品查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 添加第二排序:有效区分
		String sortValue = searchMap.get(CherryConstants.SORT_ID).toString();
		sortValue += ", validFlag desc";
		searchMap.put(CherryConstants.SORT_ID, sortValue);
		
		// 取得产品总数
		int count = binOLPTJCS24_IF.getProCount(searchMap);
		if (count > 0) {
			// 取得产品信息List
			proList = binOLPTJCS24_IF.getProList(searchMap);
		}
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * 查看相同产品是否存在有效数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getPrtDetail() throws Exception{
		Map<String, Object> map = getSearchMap();
		Map<String, Object> vfMap = new HashMap<String, Object>();
		vfMap.putAll(map);
		vfMap.put("productId", form.getProductId());
		vfMap.put("enableBarCode", form.getBarCode());
		vfMap.remove("validFlag");
		vfMap.remove("unitCode");
		// 当前启用的条码[barcode]已经是有效条码，无需再次启用
		enableMap = binOLPTJCS24_IF.getPrtBarCodeVF(vfMap);
		if(null == enableMap || enableMap.isEmpty()){
			
			map.put("productId", form.getProductId());
			map.put("validFlag", form.getValidFlag());
			
			ubMap = binOLPTJCS24_IF.getPrtDetailInfo(map);
			// 当前产品已存在有效的编码条码[oldbarcode]继续启用当前无效编码条码[newbarcode],则会将原有效编码条码[oldbarcode]停用，是否继续？
			if(null != ubMap){
				ubMap.put("invalidUnitCode", form.getUnitCode());
				ubMap.put("invalidBarCode", form.getBarCode());
			}
			
		} else{
			ubMap = null;
		}
		// 当前启用的条码[barcode]在促销品中已存在，无法启用！
		// 查询促销品中是否存在当前需要添加的barCode
		List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(vfMap);
		if(promPrtIdList.size() != 0){
			extistBarcodeMap.put("barCode", form.getBarCode());
		}
		
		return SUCCESS;
		
	}
	
	/**
	 * 取得同产品下有效或无效的厂商信息()
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getPrtBarCodeVF() throws Exception{
		Map<String, Object> map = getSearchMap();
		map.put("productId", form.getProductId());
		map.put("enableBarCode", form.getBarCode());
		
		enableMap = binOLPTJCS24_IF.getPrtBarCodeVF(map);
		
		return SUCCESS;
		
	}
	
	/**
	 * 停用或启用
	 * @return
	 * @throws Exception
	 */
	public String disOrEnable() throws Exception {
		Map<String, Object> map = getSearchMap();
		map.put("productId", form.getProductId());
		map.put("prtUpdateTime", form.getPrtUpdateTime());
		map.put("prtModifyCount", form.getPrtModifyCount());
		map.put("unitCode", form.getUnitCode());
		map.put("barCode", form.getBarCode());
		map.put("invlidUBFlag", form.getInvlidUBFlag());
		map.put("prtVendorId", form.getPrtVendorId());
		//是否一品多码
		isU2M = binOLCM14_BL.isConfigOpen("1077",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		map.put("isU2M", isU2M);

		//validFlag=0&productId=40675&updateTime=&modifyCount=&csrftoken=581dad2be3aa42e390d61675fc27be57
		binOLPTJCS24_IF.tran_disOrEnable(map);
		
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 
	 * 批量停用产品处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String delProductInfo() throws Exception {
		try{
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			//所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			//语言
			map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
			// 组织代号
			map.put("orgCode", userInfo.getOrganizationInfoCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLPTJCS04");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS04");
			//登录名
			map.put("loginName", userInfo.getLoginName());
			
			// 业务日期
			String businessDate = binOLPTJCS24_IF.getBussinessDate(map);
			map.put("businessDate",businessDate);
			// 停用时间
			String closingTime = CherryUtil.suffixDate(businessDate, 1);
			map.put("closingTime",closingTime);
			
			// 停用产品处理
			binOLPTJCS24_IF.tran_delProductInfo(map);
			this.addActionMessage(getText("ICM00002"));
		}catch(Exception e){
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 产品实时下发
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void issuePrt() throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 操作者
		map.put("EmployeeId", userInfo.getBIN_EmployeeID() + "");
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// BrandCode
		map.put("brandCode", userInfo.getBrandCode());
		//语言
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		// 组织代号
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS04");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS04");
		
		
		// 业务日期
		String businessDate = binOLPTJCS24_IF.getBussinessDate(map);
		map.put("businessDate", businessDate);
		// 当天业务结束，下发业务日期下一天的价格，否则下发当天价格
		map.put("priceDate", businessDate);
		// 启用日时()
//		map.put(ProductConstants.STARTTIME, startTime);
		// 停用日时
//		map.put(ProductConstants.CLOSINGTIME, closingTime);
		// 新产品生效日期
//		String enable_time = DateUtil.suffixDate(businessDate, 1);
//		map.put("enable_time", enable_time);
		
		// 实时下发
		try{
			/*// 柜台产品下发
			resultMap = binOLPTJCS17_IF.tran_issuedCntPrt(map);
			String result = ConvertUtil.getString(resultMap.get("result"));
			// 产品实时下发
			resultMap = binOLPTJCS24_IF.tran_issuedPrt(map);
			ConvertUtil.setResponseByAjax(response, resultMap);*/

			// 品牌是否支持产品下发
			boolean isPrtIss = binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if(!isPrtIss){
				resultMap.put("result", "2"); // 品牌的系统配置项不支持产品下发功能，请联系管理员！
				//logger.error("********* BINOLPTJCS04品牌的系统配置项不支持产品下发功能，请联系管理员！*********");
			} else {
				resultMap = binolptjcs04_IF.tran_issuedPrtByWS(map);
			}

			ConvertUtil.setResponseByAjax(response, resultMap);
		} catch(Exception e){
			resultMap.put("result", "1");
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
	}

	/**
	 * 共通参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 厂商编码
		map.put(CherryConstants.UNITCODE, form.getUnitCode());
		// 条码
		map.put(CherryConstants.BARCODE, form.getBarCode());
		// 条码
		map.put(ProductConstants.MODE, form.getMode());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		// 产品状态
		map.put("status", form.getStatus());
		// path
		map.put(CherryConstants.PATH, form.getPath());
		if(!CherryChecker.isNullOrEmpty(form.getPath())){
			String[] path = form.getPath().split(CherryConstants.SLASH);
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, path[0]);
		}
		// 业务日期
		String businessDate = binOLPTJCS24_IF.getBussinessDate(map);
		map.put("businessDate",businessDate);
		// 停用时间
//		String closingTime = CherryUtil.suffixDate(businessDate, 1);
//		map.put("closingTime",closingTime);
		
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS24");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS24");
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}
	
	/**
	 * 取得品牌List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> queryBrandList(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 登陆用户不为总部员工
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			brandMap.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
			list.add(brandMap);
		} else {
			// 取得所属品牌List
			list = binOLCM05_BL.getBrandInfoList(map);
		}
		if(null != list && list.size() > 0){
			// 页面初始化时,默认品牌
			form.setBrandInfoId(ConvertUtil.getString(list.get(0).get(CherryConstants.BRANDINFOID)));
		}
		return list;
	}

    /**
     * 导出Excel
     * @throws JSONException 
     */
    public String export() throws JSONException{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得产品信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            searchMap.put("language", language);
            String zipName = binOLMOCOM01_BL.getResourceValue("BINOLPTJCS04", language, "downloadFileName");
            downloadFileName = zipName + ".zip";
            byte[] byteArray = binOLPTJCS24_IF.exportExcel(searchMap);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName + ".xls"));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLPTJCS04_excel";
    }
	
    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
        //转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
}
