package com.cherry.ct.common.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;

/**
 * 沟通管理首页Action
 * 
 * @author zhanggs
 * @version 1.0 2011.11.12
 */
public class CtIndexAction extends BaseAction{
	/**
	 * 权限管理首页初始化
	 * return String
	 */
	private static final long serialVersionUID = -8045053802578918898L;

	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(CtIndexAction.class);
	
	public String initial(){
		try{
			request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
			Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
}
