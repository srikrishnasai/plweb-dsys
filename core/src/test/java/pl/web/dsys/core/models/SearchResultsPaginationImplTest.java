package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.search.impl.SearchResultsPaginationImpl;

@ExtendWith(AemContextExtension.class)
class SearchResultsPaginationImplTest {
	
	@InjectMocks
	SearchResultsPaginationImpl searchResults;
	
	@Test
	void doTestSearchPagination() {
		searchResults = new SearchResultsPaginationImpl(0, "label", true, false);
		assertEquals(0, searchResults.getOffset());
		assertEquals("label", searchResults.getLabel());
		assertEquals(true, searchResults.isActive());
		assertEquals(false, searchResults.isDisabled());
	}

}
