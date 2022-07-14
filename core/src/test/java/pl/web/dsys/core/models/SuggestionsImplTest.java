package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

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

import com.day.cq.wcm.api.NameConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;
import pl.web.dsys.core.search.impl.SuggestionsImpl;
import pl.web.dsys.core.search.providers.SuggestionProvider;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class SuggestionsImplTest {

	private final AemContext context = DsysTestContext.newAemContext();
	MockSlingHttpServletRequest request;
	MockSlingHttpServletResponse response;
	MockResourceResolver resolver;

	@Mock
	SuggestionProvider provider;

	@BeforeEach
	public void setUp() {
		context.load().json("/headerModel.json", "/content");
		context.currentPage("/content/home");
		request = context.request();
		response = context.response();
		context.registerService(SuggestionProvider.class, provider);
	}

	@Test
	void doTestSuggestionsImpl() throws RepositoryException {
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("q", "ho");
		params.put("searchingPaths", new String[] { "/content" });
		request.setParameterMap(params);
		List<String> suggestions = new ArrayList<String>();
		suggestions.add("Home");
		lenient().when(provider.getSearchPaths(Mockito.any(), Mockito.any())).thenReturn(new String[] { "/content" });
		lenient().when(provider.suggest(context.resourceResolver(), new String[] { "/content" }, NameConstants.NT_PAGE,
				"Ho", 0)).thenReturn(suggestions);

		SuggestionsImpl suggest = context.request().adaptTo(SuggestionsImpl.class);
		assertNotNull(suggest.getSuggestions());
		assertNotNull(suggest.getTimeTaken());
	}

}
