package com.cherry.mq.mes.common;

import com.cherry.cm.core.SpringBeanManager;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.interfaces.LockKey_IF;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hubing on 2017/1/10.
 */
public class MultiThreadController {

    // 正在执行计算的线程锁集合
    private static final ConcurrentHashMap lockMap = new ConcurrentHashMap();
    // 各种业务类型生成对象锁key的bean前缀
    private static final String lockBeanPrefix = "lockKey";

    /**
     * 通过对象锁的Key返回对应的对象锁
     *
     * @param lockKey 对象锁的Key
     * @return Object 对象锁
     */
    public synchronized Object getLockObj(String lockKey) {
        if (null == lockKey) {
            return null;
        }
        // 从对象锁集合中获取
        Object lock = lockMap.get(lockKey);
        // 如果不存在,新生成对象锁并放入集合中
        if (lock == null) {
            lock = lockKey;
            lockMap.put(lockKey, lockKey);
        }
        return lock;
    }

    /**
     * 根据业务类型获取对象锁的key
     *
     * @param map 消息体内容
     * @return String 对象锁的key
     */
    public String getLockKey(Map<String, Object> map) {
        // 获取业务类型
        String tradeType = ConvertUtil.getString(map.get("tradeType"));
        if (!ConvertUtil.isBlank(tradeType)) {
            // 获取该业务类型对应的生成key的bean
            Object ob = SpringBeanManager.getBean(lockBeanPrefix + tradeType);
            if (null != ob) {
                return ((LockKey_IF) ob).generateLockKey(map);
            }
        }
        return null;
    }

    /**
     * 从集合中删除对象锁
     *
     * @param lockKey 对象锁的key
     */
    public void removeLock(String lockKey) {
        lockMap.remove(lockKey);
    }
}
