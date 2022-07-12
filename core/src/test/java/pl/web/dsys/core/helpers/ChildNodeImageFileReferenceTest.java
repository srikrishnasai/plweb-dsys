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
class ChildNodeImageFileReferenceTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String CHILD_NODE_IMAGE_REFERENCE_COMPONENT = "/content/jcr:content";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/childNodeImageFileReference.json", "/content");
		request = context.request();
	}

	@Test
	void doTestChildNodeImageFileReference() {
		request.setAttribute("nodeName", "image");
		ChildNodeImageFileReference childNode = getChildNodeImageFileReferenceUnderTest(
				CHILD_NODE_IMAGE_REFERENCE_COMPONENT);
		assertEquals("/content/dam/image.jpg", childNode.getImageFileReference());

		childNode.setImageFileReference("/content/dam/image2.jpg");
		assertEquals("/content/dam/image2.jpg", childNode.getImageFileReference());
	}

	private ChildNodeImageFileReference getChildNodeImageFileReferenceUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(ChildNodeImageFileReference.class);
	}

}
