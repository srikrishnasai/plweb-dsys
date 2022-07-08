package pl.web.dsys.core.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import com.day.cq.wcm.api.WCMMode;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class AuthControlFilterTest {

	private final AemContext context = DsysTestContext.newAemContext();
	MockSlingHttpServletRequest request;

	MockSlingHttpServletResponse response;
	
	MockRequestPathInfo requestPathInfo;
	
	AuthControlFilter filter = new AuthControlFilter();
	FilterChain chain;

	@BeforeEach
	void setup() {
		context.load().json("/authFilterJson.json", "/content");
		Resource resource = context.resourceResolver().getResource("/content/home");
		context.currentResource(resource);
		request = context.request();
		response = context.response();
		requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		chain = Mockito.mock(FilterChain.class);
	}
	
	@Test
	void doTestWithEditMode() throws IOException, ServletException {
		requestPathInfo.setResourcePath("/content/home");
		WCMMode.EDIT.toRequest(request);
	    filter.doFilter(request, response, chain);
	    Mockito.verify(chain, Mockito.times(1)).doFilter(request, response);
	}

	@Test
	void doTestAuthControlAccess() throws IOException, ServletException {
		requestPathInfo.setResourcePath("/content/home");
		requestPathInfo.setSelectorString("A:25448");
		filter.doFilter(request, response, chain);
		Mockito.verify(chain, Mockito.times(1)).doFilter(request, response);
	}
	
	@Test
	void doTestAuthControlNoAccess() throws IOException, ServletException {
		requestPathInfo.setResourcePath("/content/home");
		requestPathInfo.setSelectorString("A:25848");
		filter.doFilter(request, response, chain);
		Mockito.verify(chain, Mockito.times(0)).doFilter(request, response);
	}
}
