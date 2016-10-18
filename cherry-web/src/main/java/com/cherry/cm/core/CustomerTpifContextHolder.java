package com.cherry.cm.core;

import org.springframework.util.Assert;

public class CustomerTpifContextHolder {
	// // 子线程也能共享父线程的线程本地变量的值
	// private static final ThreadLocal<String> contextHolder =
	// new InheritableThreadLocal<String>();
	private static final ThreadLocal<String> contextTpifHolder = new ThreadLocal<String>();

	public static void setCustomerTpifDataSourceType(String customerTpifDataSourceType) {
		Assert.notNull(customerTpifDataSourceType,
				"customerTpifDataSourceType cannot be null");
		contextTpifHolder.set(customerTpifDataSourceType);
	}

	public static String getCustomerTpifDataSourceType() {
		return (String) contextTpifHolder.get();
	}

	public static void clearCustomerTpifDataSourceType() {
		contextTpifHolder.remove();
	}
}
