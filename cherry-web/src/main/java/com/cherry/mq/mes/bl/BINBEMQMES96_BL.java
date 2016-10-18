package com.cherry.mq.mes.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM18_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.service.BINBEMQMES96_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;

/**
 * 
 * @ClassName: BINBEMQMES99_BL 
 * @Description: TODO(纯JSON格式MQ消息接收处理相关共通BL) 
 * @author menghao
 * @version v1.0.0 2016-1-7 
 *
 */
public class BINBEMQMES96_BL {
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binBEMQMES96_Service")
	private BINBEMQMES96_Service binBEMQMES96_Service;
	
	@Resource(name="binOLCM18_Service")
	private BINOLCM18_Service binOLCM18_Service;
	
	/**
	 * 查询部门信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDepartInfo(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("departCode", map.get("departCode"));
        paramMap.put("brandInfoID", map.get("brandInfoID"));
        paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		return binBEMQMES96_Service.getDepartInfo(paramMap);
	}
	
	/**
	 * 查询员工信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("employeeCode", map.get("employeeCode"));
        paramMap.put("brandInfoID", map.get("brandInfoID"));
        paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		return binBEMQMES96_Service.getEmployeeInfo(paramMap);
	}
	
	@Cacheable(value="CherryIvtCache")
	public List<Map<String, Object>> getDepotsByDepartID(String organizationID) {
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put(CherryConstants.ORGANIZATIONID, organizationID);
    	return binOLCM18_Service.getDepotsByDepartID(praMap);
	}
	
	/**
	 * 查询产品或者促销品相关信息
	 * 注：找到code对应的ID设置到明细中
	 * @param detailDataMap： 明细数据
	 * @param map：用于将错误信息写入mongondb（即调用共通需要的参数）
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setDetailProPrmIDInfo(Map<String, Object> detailDataMap, Map<String, Object> map) throws Exception {
		
		// 明细类型，N：产品；P:促销品；Y：支付方式；
		String detailType = ConvertUtil.getString(detailDataMap.get("productType")).toUpperCase();
		if("".equals(detailType)) {
			detailType = ConvertUtil.getString(detailDataMap.get("detailType")).toUpperCase();
		}
		
		boolean isSupportProductId = (MessageConstants.DETAILTYPE_PRODUCT
				.equals(detailType) || MessageConstants.DETAILTYPE_PROMOTION
				.equals(detailType));
		
		// 新增的厂商ID，此字段有值且系统配置项【是否支持产品下发、是否支持促销品下发】配置为【是】时以此id为准，需要联合unitcode、barcode进行产品的校验。
		String productId = ConvertUtil.getString(detailDataMap.get("productId"));
		
		// 设定部门ID
		detailDataMap.put("organizationID", map.get("organizationID"));
		// 设定时间【查询产品及促销品时有用】
		detailDataMap.put("tradeDateTime", map.get("tradeDateTime"));
		// 是否在新后台管理产品【即终端产品是从新后台下发的】
		boolean isOpenProductId = isSupportProductId && binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
		// 是否在新后台管理促销品【即终端促销品是新后台下发的】
		boolean isOpenPromotionId = isSupportProductId && binOLCM14_BL.isConfigOpen("1299", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
		// 标记是否走了新逻辑
		boolean isNewFlow = false;
		// 查询商品信息（产品或者促销品）
		Map<String, Object> resultMap = null;
		
		// productId有值且【产品】在新后台管理
		if(isOpenProductId && !"".equals(productId) && MessageConstants.DETAILTYPE_PRODUCT.equals(detailType)) {
			// 产品的情况，对unitcode,barcode,productId进行校验，若在产品条码关系表（即历史表）中则取到其厂商ID后返回
			resultMap = binBEMQMES99_Service.getProductInfoByIdAndCode(detailDataMap);
			isNewFlow = true;
			// 在历史表中若不存在
			if(resultMap == null || resultMap.isEmpty()) {
				 // 没有查询到相关商品信息，程序将终止并抛出异常
                MessageUtil.addMessageWarning(map, "产品厂商ID为\""+productId+"\"厂商编码为\""+detailDataMap.get("unitcode")+"\"产品条码为\""+detailDataMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
			}
		} 
		// productId有值且【促销品】在新后台管理
		if(isOpenPromotionId && !"".equals(productId) && MessageConstants.DETAILTYPE_PROMOTION.equals(detailType)) {
			// 促销品的情况，对unitcode,barcode,productId进行校验，若在促销品条码关系表（即历史表）中则取到其厂商ID后返回
			// 对于促销品需要额外地获取当前促销品的IsStock字段值。
			resultMap = binBEMQMES99_Service.getPrmPrtInfoByIdAndCode(detailDataMap);
			isNewFlow = true;
			if(resultMap == null || resultMap.isEmpty()) {
				// 没有查询到相关商品信息，程序将终止并抛出异常
                MessageUtil.addMessageWarning(map, "促销品厂商ID为\""+productId+"\"厂商编码为\""+detailDataMap.get("unitcode")+"\"促销品条码为\""+detailDataMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
			}
		} 
		
		// detailType值为空时(不知道productId代表的是产品还是促销品)，还是延用好的处理逻辑（使用code去检索）：
		if (!isNewFlow && ("".equals(detailType) || MessageConstants.DETAILTYPE_PROMOTION.equals(detailType))) {
			// 查询促销产品信息
			resultMap = binBEMQMES99_Service.selPrmProductInfo(detailDataMap);
			if (resultMap == null || resultMap.get("promotionProductVendorID") == null) {
				resultMap = binBEMQMES99_Service.selPrmProductPrtBarCodeInfo(detailDataMap);
				Map<String, Object> temp = new HashMap<String, Object>();

				if (resultMap != null) {// 促销品信息unitcode或barcode存在变更
					temp.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
					temp.put("organizationID", detailDataMap.get("organizationID"));
					
					resultMap = binBEMQMES99_Service.selPrmProductInfoByPrmVenID(temp);
					if (resultMap == null) {
						// 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
						List<Map<String, Object>> list = binBEMQMES99_Service.selPrmAgainByPrmVenID(temp);
						if (list != null && !list.isEmpty()) {
							resultMap = list.get(0);
						} else {
							// 查询促销产品信息 根据促销产品厂商ID，不区分有效状态
							list = binBEMQMES99_Service.selPrmByPrmVenID(temp);
							if (list != null && !list.isEmpty()) {
								resultMap = list.get(0);
							}
						}
					}
				} else {
					// 在促销产品条码对应关系表里找不到，放开时间条件再找一次，还是找不到的接下来查产品
					List<Map<String, Object>> prmPrtBarCodeList = binBEMQMES99_Service
							.selPrmPrtBarCodeList(detailDataMap);
					if (null != prmPrtBarCodeList && prmPrtBarCodeList.size() > 0) {
						// 取tradeDateTime与StartTime最接近的第一条
						temp.put("promotionProductVendorID", prmPrtBarCodeList.get(0).get("promotionProductVendorID"));
						// 查询促销产品信息 根据促销产品厂商ID，不区分有效状态
						List<Map<String, Object>> list = binBEMQMES99_Service.selPrmByPrmVenID(temp);
						if (list != null && !list.isEmpty()) {
							resultMap = list.get(0);
						}
					}
				}
				// 明细里detailType为促销品时，resultMap为null抛错
				if ((null == resultMap || resultMap.get("promotionProductVendorID") == null)
						&& MessageConstants.DETAILTYPE_PROMOTION.equals(detailType)) {
					// 没有查询到相关商品信息
					MessageUtil.addMessageWarning(map,"厂商编码为\"" + detailDataMap.get("unitcode")
									+ "\"促销品条码为\""+ detailDataMap.get("barcode")+ "\"" + MessageConstants.MSG_ERROR_09);
				}
			}
		}
		// 查询参数
		detailDataMap.put("organizationInfoID", map.get("organizationInfoID"));
		detailDataMap.put("brandInfoID", map.get("brandInfoID"));
		
		if (resultMap == null || resultMap.get("promotionProductVendorID") == null){
			// 在产品条码对应表中未找到产品信息，用code查询产品信息
			if(resultMap == null || "".equals(ConvertUtil.getString(resultMap.get("productVendorID")))) {
				// 不再过滤无效的产品
				resultMap = binBEMQMES99_Service.selProductInfo(detailDataMap);
			}
			
			// 若没有找到则再去查询产品条码对应关系表中的产品数据
			if (resultMap == null || "".equals(ConvertUtil.getString(resultMap.get("productVendorID")))){
				// 查找对应的产品条码对应关系表【业务时间在起止时间内】
				resultMap = binBEMQMES99_Service.selPrtBarCode(detailDataMap);
				Map<String,Object> temp = new HashMap<String,Object>();
				
				if(resultMap != null){
					temp.put("productVendorID", resultMap.get("productVendorID"));
					// 根据产品条码，继续查找产品表【确定此产品必须是有效的】
					resultMap = binBEMQMES99_Service.selProductInfoByPrtVenID(temp);
					// 若此产品为非有效，需要再次查询
					if(resultMap == null){
						 // 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
						 List<Map<String, Object>> list = binBEMQMES99_Service.selProAgainByPrtVenID(temp);
						 if(list!=null&&!list.isEmpty()){
							 resultMap = list.get(0);
						 }else{
						     resultMap = new HashMap<String, Object>();
						     resultMap.put("productVendorID", temp.get("productVendorID"));
						 }
					}
				}
                if(resultMap==null){
                    // 在产品条码对应关系表里找不到，放开时间条件再找一次，找不到抛错
                    List<Map<String,Object>> prtBarCodeList = binBEMQMES99_Service.selPrtBarCodeList(detailDataMap);
                    if(null != prtBarCodeList && prtBarCodeList.size()>0){
                        //取tradeDateTime与StartTime最接近的第一条
                    	resultMap = new HashMap<String, Object>();
                        resultMap.put("productVendorID", prtBarCodeList.get(0).get("productVendorID"));
                    }else{
                        // 没有查询到相关商品信息
                        MessageUtil.addMessageWarning(map, "厂商编码为\""+detailDataMap.get("unitcode")+"\"产品条码为\""+detailDataMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
                    }
                }
			}
			// 该商品为产品, 设定产品厂商ID
			detailDataMap.put("isPromotionFlag", "0");
			detailDataMap.put("productVendorID",resultMap.get("productVendorID"));
		} else if (resultMap != null && resultMap.get("promotionProductVendorID") != null){
			// 该商品为促销品, 促销产品厂商ID
			detailDataMap.put("isPromotionFlag", "1");
			detailDataMap.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
		} else {
			// 没有查询到相关商品信息
			MessageUtil.addMessageWarning(map, "厂商编码为\""+detailDataMap.get("unitcode")+"\"产品条码为\""+detailDataMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
		}
	}
	
	/**
	 * 取得新后台自定义单据号
	 * @param cherry_tradeType
	 */
	public void getMQTicketNumber(Map<String,Object> map,String cherry_tradeType){
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		if (cherry_tradeType == null || "".equals(cherry_tradeType)){
			cherry_tradeType = (String)map.get("tradeType");
			// 新后台单据号采番
			String cherry_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES96_BL", cherry_tradeType);
			map.put("cherry_no", cherry_no);
		}
//		else if (MessageConstants.BUSINESS_TYPE_GR.equals(cherry_tradeType)){
//			String cherry_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES99", cherry_tradeType);
//			map.put("stockInOut_tradeNo", cherry_no);
//			map.put("stockInOut_tradeNoIF", cherry_no);
//		}
	}
}
