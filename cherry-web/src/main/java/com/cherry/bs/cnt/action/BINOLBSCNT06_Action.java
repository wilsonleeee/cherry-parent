/*	
 * @(#)BINOLBSCNT06_Action.java     1.0 2011/05/09		
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
package com.cherry.bs.cnt.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.bs.cnt.bl.BINOLBSCNT06_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT06_Form;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 	柜台Excel导入处理Action
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT06_Action extends BaseAction implements ModelDriven<BINOLBSCNT06_Form>{

	private static final long serialVersionUID = -1231995997487296454L;
	
	private BINOLBSCNT06_Form form = new BINOLBSCNT06_Form();
	
	private static Logger logger = LoggerFactory.getLogger(BINOLBSCNT06_Action.class.getName());
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 柜台Excel导入处理BL */
	@Resource(name="binOLBSCNT06_BL")
	private BINOLBSCNT06_BL binOLBSCNT06_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	/** 共通 */
	@Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	private List errorCounterList;
	
	private List<Map<String, Object>> errorInfo;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {

		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			brandInfoId = userInfo.getBIN_BrandInfoID();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 导入柜台处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importCounter() throws Exception {
		
		try {
			// 参数MAP
			Map<String, Object> map = getSearchMap();
			// 上传的文件
			map.put("upExcel", upExcel);
			// 导入柜台处理
			Map<String, Object> infoMap = binOLBSCNT06_BL.ResolveExcel(map);
			
			// 柜台编号生成规则
			String cntCodeRule = binOLCM14_BL.getConfigValue("1139", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)));
			
			form.setCntCodeRule(cntCodeRule);
			
			Map<String,Object> statisticsInfo = (Map<String, Object>) infoMap.get("statisticsInfo");
			Object counterList = infoMap.get("errorCounterList");
			
			//取出错误信息List
			Object errorInfos =  infoMap.get("errorInfo");
			errorInfo = (List) errorInfos;
			
			 List<Map<String, Object>> errorCounterLists = (List)counterList;
			 if(errorCounterLists!=null){
				 errorList(errorInfo,errorCounterLists);
			 }

			if(null != counterList){
				errorCounterList = (List) errorCounterLists;
				form.setMessage(getText("EBS00055", new String[] {
						ConvertUtil.getString(statisticsInfo.get("totalCount")),
						ConvertUtil.getString(statisticsInfo.get("successCount")),
						ConvertUtil.getString(statisticsInfo.get("failCount"))}));
			}else{
				// 导入成功
				this.addActionMessage(getText("EBS00054", new String[] {
						ConvertUtil.getString(statisticsInfo.get("totalCount")),
						ConvertUtil.getString(statisticsInfo.get("successCount")),
						ConvertUtil.getString(statisticsInfo.get("failCount"))}));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		} catch (CherryException e) {
			logger.error(e.getMessage(),e);
			this.addActionMessage(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLBSCNT06_01";
	}

	public String editInit() throws Exception{
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			map.put("brandInfoId", brandInfoList.get(0).get("brandInfoId"));
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			brandInfoId = userInfo.getBIN_BrandInfoID();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
			map.put("brandInfoId", brandInfoId);
		}
		
		
		// 柜台编号生成规则
		String cntCodeRule = binOLCM14_BL.getConfigValue("1139", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(brandInfoId));
		
		//解析画面传出来的jsonStr格式
		String json = URLDecoder.decode(jsonStr, "utf-8");
		//将其转化为map
		counterInfo = CherryUtil.json2Map(json);
		String employeeName = ConvertUtil.getString(counterInfo.get("basName"));
		String employeeCode = ConvertUtil.getString(counterInfo.get("basCode"));
		if(!"".equals(employeeName)||!"".equals(employeeCode)){
			map.put("employeeName", employeeName);
			map.put("employeeCode", employeeCode);
			List<Map<String,Object>> employeeList = binOLBSCOM01_BL.getEmployeeInfoList(map);
			if(null!=employeeList && !employeeList.isEmpty()){
				for(Map<String,Object> employeeMap : employeeList){
					employeeMap.put("manageType", "1");
				}
			}else{
				Map<String,Object> employeeInfo = new HashMap<String,Object>();
				employeeInfo.put("manageType", "1");
				employeeInfo.put("employeeName",employeeName);
				employeeInfo.put("employeeId", -1);
				employeeList.add(employeeInfo);
			}
			counterInfo.put("employeeList", employeeList);
		}
		// 取得区域List
		reginList = binolcm00BL.getReginList(map);
		
		String counterCode = form.getCounterCode();
		
		form.setCounterCode(counterCode);
		
		return SUCCESS;
	}
	
	/**
	 * 保存编辑
	 * 
	 * */
	public String savaEdit() throws Exception{
		try{
			Map<String,Object> map = getSearchMap();
			//品牌ID
			if((Integer)map.get(CherryConstants.BRANDINFOID)==CherryConstants.BRAND_INFO_ID_VALUE){
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			}
			//柜台code
			map.put("counterCode", form.getCounterCode().trim());
			//柜台名称
			map.put("counterName", form.getCounterName().trim());
			//测试区分
			map.put("counterKind", form.getCounterKind());
			//大区
			map.put("_regionId", form.getRegionId());
			map.put("regionCode", form.getRegionCode().trim());
			map.put("regionName", form.getRegionName().trim());
			//省
			map.put("provinceId", form.getProvinceId());
			map.put("provinceName", form.getProvinceName().trim());
			map.put("provinceCode", form.getProvinceCode().trim());
			//市
			map.put("cityId", form.getCityId());
			map.put("cityCode", form.getCityCode().trim());
			map.put("cityName", form.getCityName().trim());
			//英文名称
			map.put("foreignName", form.getForeignName().trim());
			//柜台分类
			map.put("counterCategory", form.getCounterCategory().trim());
			//经销商code
			map.put("resellerCode", form.getResellerCode().trim());
			//经销商名称
			map.put("resellerName", form.getResellerName().trim());
			//商场名称
			map.put("mallName", form.getMallName().trim());
			// 柜台员工数
			map.put("employeeNum", form.getEmployeeNum().trim());
			//柜台面积
			map.put("counterSpace", form.getCounterSpace().trim());
			//地址
			map.put("address", form.getAddress().trim());
			//柜台主管
			map.put("employeeId", form.getCounterHead());
			//渠道
			map.put("channeName", form.getChanneName().trim());
			
			//柜台电话
			map.put("counterTelephone", form.getCounterTelephone().trim());
			//经度
			map.put("longitude", form.getLongitude().trim());
			//纬度
			map.put("latitude", form.getLatitude().trim());
			//是否有POS机
			map.put("posFlag", form.getPosFlag().trim());
			//柜台类型
			map.put("managingType2", form.getManagingType2().trim());
			//银联设备号
			map.put("equipmentCode", form.getEquipmentCode().trim());
			//调用BL进行处理
			binOLBSCNT06_BL.tran_saveEdit(map);
			
			this.addActionMessage(getText("ICM00001"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			
		}catch(Exception e){
			if(e instanceof CherryException){
				logger.error(((CherryException)e).getErrMessage(),e);
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				logger.error(e.getMessage(),e);
				this.addActionError(e.getMessage());
			}
			
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 是否是品牌帐号
		map.put("isBrandUser", isBrandUser(userInfo));
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSCNT06");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT06");
		// 组织代号
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		//登录名
		map.put("loginName", userInfo.getLoginName());
		return map;
	}
	
	/**
	 * 导入错误信息筛选
	 * 
	 * @return
	 */
	private void errorList(List<Map<String, Object>> errorInfo,List<Map<String, Object>> errorCounterLists) {
		
		// 数据行号
		String rowNo ="";
		// 有错误的数据行号
		String errorRowNo ="";

		for(int i = 0;i<errorInfo.size();i++){
			//错误信息集合
			String errorInfoList = "";
			
			rowNo =errorInfo.get(i).get("rowNo").toString();
			
			if(errorInfo.get(i).get("counterCodeError")!=null){
				//标志柜台code有错误
				errorInfoList += getText("EBS00076")+",";
			}
			
			if(errorInfo.get(i).get("cityNameNoClearError")!=null){
				// 不能清空城市，否则无法生成柜台编号
				errorInfoList += getText("EBS00119")+",";
			}
			
			if(errorInfo.get(i).get("cityTeleCodeError")!=null){
				//城市区号没有，请先添加城市区号
				errorInfoList += getText("EBS00115")+",";
			}
			
			if(errorInfo.get(i).get("cityNoExistsError")!=null){
				// 城市不存在，无法生成柜台编号
				errorInfoList += getText("EBS00116")+",";
			}
			
			if(errorInfo.get(i).get("cityNameSoLongError")!=null){
				// 城市名称超长，无法生成柜台编号
				errorInfoList += getText("EBS00117")+",";
			}
			
			if(errorInfo.get(i).get("cityNameNoExistsError")!=null){
				// 提示必须有城市名称，柜台编号才能生成
				errorInfoList += getText("EBS00118")+",";
			}
			
			if(errorInfo.get(i).get("brandCodeError")!=null){
				//标志品牌代码有错误
				errorInfoList += getText("EBS00077")+",";
			}
			
			if(errorInfo.get(i).get("counterNameError")!=null){
				//标志柜台名称有错误
				errorInfoList += getText("EBS00078")+",";
			}
			
			if(errorInfo.get(i).get("counterKindError")!=null){
				//标志柜台测试区分有错误
				errorInfoList += getText("EBS00079")+",";
			}
			
			if(errorInfo.get(i).get("basCodeError")!=null || errorInfo.get(i).get("basNameError")!=null){
				//标志柜台主管code有错误或者柜台主管名称有错误
				errorInfoList += getText("EBS00080")+",";
			}

			if(errorInfo.get(i).get("regionNameError")!=null){
				//标志区域名称有错误
				errorInfoList += getText("EBS00081",new String[]{getText("PBS00072")})+",";
			}
			
//			if(errorInfo.get(i).get("regionCodeError")!=null){
//				//标志区域code有错误
//				errorInfoList += getText("EBS00081",new String[]{"code"})+",";
//			}
			
			if(errorInfo.get(i).get("provinceNameError")!=null){
				//标志省名称有错误
				errorInfoList += getText("EBS00082",new String[]{getText("PBS00072")})+",";
			}
			
//			if(errorInfo.get(i).get("provinceCodeError")!=null){
//				//标志省code有错误
//				errorInfoList += getText("EBS00082",new String[]{"code"})+",";
//			}

			if(errorInfo.get(i).get("cityNameError")!=null){
				//标志省城市名称有错误
				errorInfoList += getText("EBS00083",new String[]{getText("PBS00072")})+",";
			}
			
//			if(errorInfo.get(i).get("cityCodeError")!=null){
//				//标志省城市code有错误
//				errorInfoList += getText("EBS00083",new String[]{"code"})+",";
//			}
			if(errorInfo.get(i).get("compareProviceByCityError") != null){
				// 导入的省份与通过城市在系统查询出来的省份不一致
				errorInfoList += getText("EBS00097",new String[]{getText("PBS00074"),getText("PBS00075")})+",";
			}
			if(errorInfo.get(i).get("compareRegionByCityError") != null){
				// 导入的区域与通过城市在系统查询出来的区域不一致
				errorInfoList += getText("EBS00097",new String[]{getText("PBS00073"),getText("PBS00075")})+",";
			}
			if(errorInfo.get(i).get("compareRegionByProvError") != null){
				// 导入的区域与通过省份在系统查询出来的区域不一致
				errorInfoList += getText("EBS00097",new String[]{getText("PBS00073"),getText("PBS00074")})+",";
			}

			if(errorInfo.get(i).get("belongFactionNameError")!=null) {
				// 柜台所属系统名称不存在
				errorInfoList += getText("EBS00122") + ",";
			}
			
			if(errorInfo.get(i).get("counterLevelError")!=null) {
				// 柜台级别不存在
				errorInfoList += getText("EBS00123") + ",";
			}
			
			if(errorInfo.get(i).get("statusError")!=null) {
				// 柜台状态不存在
				errorInfoList += getText("EBS00124") + ",";
			}
			
			if(errorInfo.get(i).get("channelNameError")!=null){
				//标志渠道名称有错误
				errorInfoList += getText("EBS00084")+",";
			}
			
			if(errorInfo.get(i).get("resellerNameError")!=null || errorInfo.get(i).get("resellerCodeError")!=null){
				//标志经销商名称有错误
				errorInfoList += getText("EBS00085")+",";
			}
			
			if(errorInfo.get(i).get("counterCategoryError")!=null){
				//标志经柜台分类有错
				errorInfoList += getText("EBS00086")+",";
			}
			
			if(errorInfo.get(i).get("mallNameError")!=null){
				//标志商场名称有错误
				errorInfoList += getText("EBS00087")+",";
			}
			
			if(errorInfo.get(i).get("foreignNameError")!=null){
				//标志英文名称有错误
				errorInfoList += getText("EBS00088")+",";
			}

			if(errorInfo.get(i).get("addressError")!=null){
				//标志柜台地址有错误
				errorInfoList += getText("EBS00089")+",";
			}

			if(errorInfo.get(i).get("employeeNumError")!=null){
				//标志柜台员工数有错误
				errorInfoList += getText("EBS00101",new String[]{getText("PBS00094")})+",";
			}
			
			if(errorInfo.get(i).get("counterSpaceError")!=null){
				//标志柜台面积有错误
				errorInfoList += getText("EBS00090")+",";
			}
			
			if(errorInfo.get(i).get("counterTelephoneError")!=null){
				//标志柜台面积有错误
				errorInfoList += getText("EBS00105")+",";
			}
			if(errorInfo.get(i).get("longitudeError")!=null){
				//经度有错误
				errorInfoList += getText("EBS00106")+",";
			}
			if(errorInfo.get(i).get("latitudeError")!=null){
				//纬度有错误
				errorInfoList += getText("EBS00107")+",";
			}
			if(errorInfo.get(i).get("posFlagError")!=null){
				//是否有POS机有错误
				errorInfoList += getText("EBS00101",new String[]{getText("PBS00095")})+",";
			}
			if(errorInfo.get(i).get("busDistrictError")!=null){
				//标志商圈有错误
				errorInfoList += getText("EBS00101",new String[]{getText("PBS00096")})+",";
			}
			if(errorInfo.get(i).get("equipmentCodeError")!=null){
				//银联设备号有错误
				errorInfoList += getText("EBS00101",new String[]{getText("PBS00099")})+",";
			}
			if(errorInfo.get(i).get("managingType2Error")!=null){
				//柜台类型
				errorInfoList += getText("EBS00101",new String[]{getText("PBS00100")})+",";
			}
			for(int j = 0;j<errorCounterLists.size();j++){
				Map<String, Object> map = new HashMap<String, Object>();
				errorRowNo =errorCounterLists.get(j).get("rowNo").toString();
				map = errorCounterLists.get(j);

				if(rowNo.equals(errorRowNo)){
					map.put("errorInfoList",errorInfoList.length() > 0 ? errorInfoList.subSequence(0, errorInfoList.length() - 1):errorInfoList);
				}

			}

		}

	}
	
	
	/**
     * 导出Excel
     * @author ZhaoChaoFan 6.5
     * @throws JSONException 
     */
    public String export() throws JSONException{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得考勤信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLBSCNT01", language, "downloadFileName2");
            setExcelStream(new ByteArrayInputStream(binOLBSCNT06_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return "BINOLBSCNT06_excel";
    }
	
	
	private boolean isBrandUser(UserInfo userInfo){
		return userInfo.getBIN_BrandInfoID()==-9999? false:true;
	}
	
	/** 编辑的柜台json字符串*/
	private String jsonStr;
	
	/** 返回编辑的柜台信息*/
	private Map counterInfo;
	
	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;

	/** 产品品牌ID */
	private int brandInfoId;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;

	public List<Map<String, Object>> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<Map<String, Object>> errorInfo) {
		this.errorInfo = errorInfo;
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

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getErrorCounterList() {
		return errorCounterList;
	}

	public void setErrorCounterList(List errorCounterList) {
		this.errorCounterList = errorCounterList;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public Map getCounterInfo() {
		return counterInfo;
	}

	public void setCounterInfo(Map counterInfo) {
		this.counterInfo = counterInfo;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
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
	
	@Override
	public BINOLBSCNT06_Form getModel() {
		return form;
	}

	
}
