package com.cherry.mo.cio.action;

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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.cio.form.BINOLMOCIO16_Form;
import com.cherry.mo.cio.form.BINOLMOCIO17_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO17_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO17_Action extends BaseAction implements ModelDriven<BINOLMOCIO17_Form> {

	private static final long serialVersionUID = 6707742057399280784L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO17_Action.class);
	
	private BINOLMOCIO17_Form form = new BINOLMOCIO17_Form();
	
	@Resource(name="binOLMOCIO17_BL")
	private BINOLMOCIO17_IF binOLMOCIO17_BL;
	@Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
    
    /**一个导入批次的柜台消息导入LIST*/
    private List<Map<String, Object>> counterMessageImportList;
    /**一条柜台消息的主数据*/
    private Map counterMessageImportInfo;
    /**一条柜台消息的明细数据*/
    private List<Map<String, Object>> counterMessageImportDetailList;
    
    /** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	/**
	 * 柜台消息导入查询画面
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	public String search() throws Exception {
		try {
			Map<String, Object> map = getComMap();
			Map<String, Object> formMap = (Map<String, Object>)Bean2Map.toHashMap(form);
			map.putAll(formMap);
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLMOCIO17_BL.getCntMsgImportCount(map);
			if(count != 0){
				setCounterMessageImportList(binOLMOCIO17_BL.getCntMsgImportList(map));
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 取得一条柜台消息的导入明细
	 * @return
	 * @throws Exception
	 */
	public String getCntMsgImportDetail() throws Exception {
		try {
			Map<String, Object> map = getComMap();
			map.put("counterMessageImportId", form.getCounterMessageImportId());
			counterMessageImportDetailList = binOLMOCIO17_BL.getCntMsgImportDetailList(map);	
			setCounterMessageImportInfo(binOLMOCIO17_BL.getCntMsgImportInfo(map));	
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 柜台消息导入明细EXCEL导出
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		 // 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getComMap();
        	Map<String, Object> formMap = (Map<String, Object>)Bean2Map.toHashMap(form);
        	searchMap.putAll(formMap);
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLMOCIO17", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            setExcelStream(new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLMOCIO17_BL.exportExcel(searchMap), fileName+".xls")));
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
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getComMap() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoId.equals("-9999")){
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
		}else{
			map.put(CherryConstants.BRANDINFOID,userInfo.getCurrentBrandInfoID());
		}
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}	

	@Override
	public BINOLMOCIO17_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getCounterMessageImportList() {
		return counterMessageImportList;
	}

	public void setCounterMessageImportList(List<Map<String, Object>> counterMessageImportList) {
		this.counterMessageImportList = counterMessageImportList;
	}

	public Map getCounterMessageImportInfo() {
		return counterMessageImportInfo;
	}

	public void setCounterMessageImportInfo(Map counterMessageImportInfo) {
		this.counterMessageImportInfo = counterMessageImportInfo;
	}

	public List<Map<String, Object>> getCounterMessageImportDetailList() {
		return counterMessageImportDetailList;
	}

	public void setCounterMessageImportDetailList(
			List<Map<String, Object>> counterMessageImportDetailList) {
		this.counterMessageImportDetailList = counterMessageImportDetailList;
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
