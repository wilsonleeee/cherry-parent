package com.cherry.mb.arc.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.arc.bl.BINBEMBARC05_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 汇美舍官网奖励积分导入处理FN
 * @author menghao
 *
 */
public class BINBEMBARC05_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC05_FN.class.getName());
	
	/** 汇美舍官网奖励积分导入处理BL */
	@Resource(name="binBEMBARC05_BL")
	private BINBEMBARC05_BL binBEMBARC05_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************汇美舍官网奖励积分导入处理开始***************************");
			int flag = binBEMBARC05_BL.pointChangeImport(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(), e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************汇美舍官网奖励积分导入处理结束***************************");
		}
	}

}
