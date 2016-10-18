/*  
 * @(#)WorkFlowAction.java     1.0 2011/05/31      
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
package com.cherry.lg.top.action;


import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM10_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

public class WorkFlowAction extends BaseAction{
	@Resource
	BINOLCM10_BL binOLCM10_BL;
    
    public String doTask() throws Exception{  
     	//UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
     	//整理URL中传入的参数
    	//工作流实例ID
     	String entryID= request.getParameter("OS_EntryID");
     	//业务类型代码  SD MV 等等
     	String billType= request.getParameter("OS_BillType");
     	//单据ID
     	String billID= request.getParameter("OS_BillID");
     	//产品:N 促销品:PRM
     	String proType= request.getParameter("OS_ProType"); 

    	this.setMainOrderID(billID); 
		this.setTaskInstanceID(entryID); 
    	this.setCsrftoken(csrftoken);
    	//根据参数的情况，来决定跳转到哪个action  和 struts-lg.xml中的配置呼应
    	String redirectActionName ="";
    	if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
    		//如果是促销品
    		if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
    			//发货
    			redirectActionName = "100";
    		}else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
    			//调拨
    			redirectActionName = "101";
    		}else if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
                //移库
                redirectActionName = "102";
    		}else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
                //入库
                redirectActionName = "103";
    		}
    	}else if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(proType)){
    		//如果是产品
    		if(CherryConstants.OS_BILLTYPE_OD.equals(billType)){
    			//订货
    			redirectActionName = "200";
    		}else if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
    			//发货
    			redirectActionName = "201";
    		}else if(CherryConstants.OS_BILLTYPE_LS.equals(billType)){
    		    //报损
                redirectActionName = "202";
    		}else if(CherryConstants.OS_BILLTYPE_CA.equals(billType)){
                //盘点
                redirectActionName = "203";
            }else if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
    		    //移库
                redirectActionName = "204";
    		}else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
            	//入库
            	redirectActionName = "205";
            }else if(CherryConstants.OS_BILLTYPE_RR.equals(billType)){
                //退库
                redirectActionName = "206";
            }else if(CherryConstants.OS_BILLTYPE_RA.equals(billType)){
                //退库申请
                redirectActionName = "207";
            }else if(CherryConstants.OS_BILLTYPE_CR.equals(billType)){
                //盘点申请
                redirectActionName = "208";
            }else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
                //调拨
                redirectActionName = "209";
            }else if(CherryConstants.OS_BILLTYPE_SA.equals(billType)){
                //调拨
                redirectActionName = "211";
            }
    	}else if(CherryConstants.OS_BILLTYPE_NS.equals(billType)){
            //后台销售
            redirectActionName = "210";
        }
    	return redirectActionName;
    }
    
    private String mainOrderID;
    
    private String taskInstanceID;
    
    private String csrftoken;
    
    private String parameter1;


	/**
	 * @return the taskInstanceID
	 */
	public String getTaskInstanceID() {
		return taskInstanceID;
	}

	/**
	 * @param taskInstanceID the taskInstanceID to set
	 */
	public void setTaskInstanceID(String taskInstanceID) {
		this.taskInstanceID = taskInstanceID;
	}

	/**
	 * @return the csrftoken
	 */
	public String getCsrftoken() {
		return csrftoken;
	}

	/**
	 * @param csrftoken the csrftoken to set
	 */
	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	/**
	 * @return the parameter1
	 */
	public String getParameter1() {
		return parameter1;
	}

	/**
	 * @param parameter1 the parameter1 to set
	 */
	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	/**
	 * @return the mainOrderID
	 */
	public String getMainOrderID() {
		return mainOrderID;
	}

	/**
	 * @param mainOrderID the mainOrderID to set
	 */
	public void setMainOrderID(String mainOrderID) {
		this.mainOrderID = mainOrderID;
	}
}
