package com.cherry.webserviceout.kingdee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 韩束WS接口
 * @author jijw
 *
 * 韩束品牌已经结束服务，不再使用
 */
public class WebServiceKingdee {

//	@Resource(name = "webserviceDataSource")
//	private WebserviceDataSource webserviceDataSource;
	
	private static Logger logger = LoggerFactory.getLogger(WebServiceKingdee.class.getName());
	
	/** 200 获取成功 */
	public static final String Result_200 = "200";
	
	/** 201 获取失败 */
	public static final String Result_201 = "201";
	
	/** 业务处理返回值 Failed:成功 */
	public static final String FStatus_Successful = "Successful";
	
	/** 业务处理返回值 Failed:失败 */
	public static final String FStatus_Failed = "Failed";
	
	/**
	 * 企业号
	 */
	private String EID;
	
	/**
	 * AccSecret是Kis平台分配的
	 */
	private String AccSecret;
	
	/**
	 * 请求接口ID
	 */
	private String AccKey;
	
	/**
	 * 请求端的状态值（由16位大小写字母和数字组成）
	 */
	private String State;
	
	/**
	 * 签名串，值为md5(EID + AccSecret + Timestamp + State )，其中加号“+”为字符串连接符，AccSecret是本平台分配的
	 */
	private String Sign;
	
	/**
	 * 时间戳，格式为yyyy-mm-dd HH:mm:ss，例如：2014-03-14 16:52:03，KD API服务端允许客户端请求时间误差为3分钟，需要urlencode，Get方式需要urlencode
	 */
	private String Timestamp;
	
	/**
	 * 获取KIS ServerUrl方法的请求地址
	 */
	private String serverUrlStr;
	
	/**************************************************************************************************/
	
