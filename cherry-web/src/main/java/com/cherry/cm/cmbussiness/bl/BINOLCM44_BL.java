package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cherry.cm.core.PropertiesUtil;
import com.cherry.wp.common.entity.SaleActivityDetailEntity;
import com.cherry.wp.common.entity.SaleDetailEntity;
import com.cherry.wp.common.entity.SaleMainEntity;
import com.cherry.wp.common.entity.SaleProductDetailEntity;
import com.cherry.wp.common.entity.SaleRuleResultEntity;
import com.cherry.wp.sal.action.BINOLWPSAL08_Action;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

/**
 * 所有用Jni调用的方法
 * @author hxhao
 *
 */
public class BINOLCM44_BL {
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLCM44_BL.class);
	static{
			String path =BINOLWPSAL08_Action.class.getClassLoader().getResource("").getPath();
			String loadDll = PropertiesUtil.pps.getProperty("smartPromotion.Load");
			if("1".equals(loadDll)){
				System.load(path+"JniServer.dll");
			}else if("2".equals(loadDll)){
				System.load(path+"libJniServer.so");
			}
	}
	
	private static final String IP=PropertiesUtil.pps.getProperty("smartPromotion.IP");
	private static final String PORT=PropertiesUtil.pps.getProperty("smartPromotion.PORT");
	private static final String LOGFLAG=PropertiesUtil.pps.getProperty("smartPromotion.LogFlag");
	private native int  Cloud_MatchRule(String ip,String port,String dataLogFlag,String abbr ,String  BIN_OrganizationInfoID ,  
			ArrayList<SaleMainEntity> inputsalemain,ArrayList<SaleDetailEntity> inputdetail,
			ArrayList<SaleMainEntity> outsalemain,ArrayList<SaleDetailEntity> outdetail,ArrayList<SaleRuleResultEntity> outresult);
	private native int  Cloud_ComputeRule(String ip,String port,String dataLogFlag,String abbr ,  ArrayList<SaleMainEntity> inputsalemain,
			ArrayList<SaleDetailEntity> inputdetail,ArrayList<SaleRuleResultEntity> inputresult,ArrayList<SaleProductDetailEntity> inputpro,
			ArrayList<SaleMainEntity> outsalemain,ArrayList<SaleDetailEntity> outdetail,ArrayList<SaleRuleResultEntity> outresult);
	private native  int Cloud_LoadRule(String ip, String port ,String log_flag);
	private native void Cloud_Destroy(ArrayList<SaleMainEntity> outsalemain,ArrayList<SaleDetailEntity> outdetail,ArrayList<SaleRuleResultEntity> outresult);
	
	private native int Cloud_MatchRule_JIAHUA(String ip,String port,String dataLogFlag,String abbr,String  BIN_OrganizationInfoID ,
			ArrayList<SaleMainEntity> sale_main_input,ArrayList<SaleDetailEntity> sale_detail_input,ArrayList<SaleActivityDetailEntity> sale_activity_out,ArrayList<SaleRuleResultEntity> sale_result_out,ArrayList<SaleProductDetailEntity> sale_product_out);
	
	private native int Cloud_ComputeRule_JIAHUA(String ip,String port,String dataLogFlag,String abbr ,ArrayList<SaleMainEntity> salemain_input,ArrayList<SaleDetailEntity> saledetail_input,
			ArrayList<SaleRuleResultEntity> saleresult_input,ArrayList<SaleRuleResultEntity> sale_result_out,ArrayList<SaleDetailEntity> saledetail_out);
	/**
	 * 智能促销查询接口
	 * @param ip
	 * @param port
	 * @param dataLogFlag
	 * @param abbr
	 * @param BIN_OrganizationInfoID
	 * @param inputsalemain
	 * @param inputdetail
	 * @param outsalemain
	 * @param outdetail
	 * @param outresult
	 * @return
	 */
	public int cloud_MatchRule(String abbr ,String  BIN_OrganizationInfoID ,  
			ArrayList<SaleMainEntity> inputsalemain,ArrayList<SaleDetailEntity> inputdetail,
			ArrayList<SaleMainEntity> outsalemain,ArrayList<SaleDetailEntity> outdetail,ArrayList<SaleRuleResultEntity> outresult){
		int result=0;
		try{
			result= this.Cloud_MatchRule(IP, PORT, LOGFLAG, abbr, BIN_OrganizationInfoID, inputsalemain, inputdetail, outsalemain, outdetail, outresult);
		}catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
		return result;
	}
	/**
	 * 智能促销计算接口
	 * @param ip
	 * @param port
	 * @param dataLogFlag
	 * @param abbr
	 * @param inputsalemain
	 * @param inputdetail
	 * @param inputresult
	 * @param outsalemain
	 * @param outdetail
	 * @param outresult
	 * @return
	 */
	public int cloud_ComputeRule(String abbr ,  ArrayList<SaleMainEntity> inputsalemain,
			ArrayList<SaleDetailEntity> inputdetail,ArrayList<SaleRuleResultEntity> inputresult,ArrayList<SaleProductDetailEntity> inputpro,
			ArrayList<SaleMainEntity> outsalemain,ArrayList<SaleDetailEntity> outdetail,ArrayList<SaleRuleResultEntity> outresult){
		int result=-1;
		try{
			result=this.Cloud_ComputeRule(IP, PORT, LOGFLAG, abbr, inputsalemain, inputdetail, inputresult, inputpro,outsalemain, outdetail, outresult);
		}catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
		return result;
	}
	/**
	 * 智能促销加载规则方法
	 * @param ip
	 * @param port
	 * @param log_flag
	 * @return
	 */
	public int cloud_LoadRule(){
		int result=0;
		try{
			result= this.Cloud_LoadRule(IP, PORT, LOGFLAG);
		}catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
		return result;
	}
	/**
	 * 智能促销销毁方法
	 * @param outsalemain
	 * @param outdetail
	 * @param outresult
	 */
	public void cloud_Destroy(ArrayList<SaleMainEntity> outsalemain,ArrayList<SaleDetailEntity> outdetail,ArrayList<SaleRuleResultEntity> outresult){
		try{
			this.Cloud_Destroy(outsalemain, outdetail, outresult);
		}catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
	}
	/**
	 * 智能促销查询方法（家化）
	 * @param ip
	 * @param port
	 * @param dataLogFlag
	 * @param abbr
	 * @param BIN_OrganizationInfoID
	 * @param sale_main_input
	 * @param sale_detail_input
	 * @param sale_main_out
	 * @param sale_result_out
	 * @return
	 */
	public  int cloud_MatchRule_JIAHUA(String abbr,String  BIN_OrganizationInfoID ,
			ArrayList<SaleMainEntity> sale_main_input,ArrayList<SaleDetailEntity> sale_detail_input,ArrayList<SaleActivityDetailEntity> sale_activity_out,ArrayList<SaleRuleResultEntity> sale_result_out,ArrayList<SaleProductDetailEntity> sale_product_out){
		Transaction transaction = Cat.newTransaction("rule", "cloud_MatchRule_JIAHUA");
		int result=0;
		try{
			result=this.Cloud_MatchRule_JIAHUA(IP, PORT, LOGFLAG, abbr, BIN_OrganizationInfoID,  sale_main_input, sale_detail_input,sale_activity_out, sale_result_out,sale_product_out);
			transaction.setStatus(Transaction.SUCCESS);
		}catch (Exception e) {
        	logger.error(e.getMessage(), e);
        	transaction.setStatus(e);
			Cat.logError(e);
        } finally {
			transaction.complete();
		}
		return result;
	}
	/**
	 * 智能促销计算方法(家化)
	 * @param abbr
	 * @param txdtype
	 * @param salemain_input
	 * @param saledetail_input
	 * @param saleresult_input
	 * @param salemain_out
	 * @param saledetail_out
	 * @param sale_result_out
	 * @return
	 */
	public int cloud_ComputeRule_JIAHUA(String abbr,ArrayList<SaleMainEntity> salemain_input,ArrayList<SaleDetailEntity> saledetail_input,
			ArrayList<SaleRuleResultEntity> saleresult_input,ArrayList<SaleRuleResultEntity> sale_result_out,ArrayList<SaleDetailEntity> saledetail_out){
		Transaction transaction = Cat.newTransaction("rule", "cloud_ComputeRule_JIAHUA");
		int result=0;
		try{
			result=this.Cloud_ComputeRule_JIAHUA(IP, PORT, LOGFLAG, abbr, salemain_input, saledetail_input, saleresult_input, sale_result_out,saledetail_out);
			transaction.setStatus(Transaction.SUCCESS);
		}catch (Exception e) {
        	logger.error(e.getMessage(), e);
        	transaction.setStatus(e);
			Cat.logError(e);
        } finally {
			transaction.complete();
		}
		return result;
	}
}
