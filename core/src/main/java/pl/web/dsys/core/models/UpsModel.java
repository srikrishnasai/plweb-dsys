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

/**
 * This Sling Model returns Ups Component's authored dialog values and exports
 * them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UpsModel {

	private static final Logger log = LoggerFactory.getLogger(UpsModel.class);

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String title;

	@ValueMapValue
	private String description;

	@ValueMapValue
	private String alignments;

	@ValueMapValue
	private String numberOfSections;

	@ValueMapValue
	private String sectionOneImgReference;

	@ValueMapValue
	private String sectionTwoImgReference;

	@ValueMapValue
	private String sectionThreeImgReference;

	@ValueMapValue
	private String sectionFourImgReference;

	@ValueMapValue
	private String sectionOneHeader;

	@ValueMapValue
	private String sectionTwoHeader;

	@ValueMapValue
	private String sectionThreeHeader;

	@ValueMapValue
	private String sectionFourHeader;

	@ValueMapValue
	private String sectionOneDescription;

	@ValueMapValue
	private String sectionTwoDescription;

	@ValueMapValue
	private String sectionThreeDescription;

	@ValueMapValue
	private String sectionFourDescription;

	@ValueMapValue
	private String sectionOneSuperScript;

	@ValueMapValue
	private String sectionTwoSuperScript;

	@ValueMapValue
	private String sectionThreeSuperScript;

	@ValueMapValue
	private String sectionFourSuperScript;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Ups Model..");
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getAlignments() {
		return alignments;
	}

	public String getNumberOfSections() {
		return numberOfSections;
	}

	public String getSectionOneImgReference() {
		return sectionOneImgReference;
	}

	public String getSectionTwoImgReference() {
		return sectionTwoImgReference;
	}

	public String getSectionThreeImgReference() {
		return sectionThreeImgReference;
	}

	public String getSectionFourImgReference() {
		return sectionFourImgReference;
	}

	public String getSectionOneHeader() {
		return sectionOneHeader;
	}

	public String getSectionTwoHeader() {
		return sectionTwoHeader;
	}

	public String getSectionThreeHeader() {
		return sectionThreeHeader;
	}

	public String getSectionFourHeader() {
		return sectionFourHeader;
	}

	public String getSectionOneDescription() {
		return sectionOneDescription;
	}

	public String getSectionTwoDescription() {
		return sectionTwoDescription;
	}

	public String getSectionThreeDescription() {
		return sectionThreeDescription;
	}

	public String getSectionFourDescription() {
		return sectionFourDescription;
	}

	public String getSectionOneSuperScript() {
		return sectionOneSuperScript;
	}

	public String getSectionTwoSuperScript() {
		return sectionTwoSuperScript;
	}

	public String getSectionThreeSuperScript() {
		return sectionThreeSuperScript;
	}

	public String getSectionFourSuperScript() {
		return sectionFourSuperScript;
	}

}
