package com.cherry.cm.cmbussiness.function;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM09_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM59_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM61_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINOLCM09_FN implements FunctionProvider{
	
	@Resource
	private BINOLCM09_BL binOLCM09_BL;
	@Resource(name="binOLCM59_BL")
	private BINOLCM59_BL binOLCM59_BL;
	@Resource(name="binOLCM61_BL")
	private BINOLCM61_BL binOLCM61_BL;
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	private static Logger logger = LoggerFactory.getLogger(BINOLCM09_FN.class.getName());
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		logger.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠【促销活动下发-开始】♠♠♠♠♠♠♠♠♠♠♠♠♠♠");
		Date d1 = new Date();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Object orgIdObj = transientVars.get("organizationInfoId");
			Object brandIdObj = transientVars.get("brandInfoId");
			// 组织信息ID
			map.put("bin_OrganizationInfoID", orgIdObj);
			// 品牌信息ID
			map.put("brandInfoID", brandIdObj);
			// 品牌Code
			map.put("brandCode", transientVars.get("brandCode"));
			
			int result = binOLCM09_BL.tran_publicProActive(map);
			if(result == CherryBatchConstants.BATCH_SUCCESS){
				logger.info("*********【促销规则下发-开始】*********");
				if ("2".equals(binOLCM14_BL.getConfigValue("1347", String.valueOf(orgIdObj), String.valueOf(brandIdObj)))) {
					result = binOLCM61_BL.tran_PromotionRule(map);
				} else {
					result = binOLCM59_BL.tran_publicProActiveRule(map);
				}
				
			}
			ps.setInt("result", result);
			Date d2 = new Date();
			long time = (d2.getTime() - d1.getTime()) / 1000;
			logger.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠【促销活动下发-结束( "+time+" 秒)】♠♠♠♠♠♠♠♠♠♠♠♠♠♠");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		
	}

}
