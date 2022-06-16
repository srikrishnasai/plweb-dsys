package pl.web.dsys.core.servlets;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPathsStrict;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { Servlet.class }, immediate = true)
@SlingServletPathsStrict(paths = "/bin/pacificlife/vpctest", methods = { HttpConstants.METHOD_GET })
public class VPCEndpointVerificationServlet extends SlingSafeMethodsServlet {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		logger.debug("Inside VPCEndpointVerification Servlet changed to Apache Client");
		String result = StringUtils.EMPTY;
		/*
		 * StringBuffer response = new StringBuffer(""); try { URL url = new URL(
		 * "https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312"
		 * ); SSLContext context = SSLContext.getInstance("TLSv1.2"); // Create a trust
		 * manager that does not validate certificate chains TrustManager[] trustManager
		 * = new TrustManager[] { new X509ExtendedTrustManager() { public
		 * X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
		 * 
		 * public void checkClientTrusted(X509Certificate[] certificate, String str) { }
		 * 
		 * public void checkServerTrusted(X509Certificate[] certificate, String str) { }
		 * 
		 * @Override public void checkClientTrusted(X509Certificate[] chain, String
		 * authType, Socket socket) throws CertificateException { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public void checkServerTrusted(X509Certificate[] chain, String
		 * authType, Socket socket) throws CertificateException { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public void checkClientTrusted(X509Certificate[] chain, String
		 * authType, SSLEngine engine) throws CertificateException { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void checkServerTrusted(X509Certificate[] chain, String
		 * authType, SSLEngine engine) throws CertificateException { // TODO
		 * Auto-generated method stub
		 * 
		 * } } }; // Install the all-trusting trust manager context.init(null,
		 * trustManager, new SecureRandom());
		 * 
		 * // Create all-trusting host name verifier HostnameVerifier allHostsValid =
		 * new HostnameVerifier() { public boolean verify(String hostname, SSLSession
		 * session) { return true; } };
		 * 
		 * HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		 * HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		 * HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); // Install the
		 * all-trusting host verifier //
		 * conn.setSSLSocketFactory(context.getSocketFactory()); //
		 * conn.setHostnameVerifier(allHostsValid); conn.setRequestMethod("GET"); int
		 * responseCode = conn.getResponseCode(); if (responseCode ==
		 * HttpsURLConnection.HTTP_OK) { BufferedReader in = new BufferedReader(new
		 * InputStreamReader(conn.getInputStream())); String inputLine; while
		 * ((inputLine = in.readLine()) != null) { response.append(inputLine); }
		 * logger.debug("REsponse ::{}", response); in.close(); } } catch
		 * (KeyManagementException | NoSuchAlgorithmException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		CloseableHttpClient httpclient;
		try {
			httpclient = HttpClients.custom()
					.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
			logger.debug("Apache Client http client");
			HttpGet apiRequest = new HttpGet(
					"https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312");
			logger.debug("api request");
			HttpResponse response = httpclient.execute(apiRequest);
			logger.debug("http response");
			if (200 != response.getStatusLine().getStatusCode()) {
				throw new IOException("Server returned error: " + response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.debug("APache Results ::{} ", result);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resp.getWriter().print(result);
	}
}
