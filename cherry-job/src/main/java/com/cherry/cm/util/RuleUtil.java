package com.cherry.cm.util;

import java.util.ArrayList;
import java.util.List;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.ss.prm.dto.CommodityDTO;
import com.cherry.ss.prm.dto.PromotionRewardDTO;
import com.cherry.ss.prm.dto.SaleRecordDTO;
import com.cherry.ss.prm.dto.SaleRecordDetailDTO;

@SuppressWarnings("unchecked")
public class RuleUtil {
	/**
	 * 循环比较购买产品和所对应的数量
	 * 
	 * @param saleRecordDetailList
	 * @param unitCode
	 * @param quantity
	 * @param option
	 * @return
	 */
	public static boolean compareListKey(List<SaleRecordDetailDTO> saleRecordDetailList, String unitCode, int quantity, int option) {
		// 循环销售明细数据List
		for (int i = 0; i < saleRecordDetailList.size(); i++) {
			SaleRecordDetailDTO saleRecordDetailDTO = (SaleRecordDetailDTO) saleRecordDetailList.get(i);
			if (!saleRecordDetailDTO.getUnitCode().equals(unitCode)) {
				continue;
			} else {
				if (option != -1) {
					switch (option) {
					case 0:
						if (saleRecordDetailDTO.getQuantity() < quantity) {
							return true;
						}
					case 1:
						if (saleRecordDetailDTO.getQuantity() <= quantity) {
							return true;
						}
					case 2:
						if (saleRecordDetailDTO.getQuantity() == quantity) {
							return true;
						}
					case 3:
						if (saleRecordDetailDTO.getQuantity() != quantity) {
							return true;
						}
					case 4:
						if (saleRecordDetailDTO.getQuantity() >= quantity) {
							return true;
						}
					case 5:
						if (saleRecordDetailDTO.getQuantity() > quantity) {
							return true;
						}
					}
				} else {
					continue;
				}
			}
		}
		return false;
	}

	/**
	 * 设定促销奖励
	 * 
	 * @param saleRecordDetailDTO
	 * @param rewardAmount
	 * @param commodityDTOList
	 */
	public static void setPromotionReward(SaleRecordDTO saleRecordDTO, float rewardAmount, List<CommodityDTO> commodityDTOList,String ruleName) {
		// 新建促销结果
		PromotionRewardDTO promotionRewardDTO = new PromotionRewardDTO();
		// 设定促销金额
		promotionRewardDTO.setRewardAmount(rewardAmount);
		// 设定促销奖励
		promotionRewardDTO.setCommodityDTOList(commodityDTOList);
		promotionRewardDTO.setActiveId(ruleName);
		List<PromotionRewardDTO> promotionRewardList = saleRecordDTO.getPromotionRewardList();
		if (promotionRewardList==null || promotionRewardList.isEmpty()){
			List <PromotionRewardDTO> list = new ArrayList();
			list.add(promotionRewardDTO);
			saleRecordDTO.setPromotionRewardList(list);
		}else{
			promotionRewardList.add(promotionRewardDTO);
			saleRecordDTO.setPromotionRewardList(promotionRewardList);
		}
		
	}

	/**
	 * 比较促销品入出库数量
	 * 
	 * @param saleRecordDTO
	 */

