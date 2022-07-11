package pl.web.dsys.core.pojos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class CategoryListTest {

	@InjectMocks
	CategoryList categoryList;

	@Test
	void doTestCategoryList() {
		categoryList = new CategoryList();
		categoryList.setCategory("Category");
		categoryList.setPath("/content/home");
		categoryList.setPageInfolist(new ArrayList<PageInfo>());
		assertEquals("Category", categoryList.getCategory());
		assertEquals("/content/home", categoryList.getPath());
		assertNotNull(categoryList.getPageInfolist());
	}
}
