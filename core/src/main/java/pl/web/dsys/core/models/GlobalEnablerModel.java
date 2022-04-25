package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;

import pl.web.dsys.core.utils.SharedContants;

/**
 * This Sling Model returns Global Enabler component's authored dialog values
 * and exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = GlobalEnablerModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class GlobalEnablerModel {

	private static final Logger log = LoggerFactory.getLogger(GlobalEnablerModel.class);
	public static final String resourceType = "plweb-dsys/components/globalenabler/v1/globalenabler";

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	private ResourceResolver resolver;

	@ValueMapValue
	private String componentType;

	@ScriptVariable
	private Page currentPage;

	String experienceFragmentPath = StringUtils.EMPTY;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of GlobalEnabler Model..::{}", currentPage.getPath());
		if (StringUtils.isNotBlank(componentType)) {
			if (StringUtils.equals(componentType, "header")) {
				experienceFragmentPath = getInheritedVm(currentPage.getContentResource(),
						SharedContants.HEADER_XF_PATH);
			} else if (StringUtils.equals(componentType, "footer")) {
				experienceFragmentPath = getInheritedVm(currentPage.getContentResource(),
						SharedContants.FOOTER_XF_PATH);
			}
		}
		log.debug("Experience Fragment Path ::{}", experienceFragmentPath);
	}

	public String getInheritedVm(Resource resource, String property) {
		InheritanceValueMap ivm = new HierarchyNodeInheritanceValueMap(resource);
		return ivm.getInherited(property, String.class);
	}

	public String getComponentType() {
		return componentType;
	}

	public String getExperienceFragmentPath() {
		if (StringUtils.isNotBlank(experienceFragmentPath)) {
			return experienceFragmentPath + "/" + JcrConstants.JCR_CONTENT;
		}
		return experienceFragmentPath;
	}

}
