package com.cherry.wp.sal.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.wp.common.entity.SaleMainEntity;
import com.cherry.wp.common.entity.SaleProductDetailEntity;
import com.cherry.wp.common.entity.SaleRuleResultEntity;
import com.cherry.wp.common.entity.SaleDetailEntity;
import com.cherry.wp.sal.form.BINOLWPSAL08_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL08_IF;
import com.cherry.wp.sal.service.BINOLWPSAL08_Service;

public class BINOLWPSAL08_BL extends BaseAction implements BINOLWPSAL08_IF{
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM18_BL binOLCM18_BL;
	//在此用于取得库存时用到
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	//在此用于取得库存时用到
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_BL binOLCM20_BL;
	@Resource(name="binOLWPSAL08_Service")
	private BINOLWPSAL08_Service binOLWPSAL08_Service;
	
	// 下面这个方法中的坑爹代码是由于现在的活动设计不合理导致的，现在在促销活动和会员活动中都可以设置会员和非会员活动并且没有明确的标识进行区分，导致需要做重复去除操作，并且需要根据条件分别进行不同的查询
	@Override
	public List<Map<String, Object>> getPromotionList(Map<String, Object> map)
			throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String counterCode = ConvertUtil.getString(map.get("counterCode"));
		String customerType = ConvertUtil.getString(map.get("customerType"));
		String changablePoint = ConvertUtil.getString(map.get("changablePoint"));
		String memberInfoId = ConvertUtil.getString(map.get("memberInfoId"));
		String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
		// 发卡柜台
		String counterCodeBelong = ConvertUtil.getString(map.get("counterCodeBelong"));
		// 首次购买柜台
		String firstSaleCounter = ConvertUtil.getString(map.get("firstSaleCounter"));
		// 活动预约柜台
		List<Map<String, Object>> orderCounterCode = new ArrayList<Map<String,Object>>();
		if(null!=map.get("orderCounterCode") && !"".equals(map.get("orderCounterCode"))){
			orderCounterCode = ConvertUtil.json2List(map.get("orderCounterCode").toString());
		}
		String nowTime = binOLWPSAL08_Service.getSYSDate();
		String businessDate = binOLWPSAL08_Service.getBussinessDate(map);
		double usablePoint = 0.00;
		
