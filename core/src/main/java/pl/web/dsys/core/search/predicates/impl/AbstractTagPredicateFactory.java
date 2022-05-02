package pl.web.dsys.core.search.predicates.impl;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.search.predicates.PredicateOption;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public abstract class AbstractTagPredicateFactory {
    private static final Logger log = LoggerFactory.getLogger(AbstractTagPredicateFactory.class);

    // Ensure every group is unique
    private final String group(int groupId, String key) {
        return groupId + "_group." + key;
    }

    public Map<String, String> getRequestPredicate(int groupId, String[] values, String propertyPath) {
        if (ArrayUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }

        final Map<String, String> params = new HashMap<String, String>();

        // And the groups
        params.put(group(groupId, "p.and"), "true");

        params.put(group(groupId, "tagid.property"), propertyPath);
        params.put(group(groupId, "tagid.and"), "false");

        int i = 0;
        for (final String value : values) {
            params.put(group(groupId, "tagid." + i++ + "_value"), value);
        }

        if (i == 0) {
            return Collections.emptyMap();
        } else {
            return params;
        }
    }

    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request, String namespaceName) {
        final List<PredicateOption> options = new ArrayList<PredicateOption>();

        final TagManager tagManager = request.getResourceResolver().adaptTo(TagManager.class);
		
		// Code Scan Remediation
		if (tagManager == null) {
            log.error("tagManager is NULL");
            return options;
        }
		
        final Tag namespace = tagManager.resolve("/etc/tags/" + namespaceName);
		
        if (namespace == null) {
            log.error("Unable to resolve [ {} ] to a Tag namespace", namespaceName);
            return options;
        }

        final Iterator<Tag> tags = namespace.listChildren();

        while (tags.hasNext()) {
            Tag tag = tags.next();
            options.add(new PredicateOption(StringUtils.defaultIfEmpty(tag.getTitle(request.getLocale()), tag.getTitle()),
                    tag.getTagID()));
        }

        // Sort Alphabetically by Label
        Collections.sort(options, new PredicateOption.AlphabeticalByLabel());

        return options;
    }
}
