package pl.web.dsys.core.search;


import java.util.List;

@org.osgi.annotation.versioning.ProviderType
public interface SearchResults {
    List<SearchResult> getResults();

    List<SearchResultsPagination> getPagination();

    String getSearchTerm();

    long getTimeTaken();
    
    String getResultTotal();
    
    com.day.cq.search.result.SearchResult getResult();
}
