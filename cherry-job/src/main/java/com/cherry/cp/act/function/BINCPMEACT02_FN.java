package com.cherry.cp.act.function;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cp.act.bl.BINCPMEACT02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 会员活动单据下发Function
 * @author lipc
 *
 */
public class BINCPMEACT02_FN implements FunctionProvider{
	
	@Resource
	private BINCPMEACT02_BL bincpmeact02_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINCPMEACT02_FN.class.getName());
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, transientVars.get(CherryBatchConstants.ORGANIZATIONINFOID));
			// 品牌信息ID
			map.put(CherryBatchConstants.BRANDINFOID, transientVars.get(CherryBatchConstants.BRANDINFOID));
			// 品牌Code
			map.put(CherryBatchConstants.BRAND_CODE, transientVars.get(CherryBatchConstants.BRAND_CODE));
			
			logger.info("======================活动预约单据下发BATCH处理开始======================");
			int result = bincpmeact02_BL.tran_publicOrder(map);
			logger.info("======================活动预约单据下发BATCH处理结束======================");
			ps.setInt("result", result);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		
	}

}
