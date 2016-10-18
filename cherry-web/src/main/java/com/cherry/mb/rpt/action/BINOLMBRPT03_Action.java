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
import com.cherry.cm.cmbussiness.bl.BINOLCM13_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT03_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT03_Form;
import com.cherry.mb.rpt.service.BINOLMBRPT01_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员等级统计Action
 * 
 * @author WangCT
 * @version 1.0 2014/11/13
 */
public class BINOLMBRPT03_Action extends BaseAction implements ModelDriven<BINOLMBRPT03_Form> {

	private static final long serialVersionUID = -7474538192486834378L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT03_Action.class.getName());
	
	@Resource
	private BINOLCM13_BL binOLCM13_BL;
	
	@Resource
	private BINOLMBRPT03_BL binOLMBRPT03_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 会员销售报表Service **/
	@Resource
	private BINOLMBRPT01_Service binOLMBRPT01_Service;
	
	/**
     * 会员等级统计画面
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
		
		String sysDate = binOLMBRPT01_Service.getDateYMD();
		// 开始日期
		form.setSaleDateStart(sysDate);
		// 截止日期
		form.setSaleDateEnd(sysDate);
    	return SUCCESS;
    }
    
    /**
     * 会员等级统计画面
     */
    public String search() throws Exception {
    	
    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
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
		// 会员等级统计
		memLevelCountList = binOLMBRPT03_BL.getMemLevelCount(map);
    	return SUCCESS;
    }
    
    /**
     * 导出Excel
     */
    public String exportExcel() throws Exception {
        
        try {
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
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
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBRPT03_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBRPT03_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
    /** 渠道柜台信息Json **/
    private String channelCounterJson;
    
    /** 会员等级统计List **/
    private List<Map<String, Object>> memLevelCountList;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;

	public String getChannelCounterJson() {
		return channelCounterJson;
	}

	public void setChannelCounterJson(String channelCounterJson) {
		this.channelCounterJson = channelCounterJson;
	}

	public List<Map<String, Object>> getMemLevelCountList() {
		return memLevelCountList;
	}

	public void setMemLevelCountList(List<Map<String, Object>> memLevelCountList) {
		this.memLevelCountList = memLevelCountList;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员等级统计Form */
	private BINOLMBRPT03_Form form = new BINOLMBRPT03_Form();
	
	@Override
	public BINOLMBRPT03_Form getModel() {
		return form;
	}

}
