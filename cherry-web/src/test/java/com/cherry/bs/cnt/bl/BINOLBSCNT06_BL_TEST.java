package com.cherry.bs.cnt.bl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.cnt.service.BINOLBSCNT06_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
public class BINOLBSCNT06_BL_TEST extends CherryJunitBase {
	@Resource(name="binOLBSCOM01_BL")
		private BINOLBSCOM01_BL binOLBSCOM01_BL;
		
		@Resource(name="binOLCM27_BL")
		private BINOLCM27_BL binOLCM27_BL;
		
		@Resource(name="binOLBSCNT06_Service")
		private BINOLBSCNT06_Service binOLBSCNT06_Service;
		
		
		@Test
	    @Rollback(true)
	    @Transactional 
	    public void testTran_addCounterInfo() throws Exception{
	        String caseName = "testTran_addCounterInfo";
	        //往柜台信息表插入数据
	        int CounterId=0;
	        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
			if (dataList != null) {
					// 将数据插入到数据库
					// 插入柜台信息
					for(int i=0;i<dataList.size();i++){
				        Map<String,Object> mainData = dataList.get(i);
						CounterId = binOLBSCNT06_Service.insertCounterInfo(mainData);
				        mainData.put("counterInfoId", CounterId);
				        //柜台信息Map
				        Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(mainData);
				        //WebServiceUrl测试地址
				        String WebServiceUrl="http://192.168.101.36/rest/SynData.svc/JSON";
				        //取得WebService返回的map
						Map<String, Object> resultMap = binOLCM27_BL.accessWebService(WebServiceUrl,WSMap);
						Assert.assertEquals("mgp",ConvertUtil.getString(resultMap.get("BrandCode")));
						Assert.assertEquals("OK",ConvertUtil.getString(resultMap.get("State")));
						Assert.assertEquals("柜台更新成功", ConvertUtil.getString(resultMap.get("Data")));
				}
				
			}
	    }
}
