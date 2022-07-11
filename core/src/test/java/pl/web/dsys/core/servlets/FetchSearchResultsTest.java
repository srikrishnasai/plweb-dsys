package pl.web.dsys.core.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
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
import pl.web.dsys.core.search.predicates.PredicateResolver;
import pl.web.dsys.core.search.providers.SearchProvider;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class FetchSearchResultsTest {

	private final AemContext context = DsysTestContext.newAemContext();
	// private final AemContext context = new
	// AemContext(ResourceResolverType.JCR_MOCK);
	MockSlingHttpServletRequest request;

	MockSlingHttpServletResponse response;

	MockRequestPathInfo requestPathInfo;

	@Mock
	PredicateResolver predicateResolver;

	@Mock
	SearchProvider searchProvider;

	FetchSerchResults fetchResults;

	@BeforeEach
	void setup() {
		context.load().json("/fetchResults.json", "/content");
		Resource resource = context.resourceResolver().getResource("/content/home");
		context.registerService(PredicateResolver.class, predicateResolver);
		context.registerService(SearchProvider.class, searchProvider);
		context.currentResource(resource);
		request = context.request();
		response = context.response();
		fetchResults = context.registerInjectActivateService(new FetchSerchResults());
	}

	@Test
	void doTestGetMethod() {
		try {
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
			Mockito.when(result.getHitsPerPage()).thenReturn(10L);
			Mockito.lenient().when(predicateResolver.getRequestPredicates(request))
					.thenReturn(new HashedMap<String, String>());
			Mockito.lenient().when(searchProvider.search(Mockito.any(), Mockito.any())).thenReturn(result);
			// Mockito.lenient().when(searchProvider.buildSearchResults(Mockito.any(),
			// Mockito.any())).thenReturn(results);
			fetchResults.doGet(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void doTestGetMethodWithEmptyParam() {
		try {
			Map<String, Object> params = new HashedMap<String, Object>();
			params.put("q", "");
			params.put("offset", "0");
			params.put("noOfResPerPage", "10");
			request.setParameterMap(params);
			com.day.cq.search.result.SearchResult result = Mockito.mock(com.day.cq.search.result.SearchResult.class);
			Mockito.lenient().when(predicateResolver.getRequestPredicates(request))
					.thenReturn(new HashedMap<String, String>());
			Mockito.lenient().when(searchProvider.search(Mockito.any(), Mockito.any())).thenReturn(result);
			fetchResults.doGet(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void doTestGetMethodWithMultiWord() {
		try {
			Map<String, Object> params = new HashedMap<String, Object>();
			params.put("q", "home page");
			params.put("offset", "0");
			params.put("noOfResPerPage", "10");
			request.setParameterMap(params);
			com.day.cq.search.result.SearchResult result = Mockito.mock(com.day.cq.search.result.SearchResult.class);
			Mockito.lenient().when(predicateResolver.getRequestPredicates(request))
					.thenReturn(new HashedMap<String, String>());
			Mockito.lenient().when(searchProvider.search(Mockito.any(), Mockito.any())).thenReturn(result);
			Mockito.when(result.getHitsPerPage()).thenReturn(10L);
			fetchResults.doGet(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void doTestUrl() {
		try {
			Method method = FetchSerchResults.class.getDeclaredMethod("getFixedUrl", String.class,
					SlingHttpServletRequest.class);
			method.setAccessible(true);
			method.invoke(fetchResults, "/content/home", request);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Test
	void doTestUrlExternal() {
		try {
			Method method = FetchSerchResults.class.getDeclaredMethod("getFixedUrl", String.class,
					SlingHttpServletRequest.class);
			method.setAccessible(true);
			method.invoke(fetchResults, "https://www.google.com", request);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
