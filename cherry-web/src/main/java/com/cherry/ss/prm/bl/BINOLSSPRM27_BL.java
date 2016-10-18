/*
 * @(#)BINOLSSPRM27_BL.java     1.0 2010/11/03
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.ss.prm.service.BINOLSSPRM27_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 发货单查询BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.03
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM27_BL {
	@Resource
	private BINOLSSPRM27_Service binolssprm27_Service;

	/**
	 * 取得发货单总数
	 * 
	 * @param map
	 * @return
	 */
	public int searchDeliverCount(Map<String, Object> map) {
		// 取得发货单总数
		return binolssprm27_Service.getDeliverCount(map);
	}

	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List searchDeliverList(Map<String, Object> map) {
		// 取得发货单List
		return binolssprm27_Service.getDeliverList(map);
	}

	/**
	 * 取得产品总数量和总金额
	 * 
	 * */
	public Map getSumInfo(Map<String,Object> map){
		return binolssprm27_Service.getSumInfo(map);
	}
	
	/**
	 * 删除选中的促销品发货单
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception
	 */
	public void tran_deleteDeliver(Map<String, Object> map) throws Exception {
		// 促销产品发货单信息
		String deliverInfo = (String) map.get("deliverInfo");
		if (null != deliverInfo && !"".equals(deliverInfo)) {
			// 发货单List
			List<Map<String, Object>> deliverList = (List<Map<String, Object>>) JSONUtil
					.deserialize(deliverInfo);
			if (null != deliverList) {
				// 存放共通参数的map
				Map<String, Object> comMap = (Map<String, Object>) map
						.get("comMap");
				for (Map<String, Object> deliver : deliverList) {
					if (null != comMap) {
						deliver.putAll(comMap);
						// 伦理删除促销产品收发货业务单据表
						int result = binolssprm27_Service
								.invalidPromDeliver(deliver);
						if (0 == result) {
							throw new CherryException("ECM00038");
						}
						// 伦理删除促销产品收发货业务单据明细表
						binolssprm27_Service.invalidPromDeliverDetail(deliver);
					}
				}
			}
		}
	}

	/**
	 * 发货选中的促销品发货单
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception
	 */
//	public void tran_sendDeliver(Map<String, Object> map, UserInfo userInfo) throws Exception {
//		//读取配置文件，工作流是否开启
//		String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
//		
//		// 促销产品发货单信息
//		String deliverInfo = (String) map.get("deliverInfo");
//		if (null != deliverInfo && !"".equals(deliverInfo)) {
//			// 发货单List
//			List<Map<String, Object>> deliverList = (List<Map<String, Object>>) JSONUtil
//					.deserialize(deliverInfo);
//			if (null != deliverList) {
//				// 存放共通参数的map
//				Map<String, Object> comMap = new HashMap<String, Object>();
//				// 更新者
//				comMap.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
//				// 更新程序名
//				comMap.put(CherryConstants.UPDATEPGM, CherryConstants.BINOLSSPRM27);
//				for (Map<String, Object> deliver : deliverList) {
//					deliver.putAll(comMap);
//					// 入库区分
//					deliver.put("stockInFlag", "2");
//					// 更新入库区分
//					int result = binOLSSPRM34_Service
//							.updateStockInFlag(deliver);
//					if (0 == result) {
//						throw new CherryException("ECM00025");
//					}
//					// 完善用户信息
//					binOLCM01BL.completeUserInfo(userInfo, (String) deliver.get("deliverDepId"),
//							CherryConstants.BINOLSSPRM27);
//					// 促销产品收发货ID
//					int[] deliverIdArr = {CherryUtil.string2int((String) deliver
//							.get("deliverId"))};
//					// 发货处理
//					binOLSSCM01_BL.insertStockInOutAndDetail(deliverIdArr, userInfo);
//					String deliverRecNo = (String)map.get("deliverRecNo");
//					String receiveOrganizationID = (String)map.get("receiveDepId");
//					if(jbpmFlag.equals("true")){				
//						Map<String,Object> mapJbpm =new HashMap<String,Object>();
//						mapJbpm.put(CherryConstants.JBPM_MAIN_ID, CherryUtil.string2int((String) map.get("deliverId")));
//						mapJbpm.put(CherryConstants.JBPM_MAIN_NO, deliverRecNo);
//						mapJbpm.put("BIN_OrganizationIDReceive", receiveOrganizationID);
//						promotionDeliverJbpm.runJbpmFlow(userInfo,mapJbpm);	
//					}
//				}
//			}
//		}
//	}
}
