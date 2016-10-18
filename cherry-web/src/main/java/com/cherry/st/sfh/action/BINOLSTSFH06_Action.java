/*	
 * @(#)BINOLSSPRM17_Action.java     1.0 2010/10/29		
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
package com.cherry.st.sfh.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.interfaces.BINOLPTRPS39_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.sfh.form.BINOLSTSFH06_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品发货
 * @author zhanghuyi
 *
 */
public class BINOLSTSFH06_Action extends BaseAction implements ModelDriven<BINOLSTSFH06_Form>{
	
	private static final long serialVersionUID = 1L;
	
    /**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH06_Action.class);
	
	private BINOLSTSFH06_Form form = new BINOLSTSFH06_Form();
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_BL bINOLCM20_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_IF binOLCM20_BL;

	@Resource(name="binOLSTSFH06_BL")
	private BINOLSTSFH06_IF binOLSTSFH06_BL;
	
	@Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource(name="binOLPTRPS39_BL")
	private BINOLPTRPS39_IF binOLPTRPS39_IF; 
	
	
	//逻辑仓库list
	private List<Map<String,Object>> logicInventoryList = null;

	//逻辑仓库list
	private List<Map<String,Object>> productList = null;
	
	private String remInOrganizationId;
	
	
    /** 是否显示金蝶码 **/
    private String configVal;
	
