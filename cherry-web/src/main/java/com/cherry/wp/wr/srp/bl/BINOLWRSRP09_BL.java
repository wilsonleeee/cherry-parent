package com.cherry.wp.wr.srp.bl;

import com.cherry.cm.util.CherryUtil;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP09_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付构成报表BL
 *
 * @author fengxuebo
 * @version 1.0 2016/10/31
 */
public class BINOLWRSRP09_BL implements BINOLWRSRP09_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;

	@Override
	public Map<String, Object> getPayTypeCountInfo(Map<String, Object> map) {
		Map<String, Object> saleCountInfo = binOLWRSRP99_Service.getPayTypeCountInfo(map);
		if (saleCountInfo != null){
			BigDecimal amount = (BigDecimal)saleCountInfo.get("amount");
			BigDecimal totalCostPrice = (BigDecimal)saleCountInfo.get("totalCostPrice");
			BigDecimal quantity = (BigDecimal)saleCountInfo.get("quantity");
			Integer saleCount = (Integer)saleCountInfo.get("saleCount");

			/*连带率=总数量/总单数*/
			double joinRate = 0;
			/*客单价=总金额/总单数*/
			double guestPrice = 0;
			if(saleCount != 0) {
				joinRate = CherryUtil.div(quantity.doubleValue(), saleCount, 2);
				guestPrice = CherryUtil.div(amount.doubleValue(), saleCount, 2);
				saleCountInfo.put("joinRate", joinRate);
				saleCountInfo.put("guestPrice", guestPrice);
			} else {
				saleCountInfo.put("joinRate", 0);
				saleCountInfo.put("guestPrice", 0);
			}

			/*均价=客单价/连带率*/
			if(joinRate != 0) {
				saleCountInfo.put("averagePrice", CherryUtil.div(guestPrice, joinRate, 2));
			} else {
				saleCountInfo.put("averagePrice", 0);
			}

			/*毛利额=销售金额-成本（如果成本字段为空，则以0对待）*/
			BigDecimal grossProfitAmount = amount.subtract(totalCostPrice);
			BigDecimal isZero = new BigDecimal("0");
			if (totalCostPrice.compareTo(isZero)==0){
				grossProfitAmount=  new BigDecimal("0");
			}
			saleCountInfo.put("grossProfitAmount",grossProfitAmount);

			/*毛利率=毛利额/销售额*/
			if(amount.doubleValue() != 0) {
				saleCountInfo.put("grossMargin", grossProfitAmount.divide(amount,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			} else {
				saleCountInfo.put("grossMargin", 0);
			}

		}
		return saleCountInfo;
	}

	@Override
	public List<Map<String, Object>> getPayTypeSaleList(Map<String, Object> map) {
		return binOLWRSRP99_Service.getPayTypeSaleList(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		return null;
	}
}
