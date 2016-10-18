package com.cherry.pt.jcs.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.jcs.action.BINOLPTJCS04_Action;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS04_IF;

public class BINOLPTJCS04_BL_TEST extends CherryJunitBase{
    
    private BINOLPTJCS04_Action action;
    
    private BINOLPTJCS04_IF bl;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testExportExcel() throws Exception {
        String caseName = "testExportExcel1"; 
        action = createAction(BINOLPTJCS04_Action.class, "/pt", "BINOLPTJCS04_export");
        bl = applicationContext.getBean(BINOLPTJCS04_IF.class);
        
        String sql = "UPDATE Tools.BIN_SystemConfig SET ConfigValue = '3' WHERE ConfigCode = '1031'";
        testCOM_Service.update(sql);
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.setSession(session);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("brandInfoId", action.getModel().getBrandInfoId());
        byte[] b = bl.exportExcel(map);
        try {
            InputStream is =new ByteArrayInputStream(b);
            Workbook wb = null;
            wb = Workbook.getWorkbook(is);
            if (null != wb) {
                Sheet[] sheets = wb.getSheets();
                Sheet dateSheet = null;
                dateSheet = sheets[0];
                assertEquals("第1～3条",dateSheet.getName());
                assertEquals(4,dateSheet.getRows());
            }
        } catch (Exception e) {
            fail("导出Excel错误！");
        }
    }
}
