package com.cherry.hmw.ft.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.hmw.ft.service.BINBEHMWFT01_Service;

/**
 * 海名微利润分摊BL
 * 
 * @author songka
 * @version 1.0 2015.09.08
 */

public class BINBEHMWFT01_BL{
	
	
	private static Logger logger = LoggerFactory.getLogger(BINBEHMWFT01_BL.class.getName());
	
	@Resource
	private BINBEHMWFT01_Service binBEHMWFT01_Service;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	public int profitRebate(Map<String, Object> paraMap) throws Exception{
		paraMap.put("CodeType", "1000");
		List<Map<String, Object>> rebateDivideList = new ArrayList<Map<String,Object>>();
		float[] rebateDivide;
		try {
			// 返利百分比信息
			try {
				rebateDivideList = binBEHMWFT01_Service.getRebateDivideList();
				rebateDivide = new float[rebateDivideList.size()];
				if(rebateDivideList.size()>0){
					for(int i=0;i<rebateDivideList.size();i++){
						if(!"".equals(ConvertUtil.getString(rebateDivideList.get(i).get("DividePer")))){
							rebateDivide[i]=Float.parseFloat(ConvertUtil.getString(rebateDivideList.get(i).get("DividePer")));
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return CherryBatchConstants.BATCH_ERROR_PR3;
			}
			// 销售信息
			List<Map<String, Object>> salList = new ArrayList<Map<String,Object>>();
			// 修改销售记录为已返利分摊
			List<Map<String, Object>> updList = new ArrayList<Map<String,Object>>();
			try {
				// 取得系统销售利润分摊模式配置
				String organizationInfoId = ConvertUtil.getString(paraMap.get("organizationInfoId"));
				String brandInfoId = ConvertUtil.getString(paraMap.get("brandInfoId"));
				String DeliveryModelType = binOLCM14_BL.getConfigValue("1348", organizationInfoId, brandInfoId);
				if("1".equals(DeliveryModelType)){
					logger.info("================海名微利润分摊开始=============");
					// 取出销售数据(海名微)
					salList = binBEHMWFT01_Service.getSalList(paraMap);
					if(salList.size()>0){
						updList.addAll(salList);
						List<Map<String, Object>> pMapList = new ArrayList<Map<String,Object>>();
						for(Map<String, Object> m : salList){
							// 提成比率
							int fl = 0;
							Integer SaleEmployeeLevel = null;
							if(!"".equals(ConvertUtil.getString(m.get("CodeKey")))){
								// 提成人等级
								paraMap.put("CodeKey", m.get("CodeKey"));
								Integer s = binBEHMWFT01_Service.getLevel(paraMap);
								//等级减1是因为与返利百分比数组下标对应，减3是因为等级去掉前3个等级返利数组长度为3，而等级数据还存在
								if(null!=s && s>3){
									SaleEmployeeLevel = s-1-3;
									for(int i=rebateDivideList.size()-1;i>=SaleEmployeeLevel;i--){
										fl+=rebateDivide[i];
									}
								}else {
									continue;
								}
								m.put("CommissionProportion", fl);
							}
							// 利润
							float Profit = 0;
							if(!"".equals(ConvertUtil.getString(m.get("SaleProfit")))){
								Profit = Float.parseFloat(ConvertUtil.getString(m.get("SaleProfit")));
							};
							// 提成金额
							m.put("RoyaltyAmount", Profit*fl/100);
							m.put("CommissionEmployeeCode", m.get("SaleEmployeeCode"));
							m.put("CommissionEmployeeName", m.get("SaleEmployeeName"));
							m.put("CommissionEmployeeLevel", SaleEmployeeLevel);
							m.put("CommissionOrganizationID", m.get("BIN_OrganizationID"));
							m.put("BatchNo", paraMap.get("batchId"));
							m.put("createdBy", "BATCH");
							m.put("createPGM", "BINBEHMWFT01");
							m.put("SaleEmployeeLevel", SaleEmployeeLevel);
							
							// 上级人的提成情况
							String SupervisorID = null;
							if(!"".equals(ConvertUtil.getString(m.get("SupervisorID")))){
								SupervisorID = m.get("SupervisorID").toString();
								paraMap.put("SupervisorID", SupervisorID);
							};
							Integer LevelL = SaleEmployeeLevel;
							try {
								while(true){
									// 指向总部的SupervisorID不存在，所以为空时认为是总部
									if(null==SupervisorID || "".equals(SupervisorID) ){
										break;
									}
									Map<String, Object> cm = binBEHMWFT01_Service.getEmployee(paraMap);
									if(null!=cm && !cm.isEmpty()){
										if(!"".equals(ConvertUtil.getString(cm.get("CodeKey")))){
											paraMap.put("CodeKey", cm.get("CodeKey"));
											Integer s = binBEHMWFT01_Service.getLevel(paraMap);
											if(null!=s && s>3){
												cm.put("Level", s-1-3);
											}else {
												cm.put("Level", null);
											}
										}
										if(!"".equals(ConvertUtil.getString(cm.get("SupervisorID")))){
											SupervisorID = ConvertUtil.getString(cm.get("SupervisorID"));
											paraMap.put("SupervisorID", SupervisorID);
										}else {
											SupervisorID = null;
										}
										// 下级等级(比当前等级小)
										if(null==LevelL && !"".equals(ConvertUtil.getString(m.get("CommissionEmployeeLevel")))){
											LevelL = Integer.parseInt(ConvertUtil.getString(m.get("CommissionEmployeeLevel")));
										}
										//查询出的最高等级1
										Map<String, Object> sm = new HashMap<String, Object>();
										
										sm.put("SaleCount", m.get("SaleCount"));
										sm.put("Amount", m.get("Amount"));
										sm.put("Channel", m.get("Channel"));
										sm.put("SaleProfit", m.get("SaleProfit"));
										sm.put("BillCodePre", m.get("BillCodePre"));
										sm.put("SaleEmployeeName", m.get("SaleEmployeeName"));
										sm.put("BIN_BrandInfoID", m.get("BIN_BrandInfoID"));
										sm.put("SaleRecordCode", m.get("SaleRecordCode"));
										sm.put("SaleType", m.get("SaleType"));
										sm.put("SaleTime", m.get("SaleTime"));
										sm.put("BIN_EmployeeID", m.get("BIN_EmployeeID"));
										sm.put("BIN_OrganizationID", m.get("BIN_OrganizationID"));
										sm.put("SaleEmployeeCode", m.get("SaleEmployeeCode"));
										sm.put("SaleEmployeeLevel", m.get("SaleEmployeeLevel"));
										sm.put("BIN_OrganizationInfoID", m.get("BIN_OrganizationInfoID"));
										sm.put("BillCode", m.get("BillCode"));
										
										sm.put("CommissionEmployeeCode", cm.get("EmployeeCode"));
										sm.put("CommissionEmployeeName", cm.get("EmployeeName"));
										sm.put("CommissionEmployeeLevel", cm.get("Level"));
										sm.put("CommissionOrganizationID", cm.get("BIN_OrganizationID"));
										sm.put("BatchNo", paraMap.get("batchId"));
										sm.put("createdBy", "BATCH");
										sm.put("createPGM", "BINBEHMWFT01");
										if(!"".equals(ConvertUtil.getString(cm.get("CodeKey")))){
											paraMap.put("CodeKey", cm.get("CodeKey"));
											Integer s = binBEHMWFT01_Service.getLevel(paraMap);
											if(null!=s && s>3){
												// 当前提成人等级
												int LevelR = s-1-3;
												int pfl = 0;
												for(int i=LevelR;i<LevelL;i++){
													pfl+=rebateDivide[i];
												}
												fl = pfl;
												sm.put("CommissionProportion", fl);
												// 提成金额
												sm.put("RoyaltyAmount", Profit*fl/100);
												
											}else {
												break;
											}
										}
										pMapList.add(sm);
										if(!"".equals(ConvertUtil.getString(sm.get("CommissionEmployeeLevel")))){
											LevelL = Integer.parseInt(ConvertUtil.getString(sm.get("CommissionEmployeeLevel")));
										}
									}else {
										break;
									}
								}
							} catch (Exception e) {
								logger.info(e.getMessage(),e);
								return CherryBatchConstants.BATCH_ERROR_PR1;
							}
						}
						salList.addAll(pMapList);
						if(salList.size()>0){
							for(Map<String,Object> pm:salList){
								binBEHMWFT01_Service.updateProfitRebate(pm);
							}
							try {
								binBEHMWFT01_Service.InsertRebateDivideList(salList);
							} catch (Exception e) {
								logger.info(e.getMessage(),e);
								return CherryBatchConstants.BATCH_ERROR_PR2;
							}
						}
						if(updList.size()>0){
							for(Map<String, Object> m : updList){
								m.put("updatedBy", "BATCH");
								m.put("updatePGM", "BINBEHMWFT01");
							}
							binBEHMWFT01_Service.updSaleRecord(updList);
						}
					}
					logger.info("================海名微本次利润分摊结束=============");
				}else if("0".equals(DeliveryModelType)){
					logger.info("================爱沐空间利润分摊开始=============");
					// 取出销售数据(爱沐空间)
					salList = binBEHMWFT01_Service.getSalList2(paraMap);
					if(salList.size()>0){
						updList.addAll(salList);
						List<Map<String, Object>> pMapList = new ArrayList<Map<String,Object>>();
						for(Map<String, Object> m : salList){
							// 提成比率
							int fl = 0;
							Integer SaleEmployeeLevel = null;
							if(!"".equals(ConvertUtil.getString(m.get("CodeKey")))){
								// 提成人等级
								paraMap.put("CodeKey", m.get("CodeKey"));
								Integer s = binBEHMWFT01_Service.getLevel(paraMap);
								//等级减1是因为与返利百分比数组下标对应，减3是因为等级去掉前3个等级返利数组长度为3，而等级数据还存在
								if(null!=s && s>3){
									SaleEmployeeLevel = s-1-3;
									for(int i=rebateDivideList.size()-1;i>=SaleEmployeeLevel;i--){
										fl+=rebateDivide[i];
									}
								}else {
									continue;
								}
								m.put("CommissionProportion", fl);
							}
							// 利润
							float Profit = 0;
							if(!"".equals(ConvertUtil.getString(m.get("SaleProfit")))){
								Profit = Float.parseFloat(ConvertUtil.getString(m.get("SaleProfit")));
								if(Profit < 0){
									Profit = 0;
								}
							};
							// 提成金额
							if(SaleEmployeeLevel!=null && SaleEmployeeLevel==1){
								m.put("RoyaltyAmount", Profit*fl/100);
							}else if(SaleEmployeeLevel!=null && SaleEmployeeLevel==0){
								if("1".equals(ConvertUtil.getString(m.get("DeliveryModel")))){
									m.put("RoyaltyAmount", m.get("Amount"));
								}else if ("2".equals(ConvertUtil.getString(m.get("DeliveryModel")))){
									m.put("RoyaltyAmount", Profit*fl/100);
								}
							}
							m.put("CommissionEmployeeCode", m.get("SaleEmployeeCode"));
							m.put("CommissionEmployeeName", m.get("SaleEmployeeName"));
							m.put("CommissionEmployeeLevel", SaleEmployeeLevel);
							m.put("CommissionOrganizationID", m.get("BIN_OrganizationID"));
							m.put("BatchNo", paraMap.get("batchId"));
							m.put("createdBy", "BATCH");
							m.put("createPGM", "BINBEHMWFT01");
							m.put("SaleEmployeeLevel", SaleEmployeeLevel);
							
							// 上级人的提成情况
							String SupervisorID = null;
							if(!"".equals(ConvertUtil.getString(m.get("SupervisorID")))){
								SupervisorID = ConvertUtil.getString(m.get("SupervisorID"));
								paraMap.put("SupervisorID", SupervisorID);
							};
							Integer LevelL = SaleEmployeeLevel;
							try {
								while(true){
									// 指向总部的SupervisorID不存在，所以为空时认为是总部
									if(null==SupervisorID || "".equals(SupervisorID)){
										break;
									}
									Map<String, Object> cm = binBEHMWFT01_Service.getEmployee(paraMap);
									if(null!=cm && !cm.isEmpty()){
										if(!"".equals(ConvertUtil.getString(cm.get("CodeKey")))){
											paraMap.put("CodeKey", cm.get("CodeKey"));
											Integer s = binBEHMWFT01_Service.getLevel(paraMap);
											if(null!=s && s>3){
												cm.put("Level", s-1-3);
											}else {
												cm.put("Level", null);
											}
										}
										if(!"".equals(ConvertUtil.getString(cm.get("SupervisorID")))){
											SupervisorID = ConvertUtil.getString(cm.get("SupervisorID"));
											paraMap.put("SupervisorID", SupervisorID);
										}else {
											SupervisorID = null;
										}
										// 下级等级(比当前等级小)
										if(null==LevelL && !"".equals(ConvertUtil.getString(m.get("CommissionEmployeeLevel")))){
											LevelL = Integer.parseInt(ConvertUtil.getString(m.get("CommissionEmployeeLevel")));
										}
										Map<String, Object> sm = new HashMap<String, Object>();
										sm.put("SaleCount", m.get("SaleCount"));
										sm.put("Amount", m.get("Amount"));
										sm.put("Channel", m.get("Channel"));
										sm.put("SaleProfit", m.get("SaleProfit"));
										sm.put("BillCodePre", m.get("BillCodePre"));
										sm.put("SaleEmployeeName", m.get("SaleEmployeeName"));
										sm.put("BIN_BrandInfoID", m.get("BIN_BrandInfoID"));
										sm.put("SaleRecordCode", m.get("SaleRecordCode"));
										sm.put("SaleType", m.get("SaleType"));
										sm.put("SaleTime", m.get("SaleTime"));
										sm.put("BIN_EmployeeID", m.get("BIN_EmployeeID"));
										sm.put("BIN_OrganizationID", m.get("BIN_OrganizationID"));
										sm.put("SaleEmployeeCode", m.get("SaleEmployeeCode"));
										sm.put("SaleEmployeeLevel", m.get("SaleEmployeeLevel"));
										sm.put("BIN_OrganizationInfoID", m.get("BIN_OrganizationInfoID"));
										sm.put("BillCode", m.get("BillCode"));
										
										sm.put("CommissionEmployeeCode", cm.get("EmployeeCode"));
										sm.put("CommissionEmployeeName", cm.get("EmployeeName"));
										sm.put("CommissionEmployeeLevel", cm.get("Level"));
										sm.put("CommissionOrganizationID", cm.get("BIN_OrganizationID"));
										sm.put("BatchNo", paraMap.get("batchId"));
										sm.put("createdBy", "BATCH");
										sm.put("createPGM", "BINBEHMWFT01");
										if(!"".equals(ConvertUtil.getString(cm.get("CodeKey")))){
											paraMap.put("CodeKey", cm.get("CodeKey"));
											Integer s = binBEHMWFT01_Service.getLevel(paraMap);
											if(null!=s && s>3){
												// 当前提成人等级
												int LevelR = s-1-3;
												int pfl = 0;
												if(LevelL!=null){
													for(int i=LevelR;i<LevelL;i++){
														pfl+=rebateDivide[i];
													}
												}
												sm.put("CommissionProportion", pfl);
												// 提成金额
												if(!"".equals(ConvertUtil.getString(m.get("Amount"))) && LevelR==0){
													if("1".equals(ConvertUtil.getString(m.get("DeliveryModel")))){
														float ra = Float.valueOf(ConvertUtil.getString(m.get("Amount")));
														sm.put("RoyaltyAmount", ra-(Profit*fl/100));
													}else if("2".equals(ConvertUtil.getString(m.get("DeliveryModel")))){
														sm.put("RoyaltyAmount", Profit*pfl/100);
													}
												}else if(LevelR==2){
													sm.put("RoyaltyAmount", Profit*fl/100);
												}
											}else {
												break;
											}
										}
										pMapList.add(sm);
										if(!"".equals(ConvertUtil.getString(sm.get("CommissionEmployeeLevel")))){
											LevelL = Integer.parseInt(ConvertUtil.getString(sm.get("CommissionEmployeeLevel")));
										}
									}else {
										break;
									}
								}
							} catch (Exception e) {
								logger.info(e.getMessage(),e);
								return CherryBatchConstants.BATCH_ERROR_PR1;
							}
						}
						salList.addAll(pMapList);
						if(salList.size()>0){
							for(Map<String,Object> pm:salList){
								binBEHMWFT01_Service.updateProfitRebate(pm);
							}
							try {
								binBEHMWFT01_Service.InsertRebateDivideList(salList);
							} catch (Exception e) {
								logger.info(e.getMessage(),e);
								return CherryBatchConstants.BATCH_ERROR_PR2;
							}
						}
						if(updList.size()>0){
							for(Map<String, Object> m : updList){
								m.put("updatedBy", "BATCH");
								m.put("updatePGM", "BINBEHMWFT01");
							}
							binBEHMWFT01_Service.updSaleRecord(updList);
						}
					}
				}
				logger.info("================爱沐空间本次利润分摊结束=============");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return CherryBatchConstants.BATCH_ERROR_PR4;
			}
			/*if(salList.size()>0){
				updList.addAll(salList);
				List<Map<String, Object>> pMapList = new ArrayList<Map<String,Object>>();
				for(Map<String, Object> m : salList){
					// 提成比率
					int fl = 0;
					Integer SaleEmployeeLevel = null;
					if(null!=m.get("CodeKey") && ""!=m.get("CodeKey")){
						// 提成人等级
						paraMap.put("CodeKey", m.get("CodeKey"));
						Integer s = binBEHMWFT01_Service.getLevel(paraMap);
						//等级减1是因为与返利百分比数组下标对应，减3是因为等级去掉前3个等级返利数组长度为3，而等级数据还存在
						if(null!=s && s>3){
							SaleEmployeeLevel = s-1-3;
							for(int i=rebateDivideList.size()-1;i>=SaleEmployeeLevel;i--){
								fl+=rebateDivide[i];
							}
						}else {
							continue;
						}
						m.put("CommissionProportion", fl);
					}
					// 利润
					float Profit = 0;
					if(null!=m.get("SaleProfit") && ""!=m.get("SaleProfit")){
						Profit = Float.parseFloat(m.get("SaleProfit").toString());
					};
					// 提成金额
					m.put("RoyaltyAmount", Profit*fl/100);
					m.put("CommissionEmployeeCode", m.get("SaleEmployeeCode"));
					m.put("CommissionEmployeeName", m.get("SaleEmployeeName"));
					m.put("CommissionEmployeeLevel", SaleEmployeeLevel);
					m.put("CommissionOrganizationID", m.get("BIN_OrganizationID"));
					m.put("BatchNo", paraMap.get("batchId"));
					m.put("createdBy", "BATCH");
					m.put("createPGM", "BINBEHMWFT01");
					m.put("SaleEmployeeLevel", SaleEmployeeLevel);
					
					// 上级人的提成情况
					String SupervisorID = null;
					if(null!=m.get("SupervisorID") && ""!=m.get("SupervisorID")){
						SupervisorID = m.get("SupervisorID").toString();
						paraMap.put("SupervisorID", SupervisorID);
					};
					Integer LevelL = SaleEmployeeLevel;
					try {
						while(true){
							//查询出的最高等级为1
//							if(null!=m.get("SaleEmployeeLevel")|| ""!=m.get("SaleEmployeeLevel")){
//								if(m.get("SaleEmployeeLevel").toString().equals("0")){
//									break;
//								}
//							}
							// 指向总部的SupervisorID不存在，所以为空时认为是总部
							if(null==SupervisorID || ""==SupervisorID){
								// 总部利润
//								Map<String, Object> m0 = new HashMap<String, Object>();
//								int pfl = 0;
//								for(int i=0;i<LevelL;i++){
//									pfl+=rebateDivide[i];
//								}
//								m0.put("SaleCount", m.get("SaleCount"));
//								m0.put("Amount", m.get("Amount"));
//								m0.put("Channel", m.get("Channel"));
//								m0.put("SaleProfit", m.get("SaleProfit"));
//								m0.put("BillCodePre", m.get("BillCodePre"));
//								m0.put("SaleEmployeeName", m.get("SaleEmployeeName"));
//								m0.put("BIN_BrandInfoID", m.get("BIN_BrandInfoID"));
//								m0.put("SaleRecordCode", m.get("SaleRecordCode"));
//								m0.put("SaleType", m.get("SaleType"));
//								m0.put("SaleTime", m.get("SaleTime"));
//								m0.put("BIN_EmployeeID", m.get("BIN_EmployeeID"));
//								m0.put("BIN_OrganizationID", m.get("BIN_OrganizationID"));
//								m0.put("SaleEmployeeCode", m.get("SaleEmployeeCode"));
//								m0.put("SaleEmployeeLevel", m.get("SaleEmployeeLevel"));
//								m0.put("BIN_OrganizationInfoID", m.get("BIN_OrganizationInfoID"));
//								
//								m0.put("CommissionProportion", pfl);
//								// 提成金额
//								m0.put("RoyaltyAmount", Profit*pfl/100);
//								m0.put("CommissionEmployeeCode", "zb");
//								m0.put("CommissionEmployeeName", "总部");
//								m0.put("CommissionEmployeeLevel", "0");
//								m0.put("CommissionOrganizationID", m.get("BIN_OrganizationID"));
//								m0.put("BatchNo", paraMap.get("batchId"));
//								m0.put("createdBy", "BATCH");
//								m0.put("createPGM", "BINBEHMWFT01");
//								pMapList.add(m0);
								break;
							}
							Map<String, Object> cm = binBEHMWFT01_Service.getEmployee(paraMap);
							if(null!=cm && !cm.isEmpty()){
								if(null!=cm.get("CodeKey") && ""!=cm.get("CodeKey")){
									paraMap.put("CodeKey", cm.get("CodeKey"));
									Integer s = binBEHMWFT01_Service.getLevel(paraMap);
									if(null!=s && s>3){
										cm.put("Level", s-1-3);
									}else {
										cm.put("Level", null);
									}
								}
								if(null!=cm.get("SupervisorID") && ""!=cm.get("SupervisorID")){
									SupervisorID = cm.get("SupervisorID").toString();
									paraMap.put("SupervisorID", SupervisorID);
								}else {
									SupervisorID = null;
								}
								// 下级等级(比当前等级小)
								if(null==LevelL && null!=m.get("CommissionEmployeeLevel") && ""!=m.get("CommissionEmployeeLevel")){
									LevelL = Integer.parseInt(m.get("CommissionEmployeeLevel").toString());
								}
								//查询出的最高等级1
//								if(LevelL==0){
//									break;
//								}
								Map<String, Object> sm = new HashMap<String, Object>();
								
								sm.put("SaleCount", m.get("SaleCount"));
								sm.put("Amount", m.get("Amount"));
								sm.put("Channel", m.get("Channel"));
								sm.put("SaleProfit", m.get("SaleProfit"));
								sm.put("BillCodePre", m.get("BillCodePre"));
								sm.put("SaleEmployeeName", m.get("SaleEmployeeName"));
								sm.put("BIN_BrandInfoID", m.get("BIN_BrandInfoID"));
								sm.put("SaleRecordCode", m.get("SaleRecordCode"));
								sm.put("SaleType", m.get("SaleType"));
								sm.put("SaleTime", m.get("SaleTime"));
								sm.put("BIN_EmployeeID", m.get("BIN_EmployeeID"));
								sm.put("BIN_OrganizationID", m.get("BIN_OrganizationID"));
								sm.put("SaleEmployeeCode", m.get("SaleEmployeeCode"));
								sm.put("SaleEmployeeLevel", m.get("SaleEmployeeLevel"));
								sm.put("BIN_OrganizationInfoID", m.get("BIN_OrganizationInfoID"));
								sm.put("BillCode", m.get("BillCode"));
								
								sm.put("CommissionEmployeeCode", cm.get("EmployeeCode"));
								sm.put("CommissionEmployeeName", cm.get("EmployeeName"));
								sm.put("CommissionEmployeeLevel", cm.get("Level"));
								sm.put("CommissionOrganizationID", cm.get("BIN_OrganizationID"));
								sm.put("BatchNo", paraMap.get("batchId"));
								sm.put("createdBy", "BATCH");
								sm.put("createPGM", "BINBEHMWFT01");
								if(null!=cm.get("CodeKey") && ""!=cm.get("CodeKey")){
									paraMap.put("CodeKey", cm.get("CodeKey"));
									Integer s = binBEHMWFT01_Service.getLevel(paraMap);
									if(null!=s && s>3){
										// 当前提成人等级
										int LevelR = s-1-3;
										int pfl = 0;
										for(int i=LevelR;i<LevelL;i++){
											pfl+=rebateDivide[i];
										}
										fl = pfl;
										sm.put("CommissionProportion", fl);
										// 提成金额
										sm.put("RoyaltyAmount", Profit*fl/100);
										
									}
									if(s<4){
										break;
									}
								}
								pMapList.add(sm);
								if(null!=sm.get("CommissionEmployeeLevel") && ""!=sm.get("CommissionEmployeeLevel")){
									LevelL = Integer.parseInt(sm.get("CommissionEmployeeLevel").toString());
								}
							}else {
								break;
							}
						}
					} catch (Exception e) {
						return CherryBatchConstants.BATCH_ERROR_PR1;
					}
				}
				salList.addAll(pMapList);
				if(salList.size()>0){
					for(Map<String,Object> pm:salList){
						binBEHMWFT01_Service.updateProfitRebate(pm);
					}
					try {
						binBEHMWFT01_Service.InsertRebateDivideList(salList);
					} catch (Exception e) {
						return CherryBatchConstants.BATCH_ERROR_PR2;
					}
				}
				if(updList.size()>0){
					for(Map<String, Object> m : updList){
						m.put("updatedBy", "BATCH");
						m.put("updatePGM", "BINBEHMWFT01");
					}
					binBEHMWFT01_Service.updSaleRecord(updList);
				}
			}*/
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00003");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam("");
			batchLoggerDTO.addParam("");
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			//logger.error(e.getMessage(), e);
			return CherryBatchConstants.BATCH_ERROR;
		}
		return CherryBatchConstants.BATCH_SUCCESS;
	}
}
