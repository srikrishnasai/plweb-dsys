package pl.web.dsys.core.search.predicates;

import org.apache.sling.api.SlingHttpServletRequest;
import java.util.List;
import java.util.Map;

@org.osgi.annotation.versioning.ConsumerType
public interface PredicatFactory {
    Map<String, String> getRequestPredicate(SlingHttpServletRequest request);
    List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request);
    String getTitle();
    String getName();
    
}
