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
class ImageTransformHelperUseTest {
	
	private final AemContext context = DsysTestContext.newAemContext();
	public static final String INPLACE_CROPPING_HELPER_COMPONENT = "/content/home/jcr:content/flexible_image";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/flexibleImage.json", "/content");
		request = context.request();
	}
	
	@Test
	void doTestImageTransformHelperUse() {
		ImageTransformHelperUse helper = getImageTransformHelperUseUnderTest(INPLACE_CROPPING_HELPER_COMPONENT);
		assertEquals("dsys-flexImg1440w", helper.getLargeTransform());
		assertEquals("dsys-flexImg992w", helper.getMediumTransform());
		assertEquals("dsys-flexImg768w", helper.getSmallTransform());
	}
	
	private ImageTransformHelperUse getImageTransformHelperUseUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(ImageTransformHelperUse.class);
	}

}
