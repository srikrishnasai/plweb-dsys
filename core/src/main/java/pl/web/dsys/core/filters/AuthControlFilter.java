package pl.web.dsys.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.utils.AuthUtil;
import pl.web.dsys.core.utils.CommonUtils;

/**
 * This Filter checks if the user has access to the requested page or not. If no
 * access sends to no access page.
 * 
 * @author Krishna
 *
 */
@Component(service = Filter.class, property = {
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
		EngineConstants.SLING_FILTER_PATTERN + "=" + "/content/plweb-dsys(.*)" })
@ServiceDescription("This filter checks if the user has access to this page or not")
@ServiceRanking(Integer.MAX_VALUE)
@ServiceVendor("Pacific Life")
public class AuthControlFilter implements Filter {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final String ACCESS_DENIED_PATH = "/content/plweb-dsys/home/misc/access-aditional-content-error";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Inside Auth Control Filter ::");
		final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		final Resource currentRes = slingRequest.getResource();
		if (!AuthUtil.checkAccess(slingRequest, currentRes)) {
			log.debug("Do not have permissions to view this page ::");
			slingResponse.sendRedirect(
					CommonUtils.resolveUrl(ACCESS_DENIED_PATH, slingRequest.getResourceResolver(), slingRequest));
			return;
		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

}
