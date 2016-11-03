package com.cherry.job.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;

/**
 * 夜间Batch工作流运行出错时要通知相关人员
 * 
 * @author dingyongchang
 * 
 */
public class BatchErrorNotice {
	private static Logger logger = LoggerFactory.getLogger(BatchErrorNotice.class.getName());
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;

	public void execute(int organizationInfoId, int brandInfoId, String orgCode, String brandCode,String brandName) {
		try {
			if (organizationInfoId == 0 || brandInfoId == 0) {
				return;
			}
			String config = binOLCM14_BL.getConfigValue("1096", String.valueOf(organizationInfoId), String.valueOf(brandInfoId));
			if (config == null || "".equals(config) || "NULL".equalsIgnoreCase(config)) {
				return;
			}
			// 用;号分拆，
			String[] conArr = config.split(";");
			for (String tmp : conArr) {
				if (tmp.indexOf("@") > 0) {
					// 如果是邮件
				} else {
					// 不是邮件则认为是手机号码，这里不做手机号码的校验，沟通发送里会校验
					MQInfoDTO mqInfoDTO = new MQInfoDTO();
					// 品牌代码
					mqInfoDTO.setBrandCode(brandCode);

					String billType = "GT";

					String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);

					// 品牌代码
					mqInfoDTO.setBrandCode(brandCode);
					// 组织代码
					mqInfoDTO.setOrgCode(orgCode);
					// 组织ID
					mqInfoDTO.setOrganizationInfoId(organizationInfoId);
					// 品牌ID
					mqInfoDTO.setBrandInfoId(brandInfoId);
					// 业务类型
					mqInfoDTO.setBillType(billType);
					// 单据号
					mqInfoDTO.setBillCode(billCode);
					// 消息发送队列名
					mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);

					// 设定消息内容
					Map<String, Object> msgDataMap = new HashMap<String, Object>();
					// 设定消息版本号
					msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
					// 设定消息命令类型
					msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
					// 设定消息数据类型
					msgDataMap.put("DataType", CherryConstants.DATATYPE_APPLICATION_JSON);
					// 设定消息的数据行
					Map<String, Object> dataLine = new HashMap<String, Object>();
					// 消息的主数据行
					Map<String, Object> mainData = new HashMap<String, Object>();
					mainData.put("BrandCode", brandCode);
					mainData.put("TradeType", billType);
					mainData.put("TradeNoIF", billCode);
					mainData.put("EventType", "100");
					mainData.put("EventId", tmp);
					SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = new Date();
					String nowStr = dateFormat2.format(now);
					mainData.put("EventDate",nowStr );
					mainData.put("MessageContents", "Batch工作流运行出错，品牌：" + brandName +"，时间 "+nowStr+" "+PropertiesUtil.getMessage("MessageSignature", null));
					mainData.put("Sourse", "BatchWorkflow");
					dataLine.put("MainData", mainData);
					msgDataMap.put("DataLine", dataLine);
					mqInfoDTO.setMsgDataMap(msgDataMap);
					// 发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, true);
				}

			}

		} catch (Exception e) {
			logger.error("Batch工作流未能正常完成，尝试发送通知信息时出错：", e);
		}
	}
}
