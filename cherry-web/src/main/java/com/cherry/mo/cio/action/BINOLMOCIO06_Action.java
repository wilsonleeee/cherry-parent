package com.cherry.mo.cio.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.form.BINOLMOCIO06_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO05_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO06_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO06_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO06_Form> {
	
	private static Logger logger = LoggerFactory
			.getLogger(BINOLMOCIO06_Action.class.getName());
	
	private BINOLMOCIO06_Form form = new BINOLMOCIO06_Form();
	
	private static final long serialVersionUID = 1L;

	@Resource(name="binOLMOCIO06_BL")
	private BINOLMOCIO06_IF binOLMOCIO06_BL;
	
	@Resource(name="binOLMOCIO05_BL")
	private BINOLMOCIO05_IF binOLMOCIO05_BL;
	
	private List<Map<String, Object>> brandInfoList;
	
	// 申明Map，用以存放查询出的问卷的信息
	private Map<String, Object> paperMap;
	
	/** 上传的文件 */
	private File upExcel;
	
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paperId", form.getPaperId());
		// 调用BL获取问卷信息
		paperMap = binOLMOCIO05_BL.getPaperForShow(map);
		String isIssued = form.getIsIssued();
		String controlFlag = "1";
		if(isIssued.equals("1")){
//			paperMap.put("controlFlag", "0");
			controlFlag = binOLMOCIO06_BL.getControlFlag(map);
		}
		paperMap.put("isIssued", isIssued);
		form.setIssuedType(controlFlag);
		return SUCCESS;
	}
	
	/**
	 * 取得所有柜台及其所属区域用于显示柜台区域树
	 * @throws Exception
	 */
	public void getTreeNodes() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("organizationInfoId", form.getOrganizationInfoId());
		map.put("paperId", form.getPaperId());
		List<Map<String,Object>> treeNodes = binOLMOCIO06_BL.getAllCounterAndRegion(map);
		ConvertUtil.setResponseByAjax(response, treeNodes);
	}
	
	public void changeRadio() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paperId", form.getPaperId());
		map.put("organizationInfoId",form.getOrganizationInfoId());
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("controlFlag", form.getIssuedType());
		List<Map<String,Object>> treeNodes = binOLMOCIO06_BL.getAllCounterAndRegion(map);
		ConvertUtil.setResponseByAjax(response, treeNodes);
	}
	/**
	 * 问卷下发操作
	 * 
	 * 
	 * */
	public String issuedPaper() throws Exception{
		try{
			Map<String, Object> map = getIssuedMap();
			String checkedCounters = form.getCheckedCounter();
			String unCheckedCounter = form.getUnCheckedCounter();
			List<Map<String,Object>> checkedList = (List<Map<String, Object>>) JSONUtil.deserialize(checkedCounters);
			List<String> unCheckedList = (List<String>) JSONUtil.deserialize(unCheckedCounter);
			binOLMOCIO06_BL.tran_issuedPaper(map, checkedList, unCheckedList);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}
	
	/**
	 * 下发到导入的柜台
	 * @return
	 * @throws Exception
	 */
	public String importCounter() throws Exception {
    	try{
			Map<String, Object> map = getIssuedMap();
			// 解析导入的柜台信息[处理后只含有柜台的组织ID信息]
			List<Map<String, Object>> importList = binOLMOCIO06_BL.parseFile(upExcel, map);
			// 取得导入柜台id参数
			List<String> contraryIDList = new ArrayList<String>();
			for(Map<String, Object> listMap : importList) {
				contraryIDList.add(ConvertUtil.getString(listMap.get("organizationId")));
			}
			map.put("contraryIDList", contraryIDList);
			// 取得导入柜台的补集（此处的全集为"页面上的柜台树的柜台节点"）
			List<String> unImportList = binOLMOCIO06_BL.getContraryOrgID(map);
			
			binOLMOCIO06_BL.tran_issuedPaper(map, importList, unImportList);
			// 处理成功(替换整张页面)
	        this.addActionMessage(getText("ICM00002"));
	        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                throw e;
            }
    	}
	}
	
	/**
	 * 取得下发共通参数
	 * @return
	 */
	private Map<String, Object> getIssuedMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put("brandInfoId", form.getBrandInfoId());
		// 下发类型（0：下发，1：禁止下发）
		map.put("controlFlag", form.getIssuedType());
		map.put("paperId", form.getPaperId());
		map.put("organizationInfoId", form.getOrganizationInfoId());
		map.put("paperType", form.getPaperType());
		map.put("paperName", form.getPaperName());
		map.put("maxPoint", form.getMaxPoint());
		// 是否下发过
		map.put("isIssued", form.getIsIssued());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 作成者为当前用户
		map.put("createdBy", userInfo.getBIN_UserID());
		// 作成程序名为当前程序
		map.put("createPGM", "BINOLMOCIO06");
		// 更新者为当前用户
		map.put("updatedBy", userInfo.getBIN_UserID());
		// 更新程序名为当前程序
		map.put("updatePGM", "BINOLMOCIO06");
		return map;
	}

	public Map<String, Object> getPaperMap() {
		return paperMap;
	}

	public void setPaperMap(Map<String, Object> paperMap) {
		this.paperMap = paperMap;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	@Override
	public BINOLMOCIO06_Form getModel() {
		return form;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
	

}
