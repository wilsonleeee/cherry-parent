
package com.cherry.ot.yin.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.yin.bl.BINBAT116_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *  颖通接口：礼品领用单据导出(颖通)FN
 * 
 * @author lzs
 * 
 * @version 2015-07-03
 * 
 */
public class BINBAT116_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT116_FN.class.getName());
	
	/** 礼品领用单据导出(颖通)BL */
	@Resource(name = "binBAT116_BL")
	private BINBAT116_BL binBAT116_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		try {
			logger.info("****************************** 礼品领用单据导出(颖通)处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binBAT116_BL.tran_batchExportGiftDraw(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("****************************** 礼品领用单据导出(颖通)处理结束***************************");
		}

	}

}
