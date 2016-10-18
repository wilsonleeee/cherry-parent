package com.cherry.cm.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CherryUtil_TEST {
	   
    @Test
    public void testReplaceMQSpecialChar() throws Exception {
    	assertEquals("理由，~Test",CherryUtil.replaceMQSpecialChar("理由,'Test"));
    	assertEquals("理由，，~~Test",CherryUtil.replaceMQSpecialChar("理由,,''Test"));
    	
    	Map<String,Object> actualMap = new HashMap<String,Object>();
    	actualMap.put("key1", "理由,'Test");
    	actualMap.put("key2", "理由,,''Test");
    	actualMap.put("key3", "");
    	actualMap.put("key4", new HashMap<String,Object>());
        Map<String,Object> expectedMap = new HashMap<String,Object>();
        expectedMap.put("key1", "理由，~Test");
        expectedMap.put("key2", "理由，，~~Test");
        expectedMap.put("key3", "");
        expectedMap.put("key4", new HashMap<String,Object>());
        assertEquals(expectedMap,CherryUtil.replaceMQSpecialChar(actualMap));
    }

}
