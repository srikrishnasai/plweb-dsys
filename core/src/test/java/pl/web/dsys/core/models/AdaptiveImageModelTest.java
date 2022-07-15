package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class AdaptiveImageModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String ADAPTIVE_IMAGE_COMPONENT = "/content/home/jcr:content/flexible_image";

	MockSlingHttpServletRequest request;
	MockRequestPathInfo requestPathinfo;

	@BeforeEach
	public void setUp() {
		context.load().json("/flexibleImage.json", "/content");
		request = context.request();
		requestPathinfo = context.requestPathInfo();
	}
	
	@Test
	void doTest() throws RepositoryException {
		requestPathinfo.setSelectorString("large");
		AdaptiveImage image = getAdaptiveImageUnderTest(ADAPTIVE_IMAGE_COMPONENT);
	}
	
	private AdaptiveImage getAdaptiveImageUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(AdaptiveImage.class);
	}
}