	/**
     * 调用KIS WS【获取KIS的 ServerUrl】
     * @param paraMap
     * paraMap参数说明：organizationInfoId（必填。所属组织ID）,
     * paraMap参数说明：brandInfoId（必填。所属品牌ID）,
	 * @return
	 * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public Map<String,Object> getServerUrl(Map<String,Object> paraMap) throws Exception
    {
//    	Map<String, Object> selMap = new HashMap<String, Object>();
//    	Map<String, Object> resultMap = new HashMap<String, Object>();
//    	selMap.putAll(paraMap);
//
////		String brandInfoID = ConvertUtil.getString((selMap.get("organizationInfoId")));
////		String organizationInfoID = ConvertUtil.getString(selMap.get("brandInfoId"));
//
//		// 业务类型
//    	selMap.put("ESCode", "kingdee");
//		selMap.put("tradeCode", "getKISServerUrl");
//
//    	// 取得第三方WS配置信息
//    	Map<String, Object> wsConfigMap = webserviceDataSource.getWSConfig(selMap);
//
//    	String ServerUrlStr = ConvertUtil.getString(wsConfigMap.get("URL"));
//    	// 扩展配置{"EID":549616,"AccSecret":"4vHUmAAqSEW5MtH4YF8gThE4kSqJFGNT7w3qvKJd","AccKey":"1406008060"}
//    	String extJson =  ConvertUtil.getString(wsConfigMap.get("ExtJson"));
//    	Map<String, Object> extMap = (Map<String, Object>) JSONUtil.deserialize(extJson);
//    	paraMap.putAll(extMap);
//
//    	// 企业号
////        String eID = this.EID;
//        String eID = ConvertUtil.getString(extMap.get("EID"));
//
//        // AccSecret是Kis平台分配的
////        String accSecret = this.AccSecret;
//        String accSecret = ConvertUtil.getString(extMap.get("AccSecret"));
//
//        // 请求接口ID
////        String accKey = this.AccKey;
//        String accKey = ConvertUtil.getString(extMap.get("AccKey"));
//
//        // 时间
//		SimpleDateFormat sf = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
//    	Calendar ca = Calendar.getInstance();
//        String timestamp = sf.format(ca.getTime());
//        String timestampEnc = URLEncoder.encode(timestamp, "UTF-8");
//
//        // 请求端的状态值（16位随机字符串含大小写字母和数字）
//        String state = getRandomCharAndNumr(16);
//        String sign = eID + accSecret + timestamp + state;
//
//        String url = ServerUrlStr;
//        url += "?EID=" + eID + "&Sign=" + CherryMD5Coder.encryptMD5(sign) + "&Timestamp=" + timestampEnc + "&State=" + state + "&AccKey=" + accKey;
//
//        try {
//        	String resultStr =  doGet(url);
//        	resultMap = CherryUtil.json2Map(resultStr);
//
//        	if(null != resultMap && !resultMap.isEmpty()){
//        		this.serverUrlStr = ConvertUtil.getString(resultMap.get("ServerUrl"));
//        	}else{
//        		resultMap.put("Result", "WSE0041");
//        		resultMap.put("ErrMsg", "WebService返回异常(获取KIS的 ServerUrl)");
//        	}
//
//        	return resultMap;
//        }catch(Exception e){
//        	logger.error("getServerUrl WS方法调用失败");
//        	logger.error("Webservice ERROR",e);
//        }
        return null;
    }
    
    /**
     * 请求KIS WebService服务端接口处理对应业务
     * @param paraMap
     * paraMap参数说明：organizationInfoId（必填。所属组织ID）,
     * paraMap参数说明：brandInfoId（必填。所属品牌ID）,
     * paraMap参数说明：tradeCode（必填。业务类型代号，存放于【电商接口信息表】）
     * paraMap参数说明：p_dataJson（选填。业务端Json数据包，业务数据参数请参考具体业务API说明）
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> accessServerResult(Map<String,Object> paraMap) throws Exception {
//
    	Map<String, Object> resultMap = new HashMap<String, Object>();
//
//    	// 【获取KIS的 ServerUrl】
//    	Map<String, Object> serverUrlMap = getServerUrl(paraMap);
//
//    	// serverUrl(通过GetServer方法获取)
//    	String serverUrl = ConvertUtil.getString(serverUrlMap.get("ServerUrl"));
//    	serverUrl = String.format("http://%s/Webapi/Router", serverUrl);
//
//    	// 业务类型代号(新后台电商接口表)
//    	String tradeCode = ConvertUtil.getString(paraMap.get("tradeCode"));
//
//    	// 取得第三方WS配置信息
//    	paraMap.put("ESCode", "kingdee");
//    	Map<String, Object> wsConfigMap = webserviceDataSource.getWSConfig(paraMap);
//    	// 扩展配置
//    	String extJson =  ConvertUtil.getString(wsConfigMap.get("ExtJson"));
//    	Map<String, Object> extMap = (Map<String, Object>) JSONUtil.deserialize(extJson);
//
//    	// eId企业号
//    	String eId = ConvertUtil.getString(paraMap.get("EID"));
//
//    	// 业务API名称 method
//    	String method = ConvertUtil.getString(extMap.get("Method"));
//    	paraMap.put("kisMethod", method);
//
//    	// 应用CODE
//    	String appNum = ConvertUtil.getString(paraMap.get("aPPnum"));
//    	// 名称由来说明：kis+应用Code+路由名称+类名+方法名
//        method = "kis." + appNum + ".uequery.KISPDAStockController." + method;//调用服务方法名
//
//        // 时间戳
//		SimpleDateFormat sf = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
//    	Calendar ca = Calendar.getInstance();
//        String timestamp = sf.format(ca.getTime());
//        String timestampEnc = URLEncoder.encode(timestamp, "UTF-8");
//
//    	// API协议版本，目前值为:1.0
//    	String ver = ConvertUtil.getString(paraMap.get("Ver"));
//
//    	// 来源标识
//    	String fromTag = ConvertUtil.getString(paraMap.get("FromTag"));
//
//    	// 是否返回新的Json数据格式(Y为返回新的Json数据格式，N为继续使用旧的Json数据格式，默认为N)
//    	String isNewJson = ConvertUtil.getString(paraMap.get("IsNewJson"));
//    	if(CherryChecker.isNullOrEmpty(isNewJson)){
//    		isNewJson = "Y";
//    	}
//
//    	// CustData数据是否加密，Y为加密，N为不加密，默认为N
//    	String isEncrypt = ConvertUtil.getString(paraMap.get("IsEncrypt"));
//    	if(CherryChecker.isNullOrEmpty(isEncrypt)){
//    		isEncrypt = "N";
//    	}
//
//    	// appSecret
//    	String appSecret = ConvertUtil.getString(serverUrlMap.get("AppSecret"));
//    	// 请求端的状态值（16位随机字符串含大小写字母和数字）
//    	String state = getRandomCharAndNumr(16);
////    	String state = "2EAEC2";
//    	// 签名
//        String sign = CherryMD5Coder.encryptMD5(eId + appSecret + method + timestamp + state).toUpperCase();
//
//        // CustData数据包参数-ProductID
//        String productId = ConvertUtil.getString(paraMap.get("ProductID"));
//        if(CherryChecker.isNullOrEmpty(productId)){
//        	productId = "";
//        }
//
//    	// CustData数据包参数-AccountDB帐套 (根据具体业务需要是否必填，比如帐务平台此处帐套为必填项)
//    	String accountDB = ConvertUtil.getString(paraMap.get("AccountDB"));
//    	if(CherryChecker.isNullOrEmpty(accountDB)){
//    		accountDB = "";
//    	}
//
//    	// CustData数据包参数-UserID
//    	String userid = ConvertUtil.getString(paraMap.get("UserID"));
//    	if(CherryChecker.isNullOrEmpty(userid)){
//    		userid = "";
//    	}
//
//    	// CustData数据包参数-PassWord
//    	String pwd = ConvertUtil.getString(paraMap.get("PassWord"));
//    	if(CherryChecker.isNullOrEmpty(pwd)){
//    		pwd = "";
//    	}
//
//    	// CustData数据包参数-CurrentPage 当前页
//    	String currentPage = ConvertUtil.getString(paraMap.get("CurrentPage"));
//    	if(CherryChecker.isNullOrEmpty(currentPage)){
//    		currentPage = "";
//    	}
//
//    	// CustData数据包参数-ItemsOfPage 每页多少条记录
//    	String itemsOfPage = ConvertUtil.getString(paraMap.get("ItemsOfPage"));
//    	if(CherryChecker.isNullOrEmpty(itemsOfPage)){
//    		itemsOfPage = "";
//    	}
//
//
//    	// CustData数据包参数-Data 业务端Json数据包，业务数据参数请参考具体业务API说明(即Method参数对应的API名称
//    	String p_dataJson = ConvertUtil.getString(paraMap.get("p_dataJson"));
//    	if(CherryChecker.isNullOrEmpty(p_dataJson)){
//    		p_dataJson = "\"\"";
//    	}
//
//        serverUrl += "?EID=" + eId + "&Method=" + method + "&Timestamp=" + timestampEnc + "&Ver=" + ver  + "&FromTag=" + fromTag;
//        serverUrl += "&IsNewJson=" + isNewJson + "&Sign=" + sign + "&State=" + state + "&CustData=";
//
//        String custData = "{\"ProductID\":\""+ productId  +"\",\"AccountDB\":\"" + accountDB + "\",\"UserID\":\"" + userid + "\",\"PassWord\":\"" + pwd + "\",\"CurrentPage\":\""+ currentPage + "\",\"ItemsOfPage\":\""+itemsOfPage +"\",\"Data\":" + p_dataJson + "}";
//
//        custData = URLEncoder.encode(custData, "UTF-8");
//        try {
//            serverUrl += custData;
//            String resultStr = doGet(serverUrl);
//        	resultMap = CherryUtil.json2Map(resultStr);
//
//        	if(null != resultMap && !resultMap.isEmpty()){
//        		String result = ConvertUtil.getString(resultMap.get("Result"));
//        		String errMsg = ConvertUtil.getString(resultMap.get("ErrMsg"));
//        		if(!WebServiceKingdee.Result_200.equals(result)){
//        			logger.error("Kingdee ErrorCode: " + result);
//        			logger.error("Kingdee ErrMsg: " + errMsg );
//        		}
//        	}else{
//        		resultMap.put("Result", "WSE0041");
//        		resultMap.put("ErrMsg", "WebService返回异常(调用业务接口【"+ tradeCode +"】)");
//        	}
//
//        }catch(Exception e){
//        	logger.error("getServerResult WS方法调用失败");
//        	logger.error("Webservice ERROR",e);
//        }
        return resultMap;
    }
    
    /**
     * 取得WS请求返回的数据
     * @param url
     * @return
     */
    public String doGet(String url)
    {
    	String result = null;
//        try
//        {
//            String serviceAddress = url;
//            WebResource webResource = binOLCM27_BL.getWebResource(serviceAddress);
//            MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
//            result = webResource.queryParams(queryParams).get(String.class);
//        }
//        catch(Exception e)
//        {
//           logger.error(e.getMessage(),e);
//        }
        
        return result;
    }
    
	
	 /**
    * 获取随机字母数字组合
    * 
    * @param length
    *            字符串长度
    * @return
    */
   public static String getRandomCharAndNumr(Integer length) {
       String str = "";
       Random random = new Random();
       for (int i = 0; i < length; i++) {
           boolean b = random.nextBoolean();
           if (b) { // 字符串
                int choice = random.nextBoolean() ? 65 : 97; //取得65大写字母还是97小写字母
               str += (char) (choice + random.nextInt(26));// 取得大写字母
           } else { // 数字
               str += String.valueOf(random.nextInt(10));
           }
       }
       return str;
   }

	public String getEID() {
		return EID;
	}


	public void setEID(String eID) {
		EID = eID;
	}


	public String getAccSecret() {
		return AccSecret;
	}


	public void setAccSecret(String accSecret) {
		AccSecret = accSecret;
	}


	public String getAccKey() {
		return AccKey;
	}


	public void setAccKey(String accKey) {
		AccKey = accKey;
	}


	public String getState() {
		return State;
	}


	public void setState(String state) {
		State = state;
	}


	public String getSign() {
		return Sign;
	}


	public void setSign(String sign) {
		Sign = sign;
	}


	public String getTimestamp() {
		return Timestamp;
	}


	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
	
	public String getServerUrlStr() {
		return serverUrlStr;
	}



	public void setServerUrlStr(String serverUrlStr) {
		this.serverUrlStr = serverUrlStr;
	}

	
	


}
