/*
 * @(#)BINOLPTJCS06_BL.java     1.0 2011/04/26
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
package com.cherry.pt.jcs.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS06_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS06_Service;

/**
 * 产品详细 BL
 * 
 * @author lipc
 * @version 1.0 2011.04.26
 */
public class BINOLPTJCS06_BL implements BINOLPTJCS06_IF {

	@Resource
	private BINOLPTJCS06_Service binOLPTJCS06_Service;

	/**
	 * 取得产品条码List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getBarCodeList(Map<String, Object> map) {
		// ********* 2012-9-11 产品/促销品一览显示历史上存在的所有产品 NEWWITPOS-1583 start *********//
		/**
		 * 当前validFlag可能是PrtBarCode表中无效产品数据
		 * 而当一览数据显示为无效时，去查询产品的详细barcode会因为validFlag无效导致查询不到数据。
		 * 所以此处查询时，去除validFlag
		 */
		Map<String, Object> praMap = new HashMap<String,Object>();
		praMap.putAll(map);
		praMap.remove(CherryConstants.VALID_FLAG);
		// ********* 2012-9-11 产品/促销品一览显示历史上存在的所有产品 NEWWITPOS-1583 end *********//
		return binOLPTJCS06_Service.getBarCodeList(praMap);
	}

	/**
	 * 获取产品分类List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getCateList(Map<String, Object> map) {

		return binOLPTJCS06_Service.getCateList(map);
	}

	/**
	 * 取得产品详细信息
	 * 
	 * @param map
	 * @return
	 * 
	 * */
	@Override
	public Map<String, Object> getDetail(Map<String, Object> map) {

		return binOLPTJCS06_Service.getDetail(map);
	}

	/**
	 * 获取产品销售价格List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getSellPriceList(Map<String, Object> map) {

		return binOLPTJCS06_Service.getSellPriceList(map);
	}

	/**
	 * 获取产品图片List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getImgList(Map<String, Object> map) {
		// 图片路径前缀
		String imagePath = PropertiesUtil.pps
				.getProperty("uploadFilePath.upImage");
		List<Map<String, Object>> list = binOLPTJCS06_Service.getImgList(map);
		if (null != list && !list.isEmpty()) {
			for (Map<String, Object> imgMap : list) {
				imgMap.put(CherryConstants.PATH, imagePath
						+ CherryConstants.SLASH
						+ ConvertUtil.getString(imgMap.get(CherryConstants.PATH)));
			}
		}
		return list;
	}

	/**
	 * 获取产品BOM组件List
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getBOMList(Map<String, Object> map) {
		return binOLPTJCS06_Service.getBOMList(map);
	}
	
	/**
     * 取得产品编码条码修改履历
     * 
     * @param map
     * 
     * @return
     */
	public List<Map<String, Object>> getPrtBCHistoryList(Map<String, Object> map){
    	return binOLPTJCS06_Service.getPrtBCHistoryList(map);
    }
	
}
