package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

//other imports
import org.apache.commons.lang.math.NumberUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
//apache sling model api imports
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
//logger imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//A sling model for spacer component
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = Spacer.resourceType)
public class Spacer {
	private static final Logger log = LoggerFactory.getLogger(Spacer.class);
	public static final String resourceType = "plweb-dsys/components/spacer/v1/spacer";
	public static final String DEFAUT_HEIGHT = "10";
	public static final String DEFAUT_FONTSIZE = "px";

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String height;

	@ValueMapValue
	private String fontsize;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Spacer Model..");
		if (!NumberUtils.isNumber(height)) {
			height = DEFAUT_HEIGHT;
		}
		if (null == fontsize || fontsize.isEmpty()) {
			fontsize = DEFAUT_FONTSIZE;

		}
	}

	public String getHeight() {
		return height + fontsize;
	}

}
