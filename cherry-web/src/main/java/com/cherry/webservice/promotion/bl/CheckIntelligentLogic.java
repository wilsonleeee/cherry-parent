package com.cherry.webservice.promotion.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM44_BL;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.interfaces.BINOLSSPRM74_IF;
import com.cherry.ss.prm.interfaces.Coupon_IF;
import com.cherry.webservice.promotion.interfaces.CheckIntelligent_IF;
import com.cherry.wp.common.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预判智能促销（家化专用） 接口BL
 * 
 * @author yangcheng
 * @version 2016-04-08 1.0.0
 */
public class CheckIntelligentLogic implements CheckIntelligent_IF {
	/** 打印日志 */
	private Logger logger = LoggerFactory.getLogger(CheckIntelligentLogic.class);

	@Resource
	private BINOLCM44_BL binOLCM44_BL;

	@Resource(name = "binOLSSPRM74_BL")
	private BINOLSSPRM74_IF binOLSSPRM74_IF;
	
	@Resource
    private Coupon_IF coupon_IF;

	public Map<String, Object> tran_CheckIntelligentJH(Map<String, Object> map) throws Exception {
		logger.info("智能促销预判接口开始：",map);
		// 返回值的map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//得到品牌号
		String brandCode = (String) map.get("BrandCode");
		//得到传递过来的参数
		String TN = (String) map.get("TN");
		String TD = (String) map.get("TD");
		String TT = (String) map.get("TT");
		String CC = (String) map.get("CC");
		//因为传递过来的是集合封装，所以用集合接受
		List<Map<String, Object>> SC = (List<Map<String, Object>>) map.get("SC");
		// 必填字段验证
		//判断单据号 是否为空
		if (TN == null || "".equals(TN)) {
			returnMap.put("ERRORCODE", "EIP02001");
			returnMap.put("ERRORMSG", "TN 参数缺失或格式错误");
			return returnMap;
		}
		//判断 交易日期 是否为空
		if (TD == null || "".equals(TD)) {
			returnMap.put("ERRORCODE", "EIP02002");
			returnMap.put("ERRORMSG", "TD 参数缺失或格式错误");
			return returnMap;
		}
		//判断交易时间 是否为空
		if (TT == null || "".equals(TT)) {
			returnMap.put("ERRORCODE", "EIP02003");
			returnMap.put("ERRORMSG", "TT 参数缺失或格式错误");
			return returnMap;
		}
		//判断柜台代号 是否为空
		if (CC == null || "".equals(CC)) {
			returnMap.put("ERRORCODE", "EIP02004");
			returnMap.put("ERRORMSG", "CC 参数缺失或格式错误");
			return returnMap;
		}
		//判断 购物车信息 是否为空
		if (SC.size() <= 0) {
			returnMap.put("ERRORCODE", "EIP02005");
			returnMap.put("ERRORMSG", "SC 参数缺失或格式错误 ");
			return returnMap;
		} else {
			//遍历得到 购物车信息 里面的产品编码， 数量 ， 销售价 ，并逐个验证 
			for (int i = 1; i <= SC.size(); i++) {
				Map<String, Object> sc_map = SC.get(i - 1);
				String barcode = (String) sc_map.get("B");
				//因为由String转换成int和double不能强制转化，所以应该特殊处理
				//如果用这种就会报错    int quantity=(Integer)sc_map.get("Q");
				int quantity=Integer.parseInt("".equals(ConvertUtil.getString(sc_map.get("Q")))?"0":ConvertUtil.getString(sc_map.get("Q")));
				double price = Double.parseDouble("".equals(ConvertUtil.getString(sc_map.get("P")))?"0.00":ConvertUtil.getString(sc_map.get("P"))) ;
				//判断产品编码是否为空
				if (barcode == null || "".equals(barcode)) {
					returnMap.put("ERRORCODE", "EIP02006");
					returnMap.put("ERRORMSG", "B 参数缺失或格式错误 ");
					return returnMap;
				}
				//判断数量是否为空
				if (quantity==0) {
					returnMap.put("ERRORCODE", "EIP02007");
					returnMap.put("ERRORMSG", "Q 参数缺失或格式错误 ");
					return returnMap;
				}
				//判断销售价是否为空
				if (price == 0.00) {
					returnMap.put("ERRORCODE", "EIP02008");
					returnMap.put("ERRORMSG", "P 参数缺失或格式错误 ");
					return returnMap;
				}
			}
		}
		//将主单信息 和原始购物车信息封装在不同的集合里面
		Map<String, Object> convert_map = binOLSSPRM74_IF.convert2Part(map);
		//取得主单的信息
		Map<String, Object> main_map = (Map<String, Object>) convert_map.get("main_map");
		main_map.put("BC", brandCode);
		//取得购物车信息
		List<Map<String, Object>> cart_list = (List<Map<String, Object>>) convert_map.get("cart_list");
		//新增写表操作
		//如果主单中已经存在有其单据号的情况，先做物理删除再进行插入
		binOLSSPRM74_IF.checkMain(main_map);
		if(main_map != null && cart_list !=null){
			//计算完毕的积分
			double salePrice=0;
			for(Map<String,Object> cart:cart_list){
				double salePriceEach=Double.parseDouble(ConvertUtil.getString(cart.get("salePrice")));
				salePrice += salePriceEach;
			}
			int totalQuantity=0;
			for(Map<String,Object> cart:cart_list){
				int quantity=Integer.parseInt("".equals(ConvertUtil.getString(cart.get("quantity")))?"0":ConvertUtil.getString(cart.get("quantity")));
				totalQuantity += quantity;
			}
			
			main_map.put("originalAmount", salePrice);//折前金额
			main_map.put("totalQuantity", totalQuantity);//商品总数量
			main_map.put("TotalAmount", salePrice);//应收总金额
			main_map.put("computedPoint", "0");//已经使用的积分
			main_map.put("discountAmount", "0");
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
            List<Map<String, Object>> couponResult=coupon_IF.getCouponRuleList(coupon_input);
            if(couponResult != null && couponResult.size() > 0){
            	main_map.put("SendFlag", 1);
            }else{
            	main_map.put("SendFlag", 0);
            }
			main_map.put("createPGM","CheckIntelligentLogic");
			binOLSSPRM74_IF.insertMain(main_map);
			binOLSSPRM74_IF.insertCart(cart_list, TN);
		}
	
		//将处理过的传参进行封装
		Map<String, Object> input_map = binOLSSPRM74_IF.convert2Entity(main_map, cart_list,null);
		ArrayList<SaleMainEntity> salemain_input = (ArrayList<SaleMainEntity>) input_map.get("main_input");
		ArrayList<SaleDetailEntity> saledetail_input = (ArrayList<SaleDetailEntity>) input_map.get("detail_input");
		//new一个实体类
		ArrayList<SaleRuleResultEntity> saleresult_out = new ArrayList<SaleRuleResultEntity>();
		ArrayList<SaleProductDetailEntity> saleproduct_out=new ArrayList<SaleProductDetailEntity>();
		ArrayList<SaleActivityDetailEntity> saleactivity_out=new ArrayList<SaleActivityDetailEntity>();
		try {
			//获取组织id
			Map<String,Object> organizationID_map=binOLSSPRM74_IF.getOrganizationID(main_map);
			String organizationID=ConvertUtil.getString(organizationID_map.get("organizationID"));
			//执行智能促销查询方法
			int result = binOLCM44_BL.cloud_MatchRule_JIAHUA(brandCode,organizationID, salemain_input, saledetail_input,saleactivity_out,saleresult_out,saleproduct_out);
			logger.info(TN+ "调用cloud_MatchRule_JIAHUA  结束，result="+result);
			Map<String, Object> tmp = new HashMap<String, Object>();
			if (saleresult_out.size() <= 0) {
//				//调用是否可发券接口
//				Map<String,Object> interface_input=new HashMap<String, Object>();
//				interface_input.put("Main_map", main_map);
//				interface_input.put("cart_map", cart_list);
//				//此处调用接口(会员下所有的券信息)
//				List<Map<String, Object>> coupon_list=coupon_IF.getCouponList(interface_input);
//				//无门槛券所有的信息
//				int noMemberCouponCount=binOLSSPRM74_IF.getNoMemberCouponCount();
//				if((coupon_list != null && coupon_list.size() > 0) || noMemberCouponCount>0){
					tmp.put("HasPromotion", "2");
//				}else{
//					tmp.put("HasPromotion", "0");
//				}
			} else {
				tmp.put("HasPromotion", "1");
			}
			returnMap.put("ResultContent", tmp);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			returnMap.put("ERRORCODE", "WSE9999");
			returnMap.put("ERRORMSG", "处理过程中发生未知异常");
		}
		logger.info("智能促销预判接口返回",returnMap);
		return returnMap;
	}
}
