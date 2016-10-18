package com.cherry.pl.common.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.pl.common.interfaces.BINOLPLCOM02_IF;
import com.cherry.st.common.workflow.ProFlowOD_FN;
import com.opensymphony.workflow.Workflow;

public class BINOLPLCOM02_Action_TEST extends CherryJunitBase{
    
    private BINOLPLCOM02_Action action;
    
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLPLCOM02_BL")
    private BINOLPLCOM02_IF binOLPLCOM02_BL;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    private CodeTable code;
    

    
    @Before
    public void setUp() throws Exception {

    }
    
    @After
    public void tearDown() throws Exception {

    }

    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception{
        String caseName = "testInit1";
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_init");
        code = (CodeTable) applicationContext.getBean("CodeTable");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        setSession(CherryConstants.SESSION_CHERRY_LANGUAGE,language);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.setSession(session);
        assertEquals("success",proxy.execute());
        
        List<Map<String,Object>> codeList = code.getCodes("1154");
        if(null != codeList && codeList.size()>0){
            Map<String,Object> keyValue = new HashMap<String,Object>();
            for(int i = 0 ;i<codeList.size();i++){
            	String key = ConvertUtil.getString(codeList.get(i).get("CodeKey"));
            	String value = ConvertUtil.getString(codeList.get(i).get("Value"));
            	keyValue.put(key,value);
            }
            assertEquals("产品发货流程",keyValue.get("1"));
            assertEquals("产品盘点流程（纯后台业务）",keyValue.get("2"));
            assertEquals("产品入库流程",keyValue.get("3"));
            assertEquals("产品报损流程（纯后台业务）",keyValue.get("4"));
            assertEquals("产品移库流程",keyValue.get("6"));
            assertEquals("产品退库流程",keyValue.get("7"));
            assertEquals("促销品发货流程",keyValue.get("8"));
            assertEquals("促销品调拨流程（纯后台业务）",keyValue.get("9"));
            assertEquals("产品订货流程",keyValue.get("10"));
            assertEquals("产品退库申请流程",keyValue.get("11"));
            assertEquals("产品盘点申请流程",keyValue.get("12"));
            assertEquals("促销品移库流程（纯后台业务）",keyValue.get("13"));
            assertEquals("产品调拨流程（纯后台业务）",keyValue.get("14"));
        }
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());
        
        Map<String,Object> ruleMap = action.getModel().getRuleMap();
        assertEquals("2",ConvertUtil.getString(ruleMap.get("ThirdPartyFlag")));
        assertEquals(new ArrayList<Long>(){{add(10L);}},ruleMap.get("RuleOnStep"));
        assertEquals("4",ConvertUtil.getString(ruleMap.get("RuleType")));
        assertEquals(new ArrayList<Map<String,Object>>(),ruleMap.get("RuleContext"));
        assertEquals("productDeliver",ruleMap.get("RuleOnFlowCode"));
        
        List<Map<String,Object>> actualButtonList = action.getModel().getButtonList();
        assertEquals("binOLPLCOM02.doSave(101);return false;",actualButtonList.get(0).get("buttonOnclick"));
        assertEquals("icon-save",actualButtonList.get(0).get("buttonClass"));
        assertEquals("os.navigation.Save",actualButtonList.get(0).get("buttonName"));
        assertEquals("binOLPLCOM02.doNext(102, 'back');return false;",actualButtonList.get(1).get("buttonOnclick"));
        assertEquals("icon-mover",actualButtonList.get(1).get("buttonClass"));
        assertEquals("os.navigation.NextStep",actualButtonList.get(1).get("buttonName"));
        
        List<Map<String,Object>> actualTopStepsList = action.getModel().getTopStepsList();
        assertEquals("生成发货单",actualTopStepsList.get(0).get("stepName"));
        assertEquals("审核",actualTopStepsList.get(1).get("stepName"));
        assertEquals("发货",actualTopStepsList.get(2).get("stepName"));
        assertEquals("收货",actualTopStepsList.get(3).get("stepName"));
        
