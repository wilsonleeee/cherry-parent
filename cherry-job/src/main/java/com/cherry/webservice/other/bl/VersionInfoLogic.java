/*  
 * @(#)VersionInfoLogic.java     1.0 2016/09/05    
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

package com.cherry.webservice.other.bl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.other.service.VersionInfoService;

/**
 * 
 * 终端版本号变更BL
 * 
 * 
 */
public class VersionInfoLogic implements IWebservice{
	
    @Resource(name="versionInfoService")
    private VersionInfoService versionInfoService;
    
    /**
     * 终端版本号变更
     * @param map
     * @return
     */

	@Override
	public Map tran_execute(Map map) throws Exception {
		 Map<String, Object> retMap = new HashMap<String, Object>();
	        try{
	            String subType = ConvertUtil.getString(map.get("SubType"));
	            if(subType.equals("")){
	                retMap.put("ERRORCODE", "EOT01001");
	                retMap.put("ERRORMSG", "SubType 的值不能为空");
	                return retMap;
	            }
	            if(!subType.equals("A") && !subType.equals("D")){
	                retMap.put("ERRORCODE", "EOT01001");
	                retMap.put("ERRORMSG", "SubType必须是A或D。");
	                return retMap;
	            }
	            
            	List<Map<String,Object>> lis = CherryUtil.json2ArryList(ConvertUtil.getString(map.get("DataList")));
    			StringBuffer re=new StringBuffer("");
    			Boolean bo=true;
    			for (int i = 0; i < lis.size(); i++) {
    				String str=(String) lis.get(i).get("VersionValue");
					if (isValidDate(str)==false) {
						re.append("第"+(i+1)+"条数据的VersionValue格式错误，");
						bo=false;
					}
				}
    			if(bo==false){
    				 retMap.put("ERRORCODE", "EOT01002");
 	                 retMap.put("ERRORMSG", re.substring(0, re.length()-1));
 	                return retMap;
    				
    			}
    			for (Map<String, Object> versionParam : lis) {
    			    versionParam.put("Type", versionParam.get("VersionType"));
				    Map<String, Object> m=subStr((String) versionParam.get("VersionValue"));
				    versionParam.put("Year", m.get("year"));
				    versionParam.put("Month", m.get("month"));
				}
    			if (subType.equals("A")) {
    				for (Map<String, Object> versionParam : lis) {
	 	                	versionInfoService.insertVersion(versionParam);
					}
				}else{
					versionInfoService.deleteVersion(lis);
				}
    			retMap.put("ERRORCODE", "0");
 	            retMap.put("ERRORMSG", "处理成功。");
	            return retMap;
	        }catch(Exception ex){
	            retMap.put("ERRORCODE", "WSE9999");
	            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
	            return retMap;
	        }
	}
    /**
     * 判断日期格式是否符合要求
     * @param string
     * @return  boolean
     */
    public boolean isValidDate(String s){
    	 DateFormat dateFormat= new SimpleDateFormat("yyyy-MM");
        try
        {
             dateFormat.parse(s);
             if (s.length()!=7) {
            	 return false;
			}
             return true;
         }
        catch (Exception e)
        {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }
    /**
     * 把字符串转换成需要的年、月
     * @param string
     * @return map
     */
    public Map<String, Object> subStr(String s){
    	Map<String, Object> map=new HashMap<String, Object>();
    	map.put("year", s.substring(0,4));
    	map.put("month", s.substring(5));
    	return map;
    }
    }