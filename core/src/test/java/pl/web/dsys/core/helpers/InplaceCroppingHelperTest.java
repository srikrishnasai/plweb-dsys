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
class InplaceCroppingHelperTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String INPLACE_CROPPING_HELPER_COMPONENT = "/content/home/jcr:content/flexible_image";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/flexibleImage.json", "/content");
		request = context.request();
	}

	@Test
	void doTestMultifieldHelperNodeName() {
		request.setAttribute("nodeName", "image");
		InplaceCroppingHelper inplace = getInplaceCroppingHelperUnderTest(INPLACE_CROPPING_HELPER_COMPONENT);
		assertEquals(false, inplace.isLargeImage());
		assertEquals(false, inplace.isMediumImage());
		assertEquals(false, inplace.isSmallImage());
		assertEquals(false, inplace.isNamedImage());
	}


	private InplaceCroppingHelper getInplaceCroppingHelperUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(InplaceCroppingHelper.class);
	}

}
