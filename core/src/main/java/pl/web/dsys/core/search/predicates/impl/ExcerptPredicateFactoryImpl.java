package pl.web.dsys.core.search.predicates.impl;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;

//OSGI Annotations
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.SlingHttpServletRequest;

import pl.web.dsys.core.search.predicates.PredicateFactory;
import pl.web.dsys.core.search.predicates.PredicateOption;

//java util imports
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service={})
public class ExcerptPredicateFactoryImpl implements PredicateFactory {
    public static final String PN_USE_EXCERPT = "useExcerpt";
    public static final boolean DEFAULT_USE_EXCERPT = false;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return PN_USE_EXCERPT;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final boolean useExcerpt = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_USE_EXCERPT, DEFAULT_USE_EXCERPT);

        params.put("p.excerpt", String.valueOf(useExcerpt));

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
