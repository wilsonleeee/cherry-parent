//package com.cherry.monitor;
//
//import com.dianping.zebra.group.util.DaoContextHolder;
//import com.ibatis.common.util.PaginatedList;
//import com.ibatis.sqlmap.client.SqlMapClient;
//import com.ibatis.sqlmap.client.SqlMapSession;
//import com.ibatis.sqlmap.client.event.RowHandler;
//import com.ibatis.sqlmap.engine.execution.BatchException;
//import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
//import com.ibatis.sqlmap.engine.impl.SqlMapSessionImpl;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by feipeng on 16/3/7.
// */
//public class MonitorWrapSqlMapClient extends SqlMapClientImpl {
//    /**
//     * Constructor to supply a delegate
//     *
//     * @param delegate - the delegate
//     */
//    public MonitorWrapSqlMapClient(SqlMapClient delegate) {
//        this.delegate = delegate;
//    }
//
//
//    public MonitorWrapSqlMapClient() {
//        this.delegate = delegate;
//    }
//
//
//    /**
//     * Delegate for SQL execution
//     */
//    public SqlMapClient delegate;
//
//
//    @Override
//    public SqlMapSession openSession() {
//        return delegate.openSession();
//    }
//
//    @Override
//    public SqlMapSession openSession(Connection conn) {
//        return delegate.openSession(conn);
//    }
//
//    @Override
//    public SqlMapSession getSession() {
//        return delegate.getSession();
//    }
//
//    @Override
//    public void flushDataCache() {
//        delegate.flushDataCache();
//    }
//
//    @Override
//    public void flushDataCache(String cacheId) {
//        delegate.flushDataCache(cacheId);
//    }
//
//    @Override
//    public Object insert(String id, Object parameterObject) throws SQLException {
//
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.insert(id, parameterObject);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public Object insert(String id) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.insert(id);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public int update(String id, Object parameterObject) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.update(id, parameterObject);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public int update(String id) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.update(id);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public int delete(String id, Object parameterObject) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.delete(id, parameterObject);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public int delete(String id) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.delete(id);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public Object queryForObject(String id, Object parameterObject) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForObject(id, parameterObject);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public Object queryForObject(String id) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForObject(id);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public Object queryForObject(String id, Object parameterObject, Object resultObject) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForObject(id, parameterObject, resultObject);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public List queryForList(String id, Object parameterObject) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForList(id, parameterObject);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public List queryForList(String id) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForList(id);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public List queryForList(String id, Object parameterObject, int skip, int max) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForList(id, parameterObject, skip, max);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public List queryForList(String id, int skip, int max) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForList(id, skip, max);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public void queryWithRowHandler(String id, Object parameterObject, RowHandler rowHandler) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            delegate.queryWithRowHandler(id, parameterObject, rowHandler);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            delegate.queryWithRowHandler(id, rowHandler);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public PaginatedList queryForPaginatedList(String id, Object parameterObject, int pageSize) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForPaginatedList(id, parameterObject, pageSize);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForPaginatedList(id, pageSize);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public Map queryForMap(String id, Object parameterObject, String keyProp) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForMap(id, parameterObject, keyProp);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public Map queryForMap(String id, Object parameterObject, String keyProp, String valueProp) throws SQLException {
//        DaoContextHolder.setSqlName(id);
//        try {
//            return delegate.queryForMap(id, parameterObject, keyProp, valueProp);
//        } finally {
//            DaoContextHolder.clearSqlName();
//        }
//    }
//
//    @Override
//    public void startBatch() throws SQLException {
//        delegate.startBatch();
//    }
//
//    @Override
//    public int executeBatch() throws SQLException {
//        return delegate.executeBatch();
//    }
//
//    @Override
//    public List executeBatchDetailed() throws SQLException, BatchException {
//        return delegate.executeBatchDetailed();
//    }
//
//    @Override
//    public void startTransaction() throws SQLException {
//        delegate.startTransaction();
//    }
//
//    @Override
//    public void startTransaction(int transactionIsolation) throws SQLException {
//        delegate.startTransaction(transactionIsolation);
//    }
//
//    @Override
//    public void commitTransaction() throws SQLException {
//        delegate.commitTransaction();
//    }
//
//    @Override
//    public void endTransaction() throws SQLException {
//        delegate.endTransaction();
//    }
//
//    @Override
//    public void setUserConnection(Connection connnection) throws SQLException {
//        delegate.setUserConnection(connnection);
//    }
//
//    @Override
//    public Connection getUserConnection() throws SQLException {
//        return delegate.getUserConnection();
//    }
//
//    @Override
//    public Connection getCurrentConnection() throws SQLException {
//        return delegate.getCurrentConnection();
//    }
//
//    @Override
//    public DataSource getDataSource() {
//        return delegate.getDataSource();
//    }
//}
