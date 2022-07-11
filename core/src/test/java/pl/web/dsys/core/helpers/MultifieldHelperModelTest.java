package pl.web.dsys.core.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class MultifieldHelperModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String MULTIFIELD_HELPER_COMPONENT = "/content";

	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/linkHelpers.json", "/content");
		request = context.request();
	}
	
	@Test
	void doTestMultifieldHelperNodeName() {
		request.setAttribute("nodeName", "home");
		MultifieldHelperModel multi = getMultifieldHelperModelUnderTest(MULTIFIELD_HELPER_COMPONENT);
		assertNotNull(multi.getChildResource());
		assertEquals(1, multi.getLength());
		
	}
	
	@Test
	void doTestMultifieldHelperNodePath() {
		request.setAttribute("nodePath", "/content/home");
		MultifieldHelperModel multi = getMultifieldHelperModelUnderTest(MULTIFIELD_HELPER_COMPONENT);
		assertNotNull(multi.getChildResource());
		assertEquals(1, multi.getLength());
		
	}
	
	private MultifieldHelperModel getMultifieldHelperModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(MultifieldHelperModel.class);
	}
}
