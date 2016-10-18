/*      
 * @(#)BINOLCM18_IF.java     1.0 2011/08/30             
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLCM18_IF {
	
	/**
	 * 取得指定组织下的所有实体仓库信息
	 * @param praMap
	 * @return
	 */
	public List<Map<String, Object>> getDepotsList(Map<String, String> praMap);
	
	/**
	 * 取得指定部门使用的实体仓库信息
	 * @param departID 部门ID
	 * @param language 语言，为空则按中文处理
	 * @return
	 */
	public List<Map<String, Object>> getDepotsByDepartID(String departID,String language);
	
	/**
     * 取得用户能操作的所有实体仓库信息。和用户权限表进行了关联，且忽略了业务类型
     * @param userID 用户ID
     * @param depotType 仓库类型，为空则不作条件
     * @param language 语言，用于中英文对应，为空则按中文处理
     * @return
     */
	@Deprecated
    public List<Map<String, Object>> getDepotsByUser(String userID,String depotType,String language);
    
    /**
     * 给定业务类型，取得配置的逻辑仓库。如果该业务类型没有配置过逻辑仓库，则取出品牌下所有的逻辑仓库，且排序是默认仓库在前，其他仓库在后。
     * @param BIN_BrandInfoID 品牌ID
     * @param BusinessType 业务类型，为空则不作条件
     * @param Type 终端、后台区分，0为后台的逻辑仓库，1为终端的逻辑仓库，为空则不区分
     * @param language 语言，用于中英文对应，为空则按中文处理
     * @return
     */
//	@Deprecated
//    public List<Map<String, Object>> getLogicDepotByBusinessType(Map<String, Object> pram);

    /**
     * 给定业务类型和终端的逻辑仓库ID，取得对应的后台逻辑仓库。
     * @param brandInfoID
     * @param businessType
     * @param posLogicDepotID
     * @return
     */
    public List<Map<String,Object>> getLogicDepotBackEnd(String brandInfoID,String businessType,String posLogicDepotID);
    
    /**
     * 根据指定的仓库和业务类型，取得对方仓库信息；
	 * 现在仓库间的业务关系都是可以配置的，一个仓库可以向哪些仓库（或者哪些区域的仓库）发货，一个仓库退库要退向哪个仓库都是通过画面配置的；
	 * 使用该方法，指定仓库ID，可以：
     *（1）业务类型=发货，可以取得指定仓库能向哪些仓库发货(InOutFlag=OUT)，或者取得哪些仓库向指定仓库发货(InOutFlag=IN)；
	 *（2）业务类型=退库，可以取得指定仓库能向哪些仓库退库(InOutFlag=OUT)，或者取得哪些仓库能向指定仓库退库(InOutFlag=IN)；
     *（3）业务类型=订货，可以取得哪些仓库能从指定仓库能订货(InOutFlag=OUT)，或者取得指定仓库能向哪些仓库订货(InOutFlag=IN)；
     * @param map map中的值为“DepotID”：仓库ID，“InOutFlag”：入/出库方区分，指示DepotID所代表的仓库在该业务类型下是出库方还是入库方。值：IN/OUT，“BusinessType”：业务类型代码，40.发货，60.退库，“language”：语言，用于中英文对应，为空则按中文处理
     * @return
     * 
     * */
    public List<Map<String,Object>> getOppositeDepotsByBussinessType(Map<String, Object> pram);
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * (1)LogicInventoryCode 如果填了值，就按该值去查找;
     * (2)LogicInventoryCode 如果为空，则取得默认仓库；
     * 不管是(1)还是(2),最终如果取得出的逻辑仓库数量为0或是多条，则报错。
     * 目前该方法仅由MQ程序调用，其他地方不可擅用
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：LogicInventoryCode（可选。逻辑仓库代码）,
     * praMap参数说明：Type（必填。逻辑仓库类型。0：后台逻辑仓库，1：终端逻辑仓库）,
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    public Map<String, Object> getLogicDepotByCode(Map<String, Object> praMap) throws Exception;
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息(当前方法废除，请勿使用)
     * @param praMap
     * praMap参数说明：BIN_LogicInventoryInfoID （逻辑仓库ID）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    @Deprecated
    public Map<String, Object> getLogicDepotByID(Map<String, Object> praMap) throws Exception;
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * 返回的列表中按照OrderNO从小到大排序，且默认仓库在前
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：Type （可选。逻辑仓库类型。0：后台逻辑仓库，1：终端逻辑仓库）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getLogicDepotList(Map<String, Object> praMap) throws Exception;
    
    /**
     * 从【逻辑仓库业务配置表BIN_LogicDepotBusiness】中取得符合参数条件的逻辑仓库信息
	 * 返回的列表按照优先级从高到低排序(代表优先级的数字越小，则优先级越高)。
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：Type（必填。0为后台的逻辑仓库业务，1为终端的逻辑仓库业务）,
     * praMap参数说明：BusinessType（必填。业务类型，请参见code值表1133）,
     * praMap参数说明：ProductType（必填。产品类型：1、正常产品；2、促销品。参见code值表1134）,
     * praMap参数说明：SubType（可选。业务类型子类型，目前只有入库业务有子类型，code值表1168）,
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）,
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getLogicDepotByBusiness(Map<String, Object> praMap) throws Exception;
    
}
