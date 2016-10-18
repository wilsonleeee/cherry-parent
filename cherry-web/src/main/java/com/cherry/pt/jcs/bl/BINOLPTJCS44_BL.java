package com.cherry.pt.jcs.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS44_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS44_Service;

public class BINOLPTJCS44_BL implements BINOLPTJCS44_IF{
	
	@Resource
	private BINOLPTJCS44_Service binOLPTJCS44_Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
		
	@Override
	public Map<String, Object> getDropCountInfo(Map<String, Object> map) {
		return binOLPTJCS44_Service.getDropCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getDropList(Map<String, Object> map) {
		return binOLPTJCS44_Service.getDropList(map);
	}

	public List<Map<String, Object>> getCntProductList(Map<String, Object> map) {
		
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 产品方案添加产品模式 1:标准模式 2:颖通模式
		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		isPosCloud = "1";
		map.put("isPosCloud", isPosCloud);
		// TODO Auto-generated method stub
		String businessDate = binOLPTJCS44_Service.getBussinessDate(map);
		map.put("businessDate", businessDate);
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
			map.put("cntPrtModeConf", cntPrtModeConf);
			map.put("soluAddModeConf", soluAddModeConf);
			resultList = binOLPTJCS44_Service.getCntProductList(map);
		}else{
			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
				resultList = binOLPTJCS44_Service.getProductList(map);
			}else{
				// 检查柜台是否有分配可用的方案
				resultList = binOLPTJCS44_Service.chkCntSoluData(map);
				if(CherryUtil.isBlankList(resultList)){
					// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
					resultList = binOLPTJCS44_Service.getProductList(map);
				}else{
					// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
					map.put("cntPrtModeConf", cntPrtModeConf);
					map.put("soluAddModeConf", soluAddModeConf);
					resultList = binOLPTJCS44_Service.getCntProductList(map);
				}
				
			}
		}
		return resultList;
	}
	
	public int getCntProductCount(Map<String, Object> map) {
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 产品方案添加产品模式 1:标准模式 2:颖通模式
		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//				isPosCloud = "1";
		map.put("isPosCloud", isPosCloud);
		// TODO Auto-generated method stub
		String businessDate = binOLPTJCS44_Service.getBussinessDate(map);
		map.put("businessDate", businessDate);
		
		int resultCount = 0;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
			map.put("cntPrtModeConf", cntPrtModeConf);
			map.put("soluAddModeConf", soluAddModeConf);
			resultCount = binOLPTJCS44_Service.getCntProductCount(map);
		}else{
			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
				resultCount = binOLPTJCS44_Service.getProductCount(map);
			}else{
				// 检查柜台是否有分配可用的方案
				resultList = binOLPTJCS44_Service.chkCntSoluData(map);
				if(CherryUtil.isBlankList(resultList)){
					// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
					resultCount = binOLPTJCS44_Service.getProductCount(map);
				}else{
					// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
					map.put("cntPrtModeConf", cntPrtModeConf);
					map.put("soluAddModeConf", soluAddModeConf);
					resultCount = binOLPTJCS44_Service.getCntProductCount(map);
				}
				
			}
		}
		return resultCount;
	}

}
