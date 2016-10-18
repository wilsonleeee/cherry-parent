/*  
 * @(#)BINOLSTIOS12_Action.java     1.0 2015/09/24      
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
package com.cherry.st.ios.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.ios.form.BINOLSTIOS12_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS12_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @ClassName: BINOLSTIOS12_Action 
 * @Description: TODO(大仓入库Action) 
 * @author menghao
 * @version v1.0.0 2015-9-24 
 *
 */
public class BINOLSTIOS12_Action  extends BaseAction implements ModelDriven<BINOLSTIOS12_Form>{

	private static final long serialVersionUID = 1355278876734768891L;

	/**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS12_Action.class);

    /** 参数FORM */
    private BINOLSTIOS12_Form form = new BINOLSTIOS12_Form();
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLSTIOS12_BL")
    private BINOLSTIOS12_IF binOLSTIOS12_BL;
    
    @Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_IF binOLSTCM08_BL;
    
    private List<Map<String,Object>> logicDepotsList =null;
    
    @Override
    public BINOLSTIOS12_Form getModel() {
        return form;
    }

    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String init() throws Exception {
        try {
            // 用户信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            
            String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
            String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
            // 语言类型
            String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
            
            form.setInDepotDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
            
            // 入库方【只有给品牌总部部门 做入库】
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoID);
            paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
            Map<String, Object> brandDepartInfo = this.getDepartCodeName(paramMap);

            //初始化时是否显示部门仓库
            boolean initShowFlag = false;
            String inDepartCodeName = "";
            String inOrganizationID = "";
            if(null != brandDepartInfo && !brandDepartInfo.isEmpty()){
                initShowFlag = true;
                inDepartCodeName = ConvertUtil.getString(brandDepartInfo.get("departCodeName"));
                inOrganizationID = ConvertUtil.getString(brandDepartInfo.get("BIN_OrganizationID"));
                //判断是否有向用户所属部门操作的权限
                Map<String,Object> privilegeParam = new HashMap<String,Object>();
                privilegeParam.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoID);
                privilegeParam.put(CherryConstants.BRANDINFOID, brandInfoId);
                privilegeParam.put("BIN_OrganizationID", inOrganizationID);
                privilegeParam.put("userId", userInfo.getBIN_UserID());
                privilegeParam.put("employeeId", userInfo.getBIN_EmployeeID());
                privilegeParam.put("operationType", "0");
                privilegeParam.put("businessType", "5");//入库
                privilegeParam.put("ValidFlag", "1");
                initShowFlag = binOLCM01_BL.checkDepartByDepartPrivilege(privilegeParam);
            }
            
