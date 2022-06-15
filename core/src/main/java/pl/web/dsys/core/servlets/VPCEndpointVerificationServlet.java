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

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			HttpGet get = new HttpGet(
					"https://vpce-0cfe6cc9e3df992e1-zgw8ttm9.vpce-svc-015109a36b504d84f.us-west-2.vpce.amazonaws.com/IdentityProviderService/pinger.aspx?a=444aa312");
			HttpResponse response = client.execute(get);
			if (200 != response.getStatusLine().getStatusCode()) {
				throw new IOException("Server returned error: " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			resp.getWriter().write(EntityUtils.toString(entity));

		}

	}
}
