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


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM44_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.form.BINOLSSPRM78_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM74_IF;
import com.cherry.ss.prm.interfaces.Coupon_IF;
import com.cherry.ss.prm.service.BINOLSSPRM74_Service;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.opensymphony.xwork2.ModelDriven;


/**
 * 家化专用，POS弹窗，优惠券查询，发送短信
 * @author dingyongchang
 *
 */
public class BINOLSSPRM78_Action extends BaseAction implements ModelDriven<BINOLSSPRM78_Form> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1384075613172435641L;

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM78_Action.class);
	
	private BINOLSSPRM78_Form form = new BINOLSSPRM78_Form();
	

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
	
	@Resource
	private BINOLCM44_BL binOLCM44_BL;
	
	@Resource(name="binOLSSPRM74_Service")
	private BINOLSSPRM74_Service binOLSSPRM74_Service;
	
	@Resource
    private Coupon_IF coupon_IF;

	@Override
	public BINOLSSPRM78_Form getModel() {
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
	public String prm78Init() {
		//Cat埋点
		Transaction transaction = Cat.newTransaction("BINOLSSPRM77_Action", "prm77Init");
		try {
			Map<String,Object> result_map=new HashMap<String, Object>();
			String brandCode=form.getBrandCode();
			Map<String,Object> dataSource_map=new HashMap<String, Object>();
			dataSource_map.put("brandCode", brandCode);
			Map<String,Object> datasource=binOLSSPRM74_IF.getDateSourceName(dataSource_map);
			if(datasource == null){
				result_map.put("resultCode", "-7777");
				result_map.put("resultMessage", "输入的品牌代码有误");
				form.setResult_map(result_map);
				transaction.addData("输入的品牌代码有误");
				return SUCCESS;
			}
			String datasourceName=ConvertUtil.getString(datasource.get("dataSourceName"));
			form.setDatasourceName(datasourceName);
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 设置数据源
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			if(form.getParamdata() == null){
				result_map.put("resultCode", "-6666");
				result_map.put("resultMessage", "传入参数为空");
				form.setResult_map(result_map);
				transaction.addData("传入参数为空");
				return SUCCESS;
			}
			String contentText_json=dp.decrypt(form.getParamdata());
			Map<String,Object> param=ConvertUtil.json2Map(contentText_json);
			//TODO:检查业务参数  		    
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
}
