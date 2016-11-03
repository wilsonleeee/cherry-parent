package com.cherry.ss.prm.function;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.prm.bl.BINBESSPRM04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBESSPRM04_FN implements FunctionProvider{

	@Resource
	private BINBESSPRM04_BL binbessprm04BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM03_FN.class.getName());
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织信息ID
			map.put("bin_OrganizationInfoID", transientVars.get("organizationInfoId"));
			// 品牌信息ID
			map.put("brandInfoID", transientVars.get("brandInfoId"));
			// 品牌Code
			map.put("brandCode", transientVars.get("brandCode"));
			int result = binbessprm04BL.tran_prmRuleBatch(map);
			ps.setInt("result", result);
		} catch (Exception e) {
			// 打印错误信息
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		
	}

}
