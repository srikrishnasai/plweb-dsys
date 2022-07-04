package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class HeaderModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String HEADER_COMPONENT = "/content/headerComponent";

	@BeforeEach
	public void setUp() {
		context.load().json("/headerModel.json", "/content");
		context.currentPage("/content");
	}

	@Test
	void doTestHeaderModel() {
		HeaderModel header = getHeaderModelUnderTest(HEADER_COMPONENT);
		assertEquals("/content/dam/we-retail/en/activities/hiking-camping/Hiking-Campaign-Male.jpg", header.getFileReference());
		assertEquals("/content/plweb-dsys/us/en/misc/all-sites", header.getAllSitesPath());
		assertEquals("Pacific Life Advisory", header.getTopNavHeaderText());
		assertEquals("true", header.getHideSearch());
		assertEquals("true", header.getHideTopNav());
		assertEquals("/content/plweb-dsys/home/search-results.html", header.getSearchTargetPath());
		assertEquals("/content/home.html", header.getHomePagePath());
	}

	private HeaderModel getHeaderModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(HeaderModel.class);
	}
}
