package pl.web.dsys.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class NavigationBarModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String NAVIGATION_BAR_COMPONENT = "/content/jcr:content/root/navigation_bar";

	@BeforeEach
	public void setUp() {
		context.load().json("/navigationBarModel.json", "/content");
	}

	@Test
	void doTestNavigationBar() {
		NavigationBarModel navBar = getNavigationBarModelUnderTest(NAVIGATION_BAR_COMPONENT);

	}

	private NavigationBarModel getNavigationBarModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(NavigationBarModel.class);
	}
}
