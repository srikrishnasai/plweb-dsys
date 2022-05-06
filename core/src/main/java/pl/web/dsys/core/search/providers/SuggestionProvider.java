package pl.web.dsys.core.search.providers;

import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.ResourceResolver;


@org.osgi.annotation.versioning.ProviderType
public interface SuggestionProvider {
	List<String> suggest(ResourceResolver resourceResolver, String[] paths, String nodeType, String term, int limit)
			throws RepositoryException;

	String[] getSearchPaths(ResourceResolver resourceResolver, String searchRootPagePath);

}
