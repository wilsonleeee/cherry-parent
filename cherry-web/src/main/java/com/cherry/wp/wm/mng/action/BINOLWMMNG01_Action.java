package com.cherry.wp.wm.mng.action;

import com.cherry.cm.core.BaseAction;

/**
 * 会员管理Action
 * 
 * @author WangCT
 * @version 1.0 2014.10.24
 */
public class BINOLWMMNG01_Action extends BaseAction {
	
	private static final long serialVersionUID = 3966349785898087400L;

	/**
	 * 会员管理画面初始化
	 * 
	 * @return 会员管理画面
	 */
	public String init() throws Exception {
		
		wmFlag = "1";
		
		return SUCCESS;
	}
	
	private String wmFlag;

	public String getWmFlag() {
		return wmFlag;
	}

	public void setWmFlag(String wmFlag) {
		this.wmFlag = wmFlag;
	}

}
