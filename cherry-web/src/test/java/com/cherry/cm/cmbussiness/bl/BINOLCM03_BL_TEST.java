package com.cherry.cm.cmbussiness.bl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

 
import com.cherry.CherryJunitBase;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLCM03_BL_TEST extends CherryJunitBase{
    private BINOLCM03_BL bl;
    
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
    public void testGetTicketNumber1() throws Exception{
      String caseName = "testGetTicketNumber1";
      bl = applicationContext.getBean(BINOLCM03_BL.class);
      testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
      List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
      for(int index=0;index<dataList.size();index++){
          String orgId = ConvertUtil.getString(dataList.get(index).get("orgId"));
          String brandId = ConvertUtil.getString(dataList.get(index).get("brandId"));
          String userName = ConvertUtil.getString(dataList.get(index).get("userName"));
          String type = ConvertUtil.getString(dataList.get(index).get("type"));
          String expected = ConvertUtil.getString(dataList.get(index).get("expected"));
          expected = expected.replace("YYMMDD", testCOM_Service.getDateYMD().substring(2).replace("-", ""));
          String ticketNumber = bl.getTicketNumber(orgId, brandId, userName, type);
//          assertEquals(expected,ticketNumber);
          
          if(type.equals("9")){
              type = "MC";
          }else if(type.equals("D")){
              type = "HD";
          }
          String sql = "select top 1 * from Tools.BIN_TicketNumber where Type = '"+ type
                      + "' and BIN_OrganizationInfoID = '" + orgId
                      + "' and BIN_BrandInfoID = '" + brandId
                      + "' order by TicketNo desc";
          List<Map<String,Object>> ticketNumberList = testCOM_Service.select(sql);
          String expectedControlDate = ConvertUtil.getString(dataList.get(index).get("expectedControlDate"));
          if("getDateYMD".equals(expectedControlDate)){
              expectedControlDate = testCOM_Service.getDateYMD();
          }
          assertEquals(expectedControlDate,ticketNumberList.get(0).get("ControlDate"));
      }
    }
  
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetPrefix1() throws Exception{
        bl = applicationContext.getBean(BINOLCM03_BL.class);
        
        //测试私有方法
        Class<?>[] args = new Class[]{String.class};// 建立参数
        Method method = bl.getClass().getDeclaredMethod("getPrefix",args);
        method.setAccessible(true);// 允许处理私有方法
        
        String[][] arr = new String[][]{
                {"1","SD"},{"2","RD"},{"3","RR"},{"4","AR"},{"5","BG"},
                {"6","LG"},{"7","GR"},{"8","OT"},{"P","CA"},{"N","NS"},
                {"R","SR"},{"9","MC"},{"D","HD"},{"S","SP"},{"O","OD"},
                {"MG","MG"},{"MV","MV"},{"LS","LS"},{"SD","SD"},{"RD","RD"},
                {"RR","RR"},{"AR","AR"},{"BG","BG"},{"LG","LG"},{"GR","GR"},
                {"OT","OT"},{"CA","CA"},{"NS","NS"},{"SR","SR"},{"SP","SP"},
                {"OD","OD"},{"IO","IO"},{"UC","UC"},{"KS","KS"},{"ML","ML"},
                {"RU","RU"},{"MR","MR"},{"AS","AS"},{"AT","AT"},{"SE","SE"},
                {"RA","RA"},{"RJ","RJ"},{"CT","CT"},{"CR","CR"},{"CJ","CJ"}
        };
        
        for(int i=0;i<arr.length;i++){
            Object result = method.invoke(bl, new Object[] {arr[i][0]});// 调用方法
            assertEquals(arr[i][1],result);
        }
        
        method.setAccessible(false);
    }
}