		String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
		if(!"2".equals(saleDateType)){
			businessDate = DateUtil.coverTime2YMD(nowTime, "yyyy-MM-dd");
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoId", organizationInfoId);
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put("counterCode", counterCode);
		paramMap.put("nowTime", nowTime);
		paramMap.put("businessDate", businessDate);
		paramMap.put("memberInfoId", memberInfoId);
		paramMap.put("mobilePhone", mobilePhone);
		// 处理可兑换积分
		if(null != changablePoint && !"".equals(changablePoint) && !"null".equals(changablePoint)){
			if(CherryUtil.string2double(changablePoint) > 0){
				usablePoint = CherryUtil.string2double(changablePoint);
				paramMap.put("changablePoint", changablePoint);
			}else{
				usablePoint = 0.00;
				paramMap.put("changablePoint", "0");
			}
		}else{
			usablePoint = 0.00;
			paramMap.put("changablePoint", "0");
		}
		
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		if("MB".equals(customerType)){
			// 增加会员活动条件
			paramMap.put("memberActivity", "Y");
			// 定义需要去除的活动列表
			List<Map<String, Object>> removeList = new ArrayList<Map<String, Object>>();
			// 获取需要Coupon号的会员活动
			List<Map<String, Object>> couponOrderList = binOLWPSAL08_Service.getCouponOrderList(paramMap);
			if(null != couponOrderList && !couponOrderList.isEmpty()){
				List<Map<String, Object>> couponOrderRemoveList = new ArrayList<Map<String,Object>>();
				for(Map<String, Object> m : couponOrderList){
					// 领用柜台类型:1:任意柜台,2:发卡柜台,3:预约柜台,4:首次购买柜台
					String gotCounter = ConvertUtil.getString(m.get("gotCounter"));
					if(!gotCounter.equals("1") && null!=gotCounter && !gotCounter.equals("")){
						if(gotCounter.equals("2")){							// 发卡柜台
							if(!counterCode.equalsIgnoreCase(counterCodeBelong)){
								couponOrderRemoveList.add(m);
							}
						}else if(gotCounter.equals("3")){					// 活动预约柜台
							if(orderCounterCode.size()>0){
								for(Map<String, Object> om : orderCounterCode){
									if(!counterCode.equalsIgnoreCase(om.toString())){
										couponOrderRemoveList.add(m);
									}
								}
							}
						}else if(gotCounter.equals("4")){					// 首次购买柜台
							if(!counterCode.equalsIgnoreCase(firstSaleCounter)){
								couponOrderRemoveList.add(m);
							}
						}
					}
				}
				if(null!=couponOrderRemoveList && !couponOrderRemoveList.isEmpty()){
					couponOrderList.removeAll(couponOrderRemoveList);
				}
			}
			activityList.addAll(couponOrderList);
			// 获取不需要Coupon号的会员活动
			List<Map<String, Object>> campaignList = binOLWPSAL08_Service.getCampaignList(paramMap);
			if (null != campaignList && !campaignList.isEmpty()){
				for(Map<String, Object> m : campaignList){
					// 领用柜台类型:1:任意柜台,2:发卡柜台,3:预约柜台,4:首次购买柜台
					String gotCounter = ConvertUtil.getString(m.get("gotCounter"));
					if(!gotCounter.equals("1") && null!=gotCounter && !gotCounter.equals("")){
						if(gotCounter.equals("2")){							// 发卡柜台
							if(!counterCode.equalsIgnoreCase(counterCodeBelong)){
								removeList.add(m);
							}
						}else if(gotCounter.equals("3")){					// 活动预约柜台
							if(orderCounterCode.size()>0){
								for(Map<String, Object> om : orderCounterCode){
									if(!counterCode.equalsIgnoreCase(om.toString())){
										removeList.add(m);
									}
								}
							}
						}else if(gotCounter.equals("4")){					// 首次购买柜台
							if(!counterCode.equalsIgnoreCase(firstSaleCounter)){
								removeList.add(m);
							}
						}
					}
				}
				for(Map<String,Object> campaignInfo : campaignList){
					// 获取会员活动编号
					String campaignCode = ConvertUtil.getString(campaignInfo.get("activityCode"));
					String exNeedPoint = ConvertUtil.getString(campaignInfo.get("exNeedPoint"));
					// 判断需要的积分是否大于可用积分
					if(CherryUtil.string2double(exNeedPoint) > usablePoint){
						// 如果需要的积分大于可用积分则加入移除列表，移除因积分不足不能参与的活动
						removeList.add(campaignInfo);
					}else{
						// 如果需要的积分小于等于可用积分则判断活动是否已加入到了列表中
						if (null != couponOrderList && !couponOrderList.isEmpty()){
							for(Map<String,Object> couponOrderInfo : couponOrderList){
								String couponActivityCode = ConvertUtil.getString(couponOrderInfo.get("activityCode"));
								// 活动若在列表中已存在则将活动加入到删除列表中
								if(campaignCode.equals(couponActivityCode)){
									removeList.add(campaignInfo);
									break;
								}
							}
						}
					}
				}
			}
			if (null != removeList && !removeList.isEmpty()){
				campaignList.removeAll(removeList);
			}
			activityList.addAll(campaignList);
			
			// 获取促销表中的会员活动
			removeList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> memberPromotionList = binOLWPSAL08_Service.getPromotionList(paramMap);
			// 移除因积分不足不能参与的活动
			if (null != memberPromotionList && !memberPromotionList.isEmpty()){
				for(Map<String,Object> memberPromotionInfo : memberPromotionList){
					String exNeedPoint = ConvertUtil.getString(memberPromotionInfo.get("exNeedPoint"));
					// 判断需要的积分是否大于可用积分
					if(CherryUtil.string2double(exNeedPoint) > usablePoint){
						removeList.add(memberPromotionInfo);
					}
				}
			}
			if (null != removeList && !removeList.isEmpty()){
				memberPromotionList.removeAll(removeList);
			}
			activityList.addAll(memberPromotionList);
			
			// 移除会员活动条件
			paramMap.remove("memberActivity");
		}
		// 获取会员活动表的非会员促销活动
		List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> campaignPromotionList = binOLWPSAL08_Service.getCampaignPromotionList(paramMap);
		if (null != campaignPromotionList && !campaignPromotionList.isEmpty()){
			for(Map<String, Object> m : campaignPromotionList){
				// 领用柜台类型:1:任意柜台,2:发卡柜台,3:预约柜台,4:首次购买柜台
				String gotCounter = ConvertUtil.getString(m.get("gotCounter"));
				if(!gotCounter.equals("1") && null!=gotCounter && !gotCounter.equals("")){
					if(gotCounter.equals("2")){							// 发卡柜台
						if(!counterCode.equalsIgnoreCase(counterCodeBelong)){
							deleteList.add(m);
						}
					}else if(gotCounter.equals("3")){					// 活动预约柜台
						if(orderCounterCode.size()>0){
							for(Map<String, Object> om : orderCounterCode){
								if(!counterCode.equalsIgnoreCase(om.toString())){
									deleteList.add(m);
								}
							}
						}
					}else if(gotCounter.equals("4")){					// 首次购买柜台
						if(!counterCode.equalsIgnoreCase(firstSaleCounter)){
							deleteList.add(m);
						}
					}
				}
			}
			for(Map<String,Object> campaignPromotionInfo : campaignPromotionList){
				// 获取非会员促销活动编号
				String campaignPromotionCode = ConvertUtil.getString(campaignPromotionInfo.get("activityCode"));
				if (null != activityList && !activityList.isEmpty()){
					for(Map<String,Object> activityInfo : activityList){
						String activityCode = ConvertUtil.getString(activityInfo.get("activityCode"));
						// 活动若在列表中已存在则将活动加入到删除列表中
						if(campaignPromotionCode.equals(activityCode)){
							deleteList.add(campaignPromotionInfo);
							break;
						}
					}
				}
			}
		}
		if (null != deleteList && !deleteList.isEmpty()){
			campaignPromotionList.removeAll(deleteList);
		}
		activityList.addAll(campaignPromotionList);
		// 增加非会员活动条件
		paramMap.put("noMemberActivity", "Y");
		// 获取促销活动
		List<Map<String, Object>> promotionList = binOLWPSAL08_Service.getPromotionList(paramMap);
		activityList.addAll(promotionList);
		//根据活动类型区别0非会员活动1会员活动，在活动名称后面加上所对应的标识
		for(Map<String,Object> m:activityList){
			String type=ConvertUtil.getString(m.get("activityType"));
			if("0".equals(type)){
				m.put("activityName", ConvertUtil.getString(m.get("activityName"))+"(促销)");
			}else if("1".equals(type)){
				m.put("activityName", ConvertUtil.getString(m.get("activityName"))+"(会员)");
			}
		}
		return activityList;
	}
	
