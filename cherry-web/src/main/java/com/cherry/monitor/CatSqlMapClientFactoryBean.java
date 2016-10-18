package com.cherry.monitor;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;

/**
 * Created by feipeng on 16/3/7.
 */
public class CatSqlMapClientFactoryBean extends SqlMapClientFactoryBean {

    private SqlMapClient wrapSqlMapClient;


    public SqlMapClient getObject() {

        if(this.wrapSqlMapClient!=null){
            return this.wrapSqlMapClient;
        }


        SqlMapClientImpl s = (SqlMapClientImpl)super.getObject();


        SqlMapExecutorDelegate sqlMapExecutorDelegate = new MonitorSqlMapExecutorDelegate(s.getDelegate());
        this.wrapSqlMapClient = new SqlMapClientImpl(sqlMapExecutorDelegate);


        return this.wrapSqlMapClient;
    }
}