        for(int i=2;i<10;i++){
            action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_init");
            DataUtil.getForm(this.getClass(), caseName, action.getModel());
            action.setSession(session);
            action.getModel().setFlowType(String.valueOf(i));
            assertEquals("success",proxy.execute());
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testPreNextStep() throws Exception{
        String caseName = "testPreNextStep1";
        workflow = (Workflow) applicationContext.getBean("workflow");
        binOLPLCOM02_BL = (BINOLPLCOM02_IF) applicationContext.getBean("binOLPLCOM02_BL");
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String flowType = action.getModel().getFlowType();
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        String orgCode = "TCWFPLCOM02";
        String brandCode = "TCWFPLCOM02";
        String flowName = "productDeliver";
        String flowFile = "productDeliver.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //创建一个工作流ID
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        Long workFlowId = workflow.initialize(wfName, 1, null);
        
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> actionList = (List<Map<String,Object>>) dataMap.get("actionList");
        List<Map<String,Object>> ruleList = (List<Map<String,Object>>) dataMap.get("ruleList");
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String actionId = ConvertUtil.getString(actionList.get(0).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(0).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(0).get("onStep")));
        action.setSession(session);
        proxy.execute();
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(1).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(1).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(1).get("onStep")));
        action.setSession(session);
        proxy.execute();
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(2).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(2).get("ruleParams")));
        action.setSession(session);
        proxy.execute();
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());

        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(3).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.setSession(session);
        proxy.execute();
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(4).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.setSession(session);
        proxy.execute();
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());

        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(5).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.setSession(session);
        proxy.execute();
        assertEquals("1",action.getModel().getOnStep());
        assertEquals(10,action.getModel().getStepId());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSaveRule1() throws Exception{
        String caseName = "testSaveRule1";
        workflow = (Workflow) applicationContext.getBean("workflow");
        binOLPLCOM02_BL = (BINOLPLCOM02_IF) applicationContext.getBean("binOLPLCOM02_BL");
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String flowType = action.getModel().getFlowType();
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        //创建一个工作流ID
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        Long workFlowId = workflow.initialize(wfName, 1, null);
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.setSession(session);
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,proxy.execute());
    }

    @Test 
    @Rollback(true)
    @Transactional
    public void testSaveRule2() throws Exception{
        String caseName = "testSaveRule2";
        workflow = (Workflow) applicationContext.getBean("workflow");
        binOLPLCOM02_BL = (BINOLPLCOM02_IF) applicationContext.getBean("binOLPLCOM02_BL");
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String flowType = action.getModel().getFlowType();
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        //创建一个工作流ID
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        Long workFlowId = workflow.initialize(wfName, 1, null);
        
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> actionList = (List<Map<String,Object>>) dataMap.get("actionList");
        List<Map<String,Object>> ruleList = (List<Map<String,Object>>) dataMap.get("ruleList");
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String actionId = ConvertUtil.getString(actionList.get(0).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(0).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(0).get("onStep")));
        action.getModel().setPreferredFlag("true");
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.setSession(session);
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,proxy.execute());
//        Object[] obj = action.getActionMessages().toArray();
//        assertEquals("保存成功！",obj[0]);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSaveAllRule1() throws Exception{
        String caseName = "testSaveAllRule1";
        workflow = (Workflow) applicationContext.getBean("workflow");
        binOLPLCOM02_BL = (BINOLPLCOM02_IF) applicationContext.getBean("binOLPLCOM02_BL");
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveAllRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String flowType = action.getModel().getFlowType();
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        String orgCode = "TCWFPLCOM02";
        String brandCode = "TCWFPLCOM02";
        String flowName = "productDeliver";
        String flowFile = "productDeliver.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        //创建一个工作流ID
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        Long workFlowId = workflow.initialize(wfName, 1, null);
        
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> actionList = (List<Map<String,Object>>) dataMap.get("actionList");
        List<Map<String,Object>> ruleList = (List<Map<String,Object>>) dataMap.get("ruleList");
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String actionId = ConvertUtil.getString(actionList.get(0).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(0).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(0).get("onStep")));
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(1).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(1).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(1).get("onStep")));
        action.getModel().setPreferredFlag("true");
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(2).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(2).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(2).get("onStep")));
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveAllRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(3).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(3).get("onStep")));
        action.setSession(session);
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,proxy.execute());
//        Object[] obj = action.getActionMessages().toArray();
//        assertEquals("保存成功！",obj[0]);
    }
    
    @Test 
    @Rollback(true)
    @Transactional
    public void testSaveAllRule2() throws Exception{
        String caseName = "testSaveAllRule1";
        workflow = (Workflow) applicationContext.getBean("workflow");
        binOLPLCOM02_BL = (BINOLPLCOM02_IF) applicationContext.getBean("binOLPLCOM02_BL");
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveAllRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String flowType = action.getModel().getFlowType();
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        String orgCode = "TCWFPLCOM02";
        String brandCode = "TCWFPLCOM02";
        String flowName = "productDeliver";
        String flowFile = "productDeliver.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        //创建一个工作流ID
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        Long workFlowId = workflow.initialize(wfName, 1, null);
        
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> actionList = (List<Map<String,Object>>) dataMap.get("actionList");
        List<Map<String,Object>> ruleList = (List<Map<String,Object>>) dataMap.get("ruleList");
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        String actionId = ConvertUtil.getString(actionList.get(0).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(0).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(0).get("onStep")));
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(1).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(1).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(1).get("onStep")));
        action.getModel().setPreferredFlag("true");
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_preNextStep");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        actionId = ConvertUtil.getString(actionList.get(2).get("actionId"));
        action.getModel().setActionId(actionId);
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(2).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(2).get("onStep")));
        action.setSession(session);
        proxy.execute();
        
        action = createAction(BINOLPLCOM02_Action.class, "/pl","BINOLPLCOM02_saveAllRule");
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.getModel().setWorkFlowId(ConvertUtil.getString(workFlowId));
        action.getModel().setRuleParams(ConvertUtil.getString(ruleList.get(3).get("ruleParams")));
        action.getModel().setOnStep(ConvertUtil.getString(actionList.get(3).get("onStep")));
        action.setSession(session);
        
        session.remove(wfName);
        
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,proxy.execute());
//        Object[] obj = action.getActionMessages().toArray();
//        assertEquals("保存成功！",obj[0]);
    }
    
    @Test
    @Rollback(false)
    /**
     * 这个测试方法用于删除在数据库中无法回滚的新增工作流配置文件
     */
    public void deleteConfig(){
        //删除插入的流程文件
        String sql = "delete from [CherryConfig].[Tools].[BIN_FileStore] where [OrgCode] = 'bingkun' and [BrandCode] = 'FBC' and [FileCategory] = 'WF'";
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
        testCOM_Service.delete(sql);
    }
    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName){
        String rootpath = ProFlowOD_FN.class.getResource("/").getPath();
        rootpath = rootpath.replace("test-classes", "classes");
        String path = rootpath + "worflowfile/st/"+fileName;
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
