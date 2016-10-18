package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;

/**
 * 大仓业务共通接口，各种业务的起始方法
 * @author dingyongchang
 *
 */
public interface BINOLSTCM00_IF {

	long StartOSWorkFlow(Map<String,Object> mainData) throws Exception;
	void DoAction(Map<String,Object> mainData)throws Exception;
	void posReceiveFinishFlow(Map<String, Object> mainData) throws Exception;
}
