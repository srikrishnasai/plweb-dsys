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
 * This Sling Model returns Ups Component's authored dialog values and exports
 * them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = UpsModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class UpsModel {

	private static final Logger log = LoggerFactory.getLogger(UpsModel.class);
	public static final String resourceType = "plweb-dsys/components/ups/v1/ups";
	
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

	@ChildResource
	private List<UpsSectionItems> sections;

	int noOfSecs;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Ups Model..");
		if (StringUtils.isNotBlank(numberOfSections)) {
			noOfSecs = Integer.parseInt(numberOfSections);
			log.debug("No of sections ::{}", noOfSecs);
			sections = sections.subList(0, noOfSecs);
		} else {
			sections = new ArrayList<UpsSectionItems>();
		}
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

	public List<UpsSectionItems> getSections() {
		return sections;
	}

}
