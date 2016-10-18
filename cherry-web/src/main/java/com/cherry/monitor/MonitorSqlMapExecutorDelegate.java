package com.cherry.monitor;

import com.dianping.zebra.group.util.DaoContextHolder;
import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.cache.CacheKey;
import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.exchange.DataExchangeFactory;
import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.InsertStatement;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.mapping.statement.PaginatedDataList;
import com.ibatis.sqlmap.engine.mapping.statement.SelectKeyStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.transaction.Transaction;
import com.ibatis.sqlmap.engine.transaction.TransactionException;
import com.ibatis.sqlmap.engine.transaction.TransactionManager;
import com.ibatis.sqlmap.engine.transaction.TransactionState;
import com.ibatis.sqlmap.engine.transaction.user.UserProvidedTransaction;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by feipeng on 16/3/8.
 */
public class MonitorSqlMapExecutorDelegate extends SqlMapExecutorDelegate {


    SqlMapExecutorDelegate target;


    public MonitorSqlMapExecutorDelegate(SqlMapExecutorDelegate sqlMapExecutorDelegate){
        this.target = sqlMapExecutorDelegate;
    }

    /**
     * DO NOT DEPEND ON THIS. Here to avoid breaking spring integration.
     * @deprecated
     */
    public int getMaxTransactions() {
        return target.getMaxTransactions();
    }

    /**
     * Getter for the DataExchangeFactory
     *
     * @return - the DataExchangeFactory
     */
    public DataExchangeFactory getDataExchangeFactory() {
        return target.getDataExchangeFactory();
    }

    /**
     * Getter for the TypeHandlerFactory
     *
     * @return - the TypeHandlerFactory
     */
    public TypeHandlerFactory getTypeHandlerFactory() {
        return target.getTypeHandlerFactory();
    }

    /**
     * Getter for the status of lazy loading
     *
     * @return - the status
     */
    public boolean isLazyLoadingEnabled() {
        return target.isLazyLoadingEnabled();
    }

