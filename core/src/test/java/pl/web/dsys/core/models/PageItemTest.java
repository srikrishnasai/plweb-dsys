package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class PageItemTest {

	private final AemContext context = DsysTestContext.newAemContext();
	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/headerModel.json", "/content");
		context.currentPage("/content/home");
		request = context.request();
	}

	@Test
	void doTest() {
		Resource resource = context.resourceResolver().getResource("/content/home");
		context.currentResource(resource);
		PageItem item = context.currentResource().adaptTo(PageItem.class);
		assertNotNull(item.getContentType());
		assertNull(item.getDescription());
		assertNotNull(item.getLastModified());
		assertNotNull(item.getName());
		assertNotNull(item.getPath());
		assertNotNull(item.getPublishedDate());
		assertNotNull(item.getTitle());
		assertNotNull(item.getUrl());
		assertNotNull(item.hashCode());
	}

}
