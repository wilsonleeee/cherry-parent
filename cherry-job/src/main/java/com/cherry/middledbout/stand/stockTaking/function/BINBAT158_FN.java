package com.cherry.middledbout.stand.stockTaking.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.stockTaking.bl.BINBAT157_BL;
import com.cherry.middledbout.stand.stockTaking.bl.BINBAT158_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 盘点审核单导入标准接口
 * @author Wangmz
 *
 */
public class BINBAT158_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT158_FN.class.getName());
	
	@Resource(name = "binBAT158_BL")
	private BINBAT158_BL binBAT158_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		
		try {
			logger.info("******************************盘点审核单导入(标准接口)处理开始***************************");
			//Job运行履历表的运行方式->自动运行标志
			transientVars.put("RunType", "AT");
			int flag = binBAT158_BL.tran_batch(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************盘点审核单导入(标准接口)处理结束***************************");
		}
		
	}

}
