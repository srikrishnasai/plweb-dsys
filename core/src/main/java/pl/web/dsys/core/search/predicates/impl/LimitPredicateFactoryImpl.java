package pl.web.dsys.core.search.predicates.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.day.cq.commons.inherit.ComponentInheritanceValueMap;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
//OSGI Annotations
import org.osgi.service.component.annotations.Component;

import pl.web.dsys.core.search.predicates.PredicateFactory;
import pl.web.dsys.core.search.predicates.PredicateOption;

//OSGI Annotation Declaration R7 Format
@Component(service = {})
public class LimitPredicateFactoryImpl implements PredicateFactory {

    public static final String PN_LIMIT = "limit";
    public static final int DEFAULT_LIMIT = 10;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return PN_LIMIT;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        int limit = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_LIMIT, DEFAULT_LIMIT);

        String numberOfResPerPage = request.getParameter("noOfResPerPage");
        if (null != numberOfResPerPage && StringUtils.isNotBlank(numberOfResPerPage)) {
            int numberOfResults = Integer.parseInt(numberOfResPerPage);
            if (numberOfResults > 0) {
                limit = numberOfResults;
            }
        }

        params.put("p.limit", String.valueOf(limit));

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
