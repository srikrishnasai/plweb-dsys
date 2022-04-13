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
 * This class is adapted as child resource for UpsModel..
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UpsSectionItems {

	private static final Logger log = LoggerFactory.getLogger(UpsSectionItems.class);

	@SlingObject
	private Resource resource;

	@ValueMapValue
	private String sectionImgReference;

	@ValueMapValue
	private String sectionHeader;

	@ValueMapValue
	private String sectionDescription;

	@ValueMapValue
	private String sectionSuperScript;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Ups Model..");
	}

	public String getSectionImgReference() {
		return sectionImgReference;
	}

	public String getSectionHeader() {
		return sectionHeader;
	}

	public String getSectionDescription() {
		return sectionDescription;
	}

	public String getSectionSuperScript() {
		return sectionSuperScript;
	}

}
