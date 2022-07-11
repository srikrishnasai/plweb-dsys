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
class ImageTransformHelperTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String IMAGE_TRANSFORM_HELPER_COMPONENT = "/content/jcr:content";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/imageTransformHelper.json", "/content");
		request = context.request();
	}

	@Test
	void doTestImageTransformHelper() {
		request.setAttribute("transform", "dsys-flexImg");
		request.setAttribute("transparent", "true");
		request.setAttribute("nodeName", "image");
		ImageTransformHelper childNode = getImageTransformHelperUnderTest(IMAGE_TRANSFORM_HELPER_COMPONENT);
		assertEquals("/content/jcr:content/image.image.jpg.img.transform/dsys-flexImg/image.png",
				childNode.getTransformedImageUrl());
	}

	@Test
	void doTestImageTransformHelperOne() {
		request.setAttribute("transform", "dsys-flexImg");
		request.setAttribute("transparent", "true");
		request.setAttribute("nodeName", "imageOne");
		ImageTransformHelper childNode = getImageTransformHelperUnderTest(IMAGE_TRANSFORM_HELPER_COMPONENT);
		assertEquals("/content/dam/image.svg", childNode.getTransformedImageUrl());
	}

	@Test
	void doTestImageTransformHelperNoTransparent() {
		request.setAttribute("transform", "dsys-flexImg");
		request.setAttribute("transparent", "false");
		request.setAttribute("nodeName", "image");
		ImageTransformHelper childNode = getImageTransformHelperUnderTest(IMAGE_TRANSFORM_HELPER_COMPONENT);
		assertEquals("/content/jcr:content/image.image.jpg.img.transform/dsys-flexImg/image.jpg",
				childNode.getTransformedImageUrl());
	}

	private ImageTransformHelper getImageTransformHelperUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(ImageTransformHelper.class);
	}

}
