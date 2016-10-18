package com.cherry.mb.mbm.action;

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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM19_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM19_Form;
import com.opensymphony.xwork2.ModelDriven;

/***
 * 会员档案导入Action
 * 
 * @author luohong
 * 
 */
public class BINOLMBMBM19_Action extends BaseAction implements
		ModelDriven<BINOLMBMBM19_Form> {
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM19_Action.class);
	/**
	 * 会员档案导入
	 */
	private static final long serialVersionUID = 1281935494264055673L;

	/**
	 * 会员档案导入FORM
	 */
	private BINOLMBMBM19_Form form = new BINOLMBMBM19_Form();

	/** 取得品牌共通 */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/**档案导入BL*/
	@Resource(name="binOLMBMBM19_BL")
	private BINOLMBMBM19_BL binOLMBMBM19_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	/**
	 * 品牌List
	 */
	private List<Map<String, Object>> brandInfoList;
	
	
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	/** 上传的文件 */
	private File upExcel;
	
	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
	/**
	 * 档案导入初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 共通参数MAP
		Map<String, Object> map = getCommonMap();
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		// 组织Id
		String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		// 品牌Id
		String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
		// 是否开启新增
		form.setAddMemflag(binOLCM14_BL.isConfigOpen("1112", orgId, brandId));		
		// 是否开启更新
		form.setUpdateMemflag(binOLCM14_BL.isConfigOpen("1113",  orgId, brandId));
		return SUCCESS;
	}
	/**
	 * 导入会员
	 * @throws Exception
	 */
	public void  importMember()throws Exception{
		logger.info("------- 开始会员导入！---------");
		Map<String, Object> map = getCommonMap();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			// 上传的文件
			map.put("upExcel", upExcel);
			// 导入原因
			map.put("reason",form.getReason());
			// 导入批次名称
			map.put("importName",form.getImportName());
			// 导入方式
			map.put("importType",form.getImportType());
			// 导入处理
			logger.info(" 开始处理 binOLMBMBM19_BL.ResolveExcel 方法");
			List<Map<String, Object>> resultList = binOLMBMBM19_BL.ResolveExcel(map);
			logger.info(" 开始处理 binOLMBMBM19_BL.tran_SaveImportMemberInfo 方法");
			Map<String, Object> infoMap = binOLMBMBM19_BL.tran_SaveImportMemberInfo(resultList,map);
			//分页取数据新增会员档案信息
			logger.info(" 开始处理 binOLMBMBM19_BL.saveMemberInfo 方法");
			binOLMBMBM19_BL.saveMemberInfo(infoMap);
			msgMap.put("suessMsg", getText("ICM00002"));
			msgMap.put("totalCount", resultList.size());
			msgMap.put("billNo", infoMap.get("billNo"));
			msgMap.put("memImportId", infoMap.get("memImportId"));
			msgMap.put("importType", form.getImportType());
		} catch (CherryException e) {
			logger.error(e.getMessage(), e);
			// 导入失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                msgMap.put("errorMsg", temp.getErrMessage());
            }else{
            	msgMap.put("errorMsg", getText("ECM00005"));
            }
		}
		logger.info("------- 会员导入完成！---------");
		ConvertUtil.setResponseByAjax(response, msgMap);
		
	}
	
	/**
	 * 会员档案导入共通Map
	 * 
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> getCommonMap() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")) {
			// 取得所属品牌
			map.put(CherryConstants.BRANDINFOID, brandInfoID);
		} else {
			map.put(CherryConstants.BRANDINFOID,
					userInfo.getCurrentBrandInfoID());
		}
		//品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		// 员工ID
		map.put("employeeId", userInfo.getBIN_EmployeeID());
		// 员工code
		map.put("employeeCode", userInfo.getEmployeeCode());
		// 操作员工
		map.put("modifyEmployee", userInfo.getEmployeeCode());
		// 组织code
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 品牌code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		map.put("CreatedBy", map.get(CherryConstants.USERID));
		map.put("CreatePGM", "BINOLMBMBM19");
		map.put("UpdatedBy", map.get(CherryConstants.USERID));
		map.put("UpdatePGM", "BINOLMBMBM19");
		return map;
	}

	@Override
	public BINOLMBMBM19_Form getModel() {
		return form;
	}

}
