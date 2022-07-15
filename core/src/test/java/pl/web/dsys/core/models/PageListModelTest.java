package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class PageListModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String PAGE_LIST_COMPONENT = "/content/home/jcr:content/page-list";

	@BeforeEach
	public void setUp() {
		context.load().json("/pageListModel.json", "/content");
	}

	@Test
	@Disabled
	void doTestPageList() {
		PageListModel pageList = getPageListModelUnderTest(PAGE_LIST_COMPONENT);
		assertNotNull(pageList.getRemovePadding());
		assertNotNull(pageList.getRemoveBullets());
		assertNotNull(pageList.getListColumn());
		assertNotNull(pageList.getTitle());
	}

	private PageListModel getPageListModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(PageListModel.class);
	}

}
