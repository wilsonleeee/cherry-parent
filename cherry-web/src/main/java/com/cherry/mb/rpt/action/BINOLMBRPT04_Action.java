package com.cherry.mb.rpt.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM13_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.form.BINOLMBRPT04_Form;
import com.cherry.mb.rpt.interfaces.BINOLMBRPT04_IF;
import com.cherry.mb.rpt.service.BINOLMBRPT04_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
* @ClassName: BINOLMBRPT04_Action 
* @Description: TODO(会员发展统计Action) 
* @author menghao
* @version v1.0.0 2015-1-5 
*
 */
public class BINOLMBRPT04_Action extends BaseAction implements
		ModelDriven<BINOLMBRPT04_Form> {

	private static final long serialVersionUID = 5202749569598044092L;

	private static Logger logger = LoggerFactory
			.getLogger(BINOLMBRPT04_Action.class.getName());

	@Resource(name = "binOLMBRPT04_BL")
	private BINOLMBRPT04_IF binOLMBRPT04_BL;
	
	@Resource(name="binOLCM13_BL")
	private BINOLCM13_BL binOLCM13_BL;
	
	@Resource(name="binOLMBRPT04_Service")
	private BINOLMBRPT04_Service binOLMBRPT04_Service;

	/** 导出共通BL **/
	@Resource(name = "binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/**会员发展统计一览*/
	private List<Map<String, Object>> memberDevelopRptList;
	
	 /** 渠道柜台信息Json **/
    private String channelCounterJson;

	/**
	 * 会员发展统计查询画面
	 */
	public String init() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
    	// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得渠道柜台信息List
		List<Map<String, Object>> channelCounterList = binOLCM13_BL.getChannelCounterList(map);
		channelCounterJson = CherryUtil.obj2Json(channelCounterList);
		
		String sysDate = binOLMBRPT04_Service.getDateYMD();
		// 开始日期
		form.setStartDate(sysDate);
		// 截止日期
		form.setEndDate(sysDate);
    	return SUCCESS;
	}

	/**
	 * 会员发展统计查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = this.getSearchMap();
			sumInfo = binOLMBRPT04_BL.getSumInfo(map);
			int count =  ConvertUtil.getInt(sumInfo.get("count"));
			if(count > 0) {
				// 会员发展统计
				memberDevelopRptList = binOLMBRPT04_BL.getMemberDevelopRptList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch (Exception e) {
        	logger.error(e.getMessage(), e);
        	// 查询出现异常，请重试
            this.addActionError(getText("ECM00018"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
		return SUCCESS;
	}
	
	/**
	 * 导出Excel
	 */
	public String exportExcel() throws Exception {
		try {
    		Map<String, Object> map = this.getSearchMap();
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBRPT04_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBRPT04_BL);
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
	private Map<String, Object> getSearchMap() throws Exception{
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, map);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
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

	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		// 转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员发展统计Form */
	private BINOLMBRPT04_Form form = new BINOLMBRPT04_Form();

	@Override
	public BINOLMBRPT04_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getMemberDevelopRptList() {
		return memberDevelopRptList;
	}

	public void setMemberDevelopRptList(
			List<Map<String, Object>> memberDevelopRptList) {
		this.memberDevelopRptList = memberDevelopRptList;
	}

	public String getChannelCounterJson() {
		return channelCounterJson;
	}

	public void setChannelCounterJson(String channelCounterJson) {
		this.channelCounterJson = channelCounterJson;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

}
