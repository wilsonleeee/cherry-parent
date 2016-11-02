/*	
 * @(#)CampBaseAction.java     1.0 2011/7/18		
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
package com.cherry.cp.common.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.util.CampUtil;

/**
 * 会员活动基础  Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class CampBaseAction extends BaseAction{

	private static final long serialVersionUID = -7296079886462676511L;
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * <p>
	 * 打印错误信息
	 * </p>
	 * 
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 */
	public void printErrMsg(Map<String, Object> map) {
		// 错误信息集合
		List<ActionErrorDTO> actionErrorList = (List<ActionErrorDTO>) map.get("actionErrorList");
		if (null != actionErrorList) {
			// 循环错误信息集合
			for (ActionErrorDTO actionErrorDTO : actionErrorList) {
				// 错误级别
				int errorLevel = actionErrorDTO.getErrorLevel();
				// 错误信息
				String errMsg = getText(actionErrorDTO.getErrMsgCode(), actionErrorDTO.getErrMsgParams());
				switch(errorLevel) {
					// 字段错误信息
					case CampUtil.ERROR_LEVEL_1:
					this.addFieldError(actionErrorDTO.getFieldName(), errMsg);
					break;
					// Action错误信息
					case CampUtil.ERROR_LEVEL_2:
					this.addActionError(errMsg);
				}
				
			}
		}
	}
	
	/**
	 * 首页模板
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public boolean ValidateCamInfo(CampaignDTO dto,Map<String, Object> paramMap) throws Exception {
		// 验证结果
		boolean result = true;
		// 活动名称
		if(CherryChecker.isNullOrEmpty(dto.getCampaignName(), true)){
			this.addFieldError("campInfo.campaignName",getText("ECM00009",new String[]{getText("PMB00017")}));
			result = false;
		} else if(dto.getCampaignName().length() > 50){
			this.addFieldError("campInfo.campaignName",getText("ECM00020",new String[]{getText("PMB00017"),"50"}));
			result = false;
		} else if (dto.getCampaignName().contains("\t")||dto.getCampaignName().contains("\n")||dto.getCampaignName().contains("\r")){
			this.addFieldError("campInfo.campaignName",getText("ECM00109",new String[]{getText("PMB00017")}));
			result = false;
		}
		if(!CherryChecker.isNullOrEmpty(dto.getDescriptionDtl(), true) && dto.getDescriptionDtl().length() > 300){
			this.addFieldError("campInfo.descriptionDtl",getText("ECM00020",new String[]{getText("PMB00018"),"300"}));
			result = false;
		}
		boolean dateFlag = true;
		boolean toDateFlag = true;
		// 主题开始时间
		if (CherryChecker.isNullOrEmpty(dto.getCampaignFromDate(), true)){
			// 主题开始时间不能为空
			this.addFieldError("campInfo.campaignFromDate", getText("ECM00009",new String[]{getText("ACT00028")}));
			result = false;
			dateFlag = false;
		} else if(!CherryChecker.checkDate(dto.getCampaignFromDate())){
			this.addFieldError("campInfo.campaignFromDate",getText("ECM00022" ,new String[] { getText("ACT00028") }));
			result = false;
			dateFlag = false;
		}
		// 主题结束时间
		if( !CherryChecker.isNullOrEmpty(dto.getCampaignToDate(), true)){
			if(!CherryChecker.checkDate(dto.getCampaignToDate())){
				this.addFieldError("campInfo.getCampaignToDate",getText("ECM00022" ,new String[] { getText("ACT00029") }));
				result = false;
				toDateFlag = false;
			} else if(dateFlag && CherryChecker.compareDate(dto.getCampaignFromDate(), dto.getCampaignToDate()) > 0){
				this.addFieldError("campInfo.campaignToDate",getText("ECM00033" ,new String[] { getText("ACT00029"), getText("ACT00028") }));
				result = false;
				toDateFlag = false;
			}
		}
		// 会员活动
    	if (CampConstants.TYPE_FLAG_1.equals(dto.getCampaignTypeFlag())) {
    		String orgId = ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
    		String brandId = ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID));
    		String validFlag = binOLCM14_BL.getConfigValue("1284", orgId, brandId);
			if("1".equals(validFlag)){
	    		String opt = ConvertUtil.getString(paramMap.get(CampConstants.OPT_KBN));
	    		List<Integer> idList = binolcpcom02IF.getActIdByName(dto.getCampaignName(), brandId);
	    		if(null != idList && idList.size() > 0){
	    			int oldId = ConvertUtil.getInt(dto.getCampaignId());
	    			if(idList.size() > 1 ||(!CampConstants.OPT_KBN2.equals(opt) || idList.get(0) != oldId)){
	        			this.addFieldError("campInfo.campaignName",getText("ECM00032",new String[]{getText("PMB00017")}));
	        			result = false;
	        		}
	    		}
			}
    		// 主题结束时间验证
    		if (CherryChecker.isNullOrEmpty(dto.getCampaignToDate(), true)){
    			// 主题结束时间不能为空
    			this.addFieldError("campInfo.campaignToDate", getText("ECM00009",new String[]{getText("PMB00010")}));
    			result = false;
    			toDateFlag = false;
    		}
    		// 活动验证
    		String subCampaignValid = dto.getSubCampaignValid();
    		// 本地验证
    		if ("1".equals(subCampaignValid)) {
    			// 本地校验规则
    			String localValidRule = dto.getLocalValidRule();
    			// 本地校验规则为空验证
        		if (CherryChecker.isNullOrEmpty(localValidRule, true)){
        			// 本地校验规则不能为空
        			this.addFieldError("campInfo.localValidRule", getText("ECM00009",new String[]{getText("PCP00047")}));
        			result = false;
        		}else if(localValidRule.length() > 10){
        			// 本地校验规则不能超过10位
        			this.addFieldError("campInfo.localValidRule", getText("ECM00020",new String[]{getText("PCP00047"), "10"}));
        			result = false;
        		}
    		}
    		boolean obtainFromFlag = true;
    		// 指定日期
    		if(CampConstants.REFER_TYPE_0.equals(dto.getReferType())){
	    		// 领用开始时间
	    		String obtainFromDate = dto.getObtainFromDate();
	    		// 领用结束时间
	    		String obtainToDate = dto.getObtainToDate();
	    		// 领用开始时间验证
	    		if (CherryChecker.isNullOrEmpty(obtainFromDate, true)){
	    			// 领用开始时间不能为空
	    			this.addFieldError("campInfo.obtainFromDate", getText("ECM00009",new String[]{getText("ESS00057")}));
	    			obtainFromFlag = false;
	    			result = false;
	    		}else if(!CherryChecker.checkDate(obtainFromDate)){
	    			// 领用开始时间必须为日期格式
	    			this.addFieldError("campInfo.obtainFromDate", getText("ECM00022",new String[]{getText("ESS00057")}));
	    			obtainFromFlag = false;
	    			result = false;
	    		} else if (dateFlag && CherryChecker.compareDate(obtainFromDate, dto.getCampaignFromDate()) < 0){
	    			// 领用开始时间不能小于主题开始时间
	    			this.addFieldError("campInfo.obtainFromDate", getText("ECM00033",new String[]{getText("ESS00057"), getText("ACT00028")}));
	    			obtainFromFlag = false;
	    			result = false;
	    		}
	    		// 领用结束时间验证
	    		if (CherryChecker.isNullOrEmpty(obtainToDate, true)){
	    			// 领用结束时间不能为空
	    			this.addFieldError("campInfo.obtainToDate", getText("ECM00009",new String[]{getText("ESS00058")}));
	    			result = false;
	    		}else if(!CherryChecker.checkDate(obtainToDate)){
	    			// 领用结束时间必须为日期格式
	    			this.addFieldError("campInfo.obtainToDate", getText("ECM00022",new String[]{getText("ESS00058")}));
	    			result = false;
	    		}else if(obtainFromFlag && CherryChecker.compareDate(obtainFromDate, obtainToDate) > 0){
	    			// 领用结束时间必须大于领用开始时间
	    			this.addFieldError("campInfo.obtainToDate", getText("ECM00027",new String[]{getText("ESS00058"), getText("ESS00057")}));
	    			result = false;
	    		}else if (toDateFlag && CherryChecker.compareDate(obtainToDate, dto.getCampaignToDate()) > 0){
	    			// 领用结束时间不能大于主题结束时间
	    			this.addFieldError("campInfo.obtainToDate", getText("ECM00052",new String[]{getText("ESS00058"), getText("ACT00028")}));
	    			result = false;
	    		}
    		}else{
    			obtainFromFlag = false;
    			if(CherryChecker.isNullOrEmpty(dto.getValA())){
    				this.addFieldError("campInfo.valA", getText("ECM00009",new String[]{""}));
    				result = false;
    			}else if(!CherryChecker.isNumeric(dto.getValA())){
    				this.addFieldError("campInfo.valA", getText("ECM00021",new String[]{""}));
    				result = false;
    			}
    			if(CherryChecker.isNullOrEmpty(dto.getValB())){
    				this.addFieldError("campInfo.valB", getText("ECM00009",new String[]{""}));
    				result = false;
    			}else if(!CherryChecker.isNumeric(dto.getValB())){
    				this.addFieldError("campInfo.valB", getText("ECM00021",new String[]{""}));
    				result = false;
    			}
    		}
			// 预约开始时间
    		String orderFromDate = dto.getCampaignOrderFromDate();
    		// 预约结束时间
    		String orderToDate = dto.getCampaignOrderToDate();
    		boolean orderFromFlag = true;
    		if (!CherryChecker.isNullOrEmpty(orderFromDate, false) || !CherryChecker.isNullOrEmpty(orderToDate, false)) {
				// 预约开始时间验证
	    		if (CherryChecker.isNullOrEmpty(orderFromDate, true)){
	    			// 预约开始时间不能为空
	    			this.addFieldError("campInfo.campaignOrderFromDate", getText("ECM00009",new String[]{getText("ESS00055")}));
	    			orderFromFlag = false;
	    			result = false;
	    		}else if(!CherryChecker.checkDate(orderFromDate)){
	    			// 预约开始时间必须为日期格式
	    			this.addFieldError("campInfo.campaignOrderFromDate", getText("ECM00022",new String[]{getText("ESS00055")}));
	    			orderFromFlag = false;
	    			result = false;
	    		}else if (dateFlag && CherryChecker.compareDate(orderFromDate, dto.getCampaignFromDate()) < 0){
	    			// 预约开始时间不能小于主题开始时间
	    			this.addFieldError("campInfo.campaignOrderFromDate", getText("ECM00033",new String[]{getText("ESS00055"), getText("ACT00028")}));
	    			orderFromFlag = false;
	    			result = false;
	    		}
	    		// 预约结束时间验证
	    		if (CherryChecker.isNullOrEmpty(orderToDate, true)){
	    			// 预约结束时间不能为空
	    			this.addFieldError("campInfo.campaignOrderToDate", getText("ECM00009",new String[]{getText("ESS00056")}));
	    			result = false;
	    		}else if(!CherryChecker.checkDate(orderToDate)){
	    			// 预约结束时间必须为日期格式
	    			this.addFieldError("campInfo.campaignOrderToDate", getText("ECM00022",new String[]{getText("ESS00056")}));
	    			result = false;
	    		}else if(orderFromFlag && CherryChecker.compareDate(orderFromDate, orderToDate) > 0){
	    			// 预约结束时间必须大于预约开始时间
	    			this.addFieldError("campInfo.campaignOrderToDate", getText("ECM00027",new String[]{getText("ESS00056"), getText("ESS00055")}));
	    			result = false;
	    		}else if (toDateFlag && CherryChecker.compareDate(orderToDate, dto.getCampaignToDate()) > 0){
	    			// 预约结束时间不能大于主题结束时间
	    			this.addFieldError("campInfo.campaignOrderToDate", getText("ECM00052",new String[]{getText("ESS00056"), getText("ACT00029")}));
	    			result = false;
	    		}
	    		if (obtainFromFlag && orderFromFlag) {
	    			if(CherryChecker.compareDate(dto.getObtainFromDate(), orderFromDate) < 0){
	    				// 领用开始时间必须大于预约开始时间
	    				this.addFieldError("campInfo.obtainFromDate", getText("ECM00027",new String[]{getText("ESS00057"),getText("ESS00055")}));
	    				result = false;
	    			}
	    		}
    		}else{
    			// 验证领用柜台:预约柜台
    			if("3".equals(dto.getGotCounter())){
    				// 该活动无预约阶段，领用柜台不能为预约柜台
//	    			this.addFieldError("campInfo.gotCounter", getText("ACT00039"));
//	    			result = false;
    			}
    		}
    		// 备货开始时间
    		String stockFromDate = dto.getCampaignStockFromDate();
    		// 备货结束时间
    		String stockToDate = dto.getCampaignStockToDate();
    		boolean stockFromFlag = true;
    		if(!CherryChecker.isNullOrEmpty(stockFromDate, false) || !CherryChecker.isNullOrEmpty(stockToDate, false)){
    			//备货开始时间验证
        		if (CherryChecker.isNullOrEmpty(stockFromDate, true)){
        			// 备货开始时间不能为空
        			this.addFieldError("campInfo.campaignStockFromDate", getText("ECM00009",new String[]{getText("ACT00026")}));
        			stockFromFlag = false;
        			result = false;
        		}else if(!CherryChecker.checkDate(stockFromDate)){
        			// 备货开始时间必须为日期格式
        			this.addFieldError("campInfo.campaignStockFromDate", getText("ECM00022",new String[]{getText("ACT00026")}));
        			stockFromFlag = false;
        			result = false;
        		}else if (dateFlag && CherryChecker.compareDate(stockFromDate, dto.getCampaignFromDate()) < 0){
        			// 备货开始时间不能小于主题开始时间
        			this.addFieldError("campInfo.campaignStockFromDate", getText("ECM00033",new String[]{getText("ACT00026"), getText("ACT00028")}));
        			stockFromFlag = false;
        			result = false;
        		}
        		// 备货结束时间验证
        		if (CherryChecker.isNullOrEmpty(stockToDate, true)){
        			// 备货结束时间不能为空
        			this.addFieldError("campInfo.campaignStockToDate", getText("ECM00009",new String[]{getText("ACT00027")}));
        			result = false;
        		}else if(!CherryChecker.checkDate(stockToDate)){
        			// 备货结束时间必须为日期格式
        			this.addFieldError("campInfo.campaignStockToDate", getText("ECM00022",new String[]{getText("ACT00027")}));
        			result = false;
        		}else if(stockFromFlag && CherryChecker.compareDate(stockFromDate, stockToDate) > 0){
        			// 备货结束时间必须大于备货开始时间
        			this.addFieldError("campInfo.campaignStockToDate", getText("ECM00027",new String[]{getText("ACT00027"), getText("ACT00026")}));
        			result = false;
        		}else if (toDateFlag && CherryChecker.compareDate(stockToDate, dto.getCampaignToDate()) > 0){
        			// 备货结束时间不能大于主题结束时间
        			this.addFieldError("campInfo.campaignStockToDate", getText("ECM00052",new String[]{getText("ACT00027"), getText("ACT00029")}));
        			result = false;
        		}
        		if (obtainFromFlag && stockFromFlag) {
        			if(CherryChecker.compareDate(dto.getObtainFromDate(), stockFromDate) < 0){
        				// 领用开始时间必须大于备货开始时间
        				this.addFieldError("campInfo.obtainFromDate", getText("ECM00027",new String[]{getText("ESS00057"),getText("ACT00026")}));
        				result = false;
        			}
        		}
    		}
    		if(!CherryChecker.isNullOrEmpty(orderFromDate) 
    				&& !CherryChecker.isNullOrEmpty(stockFromDate)
    				&& !CherryChecker.isNullOrEmpty(dto.getObtainFromDate())){
    			if (stockFromFlag && orderFromFlag) {
        			if(CherryChecker.compareDate(stockFromDate, orderFromDate) < 0){
        				// 备货开始时间必须大于预约开始时间
        				this.addFieldError("campInfo.campaignStockFromDate", getText("ECM00027",new String[]{getText("ACT00026"),getText("ESS00055")}));
        				result = false;
        			}
        		}
    			if (obtainFromFlag && stockFromFlag) {
        			if(CherryChecker.compareDate(dto.getObtainFromDate(), stockFromDate) < 0){
        				// 领用开始时间必须大于备货开始时间
        				this.addFieldError("campInfo.obtainFromDate", getText("ECM00027",new String[]{getText("ESS00057"),getText("ACT00026")}));
        				result = false;
        			}
        		}
    		}
    		// 积分截止日期验证
    		if(!CherryChecker.isNullOrEmpty(dto.getExPointDeadDate())){
    			if( !CherryChecker.checkDate(dto.getExPointDeadDate())){
    				this.addFieldError("campInfo.exPointDeadDate", getText("ECM00022",new String[]{getText("ACT00027")}));
    				result = false;
    			}else{
    				if(!CherryChecker.isNullOrEmpty(dto.getObtainFromDate())){
	    				// 积分可兑换截止日期小于领用开始日期验证
	    				if(CherryChecker.compareDate(dto.getExPointDeadDate(), dto.getObtainFromDate()) >= 0){
	        				// 领用开始时间必须大于备货开始时间
	        				this.addFieldError("campInfo.exPointDeadDate", getText("ECM00051",new String[]{getText("ACT00042"),getText("ESS00057")}));
	        				result = false;
	        			}else{
	        				// 取得可兑换积分截止日期List
	        				List<Map<String, String>> list = binolcpcom02IF.getExPointDeadDateList(paramMap);
	        				if(null != list && list.size() > 0){
	        					String campaignName = null;
	        					for(Map<String, String> m : list){
	        						if(ConvertUtil.getInt(m.get("campaignId")) != ConvertUtil.getInt(dto.getCampaignId())
	        								&& !dto.getExPointDeadDate().equals(m.get("exPointDeadDate"))){
	        							// 日期不交叉验证
		        						if(CherryChecker.compareDate(dto.getExPointDeadDate(), m.get("exPointDeadDate")) > 0
		        								&& CherryChecker.compareDate(dto.getExPointDeadDate(), m.get(CampConstants.OBTAIN_TODATE)) <= 0){
		        							campaignName = m.get("campaignName");
		        	        				break;
		        						}else if(CherryChecker.compareDate(dto.getExPointDeadDate(), m.get("exPointDeadDate")) < 0
		        								&& CherryChecker.compareDate(dto.getObtainToDate(),m.get("exPointDeadDate")) >= 0){
		        							campaignName = m.get("campaignName");
		        	        				break;
		        						}
	        						}
	        					}
	        					if(null != campaignName){
	        						this.addFieldError("campInfo.exPointDeadDate", getText("ACT00043",
        	        						new String[]{getText("ACT00042"),campaignName,getText("ACT00042")}));
        	        				result = false;
	        					}
	        				}
	        			}
    				}
    			}
    		}
    	}
		return result;
	}

}
