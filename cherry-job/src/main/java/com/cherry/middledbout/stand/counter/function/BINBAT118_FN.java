package com.cherry.middledbout.stand.counter.function;

import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.counter.bl.BINBAT118_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
*
* 标准接口：柜台信息导入FN
*
* @author lzs
*
* @version  2015-10-14
*/
public class BINBAT118_FN implements FunctionProvider {

private static Logger logger = LoggerFactory.getLogger(BINBAT118_FN.class.getName());
	
	/** 柜台信息导入BL*/
	@Resource(name="binBAT118_BL")
	private BINBAT118_BL binBAT118_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************柜台信息导入（标准接口）BATCH处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binBAT118_BL.tran_binBAT118(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************柜台信息导入（标准接口）BATCH处理开始***************************");
		}
	}

}
