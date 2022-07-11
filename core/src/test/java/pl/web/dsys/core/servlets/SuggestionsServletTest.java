package pl.web.dsys.core.servlets;

import static org.mockito.Mockito.lenient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.collections4.map.HashedMap;
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

import com.day.cq.wcm.api.NameConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;
import pl.web.dsys.core.search.providers.SuggestionProvider;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class SuggestionsServletTest {

	private final AemContext context = DsysTestContext.newAemContext();
	MockSlingHttpServletRequest request;

	MockSlingHttpServletResponse response;

	MockRequestPathInfo requestPathInfo;

	@Mock
	SuggestionProvider suggestionProvider;

	SuggestionsServlet servlet;

	@BeforeEach
	void setup() {
		context.load().json("/suggestionServlet.json", "/content");
		Resource resource = context.resourceResolver().getResource("/content/home");
		context.currentResource(resource);
		request = context.request();
		response = context.response();
		requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		context.registerService(SuggestionProvider.class, suggestionProvider);
		servlet = context.registerInjectActivateService(new SuggestionsServlet());
	}

	@Test
	void doTestSuggestionServlet() throws RepositoryException, IOException {
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("q", "ho");
		params.put("searchingPaths", new String[] { "/content" });
		request.setParameterMap(params);
		List<String> suggestions = new ArrayList<String>();
		suggestions.add("Home");
		lenient().when(suggestionProvider.getSearchPaths(Mockito.any(), Mockito.any()))
				.thenReturn(new String[] { "/content" });
		lenient().when(suggestionProvider.suggest(context.resourceResolver(), new String[] { "/content" },
				NameConstants.NT_PAGE, "Ho", 0)).thenReturn(suggestions);
		servlet.doGet(request, response);
	}

}
