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


@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = TroikaUpsModel.resourceType)
public class TroikaUpsModel {

	private static final Logger log = LoggerFactory.getLogger(TroikaUpsModel.class);
	public static final String resourceType = "plweb-dsys/components/troika-ups/v1/troika-ups";
	
	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String cardTitle;

	@ValueMapValue
	private String cardDescription;

	@ValueMapValue
	private String cardWebpageTitle;

	@ValueMapValue
	private String cardWebpageTarget;

	@ValueMapValue
	private String cardWebpageTabTarget;

	@ValueMapValue
	private String alignments;

	@ValueMapValue
	private String disableImage;

	@ValueMapValue
	private String cardImage;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Troika/Ups Model..");
	}

	public String getCardTitle() {
		return cardTitle;
	}

	public String getCardDescription() {
		return cardDescription;
	}

	public String getCardWebpageTitle() {
		return cardWebpageTitle;
	}

	public String getCardWebpageTarget() {
		return cardWebpageTarget;
	}

	public String getCardWebpageTabTarget() {
		return cardWebpageTabTarget;
	}

	public String getAlignments() {
		return alignments;
	}

	public String getDisableImage() {
		return disableImage;
	}

	public String getCardImage() {
		return cardImage;
	}

}
