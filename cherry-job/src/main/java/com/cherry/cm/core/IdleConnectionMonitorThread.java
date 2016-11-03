package com.cherry.cm.core;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 对应CLOSE_WAIT问题，使用一个线程来清除失效链接。
 * com.cherry.cm.core.WebConfigListener中有相关代码，系统启动时会启动该线程
 * http://blog.csdn.net/lyc66666666666/article/details/37902655
 * 
 * @author dingyongchang
 *
 */
public class IdleConnectionMonitorThread extends Thread {

	private final HttpClientConnectionManager connMgr;
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					//毫秒
					wait(30000);					
					// 关闭失效连接
					connMgr.closeExpiredConnections();
					// 关闭空闲超过600秒的连接
					connMgr.closeIdleConnections(600, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}
