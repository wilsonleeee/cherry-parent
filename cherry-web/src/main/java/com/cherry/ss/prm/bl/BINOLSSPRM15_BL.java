package com.cherry.ss.prm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.service.BINOLSSPRM15_Service;

public class BINOLSSPRM15_BL {
	@Resource
	private BINOLSSPRM15_Service binOLSSPRM15_Service;
	
	/**
	 * 取得带权限柜台对应已下发促销活动
	 * @param map
	 * @return List
	 */
	public List getActiveList(Map<String,Object> map){
		List<Map<String, Object>> activeList = binOLSSPRM15_Service.getActiveList(map);
		return activeList;
	}
	/**
	 * 促销活动名模糊查询(输入框AJAX)
	 * @param map
	 * @return int
	 */
	public int getActiveCount(Map<String, Object> map) {
		int count = binOLSSPRM15_Service.getActiveCount(map);
		return count;
	}
	
	/**
	 * 柜台模糊查询(输入框AJAX)
	 * @param map
	 * @return String
	 */
	public String indSearchPrmCounter(Map<String, Object> map){
		List<Map<String, Object>> activeList = binOLSSPRM15_Service.indSearchPrmCounter(map);
		// 转化List为所需要的String类型
		StringBuffer sb= new StringBuffer();
		for (int i = 0;i<activeList.size();i++){
			HashMap resultMap = (HashMap)activeList.get(i);
			sb.append((String)resultMap.get("counterNameIF"));
			sb.append("|");
			sb.append((String)resultMap.get("counterCode"));
			sb.append("|");
			sb.append(String.valueOf(resultMap.get("prmCounterId")));
			if (i!=activeList.size()){
				sb.append("\n");
			}
		
		}
		return sb.toString();
	}

	/**
	 * 促销品信息模糊查询(输入框AJAX)
	 * @param map
	 * @return String
	 */
	public String indSearchPrmPrt(Map<String, Object> map){
		//取得促销品信息
		List<Map<String, Object>> activeList = binOLSSPRM15_Service.indSearchPrmPrt(map);
		// 转化List为所需要的String类型
		StringBuffer sb= new StringBuffer();
		if(null != activeList){
			for (int i = 0;i<activeList.size();i++){
				Map<String, Object> temp = activeList.get(i);
				sb.append(ConvertUtil.getString(temp.get(CherryConstants.NAMETOTAL)));
				sb.append("|");
				sb.append(ConvertUtil.getString(temp.get(CherryConstants.UNITCODE)))
				  .append("-")
				  .append(ConvertUtil.getString(temp.get(CherryConstants.BARCODE)));
				sb.append("|");
				sb.append(ConvertUtil.getString(temp.get("id")));
				sb.append("|");
				sb.append(ConvertUtil.getString(temp.get("type")));
				if (i!=activeList.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 促销活动名模糊查询(输入框AJAX)
	 * @param map
	 * @return String
	 */
	public String indSearchPrmActName(Map<String, Object> map){

		List<Map<String, Object>> activeList = binOLSSPRM15_Service.indSearchPrmActName(map);
		// 转化List为所需要的String类型
		StringBuffer sb= new StringBuffer();
		for (int i = 0;i<activeList.size();i++){
			HashMap resultMap = (HashMap)activeList.get(i);
			sb.append((String)resultMap.get("activityName"));
			sb.append("|");
			sb.append((String)resultMap.get("activityCode"));
			sb.append("|");
			sb.append((String)resultMap.get("activityCode"));
			if (i!=activeList.size()){
				sb.append("\n");
			}
		
		}
		return sb.toString();
	}

}
