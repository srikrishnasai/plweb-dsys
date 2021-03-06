package pl.web.dsys.core.search.predicates.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
//OSGI Annotations
import org.osgi.service.component.annotations.Component;

import pl.web.dsys.core.search.predicates.PredicateFactory;
import pl.web.dsys.core.search.predicates.PredicateOption;

//OSGI Annotation Declaration R7 Format
@Component(service = { PredicateFactory.class }, immediate = true)
public class VersionPredicateFactoryImpl extends AbstractTagPredicateFactory implements PredicateFactory {

	public static final String TAG_NAMESPACE = "version";
	public static final String REQUEST_PARAM = "version";
	public static final int GROUP_ID = 1000;
	public static final String PROPERTY_PATH = "jcr:content/cq:tags";

	@Override
	public String getTitle() {
		return "Versions";
	}

	@Override
	public String getName() {
		return REQUEST_PARAM;
	}

	@Override
	public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
		final Map<String, String> params = super.getRequestPredicate(GROUP_ID,
				request.getParameterValues(REQUEST_PARAM), PROPERTY_PATH);
		// Overrides params as needed

		return params;
	}

	@Override
	public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
		final List<PredicateOption> options = super.getPredicateOptions(request, TAG_NAMESPACE);

		for (final PredicateOption option : options) {
			option.setActive(ArrayUtils.contains(request.getParameterValues(REQUEST_PARAM), option.getValue()));
		}

		return options;
	}
}
