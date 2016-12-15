package com.cherry.tp.duiba.action;

import cn.com.duiba.credits.sdk.CreditConsumeParams;
import cn.com.duiba.credits.sdk.CreditConsumeResult;
import cn.com.duiba.credits.sdk.CreditNotifyParams;
import cn.com.duiba.credits.sdk.CreditTool;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM03_BL;
import com.cherry.tp.duiba.bl.BINOLDUIBA01_BL;
import com.cherry.webservice.member.bl.MemberInfoLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 兑吧Action
 */
public class BINOLDUIBA01_Action extends BaseAction {

	//打印异常日志
	private static final Logger logger = LoggerFactory.getLogger(BINOLDUIBA01_Action.class);

	@Resource
	private BINOLDUIBA01_BL binOLDUIBA01_BL;
	@Resource
	private MemberInfoLogic memberInfoLogic;
	@Resource
	private BINOLCM00_BL binOLCM00_BL;

	//兑吧免登陆重定向URL
	private String redirectURL;

	/** 兑吧调用Cherry接口时传递的brandCode参数 **/
	private String brandCode;

	/** 兑吧调用Cherry接口时传递的参数 **/
	private String paramJson;

	private String errorMsg;


	@Resource
	BINOLMBMBM03_BL binOLMBMBM03_BL;


    /**
	 * 扣减积分
	 * @param
	 * @return
     */
	public void deductPoints() throws Exception {
		response.setContentType("text/html;charset=utf-8");//设置输出编码防止中文乱码
		CreditConsumeResult result=new CreditConsumeResult();
		try {
			String duiBaAppKey = request.getParameter("appKey");
			SystemConfigDTO configDTO = SystemConfigManager.getSystemConfigByDuibaAppkey(duiBaAppKey);
			Map<String,Object> searchMap = new HashMap<String, Object>();
			if(configDTO!=null){
				SystemConfigManager.setBrandDataSource(configDTO.getBrandCode());

				searchMap.put("BIN_OrganizationInfoID",configDTO.getOrganizationInfoID());
				searchMap.put("BIN_BrandInfoID",configDTO.getBrandInfoID());
				searchMap.put("OrgCode",configDTO.getOrgCode());
				searchMap.put("OrganizationCode",configDTO.getOrgCode());
				searchMap.put("BrandCode",configDTO.getBrandCode());
				searchMap.put("brandInfoId",configDTO.getBrandInfoID());
				searchMap.put("organizationInfoId",configDTO.getOrganizationInfoID());
				searchMap.put("TradeType","GetMemberInfo");//获取会员信息
				searchMap.put("ConditionType","MemberCode");
				searchMap.put("IsDimensionCode","1");

				if(CherryChecker.isNullOrEmpty(request.getParameter("uid"))){
					throw new Exception("参数uid不能为空");
				}
				if(CherryChecker.isNullOrEmpty(request.getParameter("credits"))){
					throw new Exception("参数credits不能为空");
				}
				if(CherryChecker.isNullOrEmpty(request.getParameter("orderNum"))){
					throw new Exception("参数orderNum不能为空");
				}

				CreditTool tool=new CreditTool(duiBaAppKey, configDTO.getDuibaAppSecret());
				CreditConsumeParams params= tool.parseCreditConsume(request);//利用tool来解析这个请求

				searchMap.put("Condition",params.getUid());
				searchMap.put("OrderNum",params.getOrderNum());
				searchMap.put("Point",params.getCredits());
				searchMap.put("ExchangeType",params.getType());
				searchMap.put("ExchangeParams",params.getParams());
				searchMap.put("MemberCode",params.getUid());

				String uid=params.getUid();//用户id
				Long credits=params.getCredits();//消耗积分
				String orderNum=params.getOrderNum();//兑吧订单号


				//得到会员卡号对应的会员积分和会员ID
				Map<String,Object> memberInfoMap = memberInfoLogic.getMemberInfo(searchMap);
					List<Map<String,Object>> memberInfoList =  (List<Map<String,Object>>)memberInfoMap.get("ResultContent");

				if(memberInfoList==null || memberInfoList.size()==0){
					result.setSuccess(false);
					result.setErrorMessage("没查询到对应的会员信息。uid=" + uid );
					result.setCredits(0L);
					response.getWriter().write(result.toString());
					return;
				}else if(memberInfoList.size()>1){
					result.setSuccess(false);
					result.setErrorMessage("查询到多条会员信息。uid=" + uid );
					result.setCredits(0L);
					response.getWriter().write(result.toString());
					return;
				}

				searchMap.put("BIN_MemberInfoID", ConvertUtil.getString(memberInfoList.get(0).get("MemberID")));
				double currentPoint = Double.valueOf(ConvertUtil.getString(memberInfoList.get(0).get("CurrentPoint")));//当前积分
				double creditsPoint = Double.valueOf(request.getParameter("credits")); //本次兑换扣减的积分

				if(currentPoint < creditsPoint){
					result.setSuccess(false);
					result.setErrorMessage("积分不足:" + currentPoint );
					result.setCredits(0L);
					response.getWriter().write(result.toString());
					return;
				}

				//TODO 开发者系统对uid用户扣除credits个积分,同时记录该兑换请求
				searchMap.put("BrandCode", configDTO.getBrandCode());
				searchMap.put("TradeType", "PointMaintenance");//维护会员积分
				searchMap.put("MaintType", "2");//1：修改积分总值，2：修改积分差值
				searchMap.put("MaintPoint", "-" + String.valueOf(creditsPoint));
				/*SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();*/
				String sysDateTime = binOLCM00_BL.getSYSDateTime();
				searchMap.put("BusinessTime", sysDateTime);
				searchMap.put("Sourse", "Other");

				try {
					initMap(searchMap);
					binOLDUIBA01_BL.insertDuiBaExchangeRequest(searchMap);
					memberInfoLogic.modifyMemberPoint(searchMap);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new Exception("维护积分失败");
				}

				result.setSuccess(true);
				result.setErrorMessage("");
				result.setCredits(Math.round(Double.parseDouble(String.valueOf(currentPoint-creditsPoint))));//积分余额
				result.setBizId(orderNum);

				response.getWriter().write(result.toString());//将返回信息输出

			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrorMessage(e.getMessage());
			result.setCredits(0L);
			response.getWriter().write(result.toString());
		}finally{
			CustomerContextHolder.clearCustomerDataSourceType();
		}
	}


	/**
	 * 参数初始化方法
	 * @param map
     */
	public void initMap(Map<String,Object> map){
		// 作成者
		map.put(CherryConstants.CREATEDBY,  "BINOLDUIBA01");
		// 更新者
		map.put(CherryConstants.UPDATEDBY,  "BINOLDUIBA01");
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLDUIBA01");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLDUIBA01");

	}

