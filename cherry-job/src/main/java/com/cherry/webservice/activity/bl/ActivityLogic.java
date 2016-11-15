package com.cherry.webservice.activity.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cp.act.bl.BINCPMEACT05_BL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM09_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM59_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.bl.BINCPMEACT04_BL;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;

public class ActivityLogic implements CherryMessageHandler_IF{
	private static final Logger logger = LoggerFactory
			.getLogger(ActivityLogic.class);
	
	@Resource
	private BINOLCM09_BL binOLCM09_BL;
	
	@Resource(name="binOLCM59_BL")
	private BINOLCM59_BL binOLCM59_BL;
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;
	
	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14bl;
	
	@Resource
	private BINCPMEACT04_BL bincpmeact04_BL;

	@Resource
	private BINCPMEACT05_BL bincpmeact05_BL;
	
	public Map<String,Object> tran_publishActive(Map<String, Object> params){
		Map<String,Object> resMap = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		// 所属组织
		map.put("bin_OrganizationInfoID", params.get("BIN_OrganizationInfoID"));
		// 品牌信息ID
		map.put("brandInfoID", params.get("BIN_BrandInfoID"));
		// 品牌Code
		map.put("brandCode", params.get("brandCode"));
		// 活动Code
		map.put("mainCode", params.get("mainCode"));
		try {
			if(flg == CherryBatchConstants.BATCH_SUCCESS){
				logger.info("*********促销活动webService下发处理*********");
				flg = binOLCM09_BL.tran_publicProActive(map);
			}
			if(flg == CherryBatchConstants.BATCH_SUCCESS){
				logger.info("*********促销规则webService下发处理*********");
				flg = binOLCM59_BL.tran_publicProActiveRule(map);
			}
			if(flg != CherryBatchConstants.BATCH_SUCCESS){
				resMap.put("ERRORCODE", "B1");
				resMap.put("ERRORMSG", "促销活动发布失败！");
			}
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			resMap.put("ERRORCODE", "B2");
			resMap.put("ERRORMSG", cbx.getMessage());
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			resMap.put("ERRORCODE", "B3");
			resMap.put("ERRORMSG", e.getMessage());
		}
		return resMap;
	}
	
	public int[] makeOrder(Map<String, Object> params){
		Map<String,Object> map = new HashMap<String, Object>();
		// 设置batch处理标志
		int[] flag = {CherryBatchConstants.BATCH_SUCCESS,CherryBatchConstants.BATCH_SUCCESS};
		map.put("option", "WS");
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, params.get("BIN_OrganizationInfoID"));
		// 组织Code
		map.put("orgCode", params.get("OrgCode"));
		// 品牌信息ID
		map.put(CherryBatchConstants.BRANDINFOID, params.get("BIN_BrandInfoID"));
		// 品牌Code
		map.put(CherryBatchConstants.BRAND_CODE, params.get("BrandCode"));
		// 会员ID
		map.put("memId", params.get("MemId"));
		// 活动码
		map.put(CampConstants.SUBCAMP_CODE, params.get("SubCampCode"));
		map.put("repeatFlag", params.get("RepeatFlag"));
		map.put("orderCntCode", params.get("OrderCntCode"));
		// 活动类型
		String subCampType = ConvertUtil.getString(params.get("SubCampType"));
		if(!"".equals(subCampType)){
			if(subCampType.indexOf("_") > -1){
				map.put("subCampTypeArr", subCampType.split("_"));
			}else{
				map.put("subCampType", subCampType);
			}
		}
		// BATCHNO
		map.put(CampConstants.BATCHNO, params.get("BatchNo"));
		try {
			if(flag[0] == CherryBatchConstants.BATCH_SUCCESS){
				logger.debug("*********活动单据webService生成处理*********");
				flag = bincpmeact04_BL.makeOrder(map);
			}
		} catch (CherryBatchException cbx) {
			flag[0] = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flag[0] = CherryBatchConstants.BATCH_ERROR;
		}
		return flag;
	}

	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
//		if(BINCPMEACT04_BL.getStatus(brandCode) == 0){
			Map<String, Object> p1 = new HashMap<String, Object>();
			String orgId = ConvertUtil.getString(map.get("organizationInfoID"));
			String brandId = ConvertUtil.getString(map.get("brandInfoID"));
			String batchNo = ConvertUtil.getString(map.get("batchNo"));
			String sysTime = ConvertUtil.getString(map.get("sysTime"));
			String gtType = ConvertUtil.getString(map.get("gtType"));
			String subCampType = ConvertUtil.getString(map.get("subCampType"));
			p1.put("BIN_OrganizationInfoID", orgId);
			p1.put("OrgCode", map.get("orgCode"));
			p1.put("BIN_BrandInfoID",brandId);
			p1.put("BrandCode", brandCode);
			p1.put("BatchNo", batchNo);
			p1.put("MemId", map.get("memId"));
			p1.put("SubCampType", subCampType);
			p1.put("SubCampCode", map.get("subCampCode"));
			p1.put("RepeatFlag", map.get("repeatFlag"));
			p1.put("OrderCntCode", map.get("orderCntCode"));
			if(subCampType.contains("BIR")) {
				// 修改生日礼单据明细
				try {
					Map<String, Object> p2 = new HashMap<String, Object>();
					// 品牌信息ID
					p2.put(CherryBatchConstants.BRANDINFOID, brandId);
					p2.put("memberId", map.get("memId"));
					bincpmeact05_BL.tran_handleOrder(p2);
				} catch (Exception e) {
					logger.info("==修改生日礼单据明细异常==");
					logger.error("==修改生日礼单据明细异常==" + e.getMessage());
				}
			}
			// 生成单据
			int[] resFlag = makeOrder(p1);
			if(null != resFlag){
				if(resFlag[0] == CherryBatchConstants.BATCH_SUCCESS
						&& resFlag[1] == CherryBatchConstants.BATCH_SUCCESS){
					Map<String, Object> p = new HashMap<String, Object>();
					p.put(CherryConstants.ORGANIZATIONINFOID, orgId);
					p.put(CherryConstants.ORG_CODE, map.get("orgCode"));
					p.put(CherryConstants.BRANDINFOID, brandId);
					p.put(CherryConstants.BRAND_CODE, brandCode);
					p.put(CampConstants.BATCHNO, batchNo);
					p.put("sysTime", sysTime);
					p.put("couponFlag", cm14bl.getConfigValue("1138", orgId,brandId));
					try{
						// 发送单据MQ
						com05IF.sendPOSMQ(p,null);
						if(!"".equals(gtType)){
							// 发送沟通MQ
							com05IF.sendGTMQ(p, batchNo, gtType);
						}
					} catch (Exception e) {
						logger.error("**********"+e.getMessage()+"**********",e);
					}
				}
			}
//		}else{
//			logger.info("***BATCH正在执行活动单据生成，MQ单据生成忽略***");
//		}
	}
}
