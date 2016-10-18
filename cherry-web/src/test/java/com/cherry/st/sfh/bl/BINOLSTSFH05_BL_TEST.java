package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.interfaces.BINOLSTCM11_IF;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH05_IF;

public class BINOLSTSFH05_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTSFH05_BL")
    private BINOLSTSFH05_IF bl;
    
    @Resource(name="binOLSTCM11_BL")
    private BINOLSTCM11_IF binOLSTCM11_BL;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testReceiveDeliverByForm1() throws Exception {
        String caseName = "testReceiveDeliverByForm1";
        bl = applicationContext.getBean(BINOLSTSFH05_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        //Inventory.BIN_ProductDeliver
        Map<String,Object> insertProductDeliver = dataList.get(0);
        int deliverID = testCOM_Service.insertTableData(insertProductDeliver);
        
        //Inventory.BIN_ProductDeliverDetail
        Map<String,Object> insertProductDeliverDetail1 = dataList.get(1);
        insertProductDeliverDetail1.put("BIN_ProductDeliverID", deliverID);
        testCOM_Service.insertTableData(insertProductDeliverDetail1);
        
        //Inventory.BIN_ProductDeliverDetail
        Map<String,Object> insertProductDeliverDetail2 = dataList.get(2);
        insertProductDeliverDetail2.put("BIN_ProductDeliverID", deliverID);
        testCOM_Service.insertTableData(insertProductDeliverDetail2);
        
        BINOLSTSFH05_Form form = new BINOLSTSFH05_Form();
        //加载form测试数据
        DataUtil.getForm(this.getClass(), caseName,form);
        form.setProductDeliverId(String.valueOf(deliverID));
        int billID = bl.receiveDeliverByForm(form, "TestCase", 1, 2);
        
        Map<String,Object> actualMap = binOLSTCM11_BL.getProductReceiveMainData(billID, null);
        assertEquals(ConvertUtil.getString(insertProductDeliver.get("WorkFlowID")),ConvertUtil.getString(actualMap.get("WorkFlowID")));
    }
}