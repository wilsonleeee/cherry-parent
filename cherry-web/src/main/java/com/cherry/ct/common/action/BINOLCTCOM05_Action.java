package com.cherry.ct.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM05_Form;
import com.cherry.ct.common.interfaces.BINOLCTCOM05_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCTCOM05_Action extends BaseAction implements ModelDriven<BINOLCTCOM05_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BINOLCTCOM05_Form form = new BINOLCTCOM05_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM05_Action.class);
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCTCOM05_IF binOLCTCOM05_IF;
	
	public String init() throws Exception{
		return SUCCESS;
	}
	
	public void send() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 品牌ID
			int brandInfoId = userInfo.getBIN_BrandInfoID();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 组织代码
    		map.put("orgCode", userInfo.getOrganizationInfoCode());
			// 品牌ID
			map.put("brandInfoId", brandInfoId);
			// 品牌代码
			map.put("brandCode", brandCode);
			// 沟通类型
			map.put("messageType", form.getMessageType());
			// 沟通信息内容
			map.put("contents", form.getContents());
			// 信息接收号码列表
			map.put("resCodeList", form.getResCodeList());
			//短信通道类型
			map.put("smsChannel", form.getSmsChannel());
			binOLCTCOM05_IF.tran_sendTestMsg(map);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
	}
	
	public void validateSend() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织ID
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 获取手机号验证规则
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
				
		boolean errorflag = false;
		String resCodeText = ConvertUtil.getString(form.getResCodeList());
		resCodeText = resCodeText.replace("；", ";");
		resCodeText = resCodeText.replace("，", ";");
		resCodeText = resCodeText.replace(",", ";");
		String[] resCodes = resCodeText.split(";");
		for (String resCode : resCodes) {
			if(!"".equals(resCode) && resCode.length()>0){
				if(!CherryChecker.isPhoneValid(resCode, mobileRule)){
					errorflag = true;
				}
			}
		}
		if(errorflag){
			this.addFieldError("resCodeList", getText("CTM00018"));
		}
	}
	
	@Override
	public BINOLCTCOM05_Form getModel() {
		return form;
	}
}
