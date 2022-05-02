package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ButtonGroupModel.resourceType)
@Exporter(name = "jackson", extensions = "json")		
public class ButtonGroupModel {

	private static final Logger log = LoggerFactory.getLogger(ButtonGroupModel.class);
	public static final String resourceType = "plweb-dsys/components/button-group/v1/button-group";
	
	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String numberOfButtons;

	
	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Button-Group Model..");
	}

	public String getNumberOfButtons() {
		return numberOfButtons;
	}
}
