package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is adapted as child resource for TroikaModel..
 */
@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TroikaSectionItems {

	private static final Logger log = LoggerFactory.getLogger(TroikaSectionItems.class);

	@SlingObject
	private Resource resource;

	@ValueMapValue
	private String sectionImgReference;

	@ValueMapValue
	private String sectionTitle;

	@ValueMapValue
	private String sectionDescription;

	@ValueMapValue
	private String webpageTitle;

	@ValueMapValue
	private String webpageTarget;

	@ValueMapValue
	private String webpageTabTarget;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Troika Model..");
	}

	public String getSectionImgReference() {
		return sectionImgReference;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public String getSectionDescription() {
		return sectionDescription;
	}

	public String getWebpageTitle() {
		return webpageTitle;
	}

	public String getWebpageTarget() {
		return webpageTarget;
	}

	public String getWebpageTabTarget() {
		return webpageTabTarget;
	}

}
