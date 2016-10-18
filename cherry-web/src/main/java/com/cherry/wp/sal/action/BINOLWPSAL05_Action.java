package com.cherry.wp.sal.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.sal.form.BINOLWPSAL05_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL05_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL05_Action extends BaseAction implements ModelDriven<BINOLWPSAL05_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLWPSAL05_Form form = new BINOLWPSAL05_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL05_Action.class);
	
	@Resource(name="binOLWPSAL05_BL")
	private BINOLWPSAL05_IF binOLWPSAL05_IF;
	
	public String init(){
		return SUCCESS;
	}
	
	public void hangBill() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 挂单
			String result = binOLWPSAL05_IF.tran_hangBill(form, userInfo);
			if(result.equals(CherryConstants.WP_SUCCESS_STATUS)){
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
	public BINOLWPSAL05_Form getModel() {
		return form;
	}

}
