package com.cherry.mq.mes.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 问卷消息数据接收处理Service
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES05_Service extends BaseService{
	/**
	 * 插入答卷信息主表
	 * @param map
	 * @return
	 */
	public int addPaperAnswer(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.addPaperAnswer");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入答卷信息明细表
	 * @param map
	 * @return
	 */
	public void addPaperAnswerDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES05.addPaperAnswerDetail");	
	}
	
	/**
	 * 插入考核答卷信息主表
	 * @param map
	 * @return
	 */
	public int addCheckAnswer(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.addCheckAnswer");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入考核答卷信息明细表
	 * @param map
	 * @return
	 */
	public void addCheckAnswerDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES05.addCheckAnswerDetail");	
	}
	
	/**
	 * 移除旧的答卷信息(答卷主表)
	 * @param map
	 */
	public int delOldPaperAnswer(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.delOldPaperAnswer");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 移除旧的答卷信息(答卷明细表)
	 * @param map
	 */
	public int delOldPaperAnswerDetail(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.delOldPaperAnswerDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 移除旧的考核信息(考核主表)
	 * @param map
	 * @return
	 */
	public int delOldCheckAnswer(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.delOldCheckAnswer");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 移除旧的考核信息(考核明细表)
	 * @param map
	 * @return
	 */
	public int delOldCheckAnswerDetail(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.delOldCheckAnswerDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询答卷ID
	 * @param map
	 * @return
	 */
	public Map selAnswerID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.selAnswerID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询答卷ID
	 * @param map
	 * @return
	 */
	public Map selCheckAnswerID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.selCheckAnswerID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询问题ID
	 * @param map
	 * @return
	 */
	public Map selQuestionID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.selQuestionID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询考核问题ID
	 * @param map
	 * @return
	 */
	public Map selCheckQuestionID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.selCheckQuestionID");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询考核问卷评分级别表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selCheckPaperLevel(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.selCheckPaperLevel");
		return  baseServiceImpl.getList(map);
	}
	
    /**
     * 查询推荐会员
     * @param map
     * @return
     */
    public Map selReferrerID(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.selReferrerID");
        return (Map) baseServiceImpl.get(map);
    }
    
    /**
     * 更新推荐会员
     * @param map
     */
    public int updateReferrerID(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.updateReferrerID");
        return baseServiceImpl.update(map);
    }
    
	/**
	 * 查询问卷明细
	 * @param map
	 * @return
	 */
	public Map getQuestionList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.getQuestionList");
		return  (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询问卷主表
	 * @param map
	 * @return
	 */
	public Map getQuestionMain(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES05.getQuestionMain");
		return  (Map) baseServiceImpl.get(map);
	}
}
