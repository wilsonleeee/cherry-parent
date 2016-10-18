package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员消息数据接收处理Service
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES03_Service extends BaseService{
	/**
	 * 插入会员信息表
	 * @param map
	 * @return
	 */
	public int addMemberInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addMemberInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入会员持卡信息表
	 * @param map
	 * @return
	 */
	public void addMemberCardInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addMemCardInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入地址信息表
	 * @param map
	 * @return
	 */
	public int addAddressInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addAddressInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入会员地址表
	 * @param map
	 */
	public void addMemberAddress(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addMemberAddress");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入员工信息表
	 * @param map
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int addEmployee(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addEmployee");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入BA信息表
	 * @param map
	 */
	public void addBaInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addBaInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入BAS考勤信息表
	 * @param map
	 */
	public void addBasAttInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addBasAttInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询BAS考勤信息表
	 * @param map
	 */
	public Map selBasAttInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selBasAttInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新BAS考勤信息表
	 * @param map
	 */
	public int updBasAttInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updBasAttInfo");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 移除旧的会员卡
	 * @param map
	 */
	public int delOldMemberCard(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delOldMemberCard");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除旧BA信息(员工表)
	 * @param map
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int delOldBaFromEmployeeTable(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delOldBaFromEmployeeTable");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除旧BA信息(BA信息表)
	 * @param map
	 */
	public int delOldBaFromBaInfoTable(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delOldBaFromBaInfoTable");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据BAcode删除旧BA信息(员工表)
	 * @param map
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int delOldBaFromEmployeeByBaCode(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delOldBaFromEmployeeByBaCode");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据BAcode删除旧BA信息(BA信息表) zhhuyi
	 * @param map
	 */
	public int delOldBaFromBaInfoByBaCode(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delOldBaFromBaInfoByBaCode");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新会员信息表
	 * @param map
	 * @return
	 */
	public int updMemberInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updMemberInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新会员等级
	 * @param map
	 * @return
	 */
	public int updMemLevelInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updMemLevelInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新地址信息表
	 * @param map
	 * @return
	 */
	public int updAddressInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updAddressInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	public Map selMemberInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selMemberInfo");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询省市ID
	 * @param map
	 * @return
	 */
	public List selProvinceCityID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selProvinceCityID");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询省ID
	 * @param map
	 * @return
	 */
	public List selProvinceID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selProvinceID");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询新节点ID
	 * @param map
	 * @return
	 */
	public Map selNewNodeID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selNewNodeID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 *  查询营业员所在柜台的柜台主管path
	 * @param map
	 * @return
	 */
	public Map selSuperPath(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selSuperPath");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询子节点ID
	 * @param map
	 * @return
	 */
	public Map selChildNodeID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selChildNodeID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询岗位类别
	 * @param map
	 * @return
	 */
	public List selPositionCategoryList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selPositionCategoryList");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询会员  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selMemberIsRepeatData(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selMemberIsRepeatData");
		return  (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询bas考勤  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selBasIsRepeatData(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selBasIsRepeatData");
		return  (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询U盘对应的员工信息
	 * @param map
	 * @return
	 */
	public Map selUdiskInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selUdiskInfo");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询员工信息及对应的岗位
	 * @param map
	 * @return
	 */
	public Map selEmpAndPosinfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selEmpAndPosinfo");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员等级信息
	 * @param map
	 * @return
	 */
	public Map selMemberLevel(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selMemberLevel");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新员工信息
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updateEmployee(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateEmployee");
		return  (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 查询BA信息表
	 * @param map
	 * @return
	 */
	public Map selBaInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selBaInfo");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新BA信息
	 * @param map
	 * @return
	 */
	public int updateBaInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateBaInfo");
		return  (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 查询员工信息
	 * @param map
	 * @return
	 */
	public Map selEmployeeInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selEmployeeInfo");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
     * 查询员工信息（用员工名字查）
     * @param map
     * @return
     */
    public List<Map<String,Object>> selEmployeeInfoByName(Map<String,Object> map){
        Map<String,Object> param = new HashMap<String,Object>();
        param.putAll(map);
        param.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selEmployeeInfoByName");
        return baseServiceImpl.getList(param);
    }
	
	/**
	 * 查询BAS下属员工信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selEmployeesByBAS(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selEmployeesByBAS");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询BA的原上级BAS
	 * @param map
	 * @return
	 */
	public List<String> getOldBASList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getOldBASList");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询关注BA的员工
	 * @param map
	 * @return
	 */
	public List<String> getLikeBAList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getLikeBAList");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 对该柜台下的所有BA对应的员工信息中的部门ID设置为空
	 * @param map
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updEmpOrgIdISNull(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updEmpOrgIdISNull");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 查询部门子节点ID
	 * @param map
	 * @return
	 */
	public Map selOrgChildNodeID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selOrgChildNodeID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新该部门对应的上级path
	 * @param map
	 */
	public int updOrgNodeID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updOrgNodeID");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 查询部门所属上级部门ID
	 * @param map
	 * @return
	 */
	public Map selOrgSuperOrgID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selOrgSuperOrgID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询部门所属上级部门path
	 * @param map
	 * @return
	 */
	public Map selOrgSuperOrgPath(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selOrgSuperOrgPath");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 插入会员回访信息表 
	 * @param map
	 */
	public void InsertMemVisitInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.InsertMemVisitInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 *更新会员回访信息 
	 * 
	 * @return
	 */
	public int updateMemVitInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateMemVitInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 把新会员卡号合并到老会员卡号中
	 * @param map
	 */
	public int updNewMemberCardInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updNewMemberCardInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 把新会员对应的销售记录的会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updateSaleMemId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateSaleMemId");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 把新会员对应的活动履历记录的会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updateCampaignHistoryMemId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateCampaignHistoryMemId");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 把新会员对应的会员使用化妆次数积分明细记录的会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updateMemUsedMemId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateMemUsedMemId");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 把新会员对应的规则执行履历记录的会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updateRuleRecordMemId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateRuleRecordMemId");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 删除新会员信息
	 * @param map
	 */
	public int delNewMemberInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delNewMemberInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 查询新卡对应的会员信息数据
	 * @param map
	 * @return
	 */
	public Map selMemInfoByNewMemcode(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selMemInfoByNewMemcode");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入重算信息表
	 * @param map
	 */
	public void insertReCalcInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertReCalcInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询新卡对应的会员产生的最早业务时间
	 * @param map
	 * @return
	 */
	public String getMinTicketDate(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getMinTicketDate");
		return  (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新会员卡状态
	 * @param map
	 */
	public int updMemberCardStatus(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updMemberCardStatus");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新会员状态
	 * @param map
	 */
	public int updMemValidFlag(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updMemValidFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询会员的有效卡数量
	 * @param map
	 */
	public int getMemCardCount(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getMemCardCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 更新会员卡信息
	 * @param map
	 */
	public int updMemberCardInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updMemberCardInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询推荐会员ID
	 * @param map
	 */
	public Map<String, Object> selReferrerID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selReferrerID");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询新会员对应的答卷信息
	 * @param map
	 */
	public Map<String, Object> getNewMemPaperAnswer(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getNewMemPaperAnswer");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 把老会员对应的答卷信息更新成无效
	 * @param map
	 */
	public int updatePaperAnswerValid(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updatePaperAnswerValid");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 把新会员对应的答卷信息的会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updatePaperAnswer(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updatePaperAnswer");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除新会员扩展信息
	 * @param map
	 */
	public int delNewMemberExtInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delNewMemberExtInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 把新会员对应的推荐会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updateReferrerID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateReferrerID");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除地址信息
	 * @param map
	 */
	public int delAddressInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delAddressInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除新会员地址信息
	 * @param map
	 */
	public int delNewMemberAddress(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delNewMemberAddress");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除新会员积分信息
	 * @param map
	 */
	public int delNewMemberPoint(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delNewMemberPoint");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 把新会员对应的积分变化记录的会员ID更新成老会员对应的会员ID
	 * @param map
	 */
	public int updatePointChangeMemId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updatePointChangeMemId");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询会员的最新卡号
	 * @param map
	 */
	public String getCurMemCode(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getCurMemCode");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员的最早销售时间
	 * @param map
	 */
	public String getMinSaleTime(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getMinSaleTime");
		return (String)baseServiceImpl.get(map);
	}
	
    /**
     * 插入会员回访表 
     * @param map
     */
    public void insertMemberVisit(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertMemberVisit");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 插入答卷主表
     * @param map
     */
    public int insertPaperAnswer(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertPaperAnswer");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入答卷信息明细表
     * @param map
     * @return
     */
    public void insertPaperAnswerDetail(List detailDataList){
        // 批量插入
        baseServiceImpl.saveAll(detailDataList, "BINBEMQMES03.insertPaperAnswerDetail");   
    }
    
    /**
     * 更新回访任务表
     * @param map
     */
    public int updateVisitTask(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateVisitTask");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入员工入退职信息表
     * @param map
     */
    public void insertEmployeeQuit(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertEmployeeQuit");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
	 * 查询新会员的初始积分信息
	 * @param map
	 * @return 新会员的初始积分信息
	 */
	public Map<String,Object> getMemberPoint(Map map){
		Map<String,Object> parameterMap = new HashMap<String,Object>();
	    parameterMap.putAll(map);
	    parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getMemberPoint");
		return (Map)baseServiceImpl.get(parameterMap);
	}
    
    /**
     * 更新会员积分信息
     * @param map
     * @return 更新件数
     */
    public int updateMemberPoint(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateMemberPoint");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 把新会员积分信息更新成老会员积分信息
     * @param map
     * @return 更新件数
     */
    public int changeMemberPoint(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.changeMemberPoint");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
	 * 查询新会员的扩展信息
	 * @param map 查询条件
	 * @return 新会员的扩展信息
	 */
	public Map<String,Object> getMemberExtInfo(Map map){
		Map<String,Object> parameterMap = new HashMap<String,Object>();
	    parameterMap.putAll(map);
	    parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getMemberExtInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
    /**
     * 更新销售目标设定表
     * @param map
     * @return 更新件数
     */
    public int updateSaleTarget(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateSaleTarget");
        return baseServiceImpl.update(parameterMap);
    }
	
    /**
     * 插入销售目标设定表
     * @param map
     */
    public void insertSaleTarget(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertSaleTarget");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 查询销售目标设定表
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleTarget(Map<String,Object> map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.getSaleTarget");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询会员积分信息是否存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> selMemberPointInfo(Map<String,Object> map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selMemberPointInfo");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入会员积分信息表
     * @param map
     */
    public int insertMemberPoint(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertMemberPoint");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 修改会员积分信息表（时序控制）
     * @param map
     * @return
     */
    public int updateMemberPointByTimeSequence(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateMemberPointByTimeSequence");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 查询会员积分变化信息是否存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPointChangeInfo(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selPointChangeInfo");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入会员积分变化主表
     * @param map
     */
    public int insertPointChange(Map<String,Object> map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.insertPointChange");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入会员积分变化明细表
     * @param map
     * @return
     */
    public void insertPointChangeDetail(List<Map<String,Object>> detailDataList){
        // 批量插入
        baseServiceImpl.saveAll(detailDataList, "BINBEMQMES03.insertPointChangeDetail");   
    }
    
    /**
     * 更新会员积分变化主表
     * @param map
     * @return
     */
    public int updatePointChange(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updatePointChange");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 给定会员积分变化主ID，删除会员积分变化明细
     * @param map
     * @return
     */
    public int delPointChangeDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.delPointChangeDetail");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 插入BA考勤信息表
     * @param map
     */
    public void addBAAttendance(Map map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addBAAttendance");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 更新会员激活状态
     * @param map
     * @return
     */
    public int updateMemberActive(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.updateMemberActive");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 根据手机号查会员
     * @param map
     * @return
     */
    public List<Map<String,Object>> selMemberInfoByMobilePhone(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.selMemberInfoByMobilePhone");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入短信回复记录表
     * @param map
     */
    public void addMobileMsgReply(Map map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES03.addMobileMsgReply");
        baseServiceImpl.save(parameterMap);
    }
}