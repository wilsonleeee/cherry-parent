/*
 * @(#)BINOLCM41_IF.java v1.0 2014-11-6
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 产品基础信息   共通IF
 * 
 * @author JiJW
 * @version 1.0 2014-11-6
 */
public interface BINOLCM41_IF extends ICherryInterface {
	
	/**
	 * 取得柜台对应的产品列表
     * 返回的列表中按照OrderNO从小到大排序，且默认仓库在前
     * @param praMap
     * praMap参数说明：organizationInfoId（必填。所属组织ID）,
     * praMap参数说明：brandInfoId（必填。所属品牌ID）,
     * praMap参数说明：counterCode （必填。柜台编码。）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * 
	 * @param pram
	 * @return
	 */
	public List<Map<String, Object>> getCntProductList(Map<String, Object> praMap) throws Exception;
	
	/**
	 * 查看柜台使用产品方案还是标准产品
	 * @param praMap
     * praMap参数说明：organizationInfoId（必填。所属组织ID）,
     * praMap参数说明：brandInfoId（必填。所属品牌ID）,
     * praMap参数说明：counterCode （必填。柜台编码。）
	 * @return cntPrt 1:使用柜台产品方案 0：使用标准产品
	 * @throws Exception
	 */
	public String getIsCntPrt(Map<String, Object> praMap)throws Exception;

}
