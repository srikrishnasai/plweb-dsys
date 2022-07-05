package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class HeaderModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String HEADER_COMPONENT = "/content/home/jcr:content/headerComponent";

	@Mock
	Page page;

	@BeforeEach
	public void setUp() {
		context.load().json("/headerModel.json", "/content");
		context.currentPage("/content/home");
	}

	@Test
	void doTestHeaderModel() {
		HeaderModel header = getHeaderModelUnderTest(HEADER_COMPONENT);
		assertEquals("/content/dam/we-retail/en/activities/hiking-camping/Hiking-Campaign-Male.jpg",
				header.getFileReference());
		assertEquals("/content/home", header.getAllSitesPath());
		assertEquals("Pacific Life Advisory", header.getTopNavHeaderText());
		assertEquals("true", header.getHideSearch());
		assertEquals("true", header.getHideTopNav());
		assertEquals("/content/plweb-dsys/home/search-results.html", header.getSearchTargetPath());
		assertNotNull(header.getPrimarylinks());
		assertEquals("Home", header.getPrimarylinks().get(0).getLinkText());
		assertEquals("/content/home", header.getPrimarylinks().get(0).getTargetPath());
		assertEquals("/content/home/home.html", header.getHomePagePath());
		assertEquals("_blank", header.getPrimarylinks().get(0).getLinkTarget());
		assertNull(header.getPrimarylinks().get(0).getDescription());
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/secondary-nav/second-header/jcr:content",
				header.getPrimarylinks().get(0).getSecondaryNavXfPath());
		assertNull(header.getPrimarylinks().get(0).getIconPath());
		assertNotNull(header.getAllSites());
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
