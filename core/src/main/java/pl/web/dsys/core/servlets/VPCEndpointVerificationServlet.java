package pl.web.dsys.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
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

		StringBuffer response = new StringBuffer("");
		try {
			URL url = new URL(
					"https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312");
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			TrustManager[] trustManager = new TrustManager[] { new X509ExtendedTrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certificate, String str) {
				}

				public void checkServerTrusted(X509Certificate[] certificate, String str) {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {

				}
			} }; // Install the all-trusting trust manager
			context.init(null, trustManager, new SecureRandom());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); // Install
																			// the
																			// all-trusting
																			// host
																			// verifier
																			// //
			//conn.setSSLSocketFactory(context.getSocketFactory()); //
			//conn.setHostnameVerifier(allHostsValid);
			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				logger.debug("REsponse ::{}", response);
				in.close();
			}
		} catch (KeyManagementException | NoSuchAlgorithmException e) { // TODO Auto-generated catch block
																		// e.printStackTrace();
		}

		/*
		 * CloseableHttpClient httpclient; try { httpclient = HttpClients.custom()
		 * .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
		 * TrustAllStrategy.INSTANCE).build())
		 * .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		 * logger.debug("Apache Client http client"); HttpGet apiRequest = new HttpGet(
		 * "https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312"
		 * ); logger.debug("api request"); HttpResponse response =
		 * httpclient.execute(apiRequest); logger.debug("http response"); if (200 !=
		 * response.getStatusLine().getStatusCode()) { throw new
		 * IOException("Server returned error: " +
		 * response.getStatusLine().getReasonPhrase()); } HttpEntity entity =
		 * response.getEntity(); result = EntityUtils.toString(entity);
		 * logger.debug("APache Results ::{} ", result); } catch (KeyManagementException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (KeyStoreException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * 
		 * resp.getWriter().print(result);
		 */
		resp.getWriter().print(response);
	}
}
