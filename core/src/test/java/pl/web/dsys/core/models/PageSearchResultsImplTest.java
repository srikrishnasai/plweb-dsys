package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
class PageSearchResultsImplTest {
	
	private final AemContext context = DsysTestContext.newAemContext();
	public static final String PAGE_SEARCH_RESULT_COMPONENT = "/content";

	@BeforeEach
	public void setUp() {
		context.load().json("/headerModel.json", "/content");
		context.currentPage("/content");
	}

	@Test
	void doTestPageSearchResult() {
		SearchResult page = getPageSearchResultImplUnderTest(PAGE_SEARCH_RESULT_COMPONENT);
		List<String> excerpts = new ArrayList<String>();
		excerpts.add("excerpt");
        assertEquals("page", page.getContentType());
        assertEquals("plweb-dsys", page.getTitle());
        assertEquals("/content", page.getPath());
        assertEquals("/content.html", page.getURL());
        assertEquals(ArrayUtils.EMPTY_STRING_ARRAY, page.getKeywords());
        assertEquals("", page.getIcon());
        assertEquals(new ArrayList<String>(), page.getTagIds());
        page.setExcerpts(excerpts);
        page.setFixedUrl("/content.html");
        assertEquals("excerpt", page.getDescription());
        assertEquals("", page.getThumbnail());
        assertEquals("", page.getThumbnailWebPreview());
	}
	
	@Test
	void doTestPageSearchResultWithoutExcerpts() {
		SearchResult page = getPageSearchResultImplUnderTest(PAGE_SEARCH_RESULT_COMPONENT);
        assertEquals("page", page.getContentType());
        assertEquals("plweb-dsys", page.getTitle());
        assertEquals("/content", page.getPath());
        assertEquals("/content.html", page.getURL());
        assertEquals(ArrayUtils.EMPTY_STRING_ARRAY, page.getKeywords());
        assertEquals("", page.getIcon());
        assertEquals(new ArrayList<String>(), page.getTagIds());
        page.setFixedUrl("/content.html");
        assertEquals("", page.getDescription());
        assertEquals("", page.getThumbnail());
        assertEquals("", page.getThumbnailWebPreview());
	}

	private SearchResult getPageSearchResultImplUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.currentResource().adaptTo(SearchResult.class);
	}

}
