package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Sling Model returns Troika Component's authored dialog values and exports
 * them as json.
 */
 
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = TroikaModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class TroikaModel {

	private static final Logger log = LoggerFactory.getLogger(TroikaModel.class);
	public static final String resourceType = "plweb-dsys/components/troika/v1/troika";
	
	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String generalTitle;

	@ValueMapValue
	private String generalDescription;

	@ValueMapValue
	private String disableImage;

	@ValueMapValue
	private String alignments;

	@ValueMapValue
	private String numberOfSections;

	@ChildResource
	private List<TroikaSectionItems> sections;

	int noOfSecs;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Troika Model..");
		if (StringUtils.isNotBlank(numberOfSections)) {
			noOfSecs = Integer.parseInt(numberOfSections);
			log.debug("No of sections ::{}", noOfSecs);
			sections = sections.subList(0, noOfSecs);
		} else {
			sections = new ArrayList<TroikaSectionItems>();
		}
	}

	public String getGeneralTitle() {
		return generalTitle;
	}

	public String getGeneralDescription() {
		return generalDescription;
	}

	public String getDisableImage() {
		return disableImage;
	}

	public String getAlignments() {
		return alignments;
	}

	public String getNumberOfSections() {
		return numberOfSections;
	}

	public List<TroikaSectionItems> getSections() {
		return sections;
	}

}
