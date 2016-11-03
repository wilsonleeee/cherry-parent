package com.cherry.middledbout.stand.stock.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.stock.bl.BINBAT144_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 标准接口：实时库存数据导出到标准接口表(库存表)FN
 * @author lzs
 *
 */
public class BINBAT144_FN implements FunctionProvider {
	private static Logger logger = LoggerFactory.getLogger(BINBAT144_FN.class.getName());
	/**实时库存数据导出Batch处理BL **/
	@Resource(name = "binBAT144_BL")
	private BINBAT144_BL binBAT144_BL;

	@Override
	public void execute(Map transientVars, Map arg, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("**************************实时库存数据导出Batch处理开始*******************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			
			int flag = binBAT144_BL.tran_binBAT144(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		finally{
			logger.info("***************************实时库存数据导出Batch处理结束******************************");
		}
	}

}
