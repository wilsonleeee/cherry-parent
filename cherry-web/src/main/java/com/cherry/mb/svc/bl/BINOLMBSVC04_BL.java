package com.cherry.mb.svc.bl;

import java.util.Map;
import javax.annotation.Resource;
import com.cherry.mb.svc.interfaces.BINOLMBSVC04_IF;
import com.cherry.mb.svc.service.BINOLMBSVC04_Service;

public class BINOLMBSVC04_BL implements BINOLMBSVC04_IF{

@Resource
private BINOLMBSVC04_Service binOLMBSVC04_Service;

@Override
public Map<String, Object> getRangeInfo(Map<String, Object> map) {
	return binOLMBSVC04_Service.getRangeInfo(map);
}

@Override
/**
 * 保存范围值
 * 
 * @param map
 * @return
 *
 */
public void updateRangeInfo(Map<String, Object> map){
	binOLMBSVC04_Service.updateRangeInfo(map);		
}

public void insertRangeInfo(Map<String, Object> map){
	binOLMBSVC04_Service.insertRangeInfo(map);		
}

}
