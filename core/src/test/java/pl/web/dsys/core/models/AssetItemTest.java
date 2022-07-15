package pl.web.dsys.core.models;

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
class AssetItemTest {

	private final AemContext context = DsysTestContext.newAemContext();
	MockSlingHttpServletRequest request;

	@BeforeEach
	public void setUp() {
		context.load().json("/assetSearchResult.json", "/content");
		context.currentPage("/content/home");
		request = context.request();
	}

	@Test
	void doTest() {
		Resource resource = context.resourceResolver().getResource("/content/home/jcr:content/asset.jpg");
		context.currentResource(resource);
		AssetItem item = context.currentResource().adaptTo(AssetItem.class);
		assertNotNull(item.getContentType());
		assertNotNull(item.getDescription());
		assertNotNull(item.getIcon());
		assertNotNull(item.getLastModified());
		assertNotNull(item.getName());
		assertNotNull(item.getPath());
		assertNotNull(item.getPublishedDate());
		assertNotNull(item.getThumbnail());
		assertNotNull(item.getTitle());
		assertNotNull(item.getUrl());
		assertNotNull(item.hashCode());
	}
	
	@Test
	void doTestVideo() {
		Resource resource = context.resourceResolver().getResource("/content/home/jcr:content/asset1.mp4");
		context.currentResource(resource);
		AssetItem item = context.currentResource().adaptTo(AssetItem.class);
		assertNotNull(item.getContentType());
		assertNotNull(item.getDescription());
		assertNotNull(item.getIcon());
		assertNotNull(item.getLastModified());
		assertNotNull(item.getName());
		assertNotNull(item.getPath());
		assertNotNull(item.getPublishedDate());
		assertNotNull(item.getThumbnail());
		assertNotNull(item.getTitle());
		assertNotNull(item.getUrl());
		assertNotNull(item.hashCode());
	}
	
	@Test
	void doTestPdf() {
		Resource resource = context.resourceResolver().getResource("/content/home/jcr:content/asset2.pdf");
		context.currentResource(resource);
		AssetItem item = context.currentResource().adaptTo(AssetItem.class);
		assertNotNull(item.getContentType());
		assertNotNull(item.getDescription());
		assertNotNull(item.getIcon());
		assertNotNull(item.getLastModified());
		assertNotNull(item.getName());
		assertNotNull(item.getPath());
		assertNotNull(item.getPublishedDate());
		assertNotNull(item.getThumbnail());
		assertNotNull(item.getTitle());
		assertNotNull(item.getUrl());
		assertNotNull(item.hashCode());
	}

}
