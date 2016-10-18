/*
 * @(#)BINOLCM05_BL.java     1.0 2010/11/19
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

package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.PromotionConstants;

/**
 * 促销品共通 BL
 * 
 * @author hub
 * @version 1.0 2010.11.19
 */
@SuppressWarnings("unchecked")
public class BINOLCM05_BL {
	
	@Resource
	private BINOLCM05_Service binOLCM05_Service;
	
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * 取得系统时间(年月日)
	 * 
	 * 
	 * @return String
	 *			系统时间(年月日)
	 */
	public String getDateYMD() {
		return binOLCM05_Service.getDateYMD();
	}
	
	/**
	 * 取得所管辖的品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			所管辖的品牌List
	 */
	public List getBrandInfoList(Map<String, Object> map) {
		return binOLCM05_Service.getBrandInfoList(map);
	}
	
	/**
	 * 取得品牌List
	 * 
	 * @param userInfo 查询条件
	 * @return 品牌List
	 */
	public List<Map<String, Object>> getBrandInfoList(UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		return binOLCM05_Service.getBrandInfoShowList(map);
	}
	
	/**
	 * 取得品牌List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBrandList(Map<String, Object> session) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 登陆用户不为总部员工
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			brandMap.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
			list.add(brandMap);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户组织Id
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 取得所属品牌List
			list = getBrandInfoList(map);
		}
		return list;
	}
	
	/**
	 * 取得品牌名称
	 * 
	 * @param map 
	 * 				查询条件
	 * @return String
	 * 				品牌名称
	 */
	public String getBrandName(Map<String, Object> map) {
		return binOLCM05_Service.getBrandName(map);
	}
	
	/**
	 * 取得会员俱乐部名称
	 * 
	 * @param map 
	 * 				查询条件
	 * @return String
	 * 				会员俱乐部名称
	 */
	public String getClubName(Map<String, Object> map) {
		return binOLCM05_Service.getClubName(map);
	}
	
	/**
	 * 取得品牌代码
	 * 
	 * @param map 
	 *
	 * @return String
	 * 
	 */
	public String getBrandCode(int brandInfoId) {
		return binOLCM05_Service.getBrandCode(brandInfoId);
	}
	
	/**
	 * 取得品牌末位码
	 * 
	 * @param map 
	 *
	 * @return String
	 * 
	 */
	public String getBrandLastCode(int brandInfoId) {
		return binOLCM05_Service.getBrandLastCode(brandInfoId);
	}
	
	/**
	 * 根据品牌Code和组织ID取得品牌ID
	 * 
	 * 
	 * */
	public int getBrandInfoId(Map<String,Object> map){
		return binOLCM05_Service.getBrandInfoId(map);
	}
	
	/**
	 * 取得促销产品类型List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			促销产品类型List
	 */
	public List getPromPrtCateList(Map<String, Object> map) {
		return binOLCM05_Service.getPromPrtCateList(map);
	}
	
	/**
	 * 取得产品类型List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			产品类型List
	 */
	public List getPrtCateList(Map<String, Object> map) {
		
		return binOLCM05_Service.getPrtCateList(map);
	}
	
	/**
	 * 取得大分类List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			大分类List
	 */
	public List getPrimaryCateList(Map<String, Object> map) {
		return binOLCM05_Service.getPrimaryCateList(map);
	}
	
	/**
	 * 取得中分类List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			中分类List
	 */
	public List getSecondCateList(Map<String, Object> map) {
		return binOLCM05_Service.getSecondCateList(map);
	}
	
	/**
	 * 取得小分类List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			小分类List
	 */
	public List getSmallCateList(Map<String, Object> map) {
		return binOLCM05_Service.getSmallCateList(map);
	}
	
	/**
	 * 取得最小包装类型List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			最小包装类型List
	 */
	public List getMinPackageTypeList(Map<String, Object> map) {
		return binOLCM05_Service.getMinPackageTypeList(map);
	}
	
	/**
	 * 取得默认显示的生产厂商信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			默认显示的生产厂商信息
	 */
	public Map getFactoryInfo(Map<String, Object> map) {
		return binOLCM05_Service.getFactoryInfo(map);
	}
	
	/**
	 * 验证是否存在同样的促销品分类ID
	 * 
	 * @param map 查询条件
	 * @return 促销品分类ID
	 */
	public boolean getPrmTypeIdCheck(Map<String, Object> map) {
		
		// 验证是否存在同样的促销品分类ID
		return binOLCM05_Service.getPrmTypeIdCheck(map);
	}
	
	/**
	 * 验证厂商编码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return TRUE 不存在   FALSE 存在
	 */
	public boolean checkUnitCode(Map<String, Object> map) {
		// 厂商编码是否已经存在于促销品表
		boolean flag1 = binOLCM05_Service.checkUnitCode(map);
		// 厂商编码是否已经存在于产品表
		boolean flag2 = binOLCM05_Service.checkUnitCode2(map);
		return (flag1 && flag2);
	}
	
