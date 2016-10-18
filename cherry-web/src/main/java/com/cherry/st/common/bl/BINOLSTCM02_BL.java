package com.cherry.st.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.service.BINOLSTCM02_Service;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.FunctionDescriptor;

public class BINOLSTCM02_BL implements BINOLSTCM02_IF{

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLSTCM02_Service")
    private BINOLSTCM02_Service binOLSTCM02_Service;
    
    /**
     * 将订单信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    @Override
    public int insertProductOrderAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList) {
        int productOrderId = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String orderNo = ConvertUtil.getString(mainData.get("OrderNo"));
        String orderNoIF = ConvertUtil.getString(mainData.get("OrderNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType ="OD";
        //如果orderNo不存在调用共通生成单据号
        if(null==orderNo || "".equals(orderNo)){
            orderNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("OrderNo", orderNo);
        }
        if(null == orderNoIF || "".equals(orderNoIF)){
            mainData.put("OrderNoIF", orderNo);
        }
        if(null == mainData.get("Date")){
            mainData.put("Date", binOLSTCM02_Service.getDateYMD());
        }
        if(null == mainData.get("BIN_LogicInventoryInfoID")){
            mainData.put("BIN_LogicInventoryInfoID", 0);
        }
        if(null == mainData.get("BIN_StorageLocationInfoID")){
            mainData.put("BIN_StorageLocationInfoID", 0);
        }
        if(null == mainData.get("BIN_LogicInventoryInfoIDAccept")){
            mainData.put("BIN_LogicInventoryInfoIDAccept", 0);
        }
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        //插入产品订货单主表
        productOrderId = binOLSTCM02_Service.insertProductOrder(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productOrderDetail = detailList.get(i);
            productOrderDetail.put("BIN_ProductOrderID", productOrderId); 
            
            if(null == productOrderDetail.get("BIN_ProductVendorPackageID")){
                productOrderDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == productOrderDetail.get("BIN_LogicInventoryInfoID")){
                productOrderDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == productOrderDetail.get("BIN_StorageLocationInfoID")){
                productOrderDetail.put("BIN_StorageLocationInfoID", 0);
            }
            if(null == productOrderDetail.get("BIN_LogicInventoryInfoIDAccept")){
                productOrderDetail.put("BIN_LogicInventoryInfoIDAccept", 0);
            }
            
            //插入产品订货单明细表
            binOLSTCM02_Service.insertProductOrderDetail(productOrderDetail);
        }
        
        return productOrderId;
    }
    
    /**
     * 修改订单主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateProductOrderMain(Map<String, Object> praMap) {
        return binOLSTCM02_Service.updateProductOrderMain(praMap);
    }
    
    @Override
    public void updateProductOrderDetail(List<Map<String, Object>> list){
    	binOLSTCM02_Service.updateProductOrderDetail(list);
    }
    
    /**
     * 给订货单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    @Override
    public Map<String, Object> getProductOrderMainData(int productOrderID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductOrderID", productOrderID);
        map.put("language", language);
        return binOLSTCM02_Service.getProductOrderMainData(map);
    }

    /**
     * 给定订单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getProductOrderDetailData(
            int productOrderID,String language,Map<String,Object> otherParam) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductOrderID", productOrderID);
        map.put("language", language);
        if(null != otherParam){
            //排序方式
            String organizationInfoID = ConvertUtil.getString(otherParam.get("BIN_OrganizationInfoID"));
            String brandInfoID = ConvertUtil.getString(otherParam.get("BIN_BrandInfoID"));
            String detailOrderBy = binOLCM14_BL.getConfigValue("1120", organizationInfoID, brandInfoID);
            map.put("detailOrderBy", detailOrderBy);
        }
        return binOLSTCM02_Service.getProductOrderDetailData(map);
    }
    
    /**
     * 通过判断工作流文件里action=1的bean.name来得知工作流FN是否是proFlowOD_YT_FN
     * @param workFlowName
     * @return
     */
    @Override
    public boolean isProFlowOD_YT_FN(String workFlowName) {
        boolean flag = false;
        try{
            List<FunctionDescriptor> postFunctionsList = workflow.getWorkflowDescriptor(workFlowName).getAction(1).getUnconditionalResult().getPostFunctions();
            for(int i=0;i<postFunctionsList.size();i++){
                if("proFlowOD_YT_FN".equals(postFunctionsList.get(i).getArgs().get("bean.name"))){
                    flag = true;
                    break;
                }
            }
        }catch(Exception e){
            
        }
        return flag;
    }
    
    /**
     * 通过判断工作流文件里action=1的参数brandCode来得知工作流文件是否属于某个品牌
     * @param workFlowName
     * @param brandCodes
     * @return
     */
    @Override
    public boolean checkWorkFlowBrand(String workFlowName,String... brandCodes) {
        boolean flag = false;
        try{
            List<FunctionDescriptor> postFunctionsList = workflow.getWorkflowDescriptor(workFlowName).getAction(1).getUnconditionalResult().getPostFunctions();
            for(int i=0;i<postFunctionsList.size();i++){
                String fileBrandCode = ConvertUtil.getString(postFunctionsList.get(i).getArgs().get("brandCode"));
                for(String brandCode: brandCodes){
                    if(brandCode.equals(fileBrandCode)){
                        flag = true;
                        break;
                    }
                }
            }
        }catch(Exception e){
            
        }
        return flag;
    }
}
