package com.cherry.wp.wr.krp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.interfaces.BINOLPTRPS11_IF;
import com.cherry.wp.wr.common.service.BINOLWRCOM01_Service;
import com.cherry.wp.wr.krp.form.BINOLWRKRP02_Form;
import com.cherry.wp.wr.krp.service.BINOLWRKRP99_Service;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 进销存查询Action
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWRKRP02_Action extends BaseAction implements ModelDriven<BINOLWRKRP02_Form> {

	private static final long serialVersionUID = 3617992424009407071L;
	
	@Resource
	private BINOLWRCOM01_Service binOLWRCOM01_Service;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="BINOLPTRPS11_IF")
	private BINOLPTRPS11_IF binolptrps11BL;
	
	@Resource
	private BINOLWRKRP99_Service binOLWRKRP99_Service;
	
	/**
	 * 进销存查询画面初始化
	 * 
	 * @return 进销存查询画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		logicInventoryList = binOLWRCOM01_Service.getLogicInventoryList(map);
		
		List<Map<String, Object>> categoryList = binOLWRCOM01_Service.getCategoryList(map);
		classList = new ArrayList<Map<String,Object>>();
		List<String[]> keyList = new ArrayList<String[]>();
		String[] key1 = {"bigClassId", "bigClassCode", "bigClassName"};
		keyList.add(key1);
		ConvertUtil.convertList2DeepList(categoryList,classList,keyList,0);
		
		String type = binOLCM14_BL.getConfigValue("1095",
				userInfo.getBIN_OrganizationInfoID()+"",
				userInfo.getBIN_BrandInfoID()+"");
		form.setType(type);
		
		Map<String, Object> couInfo = binOLWRKRP99_Service.getCouInfoByCouId(map);
		if(couInfo != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("businessType", "1");
			params.put("operationType", "1");
			params.put("departId", counterInfo.getOrganizationId());
			Integer counterKind = (Integer)couInfo.get("counterKind");
			if(counterKind != null && counterKind == 1) {
				params.put("testType", counterKind);
			}
			form.setParams(JSONUtil.serialize(params));
		}
		
		String sysDate = binOLWRCOM01_Service.getDateYMD();
		// 开始日期
		form.setStartDate(binOLWRCOM01_Service.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), 
				DateUtil.coverString2Date(sysDate, DateUtil.DATE_PATTERN)));
		// 截止日期
		form.setEndDate(sysDate);
		
		return SUCCESS;
	}
	
	/**
	 * 进销存查询
	 * 
	 * @return 进销存查询画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> searchMap = getSearchMap();
		
		// 取得汇总信息
		sumInfo = binolptrps11BL.getSumInfo(searchMap);
		// 取得库存记录总数
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 取得库存记录List
			proStockList = binolptrps11BL.getProStockList(searchMap);
		}
		return SUCCESS;
	}
	
	/**
	 * 取得查询参数MAP
	 * 
	 * @return 查询参数MAP
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 库存相关的日期参数设置到paramMap中
		binOLCM00_BL.setParamsMap(map, orgInfoId, form.getStartDate(), 
				form.getEndDate(),"Pro");
		// form中dataTable相关参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 库存统计方式
		map.put("type",form.getType());
		// 产品有效状态
		map.put(CherryConstants.VALID_FLAG,form.getValidFlag());
		// 大分类ID
		map.put("catePropValId", form.getCatePropValId());
		// 逻辑仓库ID
		map.put("lgcInventoryId", form.getLgcInventoryId());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		// 产品ID
		map.put(ProductConstants.PRODUCTID, form.getProductId());
		
		// 共通条参数
		Map<String, Object> paramsMap = (Map<String, Object>)JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
        // 业务日期
        String businessDate = binOLWRCOM01_Service.getBussinessDate(map);
        map.put("businessDate",businessDate);
        
		return map;
	}
	
	/** 产品分类List */
	private List<Map<String, Object>> classList;
	
	/** 逻辑仓库List */
	private List<Map<String, Object>> logicInventoryList;
	
	/** 库存记录List */
	private List<Map<String, Object>> proStockList;
	
	/** 汇总信息 */
	private Map sumInfo;
	
	public List<Map<String, Object>> getClassList() {
		return classList;
	}

	public void setClassList(List<Map<String, Object>> classList) {
		this.classList = classList;
	}

	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}

	public List<Map<String, Object>> getProStockList() {
		return proStockList;
	}

	public void setProStockList(List<Map<String, Object>> proStockList) {
		this.proStockList = proStockList;
	}

	public Map getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map sumInfo) {
		this.sumInfo = sumInfo;
	}

	/** 库存报表Form **/
	private BINOLWRKRP02_Form form = new BINOLWRKRP02_Form();

	@Override
	public BINOLWRKRP02_Form getModel() {
		return form;
	}

}
