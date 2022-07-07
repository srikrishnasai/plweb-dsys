package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class EnhancedListModelTest {
	
	private final AemContext context = DsysTestContext.newAemContext();
	public static final String ENHANCED_LIST_CHILDREN_COMPONENT = "/content/home/jcr:content/enhanced_list_children";
	public static final String ENHANCED_LIST_FIXED_COMPONENT = "/content/home/jcr:content/enhanced_list_fixed";

	MockSlingHttpServletRequest request;
	@BeforeEach
	public void setUp() {
		context.load().json("/enhancedListModel.json", "/content");
		request = context.request();
	}
	
	@Test
	void doTestEnhancedListChildren() {
		request.setAttribute("listType", "pages");
		EnhancedListModel enhanced = getEnhancedListModelUnderTest(ENHANCED_LIST_CHILDREN_COMPONENT);
		assertEquals("Description", enhanced.getDescription());
		assertEquals(5, enhanced.getLimit());
		assertEquals("Title", enhanced.getTitle());
		assertEquals("children", enhanced.getListFrom());
		assertEquals("/content", enhanced.getListRootPath());
		assertNotNull(enhanced.getEnhancedListItems());
		assertEquals("1", enhanced.getLength());
		assertNotNull(enhanced.getEnhancedAssetItems());
		assertNotNull(enhanced.getEnhancedPageItems());
		assertEquals("1", enhanced.getChildDepth());
		assertEquals("pages", enhanced.getListType());
		assertNotNull(enhanced.getResourcesList());
	}
	
	@Test
	void doTestEnhancedListChildrenAssets() {
		request.setAttribute("listType", "assets");
		EnhancedListModel enhanced = getEnhancedListModelUnderTest(ENHANCED_LIST_CHILDREN_COMPONENT);
		assertEquals("Description", enhanced.getDescription());
		assertEquals("Title", enhanced.getTitle());
		assertEquals("children", enhanced.getListFrom());
		assertEquals("/content", enhanced.getListRootPath());
		assertNotNull(enhanced.getEnhancedListItems());
		assertEquals("3", enhanced.getLength());
		assertNotNull(enhanced.getEnhancedAssetItems());
		assertNotNull(enhanced.getEnhancedPageItems());
		assertEquals("1", enhanced.getChildDepth());
		assertEquals("assets", enhanced.getListType());
		assertNotNull(enhanced.getResourcesList());
	}
	
	@Test
	void doTestEnhancedListFixed() {
		request.setAttribute("listType", "pages");
		EnhancedListModel enhanced = getEnhancedListModelUnderTest(ENHANCED_LIST_FIXED_COMPONENT);
		assertEquals("Description", enhanced.getDescription());
		assertEquals("Title", enhanced.getTitle());
		assertEquals("static", enhanced.getListFrom());
		assertEquals("/content", enhanced.getListRootPath());
		assertNotNull(enhanced.getEnhancedListItems());
		assertEquals("1", enhanced.getLength());
		assertNotNull(enhanced.getEnhancedAssetItems());
		assertNotNull(enhanced.getEnhancedPageItems());
		assertEquals("1", enhanced.getChildDepth());
		assertEquals("pages", enhanced.getListType());
		assertNotNull(enhanced.getResourcesList());
		assertNotNull(enhanced.getListItems());
	}
	
	private EnhancedListModel getEnhancedListModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(EnhancedListModel.class);
	}

}
