/*	
 * @(#)BINOLSSPRM71_Action.java     1.0 2015/09/21		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.ss.prm.action;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM77_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM99_BL;
import com.cherry.ss.prm.dto.BillInfo;
import com.cherry.ss.prm.form.BINOLSSPRM77_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM74_IF;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.opensymphony.xwork2.ModelDriven;


/**
 * 家化专用，POS弹窗，优惠券查询，发送短信
 * @author dingyongchang
 *
 */
public class BINOLSSPRM77_Action extends BaseAction implements ModelDriven<BINOLSSPRM77_Form> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1384075613172435642L;

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM77_Action.class);
	
	private BINOLSSPRM77_Form form = new BINOLSSPRM77_Form();
	
	static DESPlus dp=null;
	static{
		try {
			dp =new DESPlus(CherryConstants.PROMOTIONKEY);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	
	@Resource(name="binOLSSPRM74_BL")
	private BINOLSSPRM74_IF binOLSSPRM74_IF;
	
	@Resource(name="binOLSSPRM77_BL")
	private BINOLSSPRM77_BL binOLSSPRM77_BL;
	
	@Resource(name="coupon_IF")
	private BINOLSSPRM99_BL coupon_IF;

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public BINOLSSPRM77_Form getModel() {
		return form;
	}
	/**
	 * 初始化页面
	 */
	public String init_1() {
		return SUCCESS;
	}

	/**
	 * 页面初始化
	 * @return
	 */
	public String prm77Init() {
		//Cat埋点
		Transaction transaction = Cat.newTransaction("BINOLSSPRM77_Action", "prm77Init");
		try {
			Map<String,Object> result_map=new HashMap<String, Object>();
			String brandCode=form.getBrandCode();
			if(brandCode == null || "".equals(brandCode) ){
				result_map.put("resultCode", "-8888");
				result_map.put("resultMessage", "传入brandCode参数为空");
				form.setResult_map(result_map);
				transaction.addData("传入brandCode参数为空");
				return ERROR;
			}
			Map<String,Object> dataSource_map=new HashMap<String, Object>();
			dataSource_map.put("brandCode", brandCode);
			Map<String,Object> datasource=binOLSSPRM74_IF.getDateSourceName(dataSource_map);
			if(datasource == null){
				result_map.put("resultCode", "-7777");
				result_map.put("resultMessage", "传入brandCode没有找到相应数据源");
				form.setResult_map(result_map);
				transaction.addData("传入brandCode没有找到相应数据源");
				return ERROR;
			}
			String datasourceName=ConvertUtil.getString(datasource.get("dataSourceName"));
			form.setDatasourceName(datasourceName);
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 设置数据源
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			if(form.getParamdata() == null || "".equals(form.getParamdata()) ){
				result_map.put("resultCode", "-6666");
				result_map.put("resultMessage", "传入paramData参数为空");
				form.setResult_map(result_map);
				transaction.addData("传入paramData参数为空");
				return ERROR;
			}
			String contentText_json = "";
			try {
				contentText_json = dp.decrypt(form.getParamdata());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				result_map.put("resultCode", "-6666");
				result_map.put("resultMessage", "传入paramData参数解密失败");
				form.setResult_map(result_map);
				transaction.addData("传入paramData参数解密失败");
				return ERROR;
			}
			
			Map<String, Object> param = new HashMap<String,Object>();
			try {
				param = ConvertUtil.json2Map(contentText_json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				result_map.put("resultCode", "-6666");
				result_map.put("resultMessage", "传入paramData参数转换Map失败");
				form.setResult_map(result_map);
				transaction.addData("传入paramData参数转换Map失败");
				return ERROR;
			}
			
			//TODO:检查业务参数  	
			if(param.get("BP") == null || "".equals(param.get("BP"))){
				result_map.put("resultCode", "-5555");
				result_map.put("resultMessage", "传入BP参数为空");
				form.setResult_map(result_map);
				transaction.addData("传入BP参数不能为空");
				return ERROR;
			}else{
				form.setBpCode((String) param.get("BP"));
				form.setMemCount((String)param.get("MC"));
				form.setMemName((String)param.get("MN"));
				form.setMemPhone((String)param.get("MP"));
				form.setMemLevel((String)param.get("ML"));
			}
			form.setParamdataStr(contentText_json);
			transaction.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			logger.error("促销页面初始化失败");
			logger.error(e.getMessage(), e);
			Map<String,Object> result_map=new HashMap<String, Object>();
			result_map.put("resultCode", "-9999");
			result_map.put("resultMessage", "系统发生错误");
			form.setResult_map(result_map);
			transaction.setStatus(e);
			Cat.logError(e);
		}finally {
			transaction.complete();
		}
		return SUCCESS;
	}
	
	public String prm77Seach() throws Exception{
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		map.put("bpCode", form.getBpCode());
		map.put("status", form.getStatus());
		map.put("memPhone", form.getMemPhone());

		int count = binOLSSPRM77_BL.getCouponInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// dataTable上传的参数设置到map
		
		if(count != 0){
			
			//获取优惠券的List
			form.setCouponList(binOLSSPRM77_BL.getCouponInfoList(map));
		}
		
		return SUCCESS;
		
	}
	
	/**
	 * 短信重发
	 * @throws Exception 
	 */
	public void prm77SendMsg() throws Exception{
		
		//获取请求的参数
		String couponNo = request.getParameter("couponNo");
		String memberCode = request.getParameter("memberCode");
		String mobile = request.getParameter("mobile");
		String bpCode = request.getParameter("bpCode");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("brandCode", request.getParameter("brandCode"));
		//获取数据库品牌组织信息配置
		Integer organizationInfoID = null;
		Integer brandInfoID = null;
		Map<String,Object> brandInfo = binOLSSPRM77_BL.getBrandInfo(map);
		if(brandInfo != null){
			organizationInfoID = CherryUtil.obj2int(brandInfo.get("organizationInfoID"));
			brandInfoID = CherryUtil.obj2int(brandInfo.get("brandInfoID"));
		}
		//获取会员电子优惠券信息
		map = new HashMap<String, Object>();
    	map.put("couponNo", couponNo);
    	map.put("bpCode", bpCode);
    	Map<String, Object> memCouponInfo = binOLSSPRM77_BL.getCouponInfo(map);
    	String billCode = null;
    	if(memCouponInfo != null){
    		billCode = (String) memCouponInfo.get("batchNo");
    	}
    	
    	BillInfo billInfo = new BillInfo();
    	
    	billInfo.setBrandInfoId(brandInfoID);
    	billInfo.setOrganizationInfoId(organizationInfoID);
    	billInfo.setMobile(mobile);
    	billInfo.setMemberCode(memberCode);
    	billInfo.setAllCoupon(couponNo);
    	billInfo.setBillCode(billCode);
    	boolean sendFlag = false;
    	try {
    		if (binOLCM14_BL.isConfigOpen("1366", billInfo.getOrganizationInfoId(), billInfo.getBrandInfoId())) {
    			coupon_IF.sendSms(billInfo);
    			sendFlag = true;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	//将发送信息的结果返回到异步请求
    	ConvertUtil.setResponseByAjax(response, sendFlag);
	}
	
}
