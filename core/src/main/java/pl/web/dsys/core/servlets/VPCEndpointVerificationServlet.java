package pl.web.dsys.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

		logger.debug("Inside VPCEndpointVerification Servlet");
        resp.setContentType("text/html; charset=utf-8");
        resp.setHeader("content-encoding", "gzip");
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpGet apiRequest = new HttpGet(
					"https://lid-ips.websolutions.dev.rs.pacificlife.com/identityproviderservice/pinger.aspx?a=444aa312");
			logger.debug("Http Get ::{}", apiRequest);
			HttpResponse response = httpclient.execute(apiRequest);
			if (200 != response.getStatusLine().getStatusCode()) {
				throw new IOException("Server returned error: " + response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			logger.debug("Response ::{}", EntityUtils.toString(entity));
			resp.getWriter().print(EntityUtils.toString(entity));

		}

	}
}
