/*
 * @(#)BINOLSSPRM71_BL.java     1.0 2015/09/21
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
package com.cherry.ss.prm.bl;


import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.form.BINOLSSPRM74_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM74_IF;
import com.cherry.ss.prm.interfaces.Coupon_IF;
import com.cherry.ss.prm.service.BINOLSSPRM74_Service;
import com.cherry.webserviceout.jahwa.ZSAL_MEMINFO;
import com.cherry.webserviceout.jahwa.common.JahwaWebServiceProxy;
import com.cherry.wp.common.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLSSPRM74_BL implements BINOLSSPRM74_IF {

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM74_BL.class);

	@Resource(name="binOLSSPRM74_Service")
	private BINOLSSPRM74_Service binOLSSPRM74_Service;

	@Resource
	private Coupon_IF coupon_IF;

	@Override
	public Map<String, Object> convert2Entity(Map<String,Object> main_map,List<Map<String,Object>> cart_list,List<Map<String,Object>> coupon_list) {
		Map<String,Object> result_map=new HashMap<String, Object>();
		//单据号
//		String TN=ConvertUtil.getString(main_map.get("TN"));
		//交易日期
		String TD=ConvertUtil.getString(main_map.get("TD"));
		//交易时间
		String TT=ConvertUtil.getString(main_map.get("TT"));
		//品牌代号
		String BC=ConvertUtil.getString(main_map.get("BC"));
		//柜台代号
		String CC=ConvertUtil.getString(main_map.get("CC"));
		//会员卡号
		String MC=ConvertUtil.getString(main_map.get("MC"));
		//会员姓名
		String MN=ConvertUtil.getString(main_map.get("MN"));
		//会员手机号
		String MP=ConvertUtil.getString(main_map.get("MP"));
		//会员等级
		String ML=ConvertUtil.getString(main_map.get("ML"));
		//柜台ID
		String organizationID=ConvertUtil.getString(main_map.get("organizationID"));
		//会员积分
		double MPOINT=Double.parseDouble("".equals(ConvertUtil.getString(main_map.get("MPOINT")))?"0":ConvertUtil.getString(main_map.get("MPOINT")));
		//购物车信息
		List<SaleMainEntity> main_input=new ArrayList<SaleMainEntity>();
		List<SaleDetailEntity> detail_input=new ArrayList<SaleDetailEntity>();
		SaleMainEntity main=new SaleMainEntity(MC, TD, TT,"", organizationID, 100,ML,MPOINT);
		main_input.add(main);
		double sum=0;
		for(int i=1;i<=cart_list.size();i++){
			Map<String,Object> cart_map=cart_list.get(i-1);
			String barcode=ConvertUtil.getString(cart_map.get("barCode"));
			int quantity=Integer.parseInt(ConvertUtil.getString(cart_map.get("quantity")));
			double price=Double.parseDouble(ConvertUtil.getString(cart_map.get("price")).equals("")?"0.00":ConvertUtil.getString(cart_map.get("price")));
			int productId=ConvertUtil.getInt(cart_map.get("productId"));
			String unitCode=ConvertUtil.getString(cart_map.get("unitCode"));
			double salePrice=Double.parseDouble(ConvertUtil.getString(cart_map.get("salePrice")).equals("")?"0.00":ConvertUtil.getString(cart_map.get("salePrice")));
			sum += salePrice*quantity;
			String nameTotal=ConvertUtil.getString(cart_map.get("nameTotal"));
			String originalBrand=ConvertUtil.getString(cart_map.get("originalBrand"));
			String maincode=ConvertUtil.getString(cart_map.get("maincode"));
			double discount=100;
			if(salePrice == 0.00 || price == 0.00){
				discount=100;
			}else{
				discount=(salePrice/price)*100;
			}
			SaleDetailEntity detail=new SaleDetailEntity(barcode, unitCode, quantity, salePrice,
					price, "N", maincode, "", i, discount, 
					productId, nameTotal, 0, "", 0, originalBrand);
			detail_input.add(detail);
		}
		//如果存在优惠券加入detail中
		StringBuilder coupon_id = new StringBuilder("");
		if(coupon_list !=null && coupon_list.size() >0){
			for(Map<String,Object> coupon_map:coupon_list){
				if(null == coupon_map){
					continue;
				}
				String couponName=ConvertUtil.getString(coupon_map.get("couponName"));
				String barcode=ConvertUtil.getString(coupon_map.get("barcode"));
				String unicode=ConvertUtil.getString(coupon_map.get("unicode"));
				Double planDiscountPrice =Double.parseDouble(ConvertUtil.getString(coupon_map.get("planDiscountPrice")).equals("")?"0.00":ConvertUtil.getString(coupon_map.get("planDiscountPrice")));
				Double actualDiscountPrice =Double.parseDouble(ConvertUtil.getString(coupon_map.get("actualDiscountPrice")).equals("")?"0.00":ConvertUtil.getString(coupon_map.get("actualDiscountPrice")));
				String checkFlag=ConvertUtil.getString(coupon_map.get("checkFlag"));
				int couponType=Integer.parseInt("".equals(ConvertUtil.getString(coupon_map.get("couponType")))?"1":ConvertUtil.getString(coupon_map.get("couponType")));
				String cp="";
				if(1 == couponType){
					cp="T";
				}else if(3 == couponType){
					cp="Z";
				}else if(4 == couponType){
					cp="J";
				}
				double discount=100;
				if(actualDiscountPrice == 0.00 || planDiscountPrice == 0.00){
					discount=100;
				}else{
					discount=(actualDiscountPrice/planDiscountPrice)*100;
				}
				if("1".equals(checkFlag)){
					SaleDetailEntity detail=new SaleDetailEntity(barcode, unicode, 1, actualDiscountPrice,
							planDiscountPrice, cp, "", "", detail_input.size()+1, discount, 
							0, couponName, 0, "", 0, "");
					coupon_id.append("1");
					detail_input.add(detail);
				}else{
					coupon_id.append("0");
				}
			}
		}
		
		
		result_map.put("main_input", main_input);
		result_map.put("detail_input", detail_input);
		result_map.put("sum_cart", sum);
		result_map.put("coupon_id", coupon_id.toString());
		return result_map;
	}

	@Override
	public Map<String,Object> convert2JSP(
			ArrayList<SaleRuleResultEntity> ruleResult_list,ArrayList<SaleActivityDetailEntity> saleactivity_out) {
		Map<String,Object> result_map=new HashMap<String, Object>();
		ArrayList<Map<String,Object>> rule_list=new ArrayList<Map<String,Object>>();
		StringBuilder rule_id = new StringBuilder();
		for(SaleRuleResultEntity rule:ruleResult_list){ 
			Map<String,Object> rule_map=new HashMap<String, Object>();
			rule_map.put("maincode", rule.getMaincode());
			Map<String,Object> activityInfo=binOLSSPRM74_Service.getActivityInfo(rule_map);
			//活动详细描述
			String descriptionDtl=ConvertUtil.getString(activityInfo.get("descriptionDtl"));
			rule_map.put("descriptionDtl", descriptionDtl);
			//截至时间
			String endTime=ConvertUtil.getString(activityInfo.get("endTime"));
			rule_map.put("endTime", endTime);
			//规则名称
			rule_map.put("mainname", rule.getMainname());
			//是否选中
			rule_map.put("checkFlag", rule.getCheckflag());
			//是否存在需要交互的产品，0表示不需要交互，1表示需要交互
			rule_map.put("flag", rule.getFlag());
			//活动数量
			rule_map.put("times", rule.getTimes());
			//最大匹配数量
			rule_map.put("matchtimes", rule.getMatchtimes());
			//可选不可选  0：可选；1：不可选 
			rule_map.put("ismust", rule.getIsmust());
			//促销条件类型 1：整单类，2：非整单类
			rule_map.put("rulecondtype", rule.getRulecondtype());
			//校验方式[0(无需校验) 1（本地校验）2（在线校验）]
			rule_map.put("subcampaignvalid", rule.getSubcampaignvalid());
			//规则等级
			rule_map.put("level", rule.getLevel());
			//是否选中，0是未选中，1是选中
			rule_map.put("checkflag", rule.getCheckflag());
			//活动code
			rule_map.put("activitycode", rule.getActivitycode());
			//预计优惠金额
			rule_map.put("planDiscountPrice", rule.getPlanDiscountPrice());
			//实际优惠金额
			rule_map.put("actualDiscountPrice", rule.getActualDiscountPrice());
			//活动类型
			rule_map.put("activityType", rule.getActivityType());
			//产品可选数量
			rule_map.put("productNumber", rule.getProductNumber());
			//是否需要资格券的标识
			rule_map.put("ZGQFlag", rule.getZGQFlag());
			//参加当前活动需要的积分
			rule_map.put("computePoint", rule.getComputePoint());
			//是否全部选中
			rule_map.put("fullFlag", rule.getFullFlag());
			//是否可以同时使用优惠券
			rule_map.put("couponFlag", rule.getCouponFlag());
			//单位使用积分
			rule_map.put("unitPoint", rule.getUnitPoint());
			//最大使用积分
			rule_map.put("maxPoint", rule.getMaxPoint());
			if(saleactivity_out != null){
				for(SaleActivityDetailEntity detail:saleactivity_out){
					if(ConvertUtil.getString(detail.getMaincode()).equals(rule.getMaincode())){
						rule_map.put("unicode", detail.getUnitcode());
						rule_map.put("barcode", detail.getBarcode());
					}
				}
			}
			if(rule.getCheckflag() == 1){
				rule_id.append("1");
			}else{
				rule_id.append("0");
			}
			rule_list.add(rule_map);
		}
		result_map.put("rule_list", rule_list);
		result_map.put("rule_id", rule_id.toString());
		return result_map;
	}

	@Override
	public ArrayList<SaleRuleResultEntity> convert2Rule(
			List<Map<String, Object>> param) {
		ArrayList<SaleRuleResultEntity> rule_list=new ArrayList<SaleRuleResultEntity>();
		for(Map<String,Object> rule:param){
			int flag=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("flag")))?"0":ConvertUtil.getString(rule.get("flag")));
			int times=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("times")))?"0":ConvertUtil.getString(rule.get("times")));
			int matchtimes=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("matchtimes")))?"0":ConvertUtil.getString(rule.get("matchtimes")));
			String maincode=ConvertUtil.getString(rule.get("maincode"));
			String mainname=ConvertUtil.getString(rule.get("mainname"));
			String rulecondtype=ConvertUtil.getString(rule.get("rulecondtype"));
			String ismust=ConvertUtil.getString(rule.get("ismust"));	
			String subcampaignvalid=ConvertUtil.getString(rule.get("subcampaignvalid"));	
			int level=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("level")))?"0":ConvertUtil.getString(rule.get("level")));
			int checkFlag=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("checkFlag")))?"0":ConvertUtil.getString(rule.get("checkFlag")));
			String activitycode=ConvertUtil.getString(rule.get("activitycode"));
			Double planDiscountPrice=Double.parseDouble("".equals(ConvertUtil.getString(rule.get("planDiscountPrice")))?"0.00":ConvertUtil.getString(rule.get("planDiscountPrice")));
			Double actualDiscountPrice=Double.parseDouble("".equals(ConvertUtil.getString(rule.get("actualDiscountPrice")))?"0.00":ConvertUtil.getString(rule.get("actualDiscountPrice")));
			String activityType=ConvertUtil.getString(rule.get("activityType"));
			int ZGQFlag=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("ZGQFlag")))?"0":ConvertUtil.getString(rule.get("ZGQFlag")));
			int fullFlag=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("fullFlag")))?"0":ConvertUtil.getString(rule.get("fullFlag")));
			int couponFlag=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("couponFlag")))?"0":ConvertUtil.getString(rule.get("couponFlag")));
			double computePoint=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("computePoint")))?"0":ConvertUtil.getString(rule.get("computePoint")));
			int unitPoint=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("unitPoint")))?"0":ConvertUtil.getString(rule.get("unitPoint")));
			int maxPoint=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("maxPoint")))?"0":ConvertUtil.getString(rule.get("maxPoint")));
			SaleRuleResultEntity ruleResult=new SaleRuleResultEntity(flag, times, matchtimes, maincode, mainname, ismust, rulecondtype, 
				subcampaignvalid, level, checkFlag, activitycode, planDiscountPrice,actualDiscountPrice,activityType,0,0,0,"",0,ZGQFlag,computePoint,fullFlag,couponFlag,unitPoint,maxPoint);
			rule_list.add(ruleResult);
		}
		return rule_list;
	}

	@Override
	public List<Map<String,Object>> detail2List(ArrayList<SaleDetailEntity> detail_list) throws Exception {
		List<Map<String,Object>> detail_all=new ArrayList<Map<String,Object>>();
		for(SaleDetailEntity detail:detail_list){
			Map<String,Object> detail_map=new HashMap<String, Object>();
			detail_map.put("barcode", detail.getBarcode());
			detail_map.put("unitcode", detail.getUnitcode());
			detail_map.put("quantity", detail.getQuantity());
			detail_map.put("price", detail.getPrice());
			detail_map.put("ori_price", detail.getOri_price());
			detail_map.put("type", detail.getType());
			detail_map.put("maincode", detail.getMaincode());
			detail_map.put("mainname", detail.getMainname());
			detail_map.put("ItemTag", detail.getItemTag());
			detail_map.put("discount", detail.getDiscount());
			detail_map.put("productid", detail.getProductid());
			detail_map.put("proname", detail.getProname());
			detail_map.put("mainitem_tag", detail.getMainitem_tag());
			detail_map.put("activitycode", detail.getActivitycode());
			detail_map.put("new_flag", detail.getNew_flag());
			detail_all.add(detail_map);
		}
		
		return detail_all;
	}

	@Override
	public Map<String,Object> convert2Part(Map<String,Object> param) {
	
	Map<String,Object> result_map=new HashMap<String, Object>();
	List<Map<String,Object>> cart_list=new ArrayList<Map<String,Object>>();
	Map<String,Object> main_map=new HashMap<String, Object>();
	//主单信息
	String TN=ConvertUtil.getString(param.get("TN"));
	//交易日期
	String TD=ConvertUtil.getString(param.get("TD")).substring(2);
	String tradeDate=ConvertUtil.getString(param.get("TD"));
	//交易时间
	String TT=ConvertUtil.getString(param.get("TT")).replace(":", "");
	String tradeTime=ConvertUtil.getString(param.get("TT"));
	//品牌代号
	String BC=ConvertUtil.getString(param.get("BC"));
	//柜台代号
	String CC=ConvertUtil.getString(param.get("CC"));
	
	//会员卡号
	String MC=ConvertUtil.getString(param.get("MC"));
	if("VBC000000001".equals(MC)){
		MC="";
	}
	//会员姓名
	String MN=ConvertUtil.getString(param.get("MN"));
	//会员手机号
	String MP=ConvertUtil.getString(param.get("MP"));
	//会员等级
	String ML=ConvertUtil.getString(param.get("ML"));
	//会员BP号
	String bpCode=ConvertUtil.getString(param.get("BP"));
	main_map.put("TN", TN);
	main_map.put("TD", TD);
	main_map.put("TT", TT);
	main_map.put("tradeDate", tradeDate);
	main_map.put("tradeTime", tradeTime);
	main_map.put("BC", BC);
	main_map.put("brandCode", BC);
	main_map.put("CC", CC);
	main_map.put("MC", MC);
	main_map.put("MN", MN);
	main_map.put("MP", MP);
	main_map.put("ML", ML);
	main_map.put("BP", bpCode);
	double point=0;
	if(ML != null && !"".equals(ML)){
		ZSAL_MEMINFO[] arr=JahwaWebServiceProxy.getMemberList(main_map);
		if(arr != null && arr[0].getZCCUR_POINT() != null ){
			point=Double.parseDouble(arr[0].getZCCUR_POINT().toString());
		}
	}
	main_map.put("MPOINT", point);
	Map<String,Object> organizationID_map=binOLSSPRM74_Service.getOrganizationID(main_map);
	String organizationID=ConvertUtil.getString(organizationID_map.get("organizationID"));
	String organizationInfoID=ConvertUtil.getString(organizationID_map.get("organizationInfoID"));
	String brandInfoID=ConvertUtil.getString(organizationID_map.get("brandInfoID"));
	String departCode=ConvertUtil.getString(organizationID_map.get("departCode"));
	String orgCode=ConvertUtil.getString(organizationID_map.get("orgCode"));
	main_map.put("brandCode", BC);
	main_map.put("organizationID", organizationID);
	main_map.put("organizationInfoID", organizationInfoID);
	main_map.put("brandInfoId", brandInfoID);
	main_map.put("departCode", departCode);
	main_map.put("orgCode", orgCode);
	//新增逻辑：根据会员等级想对应的编码去会员等级表中查询对应的中文话术，没有则写入原始值
	Map<String,Object> memLevelName_map=binOLSSPRM74_Service.getMemberLevel(main_map);
	if(memLevelName_map == null){
		main_map.put("MLN", ML);
	}else{
		String levelName=ConvertUtil.getString(memLevelName_map.get("levelName"));
		main_map.put("MLN", levelName);
	}
	//购物车信息
	List<Map<String,Object>> SC=(List<Map<String,Object>>)param.get("SC");
	String systime=CherryUtil.getSysDateTime("yyyy-MM-dd");
	List<Map<String, Object>> cartMap_list=new ArrayList<Map<String,Object>>();
	if(SC != null){
		for(int i=1;i<=SC.size();i++){
			Map<String,Object> cart_map=new HashMap<String, Object>();
			Map<String,Object> sc_map=SC.get(i-1);
			sc_map.put("systime", systime);
			int quantity=Integer.parseInt(ConvertUtil.getString(sc_map.get("Q")));
			double salePrice=Double.parseDouble(ConvertUtil.getString(sc_map.get("P")));//传过来的实际销售价格
			Map<String,Object> productInfo=binOLSSPRM74_Service.getProductInfo(sc_map);
			if(productInfo == null){
				result_map.put("errCode", "Pro");
				return result_map;
			}
			String productId=ConvertUtil.getString(productInfo.get("productId"));
			String prtVendorId=ConvertUtil.getString(productInfo.get("prtVendorId"));
			String barCode=ConvertUtil.getString(productInfo.get("barCode"));
			String unitCode=ConvertUtil.getString(productInfo.get("unitCode"));
			String price=ConvertUtil.getString(productInfo.get("price"));//查询得到的原价
			String nameTotal=ConvertUtil.getString(productInfo.get("nameTotal"));
			String originalBrand=ConvertUtil.getString(productInfo.get("originalBrand"));
			cart_map.put("barCode", barCode);
			cart_map.put("quantity", quantity);
			cart_map.put("salePrice", salePrice);
			cart_map.put("productId", productId);
			cart_map.put("prtVendorId", prtVendorId);
			cart_map.put("unitCode", unitCode);
			cart_map.put("price", price);
			cart_map.put("nameTotal", nameTotal);
			cart_map.put("originalBrand", originalBrand);
			cart_list.add(cart_map);
		}
		//对购物车进行合并分组
		List<Map<String, Object>> order_list=ConvertUtil.listGroup(cart_list, "barCode", "order_list");
		for(Map<String,Object> order:order_list){
			List<Map<String,Object>> cartOrder_list=(List<Map<String,Object>>)order.get("order_list");
			Map<String,Object> cart0=cartOrder_list.get(0);
			Map<String,Object> order_map=new HashMap<String, Object>();
			order_map.putAll(cart0);
			order_map.put("quantity", cartOrder_list.size());
			cartMap_list.add(order_map);
		}
	}
	
	result_map.put("cartOrder_list", cartMap_list);
	result_map.put("cart_list", cart_list);
	result_map.put("main_map", main_map);
	return result_map;
	}

	@Override
	public void insertMain(Map<String, Object> main_map) {
		binOLSSPRM74_Service.insertMain(main_map);
	}

	@Override
	public void insertRule(List<Map<String, Object>> ruleAll_list, String TN) {
		//拆单操作
		List<Map<String, Object>> insertRule=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> rule:ruleAll_list){
			int quantity=Integer.parseInt("".equals(ConvertUtil.getString(rule.get("quantity")))?"1":ConvertUtil.getString(rule.get("quantity")));
			double price=Double.parseDouble("".equals(ConvertUtil.getString(rule.get("price")))?"0":ConvertUtil.getString(rule.get("price")));
			double salePrice=Double.parseDouble("".equals(ConvertUtil.getString(rule.get("salePrice")))?"0":ConvertUtil.getString(rule.get("salePrice")));
			for(int i=0;i<quantity;i++){
				rule.put("TN", TN);
				rule.put("quantity", 1);
				rule.put("price", price/quantity);
				rule.put("salePrice", salePrice/quantity);
				insertRule.add(rule);
			}
		}
		binOLSSPRM74_Service.insertRule(insertRule);
		
	}

	@Override
	public void insertCart(List<Map<String, Object>> cart_list, String TN) {
		//拆单操作
		List<Map<String, Object>> insertCart=new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> cart:cart_list){
			cart.put("TN", TN);
			String price=ConvertUtil.getString(cart.get("price")).equals("")?"0.00":ConvertUtil.getString(cart.get("price"));
			String salePrice=ConvertUtil.getString(cart.get("salePrice")).equals("")?"0.00":ConvertUtil.getString(cart.get("salePrice"));
			cart.put("price", price);
			cart.put("salePrice", salePrice);
			int quantity=Integer.parseInt(ConvertUtil.getString(cart.get("quantity")).equals("")?"1":ConvertUtil.getString(cart.get("quantity")));
			for(int i=0;i<quantity;i++){
				cart.put("quantity", 1);
				insertCart.add(cart);
			}
		}
		binOLSSPRM74_Service.insertCart(insertCart);
		
	}

	@Override
	public Map<String, Object> getDateSourceName(Map<String, Object> param_map) {
		return binOLSSPRM74_Service.getDateSourceName(param_map);
	}

	@Override
	public Map<String, Object> getOrganizationID(Map<String, Object> param_map) {
		return binOLSSPRM74_Service.getOrganizationID(param_map);
	}

	@Override
	public List<Map<String, Object>> convertProduct(
			ArrayList<SaleProductDetailEntity> saleproduct_out) {
		//转换List
		List<Map<String,Object>> convert_list=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> result_list=new ArrayList<Map<String,Object>>();
		if(saleproduct_out != null){
			Map<String,Object> param=new HashMap<String, Object>();
			String systime=CherryUtil.getSysDateTime("yyyy-MM-dd");
			for(SaleProductDetailEntity detail:saleproduct_out){
				param.put("B", detail.getUnicode());
				param.put("systime", systime);
				//查询出产品原价
				Map<String,Object> salePrice_map=binOLSSPRM74_Service.getProductInfo(param);
				Double price=Double.parseDouble("".equals(ConvertUtil.getString(salePrice_map.get("price")))?"0.00":ConvertUtil.getString(salePrice_map.get("price")));
				//结果list中没有数据的情况直接写入
				Map<String,Object> product_detail=new HashMap<String, Object>();
				product_detail.put("maincode", detail.getMaincode());
				product_detail.put("barcode", detail.getBarcode());
				product_detail.put("unicode", detail.getUnicode());
				product_detail.put("proname", detail.getProname());
				product_detail.put("quantity", detail.getQuantity());
				product_detail.put("price", detail.getPrice()*detail.getQuantity());
				product_detail.put("salePrice", price*detail.getQuantity());
				convert_list.add(product_detail);
			}
			List<Map<String,Object>> group_list=ConvertUtil.listGroup(convert_list, "maincode", "product_list");
			for(Map<String,Object> group:group_list){
				List<Map<String,Object>> product_list=(List<Map<String,Object>>)group.get("product_list");
				String maincode=ConvertUtil.getString(product_list.get(0).get("maincode"));
				Map<String,Object> result_map=new HashMap<String, Object>();
				result_map.put("maincode", maincode);
				result_map.put("product_list", product_list);
				result_list.add(result_map);
			}
		}
		return result_list;
	}

	@Override
	public void checkMain(Map<String, Object> param) {
		//删除主表数据
		int delcnt = binOLSSPRM74_Service.delmain_all(param);
		logger.info("从BIN_GetIntelligentResultMain表删除数据条数："+delcnt);
		//获取Cart单据对应的明细
		List<Map<String,Object>> cart_list=binOLSSPRM74_Service.getCartDetailByTradeNo(param);
		//获取Rule单据对应的明细
		List<Map<String,Object>> rule_list=binOLSSPRM74_Service.getRuleDetailByTradeNo(param);
		//获取Coupon单据对于的明细
		List<Map<String,Object>> coupon_list=binOLSSPRM74_Service.getCouponDetailByTradeNo(param);
		if(cart_list != null && cart_list.size() > 0){
			binOLSSPRM74_Service.deleteCartDetail(cart_list);
			logger.info("从BIN_IntelligentResultShoppingCart表删除数据条数："+cart_list.size());
		}

		if(rule_list != null && rule_list.size() > 0){
			binOLSSPRM74_Service.deleteRuleDetail(rule_list);
			logger.info("从BIN_IntelligentResultRule表删除数据条数："+rule_list.size());
		}

		if(coupon_list != null && coupon_list.size() > 0){
			binOLSSPRM74_Service.deleteCouponDetail(coupon_list);
			logger.info("从BIN_IntelligentResultCoupon表删除数据条数："+coupon_list.size());
		}
	}

	
	@Override
	public void insertCoupon(List<Map<String, Object>> coupon_list, String TN) {
		for(Map<String,Object> rule:coupon_list){
			rule.put("TN", TN);
		}
		binOLSSPRM74_Service.insertCoupon(coupon_list);
	}

	@Override
	public List<Map<String, Object>> convert2Coupon(
			List<Map<String, Object>> coupon_list,
			List<Map<String, Object>> couponAll_list,List<Map<String,Object>> couponCheck_list) {
		if(couponAll_list != null && couponAll_list.size() == 0){
			for(Map<String,Object> coupon:coupon_list){
				coupon.put("checkFlag", 1);
				couponAll_list.add(coupon);
			}
			return couponAll_list;
		}
		for(Map<String,Object> coupon_map:coupon_list){
			String couponCode=ConvertUtil.getString(coupon_map.get("couponCode"));
			for(int i=0;i<couponAll_list.size();i++){
				String $couponCode=ConvertUtil.getString(couponAll_list.get(i).get("couponCode"));
				if(couponCode.equals($couponCode)){
					couponAll_list.set(i,coupon_map);
					break;
				}else if(i == couponAll_list.size()-1){
					couponAll_list.add(coupon_map);
				}
			}
		}
		
		for(Map<String,Object> check_map:couponCheck_list){
			String couponCode=ConvertUtil.getString(check_map.get("couponCode"));
			for(Map<String,Object> all_map:couponAll_list){
				String $couponCode=ConvertUtil.getString(all_map.get("couponCode"));
				if(couponCode.equals($couponCode)){
					all_map.put("checkFlag", 1);
				}
			}
		}
		return couponAll_list;
	}

	@Override
	public List<Map<String, Object>> convert2AllActivity(
			Map<String, Object> param) {
		return binOLSSPRM74_Service.getAllActivity(param);
	}
	@Override
	public List<Map<String,Object>> detail2ComputedList(ArrayList<SaleDetailEntity> detail_list) throws Exception {
		List<Map<String,Object>> detail_all=new ArrayList<Map<String,Object>>();
		for(SaleDetailEntity detail:detail_list){
			//去除TZZK明细行
			String barcode=detail.getBarcode();
			if(barcode.contains("TZZK") || barcode.contains("DH")){
				continue;
			}
			Map<String,Object> detail_map=new HashMap<String, Object>();
			detail_map.put("barCode", detail.getBarcode());
			detail_map.put("unitCode", detail.getUnitcode());
			detail_map.put("quantity", detail.getQuantity());
			detail_map.put("salePrice", detail.getPrice());
			detail_map.put("price", detail.getOri_price());
			detail_map.put("type", detail.getType());
			detail_map.put("maincode", detail.getMaincode());
			detail_map.put("mainname", detail.getMainname());
			detail_map.put("ItemTag", detail.getItemTag());
			detail_map.put("discount", detail.getDiscount());
			detail_map.put("productId", detail.getProductid());
			detail_map.put("nameTotal", detail.getProname());
			detail_map.put("mainitem_tag", detail.getMainitem_tag());
			detail_map.put("new_flag", detail.getNew_flag());
			detail_all.add(detail_map);
		}
		
		return detail_all;
	}

	@Override
	public List<Map<String,Object>> detail2RuleList(ArrayList<SaleDetailEntity> detail_list,ArrayList<SaleRuleResultEntity> result_list) throws Exception {
		List<Map<String,Object>> detail_all=new ArrayList<Map<String,Object>>();
		for(SaleDetailEntity detail:detail_list){
			//去除TZZK明细行
			String barcode=detail.getBarcode();
			if(barcode.contains("TZZK") || barcode.contains("DH")){
				Map<String,Object> detail_map=new HashMap<String, Object>();
				detail_map.put("barCode", detail.getBarcode());
				detail_map.put("unitCode", detail.getUnitcode());
				detail_map.put("quantity", detail.getQuantity());
				detail_map.put("salePrice", detail.getPrice());
				detail_map.put("price", "0");
				detail_map.put("type", detail.getType());
				detail_map.put("maincode", detail.getMaincode());
				detail_map.put("activitycode", detail.getActivitycode());
				detail_map.put("mainname", detail.getMainname());
				detail_map.put("ItemTag", detail.getItemTag());
				detail_map.put("discount", detail.getDiscount());
				detail_map.put("productId", detail.getProductid());
				detail_map.put("nameTotal", detail.getProname());
				detail_map.put("mainitem_tag", detail.getMainitem_tag());
				detail_map.put("new_flag", detail.getNew_flag());
				//通过Maincode匹配活动类型写入数据中
				String ruleType = null;
				for(SaleRuleResultEntity result:result_list){
					if(result.getMaincode().equals(detail.getMaincode())){
						ruleType=result.getActivityType();
					}
				}
				detail_map.put("ruleType", ruleType);
				detail_all.add(detail_map);
			}
		}
		
		return detail_all;
	}
	
	@Override
	public List<Map<String, Object>> collect2pro(
			List<Map<String, Object>> cart_list) {
		List<Map<String,Object>> cart2list=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> cart:cart_list){
			Map<String,Object> result=binOLSSPRM74_Service.collect2pro(cart);
			cart2list.add(result);
		}
		return cart2list;
	}

	@Override
	public Map<String, Object> getMainByTradeNo(Map<String, Object> param) {
		Map<String,Object> main_map=binOLSSPRM74_Service.getMainByTradeNo(param);
		String tradeDate=ConvertUtil.getString(main_map.get("TD"));
		String tradeTime=ConvertUtil.getString(main_map.get("TT"));
		main_map.put("tradeDate", tradeDate);
		main_map.put("tradeTime", tradeTime);
		return main_map;
	}

	@Override
	public List<Map<String, Object>> getShoppingCartByTradeNo(
			Map<String, Object> param) {
		
		//合并操作废弃
//		List<Map<String, Object>> result_list=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> cart_list=binOLSSPRM74_Service.getShoppingCartByTradeNo(param);
//		List<Map<String, Object>> order_list=ConvertUtil.listGroup(cart_list, "unitcode", "order_list");
//		for(Map<String,Object> order:order_list){
//			List<Map<String,Object>> cartOrder_list=(List<Map<String,Object>>)order.get("order_list");
//			Map<String,Object> cart0=cartOrder_list.get(0);
//			Map<String,Object> order_map=new HashMap<String, Object>();
//			order_map.putAll(cart0);
//			order_map.put("quantity", cartOrder_list.size());
//			result_list.add(order_map);
//		}
		return cart_list;
				
	}

	@Override
	public List<Map<String, Object>> getRuleByTradeNo(Map<String, Object> param) {
		return binOLSSPRM74_Service.getRuleByTradeNo(param);
	}
	
	@Override
	public List<Map<String, Object>> getCouponByTradeNo(
			Map<String, Object> param) {
		return binOLSSPRM74_Service.getCouponByTradeNo(param);
	}

	@Override
	public List<Map<String, Object>> groupSendRule(
			List<Map<String, Object>> rule_list) {
		List<Map<String, Object>> result_list=new ArrayList<Map<String,Object>>();
		if(null != rule_list && rule_list.size()>0){
			List<Map<String, Object>> order_list=ConvertUtil.listGroup(rule_list, "groupId", "order_list");
			for(Map<String,Object> order:order_list){
				List<Map<String, Object>> order_rule_list=(List<Map<String, Object>>)order.get("order_list");
				if(order_rule_list.size() >1){
					for(Map<String,Object> rule:order_rule_list){
						rule.put("groupFlag", 1);
						result_list.add(rule);
					}
				}else{
					for(Map<String,Object> rule:order_rule_list){
						rule.put("groupFlag", 0);
						result_list.add(rule);
					}
				}
			}
		}
		return result_list;
	}
	
	public List<Map<String, Object>> couponProOrder(
			List<Map<String, Object>> ProductList) {
		if(ProductList == null){
			return null;
		}
		List<Map<String,Object>> ProductOrderList=ConvertUtil.listGroup(ProductList, "couponCode", "pro");
		List<Map<String,Object>> result_list=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> pro:ProductOrderList){
			List<Map<String,Object>> pro_list=(List<Map<String,Object>>)pro.get("pro");
			Map<String,Object> pro_map=pro_list.get(0);
			String couponCode=ConvertUtil.getString(pro_map.get("couponCode"));
			Map<String,Object> result_map=new HashMap<String, Object>();
			result_map.put("couponCode", couponCode);
			result_map.put("pro_list", pro_list);
			result_list.add(result_map);
		}
		return result_list;
	}

	@Override
	public List<Map<String, Object>> checkRule(
			List<Map<String, Object>> rule_list) {
		List<Map<String,Object>> result_list=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> rule:rule_list){
			String checkFlag=ConvertUtil.getString(rule.get("checkFlag"));
			String ZGQFlag=ConvertUtil.getString(rule.get("ZGQFlag"));
			if("1".equals(checkFlag) || "1".equals(ZGQFlag)){
				result_list.add(rule);
			}
		}
		return result_list;
	}

	@Override
	public void updateProCoupon(Map<String, Object> param) {
		binOLSSPRM74_Service.updateProCoupon(param);
	}

	@Override
	public int getNoMemberCouponCount() {
		return binOLSSPRM74_Service.getNoMemberCouponCount();
	}
	
	/**
	 * 合并完整的促销活动 1+（2&3）
	 * @param rule_list 1、计算完毕的规则（N类型）和产品有交互的活动
	 * @param promotionRule_list 2、促销引擎计算完毕的规则（所有P类型）
	 * @param pointRule_list 3、计算完毕的P类型积分兑换活动(和2数据进行合并用)
	 * @return
	 */
	@Override
	public List<Map<String, Object>> convertRuleList(
			List<Map<String, Object>> rule_list,
			List<Map<String, Object>> promotionRule_list,
			List<Map<String, Object>> pointRule_list) {
		//对所有P类型的数据的扣减价格进行价格乘以数量的操作，与之后N类型活动的拆单逻辑吻合
		if(null != promotionRule_list){
			for(Map<String,Object> promotion_info:promotionRule_list){
				double salePrice=Double.parseDouble(ConvertUtil.getString(promotion_info.get("salePrice")));
				double price=Double.parseDouble(ConvertUtil.getString(promotion_info.get("price")));
				int quantity=Integer.parseInt(ConvertUtil.getString(promotion_info.get("quantity")));
				promotion_info.put("salePrice",salePrice*quantity);
				promotion_info.put("price",price*quantity);
			}
		}
		
		//替换3中的数据入2中
		if(null != pointRule_list){
			for(Map<String,Object> pointRule :pointRule_list){
				String point_maincode=ConvertUtil.getString(pointRule.get("maincode"));
				if(null != promotionRule_list){
					for(int i=0;i<promotionRule_list.size();i++){
						String promotionRule_maincode=ConvertUtil.getString(promotionRule_list.get(i).get("maincode"));
						if(point_maincode.equals(promotionRule_maincode)){
							promotionRule_list.set(i, pointRule);
						}
					}
				}
			}
		}
		
		//合并1与上面的结果
		if(null==promotionRule_list){
			promotionRule_list = new ArrayList<Map<String, Object>>();
		}
		promotionRule_list.addAll(rule_list);
		//对没有积分的数据进行补0操作
		for(Map<String,Object> promotion_rule:promotionRule_list){
			String computePoint=ConvertUtil.getString(promotion_rule.get("computePoint"));
			if("".equals(computePoint)){
				promotion_rule.put("computePoint", "0.0");
			}
		}
		
		
		return promotionRule_list;
	}

	@Override
	public  int tran_collect(BINOLSSPRM74_Form form) throws Exception {
		//主单信息
		Map<String, Object> main_map = ConvertUtil.json2Map(form.getMain_json());
//		if(1 == ConvertUtil.getInt(form.getCloseFlag())){
//			//取消按钮触发，删除该单据号所有的数据
//			this.checkMain(main_map);
//			logger.info("取消按钮触发，删除该单据号所有的数据成功");
//			ConvertUtil.setResponseByAjax(response, "0");
//			return;
//		}
		//主单信息非空校验
		if(null==main_map ){
			logger.error("销售单据录入过程中发生错误，主单信息转换失败：" + form.getMain_json());
			return 1;
		}
		this.checkMain(main_map);

		//购物车信息
		List<Map<String, Object>> cart_list = ConvertUtil.json2List(form.getShoppingcart_json());
		if(null==cart_list || cart_list.size()==0){
			logger.error("销售单据录入过程中发生错误，购物车列表转换失败：" + form.getShoppingcart_json());
			return 1;
		}
		String TN = ConvertUtil.getString(main_map.get("TN"));
		//如果主单中已经存在有其单据号的情况，先做物理删除再进行插入
		//1、计算完毕的规则（N类型）
		List<Map<String, Object>> rule_list = ConvertUtil.json2List(form.getRule_json());
		//2、计算完毕的规则（P类型）
		List<Map<String, Object>> promotionRule_list = ConvertUtil.json2List(form.getPromotionRule_json());
		//3、计算完毕的P类型积分兑换活动(和2数据进行合并用)
		List<Map<String, Object>> pointRule_list = ConvertUtil.json2List(form.getPointRule_json());

		//合并完整的促销活动 1+（2&3）
		List<Map<String, Object>> result_rule = this.convertRuleList(rule_list, promotionRule_list, pointRule_list);

		//计算完毕的优惠券信息
		List<Map<String, Object>> coupon_list = ConvertUtil.json2List(form.getCoupon_json());
		if (main_map != null && cart_list != null && cart_list.size() > 0) {
			//获得累计优惠金额
			double discountTotal = 0;
			if (form.getDiscountTotal() == null || "".equals(form.getDiscountTotal())) {
				discountTotal = 0;
			} else {
				discountTotal = Double.parseDouble(form.getDiscountTotal());
			}
			main_map.put("discountAmount", discountTotal);
			//实际销售价
			double actualTotal = 0;
			if (form.getActualTotal() == null || "".equals(form.getActualTotal())) {
				actualTotal = 0;
			} else {
				actualTotal = Double.parseDouble(form.getActualTotal());
			}
			//折前金额
			double receivableTotal = 0;
			if (form.getReceivableTotal() == null || "".equals(form.getReceivableTotal())) {
				receivableTotal = 0;
			} else {
				receivableTotal = Double.parseDouble(form.getReceivableTotal());
			}
			//计算完毕的积分
			double computedPoint = 0;
			for (Map<String, Object> rule : result_rule) {
				double rulepoint = Double.parseDouble("".equals(ConvertUtil.getString(rule.get("computePoint"))) ? "0" : ConvertUtil.getString(rule.get("computePoint")));
				computedPoint += rulepoint;
			}
			int totalQuantity = 0;
			for (Map<String, Object> cart : cart_list) {
				int quantity = Integer.parseInt("".equals(ConvertUtil.getString(cart.get("quantity"))) ? "0" : ConvertUtil.getString(cart.get("quantity")));
				totalQuantity += quantity;
			}
			main_map.put("originalAmount", receivableTotal);//折前金额
			main_map.put("totalQuantity", totalQuantity);//商品总数量
			main_map.put("totalAmount", actualTotal);//应收总金额
			main_map.put("computedPoint", computedPoint);//已经使用的积分
			//如果memberLevel为空的话，不写入memberCode
			if ("".equals(ConvertUtil.getString(main_map.get("ML")))) {
				main_map.put("MC", "");
			}
			//在写入主表数据之前先调用发券查询接口判断此单是否可以发券
			//转换购物车添加产商ID字段
			List<Map<String, Object>> cartConvert = this.collect2pro(cart_list);
			main_map.put("TT", main_map.get("tradeTime"));
			main_map.put("brandInfoID", main_map.get("brandInfoId"));
			main_map.put("TotalAmount", main_map.get("totalAmount"));
			if (form.getMemberPhone() != null ) {
				String memberPhone = form.getMemberPhone().trim();
				main_map.put("MP", memberPhone);
			}
			Map<String, Object> coupon_input = new HashMap<String, Object>();
			coupon_input.put("Main_map", main_map);
			coupon_input.put("cart_map", cartConvert);
			coupon_input.put("completedRule", result_rule);
			coupon_input.put("completedCoupon", coupon_list);
			List<Map<String, Object>> couponResult = coupon_IF.getCouponRuleList(coupon_input);
			if (couponResult != null && couponResult.size() > 0) {
				main_map.put("SendFlag", 1);
			} else {
				main_map.put("SendFlag", 0);
			}
			//打印智能促销页面写入的主单数据
			main_map.put("createPGM", "BINOLSSPRM74_1");
			logger.info("打印智能促销页面写入的主单数据："+ main_map);
			this.insertMain(main_map);

			if (coupon_list != null && coupon_list.size()>0) {
				this.insertCoupon(coupon_list, TN);
			}
			if (result_rule != null && result_rule.size()>0) {
				this.insertRule(result_rule, TN);
			}
			if (cart_list != null && cart_list.size()>0) {
				this.insertCart(cart_list, TN);
			} else {
				logger.info("智能促销录入数据购物车信息为空");
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void tran_collectPro(BINOLSSPRM74_Form form) throws Exception {
//主单信息
		Map<String, Object> main_map = ConvertUtil.json2Map(form.getMain_json());
		String TN = ConvertUtil.getString(main_map.get("TN"));
//			if(1 == ConvertUtil.getInt(form.getCloseFlag())){
//				//取消按钮触发，删除该单据号所有的数据
//				binOLSSPRM74_IF.checkMain(main_map);
//				logger.info("取消按钮触发，删除该单据号所有的数据成功");
//				ConvertUtil.setResponseByAjax(response, "0");
//				return;
//			}
		logger.info("代物券数据录入开始");
		//如果主单中已经存在有其单据号的情况，先做物理删除再进行插入
		this.checkMain(main_map);
		//计算完毕的优惠券信息
		List<Map<String, Object>> coupon_list = ConvertUtil.json2List(form.getCoupon_json());
		List<Map<String, Object>> cart_list = ConvertUtil.json2List(form.getShoppingcart_json());
		double amount = 0;
		int totalQuantity = 0;
		if (cart_list != null) {
			for (Map<String, Object> coupon : cart_list) {
				double price = Double.parseDouble(ConvertUtil.getString(coupon.get("price")));
				int quantity = Integer.parseInt(ConvertUtil.getString(coupon.get("quantity")));
				totalQuantity += quantity;
				amount += price * quantity;
			}
		}
		if (main_map != null) {
			main_map.put("MP", form.getMemberPhone());
			main_map.put("originalAmount", amount);//折前金额
			main_map.put("totalQuantity", totalQuantity);//商品总数量
			main_map.put("totalAmount", 0);//应收总金额
			main_map.put("discountAmount", -amount);//总优惠金额
			//代物券暂时没有与积分挂钩默认写入0
			main_map.put("computedPoint", 0);
			//如果memberLevel为空的话，不写入memberCode
			if ("".equals(ConvertUtil.getString(main_map.get("ML")))) {
				main_map.put("MC", "");
			}
			//打印代物券写表的主单数据
			logger.error("打印代物券写表的主单数据：", main_map);
			main_map.put("createPGM", "BINOLSSPRM74_2");
			this.insertMain(main_map);

			if (cart_list != null) {
				this.insertCart(cart_list, TN);
			}
			List<Map<String, Object>> order_list = new ArrayList<Map<String, Object>>();
			if (coupon_list != null) {
				this.insertCoupon(coupon_list, TN);
				//写入规则表之前进行分组
				List<Map<String, Object>> group_list = ConvertUtil.listGroup(coupon_list, "maincode", "rule_list");
				for (Map<String, Object> group : group_list) {
					List<Map<String, Object>> product_list = (List<Map<String, Object>>) group.get("rule_list");
					Map<String, Object> result_map = new HashMap<String, Object>();
					result_map.putAll(product_list.get(0));
					result_map.put("Quantity", product_list.size());
					result_map.put("mainCode", result_map.get("Maincode"));
					order_list.add(result_map);
				}
				this.insertRule(order_list, TN);
			}
			String MemberCode = ConvertUtil.getString(main_map.get("MC"));
			String MemberPhone = ConvertUtil.getString(main_map.get("MP"));
			String bpCode = ConvertUtil.getString(main_map.get("BP"));
			//代物券核券操作
			for (Map<String, Object> coupon : coupon_list) {
				coupon.put("TradeNoIF", TN);
				coupon.put("MemberCode", MemberCode);
				coupon.put("MemberPhone", MemberPhone);
				coupon.put("bpCode", bpCode);
				this.updateProCoupon(coupon);
			}
		}
	}
}
