package com.cherry.webserviceout.common;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * 使用https访问webservice时可用此类创建client
 * @author dingyongchang
 *
 */
public class JerseyWebserviceClientSSL {


	public JerseyWebserviceClientSSL() {
	}

	static HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				return true;
			}
		};
	}

	public static Client getClient(final String wsUsername,final String wsPassword) throws Exception {
		Client client = null;
		SSLContext sslContext = null;

		WechatX509TrustManager secureRestClientTrustManager = new WechatX509TrustManager();
		sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, new javax.net.ssl.TrustManager[] { secureRestClientTrustManager }, null);

		DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
		defaultClientConfig.getProperties().put(com.sun.jersey.client.urlconnection.HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new com.sun.jersey.client.urlconnection.HTTPSProperties(getHostnameVerifier(), sslContext));
		client = Client.create(defaultClientConfig);
		client.addFilter(new HTTPBasicAuthFilter(wsUsername, wsPassword));

		return client;
	}
}
