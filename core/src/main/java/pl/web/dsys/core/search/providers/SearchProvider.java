package pl.web.dsys.core.search.providers;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.search.result.SearchResult;

import java.util.List;
import java.util.Map;

@org.osgi.annotation.versioning.ProviderType
public interface SearchProvider {
	SearchResult search(ResourceResolver resourceResolver, Map<String, String> predicates);

	List<pl.web.dsys.core.search.SearchResultsPagination> buildPagination(SearchResult result, String previousLabel, String nextLabel);

    List<pl.web.dsys.core.search.SearchResult> buildSearchResults(SearchResult result, String searchTerm);
}
