package com.cherry.ct.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM09_Form;
import com.cherry.ct.common.interfaces.BINOLCTCOM09_IF;
import com.cherry.lg.lgn.bl.LoginBusinessLogic;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCTCOM09_Action extends BaseAction implements ModelDriven<BINOLCTCOM09_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8661719698002628362L;
	
	private BINOLCTCOM09_Form form = new BINOLCTCOM09_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM09_Action.class);
	
	@Resource
	private BINOLCTCOM09_IF binOLCTCOM09_IF;
	
	@Resource
    private LoginBusinessLogic loginbusinesslogic;  
	
	public String getPwdInit() throws Exception{
		return SUCCESS;
	}
	
	public void getPwdSendMessage() throws Exception{
		try{
			String userName = ConvertUtil.getString(form.getUserName());
			// 获取数据源
			String dbname = loginbusinesslogic.userLogin(userName);
			// 设置数据源
			CustomerContextHolder.setCustomerDataSourceType(dbname);
			
			//获取品牌编号和组织编号所需参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userName", userName);
			Map<String, Object> userMap = binOLCTCOM09_IF.getBrandAndOrg(paramMap);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userMap.get("BIN_OrganizationInfoID"));
			// 品牌ID
			map.put("brandInfoId", userMap.get("BIN_BrandInfoID"));
			// 组织代码
    		map.put("orgCode", userMap.get("OrgCode"));
			// 品牌代码
			map.put("brandCode", userMap.get("BrandCode"));
			// 沟通类型
			map.put("mobilePhone", form.getMobilePhone());
			
			binOLCTCOM09_IF.tran_getPwdMsg(map);
			ConvertUtil.setResponseByAjax(response, "Y");
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "N");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "E");
			 }
		}
	}
	
	@Override
	public BINOLCTCOM09_Form getModel() {
		return form;
	}

}
