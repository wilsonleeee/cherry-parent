package com.cherry.cm.core;

import org.springframework.util.Assert;

public class CustomerContextHolder {
	// // 子线程也能共享父线程的线程本地变量的值
	// private static final ThreadLocal<String> contextHolder =
	// new InheritableThreadLocal<String>();
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCustomerDataSourceType(String customerDataSourceType) {
		Assert.notNull(customerDataSourceType,
				"customerDataSourceType cannot be null");
		contextHolder.set(customerDataSourceType);
	}

	public static String getCustomerDataSourceType() {
		return (String) contextHolder.get();
	}

	public static void clearCustomerDataSourceType() {
		contextHolder.remove();
	}
}
