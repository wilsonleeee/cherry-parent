package com.cherry.st.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.st.common.interfaces.BINOLSTCM20_IF;
import com.cherry.st.common.service.BINOLSTCM20_Service;

/**
 * 
 * @ClassName: BINOLSTCM20_BL 
 * @Description: TODO(大仓加权平均成本价格表操作共通) 
 * @author menghao
 * @version v1.0.0 2015-10-8 
 *
 */
public class BINOLSTCM20_BL implements BINOLSTCM20_IF {

    @Resource(name="binOLSTCM20_Service")
    private BINOLSTCM20_Service binOLSTCM20_Service;
    
    @Override
	public List<Map<String, Object>> getProductWeightedAvgInfo(Map<String, Object> map) {
    	// 根据入库单明细中的产品，获取其大仓库存并计算加权平均价
    	return binOLSTCM20_Service.getProductWeightedAvgInfo(map);
    	
	}

	@Override
	public void insertWeightedAvgPrice(List<Map<String, Object>> list) {
		// 将数据批量写入大仓加权平均成本价格表
		binOLSTCM20_Service.insertProductWeightedAvgPrice(list);
	} 
	
	@Override
	public void handleProductWeightedAvgPriceByIndepotBill(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 入库单主ID
		paramMap.put("BIN_ProductInDepotID", map.get("BIN_ProductInDepotID"));
		paramMap.put("CreatedBy",map.get("CreatedBy"));
		paramMap.put("CreatePGM",map.get("CreatePGM"));
		paramMap.put("UpdatedBy",map.get("CreatedBy"));
		paramMap.put("UpdatePGM",map.get("CreatePGM"));
		List<Map<String, Object>> productWeightedAvgInfo = this.getProductWeightedAvgInfo(paramMap);
		this.insertWeightedAvgPrice(productWeightedAvgInfo);
	}

}
