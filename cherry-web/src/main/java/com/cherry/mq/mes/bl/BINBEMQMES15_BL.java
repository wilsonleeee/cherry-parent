package com.cherry.mq.mes.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.webservice.client.WebserviceClient;

public class BINBEMQMES15_BL implements CherryMessageHandler_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES15_BL.class);
	
	/** 接收MQ消息共通 BL **/
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		
		Map<String, Object> memInfo = binBEMQMES99_Service.getMemberInfoByCode(map);
		if(memInfo != null) {
			String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
			String brandInfoID = String.valueOf(map.get("brandInfoID"));
			// 发送微信模板消息URL
			String weChatTempWebServiceUrl = binOLCM14_BL.getConfigValue("1353", organizationInfoID, brandInfoID);
			if(weChatTempWebServiceUrl == null || "".equals(weChatTempWebServiceUrl)) {
				return;
			}
			if("sendWeChatTempMsgByOther".equals(weChatTempWebServiceUrl)) {
				String mobilePhone = (String)memInfo.get("mobilePhone");
				if(mobilePhone != null && !"".equals(mobilePhone)) {
					map.put("mobilePhone", mobilePhone);
					sendWeChatTempMsgByOther(map);
				}
			} else {
				String messageId = (String)memInfo.get("messageId");
				// 绑定过微信的会员才能发送模板消息
				if(messageId != null && !"".equals(messageId)) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("openID", messageId);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("MemberCode", ConvertUtil.getString(map.get("memberCode")));
					data.put("MemName", ConvertUtil.getString(map.get("memName")));
					data.put("PointType", ConvertUtil.getString(map.get("pointType")));
					data.put("NewPoint", ConvertUtil.getString(map.get("newPoint")));
					data.put("ChangeTime", ConvertUtil.getString(map.get("changeTime")));
					data.put("CounterName", ConvertUtil.getString(map.get("counterName")));
					data.put("TotalPoint", ConvertUtil.getString(map.get("totalPoint")));
					data.put("SaleType", ConvertUtil.getString(map.get("saleType")));
					data.put("SaleAmount", ConvertUtil.getString(map.get("saleAmount")));
					param.put("data", CherryUtil.obj2Json(data));
					Map<String, Object> resultMap = WebserviceClient.accessWeChatWebService(weChatTempWebServiceUrl, param);
					String errcode = ConvertUtil.getString(resultMap.get("errcode"));
					if(errcode != null && !"0".equals(errcode)) {
						logger.error("weChatTempWebServiceUrl error: "+resultMap);
					}
				}
			}
		}
	}
	
	public void sendWeChatTempMsgByOther(Map<String, Object> map) {
		try {
			String endPoint="http://www.wetherm.com/Api/WXOA.asmx";
			String namespace = "http://tempuri.org/";
			String opName = "SendMessage";
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endPoint));
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(namespace+opName);
			call.setOperationName(new QName(namespace,opName));    
			call.addParameter(new QName(namespace,"json"), org.apache.axis.Constants.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			call.setTimeout(10000);
	    
			Map<String, String> webMap = new HashMap<String, String>();
			webMap.put("appID", "#$%6shfwetherm^&0()s&*");
			webMap.put("MemberPhone", ConvertUtil.getString(map.get("mobilePhone")));
			webMap.put("time", ConvertUtil.getString(map.get("changeTime")));
			webMap.put("org", ConvertUtil.getString(map.get("counterName")));
			String saleType = ConvertUtil.getString(map.get("saleType"));
			if("NS".equals(saleType)) {
				webMap.put("type", "购买");
			} else if("SR".equals(saleType)) {
				webMap.put("type", "退货");
			} else if("PX".equals(saleType)) {
				webMap.put("type", "积分兑换");
			}
			webMap.put("money", ConvertUtil.getString(map.get("saleAmount")));
			webMap.put("point", ConvertUtil.getString(map.get("totalPoint")));
			String param = CherryAESCoder.encrypt(CherryUtil.obj2Json(webMap),"sO4rcLIxxGysDsA4OKRVCg==");
			String str = (String)call.invoke( new Object[]{param});
			Map<String, Object> resultMap = CherryUtil.json2Map(str);
			String errcode = ConvertUtil.getString(resultMap.get("errcode"));
			if(!"0".equals(errcode)) {
				//System.out.println("sendWeChatTempMsgByOther error: "+str);
				logger.error("sendWeChatTempMsgByOther error: "+str);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}   
	}
	
//	public static void main(String[] args) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("mobilePhone", "13800138000");
//		map.put("counterName", "上海人民广场旗舰店");
//		map.put("changeTime", "2016-06-23");
//		map.put("saleType", "NS");
//		map.put("saleAmount", "200");
//		map.put("totalPoint", "200");
//		sendWeChatTempMsgByOther(map);
//	}

}
