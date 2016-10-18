/*
 * @(#)BINOLBSREG04_IF.java     1.0 2011/11/23
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
package com.cherry.bs.reg.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 区域添加画面IF
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public interface BINOLBSREG04_IF extends ICherryInterface {
	
	/**
	 * 添加区域处理
	 * 
	 * @param map 添加内容
	 */
    public void tran_addRegion(Map<String, Object> map);

}
