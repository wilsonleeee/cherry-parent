

package com.cherry.middledbout.stand.order.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.order.bl.BINBAT132_BL;
import com.cherry.ot.yin.bl.BINOTYIN02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 标准接口：产品订单导出FN
 * 
 * @author chenkuan
 * 
 * @version 2015/12/15
 * 
 */
public class BINBAT132_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT132_FN.class
			.getName());

	/** 订单详细数据导出到标准接口表BL */
	@Resource(name = "binBAT132_BL")
	private BINBAT132_BL binBAT132_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************标准产品订单导出处理开始***************************");
			int flag = binBAT132_BL.tran_batchExportPrtOrder(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************标准产品订单导出处理结束***************************");
		}

	}

}
