package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

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
import pl.web.dsys.core.search.impl.SearchFacetsImpl;
import pl.web.dsys.core.search.predicates.PredicateGroup;
import pl.web.dsys.core.search.predicates.PredicateResolver;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class SearchFacetsImplTest {

	private final AemContext context = DsysTestContext.newAemContext();

	MockSlingHttpServletRequest request;
	MockSlingHttpServletResponse response;
	MockRequestPathInfo requestPathInfo;

	@Mock
	PredicateResolver predicateResolver;

	@BeforeEach
	public void setUp() {
		context.load().json("/headerModel.json", "/content");
		context.currentPage("/content/home");
		request = context.request();
		response = context.response();
		requestPathInfo = context.requestPathInfo();
		context.registerService(PredicateResolver.class, predicateResolver);
	}

	@Test
	void doTestSearchFacets() {
		SearchFacetsImpl searchFacets = context.request().adaptTo(SearchFacetsImpl.class);
		Mockito.when(predicateResolver.getPredicateGroups(request)).thenReturn(new ArrayList<PredicateGroup>());
		assertNotNull(searchFacets.getPredicateGroups());
	}

}
