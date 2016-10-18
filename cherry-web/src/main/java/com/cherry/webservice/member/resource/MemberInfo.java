package com.cherry.webservice.member.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.webservice.common.WebserviceBase;
import com.cherry.webservice.member.interfaces.MemberInfo_IF;

@Path("/memInfo")
public class MemberInfo extends WebserviceBase{

	private static Logger logger = LoggerFactory.getLogger(MemberInfo.class.getName());

	@Resource(name = "memberInfoLogic")
	private MemberInfo_IF memberInfoLogic;

	//获取会员信息
	public static final String TradeType_GetMemberInfo = "GetMemberInfo";
	// 获取会员积分
	public static final String TradeType_GetMemberPoint = "GetMemberPoint";
	// 获取最新积分变化
	public static final String TradeType_GetPointChange = "GetPointChange";
	// 微信认证
	public static final String TradeType_Authenticate = "Authenticate";
	// 微信号检测
	public static final String TradeType_CheckWeixinID = "CheckWeixinID";	

	/**
	 * 查询会员基本信息目前珀莱雅已经在使用，不可擅动，其它的接口将以新的结构提供，请见WebserviceEntrance类
	 * @param brandCode
	 * @param paramData
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	public String getMemberInfo(@QueryParam("brandCode") String brandCode, @QueryParam("paramData") String paramData,@Context HttpServletRequest request) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			//调用父类方法，解析参数，处理数据源
			Map<String, Object> paramMap = new HashMap<String, Object>();
			boolean parseFlag = this.parseParam(brandCode, paramData,paramMap);
			if(!parseFlag){
				return getEncryptReturnString(null,paramMap);
			}
			
			if(CherryChecker.isNullOrEmpty(paramMap.get("TradeType"))){
				retMap.put("ERRORCODE", "E0001");
				retMap.put("ERRORMSG", "参数TradeType是必须的");
				return getEncryptReturnString(null,retMap);
			}
			String tradeType = String.valueOf(paramMap.get("TradeType"));

			if (tradeType.equals(TradeType_GetMemberInfo)){
				// 查询会员基本信息			
				return getEncryptReturnString(brandCode,memberInfoLogic.getMemberInfo(paramMap));
			}else{
				retMap.put("ERRORCODE", "E0002");
				retMap.put("ERRORMSG", "参数TradeType不正确");
				return getEncryptReturnString(null,retMap);
			}
	
		} catch (Exception ex) {
			logger.error("WS ERROR:", ex);
			logger.error("WS ERROR brandCode:"+ brandCode);
			logger.error("WS ERROR paramData:"+ paramData);
			retMap.put("ERRORCODE", "E0099");
			retMap.put("ERRORMSG", "处理过程中发生未知异常。");
			return getEncryptReturnString(null,retMap);
		}
	}
	
//	@GET
//	 @Produces(MediaType.TEXT_PLAIN)
//	 public String validate(@DefaultValue("") @QueryParam("authCode") String authCode,
//	       @Context HttpServletRequest request){
//	  String flag = "false";
//	  String sessionAuthCode = ""+request.getSession().getAttribute("authCode");
//	  if(sessionAuthCode != null && sessionAuthCode.equalsIgnoreCase(authCode)){
//	   flag = "true";
//	  }
//	  return flag;
//	 }


}
