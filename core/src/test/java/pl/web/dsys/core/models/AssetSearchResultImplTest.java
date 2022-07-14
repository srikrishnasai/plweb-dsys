package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;
import pl.web.dsys.core.search.SearchResult;

@ExtendWith(AemContextExtension.class)
class AssetSearchResultImplTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String ASSET_SEARCH_RESULT_COMPONENT = "/content/home/jcr:content/asset.jpg";

	@BeforeEach
	public void setUp() {
		context.load().json("/assetSearchResult.json", "/content");
	}

	@Test
	void doTestAssetSearchResult() {
		SearchResult asset = getAssetSearchResultImplUnderTest(ASSET_SEARCH_RESULT_COMPONENT);
        assertEquals("asset", asset.getContentType());
        assertEquals("asset.jpg", asset.getTitle());
        assertEquals("/content/home/jcr:content/asset.jpg", asset.getPath());
        assertEquals("/content/home/jcr:content/asset.jpg", asset.getURL());
        assertEquals(ArrayUtils.EMPTY_STRING_ARRAY, asset.getKeywords());
        assertEquals("", asset.getDescription());
        assertEquals("", asset.getIcon());
        assertEquals(new ArrayList<String>(), asset.getTagIds());
        asset.setExcerpts(new ArrayList<String>());
        asset.setFixedUrl("/content/home/jcr:content/asset.jpg");
        assertEquals("/content/home/jcr:content/asset.jpg/jcr:content/renditions/cq5dam.thumbnail.319.319.png", asset.getThumbnail());
        assertEquals("/content/home/jcr:content/asset.jpg/jcr:content/renditions/cq5dam.web.1280.1280.jpeg", asset.getThumbnailWebPreview());
	}

	private SearchResult getAssetSearchResultImplUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.currentResource().adaptTo(SearchResult.class);
	}

}
