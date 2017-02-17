package com.cherry.wp.sal.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.pay.interfaces.AlipayIf;
import com.cherry.cm.pay.interfaces.WeChatPayIf;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pos.bl.BINOLMOPOS01_BL;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.wp.sal.bl.BINOLWPSAL13_BL;
import com.cherry.wp.sal.form.BINOLWPSAL03_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL03_IF;
import com.cherry.wp.sal.interfaces.BINOLWPSAL07_IF;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL03_Action extends BaseAction implements ModelDriven<BINOLWPSAL03_Form>{

	/**
	 *
	 */
	static{
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO("pekonws");
		SavingscardWebServiceUrl = wsconfigDTO.getWebserviceURL();//PropertiesUtil.pps.getProperty("SavingscardWebServiceUrl");
		SavingscardAppID = wsconfigDTO.getAppID();//PropertiesUtil.pps.getProperty("SavingscardAppID");
	}
	private static String SavingscardWebServiceUrl;
	private static String SavingscardAppID;
	private static final long serialVersionUID = 1L;

	private BINOLWPSAL03_Form form = new BINOLWPSAL03_Form();

	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL03_Action.class);

	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	@Resource
	private BINOLCM27_BL binOLCM27_BL;

	/**查询服务卡信息*/
	@Resource
	private BINOLWPSAL13_BL binOLWPSAL13_BL;

	/**查询储值卡退货信息*/
	@Resource(name="binOLWPSAL07_BL")
	private BINOLWPSAL07_IF binOLWPSAL07_IF;

	/**查询门店销售支付方式配置*/
	@Resource
	private BINOLMOPOS01_BL binOLMOPOS01_BL;

	/**查询调用Penkonws接口的密钥信息*/
	@Resource(name="thirdPartyConfig")
	private ThirdPartyConfig thirdPartyConfig;

	@Resource
	private CodeTable code;

	@Resource(name = "aliPayBL")
	private AlipayIf aliPayIF;

	@Resource(name = "weChatPayBL")
	private WeChatPayIf weChatPayIF;

	@Resource(name="binOLWPSAL03_BL")
	private BINOLWPSAL03_IF binOLWPSAL03_IF;

	private String cardCode;
	// 云POS是否支持新储值卡支付
	private String NEW_CZK_PAY;
	// 云POS是否允许去掉现金支付
	private String isCA;

	// 云POS储值卡服务卡消费时是否取消验证
	private String isIngnoreConfirm ;
	// 服务卡信息Map
	private Map<String, Object> consumptionCodeMap;
	private List<Map<String, Object>> serverList;
	// 储值卡退货信息List
	private List<Map<String, Object>> savingsList;
	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String init(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 获取积分兑换比率
			String pointRatio = binOLCM14_BL.getWebposConfigValue("9004", organizationInfoId, brandInfoId);
			if(null == pointRatio || "".equals(pointRatio)){
				pointRatio = "100";
			}
			// 云POS是否支持新储值卡支付
			NEW_CZK_PAY = binOLCM14_BL.getWebposConfigValue("9022", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));
			isCA = binOLCM14_BL.getWebposConfigValue("9030", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));
			form.setPointRatio(pointRatio);

			// 云POS储值卡服务卡消费时是否取消验证
			isIngnoreConfirm = binOLCM14_BL.getWebposConfigValue("9056", organizationInfoId, brandInfoId);

			// 获取Cookie值
			/*Map<String, Object> paymentMap = getPaymentCookies();
			String creditCardPayment = ConvertUtil.getString(paymentMap.get("creditCardPayment"));
			String bankCardPayment = ConvertUtil.getString(paymentMap.get("bankCardPayment"));
			String cashCardPayment = ConvertUtil.getString(paymentMap.get("cashCardPayment"));
			String aliPayment = ConvertUtil.getString(paymentMap.get("aliPayment"));
			String wechatPayment = ConvertUtil.getString(paymentMap.get("wechatPayment"));
			String pointsPayment = ConvertUtil.getString(paymentMap.get("pointsPayment"));
			form.setCreditCardPayment(creditCardPayment);
			form.setBankCardPayment(bankCardPayment);
			form.setCashCardPayment(cashCardPayment);
			form.setAliPayment(aliPayment);
			form.setWechatPayment(wechatPayment);
			form.setPointsPayment(pointsPayment);*/
			List<Map<String, Object>> pmList = getPaymentCookies();
			form.setPaymentList(pmList);

			String totalAmount = ConvertUtil.getString(form.getTotalAmount());
			if(CherryUtil.string2double(totalAmount) < 0){
				totalAmount = "0.00";
			}
			form.setTotalAmount(totalAmount);
			String collectType = ConvertUtil.getString(form.getCollectPageType());
			if("SRDT".equals(collectType)){
				// 退货情况
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("billCode", ConvertUtil.getString(form.getBillCode()));
				List<Map<String, Object>> payDetailList = binOLWPSAL03_IF.getPaymentDetailList(map);
				if (null != payDetailList && !payDetailList.isEmpty()){
					double cashAmount = 0.00;
					// 获取现金支付金额
					for(Map<String,Object> payDetailMap : payDetailList){
						String payType = ConvertUtil.getString(payDetailMap.get("payType"));
						String payAmount = ConvertUtil.getString(payDetailMap.get("payAmount"));
						if(payType.equals(CherryConstants.WP_PAYTYPECODE_CA)){
							cashAmount = CherryUtil.string2double(payAmount);
						}
					}
					if(NEW_CZK_PAY.equals("Y")){
						/**查询储值卡退货信息*/
						savingsList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
						if(savingsList.size()>0){
							// 服务类型
							serverList = code.getCodesByGrade("1338");
						}
					}
					if(CherryUtil.string2double(totalAmount) > cashAmount){
						// 除掉现金还需要退款的金额
						BigDecimal totalAmount_big = new BigDecimal(totalAmount);
						BigDecimal cashAmount_big = new BigDecimal(Double.toString(cashAmount));
						double surplusAmount = totalAmount_big.subtract(cashAmount_big).doubleValue();
						if(null!=form.getPaymentList() && !form.getPaymentList().isEmpty()){
							for(Map<String, Object> m : form.getPaymentList()){
								boolean b= false;
								String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
								for(Map<String,Object> payDetailMap : payDetailList){
									String payType = ConvertUtil.getString(payDetailMap.get("payType"));
									String payAmount = ConvertUtil.getString(payDetailMap.get("payAmount"));
									if(CherryUtil.string2double(payAmount)==0){
										payAmount="";
									}
									if(storePayCode.equals(payType) && !"CA".equals(payType)){
										if(storePayCode.equals("alipay")){
											form.setAliPay(payAmount);
										}else if(storePayCode.equals("WEPAY")){
											form.setWechatPay(payAmount);
										}
										if(surplusAmount > CherryUtil.string2double(payAmount)){
											// 重置剩余退款金额
											surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
											// 如果剩余退款金额大于付款金额，全退付款金额
											m.put("storePayAmount", payAmount);
											if(storePayCode.equals("CZK") && payDetailMap.get("serialNumber")!=null){
												form.setCardCode(ConvertUtil.getString(payDetailMap.get("serialNumber")));
											}
										}else{
											// 退剩余金额，跳出循环
											m.put("storePayAmount", surplusAmount);
											// 重置剩余退款金额
											surplusAmount = 0.00;
											if(storePayCode.equals("CZK") && payDetailMap.get("serialNumber")!=null){
												form.setCardCode(ConvertUtil.getString(payDetailMap.get("serialNumber")));
											}
											b=true;
											break;
										}
									}
								}
								if(b){
									break;
								}
							}
						}
						// 继续计算其它支付方式需要退款的金额
						/*for(Map<String,Object> payDetailMap : payDetailList){
							String payType = ConvertUtil.getString(payDetailMap.get("payType"));
							String payAmount = ConvertUtil.getString(payDetailMap.get("payAmount"));
							if(payType.equals(CherryConstants.WP_PAYTYPECODE_CR)){
								// 存在信用卡退款的情况下
								if(surplusAmount > CherryUtil.string2double(payAmount)){
									// 重置剩余退款金额
									surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
									// 如果剩余退款金额大于信用卡付款金额，全退信用卡付款金额
									form.setCreditCard(payAmount);
									if(payDetailMap.get("serialNumber")!=null){
										form.setCardCode(payDetailMap.get("serialNumber").toString());
									}
								}else{
									// 退剩余金额，跳出循环
									form.setCreditCard(ConvertUtil.obj2Price(surplusAmount));
									if(payDetailMap.get("serialNumber")!=null){
										form.setCardCode(payDetailMap.get("serialNumber").toString());
									}
									// 重置剩余退款金额
									surplusAmount = 0.00;
									break;
								}
							}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_BC)){
								// 存在银行卡付款的情况下
								if(surplusAmount > CherryUtil.string2double(payAmount)){
									// 重置剩余退款金额
									surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
									// 如果剩余退款金额大于银行卡付款金额，全退银行卡付款金额
									form.setBankCard(payAmount);
								}else{
									// 退剩余金额，跳出循环
									form.setBankCard(ConvertUtil.obj2Price(surplusAmount));
									// 重置剩余退款金额
									surplusAmount = 0.00;
									break;
								}
							}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_CZK)){
								// 存在储值卡付款的情况下
								if(surplusAmount > CherryUtil.string2double(payAmount)){
									// 重置剩余退款金额
									surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
									// 如果剩余退款金额大于储值卡付款金额，全退储值卡付款金额
									form.setCashCard(payAmount);
									if(payDetailMap.get("serialNumber")!=null){
										form.setCardCode(payDetailMap.get("serialNumber").toString());
									}
								}else{
									// 退剩余金额，跳出循环
									form.setCashCard(ConvertUtil.obj2Price(surplusAmount));
									if(payDetailMap.get("serialNumber")!=null){
										form.setCardCode(payDetailMap.get("serialNumber").toString());
									}
									// 重置剩余退款金额
									surplusAmount = 0.00;
									break;
								}
							}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_PT)){
								// 存在支付宝付款的情况下
								if(surplusAmount > CherryUtil.string2double(payAmount)){
									// 重置剩余退款金额
									surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
									// 如果剩余退款金额大于银行卡付款金额，全退支付宝付款金额
									form.setAliPay(payAmount);
								}else{
									// 退剩余金额，跳出循环
									form.setAliPay(ConvertUtil.obj2Price(surplusAmount));
									// 重置剩余退款金额
									surplusAmount = 0.00;
									break;
								}
							}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_WP)){
								// 存在微信支付退款的情况下
								if(surplusAmount > CherryUtil.string2double(payAmount)){
									// 重置剩余退款金额
									surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
									// 如果剩余退款金额大于微信支付付款金额，全退微信支付付款金额
									form.setWechatPay(payAmount);
								}else{
									// 退剩余金额，跳出循环
									form.setWechatPay(ConvertUtil.obj2Price(surplusAmount));
									// 重置剩余退款金额
									surplusAmount = 0.00;
									break;
								}
							}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_EX)){
								// 存在积分支付付款的情况下
								if(surplusAmount > CherryUtil.string2double(payAmount)){
									// 重置剩余退款金额
									surplusAmount = surplusAmount - CherryUtil.string2double(payAmount);
									// 如果剩余退款金额大于积分支付付款金额，全退积分支付付款金额
									form.setExchangeCash(payAmount);
								}else{
									// 退剩余金额，跳出循环
									form.setExchangeCash(ConvertUtil.obj2Price(surplusAmount));
									// 重置剩余退款金额
									surplusAmount = 0.00;
									break;
								}
							}
						}*/
						// 退现金的情况，其它支付方式退款后剩余金额加上现金付款金额
						cashAmount += surplusAmount;
						form.setCash(ConvertUtil.obj2Price(cashAmount));
					}else{
						// 退款金额小于等于付款现金的情况下默认退现金
						form.setCash(totalAmount);
					}
				}else{
					form.setCash(totalAmount);
				}
			}else if("SRBL".equals(collectType)){
				// 退单情况
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("billCode", ConvertUtil.getString(form.getBillCode()));
				List<Map<String, Object>> payDetailList = binOLWPSAL03_IF.getPaymentDetailList(map);
				// 现金支付的金额
				for(Map<String,Object> payDetailMap : payDetailList){
					String payType = ConvertUtil.getString(payDetailMap.get("payType"));
					String payAmount = ConvertUtil.getString(payDetailMap.get("payAmount"));
					if(payType.equals("CA")){
						form.setCash(payAmount);
					}
				}
				if(null!=form.getPaymentList() && !form.getPaymentList().isEmpty()){
					for(Map<String, Object> m : form.getPaymentList()){
						String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
						for(Map<String,Object> payDetailMap : payDetailList){
							String payType = ConvertUtil.getString(payDetailMap.get("payType"));
							String payAmount = ConvertUtil.getString(payDetailMap.get("payAmount"));
							if(CherryUtil.string2double(payAmount)==0){
								payAmount="";
							}
							if(storePayCode.equals(payType)){
								m.put("storePayAmount", payAmount);
								if(storePayCode.equals("CZK")){
									if(payDetailMap.get("serialNumber")!=null){
										form.setCardCode(payDetailMap.get("serialNumber").toString());
									}
									if(NEW_CZK_PAY.equals("Y")){
										/**查询储值卡退货信息*/
										savingsList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
										if(savingsList.size()>0){
											// 服务类型
											serverList = code.getCodesByGrade("1338");
										}
									}
								}
							}
						}
					}
				}
/*				if (null != payDetailList && !payDetailList.isEmpty()){
					boolean cashFlag = false;
					double cashAmount = 0.00;
					for(Map<String,Object> payDetailMap : payDetailList){
						String payType = ConvertUtil.getString(payDetailMap.get("payType"));
						String payAmount = ConvertUtil.getString(payDetailMap.get("payAmount"));
						if(payType.equals(CherryConstants.WP_PAYTYPECODE_CR)){
							form.setCreditCard(payAmount);
						}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_BC)){
							form.setBankCard(payAmount);
						}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_CZK)){
							if(!"".equals(payAmount) && Double.valueOf(payAmount)==0){
								form.setCashCard("");
							}else {
								form.setCashCard(payAmount);
							}
							if(payDetailMap.get("serialNumber")!=null){
								form.setCardCode(payDetailMap.get("serialNumber").toString());
							}
							if(NEW_CZK_PAY.equals("Y")){
								//查询储值卡退货信息
								savingsList = binOLWPSAL07_IF.tran_getBillDetailSavings(map);
								if(savingsList.size()>0){
									// 服务类型
									serverList = code.getCodesByGrade("1338");
								}
							}
						}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_PT)){
							form.setAliPay(payAmount);
						}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_WP)){
							form.setWechatPay(payAmount);
						}else if(payType.equals(CherryConstants.WP_PAYTYPECODE_EX)){
							form.setExchangeCash(payAmount);
						}else{
							cashFlag = true;
							cashAmount += CherryUtil.string2double(payAmount);
						}
					}
					if(cashFlag){
						form.setCash(ConvertUtil.getString(cashAmount));
					}
				}else{
					form.setCash(totalAmount);
				}*/
			}else{
				// 正常销售情况
				// 云POS销售是否允去掉现金支付
				String isCA = binOLCM14_BL.getWebposConfigValue("9030", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
				if(null == isCA || "".equals(isCA)){
					isCA = "N";
				}
				if("Y".equals(isCA)){
					if(null!=form.getPaymentList() && !form.getPaymentList().isEmpty()){
						for(Map<String, Object> m : pmList){
							if("Y".equals(ConvertUtil.getString(m.get("defaultPay"))) && "Y".equals(ConvertUtil.getString(m.get("check")))){ // 判断是否为默认支付方式
								// 给默认支付方式赋值
								m.put("payValue", totalAmount);
								// 只能有一个默认支付方式，故结束循环
								break;
							}
						}
					}
				}else {
					// 原默认支付方式为现金
					form.setCash(totalAmount);
				}
				// 云POS是否支持新储值卡支付
				String NEW_CZK_PAY = binOLCM14_BL.getWebposConfigValue("9022", organizationInfoId, brandInfoId);
				if(NEW_CZK_PAY.equals("Y")){
					// 柜台信息
					CounterInfo counterInfo = (CounterInfo) session
							.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
					Map<String,Object> map=new HashMap<String, Object>();
					// 登陆用户信息
					map.put("brandCode", ConvertUtil.getString(userInfo.getBrandCode()));
					map.put("memberCode", form.getMemberCode());
					map.put("counterCode", counterInfo.getCounterCode());
					if(form.getMemberCode()!=null && !"".equals(form.getMemberCode())){
						List<Map<String,Object>> list = binOLWPSAL13_BL.getCard(map);
						if(list!=null){
							if(list.size()==1){
								consumptionCodeMap = list.get(0);
							}
						}
					}
					// 服务卡服务类型
					serverList = code.getCodesByGrade("1338");
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
		}
		return SUCCESS;
	}

	public void collect() throws Exception{
		try{
			//做销售单格式校验 如不是WN开头的直接返回错误
			String billCode=form.getBillCode();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			DecimalFormat   df   =new   DecimalFormat("#.00");
			//BACode相关校验
			String baCode=ConvertUtil.getString(form.getBaCode());
			if("".equals(baCode)){
				ConvertUtil.setResponseByAjax(response, "SALEBACODEERROR");
				return;
			}
			// 销售明细校验
			String saleDetailListData = ConvertUtil.getString(form.getSaleDetailList());
			if(!"".equals(saleDetailListData)){
				List<Map<String, Object>> saleDetailList = ConvertUtil.json2List(saleDetailListData);
				if(null!=saleDetailList && !saleDetailList.isEmpty()){
					double totalAmount = 0;
					if(!"".equals(ConvertUtil.getString(form.getTotalAmount()))){
						totalAmount = Double.parseDouble(ConvertUtil.getString(form.getTotalAmount()));
					}
					double allAmount = 0;
					for(Map<String, Object> m : saleDetailList){
						String activityTypeCode = ConvertUtil.getString(m.get("activityTypeCode"));
						String productVendorIDArr = ConvertUtil.getString(m.get("productVendorIDArr"));
						if("".equals(activityTypeCode) || "ZDZK".equals(activityTypeCode) || "ZDQL".equals(activityTypeCode)){
							if(!"".equals(ConvertUtil.getString(m.get("payAmount")))){
								double payAmount= Double.parseDouble(ConvertUtil.getString(m.get("payAmount")));
								allAmount += payAmount;
							}
						}else if("CXHD".equals(activityTypeCode) && "HDZD".equals(productVendorIDArr)){
							if(!"".equals(ConvertUtil.getString(m.get("payAmount")))){
								double payAmount= Double.parseDouble(ConvertUtil.getString(m.get("payAmount")));
								allAmount += payAmount;
							}
						}
					}
					if(df.format(totalAmount).equals(df.format(allAmount))){
						//获取微信核销CouponUrl地址
						String weChatCouponUrl = binOLCM14_BL.getWebposConfigValue("9032", Integer.toString(userInfo.getBIN_OrganizationInfoID()), Integer.toString( userInfo.getBIN_BrandInfoID()));
						String weChatState = binOLCM14_BL.getWebposConfigValue("9034", Integer.toString(userInfo.getBIN_OrganizationInfoID()), Integer.toString( userInfo.getBIN_BrandInfoID()));
						logger.info("weChatCouponUrl:"+weChatCouponUrl+"====weChatState:"+weChatState);
						if(!"".equals(weChatCouponUrl) && !"".equals(weChatState)){
							logger.info("微信核券开始");
							int flag=0;
							for(Map<String,Object> detail :saleDetailList){
								String maincode=ConvertUtil.getString(detail.get("mainCode"));
								String couponCode=ConvertUtil.getString(detail.get("couponCode"));
								String campaignValid=ConvertUtil.getString(detail.get("campaignValid"));
								if(!"".equals(couponCode) && "5".equals(campaignValid)){
									Map<String,String> param=new HashMap<String, String>();
									param.put("camcode", maincode);
									param.put("codenum", couponCode);
									param.put("state", weChatState);
									Map<String,Object> result_map=WebserviceClient.accessWeChatWebService(weChatCouponUrl, param);
									String errcode=ConvertUtil.getString(result_map.get("errcode"));
									String errmsg=ConvertUtil.getString(result_map.get("errmsg"));
									logger.info("errcode:"+errcode+"===errmsg:"+errmsg);
									if(!"0".equals(errcode)){
										logger.error("云POS调用微信核券接口："+errmsg);
										flag=-1;
									}
								}
							}
							if(flag < 0){
								ConvertUtil.setResponseByAjax(response, "WECHATERROR");
								logger.info("微信核券失败");
								return;
							}
							logger.info("微信核券正常结束");
						}
						if(df.format(totalAmount).equals(df.format(allAmount))){
							// 收款
							String result = binOLWPSAL03_IF.tran_collect(form, userInfo);
							if(!result.equals(CherryConstants.WP_ERROR_STATUS)){
								ConvertUtil.setResponseByAjax(response, result);
							}else{
								ConvertUtil.setResponseByAjax(response, "ERROR");
							}
						}else {
							ConvertUtil.setResponseByAjax(response, "SALEDETAILERROR");
						}
					}else {
						ConvertUtil.setResponseByAjax(response, "SALEDETAILERROR");
					}
				}else {
					ConvertUtil.setResponseByAjax(response, "SALEDETAILERROR");
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}
	}
	/**
	 * 对于支付宝/微信支付成功，但是由于网络原因没有录单的单据进行重发MQ操作
	 * @throws Exception
	 */
	public void sendMQCollect() throws Exception{
		try{
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
			binOLWPSAL03_IF.getHangBillSetForm(param,form);
			// 收款
			String result = binOLWPSAL03_IF.tran_collect(form, userInfo);
			if(Integer.parseInt(result) > 0){
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}else{
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}
	}

	public void getPaymentSize() throws Exception{
		try{
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
			map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			map.put("isEnable", "Y");
			List<Map<String, Object>> paymentList =  binOLMOPOS01_BL.getStorePayCodeList(map);
			if(paymentList!=null && !paymentList.isEmpty()){
				ConvertUtil.setResponseByAjax(response, paymentList.size());
			}else {
				ConvertUtil.setResponseByAjax(response, "0");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}
	}
	public void paymentInit() throws Exception{
		try{
			// 获取Cookie值
			List<Map<String, Object>> paymentList = getPaymentCookies();
			ConvertUtil.setResponseByAjax(response, paymentList);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}
	}

	public void setPayment() throws Exception{
		try{
			String json = ConvertUtil.getString(form.getPaymentJsonList());
			if(!"".equals(json)){
				List<Map<String, Object>> paymentlist = ConvertUtil.json2List(json);
				if(null!=paymentlist && !paymentlist.isEmpty()){
					for(Map<String, Object> m : paymentlist){
						if(null!=m){
							String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
							if("Y".equals(ConvertUtil.getString(m.get("isCheck")))){
								Cookie cookie = new Cookie(storePayCode,storePayCode);
								cookie.setMaxAge(60*60*24*365*5);
								response.addCookie(cookie);
							}else {
								Cookie[] cookies = request.getCookies();
								if(null!=cookies){
									for(int i = 0; i < cookies.length; i++){
										if(storePayCode.equals(cookies[i].getName())){
											Cookie c = cookies[i];
											c.setMaxAge(0);
											response.addCookie(c);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
			/*String crPayment = ConvertUtil.getString(form.getCreditCardPayment());
			String bkPayment = ConvertUtil.getString(form.getBankCardPayment());
			String ccPayment = ConvertUtil.getString(form.getCashCardPayment());
			String alPayment = ConvertUtil.getString(form.getAliPayment());
			String wtPayment = ConvertUtil.getString(form.getWechatPayment());
			String exPayment = ConvertUtil.getString(form.getPointsPayment());
			// 信用卡支付方式
			Cookie creditCardPayment = new Cookie("creditCardPayment",crPayment);
			creditCardPayment.setMaxAge(60*60*24*365*5);
			response.addCookie(creditCardPayment);
			// 银行卡支付方式
			Cookie bankCardPayment = new Cookie("bankCardPayment",bkPayment);
			bankCardPayment.setMaxAge(60*60*24*365*5);
			response.addCookie(bankCardPayment);
			// 储值卡支付方式
			Cookie cashCardPayment = new Cookie("cashCardPayment",ccPayment);
			cashCardPayment.setMaxAge(60*60*24*365*5);
			response.addCookie(cashCardPayment);
			// 支付宝支付方式
			Cookie aliPayment = new Cookie("aliPayment",alPayment);
			aliPayment.setMaxAge(60*60*24*365*5);
			response.addCookie(aliPayment);
			// 微信支付方式
			Cookie wechatPayment = new Cookie("wechatPayment",wtPayment);
			wechatPayment.setMaxAge(60*60*24*365*5);
			response.addCookie(wechatPayment);
			// 积分支付方式
			Cookie pointsPayment = new Cookie("pointsPayment",exPayment);
			pointsPayment.setMaxAge(60*60*24*365*5);
			response.addCookie(pointsPayment);*/
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		}
	}

	public void webPayment() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String brandCode = ConvertUtil.getString(userInfo.getBrandCode());
			// 云POS是否支持新储值卡支付
			String NEW_CZK_PAY = binOLCM14_BL.getWebposConfigValue("9022", organizationInfoId, brandInfoId);
			String counterCode = ConvertUtil.getString(form.getCounterCode());
			String billCode = ConvertUtil.getString(form.getBillCode());
			String authCode = ConvertUtil.getString(form.getAuthCode());
			String alipay = ConvertUtil.getString(form.getAliPay());
			String wechatPay = ConvertUtil.getString(form.getWechatPay());
			String version = binOLCM14_BL.getWebposConfigValue("9029", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));

			// 云POS储值卡服务卡消费时是否取消验证
			isIngnoreConfirm = binOLCM14_BL.getWebposConfigValue("9056", organizationInfoId, brandInfoId);
			if("".equals(billCode)){
				// 单据号为空的情况
				ConvertUtil.setResponseByAjax(response, "BN");
			}else if("N".equals(isIngnoreConfirm) && "".equals(authCode)){
				// 扫描码为空的情况
				ConvertUtil.setResponseByAjax(response, "AN");
			}else if("".equals(alipay) && "".equals(wechatPay) && "N".equals(NEW_CZK_PAY)){
				// 支付金额为空的情况
				ConvertUtil.setResponseByAjax(response, "TN");
			}else{
				String payType = ConvertUtil.getString(form.getPayType());
				if("AL".equals(payType)){
					// 支付类型为支付宝支付时
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					boolean flag = aliPayIF.getAliPayConfig(map);
					if(flag){
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
								parMap.put("strTotalFee", alipay);
								parMap.put("strBaCode", ConvertUtil.getString(form.getBaCode()));
								parMap.put("strDynamicId", authCode);
								parMap.put("strDynamicIdType", "bar_code");
								if("1.0".equals(version)){
									logger.info("配置信息存在的情况下开始调用支付宝1.0接口");
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndCreateAndPay2(parMap);
									logger.info("配置信息存在的情况下调用支付宝1.0接口后的返回值"+ConvertUtil.getString(returnList));
									String result = getAliPayResult(returnList);
									logger.info("配置信息存在的情况下调用支付宝1.0接口后的页面返回值"+result);
									logger.info("配置信息存在的情况下调用支付宝1.0接口结束");
									//确认成功之后把挂单主表中的支付状态更新为已经支付
									Map<String,Object> param=new HashMap<String, Object>();
									param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
									param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
									param.put("billCode", billCode);
									if("SUCCESS".equals(result)){
										param.put("collectStatus", 1);
										try{
											binOLWPSAL03_IF.updateHangBillCollectState(param);
										}catch(Exception e){
											logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
										}
									}else if("PROCESSING".equals(result)){
										param.put("collectStatus", 2);
										try{
											binOLWPSAL03_IF.updateHangBillCollectState(param);
										}catch(Exception e){
											logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
										}
									}
									ConvertUtil.setResponseByAjax(response, result);
								}else if("2.0".equals(version)){
									logger.info("配置信息存在的情况下开始调用支付宝2.0接口");
									List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndCreateAndPayTwo(parMap);
									logger.info("配置信息存在的情况下调用支付宝2.0接口后的返回值"+ConvertUtil.getString(returnList));
									String result = getAliPayResult(returnList);
									logger.info("配置信息存在的情况下调用支付宝2.0接口后的页面返回值"+result);
									logger.info("配置信息存在的情况下调用支付宝2.0接口结束");
									//确认成功之后把挂单主表中的支付状态更新为已经支付
									Map<String,Object> param=new HashMap<String, Object>();
									param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
									param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
									param.put("billCode", billCode);
									if("SUCCESS".equals(result)){
										param.put("collectStatus", 1);
										try{
											binOLWPSAL03_IF.updateHangBillCollectState(param);
										}catch(Exception e){
											logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
										}
									}else if("PROCESSING".equals(result)){
										param.put("collectStatus", 2);
										try{
											binOLWPSAL03_IF.updateHangBillCollectState(param);
										}catch(Exception e){
											logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
										}
									}
									ConvertUtil.setResponseByAjax(response, result);
								}
							}else{
								if (null != allConfigMap && !allConfigMap.isEmpty()){
									Map<String, Object> parMap = new HashMap<String, Object>();
									parMap.putAll(allConfigMap);
									parMap.put("strOutTradeNo", billCode);
									parMap.put("strTotalFee", alipay);
									parMap.put("strBaCode", ConvertUtil.getString(form.getBaCode()));
									parMap.put("strDynamicId", authCode);
									parMap.put("strDynamicIdType", "bar_code");
									if("1.0".equals(version)){
										logger.info("配置信息不存在的情况下开始调用支付宝1.0接口");
										List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndCreateAndPay2(parMap);
										logger.info("配置信息不存在的情况下调用支付宝1.0接口后的返回值"+ConvertUtil.getString(returnList));
										String result = getAliPayResult(returnList);
										logger.info("配置信息不存在的情况下调用支付宝1.0接口后的页面返回值"+result);
										logger.info("配置信息不存在的情况下调用支付宝1.0接口结束");
										//确认成功之后把挂单主表中的支付状态更新为已经支付
										Map<String,Object> param=new HashMap<String, Object>();
										param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
										param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
										param.put("billCode", billCode);
										if("SUCCESS".equals(result)){
											param.put("collectStatus", 1);
											try{
												binOLWPSAL03_IF.updateHangBillCollectState(param);
											}catch(Exception e){
												logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
											}
										}else if("PROCESSING".equals(result)){
											param.put("collectStatus", 2);
											try{
												binOLWPSAL03_IF.updateHangBillCollectState(param);
											}catch(Exception e){
												logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
											}
										}
										ConvertUtil.setResponseByAjax(response, result);
									}else if("2.0".equals(version)){
										logger.info("配置信息不存在的情况下开始调用支付宝2.0接口");
										List<Map<String, Object>> returnList = aliPayIF.getAlipayQueryAndCreateAndPayTwo(parMap);
										logger.info("配置信息不存在的情况下调用支付宝2.0接口后的返回值"+ConvertUtil.getString(returnList));
										String result = getAliPayResult(returnList);
										logger.info("配置信息不存在的情况下调用支付宝2.0接口后的页面返回值"+result);
										logger.info("配置信息不存在的情况下调用支付宝2.0接口结束");
										//确认成功之后把挂单主表中的支付状态更新为已经支付
										Map<String,Object> param=new HashMap<String, Object>();
										param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
										param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
										param.put("billCode", billCode);
										if("SUCCESS".equals(result)){
											param.put("collectStatus", 1);
											try{
												binOLWPSAL03_IF.updateHangBillCollectState(param);
											}catch(Exception e){
												logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
											}
										}else if("PROCESSING".equals(result)){
											param.put("collectStatus", 2);
											try{
												binOLWPSAL03_IF.updateHangBillCollectState(param);
											}catch(Exception e){
												logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
											}
										}
										ConvertUtil.setResponseByAjax(response, result);
									}
								}else{
									// 没有给指定柜台配置收款账户信息的情况
									ConvertUtil.setResponseByAjax(response, "NAP");
								}
							}
						}else{
							// 没有给指定柜台配置收款账户信息的情况
							ConvertUtil.setResponseByAjax(response, "NAP");
						}
					}else{
						// 没有获取到支付宝配置的情况
						ConvertUtil.setResponseByAjax(response, "NC");
					}
				}else if("WT".equals(payType)){
					// 支付类型为微信支付时
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("organizationInfoId", organizationInfoId);
					map.put("brandInfoId", brandInfoId);
					map.put("counterCode", counterCode);
					map.put("payType", "WECHATPAY");
					List<Map<String, Object>> configList = binOLWPSAL03_IF.getPayPartnerConfig(map);
					if (null != configList && !configList.isEmpty()){
						int totalFee=Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(form.getWechatPay())*100)));
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
							parMap.put("total_fee", ConvertUtil.getString(totalFee));
							parMap.put("device_info", userInfo.getLoginIP());
							parMap.put("auth_code", authCode);
							parMap.put("counterCode", counterCode);
							logger.info("微信支付配置存在情况下开始调用递交被扫接口"+ConvertUtil.getString(parMap));
							List<Map<String, Object>> returnList = weChatPayIF.getMicropay(parMap);
							logger.info("微信支付配置存在情况下调用递交被扫接口结束"+ConvertUtil.getString(parMap));
							String result = getWechatPayResult(returnList);
							logger.info("微信支付配置存在情况下调用递交被扫接口页面返回结果==="+result);
							//确认成功之后把挂单主表中的支付状态更新为已经支付
							Map<String,Object> param=new HashMap<String, Object>();
							param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
							param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
							param.put("billCode", billCode);
							if("SUCCESS".equals(result)){
								param.put("collectStatus", 1);
								try{
									binOLWPSAL03_IF.updateHangBillCollectState(param);
								}catch(Exception e){
									logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
								}
							}else if("PROCESSING".equals(result)){
								param.put("collectStatus", 2);
								try{
									binOLWPSAL03_IF.updateHangBillCollectState(param);
								}catch(Exception e){
									logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
								}
							}
							ConvertUtil.setResponseByAjax(response, result);
						}else{
							if (null != allConfigMap && !allConfigMap.isEmpty()){
								Map<String, Object> parMap = new HashMap<String, Object>();
								parMap.putAll(allConfigMap);
								parMap.put("out_trade_no", billCode);
								parMap.put("total_fee", ConvertUtil.getString(totalFee));
								parMap.put("device_info", userInfo.getLoginIP());
								parMap.put("auth_code", authCode);
								parMap.put("counterCode", counterCode);
								logger.info("微信支付配置不存在情况下开始调用递交被扫接口"+ConvertUtil.getString(parMap));
								List<Map<String, Object>> returnList = weChatPayIF.getMicropay(parMap);
								logger.info("微信支付配置不存在情况下调用递交被扫接口结束"+ConvertUtil.getString(parMap));
								String result = getWechatPayResult(returnList);
								logger.info("微信支付配置不存在情况下调用递交被扫接口页面返回结果==="+result);
								//确认成功之后把挂单主表中的支付状态更新为已经支付
								Map<String,Object> param=new HashMap<String, Object>();
								param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
								param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
								param.put("billCode", billCode);
								if("SUCCESS".equals(result)){
									param.put("collectStatus", 1);
									try{
										binOLWPSAL03_IF.updateHangBillCollectState(param);
									}catch(Exception e){
										logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
									}
								}else if("PROCESSING".equals(result)){
									param.put("collectStatus", 2);
									try{
										binOLWPSAL03_IF.updateHangBillCollectState(param);
									}catch(Exception e){
										logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
									}
								}
								ConvertUtil.setResponseByAjax(response, result);
							}else{
								// 没有给指定柜台配置收款账户信息的情况
								ConvertUtil.setResponseByAjax(response, "NWP");
							}
						}
					}else{
						// 没有给指定柜台配置收款账户信息的情况
						ConvertUtil.setResponseByAjax(response, "NWP");
					}
				}else if("Y".equals(NEW_CZK_PAY) || "CZK".equals(payType)){
					// 验证码或密码
					String verificationCode = ConvertUtil.getString(form.getVerificationCode());
					// 储值卡金额
					String cashCard = ConvertUtil.getString(form.getCashCard());

					String json = ConvertUtil.getString(form.getServiceJsonList());
					List<Map<String, Object>> jsonList = new ArrayList<Map<String,Object>>();
					if(!"".equals(json)){
						jsonList = ConvertUtil.json2List(json);
					}
					if("".equals(cashCard) && "".equals(json)){
						// 支付金额为空的情况
						ConvertUtil.setResponseByAjax(response, "TN");
						return;
					}
					String VerificationType = ConvertUtil.getString(form.getVerificationType());
					VerificationType = "Y".equals(isIngnoreConfirm) ? "4" : VerificationType ;

					CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
					Map<String, Object> data = new HashMap<String, Object>();
					if(VerificationType.equals("2") || VerificationType.equals("3") ){
						if("".equals(verificationCode)){
							// 验证码为空
							ConvertUtil.setResponseByAjax(response, "VN");
							return;
						}else {
							data.put("VerificationCode", verificationCode);
						}
					}else if (VerificationType.equals("1")) {
						data.put("Password", verificationCode);
					}
					data.put("TradeType", "SavingsCardTrade");
					data.put("CardCode", authCode);
					data.put("VerificationType", VerificationType);
					data.put("TransactionType", "US");
					data.put("BillCode", billCode);
					data.put("RelevantCode", billCode);
					data.put("CounterCode", ConvertUtil.getString(counterInfo.getCounterCode()));
					data.put("EmployeeCode", userInfo.getEmployeeCode());
					data.put("TradeAmount", cashCard);
					data.put("ServiceDetail", jsonList);
					data.put("Memo", form.getComments());
					MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
					queryParams.add("brandCode", brandCode);
					queryParams.add("appID", SavingscardAppID + "_" + brandCode);
					queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
					WebResource wr= binOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
					String result_card=wr.queryParams(queryParams).get(String.class);
					Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
					String ERRORCODE = result_card1.get("ERRORCODE").toString();
					if(ERRORCODE.equals("0")){
							/*String ResultContent = result_card1.get("ResultContent").toString();
							String s = CherryAESCoder.decrypt(ResultContent, SavingscardAESKEY);
							Map<String, Object> consumptionCodeMap = ConvertUtil.json2Map(s);*/
						//确认成功之后把挂单主表中的支付状态更新为已经支付
						Map<String,Object> param=new HashMap<String, Object>();
						param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
						param.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
						param.put("billCode", billCode);
						try{
							binOLWPSAL03_IF.updateHangBillCollectState(param);
						}catch(Exception e){
							logger.error("更新挂单主表支付状态发生异常，单据号："+billCode+e.getMessage(), e);
						}
						ConvertUtil.setResponseByAjax(response, "SUCCESS");
					}else if(ERRORCODE.equals("STE0014")){
						ConvertUtil.setResponseByAjax(response, "STE0014");
					}else if(ERRORCODE.equals("STE0015")){
						ConvertUtil.setResponseByAjax(response, "STE0015");
					}else if(ERRORCODE.equals("STE0016")){
						ConvertUtil.setResponseByAjax(response, "STE0016");
					}else if(ERRORCODE.equals("STE0017")){
						ConvertUtil.setResponseByAjax(response, "STE0017");
					}else {
						// 记录日志
						logger.info("储值卡支付WebService调用结果："+ ConvertUtil.getString(result_card1.get("ERRORMSG")));
						ConvertUtil.setResponseByAjax(response, "ERROR");
					}
				}else{
					// 支付类型为非支付宝和微信支付时
					ConvertUtil.setResponseByAjax(response, "LC");
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			logger.error("支付过程中发生系统异常信息");
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "PROCESSING");
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "PROCESSING");
			}
		}
	}

	// 从Cookie中获取柜台的支付方式设置
	private List<Map<String, Object>> getPaymentCookies() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
		map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("isEnable", "Y");
		List<Map<String, Object>> codeList =  binOLMOPOS01_BL.getStorePayCodeList(map);
		List<Map<String, Object>> paymentList = new ArrayList<Map<String,Object>>();
		// 云POS销售是否允去掉现金支付
		String isCA = binOLCM14_BL.getWebposConfigValue("9030", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		if(null == isCA || "".equals(isCA)){
			isCA = "N";
		}
		Cookie[] cookies = request.getCookies();
		if(null!=codeList && !codeList.isEmpty()){
			for(Map<String, Object> m : codeList){
				if(null!=cookies){
					Map<String, Object> paymentMap = new HashMap<String, Object>();
					String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
					if("N".equals(isCA)){
						if(storePayCode.equals("CA")){
							continue;
						}
					}
					String storePayValue = ConvertUtil.getString(m.get("storePayValue"));
					paymentMap.put("storePayCode", storePayCode);
					paymentMap.put("storePayValue", storePayValue);
					paymentMap.put("defaultPay", m.get("defaultPay"));
					for(int i = 0; i < cookies.length; i++){
						if(storePayCode.equals(cookies[i].getName())){
							paymentMap.put("check", "Y");
							break;
						}
					}
					paymentList.add(paymentMap);
				}
			}
		}
		/*String creditCardPayment = "CR";
		String bankCardPayment = "BC";
		String cashCardPayment = "CZK";
		String aliPayment = "AL";
		String wechatPayment = "WT";
		String pointsPayment = "EX";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if("creditCardPayment".equals(cookies[i].getName())){
					creditCardPayment = cookies[i].getValue();
				}else if("bankCardPayment".equals(cookies[i].getName())){
					bankCardPayment = cookies[i].getValue();
				}else if("cashCardPayment".equals(cookies[i].getName())){
					cashCardPayment = cookies[i].getValue();
				}else if("aliPayment".equals(cookies[i].getName())){
					aliPayment = cookies[i].getValue();
				}else if("wechatPayment".equals(cookies[i].getName())){
					wechatPayment = cookies[i].getValue();
				}else if("pointsPayment".equals(cookies[i].getName())){
					pointsPayment = cookies[i].getValue();
				}
			}
		}
		Map<String, Object> paymentMap = new HashMap<String, Object>();
		paymentMap.put("creditCardPayment", creditCardPayment);
		paymentMap.put("bankCardPayment", bankCardPayment);
		paymentMap.put("cashCardPayment", cashCardPayment);
		paymentMap.put("aliPayment", aliPayment);
		paymentMap.put("wechatPayment", wechatPayment);
		paymentMap.put("pointsPayment", pointsPayment);*/
		return paymentList;
	}

	// 获取支付宝支付支付结果
	private String getAliPayResult(List<Map<String, Object>> returnList) throws Exception{
		String result;
		try {
			result = "";
			if (returnList != null && !returnList.isEmpty()) {
				for (Map<String, Object> returnMap : returnList) {
					String returnCode = ConvertUtil.getString(returnMap
							.get("is_success"));
					logger.info("获取支付宝支付支付结果:"+ConvertUtil.getString(returnMap));
					if ("T".equals(returnCode)) {
						String resultCode = ConvertUtil.getString(returnMap
								.get("result_code"));
						if ("ORDER_SUCCESS_PAY_SUCCESS".equals(resultCode)) {
							// 下单成功支付成功
							result = "SUCCESS";
						} else if ("ORDER_SUCCESS_PAY_INPROCESS"
								.equals(resultCode)) {
							// 下单成功支付处理中
							result = "PROCESSING";
						} else {
							// 支付宝支付下单失败的情况
							result = "FAIL";
							logger.error("支付宝支付结果为未知情况resultCode====="
									+ resultCode);
							logger.error(ConvertUtil.getString(returnMap));
						}
					} else {
						// 支付宝支付接口通讯失败的情况
						result = "FA";
						logger.error("支付宝支付接口通讯通讯失败");
						logger.error(ConvertUtil.getString(returnMap));
					}
				}
			} else {
				// 没有获取到返回结果的情况
				// 没有获取到返回结果的情况
				result = "PROCESSING";
				logger.error("支付宝没有获取到返回结果的情况,去查询等待页面再进行查询判断");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("支付宝发生系统异常，去查询等待页面再进行查询判断");
			return "PROCESSING";
		}
	}

	// 获取微信支付支付结果
	private String getWechatPayResult(List<Map<String, Object>> returnList){
		String result;
		try {
			result = "";
			if (returnList != null && !returnList.isEmpty()) {
				for (Map<String, Object> returnMap : returnList) {
					String returnCode = ConvertUtil.getString(returnMap
							.get("return_code"));
					if ("SUCCESS".equals(returnCode)) {
						String resultCode = ConvertUtil.getString(returnMap
								.get("result_code"));
						if ("SUCCESS".equals(resultCode)) {
							// 微信支付成功
							result = "SUCCESS";
						} else {
							String errorCode = ConvertUtil.getString(returnMap
									.get("err_code"));
							if ("ORDERPAID".equals(errorCode)) {
								// 订单已被支付的情况
								result = "SUCCESS";
								logger.error("支付成功的情况====="
										+ ConvertUtil.getString(returnMap));
							} else if ("USERPAYING".equals(errorCode)) {
								// 支付处理中需要输入密码的情况
								result = "PROCESSING";
								logger.error("支付处理中需要输入密码的情况====="
										+ ConvertUtil.getString(returnMap));
							} else {
								// 支付失败的情况
								result = "FAIL";
								logger.error("支付状态返回值未知的情况下=====errcode====="+errorCode+"====="
										+ ConvertUtil.getString(returnMap));
							}
						}
					} else {
						// 微信支付接口通讯失败的情况
						result = "FC";
						logger.error("微信支付接口通讯失败的情况"
								+ ConvertUtil.getString(returnMap));
					}
				}
			} else {
				// 没有获取到返回结果的情况
				result = "PROCESSING";
				logger.error("微信支付接口没有获取到返回结果的情况,去查询等待页面再进行查询判断");
			}
			logger.error("最后微信当面付最后返回的result==" + result);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("发生系统异常,去查询等待页面再进行查询判断");
			return "PROCESSING";
		}
	}
	// 获取验证码 cardCode是储值卡卡号
	public void getVerificationCode() throws Exception{
		//调用接口
		String cardCode = form.getCardCode();
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardValidate");
		data.put("CardCode", cardCode);
		if(form.getUsesType()!=null && !"".equals(form.getUsesType().toString())){
			data.put("UsesType", form.getUsesType());
		}else {
			data.put("UsesType", "1");
		}
		data.put("BillCode", form.getBillCode());
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String brandCode = ConvertUtil.getString(userInfo.getBrandCode());
		// 活动查询
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= binOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String r_validateCode=wr.queryParams(queryParams).get(String.class);
		Map<String,Object> r_validateCode1=ConvertUtil.json2Map(r_validateCode);
		String ERRORCODE = r_validateCode1.get("ERRORCODE").toString();
		if(ERRORCODE.equals("0")){
			logger.info("获取验证码成功：ERRORCODE="+ERRORCODE);
			ConvertUtil.setResponseByAjax(response, ERRORCODE);
		}else {
			logger.info("获取验证码失败：ERRORCODE="+ERRORCODE+"data"+data);
			ConvertUtil.setResponseByAjax(response, ERRORCODE);
		}
	}
	@Override
	public BINOLWPSAL03_Form getModel() {
		return form;
	}

	public String getNEW_CZK_PAY() {
		return NEW_CZK_PAY;
	}

	public void setNEW_CZK_PAY(String nEW_CZK_PAY) {
		NEW_CZK_PAY = nEW_CZK_PAY;
	}

	public Map<String, Object> getConsumptionCodeMap() {
		return consumptionCodeMap;
	}

	public void setConsumptionCodeMap(Map<String, Object> consumptionCodeMap) {
		this.consumptionCodeMap = consumptionCodeMap;
	}

	public List<Map<String, Object>> getServerList() {
		return serverList;
	}

	public void setServerList(List<Map<String, Object>> serverList) {
		this.serverList = serverList;
	}

	public List<Map<String, Object>> getSavingsList() {
		return savingsList;
	}

	public void setSavingsList(List<Map<String, Object>> savingsList) {
		this.savingsList = savingsList;
	}

	public String getIsCA() {
		return isCA;
	}

	public void setIsCA(String isCA) {
		this.isCA = isCA;
	}

	public String getIsIngnoreConfirm() {
		return isIngnoreConfirm;
	}

	public void setIsIngnoreConfirm(String isIngnoreConfirm) {
		this.isIngnoreConfirm = isIngnoreConfirm;
	}
}
