package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CardModel.resourceType)
public class CardModel {

	private static final Logger log = LoggerFactory.getLogger(CardModel.class);
	public static final String resourceType = "plweb-dsys/components/card/v1/card";

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
	private String useTransparentImage;

	@ValueMapValue
	private String useOriginalImage;

	Resource child;
	ValueMap valueMap;
	private String fileReference;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Card Model..");
		if (null != resource) {
			child = resource.getChild("image");
			if (null != child) {
				valueMap = child.getValueMap();
				fileReference = valueMap.get("fileReference", String.class);
			}
		}
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

	public String getFileReference() {
		return fileReference;
	}

	public String getUseTransparentImage() {
		return useTransparentImage;
	}

	public String getUseOriginalImage() {
		return useOriginalImage;
	}

}