	/**
     * 画面初始化
     * @return
	 * @throws Exception 
     */	
    public String init() throws Exception{
    	try {
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		//登录用户的所属品牌
		String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		//语言
		String language = userInfo.getLanguage();
        //所属部门
        Map<String,Object> map =  new HashMap<String,Object>();
        map.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        //如果所属部门属于无效，初始化时不显示
        Map<String,Object> departInfoMap = binolcm01BL.getDepartmentInfoByID(ConvertUtil.getString(userInfo.getBIN_OrganizationID()), null);
            if (null == departInfoMap || departInfoMap.isEmpty()
                    || ConvertUtil.getString(departInfoMap.get("ValidFlag")).equals(CherryConstants.VALIDFLAG_DISABLE)
                    || ConvertUtil.getString(departInfoMap.get("DepartType")).equals(CherryConstants.ORGANIZATION_TYPE_FOUR)) {
            form.setDepartInit("");
            form.setOrganizationId(0);
        }else{
            form.setDepartInit(binOLSTSFH06_BL.getDepart(map));
            form.setOrganizationId(userInfo.getBIN_OrganizationID());
        }
        
    	//调用共通获取逻辑仓库
        Map<String,Object> pram =  new HashMap<String,Object>();
        pram.put("BIN_BrandInfoID", brandInfoId);
//        pram.put("BusinessType", CherryConstants.OPERATE_SD);
        pram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_SD);
        pram.put("ProductType", "1");
        pram.put("language", language);
        pram.put("Type", "0");
//		logicInventoryList = binOLCM18_BL.getLogicDepotByBusinessType(pram);
		logicInventoryList = binOLCM18_BL.getLogicDepotByBusiness(pram);
		
        //取得系统配置项库存是否允许为负
        String configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
        form.setCheckStockFlag(configValue);
		
        //取得系统配置项发货画面按钮清理建议明细，删除数量小于多少的明细
        configValue = binOLCM14_BL.getConfigValue("1129", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "2";
        }
        form.setDelQuantityLT(configValue);
        
        //配置项 产品发货使用价格（销售价格/会员价格）
        configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        //配置项  实际执行价是否按成本价计算
        configValue = binOLCM14_BL.getConfigValue("1374", organizationInfoID, brandInfoId);
        form.setUseCostPrice(configValue);
        
        //配置项【是否开启金蝶码】0：关闭，1：开启
		configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		} catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                //return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                //return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
        return SUCCESS;
    }
    
    /**
     * 直接发货
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
		try {
		    if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
		    }
            if (!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			binolcm01BL.completeUserInfo(userInfo,form.getOutOrganizationId(),"BINOLSTSFN06");
			//针对于反向催单中的发货处理
			String reminderId = form.getReminderId();
			if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId)) {
				String temp = getRemInOrganizationId();//反向催单收货部门
				//发货单填写的收货部门
				String tempArr = form.getInOrganizationId();
				if(!temp.equals(tempArr)) {
					//收货部门有误！
					this.addActionError(getText("PSS00064"));
	                return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
			
			int billId = binOLSTSFH06_BL.tran_deliver(form, userInfo);
			if(billId==0){
				throw new CherryException("ISS00005");
			}else{
				if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId)) {
					Map<String, Object> reminderMap = new HashMap<String, Object>();
					reminderMap.put("cargoType", "N");
					reminderMap.put("billId", billId);
					reminderMap.put("reminderId" , reminderId);
					binOLPTRPS39_IF.tran_updateReminder(reminderMap, userInfo);
				}
				//语言
				String language = userInfo.getLanguage();
				//取得发货单概要信息 和详细信息
				Map<String,Object> mainMap = binOLSTCM03_BL.getProductDeliverMainData(billId,language);
				//申明一个Map用来存放要返回的ActionMessage
				Map<String,Object> messageMap = new HashMap<String,Object>();
				//是否要显示工作流程图标志：设置为true
				messageMap.put("ShowWorkFlow",true);
				//工作流ID
				messageMap.put("WorkFlowID", mainMap.get("WorkFlowID"));
				//消息：操作已成功！
				messageMap.put("MessageBody", getText("ICM00002"));
				//将messageMap转化成json格式字符串然后添加到ActionMessage中
				this.addActionMessage(JSONUtil.serialize(messageMap));
				//返回MESSAGE共通页
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		} catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
		}

    }
    
    /**
     * 保存发货单
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
    	try{
            if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        	binolcm01BL.completeUserInfo(userInfo,form.getOutOrganizationId(),"BINOLSTSFN06");
        	//针对于反向催单中的发货处理
			String reminderId = form.getReminderId();
			if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId)) {
				String temp = getRemInOrganizationId();//反向催单收货部门
				//发货单填写的收货部门
				String tempArr = form.getInOrganizationId();
				if(!temp.equals(tempArr)) {
					//收货部门有误！
					this.addActionError(getText("PSS00064"));
	                return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
        	int billId =  binOLSTSFH06_BL.tran_saveDeliver(form,userInfo);
        	//针对于反向催单中的发货处理
			if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId) && 0!=billId) {
				Map<String, Object> reminderMap = new HashMap<String, Object>();
				reminderMap.put("billId", billId);
				reminderMap.put("cargoType", "N");
				reminderMap.put("reminderId" , reminderId);
				binOLPTRPS39_IF.tran_updateReminder(reminderMap, userInfo);
			}
        	this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
    	}catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
    	}
    }
	/**
	 * 通过Ajax取得指定部门所拥有的仓库
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getDepotByAjax() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

		String organizationid = request.getParameter("organizationid");
		
		List resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
    
	/**
	 * 切换发货部门后，收货部门联动
	 * 通过Ajax取得指定部门所管辖的所有下级部门
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getConDepartByAjax() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

		String organizationid = request.getParameter("organizationid");

		List resultTreeList =  binolcm01BL.getManagerOrgByOrgPrivilege(userInfo.getBIN_UserID(),
    			Integer.parseInt(organizationid),userInfo.getLanguage(),
    			CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,"1"
    			);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	} 
	/**
	 * 取得库存数量
	 * @throws Exception 
	 */
	public void getPrtVenIdAndStock() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		//实体仓库
		map.put("BIN_DepotInfoID", form.getOutDepotId());
		String outLoginDepotId = form.getOutLoginDepotId();
		if(null == outLoginDepotId || "".equals(outLoginDepotId)){
			outLoginDepotId = "0";
		}
		//逻辑仓库
		map.put("BIN_LogicInventoryInfoID", outLoginDepotId);
		//是否取冻结 =2 扣库存
		map.put("FrozenFlag", "2");
		//库存锁定阶段-制单
		map.put("LockSection", "0");
		//产品厂商ID
		String[] prtIdArr = form.getProductVendorIDArr();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(null != prtIdArr){
			for(String prtId : prtIdArr){
				map.put("BIN_ProductVendorID", prtId);
				int stock = bINOLCM20_BL.getProductStock(map);
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put(ProductConstants.PRT_VENDORID, prtId);
				temp.put("stock", stock);
				list.add(temp);
			}
		}
		ConvertUtil.setResponseByAjax(response, list);
		
	}
	
	/**
	 * 取产品在收货方的库存量
	 * @throws Exception
	 */
	public void getReceivePrtStock() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 收货方部门
		map.put("BIN_OrganizationID", form.getInOrganizationId());
		// 是否取冻结
		map.put("FrozenFlag", "1");
		//产品厂商ID
		String[] prtIdArr = form.getProductVendorIDArr();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(null != prtIdArr){
			for(String prtId : prtIdArr){
				map.put("BIN_ProductVendorID", prtId);
				int stock = bINOLCM20_BL.getDepartProductStock(map);
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put(ProductConstants.PRT_VENDORID, prtId);
				temp.put("stock", stock);
				list.add(temp);
			}
		}
		ConvertUtil.setResponseByAjax(response, list);
	}
    /**
     * 根据指定的部门ID和语言信息取得仓库信息
     * @param organizationID
     * @param language
     * @return
     */
    private List<Map<String, Object>> getDepotList(String organizationID,String language){    	
    	List<Map<String, Object>> ret = binolcm01BL.getDepotList(organizationID, language);
    	return ret;
    }
	/**
	 * <p>
	 * 查询出所有有效产品
	 * </p>
	 * 
	 * @return
	 */
	public String addBlankBill() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 取得产品List
		productList = binOLSTSFH06_BL.searchProductList(map);
		// 实体仓库
		map.put("BIN_DepotInfoID", form.getOutDepotId());
		String outLoginDepotId = form.getOutLoginDepotId();
		if(null == outLoginDepotId || "".equals(outLoginDepotId)){
			outLoginDepotId = "0";
		}
		//配置项【是否开启金蝶码】0：关闭，1：开启
		configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		//逻辑仓库
		map.put("BIN_LogicInventoryInfoID", outLoginDepotId);
		// 收货方部门ID
		map.put("BIN_OrganizationID", form.getInOrganizationId());
		// 是否取冻结
		map.put("FrozenFlag", '1');
		
		if(null != productList){
			for(Map<String,Object> proMap : productList){
				map.put("BIN_ProductVendorID", proMap.get("BIN_ProductVendorID"));
				int stockQuantity = bINOLCM20_BL.getProductStock(map);
				/***获取收货方部门的库存数量**/
				int receiveStockQuantity = binOLCM20_BL.getDepartProductStock(map);
				proMap.put("StockQuantity", stockQuantity);
				proMap.put("receiveStockQuantity", receiveStockQuantity);
			}
			
			//得到明细对应的平均成本和总成本
			for(Map<String, Object> detailsMap : productList){
	        	detailsMap.put("sort", "asc");
	        	detailsMap.put("depotInfoId", form.getOutDepotId());
	        	detailsMap.put("BIN_LogicInventoryInfoID",outLoginDepotId);
	        	detailsMap.put("productVendorId", detailsMap.get("BIN_ProductVendorID"));
	        	detailsMap.put("quantity", detailsMap.get("Quantity"));
	            
	        	 DecimalFormat df=new DecimalFormat("0.00");
	        	 
	        	List<Map<String,Object>> proNewBatchStockList =binOLSTSFH06_BL.getProNewBatchStockList(detailsMap);
	        	if(CherryUtil.isBlankList(proNewBatchStockList)){
	        		detailsMap.put("costPrice", "");
	        		detailsMap.put("totalCostPrice", "");
	        	}else{
	        		int quantity=ConvertUtil.getInt(detailsMap.get("quantity"));//发货单明细数量
					int tempQuantity=0;//总数量				
					double totalCostPrice = 0.00;//总成本价
					
					for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
						int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
						double costPrice = ConvertUtil.getDouble(proNewBatchStock.get("CostPrice"));//成本价
						
						if(amount<quantity){//表示库存不够扣减
							totalCostPrice+=amount*costPrice;
							tempQuantity+=amount;
							quantity=quantity-amount;
						}
						else{//表示库存不够扣减此时停止循环
							totalCostPrice+=quantity*costPrice;
							tempQuantity+=quantity;
							break;
						}
					}	
					if(quantity == 0){
						detailsMap.put("costPrice", "");
						detailsMap.put("totalCostPrice", "");
					}else{					
						detailsMap.put("costPrice", df.format(totalCostPrice/tempQuantity));//默认四舍五入
						detailsMap.put("totalCostPrice", totalCostPrice);
					}
	        	}
	        }
		}
		form.setProductList(productList);
		
        //配置项 产品发货使用价格（销售价格/会员价格）
        String configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
		
      //配置项  实际执行价是否按成本价计算
        configValue = binOLCM14_BL.getConfigValue("1374", organizationInfoID, brandInfoId);
        form.setUseCostPrice(configValue);
        
		return SUCCESS;
	}
	
	/**
     * <p>
     * 取建议发货单
     * </p>
     * 
     * @return
     */
    public String searchSuggestBill() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoID);
        //所属品牌
        map.put(CherryConstants.BRANDINFOID, brandInfoId);
        //收货方
        map.put("InOrganizationID", form.getInOrganizationId());
        // 取得产品List
        productList = binOLSTSFH06_BL.searchSuggestProductList(map);
        // 实体仓库
        map.put("BIN_DepotInfoID", form.getOutDepotId());
        String outLoginDepotId = form.getOutLoginDepotId();
        if(null == outLoginDepotId || "".equals(outLoginDepotId)){
            outLoginDepotId = "0";
        }
        //配置项【是否开启金蝶码】0：关闭，1：开启
		configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        //逻辑仓库
        map.put("BIN_LogicInventoryInfoID", outLoginDepotId);
        // 是否取冻结
        map.put("FrozenFlag", '1');
        // 收货方部门ID【用于查询出收货方部门的库存】
     	map.put("BIN_OrganizationID", form.getInOrganizationId());
        
        if(null != productList){
            for(Map<String,Object> proMap : productList){
                map.put("BIN_ProductVendorID", proMap.get("BIN_ProductVendorID"));
                int stockQuantity = bINOLCM20_BL.getProductStock(map);
                /***获取收货方部门的库存数量**/
				int receiveStockQuantity = binOLCM20_BL.getDepartProductStock(map);
                proMap.put("StockQuantity", stockQuantity);
                proMap.put("receiveStockQuantity", receiveStockQuantity);
            }
        }
        form.setProductList(productList);
        
        //配置项 产品发货使用价格（销售价格/会员价格）
        String configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        return SUCCESS;
    }
	
    /**
     * 根据系统配置项是否需要验证库存大于发货数量
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateStock() {
        boolean isCorrect = true;
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getOutDepotId()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getOutLoginDepotId()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "1");//产品
            paramMap.put("IDArr", form.getProductVendorIDArr());
            paramMap.put("QuantityArr", form.getQuantityuArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
                this.addActionError(getText("EST00034"));
                return isCorrect;
            }
        }

        return isCorrect;
    }
    
    
    /**
	 * 根据实体仓库ID，逻辑仓库ID，产品厂商ID，数量 ,排序方式  查询对应的平均成本价
	 * 
	 * @param map
	 * @return
	 */
	public void getCostPrice() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("depotInfoId", form.getOutDepotId());//实体仓库ID
		map.put("BIN_LogicInventoryInfoID", form.getOutLoginDepotId());//逻辑仓库ID
		map.put("productVendorId", form.getProductVendorId());//产品厂商ID
		map.put("quantity", form.getPrtCount());//数量 
		map.put("sort", "asc");//排序方式
		List<Map<String,Object>> proNewBatchStockList = binOLSTSFH06_BL.getProNewBatchStockList(map);
		
		if(CherryUtil.isBlankList(proNewBatchStockList)){
			map.put("costPrice", "");
			map.put("totalCostPrice", "");
		}else {
			int quantity=ConvertUtil.getInt(form.getPrtCount());//发货单明细数量
			int tempQuantity=0;//总数量				
			Double totalCostPrice = 0.00;//总成本价
			
			for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
				int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
				Double costPrice = ConvertUtil.getDouble(proNewBatchStock.get("CostPrice"));//成本价
				
				if(amount<quantity){//表示库存不够扣减
					totalCostPrice+=amount*costPrice;
					tempQuantity+=amount;
					quantity=quantity-amount;
				}
				else{//表示库存不够扣减此时停止循环
					totalCostPrice+=quantity*costPrice;
					tempQuantity+=quantity;
					break;
				}
			}
			
			if(quantity==0){	
				map.put("costPrice", "");
				map.put("totalCostPrice", "");
			}else{
				map.put("costPrice",totalCostPrice/tempQuantity);
				map.put("totalCostPrice", totalCostPrice);
			}
		}
		ConvertUtil.setResponseByAjax(response, map);
	}
	
	
	@Override
	public BINOLSTSFH06_Form getModel() {
		return form;
	}

	/** 产品信息List  */
	private List<Map<String, Object>> prtInfoList;

	public List<Map<String, Object>> getPrtInfoList() {
		return prtInfoList;
	}

	public void setPrtInfoList(List<Map<String, Object>> prtInfoList) {
		this.prtInfoList = prtInfoList;
	}

	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}
	
	private boolean validateForm(){
	    boolean isCorrect = true;
	    String planArriveDate = ConvertUtil.getString(form.getPlanArriveDate());
	    if(!planArriveDate.equals("")){
	        if(!CherryChecker.checkDate(planArriveDate)){
	               this.addActionError(getText("ECM00008", new String[]{getText("PST00036")}));
	                isCorrect = false;
	        }
	    }
	    return isCorrect;
	}

	public String getRemInOrganizationId() {
		return remInOrganizationId;
	}

	public void setRemInOrganizationId(String remInOrganizationId) {
		this.remInOrganizationId = remInOrganizationId;
	}

	public String getConfigVal() {
		return configVal;
	}

	public void setConfigVal(String configVal) {
		this.configVal = configVal;
	}
	
}