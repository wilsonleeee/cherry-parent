package com.cherry.cm.core;

import org.springframework.util.Assert;

public class CustomerSmsContextHolder {
	// // 子线程也能共享父线程的线程本地变量的值
	// private static final ThreadLocal<String> contextHolder =
	// new InheritableThreadLocal<String>();
	private static final ThreadLocal<String> contextSmsHolder = new ThreadLocal<String>();

	public static void setCustomerSmsDataSourceType(String customerSmsDataSourceType) {
		Assert.notNull(customerSmsDataSourceType,
				"customerSmsDataSourceType cannot be null");
		contextSmsHolder.set(customerSmsDataSourceType);
	}

	public static String getCustomerSmsDataSourceType() {
		return (String) contextSmsHolder.get();
	}

	public static void clearCustomerSmsDataSourceType() {
		contextSmsHolder.remove();
	}
}
