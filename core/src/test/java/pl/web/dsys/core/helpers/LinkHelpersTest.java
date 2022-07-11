package pl.web.dsys.core.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
public class LinkHelpersTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String LINK_HELPER_COMPONENT = "/content";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/linkHelpers.json", "/content");
		request = context.request();
	}

	@Test
	void doTestLinkHelpersInternal() {
		request.setAttribute("url", "/content/home");
		LinkHelpers linkHelper = getLinkHelpersUnderTest(LINK_HELPER_COMPONENT);
		assertEquals("/content/home.html", linkHelper.getLinkURL());
	}
	
	@Test
	void doTestLinkHelpersExternal() {
		request.setAttribute("url", "https://www.google.com");
		LinkHelpers linkHelper = getLinkHelpersUnderTest(LINK_HELPER_COMPONENT);
		assertEquals("https://www.google.com", linkHelper.getLinkURL());
	}

	private LinkHelpers getLinkHelpersUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(LinkHelpers.class);
	}
}
