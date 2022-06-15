package pl.web.dsys.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

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

		logger.debug("Inside VPCEndpointVerification Servlet");

		StringBuffer response = new StringBuffer("");
		try {
			URL url = new URL(
					"https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312");
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certificate, String str) {
				}

				public void checkServerTrusted(X509Certificate[] certificate, String str) {
				}
			} };
			context.init(null, trustManager, new SecureRandom());

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(context.getSocketFactory());
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
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resp.getWriter().print(response);
	}
}