    /**
     * Turn on or off lazy loading
     *
     * @param lazyLoadingEnabled - the new state of caching
     */
    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        target.setLazyLoadingEnabled(lazyLoadingEnabled);
    }

    /**
     * Getter for the status of caching
     *
     * @return - the status
     */
    public boolean isCacheModelsEnabled() {
        return target.isCacheModelsEnabled();
    }

    /**
     * Turn on or off caching
     *
     * @param cacheModelsEnabled - the new state of caching
     */
    public void setCacheModelsEnabled(boolean cacheModelsEnabled) {
        target.setCacheModelsEnabled(cacheModelsEnabled);
    }

    /**
     * Getter for the status of CGLib enhancements
     *
     * @return - the status
     */
    public boolean isEnhancementEnabled() {
        return target.isEnhancementEnabled();
    }

    /**
     * Turn on or off CGLib enhancements
     *
     * @param enhancementEnabled - the new state
     */
    public void setEnhancementEnabled(boolean enhancementEnabled) {
        target.setEnhancementEnabled(enhancementEnabled);
    }

    public boolean isUseColumnLabel() {
        return target.isUseColumnLabel();
    }

    public void setUseColumnLabel(boolean useColumnLabel) {
        target.setUseColumnLabel(useColumnLabel);
    }

    /**
     * Getter for the transaction manager
     *
     * @return - the transaction manager
     */
    public TransactionManager getTxManager() {
        return target.getTxManager();
    }

    /**
     * Setter for the transaction manager
     *
     * @param txManager - the transaction manager
     */
    public void setTxManager(TransactionManager txManager) {
        target.setTxManager(txManager);
    }

    /**
     * Add a mapped statement
     *
     * @param ms - the mapped statement to add
     */
    public void addMappedStatement(MappedStatement ms) {
        target.addMappedStatement(ms);
    }

    /**
     * Get an iterator of the mapped statements
     *
     * @return - the iterator
     */
    public Iterator getMappedStatementNames() {
        return target.getMappedStatementNames();
    }

    /**
     * Get a mappedstatement by its ID
     *
     * @param id - the statement ID
     * @return - the mapped statement
     */
    public MappedStatement getMappedStatement(String id) {
        return target.getMappedStatement(id);
    }

    /**
     * Add a cache model
     *
     * @param model - the model to add
     */
    public void addCacheModel(CacheModel model) {
        target.addCacheModel(model);
    }

    /**
     * Get an iterator of the cache models
     *
     * @return - the cache models
     */
    public Iterator getCacheModelNames() {
        return target.getCacheModelNames();
    }

    /**
     * Get a cache model by ID
     *
     * @param id - the ID
     * @return - the cache model
     */
    public CacheModel getCacheModel(String id) {
        return target.getCacheModel(id);
    }

    /**
     * Add a result map
     *
     * @param map - the result map to add
     */
    public void addResultMap(ResultMap map) {
        target.addResultMap(map);
    }

    /**
     * Get an iterator of the result maps
     *
     * @return - the result maps
     */
    public Iterator getResultMapNames() {
        return target.getResultMapNames();
    }

    /**
     * Get a result map by ID
     *
     * @param id - the ID
     * @return - the result map
     */
    public ResultMap getResultMap(String id) {
        return target.getResultMap(id);
    }

    /**
     * Add a parameter map
     *
     * @param map - the map to add
     */
    public void addParameterMap(ParameterMap map) {
        target.addParameterMap(map);
    }

    /**
     * Get an iterator of all of the parameter maps
     *
     * @return - the parameter maps
     */
    public Iterator getParameterMapNames() {
        return target.getParameterMapNames();
    }

    /**
     * Get a parameter map by ID
     *
     * @param id - the ID
     * @return - the parameter map
     */
    public ParameterMap getParameterMap(String id) {
        return target.getParameterMap(id);
    }

    /**
     * Flush all of the data caches
     */
    public void flushDataCache() {
        target.flushDataCache();
    }

    /**
     * Flush a single cache by ID
     *
     * @param id - the ID
     */
    public void flushDataCache(String id) {
        target.flushDataCache(id);
    }

    //-- Basic Methods
    /**
     * Call an insert statement by ID
     *
     * @param sessionScope - the session
     * @param id      - the statement ID
     * @param param   - the parameter object
     * @return - the generated key (or null)
     * @throws java.sql.SQLException - if the insert fails
     */
    public Object insert(SessionScope sessionScope, String id, Object param) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.insert(sessionScope,id, param);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }


    /**
     * Execute an update statement
     *
     * @param sessionScope - the session scope
     * @param id      - the statement ID
     * @param param   - the parameter object
     * @return - the number of rows updated
     * @throws SQLException - if the update fails
     */
    public int update(SessionScope sessionScope, String id, Object param) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.update(sessionScope, id, param);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a delete statement
     *
     * @param sessionScope - the session scope
     * @param id      - the statement ID
     * @param param   - the parameter object
     * @return - the number of rows deleted
     * @throws SQLException - if the delete fails
     */
    public int delete(SessionScope sessionScope, String id, Object param) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.delete(sessionScope, id, param);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a select for a single object
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @return - the result of the query
     * @throws SQLException - if the query fails
     */
    public Object queryForObject(SessionScope sessionScope, String id, Object paramObject) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForObject(sessionScope, id, paramObject);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a select for a single object
     *
     * @param sessionScope      - the session scope
     * @param id           - the statement ID
     * @param paramObject  - the parameter object
     * @param resultObject - the result object (if not supplied or null, a new object will be created)
     * @return - the result of the query
     * @throws SQLException - if the query fails
     */
    public Object queryForObject(SessionScope sessionScope, String id, Object paramObject, Object resultObject) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForObject(sessionScope, id, paramObject, resultObject);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a query for a list
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @return - the data list
     * @throws SQLException - if the query fails
     */
    public List queryForList(SessionScope sessionScope, String id, Object paramObject) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForList(sessionScope, id, paramObject);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a query for a list
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @param skip        - the number of rows to skip
     * @param max         - the maximum number of rows to return
     * @return - the data list
     * @throws SQLException - if the query fails
     */
    public List queryForList(SessionScope sessionScope, String id, Object paramObject, int skip, int max) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForList(sessionScope, id, paramObject, skip, max);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a query with a row handler.
     * The row handler is called once per row in the query results.
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @param rowHandler  - the row handler
     * @throws SQLException - if the query fails
     */
    public void queryWithRowHandler(SessionScope sessionScope, String id, Object paramObject, RowHandler rowHandler) throws SQLException {

        DaoContextHolder.setSqlName(id);
        try {
            target.queryWithRowHandler(sessionScope,id, paramObject,rowHandler);
        } finally {
            DaoContextHolder.clearSqlName();
        }

    }

    /**
     * Execute a query and return a paginated list
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @param pageSize    - the page size
     * @return - the data list
     * @throws SQLException - if the query fails
     * @deprecated All paginated list features have been deprecated
     */
    public PaginatedList queryForPaginatedList(SessionScope sessionScope, String id, Object paramObject, int pageSize) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForPaginatedList(sessionScope, id, paramObject, pageSize);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a query for a map.
     * The map has the table key as the key, and the results as the map data
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @param keyProp     - the key property (from the results for the map)
     * @return - the Map
     * @throws SQLException - if the query fails
     */
    public Map queryForMap(SessionScope sessionScope, String id, Object paramObject, String keyProp) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForMap(sessionScope, id, paramObject, keyProp);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    /**
     * Execute a query for a map.
     * The map has the table key as the key, and a property from the results as the map data
     *
     * @param sessionScope     - the session scope
     * @param id          - the statement ID
     * @param paramObject - the parameter object
     * @param keyProp     - the property for the map key
     * @param valueProp   - the property for the map data
     * @return - the Map
     * @throws SQLException - if the query fails
     */
    public Map queryForMap(SessionScope sessionScope, String id, Object paramObject, String keyProp, String valueProp) throws SQLException {
        DaoContextHolder.setSqlName(id);
        try {
            return target.queryForMap(sessionScope, id, paramObject, keyProp, valueProp);
        } finally {
            DaoContextHolder.clearSqlName();
        }
    }

    // -- Transaction Control Methods
    /**
     * Start a transaction on the session
     *
     * @param sessionScope - the session
     * @throws SQLException - if the transaction could not be started
     */
    public void startTransaction(SessionScope sessionScope) throws SQLException {
        target.startTransaction(sessionScope);
    }

    /**
     * Start a transaction on the session with the specified isolation level.
     *
     * @param sessionScope - the session
     * @throws SQLException - if the transaction could not be started
     */
    public void startTransaction(SessionScope sessionScope, int transactionIsolation) throws SQLException {
        target.startTransaction(sessionScope,transactionIsolation);
    }

    /**
     * Commit the transaction on a session
     *
     * @param sessionScope - the session
     * @throws SQLException - if the transaction could not be committed
     */
    public void commitTransaction(SessionScope sessionScope) throws SQLException {
        target.commitTransaction(sessionScope);
    }

    /**
     * End the transaction on a session
     *
     * @param sessionScope - the session
     * @throws SQLException - if the transaction could not be ended
     */
    public void endTransaction(SessionScope sessionScope) throws SQLException {
        target.endTransaction(sessionScope);
    }

    /**
     * Start a batch for a session
     *
     * @param sessionScope - the session
     */
    public void startBatch(SessionScope sessionScope) {
        target.startBatch(sessionScope);
    }

    /**
     * Execute a batch for a session
     *
     * @param sessionScope - the session
     * @return - the number of rows impacted by the batch
     * @throws SQLException - if the batch fails
     */
    public int executeBatch(SessionScope sessionScope) throws SQLException {
        return target.executeBatch(sessionScope);
    }

    /**
     * Execute a batch for a session
     *
     * @param sessionScope - the session
     * @return - a List of BatchResult objects (may be null if no batch
     *  has been initiated).  There will be one BatchResult object in the
     *  list for each sub-batch executed
     * @throws SQLException if a database access error occurs, or the drive
     *   does not support batch statements
     * @throws com.ibatis.sqlmap.engine.execution.BatchException if the driver throws BatchUpdateException
     */
    public List executeBatchDetailed(SessionScope sessionScope) throws SQLException, BatchException {

        return target.executeBatchDetailed(sessionScope);
    }

    /**
     * Use a user-provided transaction for a session
     *
     * @param sessionScope        - the session scope
     * @param userConnection - the user supplied connection
     */
    public void setUserProvidedTransaction(SessionScope sessionScope, Connection userConnection) {
        target.setUserProvidedTransaction(sessionScope,userConnection);
    }
    /**
     * Get the DataSource for the session
     *
     * @return - the DataSource
     */
    public DataSource getDataSource() {
        return target.getDataSource();
    }

    /**
     * Getter for the SqlExecutor
     *
     * @return the SqlExecutor
     */
    public SqlExecutor getSqlExecutor() {
        return target.getSqlExecutor();
    }

    /**
     * Get a transaction for the session
     *
     * @param sessionScope - the session
     * @return - the transaction
     */
    public Transaction getTransaction(SessionScope sessionScope) {
        return target.getTransaction(sessionScope);
    }





    public boolean equals(Object obj) {
        return this == obj;
    }

    public int hashCode() {
        return target.hashCode();
    }



    public ResultObjectFactory getResultObjectFactory() {
        return target.getResultObjectFactory();
    }

    public void setResultObjectFactory(ResultObjectFactory resultObjectFactory) {
        target.setResultObjectFactory(resultObjectFactory);
    }

    public boolean isStatementCacheEnabled() {
        return target.isStatementCacheEnabled();
    }

    public void setStatementCacheEnabled(boolean statementCacheEnabled) {
        target.setStatementCacheEnabled(statementCacheEnabled);
    }

    public boolean isForceMultipleResultSetSupport() {
        return target.isForceMultipleResultSetSupport();
    }

    public void setForceMultipleResultSetSupport(boolean forceMultipleResultSetSupport) {
        target.setForceMultipleResultSetSupport(forceMultipleResultSetSupport);
    }
}
