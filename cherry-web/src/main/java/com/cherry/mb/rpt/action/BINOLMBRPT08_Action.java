package com.cherry.mb.rpt.action;

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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT08_BL;
import com.cherry.mb.rpt.bl.BINOLMBRPT09_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT08_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 活动统计信息报表Action
 * 
 * @author WangCT
 * @version 1.0 2014/12/29
 */
public class BINOLMBRPT08_Action extends BaseAction implements ModelDriven<BINOLMBRPT08_Form> {

	private static final long serialVersionUID = -1135078800184964658L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT08_Action.class.getName());
	
	/** 活动统计信息报表BL **/
	@Resource
	private BINOLMBRPT08_BL binOLMBRPT08_BL;
	
	/** 按柜台统计活动信息报表BL **/
	@Resource
	private BINOLMBRPT09_BL binOLMBRPT09_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 活动统计信息画面初始化
	 * 
	 * @return 活动统计信息画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 取得活动List
		campaignList = binOLMBRPT09_BL.getCampaignList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 查询活动统计信息
	 * 
	 * @return 活动统计信息画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得活动统计信息
		camCountInfo = binOLMBRPT08_BL.getCamCountInfo(map);		
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
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBRPT08_BL.getExportParam(map);
    		
    		Map<String, Object> camCountInfo = binOLMBRPT08_BL.getCamCountInfo(map);
    		if(camCountInfo != null) {
    			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
    			dataList.add(camCountInfo);
    			exportMap.put("dataList", dataList);
    		}
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	private List<Map<String, Object>> campaignList;
	
	private Map camCountInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List<Map<String, Object>> campaignList) {
		this.campaignList = campaignList;
	}

	public Map getCamCountInfo() {
		return camCountInfo;
	}

	public void setCamCountInfo(Map camCountInfo) {
		this.camCountInfo = camCountInfo;
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

	/** 活动统计信息报表Form **/
	private BINOLMBRPT08_Form form = new BINOLMBRPT08_Form();

	@Override
	public BINOLMBRPT08_Form getModel() {
		return form;
	}

}
