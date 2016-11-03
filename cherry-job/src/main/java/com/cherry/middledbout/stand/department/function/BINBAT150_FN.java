package com.cherry.middledbout.stand.department.function;

import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.department.bl.BINBAT150_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 加盟商导入（以柜台形式）FN
 * 
 * @author zw
 * 
 * @version 2016/10/10
 * 
 */
public class BINBAT150_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT150_FN.class.getName());

	/** 加盟商导入BATCH程序 */
	@Resource(name = "binBAT150_BL")
	private BINBAT150_BL binBAT150_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************加盟商导入（以柜台形式）BATCH处理开始***************************");
			int flag = binBAT150_BL.tran_batch(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************加盟商导入（以柜台形式）BATCH处理结束***************************");
		}

	}

}
