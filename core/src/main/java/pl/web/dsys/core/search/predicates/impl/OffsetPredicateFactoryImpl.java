package pl.web.dsys.core.search.predicates.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
//OSGI Annotations
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.search.predicates.PredicateFactory;
import pl.web.dsys.core.search.predicates.PredicateOption;

//OSGI Annotation Declaration R7 Format
@Component(service = { PredicateFactory.class }, immediate = true)
public class OffsetPredicateFactoryImpl implements PredicateFactory {
	private static final Logger log = LoggerFactory.getLogger(OffsetPredicateFactoryImpl.class);

	public static final String REQUEST_PARAM = "offset";
	public static final int DEFAULT_OFFSET = 0;

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

		final String offsetStr = request.getParameter(REQUEST_PARAM);

		int offset = DEFAULT_OFFSET;

		if (StringUtils.isNotBlank(offsetStr)) {
			try {
				offset = Integer.parseInt(offsetStr);
				// Code Scan Remediation - Changing Exception catch to specific
			} catch (NumberFormatException e) {
				log.warn("Unable to parse valid integer from Request Param value [ {} ]", offsetStr);

			}
		}

		params.put("p.offset", String.valueOf(offset));

		return params;
	}

	@Override
	public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
		return Collections.emptyList();
	}
}