            //入库方仓库List
            List<Map<String,Object>> inDepotList = new ArrayList<Map<String,Object>>();
            if(initShowFlag){
                inDepotList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(inOrganizationID), language);
            }
            form.setDepotsList(inDepotList);
            
            //取得入库方逻辑仓库List
            Map<String,Object> logicParam = new HashMap<String,Object>();
            logicParam.put("organizationId", inOrganizationID);
            List<Map<String,Object>> inLogicDepotList = new ArrayList<Map<String,Object>>();
            if(initShowFlag){
                inLogicDepotList = getLogicList(logicParam);
            }
            form.setLogicDepotsInfoList(inLogicDepotList);
            
            //初始化默认显示入库方
            Map<String,Object> initInfoMap = new HashMap<String,Object>();
            if(initShowFlag){
                initInfoMap.put("defaultInDepartID", inOrganizationID);
                initInfoMap.put("defaultInDepartCodeName", inDepartCodeName);
            }else{
                initInfoMap.put("defaultInDepartID", "");
                initInfoMap.put("defaultInDepartCodeName", "");
            }
            form.setInitInfoMap(initInfoMap);
            
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
            }
        }
        return SUCCESS;
    }
    
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception
     */
    public void getDepotByAjax() throws Exception{
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> list = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 提交
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);
            //用户ID
            map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            //组织ID
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                    .getBIN_OrganizationInfoID());
            //所属品牌ID
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            //部门ID
            map.put(CherryConstants.ORGANIZATIONID, form.getInOrganizationId());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSTIOS12");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            //入库理由
            map.put("comments", form.getReason());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSTIOS12");
            //实体仓库ID
            map.put("depotInfoId", form.getDepotInfoId());
            //逻辑仓库ID
            map.put("logicInventoryInfoId", form.getLogicDepotsInfoId());
            //往来单位
            map.put("bussinessPartnerId", form.getBussinessPartnerId());
            //入库日期
            map.put("inDepotDate", form.getInDepotDate().trim());
            //产品厂商ID
            String[] productVendorIdArr = form.getProductVendorIDArr();
            //数量
            String[] quantityArr = form.getQuantityuArr();
            //备注
            String[] reasonArr = form.getReasonArr();
            //执行价
            String[] priceArr = form.getPriceArr();
            //参考价
            String[] referencePriceArr = form.getReferencePriceArr();
            List<String[]> arrList = new ArrayList<String[]>();
            arrList.add(productVendorIdArr);
            arrList.add(quantityArr);
            arrList.add(reasonArr);
            arrList.add(priceArr);
            arrList.add(referencePriceArr);
            int billId = binOLSTIOS12_BL.tran_submit(map, arrList, userInfo);
           
            if(billId == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
            }else{
                //语言
                String language = userInfo.getLanguage();
                //取得入库单概要信息
                Map<String,Object> mainMap = binOLSTCM08_BL.getProductInDepotMainData(billId, language);
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
     * 暂存
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            //用户ID
            map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            //组织ID
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                    .getBIN_OrganizationInfoID());
            //所属品牌ID
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            //部门ID
            map.put(CherryConstants.ORGANIZATIONID, form.getInOrganizationId());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSTIOS12");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            //入库理由
            map.put("comments", form.getReason());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSTIOS12");
            //实体仓库ID
            map.put("depotInfoId", form.getDepotInfoId());
            //逻辑仓库ID
            map.put("logicInventoryInfoId", form.getLogicDepotsInfoId());
            //往来单位
            map.put("bussinessPartnerId", form.getBussinessPartnerId());
            //入库日期
            map.put("inDepotDate", form.getInDepotDate().trim());
            //产品厂商ID
            String[] productVendorIdArr = form.getProductVendorIDArr();
            //数量
            String[] quantityArr = form.getQuantityuArr();
            //备注
            String[] reasonArr = form.getReasonArr();
            //执行价
            String[] priceArr = form.getPriceArr();
            //参考价
            String[] referencePriceArr = form.getReferencePriceArr();
            List<String[]> arrList = new ArrayList<String[]>();
            arrList.add(productVendorIdArr);
            arrList.add(quantityArr);
            arrList.add(reasonArr);
            arrList.add(priceArr);
            arrList.add(referencePriceArr);
            int billId = binOLSTIOS12_BL.tran_save(map, arrList, userInfo);
            if(billId == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
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
     * 取得逻辑仓库
     * @param param
     * @return
     * @throws Exception
     */
    private List<Map<String,Object>> getLogicList(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String language = userInfo.getLanguage();
        String organizationId = ConvertUtil.getString(param.get("organizationId"));
        String logicType = "0";
        String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_GR;
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationId,language);
        if(null != departInfoMap){
            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
            //终端
            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                logicType = "1";
                bussinessType = CherryConstants.LOGICDEPOT_TERMINAL_GR;
            }
        }
        
        //调用共通获取逻辑仓库
        Map<String,Object> paramMap =  new HashMap<String,Object>();
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BusinessType", bussinessType);
        paramMap.put("Type", logicType);
        paramMap.put("ProductType", "1");
        paramMap.put("language", language);
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
        return logicList;
    }
    
    /**
     * 取得品牌总部部门编号名称及ID
     * @param organizationID
     * @param language
     * @return
     */
    private Map<String, Object> getDepartCodeName(Map<String, Object> map){
        Map<String,Object> departInfoMap = binOLCM01_BL.getBrandDepartInfo(map);
        if(null != departInfoMap){
            String departCode = ConvertUtil.getString(departInfoMap.get("DepartCode"));
            String departName = ConvertUtil.getString(departInfoMap.get("DepartName"));
            if(!"".equals(departCode)){
            	departInfoMap.put("departCodeName", "("+departCode+")"+departName);
            }else{
            	departInfoMap.put("departCodeName", departName);
            }
        }
        return departInfoMap;
    }
    
    /**
     * 取得逻辑仓库信息
     * @throws Exception
     */
    public void getLogicInfo()throws Exception{
        String organizationID = form.getInOrganizationId();
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationID);
        ConvertUtil.setResponseByAjax(response, getLogicList(param));
    }
    
    /**
     * 校验暂存参数
     * @return
     * @throws Exception
     */
    public boolean validateSave() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        //组织ID
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        //所属品牌ID
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        String bussinessDate = binOLSTIOS12_BL.getBussinessDate(map);
		boolean isCorrect = true;
    	// 入库日期
		String InDepotDate = ConvertUtil.getString(form.getInDepotDate());
    	if (CherryChecker.isNullOrEmpty(InDepotDate)) {
			this.addFieldError("inDepotDate", getText("EBS00043"));
			isCorrect = false;
		}else if (!CherryChecker.checkDate(InDepotDate)) {
			this.addActionError(getText("ECM00008",new String[] { getText("PCM00003")}));
			isCorrect = false;
		}else if(bussinessDate.compareTo(InDepotDate) < 0){
			this.addActionError(getText("EST00031",new String[]{bussinessDate}));
			isCorrect = false;
		}
    	return isCorrect;
    }
    
    /**
     * 校验提交参数
     * @return
     * @throws Exception
     */
    public boolean validateSubmit() throws Exception {
    	 Map<String, Object> map = new HashMap<String, Object>();
         // 用户信息
         UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
         //用户ID
         map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
         //组织ID
         map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                 .getBIN_OrganizationInfoID());
         //所属品牌ID
         map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
         String bussinessDate = binOLSTIOS12_BL.getBussinessDate(map);
        boolean isCorrect = true;
        // 入库日期
        String InDepotDate = ConvertUtil.getString(form.getInDepotDate());
        if (CherryChecker.isNullOrEmpty(InDepotDate)) {
            this.addFieldError("inDepotDate", getText("EBS00043"));
            isCorrect = false;
        }else if (!CherryChecker.checkDate(InDepotDate)) {
            this.addActionError(getText("ECM00008",new String[] { getText("PCM00003")}));
            isCorrect = false;
        }else if(bussinessDate.compareTo(InDepotDate) < 0){
			this.addActionError(getText("EST00031",new String[]{bussinessDate}));
			isCorrect = false;
		}
        return isCorrect;
    }

	public List<Map<String, Object>> getLogicDepotsList() {
		return logicDepotsList;
	}

	public void setLogicDepotsList(List<Map<String, Object>> logicDepotsList) {
		this.logicDepotsList = logicDepotsList;
	}
    
}
