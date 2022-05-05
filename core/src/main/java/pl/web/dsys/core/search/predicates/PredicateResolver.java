package pl.web.dsys.core.search.predicates;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.annotation.versioning.ProviderType;

import java.util.List;
import java.util.Map;

@ProviderType
public interface PredicateResolver {
    Map<String, String> getRequestPredicates(SlingHttpServletRequest request);

    List<PredicateGroup> getPredicateGroups(SlingHttpServletRequest request);
}
