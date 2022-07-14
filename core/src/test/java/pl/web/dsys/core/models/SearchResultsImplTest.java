package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.apache.sling.testing.resourceresolver.MockResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;
import pl.web.dsys.core.search.SearchResult;
import pl.web.dsys.core.search.impl.AssetSearchResultImpl;
import pl.web.dsys.core.search.impl.PageSearchResultImpl;
import pl.web.dsys.core.search.impl.SearchResultsImpl;
import pl.web.dsys.core.search.predicates.PredicateResolver;
import pl.web.dsys.core.search.providers.SearchProvider;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class SearchResultsImplTest {

	private final AemContext context = DsysTestContext.newAemContext();
	MockSlingHttpServletRequest request;
	MockSlingHttpServletResponse response;
	MockResourceResolver resolver;

	@Mock
	SearchProvider provider;

	@Mock
	PredicateResolver predicateResolver;

	@BeforeEach
	public void setUp() {
		context.load().json("/fetchResults.json", "/content");
		context.currentPage("/content/home");
		request = context.request();
		response = context.response();
		context.registerService(SearchProvider.class, provider);
		context.registerService(PredicateResolver.class, predicateResolver);
	}

	@Test
	void doTestGetMethod() {
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("q", "ho");
		params.put("offset", "0");
		params.put("noOfResPerPage", "10");
		request.setParameterMap(params);
		List<SearchResult> results = new ArrayList<SearchResult>();
		SearchResult result1 = new PageSearchResultImpl();
		SearchResult result2 = new AssetSearchResultImpl();
		results.add(result1);
		results.add(result2);
		com.day.cq.search.result.SearchResult result = Mockito.mock(com.day.cq.search.result.SearchResult.class);
		Mockito.when(result.getTotalMatches()).thenReturn(5L);
		Mockito.when(result.hasMore()).thenReturn(false);
		Mockito.lenient().when(result.getHitsPerPage()).thenReturn(10L);
		Mockito.lenient().when(predicateResolver.getRequestPredicates(request))
				.thenReturn(new HashedMap<String, String>());
		Mockito.lenient().when(provider.search(Mockito.any(), Mockito.any())).thenReturn(result);
		// Mockito.lenient().when(searchProvider.buildSearchResults(Mockito.any(),
		// Mockito.any())).thenReturn(results);
		SearchResultsImpl searchResults = request.adaptTo(SearchResultsImpl.class);
		assertNotNull(searchResults.getTimeTaken());
		assertNotNull(searchResults.getResult());
		assertNotNull(searchResults.getResultTotal());
		assertNotNull(searchResults.getPagination());
		assertNotNull(searchResults.getResults());
	}

	@Test
	void doTestGetMethodWithEmptyParam() {
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("q", "");
		params.put("offset", "0");
		params.put("noOfResPerPage", "10");
		request.setParameterMap(params);
		com.day.cq.search.result.SearchResult result = Mockito.mock(com.day.cq.search.result.SearchResult.class);
		Mockito.lenient().when(predicateResolver.getRequestPredicates(request))
				.thenReturn(new HashedMap<String, String>());
		Mockito.lenient().when(provider.search(Mockito.any(), Mockito.any())).thenReturn(result);
		SearchResultsImpl searchResults = request.adaptTo(SearchResultsImpl.class);
		assertNotNull(searchResults.getTimeTaken());
		assertNull(searchResults.getResult());
		assertNull(searchResults.getResultTotal());
		assertNull(searchResults.getPagination());
		assertNull(searchResults.getResults());
	}

	@Test
	void doTestGetMethodWithMultiWord() {
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("q", "home page");
		params.put("offset", "0");
		params.put("noOfResPerPage", "10");
		request.setParameterMap(params);
		com.day.cq.search.result.SearchResult result = Mockito.mock(com.day.cq.search.result.SearchResult.class);
		Mockito.lenient().when(predicateResolver.getRequestPredicates(request))
				.thenReturn(new HashedMap<String, String>());
		Mockito.lenient().when(provider.search(Mockito.any(), Mockito.any())).thenReturn(result);
		Mockito.lenient().when(result.getHitsPerPage()).thenReturn(10L);
		SearchResultsImpl searchResults = request.adaptTo(SearchResultsImpl.class);
		assertNotNull(searchResults.getTimeTaken());
		assertNotNull(searchResults.getResult());
		assertNotNull(searchResults.getResultTotal());
		assertNotNull(searchResults.getPagination());
		assertNotNull(searchResults.getResults());
	}

}
