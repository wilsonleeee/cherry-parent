package com.cherry.middledbout.stand.member.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.member.bl.MemberInfo_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 标准接口:会员数据导出至标准接口表(IF_MemberInfo)FN
 * @author lzs
 * 下午1:54:03
 */
public class MemberInfo_FN implements FunctionProvider {
	private static Logger logger = LoggerFactory.getLogger(MemberInfo_FN.class.getName());
	
	/**标准接口会员数据导出Batch处理BL **/
	@Resource(name = "memberInfo_BL")
	private MemberInfo_BL memberInfo_BL;

	@Override
	public void execute(Map transientVars, Map arg, PropertySet ps) throws WorkflowException {
		try {
			logger.info("**************************标准接口会员数据导出Batch处理开始*******************************");
			transientVars.put("RunType", "AT");
			int flag=memberInfo_BL.tran_batchMemberInfo(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			//打印错误日志信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		finally{
			logger.info("***************************标准接口会员数据导出Batch处理结束******************************");
		}
	}

}