	/**
	 * 验证厂商编码促销品条码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return TRUE 不存在   FALSE 存在
	 */
	public boolean checkUnitCodeBarCode(Map<String, Object> map) {
		
		// 验证厂商编码促销品条码是否已经存在
		return binOLCM05_Service.checkUnitCodeBarCode(map);
	}
	
	/**
	 * 取得产品ID(根据barcode)
	 * @param map
	 * @return
	 */
	public List<Integer> getProductIdByBarCode(Map<String, Object> map){
		return binOLCM05_Service.getProductIdByBarCode(map);
	}
	
	/**
	 * 取得促销产品ID(根据barcode)
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getPromotionIdByBarCode(Map<String, Object> map){
		return binOLCM05_Service.getPromotionIdByBarCode(map);
	}
	
	/**
	 * 取得产品信息(根据barcode)
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductListByBarCode(Map<String, Object> map){
		return binOLCM05_Service.getProductListByBarCode(map);
	}
	
	/**
	 * 取得产品云及产品方案中的产品信息(根据barcode)
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductListByBarCodeForSolu(Map<String, Object> map){
		return binOLCM05_Service.getProductListByBarCodeForSolu(map);
	}
	
	/**
	 * 促销品/产品是否有效
	 * 
	 * @param brandInfoId
	 * @param unitCode
	 * @param barCode
	 * @return
	 */
	public boolean isValid(String brandInfoId, String unitCode,String barCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put(CherryConstants.UNITCODE, unitCode);
		map.put(CherryConstants.BARCODE,barCode);
		int count = binOLCM05_Service.getValidCount(map);
		return count== 0 ? false : true;
	}
	
	/**
	 * 根据unitCode查询产品/促销品的当前及历史编码条码关系表中是否已存在相同unitCode
	 * 
	 * @param map
	 * @return List
	 */
	public int getExistUnitCodeForPrtAndProm(Map<String, Object> map) {
		return binOLCM05_Service.getExistUnitCodeForPrtAndProm(map);
	}
	
	/**
	 * 取得会员俱乐部列表
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员俱乐部列表
	 */
	public List<Map<String, Object>> getClubList(Map<String, Object> map) {
		return binOLCM05_Service.getClubList(map);
	}
	
