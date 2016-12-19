/*		
 * @(#)BINOLSSPRM01_BL.java     1.0 2010/11/23		
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
package com.cherry.ss.prm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.util.Util;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.prm.service.BINOLSSPRM01_Service;
import com.cherry.webservice.client.WebserviceClient;

/**
 * 促销产品查询 BL
 * 
 */

public class BINOLSSPRM01_BL {

	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM01_BL.class);
	
	@Resource
	private BINOLSSPRM01_Service binolssprm01_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * 取得促销品总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrmCount(Map<String, Object> map) {

		return binolssprm01_Service.getPrmCount(map);
	}

	/**
	 * 取得促销品信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List searchPromotionProList(Map<String, Object> map) {

		// 取得促销品基本信息List
		return binolssprm01_Service.getPromotionProList(map);
	}
	
	/**
	 * 促销产品实时下发
	 * @param map
	 * @throws Exception
	 */
	public Map<String,Object> tran_issuedPrm(Map<String, Object> map) throws Exception{
		
        Map<String, Object> result;
        String errCode = "";
        String errMsg = "OK";
        
        Map<String, Object> msgMap = new HashMap<String, Object>();
		try{
            logger.info("*********促销品webService下发处理开始*********");
			// 品牌是否支持促销品下发
			boolean isPrmIss = binOLCM14_BL.isConfigOpen("1296", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if(!isPrmIss){
				msgMap.put("result", "2"); // 品牌的系统配置项不支持促销品下发功能，请联系管理员！
				return msgMap;
			}
			
			map.put("TradeType","PublishPromotion");
			result = WebserviceClient.accessBatchWebService(map);
            if(null != result){
                errCode = ConvertUtil.getString(result.get("ERRORCODE"));
                errMsg = ConvertUtil.getString(result.get("ERRORMSG"));
                if(!"0".equals(errCode)){
                	msgMap.put("result", "1");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                    logger.error("*********促销品webService下发处理异常ERRORCODE【"+errCode+"】*********");
                    logger.error("*********促销品webService下发处理异常ERRORMSG【"+errMsg+"】*********");
                } else {
                	msgMap.put("result", "0");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                }
            }else{
            	msgMap.put("result", "1");
                errCode = "-1";
                errMsg = "webService访问返回结果信息为空";
                msgMap.put("ERRORCODE", errCode);
                msgMap.put("ERRORMSG", errMsg);
                logger.error("********* 促销品webService下发处理异常ERRORCODE【"+errCode+"】*********");
                logger.error("********* 促销品webService下发处理异常ERRORMSG【"+errMsg+"】*********");
            }
            
            logger.info("*********促销品webService下发处理结束【"+errCode+"】*********");
		}catch(Exception e){
			msgMap.put("result", "1");
			logger.error(e.getMessage(),e);
		}

		return msgMap;
	}

	/**
	 * 伦理删除促销产品信息
	 * 
	 * @param prmInfos
	 *            促销产品参数数组（有效区分+促销产品ID+更新日期+更新次数）
	 * @param updatedBy （更新者ID）
	 * 
	 * @param validFlag
	 *            有效区分1：启用, 0：停用           
	 * @return
	 */
	public void tran_operatePrm(String[] prmInfos, String validFlag,
			int updatedBy) throws CherryException, Exception {
		// 没有选择任何促销品
		if (null == prmInfos || 0 == prmInfos.length) {
			throw new CherryException("ESS00005");
		}
		for (String prmInfo : prmInfos) {
			String[] info = prmInfo.split(CherryConstants.UNLINE);
			// 最后修改时间或者修改次数为空时
			if (info.length < 4) {
				String nameTotal = ConvertUtil.getString(binolssprm01_Service
						.getPromotionPro(info[1]).get(CherryConstants.NAMETOTAL));
				throw new CherryException("ESS00006", new String[] { nameTotal });
			}
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 促销产品ID
			map.put(CherryConstants.PROMOTIONPROID, info[1]);
			// 修改时间
			map.put(CherryConstants.MODIFY_TIME, info[2]);
			// 修改次数
			map.put(CherryConstants.MODIFY_COUNT, info[3]);
			// 更新者
			map.put(CherryConstants.UPDATEDBY, updatedBy);
			//有效区分
			map.put(CherryConstants.VALID_FLAG, validFlag);
			// 更新时间
			map.put(CherryConstants.UPDATE_TIME, binolssprm01_Service.getSYSDate());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM01");
			
			// 伦理删除促销品信息
			int sum = binolssprm01_Service.operatePromotionPro(map);
			if (sum == 0) {
				String nameTotal = ConvertUtil.getString(binolssprm01_Service
						.getPromotionPro(info[1]).get(CherryConstants.NAMETOTAL));
				throw new CherryException("ESS00006", new String[] { nameTotal});
			}
			// 伦理删除促销产品价格
			binolssprm01_Service.operatePromotionProPrice(map);
			// 伦理删除促销产品BOM
			binolssprm01_Service.operatePromotionProBOM(map);
			// 伦理删除促销产品厂商
			binolssprm01_Service.operatePromotionProFac(map);
		}
	}
	/**
	 * 导出促销品信息Excel
	 * 
	 * @param map
	 * @return 返回导出促销品信息List
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
	    List<Map<String, Object>> dataList = binolssprm01_Service.getPromotionInfoListExcel(map);
	    String[][] array = {
	    		{ "BrandCode", "prm.brandInfo", "15", "", "" },
	    		{ "BrandNameChinese", "prm.brandNameChinese", "20", "", "" },
	            { "NameTotal", "prm.nameTotal", "20", "", "" },
	            { "UnitCode", "prm.unitCode", "20", "", "" },
	            { "BarCode", "prm.barCode", "20", "", "" },
	            { "standardCost", "prm.standardCost", "15", "right", "" },
	            { "validFlag", "prm.validFlag", "15", "", "1137" },
	            { "PromotionCateCD", "prm.promCate", "18", "", "1139" },
	            { "PrimaryCategoryNameChinese", "prm_primaryCategoryName", "15", "", "" },
	            { "SecondryCategoryNameChinese", "prm_secondryCategoryName", "15", "", "" },
	            { "SmallCategoryNameChinese", "prm_smallCategoryName", "15", "", "" },
	            { "SellStartDate", "prm.sellStartDate", "15", "right", "" },
	            { "SellEndDate", "prm.sellEndDate", "15", "right", "" },
	            { "StyleCode", "prm.styleCode", "15", "right", "1012" },
	            { "OperationStyle", "prm.operationStyle", "10", "", "1013" },
	            { "Volume","prm.volume", "10", "right", "" },
	            { "Weight", "prm.weight", "10", "right", "" },
	            { "NameForeign", "prm.nameForeign", "15", "", "" },
	            { "NameShortForeign", "prm.nameShortForeign", "15", "", "" },
	            { "ShelfLife", "prm.shelfLife", "15", "right", "" },
	            { "exPoint", "prm.exPoint", "20", "right", "" },
	            { "isStock", "prm.isStock", "20", "right", "1140" },
	            { "isPosIss", "prm.isPosIss", "20", "right", "1341" },
	           
	    };
	    BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
	    ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSSPRM01");
		ep.setSheetLabel("sheetName");
		ep.setDataList(dataList);
	    return binOLMOCOM01_BL.getExportExcel(ep);
	}
}
