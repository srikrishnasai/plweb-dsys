package pl.web.dsys.core.search;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;


@ProviderType
public interface Suggestions {

	List<String> getSuggestions();

	String getSearchTerm();

	long getTimeTaken();
}
