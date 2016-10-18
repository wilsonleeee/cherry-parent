/*
 * @(#)BINOLJNMAN03_IF.java     1.0 2011/4/18
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.jn.man.interfaces;

import java.util.Map;

/**
 * 确认与创建Form
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public interface BINOLJNMAN03_IF {
	
	/**
	 * 取得更新会员活动相关表时所需要的参数
	 * 
	 * 
	 * @param Map
	 *            更新数据库参数集合
	 * @return 无
	 */
	public void getCamTempParams (Map<String, Object> map) throws Exception;
	
	/**
	 * 会员活动保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveCampaign(Map<String, Object> map) throws Exception;
			
}
