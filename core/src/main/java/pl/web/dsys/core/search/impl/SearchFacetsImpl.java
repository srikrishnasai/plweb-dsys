package pl.web.dsys.core.search.impl;


import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import pl.web.dsys.core.search.SearchFacets;
import pl.web.dsys.core.search.predicates.PredicateGroup;
import pl.web.dsys.core.search.predicates.PredicateResolver;


@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchFacets.class
)
public class SearchFacetsImpl implements SearchFacets {
    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private PredicateResolver predicateResolver;

    public List<PredicateGroup> getPredicateGroups() {
        return predicateResolver.getPredicateGroups(request);
    }
}
