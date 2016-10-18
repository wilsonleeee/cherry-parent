package com.cherry.wp.wo.set.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.wo.set.interfaces.BINOLWOSET01_IF;
import com.cherry.wp.wo.set.service.BINOLWOSET01_Service;

/**
 * 营业员管理BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWOSET01_BL implements BINOLWOSET01_IF {
	
	/** 营业员管理Service **/
	@Resource
	private BINOLWOSET01_Service binOLWOSET01_Service;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;

	@Override
	public int getBAInfoCount(Map<String, Object> map) throws Exception {
		this.encryptData(map);
		return binOLWOSET01_Service.getBAInfoCount(map);
	}

	@Override
	public List<Map<String, Object>> getBAInfoList(Map<String, Object> map) throws Exception {
//		this.encryptData(map);
		List<Map<String, Object>> baInfoList = binOLWOSET01_Service.getBAInfoList(map);
		if(baInfoList != null && !baInfoList.isEmpty()) {
			for(int i = 0; i < baInfoList.size(); i++) {
				Map<String, Object> baInfoMap = baInfoList.get(i);
				this.decryptListData(map, baInfoMap);
				String identityCard = (String)baInfoMap.get("identityCard");
				if(identityCard != null && !"".equals(identityCard)) {
					baInfoMap.put("identityCard", CherryUtil.replaceSubString(identityCard, 4, 4));
				}
			}
		}
		return baInfoList;
	}

	@Override
	public void tran_addBA(Map<String, Object> map) throws Exception {
		
		String employeeId = (String)map.get("employeeId");
		if(employeeId == null || "".equals(employeeId)) {
			map.put("path", CherryConstants.DUMMY_VALUE);
			// 取得新节点
			String newEmpNodeId = binOLWOSET01_Service.getNewEmpNodeId(map);
			map.put("newNodeId", newEmpNodeId);
			map.put("categoryCode", "01");
			Integer posId = (Integer)binOLWOSET01_Service.getPosIdByCode(map);
			if(posId != null) {
				map.put("positionCategoryId", posId);
			}
			Integer orgId = (Integer)map.get(CherryConstants.ORGANIZATIONINFOID);
			Integer brandId = (Integer)map.get(CherryConstants.BRANDINFOID);
			String employeeCode = binOLCM15_BL.getSequenceId(orgId, brandId, "1");
			map.put("employeeCode", employeeCode);
			while(true) {
				Map<String, Object> empMap = binOLWOSET01_Service.getEmployeeId(map);
				if(empMap != null) {
					employeeCode = binOLCM15_BL.getSequenceId(orgId, brandId, "1");
					map.put("employeeCode", employeeCode);
				} else {
					break;
				}
			}
			this.encryptData(map);
			int _employeeId = binOLWOSET01_Service.insertEmployee(map);
			map.put("employeeId", _employeeId);
			binOLWOSET01_Service.insertBaInfo(map);
			this.sendBAMQMsg(map);
		} else {
			Integer empId = binOLWOSET01_Service.getEmpCouInfo(map);
			if(empId != null) {
				throw new CherryException("EWP00001");
			}
		}
		binOLWOSET01_Service.insertEmpCou(map);
	}

	@Override
	public Map<String, Object> getBAInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> baInfoMap = binOLWOSET01_Service.getEmployeeInfo(map);
		this.decryptListData(map, baInfoMap);
		return baInfoMap;
	}

	@Override
	public void tran_updBA(Map<String, Object> map) throws Exception {
		this.encryptData(map);
		binOLWOSET01_Service.updEmpInfo(map);
		binOLWOSET01_Service.updBAInfo(map);
		this.sendBAMQMsg(map);
	}

	@Override
	public void tran_delBA(Map<String, Object> map) throws Exception {
		binOLWOSET01_Service.delEmpCou(map);
	}

	@Override
	public Map<String, Object> checkBAInfo(Map<String, Object> map) throws Exception {
		this.encryptData(map);
		Map<String, Object> baInfoMap = binOLWOSET01_Service.getEmployeeId(map);
		this.decryptListData(map, baInfoMap);
		return baInfoMap;
	}
	
	/***
	 * 发送BAMQ消息
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void sendBAMQMsg(Map<String, Object> map) throws Exception {
		// BA有效性	0 有效；1 无效
		map.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
		Map<String,Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(map, null,"BA");
		if(MQMap.isEmpty()) return;
		//设定MQInfoDTO
		MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
		//调用共通发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
	}
	
	/***
	 * 加密身份证号、电话信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void encryptData(Map<String, Object> map) throws Exception {
		// 所谓品牌Code，【加密参数】
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		if (!CherryChecker.isNullOrEmpty(map.get("identityCard"), true)) {
			String identityCard = ConvertUtil.getString(map.get("identityCard"));
			map.put("identityCard", CherrySecret.encryptData(brandCode, identityCard));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode, mobilePhone));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("identityCardQ"), true)) {
			String identityCard = ConvertUtil.getString(map.get("identityCardQ"));
			map.put("identityCardQ", CherrySecret.encryptData(brandCode, identityCard));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhoneQ"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhoneQ"));
			map.put("mobilePhoneQ", CherrySecret.encryptData(brandCode, mobilePhone));
		}
	}
	
	/**
	 * 解密指定的人员信息
	 * 
	 * @param map
	 * @param employeeInfo
	 * @throws Exception
	 */
	private void decryptListData(Map<String, Object> map, Map<String, Object> employeeInfo) throws Exception {
		if(employeeInfo != null) {
			// 品牌Code
			String  brandCode = ConvertUtil.getString(map.get("brandCode"));
			// 手机号码解密
			if (!CherryChecker.isNullOrEmpty(employeeInfo.get("mobilePhone"), true)) {
				String  mobilePhone = ConvertUtil.getString(employeeInfo.get("mobilePhone"));
				employeeInfo.put("mobilePhone",CherrySecret.decryptData(brandCode, mobilePhone));
			}
			// 身份证解密
			if (!CherryChecker.isNullOrEmpty(employeeInfo.get("identityCard"), true)) {
				String identityCard = ConvertUtil.getString(employeeInfo.get("identityCard"));
				employeeInfo.put("identityCard", CherrySecret.decryptData(brandCode, identityCard));
			}
		}
	}
	
	/**
	 * 同步BA数据方法
	 * （1）从新后台Employee表中取当前柜台的BA数据
	 * （2）插入/更新柜台关系表中
	 */
	@Override
	public void synaBa(Map<String, Object> map) throws Exception {
		//（1）从新后台Employee表中取当前柜台的BA数据
		List<Map<String,Object>> Balist=binOLWOSET01_Service.getBaListByOrganizationId(map);
		//（2）插入/更新柜台关系表中
		binOLWOSET01_Service.synaBa(Balist);
	}

}
