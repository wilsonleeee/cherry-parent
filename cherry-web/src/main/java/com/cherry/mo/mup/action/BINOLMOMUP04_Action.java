package com.cherry.mo.mup.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.mup.form.BINOLMOMUP01_Form;
import com.cherry.mo.mup.form.BINOLMOMUP04_Form;
import com.cherry.mo.mup.interfaces.BINOLMOMUP04_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOMUP04_Action extends BaseAction 
	implements ModelDriven<BINOLMOMUP04_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLMOMUP04_Form form = new BINOLMOMUP04_Form();
	
	@Resource
	private BINOLMOMUP04_IF binOLMOMUP04_BL;
	
	private List<Map<String,Object>> softVersionInfoList;
	
	/**
	 * 初始化页面
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception{

        return SUCCESS;
	}
	
	public String save()throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLMOMUP04");	
		// 作成日时
		map.put(CherryConstants.CREATE_TIME,CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		
		map.put("version", form.getVersion().trim());
		
		map.put("downloadUrl", form.getDownloadUrl().trim());
		
		map.put("openUpdateTime", form.getOpenUpdateTime());

		map.put("md5Key",form.getMd5Key());

		binOLMOMUP04_BL.saveSoftVersionInfo(map);
		//处理成功
		this.addActionMessage(getText("IMO00107"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	
	}

	/**
	 * 添加验证
	 * @throws Exception
     */
	public void validateSave() throws Exception {

		//版本号验证
		if(ConvertUtil.isBlank(form.getVersion())){

			this.addFieldError("version",getText("EMO00108"));
		}else{

			String version = form.getVersion();
			String regex = "([1-9]\\d*(\\.[1-9]\\d*){2})";
			boolean flg = Pattern.matches(regex, version);
			if (!flg){
				this.addFieldError("version",getText("EMO00107"));
			}
		}
		//下载地址验证
		if(ConvertUtil.isBlank(form.getDownloadUrl())){
			this.addFieldError("downloadUrl",getText("ACT000100"));
		}
		//md5Key验证
		if(ConvertUtil.isBlank(form.getMd5Key())){
			this.addFieldError("md5Key",getText("ACT000100"));
		}


	}
	
	@Override
	public BINOLMOMUP04_Form getModel() {
		
		return form;
	}

}
