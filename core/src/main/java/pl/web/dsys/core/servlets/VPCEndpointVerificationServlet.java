package pl.web.dsys.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

		/*
		 * try (CloseableHttpClient client = HttpClients.createDefault()) { HttpGet get
		 * = new HttpGet(
		 * "https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312"
		 * );
		 * 
		 * HttpResponse response = client.execute(get); if (200 !=
		 * response.getStatusLine().getStatusCode()) { throw new
		 * IOException("Server returned error: " +
		 * response.getStatusLine().getReasonPhrase()); }
		 * 
		 * HttpEntity entity = response.getEntity();
		 * logger.debug("Response from test servlet ::{}",
		 * EntityUtils.toString(entity));
		 * resp.getWriter().write(EntityUtils.toString(entity));
		 * 
		 * }
		 */
		StringBuffer response = new StringBuffer("");
		try {
			URL url = new URL(
					"https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312");
			InputStream stream = null;
			logger.debug("URL ::{}", url);
			// SonarQube - try with a resource
			try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					logger.debug("REsponse ::{}", response);
					in.close();
				}
			}
		} finally {

		}
		resp.getWriter().write("Response " + response);
	}
}
