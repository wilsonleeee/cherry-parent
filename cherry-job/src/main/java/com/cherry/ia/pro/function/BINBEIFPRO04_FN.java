package com.cherry.ia.pro.function;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.util.CherryBatchUtil;
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

			// 发送MQ
			String isSendMQ = ConvertUtil.getString(transientVars.get("IsSendMQ"));
			int result=ConvertUtil.getInt(resMap.get("flag"));
			if(result == CherryBatchConstants.BATCH_SUCCESS && !CherryBatchUtil.isBlankString(isSendMQ)) {
				// 备份产品下发数据/MQ下发
				Map<String, Object> flagMapMQ = binbeifpro04BL.tran_batchProductsMQSend(transientVars);
				resMap.putAll(flagMapMQ);
			}
			result=ConvertUtil.getInt(resMap.get("flag"));

			binbeifpro04BL.outMessage();
			binbeifpro04BL.tran_programEnd(transientVars);

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
