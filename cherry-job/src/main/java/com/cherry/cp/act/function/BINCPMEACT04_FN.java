package com.cherry.cp.act.function;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.bl.BINCPMEACT04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 会员活动单据下发Function
 * @author lipc
 *
 */
public class BINCPMEACT04_FN implements FunctionProvider{
	
	@Resource
	private BINCPMEACT04_BL bincpmeact04_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINCPMEACT04_FN.class.getName());
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		String brandCode = ConvertUtil.getString(transientVars.get(CherryBatchConstants.BRAND_CODE));
		try {
			BINCPMEACT04_BL.setStatus(brandCode,BINCPMEACT04_BL.getStatus(brandCode) + 1);
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, transientVars.get(CherryBatchConstants.ORGANIZATIONINFOID));
			// 组织Code
			map.put("orgCode", transientVars.get("orgCode"));
			// 操作类型
			map.put("option", "FN");
			// 品牌信息ID
			map.put(CherryBatchConstants.BRANDINFOID, transientVars.get(CherryBatchConstants.BRANDINFOID));
			// 品牌Code
			map.put(CherryBatchConstants.BRAND_CODE, transientVars.get(CherryBatchConstants.BRAND_CODE));
			map.put("DIFFER_DATE",args.get("DIFFER_DATE"));
			map.put("DATE_SIZE_LIMIT",args.get("DATE_SIZE_LIMIT"));
			logger.info("======================活动单据生成BATCH处理开始======================");
			int[] result = bincpmeact04_BL.makeOrder(map);
			logger.info("======================活动单据生成BATCH处理结束======================");
			ps.setInt("result", result[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}finally {
			BINCPMEACT04_BL.setStatus(brandCode,BINCPMEACT04_BL.getStatus(brandCode) - 1);
		}
		
	}

}
