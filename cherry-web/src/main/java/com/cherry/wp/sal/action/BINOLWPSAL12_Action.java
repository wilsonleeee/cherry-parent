package com.cherry.wp.sal.action;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.pay.interfaces.AlipayIf;
import com.cherry.cm.pay.interfaces.WeChatPayIf;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.sal.form.BINOLWPSAL12_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL03_IF;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL12_Action extends BaseAction implements ModelDriven<BINOLWPSAL12_Form>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2300506742015490095L;
	
	private BINOLWPSAL12_Form form = new BINOLWPSAL12_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL12_Action.class);
	
	@Resource(name = "aliPayBL")
	private AlipayIf aliPayIF;
	
	@Resource(name = "weChatPayBL")
	private WeChatPayIf weChatPayIF;
	
	@Resource(name="binOLWPSAL03_BL")
	private BINOLWPSAL03_IF binOLWPSAL03_IF;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	public String init(){
		return SUCCESS;
	}


	// 查单（用于支付宝/微信在第一次支付时候为等待，并且没有刷新情况后的重发MQ之前的查单操作）
	public void getPayResultBySendMQ() throws Exception{
		//获取该单据的所有信息
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		//获取挂单ID
		String billId=form.getBillId();
		Map<String,Object> param=new HashMap<String,Object>();
		//通过挂单ID获取挂单表中相关数据进行SetForm操作
		param.put("billId", billId);
		param.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
		param.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		Map<String,Object> param_info=binOLWPSAL03_IF.getHangBillInfo(param);

		logger.info("补发MQ查单START单据号："+ConvertUtil.getString(param_info.get("billCode")));
		String counterCode = ConvertUtil.getString(param_info.get("counterCode"));
		String billCode = ConvertUtil.getString(param_info.get("billCode"));
		String businessDate = ConvertUtil.getString(param_info.get("businessDate"));
		String memberCode = ConvertUtil.getString(param_info.get("memberCode"));
//		String memberName = ConvertUtil.getString(form.getPayMemberName());
		String payAmount = ConvertUtil.getString(param_info.get("totalAmount"));

		List<Map<String,Object>> payDetail_list= CherryUtil.json2ArryList(ConvertUtil.getString(param_info.get("payDetailStr")));
		String payType = "";
		for(Map<String,Object> payDetail_info:payDetail_list){
			String storePayCode =ConvertUtil.getString(payDetail_info.get("storePayCode"));
			if("PT".equals(storePayCode) || "WT".equals(storePayCode)){
				payType=storePayCode;
			}
		}
		int resultCount = 1;

		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String, Object>> payResult = new ArrayList<Map<String, Object>>();
		resultMap.put("billCode", billCode);
		resultMap.put("businessDate", businessDate);
		resultMap.put("memberCode", memberCode);
//		resultMap.put("memberName", memberName);
		resultMap.put("payAmount", payAmount);
		try{
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String version = binOLCM14_BL.getWebposConfigValue("9029", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));
			if("".equals(billCode)){
				// 查单单据号为空的情况
				resultMap.put("payState", "ERROR");
				resultMap.put("payMessage", "单据号为空");
				payResult.add(resultMap);
				form.setPayResultList(payResult);
				form.setITotalDisplayRecords(resultCount);
				form.setITotalRecords(resultCount);
			}else{
				if("PT".equals(payType)){
					// 支付宝支付的情况
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("counterCode", counterCode);
					if("1.0".equals(version)){
						map.put("payType", "ALIPAY");
					}else if("2.0".equals(version)){
						map.put("payType", "ALIPAY2");
					}
					List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
					if (null != configList && !configList.isEmpty()){
						// 定义全部柜台收款账户存放Map
						Map<String, Object> allConfigMap = new HashMap<String, Object>();
						// 定义单个柜台收款账户存放Map
						Map<String, Object> counterConfigMap = new HashMap<String, Object>();

						// 从配置列表中获取配置项
						for(Map<String,Object> configMap : configList){
							String payCounter = ConvertUtil.getString(configMap.get("counterCode"));
							if("ALL".equals(payCounter)){
								// 当配置项为全部柜台情况下获取配置信息放入 allConfigMap 中
								allConfigMap.put("alipayPartnerId", ConvertUtil.getString(configMap.get("partnerId")));
								allConfigMap.put("alipaySignKey", ConvertUtil.getString(configMap.get("paternerKey")));
								allConfigMap.put("alipaySignType", ConvertUtil.getString(configMap.get("keyType")));
								allConfigMap.put("aliPayInputCharSet", ConvertUtil.getString(configMap.get("inputCharSet")));
							}else if(counterCode.equals(payCounter)){
								// 当配置项为指定柜台情况下获取配置信息放入 counterConfigMap 中
								counterConfigMap.put("alipayPartnerId", ConvertUtil.getString(configMap.get("partnerId")));
								counterConfigMap.put("alipaySignKey", ConvertUtil.getString(configMap.get("paternerKey")));
								counterConfigMap.put("alipaySignType", ConvertUtil.getString(configMap.get("keyType")));
								counterConfigMap.put("aliPayInputCharSet", ConvertUtil.getString(configMap.get("inputCharSet")));
							}
						}
						// 判断指定柜台的支付配置信息是否存在
						if (null != counterConfigMap && !counterConfigMap.isEmpty()){
							Map<String, Object> parMap = new HashMap<String, Object>();
							parMap.putAll(counterConfigMap);
							parMap.put("strOutTradeNo", billCode);
							if("1.0".equals(version)){
								List<Map<String, Object>> returnList = aliPayIF.getAlipayQuery(parMap);
								payResult = getAliPayQueryResult(returnList, resultMap);
								resultCount = returnList.size();
							}else if("2.0".equals(version)){
								List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryTwo(parMap);
								payResult = getAliPayQueryResult(returnList, resultMap);
								resultCount = returnList.size();
							}
						}else{
							if (null != allConfigMap && !allConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(allConfigMap);
								parMap.put("strOutTradeNo", billCode);
								if("1.0".equals(version)){
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQuery(parMap);
									payResult = getAliPayQueryResult(returnList, resultMap);
									resultCount = returnList.size();
								}else if("2.0".equals(version)){
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryTwo(parMap);
									payResult = getAliPayQueryResult(returnList, resultMap);
									resultCount = returnList.size();
								}
							}else{
								// 没有给指定柜台配置收款账户信息的情况
								resultMap.put("payState", "ERROR");
								resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
								payResult.add(resultMap);
							}
						}
					}else{
						// 没有给指定柜台配置收款账户信息的情况
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
						payResult.add(resultMap);
					}
				}else if("WEPAY".equals(payType)){
					// 微信支付的情况
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("counterCode", counterCode);
					map.put("payType", "WECHATPAY");
					List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
					if (null != configList && !configList.isEmpty()){
						// 定义全部柜台收款账户存放Map
						Map<String, Object> allConfigMap = new HashMap<String, Object>();
						// 定义单个柜台收款账户存放Map
						Map<String, Object> counterConfigMap = new HashMap<String, Object>();

						// 从配置列表中获取配置项
						for(Map<String,Object> configMap : configList){
							String payCounter = ConvertUtil.getString(configMap.get("counterCode"));
							if("ALL".equals(payCounter)){
								// 当配置项为全部柜台情况下获取配置信息放入 allConfigMap 中
								allConfigMap.put("appid", ConvertUtil.getString(configMap.get("appId")));
								allConfigMap.put("mch_id", ConvertUtil.getString(configMap.get("partnerId")));
								allConfigMap.put("sub_mch_id", ConvertUtil.getString(configMap.get("subMchId")));
								allConfigMap.put("paternerKey", ConvertUtil.getString(configMap.get("paternerKey")));
							}else if(counterCode.equals(payCounter)){
								// 当配置项为指定柜台情况下获取配置信息放入 counterConfigMap 中
								counterConfigMap.put("appid", ConvertUtil.getString(configMap.get("appId")));
								counterConfigMap.put("mch_id", ConvertUtil.getString(configMap.get("partnerId")));
								counterConfigMap.put("sub_mch_id", ConvertUtil.getString(configMap.get("subMchId")));
								counterConfigMap.put("paternerKey", ConvertUtil.getString(configMap.get("paternerKey")));
							}
						}
						// 判断指定柜台的支付配置信息是否存在
						if (null != counterConfigMap && !counterConfigMap.isEmpty()){
							Map<String, Object> parMap = new HashMap<String, Object>();
							parMap.putAll(counterConfigMap);
							parMap.put("out_trade_no", billCode);
							List<Map<String, Object>> returnList = weChatPayIF.getOrderQuery(parMap);
							payResult = getWechatPayQueryResult(returnList, resultMap);
							resultCount = returnList.size();
						}else{
							if (null != allConfigMap && !allConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(allConfigMap);
								parMap.put("out_trade_no", billCode);
								List<Map<String, Object>> returnList = weChatPayIF.getOrderQuery(parMap);
								payResult = getWechatPayQueryResult(returnList, resultMap);
								resultCount = returnList.size();
							}else{
								// 没有给指定柜台配置收款账户信息的情况
								resultMap.put("payState", "ERROR");
								resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
								payResult.add(resultMap);
							}
						}
					}else{
						// 没有给指定柜台配置收款账户信息的情况
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
						payResult.add(resultMap);
					}
				}else{
					// 支付类型为非支付宝和微信支付时
					resultMap.put("payState", "ERROR");
					resultMap.put("payMessage", "支付类型不是支付宝或微信");
					payResult.add(resultMap);
				}
			}
			logger.info("补发MQ查单END单据号："+billCode+"查单结果为："+ConvertUtil.getString(payResult));
			ConvertUtil.setResponseByAjax(response, payResult.get(0));
		}catch(Exception e){
			logger.info("补发MQ查单ERROR单据号："+billCode);
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				// 返回查单结果为失败
				resultMap.put("payState", "ERROR");
				resultMap.put("payMessage", temp.getErrMessage());
				payResult.add(resultMap);
			}else{
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				// 返回查单结果为失败
				resultMap.put("payState", "ERROR");
				resultMap.put("payMessage", getText("ECM00036"));
				payResult.add(resultMap);
			}
			ConvertUtil.setResponseByAjax(response, payResult.get(0));
		}
	}

	// 查单
	public String getPayResult() throws Exception{
		logger.info("查单弹框打开START单据号："+form.getPayBillCode());
		String counterCode = ConvertUtil.getString(form.getPayCounterCode());
		String billCode = ConvertUtil.getString(form.getPayBillCode());
		String businessDate = ConvertUtil.getString(form.getPayBillTime());
		String memberCode = ConvertUtil.getString(form.getPayMemberCode());
		String memberName = ConvertUtil.getString(form.getPayMemberName());
		String payAmount = ConvertUtil.getString(form.getPayAmount());
		int resultCount = 1;
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String, Object>> payResult = new ArrayList<Map<String, Object>>();
		resultMap.put("billCode", billCode);
		resultMap.put("businessDate", businessDate);
		resultMap.put("memberCode", memberCode);
		resultMap.put("memberName", memberName);
		resultMap.put("payAmount", payAmount);
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String version = binOLCM14_BL.getWebposConfigValue("9029", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));
			if("".equals(billCode)){
				// 查单单据号为空的情况
				resultMap.put("payState", "ERROR");
				resultMap.put("payMessage", "单据号为空");
				payResult.add(resultMap);
				form.setPayResultList(payResult);
				form.setITotalDisplayRecords(resultCount);
				form.setITotalRecords(resultCount);
			}else{
				String payType = ConvertUtil.getString(form.getPayType());
				if("AL".equals(payType)){
					// 支付宝支付的情况
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("counterCode", counterCode);
					if("1.0".equals(version)){
						map.put("payType", "ALIPAY");
					}else if("2.0".equals(version)){
						map.put("payType", "ALIPAY2");
					}
					List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
					if (null != configList && !configList.isEmpty()){
						// 定义全部柜台收款账户存放Map
						Map<String, Object> allConfigMap = new HashMap<String, Object>();
						// 定义单个柜台收款账户存放Map
						Map<String, Object> counterConfigMap = new HashMap<String, Object>();
						
						// 从配置列表中获取配置项
						for(Map<String,Object> configMap : configList){
							String payCounter = ConvertUtil.getString(configMap.get("counterCode"));
							if("ALL".equals(payCounter)){
								// 当配置项为全部柜台情况下获取配置信息放入 allConfigMap 中
								allConfigMap.put("alipayPartnerId", ConvertUtil.getString(configMap.get("partnerId")));
								allConfigMap.put("alipaySignKey", ConvertUtil.getString(configMap.get("paternerKey")));
								allConfigMap.put("alipaySignType", ConvertUtil.getString(configMap.get("keyType")));
								allConfigMap.put("aliPayInputCharSet", ConvertUtil.getString(configMap.get("inputCharSet")));
							}else if(counterCode.equals(payCounter)){
								// 当配置项为指定柜台情况下获取配置信息放入 counterConfigMap 中
								counterConfigMap.put("alipayPartnerId", ConvertUtil.getString(configMap.get("partnerId")));
								counterConfigMap.put("alipaySignKey", ConvertUtil.getString(configMap.get("paternerKey")));
								counterConfigMap.put("alipaySignType", ConvertUtil.getString(configMap.get("keyType")));
								counterConfigMap.put("aliPayInputCharSet", ConvertUtil.getString(configMap.get("inputCharSet")));
							}
						}
						// 判断指定柜台的支付配置信息是否存在
						if (null != counterConfigMap && !counterConfigMap.isEmpty()){
							Map<String, Object> parMap = new HashMap<String, Object>();
							parMap.putAll(counterConfigMap);
							parMap.put("strOutTradeNo", billCode);
							if("1.0".equals(version)){
								List<Map<String, Object>> returnList = aliPayIF.getAlipayQuery(parMap);
								payResult = getAliPayQueryResult(returnList, resultMap);
								resultCount = returnList.size();
							}else if("2.0".equals(version)){
								List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryTwo(parMap);
								payResult = getAliPayQueryResult(returnList, resultMap);
								resultCount = returnList.size();
							}
						}else{
							if (null != allConfigMap && !allConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(allConfigMap);
								parMap.put("strOutTradeNo", billCode);
								if("1.0".equals(version)){
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQuery(parMap);
									payResult = getAliPayQueryResult(returnList, resultMap);
									resultCount = returnList.size();
								}else if("2.0".equals(version)){
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryTwo(parMap);
									payResult = getAliPayQueryResult(returnList, resultMap);
									resultCount = returnList.size();
								}
							}else{
								// 没有给指定柜台配置收款账户信息的情况
								resultMap.put("payState", "ERROR");
								resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
								payResult.add(resultMap);
							}
						}
					}else{
						// 没有给指定柜台配置收款账户信息的情况
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
						payResult.add(resultMap);
					}
				}else if("WT".equals(payType)){
					// 微信支付的情况
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("counterCode", counterCode);
					map.put("payType", "WECHATPAY");
					List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
					if (null != configList && !configList.isEmpty()){
						// 定义全部柜台收款账户存放Map
						Map<String, Object> allConfigMap = new HashMap<String, Object>();
						// 定义单个柜台收款账户存放Map
						Map<String, Object> counterConfigMap = new HashMap<String, Object>();
						
						// 从配置列表中获取配置项
						for(Map<String,Object> configMap : configList){
							String payCounter = ConvertUtil.getString(configMap.get("counterCode"));
							if("ALL".equals(payCounter)){
								// 当配置项为全部柜台情况下获取配置信息放入 allConfigMap 中
								allConfigMap.put("appid", ConvertUtil.getString(configMap.get("appId")));
								allConfigMap.put("mch_id", ConvertUtil.getString(configMap.get("partnerId")));
								allConfigMap.put("sub_mch_id", ConvertUtil.getString(configMap.get("subMchId")));
								allConfigMap.put("paternerKey", ConvertUtil.getString(configMap.get("paternerKey")));
							}else if(counterCode.equals(payCounter)){
								// 当配置项为指定柜台情况下获取配置信息放入 counterConfigMap 中
								counterConfigMap.put("appid", ConvertUtil.getString(configMap.get("appId")));
								counterConfigMap.put("mch_id", ConvertUtil.getString(configMap.get("partnerId")));
								counterConfigMap.put("sub_mch_id", ConvertUtil.getString(configMap.get("subMchId")));
								counterConfigMap.put("paternerKey", ConvertUtil.getString(configMap.get("paternerKey")));
							}
						}
						// 判断指定柜台的支付配置信息是否存在
						if (null != counterConfigMap && !counterConfigMap.isEmpty()){
							Map<String, Object> parMap = new HashMap<String, Object>();
							parMap.putAll(counterConfigMap);
							parMap.put("out_trade_no", billCode);
							List<Map<String, Object>> returnList = weChatPayIF.getOrderQuery(parMap);
							payResult = getWechatPayQueryResult(returnList, resultMap);
							resultCount = returnList.size();
						}else{
							if (null != allConfigMap && !allConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(allConfigMap);
								parMap.put("out_trade_no", billCode);
								List<Map<String, Object>> returnList = weChatPayIF.getOrderQuery(parMap);
								payResult = getWechatPayQueryResult(returnList, resultMap);
								resultCount = returnList.size();
							}else{
								// 没有给指定柜台配置收款账户信息的情况
								resultMap.put("payState", "ERROR");
								resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
								payResult.add(resultMap);
							}
						}
					}else{
						// 没有给指定柜台配置收款账户信息的情况
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "没有给指定柜台配置收款账户信息");
						payResult.add(resultMap);
					}
				}else if("CZK".equals(payType)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("billCode", billCode);
					//查询储值卡的支付状态
					int czkPayCount=binOLWPSAL03_IF.getCZKPayStateCount(map);
					if(czkPayCount >0){
						resultMap.put("payState", "SUCCESS");
						resultMap.put("payMessage", "支付成功");
						payResult.add(resultMap);
					}else{
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "储值卡收款失败，请稍后重试");
						payResult.add(resultMap);
					}
				}else{
					// 支付类型为非支付宝和微信支付时
					resultMap.put("payState", "ERROR");
					resultMap.put("payMessage", "支付类型不是支付宝或微信或储值卡支付");
					payResult.add(resultMap);
				}
				form.setPayResultList(payResult);
				form.setITotalDisplayRecords(resultCount);
				form.setITotalRecords(resultCount);
			}
			logger.info("单据号"+billCode+"查单结果为："+ConvertUtil.getString(payResult));
			logger.info("查单弹框打开END单据号："+form.getPayBillCode());
		}catch(Exception e){
			logger.info("查单弹框打开ERROR单据号："+form.getPayBillCode());
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				// 返回查单结果为失败
				resultMap.put("payState", "ERROR");
				resultMap.put("payMessage", temp.getErrMessage());
				payResult.add(resultMap);
				form.setPayResultList(payResult);
				form.setITotalDisplayRecords(resultCount);
				form.setITotalRecords(resultCount);
			 }else{
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				// 返回查单结果为失败
				resultMap.put("payState", "ERROR");
				resultMap.put("payMessage", getText("ECM00036"));
				payResult.add(resultMap);
				form.setPayResultList(payResult);
				form.setITotalDisplayRecords(resultCount);
				form.setITotalRecords(resultCount);
			 }
		}
		return SUCCESS;
	}
	
	// 获取支付宝支付退款结果
	private List<Map<String, Object>> getAliPayQueryResult(List<Map<String, Object>> returnList, Map<String, Object> resultMap) throws Exception{
		List<Map<String, Object>> payResultList = new ArrayList<Map<String, Object>>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String version = binOLCM14_BL.getWebposConfigValue("9029", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));
		if(returnList != null && !returnList.isEmpty()) {
			for(Map<String,Object> returnMap : returnList){
				String returnCode = ConvertUtil.getString(returnMap.get("is_success"));
				if("T".equals(returnCode)){
					String resultCode = ConvertUtil.getString(returnMap.get("result_code"));
					if("2.0".equals(version) && "10000".equals(resultCode)){
						resultCode="SUCCESS";
					}
					if("SUCCESS".equals(resultCode)){
						String tradeStatus = ConvertUtil.getString(returnMap.get("trade_status"));
						if("TRADE_SUCCESS".equals(tradeStatus)){
							resultMap.put("payState", "SUCCESS");
							resultMap.put("payMessage", "支付成功");
							payResultList.add(resultMap);
						}else if("WAIT_BUYER_PAY".equals(tradeStatus)){
							resultMap.put("payState", "WAIT");
							resultMap.put("payMessage", "等待付款,请刷新支付结果");
							payResultList.add(resultMap);
						}else if("TRADE_PENDING".equals(tradeStatus)){
							resultMap.put("payState", "WAIT");
							resultMap.put("payMessage", "等待收款,请刷新支付结果");
							payResultList.add(resultMap);
						}else if("TRADE_FINISHED".equals(tradeStatus)){
							resultMap.put("payState", "SUCCESS");
							resultMap.put("payMessage", "交易已成功");
							payResultList.add(resultMap);
						}else if("TRADE_CLOSED".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "未在指定时间内付款，单据关闭");
							payResultList.add(resultMap);
						}
					}else{
						// 查单失败的情况
						String detailError = ConvertUtil.getString(returnMap.get("detail_error_des"));
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "查单失败，" + detailError);
						payResultList.add(resultMap);
					}
				}else{
					// 支付宝支付接口通讯失败的情况
					resultMap.put("payState", "ERROR");
					resultMap.put("payMessage", "连接支付宝接口失败，请刷新再试");
					payResultList.add(resultMap);
				}
			}
		}else{
			// 没有获取到返回结果的情况
			resultMap.put("payState", "ERROR");
			resultMap.put("payMessage", "没有获取到接口返回结果");
			payResultList.add(resultMap);
		}
		logger.info("调用支付宝“收单查询”接口alipay.acquire.query，页面返回值："+ConvertUtil.getString(payResultList));
		return payResultList;
	}
	
	// 获取微信支付退款结果
	private List<Map<String, Object>> getWechatPayQueryResult(List<Map<String, Object>> returnList, Map<String, Object> resultMap) throws Exception{
		List<Map<String, Object>> payResultList = new ArrayList<Map<String, Object>>();
		if(returnList != null && !returnList.isEmpty()) {
			for(Map<String,Object> returnMap : returnList){
				String returnCode = ConvertUtil.getString(returnMap.get("return_code"));
				if("SUCCESS".equals(returnCode)){
					String resultCode = ConvertUtil.getString(returnMap.get("result_code"));
					if("SUCCESS".equals(resultCode)){
						String tradeStatus = ConvertUtil.getString(returnMap.get("trade_state"));
						if("SUCCESS".equals(tradeStatus)){
							resultMap.put("payState", "SUCCESS");
							resultMap.put("payMessage", "支付成功");
							payResultList.add(resultMap);
						}else if("REFUND".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "已转入退款");
							payResultList.add(resultMap);
						}else if("NOTPAY".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "未支付，放弃本单，请重新下单");
							payResultList.add(resultMap);
						}else if("CLOSED".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "已关闭，放弃本单，请重新下单");
							payResultList.add(resultMap);
						}else if("REVOKED".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "已撤销，放弃本单，请重新下单");
							payResultList.add(resultMap);
						}else if("USERPAYING".equals(tradeStatus)){
							resultMap.put("payState", "WAIT");
							resultMap.put("payMessage", "用户支付中");
							payResultList.add(resultMap);
						}else if("NOPAY".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "支付超时，放弃本单，请重新下单");
							payResultList.add(resultMap);
						}else if("PAYERROR".equals(tradeStatus)){
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "支付失败，放弃本单，请重新下单");
							payResultList.add(resultMap);
						}else{
							resultMap.put("payState", "ERROR");
							resultMap.put("payMessage", "支付失败，请刷新再试");
							payResultList.add(resultMap);
						}
					}else{
						resultMap.put("payState", "ERROR");
						resultMap.put("payMessage", "订单不存在，本单放弃，请重新下单");
						payResultList.add(resultMap);
					}
				}else{
					// 微信支付接口通讯失败的情况
					resultMap.put("payState", "WAIT");
					resultMap.put("payMessage", "连接微信支付接口失败，请刷新再试");
					payResultList.add(resultMap);
				}
			}
		}else{
			// 没有获取到返回结果的情况
			resultMap.put("payState", "ERROR");
			resultMap.put("payMessage", "没有获取到接口返回结果");
			payResultList.add(resultMap);
		}
		return payResultList;
	}

	@Override
	public BINOLWPSAL12_Form getModel() {
		return form;
	}
}