	/**
	 * 取得会员已经拥有的俱乐部列表
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员已经拥有的俱乐部列表
	 */
	public List<Map<String, Object>> getMemClubList(Map<String, Object> map) {
		return binOLCM05_Service.getMemClubList(map);
	}
	
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @param prmCate
	 */
	@CacheEvict(value="CherryPromotionCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> getPrmInfo(Map<String, Object> map, int orgId, int brandId, String prmCate) throws CherryException{
		
		// 取得促销品当前表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		seqMap.put(CherryConstants.BRANDINFOID, brandId);
		seqMap.put("type", "H");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		Map<String, Object> prmInfo = new HashMap<String, Object>(map);
		if(CherryChecker.isNullOrEmpty(prmCate)){// 加价购
			prmInfo.put(CherryConstants.UNITCODE, PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
			prmInfo.put(CherryConstants.BARCODE, PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
			
			Map<String, Object> sel = new HashMap<String, Object>();
			sel.put(CherryConstants.BRANDINFOID, brandId);
			sel.put(CherryConstants.BARCODE, PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
			
			String prmVendorId = binOLCM15_BL.getVirtualPrmVendorId(sel);
			
			if("".equals(ConvertUtil.getString(prmVendorId))){
				map.put(CherryConstants.ORGANIZATIONINFOID, orgId);
				map.put(CherryConstants.BRANDINFOID, brandId);
				insertPrm(map);
				prmVendorId = ConvertUtil.getString(map.get("prmVendorId"));
			}
			
			prmInfo.put("prmVendorId", prmVendorId);
			
		}else{
			map.put(CherryConstants.UPDATEDBY, "BINOLCM15");
			map.put(CherryConstants.CREATEDBY, "BINOLCM15");
			map.put(CherryConstants.UPDATEPGM, "BINOLCM15");
			map.put(CherryConstants.CREATEPGM, "BINOLCM15");
			map.put(PromotionConstants.PRMCATE, prmCate);
			// 取得虚拟促销品厂商ID
			int prmVendorId = ConvertUtil.getInt(map.get(PromotionConstants.PRMVENDORID));
			if(prmVendorId == 0){// 添加
				String code = ConvertUtil.getString(map.get(CherryConstants.BARCODE));
				if("".equals(code)){
					Map<String,Object> params = new HashMap<String, Object>();
					params.put(PromotionConstants.PRMCATE, prmCate);
					int count = 0;
					do{
						if(PromotionConstants.PROMOTION_TZZK_TYPE_CODE.equals(prmCate)){// 套装折扣
							code = binOLCM15_BL.getSequenceId(orgId,brandId,"4");
						}else if(PromotionConstants.PROMOTION_DHCP_TYPE_CODE.equals(prmCate)){// 积分兑换
							code = binOLCM15_BL.getSequenceId(orgId,brandId,"5");
						}else if(PromotionConstants.PROMOTION_DHMY_TYPE_CODE.equals(prmCate)){// 积分兑现
							code = binOLCM15_BL.getSequenceId(orgId,brandId,"A");
						}
						params.put(CherryConstants.UNITCODE, code);
						params.put(CherryConstants.BARCODE, code);
						count = binOLCM15_BL.getPrmCount(params, brandId);
					}while(count != 0);
				}
				if(!"".equals(code)){
					map.put(CherryConstants.UNITCODE, code);
					map.put(CherryConstants.BARCODE, code);
					// 添加促销品
					prmVendorId = binOLCM15_BL.addPrmInfo(map, orgId, brandId);
					prmInfo.put(PromotionConstants.PRMVENDORID, prmVendorId);
					prmInfo.put(CherryConstants.UNITCODE, code);
					prmInfo.put(CherryConstants.BARCODE, code);
				}else{
					throw new CherryException(CherryConstants.UNITCODE + " and " + CherryConstants.BARCODE + "is null");
				}
				
				
			}else{// 更新
				binOLCM15_BL.updPrmInfo(map, prmVendorId);
				prmInfo.put(PromotionConstants.PRMVENDORID, prmVendorId);
				prmInfo.put(CherryConstants.UNITCODE, map.get(CherryConstants.BARCODE));
				prmInfo.put(CherryConstants.BARCODE, map.get(CherryConstants.BARCODE));
			}
		}
		prmInfo.put("saleType", "P");
		return prmInfo;
	}
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @param prmCate
	 */
	public Map<String, Object> getPrmInfo(Map<String, Object> map,String prmCate) throws CherryException{
		int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		return getPrmInfo(map,orgId,brandId,prmCate);
	}
	
	/**
	 * 取得虚拟促销品(运费)
	 * @param map
	 * @param prmCate
	 */
	public Map<String, Object> getPrmInfoYF(int orgId, int brandId) throws CherryException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		map.put(CherryConstants.BRANDINFOID, brandId);
		map.put(CherryConstants.UNITCODE, "YF000000");
		map.put(CherryConstants.BARCODE, "YF000000");
		map.put(PromotionConstants.PRMCATE, "YF");
		map.put("nameTotal", "运费");
		map.put("exPoint", 1);
		map.put("saleType", "P");
		Integer prmVendorId = 0;
		Map<String, Object> prmInfo = new HashMap<String, Object>(map);
		prmVendorId = binOLCM15_BL.getPrmVendorId(map);
		if(null == prmVendorId || 0== prmVendorId){
			// 取得促销品当前表版本号
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
			seqMap.put(CherryConstants.BRANDINFOID, brandId);
			seqMap.put("type", "H");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			
			map.put("tVersion", tVersion);
			map.put(CherryConstants.UPDATEDBY, "BINOLCM15");
			map.put(CherryConstants.CREATEDBY, "BINOLCM15");
			map.put(CherryConstants.UPDATEPGM, "BINOLCM15");
			map.put(CherryConstants.CREATEPGM, "BINOLCM15");
			// 添加促销品
			prmVendorId = binOLCM15_BL.addPrmInfo(map, orgId, brandId);
		}
		prmInfo.put(PromotionConstants.PRMVENDORID, prmVendorId);
		return prmInfo;
	}
	
	/**
	 * 新增促销品TZZK
	 * @param virtualMap
	 * @param paramMap
	 */
	private void insertPrm(Map<String, Object> paramMap){
		Map<String, Object> prmMap = new HashMap<String,Object>();
		
		prmMap.put(CherryConstants.UNITCODE, PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		prmMap.put(CherryConstants.BARCODE, PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		prmMap.put("nameTotal", "套装折扣");
		prmMap.put("promCate", "TZZK");
		
		// 作成者
		prmMap.put(CherryConstants.CREATEDBY, "BINOLCM05");
		// 作成程序名
		prmMap.put(CherryConstants.CREATEPGM, "BINOLCM05");
		// 更新者
		prmMap.put(CherryConstants.UPDATEDBY, "BINOLCM05");
		// 更新程序名
		prmMap.put(CherryConstants.UPDATEPGM, "BINOLCM05");
		
		prmMap.put(CherryConstants.ORGANIZATIONINFOID, paramMap.get("organizationInfoId"));
		prmMap.put(CherryConstants.BRANDINFOID, paramMap.get("brandInfoId"));
		prmMap.put("isStock", "0"); // 是否管理库存
		
		prmMap.put("tVersion", paramMap.get("tVersion")); // 促销品表版本号
		
		int prmId = binOLCM15_BL.insertPromotionProductBackId(prmMap);
		
		prmMap.put("promProductId", prmId);  // 促销品ID
		prmMap.put("manuFactId", 1); // 生产厂商ID
		
		int prmVendorId = binOLCM15_BL.insertPromProductVendor(prmMap);
		paramMap.put("prmVendorId", prmVendorId);
	}
}
