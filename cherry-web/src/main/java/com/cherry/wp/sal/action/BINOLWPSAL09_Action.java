package com.cherry.wp.sal.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.sal.form.BINOLWPSAL09_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL09_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL09_Action extends BaseAction implements ModelDriven<BINOLWPSAL09_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL09_Action.class);
	
	private BINOLWPSAL09_Form form = new BINOLWPSAL09_Form();
	
	@Resource(name="binOLWPSAL09_BL")
	private BINOLWPSAL09_IF binOLWPSAL09_IF;
	
	public String init(){
		return SUCCESS;
	}
	
	public void bind() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 获取登录IP作为机器号
			map.put("machineCode", userInfo.getLoginIP());
			// 柜台号
			map.put("counterCode", form.getCounterCode());
			// 柜台密码
			map.put("counterPassword", form.getCounterPassword());
			
			String bindResult = binOLWPSAL09_IF.tran_bindCounter(map);
			if(bindResult.equals(CherryConstants.WP_SUCCESS_STATUS)){
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}else{
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
	}
	
	@Override
	public BINOLWPSAL09_Form getModel() {
		return form;
	}
}
