package pl.web.dsys.core.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;

import pl.web.dsys.core.utils.AuthUtil;
import pl.web.dsys.core.utils.SharedContants;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class AuthControlledExperienceFragment {

	private static final Logger log = LoggerFactory.getLogger(AuthControlledExperienceFragment.class);

	@SlingObject
	private ResourceResolver resolver;

	@SlingObject
	private Resource resource;

	@SlingObject
	private SlingHttpServletRequest request;

	@ValueMapValue
	private String defaultXfPath;

	@ChildResource
	private Resource variations;

	String experienceFragmentPath = StringUtils.EMPTY;
	Map<String, String> variationMap;
	String group = SharedContants.AGENCY_PREFIX + SharedContants.DEFAULT_PUBLIC_AGENCY;

	@PostConstruct
	protected void init() {
		group = AuthUtil.getAgencyTag(request);
		log.debug("Inside Auth Controlled Experience Fragment Model:: and Agency Tag ::{}", group);
		if (null != variations && variations.hasChildren()) {
			Iterator<Resource> itr = variations.listChildren();
			variationMap = new HashMap<String, String>();
			while (itr.hasNext()) {
				Resource itemRes = itr.next();
				ValueMap vm = itemRes.getValueMap();
				String authTag = vm.get("cq:authtags", String.class);
				String fragmentPath = vm.get("variationXfPath", String.class);
				// make multifield dialog fields mandatory..
				if (StringUtils.isNotBlank(authTag) && StringUtils.equals(authTag, group)) {
					// User is matched and mapped variation is returned.
					experienceFragmentPath = fragmentPath;
					break;
				} else {
					// User is not mapped to any variation so showing default variation.
					experienceFragmentPath = defaultXfPath;
				}
			}
		} else {
			// If there are no variations use default one.
			experienceFragmentPath = defaultXfPath;
		}

	}

	public String getDefaultXfPath() {
		return defaultXfPath;
	}

	public Resource getVariations() {
		return variations;
	}

	public String getExperienceFragmentPath() {
		if (StringUtils.isNotBlank(experienceFragmentPath)) {
			return experienceFragmentPath + "/" + JcrConstants.JCR_CONTENT;
		}
		return experienceFragmentPath;
	}

	public String getGroup() {
		return group;
	}

}
