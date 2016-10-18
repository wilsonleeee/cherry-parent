/*
 * @(#)BINOLSSPRM34_BL.java     1.0 2010/11/24
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

import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.prm.service.BINOLSSPRM34_Service;

/**
 * 
 * 发货单编辑BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.24
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM34_BL {

	@Resource
	private BINOLSSPRM34_Service binOLSSPRM34_Service;
	
	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return 无
	 */
	public Map searchDeliverInfo(Map<String, Object> map) {
		// 取得发货单信息
		return binOLSSPRM34_Service.getDeliverInfo(map);
	}

	/**
	 * 取得发货单明细List
	 * 
	 * @param map
	 * @return 无
	 */
	public List searchDeliverDetailList(Map<String, Object> map) {
		// 取得发货单明细List
		return binOLSSPRM34_Service.getDeliverDetailList(map);
	}

	/**
	 * 保存促销品发货单和明细信息
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception
	 */
//	public void tran_saveDeliver(Map<String, Object> map, UserInfo userInfo)
//			throws Exception {
//		// 所属组织
//		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
//				.getBIN_OrganizationInfoID());
//		// 所属品牌
//		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//		// 存放共通参数的map
//		Map<String, Object> comMap = new HashMap<String, Object>();
//		// 作成者
//		comMap.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
//		// 作成程序名
//		comMap.put(CherryConstants.CREATEPGM, CherryConstants.BINOLSSPRM34);
//		// 更新者
//		comMap.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
//		// 更新程序名
//		comMap.put(CherryConstants.UPDATEPGM, CherryConstants.BINOLSSPRM34);
//		map.putAll(comMap);
//		// 需要保存处理
//		if ("1".equals(map.get("saveKbn"))) {
//			// 总数量
//			int totalQuantity = 0;
//			// 总金额
//			double totalAmount = 0;
//			// 取得发货明细信息
//			String detailInfo = (String) map.get("detailInfo");
//			// 发货明细List
//			List<Map<String, Object>> detailList = null;
//			if (null != detailInfo && !"".equals(detailInfo)) {
//				// 发货明细List
//				detailList = (List<Map<String, Object>>) JSONUtil
//						.deserialize(detailInfo);
//				// 计算总数量和总金额
//				if (null != detailList) {
//					for (Map<String, Object> detailMap : detailList) {
//						// 促销产品收发货明细ID
//						String deliverDetailId = (String) detailMap
//								.get("deliverDetailId");
//						if (null != deliverDetailId && !"".equals(deliverDetailId)) {
//							// 有效区分
//							String validFlag = (String) detailMap.get("validFlag");
//							if ("0".equals(validFlag)) {
//								continue;
//							}
//						}
//						// 发货数量
//						int quantity = CherryUtil.string2int((String) detailMap
//								.get("quantity"));
//						// 单价
//						double price = CherryUtil.string2double((String) detailMap
//								.get("price"));
//						detailMap.put("quantity", quantity);
//						detailMap.put("price", price);
//						totalQuantity += quantity;
//						totalAmount += price * quantity;
//					}
//				}
//			}
//			// 总数量
//			map.put("totalQuantity", totalQuantity);
//			// 总金额
//			map.put("totalAmount", totalAmount);
//			// 需要发货处理
//			if ("1".equals(map.get("deliverKbn"))) {
//				// 入库区分(已发货)
//				map.put("stockInFlag", "2");
//			} else {
//				// 入库区分(未发货)
//				map.put("stockInFlag", "1");
//			}
//			// 更新促销产品收发货业务单据表
//			int result = binOLSSPRM34_Service.updatePromDeliver(map);
//			// 更新处理失败
//			if (0 == result) {
//				throw new CherryException("ECM00005");
//			}
//			if (null != detailList) {
//				for (Map<String, Object> detail : detailList) {
//					Map<String, Object> detailMap = CherryUtil.removeEmptyVal(detail);
//					detailMap.putAll(comMap);
//					// 促销产品收发货明细ID
//					String deliverDetailId = (String) detailMap
//							.get("deliverDetailId");
//					if (null != deliverDetailId && !"".equals(deliverDetailId)) {
//						// 有效区分
//						String validFlag = (String) detailMap.get("validFlag");
//						// 更新结果
//						int detailRst = 0;
//						if ("0".equals(validFlag)) {
//							// 伦理删除促销产品收发货业务单据明细表
//							detailRst = binOLSSPRM34_Service
//									.invalidPromDeliverDetail(detailMap);
//						} else {
//							// 实体仓库ID
//							detailMap
//									.put("inventoryId", map.get("inventoryId"));
//							// 更新促销产品收发货业务单据明细表
//							detailRst = binOLSSPRM34_Service
//									.updatePromDeliverDetail(detailMap);
//						}
//						// 更新处理失败
//						if (0 == detailRst) {
//							throw new CherryException("ECM00005");
//						}
//					} else {
//						// 促销产品收发货ID
//						detailMap.put("deliverId", map.get("deliverId"));
//						// 实体仓库ID
//						detailMap.put("inventoryId", map.get("inventoryId"));
//						// 包装类型ID
//						detailMap.put("packageId", 0);
//						// 逻辑仓库ID
//						detailMap.put("logicInventoryId", 0);
//						// 收发货仓库库位ID
//						detailMap.put("storageLocationId", 0);
//						// 插入促销产品收发货业务单据明细表
//						binOLSSPRM34_Service.insertPromDeliverDetail(detailMap);
//					}
//				}
//			}
//		}
//		// 需要发货处理
//		if ("1".equals(map.get("deliverKbn"))) {
//			// 不需要保存处理
//			if ("0".equals(map.get("saveKbn"))) {
//				// 入库区分
//				map.put("stockInFlag", "2");
//				// 更新入库区分
//				int result = binOLSSPRM34_Service.updateStockInFlag(map);
//				if (0 == result) {
//					throw new CherryException("ECM00025");
//				}
//			}
//			// 完善用户信息
//			binOLCM01BL.completeUserInfo(userInfo, (String) map.get("deliverDepId"),
//					CherryConstants.BINOLSSPRM34);
//			// 促销产品收发货ID
//			int[] deliverIdArr = { CherryUtil.string2int((String) map
//					.get("deliverId")) };
//			// 发货处理
//			binOLSSCM01_BL.insertStockInOutAndDetail(deliverIdArr, userInfo);
//			
//			String deliverRecNo = (String)map.get("deliverRecNo");
//			String receiveOrganizationID = (String)map.get("receiveDepId");
//			//读取配置文件，工作流是否开启
//			String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
//			if(jbpmFlag.equals("true")){				
//				Map<String,Object> mapJbpm =new HashMap<String,Object>();
//				mapJbpm.put(CherryConstants.JBPM_MAIN_ID, CherryUtil.string2int((String) map.get("deliverId")));
//				mapJbpm.put(CherryConstants.JBPM_MAIN_NO, deliverRecNo);
//				mapJbpm.put("BIN_OrganizationIDReceive", receiveOrganizationID);
//				promotionDeliverJbpm.runJbpmFlow(userInfo,mapJbpm);	
//			}
//		}
//	}
}
