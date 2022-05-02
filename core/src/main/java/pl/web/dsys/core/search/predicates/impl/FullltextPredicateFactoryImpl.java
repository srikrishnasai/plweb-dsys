package pl.web.dsys.core.search.predicates.impl;

import org.apache.commons.lang.StringUtils;

//OSGI Annotations
import org.osgi.service.component.annotations.Component;

import pl.web.dsys.core.search.predicates.PredicateFactory;
import pl.web.dsys.core.search.predicates.PredicateOption;

import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = {})
public class FullltextPredicateFactoryImpl implements PredicateFactory {
    public static final String REQUEST_PARAM = "q";

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return REQUEST_PARAM;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        String term = request.getParameter(REQUEST_PARAM);
        if (StringUtils.isNotBlank(term)) {
            params.put("fulltext", term);
        }

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