	public static void comparePromotionStockInOut(SaleRecordDTO saleRecordDTO) {
		// 取得销售明细数据
		List<SaleRecordDetailDTO> saleRecordDetailDTOList = saleRecordDTO.getSaleRecordDetailList();
		// 取得促销奖励List
		List<PromotionRewardDTO> promotionRewardList = saleRecordDTO.getPromotionRewardList();
		if (promotionRewardList==null || promotionRewardList.isEmpty()){
			return;
		}
		
		// 差异促销品List
		List<CommodityDTO> defProProductList = new ArrayList();
		
		for (int k=0;k<promotionRewardList.size();k++){
			PromotionRewardDTO promotionRewardDTO =promotionRewardList.get(k);
			// 取得奖励促销产品List
			List<CommodityDTO> commodityDTOList = promotionRewardDTO.getCommodityDTOList();

			for (int i = 0; i < commodityDTOList.size(); i++) {
				CommodityDTO commodityDTO = commodityDTOList.get(i);
				boolean skipFlag = false;
				for (int j = 0; j < saleRecordDetailDTOList.size(); j++) {
					SaleRecordDetailDTO saleRecordDetailDTO = saleRecordDetailDTOList.get(j);
					// 判断是促销品的条码,unitcode,数量是否一致
					if (saleRecordDetailDTO.getSaleType().equals("P")) {
						// 当一条销售记录匹配多个规则时，不管正确与否都会被记录下来
						if (promotionRewardList.size()>1 || (commodityDTO.getUnitCode().equals(saleRecordDetailDTO.getUnitCode()) && commodityDTO.getBarCode().equals(saleRecordDetailDTO.getBarCode()))) {
							if (promotionRewardList.size()>1 || commodityDTO.getQuantity() != saleRecordDetailDTO.getQuantity()) {
								CommodityDTO commodityDTONew = new CommodityDTO();
								commodityDTONew.setBarCode(commodityDTO.getBarCode());
								commodityDTONew.setUnitCode(commodityDTO.getUnitCode());
								// 理论数量
								commodityDTONew.setQuantityOther(commodityDTO.getQuantity());
								// 实际数量
								commodityDTONew.setQuantity(saleRecordDetailDTO.getQuantity());
								// 促销活动id
								commodityDTONew.setActiveID(promotionRewardDTO.getActiveId());
								// 价格
								commodityDTONew.setPrice(commodityDTO.getPrice());
								defProProductList.add(commodityDTONew);
							}
							skipFlag = true;
							break;
						}
					}
				}
				if (!skipFlag) {
					CommodityDTO commodityDTONew = new CommodityDTO();
					commodityDTONew.setBarCode(commodityDTO.getBarCode());
					commodityDTONew.setUnitCode(commodityDTO.getUnitCode());
					// 理论数量
					commodityDTONew.setQuantityOther(commodityDTO.getQuantity());
					// 实际数量
					commodityDTONew.setQuantity(0);
					// 促销活动id
					commodityDTONew.setActiveID(promotionRewardDTO.getActiveId());
					// 价格
					commodityDTONew.setPrice(commodityDTO.getPrice());
					defProProductList.add(commodityDTONew);
				}
			}
			
			for (int i=0;i<saleRecordDetailDTOList.size();i++){
				SaleRecordDetailDTO saleRecordDetailDTO = saleRecordDetailDTOList.get(i);
				// 标记是否进行过促销奖励比较
				boolean tzzk_flag = false;
				if (saleRecordDetailDTO.getSaleType().equals("P")){
					if (saleRecordDetailDTO.getUnitCode().equals(CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE)){
						double rewardAmount = saleRecordDetailDTO.getPrice();
						if (rewardAmount !=promotionRewardDTO.getRewardAmount()){
							CommodityDTO commodityDTONew = new CommodityDTO();
							commodityDTONew.setBarCode(CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE);
							commodityDTONew.setUnitCode(CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE);
							// 实际价格
							commodityDTONew.setPrice(saleRecordDetailDTO.getPrice());
							// 理论价格
							commodityDTONew.setPriceOther(promotionRewardDTO.getRewardAmount());
							// 促销活动id
							commodityDTONew.setActiveID(promotionRewardDTO.getActiveId());
							defProProductList.add(commodityDTONew);
						}
						tzzk_flag = true;
					}else{
						if (commodityDTOList.isEmpty()){
							CommodityDTO commodityDTONew = new CommodityDTO();
							commodityDTONew.setBarCode(saleRecordDetailDTO.getBarCode());
							commodityDTONew.setUnitCode(saleRecordDetailDTO.getUnitCode());
							// 理论数量
							commodityDTONew.setQuantityOther(0);
							// 实际数量
							commodityDTONew.setQuantity(saleRecordDetailDTO.getQuantity());
							// 促销活动id
							commodityDTONew.setActiveID(promotionRewardDTO.getActiveId());
							defProProductList.add(commodityDTONew);
						}else{
							boolean skipFlag =false;
							for (int j = 0; j < commodityDTOList.size(); j++) {
								CommodityDTO commodityDTO = commodityDTOList.get(j);
								if (commodityDTO.getUnitCode().equals(saleRecordDetailDTO.getUnitCode()) && commodityDTO.getBarCode().equals(saleRecordDetailDTO.getBarCode())){
									skipFlag = true;
									break;
								}else{
									continue;
								}
							}
							
							if (!skipFlag){
								CommodityDTO commodityDTONew = new CommodityDTO();
								commodityDTONew.setBarCode(saleRecordDetailDTO.getBarCode());
								commodityDTONew.setUnitCode(saleRecordDetailDTO.getUnitCode());
								// 理论数量
								commodityDTONew.setQuantityOther(0);
								// 实际数量
								commodityDTONew.setQuantity(saleRecordDetailDTO.getQuantity());
								// 促销活动id
								commodityDTONew.setActiveID(promotionRewardDTO.getActiveId());
								defProProductList.add(commodityDTONew);
							}
						}
					}
				}
				
				// 促销奖励中设定了金额奖励,实际操作中没有促销金额奖励
				if (i == saleRecordDetailDTOList.size()-1 && promotionRewardDTO.getRewardAmount()!=0 && !tzzk_flag){
					CommodityDTO commodityDTONew = new CommodityDTO();
					commodityDTONew.setBarCode(CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE);
					commodityDTONew.setUnitCode(CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE);
					// 实际价格
					commodityDTONew.setPrice(0);
					// 理论价格
					commodityDTONew.setPriceOther(promotionRewardDTO.getRewardAmount());
					// 促销活动id
					commodityDTONew.setActiveID(promotionRewardDTO.getActiveId());
					defProProductList.add(commodityDTONew);
				}
			}
			
		}

		saleRecordDTO.setDefProProductList(defProProductList);
	}
	
}
