package com.cherry.ia.pro.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEIFPRO04_FN implements FunctionProvider{
	
	@Resource
	private BINBEIFPRO04_BL binbeifpro04BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFPRO04_FN.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		logger.info("******************************产品下发(实时)BATCH处理开始***************************");
		try {
			transientVars.put("RunType", "AT");
			Map<String,Object> resMap= binbeifpro04BL.tran_batchProducts(transientVars);
			int result=ConvertUtil.getInt(resMap.get("flag"));
			ps.setInt("result", result);
		} catch (CherryBatchException e) {
			logger.error(e.toString(),e);
		} catch (CherryException e) {
			// TODO Auto-generated catch block
			logger.error(e.toString(),e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString(),e);
		}catch(Throwable t){
			logger.error(t.getMessage(),t);
			
		}
		logger.info("******************************产品下发(实时)BATCH处理结束***************************");
	}

}
