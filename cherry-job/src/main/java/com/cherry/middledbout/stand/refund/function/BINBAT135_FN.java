

package com.cherry.middledbout.stand.refund.function;

import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.middledbout.stand.refund.bl.BINBAT135_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 标准接口：退库单导入FN
 * 
 * @author chenkuan
 * 
 * @version 2015/12/24
 * 
 */
public class BINBAT135_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT135_FN.class
			.getName());

	/** 退库单导入BATCH程序 */
	@Resource(name = "binBAT135_BL")
	private BINBAT135_BL binBAT135_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************退库单导入BATCH处理开始***************************");
			int flag = binBAT135_BL.tran_batch(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************退库单导入BATCH处理开始***************************");
		}

	}

}
