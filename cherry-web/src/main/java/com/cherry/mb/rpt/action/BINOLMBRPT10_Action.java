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
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT09_BL;
import com.cherry.mb.rpt.bl.BINOLMBRPT10_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT10_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 回柜会员销售明细报表Action
 * 
 * @author WangCT
 * @version 1.0 2015/01/09
 */
public class BINOLMBRPT10_Action extends BaseAction implements ModelDriven<BINOLMBRPT10_Form> {

	private static final long serialVersionUID = 620264702045267370L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT10_Action.class.getName());
	
	/** 回柜会员销售明细报表BL **/
	@Resource
	private BINOLMBRPT10_BL binOLMBRPT10_BL;
	
	/** 按柜台统计活动信息报表BL **/
	@Resource
	private BINOLMBRPT09_BL binOLMBRPT09_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 销售明细画面初始化
	 * 
	 * @return 销售明细画面
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
	 * 查询销售明细
	 * 
	 * @return 销售明细画面
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
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		int count = 0;
		saleDetailCountInfo = binOLMBRPT10_BL.getSaleDetailCountInfo(map);
		if(saleDetailCountInfo != null) {
			count = (Integer)saleDetailCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			saleDetailList = binOLMBRPT10_BL.getSaleDetailList(map);
		}
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
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBRPT10_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBRPT10_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	private List<Map<String, Object>> campaignList;
	
	private List<Map<String, Object>> saleDetailList;
	
	private Map saleDetailCountInfo;
	
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

	public List<Map<String, Object>> getSaleDetailList() {
		return saleDetailList;
	}

	public void setSaleDetailList(List<Map<String, Object>> saleDetailList) {
		this.saleDetailList = saleDetailList;
	}

	public Map getSaleDetailCountInfo() {
		return saleDetailCountInfo;
	}

	public void setSaleDetailCountInfo(Map saleDetailCountInfo) {
		this.saleDetailCountInfo = saleDetailCountInfo;
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

	/** 回柜会员销售明细报表Form **/
	private BINOLMBRPT10_Form form = new BINOLMBRPT10_Form();

	@Override
	public BINOLMBRPT10_Form getModel() {
		return form;
	}

}