	/**
	 * 第三方借道跳转至兑吧时使用的方法
	 * 针对会员，在此方法中验证会员信息，返回redirect的URL
	 * @return
	 * @throws Exception
	 */
	public String memberRedirectDuiba() throws Exception {

		Map retMap = new HashMap<String,String>();
		String retString="";

		try {

			SystemConfigDTO configDTO = SystemConfigManager.getSystemConfig(brandCode);

			String organizationInfoId = ConvertUtil.getString(configDTO.getOrganizationInfoID());
			String brandInfoId = ConvertUtil.getString(configDTO.getBrandInfoID());

			String appKey = configDTO.getDuibaAppKey();
			String appSecret = configDTO.getDuibaAppSecret();


			//检查品牌代码，设定数据源
			if(!SystemConfigManager.setBrandDataSource(brandCode)){
				retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
				retString = CherryUtil.map2Json(retMap);
				this.addActionError(retString);
				return ERROR;
			}
			//解密参数
			String aeskey = configDTO.getAesKey();

			Map<String,Object> paramMap = null;
			try{
				paramMap = CherryUtil.json2Map(CherryAESCoder.decrypt(paramJson, aeskey));
			}catch (Exception e){
				retMap.put("ERRORMSG", "参数paramJson错误。paramJson=" + paramJson);
				retString = CherryUtil.map2Json(retMap);
				this.addActionError(retString);
				logger.error(retString);
				return ERROR;
			}

			Map<String,Object> tempMap = new HashMap<String,Object>();
			tempMap.put("BIN_OrganizationInfoID",organizationInfoId);
			tempMap.put("BIN_BrandInfoID",brandInfoId);
			tempMap.put("ConditionType","MessageID");
			tempMap.put("Condition",paramMap.get("openid"));

			Map<String,Object> memberInfoMap =  memberInfoLogic.getMemberInfo(tempMap);

			if(memberInfoMap.get("ResultContent") == null){
				retMap.put("ERRORMSG", "未找到对应的会员积分信息。paramJson=" + paramJson);
				retString = CherryUtil.map2Json(retMap);
				this.addActionError(retString);
				logger.error(retString);
				return ERROR;
			}

			List<Map<String,Object>> memberPointList = (List<Map<String,Object>>)memberInfoMap.get("ResultContent");

			Map<String,Object> memberPointMap;

			if( memberPointList.size() == 1 ){
				memberPointMap = memberPointList.get(0);
			}else{
				retMap.put("ERRORMSG", "对应的会员积分信息不存在或存在多条，请联系管理员。paramJson=" + paramJson);
				retString = CherryUtil.map2Json(retMap);
				this.addActionError(retString);
				logger.error(retString);
				return ERROR;
			}


			String credits = ConvertUtil.getString(memberPointMap.get("CurrentPoint"));
			String uid = ConvertUtil.getString(memberPointMap.get("MemberCode"));

			Map<String, String> signParams=new HashMap<String, String>();
			signParams.put("uid",uid);
			signParams.put("credits", String.valueOf(Math.round(Double.parseDouble(credits))));
			CreditTool tool=new CreditTool(appKey,appSecret);
			redirectURL=tool.buildUrlWithSign("http://www.duiba.com.cn/autoLogin/autologin?",signParams);

			return SUCCESS;

		} catch (Exception ex) {

			logger.error(ex.getMessage(),ex);

			retMap.put("ERRORMSG", ex.getMessage());
			retString = CherryUtil.map2Json(retMap);
			this.addActionError(CherryUtil.map2Json(retMap));

			return ERROR;
		}finally {
			CustomerContextHolder.clearCustomerDataSourceType();
		}
	}
	/**
	 * 兑吧兑换结果通知接收的方法
	 * 接收兑换结果,更新兑换记录表
	 * @return
	 * @throws Exception
	 */
	public void duibaresult() throws Exception {

		response.setContentType("text/html;charset=utf-8");
		String orderNum = "";
		try {
			SystemConfigDTO configDTO = SystemConfigManager.getSystemConfigByDuibaAppkey(request.getParameter("appKey"));
			Map<String, Object> map = new HashMap<String, Object>();
			CreditTool tool = new CreditTool(configDTO.getDuibaAppKey(), configDTO.getDuibaAppSecret());

			boolean isSuccess = false;
			String errmsg = "";

			//如果MD5校验失败会抛出异常
			CreditNotifyParams params = tool.parseCreditNotify(request);//利用tool来解析这个请求
			orderNum = params.getOrderNum();
			isSuccess = params.isSuccess();
			errmsg = params.getErrorMessage();

			SystemConfigManager.setBrandDataSource(configDTO.getBrandCode());
			map.put("orderNum", orderNum);

			if (isSuccess) {
				//兑换成功
				map.put("exchangeStatus", "1");
				map.put("updatePGM", "BINOLDUIBA01");
				binOLDUIBA01_BL.updateExchangeRequest(map);
			} else {
				//兑换失败，根据orderNum，对用户的积分进行返还，回滚操作
				Map<String, Object> membermap = new HashMap<String, Object>();
				membermap = binOLDUIBA01_BL.getBIN_ExchangeRequest(map);
				if (!CherryUtil.isEmpty(ConvertUtil.getString(membermap.get("MemberCode")))) {
					map.put("exchangeStatus", "0");
					map.put("updatePGM", "BINOLDUIBA01");
					//更新兑换请求记录表
					int result = binOLDUIBA01_BL.updateExchangeRequest(map);
					//发送积分维护的MQ
					if (result == 1) {
						map.put("BIN_OrganizationInfoID",configDTO.getOrganizationInfoID());
						map.put("BIN_BrandInfoID", configDTO.getBrandInfoID());
						map.put("OrganizationCode", configDTO.getOrgCode());
						map.put("OrgCode", configDTO.getOrgCode());
						map.put("BrandCode", configDTO.getBrandCode());
						map.put("TradeType", "PointMaintenance");
						map.put("BillCode", orderNum);
						map.put("MemberCode", membermap.get("MemberCode"));
						map.put("MaintType", "2");
						map.put("MaintPoint", ConvertUtil.getString(membermap.get("Point")));
						map.put("MaintainType", "0");
						//SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String sysDateTime = binOLCM00_BL.getSYSDateTime();
						map.put("BusinessTime",sysDateTime);
						map.put("Sourse", "Other");
						map.put("Comment", errmsg);
						memberInfoLogic.modifyMemberPoint(map);
					}
				}

			}
			response.getWriter().write("OK");
		} catch (Exception e) {
			logger.error("兑吧兑换结果通知接收失败单号为:" + orderNum, e);
			response.getWriter().write("fail");
			return;
		} finally {
			CustomerContextHolder.clearCustomerDataSourceType();
		}

	}


	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
