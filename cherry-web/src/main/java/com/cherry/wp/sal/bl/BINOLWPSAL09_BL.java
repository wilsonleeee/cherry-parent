package com.cherry.wp.sal.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.wp.sal.interfaces.BINOLWPSAL09_IF;
import com.cherry.wp.sal.service.BINOLWPSAL09_Service;

public class BINOLWPSAL09_BL implements BINOLWPSAL09_IF{

	@Resource(name="binOLWPSAL09_Service")
	private BINOLWPSAL09_Service binOLWPSAL09_Service;
	
	@Override
	public String tran_bindCounter(Map<String, Object> map) throws Exception {
		String bindResult = "";
		// 验证输入的柜台号与密码是否正确
		int counterCount = binOLWPSAL09_Service.checkCounterPassword(map);
		if(counterCount > 0){
			// 系统时间
			String sysDate = binOLWPSAL09_Service.getSYSDate();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.CREATE_TIME, sysDate);
			paramMap.put(CherryConstants.UPDATE_TIME, sysDate);
			paramMap.put(CherryConstants.CREATEPGM, "BINOLWPSAL09");
			paramMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL09");
			paramMap.put("createdBy", map.get("userId"));
			paramMap.put("updatedBy", map.get("userId"));
			paramMap.putAll(map);
			// 解除当前用户已绑定的柜台记录
			binOLWPSAL09_Service.unBindCounter(paramMap);
			// 写入用户与柜台绑定信息			
			binOLWPSAL09_Service.bindCounter(paramMap);
			bindResult = CherryConstants.WP_SUCCESS_STATUS;
		}else{
			bindResult = CherryConstants.WP_ERROR_STATUS;
		}
		return bindResult;
	}

}
