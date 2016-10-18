package com.cherry.bs.cnt.bl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
public class BINOLBSCNT03_BL_TEST extends CherryJunitBase {
		@Resource(name="TESTCOM_Service")
		private TESTCOM_Service testCOM_Service;
		
		@Resource(name="binOLBSCOM01_BL")
		private BINOLBSCOM01_BL binOLBSCOM01_BL;
		
		@Resource(name="binOLCM27_BL")
		private BINOLCM27_BL binOLCM27_BL;
		
		
		@Test
	    @Rollback(true)
	    @Transactional 
	    public void testTran_updateCounterInfo() throws Exception{
	        String caseName = "testTran_updateCounterInfo";
	        //往柜台信息表插入数据
	        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
	        Map<String,Object> mainData = dataList.get(0);
	        int CounterId = testCOM_Service.insertTableData(mainData);
	        mainData.put("counterInfoId", CounterId);
	        //更新柜台数据
	        testCOM_Service.update("Update Basis.BIN_CounterInfo Set CounterSynergyFlag = '0' , PassWord='123456' WHERE BIN_CounterInfoID="+CounterId);
	        //取得柜台信息Map
	        Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(mainData);
	        //品牌code
	        Assert.assertEquals("mgp",ConvertUtil.getString(WSMap.get("BrandCode")));
	        //柜台类型（0：正式柜台 1：测试柜台 2：促销柜台）
	        Assert.assertEquals("0",ConvertUtil.getString(WSMap.get("CounterKind")));
	        //柜台号
	        Assert.assertEquals("abcdef",ConvertUtil.getString(WSMap.get("CounterCode")));
	        //柜台名称
	        Assert.assertEquals("上海柜台0001",ConvertUtil.getString(WSMap.get("CounterName")));
	        //业务类型
	        Assert.assertEquals("Counter",ConvertUtil.getString(WSMap.get("BussinessType")));
	        //柜台协同区分
	        Assert.assertEquals("0",ConvertUtil.getString(WSMap.get("CounterSynergyFlag")));
	        //柜台密码
	        Assert.assertEquals("123456",ConvertUtil.getString(WSMap.get("PassWord")));
	        //版本
	        Assert.assertEquals("1.0",ConvertUtil.getString(WSMap.get("Version")));
	        //柜台有效性区分
	        Assert.assertEquals("1",ConvertUtil.getString(WSMap.get("ValidFlag")));
	        //WebServiceUrl测试地址
	        String WebServiceUrl="http://192.168.101.36/rest/SynData.svc/JSON";
	        //取得WebService返回的map
			Map<String, Object> resultMap = binOLCM27_BL.accessWebService(WebServiceUrl,WSMap);
			//品牌code
			Assert.assertEquals("mgp",ConvertUtil.getString(resultMap.get("BrandCode")));
			//OK：成功；ERROR：有错误发生
			Assert.assertEquals("OK",ConvertUtil.getString(resultMap.get("State")));
			//数据更新信息
			Assert.assertEquals("柜台更新成功", ConvertUtil.getString(resultMap.get("Data")));
	       
	    }
		@Test
	    @Rollback(true)
	    @Transactional 
	    public void testTran_updateCounterInfo1() throws Exception{
	        String caseName = "testTran_updateCounter";
	        //往柜台信息表插入数据
	        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
	        Map<String,Object> mainData = dataList.get(0);
	        int CounterId = testCOM_Service.insertTableData(mainData);
	        mainData.put("counterInfoId", CounterId);
	        //更新柜台数据
	        testCOM_Service.update("Update Basis.BIN_CounterInfo Set CounterSynergyFlag = '1' , PassWord='' WHERE BIN_CounterInfoID="+CounterId);
	        Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(mainData);
	        //品牌code
	        Assert.assertEquals("mgp",ConvertUtil.getString(WSMap.get("BrandCode")));
	        //柜台类型（0：正式柜台 1：测试柜台 2：促销柜台）
	        Assert.assertEquals("0",ConvertUtil.getString(WSMap.get("CounterKind")));
	        //柜台号
	        Assert.assertEquals("abcdef",ConvertUtil.getString(WSMap.get("CounterCode")));
	        //业务类型
	        Assert.assertEquals("Counter",ConvertUtil.getString(WSMap.get("BussinessType")));
	        //柜台协同区分
	        Assert.assertEquals("1",ConvertUtil.getString(WSMap.get("CounterSynergyFlag")));
	        //柜台密码
	        Assert.assertEquals("",ConvertUtil.getString(WSMap.get("PassWord")));
	        //版本
	        Assert.assertEquals("1.0",ConvertUtil.getString(WSMap.get("Version")));
	        //柜台有效区分
	        Assert.assertEquals("1",ConvertUtil.getString(WSMap.get("ValidFlag")));
	        //WebServiceUrl测试地址
	        String WebServiceUrl="http://192.168.101.36/rest/SynData.svc/JSON";
	        //取得WebService返回的map
			Map<String, Object> resultMap = binOLCM27_BL.accessWebService(WebServiceUrl,WSMap);
			//品牌code
			Assert.assertEquals("mgp",ConvertUtil.getString(resultMap.get("BrandCode")));
			//OK：成功；ERROR：有错误发生
			Assert.assertEquals("OK",ConvertUtil.getString(resultMap.get("State")));
			//数据更新信息
			Assert.assertEquals("柜台更新成功", ConvertUtil.getString(resultMap.get("Data")));
	       
	    }
	    
}
