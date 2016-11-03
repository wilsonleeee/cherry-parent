package com.cherry.middledbout.stand.sale.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.sale.bl.SaleInfo_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 标准接口：销售数据导出到标准接口表(销售)FN
 * @author lzs
 *
 */
public class SaleInfo_FN implements FunctionProvider {
	private static Logger logger = LoggerFactory.getLogger(SaleInfo_FN.class.getName());
	/**标准接口销售数据导出（销售）Batch处理BL **/
	@Resource(name = "saleInfo_BL")
	private SaleInfo_BL saleInfo_BL;

	@Override
	public void execute(Map transientVars, Map arg, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("**************************标准接口销售数据导出（销售）Batch处理开始*******************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			
			int flag=saleInfo_BL.tran_batchSaleInfo(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		finally{
			logger.info("***************************标准接口销售数据导出（销售）Batch处理结束******************************");
		}
	}

}
