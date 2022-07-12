package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class IframeModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String IFRAME_COMPONENT = "/content/home/jcr:content/iframe";
	public static final String IFRAMEONE_COMPONENT = "/content/home/jcr:content/iframe_one";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/iframe.json", "/content");
		context.currentPage("/content/home");
		request = context.request();
	}

	@Test
	void doTestIframe() {
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("wcmmode", "disabled");
		request.setParameterMap(params);
		IframeModel iframe = getIframeModelUnderTest(IFRAME_COMPONENT);
		assertEquals("HTML", iframe.getHtml());
		assertEquals("https://www.google.com?&wcmmode=disabled", iframe.getAppUrl());
		assertNull(iframe.getScrollbars());
		assertNull(iframe.getRemoveWH());
		assertEquals(false, iframe.showFrame());
		assertEquals(true, iframe.isShowHtml());
		assertEquals("IFRM_EXTERNAPP", iframe.getIFrameId());
	}

	@Test
	void doTestIframeOne() {
		Map<String, Object> params = new HashedMap<String, Object>();
		request.setParameterMap(params);
		IframeModel iframe = getIframeModelUnderTest(IFRAMEONE_COMPONENT);
		assertEquals("HTML", iframe.getHtml());
		assertEquals("#", iframe.getAppUrl());
		assertEquals("scrolling=\"no\"", iframe.getScrollbars());
		assertEquals("width=\"100%\" height=\"100%\"", iframe.getRemoveWH());
		assertEquals(false, iframe.showFrame());
		assertEquals(true, iframe.isShowHtml());
		assertEquals("IFRM_EXTERNAPP", iframe.getIFrameId());
	}

	private IframeModel getIframeModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(IframeModel.class);
	}
}
