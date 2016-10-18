package com.cherry.cm.cmbussiness.bl;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cherry.CherryJunitBase;

public class BINOLCM27_BL_TEST extends CherryJunitBase {
	
	@Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    public void testAccessWebService() throws Exception {
    	
    	BINOLCM27_BL binOLCM27_BL = applicationContext.getBean(BINOLCM27_BL.class);
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("BrandCode", "mgp");
    	paramMap.put("BussinessType", "Counter");
    	paramMap.put("Version", "1.0");
    	paramMap.put("CounterCode", "test");
    	paramMap.put("CounterName", "test");
    	paramMap.put("CounterSynergyFlag", "1");
    	paramMap.put("ValidFlag", "1");
    	try {
    		Map<String,Object> resultMap = binOLCM27_BL.accessWebService(paramMap);
    		Assert.assertEquals("访问WebService失败！", resultMap != null, true);
    	} catch (Exception e) {
    		Assert.fail(e.getMessage());
		}
    }

}
