package com.cherry.mb.svc.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.svc.form.BINOLMBSVC02_Form;
import com.cherry.mb.svc.interfaces.BINOLMBSVC02_1_IF;
import com.cherry.mb.svc.interfaces.BINOLMBSVC02_IF;
import com.cherry.mb.svc.service.BINOLMBSVC02_Service;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLMBSVC02_Action  extends BaseAction implements
ModelDriven<BINOLMBSVC02_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5580782144874637852L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMBSVC02_Action.class);
	@Resource(name ="binOLMBSVC02_IF")  
	private BINOLMBSVC02_IF binOLMBSVC02_IF;
	
	@Resource(name ="binOLMBSVC02_1_IF")  
	private BINOLMBSVC02_1_IF binOLMBSVC02_1_IF;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLMBSVC02_Service binOLMBSVC02_Service;
	
	/** 参数FORM */
	private BINOLMBSVC02_Form form = new BINOLMBSVC02_Form();
	/**
	 *初始化储值卡界面 
	 */
	public String init(){
		// 用户信息
		CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		if(counterInfo!=null && !"".equals(counterInfo)){
			form.setCounterCode(counterInfo.getCounterCode());
		}else {
			form.setCounterCode("");
		}
		return SUCCESS;
	}
	/**
	 * 储值卡业务小结画面查询
	 * 
	 * @return 业务小结画面
	 */
	public String cardSearch() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 所属组织
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 用户信息
		CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		if(counterInfo!=null && !"".equals(counterInfo)){
			map.put("counterCode", ConvertUtil.getString(counterInfo.getCounterCode()));
			form.setCounterCode(counterInfo.getCounterCode());
		}
		if("".equals(ConvertUtil.getString(form.getDateType()))){
			//正式数据的情况
			map.put("dateType", 0);
		}else if("1".equals(ConvertUtil.getString(form.getDateType()))){
			//测试数据的情况
			map.put("dateType", 1);
		}
		map.put("employeeId", userInfo.getBIN_EmployeeID());
		// 查询数据的条数
		Map<String,Object> cardCountInfo = new HashMap<String, Object>();
		int count = binOLMBSVC02_Service.getCardDetailCount(map);
		cardCountInfo.put("count", count);		
		form.setCardCountInfo(cardCountInfo);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			List<Map<String,Object>> cardList = binOLMBSVC02_IF.getCardList(map);
			form.setCardList(cardList);
		}
		return SUCCESS;
	}
	
	/**
	 * 储值卡销售明细业务小结画面查询
	 * 
	 * @return 业务小结画面
	 */
	public String saleSearch() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
				String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
				String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 所属组织
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询数据的条数
		Map<String,Object> saleCountInfo = binOLMBSVC02_IF.getSaleCountInfo(map);
		int count = 0;
		if (saleCountInfo != null) {
			count = (Integer) saleCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			List<Map<String,Object>> saleList = binOLMBSVC02_IF.getSaleList(map);
			form.setSaleList(saleList);
		}
		return SUCCESS;
	}
	
	/**
     * 储值卡信息导出Excel验证处理
     */
	public void cardExportCheck() throws Exception {
		// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
						String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
						String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		
		
		int count = binOLMBSVC02_Service.getCardDetailCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	
	/**
     * 储值卡信息导出Excel
     */
    public String cardExportExcel() throws Exception {
        
        try {
        	// 登陆用户信息
        	UserInfo userInfo = (UserInfo) session
        			.get(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> msgParam = new HashMap<String, Object>();
    		msgParam.put("exportStatus", "1");
    		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, brandInfoId);
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "cardCode desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBSVC02_IF.getCardExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBSVC02_IF);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	
    /**
     * 储值卡信息导出CSV
     */
    public String cardExportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
    	try {
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		msgParam.put("exportStatus", "1");
    		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, brandInfoId);
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "cardCode desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLMBSVC02_Service.getCardDetailCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLMBSVC02_IF.cardExportCSV(map);
	    		if(tempFilePath != null) {
	    			msgParam.put("exportStatus", "1");
	    			msgParam.put("message", getText("ECM00096"));
	    			msgParam.put("tempFilePath", tempFilePath);
	    		} else {
	    			msgParam.put("exportStatus", "0");
	    			msgParam.put("message", getText("ECM00094"));
	    		}
			}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
    
    /**
     * 储值卡销售信息导出Excel验证处理
     */
	public void saleExportCheck() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
				String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
				String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		
		
		int count = binOLMBSVC02_Service.getSaleDetailCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	
	/**
     * 储值卡销售信息导出Excel
     */
    public String saleExportExcel() throws Exception {
        
        try {
        	Map<String, Object> msgParam = new HashMap<String, Object>();
        	// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		msgParam.put("exportStatus", "1");
    		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		msgParam.put("exportStatus", "1");
    		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, brandInfoId);
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "cardCode desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBSVC02_IF.getSaleExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "saleDownloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBSVC02_1_IF);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	
    /**
     * 储值卡销售信息导出CSV
     */
    public String saleExportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
    	try {
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		msgParam.put("exportStatus", "1");
    		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, brandInfoId);
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "cardCode desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLMBSVC02_Service.getCardDetailCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLMBSVC02_IF.saleExportCSV(map);
	    		if(tempFilePath != null) {
	    			msgParam.put("exportStatus", "1");
	    			msgParam.put("message", getText("ECM00096"));
	    			msgParam.put("tempFilePath", tempFilePath);
	    		} else {
	    			msgParam.put("exportStatus", "0");
	    			msgParam.put("message", getText("ECM00094"));
	    		}
			}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
    
	/**
	 * 初始化查看页面
	 */
	public String viewSaleInit(){
		return SUCCESS;
	}
	
	/**
	 * 停用储值卡
	 */
	public String stopCard() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cardId", form.getCardId());
			binOLMBSVC02_IF.stopCard(params);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 废弃储值卡
	 */
	public String abandonCard(){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cardId", form.getCardId());
			binOLMBSVC02_IF.abandonCard(params);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 储值卡开卡界面
	 */
	public String openCardInit(){
		return SUCCESS;
	}
	public static void main(String[] args) {
		try {
			DESPlus des = new DESPlus();
			String password=des.decrypt("");
			System.out.println(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 储值卡开卡操作
	 * @throws Exception 
	 */
	public void openCard() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String cardArr=form.getCardArr();
		List<Map<String,Object>> card_list=ConvertUtil.json2List(cardArr);
		Map<String,Object> card_map=new HashMap<String, Object>();
		//对批量开卡的储值卡密码进行加密操作
		for(Map<String,Object> cardInfo:card_list){
			String password=ConvertUtil.getString(cardInfo.get("Password"));
			String cardCode=ConvertUtil.getString(cardInfo.get("CardCode"));
			// 传入密码字段为空时自动生成密码
			DESPlus des = new DESPlus();
			try{
				if("".equals(password)){
						StringBuilder str=new StringBuilder();//定义变长字符串
						Random random=new Random();
						//随机生成数字，并添加到字符串
						for(int i=0;i<8;i++){
							str.append(random.nextInt(10));
						}
						//将字符串转换为数字并输出
						String password_num=str.toString();
//						password = des.encrypt(password_num);
//						cardInfo.put("Password", password);
						cardInfo.put("Password", password_num);
				}else{
					//已经存在密码加密后存入
//					cardInfo.put("Password", des.encrypt(password));
					cardInfo.put("Password", password);
				}
			}catch(Exception ex){
				// 加密失败，记录日志
				logger.info("储值卡批量录入WebService自动生成密码失败！储值卡号："+ cardCode +"，异常信息："+ ConvertUtil.getString(ex));
			}
		}
		card_map.put("CardList", card_list);
		card_map.put("brandCode", userInfo.getBrandCode());
		Map<String,Object> result_map=binOLMBSVC02_IF.SavingsCardAddCard(card_map);
		if(!"0".equals(ConvertUtil.getString(result_map.get("ERRORCODE")))){
			logger.info(result_map.toString());
		}
		ConvertUtil.setResponseByAjax(response, result_map);
	}
	
	/**
	 * 查看服务明细操作
	 */
	public String viewSaleDetailInit(){
		
		return SUCCESS;
	}
	
	/**
	 * 发送短信
	 * @return
	 */
	public void sendMessage(){
		String resultStr = "";
		try {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("cardCode", form.getCardCode());
			map.put("counterCode", form.getCounterCode());
			map.put("brandCode", userInfo.getBrandCode());
			resultStr = binOLMBSVC02_IF.getCouponCode(map);
		} catch (Exception e) {
			resultStr = "处理储值卡密码异常："+e;
			logger.error(resultStr);
		}finally{
			try {
				ConvertUtil.setResponseByAjax(response, resultStr);
			} catch (Exception e1) {
				logger.error("短信发送异常" , e1);
			}
		}
	}
	
	/**
	 * 确认短信验证码
	 */
	public void messageConfirm() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
		map.put("brandCode", userInfo.getBrandCode());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> retMap = binOLMBSVC02_IF.getNewPassword(map);
		ConvertUtil.setResponseByAjax(response, retMap);	
	}
	
	/**
	 * 储值卡修改密码确认
	 */
	public void passwordConfirm() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
		map.put("brandCode", userInfo.getBrandCode());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> retMap = binOLMBSVC02_IF.getNewPassword(map);
		String errorCode = ConvertUtil.getString(retMap.get("ERRORCODE"));
		if(!"0".equals(errorCode)){
			String errorMsg = ConvertUtil.getString(retMap.get("ERRORMSG"));
			ConvertUtil.setResponseByAjax(response, errorMsg);
		}else {
			ConvertUtil.setResponseByAjax(response, errorCode);
		}
			
	}
	
	/**
	 * 发送短信初始化页面
	 * @return
	 */
	public String SMSInit(){
		return SUCCESS;
	}
	
	/**
	 * 储值卡修改密码初始化页面
	 * @return
	 */
	public String oldPasswordInit(){
		return SUCCESS;
	}
	
	/**
	 * 储值卡查看服务查询
	 */
	public String serviceSearch() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 所属组织
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询数据的条数
		Map<String,Object> serviceCountInfo = binOLMBSVC02_IF.getServiceCountInfo(map);
		form.setServiceCountInfo(serviceCountInfo);
		int count = 0;
		if (serviceCountInfo != null) {
			count = (Integer) serviceCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			List<Map<String,Object>> serviceList = binOLMBSVC02_IF.getServiceList(map);
			form.setServiceList(serviceList);
		}
		return SUCCESS;
	}
	
	@Override
	public BINOLMBSVC02_Form getModel() {
		return form;
	}
	
	/** 下载文件名 */
    private String downloadFileName;
    /** Excel输入流 */
    private InputStream excelStream;
    
	public String getDownloadFileName() throws Exception {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	
}
