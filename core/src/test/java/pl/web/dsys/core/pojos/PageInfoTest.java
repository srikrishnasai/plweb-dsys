package pl.web.dsys.core.pojos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PageInfoTest {

	@InjectMocks
	PageInfo pageInfo;

	@Test
	void doTestPageInfo() {
		pageInfo = new PageInfo();
		pageInfo.setPageTile("Page Title");
		pageInfo.setPath("/content/home");
		pageInfo.setResourceType("plweb-dsys/resourceType");
		assertEquals("Page Title", pageInfo.getPageTile());
		assertEquals("/content/home", pageInfo.getPath());
		assertEquals("plweb-dsys/resourceType", pageInfo.getResourceType());
	}

}
