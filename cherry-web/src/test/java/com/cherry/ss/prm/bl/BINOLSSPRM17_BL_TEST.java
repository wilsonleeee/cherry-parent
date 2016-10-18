package com.cherry.ss.prm.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.prm.form.BINOLSSPRM17_Form;
import com.cherry.st.common.workflow.ProFlowRA_FN;
import com.opensymphony.workflow.Workflow;

public class BINOLSSPRM17_BL_TEST extends CherryJunitBase{
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSSPRM17_BL")
    private BINOLSSPRM17_BL bl;

    @Test
    @Rollback(true)
    @Transactional 
    public void testTran_deliver1() throws Exception{
        String caseName = "testTran_deliver1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> otherInfo = new HashMap<String,Object>();
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        BINOLSSPRM17_Form form = new BINOLSSPRM17_Form();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        //Basis.BIN_Organization
        String departCode = ConvertUtil.getString(otherInfo.get("DepartCode"));
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#DepartCode#", departCode);
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", departCode);
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        userInfo.setBIN_BrandInfoID(brandInfoID);
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_OrganizationID(organizationID);
        form.setOutOrganizationId(String.valueOf(organizationID));
        
        //加载工作流文件
        String orgCode = ConvertUtil.getString(insertOrganizationInfoMap.get("OrgCode"));
        String brandCode = ConvertUtil.getString(insertBrandInfoMap.get("BrandCode"));
        String flowName = "promotionDFS";
        String flowFile = "promotionDFS.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        userInfo.setOrganizationInfoCode(orgCode);
        userInfo.setBrandCode(brandCode);
        
        //设置审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);
        
        int billID = bl.tran_deliver(form, userInfo);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Inventory.BIN_PromotionDeliver");
        param.put("BIN_PromotionDeliverID", billID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(param);
        assertEquals(null,actualList.get(0).get("DeliverDate"));
    }
    
    @Test
    @Rollback(true)
    @Transactional 
    public void testTran_saveDeliver1() throws Exception{
        String caseName = "testTran_saveDeliver1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> otherInfo = new HashMap<String,Object>();
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");

        BINOLSSPRM17_Form form = new BINOLSSPRM17_Form();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        //Basis.BIN_Organization
        String departCode = ConvertUtil.getString(otherInfo.get("DepartCode"));
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#DepartCode#", departCode);
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", departCode);
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        userInfo.setBIN_BrandInfoID(brandInfoID);
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_OrganizationID(organizationID);
        form.setOutOrganizationId(String.valueOf(organizationID));
        
        bl.tran_saveDeliver(form, userInfo);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Inventory.BIN_PromotionDeliver");
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        param.put("BIN_OrganizationID", organizationID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(param);
        assertEquals(null,actualList.get(0).get("DeliverDate"));
    }
    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName){
        String rootpath = ProFlowRA_FN.class.getResource("/").getPath();
        rootpath = rootpath.replace("test-classes", "classes");
        String path = rootpath + "worflowfile/ss/"+fileName;
        return path;
    }
    
    /**
     * 加载工作流文件到内存中
     * @param filePath
     * @param orgCode
     * @param brandCode
     * @param fileCode
     * @throws Exception
     */
    public void loadWorkFlowDescriptor(String filePath,String fileCode,String orgCode,String brandCode) throws Exception{
        String path = getWorkFlowFilePath(filePath);
        String fileContentNew = FileUtils.readFileToString(new File(path),"UTF-8");
        FileStoreDTO fileStoreNew = null;
        FileStoreDTO fileStoreDTO = binolcm30IF.getFileStoreByCode(fileCode, orgCode, brandCode);
        fileStoreNew = fileStoreDTO;
        ConvertUtil.convertDTO(fileStoreNew, fileStoreDTO, true);
        fileStoreNew.setFileStoreId(0);
        fileStoreNew.setFileCode(fileCode);
        fileStoreNew.setOrgCode(orgCode);
        fileStoreNew.setBrandCode(brandCode);
        // 刷新内存的工作流文件内容
        fileStoreNew.setFileContent(fileContentNew);
    }
}