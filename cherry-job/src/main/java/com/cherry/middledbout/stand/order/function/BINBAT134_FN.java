

package com.cherry.middledbout.stand.order.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.order.bl.BINBAT132_BL;
import com.cherry.middledbout.stand.order.bl.BINBAT134_BL;
import com.cherry.ot.yin.bl.BINOTYIN02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 标准接口：发货单导入FN
 * 
 * @author chenkuan
 * 
 * @version 2015/12/15
 * 
 */
public class BINBAT134_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT134_FN.class
			.getName());

	/** 发货单退库单导入BATCH程序 */
	@Resource(name = "binBAT134_BL")
	private BINBAT134_BL binBAT134_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************发货单退库单导入BATCH处理开始***************************");
			int flag = binBAT134_BL.tran_batch(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************发货单退库单导入BATCH处理开始***************************");
		}

	}

}
