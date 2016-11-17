package com.webconsole.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;

public class ViewBatchFailHistoryAction extends BaseAction{

	protected static final Logger logger = LoggerFactory.getLogger(ViewBatchFailHistoryAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	public void getJobFailureRunHistory() throws Exception{
		List<Map<String,Object>> retMapList = null;
		try{			

			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			
			retMapList = binbecm01_IF.getJobFailureRunHistory(map);
	        ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(retMapList));			
		}catch(Exception ex){
			logger.error("读取Batch失败履历操作出错：",ex);
		}finally{
		}		
	}
}
