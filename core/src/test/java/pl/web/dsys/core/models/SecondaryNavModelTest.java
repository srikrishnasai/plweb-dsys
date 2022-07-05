package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class SecondaryNavModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String SECONDARY_NAV_COMPONENT = "/content/home/jcr:content/secondary_nav";

	@BeforeEach
	public void setUp() {
		context.load().json("/secondaryNav.json", "/content");
	}

	@Test
	void doTestSecondaryNavModel() {
		SecondaryNavModel secondary = getSecondaryNavModelUnderTest(SECONDARY_NAV_COMPONENT);
		assertNotNull(secondary.getColumnOne());
		assertNotNull(secondary.getColumnTwo());
		assertNotNull(secondary.getColumnThree());
		assertEquals("overview", secondary.getColumnOne().getNavType());
		assertNotNull(secondary.getColumnOne().getOverview());
		assertNotNull(secondary.getColumnOne().getMultilinks());
		assertNotNull(secondary.getColumnOne().getMarketingtile());
		assertEquals("First Page Header", secondary.getColumnOne().getOverview().getHeader());
		assertEquals("This is the test description for the column one", secondary.getColumnOne().getOverview().getOverviewDescription());
		assertEquals("What is Fixed Annuities?", secondary.getColumnOne().getOverview().getPrimaryLinkText());
		assertEquals("/content/plweb-dsys/us/en/first-page", secondary.getColumnOne().getOverview().getPrimaryLinkTargetPath());
		assertEquals("_blank", secondary.getColumnOne().getOverview().getPrimaryLinkTarget());
        assertNull(secondary.getColumnOne().getOverview().getTelephone());
        assertNotNull(secondary.getColumnOne().getOverview().getSecondaryLinks());
        assertEquals("View Performance", secondary.getColumnOne().getOverview().getSecondaryLinks().get(0).getLinkText());
        assertNull(secondary.getColumnOne().getOverview().getSecondaryLinks().get(0).getDescription());
        assertNull(secondary.getColumnOne().getOverview().getSecondaryLinks().get(0).getIconPath());
        assertEquals("/content/home", secondary.getColumnOne().getOverview().getSecondaryLinks().get(0).getTargetPath());
        assertEquals("_self", secondary.getColumnOne().getOverview().getSecondaryLinks().get(0).getLinkTarget());
        assertNotNull(secondary.getColumnOne().getMultilinks().getLinks());
        assertNull(secondary.getColumnTwo().getMarketingtile().getUseOriginalImage());
        assertNull(secondary.getColumnTwo().getMarketingtile().getUseTransparentImage());
        assertEquals("/content/dam/JimMorris_quote_bg.jpg", secondary.getColumnTwo().getMarketingtile().getFileReference());
        assertEquals("test", secondary.getColumnTwo().getMarketingtile().getDescription());
	}

	private SecondaryNavModel getSecondaryNavModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(SecondaryNavModel.class);
	}

}