	@Override
	public List<Map<String, Object>> getActivityProduct(Map<String, Object> map)
			throws Exception {
		String language = ConvertUtil.getString(map.get("language"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String organizationId = ConvertUtil.getString(map.get("organizationId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		// 获取系统业务日期
		String nowTime = binOLWPSAL08_Service.getSYSDate();
		map.put("nowTime", nowTime);
		String businessDate = binOLWPSAL08_Service.getBussinessDate(map);
		String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
		if(!"2".equals(saleDateType)){
			businessDate = DateUtil.coverTime2YMD(binOLWPSAL08_Service.getSYSDate(), "yyyy-MM-dd");
		}
		map.put("businessDate", businessDate);
		// 获取活动类型
		String activityType = ConvertUtil.getString(map.get("activityType"));
		//实体仓库ID
		String inInventoryInfoID="";
		List<Map<String,Object>> departList = binOLCM18_BL.getDepotsByDepartID(organizationId, language);
		if (null == departList || departList.size()==0){
			inInventoryInfoID="";
		}else {
			inInventoryInfoID=ConvertUtil.getString(departList.get(0).get("BIN_DepotInfoID"));
		}
		if("1".equals(activityType)){
			// 会员活动的情况下获取活动礼品信息
			List<Map<String, Object>> campaignProduct = binOLWPSAL08_Service.getCampaignProduct(map);
			if(campaignProduct.size()!=0){
				for(Map<String, Object> m:campaignProduct){
			        Map<String, Object> pramMap = new HashMap<String, Object>(); 
			        pramMap.put("inventoryInfoID", inInventoryInfoID);
					pramMap.put("BIN_DepotInfoID", inInventoryInfoID);
					String BIN_PromotionProductVendorID = ConvertUtil.getString(m.get("productVendorId"));
					pramMap.put("promotionProductVendorID", BIN_PromotionProductVendorID);
					pramMap.put("BIN_ProductVendorID", BIN_PromotionProductVendorID);
					pramMap.put("FrozenFlag", "2");
					String proType = ConvertUtil.getString(m.get("proType"));
					int stock = 0;
					if(proType.equals("P")){
						stock = binOLSSCM01_BL.getPrmStock(pramMap);
					}else {
						stock = binOLCM20_BL.getProductStock(pramMap);
					}
					m.put("stock", stock);
				}
			}
			return campaignProduct;
		}else{
			// 促销活动情况下获取促销礼品信息
			List<Map<String, Object>> promotionProduct = binOLWPSAL08_Service.getPromotionProduct(map);
			if(promotionProduct.size()!=0){
				for(Map<String, Object> m:promotionProduct){
			        Map<String, Object> pramMap = new HashMap<String, Object>(); 
			        pramMap.put("inventoryInfoID", inInventoryInfoID);
					pramMap.put("BIN_DepotInfoID", inInventoryInfoID);
					String BIN_PromotionProductVendorID = ConvertUtil.getString(m.get("productVendorId"));
					pramMap.put("promotionProductVendorID", BIN_PromotionProductVendorID);
					pramMap.put("BIN_ProductVendorID", BIN_PromotionProductVendorID);
					pramMap.put("FrozenFlag", "2");
					String proType = ConvertUtil.getString(m.get("proType"));
					int stock = 0;
					if(proType.equals("P")){
						stock = binOLSSCM01_BL.getPrmStock(pramMap);
					}else {
						stock = binOLCM20_BL.getProductStock(pramMap);
					}
					m.put("stock", stock);
				}
			}
			return promotionProduct;
		}
	}
	
	@Override
	public Map<String, Object> getActivityInfo(Map<String, Object> map)
			throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		// 获取系统业务日期
		String businessDate = binOLWPSAL08_Service.getBussinessDate(map);
		String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
		if(!"2".equals(saleDateType)){
			businessDate = DateUtil.coverTime2YMD(binOLWPSAL08_Service.getSYSDate(), "yyyy-MM-dd");
		}
		map.put("businessDate", businessDate);
		// 获取活动类型
		String activityType = ConvertUtil.getString(map.get("activityType"));
		if("1".equals(activityType)){
			// 会员活动的情况下获取活动信息
			Map<String, Object> campaignInfo = binOLWPSAL08_Service.getCampaignInfo(map);
			if(null!=campaignInfo.get("ActivityToDate")){
				String ActivityToDate = campaignInfo.get("ActivityToDate").toString();
				Date t = DateUtil.coverString2Date(ActivityToDate);
				// 取得数据库系统时间(YYYY-MM-DD HH：mm：SS)
				String sysTime = binOLWPSAL08_Service.getSYSDateTime();
				Date nowTime = DateUtil.coverString2Date(sysTime);
				if(t.getTime()<nowTime.getTime()){
					campaignInfo.put("CODEMESSAGE", "TIMEEXPIRED");
					return campaignInfo;
				}
			}
			return campaignInfo;
		}else{
			// 促销活动情况下获取促销信息
			Map<String, Object> promotionInfo = binOLWPSAL08_Service.getPromotionInfo(map);
			if(null!=promotionInfo.get("ActivityToDate")){
				String ActivityToDate = promotionInfo.get("ActivityToDate").toString();
				Date t = DateUtil.coverString2Date(ActivityToDate);
				// 取得数据库系统时间(YYYY-MM-DD HH：mm：SS)
				String sysTime = binOLWPSAL08_Service.getSYSDateTime();
				Date nowTime = DateUtil.coverString2Date(sysTime);
				if(t.getTime()<nowTime.getTime()){
					promotionInfo.put("CODEMESSAGE", "TIMEEXPIRED");
					return promotionInfo;
				}
			}
			return promotionInfo;
		}
	}

	@Override
	public String getActivityType(Map<String, Object> map) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("MainCode", "".equals((String)map.get("MainCode"))?"0":(String)map.get("MainCode"));
		List<Map<String,Object>>  list=binOLWPSAL08_Service.getActivityType(paramMap);
		try {
			Map<String,Object> map_=list.get(0);
			return (String)map_.get("ActivityType");
		} catch (Exception e) {
			return "3";
		}
	}


	/**
	 * 根据jni返回的Bacode和unicode来查询出智能促销接口拿不到的属性
	 */
	@Override
	public Map<String, Object> getPromotionProductinfo(Map<String,Object> paramMap) {
		Map<String,Object> map=binOLWPSAL08_Service.getPromotionProductinfo(paramMap);
		return map;
	}


	@Override
	public ArrayList<SaleDetailEntity> getSaleDetailEntityList(
			List<Map<String, Object>> outdetail_back) {
		ArrayList<SaleDetailEntity> inputSalemain = new ArrayList<SaleDetailEntity>();
		int num=1;
		for(Map<String,Object> m:outdetail_back){
			SaleDetailEntity detail_back=new SaleDetailEntity();
			detail_back.setBarcode(ConvertUtil.getString(m.get("barcode")));
			detail_back.setUnitcode(ConvertUtil.getString(m.get("unitcode")));
			detail_back.setOriginalBrand(ConvertUtil.getString(m.get("originalBrand")));
			detail_back.setQuantity(Integer.parseInt(ConvertUtil.getString(m.get("quantity"))));
			detail_back.setPrice(Double.parseDouble(ConvertUtil.getString(m.get("price"))));
			detail_back.setOri_price(Double.parseDouble(ConvertUtil.getString(m.get("ori_price"))));
			detail_back.setType(ConvertUtil.getString(m.get("type")));
			detail_back.setMaincode(ConvertUtil.getString(m.get("maincode")));
			detail_back.setMainname(ConvertUtil.getString(m.get("mainname")));
			detail_back.setItemTag(num);
			num++;
			detail_back.setDiscount(Double.parseDouble(ConvertUtil.getString(m.get("discount"))));
			detail_back.setProductid(Integer.parseInt(ConvertUtil.getString(m.get("productid"))));
			detail_back.setProname(ConvertUtil.getString(m.get("proname")));
			detail_back.setMainitem_tag(Integer.parseInt(ConvertUtil.getString(m.get("mainitem_tag"))));
			detail_back.setActivitycode(ConvertUtil.getString(m.get("activitycode")));
			detail_back.setNew_flag(Integer.parseInt(ConvertUtil.getString(m.get("new_flag"))));
			inputSalemain.add(detail_back);
		}
		
		return inputSalemain;
	}

	@Override
	public ArrayList<SaleRuleResultEntity> getSaleRuleResultlEntityList(
			List<Map<String, Object>> outresult_back) {
		ArrayList<SaleRuleResultEntity> inputSaleResult = new ArrayList<SaleRuleResultEntity>();
		for(Map<String,Object> m:outresult_back){
			SaleRuleResultEntity s=new SaleRuleResultEntity();
			s.setFlag(Integer.parseInt(ConvertUtil.getString(m.get("flag"))));
			s.setTimes(Integer.parseInt(ConvertUtil.getString(m.get("times"))));
			s.setMatchtimes(Integer.parseInt(ConvertUtil.getString(m.get("matchtimes"))));
			s.setMaincode(ConvertUtil.getString(m.get("maincode")));
			s.setMainname(ConvertUtil.getString(m.get("mainname")));
			s.setIsmust(ConvertUtil.getString(m.get("ismust")));
			s.setRulecondtype(ConvertUtil.getString(m.get("rulecondtype")));
			s.setSubcampaignvalid(ConvertUtil.getString(m.get("subcampaignvalid")));
			s.setLevel(Integer.parseInt(ConvertUtil.getString(m.get("level"))));
			s.setCheckflag(Integer.parseInt(ConvertUtil.getString(m.get("checkflag"))));
			s.setActivitycode(ConvertUtil.getString(m.get("activitycode")));
			s.setRulebcj(ConvertUtil.getInt(m.get("rulebcj")));
			s.setOriginalBrand(ConvertUtil.getString(m.get("originalBrand")));
			s.setProductNumber(ConvertUtil.getInt(m.get("productNumber")));
			s.setRuleamount(Double.parseDouble(ConvertUtil.getString(m.get("ruleamount"))));
			s.setActivityType(ConvertUtil.getString(m.get("activityType")));
			inputSaleResult.add(s);
		}
		return inputSaleResult;
	}

	@Override
	public ArrayList<SaleDetailEntity> saleDetail2Entity(
			List<Map<String, Object>> saleDetailMap_list) {
		int num=1;
		ArrayList<SaleDetailEntity> inputSaledetail=new ArrayList<SaleDetailEntity>();
		for(Map<String,Object> m:saleDetailMap_list){
			SaleDetailEntity sale_detail=new SaleDetailEntity();
			if("".equals(ConvertUtil.getString(m.get("prtVendorId"))) || "HDZD".equals(ConvertUtil.getString(m.get("prtVendorId")))){
				continue;
			}else{
				sale_detail.setProductid(Integer.parseInt(ConvertUtil.getString(m.get("prtVendorId"))));
			}
			Map<String,Object> originalBrand_map=binOLWPSAL08_Service.getProductBrand(m);
			if(originalBrand_map != null){
				String originalBrand=ConvertUtil.getString(originalBrand_map.get("originalBrand"));
				sale_detail.setOriginalBrand(originalBrand);//产品自身品牌,（Code类别：1299）
			}else{
				sale_detail.setOriginalBrand("");//产品自身品牌,（Code类别：1299）
			}
			sale_detail.setBarcode(ConvertUtil.getString(m.get("barCode")));//商品条码
			sale_detail.setUnitcode(ConvertUtil.getString(m.get("unitCode")));//厂商编码
			sale_detail.setQuantity(ConvertUtil.getInt(m.get("quantityuArr")));//数量
			double ori_price=Double.parseDouble("".equals(ConvertUtil.getString(m.get("pricePay")))?"0.00":ConvertUtil.getString(m.get("pricePay")));
			sale_detail.setOri_price(ori_price);//原价
			double price=Double.parseDouble("".equals(ConvertUtil.getString(m.get("realPriceArr")))?"0.00":ConvertUtil.getString(m.get("realPriceArr")));
			sale_detail.setPrice(price);//价格price现在的售价
			sale_detail.setType(ConvertUtil.getString(m.get("proType")));//产品类型
			sale_detail.setMaincode("");//主活动码
			sale_detail.setMainname("");//主活动名称
			sale_detail.setNew_flag(0);//是否是促销引擎新增的记录，如果0不是新增的记录，如果是1是新增的记录
			sale_detail.setProname(ConvertUtil.getString(m.get("productNameArr")));
			sale_detail.setActivitycode("");
			sale_detail.setItemTag(num);//业务的每个商品流水号discountRateArr
			num++;
			sale_detail.setDiscount(Double.parseDouble("".equals(ConvertUtil.getString(m.get("discountRateArr")))?"100.0":ConvertUtil.getString(m.get("discountRateArr"))));//没有折扣的话就是100
			inputSaledetail.add(sale_detail);
		}
		
		return inputSaledetail;
	}

	@Override
	public Map<String, Object> convert2JSPEntity(
			ArrayList<SaleDetailEntity> outdetail) {
		Map<String,Object> JSPEntityMap=new HashMap<String, Object>();
//		List<Map<String,Object>> promotionInfo_list_old=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> promotion_all_list_old=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> promotionInfo_list_new=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> promotion_all_list_new=new ArrayList<Map<String,Object>>();
		//新建一个List<Map>来存放所得到的数据,在后台进行封装页面上需要显示的数据
		for(SaleDetailEntity detail:outdetail){
			if(1 == detail.getNew_flag()){//新添加的数据
				Map<String,Object> promotionInfo=new HashMap<String, Object>();
				Map<String,Object> promotion_all=new HashMap<String, Object>();
				// 获取活动信息
				promotionInfo.put("oldPrice", detail.getOri_price());
				promotionInfo.put("quantity", detail.getQuantity());
				promotionInfo.put("price", detail.getPrice()*detail.getQuantity());
				promotionInfo.put("exPoint", 0);
				promotionInfo.put("mainCode", detail.getMaincode());
				promotionInfo.put("activityName", detail.getMainname());
				promotionInfo.put("prtType",detail.getType() );
				promotionInfo.put("discount",detail.getDiscount());
				promotionInfo_list_new.add(promotionInfo);
				//获取活动产品信息
				Map<String,Object> paramMap=new HashMap<String, Object>();
				paramMap.put("UnitCode", detail.getUnitcode());
				paramMap.put("BarCode", detail.getBarcode());
				paramMap.put("MainCode", detail.getMaincode());
				String type=this.getActivityType(paramMap);
				Map<String,Object> promotionProduct=new HashMap<String, Object>();
				promotionProduct.put("unitCode", detail.getUnitcode());
				promotionProduct.put("barCode", detail.getBarcode());
				promotionProduct.put("productName", detail.getMainname());
				promotionProduct.put("isStock", 0);
				promotionProduct.put("counterActCode","" );
				promotionProduct.put("productVendorId",detail.getProductid());
				promotionProduct.put("mainCode", detail.getMaincode());
				promotionProduct.put("activityName", detail.getMainname());
				promotionProduct.put("activityType", type);
				promotionProduct.put("quantity", detail.getQuantity());
				promotionProduct.put("oldPrice", detail.getOri_price());
				promotionProduct.put("price", detail.getPrice());
				promotionProduct.put("proType", detail.getType());
				promotionProduct.put("exPoint", 0);
				promotionProduct.put("isExchanged", 1);
				promotionProduct.put("discount", detail.getDiscount());
				promotion_all.put("promotionInfo", promotionInfo);
				promotion_all.put("promotionProduct", promotionProduct);
				promotion_all_list_new.add(promotion_all);
			}else if(0 == detail.getNew_flag()){//计算后的购物车数据
//				Map<String,Object> promotionInfo=new HashMap<String, Object>();
				Map<String,Object> promotion_all=new HashMap<String, Object>();
				// 获取活动信息
//				promotionInfo.put("oldPrice", detail.getOri_price());
//				promotionInfo.put("quantity", detail.getQuantity());
//				promotionInfo.put("price", detail.getPrice()*detail.getQuantity());
//				promotionInfo.put("exPoint", 0);
//				promotionInfo.put("mainCode", detail.getMaincode());
//				promotionInfo.put("activityName", detail.getMainname());
//				promotionInfo.put("prtType",detail.getType() );
//				promotionInfo.put("discount",detail.getDiscount());
//				promotionInfo_list_old.add(promotionInfo);
				//获取活动产品信息
				Map<String,Object> paramMap=new HashMap<String, Object>();
				paramMap.put("UnitCode", detail.getUnitcode());
				paramMap.put("BarCode", detail.getBarcode());
				paramMap.put("MainCode", detail.getMaincode());
				String type=this.getActivityType(paramMap);
				Map<String,Object> promotionProduct=new HashMap<String, Object>();
				promotionProduct.put("unitCode", detail.getUnitcode());
				promotionProduct.put("barCode", detail.getBarcode());
				promotionProduct.put("productName", detail.getProname());
				promotionProduct.put("isStock", 0);
				promotionProduct.put("counterActCode","" );
				promotionProduct.put("productVendorId",detail.getProductid());
				promotionProduct.put("mainCode", detail.getMaincode());
				promotionProduct.put("activityName", detail.getMainname());
				promotionProduct.put("activityType", type);
				promotionProduct.put("quantity", detail.getQuantity());
				promotionProduct.put("oldPrice", detail.getOri_price());
				promotionProduct.put("price", detail.getPrice());
				promotionProduct.put("proType", detail.getType());
				promotionProduct.put("exPoint", 0);
				promotionProduct.put("isExchanged", 1);
				promotionProduct.put("discount", detail.getDiscount());
//				promotion_all.put("promotionInfo", promotionInfo);
				promotion_all.put("promotionProduct", promotionProduct);
				promotion_all_list_old.add(promotion_all);
			}
		}
//		JSPEntityMap.put("promotionInfo_list_old", promotionInfo_list_old);
		JSPEntityMap.put("promotion_all_list_old", promotion_all_list_old);
		JSPEntityMap.put("promotionInfo_list_new", promotionInfo_list_new);
		JSPEntityMap.put("promotion_all_list_new", promotion_all_list_new);
		return JSPEntityMap;
	}

	@Override
	public List<Map<String, Object>> delPromotionMainInfo(
			List<Map<String, Object>> saleDetailMap_list) {
		saleDetailMap_list.remove(saleDetailMap_list.size()-1);//去除多余的exchange=1，空行的那一项
//		for(int i=0;i<saleDetailMap_list.size();i++){
//			String prtVendorId=ConvertUtil.getString(saleDetailMap_list.get(i).get("prtVendorId"));
//            if(Character.isLetter(prtVendorId.charAt(i))){   //用char包装类中的判断字母的方法判断每一个字符
//            	saleDetailMap_list.remove(i);
//            }
//		}
		return saleDetailMap_list;
	}

	@Override
	public ArrayList<SaleMainEntity> saleMain2Entity(BINOLWPSAL08_Form form,String counterCode) {
		ArrayList<SaleMainEntity> inputSalemain=new ArrayList<SaleMainEntity>();
		SaleMainEntity sale_main=new SaleMainEntity();
		if("".equals(form.getMemberCode().trim())){
			sale_main.setMemcode("000000000");//会员号	
		}else{
			sale_main.setMemcode(ConvertUtil.getString(form.getMemberCode()));//会员号
		}
		sale_main.setTxddate(CherryUtil.getSysDateTime("yyMMdd"));//交易日期
		sale_main.setTxdtime(CherryUtil.getSysDateTime("HHmmss"));//交易时间
		sale_main.setBacode(ConvertUtil.getString(form.getBaCode()));//ba号
		sale_main.setCountercode(counterCode);//柜台号
		sale_main.setDiscount_zd(0.0==form.getTotalDiscountRate()?100:form.getTotalDiscountRate());
		sale_main.setMemlevel(ConvertUtil.getString(form.getMemberLevel()));
		inputSalemain.add(sale_main);
		return inputSalemain;
	}


	@Override
	public Map<String, Object> getLYHDProductInfo(Map<String, Object> params) {
		return binOLWPSAL08_Service.getLYHDProductInfo(params);
	}

	@Override
	public List<Map<String, Object>> getLYHDProductDetail(Map<String, Object> params) {
		return binOLWPSAL08_Service.getLYHDProductDetail(params);
	}

	@Override
	public Integer checkRegular(Map<String, Object> params) {
		//正则表达式
		Map<String,Object> regular_map=binOLWPSAL08_Service.getRegular(params);
		if(null == regular_map ){
			return 0;
		}
		String localValidRule=ConvertUtil.getString(regular_map.get("localValidRule"));
		if("".equals(localValidRule)){
			return 0;
		}else{
			//用户的输入信息
			String customerText=ConvertUtil.getString(params.get("customerText"));
			Pattern p = Pattern.compile(localValidRule);
			Matcher m1 = p.matcher(customerText); 
			Boolean flag=m1.matches();
			if(flag){
				return 1;
			}else{
				return 0;
			}
		}
	}

	@Override
	public Integer checkMobilephoneMemcode(Map<String, Object> params) {
		String memcode=ConvertUtil.getString(params.get("memcode"));
		String mobile=ConvertUtil.getString(params.get("mobile"));
		if("".equals(memcode) && "".equals(mobile)){
			return 0;
		}
		Map<String,Object> result_map=binOLWPSAL08_Service.checkMobilephoneMemcode(params);
		if(result_map == null){
			return 0;
		}
		String mobile_result=ConvertUtil.getString(result_map.get("mobile"));
		String memcode_result=ConvertUtil.getString(result_map.get("memcode"));
		if(memcode.equals(memcode_result)){
			return 1;
		}
		if(mobile.equals(mobile_result)){
			return 1;
		}
		return 0;
	}

	@Override
	public ArrayList<SaleProductDetailEntity> getSaleProductDetialEntityList(
			List<Map<String, Object>> outproduct_back) {
		ArrayList<SaleProductDetailEntity> inputProductList = new ArrayList<SaleProductDetailEntity>();
		if(null!=outproduct_back && !outproduct_back.isEmpty()){
			for(Map<String, Object> m : outproduct_back){
				SaleProductDetailEntity sale_product = new SaleProductDetailEntity();
				sale_product.setMaincode(ConvertUtil.getString(m.get("maincode")));
				sale_product.setBarcode(ConvertUtil.getString(m.get("barcode")));
				sale_product.setUnicode(ConvertUtil.getString(m.get("unicode")));
				sale_product.setQuantity(ConvertUtil.getInt(m.get("quantity")));
				sale_product.setPrice(CherryUtil.string2double(ConvertUtil.getString(m.get("price"))));
				sale_product.setOri_price(CherryUtil.string2double(ConvertUtil.getString(m.get("ori_price"))));
				sale_product.setProname(ConvertUtil.getString(m.get("proname")));
				inputProductList.add(sale_product);
			}
		}
		return inputProductList;
	}
	
}
