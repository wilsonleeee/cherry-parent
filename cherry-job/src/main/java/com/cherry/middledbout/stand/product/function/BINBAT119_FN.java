package com.cherry.middledbout.stand.product.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.product.bl.BINBAT119_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 标准接口:产品信息导出至标准接口表(IF_Product)FN
 * @author lzs
 * 下午2:23:42
 */
public class BINBAT119_FN implements FunctionProvider {
	private static Logger logger = LoggerFactory.getLogger(BINBAT119_FN.class.getName());
	
	/**标准接口产品信息Batch数据导出处理BL **/
	@Resource(name = "binbat119_BL")
	private BINBAT119_BL binbat119_BL;

	@Override
	public void execute(Map transientVars, Map arg, PropertySet ps) throws WorkflowException {
		try {
			logger.info("**************************标准接口产品信息Batch数据导出处理开始*******************************");
			//Job运行履历表的运行方式->自动运行标志
			transientVars.put("RunType", "AT");
			int flag=binbat119_BL.tran_batchProductExp(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			//打印错误日志信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		finally{
			logger.info("***************************标准接口产品信息Batch数据导出处理结束******************************");
		}
	}

}
