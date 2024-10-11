package com.example.spring_app.util;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpClientFactory {

	private HttpClientFactory() {
		super();
	}
	private static final Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);

	public static CloseableHttpAsyncClient getHttpClient(int readTimeout, int connectTimeout, int maxTotalCon, int maxConRoute, String cert, String certAlias, Boolean isSecure, Boolean isByPass) {

		TlsStrategy tlsStrategy = null;

		try{
			tlsStrategy = ClientTlsStrategyBuilder.create()
					.setSslContext(getSSLContext(cert, certAlias, isSecure, isByPass))
					.setHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		} catch (Exception e){
			LOG.error("Exception",e);
		}

		PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create().
				setTlsStrategy(tlsStrategy).
				setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT).setConnPoolPolicy(PoolReusePolicy.LIFO).
				setDefaultConnectionConfig(
						ConnectionConfig.custom()
								.setSocketTimeout(Timeout.ofMilliseconds(readTimeout))
								.setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
								.setTimeToLive(TimeValue.ofMinutes(10)).build())
				.setMaxConnTotal(maxTotalCon)
				.setMaxConnPerRoute(maxConRoute)
				.build();

		return HttpAsyncClients.custom()
				.setConnectionManager(connectionManager).build();
	}

	public static SSLContext getSSLContext(String certificate, String alias, boolean isSecure, boolean bypassSSL)
			throws CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException,
			KeyStoreException {

		SSLContext sslContext = null;		
		
		if(isSecure) {
			if (bypassSSL) {
				sslContext = SSLContextBuilder.create().loadTrustMaterial(null, new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						return true;
					}
				}).build();
			} else {
				X509Certificate cert = generateCertificateFromDER(certificate.getBytes());
	
				KeyStore keystore = KeyStore.getInstance("JKS");
				keystore.load(null);
				keystore.setCertificateEntry(alias, cert);
	
				sslContext = SSLContexts.custom().loadTrustMaterial(keystore, new TrustSelfSignedStrategy()).build();
			}
		} else {
			sslContext = SSLContext.getDefault();
		}

		return sslContext;
	}

	private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

}