package com.cherry.pt.rps.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.rps.bl.BINOLPTRPS36_BL;
import com.cherry.pt.rps.form.BINOLPTRPS36_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @ClassName: BINOLPTRPS36_Action 
 * @Description: TODO(柜台月度人效明细Action) 
 * @author menghao
 * @version v1.0.0 2015-1-13 
 *
 */
public class BINOLPTRPS36_Action extends BaseAction 
	implements ModelDriven<BINOLPTRPS36_Form> {

	private static final long serialVersionUID = 1975571425270592510L;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINOLPTRPS36_Action.class.getName());
	
	private BINOLPTRPS36_Form form = new BINOLPTRPS36_Form();
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLPTRPS36_BL")
	private BINOLPTRPS36_BL binOLPTRPS36_BL;
	
	/** 导出共通BL **/
	@Resource(name = "binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	private List<Map<String, Object>> brandInfoList;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    private List<Map<String, Object>> monthPeopleEffectList;
	
	/**
	 * 页面初始化
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		map.put(CherryConstants.SESSION_LANGUAGE, language);
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
		//目标日期
		form.setMonth(CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		return SUCCESS;
	}
	
	/**
	 * 查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> map = this.getSearchMap();
		int count = binOLPTRPS36_BL.getMonthPeopleEffectCount(map);
		if(count > 0) {
			monthPeopleEffectList = binOLPTRPS36_BL.getMonthPeopleEffectList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLPTRPS36_1";
	}
	
	/**
	 * 导出Excel
	 */
	public String export() throws Exception {
		try {
    		Map<String, Object> map = this.getSearchMap();
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLPTRPS36_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLPTRPS36_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
	}
	
	/**
	 * 查询参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSearchMap() throws Exception{
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, map);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if(userInfo.getBIN_BrandInfoID() == -9999) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		 // 语言类型
        String language = (String) session
                .get(CherryConstants.SESSION_LANGUAGE);
		// 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		return map;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLPTRPS36_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getMonthPeopleEffectList() {
		return monthPeopleEffectList;
	}

	public void setMonthPeopleEffectList(List<Map<String, Object>> monthPeopleEffectList) {
		this.monthPeopleEffectList = monthPeopleEffectList;
	}

}
