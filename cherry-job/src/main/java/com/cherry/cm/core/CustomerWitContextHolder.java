package com.cherry.cm.core;

import org.springframework.util.Assert;

public class CustomerWitContextHolder {
	// // 子线程也能共享父线程的线程本地变量的值
	// private static final ThreadLocal<String> contextWitHolder =
	// new InheritableThreadLocal<String>();
	private static final ThreadLocal<String> contextWitHolder = new ThreadLocal<String>();

	public static void setCustomerWitDataSourceType(String customerWitDataSourceType) {
		Assert.notNull(customerWitDataSourceType,
				"customerWitDataSourceType cannot be null");
		contextWitHolder.set(customerWitDataSourceType);
	}

	public static String getCustomerWitDataSourceType() {
		return (String) contextWitHolder.get();
	}

	public static void clearCustomerWitDataSourceType() {
		contextWitHolder.remove();
	}

}
