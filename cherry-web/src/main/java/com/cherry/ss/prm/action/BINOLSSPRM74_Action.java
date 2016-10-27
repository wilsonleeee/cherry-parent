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


import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM44_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.form.BINOLSSPRM74_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM74_IF;
import com.cherry.ss.prm.interfaces.Coupon_IF;
import com.cherry.ss.prm.service.BINOLSSPRM74_Service;
import com.cherry.wp.common.entity.*;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BINOLSSPRM74_Action extends BaseAction implements ModelDriven<BINOLSSPRM74_Form> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1384075613172435643L;

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM74_Action.class);
	
	private BINOLSSPRM74_Form form = new BINOLSSPRM74_Form();
	

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

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public BINOLSSPRM74_Form getModel() {
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
	public String promotionInit() {
		Transaction transaction = Cat.newTransaction("BINOLSSPRM74_Action", "promotionInit");
		try {
			logger.info("促销页面初始化开始");
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
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
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
			//打印POS传入的参数
			logger.error("智能促销页面初始化参数：",param);
  		    //封装原始购物车数据
			Map<String,Object> convert_map= binOLSSPRM74_IF.convert2Part(param);
			if("Pro".equals(ConvertUtil.getString(convert_map.get("errCode")))){
				result_map.put("resultCode", "-8888");
				result_map.put("resultMessage", "输入的产品有误");
				form.setResult_map(result_map);
				transaction.addData("输入的产品有误");
				return SUCCESS;
			}
			Map<String,Object> main_map=(Map<String,Object>)convert_map.get("main_map");
			main_map.put("brandCode", brandCode);
			main_map.put("BC", brandCode);
			String organizationID=ConvertUtil.getString(main_map.get("organizationID"));
			List<Map<String,Object>> cart_list=(List<Map<String,Object>>) convert_map.get("cart_list");
			List<Map<String,Object>> cartOrder_list=(List<Map<String,Object>>) convert_map.get("cartOrder_list");
			//之前先调用优惠券查询接口(MP MC 不为NULL时)
			String MP=ConvertUtil.getString(main_map.get("MP"));
			String MC=ConvertUtil.getString(main_map.get("MC"));
			List<Map<String,Object>> coupon_list=new ArrayList<Map<String,Object>>();
			if(!"".equals(MP) && !"".equals(MC)){
				Map<String,Object> coupon_param=new HashMap<String, Object>();
				coupon_param.put("Main_map", main_map);
				coupon_param.put("cart_map", cartOrder_list);
				coupon_list= coupon_IF.getCouponList(coupon_param);
			}
			Map<String,Object> input_map=binOLSSPRM74_IF.convert2Entity(main_map,cartOrder_list,coupon_list);
			ArrayList<SaleMainEntity> salemain_input=(ArrayList<SaleMainEntity>)input_map.get("main_input");
			ArrayList<SaleDetailEntity> saledetail_input=(ArrayList<SaleDetailEntity>)input_map.get("detail_input");
			double sum_cart=Double.parseDouble(ConvertUtil.getString(input_map.get("sum_cart")).equals("")?"0.00":ConvertUtil.getString(input_map.get("sum_cart")));
			String coupon_id=ConvertUtil.getString(input_map.get("coupon_id"));
			ArrayList<SaleRuleResultEntity> saleresult_out=new ArrayList<SaleRuleResultEntity>();
			ArrayList<SaleProductDetailEntity> saleproduct_out=new ArrayList<SaleProductDetailEntity>();
			ArrayList<SaleActivityDetailEntity> saleactivity_out=new ArrayList<SaleActivityDetailEntity>();
			int result=binOLCM44_BL.cloud_MatchRule_JIAHUA(brandCode, organizationID, salemain_input, saledetail_input,saleactivity_out ,saleresult_out,saleproduct_out);
			//记录日志
			smartLog(result,1);
			//封装页面右侧的产品列表，通过maincode进行分组
			List<Map<String,Object>> product_list=binOLSSPRM74_IF.convertProduct(saleproduct_out);
			
			//封装页面需要的参数,其中有2部分.并且把返回的TZZK数据的Unicode和Barcode合并入rule_list中
			Map<String,Object> rule_list_jsp=binOLSSPRM74_IF.convert2JSP(saleresult_out,saleactivity_out);
			ArrayList<Map<String,Object>> rule_list= (ArrayList<Map<String,Object>>)rule_list_jsp.get("rule_list");
			String rule_id=ConvertUtil.getString(rule_list_jsp.get("rule_id"));
			List<Map<String,Object>> allActivity=binOLSSPRM74_IF.convert2AllActivity(main_map);
			Map<String,Object> resultAll=new HashMap<String, Object>();
			resultAll.put("rule_list", rule_list);
			resultAll.put("coupon_list", coupon_list);
			result_map.put("main_map", main_map);//主单信息
			result_map.put("allActivity", allActivity);//全部规则列表
			result_map.put("rule_list", rule_list);//规则列表
			result_map.put("shoppingcart_list", cartOrder_list);//分组后的购物车List
			result_map.put("coupon_list", coupon_list);//优惠券列表
			result_map.put("product_map", product_list);//活动对应的可选产品
			result_map.put("result_json", CherryUtil.obj2Json(resultAll));//规则列表Json
			result_map.put("result_id", coupon_id+rule_id.toString());//规则列表对应的Id
			result_map.put("main_json", CherryUtil.map2Json(main_map));//主单信息Json
			result_map.put("shoppingcart_json", CherryUtil.obj2Json(cart_list));//原始购物车Json
			result_map.put("shoppingcartOrder_json", CherryUtil.obj2Json(cartOrder_list));//分组购物车Json
			result_map.put("sum_cart", sum_cart);//购物车总金额
			form.setResult_map(result_map);
			logger.info("促销页面初始化成功结束");
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
	/**
	 * 计算方法
	 * @throws Exception
	 */
	public void promotionGetComputeRule() throws Exception{
		Transaction transaction = Cat.newTransaction("BINOLSSPRM74_Action", "promotionInit");
		try {
			String datasourceName=form.getDatasourceName();
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			Map<String,Object> result_map=new HashMap<String, Object>();
			List<Map<String,Object>> shoppingcart_list=ConvertUtil.json2List(form.getShoppingcart_json());
			List<Map<String,Object>> couponCheck_list=ConvertUtil.json2List(form.getCoupon_json());
			List<Map<String,Object>> couponAll_list=ConvertUtil.json2List(form.getCouponAll_json());
			Map<String,Object> main_map=ConvertUtil.json2Map(form.getMain_json());
			String usePoint="".equals(form.getUsePoint())?"0":form.getUsePoint();
			//非会员用券的手机号
			String memberPhone=ConvertUtil.getString(form.getMemberPhone());
			if("0".equals(usePoint)){
				main_map.put("MPOINT", null);
			}else{
				main_map.put("MPOINT", usePoint);
			}
			if("".equals(ConvertUtil.getString(main_map.get("MP")))){
				main_map.put("MP", memberPhone);
			}
			List<Map<String,Object>> rule_list_input= ConvertUtil.json2List(form.getRule_json());
			String brandCode=ConvertUtil.getString(main_map.get("brandCode"));
//			String organizationID=ConvertUtil.getString(main_map.get("organizationID"));
			//校验券接口，确定券可以使用后传入活动中进行计算
			List<Map<String,Object>> coupon_list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> cart_list=new ArrayList<Map<String,Object>>();
			Map<String,Object> coupon_map=new HashMap<String, Object>();
			List<Map<String,Object>> rule_coupon_input =binOLSSPRM74_IF.checkRule(rule_list_input);
			String resultCode="0";
			if(couponCheck_list != null){
				if(couponCheck_list.size() > 0){
					Map<String,Object> check_map=new HashMap<String, Object>();
					check_map.put("Main_map", main_map);
					check_map.put("cart_map", shoppingcart_list);
					check_map.put("coupon_list", couponCheck_list);
					check_map.put("Rule_list", rule_coupon_input);
					Map<String, Object> couponCheck_map=coupon_IF.checkCoupon(check_map);
					resultCode=ConvertUtil.getString(couponCheck_map.get("ResultCode"));
					String resultMsg=ConvertUtil.getString(couponCheck_map.get("ResultMsg"));
					String errorCoupon=ConvertUtil.getString(couponCheck_map.get("errorCoupon"));
					if(!"0".equals(resultCode)){
						result_map.put("resultCode", resultCode);
						result_map.put("resultMsg", resultMsg);
						result_map.put("errorCoupon", errorCoupon);
//						ConvertUtil.setResponseByAjax(response, result_map);
//						transaction.setStatus(Transaction.SUCCESS);
//						return;
					}else{
						Map<String,Object> content=coupon_map=(Map<String,Object>)couponCheck_map.get("Content");
						List<Map<String,Object>> checked_coupon=(List<Map<String,Object>>)content.get("checked_coupon");
						List<Map<String,Object>> checked_cart=(List<Map<String,Object>>)content.get("checked_cart");
						cart_list=checked_cart;
						coupon_list=checked_coupon;
					}
				}
			}
			List<Map<String,Object>> result_coupon=binOLSSPRM74_IF.convert2Coupon(coupon_list,couponAll_list,couponCheck_list);
			Map<String,Object> input_map=new HashMap<String, Object>();
			if(cart_list != null && cart_list.size() > 0){
				input_map=binOLSSPRM74_IF.convert2Entity(main_map,cart_list,result_coupon);
			}else{
				input_map=binOLSSPRM74_IF.convert2Entity(main_map,shoppingcart_list,result_coupon);
			}
			ArrayList<SaleMainEntity> salemain_input=(ArrayList<SaleMainEntity>)input_map.get("main_input");
			ArrayList<SaleDetailEntity> saledetail_input=(ArrayList<SaleDetailEntity>)input_map.get("detail_input");
			String coupon_id=ConvertUtil.getString(input_map.get("coupon_id"));
			ArrayList<SaleRuleResultEntity> saleresult_input=binOLSSPRM74_IF.convert2Rule(rule_list_input);
			ArrayList<SaleRuleResultEntity> saleresult_out=new ArrayList<SaleRuleResultEntity>();
			ArrayList<SaleDetailEntity> saledetail_out=new ArrayList<SaleDetailEntity>();
			int result=binOLCM44_BL.cloud_ComputeRule_JIAHUA(brandCode, salemain_input, saledetail_input, saleresult_input, saleresult_out,saledetail_out);
			//记录日志
			smartLog(result,2);
			//封装促销引擎计算出的TZZK与DH的数据，后续直接写表
			List<Map<String,Object>> computedRule=binOLSSPRM74_IF.detail2RuleList(saledetail_out,saleresult_out);
			//封装优惠券与促销引擎计算完成的购物车相关信息
			List<Map<String,Object>> computedCart=binOLSSPRM74_IF.detail2ComputedList(saledetail_out);
			//封装页面需要的参数,其中有2部分
			Map<String,Object> rule_list_jsp=binOLSSPRM74_IF.convert2JSP(saleresult_out,null);
			ArrayList<Map<String,Object>> rule_list= (ArrayList<Map<String,Object>>)rule_list_jsp.get("rule_list");
			String rule_id=ConvertUtil.getString(rule_list_jsp.get("rule_id"));
			Map<String,Object> param_map=new HashMap<String, Object>();
			if(coupon_list !=null && coupon_list.size() >0){
				param_map.put("coupon_map", coupon_list.get(0));
			}
			param_map.put("coupon_list", result_coupon);
			param_map.put("rule_list", rule_list);
			param_map.put("rule_id",coupon_id+rule_id.toString());
			param_map.put("computedRule", computedRule);
			param_map.put("computedCart", computedCart);
			result_map.put("resultCode", resultCode);
			result_map.put("content", param_map);
			ConvertUtil.setResponseByAjax(response, result_map);
			transaction.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			transaction.setStatus(e);
			Cat.logError(e);
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
		} finally {
			transaction.complete();
		}
	}
	public static void main(String[] args) throws Exception {
//		生成测试的购物车数据
		Map<String,Object> jiahua_input=new HashMap<String, Object>();
//		jiahua_input.put("BP", "2004110576");
//		jiahua_input.put("MC", "18516660913");
//		jiahua_input.put("MN", "TEST");
//		jiahua_input.put("MP", "18516660913");
//		jiahua_input.put("ML", "ZBCCJ0103");
		List<Map<String,Object>> jiahua_cart=new ArrayList<Map<String,Object>>();
		jiahua_input.put("TN", "TN0003");
		jiahua_input.put("TD", "20160905");
		jiahua_input.put("TT", "11:30:33");
		jiahua_input.put("CC", "ZZSN2");
		jiahua_input.put("MC", "18916142988");
		jiahua_input.put("MN", "小龙人");
		jiahua_input.put("MP", "18916142988");
		jiahua_input.put("ML", "ZBCCJ0101");
		jiahua_input.put("SC", jiahua_cart);
		Map<String,Object> cart1=new HashMap<String, Object>();
		cart1.put("B", "28666");
		cart1.put("Q", "1");
		cart1.put("P", "320");
		jiahua_cart.add(cart1);
		Map<String,Object> cart2=new HashMap<String, Object>();
		cart2.put("B", "28666");
		cart2.put("Q", "1");
		cart2.put("P", "320");
		jiahua_cart.add(cart2);
		Map<String,Object> cart3=new HashMap<String, Object>();
		cart3.put("B", "28666");
		cart3.put("Q", "1");
		cart3.put("P", "320");
		jiahua_cart.add(cart3);
		Map<String,Object> cart4=new HashMap<String, Object>();
		cart4.put("B", "28666");
		cart4.put("Q", "1");
		cart4.put("P", "320");
		jiahua_cart.add(cart4);
//		Map<String,Object> cart5=new HashMap<String, Object>();
//		cart5.put("B", "75001");
//		cart5.put("Q", "1");
//		cart5.put("P", "30");
//		jiahua_cart.add(cart5);
//		Map<String,Object> cart6=new HashMap<String, Object>();
//		cart6.put("B", "75008");
//		cart6.put("Q", "1");
//		cart6.put("P", "30");
//		jiahua_cart.add(cart6);
		String jiahua_json=CherryUtil.map2Json(jiahua_input);
		//加密字符串
		String mima1=dp.encrypt(jiahua_json);
		System.out.println(mima1);
		//解密字符串
//		String mima2=dp.decrypt("1b5c6c8275859804569423336efe475dd6492908f09a2d582d007ab441cf285eb07263193aac1f3be60b7bea628e166159c1839823d9daf758a7a84e2501ace367fe7eeb6dfbba4f3ccdf69c4568e9b6848f3f054b48da4366a77d71718d9adee14b6285446c080f8fae680041e1230d92791378888b533b8a0d37202bcbb1ff3fc7548e82d179260f28e7ccc7ad2857a6668392376baffc42296fdaaac42a5349223d4d25557e63206379accdbdbf37bb601e12ffd7a7b0337e957497e3e6c1b7bd9b8778eaac783ea72f5b133a2fde575e94655306cbdf43bfe470ac67ab5ce4e651fe1d659e78eae5d536cc823e78048f7a8d354afa16bb6c4cf81f027b3c1f92ec8c7a1a1dd4c11ca1e6a1b4c47ac2225c6a4cb8d4cd1efd33d7b018a36ab458aa93800e131c003c510adb88f0fcb830530a2340ad390cf610f821cf34f254c220b9a443b1e17a09da2f05edf653bd781b774ac76740edc1fbe04abedfc15d0c49df45b62207cb5f4565c7df1474eaf30fa689ab99262238f36ba542720d2a976237887097e96ecb7353e598838c25fb3707333ff3fb1b09ed219c73c2ea058d01b3bb6d111e");
//		Map<String,Object> map_json=ConvertUtil.json2Map(mima2);
	}
	/**
	 * 写表操作
	 * @throws Exception 
	 */
	public void tran_promotionCollect() throws Exception{
		try {
			String datasourceName=form.getDatasourceName();
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			//主单信息
			Map<String,Object> main_map=ConvertUtil.json2Map(form.getMain_json());
			if(1 == ConvertUtil.getInt(form.getCloseFlag())){
				//取消按钮触发，删除该单据号所有的数据
				binOLSSPRM74_IF.checkMain(main_map);
				logger.info("取消按钮触发，删除该单据号所有的数据成功");
				ConvertUtil.setResponseByAjax(response, "0");
				return;
			}
			logger.info("数据录入开始");
			String TN=ConvertUtil.getString(main_map.get("TN"));
			//如果主单中已经存在有其单据号的情况，先做物理删除再进行插入
			binOLSSPRM74_IF.checkMain(main_map);
			//购物车信息
			List<Map<String,Object>> cart_list=ConvertUtil.json2List(form.getShoppingcart_json());
			//1、计算完毕的规则（N类型）
			List<Map<String,Object>> rule_list=ConvertUtil.json2List(form.getRule_json());
			//2、计算完毕的规则（P类型）
			List<Map<String,Object>> promotionRule_list=ConvertUtil.json2List(form.getPromotionRule_json());
			//3、计算完毕的P类型积分兑换活动(和2数据进行合并用)
			List<Map<String,Object>> pointRule_list=ConvertUtil.json2List(form.getPointRule_json());
			
			//合并完整的促销活动 1+（2&3）
			List<Map<String,Object>> result_rule= binOLSSPRM74_IF.convertRuleList(rule_list, promotionRule_list, pointRule_list);
			
			//计算完毕的优惠券信息
			List<Map<String,Object>> coupon_list=ConvertUtil.json2List(form.getCoupon_json());
			if(main_map != null && cart_list !=null){
				//获得累计优惠金额
				double discountTotal=0;
				if(form.getDiscountTotal() == null || "".equals(form.getDiscountTotal())){
					discountTotal=0;
				}else{
					discountTotal=Double.parseDouble(form.getDiscountTotal());
				}
				main_map.put("discountAmount", discountTotal);
				//实际销售价
				double actualTotal=0;
				if(form.getActualTotal() == null || "".equals(form.getActualTotal())){
					actualTotal=0;
				}else{
					actualTotal=Double.parseDouble(form.getActualTotal());
				}
				//折前金额
				double receivableTotal=0;
				if(form.getReceivableTotal() == null || "".equals(form.getReceivableTotal())){
					receivableTotal=0;
				}else{
					receivableTotal=Double.parseDouble(form.getReceivableTotal());
				}
				//计算完毕的积分
				double computedPoint=0;
				for(Map<String,Object> rule:result_rule){
					double rulepoint=Double.parseDouble("".equals(ConvertUtil.getString(rule.get("computePoint")))?"0":ConvertUtil.getString(rule.get("computePoint")));
					computedPoint += rulepoint;
				}
				int totalQuantity=0;
				for(Map<String,Object> cart:cart_list){
					int quantity=Integer.parseInt("".equals(ConvertUtil.getString(cart.get("quantity")))?"0":ConvertUtil.getString(cart.get("quantity")));
					totalQuantity += quantity;
				}
				main_map.put("originalAmount", receivableTotal);//折前金额
				main_map.put("totalQuantity", totalQuantity);//商品总数量
				main_map.put("totalAmount", actualTotal);//应收总金额
				main_map.put("computedPoint", computedPoint);//已经使用的积分
				//如果memberLevel为空的话，不写入memberCode
                if( "".equals(ConvertUtil. getString(main_map.get("ML")))){
                     main_map.put("MC", "");
               }
                //在写入主表数据之前先调用发券查询接口判断此单是否可以发券
                //转换购物车添加产商ID字段
				List<Map<String,Object>> cartConvert=binOLSSPRM74_IF.collect2pro(cart_list);
                Map<String,Object> coupon_input=new HashMap<String, Object>();
                coupon_input.put("Main_map", main_map);
                coupon_input.put("cart_map", cartConvert);
                coupon_input.put("completedRule", result_rule);
                coupon_input.put("completedCoupon", coupon_list);
                List<Map<String, Object>> couponResult=coupon_IF.getCouponRuleList(coupon_input);
                if(couponResult != null && couponResult.size() > 0){
                	main_map.put("SendFlag", 1);
                }else{
                	main_map.put("SendFlag", 0);
                }
                if(form.getMemberPhone() != null && form.getMemberPhone().length() == 11){
                	String memberPhone=form.getMemberPhone().trim();
                	main_map.put("MP", memberPhone);
                }
                //打印智能促销页面写入的主单数据
    			logger.error("打印智能促销页面写入的主单数据：",main_map);
				binOLSSPRM74_IF.insertMain(main_map);
				
				if(coupon_list != null){
					binOLSSPRM74_IF.insertCoupon(coupon_list, TN);
				}
				if(rule_list != null){
					binOLSSPRM74_IF.insertRule(result_rule, TN);
				}
				if(cart_list != null){
					binOLSSPRM74_IF.insertCart(cart_list, TN);
				}else{
					logger.info("智能促销录入数据购物车信息为空");
				}
				ConvertUtil.setResponseByAjax(response, "0");
			}
			logger.info("数据录入成功结束");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("智能促销数据录入失败");
			ConvertUtil.setResponseByAjax(response, "1");
		}
		
		
	}
	
	/**
	 * 代物券页面初始化
	 * @return
	 */
	public String promotionProSendInit() {
		Transaction transaction = Cat.newTransaction("BINOLSSPRM74_Action", "promotionProSendInit");
		try {
			logger.info("代物券页面初始化开始");
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
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
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
			//打印代物券POS传入的参数
			logger.error("打印代物券POS传入的参数：",param);
			Map<String,Object> convertMap=binOLSSPRM74_IF.convert2Part(param);
			Map<String,Object> main_map=(Map<String,Object>)convertMap.get("main_map");
			main_map.put("BC", brandCode);
			main_map.put("brandCode", brandCode);
			Map<String,Object> param_map=new HashMap<String,Object>();
			param_map.put("Main_map", main_map);
			//调用代物券查询接口
			String MP=ConvertUtil.getString(main_map.get("MP"));
			String MC=ConvertUtil.getString(main_map.get("MC"));
			List<Map<String,Object>> Rule_list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> ProductList=new ArrayList<Map<String,Object>>();
			if(!"".equals(MP) && !"".equals(MC)){
				Map<String, Object> dwq_map=coupon_IF.getDwqInfo(param_map);
				if(null != dwq_map){
					Rule_list=(List<Map<String,Object>>)dwq_map.get("checked_coupon");
					ProductList=(List<Map<String,Object>>)dwq_map.get("ProductList");
				}
			}
			//接口返回的产品根据couponCode进行分组
			List<Map<String,Object>> couponProList=binOLSSPRM74_IF.couponProOrder(ProductList);
			result_map.put("main_json", CherryUtil.map2Json(main_map));//主单信息Json
			result_map.put("main_map", main_map);//主单信息
			result_map.put("couponProList", couponProList);//代物券产品列表
			result_map.put("rule_list", Rule_list);//代物券产品列表
			form.setResult_map(result_map);
			logger.info("代物券页面初始化成功结束");
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
	
	/**
	 * 代物券界面左下角Coupon校验
	 * @throws Exception
	 */
	public void promotionProSendCheck() throws Exception{
		try {
			logger.info("代物券界面Coupon校验开始");
			String datasourceName=form.getDatasourceName();
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			//主单信息
			Map<String,Object> main_map=ConvertUtil.json2Map(form.getMain_json());
			//需要检验的券信息
			Map<String,Object> coupon_map=ConvertUtil.json2Map(form.getCoupon_json());
			//此处调用接口
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("Main_map", main_map);
			param.put("coupon_map", coupon_map);
			Map<String, Object> result_map=coupon_IF.checkDwq(param);
			//测试数据开始
			ConvertUtil.setResponseByAjax(response, result_map);
			logger.info("代物券界面Coupon校验正常结束");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("代物券界面Coupon校验失败");
			ConvertUtil.setResponseByAjax(response, "1");
		}
	}
	/**
	 * 代物券写表操作
	 * @throws Exception
	 */
	public void tran_promotionSendProCollect() throws Exception{
		try {
			String datasourceName=form.getDatasourceName();
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			//主单信息
			Map<String,Object> main_map=ConvertUtil.json2Map(form.getMain_json());
			String TN=ConvertUtil.getString(main_map.get("TN"));
			if(1 == ConvertUtil.getInt(form.getCloseFlag())){
				//取消按钮触发，删除该单据号所有的数据
				binOLSSPRM74_IF.checkMain(main_map);
				logger.info("取消按钮触发，删除该单据号所有的数据成功");
				ConvertUtil.setResponseByAjax(response, "0");
				return;
			}
			logger.info("代物券数据录入开始");
			//如果主单中已经存在有其单据号的情况，先做物理删除再进行插入
			binOLSSPRM74_IF.checkMain(main_map);
			//计算完毕的优惠券信息
			List<Map<String,Object>> coupon_list=ConvertUtil.json2List(form.getCoupon_json());
			List<Map<String,Object>> cart_list=ConvertUtil.json2List(form.getShoppingcart_json());
			double amount=0;
			int totalQuantity=0;
			if(cart_list != null){
				for(Map<String,Object> coupon:cart_list){
					double price=Double.parseDouble(ConvertUtil.getString(coupon.get("price")));
					int quantity=Integer.parseInt(ConvertUtil.getString(coupon.get("quantity")));
					totalQuantity += quantity;
					amount += price*quantity;
				}
			}
			if(main_map != null){
				main_map.put("MP", form.getMemberPhone());
				main_map.put("originalAmount", amount);//折前金额
				main_map.put("totalQuantity", totalQuantity);//商品总数量
				main_map.put("totalAmount", 0);//应收总金额
				main_map.put("discountAmount", -amount);//总优惠金额
				//代物券暂时没有与积分挂钩默认写入0
				main_map.put("computedPoint", 0);
				//如果memberLevel为空的话，不写入memberCode
                if( "".equals(ConvertUtil. getString(main_map.get("ML")))){
                     main_map.put( "MC", "");
               }
                //打印代物券写表的主单数据
    			logger.error("打印代物券写表的主单数据：",main_map);
				binOLSSPRM74_IF.insertMain(main_map);
				
				if(cart_list != null){
					binOLSSPRM74_IF.insertCart(cart_list, TN);
				}
				List<Map<String,Object>> order_list=new ArrayList<Map<String,Object>>();
				if(coupon_list != null){
					binOLSSPRM74_IF.insertCoupon(coupon_list, TN);
					//写入规则表之前进行分组
					List<Map<String,Object>> group_list=ConvertUtil.listGroup(coupon_list, "maincode", "rule_list");
					for(Map<String,Object> group:group_list){
						List<Map<String,Object>> product_list=(List<Map<String,Object>>)group.get("rule_list");
						Map<String,Object> result_map=new HashMap<String, Object>();
						result_map.putAll(product_list.get(0));
						result_map.put("Quantity", product_list.size());
						result_map.put("mainCode", result_map.get("Maincode"));
						order_list.add(result_map);
					}
					binOLSSPRM74_IF.insertRule(order_list, TN);
				}
				String MemberCode=ConvertUtil.getString(main_map.get("MC"));
				String MemberPhone=ConvertUtil.getString(main_map.get("MP"));
				String bpCode=ConvertUtil.getString(main_map.get("BP"));
				//代物券核券操作
				for(Map<String,Object> coupon:coupon_list){
					coupon.put("TradeNoIF", TN);
					coupon.put("MemberCode", MemberCode);
					coupon.put("MemberPhone", MemberPhone);
					coupon.put("bpCode", bpCode);
					binOLSSPRM74_IF.updateProCoupon(coupon);
				}
				ConvertUtil.setResponseByAjax(response, "0");
			}
			logger.info("代物券数据录入成功结束");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("代物券数据录入失败");
			ConvertUtil.setResponseByAjax(response, "1");
		}
	}
	
	public String promotionSendInit() {
		Transaction transaction = Cat.newTransaction("BINOLSSPRM74_Action", "promotionSendInit");
		try {
			logger.info("发券页面初始化开始");
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
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			if(form.getParamdata() == null){
				result_map.put("resultCode", "-6666");
				result_map.put("resultMessage", "传入参数为空");
				form.setResult_map(result_map);
				transaction.addData("传入参数为空");
				return SUCCESS;
			}
			String contentText_json=dp.decrypt(form.getParamdata());
			Map<String,Object> input_param=ConvertUtil.json2Map(contentText_json);
			//打印发券页面初始化POS传入的参数
			logger.error("打印发券页面初始化POS传入的参数：",input_param);
			String tradeNoIF=ConvertUtil.getString(input_param.get("TN"));
			if("".equals(tradeNoIF)){
				result_map.put("resultCode", "-7777");
				result_map.put("resultMessage", "传入单据号为空");
				form.setResult_map(result_map);
				transaction.addData("传入单据号为空");
				return SUCCESS;
			}
			//通过单据号查询相关的信息
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("tradeNoIF", tradeNoIF);
			Map<String,Object> main_map=binOLSSPRM74_IF.getMainByTradeNo(param);
			if(null == main_map){
				result_map.put("resultCode", "-5555");
				result_map.put("resultMessage", "没有找到相应的单据");
				form.setResult_map(result_map);
				transaction.addData("没有找到相应的单据");
				return SUCCESS;
			}
			String sendChooseFlag=binOLCM14_BL.getConfigValue("1376", ConvertUtil.getString(main_map.get("organizationInfoID")), ConvertUtil.getString(main_map.get("brandInfoID")));
			form.setSendChooseFlag(sendChooseFlag);
			param.put("CC", ConvertUtil.getString(main_map.get("CC")));
			String memberCode=ConvertUtil.getString(main_map.get("MC"));
			String memberPhone=ConvertUtil.getString(main_map.get("MP"));
			//通过柜台号获取响应的组织ID等等的参数
			Map<String,Object> sysInfo=binOLSSPRM74_Service.getOrganizationID(param);
			String orgCode=ConvertUtil.getString(sysInfo.get("orgCode"));
			String brandInfoID=ConvertUtil.getString(sysInfo.get("brandInfoID"));
			String organizationInfoID=ConvertUtil.getString(sysInfo.get("organizationInfoID"));
			main_map.put("brandInfoId", brandInfoID);
			main_map.put("orgCode", orgCode);
			main_map.put("BC", brandCode);
			main_map.put("organizationInfoID", organizationInfoID);
			List<Map<String,Object>> cart_list=binOLSSPRM74_IF.getShoppingCartByTradeNo(param);
			List<Map<String,Object>> completedRule=binOLSSPRM74_IF.getRuleByTradeNo(param);
			List<Map<String,Object>> completedCoupon=binOLSSPRM74_IF.getCouponByTradeNo(param);
			Map<String,Object> interface_input=new HashMap<String, Object>();
			interface_input.put("Main_map", main_map);
			interface_input.put("cart_map", cart_list);
			interface_input.put("completedCoupon", completedCoupon);
			interface_input.put("completedRule", completedRule);
			//此处调用接口
			List<Map<String, Object>> rule_list=coupon_IF.getCouponRuleList(interface_input);
			//对返回的rule_list进行封装，加上标识 让前端同一组的用radio 单独的用checkbox
			List<Map<String, Object>> rule_ord_list=binOLSSPRM74_IF.groupSendRule(rule_list);
			//调用接口完毕
			result_map.put("main_map_json", CherryUtil.map2Json(main_map));
			result_map.put("cart_list_json", CherryUtil.obj2Json(cart_list));
			result_map.put("rule_list_json",CherryUtil.obj2Json(completedRule));
			result_map.put("rule_list", rule_ord_list);
			result_map.put("main_map", main_map);
			result_map.put("tradeNoIF", tradeNoIF);
			result_map.put("memberCode", memberCode);
			result_map.put("memberPhone", memberPhone);
			form.setResult_map(result_map);
			logger.info("发券页面初始化结束");
			transaction.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			logger.error("发券页面初始化失败");
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
	
	
	/**
	 * 发券操作
	 * @throws Exception 
	 */
	public void promotionSend() throws Exception{
		try {
			logger.info("智能促销发券操作开始");
			String datasourceName=form.getDatasourceName();
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,datasourceName);
			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(datasourceName);
			//主单信息
			Map<String,Object> main_map=ConvertUtil.json2Map(form.getMain_json());
			//购物车信息
			List<Map<String,Object>> shoppongcart_list=ConvertUtil.json2List(form.getShoppingcart_json());
			//已经享受的促销活动
			List<Map<String,Object>> rule_list=ConvertUtil.json2List(form.getRule_json());
			//页面上已经计算完成的促销活动
			List<Map<String,Object>> competedRule_list=ConvertUtil.json2List(form.getCompetedRule_json());
			//非会员发券情况添加手机号
			if(form.getMemberPhone() != null && form.getMemberPhone().length() == 11){
            	String memberPhone=form.getMemberPhone().trim();
            	main_map.put("MP", memberPhone);
            }
			//打印发券操作调用时传入的主单数据
			logger.error("打印发券操作调用时传入的主单数据：",main_map);
			Map<String,Object> coupon_input=new HashMap<String, Object>();
			coupon_input.put("Main_map", main_map);
			coupon_input.put("cart_map", shoppongcart_list);
			coupon_input.put("completedRule", rule_list);
			coupon_input.put("calculatedRule", competedRule_list);
			Map<String, Object> result_map=coupon_IF.tran_createCoupon(coupon_input);
			
			//测试数据
//			Map<String,Object> result_map=new HashMap<String, Object>();
//			result_map.put("ResultCode", 1);
//			result_map.put("ResultMsg", "核券失败");
//			logger.error("");
			ConvertUtil.setResponseByAjax(response, result_map);
			logger.info("智能促销发券操作正常结束");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("智能促销发券操作失败");
			ConvertUtil.setResponseByAjax(response, "1");
		}
		
		
	}
	
	
	/**
	 * 用来打印智能促销查询规则方法返回所对应的日志
	 */
	private void smartLog(int num,int type){
		if(type == 1){
			if(num == 0){
				logger.error("智能促销查询方法调用异常");
			}else if(num == 3){
				logger.error("智能促销查询方法数据库连接异常");
			}else if(num == 4){
				logger.error("智能促销查询方法更新最新的规则,稍后重试");
			}else if(num == 5){
				logger.error("智能促销查询方法socket通讯失败");
			}
		}else if(type == 2){
			if(num == 0){
				logger.error("智能促销计算方法调用异常");
			}else if(num == 3){
				logger.error("智能促销计算方法数据库连接异常");
			}else if(num == 4){
				logger.error("智能促销计算方法更新最新的规则,稍后重试");
			}else if(num == 5){
				logger.error("智能促销计算方法socket通讯失败");
			}
		}
	}
	
}
