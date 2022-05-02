package pl.web.dsys.core.search;


import java.util.List;


import org.osgi.annotation.versioning.ProviderType;

import pl.web.dsys.core.search.predicates.PredicateGroup;

@ProviderType
@FunctionalInterface
public interface SearchFacets {
    List<PredicateGroup> getPredicateGroups();
}
