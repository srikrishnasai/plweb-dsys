package pl.web.dsys.core.helpers;

import javax.inject.Inject;

import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultifieldHelperModel {

	Logger logger = LoggerFactory.getLogger(MultifieldHelperModel.class);
	
	@Inject
	private String nodeName = null;
	
	@Inject
	public Resource resource;
	
	public Resource childResource;
	
	@PostConstruct
	protected void init() {
		
		if (nodeName != null) {
			childResource = resource.getChild(nodeName);
		} 

	}
}
